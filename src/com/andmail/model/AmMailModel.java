package com.andmail.model;

public class AmMailModel implements com.andmail.api.model.MailModel {

	public String host = "";//"imap.163.com";
	public String username = "";//"zaituren@163.com";
	public String password = "";

	@Override
	public String getHost() {
		return host;
	}

	@Override
	public void setHost(String host) {
		this.host = host;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public void setPassword(String password) {
		this.password = password;
	}

}
