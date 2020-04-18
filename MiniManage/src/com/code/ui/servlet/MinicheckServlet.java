package com.code.ui.servlet;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.servlet.annotation.WebServlet;

import com.code.dao.MiniGamebackDao;
import com.code.entity.Mini_check;
import com.code.entity.Mini_game;
import com.code.ui.UIMoudleServlet;
import com.google.gson.reflect.TypeToken;
@WebServlet(urlPatterns = { "/mini_check", "/pages/mini_check" })
public class MinicheckServlet extends UIMoudleServlet {
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
			Mini_check model = (Mini_check) t;
			model.open_recommend=1;
			model.ad_cheat=1;
			MiniGamebackDao.instance.saveOrUpdate(model);
			
			updateGame(model);
		} catch (Exception e) {
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
			Mini_check model = (Mini_check) t;
			String updateSql = "UPDATE mini_game SET plat_recommend="+model.plat_recommend+" where game_id="+model.check_gameid;
			MiniGamebackDao.instance.execSQLCMDInfo(updateSql);
			MiniGamebackDao.instance.saveOrUpdate(model);
			updateGame(model);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void updateGame(Mini_check model)
	{
		try
		{
			Mini_game game = MiniGamebackDao.instance.findById(Mini_game.class, "mini_game", new String[]{"game_id", model.check_gameid + ""});
			if(game != null)
			{
				game.game_ispublish = model.game_ispublish;
				game.game_iswheel = model.game_iswheel;
				game.game_wheel_count = model.game_wheel_count;
				if(model.game_ispublish==1)
				{
					if(game.game_sorttype_rec==null||
							!game.game_sorttype_rec.equals("A"))
					{
						game.game_sorttype_rec="B";
					}
					if(game.game_sorttype_shop==null||
							!game.game_sorttype_shop.equals("A"))
					{
						game.game_sorttype_shop="B";
					}
				}
				MiniGamebackDao.instance.saveOrUpdate(game);
			}
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
			if("gameappid".equals(key)){
				sb.append(" AND check_gameid= (SELECT game_id FROM mini_game WHERE game_appid='"+map.get(key)+"' limit 0,1)" );
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
				"SELECT a.*,b.`game_name`,b.`game_appid`,b.`game_ispublish`,b.`game_iswheel`,b.`game_wheel_count` FROM `mini_check` a "+
				"LEFT JOIN `mini_game` b ON a.`check_gameid`=b.`game_id` ";
		return sql;
	}
	
	/**
	 * 删除数据
	 * 
	 * @param <T>
	 * 
	 * @param parameter
	 */
	protected void deleteData(Object t) {
		Mini_check model = (Mini_check) t;
		String deleteSQL = "delete from mini_check where check_id="
				+ model.check_id;
		MiniGamebackDao.instance.execSQLCMDInfo(deleteSQL);
	}
	
	@Override
	protected void operateMultiData(String ids, String operate)
	{
		String updateSql = null;
		if("wheelOpen".equals(operate))
		{
			updateSql = "UPDATE mini_game SET game_iswheel=1 WHERE game_id IN ("+ids+")";
		}
		else if("wheelClose".equals(operate))
		{
			updateSql = "UPDATE mini_game SET game_iswheel=2 WHERE game_id IN ("+ids+")";
		}
		else if("shellOpen".equals(operate))
		{
			updateSql = "UPDATE mini_check SET check_forceshell=1 WHERE check_gameid IN ("+ids+")";
		}
		else if("shellClose".equals(operate))
		{
			updateSql = "UPDATE mini_check SET check_forceshell=2 WHERE check_gameid IN ("+ids+")";
		}
		else if("recommendOpen".equals(operate))
		{
			updateSql = "UPDATE mini_check SET open_recommend=1 WHERE check_gameid IN ("+ids+")";
		}
		else if("recommendClose".equals(operate))
		{
			updateSql = "UPDATE mini_check SET open_recommend=0 WHERE check_gameid IN ("+ids+")";
		}
		else if("cheatOpen".equals(operate))
		{
			updateSql = "UPDATE mini_check SET ad_cheat=1 WHERE check_gameid IN ("+ids+")";
		}
		else if("cheatClose".equals(operate))
		{
			updateSql = "UPDATE mini_check SET ad_cheat=0 WHERE check_gameid IN ("+ids+")";
		}
		else if(operate != null && operate.contains("wheel_count"))
		{
			updateSql = "UPDATE mini_game SET game_wheel_count="+operate.replace("wheel_count", "")+" WHERE game_id IN ("+ids+")";
		}
		if(updateSql != null)
		{
			MiniGamebackDao.instance.execSQLCMDInfo(updateSql);
		}
	}
	
	public Class<?> getClassInfo() {
		return Mini_check.class;
	}
	
	protected Type getTypeof() {
		// TODO Auto-generated method stub
		return new TypeToken<Vector<Mini_check>>() {
		}.getType();
	}

	@SuppressWarnings("unchecked")
	public Vector<Mini_check> findData() {
		Vector<Mini_check> list = (Vector<Mini_check>) MiniGamebackDao.instance
				.findBySQL(getSQL(),Mini_check.class);
		for(Mini_check check : list){
			Vector<Mini_game> list1 = (Vector<Mini_game>) MiniGamebackDao.instance
					.findBySQL("select *from mini_game where game_id= "+check.check_gameid,Mini_game.class);
			for(Mini_game game : list1){
				check.plat_recommend = game.plat_recommend;
			}
			
		}
		return list;
	}

}
