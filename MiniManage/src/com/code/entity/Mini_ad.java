package com.code.entity;
import javax.persistence.Column;
import javax.persistence.Entity;

import com.annotation.Comments;
import com.annotation.Page;
import com.annotation.PrimaryKey;

   /**
    * mini_check 实体类
    * 2018-12-06 xuweihua
    */ 

@Entity
@Page
public class Mini_ad
{
	@PrimaryKey
	@Column(name="ad_id")
	@Comments(name="主键id")
	public int ad_id;
	
	@Column(name="ad_name")
	@Comments(name="广告名称")
	public String ad_name;
	
	@Column(name="ad_type")
	@Comments(name="广告类型")
	public int ad_type;
	
	@Column(name="ad_attr")
	@Comments(name="广告属性")
	public String ad_attr;
	
}
