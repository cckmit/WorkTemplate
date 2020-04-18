package com.code.entity;
import java.util.Map;
import java.util.Vector;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.annotation.Comments;
import com.annotation.Page;
import com.annotation.PrimaryKey;
import com.annotation.ReadOnly;
import com.code.dao.MiniGamebackDao;

   /**
    * minitj_manual 实体类
    * 2019-02-21 xuweihua
    */ 

@Entity
@Page
public class Minitj_manual
{
	@PrimaryKey
	@Column(name="manual_gameid")
	@Comments(name="游戏id")
	public int manual_gameid;

	@ReadOnly
	@Column(name="game_name")
	@Comments(name="游戏名称")
	public String game_name;
	@PrimaryKey
	@Column(name="manual_date")
	@Comments(name="日期")
	public String manual_date;
	@Column(name="manual_outgo")
	@Comments(name="买量支出")
	public Double manual_outgo;
	@Column(name="manual_click")
	@Comments(name="点击次数")
	public int manual_click;

	public Double clickPrice;
	
	/**
	 * Excel导入计费点信息
	 * @param vector
	 * @return
	 */
	public static String importFromExcel(Vector<Vector<Object>> vector)
	{
		StringBuilder result = new StringBuilder();
		boolean isValid = true;
		Vector<Minitj_manual> importList = new Vector<>();
		
		Map<String, Mini_game> appid2Game = Mini_game.getAppid2Game();
		
		for (Vector<Object> lineVector : vector)
		{
			if(lineVector.size() == 0)
			{
				continue;
			}
			
			if(lineVector.size() != 4)
			{
				result.append("本行数据格式非法:"+lineVector.toString()+"，导入失败！");
				isValid = false;
				continue;
			}
			if(lineVector.get(0) == null || lineVector.get(1) == null 
					|| lineVector.get(2) == null || lineVector.get(3) == null)
			{
				continue;
			}
			String date = ((String) lineVector.get(0)).trim();
			String appid = ((String) lineVector.get(1)).trim();
			String outgo = ((String) lineVector.get(2)).trim();
			String click = ((String) lineVector.get(3)).trim();
			
			if(appid2Game.get(appid) == null)
			{
				result.append("未找到此appid:"+appid+"，导入失败！");
				isValid = false;
				continue;
			}
			
			Minitj_manual manual = new Minitj_manual();
			manual.manual_date = date;
			manual.manual_gameid = appid2Game.get(appid).game_id;
			manual.manual_outgo = Double.parseDouble(outgo);
			manual.manual_click = Integer.parseInt(click);
			
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
