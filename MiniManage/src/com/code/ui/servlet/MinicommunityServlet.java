package com.code.ui.servlet;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.servlet.annotation.WebServlet;

import com.code.dao.MiniGamebackDao;
import com.code.entity.Mini_community;
import com.code.ui.UIMoudleServlet;
import com.google.gson.reflect.TypeToken;
@WebServlet(urlPatterns = { "/mini_community", "/pages/mini_community" })
public class MinicommunityServlet extends UIMoudleServlet {
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
			Mini_community model = (Mini_community) t;
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
			Mini_community model = (Mini_community) t;
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
			if("gameappid".equals(key)){
				sb.append(" AND community_gameid= (SELECT game_id FROM mini_game WHERE game_appid='"+map.get(key)+"' limit 0,1)" );
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
				"SELECT a.*,b.`game_name`,b.`game_appid` FROM `mini_community` a "+
				"LEFT JOIN `mini_game` b ON a.`community_gameid`=b.`game_id` ";
		return sql;
	}
	
	public Class<?> getClassInfo() {
		return Mini_community.class;
	}
	
	protected Type getTypeof() {
		// TODO Auto-generated method stub
		return new TypeToken<Vector<Mini_community>>() {
		}.getType();
	}

	@SuppressWarnings("unchecked")
	public Vector<Mini_community> findData() {
		Vector<Mini_community> list = (Vector<Mini_community>) MiniGamebackDao.instance
				.findBySQL(getSQL(),Mini_community.class);
		return list;
	}

}
