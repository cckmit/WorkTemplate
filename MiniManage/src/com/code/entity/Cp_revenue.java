package com.code.entity;

import java.math.BigDecimal;
import java.util.Vector;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.annotation.Comments;
import com.annotation.Page;
import com.annotation.PrimaryKey;
import com.code.dao.MiniGamebackDao;

@Entity
@Page
public class Cp_revenue {
	
	@PrimaryKey
	@Column(name="id")
	@Comments(name="主键id")
	public int id;
	
	@Column(name="cp_date")
	@Comments(name="日期")
	public String cp_date;
	
	@Column(name="cp_partners")
	@Comments(name="合作方")
	public String cp_partners;
	
	
	@Column(name="cp_gamename")
	@Comments(name="产品名称")
	public String cp_gamename;
	
	
	@Column(name="cp_settlement")
	@Comments(name="结算方式")
	public int cp_settlement;
	
	
	@Column(name="cp_proportion")
	@Comments(name="分成比例")
	public BigDecimal cp_proportion;
	
	
	@Column(name="cp_price")
	@Comments(name="单价")
	public BigDecimal cp_price;
	
	
	@Column(name="cp_recharge")
	@Comments(name="充值")
	public BigDecimal cp_recharge;
	
	
	@Column(name="cp_activation")
	@Comments(name="激活")
	public int cp_activation;
	
	//收益
	@Comments(name="收益")
	public BigDecimal cp_product;
	
	
	/**
	 * Excel导入计费点信息
	 * @param vector
	 * @return
	 */
	public static String importFromExcel(Vector<Vector<Object>> vector)
	{
		StringBuilder result = new StringBuilder();
		boolean isValid = true;
		Vector<Cp_revenue> importList = new Vector<>();
		
		
		for (Vector<Object> lineVector : vector)
		{
			if(lineVector.size() == 0)
			{
				continue;
			}
			
			if(lineVector.size() != 8)
			{
				result.append("本行数据格式非法:"+lineVector.toString()+"，导入失败！");
				isValid = false;
				continue;
			}
			if(lineVector.get(0) == null || lineVector.get(1) == null 
					|| lineVector.get(2) == null || lineVector.get(3) == null ||
					lineVector.get(4) == null|| lineVector.get(5) == null||
					lineVector.get(6) == null|| lineVector.get(7) == null)  
			{
				continue;
			}
			String cp_date = ((String) lineVector.get(0)).trim();
			String cp_gamename = ((String) lineVector.get(1)).trim();
			String cp_partners = ((String) lineVector.get(2)).trim();
			String cp_settlement = ((String) lineVector.get(3)).trim();
			String cp_proportion = ((String) lineVector.get(4)).trim();
			String cp_price = ((String) lineVector.get(5)).trim();
			String cp_recharge = ((String) lineVector.get(6)).trim();
			String cp_activation = ((String) lineVector.get(7)).trim();

			 
			
		
			Cp_revenue manual = new Cp_revenue();
			
			manual.cp_date = cp_date;
			manual.cp_gamename = cp_gamename;
			manual.cp_partners = cp_partners;
			manual.cp_settlement = Integer.parseInt(cp_settlement);
			manual.cp_proportion = new BigDecimal(cp_proportion);
			manual.cp_price =new BigDecimal(cp_price);
			manual.cp_recharge = new BigDecimal(cp_recharge);
			manual.cp_activation =Integer.parseInt(cp_activation);
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
