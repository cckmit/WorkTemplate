package com.fish.dao.primary.mapper;

import com.fish.dao.primary.model.RoundRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoundRecordMapper {

    List<RoundRecord> selectGRank();

    List<RoundRecord> selectGRankTime(@Param("start") String start, @Param("end") String end);

    List<RoundRecord> selectSRank();

    List<RoundRecord> selectSRankTime(@Param("start") String start, @Param("end") String end);

}