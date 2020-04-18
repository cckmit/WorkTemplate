package com.code.ui.servlet;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import javax.servlet.annotation.WebServlet;
import com.code.dao.MiniGamebackDao;
import com.code.entity.Mini_record;
import com.code.ui.UIMoudleServlet;
import com.google.gson.reflect.TypeToken;
@WebServlet(urlPatterns = { "/mini_record", "/pages/mini_record" })
public class MinirecordServlet extends UIMoudleServlet {
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
			Mini_record model = (Mini_record) t;
			MiniGamebackDao.instance.saveOrUpdate(model);
		} catch (Exception e) {
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
			Mini_record model = (Mini_record) t;
			MiniGamebackDao.instance.saveOrUpdate(model);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public String getGivenSearch(Set<String> keySet, Map<String, String> map) {
		StringBuilder sb = new StringBuilder();
		for (String key : keySet) {
			if("gameappid".equals(key)){
				sb.append(" AND a.game_id= (SELECT game_id FROM mini_game WHERE game_appid='"+map.get(key)+"' limit 0,1)" );
			}else if("gameinitid".equals(key)){
				sb.append(" AND a.game_id= (SELECT game_id FROM mini_game WHERE game_initid='"+map.get(key)+"' limit 0,1)" );
			}else if("game_id".equals(key)){
				sb.append(" AND a.game_id="+map.get(key)+"" );
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
				"SELECT a.*,b.`game_name`,b.`game_appid`,b.`game_initid` FROM `mini_record` a "+
				"LEFT JOIN `mini_game` b ON a.`game_id`=b.`game_id` ";
		return sql;
	}
	
	/**
	 * 删除数据
	 * 
	 * @param <T>
	 * 
	 * @param parameter
	 */
	protected void deleteData(Object t) {
		Mini_record model = (Mini_record) t;
		String deleteSQL = "delete from mini_record where game_id="
				+ model.game_id;
		MiniGamebackDao.instance.execSQLCMDInfo(deleteSQL);
	}
	public Class<?> getClassInfo() {
		return Mini_record.class;
	}
	protected Type getTypeof() {
		// TODO Auto-generated method stub
		return new TypeToken<Vector<Mini_record>>() {
		}.getType();
	}
	@SuppressWarnings("unchecked")
	public Vector<Mini_record> findData() {
		Vector<Mini_record> list = (Vector<Mini_record>) MiniGamebackDao.instance
				.findBySQL(getSQL(),Mini_record.class);
		return list;
	}

}
