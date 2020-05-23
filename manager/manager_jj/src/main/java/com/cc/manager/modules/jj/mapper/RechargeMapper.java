package com.cc.manager.modules.jj.mapper;

import com.cc.manager.modules.jj.entity.Recharge;
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
 * @since 2020-05-09
 */
@Repository
public interface RechargeMapper extends BaseMapper<Recharge> {
    /**
     * 计算所有用户总的提现金额
     * @return list
     */
    @Select("SELECT  s.`ddUid` as ddUid, SUM(ddRmb) as ddRmb FROM  persie.recharge s  WHERE ddStatus = 200  GROUP BY s.`ddUid`")
    List<Recharge> selectAllUserRecharged();
}
