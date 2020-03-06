package com.fish.dao.second.mapper;

import com.fish.dao.second.model.WxGroupHistory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WxGroupHistoryMapper {

    List<WxGroupHistory> selectAll();
    List<WxGroupHistory> selectSearchTime(@Param("start") String start, @Param("end") String end);


}
