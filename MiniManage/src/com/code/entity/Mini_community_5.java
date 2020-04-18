package com.code.entity;

import java.util.Map;
import java.util.Vector;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.annotation.Comments;
import com.annotation.Page;
import com.annotation.PrimaryKey;
import com.code.dao.MiniGamebackDao;

@Entity
@Page
public class Mini_community_5 {
	@PrimaryKey
	@Column(name = "game_id")
	@Comments(name = "game_id")
	public int game_id;

	@Column(name = "pos")
	@Comments(name = "位置")
	public int pos;

	// 产品名称
	@Comments(name = "产品名称")
	public String game_name;
	// 活跃up
	@Comments(name = "活跃up")
	public Double activeUp;
	// 推广名称
	@Comments(name = "推广名称")
	public String game_spread;
	// appid
	@Comments(name = "appid")
	public String appid;
	// 推荐名称
	@Comments(name = "推荐名称")
	public String game_sorttype_shop;
	// 是否在其他商店显示
	@Comments(name = "所在商店组")
	public String isblock_stores;

	/**
	 * Excel导入计费点信息
	 * 
	 * @param vector
	 * @return
	 */
	public static String importFromExcel(Vector<Vector<Object>> vector) {
		String deletesql = "delete from mini_community_5";
		MiniGamebackDao.instance.execSQLCMDInfo(deletesql);
		StringBuilder result = new StringBuilder();
		boolean isValid = true;
		Vector<Mini_community_5> importList = new Vector<>();
		Map<String, Mini_game> appid2Game = Mini_game.getAppid2Game();

		for (Vector<Object> lineVector : vector) {
			if (lineVector.size() == 0) {
				continue;
			}

			if (lineVector.size() != 3) {
				result.append("本行数据格式非法:" + lineVector.toString() + "，导入失败！");
				isValid = false;
				continue;
			}
			if (lineVector.get(0) == null || lineVector.get(1) == null) {
				continue;
			}
			String pos = ((String) lineVector.get(0)).trim();
			String appid = ((String) lineVector.get(1)).trim();

			if (appid2Game.get(appid) == null) {
				result.append("未找到此appid:" + appid + "，导入失败！");
				isValid = false;
				continue;
			}

			Mini_community_5 manual = new Mini_community_5();
			manual.pos = Integer.parseInt(pos);
			manual.game_id = appid2Game.get(appid).game_id;
			importList.add(manual);
		}

		if (!isValid) {
			return result.toString();
		}

		try {
			MiniGamebackDao.instance.saveOrUpdate(importList);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "1";
	}
}
