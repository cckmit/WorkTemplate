package com.cc.manager.modules.jj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cc.manager.modules.jj.entity.RoundExt;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @author cf
 * @since 2020-05-11
 */
@Repository
public interface RoundExtMapper extends BaseMapper<RoundExt> {

    @Select("SELECT  COUNT(*) FROM persie_deamon.round_ext  WHERE ddGroup = TRUE")
    int selectGMaxId();

    @Select("SELECT  COUNT(*) FROM persie_deamon.round_ext  WHERE ddGroup = FALSE")
    int selectSMaxId();

}
