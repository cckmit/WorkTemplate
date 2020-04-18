package com.code.ui.servlet;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.servlet.annotation.WebServlet;

import com.code.dao.MiniGamebackDao;
import com.code.entity.Mini_jump;
import com.code.ui.UIMoudleServlet;
import com.google.gson.reflect.TypeToken;
@WebServlet(urlPatterns = { "/mini_jump", "/pages/mini_jump" })
public class MinijumpServlet extends UIMoudleServlet {
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
			Mini_jump model = (Mini_jump) t;
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
			Mini_jump model = (Mini_jump) t;
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
		Mini_jump model = (Mini_jump) t;
		String deleteSQL = "delete from mini_jump where jump_id="
				+ model.jump_id;
		MiniGamebackDao.instance.execSQLCMDInfo(deleteSQL);
	}
	
	public Class<?> getClassInfo() {
		return Mini_jump.class;
	}
	
	@Override
	public String getInitWhere()
	{
		return " b.game_plattype=1 ";//微信
	}
	
	@Override
	public String getGivenSearch(Set<String> keySet, Map<String, String> map) {
		StringBuilder sb = new StringBuilder();
		for (String key : keySet) {
			if("gameappid".equals(key)){
				sb.append(" AND jump_gameid= (SELECT game_id FROM mini_game WHERE game_appid='"+map.get(key)+"' limit 0,1)" );
			}else{
				sb.append(" AND "+key+" LIKE '%" + map.get(key) +"%' ");
			}
		}
		return sb.toString();
	}
	
	protected Type getTypeof() {
		return new TypeToken<Vector<Mini_jump>>() {
		}.getType();
	}

	@Override
	protected String getSelectData()
	{
		String sql = 
				"SELECT a.*,b.`game_name`,b.`game_appid` FROM `mini_jump` a "+
				"LEFT JOIN `mini_game` b ON a.`jump_gameid`=b.`game_id` ";
		return sql;
	}
	
	@SuppressWarnings("unchecked")
	public Vector<Mini_jump> findData() {
		Vector<Mini_jump> list = (Vector<Mini_jump>) MiniGamebackDao.instance
				.findBySQL(getSQL(),Mini_jump.class);
		return list;
	}

}
