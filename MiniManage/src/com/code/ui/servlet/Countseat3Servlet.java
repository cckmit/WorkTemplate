package com.code.ui.servlet;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Vector;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

import com.code.dao.MiniGamebackDao;
import com.code.entity.Mini_community_3;
import com.code.entity.Minitj_wx;
import com.code.ui.UIMoudleServlet;
import com.tools.XWHMathTool;

@WebServlet(urlPatterns = { "/xx2", "/pages/xx2" })
public class Countseat3Servlet extends UIMoudleServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Object doOperator(HttpServletRequest request) {
		Vector<Mini_community_3> list = (Vector<Mini_community_3>) MiniGamebackDao.instance
				.findBySQL("select * from Mini_community_3", Mini_community_3.class);
		Map<Integer, Double> activeUpMap = Minitj_wx.getActiveUpMap();
		;
		asd(list);
		for (Mini_community_3 community_1 : list) {

			if (activeUpMap.get(community_1.game_id) == null) {
				continue;
			}
			community_1.activeUp = XWHMathTool.formatMath(activeUpMap.get(community_1.game_id), 3);
		}
		asd(list);
		int i = 0;
		for (Mini_community_3 _community : list) {
			i++;
			String updateSql = "UPDATE mini_community_3 SET pos=" + i + " WHERE game_id = " + _community.game_id;
			MiniGamebackDao.instance.execSQLCMDInfo(updateSql);
		}
		return list;
	}

	public static void asd(Vector<Mini_community_3> list) {
		Collections.sort(list, new Comparator<Mini_community_3>() {

			public int compare(Mini_community_3 o1, Mini_community_3 o2) {
				double _up1 = o1.activeUp != null ? o1.activeUp : 0;
				double _up2 = o2.activeUp != null ? o2.activeUp : 0;
				if (_up1 < _up2) {
					return 1;
				} else {
					return -1;
				}
			}
		});
	}
}
