package com.cc.manager.modules.jj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cc.manager.modules.jj.entity.Recharge;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author cf
 * @since 2020-05-09
 */
@Repository
public interface RechargeMapper extends BaseMapper<Recharge> {
    /**
     * 计算所有用户总的提现金额
     *
     * @return list
     */
    List<Recharge> selectAllUserRecharged();

    /**
     * 查询提现记录数据
     *
     * @return list
     */
    List<Recharge> selectAllChargeRecord(String start, String end);

    /**
     * 查询提现审核数据
     *
     * @return list
     */
    List<Recharge> selectAllRechargeAudit(String start, String end);

    /**
     * 查询用户已提现金额
     *
     * @return int
     */
    int selectCashOut(String ddUid, String ddTime);

    /**
     * 更新提现审核选择数据
     *
     * @return int
     */
    int updateBySelective(Recharge record);

}
