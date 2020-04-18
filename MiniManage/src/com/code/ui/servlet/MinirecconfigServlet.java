package com.code.ui.servlet;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Vector;

import javax.servlet.annotation.WebServlet;

import com.code.dao.MiniGamebackDao;
import com.code.entity.Mini_binding;
import com.code.entity.Mini_recconfig;
import com.code.ui.UIMoudleServlet;
import com.google.gson.reflect.TypeToken;

@WebServlet(urlPatterns = { "/mini_recconfig", "/pages/mini_recconfig" })
public class MinirecconfigServlet extends UIMoudleServlet
{
	private static final long serialVersionUID = 1L;

	/**
	 * 新增数据
	 * 
	 * @param <T>
	 * 
	 * @param t
	 */
	protected void newData(Object t)
	{
		try
		{
			Mini_recconfig model = (Mini_recconfig) t;

			MiniGamebackDao.instance.saveOrUpdate(model);
		}
		catch (Exception e)
		{
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
	protected void editData(Object t)
	{
		try
		{

			Mini_recconfig model = (Mini_recconfig) t;

			MiniGamebackDao.instance.saveOrUpdate(model);
		}
		catch (Exception e)
		{
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
	protected void deleteData(Object t)
	{
		Mini_recconfig model = (Mini_recconfig) t;
		String deleteSQL = "delete from mini_game where game_id="
				+ model.game_id;
		MiniGamebackDao.instance.execSQLCMDInfo(deleteSQL);
	}

	public Class<?> getClassInfo()
	{
		return Mini_recconfig.class;
	}
	
	@Override
	protected void operateMultiData(String ids, String operate)
	{
		String updateSql = null;
		if(operate != null && operate.contains("sorttype"))
		{
			updateSql = "UPDATE mini_game SET game_sorttype_rec='"+operate.replace("sorttype", "")+"' WHERE game_id IN ("+ids+")";
		}
		
		if(updateSql != null)
		{
			MiniGamebackDao.instance.execSQLCMDInfo(updateSql);
		}
	}

	protected Type getTypeof()
	{
		// TODO Auto-generated method stub
		return new TypeToken<Vector<Mini_recconfig>>()
		{
		}.getType();
	}
	
	@Override
	public String getTableName()
	{
		return "mini_game";
	}

	@SuppressWarnings("unchecked")
	public Vector<Mini_recconfig> findData()
	{
		Vector<Mini_recconfig> list = (Vector<Mini_recconfig>) MiniGamebackDao.instance
				.findBySQL(getSQL(), Mini_recconfig.class);
		
		Map<String , Integer> countMap = Mini_binding.getRecCountMap();
		for (Mini_recconfig recconfig : list)
		{
			if(countMap.get(recconfig.game_appid) != null)
			{
				recconfig.recCount = countMap.get(recconfig.game_appid);
			}
		}
		return list;
	}
	
}
