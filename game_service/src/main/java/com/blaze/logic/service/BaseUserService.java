package com.blaze.logic.service;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blaze.common.PostResult;
import com.blaze.common.utils.RedisUtil;
import com.blaze.common.utils.SimpleUtil;
import com.blaze.data.entity.UserInfo;
import com.blaze.data.service.UserInfoService;
import com.blaze.data.service.WxConfigService;
import com.blaze.logic.BusinessConfig;
import com.blaze.logic.Constant;
import com.blaze.logic.RequestConst;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户信息处理逻辑Service
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-06-09 15:44
 */
@Service
public abstract class BaseUserService implements Constant {

    protected static final Logger LOGGER = LoggerFactory.getLogger(BaseUserService.class);

    protected RedisUtil redisUtil;
    protected UserInfoService userInfoService;
    protected WxConfigService wxConfigService;
    protected BusinessConfig businessConfig;

    /**
     * 用户登录
     *
     * @param loginInfo 登录信息
     * @return 登录结果
     */
    public PostResult login(JSONObject loginInfo) {
        LOGGER.info("loginInfo -> " + loginInfo.toJSONString());
        PostResult postResult = new PostResult();
        // 判断请求登录时是否有uid
        boolean isExist = true;
        String uid = loginInfo.getString(RequestConst.UID);
        if (StringUtils.isBlank(uid)) {
            isExist = false;
            uid = getUid(loginInfo);
        }
        // 如果仍未获取到uid，返回登录失败
        if (StringUtils.isBlank(uid)) {
            postResult.updateMsg(0, "获取uid失败！");
            return postResult;
        }

        // redis-key
        String key = REDIS_USER_HASH_KEY + uid;

        // 登录用户信息
        JSONObject user = new JSONObject();
        user.put("uid", uid);
        if (!this.redisUtil.hasKey(key)) {
            createNewUser(loginInfo, user, uid, key);
        } else {
            Map<Object, Object> hash = this.redisUtil.hashEntries(key);
            boolean isTourist = !hash.containsKey("openId");
            user.put("isTourist", isTourist);
            user.put("isNewUser", false);
            hash.forEach((_key, _value) -> {
                if (GAME_CODE_DATA.equals(_key.toString())) {
                    user.put(_key.toString(), JSONObject.parseObject(_value.toString()));
                } else {
                    user.put(_key.toString(), _value);
                }
            });
            if (!isExist && isTourist) {
                // 从数据库获取信息
                UserInfo userInfo = this.userInfoService.getById(uid);
                if (userInfo != null) {
                    user.put("nickName", userInfo.getNickName());
                    user.put("avatarUrl", userInfo.getAvatarUrl());
                    user.put("gender", userInfo.getGender());
                } else {
                    createNewUser(loginInfo, user, uid, key);
                }
            }
        }

        JSONObject dataObject = new JSONObject();
        dataObject.put("user", user);
        LOGGER.info("loginReturn -> " + dataObject.toJSONString());
        if ("tt".equals(loginInfo.getString(RequestConst.PLATFORM)) && SimpleUtil.convertVersion("3.0.2") >= SimpleUtil.convertVersion(loginInfo.getString(RequestConst.VERSION))) {
            postResult.setData(dataObject.toJSONString());
        } else {
            postResult.setData(dataObject);
        }
        return postResult;
    }

    /**
     * 创建新用户并存储数据
     *
     * @param loginInfo 用户登录信息
     * @param user      用户新
     * @param uid       uid
     * @param key       redis-key
     */
    private void createNewUser(JSONObject loginInfo, JSONObject user, String uid, String key) {
        String nickName = "游客" + RandomUtil.randomString(6);
        user.put("nickName", nickName);
        user.put("isTourist", true);
        user.put("isNewUser", true);
        // 存储数据
        Map<String, String> hashMap = new HashMap<>(0);
        hashMap.put("loginTime", String.valueOf(System.currentTimeMillis()));
        if (this.redisUtil.hashPut(key, hashMap)) {
            UserInfo userInfo = new UserInfo();
            userInfo.setUid(uid);
            userInfo.setAppId(loginInfo.getString(RequestConst.APP_ID));
            userInfo.setPlatform(loginInfo.getString(RequestConst.PLATFORM));
            userInfo.setNickName(nickName);
            this.userInfoService.save(userInfo);
        }
    }


