package com.code.entity;

import com.annotation.Comments;
import com.annotation.Page;
import com.annotation.TableName;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.math.BigDecimal;
import java.util.Vector;

/**
 * persie_value 实体类
 * 2020-04-17
 */

@Entity
@Page
@TableName("persie_value")
public class Persie_value {

    @Column(name = "wx_appid")
    @Comments(name = "游戏id")
    public String wx_appid;

    @Comments(name = "插屏曝光量")
    public int wx_screen_show;

    @Comments(name = "插屏点击数")
    public BigDecimal wx_screen_click;

    @Comments(name = "插屏点击率")
    public BigDecimal wx_screen_clickrate;

    @Comments(name = "插屏收入")
    public BigDecimal wx_screen_income;

    @Comments(name = "screen ECPM")
    public BigDecimal screenEcpm;

    @Column(name = "wx_date")
    @Comments(name = "日期")
    public String wx_date;

    public static void addSum(Vector<Persie_value> list) {
        if (list == null || list.size() <= 0) {
            return;
        }
        for (Persie_value persieValue : list) {
            persieValue.wx_screen_clickrate = new BigDecimal(0);
            if (persieValue.wx_screen_click != null && persieValue.wx_screen_show != 0) {
                persieValue.wx_screen_clickrate = persieValue.wx_screen_click.divide(BigDecimal.valueOf(persieValue.wx_screen_show), 2, BigDecimal.ROUND_HALF_UP);
            }
            persieValue.screenEcpm = new BigDecimal(0);
            if (persieValue.wx_screen_income != null && persieValue.wx_screen_show != 0) {
                persieValue.screenEcpm = persieValue.wx_screen_income.divide(BigDecimal.valueOf(persieValue.wx_screen_show), 2, BigDecimal.ROUND_HALF_UP);
            }
        }

    }

}
