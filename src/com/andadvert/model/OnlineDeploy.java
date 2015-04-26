package com.andadvert.model;

import com.google.gson.Gson;

public class OnlineDeploy {
	public int Verson = 0;
	public String Name = "";
	public String Version = "";
	public boolean HideAd = false;
	public String Remark = "";
	public String Urls = "";
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return new Gson().toJson(this);
	}
}