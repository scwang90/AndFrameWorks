package com.ontheway.model;

import com.ontheway.model.framework.AfModel;

/**
 * ПаІб
 */
public class Photo extends AfModel {
	
	public String Url = "";
	public String Describe = "";
	public String SmallUrl = "";

	public Photo() {
		// TODO Auto-generated constructor stub
	}

	public Photo(String Name, String Url, String Describe) {
		// TODO Auto-generated method stub
		this.Name = Name;
		this.Describe = Describe;
		this.Url = Url;
	}
	
	public Photo(String Name, String Url, String Describe,String small) {
		this(Name,Url,Describe);
		// TODO Auto-generated method stub
		this.SmallUrl = small;
	}
	
}
