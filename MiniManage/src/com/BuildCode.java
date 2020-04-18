package com;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Properties;

import com.tools.XwhTool;

/**
 * 进行创建数据库配置文件以及log4j配置文件
 * 
 * @author Host-0222
 * 
 */
public class BuildCode
{
	public static void main(String[] args)
	{
		buildLog4j();
		System.out.println(new Timestamp(1481006565482l));
	}

	/**
	 * 进行建立LOG4J代码
	 */
	private static void buildLog4j()
	{
		String path = System.getProperty("user.dir");
		// 进行检测LOG4J.java 是否存在
		File srcPath = new File(path, "src");
		File log4jFile = XwhTool.getSrcPath(srcPath, "Log4j.java");
		File configPath = new File(path, "config");
		if (!configPath.exists())
		{
			System.err.println("请创建名为config的source Folder文件夹!");
			return;
		}
		String fullcode = null;
		if (log4jFile != null && log4jFile.exists())
		{
			fullcode = XwhTool.readFileString(log4jFile.getPath());
		} else
		{
			File tempDir = XwhTool.createNextDir(srcPath, "com", "tools", "log4j");
			log4jFile = new File(tempDir, "Log4j.java");
		}
		buildLog4jFile(log4jFile, fullcode);
	}

	/**
	 * 创建log4j文件
	 * 
	 * @param log4jFile
	 */
	private static void buildLog4jFile(File log4jFile, String fullcode)
	{
		try
		{
			Properties properties = getPropertie();
			StringBuilder addCode = new StringBuilder();
			String LOG_HOME = properties.getProperty("LOG_HOME");
			if (LOG_HOME == null)
			{
				return;
			}
			StringBuilder logbackXML = new StringBuilder();
			for (Object temp : properties.keySet())
			{
				String key = temp.toString();
				if (key.startsWith("LOG4J_"))
				{
					String value = properties.getProperty(key);
					String[] data = value.split("#");
					String log4j_name = data[0];
					String name = createLog4jName(log4j_name);
					addCode.append("\n\tstatic Logger " + name.toUpperCase() + " = LoggerFactory.getLogger(\""
							+ log4j_name + "\");\n");
					logbackXML.append(addPieceLog4j(data));
				}
			}
			if (addCode.length() > 0)
			{
				if (fullcode == null)
				{
					fullcode = readFullCode("log4j.code");
				}
				fullcode = fullcode.replaceAll("(?<=(public interface NAME))[^}]+", "{" + addCode.toString());
				XwhTool.writeFile(log4jFile.getPath(), fullcode);
				saveBuiderXML(LOG_HOME, logbackXML.toString());
			}

		} catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	/**
	 * 获取别名参数值
	 * 
	 * @param log4j_name
	 * @return
	 */
	private static String createLog4jName(String log4j_name)
	{
		return XwhTool.addStrUnderlineByLastLen(log4j_name, 3);
	}

	/**
	 * 读取模板代码
	 * 
	 * @param code
	 * @return
	 * @throws IOException
	 */
	public static String readFullCode(String code) throws IOException
	{
		InputStream in = BuildCode.class.getClassLoader().getResourceAsStream(code);
		return XwhTool.readInputStream(in);
	}

	/**
	 * 进行保存log4j配置文件
	 * 
	 * @param lOG_HOME
	 * @param logbackXML
	 * @throws IOException
	 */
	private static void saveBuiderXML(String lOG_HOME, String logbackXML) throws IOException
	{
		String logback = readFullCode("logback.code");
		String fullcode = logback.replace("#LOG_HOME#", lOG_HOME).replace("#CODE#", logbackXML);
		String path = System.getProperty("user.dir");
		// 进行检测LOG4J.java 是否存在
		File configPath = new File(path, "config");
		if (!configPath.exists())
		{
			System.err.println("请创建名为config的source Folder文件夹!");
		} else
		{
			File saveFile = new File(configPath, "logback.xml");
			XwhTool.writeFile(saveFile.getPath(), fullcode);
		}
	}

	/**
	 * 
	 * @param data
	 * @return
	 * @throws IOException
	 */
	private static String addPieceLog4j(String[] data) throws IOException
	{
		String maxHistory = "";
		String maxFileSize = "";
		String pattern = "%msg%n";
		String config_name = null;
		String ref = null;
		String save_path = null;
		String temp_path = null;
		String level = null;
		config_name = data[0];
		ref = config_name.substring(0, config_name.length() - 3);
		level = data[1];
		if (!data[2].equals("0"))
		{
			maxFileSize = "<timeBasedFileNamingAndTriggeringPolicy class=\"ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP\"><maxFileSize>"
					+ data[2] + "</maxFileSize></timeBasedFileNamingAndTriggeringPolicy>";
		}

		if (!data[3].equals("0"))
		{
			/**
			 * <maxHistory>7</maxHistory>
			 * <timeBasedFileNamingAndTriggeringPolicy
			 * class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP"> <!--
			 * or whenever the file size reaches 100MB -->
			 * <maxFileSize>100MB</maxFileSize>
			 * </timeBasedFileNamingAndTriggeringPolicy>
			 */
			maxHistory = "<maxHistory>" + data[3] + "</maxHistory>";
		}
		temp_path = File.separator + config_name + File.separator + ref + ".log";
		save_path = File.separator + config_name + File.separator + ref + "_%d{yyyyMMdd}.%i.log.zip";
		if (data.length == 5)
		{
			pattern = data[4];
		}
		String piece = readFullCode("logback_piece.code") + "\n";
		return piece.replace("#MAXHISTORY#", maxHistory).replace("#MAXFILESIZE#", maxFileSize)
				.replace("#PATTERN#", pattern).replace("#CONFIG_NAME#", config_name).replace("#REF#", ref)
				.replace("#SAVE_PATH#", save_path).replace("#TEMP_PATH#", temp_path).replace("#LEVEL#", level);
	}

	/**
	 * 进行解析配置文件
	 * 
	 * @throws IOException
	 */
	private static Properties getPropertie() throws IOException
	{
		InputStream in = BuildCode.class.getClassLoader().getResourceAsStream("build_config.properties");
		Properties properties = new Properties();
		properties.load(in);
		in.close();
		return properties;
	}
}
