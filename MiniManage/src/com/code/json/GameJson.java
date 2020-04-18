package com.code.json;

import java.util.List;

public class GameJson
{
	private String deviceOrientation;
	private String openDataContext;
	private List<String> navigateToMiniProgramAppIdList;
	public String getDeviceOrientation()
	{
		return deviceOrientation;
	}
	public void setDeviceOrientation(String deviceOrientation)
	{
		this.deviceOrientation = deviceOrientation;
	}
	public String getOpenDataContext()
	{
		return openDataContext;
	}
	public void setOpenDataContext(String openDataContext)
	{
		this.openDataContext = openDataContext;
	}
	public List<String> getNavigateToMiniProgramAppIdList()
	{
		return navigateToMiniProgramAppIdList;
	}
	public void setNavigateToMiniProgramAppIdList(
			List<String> navigateToMiniProgramAppIdList)
	{
		this.navigateToMiniProgramAppIdList = navigateToMiniProgramAppIdList;
	}
	
	
}
