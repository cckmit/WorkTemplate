package com.cc.manager.modules.jj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cc.manager.modules.jj.entity.Games;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * @author cf
 * @since 2020-05-08
 */
@Repository
public interface GamesMapper extends BaseMapper<Games> {


    @Update("")
    int updateByPrimaryKeySelective(Games game);
}
