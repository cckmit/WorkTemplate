package com.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Vector;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


/**
 * 读取Excel文件信息
 * 
 * @author user
 * 
 */
public class ReadExcel
{
	private TableShow tables;

	public ReadExcel(File file)
	{
		readXml(file);
	}

	public ReadExcel(String file)
	{
		readXml(new File(file));
	}

	public TableShow getTables()
	{
		// TODO Auto-generated method stub
		return tables;
	}

	public void readXml(File fileName)
	{
		tables = new TableShow();
		if (!fileName.exists())
		{
			return;
		}
		Vector<Object> ths = new Vector<Object>();
		Vector<Vector<Object>> tds = new Vector<Vector<Object>>();
		tables.setTds(tds);
		tables.setThs(ths);
		boolean isE2007 = false; // 判断是否是excel2007格式
		if (fileName.getName().endsWith("xlsx"))
			isE2007 = true;
		try
		{
			InputStream input = new FileInputStream(fileName); // 建立输入流
			Workbook wb = null;
			// 根据文件格式(2003或者2007)来初始化
			if (isE2007)
			{
				wb = new XSSFWorkbook(input);
			}
			else
			{
				wb = new HSSFWorkbook(input);
			}
			Sheet sheet = wb.getSheetAt(0); // 获得第一个表单
			Iterator<Row> rows = sheet.rowIterator(); // 获得第一个表单的迭代器
			while (rows.hasNext())
			{
				Row row = rows.next(); // 获得行数据
//				System.out.println("Row #" + row.getRowNum()); // 获得行号从0开始
				int rowIndex = row.getRowNum();
				Iterator<Cell> cells = row.cellIterator(); // 获得第一行的迭代器
				Vector<Object> objects = new Vector<Object>();
				while (cells.hasNext())
				{
					Cell cell = cells.next();
//					int index = cell.getColumnIndex();
//					System.out.println("Cell #" + index);
					switch (cell.getCellType())
					{ // 根据cell中的类型来输出数据
						case HSSFCell.CELL_TYPE_NUMERIC:
						{
							Double value = cell.getNumericCellValue();
							int ddint = value.intValue();
							double ddAfterDot = value - ddint;
							if (ddAfterDot > 0)
							{
								objects.add(value.toString());
							}
							else
							{
								objects.add(String.valueOf(ddint));
							}
//							System.out.println(cell.getNumericCellValue());
						}
							break;
						case HSSFCell.CELL_TYPE_STRING:
						{
							objects.add(cell.getStringCellValue());
//							System.out.println(cell.getStringCellValue());
						}
							break;
						case HSSFCell.CELL_TYPE_BOOLEAN:
						{
							objects.add(cell.getBooleanCellValue());
//							System.out.println(cell.getBooleanCellValue());
						}
							break;
						case HSSFCell.CELL_TYPE_FORMULA:
						{
							objects.add(cell.getCellFormula());
//							System.out.println(cell.getCellFormula());
						}
							break;
						default:
						{
							objects.add(null);
//							System.out.println("unsuported sell type");
						}
							break;
					}
				}
				if (rowIndex == 0)
				{
					ths.addAll(objects);
				}
				else
				{
					tds.add(objects);
				}
			}
//			wb.close();
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}

	}
}
