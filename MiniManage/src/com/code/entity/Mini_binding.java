package com.code.entity;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.annotation.Comments;
import com.annotation.Page;
import com.annotation.PrimaryKey;
import com.annotation.ReadOnly;
import com.code.dao.MiniGamebackDao;

   /**
    * mini_binding 实体类
    * 2018-12-06 xuweihua
    */ 

@Entity
@Page
public class Mini_binding
{
	@PrimaryKey
	@Column(name="binding_id")
	@Comments(name="主键id")
	public int binding_id;
	@Column(name="binding_gameid")
	@Comments(name="游戏id")
	public int binding_gameid;
	@Column(name="binding_con1")
	@Comments(name="con1 gameid")
	public String binding_con1;
	@Column(name="binding_con2")
	@Comments(name="con2 gameid")
	public String binding_con2;
	@Column(name="binding_con3")
	@Comments(name="con3 gameid")
	public String binding_con3;
	@Column(name="binding_con4")
	@Comments(name="con4 gameid")
	public String binding_con4;
	@Column(name="binding_con5")
	@Comments(name="con5 gameid")
	public String binding_con5;
	@Column(name="binding_con6")
	@Comments(name="con6 gameid")
	public String binding_con6;
	@Column(name="binding_con7")
	@Comments(name="con7 gameid")
	public String binding_con7;
	@Column(name="binding_con8")
	@Comments(name="con8 gameid")
	public String binding_con8;
	@Column(name="binding_con9")
	@Comments(name="con9 gameid")
	public String binding_con9;
	@Column(name="binding_con10")
	@Comments(name="con10 gameid")
	public String binding_con10;
	@Column(name="binding_json")
	@Comments(name="json")
	public String binding_json;
	
	public String binding_name_con1;
	public String binding_name_con2;
	public String binding_name_con3;
	public String binding_name_con4;
	public String binding_name_con5;
	public String binding_name_con6;
	public String binding_name_con7;
	public String binding_name_con8;
	public String binding_name_con9;
	public String binding_name_con10;
	
	
	@Column(name="game_name")
	@Comments(name="产品名称")
	@ReadOnly
	public String game_name;
	
	@Column(name="game_appid")
	@Comments(name="game_appid")
	@ReadOnly
	public String game_appid;

	
	
	public static Map<String, Integer> getRecCountMap()
	{
		String sql = " SELECT a.appid,SUM(a.count_one) rec_count FROM "+
							 "( "+
							" SELECT binding_con1 appid, COUNT(1) count_one FROM `mini_binding` WHERE binding_con1 IS NOT NULL GROUP BY binding_con1 "+
							" UNION ALL "+
							" SELECT binding_con2 appid, COUNT(1) count_one FROM `mini_binding` WHERE binding_con2 IS NOT NULL GROUP BY binding_con2 "+
							" UNION ALL "+
							" SELECT binding_con3 appid, COUNT(1) count_one FROM `mini_binding` WHERE binding_con3 IS NOT NULL GROUP BY binding_con3 "+
							" UNION ALL "+
							" SELECT binding_con4 appid, COUNT(1) count_one FROM `mini_binding` WHERE binding_con4 IS NOT NULL GROUP BY binding_con4 "+
							" UNION ALL "+
							" SELECT binding_con5 appid, COUNT(1) count_one FROM `mini_binding` WHERE binding_con5 IS NOT NULL GROUP BY binding_con5 "+
							" UNION ALL "+
							" SELECT binding_con6 appid, COUNT(1) count_one FROM `mini_binding` WHERE binding_con6 IS NOT NULL GROUP BY binding_con6 "+
							" UNION ALL "+
							" SELECT binding_con7 appid, COUNT(1) count_one FROM `mini_binding` WHERE binding_con7 IS NOT NULL GROUP BY binding_con7 "+
							" UNION ALL "+
							" SELECT binding_con8 appid, COUNT(1) count_one FROM `mini_binding` WHERE binding_con8 IS NOT NULL GROUP BY binding_con8 "+
							" UNION ALL "+
							" SELECT binding_con9 appid, COUNT(1) count_one FROM `mini_binding` WHERE binding_con9 IS NOT NULL GROUP BY binding_con9 "+
							" UNION ALL "+
							" SELECT binding_con10 appid, COUNT(1) count_one FROM `mini_binding` WHERE binding_con10 IS NOT NULL GROUP BY binding_con10 "+
							" ) a "+
							" GROUP BY a.appid";
		
		Map<String, Integer> map = new HashMap<String, Integer>();
		Connection conn = null;
		try
		{
			conn = MiniGamebackDao.instance.openConnection();
			ResultSet resultSet = MiniGamebackDao.instance.setResultSet(conn, sql);
			while(resultSet.next())
			{
				map.put(resultSet.getString("appid"), resultSet.getInt("rec_count"));
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
