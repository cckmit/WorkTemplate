package com.cc.manager.modules.jj.mapper;

import com.cc.manager.modules.jj.entity.PublicCentre;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author cf
 * @since 2020-05-13
 */
@Repository
public interface PublicCentreMapper extends BaseMapper<PublicCentre> {

    @Select("select * from persie_deamon.public_centre where recommend_type = 0 ORDER BY show_id")
    List<PublicCentre> selectAllBanner();
    @Select("select * from persie_deamon.public_centre where recommend_type = 1 ORDER BY show_id")
    List<PublicCentre> selectAllRecommend();
    @Select("select * from persie_deamon.public_centre where recommend_type = 2 ORDER BY show_id")
    List<PublicCentre> selectAllGame();
}
