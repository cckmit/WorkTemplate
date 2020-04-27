package com.tools;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.annotation.KeyAuto;
import com.annotation.PrimaryKey;
import com.annotation.ReadOnly;
import com.annotation.TableName;
import com.annotation.TableSub;
import com.annotation.Timing;
import com.tools.log4j.Log4j;

/**
 * 通过entity注释将resultSet数据解析
 * 
 * @author Host-0222
 * 
 */
public abstract class XWHResultSetMapper
{
	/**
	 * 添加缓存数据参数
	 */
	ConcurrentHashMap<String, ConcurrentHashMap<String, Object>> cacheMap = new ConcurrentHashMap<>();

	// 不定时刷新缓存
	ConcurrentHashMap<String, Object> untimeCache = new ConcurrentHashMap<>();

	// 定时刷新缓存
	ConcurrentHashMap<String, Object> timingCache = new ConcurrentHashMap<>();

	// 定时刷新进程
	private TimingThread timingRunable = null;

	// 数据库表群信息
	public Set<String> showTables = null;

	/**
	 * 销毁
	 */
	public void destory()
	{
		for (String classname : timingCache.keySet())
		{
			try
			{
				Class<?> outclass = Class.forName(classname);
				if (outclass.isAnnotationPresent(Timing.class))
				{
					Object object = timingCache.get(classname);
					Method method = outclass.getDeclaredMethod("Timing", object.getClass());
					if (method != null)
					{
						method.invoke(null, object);
					}
				}
			} catch (Exception e)
			{
				// TODO: handle exception
			}

		}
	}

	/**
	 * 定时刷新缓存进程,1分钟为基本单位
	 * 
	 * @author Host-0222
	 * 
	 */
	class TimingThread implements Runnable
	{

