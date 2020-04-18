package com.code.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.annotation.Comments;
import com.annotation.Page;
import com.annotation.PrimaryKey;

@Entity
@Page
public class Mini_fixed_old_community {
	@PrimaryKey
	@Column(name="appid")
	@Comments(name="appid")
	public String appid;
	
	@Column(name="name")
	@Comments(name="游戏名")
	public String name;
	
	@Comments(name="排序位置")
	public int game_pos;
	
	@Comments(name="商店排序类型 A B C")
	public String game_sorttype_shop;
	
	public Double activeUp;
	
	
	@Comments(name="主键id")
	public int game_id;
	

}
