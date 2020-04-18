package com.code.ui.servlet;


import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

import com.code.entity.Account;
import com.code.ui.UIMoudleServlet;
@WebServlet(urlPatterns = { "/acServlet", "/pages/acServlet" })
public class UserInfosServlet extends UIMoudleServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public Object doOperator(HttpServletRequest request){
		String string="";
        Account account = (Account) request.getSession().getAttribute("account"); 
        string =account.operation;
		return string;
	}

}
