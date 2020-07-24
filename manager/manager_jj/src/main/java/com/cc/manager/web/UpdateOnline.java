package com.cc.manager.web;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cc.manager.modules.jj.config.JjConfig;
import com.cc.manager.modules.jj.entity.Online;
import com.cc.manager.modules.jj.mapper.OnlineMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author cf
 */
@Component
public class UpdateOnline implements Runnable {

    private JjConfig jjConfig;
    private OnlineMapper onlineMapper;

    @Override
    public void run() {
        updateOnlineData();
    }

    public void updateOnlineData() {
        //获取在线人数请求URL
        String url = jjConfig.getFlushOnline();
        String res = HttpUtil.get(url);
        if (res != null) {
            JSONObject resObject = JSONObject.parseObject(res);
            if (!StringUtils.equals("success", resObject.getString("result")))
                return;
            JSONArray server = resObject.getJSONArray("server");
            Online online = new Online();
            online.setTimes(LocalDateTime.now());
            online.setInsertTime(LocalDateTime.now());
            AtomicInteger buzyRoom = new AtomicInteger(), idleRoom = new AtomicInteger(), onlineCount = new AtomicInteger();
            JSONObject gameInfo = new JSONObject();
            for (int i = 0; i < server.size(); i++) {
                JSONObject jsonObject = server.getJSONObject(i);
                JSONArray data = jsonObject.getJSONArray("data");
                for (int j = 0; j < data.size(); j++) {
                    JSONObject roomInfo = data.getJSONObject(j);
                    //空闲房间数
                    int idleCount = roomInfo.getInteger("idleCount");
                    //繁忙房间数
                    int buzyCount = roomInfo.getInteger("buzyCount");
                    buzyRoom.addAndGet(buzyCount);
                    idleRoom.addAndGet(idleCount);
                    JSONArray buzyRooms = roomInfo.getJSONArray("buzyRooms");
                    for (int k = 0; k < buzyRooms.size(); k++) {
                        JSONObject room = buzyRooms.getJSONObject(k);
                        int userCount = room.getInteger("userCount");
                        int gameCode = room.getInteger("gameCode");
                        String key = String.valueOf(gameCode);
                        JSONObject count = gameInfo.getJSONObject(key);
                        if (count == null)
                            count = new JSONObject();
                        if (count.containsKey("room"))
                            count.put("room", count.getInteger("room") + 1);
                        else
                            count.put("room", 1);

                        if (count.containsKey("total"))
                            count.put("total", count.getInteger("total") + userCount);
                        else
                            count.put("total", userCount);
                        gameInfo.put(key, count);
                        onlineCount.addAndGet(userCount);
                    }
                }
            }
            online.setBuzyRoom(buzyRoom.get());
            online.setIdleRoom(idleRoom.get());
            online.setOnline(onlineCount.get());
            online.setGameInfo(gameInfo.toJSONString());
            onlineMapper.insert(online);
        }
    }

    @Autowired
    public void setJjConfig(JjConfig jjConfig) {
        this.jjConfig = jjConfig;
    }

    @Autowired
    public void setOnlineMapper(OnlineMapper onlineMapper) {
        this.onlineMapper = onlineMapper;
    }

}
