package com.code.ui.servlet;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.servlet.annotation.WebServlet;

import com.code.dao.MiniGamebackDao;
import com.code.entity.Mini_game;
import com.code.entity.Minitj_wxsub;
import com.code.ui.UIMoudleServlet;
import com.google.gson.reflect.TypeToken;
@WebServlet(urlPatterns = { "/minitj_wxsub", "/pages/minitj_wxsub" })
public class MinitjwxsubServlet extends UIMoudleServlet {
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
			Minitj_wxsub model = (Minitj_wxsub) t;
			model.wxsub_ctime= new Timestamp(System.currentTimeMillis()).toString();
			MiniGamebackDao.instance.saveOrUpdate(model);
		} catch (Exception e) {
			// TODO Auto-generated catch block
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
			Minitj_wxsub model = (Minitj_wxsub) t;
			MiniGamebackDao.instance.saveOrUpdate(model);
		} catch (Exception e) {
			// TODO Auto-generated catch block
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
	protected void deleteData(Object t) {
		Minitj_wxsub model = (Minitj_wxsub) t;
		String deleteSQL = "delete from minitj_wxsub where wxsub_id="
				+ model.wxsub_id;
		MiniGamebackDao.instance.execSQLCMDInfo(deleteSQL);
	}
	
	public Class<?> getClassInfo() {
		return Minitj_wxsub.class;
	}
	
	protected Type getTypeof() {
		// TODO Auto-generated method stub
		return new TypeToken<Vector<Minitj_wxsub>>() {
		}.getType();
	}
	@Override
	public String getGivenSearch(Set<String> keySet, Map<String, String> map) {
		StringBuilder sb = new StringBuilder();
		for (String key : keySet) {
			if("wxsub_gameid".equals(key)){
				sb.append(" AND (wxsub_bindgameid1="+map.get(key)+" OR wxsub_bindgameid2="+map.get(key)+""
						+ " OR wxsub_bindgameid3="+map.get(key)+")" );
			}else{
				sb.append(" AND "+key+" LIKE '%" + map.get(key) +"%' ");
			}
		}
		return sb.toString();
	}
	@SuppressWarnings("unchecked")
	public Vector<Minitj_wxsub> findData() {
		Vector<Minitj_wxsub> list = (Vector<Minitj_wxsub>) MiniGamebackDao.instance
				.findBySQL(getSQL(),Minitj_wxsub.class);
		Vector<Mini_game> gamelist = (Vector<Mini_game>) MiniGamebackDao.instance
				.findBySQL("select * from mini_game",Mini_game.class);
		Map<Integer,String> map=new HashMap<Integer, String>();
		for(Mini_game mini_game:gamelist)
		{
			map.put(mini_game.game_id, mini_game.game_name);
		}
		for(Minitj_wxsub minitj_wxsub:list)
		{
			if(minitj_wxsub.wxsub_bindgameid1!=0)
			{
				minitj_wxsub.setWxsub_bindgameid1Name(map.get(minitj_wxsub.wxsub_bindgameid1));
			}
			if(minitj_wxsub.wxsub_bindgameid2!=0)
			{
				minitj_wxsub.setWxsub_bindgameid2Name(map.get(minitj_wxsub.wxsub_bindgameid2));
			}
			if(minitj_wxsub.wxsub_bindgameid3!=0)
			{
				minitj_wxsub.setWxsub_bindgameid3Name(map.get(minitj_wxsub.wxsub_bindgameid3));
			}
		}
		return list;
	}
	public String getGsonFormat() {
		return "yyyy-MM-dd HH:mm:ss";
	}
}
