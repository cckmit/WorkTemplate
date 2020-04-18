package com.code.entity;

import java.util.concurrent.ConcurrentHashMap;

//import cache.DCEventIdCache;

public class DCGameEvent {

	// 默认参数
	//
	public static final String DEFAULT_PARAM = "default";
	
	// 事件详情
	// 事件id - 事件详情
	public ConcurrentHashMap<Integer, DCEventItem> eventInfo = new ConcurrentHashMap<Integer, DCEventItem>();
	
	// 添加事件数量
	//
	public void addEventCount(int eventId, String paramName, int count) {
		String _paramName = paramName == null ? DEFAULT_PARAM : paramName;
		DCEventItem _eventItem = eventInfo.get(eventId);
		
		if(_eventItem == null) {
			_eventItem = new DCEventItem();
			eventInfo.put(eventId, _eventItem);
		}
		
		_eventItem.addParamCount(_paramName, count);
	}
	
	/*// 添加事件数量
	//
	public void addEventCount(String eventName, String paramName, int count) {
		int _eventId = DCEventIdCache.getEventIdByName(eventName);
		addEventCount(_eventId, paramName, count);
	}*/
	
	
}
