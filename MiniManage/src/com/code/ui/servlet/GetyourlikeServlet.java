package com.code.ui.servlet;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.annotation.WebServlet;

import com.alibaba.fastjson.JSONObject;
import com.code.dao.MiniGamebackDao;
import com.code.dao.MiniPackageDao;
import com.code.entity.DCEventItem;
import com.code.entity.DCGameEvent;
import com.code.entity.Game_package_info;
import com.code.entity.Get_Yourlike;
import com.code.entity.Mini_binding;
import com.code.entity.Mini_game;
import com.code.ui.UIMoudleServlet;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tools.JsonTool;

@WebServlet(urlPatterns = { "/getyourlikeservlet", "/pages/getyourlikeservlet" })
public class GetyourlikeServlet extends UIMoudleServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Class<?> getClassInfo() {
		return Game_package_info.class;
	}

	protected Type getTypeof() {
		// TODO Auto-generated method stub
		return new TypeToken<Vector<Game_package_info>>() {
		}.getType();
	}
	@SuppressWarnings("unchecked")
	public Vector<Get_Yourlike> findData() {

		Date nowtime = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		int date = Integer.parseInt(sdf.format(nowtime));
		String game_name = null;
		String searchdata = get("search-data");
		if (searchdata != null && searchdata.length() > 0) {
			Type type = new TypeToken<Map<String, String>>() {
			}.getType();
			Map<String, String> map = new Gson().fromJson(searchdata, type);
			if (map.containsKey("dates")) {
				date = Integer.parseInt(map.get("dates"));
			}
			if (map.containsKey("game_name")) {
				game_name = map.get("game_name");
			}
		}
		Vector<Mini_binding> listappid = (Vector<Mini_binding>) MiniGamebackDao.instance
				.findBySQL("SELECT m.*,'' as game_name,'' as game_appid  FROM mini_binding m WHERE binding_json IS NOT NULL",Mini_binding.class);
		Map<String, String> gameMap = new HashMap<String, String>();
		Vector<Mini_game> listgamename = (Vector<Mini_game>) MiniGamebackDao.instance
				.findBySQL("select * from mini_game", Mini_game.class);
		for (Mini_game game : listgamename) {
			gameMap.put(game.game_appid, game.game_name);
		}
		Map<String, Get_Yourlike> map = new HashMap<String, Get_Yourlike>();
		Vector<Game_package_info> list = (Vector<Game_package_info>) MiniPackageDao.instance
				.findBySQL("select * from game_package_info where date=" + date, Game_package_info.class);
		for (Game_package_info package_info : list) {
			DCGameEvent eventIDItem = JsonTool.toObject(package_info.event_info, DCGameEvent.class);
			ConcurrentHashMap<Integer, DCEventItem> blockCacheNode = eventIDItem.eventInfo;
			for (Map.Entry<Integer, DCEventItem> entry : blockCacheNode.entrySet()) {
				int _openId = entry.getKey();
				DCEventItem value = entry.getValue();
				ConcurrentHashMap<String, Integer> blockCacheNodes = value.paramsInfo;
				for (Entry<String, Integer> entry1 : blockCacheNodes.entrySet()) {
					if (_openId == 11 || _openId == 12) {
						continue;
					}
					String keyString = package_info.date + "_" + entry1.getKey();
					if (game_name != null && !game_name.equals(gameMap.get(entry1.getKey()))) {
						continue;
					}
					if (map.containsKey(keyString)) {
						Get_Yourlike managers = map.get(keyString);
						if (_openId == 8) {
							managers.clickcounts += entry1.getValue();
						}
						if (_openId == 9) {
							managers.jumpclickcounts += entry1.getValue();
						}
					} else {
						Get_Yourlike managers = new Get_Yourlike();
						int num = 0;
						for(Mini_binding binding : listappid){
							String  ni  = binding.binding_json.toString();
							JSONObject jsonStr = JSONObject.parseObject(ni);
						    List<String> sd	= (List<String>) jsonStr.get("navigateToMiniProgramAppIdList");
							if(sd.contains(entry1.getKey()))
							{
	                             num++;
							}
						}
						managers.dates = package_info.date;
						managers.game_name = gameMap.get(entry1.getKey());
						managers.seatcount = num;
						if (_openId == 8) {
							managers.clickcounts += entry1.getValue();
						}
						if (_openId == 9) {
							managers.jumpclickcounts += entry1.getValue();
						}
						map.put(keyString, managers);

					}
				}
			}
		}
		Vector<Get_Yourlike> vector = new Vector<Get_Yourlike>();
		if (map != null && map.size() > 0) {
			for (String manlist : map.keySet()) {
				vector.add(map.get(manlist));
			}
		}
		return vector;
	}

}
