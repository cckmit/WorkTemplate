package com.code.ui;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.alibaba.fastjson.JSONObject;
import com.annotation.Comments;
import com.annotation.NonExcel;
import com.code.dao.MiniGamebackDao;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tools.XwhHttp;
import com.tools.XwhTool;
import com.tools.XwhXorSecurity;
import com.tools.config.ReadConfig;
import com.tools.log4j.Log4j;

@WebServlet(urlPatterns = { "/init", "/pages/init" })
public class UIMoudleServlet extends HttpServlet {

	/**
	 * 查询结果
	 */
	private String searchValue;

	/**
	 * 查询条件
	 */
	private String searchName;

	/**
	 * 分页参数 - 第几页
	 */
	private int page;

	/**
	 * 分页参数 - 本页多少行
	 */
	private int rows;
	/**
	 * 操作类型
	 */
	private String operator;
	/**
	 * 操作时间
	 */
	private String st;
	private String et;

	private String sort;
	private String order;

	public String getSearchValue() 
	{
		return searchValue;
	}

	public String getSearchName() 
	{
		return searchName;
	}

	public int getPage() 
	{
		return page;
	}

	public int getRows() 
	{
		return rows;
	}

	public String getOperator() 
	{
		return operator;
	}

	public String getSt() 
	{
		return st;
	}

	public String getEt() 
	{
		return et;
	}

	public String getSort() 
	{
		return sort;
	}

	public String getOrder() 
	{
		return order;
	}
	/**
	 * 记录统计数据结果
	 */
	public static HashMap<String,String> noticeMap=new HashMap<String,String>();
	
	// 条件数据输入参数
	protected HttpServletRequest request;
	// 条件数据输出参数
//	protected HttpServletResponse response;

	protected Vector<?> excelSaveData;

