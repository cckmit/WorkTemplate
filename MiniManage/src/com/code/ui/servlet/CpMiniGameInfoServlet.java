package com.code.ui.servlet;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Vector;

import javax.servlet.annotation.WebServlet;

import com.code.dao.MiniGamebackDao;
import com.code.entity.CpMini_GameInfo;
import com.code.entity.Mini_ad;
import com.code.ui.UIMoudleServlet;
import com.google.gson.reflect.TypeToken;

@WebServlet(urlPatterns = { "/cpmini_gameinfo", "/pages/cpmini_gameinfo" })
public class CpMiniGameInfoServlet extends UIMoudleServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 新增数据
	 * 
	 * @param <T>
	 * 
	 * @param t
	 */
	protected void newData(Object t) {
		try {
			CpMini_GameInfo model = (CpMini_GameInfo) t;
			if(model.cp_price==null){
				model.cp_price=new BigDecimal("0");
			}
			if(model.cp_proportion==null){
				model.cp_proportion=new BigDecimal("0");
			}
			MiniGamebackDao.instance.saveOrUpdate(model);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 修改数据
	 * 
	 * @param <T>
	 * 
	 * @param t
	 */
	protected void editData(Object t) {
		try {
			CpMini_GameInfo model = (CpMini_GameInfo) t;
			if(model.cp_settlement==0){
				model.cp_price= new BigDecimal("0");
			}
			if(model.cp_settlement==1){
				model.cp_proportion=new BigDecimal("0");
			}
			MiniGamebackDao.instance.saveOrUpdate(model);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	
	public Class<?> getClassInfo() {
		return CpMini_GameInfo.class;
	}
	
	protected Type getTypeof() {
		// TODO Auto-generated method stub
		return new TypeToken<Vector<CpMini_GameInfo>>() {
		}.getType();
	}

	
	
	@SuppressWarnings("unchecked")
	public Vector<CpMini_GameInfo> findData() {
		Vector<CpMini_GameInfo> list = (Vector<CpMini_GameInfo>) MiniGamebackDao.instance
				.findBySQL(getSQL(),CpMini_GameInfo.class);
		for(CpMini_GameInfo cpMini_GameInfo : list){
			
			cpMini_GameInfo.cp_proportion =cpMini_GameInfo.cp_proportion.setScale(2, RoundingMode.HALF_UP);
			if(cpMini_GameInfo.cp_proportion==null){
				cpMini_GameInfo.cp_proportion =new BigDecimal("0");
			}
			
		}
		return list;
	}
	

}
