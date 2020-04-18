package com.code.ui.servlet;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;

import com.code.dao.MiniGamebackDao;
import com.code.entity.Mini_community_1;
import com.code.entity.Mini_community_10;
import com.code.entity.Mini_community_2;
import com.code.entity.Mini_community_3;
import com.code.entity.Mini_community_4;
import com.code.entity.Mini_community_5;
import com.code.entity.Mini_community_6;
import com.code.entity.Mini_community_7;
import com.code.entity.Mini_community_8;
import com.code.entity.Mini_community_9;
import com.code.entity.Mini_game;
import com.code.entity.Minitj_wx;
import com.code.ui.UIMoudleServlet;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tools.ReadExcel;
import com.tools.TableShow;
import com.tools.XWHMathTool;

@WebServlet(urlPatterns = { "/minicommunity8", "/pages/minicommunity8" })
public class Minicommunity8Servlet extends UIMoudleServlet{

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
			Mini_community_8 model = (Mini_community_8) t;
			String updateSql = "UPDATE mini_game SET game_sorttype_shop= '"+model.game_sorttype_shop+"' , game_spread= '"+model.game_spread+"' where game_id="+model.game_id;
			MiniGamebackDao.instance.execSQLCMDInfo(updateSql);
			model.pos = 999;
			MiniGamebackDao.instance.saveOrUpdate(model);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Class<?> getClassInfo() {
		return Mini_community_8.class;
	}

	protected Type getTypeof() {
		// TODO Auto-generated method stub
		return new TypeToken<Vector<Mini_community_8>>() {
		}.getType();
	}

	/**
	 * 删除数据
	 * 
	 * @param <T>
	 * 
	 * @param parameter
	 */
	protected void deleteData(Object t) {
		Mini_community_5 model = (Mini_community_5) t;
		String deleteSQL = "delete from Mini_community_8 where game_id=" + model.game_id;
		MiniGamebackDao.instance.execSQLCMDInfo(deleteSQL);
	}
	
	
	@Override
	public String getGivenSearch(Set<String> keySet, Map<String, String> map) {
		StringBuilder sb = new StringBuilder();
		for (String key : keySet) {
			if ("isblock_stores".equals(key)) {
				/*
				 * sb.append(" AND "+key+" LIKE '%" + map.get(key) +"%' ");
				 */ } else {
				sb.append(" AND " + key + " LIKE '%" + map.get(key) + "%' ");
			}
		}
		return sb.toString();
	}

	
	@SuppressWarnings("unchecked")
	public Vector<Mini_community_8> findData() {
		String isblock_stores = "";
		String searchdata = get("search-data");
		if (searchdata != null && searchdata.length() > 0) {
			Type type = new TypeToken<Map<String, String>>() {
			}.getType();
			Map<String, String> map = new Gson().fromJson(searchdata, type);
			if (map.containsKey("isblock_stores")) {
				isblock_stores = map.get("isblock_stores");
			}
		}
		Vector<Mini_community_8> list = (Vector<Mini_community_8>) MiniGamebackDao.instance.findBySQL(getSQL(),
				Mini_community_8.class);
		Vector<Mini_community_8> list3 = new Vector<Mini_community_8>();

		Map<Integer, Double> activeUpMap = Minitj_wx.getActiveUpMap();
		for (Mini_community_8 community_3 : list) {
			Vector<Mini_community_2> liststore2 = (Vector<Mini_community_2>) MiniGamebackDao.instance.findBySQL(
					"select *from mini_community_2 where game_id=" + community_3.game_id, Mini_community_2.class);
			Vector<Mini_community_1> liststore1 = (Vector<Mini_community_1>) MiniGamebackDao.instance.findBySQL(
					"select *from mini_community_1 where game_id=" + community_3.game_id, Mini_community_1.class);
			Vector<Mini_community_3> liststore3 = (Vector<Mini_community_3>) MiniGamebackDao.instance.findBySQL(
					"select *from mini_community_3 where game_id=" + community_3.game_id, Mini_community_3.class);
			Vector<Mini_community_4> liststore4 = (Vector<Mini_community_4>) MiniGamebackDao.instance.findBySQL(
					"select *from mini_community_4 where game_id=" + community_3.game_id, Mini_community_4.class);
			Vector<Mini_community_5> liststore5 = (Vector<Mini_community_5>) MiniGamebackDao.instance.findBySQL(
					"select *from mini_community_5 where game_id=" + community_3.game_id, Mini_community_5.class);
			Vector<Mini_community_6> liststore6 = (Vector<Mini_community_6>) MiniGamebackDao.instance.findBySQL(
					"select *from mini_community_6 where game_id=" + community_3.game_id, Mini_community_6.class);
			Vector<Mini_community_7> liststore7 = (Vector<Mini_community_7>) MiniGamebackDao.instance.findBySQL(
					"select *from mini_community_7 where game_id=" + community_3.game_id, Mini_community_7.class);
			Vector<Mini_community_9> liststore9 = (Vector<Mini_community_9>) MiniGamebackDao.instance.findBySQL(
					"select *from mini_community_9 where game_id=" + community_3.game_id, Mini_community_9.class);
			Vector<Mini_community_10> liststore10 = (Vector<Mini_community_10>) MiniGamebackDao.instance.findBySQL(
					"select *from mini_community_10 where game_id=" + community_3.game_id, Mini_community_10.class);
			if (community_3.pos == 0) {
				community_3.pos = 999;
			}
			Vector<Mini_game> listgameinfo = (Vector<Mini_game>) MiniGamebackDao.instance
					.findBySQL("select*from mini_game where game_id=" + community_3.game_id, Mini_game.class);
			for (Mini_game mini_game : listgameinfo) {
				community_3.game_name = mini_game.game_name;
				community_3.game_spread = mini_game.game_spread;
				community_3.game_sorttype_shop = mini_game.game_sorttype_shop;
				community_3.appid = mini_game.game_appid;
				String nameString = "";
				if (liststore1 != null && liststore1.size() > 0) {
					nameString += "1";
				}
				if (liststore2 != null && liststore2.size() > 0) {
					if (nameString.isEmpty()) {
						nameString += "2";
					} else {
						nameString += "#2";
					}
				}
				if (liststore3 != null && liststore3.size() > 0) {
					if (nameString.isEmpty()) {
						nameString += "3";
					} else {
						nameString += "#3";
					}
				}
				if (liststore4 != null && liststore4.size() > 0) {
					if (nameString.isEmpty()) {
						nameString += "4";
					} else {
						nameString += "#4";
					}
				}
				if (liststore5 != null && liststore5.size() > 0) {
					if (nameString.isEmpty()) {
						nameString += "5";
					} else {
						nameString += "#5";
					}
				}
				if (liststore6 != null && liststore6.size() > 0) {
					if (nameString.isEmpty()) {
						nameString += "6";
					} else {
						nameString += "#6";
					}
				}
				if (liststore7 != null && liststore7.size() > 0) {
					if (nameString.isEmpty()) {
						nameString += "7";
					} else {
						nameString += "#7";
					}
				}
				if (liststore9 != null && liststore9.size() > 0) {
					if (nameString.isEmpty()) {
						nameString += "9";
					} else {
						nameString += "#9";
					}
				}
				if (liststore10 != null && liststore10.size() > 0) {
					if (nameString.isEmpty()) {
						nameString += "10";
					} else {
						nameString += "#10";
					}
				}
				if (!nameString.isEmpty()) {
					community_3.isblock_stores = nameString;
				}
				if (activeUpMap.get(community_3.game_id) == null) {
					community_3.activeUp = 0.0;
					continue;
				}
				community_3.activeUp = XWHMathTool.formatMath(activeUpMap.get(community_3.game_id), 3);
				// 计算活跃up
			}
			if (!isblock_stores.isEmpty() && !isblock_stores.equals("3")
					&& (community_3.isblock_stores == null || !community_3.isblock_stores.contains(isblock_stores))) {
				continue;
			}
			list3.add(community_3);
		}
		asd(list3);
		return list3;
	}

