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
public class Mini_community
{
	@PrimaryKey
	@Column(name="community_id")
	@Comments(name="主键id")
	public int community_id;
	
	@Column(name="community_type")
	@Comments(name="类型")
	public int community_type;
	
	@Column(name="community_gameid")
	@Comments(name="游戏id")
	public int community_gameid;
	
	@Column(name="community_status")
	@Comments(name="状态")
	public int community_status;
	
	@ReadOnly
	@Column(name="game_name")
	@Comments(name="游戏名称")
	public String game_name;
	
	@ReadOnly
	@Column(name="game_appid")
	@Comments(name="Appid")
	public String game_appid;
}
