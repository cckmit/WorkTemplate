package com.code.ui.servlet;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;

import com.code.dao.MiniGamebackDao;
import com.code.entity.Mini_binding;
import com.code.entity.Mini_game;
import com.code.json.GameJson;
import com.code.ui.UIMoudleServlet;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tools.XwhTool;
@WebServlet(urlPatterns = { "/mini_binding", "/pages/mini_binding" })
public class MinibindingServlet extends UIMoudleServlet {
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
			Mini_binding model = (Mini_binding) t;
			dealBinding(model);
			MiniGamebackDao.instance.saveOrUpdate(model);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void dealBinding(Mini_binding model)
	{
		GameJson gameJson = new Gson().fromJson(model.binding_json, GameJson.class);
		List<String> appids = gameJson.getNavigateToMiniProgramAppIdList();
		if(appids != null && appids.size() >= 1)
			model.binding_con1 = appids.get(0);
		if(appids != null && appids.size() >= 2)
			model.binding_con2 = appids.get(1);
		if(appids != null && appids.size() >= 3)
			model.binding_con3 = appids.get(2);
		if(appids != null && appids.size() >= 4)
			model.binding_con4 = appids.get(3);
		if(appids != null && appids.size() >= 5)
			model.binding_con5 = appids.get(4);
		if(appids != null && appids.size() >= 6)
			model.binding_con6 = appids.get(5);
		if(appids != null && appids.size() >= 7)
			model.binding_con7 = appids.get(6);
		if(appids != null && appids.size() >= 8)
			model.binding_con8 = appids.get(7);
		if(appids != null && appids.size() >= 9)
			model.binding_con9 = appids.get(8);
		if(appids != null && appids.size() >= 10)
			model.binding_con10 = appids.get(9);
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
			Mini_binding model = (Mini_binding) t;
			dealBinding(model);
			MiniGamebackDao.instance.saveOrUpdate(model);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Class<?> getClassInfo() {
		return Mini_binding.class;
	}
	
	protected Type getTypeof() {
		// TODO Auto-generated method stub
		return new TypeToken<Vector<Mini_binding>>() {
		}.getType();
	}
	
	@Override
	public String getGivenSearch(Set<String> keySet, Map<String, String> map) {
		StringBuilder sb = new StringBuilder();
		for (String key : keySet) {
			if("gameappid".equals(key)){
				sb.append(" AND binding_gameid= (SELECT game_id FROM mini_game WHERE game_appid='"+map.get(key)+"' limit 0,1)" );
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
				"SELECT a.*,b.`game_name`,b.`game_appid` FROM `mini_binding` a "+
				"LEFT JOIN `mini_game` b ON a.`binding_gameid`=b.`game_id` ";
		return sql;
	}
	
	@Override
	public String doSpecJson(HttpServletResponse response)
	{
		if ("getBindingInfo".equals(get("spec_type")))
		{
			List<SimpleBinding> bindingList = new ArrayList<>();
			Vector<Mini_binding> list = findData();
			if(list == null || list.size() == 0)
			{
				return "-1";
			}
			Mini_binding mini_binding = null;
			for (Mini_binding binding : list)
			{
				if((binding.binding_gameid+"").equals(get("binding_gameid")))
				{
					mini_binding = binding;
					break;
				}
			}
			if(mini_binding == null)
			{
				return "-1";
			}
			bindingList.add(new SimpleBinding(mini_binding.binding_con1, mini_binding.binding_name_con1));
			bindingList.add(new SimpleBinding(mini_binding.binding_con2, mini_binding.binding_name_con2));
			bindingList.add(new SimpleBinding(mini_binding.binding_con3, mini_binding.binding_name_con3));
			bindingList.add(new SimpleBinding(mini_binding.binding_con4, mini_binding.binding_name_con4));
			bindingList.add(new SimpleBinding(mini_binding.binding_con5, mini_binding.binding_name_con5));
			bindingList.add(new SimpleBinding(mini_binding.binding_con6, mini_binding.binding_name_con6));
			bindingList.add(new SimpleBinding(mini_binding.binding_con7, mini_binding.binding_name_con7));
			bindingList.add(new SimpleBinding(mini_binding.binding_con8, mini_binding.binding_name_con8));
			bindingList.add(new SimpleBinding(mini_binding.binding_con9, mini_binding.binding_name_con9));
			bindingList.add(new SimpleBinding(mini_binding.binding_con10, mini_binding.binding_name_con10));
			
			return XwhTool.getGsonValue(bindingList);
		}
		return "-1";
	}
	
	class SimpleBinding
	{
		public String appid;
		public String name;
		public SimpleBinding(String appid, String name)
		{
			this.appid = appid;
			this.name = name;
		}
		
	}

	@SuppressWarnings("unchecked")
	public Vector<Mini_binding> findData() {
		Vector<Mini_binding> list = (Vector<Mini_binding>) MiniGamebackDao.instance
				.findBySQL(getSQL(),Mini_binding.class);
		Map<String, Mini_game> map = Mini_game.getAppid2Game();
		for (Mini_binding binding : list)
		{
			binding.binding_name_con1 = parseGameName(map, binding.binding_con1);
			binding.binding_name_con2 = parseGameName(map, binding.binding_con2);
			binding.binding_name_con3 = parseGameName(map, binding.binding_con3);
			binding.binding_name_con4 = parseGameName(map, binding.binding_con4);
			binding.binding_name_con5 = parseGameName(map, binding.binding_con5);
			binding.binding_name_con6 = parseGameName(map, binding.binding_con6);
			binding.binding_name_con7 = parseGameName(map, binding.binding_con7);
			binding.binding_name_con8 = parseGameName(map, binding.binding_con8);
			binding.binding_name_con9 = parseGameName(map, binding.binding_con9);
			binding.binding_name_con10 = parseGameName(map, binding.binding_con10);
		}
		return list;
	}

	private String parseGameName(Map<String, Mini_game> map, String appid)
	{
		if(appid == null)
		{
			return "-";
		}
		String name = "不在列表内";
		Mini_game mini_game = map.get(appid);
		if(mini_game != null)
		{
			name = mini_game.game_name;
		}
		return name;
	}
}
