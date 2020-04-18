package com.code.ui.servlet;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.code.dao.MiniGamebackDao;
import com.code.entity.Mini_fixed_old_community;
import com.code.entity.Mini_game;
import com.code.entity.Mini_shopconfig;
import com.code.entity.Minitj_wx;
import com.code.ui.UIMoudleServlet;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tools.XWHMathTool;

@WebServlet(urlPatterns = { "/mini_fixed_old_community", "/pages/mini_fixed_old_community" })
public class MinishopconfigServlet extends UIMoudleServlet
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
			Mini_shopconfig model = (Mini_shopconfig) t;

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
			Mini_fixed_old_community model = (Mini_fixed_old_community) t;
            System.out.println(model.name);
            String updateSql = "UPDATE mini_game SET game_name='"+model.name+"' , game_sorttype_shop = '"+model.game_sorttype_shop+"' WHERE game_appid = '" + model.appid+"'";
			MiniGamebackDao.instance.execSQLCMDInfo(updateSql);
			MiniGamebackDao.instance.saveOrUpdate(model);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/*@Override
	public String getInitWhere()
	{
		return " game_ispublish=1 ";
	}
	*/
	@Override
	public String getGivenSearch(Set<String> keySet, Map<String, String> map) {
		StringBuilder sb = new StringBuilder();
		for (String key : keySet) {
			if("appid".equals(key)){
				sb.append(" AND appid= '"+map.get(key)+"'" );
			}else{
				sb.append(" AND "+key+" LIKE '%" + map.get(key) +"%' ");
			}
		}
		return sb.toString();
	}
	
/*	*//**
	 * 删除数据
	 * 
	 * @param <T>
	 * 
	 * @param parameter
	 *//*
	protected void deleteData(Object t)
	{
		Mini_shopconfig model = (Mini_shopconfig) t;
		String deleteSQL = "delete from mini_game where game_id="
				+ model.game_id;
		MiniGamebackDao.instance.execSQLCMDInfo(deleteSQL);
	}
*/

	
/*	@Override
	public String getOrderSql()
	{
		return " ORDER BY game_pos ASC ";
	}*/
	
	@Override
	public String doSpecJson(HttpServletResponse response)
	{
		if ("modifySort".equals(get("spec_type")))
		{
//			int targetGameId = Integer.parseInt(get("targetGameId"));
			int targetGamePos = Integer.parseInt(get("targetGamePos"));
			int sourceGameId = Integer.parseInt(get("sourceGameId"));
			int sourceGamePos = Integer.parseInt(get("sourceGamePos"));
			
			// 更新至目标行位置
			String updateSql = "UPDATE mini_game SET game_pos=" + targetGamePos + " WHERE game_id = " +  sourceGameId;
			MiniGamebackDao.instance.execSQLCMDInfo(updateSql);
			
//			String point = get("point");
			String point = sourceGamePos > targetGamePos ? "top" : "bottom";
			if("top".equals(point))
			{
				updateSql = "UPDATE mini_game SET game_pos=game_pos+1 "
						+ "WHERE game_id <> " +  sourceGameId + " AND game_pos>= "+targetGamePos+" AND game_pos<="+sourceGamePos;
				MiniGamebackDao.instance.execSQLCMDInfo(updateSql);
			}
			else if("bottom".equals(point))
			{
				updateSql = "UPDATE mini_game SET game_pos=game_pos-1 "
						+ "WHERE game_id <> " +  sourceGameId + " AND game_pos<= "+targetGamePos+" AND game_pos>="+sourceGamePos;
				MiniGamebackDao.instance.execSQLCMDInfo(updateSql);
			}
		}
		return "-1";
	}
	
	public Class<?> getClassInfo()
	{
		return Mini_fixed_old_community.class;
	}
	
	protected Type getTypeof()
	{
		// TODO Auto-generated method stub
		return new TypeToken<Vector<Mini_fixed_old_community>>()
		{
		}.getType();
	}
	
	/*@Override
	public String getTableName()
	{
		return "mini_game";
	}*/

	@SuppressWarnings("unchecked")
	public Vector<Mini_fixed_old_community> findData()
	{

		Vector<Mini_fixed_old_community> list1 = (Vector<Mini_fixed_old_community>) MiniGamebackDao.instance
				.findBySQL(getSQL(), Mini_fixed_old_community.class);
		Map<Integer, Double> activeUpMap = Minitj_wx.getActiveUpMap();
		//Vector<Mini_shopconfig> list = (Vector<Mini_shopconfig>) MiniGamebackDao.instance.findBySQL(getSQL(), Mini_shopconfig.class);
				for(Mini_fixed_old_community community1: list1){
					Vector<Mini_game> list2 = (Vector<Mini_game>) MiniGamebackDao.instance
							.findBySQL("select * from mini_game where game_appid='"+community1.appid+"'", Mini_game.class);
					for(Mini_game game :list2){
						community1.game_pos = game.game_pos;
                    if(community1.game_pos==0){
                    	community1.game_pos=999;
                    }
                    community1.name = game.game_name;
                    community1.game_sorttype_shop = game.game_sorttype_shop;
                    community1.game_id = game.game_id;
                    community1.appid = game.game_appid;
                    if(activeUpMap.get(community1.game_id) == null)
        			{
        				continue;
        			}
                    community1.activeUp = XWHMathTool.formatMath(activeUpMap.get(community1.game_id), 3);
					}
				}
	/*	for (Mini_shopconfig shopconfig : list)
		{
			if(shopconfig.game_pos==0)
			{
				shopconfig.game_pos=999;
			}
			if(activeUpMap.get(shopconfig.game_id) == null)
			{
				continue;
			}
			shopconfig.activeUp = XWHMathTool.formatMath(activeUpMap.get(shopconfig.game_id), 3);
		}*/
		asd(list1);
		return list1;
	}
	public static void asd(Vector<Mini_fixed_old_community> list)
	{
		Collections.sort(list, new Comparator<Mini_fixed_old_community>(){
		             /*
			          * int compare(Student o1, Student o2) 返回一个基本类型的整型，
			          * 返回负数表示：o1 小于o2，
			          * 返回0 表示：o1和o2相等，
			          * 返回正数表示：o1大于o2。
			          */
		public int compare(Mini_fixed_old_community o1, Mini_fixed_old_community o2) {
			    if(o1.game_pos > o2.game_pos){
			          return 1;
			      }
			     if(o1.game_pos == o2.game_pos){
			         return 0;
			     }
			         return -1;
			    }
		}); 
	}

}
