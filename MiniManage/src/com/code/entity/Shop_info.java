package com.code.entity;

import javax.persistence.Entity;

import com.annotation.Comments;
import com.annotation.Page;

@Entity
@Page
public class Shop_info {

	@Comments(name="日期")
	public int dates;
	
	@Comments(name="序号")
	public String  numbers;
	
	@Comments(name="商店被点击次数")
	public int  clickcounts;
	
	@Comments(name="商店内icon被点击次数")
	public int  iconclickcounts;
}
