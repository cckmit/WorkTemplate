package com.code.entity;
import java.util.Vector;

import javax.persistence.Column;
import javax.persistence.Entity;
import com.annotation.Comments;
import com.annotation.Page;
import com.annotation.PrimaryKey;
import com.annotation.ReadOnly;

   /**
    * minitj_runtime 实体类
    * 2019-02-21 xuweihua
    */ 

@Entity
@Page
public class Minitj_runtime
{
	@PrimaryKey
	@Column(name="runtime_gameid")
	@Comments(name="游戏id")
	public int runtime_gameid;
	

	@ReadOnly
	@Column(name="game_name")
	@Comments(name="游戏名称")
	public String game_name;
	@ReadOnly
	@Column(name="game_appid")
	@Comments(name="Appid")
	public String game_appid;
	
	
	@Column(name="runtime_date")
	@Comments(name="日期")
	public String runtime_date;
	@Column(name="runtime_visit")
	@Comments(name="访问次数")
	public int runtime_visit;
	@Column(name="runtime_apppull")
	@Comments(name="app拉起次数")
	public int runtime_apppull;
	@Column(name="runtime_videoreq")
	@Comments(name="视频请求次数")
	public int runtime_videoreq;
	@Column(name="runtime_videoreq_bypull")
	@Comments(name="来自APP拉起视频请求次数")
	public int runtime_videoreq_bypull;
	@Column(name="runtime_bannershow")
	@Comments(name="bannel展示次数")
	public int runtime_bannershow;
	@Column(name="runtime_bannershow_bypull")
	@Comments(name="来自APP拉起bannel展示次数")
	public int runtime_bannershow_bypull;
	@Column(name="runtime_recuser")
	@Comments(name="推荐位点击人数")
	public int runtime_recuser;
	@Column(name="runtime_reccount")
	@Comments(name="推荐位点击次数")
	public int runtime_reccount;
	@Column(name="runtime_recjump_suc")
	@Comments(name="推荐位点击跳转成功次数")
	public int runtime_recjump_suc;
	@Column(name="runtime_shopuser")
	@Comments(name="商店点击人数")
	public int runtime_shopuser;
	@Column(name="runtime_shopcount")
	@Comments(name="商店点击次数")
	public int runtime_shopcount;
	@Column(name="runtime_shoprec_count")
	@Comments(name="商店推荐位点击次数")
	public int runtime_shoprec_count;
	@Column(name="runtime_coin_cost")
	@Comments(name="金币消耗")
	public int runtime_coin_cost;
	@Column(name="runtime_coin_remain")
	@Comments(name="金币剩余")
	public int runtime_coin_remain;
	/*@Column(name="runtime_urlclick_newuser")
	@Comments(name="新用户点击次数")
	public int runtime_urlclick_newuser;
	@Column(name="runtime_urlclick_olduser")
	@Comments(name="老用户点击次数")
	public int runtime_urlclick_olduser;*/
	@Column(name="runtime_updatetime")
	@Comments(name="更新时间")
	public String runtime_updatetime;
	
	
	public static void addSumLine(Vector<Minitj_runtime> list)
	{
		if(list == null || list.size() <= 0)
		{
			return;
		}
		Minitj_runtime runtime = new Minitj_runtime();
		int runtime_visit = 0,
			runtime_apppull = 0,
			runtime_videoreq = 0,
			runtime_videoreq_bypull = 0,
			runtime_bannershow = 0,
			runtime_bannershow_bypull=0;
		for (Minitj_runtime daily : list)
		{
			runtime_visit += daily.runtime_visit;
			runtime_apppull += daily.runtime_apppull;
			runtime_videoreq += daily.runtime_videoreq;
			runtime_videoreq_bypull += daily.runtime_videoreq_bypull;
			runtime_bannershow += daily.runtime_bannershow;
			runtime_bannershow_bypull+=daily.runtime_bannershow_bypull;
		}
		runtime.runtime_date = "总计";
		runtime.game_name = "-";
		runtime.game_appid = "-";
		runtime.runtime_visit = runtime_visit;
		runtime.runtime_apppull = runtime_apppull;
		runtime.runtime_videoreq = runtime_videoreq;
		runtime.runtime_videoreq_bypull = runtime_videoreq_bypull;
		runtime.runtime_bannershow = runtime_bannershow;	
		runtime.runtime_bannershow_bypull = runtime_bannershow_bypull;	
		list.add(runtime);
	}

}
