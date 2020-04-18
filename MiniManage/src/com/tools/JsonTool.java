package com.tools;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

public class JsonTool
{
	/**
	 * 全局Gson服务
	 */
	private static Gson	globalGson	= null;

	/**
	 * 创建gson
	 */
	private static void createGson()
	{
		if (globalGson != null)
			return;

		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.disableHtmlEscaping();

		globalGson = gsonBuilder.create();
	}

	public static String toJson(Object obj)
	{
		createGson();
		return globalGson.toJson(obj);
	}

	public static <T> T toObject(String json, Class<T> classOfT)
	{
		createGson();
		return globalGson.fromJson(json, classOfT);
	}
	
	// json转list
	//
	public static <T> ArrayList<T> jsonToArrayList(String json, Class<T> clazz) {
		Type _type = new TypeToken<ArrayList<JsonObject>>()
		{
		}.getType();
		
		ArrayList<JsonObject> _jsonObjList = new Gson().fromJson(json, _type);
		ArrayList<T> _resList = new ArrayList<T>();
		int _count = _jsonObjList.size();
		
		for(int i = 0; i < _count; i++) {
			JsonObject _obj = _jsonObjList.get(i);
			_resList.add(new Gson().fromJson(_obj, clazz));
		}
		
		return _resList;
	}
}


















