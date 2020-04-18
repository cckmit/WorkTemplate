package com.code.entity;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.annotation.Comments;
import com.annotation.Page;
import com.annotation.PrimaryKey;
import com.code.dao.MiniGamebackDao;

   /**
    * minitj_wx 实体类
    * 2019-02-21 xuweihua
    */ 

@Entity
@Page
public class Minitj_wx
{
	@PrimaryKey
	@Column(name="wx_gameid")
	@Comments(name="游戏id")
	public int wx_gameid;
	@Column(name="wx_date")
	@Comments(name="日期")
	public String wx_date;
	@Column(name="wx_new")
	@Comments(name="注册用户数")
	public int wx_new;
	@Column(name="wx_active")
	@Comments(name="活跃用户数")
	public int wx_active;
	@Column(name="wx_visit")
	@Comments(name="访问用户数")
	public int wx_visit;
	@Column(name="wx_avg_login")
	@Comments(name="人均登录次数")
	public Double wx_avg_login;
	@Column(name="wx_avg_online")
	@Comments(name="人均在线时长")
	public Double wx_avg_online;
	@Column(name="wx_remain2")
	@Comments(name="注册次留")
	public Double wx_remain2;
	@Column(name="wx_video_show")
	@Comments(name="视频曝光量")
	public int wx_video_show;
	@Column(name="wx_video_clickrate")
	@Comments(name="视频点击率")
	public Double wx_video_clickrate;
	@Column(name="wx_video_income")
	@Comments(name="视频收入")
	public Double wx_video_income;
	@Column(name="wx_banner_show")
	@Comments(name="bannel曝光量")
	public int wx_banner_show;
	@Column(name="wx_banner_clickrate")
	@Comments(name="bannel点击率")
	public Double wx_banner_clickrate;
	@Column(name="wx_banner_income")
	@Comments(name="bannel收入")
	public Double wx_banner_income;
	@Column(name="wx_reg_ad")
	@Comments(name="注册用户来自广告占比")
	public int wx_reg_ad;
	@Column(name="wx_reg_jump")
	@Comments(name="注册用户来自跳转占比")
	public int wx_reg_jump;
	@Column(name="wx_reg_search")
	@Comments(name="注册用户来自搜索占比")
	public int wx_reg_search;
	@Column(name="wx_reg_app")
	@Comments(name="注册用户来自app占比")
	public int wx_reg_app;
	@Column(name="wx_reg_code")
	@Comments(name="注册用户来自二维码占比")
	public int wx_reg_code;
	@Column(name="wx_reg_session")
	@Comments(name="注册用户来自会话占比")
	public int wx_reg_session;
	@Column(name="wx_active_women")
	@Comments(name="活跃女性占比")
	public int wx_active_women;
	@Column(name="wx_share_user")
	@Comments(name="分享人数")
	public int wx_share_user;
	@Column(name="wx_share_count")
	@Comments(name="分享次数")
	public int wx_share_count;
	@Column(name="wx_share_rate")
	@Comments(name="分享率")
	public Double wx_share_rate;
	@Column(name="wx_reg_json")
	@Comments(name="注册用户来源JSON")
	public String wx_reg_json;
	@Column(name="wx_updatetime")
	@Comments(name="更新时间")
	public String wx_updatetime;

	
	public static Map<Integer, Double> getActiveUpMap()
	{
		String sql = "SELECT wx_appid, (wx_video_income+wx_banner_income)/wx_active wx_active FROM minitj_wx "
				+ "WHERE wx_date=DATE_SUB(CURDATE(),INTERVAL 1 DAY)";
		Vector<Mini_game> list = (Vector<Mini_game>) MiniGamebackDao.instance
				.findBySQL("select * from mini_game", Mini_game.class);
		Map<String,Integer> gameMap = new HashMap<String,Integer>();
		if(list!=null&&list.size()>0)
		{
			for(Mini_game game:list)
			{
				gameMap.put(game.game_appid, game.game_id);
			}
		}
		Map<Integer, Double> map = new HashMap<Integer, Double>();
		Connection conn = null;
		try
		{
			conn = MiniGamebackDao.instance.openConnection();
			ResultSet resultSet = MiniGamebackDao.instance.setResultSet(conn, sql);
			while(resultSet.next())
			{
				map.put(gameMap.get(resultSet.getString("wx_appid")), resultSet.getDouble("wx_active"));
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
			MiniGamebackDao.instance.closeConnection(conn);
		}
		return map;
	}
}
