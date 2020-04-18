package com.tools.log4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * LOG配置
 * 
 * @author user
 * 
 */
public class Log4j
{
	public interface NAME{
	static Logger EXCEPTION_LOG = LoggerFactory.getLogger("exceptionLog");

	static Logger SQLERROR_LOG = LoggerFactory.getLogger("SQLErrorLog");

	static Logger STDOUTS_LOG = LoggerFactory.getLogger("stdoutsLog");
}

	/**
	 * 获取异常具体信息
	 * 
	 * @param e
	 * @return
	 */
	public static String getExceptionInfo(Exception e)
	{
		StringBuffer exception = new StringBuffer();
		exception.append(e.getMessage() + "\n");
		StackTraceElement[] messages = e.getStackTrace();
		for (StackTraceElement stackTraceElement : messages)
		{
			exception.append(stackTraceElement + "\n");
		}
		return exception.toString();
	}
}
