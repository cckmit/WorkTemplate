package com.tools;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CommonUtil {

	public static Integer bool2int(boolean b) {
		return b == true ? 1 : 0;
	}

	public static Integer str2int(String str) {
		return str == null ? 0 : Integer.valueOf(str);
	}

	public static Date calendar2date(Calendar cal) {
		return Date.valueOf(String.format("%tF", cal.getTime()));
	}

	public static Calendar date2calendar(java.util.Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar;
	}

	public static Calendar date2calendar(String date) {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		try {
			calendar.setTime(fmt.parse(date));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
		}
		return calendar;
	}

	public static Date getWeekdayDate(java.util.Date date, int dayOfWeek) {
		Calendar calendar = date2calendar(date);
		calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
		return calendar2date(calendar);
	}

	public static String getWeekRange(java.util.Date date) {
		Date sun = getWeekdayDate(date, Calendar.SUNDAY);
		Date sat = getWeekdayDate(date, Calendar.SATURDAY);
		return String.format("%tF~%tF", sun, sat);
	}

	public static int getDayOfWeek(java.util.Date date) {
		Calendar calendar = date2calendar(date);
		return calendar.get(Calendar.DAY_OF_WEEK);
	}

	public static Date getMonthFirstDay(java.util.Date date) {
		Calendar calendar = date2calendar(date);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		return calendar2date(calendar);
	}

	public static Date getMonthLastDay(java.util.Date date) {
		Calendar calendar = date2calendar(date);
		calendar.add(Calendar.MONTH, 1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.DATE, -1);
		return calendar2date(calendar);
	}

	public static Date getNextMonth(java.util.Date date) {
		Calendar calendar = date2calendar(date);
		calendar.add(Calendar.MONTH, 1);
		return calendar2date(calendar);
	}

	public static String getYearMonth(java.util.Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		return sdf.format(date);
	}

	public static String getYearMonths(java.util.Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		return sdf.format(date);
	}

	public static String getYearMonth(String nowDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
		try {
			java.util.Date before = sdf1.parse(nowDate);
			return sdf.format(before);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
		}
		return null;

	}

	public static String getStringDate(java.util.Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd "); // ����ʱ���ʽ
		return sdf.format(date);
	}

	public static String getStringDates(java.util.Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd "); // ����ʱ���ʽ
		return sdf.format(date);
	}

	/**
	 * 
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static java.util.Date getDatesString(String date)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd"); // ����ʱ���ʽ
		return sdf.parse(date);
	}

	public static Date getBeforeDay(java.util.Date date) {
		Calendar beforeday = CommonUtil.date2calendar(date);
		beforeday.add(Calendar.DATE, -25);
		Date bfdate = CommonUtil.calendar2date(beforeday);
		return bfdate;
	}

	public static Date getBeforeDays(java.util.Date date, int num) {
		Calendar beforeday = CommonUtil.date2calendar(date);
		beforeday.add(Calendar.DATE, -num);
		Date bfdate = CommonUtil.calendar2date(beforeday);
		return bfdate;
	}

	public static String getBeforeDay(java.util.Date date, int num) {
		Calendar beforeday = CommonUtil.date2calendar(date);
		beforeday.add(Calendar.DATE, -num);
		Date bfdate = CommonUtil.calendar2date(beforeday);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String defaultStartDate = sdf.format(bfdate);
		return defaultStartDate;
	}

	public static String getBeforeDay1(java.util.Date date, int num) {
		Calendar beforeday = CommonUtil.date2calendar(date);
		beforeday.add(Calendar.DATE, -num);
		Date bfdate = CommonUtil.calendar2date(beforeday);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String defaultStartDate = sdf.format(bfdate);
		return defaultStartDate;
	}

	public static String getBeforeDay(Integer num) {
		Calendar beforeday = CommonUtil.date2calendar(new java.util.Date());
		beforeday.add(Calendar.DATE, num);
		Date bfdate = CommonUtil.calendar2date(beforeday);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String defaultStartDate = sdf.format(bfdate);
		return defaultStartDate;
	}

	/**
	 * ������� �õ�����
	 * 
	 * @param beforeDate
	 * @param endDate
	 * @return
	 * @throws ParseException
	 */
	public static long getDays(String beforeDate, String endDate)
			throws ParseException {
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(
				"yyyyMMdd");
		java.util.Date before = format.parse(beforeDate);
		java.util.Date end = format.parse(endDate);
		return (end.getTime() - before.getTime()) / (24 * 60 * 60 * 1000);
	}

	/**
	 * ��ʾ��ǰʱ��
	 * 
	 * @return
	 */
	// public static String datesInfo()
	// {
	// Calendar ca = Calendar.getInstance();
	// String dates = String.format("%tY%tm%td %tH%tM%tS", ca, ca, ca,
	// ca, ca, ca);
	// return dates;
	// }

	public static String datesInfo() {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		return simpleDateFormat.format(calendar.getTime());
	}

	/**
	 * ��ȡ����
	 * 
	 * @return
	 */
	public static String getdDates() {
		Calendar ca = Calendar.getInstance();
		String dates = String.format("%tY%tm%td", ca, ca, ca);
		return dates;
	}

	/**
	 * ��ʾСʱ
	 * 
	 * @return
	 */
	public static String getHourDateValue(int num) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR, -num);
		java.util.Date d = cal.getTime();
		SimpleDateFormat sp = new SimpleDateFormat("H");
		return sp.format(d);// ��ȡ��������
	}

	/**
	 * ��� ����֮ǰ���·ݺ���
	 * 
	 * @param startDateStr
	 * @param endDateStr
	 * @param type
	 * @return
	 * @throws ParseException
	 */
	public static List<String> getBetweenTwoDateList(String startDateStr,
			String endDateStr, int type) throws ParseException {
		List<String> list = new ArrayList<String>();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date startDate = simpleDateFormat.parse(startDateStr); // ��ʼ����
		java.util.Date endDate = simpleDateFormat.parse(endDateStr); // ��������
		Calendar startCalendar = Calendar.getInstance();
		Calendar endCalendar = Calendar.getInstance();
		Calendar endCalendar1 = Calendar.getInstance();
		startCalendar.setTime(startDate);
		endCalendar.setTime(endDate);
		endCalendar1.setTime(endDate);
		endCalendar1.add(Calendar.MONTH, 1);
		String result = null;
		while (startCalendar.compareTo(endCalendar1) <= 0) {
			startDate = startCalendar.getTime();
			switch (type) {
			case 1:
				result = new SimpleDateFormat("yyyyMM").format(startDate);
				result = result.substring(0, result.length());
				list.add(result);
				// ��ʼ���ڼ�һ����ֱ�����ڽ�������Ϊֹ
				startCalendar.add(Calendar.MONTH, 1);
				break;
			case 2:
				result = new SimpleDateFormat("yyyy-MM-dd").format(startDate);
				result = result.substring(0, result.length());
				list.add(result);
				// ��ʼ���ڼ�һ����ֱ�����ڽ�������Ϊֹ
				startCalendar.add(Calendar.DAY_OF_MONTH, 1);
				break;
			default:
				result = new SimpleDateFormat("yyyy-MM-dd HH")
						.format(startDate);
				result = result.substring(0, result.length());
				list.add(result);
				// ��ʼ���ڼ�һ����ֱ�����ڽ�������Ϊֹ
				startCalendar.add(Calendar.HOUR, 1);
				break;
			}
		}
		return list;
	}

	public static String setContentType(String returnFileName)
	{
		String contentType = "application/octet-stream";
		if (returnFileName.lastIndexOf(".") < 0)
			return contentType;
		returnFileName = returnFileName.toLowerCase();
		returnFileName = returnFileName.substring(returnFileName
				.lastIndexOf(".") + 1);

		if (returnFileName.equals("html") || returnFileName.equals("htm")
				|| returnFileName.equals("shtml"))
		{
			contentType = "text/html";
		}
		else if (returnFileName.equals("apk"))
		{
			contentType = "application/vnd.android.package-archive";
		}
		else if (returnFileName.equals("sis"))
		{
			contentType = "application/vnd.symbian.install";
		}
		else if (returnFileName.equals("sisx"))
		{
			contentType = "application/vnd.symbian.install";
		}
		else if (returnFileName.equals("exe"))
		{
			contentType = "application/x-msdownload";
		}
		else if (returnFileName.equals("msi"))
		{
			contentType = "application/x-msdownload";
		}
		else if (returnFileName.equals("css"))
		{
			contentType = "text/css";
		}
		else if (returnFileName.equals("xml"))
		{
			contentType = "text/xml";
		}
		else if (returnFileName.equals("gif"))
		{
			contentType = "image/gif";
		}
		else if (returnFileName.equals("jpeg") || returnFileName.equals("jpg"))
		{
			contentType = "image/jpeg";
		}
		else if (returnFileName.equals("js"))
		{
			contentType = "application/x-javascript";
		}
		else if (returnFileName.equals("atom"))
		{
			contentType = "application/atom+xml";
		}
		else if (returnFileName.equals("rss"))
		{
			contentType = "application/rss+xml";
		}
		else if (returnFileName.equals("mml"))
		{
			contentType = "text/mathml";
		}
		else if (returnFileName.equals("txt"))
		{
			contentType = "text/plain";
		}
		else if (returnFileName.equals("jad"))
		{
			contentType = "text/vnd.sun.j2me.app-descriptor";
		}
		else if (returnFileName.equals("wml"))
		{
			contentType = "text/vnd.wap.wml";
		}
		else if (returnFileName.equals("htc"))
		{
			contentType = "text/x-component";
		}
		else if (returnFileName.equals("png"))
		{
			contentType = "image/png";
		}
		else if (returnFileName.equals("tif") || returnFileName.equals("tiff"))
		{
			contentType = "image/tiff";
		}
		else if (returnFileName.equals("wbmp"))
		{
			contentType = "image/vnd.wap.wbmp";
		}
		else if (returnFileName.equals("ico"))
		{
			contentType = "image/x-icon";
		}
		else if (returnFileName.equals("jng"))
		{
			contentType = "image/x-jng";
		}
		else if (returnFileName.equals("bmp"))
		{
			contentType = "image/x-ms-bmp";
		}
		else if (returnFileName.equals("svg"))
		{
			contentType = "image/svg+xml";
		}
		else if (returnFileName.equals("jar") || returnFileName.equals("var")
				|| returnFileName.equals("ear"))
		{
			contentType = "application/java-archive";
		}
		else if (returnFileName.equals("doc"))
		{
			contentType = "application/msword";
		}
		else if (returnFileName.equals("pdf"))
		{
			contentType = "application/pdf";
		}
		else if (returnFileName.equals("rtf"))
		{
			contentType = "application/rtf";
		}
		else if (returnFileName.equals("xls")||returnFileName.equals("xlsx"))
		{
			contentType = "application/vnd.ms-excel";
		}
		else if (returnFileName.equals("ppt"))
		{
			contentType = "application/vnd.ms-powerpoint";
		}
		else if (returnFileName.equals("7z"))
		{
			contentType = "application/x-7z-compressed";
		}
		else if (returnFileName.equals("rar"))
		{
			contentType = "application/x-rar-compressed";
		}
		else if (returnFileName.equals("swf"))
		{
			contentType = "application/x-shockwave-flash";
		}
		else if (returnFileName.equals("rpm"))
		{
			contentType = "application/x-redhat-package-manager";
		}
		else if (returnFileName.equals("der") || returnFileName.equals("pem")
				|| returnFileName.equals("crt"))
		{
			contentType = "application/x-x509-ca-cert";
		}
		else if (returnFileName.equals("xhtml"))
		{
			contentType = "application/xhtml+xml";
		}
		else if (returnFileName.equals("zip"))
		{
			contentType = "application/zip";
		}
		else if (returnFileName.equals("mid") || returnFileName.equals("midi")
				|| returnFileName.equals("kar"))
		{
			contentType = "audio/midi";
		}
		else if (returnFileName.equals("mp3"))
		{
			contentType = "audio/mpeg";
		}
		else if (returnFileName.equals("ogg"))
		{
			contentType = "audio/ogg";
		}
		else if (returnFileName.equals("m4a"))
		{
			contentType = "audio/x-m4a";
		}
		else if (returnFileName.equals("ra"))
		{
			contentType = "audio/x-realaudio";
		}
		else if (returnFileName.equals("3gpp") || returnFileName.equals("3gp"))
		{
			contentType = "video/3gpp";
		}
		else if (returnFileName.equals("mp4"))
		{
			contentType = "video/mp4";
		}
		else if (returnFileName.equals("mpeg") || returnFileName.equals("mpg"))
		{
			contentType = "video/mpeg";
		}
		else if (returnFileName.equals("mov"))
		{
			contentType = "video/quicktime";
		}
		else if (returnFileName.equals("flv"))
		{
			contentType = "video/x-flv";
		}
		else if (returnFileName.equals("m4v"))
		{
			contentType = "video/x-m4v";
		}
		else if (returnFileName.equals("mng"))
		{
			contentType = "video/x-mng";
		}
		else if (returnFileName.equals("asx") || returnFileName.equals("asf"))
		{
			contentType = "video/x-ms-asf";
		}
		else if (returnFileName.equals("wmv"))
		{
			contentType = "video/x-ms-wmv";
		}
		else if (returnFileName.equals("avi"))
		{
			contentType = "video/x-msvideo";
		}
		return contentType;
	}
}
