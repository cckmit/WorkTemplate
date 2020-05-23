package com.cc.manager.modules.jj.mapper;

import com.cc.manager.modules.jj.entity.Games;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author cf
 * @since 2020-05-08
 */
@Repository
public interface GamesMapper extends BaseMapper<Games> {

    @Select("select * from games where ddCode = #{ddCode,jdbcType=INTEGER}")
    Games selectByPrimaryKey(int ddCode);

    @Update("")
    int updateByPrimaryKeySelective(Games game);
}
