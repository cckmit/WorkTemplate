package com.fish.dao.primary.mapper;

import com.fish.dao.primary.model.Rounds;

public interface RoundsMapper {

    Rounds selectByDdCodeS(String ddCode);

    Rounds selectByDdCodeQ(String ddCode);

}