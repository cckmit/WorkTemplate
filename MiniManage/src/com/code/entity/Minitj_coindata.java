package com.code.entity;
import javax.persistence.Column;
import javax.persistence.Entity;

import com.annotation.Comments;
import com.annotation.Page;
import com.annotation.PrimaryKey;
import com.annotation.ReadOnly;
import com.annotation.TableName;

   /**
    * minitj_runtime 实体类
    * 2019-02-21 xuweihua
    */ 

@Entity
@Page
@TableName("minitj_runtime")
public class Minitj_coindata
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
	@Column(name="wx_new")
	@Comments(name="注册用户数")
	public int wx_new;
	@ReadOnly
	@Column(name="wx_active")
	@Comments(name="活跃用户数")
	public int wx_active;
	
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
/*	@Column(name="runtime_urlclick_newuser")
	@Comments(name="新用户点击次数")
	public int runtime_urlclick_newuser;
	@Column(name="runtime_urlclick_olduser")
	@Comments(name="老用户点击次数")
	public int runtime_urlclick_olduser;*/
	@Column(name="runtime_updatetime")
	@Comments(name="更新时间")
	public String runtime_updatetime;

}
