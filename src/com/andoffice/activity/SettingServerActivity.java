package com.andoffice.activity;

import com.andframe.application.AfAppSettings;
import com.andframe.feature.AfIntent;
import com.andoffice.activity.framework.cominfo.AbCominfoActivity;
import com.andoffice.activity.framework.cominfo.Item;
import com.andoffice.activity.framework.cominfo.Project;
import com.andoffice.application.AbApplication;

import java.util.List;

public class SettingServerActivity extends AbCominfoActivity{

	protected Item 数域,文域,数端,文端,测试;

	@Override
	protected void onCreate(AfIntent intent, List<Project> list, Mode[] mode) {
		mTitlebar.setTitle("配置服务器");
		AfAppSettings setting = AfAppSettings.getInstance();
		Project prodata = new Project("数据服务");
		prodata.put(数域=new Item(true,"IP或者域名", setting.getDataServerIP()));
		prodata.put(数端=new Item(true,"服务器端口", setting.getDataServerPort()));
		list.add(prodata);
		Project profile = new Project("文件服务");
		profile.put(文域=new Item(true,"IP或者域名", setting.getFileServerIP()));
		profile.put(文端=new Item(true,"服务器端口", setting.getFileServerPort()));
		list.add(profile);
		
		boolean open = AbApplication.getDebugMode()==AbApplication.DEBUG_TESTDATA;
		Project protest = new Project("测试数据");
		protest.put(测试=new Item("开启测试数据",open,Item.CHECKPOWER));
		list.add(protest);
	}

	@Override
	protected void onSubmit(List<Project> ltproject, Mode mode) {
		try {
			AfAppSettings setting = AfAppSettings.getInstance();
			setting.setFileServerIP(文域.value);
			setting.setFileServerPort(文端.ivalue);
			setting.setDataServerIP(数域.value);
			setting.setDataServerPort(数端.ivalue);
			setting.setDebugMode(测试.blvalue?AbApplication.DEBUG_TESTDATA:AbApplication.DEBUG_TEST);
//			AfFileService.setServer(文域.value, 文端.ivalue);
//			AfSoapService.setServer(数域.value, 数端.ivalue);
			AbApplication.setDebugMode(setting.getDebugMode());
			finish();
			makeToastLong("配置服务器成功");
		} catch (Throwable e) {
			makeToastLong("服务器配置失败："+e.getMessage());
		}
	}

}
