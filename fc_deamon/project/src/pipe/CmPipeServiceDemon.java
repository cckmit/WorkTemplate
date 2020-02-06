package pipe;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import db.PeDbWeight;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tool.CmTool;
import tool.Log4j;
import tool.db.RedisUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

public class CmPipeServiceDemon implements Runnable
{
    private static final Logger LOG = LoggerFactory.getLogger(CmPipeServiceDemon.class);

    //
    // 套接字信息
    //
    private ServerSocket server = null;

    //链接socket交互
    private static Vector<CmPipeSocket> sockets = new Vector<>();

    public static Vector<CmPipeSocket> getSockets()
    {
        return sockets;
    }

    //缓存数据
    private static final String REDIS_KEY = "match-service";

    /**
     * 构造一个 CmPipeClient
     */
    public CmPipeServiceDemon(int port)
    {
        try
        {
            server = new ServerSocket(port);
        } catch (IOException e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
    }

    @Override
    public void run()
    {
        try
        {
            while (true)
            {
                Socket socket = server.accept();
                //2、调用accept()方法开始监听，等待客户端的连接
                //使用accept()阻塞等待客户请求，有客户
                //请求到来则产生一个Socket对象，并继续执行
                CmPipeSocket pipeSocket = new CmPipeSocket(socket);
                sockets.add(pipeSocket);
                pipeSocket.start();
            }

        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
    }

    /**
     * 进行移除房间数据
     */
    public static void removeSocket(CmPipeSocket socket)
    {
        sockets.removeElement(socket);
        System.out.println("移除socket");
        updateWeight();
    }

    /**
     * 更新权重参数
     */
    public static void updateWeight()
    {
        //通过地区分布服务器
        Vector<CmPipeSocket> sockets = getSockets();
        //当前策略配置
        String configStr = RedisUtils.hget(REDIS_KEY, "config");
        JSONObject config = new JSONObject();
        if (configStr != null)
            config = JSONObject.parseObject(configStr);

        JSONObject aresCache = new JSONObject();
        //地区分布
        for (CmPipeSocket socket : sockets)
        {
            //获取大区分配服务器IP
            String key = socket.getSocketKey();
            //获取区服务器
            JSONObject service = socket.getPkService();
            //所属区域
            int ares = service.getInteger("ares");
            JSONObject aresData = aresCache.getJSONObject(String.valueOf(ares));
            if (aresData == null)
                aresData = new JSONObject();
            //获取当前匹配服务容纳量
            int weight = service.getInteger("weight");
            JSONArray pkServices = socket.getPkRoom();
            JSONObject match = getMatchInfo(key, service, pkServices, config);
            //检测下架通知
            warningMinNotice(match, config, String.valueOf(ares));
            //当前匹配服权限配置暂停
            if (weight <= 0)
            {
                continue;
            }
            //获取当前区域匹配服的承载量
            aresData.put(key, match);
            aresCache.put(String.valueOf(ares), aresData);
        }
        //进行存储给redis，通知外部服务器通过该规则调用该数据
        RedisUtils.hset(REDIS_KEY, "service", aresCache.toJSONString());
        //按照规则分配，权重越低，优先停止
        //进行检测各区域是否达到阀值，阀值通告
        warningNotice(aresCache, config);
        updateMatchOnline();
    }

    //获取赛场在线人数信息
    private static void updateMatchOnline()
    {
        JSONObject online = new JSONObject();
        //通过地区分布服务器
        Vector<CmPipeSocket> sockets = getSockets();
        for (CmPipeSocket socket : sockets)
        {
            JSONArray pkServices = socket.getPkRoom();
            for (int i = 0; i < pkServices.size(); i++)
            {
                JSONObject pkService = pkServices.getJSONObject(i);
                JSONArray buzyRooms = pkService.getJSONArray("buzyRooms");
                for (int j = 0; j < buzyRooms.size(); j++)
                {
                    JSONObject room = buzyRooms.getJSONObject(j);
                    int userCount = room.getInteger("userCount");
                    String matchKey = room.getString("matchKey");
                    Integer count = online.getInteger(matchKey);
                    if (count == null)
                        count = userCount;
                    else
                        count += userCount;
                    online.put(matchKey, count);
                }
            }
        }
        RedisUtils.hset(REDIS_KEY, "online", online.toJSONString());
    }

    /**
     * 获取当前配置服相关参数
     */
    private static JSONObject getMatchInfo(String key, JSONObject service, JSONArray pkServices, JSONObject config)
    {
        JSONObject match = new JSONObject();
        match.put("key", key);
        match.put("port", service.getString("port"));
        match.put("weight", service.getString("weight"));
        //获取当前匹配服能够容纳房间数据
        JSONArray pkRooms = new JSONArray();
        if (pkServices != null)
            for (int i = 0; i < pkServices.size(); i++)
            {
                JSONObject pkService = pkServices.getJSONObject(i);
                String addr = pkService.getString("addr");
                Boolean pause = config.containsKey(addr) && config.getBoolean(addr);
                //空闲房间数
                int idle = pkService.getInteger("idleCount");
                //繁忙房间数
                int buzy = pkService.getInteger("buzyCount");
                //允许值
                int allow = pkService.getInteger("allow");
                //总房间数
                int roomSize = buzy + idle;
                //关停状态，不进行计算该服务器房间
                if (!pause)
                {
                    //进行判断
                    if (allow == -1 || allow == 1)
                    {
                        setRoomInfo(match, "pk", roomSize, buzy);
                    }
                    if (allow == -1 || allow == 0)
                    {
                        setRoomInfo(match, "through", roomSize, buzy);
                    }
                }
                //房间服，相关数据
                JSONObject room = new JSONObject();
                room.put("addr", addr);
                room.put("port", pkService.getInteger("port"));
                room.put("allow", pkService.getInteger("allow"));
                room.put("weight", pkService.getInteger("weight"));
                room.put("buzySize", buzy);
                room.put("roomSize", roomSize);
                pkRooms.add(room);
            }
        match.put("pkRoom", pkRooms);
        return match;
    }

    /**
     * 区域警告通知
     *
     * @param aresCache 区域服务器
     */
    private static void warningNotice(JSONObject aresCache, JSONObject config)
    {
        Set<String> configKey = new HashSet<>();
        for (Map.Entry<String, Object> set : config.entrySet())
        {
            configKey.add(set.getKey());
        }
        Set<String> currentKey = new HashSet<>();
        //进行计算是否达到阀值高峰期
        //每个区域pk和through服房间数是否达到80%
        for (Map.Entry<String, Object> set : aresCache.entrySet())
        {
            //地区内容
            String ares = set.getKey();
            //当前地区pk房间数信息，through房间数信息
            int pk = 0, through = 0, buzyPk = 0, buzyThrough = 0;
            JSONObject context = (JSONObject) set.getValue();
            for (Map.Entry<String, Object> match : context.entrySet())
            {
                JSONObject value = (JSONObject) match.getValue();
                if (value.containsKey("pk"))
                    pk += value.getInteger("pk");
                if (value.containsKey("buzy-pk"))
                    buzyPk += value.getInteger("buzy-pk");
                if (value.containsKey("through"))
                    through += value.getInteger("through");
                if (value.containsKey("buzy-through"))
                    buzyThrough += value.getInteger("buzy-through");
            }
            PeDbWeight peDbWeight = PeDbWeight.instance();
            //pk服充裕，进行关停游戏服务器
            BigDecimal pkRate = CmTool.div(buzyPk, pk, 2);
            //进行扩容报警
            if (buzyPk > 0 && pkRate.compareTo(peDbWeight.maxLimit) >= 0)
            {
                warningMaxNotice(peDbWeight, ares, pkRate, "pk服");
            }
            //闯关服扩容报警
            BigDecimal throughRate = CmTool.div(buzyThrough, through, 2);
            if (buzyThrough > 0 && throughRate.compareTo(peDbWeight.maxLimit) >= 0)
            {
                warningMaxNotice(peDbWeight, ares, throughRate, "闯关服");
            }
            JSONArray rooms = context.getJSONArray("pkRoom");
            if (rooms != null)
            {
                for (int i = 0; i < rooms.size(); i++)
                {
                    String addr = rooms.getJSONObject(i).getString("addr");
                    currentKey.add(addr);
                }
                //pk服缩容警告
                if (buzyPk <= 0 || pkRate.compareTo(peDbWeight.minLimit) <= 0)
                {
                    setPkService(config, rooms, 0);
                }
                //闯关服缩容警告
                if (buzyThrough <= 0 || throughRate.compareTo(peDbWeight.minLimit) <= 0)
                {
                    setPkService(config, rooms, 1);
                }
            }
        }
        configKey.forEach(key ->
        {
            if (!currentKey.contains(key))
            {
                config.remove(key);
            }
        });

        RedisUtils.hset(REDIS_KEY, "config", config.toJSONString());
    }

    /**
     * 设置游戏服配置
     *
     * @param config 默认配置
     * @param rooms  游戏服
     * @param allow  服务器允许类型
     */
    private static void setPkService(JSONObject config, JSONArray rooms, int allow)
    {
        //可以进行缩容
        //第一步，进行通过权重
        JSONObject pauseRoom = null;
        if (rooms.size() <= 1)
            return;
        for (int i = 0; i < rooms.size(); i++)
        {
            JSONObject room = rooms.getJSONObject(i);
            String addr = room.getString("addr");
            if (config.containsKey(addr) && config.getBoolean(addr))
                continue;
            if (room.getInteger("allow") != allow)
                if (pauseRoom == null || pauseRoom.getInteger("weight") < room.getInteger("weight"))
                {
                    pauseRoom = room;
                }
        }
        if (pauseRoom != null)
        {
            String addr = pauseRoom.getString("addr");
            config.put(addr, true);
        }
    }

    /**
     * 警告通知
     *
     * @param peDbWeight  权重配置
     * @param ares        区域信息
     * @param rate        扩容rate
     * @param serviceName 服务名称
     */
    private static void warningMaxNotice(PeDbWeight peDbWeight, String ares, BigDecimal rate, String serviceName)
    {
        String highTip = peDbWeight.ares.getString(ares);
        String per = rate.multiply(BigDecimal.valueOf(100)) + "%";
        peDbWeight.sendNotice("街机服务器扩容报警", highTip + "房间已达到" + per, serviceName + "房间空间已经占用达到" + per + ",请及时扩容保障正常游戏", serviceName + "紧张");
    }

    /**
     * 通知运营下架该服务器
     *
     * @param match  匹配服务逻辑
     * @param config 配置参数
     * @param ares   地区
     */
    private static void warningMinNotice(JSONObject match, JSONObject config, String ares)
    {
        PeDbWeight peDbWeight = PeDbWeight.instance();
        String areTip = peDbWeight.ares.getString(ares);
        String title = "街机服务器缩容通知";
        String highTip = areTip, normalTip, grayTip;
        //技术强制将匹配服下架，内部所有游戏服，房间空则下架
        JSONArray pkRoom = match.getJSONArray("pkRoom");
        int weight = match.getInteger("weight");
        boolean force = false;
        if (weight <= 0)
        {
            force = true;
            if (pkRoom == null || pkRoom.size() <= 0)
            {
                highTip += "匹配服已清空，申请下架,服务器:" + match.getString("key");
                normalTip = "服务器权重为" + weight + ",进行常规下架通知，当前游戏房间为0。";
                grayTip = "游戏房间数为空，请操作下架";
                peDbWeight.sendNotice(title, highTip, normalTip, grayTip);
                return;
            }
        }
        for (int i = 0; i < pkRoom.size(); i++)
        {
            JSONObject room = pkRoom.getJSONObject(i);
            String addr = room.getString("addr");
            if (force || (config.containsKey(addr) && config.getBoolean(addr)))
            {
                //房间已经全部释放
                if (room.getInteger("buzySize") <= 0)
                {
                    highTip += "游戏服已清空，申请下架,服务器:" + addr;
                    normalTip = MessageFormat.format("游戏服{0}申请下架，下架房间数{1},位于匹配服{2}下", addr, room.getInteger("roomSize"), match.getString("key"));
                    grayTip = "游戏房间数为空，请操作下架";
                    peDbWeight.sendNotice(title, highTip, normalTip, grayTip);
                    return;
                }
            }
        }
    }

    /**
     * 设置房间信息
     *
     * @param room     房间数据
     * @param type     房间类型
     * @param roomSize 房间大小
     * @param buzy     繁忙房间
     */
    private static void setRoomInfo(JSONObject room, String type, int roomSize, int buzy)
    {
        //总房间数
        if (room.containsKey(type))
        {
            room.put(type, room.getInteger(type) + roomSize);

        } else
        {
            room.put(type, roomSize);
        }
        //繁忙房间数
        String key = "buzy-" + type;
        if (room.containsKey(key))
        {
            room.put(key, room.getInteger(key) + buzy);

        } else
        {
            room.put(key, buzy);
        }
    }
}
