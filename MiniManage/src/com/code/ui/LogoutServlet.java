package com.code.ui;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * 注销的servlet
 * @author caobotao
 */
@WebServlet(urlPatterns = { "/logout" })
public class LogoutServlet extends HttpServlet{

	private static final long serialVersionUID = -4375265551234084850L;
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.getSession().removeAttribute("account");
		String path = req.getContextPath();
		resp.sendRedirect(path);
	}
}
