package com.andcloud.domain;

import com.andcloud.model.AvUser;
import com.andframe.application.AfApplication;
import com.andframe.exception.AfToastException;
import com.andframe.util.android.AfNetwork;
import com.avos.avoscloud.AVException;

public class AvUserDomain extends AvDomain<AvUser>{

	public void signUp(String username,String password,String nickname) throws AVException{
		AvUser user = new AvUser();
		user.setUsername(username);
		user.setPassword(password);
//		user.setEmail("steve@company.com");
		user.setNickName(nickname);
		user.signUp();
	}

	public AvUser logIn(String username,String password) throws Exception {
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

	public AvUser getCurrentUser() throws AVException {
		return AvUser.getCurrentUser(AvUser.class);
	}

	public void logOut() throws AVException {
		AvUser.logOut();
	}
}
