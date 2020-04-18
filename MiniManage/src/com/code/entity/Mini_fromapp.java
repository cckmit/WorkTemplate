package com.code.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import com.annotation.Comments;
import com.annotation.Page;
import com.annotation.PrimaryKey;

   /**
    * Mini_fromapp 实体类
    * 2019-02-21 xuweihua
    */ 

@Entity
@Page
public class Mini_fromapp
{
	@PrimaryKey
	@Column(name="fromapp_date")
	@Comments(name="主键，日期(yyyyMMdd)")
	public int fromapp_date;
	@Column(name="fromapp_videototal")
	@Comments(name="视频总任务量")
	public int fromapp_videototal;
	@Column(name="fromapp_click")
	@Comments(name="视频点击率")
	public Double fromapp_click;
	@Column(name="fromapp_limited")
	@Comments(name="单次视频展示上限")
	public int fromapp_limited;
	@Column(name="fromapp_displayinterval")
	@Comments(name="单次视频展示间隔(秒)")
	public int fromapp_displayinterval;
	@Column(name="fromapp_clickInterval")
	@Comments(name="单次视频点击间隔(秒)")
	public int fromapp_clickInterval;
	@Column(name="fromapp_bannertotal")
	@Comments(name="banner总任务量")
	public int fromapp_bannertotal;
	@Column(name="fromapp_proportion")
	@Comments(name="视频占比")
	public Double fromapp_proportion;
}
