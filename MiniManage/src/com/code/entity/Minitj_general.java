package com.code.entity;
import java.math.BigDecimal;
import java.util.Vector;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.annotation.Comments;
import com.annotation.Page;
import com.annotation.PrimaryKey;
import com.annotation.ReadOnly;
import com.annotation.TableName;

   /**
    * minitj_wx 实体类
    * 2019-02-21 xuweihua
    */ 

@Entity
@Page
@TableName("minitj_wx")
public class Minitj_general
{
	@PrimaryKey
	@Column(name="wx_appid")
	@Comments(name="游戏id")
	public String wx_appid;
	
	@ReadOnly
	@Column(name="game_name")
	@Comments(name="产品名称")
	public String game_name;
	@Comments(name="收入")
	public BigDecimal totalIncome;
	@Comments(name="活跃UP")
	public BigDecimal activeUp;
	
	@ReadOnly
	@Column(name="manual_outgo")
	@Comments(name="买量支出")
	public BigDecimal manual_outgo;
	@Comments(name="买量单价")
	public BigDecimal unit_price;
	
	@Column(name="wx_date")
	@Comments(name="日期")
	public String wx_date;
	@Column(name="wx_new")
	@Comments(name="注册用户数")
	public int wx_new;
	@Column(name="wx_active")
	@Comments(name="活跃用户数")
	public int wx_active;
	@Column(name="wx_visit")
	@Comments(name="访问用户数")
	public int wx_visit;
	@Column(name="wx_avg_login")
	@Comments(name="人均登录次数")
	public BigDecimal wx_avg_login;
	@Column(name="wx_avg_online")
	@Comments(name="人均在线时长")
	public BigDecimal wx_avg_online;
	@Column(name="wx_remain2")
	@Comments(name="注册次留")
	public BigDecimal wx_remain2;
	@Column(name="wx_video_show")
	@Comments(name="视频曝光量")
	public int wx_video_show;
	@Column(name="wx_video_clickrate")
	@Comments(name="视频点击率")
	public BigDecimal wx_video_clickrate;
	@Column(name="wx_video_income")
	@Comments(name="视频收入")
	public BigDecimal wx_video_income;
	@Column(name="wx_banner_show")
	@Comments(name="bannel曝光量")
	public int wx_banner_show;
	@Column(name="wx_banner_clickrate")
	@Comments(name="bannel点击率")
	public BigDecimal wx_banner_clickrate;
	@Column(name="wx_banner_income")
	@Comments(name="bannel收入")
	public BigDecimal wx_banner_income;

	@Comments(name="插屏收入")
	public BigDecimal wx_screen_income;

	@Column(name="wx_reg_ad")
	@Comments(name="注册用户来自广告占比")
	public int wx_reg_ad;
	@Column(name="wx_reg_jump")
	@Comments(name="注册用户来自跳转占比")
	public int wx_reg_jump;
	@Column(name="wx_reg_search")
	@Comments(name="注册用户来自搜索占比")
	public int wx_reg_search;
	@Column(name="wx_reg_app")
	@Comments(name="注册用户来自app占比")
	public int wx_reg_app;
	@Column(name="wx_reg_code")
	@Comments(name="注册用户来自二维码占比")
	public int wx_reg_code;
	@Column(name="wx_reg_session")
	@Comments(name="注册用户来自会话占比")
	public int wx_reg_session;
	@Column(name="wx_active_women")
	@Comments(name="活跃女性占比")
	public BigDecimal wx_active_women;
	@Column(name="wx_share_user")
	@Comments(name="分享人数")
	public int wx_share_user;
	@Column(name="wx_share_count")
	@Comments(name="分享次数")
	public int wx_share_count;
	@Column(name="wx_share_rate")
	@Comments(name="分享率")
	public BigDecimal wx_share_rate;
	@Column(name="wx_updatetime")
	@Comments(name="更新时间")
	public String wx_updatetime;
	public static void addSumLine(Vector<Minitj_general> list)
	{
		if(list == null || list.size() <= 0)
		{
			return;
		}
		Minitj_general general = new Minitj_general();
		int wx_visit = 0;
		BigDecimal totalIncome=new BigDecimal(0);
		BigDecimal activeUp=new BigDecimal(0);
		BigDecimal	wx_avg_login = new BigDecimal(0);
		BigDecimal	wx_avg_online = new BigDecimal(0);
		BigDecimal	wx_remain2 = new BigDecimal(0);
		BigDecimal	wx_share_rate = new BigDecimal(0);
		BigDecimal	manual_outgo = new BigDecimal(0);
		BigDecimal	unit_price = new BigDecimal(0);
			
		for (Minitj_general  minitj_general: list)
		{
			wx_visit+=minitj_general.wx_visit;
			if(minitj_general.totalIncome==null) minitj_general.totalIncome=new BigDecimal(0);
			totalIncome=totalIncome.add(minitj_general.totalIncome);
			if(minitj_general.activeUp==null) minitj_general.activeUp=new BigDecimal(0);
			activeUp=activeUp.add(minitj_general.activeUp);
			wx_avg_login=wx_avg_login.add(minitj_general.wx_avg_login);
			wx_avg_online=wx_avg_online.add(minitj_general.wx_avg_online);
			wx_remain2=wx_remain2.add(minitj_general.wx_remain2);
			wx_share_rate=wx_share_rate.add(minitj_general.wx_share_rate);
			if(minitj_general.manual_outgo==null) minitj_general.manual_outgo=new BigDecimal(0);
			if(minitj_general.unit_price==null) minitj_general.unit_price=new BigDecimal(0);
			manual_outgo=manual_outgo.add(minitj_general.manual_outgo);
			unit_price=unit_price.add(minitj_general.unit_price);
		}
		general.wx_date = "总计";
		general.game_name ="-";
		general.wx_new = 0;
		general.wx_active = 0;
		general.wx_visit = wx_visit;
		general.totalIncome = totalIncome;
		BigDecimal size = new BigDecimal(list.size());
		general.activeUp = activeUp.divide(size,2, BigDecimal.ROUND_HALF_UP);
		general.wx_avg_login = wx_avg_login.divide(size,2, BigDecimal.ROUND_HALF_UP);
		general.wx_avg_online = wx_avg_online.divide(size,2, BigDecimal.ROUND_HALF_UP);
		general.wx_remain2 = wx_remain2.divide(size,2, BigDecimal.ROUND_HALF_UP);
		general.wx_share_rate =wx_share_rate.divide(size,2, BigDecimal.ROUND_HALF_UP);
		general.manual_outgo = manual_outgo;	
		general.unit_price = unit_price.divide(size,2, BigDecimal.ROUND_HALF_UP);
		list.add(general);
	}
}
