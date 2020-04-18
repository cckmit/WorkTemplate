package com.code.ui.servlet;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.servlet.annotation.WebServlet;

import com.code.dao.MiniGamebackDao;
import com.code.entity.Minitj_pulldata;
import com.code.ui.UIMoudleServlet;
import com.google.gson.reflect.TypeToken;
import com.tools.XwhTool;
@WebServlet(urlPatterns = { "/minitj_pulldata", "/pages/minitj_pulldata" })
public class MinitjpulldataServlet extends UIMoudleServlet {
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
			Minitj_pulldata model = (Minitj_pulldata) t;
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
			Minitj_pulldata model = (Minitj_pulldata) t;
			MiniGamebackDao.instance.saveOrUpdate(model);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public String getInitWhere()
	{
		String searchdata = get("search-data");
		String date=" a.runtime_gameid!=0 ";
		if(searchdata==null||"".equals(searchdata))
		{
			date+=" and runtime_date='"+XwhTool.getPriDateWithChar()+"'";
		}
		return date;
	}
	@Override
	public String getGivenSearch(Set<String> keySet, Map<String, String> map) {
		StringBuilder sb = new StringBuilder();
		for (String key : keySet) {
			if("runtime_gameid".equals(key)){
				sb.append(" AND runtime_gameid="+map.get(key)+" " );
			}else if("runtime_date_s".equals(key)){
				sb.append(" AND runtime_date>='"+map.get(key)+"'" );
			}else if("runtime_date_e".equals(key)){
				sb.append(" AND runtime_date<='"+map.get(key)+"'" );
			}else{
				sb.append(" AND "+key+" LIKE '%" + map.get(key) +"%' ");
			}
		}
		return sb.toString();
	}
	
	@Override
	protected String getSelectData()
	{
		String sql = 
				"SELECT a.*,b.`game_name` FROM `minitj_runtime` a "+
				"LEFT JOIN `mini_game` b ON a.`runtime_gameid`=b.`game_id` ";
		return sql;
	}
	
	public Class<?> getClassInfo() {
		return Minitj_pulldata.class;
	}
	
	protected Type getTypeof() {
		// TODO Auto-generated method stub
		return new TypeToken<Vector<Minitj_pulldata>>() {
		}.getType();
	}

	@SuppressWarnings("unchecked")
	public Vector<Minitj_pulldata> findData() {
		Vector<Minitj_pulldata> list = (Vector<Minitj_pulldata>) MiniGamebackDao.instance
				.findBySQL(getSQL(),Minitj_pulldata.class);
		Minitj_pulldata.addSumLine(list);
		return list;
	}

}
