package com.andcloud.domain;

import com.andcloud.model.AvUser;
import com.andframe.application.AfApplication;
import com.andframe.exception.AfToastException;
import com.andframe.util.android.AfNetwork;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;

import java.util.Date;

public class AvUserDomain extends AvDomain<AvUser>{

	static AvUser loginUser = null;

	public static void signUp(String username,String password,String nickname) throws AVException{
		AvUser user = new AvUser();
		user.setUsername(username);
		user.setPassword(password);
//		user.setEmail("steve@company.com");
		user.setNickName(nickname);
		user.signUp();
	}

	public static AvUser logIn(String username,String password) throws Exception {
		try {
			AvUser.logOut();
			return AvUser.logIn(username, password,AvUser.class);
		} catch (AVException e){
			if (AfNetwork.getNetworkState(AfApplication.getApp()) == AfNetwork.TYPE_MOBILE){
				throw new AfToastException("您的移动网络可能不稳定，请切换WIFI再试");
			} else {
				throw e;
			}
		}
	}

	public static AvUser getCurrentUser() throws AVException {
		if (loginUser == null) {
			loginUser = AVUser.getCurrentUser(AvUser.class);
			if (loginUser != null) {
				loginUser.setLastLogin(new Date());
				loginUser.saveInBackground();
			}
		}
		return loginUser;
	}

	public static void logOut() throws AVException {
		loginUser = null;
		AvUser.logOut();
	}
}
