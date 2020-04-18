package com.code.json;

public class PermissionJson
{
	private Integer id;
	private Integer parentId;
	private String name;
	private String memo;
	private String url;
	
	public Integer getId()
	{
		return id;
	}
	public void setId(Integer id)
	{
		this.id = id;
	}
	public Integer getParentId()
	{
		return parentId;
	}
	public void setParentId(Integer parentId)
	{
		this.parentId = parentId;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getMemo()
	{
		return memo;
	}
	public void setMemo(String memo)
	{
		this.memo = memo;
	}
	public String getUrl()
	{
		return url;
	}
	public void setUrl(String url)
	{
		this.url = url;
	}
	
}
