package com.andadver;

import java.io.Serializable;

import com.google.gson.Gson;

public class OnlineDeploy implements Serializable{
	private static final long serialVersionUID = -3444358992372197247L;
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