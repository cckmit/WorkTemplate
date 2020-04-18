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
import com.code.entity.Minigamegroup;
import com.code.ui.UIMoudleServlet;
import com.google.gson.reflect.TypeToken;
import com.tools.ReadExcel;
import com.tools.TableShow;

@WebServlet(urlPatterns = { "/minigamegroupservlet", "/pages/minigamegroupservlet" })
public class MinigamegroupServlet extends UIMoudleServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 修改数据
	 * 
	 * @param <T>
	 * 
	 * @param t
	 */
	protected void editData(Object t) {
		try {
			Minigamegroup model = (Minigamegroup) t;
			MiniGamebackDao.instance.saveOrUpdate(model);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  }
	
	
	public Class<?> getClassInfo() {
		return Minigamegroup.class;
	}
	
	
	protected Type getTypeof() {
		// TODO Auto-generated method stub
		return new TypeToken<Vector<Minigamegroup>>() {
		}.getType();
	}
	
	
	@SuppressWarnings("unchecked")
	public Vector<Minigamegroup> findData() {
		Vector<Minigamegroup> list = (Vector<Minigamegroup>) MiniGamebackDao.instance
				.findBySQL(getSQL(),Minigamegroup.class);
		return list;
	}
	
	@Override
	public String getTableName()
	{
		return "mini_game";
	}
	
	@Override
	public String getGivenSearch(Set<String> keySet, Map<String, String> map) {
		StringBuilder sb = new StringBuilder();
		for (String key : keySet) {
			if("game_id".equals(key)){
				sb.append(" AND game_id="+map.get(key)+" " );
			}else{
				sb.append(" AND "+key+" LIKE '%" + map.get(key) +"%' ");
			}
		}
		return sb.toString();
	}
	/**
	 *导入数据
	 *
	 * @param <T>
	 * 
	 * @param t
	 */
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
			String result = Minigamegroup.importFromExcel(vector);
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
