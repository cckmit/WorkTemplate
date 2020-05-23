package com.cc.manager.modules.jj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cc.manager.modules.jj.entity.RoundMatch;
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
public interface RoundMatchMapper extends BaseMapper<RoundMatch> {

    @Select("select *   from persie_deamon.round_match where ddCode = #{ddcode,jdbcType=INTEGER}")
    RoundMatch selectByPrimaryKey(int ddcode);
}
