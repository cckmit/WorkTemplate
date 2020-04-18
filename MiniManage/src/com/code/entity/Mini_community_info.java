package com.code.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.annotation.Comments;
import com.annotation.Page;
import com.annotation.PrimaryKey;

/**
 * mini_community_info 实体类
 * 2018-12-06 wangkaizhou
 */ 

@Entity
@Page
public class Mini_community_info {
	@PrimaryKey
	@Column(name="community_name")
	@Comments(name="商店名称")
	public String community_name;
	
	@Column(name="zip_md5")
	@Comments(name="md5")
	public String zip_md5;
	
	
}
