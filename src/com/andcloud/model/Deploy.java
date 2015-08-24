package com.andcloud.model;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

import java.io.Serializable;

@AVClassName("Deploy")
@SuppressWarnings("serial")
public class Deploy extends AVObject implements Serializable {
	/**
	 * 版本配置
	 */
	public static final String Name = "Name";
	public static final String Version = "Version";
	public static final String BusinessModel = "BusinessModel";
	public static final String Remark = "Remark";
	public static final String Urls = "Urls";

	public boolean isBusinessModel() {
		return getBoolean(BusinessModel);
	}

	public void setBusinessModel(boolean businessModel) {
		put(BusinessModel,businessModel);
	}

	public String getName() {
		return getString(Name);
	}

	public void setName(String name) {
		put(Name,name);
	}

	public String getRemark() {
		return getString(Remark);
	}

	public void setRemark(String remark) {
		put(Remark,remark);
	}

	public String getUrls() {
		return getString(Urls);
	}

	public void setUrls(String urls) {
		put(Urls,urls);
	}

	public int getVerson() {
		return getInt(Version);
	}

	public void setVerson(int verson) {
		put(Version,verson);
	}

	@Override
	public String toString() {
		return "OnlineDeploy{" +
				"BusinessModel=" + isBusinessModel() +
				", Verson=" + getVerson() +
				", Name='" + getName() + '\'' +
				", Version='" + getVerson() + '\'' +
				", Remark='" + getRemark() + '\'' +
				", Urls='" + getUrls() + '\'' +
				'}';
	}
}