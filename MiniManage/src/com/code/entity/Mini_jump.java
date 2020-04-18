package com.code.entity;
import javax.persistence.Entity;
import javax.persistence.Column;

import com.annotation.PrimaryKey;
import com.annotation.Comments;
import com.annotation.Page;
import com.annotation.ReadOnly;

   /**
    * mini_jump 实体类
    * 2018-12-06 xuweihua
    */ 

@Entity
@Page
public class Mini_jump
{
	@PrimaryKey
	@Column(name="jump_id")
	@Comments(name="主键id")
	public int jump_id;
	@Column(name="jump_gameid")
	@Comments(name="游戏id")
	public int jump_gameid;
	@Column(name="jump_type1")
	@Comments(name="type1 1-内置 2-二维码")
	public Integer jump_type1;
	@Column(name="jump_ad1")
	@Comments(name="ad1 gameid")
	public String jump_ad1;
	@Column(name="jump_type2")
	@Comments(name="type2 1-内置 2-二维码")
	public Integer jump_type2;
	@Column(name="jump_ad2")
	@Comments(name="ad2 gameid")
	public String jump_ad2;
	@Column(name="jump_type3")
	@Comments(name="type3 1-内置 2-二维码")
	public Integer jump_type3;
	@Column(name="jump_ad3")
	@Comments(name="ad3 gameid")
	public String jump_ad3;
	
	@Column(name="jump_type4")
	@Comments(name="type4 1-内置 2-二维码")
	public Integer jump_type4;
	@Column(name="jump_ad4")
	@Comments(name="ad4 gameid")
	public String jump_ad4;
	
	@Column(name="jump_type5")
	@Comments(name="type5 1-内置 2-二维码")
	public Integer jump_type5;
	@Column(name="jump_ad5")
	@Comments(name="ad5 gameid")
	public String jump_ad5;
	
	@Column(name="jump_type6")
	@Comments(name="type6 1-内置 2-二维码")
	public Integer jump_type6;
	@Column(name="jump_ad6")
	@Comments(name="ad6 gameid")
	public String jump_ad6;
	
	@Column(name="jump_type7")
	@Comments(name="type7 1-内置 2-二维码")
	public Integer jump_type7;
	@Column(name="jump_ad7")
	@Comments(name="ad7 gameid")
	public String jump_ad7;
	
	@Column(name="jump_type8")
	@Comments(name="type8 1-内置 2-二维码")
	public Integer jump_type8;
	@Column(name="jump_ad8")
	@Comments(name="ad8 gameid")
	public String jump_ad8;
	
	@Column(name="jump_type9")
	@Comments(name="type9 1-内置 2-二维码")
	public Integer jump_type9;
	@Column(name="jump_ad9")
	@Comments(name="ad9 gameid")
	public String jump_ad9;
	
	@Column(name="jump_type10")
	@Comments(name="type10 1-内置 2-二维码")
	public Integer jump_type10;
	@Column(name="jump_ad10")
	@Comments(name="ad10 gameid")
	public String jump_ad10;
	

	@Column(name="game_name")
	@Comments(name="产品名称")
	@ReadOnly
	public String game_name;
	
	@Column(name="game_appid")
	@Comments(name="game_appid")
	@ReadOnly
	public String game_appid;

}
