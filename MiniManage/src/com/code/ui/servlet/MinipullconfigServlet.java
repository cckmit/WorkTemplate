package com.code.ui.servlet;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.servlet.annotation.WebServlet;

import com.code.dao.MiniGamebackDao;
import com.code.entity.Mini_pullconfig;
import com.code.ui.UIMoudleServlet;
import com.google.gson.reflect.TypeToken;

@WebServlet(urlPatterns = { "/mini_pullconfig", "/pages/mini_pullconfig" })
public class MinipullconfigServlet extends UIMoudleServlet
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
			Mini_pullconfig model = (Mini_pullconfig) t;

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

			Mini_pullconfig model = (Mini_pullconfig) t;

			MiniGamebackDao.instance.saveOrUpdate(model);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	
	@Override
	public String getGivenSearch(Set<String> keySet, Map<String, String> map) {
		StringBuilder sb = new StringBuilder();
		for (String key : keySet) {
			if("game_id".equals(key)){
				sb.append(" AND game_id= "+map.get(key)+" " );
			}else{
				sb.append(" AND "+key+" LIKE '%" + map.get(key) +"%' ");
			}
		}
		return sb.toString();
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
		Mini_pullconfig model = (Mini_pullconfig) t;
		String deleteSQL = "delete from mini_game where game_id="
				+ model.game_id;
		MiniGamebackDao.instance.execSQLCMDInfo(deleteSQL);
	}

	public Class<?> getClassInfo()
	{
		return Mini_pullconfig.class;
	}
	
	protected Type getTypeof()
	{
		// TODO Auto-generated method stub
		return new TypeToken<Vector<Mini_pullconfig>>()
		{
		}.getType();
	}
	@Override
	protected void operateMultiData(String ids, String operate)
	{
		String updateSql = null;
		if("wheelOpen".equals(operate))
		{
			updateSql = "UPDATE mini_game SET game_adstate=1 WHERE game_id IN ("+ids+")";
		}
		else if("wheelClose".equals(operate))
		{
			updateSql = "UPDATE mini_game SET game_adstate=2 WHERE game_id IN ("+ids+")";
		}
		else if(operate != null && operate.contains("wheel_count"))
		{
			updateSql = "UPDATE mini_game SET game_playrate="+operate.replace("wheel_count", "")+" WHERE game_id IN ("+ids+")";
		}
		if(updateSql != null)
		{
			MiniGamebackDao.instance.execSQLCMDInfo(updateSql);
		}
	}
	@Override
	public String getTableName()
	{
		return "mini_game";
	}

	@SuppressWarnings("unchecked")
	public Vector<Mini_pullconfig> findData()
	{
		Vector<Mini_pullconfig> list = (Vector<Mini_pullconfig>) MiniGamebackDao.instance
				.findBySQL(getSQL(), Mini_pullconfig.class);
		return list;
	}
	
}
