package com.code.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import com.annotation.Comments;
import com.annotation.Page;
import com.annotation.PrimaryKey;

   /**
    * Minitj_wxsub 实体类
    * 2019-02-21 xuweihua
    */ 

@Entity
@Page
public class Minitj_wxsub
{
	@PrimaryKey
	@Column(name="wxsub_id")
	@Comments(name="主键id")
	public int wxsub_id;
	@Column(name="wxsub_name")
	@Comments(name="公众号名称")
	public String wxsub_name;
	@Column(name="wxsub_company")
	@Comments(name="公众号对应公司")
	public String wxsub_company;
	@Column(name="wxsub_admin")
	@Comments(name="公众号管理员")
	public String wxsub_admin;
	@Column(name="wxsub_initialid")
	@Comments(name="初始ID")
	public String wxsub_initialid;
	@Column(name="wxsub_account")
	@Comments(name="公众号账号")
	public String wxsub_account;
	@Column(name="wxsub_pass")
	@Comments(name="公众号密码")
	public String wxsub_pass;
	@Column(name="wxsub_bindingope")
	@Comments(name="绑定运营人员")
	public String wxsub_bindingope;
	@Column(name="wxsub_cpmark")
	@Comments(name="合作方备注")
	public String wxsub_cpmark;
	@Column(name="wxsub_bindgameid1")
	@Comments(name="绑定产品A的id")
	public int wxsub_bindgameid1;
	@Column(name="wxsub_bindgameid2")
	@Comments(name="绑定产品B的id")
	public int wxsub_bindgameid2;
	@Column(name="wxsub_bindgameid3")
	@Comments(name="绑定产品C的id")
	public int wxsub_bindgameid3;
	@Column(name="wxsub_ctime")
	@Comments(name="创建时间")
	public String wxsub_ctime;
	
	public String wxsub_bindgameid1Name;
	public String wxsub_bindgameid2Name;
	public String wxsub_bindgameid3Name;
	public String getWxsub_bindgameid1Name() {
		return wxsub_bindgameid1Name;
	}
	public void setWxsub_bindgameid1Name(String wxsub_bindgameid1Name) {
		this.wxsub_bindgameid1Name = wxsub_bindgameid1Name;
	}
	public String getWxsub_bindgameid2Name() {
		return wxsub_bindgameid2Name;
	}
	public void setWxsub_bindgameid2Name(String wxsub_bindgameid2Name) {
		this.wxsub_bindgameid2Name = wxsub_bindgameid2Name;
	}
	public String getWxsub_bindgameid3Name() {
		return wxsub_bindgameid3Name;
	}
	public void setWxsub_bindgameid3Name(String wxsub_bindgameid3Name) {
		this.wxsub_bindgameid3Name = wxsub_bindgameid3Name;
	}

}
