package com.fish.service;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.fish.dao.primary.mapper.OnlineStatisticsMapper;
import com.fish.dao.primary.model.OnlineStatistics;
import com.fish.protocols.GetParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class OnlineService implements BaseService<OnlineStatistics> {

    @Autowired
    OnlineStatisticsMapper onlineStatisticsMapper;

    @Override
    //查询在线房间数信息
    public List<OnlineStatistics> selectAll(GetParameter parameter) {
        String res = HttpUtil.get("https://sgame.qinyougames.com/persieDeamon/query/online");
        com.alibaba.fastjson.JSONObject resObject = com.alibaba.fastjson.JSONObject.parseObject(res);
        JSONArray server = resObject.getJSONArray("server");
        Integer userCounts = 0;
        for (int i = 0; i < server.size(); i++) {
            Object o = server.get(i);
            com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(o.toString());
            String data = jsonObject.getString("data");
            com.alibaba.fastjson.JSONObject jsonData = JSON.parseObject(data);
            com.alibaba.fastjson.JSONArray buzyRooms = jsonData.getJSONArray("buzyRooms");
            ArrayList<OnlineStatistics> rooms = new ArrayList<>();
            if (buzyRooms != null && buzyRooms.size() > 0) {
                for (int j = 0; j < buzyRooms.size(); j++) {
                    Object o1 = buzyRooms.get(j);
                    com.alibaba.fastjson.JSONObject oData = JSON.parseObject(o1.toString());
                    String gameCode = oData.getString("gameCode");//游戏代号
                    String roomSeq = oData.getString("roomSeq");//游戏房间号
                    String userCount = oData.getString("userCount");//用户数
                    userCounts = userCounts + Integer.valueOf(userCount);

                    OnlineStatistics onlineStatistics = new OnlineStatistics();
                    onlineStatistics.setDdcode(Integer.parseInt(gameCode));
                    onlineStatistics.setDdgamedistribution(roomSeq);
                    onlineStatistics.setDdonlinecount(userCounts.longValue());
                    onlineStatistics.setCreateTime(new Timestamp(new Date().getTime()));
                    rooms.add(onlineStatistics);
                }
            }
            String idleCount = jsonData.getString("idleCount");//空闲房间
            String buzyCount = jsonData.getString("buzyCount");//游戏中房间

            if (rooms != null && rooms.size() > 0) {
                for (OnlineStatistics room : rooms) {
                    room.setDdgameroomcount(Long.parseLong(buzyCount));
                    room.setDdremainroomcount(Long.parseLong(idleCount));
                    int insert = onlineStatisticsMapper.insert(room);
                }
            }
        }
        List<OnlineStatistics> onlineRoom = onlineStatisticsMapper.selectAll();
        return onlineRoom;
    }

    //新增appconfig信息
    public int insert(OnlineStatistics record) {

        return onlineStatisticsMapper.insert(record);
    }

    //更新appconfig信息
    public int updateByPrimaryKeySelective(OnlineStatistics record) {

        return onlineStatisticsMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public void setDefaultSort(GetParameter parameter) {

    }

    @Override
    public Class<OnlineStatistics> getClassInfo() {
        return OnlineStatistics.class;
    }

    @Override
    public boolean removeIf(OnlineStatistics onlineStatistics, Map<String, String> searchData) {


        return true;
    }
}
