package com.code.entity;

import java.util.Vector;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.annotation.Comments;
import com.annotation.Page;
import com.annotation.PrimaryKey;
import com.annotation.TableName;
import com.code.dao.MiniGamebackDao;

@Entity
@Page
@TableName("mini_game")
public class Minigamegroup {
	@PrimaryKey
	@Column(name="game_id")
	@Comments(name="主键id")
	public int game_id;
	
	@Column(name="game_name")
	@Comments(name="产品名称")
	public String game_name;
	
	@Column(name="game_spread")
	@Comments(name="推广名称")
	public String game_spread;
	
	@Column(name="game_appid")
	@Comments(name="Appid")
	public String game_appid;
	
	@Column(name="show_shop_id")
	@Comments(name="商品id")
	public Integer show_shop_id;
	
	@Column(name="game_md5")
	@Comments(name="MD5")
	public String game_md5;
	
	public static String importFromExcel(Vector<Vector<Object>> vector)
	{
		StringBuilder result = new StringBuilder();
		boolean isValid = true;
		Vector<Minigamegroup> importList = new Vector<>();
		for (Vector<Object> lineVector : vector)
		{
			if(lineVector.size() == 0)
			{
				continue;
			}
			if(lineVector.size() != 5)
			{
				result.append("本行数据格式非法:"+lineVector.toString()+"，导入失败！");
				isValid = false;
				continue;
			}
			if(lineVector.get(0) == null || lineVector.get(1) == null 
					|| lineVector.get(2) == null || lineVector.get(3) == null ||
					lineVector.get(4) == null)
			{
				continue;
			}
			String game_name = ((String) lineVector.get(0)).trim();
			String game_spread = ((String) lineVector.get(1)).trim();
			String appid = ((String) lineVector.get(2)).trim();
			String  shopid  = ((String) lineVector.get(3)).trim();
			String game_md5 = ((String) lineVector.get(4)).trim();
			Minigamegroup manual = new Minigamegroup();
			manual.game_name = game_name;
			manual.game_spread = game_spread;
			manual.game_appid = appid;
			if(shopid.equals("")||shopid.equals(null)){
			 manual.show_shop_id = 0;
			}
			else{
			manual.show_shop_id = Integer.parseInt(shopid);
			}
			manual.game_md5 = game_md5;
			importList.add(manual);
		}
		if(!isValid)
		{
			return result.toString();
		}
		
		try {
			MiniGamebackDao.instance.saveOrUpdate(importList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "1";
	}
	
	
}
