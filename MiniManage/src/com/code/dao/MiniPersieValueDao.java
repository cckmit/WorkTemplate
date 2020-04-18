package com.code.dao;

import com.tools.XWHResultSetMapper;
import com.tools.db.OpDbPersieValue;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Vector;

public class MiniPersieValueDao extends XWHResultSetMapper {
    public static MiniPersieValueDao instance;

    static {
        instance = new MiniPersieValueDao();
    }

    private MiniPersieValueDao() {
        try {
            initCache();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String alias() {
        return OpDbPersieValue.DB_ALIAS.PersieValue_ALIAS;
    }

    @Override
    public ResultSet setResultSet(Connection conn, String sql) {
        return OpDbPersieValue.executeQuery(conn, sql);
    }

    @Override
    public void closeConnection(Connection conn) {
        OpDbPersieValue.closeConnection(conn);
    }

    @Override
    public Connection openConnection() {
        return OpDbPersieValue.getConnection(alias());
    }

    @Override
    public int execSQLCMDInfo(String SQL) {
        Connection conn = openConnection();
        return OpDbPersieValue.execSQLCMDInfo(conn, SQL);
    }

    @Override
    public Vector<Class<?>> getCacheClass() {
        Vector<Class<?>> vector = new Vector<>();
        return vector;
    }

}