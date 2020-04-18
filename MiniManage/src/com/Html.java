package com;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Map;
import java.util.Vector;
import com.annotation.Comments;
import com.annotation.Page;
import com.annotation.Upload;
import com.tools.ClassUtil;
import com.tools.XwhTool;

public class Html
{

	static boolean flag = false;

	public static void main(String[] args)
	{
		String dir = System.getProperty("user.dir");
		File pages = new File(dir + "/WebRoot/" + "temppages");
		if (!pages.exists())
		{
			pages.mkdirs();
		}
		Vector<Class<?>> vector = ClassUtil.getClasses("com.code.entity");
		FileOutputStream out = null;
		try
		{
			for (Class<?> c : vector)
			{
				File file = new File(pages, c.getSimpleName() + ".html");
				String f = Html.outPage(c);
				if( f == null || f.trim().isEmpty())
					continue;
				if(file.exists()){
					continue;
				}
				out = new FileOutputStream(file);
				out.write(f.getBytes());
				out.flush();
			}
			System.out.println("finish!");
		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
			try
			{
				out.close();
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@SuppressWarnings({ "rawtypes" })
	public static String outformitem(Class c,String codedir)
	{
		StringBuilder fieldbuilder  = new StringBuilder();
		Field[] fields = c.getDeclaredFields();
		String div = XwhTool.readFileString(codedir + "/div.code");
		boolean isFirst = true;
		for (Field field : fields)
		{
			String formField = "";
			Class type = field.getType();
			String FieldName = field.getName();
			if (field.isAnnotationPresent(Comments.class))
			{
				Comments Comments = field.getAnnotation(Comments.class);
				String item = Comments.name();
				if (field.isAnnotationPresent(Upload.class))
				{
					flag = true;
					formField += "<div id =\""+ FieldName
							+ "\" class=\"ztc_upload\" data-url=\"http://blazefire.f3322.net:10097/st_ui/fo\" callbackurl=\"\" tag_id=\"3\" cdn=\"true\" data-multiSelect=\"false\" data-title=\"上传图片\" maxfileSize=\"50M\" filetype=\"jpg,png,gif,apk,zip\">\n";

					formField += "</div>";
				} else if (Map.class.isAssignableFrom(type))
				{
					formField += "<input id=\""
							+ FieldName
							+ "\" na"
							+ "me=\""
							+ FieldName
							+ "\" style=\"width:90%;\" class=\"easyui-combobox\" data-options=\"required:true,label:'"
							+ (item.equals("") ? FieldName : item)
							+ ":',valueField:'',textField:'',url:'',onSelect:function(record){$('').combobox('reload', url);}\"/>";
					
				} else if (Date.class == type)
				{
					formField += "<input id=\""
							+ FieldName
							+ "\" name=\""
							+ FieldName
							+ "\" class=\"easyui-datebox\" style=\"width:90%;\" data-options=\"formatter:function(date)"
							+ "{var y=date.getFullYear();var m=date.getMonth()+1;d=date.getDate();return y+'-'+m+'-'+d;},parser:function(s){var t=Date.parse(s);if(!isNaN(t)){return new Date(t);}else{return new Date();}},label:'"
							+ (item.equals("") ? FieldName : item) + ":',required:true\">";
				} else if (Timestamp.class == type)
				{
					formField += "<input class=\"easyui-datetimebox\" id=\"" + FieldName + "\" name=\""
							+ FieldName + "\" label=\"" + (item.equals("") ? FieldName : item)
							+ "：\" data-options=\"required:true\" style=\"width:90%;\">";
				} else
				{
					formField += "<input class=\"easyui-textbox\" id=\"" + FieldName + "\" name=\"" + FieldName
							+ "\" style=\"width:90%\" data-options=\"label:'" + (item.equals("") ? FieldName : item)
							+ ":',required:true\">";
				}
				String content = "";
				if(isFirst)
				{
					isFirst = false;
					content = div.trim();
				}else
				{
					content = div;
				}
				content = content.replaceAll("#content#", formField);
				fieldbuilder.append(content + "\n");
			}
		}
		if (flag)
		{
			String button = "";
			button += "<a href=\"javascript:void(0)\" class=\"easyui-linkbutton\" iconcls=\"icon-add\" plain=\"true\" onclick=\"open()\">上传</a>\n";
			String content = div.replaceAll("#content#", button);
			fieldbuilder.append(content);
		}
		return fieldbuilder.toString();
	}

	// 输出主页面
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static String outPage(Class c)
	{
		String codedir = System.getProperty("user.dir") + "/htmlcode";
		String htmlcode = "";
		if (c.isAnnotationPresent(Page.class))
		{
			StringBuilder builder = new StringBuilder();
			htmlcode = XwhTool.readFileString(codedir + "/html.code");
			Page annotation = (Page) c.getAnnotation(Page.class);
			Field[] fields = c.getDeclaredFields();
			boolean isFirst = true;
			for (Field field : fields)
			{
				String fieldName = field.getName();
				String title = "";
				if(field.isAnnotationPresent(Comments.class))
				{
					Comments formitem = field.getAnnotation(Comments.class);
					title = formitem.name();
				}
				String th = XwhTool.readFileString(codedir + "/header.code");
				if(isFirst)
				{
					isFirst = false;
					th = th.trim();
				}
				th = th.replaceAll("#field#", fieldName);
				th = th.replaceAll("#title#", title);
				builder.append(th + "\n");
			}
			htmlcode = htmlcode.replaceAll("#header#", builder.toString());
			if (annotation.isImport())
			{
				htmlcode = htmlcode.replaceAll("#import#", "<a href=\"javascript:void(0)\" class=\"easyui-linkbutton\" iconCls=\"icon-save\" plain=\"true\" onclick=\"importClick()\">导入</a>");
			}else
			{
				htmlcode = htmlcode.replaceAll("#import#","");
			}
			if(annotation.search())
			{
				htmlcode = htmlcode.replaceAll("#search#", XwhTool.readFileString(codedir + "/searchform.code"));
			}
			htmlcode = htmlcode.replaceAll("#formField#", outformitem(c,codedir));
			if(flag)
				htmlcode = htmlcode.replaceAll("#open#", XwhTool.readFileString(codedir + "/uploadjs.code"));
			else
				htmlcode = htmlcode.replaceAll("#open#","");
		}
		return htmlcode;
	}

}
