package com.cc.manager.modules.jj.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cc.manager.common.mvc.BaseCrudEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author cf
 * @since 2020-05-08
 */
@Data
@TableName(schema = "persie_deamon", value = "buy_pay")
public class BuyPay implements BaseCrudEntity<BuyPay> {

    /**
     * Id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 日期
     */
    @TableField(value = "buy_date")
    private String buyDate;

    /**
     * AppId
     */
    @TableField(value = "buy_app_id")
    private String buyAppId;

    /**
     * 产品名称
     */
    @TableField(value = "buy_product_name")
    private String buyProductName;

    /**
     * 买量支出
     */
    @TableField(value = "buy_cost")
    private BigDecimal buyCost;

    /**
     * 点击数量
     */
    @TableField(value = "buy_click_number")
    private Integer buyClickNumber;

    /**
     * 点击价格
     */
    @TableField(value = "buy_click_price")
    private BigDecimal buyClickPrice;

    /**
     * 插入时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm")
    @TableField(value = "insert_time")
    private LocalDateTime insertTime;


    @Override
    public String getCacheKey() {
        return this.buyDate + "-" + this.buyAppId;
    }

    @Override
    public String getCacheValue() {
        return this.buyDate + "-" + this.buyAppId;
    }

}
