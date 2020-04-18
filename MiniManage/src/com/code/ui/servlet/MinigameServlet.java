package com.code.ui.servlet;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.servlet.annotation.WebServlet;

import org.apache.commons.fileupload.FileItem;

import com.code.dao.MiniGamebackDao;
import com.code.entity.Mini_binding;
import com.code.entity.Mini_check;
import com.code.entity.Mini_game;
import com.code.entity.Mini_record;
import com.code.ui.UIMoudleServlet;
import com.google.gson.reflect.TypeToken;
import com.tools.XWHMathTool;

@WebServlet(urlPatterns = { "/mini_game", "/pages/mini_game" })
public class MinigameServlet extends UIMoudleServlet
{
	private static final long serialVersionUID = 1L;

	/**
	 * 新增数据
	 * 
	 * @param <T>
	 * 
	 * @param t
	 */
	protected void newData(Object t)
	{
		try
		{

			List<FileItem> formItems = getFileItemList();
			File uploadFile = saveFile(formItems);
			Mini_game model = parseFileItems(formItems);
			if (uploadFile != null)
			{
				model.game_iconfile = uploadFile.getName();
			}
			if(model.game_jiecompany==null||model.equals(null)){
				model.game_jiecompany=0;
			}
			model.game_ispublish=2;
			model.game_iswheel=2;
			model.game_cheattype=2;
			MiniGamebackDao.instance.saveOrUpdate(model);
			Vector<Mini_game> list = (Vector<Mini_game>) MiniGamebackDao.instance
					.findBySQL("select * from mini_game where 1=1 order by game_id desc limit 1;", Mini_game.class);
			if(list!=null&&list.size()>0)
			{
				Mini_game mini_game=list.get(0);
				Mini_check mini_check =new Mini_check();
				mini_check.check_gameid=mini_game.game_id;
				mini_check.check_state=1;
				mini_check.check_forceshell=2;
				mini_check.open_recommend=1;
				mini_check.ad_cheat=1;
				MiniGamebackDao.instance.saveOrUpdate(mini_check);
				Mini_binding mini_binding=new Mini_binding();
				mini_binding.binding_gameid=mini_game.game_id;
				MiniGamebackDao.instance.saveOrUpdate(mini_binding);
				Mini_record mini_record=new Mini_record();
				mini_record.game_id=mini_game.game_id;
				mini_record.date=new Timestamp(System.currentTimeMillis()).toString();
				MiniGamebackDao.instance.saveOrUpdate(mini_record);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private Mini_game parseFileItems(List<FileItem> formItems) throws Exception
	{
		Class<Mini_game> clazz = Mini_game.class;
		Mini_game vo = clazz.newInstance();
		for (FileItem fileItem : formItems)
		{
			if (!fileItem.isFormField()
					||fileItem.getFieldName().equals("game_name_y")
					||fileItem.getFieldName().equals("game_appid_y"))
			{
				continue;
			}
			String fieldname = fileItem.getFieldName();
			String formVal = new String(fileItem.getString().getBytes("iso-8859-1"), "UTF-8");
			Field field = clazz.getDeclaredField(fieldname);
			if("game_id".equals(fieldname) ||  "game_jiecompany".equals(fieldname)
					  || "game_cheattype".equals(fieldname)
					 || "game_ispublish".equals(fieldname)
					 || "game_iswheel".equals(fieldname)
					 || "game_wheel_count".equals(fieldname))
			{
				if(XWHMathTool.isNumericInt(formVal))
				{
					field.set(vo, Integer.parseInt(formVal));
				}
			}
			else
			{
				field.set(vo, formVal);
			}
		}
		return vo;
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
		try
		{

			List<FileItem> formItems = getFileItemList();
			File uploadFile = saveFile(formItems);
			Mini_game model = parseFileItems(formItems);
			if(model.game_jiecompany==null||model.equals(null)){
				model.game_jiecompany=0;
			}
			if (uploadFile != null)
			{
				model.game_iconfile = uploadFile.getName();
			}
			MiniGamebackDao.instance.saveOrUpdate(model);
		}
		catch (Exception e)
		{
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
	protected void deleteData(Object t)
	{
		Mini_game model = (Mini_game) t;
		String deleteSQL = "delete from mini_game where game_id="
				+ model.game_id;
		MiniGamebackDao.instance.execSQLCMDInfo(deleteSQL);
	}

	public Class<?> getClassInfo()
	{
		return Mini_game.class;
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
	protected Type getTypeof()
	{
		// TODO Auto-generated method stub
		return new TypeToken<Vector<Mini_game>>()
		{
		}.getType();
	}

	@SuppressWarnings("unchecked")
	public Vector<Mini_game> findData()
	{
		Vector<Mini_game> list = (Vector<Mini_game>) MiniGamebackDao.instance
				.findBySQL(getSQL(), Mini_game.class);
		return list;
	}
	
	@Override
	protected String getUploadDirectory()
	{
		return "icon_file";
	}

}
