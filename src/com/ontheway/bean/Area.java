package com.ontheway.bean;

import java.util.ArrayList;
import java.util.List;

public class Area {
	
	private int ID=-1;
	public String Name="";
	public int Level=-1;
	public int Pid=-1;
	
	public Area PArea=null;
	public List<Area> Children = new ArrayList<Area>();
	
	public Area(){}
	
	public Area(int id,String name,int level,int pid)
	{
		this.ID = id;
		if(name != null) this.Name = name;
		this.Level = level;
		this.Pid = pid;
	}
	
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	
	/**
	 * 直辖市
	 * 0 : 北京市
	 * 1 : 天津市
	 * 2 : 上海市
	 * 3 : 重庆市
	 */
	public static final List<Area> Municipalities = new ArrayList<Area>();

	static {
		Municipalities.add(new Area(1, "北京市", 1, 0));
		Municipalities.add(new Area(2, "天津市", 1, 0));
		Municipalities.add(new Area(9, "上海市", 1, 0));
		Municipalities.add(new Area(22, "重庆市", 1, 0));
	}
}