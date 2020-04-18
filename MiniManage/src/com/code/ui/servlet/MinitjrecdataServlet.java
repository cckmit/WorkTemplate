package com.code.ui.servlet;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.servlet.annotation.WebServlet;

import com.code.dao.MiniGamebackDao;
import com.code.entity.Minitj_recdata;
import com.code.ui.UIMoudleServlet;
import com.google.gson.reflect.TypeToken;
import com.tools.XwhTool;
@WebServlet(urlPatterns = { "/minitj_recdata", "/pages/minitj_recdata" })
public class MinitjrecdataServlet extends UIMoudleServlet {
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
			Minitj_recdata model = (Minitj_recdata) t;
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
			Minitj_recdata model = (Minitj_recdata) t;
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
				"SELECT a.*,b.`game_name`,b.`game_appid`,c.wx_active FROM `minitj_runtime` a "+
				"LEFT JOIN `mini_game` b ON a.`runtime_gameid`=b.`game_id` "+
				"LEFT JOIN `minitj_wx` c ON b.`game_appid`=c.`wx_appid` AND a.runtime_date=c.wx_date ";
		return sql;
	}
	
	public Class<?> getClassInfo() {
		return Minitj_recdata.class;
	}
	
	protected Type getTypeof() {
		// TODO Auto-generated method stub
		return new TypeToken<Vector<Minitj_recdata>>() {
		}.getType();
	}

	@SuppressWarnings("unchecked")
	public Vector<Minitj_recdata> findData() {
		Vector<Minitj_recdata> list = (Vector<Minitj_recdata>) MiniGamebackDao.instance
				.findBySQL(getSQL(),Minitj_recdata.class);
		for (Minitj_recdata recdata : list)
		{
			if(recdata.wx_active != 0)
			{
				
				recdata.recClickRate = new BigDecimal(recdata.runtime_recuser*100).divide(new BigDecimal(recdata.wx_active),2, BigDecimal.ROUND_HALF_UP);
			}
			if(recdata.runtime_reccount != 0)
			{
				recdata.recJumpSucRate = new BigDecimal(recdata.runtime_recjump_suc*100).divide(new BigDecimal(recdata.runtime_reccount),2, BigDecimal.ROUND_HALF_UP);
			}
			if(recdata.wx_active != 0)
			{
				recdata.shopClickSucRate = new BigDecimal(recdata.runtime_shopuser*100).divide(new BigDecimal(recdata.wx_active),2, BigDecimal.ROUND_HALF_UP);
			}
		}
		Minitj_recdata.addSumLine(list);
		return list;
	}

}
