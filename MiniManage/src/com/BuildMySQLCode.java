package com;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Vector;

import com.tools.XwhTool;

public class BuildMySQLCode
{
	// 检测是否替换文件
	private static boolean isReplace = true;

	public static void main(String[] args)
	{
		buildDB();
	}

	/**
	 * 进行建立数据库文件
	 */
	private static void buildDB()
	{
		try
		{
			String path = System.getProperty("user.dir");
			// 进行检测LOG4J.java 是否存在
			File srcPath = new File(path, "src");
			File dbFile = XwhTool.getSrcPath(srcPath, "OpDbConnector.java");
			File configPath = new File(path, "config");
			if (!configPath.exists())
			{
				System.err.println("请创建名为config的source Folder文件夹!");
				return;
			}

			String fullcode = null;
			if (dbFile != null && dbFile.exists())
			{
				// 进行清除NAME类中参数
				fullcode = XwhTool.readFileString(dbFile.getPath());
			} else
			{
				File tempDir = XwhTool.createNextDir(srcPath, "com", "tools", "db");
				dbFile = new File(tempDir, "OpDbConnector.java");
			}
			buildDBFile(dbFile, fullcode);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 创建DB文件
	 * 
	 * @param dbFile
	 */
	private static void buildDBFile(File dbFile, String fullcode)
	{
		try
		{
			Properties properties = getPropertie();
			StringBuilder addCode = new StringBuilder();
			String DB_FILE = properties.getProperty("DB_FILE");
			if (DB_FILE == null)
			{
				return;
			}
			StringBuilder dbXML = new StringBuilder();
			for (Object temp : properties.keySet())
			{
				String key = temp.toString();
				if (key.startsWith("DB_ALIAS_"))
				{
					String value = properties.getProperty(key);
					String[] data = value.split("##");
					String db_name = data[0];
					addCode.append("\n\tstatic final String " + db_name.toUpperCase() + "_ALIAS =\"" + db_name
							+ "\"; \n");
					dbXML.append(buildProxoolPiece(data));
				}
			}
			if (addCode.length() > 0)
			{
				if (fullcode == null)
				{
					fullcode = readFullCode("db_code.code");
				}
				String db_log4j = properties.getProperty("DB_LOG4J");
				db_log4j = XwhTool.addStrUnderlineByLastLen(db_log4j, 3).toUpperCase();
				fullcode = fullcode.replaceAll("SQL_ERROR_LOG", db_log4j);
				fullcode = fullcode.replaceAll("(?<=(public interface NAME))[^}]+",
						"{static final String DB_MASTER = \"" + DB_FILE + "\";");
				fullcode = fullcode.replaceAll("(?<=(public interface DB_ALIAS))[^}]+", "{" + addCode.toString());
				writeFile(dbFile, fullcode);
				buildDBXmlFile(DB_FILE, dbXML.toString());
			}

		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 进行生成文件
	 * 
	 * @param dbFile
	 * @param fullcode
	 */
	public static void writeFile(File dbFile, String fullcode)
	{
		if (dbFile.exists() && !isReplace)
			return;
		XwhTool.writeFile(dbFile.getPath(), fullcode);
	}

	/**
	 * 进行创建数据库实体类
	 * 
	 * @param fullcode
	 * @param password
	 * @param user
	 * @param driver_url
	 * @throws ClassNotFoundException
	 */
	private static void buildeDBClass(String db_alias, String driver_url, String user, String password)
			throws ClassNotFoundException
	{
		Class.forName("com.mysql.jdbc.Driver");
		try
		{
			Connection con = DriverManager.getConnection(driver_url, user, password);
			int lastindex = driver_url.lastIndexOf("/");
			StringBuilder dbName = getDbName(driver_url, lastindex);
			String lowerDbName = dbName.toString().toLowerCase();
			String lowerDbNameFirstCh = String.valueOf(lowerDbName.charAt(0));
			String camelDbName = lowerDbName.replace(lowerDbNameFirstCh, lowerDbNameFirstCh.toUpperCase());
			String dbClassName = camelDbName + "Dao";
			String fullcode = readFullCode("db_dao.code").replaceAll("DBNAME", db_alias.toUpperCase() + "_ALIAS")
					.replaceAll("TemplateDao", dbClassName);
			// 进行保存文件路径
			String path = System.getProperty("user.dir");
			// 进行检测LOG4J.java 是否存在
			File srcPath = new File(path, "src");
			File tempDir = XwhTool.createNextDir(srcPath, "com", "code", "dao");
			File dbFile = new File(tempDir, dbClassName + ".java");
			writeFile(dbFile, fullcode);
			// 进行生成类信息
			Vector<String> tables = getAllTables(con);
			
			tempDir = XwhTool.createNextDir(srcPath, "com", "code", "entity", camelDbName);
			for (String table : tables)
			{
				String sql = "SHOW FULL FIELDS FROM " + table;
				System.out.println(sql);
				Statement gstate = con.createStatement();
				Vector<String> importedKeys = new Vector<>();
				Vector<String> colnames = new Vector<>();
				Vector<String> colTypes = new Vector<>();
				Vector<String> comments = new Vector<>();
				ResultSet fieldsrs = gstate.executeQuery(sql);
				boolean f_util = false; // 是否需要导入包java.util.*
				boolean f_sql = false; // 是否需要导入包java.sql.*
				while (fieldsrs.next())
				{
					String field = fieldsrs.getString("Field");
					String type = fieldsrs.getString("Type");
					String key = fieldsrs.getString("Key");
					String comment = fieldsrs.getString("Comment");
					if ("PRI".equals(key))
					{
						importedKeys.add(field);
					}
					if (existSame(type, "date"))
					{
						f_util = true;
					}
					if (existSame(type, "timestamp") || existSame(type, "image"))
					{
						f_sql = true;
					}
					colnames.add(field);
					colTypes.add(type);
					comments.add(comment);
				}
				String content = parse("com.code.entity." + lowerDbName, table, colnames, colTypes, importedKeys,
						comments, f_util, f_sql);
				File classFile = new File(tempDir, initcap(table) + ".java");
				writeFile(classFile, content);
			}
			con.close();
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 进行创建Servlet类
	 * 
	 * @param fullcode
	 * @param password
	 * @param user
	 * @param driver_url
	 * @throws ClassNotFoundException
	 */
	private static void buildeServletClass(String db_alias, String driver_url, String user, String password)
			throws ClassNotFoundException
			{
		Class.forName("com.mysql.jdbc.Driver");
		try
		{
			Connection con = DriverManager.getConnection(driver_url, user, password);
			int lastindex = driver_url.lastIndexOf("/");
			StringBuilder dbName = getDbName(driver_url, lastindex);
			String lowerDbName = dbName.toString().toLowerCase();
			String camelDbName = initcap(lowerDbName);
			String dbClassName = camelDbName + "Dao";
			
			// 进行保存文件路径
			String path = System.getProperty("user.dir");
			// 进行检测LOG4J.java 是否存在
			File srcPath = new File(path, "src");
			File tempDir = XwhTool.createNextDir(srcPath, "com", "code", "ui", "servlet");
			// 进行生成类信息
			Vector<String> tables = getAllTables(con);
			
			for (String table : tables)
			{
				String sql = "SHOW FULL FIELDS FROM " + table;
				String priKey = null;
				Statement gstate = con.createStatement();
				ResultSet fieldsrs = gstate.executeQuery(sql);
				while (fieldsrs.next())
				{
					String field = fieldsrs.getString("Field");
					if(priKey == null){
						priKey = field;
					}
					String key = fieldsrs.getString("Key");
					if ("PRI".equals(key))
					{
						priKey = field;
						break;
					}
				}
				String fullcode = readFullCode("servlet_template.code")
						.replaceAll("#Entity#", initcap(table))
						.replaceAll("#DBClassName#", dbClassName)
						.replaceAll("#TableName#", table)
						.replaceAll("#DbName#", db_alias)
						.replaceAll("#Class#", initcap(table.replace("_", "")))
						.replaceAll("#PriKey#", priKey);
				
				File classFile = new File(tempDir, initcap(table.replace("_", ""))+"Servlet" + ".java");
				writeFile(classFile, fullcode);
			}
			con.close();
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 获取数据库所有表
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	private static Vector<String> getAllTables(Connection con)
			throws SQLException {
		Vector<String> tables = new Vector<>();
		java.sql.Statement statement = con.createStatement();
		ResultSet rs = statement.executeQuery("SHOW TABLES;");
		while (rs.next())
		{
			String name = rs.getString(1);
			tables.add(name);
		}
		return tables;
	}

	/**
	 * 获取数据库名称
	 * @param driver_url
	 * @param lastindex
	 * @return
	 */
	private static StringBuilder getDbName(String driver_url, int lastindex) {
		StringBuilder builder = new StringBuilder();
		for (int i = lastindex + 1; i < driver_url.length(); i++)
		{
			char c = driver_url.charAt(i);
			if (c == '?')
			{
				break;
			}
			if (builder.length() == 0)
			{
				if (c >= 'a' && c <= 'z')
					c = (char) (c - 32);
			}
			builder.append(c);
		}
		return builder;
	}

	/**
	 * 功能：生成实体类主体代码
	 * 
	 * @param colnames
	 * @param colTypes
	 * @param colSizes
	 * @return
	 */
	private static String parse(String packageOutPath, String tablename, Vector<String> colnames,
			Vector<String> colTypes, Vector<String> importedKeys, Vector<String> comments, boolean f_util, boolean f_sql)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("package " + packageOutPath + ";\r\n");
		// 判断是否导入工具包
		if (f_util)
		{
			sb.append("import java.util.Date;\r\n");
		}
		if (f_sql)
		{
			sb.append("import java.sql.*;\r\n");
		}
		sb.append("import javax.persistence.Entity;\r\n");
		sb.append("import javax.persistence.Column;\r\n");
		sb.append("import com.annotation.PrimaryKey;\r\n");
		sb.append("import com.annotation.Comments;\r\n");
		sb.append("import com.annotation.Page;\r\n");
		sb.append("\r\n");
		// 注释部分
		sb.append("   /**\r\n");
		sb.append("    * " + tablename + " 实体类\r\n");
		sb.append("    * " + new java.sql.Date(System.currentTimeMillis()) + " xuweihua\r\n");
		sb.append("    */ \r\n");
		// 实体部分
		sb.append("\r\n@Entity\r\n@Page\r\npublic class " + initcap(tablename) + "\r\n{\r\n");
		processAllAttrs(sb, colnames, colTypes, importedKeys, comments);// 属性
		processAllMethod(sb, colnames, colTypes);// get set方法
		sb.append("}\r\n");

		// System.out.println(sb.toString());
		return sb.toString();
	}

	/**
	 * 功能：生成所有属性
	 * 
	 * @param sb
	 * @param colTypes
	 * @param colnames
	 * @param importedKeys
	 */
	private static void processAllAttrs(StringBuffer sb, Vector<String> colnames, Vector<String> colTypes,
			Vector<String> importedKeys, Vector<String> comments)
	{

		for (int i = 0; i < colnames.size(); i++)
		{
			String colname = colnames.elementAt(i);
			if (importedKeys.contains(colname))
			{
				sb.append("\t@PrimaryKey\r\n");
			}
			sb.append("\t@Column(name=\"" + colname + "\")\r\n");
			sb.append("\t@Comments(name=\"" + comments.elementAt(i) + "\")\r\n");
			sb.append("\tpublic " + sqlType2JavaType(colTypes.elementAt(i)) + " " + colname + ";\r\n");
		}
	}

	/**
	 * 功能：生成所有方法
	 * 
	 * @param sb
	 * @param colTypes
	 * @param colnames
	 */
	private static void processAllMethod(StringBuffer sb, Vector<String> colnames, Vector<String> colTypes)
	{

		for (int i = 0; i < colnames.size(); i++)
		{
			String colname = colnames.elementAt(i);
			String coltype = colTypes.elementAt(i);
			sb.append("\tpublic void set" + initcap(colname) + "(" + sqlType2JavaType(coltype) + " " + colname
					+ ")\r\n\t{\r\n");
			sb.append("\t\t" + "this." + colname + "=" + colname + ";\r\n");
			sb.append("\t}\r\n\r\n");
			sb.append("\tpublic " + sqlType2JavaType(coltype) + " get" + initcap(colname) + "()\r\n\t{\r\n");
			sb.append("\t\treturn " + colname + ";\r\n");
			sb.append("\t}\r\n\r\n");
		}

	}

	/**
	 * 功能：将输入字符串的首字母改成大写
	 * 
	 * @param str
	 * @return
	 */
	private static String initcap(String str)
	{

		char[] ch = str.toCharArray();
		if (ch[0] >= 'a' && ch[0] <= 'z')
		{
			ch[0] = (char) (ch[0] - 32);
		}

		return new String(ch);
	}

	/**
	 * 功能：获得列的数据类型
	 * 
	 * @param sqlType
	 * @return
	 */
	private static String sqlType2JavaType(String sqlType)
	{

		if (existSame(sqlType, "bit") || existSame(sqlType, "tinyint"))
		{
			return "boolean";
		} else if (existSame(sqlType, "smallint"))
		{
			return "short";
		} else if (existSame(sqlType, "int"))
		{
			return "int";
		} else if (existSame(sqlType, "bigint"))
		{
			return "long";
		} else if (existSame(sqlType, "timestamp"))
		{
			return "Timestamp";
		} else if (existSame(sqlType, "float"))
		{
			return "float";
		} else if (existSame(sqlType, "decimal") || existSame(sqlType, "numeric") || existSame(sqlType, "real")
				|| existSame(sqlType, "money") || existSame(sqlType, "smallmoney"))
		{
			return "double";
		} else if (existSame(sqlType, "varchar") || existSame(sqlType, "char") || existSame(sqlType, "nvarchar")
				|| existSame(sqlType, "nchar") || existSame(sqlType, "text") || existSame(sqlType, "longtext"))
		{
			return "String";
		} else if (existSame(sqlType, "datetime") || existSame(sqlType, "date"))
		{
			return "Date";
		} else if (existSame(sqlType, "image"))
		{
			return "Blod";
		}
		return null;
	}

	/**
	 * 检测是否相同
	 * 
	 * @param sqlType
	 * @param value
	 * @return
	 */
	private static boolean existSame(String sqlType, String value)
	{
		return sqlType.toLowerCase().startsWith(value);
	}

	/**
	 * 进行建立数据库配置文件
	 * 
	 * @param db_file
	 * @param proxxols
	 * @throws IOException
	 */
	private static void buildDBXmlFile(String db_file, String proxxols) throws IOException
	{
		String fullcode = readFullCode("db.code");
		fullcode = fullcode.replace("#PROXOOLS#", proxxols);
		String path = System.getProperty("user.dir");
		// 进行检测LOG4J.java 是否存在
		File configPath = new File(path, "config");
		if (!configPath.exists())
		{
			System.err.println("请创建名为config的source Folder文件夹!");
		} else
		{
			File saveFile = new File(configPath, db_file);
			writeFile(saveFile, fullcode);
		}
	}

	/**
	 * 配置数据库连接池
	 * 
	 * @param datas
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private static String buildProxoolPiece(String[] datas) throws IOException, ClassNotFoundException
	{
		String fullcode = readFullCode("db_proxool.code") + "\n";
		String db_alias = datas[0];
		String driver_url = datas[1];
		String user = datas[2];
		String password = datas[3];
		buildeDBClass(db_alias, driver_url, user, password);
		buildeServletClass(db_alias, driver_url, user, password);
		return fullcode.replace("#DB_ALIAS#", db_alias).replace("#DRIVER_URL#", driver_url).replace("#USER#", user)
				.replace("#PASSWORD#", password);
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
		InputStream in = BuildMySQLCode.class.getClassLoader().getResourceAsStream(code);
		return XwhTool.readInputStream(in);
	}

	/**
	 * 进行解析配置文件
	 * 
	 * @throws IOException
	 */
	private static Properties getPropertie() throws IOException
	{
		InputStream in = BuildMySQLCode.class.getClassLoader().getResourceAsStream("build_config.properties");
		Properties properties = new Properties();
		properties.load(in);
		in.close();
		return properties;
	}
}
