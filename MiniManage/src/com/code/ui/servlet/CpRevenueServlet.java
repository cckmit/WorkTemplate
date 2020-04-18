package com.code.ui.servlet;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.List;
import java.util.Vector;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;

import com.code.dao.MiniGamebackDao;
import com.code.entity.Cp_revenue;
import com.code.entity.Minitj_manual;
import com.code.ui.UIMoudleServlet;
import com.google.gson.reflect.TypeToken;
import com.tools.ReadExcel;
import com.tools.TableShow;

@WebServlet(urlPatterns = { "/cp_revenue", "/pages/cp_revenue" })
public class CpRevenueServlet  extends UIMoudleServlet{

	/**
	 * 
	 */
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
			Cp_revenue model = (Cp_revenue) t;
			if(model.cp_recharge==null){
				model.cp_recharge=new BigDecimal(0);
			}
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
			Cp_revenue model = (Cp_revenue) t;
			if(model.cp_settlement==0){
				model.cp_activation=0;
			}
			if(model.cp_settlement==1){
				model.cp_recharge=new BigDecimal("0");
			}
			MiniGamebackDao.instance.saveOrUpdate(model);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	
	public Class<?> getClassInfo() {
		return Cp_revenue.class;
	}
	
	protected Type getTypeof() {
		// TODO Auto-generated method stub
		return new TypeToken<Vector<Cp_revenue>>() {
		}.getType();
	}

	
	
	@SuppressWarnings("unchecked")
	public Vector<Cp_revenue> findData() {
		Vector<Cp_revenue> list = (Vector<Cp_revenue>) MiniGamebackDao.instance
			
				.findBySQL(getSQL(),Cp_revenue.class);
		for(Cp_revenue cp_revenue : list){
			if(cp_revenue.cp_settlement==0){
				BigDecimal sDecimal= new BigDecimal("100");
				cp_revenue.cp_product= cp_revenue.cp_proportion.multiply(cp_revenue.cp_recharge).setScale(2,BigDecimal.ROUND_HALF_UP);
			 	cp_revenue.cp_product = cp_revenue.cp_product.divide(sDecimal).setScale(2,BigDecimal.ROUND_HALF_UP);
			}
			if(cp_revenue.cp_settlement==1){
				BigDecimal sDecimal= new BigDecimal("100");
				BigDecimal cp_activations = new BigDecimal(cp_revenue.cp_activation);
			 	cp_revenue.cp_product= cp_activations.multiply(cp_revenue.cp_price).setScale(2,BigDecimal.ROUND_HALF_UP);
			 	cp_revenue.cp_product = cp_revenue.cp_product.divide(sDecimal).setScale(2,BigDecimal.ROUND_HALF_UP);
			}
			
		}
		return list;
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
			String result = Cp_revenue.importFromExcel(vector);
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
