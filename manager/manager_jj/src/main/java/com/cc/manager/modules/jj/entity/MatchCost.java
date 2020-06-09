package com.cc.manager.modules.jj.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.cc.manager.common.mvc.BaseStatsEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author: CF
 * @date 2020/6/3 14:43
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MatchCost extends BaseStatsEntity<MatchCost> {

    @JSONField(format = "yyyy-MM-dd")
    private LocalDateTime ddTime;

    private String appId;

    private String gameCode;

    private BigDecimal coinCount;

    private BigDecimal videoCount;

    private BigDecimal coinTotal;

    private BigDecimal videoTotal;

    /**
     * 對比是否匹配
     *
     * @param cost 消耗参数
     * @return 匹配结果
     */
    public boolean compare(MatchCost cost) {
        if (getDdTime().compareTo(cost.getDdTime()) != 0)
            return false;
        if (appId != null && !getAppId().equals(cost.getAppId()))
            return false;
        return gameCode == null || getGameCode().equals(cost.getGameCode());
    }

}
