package com.code.entity;
import javax.persistence.Column;
import javax.persistence.Entity;

import com.annotation.Comments;
import com.annotation.Page;
import com.annotation.PrimaryKey;
import com.annotation.TableName;

   /**
    * mini_game 实体类
    * 2018-12-06 xuweihua
    */ 

@Entity
@Page
@TableName("mini_game")
public class Mini_pullconfig
{
	@PrimaryKey
	@Column(name="game_id")
	@Comments(name="主键id")
	public int game_id;
	@Column(name="game_name")
	@Comments(name="产品名称")
	public String game_name;
	@Column(name="game_appid")
	@Comments(name="Appid")
	public String game_appid;
	@Column(name="game_sorttype_rec")
	@Comments(name="推荐位排序类型 A B C")
	public String game_sorttype_rec;
	
	@Column(name="game_adstate")
	@Comments(name="广告模式状态")
	public int game_adstate;
	@Column(name="game_playrate")
	@Comments(name="视频播放占比")
	public Double game_playrate;
	@Column(name="game_pullappids")
	@Comments(name="拉起appid")
	public String game_pullappids;
}
