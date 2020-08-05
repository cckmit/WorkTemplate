package com.blaze.logic.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blaze.common.utils.RedisUtil;
import com.blaze.logic.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * 排行榜业务逻辑
 *
 * @author Sky
 */
@Service
public class MyRankService implements Constant {

    @Autowired
    protected RedisUtil redisUtil;

    /**
     * 添加一场比赛记录
     *
     * @param record 记录信息
     */
    public JSONObject addOnceRecord(JSONObject record) {
        //是否追加
        boolean isUpdate = record.getBoolean(Constant.UPDATE);
        //赛场信息
        String rankingKey = record.getString(Constant.MATCH_KEY);
        //用户信息
        String uid = record.getString(Constant.UID);
        //当前分数
        double score = record.getDouble(Constant.SCORE);
        if (isUpdate) {
            redisUtil.zincrby(rankingKey, uid, score);
        } else {
            //强者更新
            Double _score = redisUtil.zScore(rankingKey, uid);
            if (_score == null || score > _score) {
                redisUtil.zAdd(rankingKey, uid, score);
            }
        }
        return getUserRank(rankingKey, uid);
    }

    /**
     * 获取用户排行榜记录
     *
     * @param uid 用户ID
     * @return 排行榜信息
     */
    public JSONObject getUserRank(String key, String uid) {
        JSONObject user = new JSONObject();
        user.put(Constant.UID, uid);
        Long rank = redisUtil.zRank(key, uid);
        user.put(Constant.RANK, rank == null ? -1 : rank + 1);
        Double score = redisUtil.zScore(key, uid);
        user.put(Constant.SCORE, score == null ? 0 : score.longValue());
        //排行榜总人数
        Long total = redisUtil.zCard(key);
        user.put(Constant.TOTAL, total == null ? 0 : total.longValue());
        return user;
    }

    /**
     * 获取一场赛场记录
     *
     * @param search 查询条件
     * @return 赛场信息
     */
    public JSONObject getOnceMatchRecord(JSONObject search) {
        String key = search.getString(Constant.MATCH_KEY);
        String uid = search.getString(Constant.UID);
        long start = search.getLong(Constant.START);
        long end = search.getLong(Constant.END);
        JSONObject result = new JSONObject();
        Set<ZSetOperations.TypedTuple<String>> items = redisUtil.zRankWithScore(key, start, end);
        JSONArray array = new JSONArray();
        if (items == null) {
            return result;
        }
        items.forEach(action -> {
            Double score = action.getScore();
            String member = action.getValue();
            JSONObject data = new JSONObject();
            data.put(member, score);
            array.add(data);
        });
        result.put(Constant.USERS, array);
        //自己节点数据
        if (search.containsKey(Constant.UPDATE)) {
            result.put(Constant.SELF, getUserRank(key, uid));
        }
        return result;
    }

    /**
     * 通过用户UID获取用户相关信息
     *
     * @param users 用户节点
     * @return 用户信息节点
     */
    public JSONArray getUserInfo(JSONArray users) {
        String key = Constant.REDIS_RANKING_USER_COLLECT_KEY;
        JSONArray array = new JSONArray();
        if (users == null || users.isEmpty()) {
            return array;
        }
        if (users.size() == 1) {
            String member = (String) redisUtil.hashGet(key, users.getString(0));
            if (member != null) {
                array.add(JSONObject.parseObject(member));
            }
            return array;
        }
        List<Object> items = redisUtil.hashMGet(key, users.toJavaList(String.class));
        if (items == null) {
            return array;
        }
        items.forEach(element -> {
            String str = (String) element;
            if (str == null) {
                return;
            }
            array.add(JSONObject.parseObject(str));
        });

        return array;
    }
}
