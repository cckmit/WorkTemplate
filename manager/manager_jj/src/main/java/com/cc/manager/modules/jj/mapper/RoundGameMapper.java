package com.cc.manager.modules.jj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cc.manager.modules.jj.entity.RoundGame;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author cf
 * @since 2020-05-08
 */
@Repository
public interface RoundGameMapper extends BaseMapper<RoundGame> {

    @Select("select *  from persie_deamon.round_game where ddCode = #{ddcode,jdbcType=INTEGER}")
    RoundGame selectByPrimaryKey(int ddcode);
}
