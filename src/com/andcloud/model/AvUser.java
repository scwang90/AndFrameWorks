package com.andcloud.model;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
/**
 * 用户
 * @author 树朾
 */
public class AvUser extends AVUser {

	/**
	 * 昵称
	 */
	private static final String NickName = "NickName";
	/**
	 * 个性化头像
	 */
	private static final String Avator = "Avator";
	/**
	 * 性别
	 */
	private static final String Gender = "Gender";
	/**
	 * 个性前面
	 */
	private static final String Signature = "Signature";

	public AvUser() {
		// TODO Auto-generated constructor stub
	}

	public void setNickName(String name) {
		this.put(NickName, name);
	}

	public String getNickName() {
		return this.getString(NickName);
	}

	public AVFile getAvator() {
		return getAVFile(Avator);
	}

	public void setAvator(AVFile avator) {
		put(Avator, avator);
	}

	public boolean getGender() {
		// TODO Auto-generated method stub
		return getBoolean(Gender);
	}

	public void setGender(boolean gender) {
		// TODO Auto-generated method stub
		put(Gender, gender);
	}

	public String getSignature() {
		// TODO Auto-generated method stub
		return getString(Signature);
	}

	public void setSignature(String signature){
		// TODO Auto-generated method stub
		put(Signature, signature);
	}
}
