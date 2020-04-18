package com.code.entity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DCEventItem {

	// 参数详情
	// 参数名称  - 参数数量
	//
	public ConcurrentHashMap<String, Integer> paramsInfo = new ConcurrentHashMap<String, Integer>();
	
	// 添加参数数量
	//
	public void addParamCount(String paramName, int count) {
		Integer _value = paramsInfo.get(paramName);
		if(_value == null) {
			paramsInfo.put(paramName, count);
			return;
		}
		
		_value += count;
	}
	// 获取参数数量
	//
	public int getParamCount(String paramName) {
		Integer _value = paramsInfo.get(paramName);
		if(_value == null) {
			return 0;
		}	
		return _value.intValue();
	}
	// 获取事件总数量
	//
	public int getTotalCount() {
		int _totalCount = 0;
		
		for(Map.Entry<String, Integer> entry : paramsInfo.entrySet()) {
			_totalCount += entry.getValue();
		}
		
		return _totalCount;
	}
}
