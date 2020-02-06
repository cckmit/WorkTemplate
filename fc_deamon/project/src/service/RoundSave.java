package service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import config.ReadConfig;
import db.PeDbRoundExt;
import db.PeDbRoundRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Tuple;
import tool.CmTool;
import tool.db.RedisUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 赛场保存一场记录
 */
public class RoundSave implements Runnable
{
    private static final Logger LOG = LoggerFactory.getLogger(RoundSave.class);
    //赛场状态
    private static final String ROUND_REDIS_KEY = "round-status";
    //一场赛场记录
    private PeDbRoundRecord roundRecord;
    //赛场名称
    private PeDbRoundExt roundExt;

    RoundSave(PeDbRoundRecord roundRecord, PeDbRoundExt roundExt)
    {
        this.roundRecord = roundRecord;
        this.roundExt = roundExt;
    }

    /**
     * 更新游戏状态
     *
     * @param instance 赛场配置
     * @param status   状态
     */
    static void updateStatus(PeDbRoundRecord instance, String status)
    {
        LOG.debug("updateStatus:" + JSONObject.toJSONString(instance) + ",status=" + status);
        JSONObject info = getRoundInfo(instance);
        String field = getField(instance.ddCode, instance.ddGroup, instance.ddIndex);
        info.put("matchKey", field);
        info.put("status", status);
        info.put("code", instance.ddCode);
        info.put("isGroup", instance.ddGroup);
        info.put("index", instance.ddIndex);
        info.put("gameCode", instance.ddGame);
        info.put("name", instance.ddName);
        info.put("round", instance.ddRound);
        info.put("start", instance.ddStart.getTime());
        info.put("end", instance.ddEnd.getTime());
        info.put("submit", instance.ddSubmit.getTime());
        info.put("result", instance.ddResult);
        LOG.debug("matchInfo:" + info.toJSONString());
        RedisUtils.hset(ROUND_REDIS_KEY, field, info.toJSONString());
    }

    /**
     * 获取赛制信息
     *
     * @param instance 赛场配置
     * @return 赛制内容
     */
    private static JSONObject getRoundInfo(PeDbRoundRecord instance)
    {
        String field = getField(instance.ddCode, instance.ddGroup, instance.ddIndex);
        String json = RedisUtils.hget(ROUND_REDIS_KEY, field);
        JSONObject info = null;
        if (json != null)
            info = JSONObject.parseObject(json);
        if (info == null)
            info = new JSONObject();
        return info;
    }

    /**
     * 获取当前状态
     *
     * @param instance 赛场配置
     */
    private static String getStatus(PeDbRoundRecord instance)
    {
        JSONObject info = getRoundInfo(instance);
        if (info.containsKey("status"))
            return info.getString("status");
        return "unknown";
    }

    /**
     * 更新赛场信息
     *
     * @param games 游戏赛场内容
     */
    static void updateNowRoundRecord(Map<Integer, PeDbRoundRecord> games)
    {
        games.forEach((k, v) ->
        {
            String field = String.format("current-g%d", k);
            String json = RedisUtils.hget(ROUND_REDIS_KEY, field);
            JSONObject data = null;
            if (json != null)
            {
                data = JSONObject.parseObject(json);
            }
            if (data == null)
            {
                data = new JSONObject();
            }
            data.put(String.valueOf(v.ddGroup), getField(v.ddCode, v.ddGroup, v.ddIndex));
            RedisUtils.hset(ROUND_REDIS_KEY, field, data.toJSONString());
        });
    }

    @Override
    public void run()
    {
        long current = System.currentTimeMillis();
        //进行关停当前赛场
        String field = getField(roundRecord.ddCode, roundRecord.ddGroup, roundRecord.ddIndex);
        //是否达到结算时间
        if (current >= this.roundRecord.ddSubmit.getTime() && getStatus(roundRecord).equals("running"))
        {
            //标志该赛制正在结算
            updateStatus(roundRecord, "finish");
            //获取排行信息0,1名
            Set<Tuple> sets = RedisUtils.zrevrangeWithScores(field, 0, -1);
            //已处理结算
            if (sets == null)
            {
                updateStatus(roundRecord, "over");
                return;
            }
            JSONObject reward = roundExt.getRoundReward();
            //排名和分数
            JSONArray array = new JSONArray();
            AtomicInteger atomic = new AtomicInteger();
            Vector<String> uids = new Vector<>();
            for (Tuple tuple : sets)
            {
                JSONObject user = new JSONObject();
                //当前分数
                long mark = BigDecimal.valueOf(tuple.getScore()).longValue();
                //用户uid
                String uid = tuple.getElement();
                //排名次数
                int index = atomic.incrementAndGet();
                user.put("mark", mark);
                user.put("uid", uid);
                user.put("index", index);
                if (reward.containsKey(String.valueOf(index)))
                {
                    JSONObject info = reward.getJSONObject(String.valueOf(index));
                    String type = info.getString("type");
                    int value = info.getInteger("value");
                    switch (type)
                    {
                        case "rmb":
                        {
                            value *= 100;
                        }
                        break;
                    }
                    user.put("type", type);
                    user.put("value", value);
                } else
                {
                    user.put("type", "none");
                    user.put("value", 0);
                }
                array.add(user);
                uids.add(uid);
            }
            //用户昵称头像
            String[] userId = new String[uids.size()];
            userId = uids.toArray(userId);
            List<String> data = getUserCollects(userId);
            if (data != null)
            {
                for (int i = 0; i < array.size(); i++)
                {
                    JSONObject user = array.getJSONObject(i);
                    if (i < data.size())
                    {
                        JSONObject info = JSONObject.parseObject(data.get(i));
                        for (Map.Entry<String, Object> entry : info.entrySet())
                        {
                            user.put(entry.getKey(), entry.getValue());
                        }
                    }
                }
            }

            int resultSize = array.size();
            if (array.size() > 0)
            {
                AtomicInteger index = new AtomicInteger();
                String name = ReadConfig.get("match-save-path") + field + "-" + index.getAndIncrement() + ".json";
                JSONArray save = new JSONArray();
                //进行保存写入文件
                for (int i = 0; i < array.size(); i++)
                {
                    save.add(array.getJSONObject(i));
                    if (save.size() >= 100)
                    {
                        String json = save.toJSONString();
                        CmTool.writeFileString(name, json);
                        save.clear();
                        name = ReadConfig.get("match-save-path") + field + "-" + index.getAndIncrement() + ".json";
                    }
                }
                if (save.size() > 0)
                {
                    String json = save.toJSONString();
                    CmTool.writeFileString(name, json);
                    save.clear();
                }
            }
            roundRecord.ddResult = resultSize;
            roundRecord.update("ddResult");
            updateStatus(roundRecord, "over");
        }
    }

    /**
     * 获取用户集合信息
     *
     * @param userId 用户编号
     * @return 集合
     */
    private static List<String> getUserCollects(String... userId)
    {
        if (userId.length <= 0)
            return null;
        String key = getUserCollectsKey();
        return RedisUtils.hmget(key, userId);
    }

    /**
     * 用户集合信息
     *
     * @return 用户集合key
     */
    private static String getUserCollectsKey()
    {
        return "ranking-user-collect";
    }

    /**
     * 获取赛场记录标签
     *
     * @param ddCode  赛场编号
     * @param ddGroup 群标签
     * @param ddIndex 轮次编号
     */
    private static String getField(int ddCode, boolean ddGroup, int ddIndex)
    {
        return String.format("match-c%d-g%d-i%d", ddCode, ddGroup ? 1 : 0, ddIndex);
    }
}
