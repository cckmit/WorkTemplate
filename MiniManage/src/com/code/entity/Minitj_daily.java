package com.code.entity;
import java.math.BigDecimal;
import java.util.Vector;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.annotation.Comments;
import com.annotation.Page;
import com.annotation.PrimaryKey;

   /**
    * minitj_daily 实体类
    * 2019-02-21 xuweihua
    */ 

@Entity
@Page
public class Minitj_daily
{
	@PrimaryKey
	@Column(name="daily_date")
	@Comments(name="日期")
	public String daily_date;
	@Column(name="daily_game_amount")
	@Comments(name="游戏数量")
	public int daily_game_amount;
	@Column(name="daily_new")
	@Comments(name="新增用户数")
	public int daily_new;
	@Column(name="daily_active")
	@Comments(name="活跃用户数")
	public int daily_active;
	@Column(name="daily_visit")
	@Comments(name="访问总次数")
	public int daily_visit;
//	@Column(name="daily_video_req")
//	@Comments(name="视频请求次数")
//	public int daily_video_req;
	@Column(name="daily_recjump_req")
	@Comments(name="推荐位跳转请求次数")
	public int daily_recjump_req;
	@Column(name="daily_recjump_suc")
	@Comments(name="推荐位跳转成功次数")
	public int daily_recjump_suc;
	@Column(name="daily_outgo")
	@Comments(name="买量支出")
	public BigDecimal daily_outgo;
	@Column(name="daily_income_ad")
	@Comments(name="广告收入")
	public BigDecimal daily_income_ad;
	@Column(name="daily_income_video")
	@Comments(name="视频收入")
	public BigDecimal daily_income_video;
	@Column(name="daily_income_banner")
	@Comments(name="banner收入")
	public BigDecimal daily_income_banner;
	@Column(name="daily_share")
	@Comments(name="分享次数")
	public int daily_share;

	public static void addSumLine(Vector<Minitj_daily> list)
	{
		if(list == null || list.size() <= 0)
		{
			return;
		}
		Minitj_daily daily = new Minitj_daily();
		int daily_new = 0,
			daily_active = 0,
			daily_visit = 0,
			daily_recjump_req = 0,
			daily_recjump_suc = 0,
			daily_share = 0;
		BigDecimal daily_outgo=new BigDecimal(0);
		BigDecimal	daily_income_ad = new BigDecimal(0);
		BigDecimal	daily_income_video = new BigDecimal(0);
		BigDecimal	daily_income_banner = new BigDecimal(0);
			
		for (Minitj_daily daily1 : list)
		{
			daily_new += daily1.daily_new;
			daily_active += daily1.daily_active;
			daily_visit += daily1.daily_visit;
			daily_recjump_req += daily1.daily_recjump_req;
			daily_recjump_suc += daily1.daily_recjump_suc;
			daily_share+=daily1.daily_share;
			daily_outgo=daily_outgo.add(daily1.daily_outgo);
			daily_income_ad=daily_income_ad.add(daily1.daily_income_ad);
			daily_income_video=daily_income_video.add(daily1.daily_income_video);
			daily_income_banner=daily_income_banner.add(daily1.daily_income_banner);
		}
		daily.daily_date = "总计";
		daily.daily_game_amount =0;
		daily.daily_new = daily_new;
		daily.daily_active = daily_active;
		daily.daily_visit = daily_visit;
		daily.daily_recjump_req = daily_recjump_req;
		daily.daily_recjump_suc = daily_recjump_suc;	
		daily.daily_share = daily_share;	
		daily.daily_outgo = daily_outgo;	
		daily.daily_income_ad = daily_income_ad;	
		daily.daily_income_video = daily_income_video;	
		daily.daily_income_banner = daily_income_banner;	
		list.add(daily);
	}
}
