package com.tools;

import java.util.Vector;

public class TableShow
{
	// 表头
	private Vector<Object> ths = new Vector<Object>();
	// 表列信息
	private Vector<Vector<Object>> tds = new Vector<Vector<Object>>();

	public Vector<Object> getThs()
	{
		return ths;
	}

	public void setThs(Vector<Object> ths)
	{
		this.ths = ths;
	}

	public Vector<Vector<Object>> getTds()
	{
		return tds;
	}

	public void setTds(Vector<Vector<Object>> tds)
	{
		this.tds = tds;
	}

	public void addTds(Vector<Vector<Object>> tds)
	{
		this.tds.addAll(tds);
	}

	public void addTd(Vector<Object> td)
	{
		// TODO Auto-generated method stub
		this.tds.addElement(td);
	}

}
