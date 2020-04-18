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
public class Mini_record
{
	@PrimaryKey
	@Column(name="game_id")
	@Comments(name="游戏id")
	public int game_id;
	
	@ReadOnly
	@Column(name="game_name")
	@Comments(name="游戏名称")
	public String game_name;
	@ReadOnly
	@Column(name="game_appid")
	@Comments(name="Appid")
	public String game_appid;
	@ReadOnly
	@Column(name="game_initid")
	@Comments(name="初始ID")
	public String game_initid;

	
	@Column(name="video_id1")
	@Comments(name="")
	public String video_id1;
	@Column(name="bannel_id1")
	@Comments(name="")
	public String bannel_id1;
	@Column(name="video_id2")
	@Comments(name="")
	public String video_id2;
	@Column(name="bannel_id2")
	@Comments(name="")
	public String bannel_id2;
	@Column(name="date")
	@Comments(name="操作时间")
	public String date;	
}
