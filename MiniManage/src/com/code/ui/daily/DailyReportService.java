package com.code.ui.daily;

import java.io.InputStream;
import java.util.Vector;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.mail.HtmlEmail;

import com.code.dao.MiniGamebackDao;
import com.code.entity.Minitj_daily;
import com.code.ui.UIMoudleServlet;
import com.tools.XwhTool;
import com.tools.config.ReadConfig;
import com.tools.db.OpDbConnector;
@WebServlet(urlPatterns = { "/dailyServlet", "/pages/dailyServlet" })
public class DailyReportService extends UIMoudleServlet
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5334870730755275707L;
	/**
	 * 向客户端发送数据
	 * 
	 * @param content
	 */
	public Object doOperator(HttpServletRequest request)
	{
		String string="日报发送成功！";
		String date=XwhTool.getPriDateWithChar(-0)+"日报";
		if(UIMoudleServlet.noticeMap==null||UIMoudleServlet.noticeMap.size()==0)
		{
			sendReport();
			UIMoudleServlet.noticeMap.put(date,XwhTool.getCurrentDateTimeValue()+"&&今日日报已发送！");
		}else
		{
			if(UIMoudleServlet.noticeMap.containsKey(date))
			{
				string="今日日报已发送\n无法发送日报！！！";
			}else
			{
				sendReport();
				UIMoudleServlet.noticeMap.put(date,XwhTool.getCurrentDateTimeValue()+"&&今日日报已发送！");
			//	UIMoudleServlet.noticeMap.remove(XwhTool.getPriDateWithChar(-1)+"日报");
			}
		}
		return string;
	}
	public static void main(String[] args) {
		sendReport();
		//System.out.println("结束!");
	}
	public static void sendReport()
	{
		OpDbConnector.init();
		DailyReportService service = new DailyReportService();
		Vector<Minitj_daily> reportData = service.dailyData();

		String reportPath = "/Template2.code";
		InputStream in = DailyReportService.class.getResourceAsStream(reportPath);
		String reportTemplate = XwhTool.readInputStream(in);

		StringBuilder dataProfitHTML = new StringBuilder();
		String lFlag = "\"";
		String rFlag = "\"";
		String tdStyle = "text-align: center;padding: 8px;border-right: 1px solid #f8f8f8;font-size: 12px;";
		String oddStyle = "background: #f2eada;";
		// 拼接盈利HTML
		int i = 0;
		for (Minitj_daily daily : reportData)
		{
			if (i % 2 == 0)
				dataProfitHTML.append(String.format("<tr style=%s%s%s>", lFlag, oddStyle, rFlag));
			else
				dataProfitHTML.append("<tr>");

			dataProfitHTML
					.append(String.format("<td style=%s%s%s>%s</td>", lFlag, tdStyle, rFlag, daily.daily_date));
			dataProfitHTML.append(String.format("<td style=%s%s%s>%s</td>", lFlag, tdStyle, rFlag, daily.daily_new));
			dataProfitHTML.append(String.format("<td style=%s%s%s>%s</td>", lFlag, tdStyle, rFlag, daily.daily_active));
			dataProfitHTML.append(String.format("<td style=%s%s%s>%s</td>", lFlag, tdStyle, rFlag, daily.daily_visit));
			dataProfitHTML.append(String.format("<td style=%s%s%s>%s</td>", lFlag, tdStyle, rFlag, daily.daily_outgo));
			dataProfitHTML.append(String.format("<td style=%s%s%s>%s</td>", lFlag, tdStyle, rFlag, daily.daily_income_ad));
			dataProfitHTML.append("</tr>");
			i++;
		}
		reportTemplate = reportTemplate.replace("#data_profit#", dataProfitHTML);
		// 头部标题
/*		String headFormatter="<tr><div style=\"color: #ffffff;background: #F5DE65; text-align: center;padding: 8px 0px 8px 0px;width:%s \">%s</div></tr>";
		reportTemplate = reportTemplate.replace("#head_self#", String.format(headFormatter,"7em", "我方统计"));*/
		StringBuilder ProfitHTML = new StringBuilder();
		ProfitHTML.append("<br/><tr>").append("详情请查阅后台：http://103.20.249.243:18080/MiniManage/pages/init.jsp").append("</tr>");
		ProfitHTML.append("<br/><tr>").append("统计项解释：").append("</tr>");
		ProfitHTML.append("<br/><tr>").append("注册用户数：是指统计日期内，所有已发布FC小游戏的新增用户总和（同一个微信用户进入不同游戏，新增+1）；").append("</tr>");
		ProfitHTML.append("<br/><tr>").append("活跃用户数：是指统计日期内，所有已发布FC小游戏的活跃用户总和（同一个微信用户进入不同游戏，新增+1）；").append("</tr>");
		ProfitHTML.append("<br/><tr>").append("访问总次数：是指统计日期内，所有已发布FC小游戏被访问的次数总和；").append("</tr>");
		ProfitHTML.append("<br/><tr>").append("买量支出：是指统计日期内，所有FC小游戏的广告投放或者买量支出；").append("</tr>");
		ProfitHTML.append("<br/><tr>").append("广告收入：是指统计日期内，所有FC小游戏的广告收入；").append("</tr>");
		reportTemplate+=ProfitHTML;
		sendEmail(reportTemplate, "[FC小游戏]日报:"+XwhTool.getPriDateWithChar(-0));
		//System.out.println(reportTemplate);
	}
	private  Vector<Minitj_daily> dailyData()
	{
		
		String sql="select * from minitj_daily where 1=1 "
				+ " AND daily_date<='"+XwhTool.getPriDateWithChar(-0)
				+ "' AND daily_date>='"+XwhTool.getPriDateWithChar(-7)
				+ "' ORDER BY daily_date DESC ";
		Vector<Minitj_daily> list = (Vector<Minitj_daily>) MiniGamebackDao.instance
				.findBySQL(sql,Minitj_daily.class);
		return list;
	}
	//发送邮件
	public static void sendEmail(String msg, String title)
	{
		String[] users = ReadConfig.get("report_user").split(",");
		// 发送email
		HtmlEmail email = new HtmlEmail();
		try
		{
			// 这里是SMTP发送服务器的名字
			email.setHostName("smtp.exmail.qq.com");
			// 字符编码集的设置
			email.setCharset("UTF-8");
			// 收件人的邮箱
			email.addTo(users);
			// 发送人的邮箱
			email.setFrom("qp@blazefire.com", "日报");
			// 用户名密码
			email.setAuthentication("qp@blazefire.com", "6u5PmSks1");
			// 要发送的邮件主题
			email.setSubject(title);
			email.setMsg(msg);
			// 发送
			email.send();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
