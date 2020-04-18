package com.code.dao;

import java.util.Vector;

import com.code.entity.Account;

/**
 * 账号Dao类
 * @author caobotao
 */
public class AccountDao {
	/**
	 * 根据账号获取Account对象
	 * @param userName
	 * @return
	 */
	public static Account findOne(String userName){
		String sql = "select * from account where account='" + userName+"'";
		Vector<Account> list = (Vector<Account>) MiniGamebackDao.instance.findBySQL(
				sql, Account.class);
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		return null;
	}
}
