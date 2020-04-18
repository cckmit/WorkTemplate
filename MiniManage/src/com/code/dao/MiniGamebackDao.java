package com.code.dao;

import java.sql.ResultSet;
import java.util.Vector;
import java.sql.Connection;
import com.tools.XWHResultSetMapper;
import com.tools.db.OpDbConnector;

public class MiniGamebackDao extends XWHResultSetMapper
{
	public static MiniGamebackDao instance;
	
	static
	{
		instance = new MiniGamebackDao();
	}
	
	private MiniGamebackDao()
	{
		try
		{
			initCache();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public String alias()
	{
		return OpDbConnector.DB_ALIAS.MINIGAMEBACK_ALIAS;
	}

	@Override
	public ResultSet setResultSet(Connection conn,String sql)
	{
		return OpDbConnector.executeQuery(conn, sql);
	}

	@Override
	public void closeConnection(Connection conn)
	{
		OpDbConnector.closeConnection(conn);
	}

	@Override
	public Connection openConnection()
	{
		return OpDbConnector.getConnection(alias());
	}

	@Override
	public int execSQLCMDInfo(String SQL)
	{
		Connection conn = openConnection();
		return OpDbConnector.execSQLCMDInfo(conn, SQL);
	}
	
	@Override
	public Vector<Class<?>> getCacheClass()
	{
		Vector<Class<?>> vector = new Vector<>();
		return vector;
	}

}