	private static final long serialVersionUID = -5715720217371529888L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
			this.request = request;
			String type = request.getParameter("datagrid");
			if (type != null) {
				if ("data".equals(type)) {
					searchValue = request.getParameter("searchvalue");
					searchName = request.getParameter("searchname");
					page = request.getParameter("page") != null ? Integer
							.valueOf(request.getParameter("page")) : 1;
					rows = request.getParameter("rows") != null ? Integer
							.valueOf(request.getParameter("rows")) : 10;
					st = request.getParameter("st");
					et = request.getParameter("et");
					sort = request.getParameter("sort");
					order = request.getParameter("order");
					sendResult(getColumText(request), response);
				} else if ("export".equals(type)) {
					exportExcel(request, response);
				} else if ("pie_chart".equals(type)) {
					showPieChart(request, response);
				} else if ("line_chart".equals(type)) {
					sendResult(new Object(), response);
				} else if ("spec_json".equals(type)) {
					String json = doSpecJson(response);
					sendResult(json, response);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 处理获取特殊json的逻辑
	 * @param request
	 * @param response
	 */
	public String doSpecJson(HttpServletResponse response) {
		return "";
	}

	/**
	 * 展示饼状图
	 * @param request2
	 * @param response
	 */
	public void showPieChart(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException{
		
	}

	/**
	 * 进行构造信息
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		this.request = request;
		String type = request.getParameter("type");
		if (type != null && "excelsave".equals(type)) 
		{
			String content = request.getParameter("data");
			System.out.println(content);
			excelSaveData = new GsonBuilder().setDateFormat(getGsonFormat())
					.create().fromJson(content, getTypeof());
			excelSaveData(request, response,excelSaveData);

		} else if ("flush".equals(type)) 
		{
			String url = flushLogicServerURL();
			if (url != null) {
				url = ReadConfig.get("flushURL") + url;
				flushCache(response, url);
			}
		}else if ("flushGet".equals(type)) 
		{
			//String url = flushLogicServerURL();
			String url = request.getParameter("url");
			if (url != null) {
				//url = ReadConfig.get("flushURL") + url;
				String daily = request.getParameter("daily");
				if(daily!=null&&"1".equals(daily))
				{
					String date = request.getParameter("date");
					String condition="";
					String[] dates=date.split("_");
					if(dates.length==2&&!"".equals(dates[0])
							&&!"".equals(dates[1]))
					{
						long st=XwhTool.getMillisecondDateTime(dates[0])/1000;
						long et=XwhTool.getMillisecondDateTime(dates[1])/1000;
						condition+="?st="+st+"&et="+et;
					}
					url+=condition;
					flushCache(response, url);
					noticeMap.put(url, XwhTool.getCurrentDateTimeValue()+"&&已通知，待处理回复！");
				}else
				{
					flushCache(response, url);
				}
			}
		}else if ("flushPost".equals(type)) 
		{
			//String url = flushLogicServerURL();
			String url = request.getParameter("url");
			String state = request.getParameter("state");
			if(state!=null)
			{
				switch(Integer.parseInt(state)) {
					case 1:
					break;
					case 2:
					JSONObject jsonObject=SavePosition();
					flushCachePost(response, url,jsonObject.toJSONString());
					break;
				}
			}
		}else if("otherPost".equals(type))
		{
			otherPost(request, response);
		}
		else
			sendResult(doOperator(request), response);
	}
	/**
	 * 商店配置保定位置
	 * @param request
	 * @param response
	 */
	public JSONObject SavePosition() {
		return new JSONObject();
	}
	protected Type getTypeof() 
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 导出excel
	 * @param request
	 * @param response
	 */
	public void exportExcel(HttpServletRequest request, HttpServletResponse response)
	{
		Vector<?> vector = findData();
		Vector<Object> list = new Vector<>();
		//excel文件名称
		String filename = get("filename");
		if(vector != null)
		{
			list.addAll(vector);
			vector.clear();
		}
		try
		{
			response.setHeader(
					"content-disposition",
					"attachment;" + "filename="
							+ URLEncoder.encode(filename + ".xlsx", "UTF-8"));
			OutputStream out = response.getOutputStream();
			// 第一步，创建一个wb，对应一个Excel文件
			XSSFWorkbook wb = new XSSFWorkbook();
			// 第二步，在wb中添加一个sheet,对应Excel文件中的sheet
			XSSFSheet sheet = wb.createSheet(filename);
			//生成表头
			XSSFRow row = sheet.createRow((int) 0);
			// 第四步，创建单元格，并设置值表头 设置表头居中
			XSSFCellStyle style = wb.createCellStyle();
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
			XSSFCell cell = null;
			Class<?> c = list.size() > 0 ? list.get(0).getClass() : getClass();
			Field[] fields = c.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) 
			{
				Field field = fields[i];
				if(field.isAnnotationPresent(Comments.class))
				{
					boolean isNonExcel = field.isAnnotationPresent(NonExcel.class);
					cell = row.createCell(i);
					cell.setCellStyle(style);
					sheet.setColumnWidth(i, isNonExcel ? 0 : 8000);
					Comments comment = field.getAnnotation(Comments.class);
					cell.setCellValue(isNonExcel ? "" : comment.name());
				}
			}
			int length = list.size();
			if(length > 0)
			{
				for (int i = length - 1; i >= 0; i--) 
				{
					row = sheet.createRow((int) i + 1);
					Field[] fls = list.get(i).getClass().getDeclaredFields();
					// 第六步，创建单元格，并设置值
					for (int j = 0; j < fls.length; j++) 
					{
						if(fls[j].isAnnotationPresent(NonExcel.class)){
							continue;
						}
						fls[j].setAccessible(true);
						String v = "";
						Object vo = fls[j].get(list.get(i));
						if (vo == null) 
							v = "";
						else 
							v = vo.toString();
						row.createCell(j).setCellValue(v);
					}
					list.removeElementAt(i);
					if (i % 10000 == 0)
						System.out.println("正在艰难的输出 excel中...,剩余条数：" + i);
				}
			}
			wb.write(out);
			out.close();
		} catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
			Log4j.NAME.EXCEPTION_LOG.debug(Log4j.getExceptionInfo(e));
		}
	}

	/**
	 * 刷新缓存
	 * 
	 * @param response
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 */
	protected void flushCache(HttpServletResponse response, String flushurl)
			throws IOException, UnsupportedEncodingException 
			{
		String json = "此处应该有刷新服务器地址!";
		if (flushurl != null) 
		{
			try 
			{
				json = XwhHttp.sendGet(flushurl);
			} catch (Exception e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				json = Log4j.getExceptionInfo(e);
			}
		}
		OutputStream out = response.getOutputStream();
		if(json != null)
		{
			out.write(json.getBytes("utf-8"));
		}else
		{
			out.write("刷新失败：返回值为null".getBytes("utf-8"));
		}
		out.close();
	}
	/**
	 * 请求访问
	 * 
	 * @param response
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 */
	protected void flushCachePost(HttpServletResponse response, String flushurl
			,String param)
			throws IOException, UnsupportedEncodingException 
			{
		String json = "此处应该有访问服务器地址!";
		if (flushurl != null) 
		{
			try 
			{
				json = XwhHttp.sendFromPost(flushurl,param);
			} catch (Exception e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				json = Log4j.getExceptionInfo(e);
			}
		}
		OutputStream out = response.getOutputStream();
		if(json != null)
		{
			out.write(json.getBytes("utf-8"));
		}else
		{
			out.write("访问失败：返回值为null".getBytes("utf-8"));
		}
		out.close();
	}
	/**
	 * 刷新地址
	 * 
	 * @return
	 */
	protected String flushLogicServerURL() 
	{
		return "OpaserverDao&update-method-name=flushCache&update-parameter="
				+ getClassInfo().getSimpleName();
	}

	/**
	 * 解析客户端提交过来数据
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public static String parseServletInputStream(HttpServletRequest request)
			throws IOException {
		ServletInputStream is = request.getInputStream();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte buff[] = new byte[1024];
		int read = -1;
		while ((read = is.read(buff)) > 0) 
		{
			baos.write(buff, 0, read);
		}
		byte[] buffer = baos.toByteArray();
		// 解密
		String requestContent = new String(buffer);
		return requestContent;
	}

	/**
	 * excel保存数据
	 * 
	 * @param request
	 * @param response
	 */
	public <T> void excelSaveData(HttpServletRequest request,
			HttpServletResponse response, Object object) 
	{
		ServletOutputStream out = null;
		try
		{
			out = response.getOutputStream();
			MiniGamebackDao.instance.saveOrUpdate(excelSaveData);
			out.write("导入成功!".getBytes("utf-8"));
		} catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
			Log4j.NAME.EXCEPTION_LOG.debug(Log4j.getExceptionInfo(e));
			try
			{
				out.write("导入失败!".getBytes("utf-8"));
			} catch (IOException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	/**
	 * 进行操作数据
	 * 
	 * @param <T>
	 * 
	 * @param request
	 */
	public <T> Object doOperator(HttpServletRequest request) 
	{
		String operator = request.getParameter("operator");
		Object privateResult = privateOperator(request, operator);
		if (privateResult != null) 
		{
			return privateResult;
		}
		Class<?> outClass = getClassInfo();
		try 
		{
			T t = mapEntity(request, outClass);
			if ("delete".equals(operator)) 
			{
				deleteData(t);
				return 1;
			}else if ("operateMulti".equals(operator)) {
				String ids = request.getParameter("ids");
				String operate = request.getParameter("operate");
				operateMultiData(ids,operate);
			}else if ("edit".equals(operator)) 
			{
				editData(t);
			} else if ("new".equals(operator)) 
			{
				newData(t);
			}
		} catch (Exception e) 
		{
			e.printStackTrace();
			System.out.println(Log4j.getExceptionInfo(e));
		}

		return 1;
	}

	protected void otherPost(HttpServletRequest request, HttpServletResponse response)
	{
		
	}
	
	/**
	 * 映射实体类
	 * 
	 * @param request
	 * @param outClass
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ParseException
	 */
	protected <T> T mapEntity(HttpServletRequest request, Class<?> outClass)
			throws InstantiationException, IllegalAccessException,
			ParseException 
	{
		@SuppressWarnings("unchecked")
		T t = (T) outClass.newInstance();
		for (Field field : outClass.getFields()) 
		{
			if (field.isAnnotationPresent(Comments.class)) {
				Object object = request.getParameter(field.getName());
				if (object != null && !object.toString().isEmpty()) 
				{
					Class<?> clazz = field.getType();
					// System.out.println(clazz.getName() + "\t" +
					// field.getName()
					// + "=" + object);
					Object returnObj = null;
					if (clazz == Date.class)
						returnObj = new SimpleDateFormat("yyyy-MM-dd")
								.parse(object.toString());
					else if (clazz == Timestamp.class) 
					{
						returnObj = new Timestamp(new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss").parse(object.toString())
								.getTime());
					} else
						returnObj = ConvertUtils.convert(object, clazz);
					field.set(t, returnObj);
				}
			}
		}
		return t;
	}

	/**
	 * 私有信息配置
	 * 
	 * @param request
	 * @param operator
	 * @return
	 */
	protected Object privateOperator(HttpServletRequest request, String operator) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 新增数据
	 * 
	 * @param <T>
	 * 
	 * @param t
	 */
	protected void newData(Object t) 
	{

	}

	/**
	 * 修改数据
	 * 
	 * @param <T>
	 * 
	 * @param t
	 */
	protected void editData(Object t) 
	{

	}

	/**
	 * 删除数据
	 * 
	 * @param <T>
	 * 
	 * @param parameter
	 */
	protected void deleteData(Object t) 
	{

	}

	/**
	 * 操作多行数据
	 * 
	 * @param <T>
	 * 
	 * @param parameter
	 */
	protected void operateMultiData(String ids, String operate) {
		
	}
	
	public Class<?> getClassInfo() 
	{
		return null;
	}

	public <T> Vector<T> findData() 
	{
		return null;
	}

	/**
	 * 获取列表信息
	 * 
	 * @param request
	 * 
	 * @return
	 */
	protected <T> ColumText getColumText(HttpServletRequest request) 
	{
		ColumText columText = new ColumText();
		Vector<T> apps = findData();
		columText.setTotal(apps.size());
		String newSize = get("newPageSize");
		if (newSize != null && !newSize.equals("")) 
		{
			Vector<Object> vector = new Vector<>();
			vector.addAll(apps);
			columText.setRows(vector);
		} else 
		{
			Vector<Object> vector = new Vector<>();
			for (int i = (getPage() - 1) * getRows(); i < getPage() * getRows(); i++) 
			{
				if (apps.size() > i) 
				{
					vector.addElement(apps.elementAt(i));
				}
			}
			columText.setRows(vector);
		}
		return columText;

	}

	/**
	 * 发送结果集
	 * 
	 * @param resultObject
	 * @param response
	 * @throws IOException
	 */
	public void sendResult(Object resultObject, HttpServletResponse response)
			throws IOException {
		try {
			String json = null;
			if (resultObject instanceof String) {
				json = (String) resultObject;
			} else {
				json = XwhTool.getGsonValue(resultObject, getGsonFormat());
			}
			OutputStream out = response.getOutputStream();
			out.write(json.getBytes("utf-8"));
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 创建加密文件
	 * 
	 * @param file
	 * @return
	 */
	public File createSecurity(File file) {
		try 
		{
			FileInputStream is = new FileInputStream(file);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte buff[] = new byte[1024];
			int read = -1;
			while ((read = is.read(buff)) > 0) 
			{
				baos.write(buff, 0, read);
			}
			byte[] buffer = baos.toByteArray();
			is.close();
			String encryptionPath = file.getPath();
			int lastindex = encryptionPath.lastIndexOf(".");
			if (lastindex > 0)
				encryptionPath = encryptionPath.substring(0, lastindex);
			String suffix = ReadConfig.get("xor_suffix");
			encryptionPath = XwhTool.getDealFilePath(encryptionPath, suffix, 0);
			FileOutputStream out = new FileOutputStream(encryptionPath);
			out.write(XwhXorSecurity.encryption(buffer));
			out.close();
			return new File(encryptionPath);
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 列表信息
	 * 
	 * @author Host-0222
	 * 
	 */
	public class ColumText 
	{
		private int total;
		private Vector<?> rows;

		public int getTotal() 
		{
			return total;
		}

		public void setTotal(int total) 
		{
			this.total = total;
		}

		public Vector<?> getRows() 
		{
			return rows;
		}

		public void setRows(Vector<?> rows) 
		{
			this.rows = rows;
		}
	}

	/**
	 * 去掉字符串右边的空格
	 * 
	 * @param str
	 *            要处理的字符串
	 * @return 处理后的字符串
	 */
	public static String rightTrim(String str) 
	{
		if (str == null) 
		{
			return "";
		}
		int length = str.length();
		for (int i = length - 1; i >= 0; i--) 
		{
			if (str.charAt(i) != 0x20) 
			{
				break;
			}
			length--;
		}
		return str.substring(0, length);

	}

	/**
	 * 设置gson参数时间组件格式
	 * 
	 * @return
	 */
	public String getGsonFormat() 
	{
		return "yyyy-MM-dd";
	}

	/**
	 * 设置查询表名
	 * @return
	 */
	public String getTableName(){
		return getClassInfo().getSimpleName().toLowerCase();
	}
	
	/**
	 * 设置查询数据
	 * 
	 * @return
	 */
	protected String getSelectData() 
	{
		return "select * from " + getTableName();
	}

	// 默认搜索SQL
	protected String getSQL() 
	{
		String selectData = getSelectData();
		if (getWhereData() != null) 
		{
			selectData += getWhereData();
		}
		if(!getGroupBy().equals("")){
			selectData += getGroupBy();
		}
		if (!getOrderSql().equals("")) 
		{
			selectData += getOrderSql();
		}
		System.out.println(selectData);
		return selectData;
	}

	protected String getGroupBy()
	{
		return "";
	}

	/**
	 * 获取session数据
	 * 
	 * @param key
	 * @return
	 */
	public String get(String key) 
	{
		return request.getParameter(key);
	}

	/**
	 * 设置查询条件
	 * 
	 * @return
	 */
	public Map<String, String> searchMap;

	protected String getWhereData() 
	{

		if (searchMap != null)
			searchMap.clear();
		String searchdata = get("search-data");
		if (searchdata != null && searchdata.length() > 0) 
		{
			Type type = new TypeToken<Map<String, String>>() {}.getType();
			Map<String, String> map = new Gson().fromJson(searchdata, type);
			this.searchMap = map;
			if (!map.isEmpty())
				return createMapCollect(map);
		}
		return " where " + getInitWhere();
	}

	/**
	 * 创建查询集合数据
	 * 
	 * @param map
	 * @return
	 */
	protected String createMapCollect(Map<String, String> map) 
	{
		StringBuilder builder = new StringBuilder(" where " + getInitWhere() + " ");
		Set<String> keySet = new HashSet<>(map.keySet());
		// 进行获取时间查询
		// 是否拥有时间索引
		if (keySet.contains("search-data")) 
		{
			String searchData = map.get("search-data");
			keySet.remove("search-data");
			String st = map.get("st");
			keySet.remove("st");
			String ed = map.get("ed");
			keySet.remove("ed");
			if (st != null && ed != null && !st.equals("") && !ed.equals("")) 
			{
				builder.append(" and date(" + searchData + ") between '" + st
						+ "' and '" + ed + "'");
			}
		} else 
		{
			for (String key : keySet) 
			{
				// 检测是否有特殊的时间查询
				if (key.endsWith("_st")) 
				{
					String searchData = key.substring(0, key.length() - 3);
					builder.append("date(" + searchData + ") >= '"
							+ map.get(key) + "'");
					String ed = keySet.contains(searchData + "_ed") ? map
							.get(searchData + "_ed") : null;
					if (ed != null)
						builder.append(" and date(" + searchData + ") <= '"
								+ ed + "'");
				}
			}
		}

		builder.append(getGivenSearch(keySet,map));

		return builder.toString();
	}

	public String getInitWhere()
	{
		return "1=1";
	}

	/**
	 * 获取定制的查询参数
	 * @return
	 */
	public String getGivenSearch(Set<String> keySet,Map<String, String> map){
		StringBuilder builder = new StringBuilder();
		for (String key : keySet) 
		{
			builder.append(" and " + key + " like '%" + map.get(key) + "%'");
		}
		return builder.toString();
	}
	
	public String getOrderSql() 
	{
		String order = "";
		String sort = getSort();
		if (sort != null && !sort.equals("")) 
		{
			order += " order by " + sort + " " + getOrder();
		}
		return order;
	}
	
	// 上传配置
    private static final int MEMORY_THRESHOLD   = 1024 * 1024 * 3;  // 3MB
    private static final int MAX_FILE_SIZE      = 1024 * 1024 * 40; // 40MB
    private static final int MAX_REQUEST_SIZE   = 1024 * 1024 * 50; // 50MB
	
    protected List<FileItem> getFileItemList(){
		List<FileItem> formItems = new ArrayList<>();
		try
		{
			// 配置上传参数
	        DiskFileItemFactory factory = new DiskFileItemFactory();
	        // 设置内存临界值 - 超过后将产生临时文件并存储于临时目录中
	        factory.setSizeThreshold(MEMORY_THRESHOLD);
	        // 设置临时存储目录
	        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
	        ServletFileUpload upload = new ServletFileUpload(factory);
	        // 设置最大文件上传值
	        upload.setFileSizeMax(MAX_FILE_SIZE);
	        // 设置最大请求值 (包含文件和表单数据)
	        upload.setSizeMax(MAX_REQUEST_SIZE);
	        // 中文处理
	        upload.setHeaderEncoding("UTF-8"); 
	        formItems = upload.parseRequest(request);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return formItems;
	}
    
    /**
     * 获取上传的file文件
     * @param formItems
     * @return
     */
    protected File saveFile(List<FileItem> formItems)
	{
		File storeFile = null;
		try {
			// 构造路径来存储上传的文件
		    String uploadPath = request.getServletContext().getRealPath("/"+getUploadDirectory());
		    // 如果目录不存在则创建
		    File uploadDir = new File(uploadPath);
		    if (!uploadDir.exists()) {
		        uploadDir.mkdir();
		    }
		    if (formItems != null && formItems.size() > 0) {
		        // 迭代表单数据
		        for (FileItem item : formItems) {
		            // 处理不在表单中的字段
		            if (!item.isFormField()) {
		                String fileName = new File(item.getName()).getName();
		                if("".equals(fileName)){
		                	break;
		                }
		                fileName = fileName.substring(0, fileName.lastIndexOf('.')) 
		                		+"_"+System.currentTimeMillis()+fileName.substring(fileName.lastIndexOf('.'));
		                storeFile = new File(uploadPath,fileName);
		                // 保存文件到硬盘
		                item.write(storeFile);
		            }
		        }
		    }
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return storeFile;
	}
    
    /**
     * 文件上传文件夹名
     * @return
     */
    protected String getUploadDirectory(){
    	return "upload_file";
    }
}
