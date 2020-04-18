package com.code.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import com.annotation.Comments;

   /**
    * Notice_state 实体类
    * 2019-02-21 xuweihua
    */ 

@Entity
public class Notice_state
{
	@Column(name="notice_url")
	@Comments(name="url")
	public String notice_url;
	@Column(name="notice_name")
	@Comments(name="状态")
	public String notice_name;
	@Column(name="notice_time")
	@Comments(name="更新时间")
	public String notice_time;
}
