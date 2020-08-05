package com.blaze.logic.service;

import com.alibaba.fastjson.JSONObject;
import com.blaze.common.PostResult;
import com.blaze.common.utils.RedisUtil;
import com.blaze.logic.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 游戏用户节点数据
 *
 * @author Sky
 */
@Service
public class UserGameService {
    @Autowired
    protected RedisUtil redisUtil;

    protected static long TIME_SUB_MAX = 1000 * 60 * 30;

    public PostResult asyncUserData(JSONObject asyncInfo) {
        PostResult result = new PostResult();
        String md5 = asyncInfo.getString(Constant.MD5);
        long md5Key = asyncInfo.getLong(Constant.MD5_KEY);
        long times = asyncInfo.getLong(Constant.TIMES);

        //进行校验参数是否正确
        if (Math.abs(System.currentTimeMillis() - times) / TIME_SUB_MAX > 0) {
            result.setCode(401);
            result.setMsg("时间校验失败，请调准时间!");
            return result;
        }
        if (!existMd5Success(asyncInfo, md5, md5Key)) {
            result.setCode(402);
            result.setMsg("数据校验失败!");
            return result;
        }
        asyncData(asyncInfo);
        return result;
    }

    /**
     * 数据同步到缓存
     *
     * @param asyncInfo 用户同步节点
     */
    public void asyncData(JSONObject asyncInfo) {
        String type = asyncInfo.getString(Constant.TYPE);
        String redisKey = Constant.REDIS_USER_HASH_KEY + asyncInfo.getString(Constant.UID);
        String json = (String) redisUtil.hashGet(redisKey, Constant.GAME_CODE_DATA);
        JSONObject data = new JSONObject();
        if (json != null) {
            data = JSONObject.parseObject(json);
        }
        JSONObject sync = data.getJSONObject(type);
        if (sync == null) {
            sync = new JSONObject();
        }
        JSONObject finalSync = sync;
        asyncInfo.forEach((key, value) -> {
            if (Constant.compareBaseKey(key)) {
                return;
            }
            if (Constant.compareGameBaseKey(key)) {
                return;
            }
            if (key.equals(Constant.TYPE)) {
                return;
            }
            finalSync.put(key, value);
        });
        System.out.println(asyncInfo);
        data.put(type, finalSync);
        redisUtil.hashPut(redisKey, Constant.GAME_CODE_DATA, data.toJSONString());
    }

    /**
     * 校验MD5是否正确
     *
     * @param data   上报数据
     * @param md5    MD5
     * @param md5Key MD5 校验值
     * @return 是否匹配
     */
    public boolean existMd5Success(JSONObject data, String md5, long md5Key) {
        AtomicInteger index = new AtomicInteger();
        StringBuilder md5Str = new StringBuilder();
        SortedMap<String, Object> sort = new TreeMap<>();
        data.forEach((key, value) -> {
            if (key.equals(Constant.MD5)) {
                return;
            }
            sort.put(key, value);
        });
        sort.forEach((key, value) -> {
                    int i = index.getAndIncrement();
                    boolean flag = (md5Key >> i & 0x01) == 1;
                    if (flag) {
                        md5Str.append(key);
                    } else {
                        if (value instanceof String) {
                            md5Str.append(value);
                        } else {
                            md5Str.append(JSONObject.toJSONString(value));
                        }
                    }
                }
        );
        System.out.println("加签参数：" + md5Str.toString());
        String md5Password = DigestUtils.md5DigestAsHex(md5Str.toString().getBytes());
        return md5.equalsIgnoreCase(md5Password);
    }
}
