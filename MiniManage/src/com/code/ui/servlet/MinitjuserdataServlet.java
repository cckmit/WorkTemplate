package com.code.ui.servlet;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.servlet.annotation.WebServlet;

import com.alibaba.fastjson.JSONObject;
import com.code.dao.MiniGamebackDao;
import com.code.entity.Minitj_userdata;
import com.code.ui.UIMoudleServlet;
import com.google.gson.reflect.TypeToken;
import com.tools.XwhTool;
@WebServlet(urlPatterns = { "/minitj_userdata", "/pages/minitj_userdata" })
public class MinitjuserdataServlet extends UIMoudleServlet {
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
			Minitj_userdata model = (Minitj_userdata) t;
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
			Minitj_userdata model = (Minitj_userdata) t;
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
		String date=" 1=1 ";
		if(searchdata==null||"".equals(searchdata))
		{
			date=" wx_date='"+XwhTool.getPriDateWithChar()+"'";
		}
		return date;
	}
	@Override
	public String getGivenSearch(Set<String> keySet, Map<String, String> map) {
		StringBuilder sb = new StringBuilder();
		for (String key : keySet) {
			if("wx_appid".equals(key)){
				sb.append(" AND wx_appid='"+map.get(key)+"' " );
			}else if("wx_date_s".equals(key)){
				sb.append(" AND wx_date>='"+map.get(key)+"'" );
			}else if("wx_date_e".equals(key)){
				sb.append(" AND wx_date<='"+map.get(key)+"'" );
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
				"SELECT a.*,b.`game_name`,null as wx_reg_other FROM `minitj_wx` a "+
				"LEFT JOIN `mini_game` b ON a.`wx_appid`=b.`game_appid` " ;
		return sql;
	}
	
	public Class<?> getClassInfo() {
		return Minitj_userdata.class;
	}
	
	protected Type getTypeof() {
		// TODO Auto-generated method stub
		return new TypeToken<Vector<Minitj_userdata>>() {
		}.getType();
	}

	@SuppressWarnings("unchecked")
	public Vector<Minitj_userdata> findData() {
		Vector<Minitj_userdata> list = (Vector<Minitj_userdata>) MiniGamebackDao.instance
				.findBySQL(getSQL(),Minitj_userdata.class);
		for(Minitj_userdata userdata:list)
		{
			userdata.wx_active_women=userdata.wx_active_women.setScale(0, BigDecimal.ROUND_HALF_UP);
			//System.out.println(userdata.wx_reg_json);
			try {
				JSONObject json=JSONObject.parseObject(userdata.wx_reg_json);
				userdata.wx_reg_other=json.getIntValue("其他");
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		Minitj_userdata.addSumLine(list);
		return list;
	}

}
