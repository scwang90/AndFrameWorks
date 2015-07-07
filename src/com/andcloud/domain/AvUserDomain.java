package com.andcloud.domain;

import com.andcloud.model.AvUser;
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

	public AvUser logIn(String username,String password) throws AVException {
		// TODO Auto-generated method stub
		AvUser.logOut();
		return AvUser.logIn(username, password,AvUser.class);
	}

	public AvUser getCurrentUser() throws AVException {
		// TODO Auto-generated method stub
		return AvUser.getCurrentUser(AvUser.class);
	}

	public void logOut() throws AVException {
		// TODO Auto-generated method stub
		AvUser.logOut();
	}
}
