package com.code.entity;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import javax.persistence.Entity;
import javax.persistence.Column;
import javax.servlet.http.HttpServletRequest;

import com.annotation.PrimaryKey;
import com.annotation.Comments;
import com.annotation.Page;
import com.code.json.PermissionJson;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

   /**
    * account 实体类
    * 2017-08-09 xuweihua
    */ 

@Entity
@Page
public class Account
{
	@PrimaryKey
	@Column(name="account")
	@Comments(name="账号,长度最长32位")
	public String account;
	@Column(name="password")
	@Comments(name="密码")
	public String password;
	@Column(name="name")
	@Comments(name="名称")
	public String name;
	@Column(name="role")
	@Comments(name="角色 0-管理员 1-运营 2-财务")
	public Integer role;
	@Column(name="permiss")
	@Comments(name="权限数组")
	public String permiss;
	@Column(name="busstr")
	@Comments(name="权限业务数组")
	public String busstr;
	@Column(name="operation")
	@Comments(name="操作 0-查看 1-添加 2-修改 3-导出")
	public String operation;
	
	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	/**
	 * 获取账户对应的权限json字符串
	 * @param request
	 * @return
	 */
	public String getPerssionJson(HttpServletRequest request){
		List<PermissionJson> newPermissionJsonList = new ArrayList<>();
		if(permiss != null){
			String[] permissArr = permiss.split(",");
			Set<Integer> permissSet = new HashSet<>();
			for (String permiss : permissArr)
			{
				if(Pattern.matches("[0-9]+", permiss)){
					permissSet.add(Integer.valueOf(permiss));
				}
			}
			
			@SuppressWarnings("unchecked")
			List<PermissionJson> permissionJsonList = (List<PermissionJson>) 
				request.getServletContext().getAttribute("permissionJsonList");
			for (PermissionJson permissionJson : permissionJsonList)
			{
				if(permissSet.contains(permissionJson.getId())){
					newPermissionJsonList.add(permissionJson);
				}
			}
		}
		Type type = new TypeToken<List<PermissionJson>>(){}.getType();
		String perssionJson = new Gson().toJson(newPermissionJsonList, type);
		//System.out.println(perssionJson);
		return perssionJson;
	}
	
	public void setAccount(String account)
	{
		this.account=account;
	}

	public String getAccount()
	{
		return account;
	}

	public void setPassword(String password)
	{
		this.password=password;
	}

	public String getPassword()
	{
		return password;
	}

	public void setName(String name)
	{
		this.name=name;
	}

	public String getName()
	{
		return name;
	}

	public void setRole(Integer role)
	{
		this.role=role;
	}

	public Integer getRole()
	{
		return role;
	}

	public void setPermiss(String permiss)
	{
		this.permiss=permiss;
	}

	public String getPermiss()
	{
		return permiss;
	}

	public void setBusstr(String busstr)
	{
		this.busstr=busstr;
	}

	public String getBusstr()
	{
		return busstr;
	}

}
