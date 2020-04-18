package com.code.ui.servlet;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;

import com.code.dao.MiniGamebackDao;
import com.code.entity.Minitj_manual;
import com.code.ui.UIMoudleServlet;
import com.google.gson.reflect.TypeToken;
import com.tools.ReadExcel;
import com.tools.TableShow;
import com.tools.XWHMathTool;
@WebServlet(urlPatterns = { "/minitj_manual", "/pages/minitj_manual" })
public class MinitjmanualServlet extends UIMoudleServlet {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 新增数据
	 * 
	 * @param <T>
	 * 
	 * @param t
	 */
	protected void newData(Object t) {
		try {
			Minitj_manual model = (Minitj_manual) t;
			MiniGamebackDao.instance.saveOrUpdate(model);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 修改数据
	 * 
	 * @param <T>
	 * 
	 * @param t
	 */
	protected void editData(Object t) {
		try {
			Minitj_manual model = (Minitj_manual) t;
			MiniGamebackDao.instance.saveOrUpdate(model);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 删除数据
	 * 
	 * @param <T>
	 * 
	 * @param parameter
	 */
	protected void deleteData(Object t) {
		Minitj_manual model = (Minitj_manual) t;
		String deleteSQL = "delete from minitj_manual where manual_gameid="
				+ model.manual_gameid +" and manual_date='"+model.manual_date+"'";
		MiniGamebackDao.instance.execSQLCMDInfo(deleteSQL);
	}
	
	@Override
	public String getGivenSearch(Set<String> keySet, Map<String, String> map) {
		StringBuilder sb = new StringBuilder();
		for (String key : keySet) {
			if("manual_gameid".equals(key)){
				sb.append(" AND manual_gameid="+map.get(key)+" " );
			}else{
				sb.append(" AND "+key+" LIKE '%" + map.get(key) +"%' ");
			}
		}
		return sb.toString();
	}
	@Override
	public String getOrderSql()
	{
		return " ORDER BY manual_date DESC ";
	}
	@Override
	protected String getSelectData()
	{
		String sql = 
				"SELECT a.*,b.`game_name` FROM `minitj_manual` a "+
				"LEFT JOIN `mini_game` b ON a.`manual_gameid`=b.`game_id` ";
		return sql;
	}
	
	public Class<?> getClassInfo() {
		return Minitj_manual.class;
	}
	
	protected Type getTypeof() {
		// TODO Auto-generated method stub
		return new TypeToken<Vector<Minitj_manual>>() {
		}.getType();
	}

	@SuppressWarnings("unchecked")
	public Vector<Minitj_manual> findData() {
		Vector<Minitj_manual> list = (Vector<Minitj_manual>) MiniGamebackDao.instance
				.findBySQL(getSQL(),Minitj_manual.class);
		for (Minitj_manual manual : list)
		{
			if(manual.manual_click!=0)
			{
				manual.clickPrice = XWHMathTool.formatMath(manual.manual_outgo / manual.manual_click, 2);
			}
		}
		return list;
	}

	@Override
	protected void otherPost(HttpServletRequest request, HttpServletResponse response)
	{
		List<FileItem> formItems = getFileItemList();
		File uploadFile = saveFile(formItems);
		ReadExcel readExcel = new ReadExcel(uploadFile);
		TableShow tables = readExcel.getTables();
		OutputStream os = null;
		try 
		{
			os = response.getOutputStream();
			Vector<Vector<Object>> vector = tables.getTds();

			System.out.println("开始处理导入数据:"+System.currentTimeMillis());
			
			//写入数据库
			String result = Minitj_manual.importFromExcel(vector);
			
			os.write(result.getBytes("utf-8"));
			os.close();
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
