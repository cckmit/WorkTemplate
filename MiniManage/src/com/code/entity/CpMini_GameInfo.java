package com.code.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.annotation.Comments;
import com.annotation.Page;
import com.annotation.PrimaryKey;

@Entity
@Page
public class CpMini_GameInfo {

	@PrimaryKey
	@Column(name="id")
	@Comments(name="主键id")
	public int id;
	
	@Column(name="cpgame_name")
	@Comments(name="产品名称")
	public String cpgame_name;
	
	@Column(name="cp_partners")
	@Comments(name="合作方")
	public String cp_partners;
	
	@Column(name="cp_settlement")
	@Comments(name="结算方式 (CPS/CPA)")
	public int cp_settlement;
	
	
	@Column(name="cp_proportion")
	@Comments(name="分成比例")
	public BigDecimal cp_proportion;
	
	
	@Column(name="cp_price")
	@Comments(name="单价")
	public BigDecimal cp_price;
	
}
