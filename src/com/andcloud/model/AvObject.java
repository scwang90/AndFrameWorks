package com.andcloud.model;

import com.andcloud.AndCloud;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.SaveCallback;

/**
 * 基类对象
 * @author 树朾
 */
public class AvObject extends com.avos.avoscloud.AvObject implements AvModel {

	public static final String Id = "objectId";
	public static final String CreatedTime = "createdAt";
	public static final String UpdatedTime = "updatedAt";

	@Override
	public void saveInBackground() {
		if (AndCloud.mDebug) {
			super.saveInBackground(new SaveCallback() {
				@Override
				public void done(AVException e) {
					if (e != null) {
						e.printStackTrace();
					}
				}
			});
		} else {
			super.saveInBackground();
		}
	}
}