	public static void asd(Vector<Mini_community_8> list) {
		Collections.sort(list, new Comparator<Mini_community_8>() {
			/*
			 * int compare(Student o1, Student o2) 返回一个基本类型的整型， 返回负数表示：o1 小于o2，
			 * 返回0 表示：o1和o2相等， 返回正数表示：o1大于o2。
			 */
			public int compare(Mini_community_8 o1, Mini_community_8 o2) {
				if (o1.pos > o2.pos) {
					return 1;
				}
				if (o1.pos == o2.pos) {
					return 0;
				}
				return -1;
			}
		});
	}

	@Override
	public String doSpecJson(HttpServletResponse response) {
		if ("modifySort".equals(get("spec_type"))) {
			// int targetGameId = Integer.parseInt(get("targetGameId"));
			int targetGamePos = Integer.parseInt(get("targetGamePos"));
			int sourceGameId = Integer.parseInt(get("sourceGameId"));
			int sourceGamePos = Integer.parseInt(get("sourceGamePos"));

			// 更新至目标行位置
			String updateSql = "UPDATE mini_community_8 SET pos=" + targetGamePos + " WHERE game_id = " + sourceGameId;
			MiniGamebackDao.instance.execSQLCMDInfo(updateSql);

			// String point = get("point");
			String point = sourceGamePos > targetGamePos ? "top" : "bottom";
			if ("top".equals(point)) {
				updateSql = "UPDATE mini_community_8 SET pos=pos+1 " + "WHERE game_id <> " + sourceGameId
						+ " AND pos>= " + targetGamePos + " AND pos<=" + sourceGamePos;
				MiniGamebackDao.instance.execSQLCMDInfo(updateSql);
			} else if ("bottom".equals(point)) {
				updateSql = "UPDATE mini_community_8 SET pos=pos-1 " + "WHERE game_id <> " + sourceGameId
						+ " AND pos<= " + targetGamePos + " AND pos>=" + sourceGamePos;
				MiniGamebackDao.instance.execSQLCMDInfo(updateSql);
			}
		}
		return "-1";
	}

	/**
	 * 导入数据
	 *
	 * @param <T>
	 * 
	 * @param t
	 */
	@Override
	protected void otherPost(HttpServletRequest request, HttpServletResponse response) {
		List<FileItem> formItems = getFileItemList();
		File uploadFile = saveFile(formItems);
		ReadExcel readExcel = new ReadExcel(uploadFile);
		TableShow tables = readExcel.getTables();
		OutputStream os = null;
		try {
			os = response.getOutputStream();
			Vector<Vector<Object>> vector = tables.getTds();
			System.out.println("开始处理导入数据:" + System.currentTimeMillis());
			// 写入数据库
			String result = Mini_community_8.importFromExcel(vector);
			os.write(result.getBytes("utf-8"));
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
