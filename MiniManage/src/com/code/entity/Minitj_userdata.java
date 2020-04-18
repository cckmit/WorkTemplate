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
public class Minitj_userdata
{
	@PrimaryKey
	@Column(name="wx_appid")
	@Comments(name="游戏id")
	public String wx_appid;
	@Column(name="wx_date")
	@Comments(name="日期")
	public String wx_date;
	
	@ReadOnly
	@Column(name="game_name")
	@Comments(name="产品名称")
	public String game_name;
	
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
	@Column(name="wx_reg_other")
	@Comments(name="注册用户来自其他")
	public int wx_reg_other;
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
	@Column(name="wx_reg_json")
	@Comments(name="注册用户来源JSON")
	public String wx_reg_json;
	@Column(name="wx_updatetime")
	@Comments(name="更新时间")
	public String wx_updatetime;
	public static void addSumLine(Vector<Minitj_userdata> list)
	{
		if(list == null || list.size() <= 0)
		{
			return;
		}
		Minitj_userdata userdata = new Minitj_userdata();
		int wx_new=0,
			wx_reg_ad=0,
			wx_reg_jump=0,
			wx_reg_search=0,
			wx_reg_app=0,
			wx_reg_code=0,
			wx_reg_session=0,
			wx_reg_other=0,
			wx_share_count=0;
		BigDecimal	wx_active_women=new BigDecimal(0);
		BigDecimal wx_share_rate=new BigDecimal(0);
		for (Minitj_userdata  minitj_userdata: list)
		{
			wx_new+=minitj_userdata.wx_new;
			wx_reg_ad += minitj_userdata.wx_reg_ad;
			wx_reg_jump += minitj_userdata.wx_reg_jump;
			wx_reg_search += minitj_userdata.wx_reg_search;
			wx_reg_app += minitj_userdata.wx_reg_app;
			wx_reg_code += minitj_userdata.wx_reg_code;
			wx_reg_session+=minitj_userdata.wx_reg_session;
			wx_share_count+=minitj_userdata.wx_share_count;
			wx_reg_other+=minitj_userdata.wx_reg_other;
			wx_active_women=wx_active_women.add(minitj_userdata.wx_active_women);
			wx_share_rate=wx_share_rate.add(minitj_userdata.wx_share_rate);
			/*if(minitj_userdata.videoEcpm==null) minitj_userdata.videoEcpm=0.00;*/
		}
		userdata.wx_date = "总计";
		userdata.game_name ="-";
		userdata.wx_new = wx_new;
		userdata.wx_reg_ad = wx_reg_ad;
		userdata.wx_reg_jump = wx_reg_jump;
		userdata.wx_reg_search = wx_reg_search;
		userdata.wx_reg_app = wx_reg_app;
		userdata.wx_reg_code = wx_reg_code;
		userdata.wx_reg_session = wx_reg_session;
		userdata.wx_share_count = wx_share_count;
		userdata.wx_reg_other = wx_reg_other;
		BigDecimal size = new BigDecimal(list.size());
		userdata.wx_active_women =  wx_active_women.divide(size,2, BigDecimal.ROUND_HALF_UP);
		userdata.wx_share_rate = wx_share_rate.divide(size,2, BigDecimal.ROUND_HALF_UP);
		list.add(userdata);
	}
}
