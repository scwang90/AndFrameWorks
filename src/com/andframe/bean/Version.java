package com.andframe.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import com.andframe.util.UUIDUtil;

/**
 * 版本标识
 * 用于更新服务使用
 */
@SuppressWarnings("serial")
public class Version implements Serializable{

	private UUID ID = UUIDUtil.Empty;

	// 版本描述字符串
	public String Version = "";
	// 版本更新时间
	public Date UpdateTime = new Date();
	// 新版本更新下载地址
	public String DownloadURL = "";
	// 更新描述信息
	public String DisplayMessage = "";
	// 版本号
	public int VersionCode = 0;
	// apk名称
	public String ApkName = "";

	public Version() {
		// TODO Auto-generated constructor stub
		this.ID = UUID.randomUUID();
	}

	public Version(String version, Date date, String url, String msg, int code,
			String apk) {
		// TODO Auto-generated constructor stub
		this.ID = UUID.randomUUID();
		this.VersionCode = code;
		this.Version = version;
		this.UpdateTime = date;
		this.DownloadURL = url;
		this.DisplayMessage = msg;
		this.ApkName = apk;
	}

	public Version(Version version) {
		// TODO Auto-generated constructor stub
		this.VersionCode = version.VersionCode;
		this.ID = version.ID;
		this.Version = version.Version;
		this.UpdateTime = version.UpdateTime;
		this.DownloadURL = version.DownloadURL;
		this.DisplayMessage = version.DisplayMessage;
		this.ApkName = version.ApkName;
	}

	public UUID getID() {
		return ID;
	}

	public void setID(UUID id) {
		ID = id;
	}
}
