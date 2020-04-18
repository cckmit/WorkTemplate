package com.code.ui.servlet;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import javax.servlet.annotation.WebServlet;
import com.code.dao.MiniGamebackDao;
import com.code.entity.Minitj_runtime;
import com.code.ui.UIMoudleServlet;
import com.google.gson.reflect.TypeToken;
import com.tools.XwhTool;
@WebServlet(urlPatterns = { "/minitj_runtime", "/pages/minitj_runtime" })
public class MinitjruntimeServlet extends UIMoudleServlet {
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
			Minitj_runtime model = (Minitj_runtime) t;
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
			Minitj_runtime model = (Minitj_runtime) t;
			MiniGamebackDao.instance.saveOrUpdate(model);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public String getGivenSearch(Set<String> keySet, Map<String, String> map) {
		StringBuilder sb = new StringBuilder();
		for (String key : keySet) {
			if("runtime_gameid".equals(key)){
				sb.append(" AND runtime_gameid="+map.get(key)+" " );
			}else{
				sb.append(" AND "+key+" LIKE '%" + map.get(key) +"%' ");
			}
		}
		return sb.toString();
	}
	@Override
	public String getInitWhere()
	{
		String searchdata = get("search-data");
		String date=" runtime_gameid!=0 ";
		if(searchdata==null||"".equals(searchdata))
		{
			date+=" and runtime_date='"+XwhTool.getPriDateWithCharS()+"'";
		}
		return date;
	}
	@Override
	protected String getSelectData()
	{
		String sql = 
				"SELECT a.*,b.`game_name`,b.`game_appid` FROM `minitj_runtime` a "+
				"LEFT JOIN `mini_game` b ON a.`runtime_gameid`=b.`game_id` ";
		return sql;
	}
	
	public Class<?> getClassInfo() {
		return Minitj_runtime.class;
	}
	
	protected Type getTypeof() {
		// TODO Auto-generated method stub
		return new TypeToken<Vector<Minitj_runtime>>() {
		}.getType();
	}

	@SuppressWarnings("unchecked")
	public Vector<Minitj_runtime> findData() {
		Vector<Minitj_runtime> list = (Vector<Minitj_runtime>) MiniGamebackDao.instance
				.findBySQL(getSQL(),Minitj_runtime.class);
		Minitj_runtime.addSumLine(list);
		return list;
	}

}