    /**
     * 获取uid
     *
     * @param loginInfo 登录信息
     * @return uid
     */
    protected abstract String getUid(JSONObject loginInfo);

    /**
     * 同步用户信息
     *
     * @param asyncInfo 同步信息
     * @return 绑定结果
     */
    public PostResult asyncUserInfo(JSONObject asyncInfo) {
        LOGGER.info("asyncUserInfo -> " + asyncInfo.toJSONString());

        PostResult postResult = new PostResult();
        String uid = asyncInfo.getString(RequestConst.UID);
        if (StringUtils.isBlank(uid) || StringUtils.equals(uid, "0")) {
            postResult.updateMsg(0, "uid异常");
            return postResult;
        }

        //
        if (StringUtils.isBlank(asyncInfo.getString("nickName"))) {
            postResult.updateMsg(0, "昵称为空，授权无效");
            return postResult;
        }

        String openId = this.getOpenId(asyncInfo);
        if (StringUtils.isNotBlank(openId)) {
            // 将openId存入redis和数据库
            String key = REDIS_USER_HASH_KEY + uid;
            if (this.redisUtil.hashPut(key, "openId", openId)) {
                UserInfo userInfo = JSONObject.parseObject(asyncInfo.toJSONString(), UserInfo.class);
                userInfo.setOpenId(openId);
                this.userInfoService.updateById(userInfo);
            }
        }

        // 更新排行榜用户节点信息
        updateRankUser(asyncInfo);

        // 更新邀请信息
        updateInvitedInfo(asyncInfo);

        LOGGER.info("asyncUserReturn -> " + JSON.toJSONString(postResult));
        return postResult;
    }

    /**
     * 更新排行榜用户节点信息
     *
     * @param asyncInfo 同步信息
     */
    private void updateRankUser(JSONObject asyncInfo) {
        JSONObject rankUserObject;
        String rankUserInfo = (String) this.redisUtil.hashGet(Constant.REDIS_RANKING_USER_COLLECT_KEY, asyncInfo.getString(RequestConst.UID));
        if (rankUserInfo == null) {
            rankUserObject = new JSONObject();
        } else {
            rankUserObject = JSON.parseObject(rankUserInfo);
        }

        rankUserObject.put("uid", asyncInfo.getString(RequestConst.UID));
        rankUserObject.put("name", asyncInfo.getString("nickName"));
        rankUserObject.put("avatarFrame", asyncInfo.getString("avatarFrame"));
        rankUserObject.put("avatarUrl", asyncInfo.getString("avatarUrl"));
        rankUserObject.put("sex", asyncInfo.getIntValue("gender"));
        this.redisUtil.hashPut(Constant.REDIS_RANKING_USER_COLLECT_KEY, asyncInfo.getString(RequestConst.UID), rankUserObject.toJSONString());
    }

    /**
     * 更新分享信息
     *
     * @param asyncInfo 同步信息
     */
    private void updateInvitedInfo(JSONObject asyncInfo) {
        if (asyncInfo.containsKey("inviteId")) {
            String inviteKey = REDIS_USER_HASH_KEY + asyncInfo.getString("inviteId");
            Object inviteObject = this.redisUtil.hashGet(inviteKey, "inviteList");
            JSONArray inviteList;
            if (inviteObject != null) {
                inviteList = (JSONArray) inviteObject;
            } else {
                inviteList = new JSONArray();
            }
            JSONObject invite = new JSONObject();
            invite.put("uid", asyncInfo.getString(RequestConst.UID));
            invite.put("headUrl", asyncInfo.getString("avatarUrl"));
            invite.put("name", asyncInfo.getString("nickName"));
            invite.put("sex", asyncInfo.getIntValue("gender"));
            invite.put("times", System.currentTimeMillis());
            invite.put("receive", false);
            inviteList.add(invite);
            this.redisUtil.hashPut(inviteKey, "inviteList", inviteList.toJSONString());
        }
    }

    /**
     * 获取openId
     *
     * @param bindInfo 绑定信息
     * @return openId
     */
    protected abstract String getOpenId(JSONObject bindInfo);

    @Autowired
    public void setUserInfoService(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @Autowired
    public void setRedisUtil(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    @Autowired
    public void setWxConfigService(WxConfigService wxConfigService) {
        this.wxConfigService = wxConfigService;
    }

    @Autowired
    public void setBusinessConfig(BusinessConfig businessConfig) {
        this.businessConfig = businessConfig;
    }

}
