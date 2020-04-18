package com.code.ui.servlet;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.servlet.annotation.WebServlet;

import com.code.dao.MiniGamebackDao;
import com.code.entity.Minitj_daily;
import com.code.ui.UIMoudleServlet;
import com.google.gson.reflect.TypeToken;
@WebServlet(urlPatterns = { "/minitj_daily", "/pages/minitj_daily" })
public class MinitjdailyServlet extends UIMoudleServlet {
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
			Minitj_daily model = (Minitj_daily) t;
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
			Minitj_daily model = (Minitj_daily) t;
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
		Minitj_daily model = (Minitj_daily) t;
		String deleteSQL = "delete from minitj_daily where daily_date="
				+ model.daily_date;
		MiniGamebackDao.instance.execSQLCMDInfo(deleteSQL);
	}
	@Override
	public String getGivenSearch(Set<String> keySet, Map<String, String> map) {
		StringBuilder sb = new StringBuilder();
		for (String key : keySet) {
			if("daily_date_s".equals(key)){
				sb.append(" AND daily_date>='"+map.get(key)+"' " );
			}else
				if("daily_date_d".equals(key)){
				sb.append(" AND daily_date<='"+map.get(key)+"' " );
			}else{
				sb.append(" AND "+key+" LIKE '%" + map.get(key) +"%' ");
			}
		}
		return sb.toString();
	}
	@Override
	public String getOrderSql()
	{
		return " ORDER BY daily_date DESC ";
	}
	public Class<?> getClassInfo() {
		return Minitj_daily.class;
	}
	
	protected Type getTypeof() {
		// TODO Auto-generated method stub
		return new TypeToken<Vector<Minitj_daily>>() {
		}.getType();
	}

	@SuppressWarnings("unchecked")
	public Vector<Minitj_daily> findData() {
		Vector<Minitj_daily> list = (Vector<Minitj_daily>) MiniGamebackDao.instance
				.findBySQL(getSQL(),Minitj_daily.class);
		Minitj_daily.addSumLine(list);
		return list;
	}

}
