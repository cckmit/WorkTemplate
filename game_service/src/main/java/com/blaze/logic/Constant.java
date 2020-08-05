package com.blaze.logic;

/**
 * 常量
 *
 * @author Sky
 */
public interface Constant {
    /**
     * 用户唯一标志
     */
    String UID = "uid";
    /**
     * 用户节点记录
     */
    String SELF = "self";
    /**
     * 用户信息redis存储key前缀
     */
    String REDIS_USER_HASH_KEY = "user-";

    //排行榜相关
    /**
     * 更新策略
     */
    String UPDATE = "update";
    /**
     * 赛场KEY值
     */
    String MATCH_KEY = "matchKey";
    /**
     * 赛场用户记录
     */
    String REDIS_RANKING_USER_COLLECT_KEY = "ranking-user-collect";
    /**
     * 用户节点
     */
    String USERS = "users";
    /**
     * 排行
     */
    String RANK = "rank";

    /**
     * 分数
     */
    String SCORE = "score";
    /**
     * 总人数
     */
    String TOTAL = "total";
    /**
     * 查询截止
     */
    String START = "start";
    String END = "end";

    String MD5 = "md5";
    String MD5_KEY = "md5Key";
    String TIMES = "times";
    String TYPE = "type";
    String GAME_CODE_DATA = "game-code-data";
    String VERSION = "version";
    String APP_ID = "appId";
    String PLATFORM = "platform";

    /**
     * 匹配基本参数
     *
     * @param key 键值
     * @return 是否匹配
     */
    static boolean compareBaseKey(String key) {
        return key.equals(Constant.UID) || key.equals(Constant.APP_ID) || key.equals(Constant.VERSION) || key.equals(Constant.PLATFORM);
    }

    /**
     * 匹配游戏基础key
     *
     * @param key 键值
     * @return 是否匹配
     */
    static boolean compareGameBaseKey(String key) {
        return key.equals(Constant.TIMES) || key.equals(Constant.MD5) || key.equals(Constant.MD5_KEY);
    }
}
