package com.code.ui;

import com.code.dao.AccountDao;
import com.code.entity.Account;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

@WebServlet(urlPatterns = "/login")
public class LoginServlet extends UIMoudleServlet {

    private static final long serialVersionUID = 5334870730755275707L;

    /**
     * 进行操作数据
     *
     * @param request
     */
    @Override
    public Object doOperator(HttpServletRequest request) {
        String pageUsername = request.getParameter("account");
        String pagePassword = request.getParameter("password");
        Account account = AccountDao.findOne(pageUsername);
        if (account != null && account.password.equals(pagePassword)) {
            request.getSession().setAttribute("account", account);
            request.getSession().setAttribute("perssionJson", account.getPerssionJson(request));
            return account;
        }
        return "";
    }
}