		@Override
		public void run()
		{
			while (true)
			{
				try
				{
					flushTiming();
				} catch (Exception e)
				{

				} finally
				{
					try
					{
						Thread.sleep(1 * 60 * 1000);
					} catch (InterruptedException e1)
					{
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		}

		/**
		 * 进行定时器刷新机制
		 */
		private void flushTiming()
		{
			Calendar calendar = Calendar.getInstance();
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			int hour = calendar.get(Calendar.HOUR_OF_DAY);
			int month = calendar.get(Calendar.MONTH);
			int minute = calendar.get(Calendar.MINUTE);
			Iterator<Entry<String, Object>> it = timingCache.entrySet().iterator();
			while (it.hasNext())
			{
				Entry<String, Object> entry = it.next();
				String classname = entry.getKey();
				try
				{
					Class<?> outclass = Class.forName(classname);
					if (outclass.isAnnotationPresent(Timing.class))
					{
						Timing timing = outclass.getAnnotation(Timing.class);
						int temp_day = timing.day();
						int temp_hour = timing.hour();
						int temp_minute = timing.minute();
						int temp_month = timing.month();
						// 进行一级匹配,月匹配
						if (temp_month <= 0 || temp_month == month)
							// 进行二级匹配,天匹配.
							if (temp_day <= 0 || temp_day == day)
								// 进行3级匹配,小时匹配.一种是间隔小时(注:天必须配置为-2),一种是小时匹配
								if (temp_hour < 0 || (temp_day == -1 && temp_hour == hour)
										|| (hour > 0 && temp_day == -2 && temp_hour / hour == 0))
									// 进行4级匹配,分钟匹配.一种是间隔小时(注:天必须配置为-2),一种是小时匹配
									if (temp_minute < 0 || (temp_day == -1 && temp_minute == minute)
											|| (minute > 0 && temp_day == -2 && temp_minute / minute == 0))
									{
										Object object = timingCache.get(classname);

										Method method = outclass.getDeclaredMethod("Timing", object.getClass());
										if (method != null)
										{
											boolean result = (Boolean) method.invoke(null, object);
											if (result)
												timingCache.remove(classname);
										}
									}
					}
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 进行刷新缓存
	 */
	public boolean flushCache(String className)
	{
		for (Class<?> outclass : getCacheClass())
		{
			if (outclass.getSimpleName().equalsIgnoreCase(className))
			{
				setCacheByClassName(outclass);
				return true;
			}
		}
		return false;
	}

	/**
	 * 清理非定时缓存
	 * 
	 * @return
	 */
	public boolean flushUntimeCache()
	{
		untimeCache.clear();
		return true;
	}

	/**
	 * 缓存机制 :1.永不刷新缓存 2.不定时刷新缓存 3.定时修改缓存
	 */
	/**
	 * 缓存机制1:永不刷新修改缓存
	 * 
	 * @throws Exception
	 */
	protected void initCache() throws Exception
	{
		cacheMap.clear();
		for (Class<?> outclass : getCacheClass())
		{
			setCacheByClassName(outclass);
		}
	}

	/**
	 * 通过类进行设置缓存数据
	 * 
	 * @param outclass
	 */
	private void setCacheByClassName(Class<?> outclass)
	{
		String classkey = outclass.getName().toLowerCase();
		cacheMap.remove(classkey);
		Field[] fields = outclass.getFields();
		// 获取主键属性结构
		Vector<Field> primaryKey = new Vector<>();
		for (Field field : fields)
		{
			if (field.isAnnotationPresent(PrimaryKey.class))
			{
				primaryKey.add(field);
			}
		}
		if (primaryKey.isEmpty())
		{
			return;
		}
		ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<>();
		String tablename = outclass.getSimpleName().toLowerCase();
		String sql = "select * from " + tablename;
		for (Object value : findBySQL(sql, outclass))
		{
			// 获取主键信息,进行配置缓存信息
			StringBuilder key = new StringBuilder();
			try
			{
				for (Field field : primaryKey)
				{
					String colums = field.getName().toLowerCase();
					String columsValue = field.get(value).toString().trim();
					if (key.length() > 0)
					{
						key.append(".");
					}
					key.append(colums + "_" + columsValue);
				}
				Log4j.NAME.EXCEPTION_LOG.debug(key.toString());
				map.put(key.toString(), value);
			} catch (Exception e)
			{
				e.printStackTrace();
				return;
			}
		}
		if (!map.isEmpty())
		{
			cacheMap.put(classkey, map);
		}
	}

	/**
	 * 进行获取需要添加缓存的class信息
	 * 
	 * @return
	 */
	public abstract Vector<Class<?>> getCacheClass();

	/**
	 * 进行设置缓存数据
	 * 
	 * @param outclass
	 * @param t
	 * @throws Exception
	 */
	public synchronized void putCache(Class<?> outclass, Object t, String[]... keys) throws Exception
	{
		if (keys == null)
			return;
		String classkey = outclass.getName().toLowerCase();
		ConcurrentHashMap<String, Object> map = cacheMap.get(classkey);
		try
		{
			if (map == null)
			{
				map = new ConcurrentHashMap<>();
			}
			StringBuilder keyBuilder = new StringBuilder();
			for (String[] key : keys)
			{
				if (keyBuilder.length() > 0)
				{
					keyBuilder.append(".");
				}
				keyBuilder.append(key[0] + "_" + key[1]);
			}
			map.put(keyBuilder.toString(), t);
			cacheMap.put(classkey, map);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 进行获取缓存数据
	 * 
	 * @param outClass
	 * @param key
	 * @return
	 * @throws Excep
	 */
	public synchronized Object getCacheKey(Class<?> outclass, String[]... keys) throws Exception
	{
		String classkey = outclass.getName().toLowerCase();
		ConcurrentHashMap<String, Object> map = cacheMap.get(classkey);
		if (map == null)
		{
			return null;
		}
		if (keys == null || keys.length == 0)
		{
			return map;
		}
		try
		{
			StringBuilder keyBuilder = new StringBuilder();
			for (String[] key : keys)
			{
				if (keyBuilder.length() > 0)
				{
					keyBuilder.append(".");
				}
				keyBuilder.append(key[0] + "_" + key[1]);
			}
			return map.get(keyBuilder.toString());
		} catch (Exception e)
		{
			throw e;
		}
	}

	/**
	 * 获取不定时缓存
	 * 
	 * @param key
	 * @return
	 */
	public synchronized Object getUntimeCache(String key)
	{
		return untimeCache.get(key);
	}

	/**
	 * 设置不定时缓存
	 * 
	 * @param <T>
	 * 
	 * @param key
	 * @param object
	 */
	public synchronized <T> void putUntimeCache(String key, T t)
	{
		untimeCache.put(key, t);
	}

	/**
	 * 获取不定时缓存
	 * 
	 * @param key
	 * @return
	 */
	public synchronized Object getTimingCache(Class<?> outclass)
	{
		return timingCache.get(outclass.getName());
	}

	/**
	 * 设置定时缓存
	 * 
	 * @param <T>
	 * 
	 * @param key
	 * @param object
	 * @throws Exception
	 */
	public synchronized void putTimingCache(Class<?> outclass, Object objects) throws Exception
	{
		if (!outclass.isAnnotationPresent(Timing.class))
		{
			throw new Exception("该类无法进行添加定时缓存");
		}
		String key = outclass.getName();
		timingCache.put(key, objects);
		if (timingRunable == null)
		{
			timingRunable = new TimingThread();
			System.out.println("进程启动:" + TimingThread.class.getName());
			new Thread(timingRunable).start();
		}
	}

	/**
	 * 获取数据库别名
	 * 
	 * @return
	 */
	public abstract String alias();

	/**
	 * 进行获取查询条件
	 * 
	 * @param conn
	 * 
	 * @param alias
	 *            数据库别名
	 * @param sql
	 *            查询SQL
	 * @return
	 */
	public abstract ResultSet setResultSet(Connection conn, String sql);

	/**
	 * 进行关闭SQL
	 * 
	 * @param conn
	 */
	public abstract void closeConnection(Connection conn);

	/**
	 * 进行开启SQL
	 */
	public abstract Connection openConnection();

	/**
	 * 进行查找表群信息
	 */
	public void showTable()
	{
		showTables = new HashSet<>();
		String SQL = "show tables";
		Connection conn = null;
		try
		{
			conn = openConnection();
			ResultSet rs = setResultSet(conn, SQL);
			while (rs.next())
			{
				showTables.add(rs.getString(1));
			}
			rs.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
			closeConnection(conn);
		}
	}

	/**
	 * 插入后返回插入的自增id
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public int insertAndReturnId(String sql) { 
	    Connection conn = null; 
	    PreparedStatement ps = null; 
	    ResultSet rs = null; 
	    int id = -1; 
	    try { 
	        conn = openConnection(); 
	        ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS); 
	        ps.executeUpdate();

	        rs = ps.getGeneratedKeys(); 
	        if (rs.next()){ 
	        	id = rs.getInt(1); 
	        }
	    }
		catch (SQLException e){
			e.printStackTrace();
		} finally { 
	    	closeConnection(conn);
	    	if(ps != null){
	    		try{
					ps.close();
				}
				catch (SQLException e){
					e.printStackTrace();
				}
	    	}
	    } 
	    return id; 
	}
	
	/**
	 * 数据库进行新增
	 * 
	 * @param <T>
	 * 
	 * @throws Exception
	 */
	public <T> void saveOrUpdate(T t) throws Exception
	{
		Vector<T> vector = new Vector<>();
		vector.add(t);
		saveOrUpdate(vector);
	}

	/**
	 * 检测表列信息
	 * 
	 * @param key
	 * @return
	 */
	public boolean containsTable(String key)
	{
		if (showTables == null)
		{
			showTable();
		}
		return showTables.contains(key);
	}

	/**
	 * 数据库进行新增
	 * 
	 * @param <T>
	 * 
	 * @throws Exception
	 */
	public <T> void saveOrUpdate(Vector<T> ts) throws Exception
	{
		Map<String, SaveEntity> map = new HashMap<>();
		String classname = null;
		for (T bean : ts)
		{
			Class<?> outclass = bean.getClass();
			if (!outclass.isAnnotationPresent(Entity.class))
				return;
			String tablename = outclass.getSimpleName().toLowerCase();
			classname = tablename;
			if (outclass.isAnnotationPresent(TableSub.class))
			{
				Method tablesub = outclass.getDeclaredMethod("getTableSub");
				tablename += "_" + tablesub.invoke(bean);
			}
			if (outclass.isAnnotationPresent(TableName.class))
			{
				tablename = outclass.getAnnotation(TableName.class).value();
			}
			SaveEntity entity = map.get(tablename);
			if (entity == null)
			{
				entity = new SaveEntity(tablename);
				map.put(tablename, entity);
			}
			Field[] fields = outclass.getFields();
			StringBuilder top = new StringBuilder();
			StringBuilder center = new StringBuilder();
			StringBuilder end = new StringBuilder();
			for (Field field : fields)
			{
				if (field.isAnnotationPresent(Column.class) 
						&& !field.isAnnotationPresent(KeyAuto.class)
						&& !field.isAnnotationPresent(ReadOnly.class))
				{
					Column column = field.getAnnotation(Column.class);
					String name = column.name();
					Object columValue = field.get(bean);
					if (entity.insertTop == null)
					{
						if (top.length() > 0)
							top.append(",");
						top.append("`" + name + "`");
					}
					if (center.length() > 0)
						center.append(",");
					if (entity.update == null)
					{
						if (!field.isAnnotationPresent(ReadOnly.class))
						{
							if (end.length() > 0)
								end.append(",");
							end.append("`" + name + "`=values(`" + name + "`)");
						}
					}
					if (columValue != null)
					{
						Class<?> columClass = columValue.getClass();
						if (columClass == Integer.class || columClass == Double.class || columClass == Boolean.class
								|| columClass == Long.class || columClass == Float.class)
							center.append(columValue);
						else if (columClass == Date.class)
							center.append("'" + new SimpleDateFormat("yyyy-MM-dd").format(columValue) + "'");
						else
							center.append("'" + columValue + "'");
					} else
						center.append("NULL");
				}
			}
			if (entity.insertTop == null && top.length() > 0)
				entity.insertTop = top.toString();
			if (entity.update == null && end.length() > 0)
				entity.update = end.toString();
			if (center.length() > 0)
				entity.datas.addElement(center.toString());
		}

		if (!map.isEmpty())
		{
			for (String tablename : map.keySet())
			{
				if (showTables == null)
				{
					showTable();
				}
				if (!showTables.contains(tablename))
				{
					createTable(tablename, classname);
				}
				SaveEntity entity = map.get(tablename);
				StringBuilder sqlBuilder = new StringBuilder();
				sqlBuilder.append("insert into " + tablename + "(").append(entity.insertTop).append(")values");
				for (int i = 0; i < entity.datas.size(); i++)
				{
					String data = entity.datas.elementAt(i);
					if (i != 0)
						sqlBuilder.append(",");
					sqlBuilder.append("(" + data + ")");
				}
				sqlBuilder.append("  ON DUPLICATE KEY UPDATE ").append(entity.update);
				String SQL = sqlBuilder.toString();
				System.out.println(SQL);
				execSQLCMDInfo(SQL);
			}
		}
	}

	/**
	 * 保存实体类
	 * 
	 * @author Host-0222
	 * 
	 */
	public class SaveEntity
	{
		public SaveEntity(String tablename)
		{
			this.tablename = tablename;
		}

		public String tablename;

		public String insertTop = null;

		public Vector<String> datas = new Vector<>();

		public String update = null;
	}

	/**
	 * 进行创建表
	 * 
	 * @param name
	 * @param classname
	 */
	private void createTable(String name, String classname)
	{
		String SQL = "CREATE TABLE IF NOT EXISTS " + name + " LIKE " + classname;
		execSQLCMDInfo(SQL);
	}

	/**
	 * 执行操作结果
	 * 
	 * @param SQL
	 */
	public abstract int execSQLCMDInfo(String SQL);

	/**
	 * 通过SQL进行查询数据
	 * 
	 * @param <T>
	 * 
	 * @param alias
	 *            数据库别名
	 * @param sql
	 *            查询SQL
	 * @param a
	 * @return
	 */
	public <T> Vector<T> findBySQL(String sql, Class<T> a)
	{
		Connection conn = null;
		try
		{
			conn = openConnection();
			ResultSet resultSet = setResultSet(conn, sql);
			return mapRersultSetToObject(resultSet, a);
		} catch (Exception e)
		{
			throw e;
		} finally
		{
			closeConnection(conn);
		}
	}

	/**
	 * 通过主键进行查找数据
	 * 
	 * @param <T>
	 * 
	 * @param alias
	 *            数据库别名
	 * @param sql
	 *            查询SQL
	 * @param a
	 * @return
	 * @throws Exception
	 */
	public <T> T findById(Class<T> a, String tablename, String[]... ids) throws Exception
	{
		if (showTables == null)
		{
			showTable();
		}
		if (!showTables.contains(tablename))
			return null;
		StringBuilder builder = new StringBuilder();
		if (ids != null)
			for (String[] data : ids)
			{
				if (builder.length() > 0)
					builder.append(" and ");
				builder.append(data[0] + "='" + data[1] + "'");
			}
		if (builder.length() > 0)
			builder.insert(0, " where ");
		String sql = "select * from " + tablename + builder.toString();
		System.out.println(sql);
		Vector<T> list = findBySQL(sql, a);
		if (list != null && list.size() > 0)
		{
			return list.firstElement();
		}
		return null;
	}

	/**
	 * 进行创建一组实体类
	 * 
	 * @param <T>
	 * 
	 * @param rs
	 *            查找结果集
	 * @param outputClass
	 *            需要构建的实体类
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> Vector<T> mapRersultSetToObject(ResultSet rs, Class<?> outputClass)
	{
		Vector<T> outputList = new Vector<>();
		try
		{
			if (rs != null)
			{
				if (outputClass.isAnnotationPresent(Entity.class))
				{
					Field[] fields = outputClass.getDeclaredFields();
					while (rs.next())
					{
						// 进行构建一个实体类

						T bean = (T) outputClass.newInstance();
						// 进行赋值
						for (Field field : fields)
						{
							if (field.isAnnotationPresent(Column.class))
							{
								Column column = field.getAnnotation(Column.class);
								String name = column.name();
								Object columnValue = null;
								try {
									columnValue = rs.getObject(name);
								} catch (SQLException e) {
									//此处抛出SQL @Column列没查询到数据查询不全影响
								}
								if (columnValue != null)
								{
									if(columnValue instanceof java.sql.Date 
											&& field.getType().getName().equals("java.lang.String")){
										columnValue = rs.getString(name);
									}else if(columnValue instanceof BigDecimal){
										if(field.getType().getName().equals("java.lang.Integer")){
											columnValue = ((BigDecimal)columnValue).intValue();
										}else if(field.getType().getName().equals("java.lang.Float")){
											columnValue = ((BigDecimal)columnValue).floatValue();
										}else if(field.getType().getName().equals("java.lang.Double")){
											columnValue = ((BigDecimal)columnValue).doubleValue();
										}
									}else if(columnValue instanceof java.sql.Timestamp){
										columnValue = formatTimestamp(rs.getString(name));
									}else if(columnValue instanceof Long
											&& field.getType().getName().equals("java.lang.Integer")){
										columnValue = ((Long)columnValue).intValue();
									} else if(field.getType().getName().equals("java.lang.Float")){
										columnValue = Float.valueOf(columnValue.toString());
									}
									field.set(bean, columnValue);
								}
							}
						}
						outputList.add(bean);
					}
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return outputList;
	}
	
	/**
	 * 格式化时间戳类型，2017-06-14 18:44:29.0 转为 2017-06-14 18:44:29
	 * @param string
	 * @return
	 */
	private String formatTimestamp(String string) {
		if(string == null || string.isEmpty() || string.equals("null")){
			return "";
		}
		int strLen = string.length();	
		return string.substring(0, strLen - 2);
	}
}