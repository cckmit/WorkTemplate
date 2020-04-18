package com.code.entity;

import javax.persistence.Entity;

import com.annotation.Comments;
import com.annotation.Page;
@Entity
@Page
public class Get_Yourlike {

	@Comments(name="日期")
	public int dates;
	
	@Comments(name="产品名称")
	public String  game_name;
	
	@Comments(name="拥有推荐位")
	public int  seatcount;
	
	@Comments(name="icon被点击次数")
	public int  clickcounts;
	
	@Comments(name="跳转成功次数")
	public int  jumpclickcounts;
	
	
}
