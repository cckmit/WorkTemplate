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
    * minitj_runtime 实体类
    * 2019-02-21 xuweihua
    */ 

@Entity
@Page
@TableName("minitj_runtime")
public class Minitj_recdata
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
	@Column(name="wx_active")
	@Comments(name="活跃用户数")
	public int wx_active;
	
	public BigDecimal recClickRate;
	public BigDecimal recJumpSucRate;
	public BigDecimal shopClickSucRate;
	
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
	@Column(name="runtime_updatetime")
	@Comments(name="更新时间")
	public String runtime_updatetime;
	public static void addSumLine(Vector<Minitj_recdata> list)
	{
		if(list == null || list.size() <= 0)
		{
			return;
		}
		Minitj_recdata recdata = new Minitj_recdata();
		int runtime_visit=0,
			runtime_reccount=0,
			runtime_recjump_suc=0,
			runtime_shoprec_count=0,
			runtime_shopcount=0;
		BigDecimal	recJumpSucRate=new BigDecimal(0);
		BigDecimal shopClickSucRate=new BigDecimal(0);
		for (Minitj_recdata  minitj_recdata: list)
		{
			runtime_visit+=minitj_recdata.runtime_visit;
			runtime_reccount += minitj_recdata.runtime_reccount;
			runtime_recjump_suc += minitj_recdata.runtime_recjump_suc;
			runtime_shoprec_count += minitj_recdata.runtime_shoprec_count;
			if(minitj_recdata.recJumpSucRate==null) minitj_recdata.recJumpSucRate=new BigDecimal(0);
			recJumpSucRate=recJumpSucRate.add(minitj_recdata.recJumpSucRate);
			runtime_shopcount += minitj_recdata.runtime_shopcount;
			if(minitj_recdata.shopClickSucRate==null) minitj_recdata.shopClickSucRate=new BigDecimal(0);
			shopClickSucRate=shopClickSucRate.add(minitj_recdata.shopClickSucRate);
			/*if(minitj_recdata.videoEcpm==null) minitj_recdata.videoEcpm=0.00;*/
		}
		/*		访问次数（求和）、PUSH点击次数（求和）、
		PUSH跳转成功次数（求和）、PUSH跳转成功率（求平均值）、
		SHOP点击次数、shop点击率（求平均值）SH_icon点击次数（求和）、*/
		recdata.runtime_date = "总计";
		recdata.game_name ="-";
		recdata.runtime_visit = runtime_visit;
		recdata.runtime_reccount = runtime_reccount;
		recdata.runtime_recjump_suc = runtime_recjump_suc;
		recdata.runtime_shoprec_count = runtime_shoprec_count;
		BigDecimal size = new BigDecimal(list.size());
		recdata.recJumpSucRate = recJumpSucRate.divide(size,2, BigDecimal.ROUND_HALF_UP);
		recdata.runtime_shopcount =runtime_shopcount;
		recdata.shopClickSucRate = shopClickSucRate.divide(size,2, BigDecimal.ROUND_HALF_UP);
		list.add(recdata);
	}
}
