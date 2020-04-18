package com.code.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.annotation.Comments;
import com.annotation.Page;
import com.annotation.PrimaryKey;

@Entity
@Page
public class Game_package_info {
	@PrimaryKey
	@Column(name="game_id")
	@Comments(name="游戏ID")
	public int game_id;
	@PrimaryKey
	@Column(name="date")
	@Comments(name="日期")
	public int date;
	
	@Column(name="event_info")
	@Comments(name="事件信息")
	public String event_info;
	
}
