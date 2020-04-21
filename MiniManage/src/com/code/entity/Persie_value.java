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
 * 2020-04-17  CF
 */

@Entity
@Page
@TableName("persie_value")
public class Persie_value {

    @Column(name = "wx_appid")
    @Comments(name = "游戏id")
    public String wx_appid;

    @Column(name = "wx_screen_show")
    @Comments(name = "插屏曝光量")
    public BigDecimal wx_screen_show;
    @Column(name = "wx_screen_click")
    @Comments(name = "插屏点击数")
    public BigDecimal wx_screen_click;

    @Comments(name = "插屏点击率")
    public BigDecimal wx_screen_clickrate;
    @Column(name = "wx_screen_income")
    @Comments(name = "插屏收入")
    public BigDecimal wx_screen_income;

    @Comments(name = "screen ECPM")
    public BigDecimal screenEcpm;

    @Column(name = "wx_date")
    @Comments(name = "日期")
    public String wx_date;

    public static void addSumScreen(Vector<Persie_value> list) {
        if (list == null || list.size() <= 0) {
            return;
        }
        for (Persie_value persieValue : list) {
            if (persieValue.wx_screen_click != null && persieValue.wx_screen_show.compareTo(BigDecimal.ZERO) != 0) {
                persieValue.wx_screen_clickrate = persieValue.wx_screen_click.divide(persieValue.wx_screen_show, 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
            }
        }
    }

}
