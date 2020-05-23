package com.cc.manager.modules.jj.mapper;

import com.cc.manager.modules.jj.entity.WxGroupManager;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author cf
 * @since 2020-05-09
 */
public interface WxGroupManagerMapper extends BaseMapper<WxGroupManager> {

    /**
     * 查询群二维码和客服二维码
     * @param ddId ddId
     * @return WxGroupManager
     */
    @Select(" select ddYes, ddNo from config_confirm where ddId = #{ddId,jdbcType=VARCHAR}")
    WxGroupManager selectQrCodeByPrimaryKey(String ddId);
}
