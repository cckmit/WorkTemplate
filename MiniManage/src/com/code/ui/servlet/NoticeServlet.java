package com.code.ui.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.code.ui.UIMoudleServlet;
import com.tools.XwhTool;
import com.tools.log4j.Log4j;
@WebServlet(urlPatterns = "/NoticeServlet")
public  class NoticeServlet extends HttpServlet
{
	private String ip;
	private String user_agent;
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	/**
	 * 
	 */
	private static final long serialVersionUID = 5465924624520269500L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try {
			String reqString=request.getQueryString();
			String ip = getIP(request);
			String url = request.getParameter("url");
			StringBuilder builder = new StringBuilder();
			builder.append("{GET,");
			builder.append("\"ip\":"+ip);
			builder.append(",\"request\":" + reqString);
			builder.append("}");
			saveFinalLog(builder.toString());
			UIMoudleServlet.noticeMap.put(url, XwhTool.getCurrentDateTimeValue()+"&&已回复,通知处理完毕!");
			ServletOutputStream out = response.getOutputStream();
			byte[] bytes ="GET-200".getBytes();
			out.write(bytes);
			out.flush();
			out.close();
		}catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 获取访问数据
	 * 
	 * @return
	 */
	public String getServletPath()
	{
		return this.request.getServletPath();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			// 记录协议开始时间
			int size = -1;
			// 获取HTTP请求流
			ServletInputStream inStream = request.getInputStream();
			// 获取HTTP请求流长度
			size = request.getContentLength();
			// 用于缓存每次读取的数据
			byte[] buffer = new byte[size];
			int count = 0;
			int offset = 0;
			// 优化从客户端获取代码输入流效率
			while (count >= 0)
			{
				count = inStream.read(buffer, offset, 1024);
				if (count == -1)
				{
					break;
				}
				offset += count;
			}
			
			String json = new String(buffer, "UTF-8");
			String ip =getIP(request);
			String url = request.getParameter("url");
			UIMoudleServlet.noticeMap.put(url, XwhTool.getCurrentDateTimeValue()+"&&已回复,通知处理完毕!");
			StringBuilder builder = new StringBuilder();
			builder.append("{POST,");
			builder.append("\"ip\":"+ip);
			builder.append(",\"request\":" + json);
			builder.append("}");
			saveFinalLog(builder.toString());
			ServletOutputStream out = response.getOutputStream();
			byte[] bytes ="POST-200".getBytes();
			out.write(bytes);
			out.flush();
			out.close();
		}
		catch (Exception e)
		{
		}
	}
	/**
	 * 保存最终日志
	 * 
	 * @param request
	 * @param response
	 */
	protected void saveFinalLog(String info)
	{
		Log4j.NAME.STDOUTS_LOG.debug(info);
	}

	/**
	 * 获取ip地址
	 * 
	 * @param request
	 * @return
	 */
	public static String getIP(HttpServletRequest request)
	{
		String ipaddr = request.getHeader("x-forwarded-for");
		if (ipaddr == null || ipaddr.equals("unknown"))
		{
			ipaddr = request.getHeader("Proxy-Client-IP");
		}
		if (ipaddr == null || ipaddr.equals("unknown"))
		{
			ipaddr = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ipaddr == null || ipaddr.equals("unknown"))
		{
			ipaddr = request.getRemoteAddr();
		}
		return ipaddr;
	}

	public String getIp()
	{
		return ip;
	}

	public void setIp(String ip)
	{
		this.ip = ip;
	}

	public String getUser_agent()
	{
		return user_agent;
	}

	public void setUser_agent(String user_agent)
	{
		this.user_agent = user_agent;
	}
}
