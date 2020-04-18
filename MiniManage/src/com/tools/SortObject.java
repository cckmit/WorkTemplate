package com.tools;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.Date;

public class SortObject implements Comparator<Object>
{

	public String sort;
	
	public String order;
	
	public SortObject(String sort,String order)
	{
		// TODO Auto-generated constructor stub
		this.sort = sort;
		this.order = order;
	}
	@Override
	public int compare(Object obj1, Object obj2)
	{
		// TODO Auto-generated method stub
		if(sort != null && order != null)
		{
			boolean desc = order.equals("desc");
			Field[] fields = obj1.getClass().getDeclaredFields();
			for (Field field : fields)
			{
				String name = field.getName();
				if(name.equals(sort))
				{
					try
					{
						Object v1 = field.get(obj1);
						Object v2 = field.get(obj2);
						if(v1 == null && v2 == null)
						{
							return 0;
						}
						if (v1 == null) 
						{
							return desc ? -1 : 1;
						}
						if (v2 == null) 
						{
							return desc ? 1 : -1;
						}
						if (v1 instanceof Integer) 
						{
							return desc ? ((Integer) v1)
									.compareTo((Integer) v2)
									: ((Integer) v2)
											.compareTo((Integer) v1);
						} else if (v2 instanceof Date) 
						{
							return desc ? ((Date) v1).compareTo((Date) v2)
									: ((Date) v2).compareTo((Date) v1);
						} else if (v1 instanceof String) 
						{
							return desc ? ((String) v1)
									.compareTo((String) v2) : ((String) v2)
									.compareTo((String) v1);
						} else if (v1 instanceof Boolean) 
						{
							return desc ? ((Boolean) v1)
									.compareTo((Boolean) v2)
									: ((Boolean) v2)
											.compareTo((Boolean) v1);
						}
					} catch (Exception e)
					{
						// TODO: handle exception
						e.printStackTrace();
					}
				}
			}
		}
		return 0;
	}

}
