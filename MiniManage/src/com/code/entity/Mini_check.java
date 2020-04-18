package com.code.entity;
import javax.persistence.Entity;
import javax.persistence.Column;

import com.annotation.PrimaryKey;
import com.annotation.Comments;
import com.annotation.Page;
import com.annotation.ReadOnly;

   /**
    * mini_check 实体类
    * 2018-12-06 xuweihua
    */ 

@Entity
@Page
public class Mini_check
{
	@PrimaryKey
	@Column(name="check_id")
	@Comments(name="主键id")
	public int check_id;
	@Column(name="check_gameid")
	@Comments(name="游戏id")
	public int check_gameid;
	
	@ReadOnly
	@Column(name="game_name")
	@Comments(name="游戏名称")
	public String game_name;
	@ReadOnly
	@Column(name="game_appid")
	@Comments(name="Appid")
	public String game_appid;
	@ReadOnly
	@Column(name="game_ispublish")
	@Comments(name="是否发布")
	public int game_ispublish;
	@ReadOnly
	@Column(name="game_iswheel")
	@Comments(name="是否打开大转盘")
	public int game_iswheel;
	@ReadOnly
	@Column(name="game_wheel_count")
	@Comments(name="大转盘次数")
	public Integer game_wheel_count;
	
	@Column(name="check_ver")
	@Comments(name="版本号")
	public String check_ver;
	@Column(name="check_state")
	@Comments(name="状态 1-游戏 2-壳子")
	public int check_state;

	@Column(name="check_forceshell")
	@Comments(name="是否强制为壳子 1-是 2-否")
	public int check_forceshell;
	@Column(name="open_recommend")
	@Comments(name="是否推荐0-关闭 1-开启")
	public int open_recommend;
	@Column(name="ad_cheat")
	@Comments(name="广告作弊1-开启 0-关闭")
	public int ad_cheat;
	
	
	@Comments(name="0-双平台 1-仅ios 2-仅安卓")
	public int plat_recommend;
	
	
	/*@Column(name="check_adstate")
	@Comments(name="广告模式状态")
	public int check_adstate;
	@Column(name="check_playrate")
	@Comments(name="视频播放占比")
	public Double check_playrate;
	@Column(name="check_pullappids")
	@Comments(name="拉起appid")
	public String check_pullappids;*/
}
