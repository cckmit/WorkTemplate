package com.code.ui.servlet;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;

import com.code.dao.*;
import com.code.entity.*;
import com.code.ui.UIMoudleServlet;
import com.google.gson.reflect.TypeToken;
import com.tools.XwhTool;
@WebServlet(urlPatterns = { "/account", "/pages/account" })
public class AccountServlet extends UIMoudleServlet {
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
			Account model = (Account) t;
			MiniGamebackDao.instance.saveOrUpdate(model);
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
			Account model = (Account) t;
			MiniGamebackDao.instance.saveOrUpdate(model);
		} catch (Exception e) {
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
		Account model = (Account) t;
		String deleteSQL = "delete from account where account='"
				+ model.account+"'";
		MiniGamebackDao.instance.execSQLCMDInfo(deleteSQL);
	}
	
	public Class<?> getClassInfo() {
		return Account.class;
	}
	
	protected Type getTypeof() {
		return new TypeToken<Vector<Account>>() {}.getType();
	}
	
	public String doSpecJson(HttpServletResponse response) {
		Account account = (Account) request.getSession().getAttribute("account");
		String role = null;
		if(account.getRole() == 0){
			role = "admin";
		}else if(account.getRole() == 1){
			role = "ope";
		}else if(account.getRole() == 2){
			role = "fin";
		}
		
		Map<String, String> roleMap = new HashMap<String, String>();
		roleMap.put("role", role);
		return XwhTool.getGsonValue(roleMap);
	}

	@SuppressWarnings("unchecked")
	public Vector<Account> findData() {
		Vector<Account> list = (Vector<Account>) MiniGamebackDao.instance
				.findBySQL(getSQL(),Account.class);
		return list;
	}

}
