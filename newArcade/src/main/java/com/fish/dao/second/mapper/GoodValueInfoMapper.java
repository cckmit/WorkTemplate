package com.fish.dao.second.mapper;

import com.fish.dao.second.model.GoodValueInfo;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface GoodValueInfoMapper{

    GoodValueInfo selectByGoodsId(Integer goodsId);

    List<GoodValueInfo> selectAll();
}
