package com.code.ui.servlet;
import java.lang.reflect.Type;
import java.util.Vector;

import javax.servlet.annotation.WebServlet;

import com.code.dao.MiniGamebackDao;
import com.code.entity.Minitj_wx;
import com.code.ui.UIMoudleServlet;
import com.google.gson.reflect.TypeToken;
@WebServlet(urlPatterns = { "/minitj_wx", "/pages/minitj_wx" })
public class MinitjwxServlet extends UIMoudleServlet {
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
			Minitj_wx model = (Minitj_wx) t;
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
			Minitj_wx model = (Minitj_wx) t;
			MiniGamebackDao.instance.saveOrUpdate(model);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Class<?> getClassInfo() {
		return MiniGamebackDao.class;
	}
	
	protected Type getTypeof() {
		// TODO Auto-generated method stub
		return new TypeToken<Vector<Minitj_wx>>() {
		}.getType();
	}

	@SuppressWarnings("unchecked")
	public Vector<Minitj_wx> findData() {
		Vector<Minitj_wx> list = (Vector<Minitj_wx>) MiniGamebackDao.instance
				.findBySQL(getSQL(),Minitj_wx.class);
		return list;
	}

}
