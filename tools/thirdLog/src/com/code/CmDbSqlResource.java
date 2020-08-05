package com.code;

import com.tools.db.BaseResultSetMapper;
import com.tools.db.druid.DBPoolConnection;

/**
 * @author xuwei
 */
public class CmDbSqlResource extends BaseResultSetMapper {
    @Override
    public String alias() {
        return DBPoolConnection.DB_ALIAS.DB_MASTER_ALIAS;
    }

    private static CmDbSqlResource instance;

    static {
        instance = new CmDbSqlResource();
    }

    private CmDbSqlResource() {
    }

    public static CmDbSqlResource instance() {
        return instance;
    }

}

