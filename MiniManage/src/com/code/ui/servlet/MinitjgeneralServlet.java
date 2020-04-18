package com.code.ui.servlet;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import javax.servlet.annotation.WebServlet;
import com.code.dao.MiniGamebackDao;
import com.code.entity.Minitj_general;
import com.code.ui.UIMoudleServlet;
import com.google.gson.reflect.TypeToken;
import com.tools.XwhTool;
@WebServlet(urlPatterns = { "/minitj_general", "/pages/minitj_general" })
public class MinitjgeneralServlet extends UIMoudleServlet {
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
			Minitj_general model = (Minitj_general) t;
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
			Minitj_general model = (Minitj_general) t;
			MiniGamebackDao.instance.saveOrUpdate(model);
		} catch (Exception e) {
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
			if("gameappid".equals(key)){
				sb.append(" AND wx_appid= (SELECT game_appid FROM mini_game WHERE game_appid='"+map.get(key)+"' limit 0,1)" );
			}else if("wx_date_s".equals(key)){
				sb.append(" AND wx_date>='"+map.get(key)+"'" );
			}else if("wx_date_e".equals(key)){
				sb.append(" AND wx_date<='"+map.get(key)+"'" );
			}
			else if("game_spread".equals(key)){
				sb.append("AND wx_appid in (SELECT game_appid FROM mini_game WHERE game_spread='"+map.get(key)+"')" );
			}
			else{
				sb.append(" AND "+key+" LIKE '%" + map.get(key) +"%' ");
			}
		}
		return sb.toString();
	}
	
	@Override
	protected String getSelectData()
	{
		String sql = 
				"SELECT a.*,b.`game_name`,c.manual_outgo FROM `minitj_wx` a "+
				"LEFT JOIN `mini_game` b ON a.`wx_appid`=b.`game_appid` " +
				"LEFT JOIN `minitj_manual` c ON b.`game_id`=c.`manual_gameid` AND a.wx_date=c.manual_date ";
		return sql;
	}
	
	public Class<?> getClassInfo() {
		return MiniGamebackDao.class;
	}
	
	protected Type getTypeof() {
		// TODO Auto-generated method stub
		return new TypeToken<Vector<Minitj_general>>() {
		}.getType();
	}

	@SuppressWarnings("unchecked")
	public Vector<Minitj_general> findData() {
		Vector<Minitj_general> list = (Vector<Minitj_general>) MiniGamebackDao.instance
				.findBySQL(getSQL(),Minitj_general.class);
		for (Minitj_general general : list)
		{
		/*	if(general.wx_banner_income.compareTo(BigDecimal.ZERO)!=0)
			{*/
				general.totalIncome = general.wx_video_income.add(general.wx_banner_income);
		//	}
			if(general.wx_active != 0)
			{
				if(general.totalIncome==null)
				{
					general.totalIncome=new BigDecimal(0);
				}
				general.activeUp = general.totalIncome.divide(new BigDecimal(general.wx_active),3, BigDecimal.ROUND_HALF_UP);
			}
			if(general.wx_reg_ad != 0)
			{
				if(general.manual_outgo==null)
				{
					general.manual_outgo=new BigDecimal("0.00");
				}
				general.unit_price = general.manual_outgo.divide(new BigDecimal(general.wx_reg_ad),2, BigDecimal.ROUND_HALF_UP);
			}
		}
		Minitj_general.addSumLine(list);
		return list;
	}

}
