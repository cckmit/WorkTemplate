package com.code.ui.servlet;

import java.lang.reflect.Type;
import java.util.Vector;

import javax.servlet.annotation.WebServlet;

import com.code.dao.MiniGamebackDao;
import com.code.entity.Mini_community_info;
import com.code.ui.UIMoudleServlet;
import com.google.gson.reflect.TypeToken;

@WebServlet(urlPatterns = { "/minicommunityinfoservlet", "/pages/minicommunityinfoservlet" })
public class MinicommunityinfoServlet extends UIMoudleServlet{

	/**
	 * 
	 */
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
			Mini_community_info model = (Mini_community_info) t;
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
			Mini_community_info model = (Mini_community_info) t;
			MiniGamebackDao.instance.saveOrUpdate(model);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Class<?> getClassInfo() {
		return Mini_community_info.class;
	}
	
	protected Type getTypeof() {
		// TODO Auto-generated method stub
		return new TypeToken<Vector<Mini_community_info>>() {
		}.getType();
	}

	@SuppressWarnings("unchecked")
	public Vector<Mini_community_info> findData() {
		Vector<Mini_community_info> list = (Vector<Mini_community_info>) MiniGamebackDao.instance
				.findBySQL(getSQL(),Mini_community_info.class);
		return list;
	}


}
