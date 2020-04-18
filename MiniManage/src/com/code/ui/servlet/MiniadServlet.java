package com.code.ui.servlet;
import java.lang.reflect.Type;
import java.util.Vector;

import javax.servlet.annotation.WebServlet;

import com.code.dao.MiniGamebackDao;
import com.code.entity.Mini_ad;
import com.code.ui.UIMoudleServlet;
import com.google.gson.reflect.TypeToken;
@WebServlet(urlPatterns = { "/mini_ad", "/pages/mini_ad" })
public class MiniadServlet extends UIMoudleServlet {
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
			Mini_ad model = (Mini_ad) t;
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
			Mini_ad model = (Mini_ad) t;
			MiniGamebackDao.instance.saveOrUpdate(model);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
//	
//	@Override
//	protected String getSelectData()
//	{
//		String sql = 
//				"SELECT a.*,b.`game_name`,b.`game_appid` FROM `mini_community` a "+
//				"LEFT JOIN `mini_game` b ON a.`community_gameid`=b.`game_id` ";
//		return sql;
//	}
	
	public Class<?> getClassInfo() {
		return Mini_ad.class;
	}
	
	protected Type getTypeof() {
		// TODO Auto-generated method stub
		return new TypeToken<Vector<Mini_ad>>() {
		}.getType();
	}

	@SuppressWarnings("unchecked")
	public Vector<Mini_ad> findData() {
		Vector<Mini_ad> list = (Vector<Mini_ad>) MiniGamebackDao.instance
				.findBySQL(getSQL(),Mini_ad.class);
		return list;
	}

}
