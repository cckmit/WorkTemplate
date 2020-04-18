package com.code.ui.servlet;

import java.util.Vector;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

import com.code.dao.MiniGamebackDao;
import com.code.entity.Mini_game;
import com.code.ui.UIMoudleServlet;
@WebServlet(urlPatterns = { "/MinigameTestingServlet", "/pages/MinigameTestingServlet" })
public class MinigameTestingServlet extends UIMoudleServlet
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5334870730755275707L;

	/**
	 * 进行操作数据
	 * 
	 * @param content
	 */
	public Object doOperator(HttpServletRequest request)
	{
		String game_name=request.getParameter("game_name");
		String game_name_y=request.getParameter("game_name_y");
		String game_appid=request.getParameter("game_appid");
		String game_appid_y=request.getParameter("game_appid_y");
		if(game_name!=null&&!"".equals(game_name)
				&&!game_name.equals(game_name_y))
		{
			Vector<Mini_game> list = (Vector<Mini_game>) MiniGamebackDao.instance
					.findBySQL("select * from  mini_game where game_name='"+game_name+"'",Mini_game.class);
			if(list.size()>0)
			{
				return "产品名称不能重复！";
			}
		}
		if(game_appid!=null&&!"".equals(game_appid)
				&&!game_appid.equals(game_appid_y))
		{
			Vector<Mini_game>	list = (Vector<Mini_game>) MiniGamebackDao.instance
					.findBySQL("select * from  mini_game where game_appid='"+game_appid+"'",Mini_game.class);
			if(list.size()>0)
			{
				return "产品APPID不能重复！";
			}
		}
		return null;
	}
}
