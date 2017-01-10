package com.andmail.model;

import com.andmail.api.model.MailReaderModel;

public class AmReaderModel extends AmMailModel implements MailReaderModel {
	
	public String folder = "";//"BACK";
	
	@Override
	public String getFolder() {
		return folder;
	}

	@Override
	public void setFolder(String folder) {
		this.folder = folder;
	}

}

