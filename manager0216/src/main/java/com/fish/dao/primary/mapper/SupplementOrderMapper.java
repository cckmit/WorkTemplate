package com.fish.dao.primary.mapper;

import com.fish.dao.primary.model.SupplementOrder;

import java.util.List;

public interface SupplementOrderMapper {

    int insert(SupplementOrder record);

    List<SupplementOrder> selectAll();

}