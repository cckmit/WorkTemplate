package com.code.ui.servlet;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.servlet.annotation.WebServlet;

import com.code.dao.MiniGamebackDao;
import com.code.entity.Mini_fromapp;
import com.code.ui.UIMoudleServlet;
import com.google.gson.reflect.TypeToken;
@WebServlet(urlPatterns = { "/mini_fromapp", "/pages/mini_fromapp" })
public class MinifromappServlet extends UIMoudleServlet {
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
			Mini_fromapp model = (Mini_fromapp) t;
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
			Mini_fromapp model = (Mini_fromapp) t;
			MiniGamebackDao.instance.saveOrUpdate(model);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 删除数据
	 * 
	 * @param <T>
	 * 
	 * @param parameter
	 */
	protected void deleteData(Object t) {
		Mini_fromapp model = (Mini_fromapp) t;
		String deleteSQL = "delete from mini_fromapp where fromapp_date="
				+ model.fromapp_date;
		MiniGamebackDao.instance.execSQLCMDInfo(deleteSQL);
	}
	@Override
	public String getGivenSearch(Set<String> keySet, Map<String, String> map) {
		StringBuilder sb = new StringBuilder();
		for (String key : keySet) {
			if("fromapp_date_s".equals(key)){
				sb.append(" AND fromapp_date>='"+map.get(key)+"'" );
			}else if("fromapp_date_e".equals(key)){
				sb.append(" AND fromapp_date<='"+map.get(key)+"'" );
			}else{
				sb.append(" AND "+key+" LIKE '%" + map.get(key) +"%' ");
			}
		}
		return sb.toString();
	}
	public Class<?> getClassInfo() {
		return Mini_fromapp.class;
	}
	
	protected Type getTypeof() {
		// TODO Auto-generated method stub
		return new TypeToken<Vector<Mini_fromapp>>() {
		}.getType();
	}
	@SuppressWarnings("unchecked")
	public Vector<Mini_fromapp> findData() {
		Vector<Mini_fromapp> list = (Vector<Mini_fromapp>) MiniGamebackDao.instance
				.findBySQL(getSQL(),Mini_fromapp.class);
		return list;
	}
}
