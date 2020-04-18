package com.code.ui.servlet;

import java.io.IOException;
import java.util.Vector;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

import com.code.dao.MiniGamebackDao;
import com.code.entity.Mini_community_info;
import com.code.ui.UIMoudleServlet;
import com.tools.JsonTool;
import com.tools.XwhHttp;

@WebServlet(urlPatterns = { "/UploadzipServlet", "/pages/UploadzipServlet" })
public class UploadzipServlet extends UIMoudleServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static class Res
	{
		//资源地址
		public String resUrl;
		//MD5
		public String  md5;
	}
	


	
	public Object doOperator(HttpServletRequest request)
	{
		    String url=request.getParameter("sendurl");
			String value;
			try {
				value = parseServletInputStream(request);
				Res res = JsonTool.toObject(value, Res.class);
				if(res!=null)
				{
					Vector<Mini_community_info> list = (Vector<Mini_community_info>) MiniGamebackDao.instance
							.findBySQL("select * from mini_community_info",Mini_community_info.class);
					for(Mini_community_info community_info : list){
						if(res.resUrl.contains(community_info.community_name)){
							String updateSqls = "UPDATE mini_community_info set zip_md5='"+res.md5+"' , zip_url='"+res.resUrl+"' where community_name='"+community_info.community_name+"'";
							MiniGamebackDao.instance.execSQLCMDInfo(updateSqls);
						}
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(url!=null)
			{
				try {
					XwhHttp.sendGet(url);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return url;
	}

}
