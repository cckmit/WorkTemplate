package com.code.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Vector;

import com.tools.XWHResultSetMapper;
import com.tools.db.OpDbGamePackage;

public class MiniPackageDao extends XWHResultSetMapper{
public static MiniPackageDao instance;
	
	static
	 {
		instance = new MiniPackageDao();
	 }
	
	 private MiniPackageDao()
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
	public Vector<Class<?>> getCacheClass() {
		// TODO Auto-generated method stub
		Vector<Class<?>> vector = new Vector<>();
		return vector;
	}

	@Override
	public String alias() {
		// TODO Auto-generated method stub
		return OpDbGamePackage.DB_ALIAS.PMINIGAMEBACK_ALIAS;
	}

	@Override
	public ResultSet setResultSet(Connection conn, String sql) {
		// TODO Auto-generated method stub
		return OpDbGamePackage.executeQuery(conn, sql);
	}

	@Override
	public void closeConnection(Connection conn) {
		// TODO Auto-generated method stub
		OpDbGamePackage.closeConnection(conn);

	}

	@Override
	public Connection openConnection() {
		return OpDbGamePackage.getConnection(alias());
	}

	@Override
	public int execSQLCMDInfo(String SQL) {
		// TODO Auto-generated method stub
		Connection conn = openConnection();
		return OpDbGamePackage.execSQLCMDInfo(conn, SQL);
	}

}
