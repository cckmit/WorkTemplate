package com.code.ui.servlet;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Vector;

import javax.servlet.annotation.WebServlet;

import com.code.dao.MiniPackageDao;
import com.code.entity.DCEventItem;
import com.code.entity.DCGameEvent;
import com.code.entity.Game_package_info;
import com.code.entity.Shop_info;
import com.code.ui.UIMoudleServlet;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tools.JsonTool;

@WebServlet(urlPatterns = { "/game_package_info", "/pages/game_package_info" })
public class GamepackageinfoServlet extends UIMoudleServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public Class<?> getClassInfo() {
		return Game_package_info.class;
	}
	protected Type getTypeof() {
		// TODO Auto-generated method stub
		return new TypeToken<Vector<Game_package_info>>() {
		}.getType();
	}
	
	
	@SuppressWarnings("unchecked")
	public Vector<Shop_info> findData() {
		Date nowtime = new Date();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
		int date =  Integer.parseInt(sdf.format(nowtime));
		String numberes = "0"  ;
	
		String searchdata = get("search-data");
		if (searchdata != null && searchdata.length() > 0) 
		{
			Type type = new TypeToken<Map<String, String>>() {}.getType();
			Map<String, String> map = new Gson().fromJson(searchdata, type);
			if(map.containsKey("dates"))
			{
				date=Integer.parseInt(map.get("dates"));
			}
			if(map.containsKey("numberes"))
			{
				numberes=map.get("numberes");
			}
		}
		
  	   Map<String , Shop_info> map=new HashMap<String, Shop_info>();
		Vector<Game_package_info> list = (Vector<Game_package_info>) MiniPackageDao.instance
				.findBySQL("select * from game_package_info where date="+date,Game_package_info.class);
       for(Game_package_info package_info :list){
    	   DCGameEvent eventIDItem  =  JsonTool.toObject(package_info.event_info, DCGameEvent.class);
    	   ConcurrentHashMap<Integer, DCEventItem> blockCacheNode =  eventIDItem.eventInfo;
    	   for(Map.Entry<Integer, DCEventItem> entry : blockCacheNode.entrySet()) {
	        	int _openId = entry.getKey();
	        	DCEventItem  value = entry.getValue();
	        	//System.out.println(_openId);
	        	System.out.println(value.paramsInfo);
	     	   ConcurrentHashMap<String, Integer> blockCacheNodes =  value.paramsInfo;
	     	  for(Entry<String, Integer> entry1 : blockCacheNodes.entrySet()) {
	     		  //System.out.println(entry1.getKey());
	     		String keyString=package_info.date+"_"+entry1.getKey();
	     	    if(_openId==9 || _openId==8){
	     		continue;
	     	    }
	     		if(numberes.equals("0")){
    			 if(map.containsKey(keyString))
    			 { 
    				Shop_info managers=map.get(keyString);
    				 if(_openId==11){
    					 managers.clickcounts+=entry1.getValue();
    	     		 }
    				 if(_openId==12){
    					 managers.iconclickcounts+=entry1.getValue();
    	     		 }
    			 }else {
					Shop_info managers=new Shop_info();
					managers.dates=package_info.date;
					managers.numbers=entry1.getKey();
					if(_openId==11){
   					 managers.clickcounts+=entry1.getValue();
	   	     		 }
	   				 if(_openId==12){
	   					 managers.iconclickcounts+=entry1.getValue();
	   	     		 }
	   				 map.put(keyString, managers);
				}
	     	  }
	     		if(numberes.equals(entry1.getKey())){
	    			 if(map.containsKey(keyString))
	    			 { 
	    				Shop_info managers=map.get(keyString);
	    				 if(_openId==11){
	    					 managers.clickcounts+=entry1.getValue();
	    	     		 }
	    				 if(_openId==12){
	    					 managers.iconclickcounts+=entry1.getValue();
	    	     		 }
	    			 }else {
						Shop_info managers=new Shop_info();
						managers.dates=package_info.date;
						managers.numbers=entry1.getKey();
						if(_openId==11){
	   					 managers.clickcounts+=entry1.getValue();
		   	     		 }
		   				 if(_openId==12){
		   					 managers.iconclickcounts+=entry1.getValue();
		   	     		 }
		   				 map.put(keyString, managers);
					}
		     	  }
	     	  }
    	   }
       }
       Vector<Shop_info> vector=new Vector<Shop_info>();
       if(map!=null&&map.size()>0)
       {
    	   for(String  manlist:map.keySet())
    	   {
    		   vector.add(map.get(manlist));
    	   }
       }
       return vector;
	}
}
