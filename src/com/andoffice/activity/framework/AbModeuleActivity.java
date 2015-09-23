package com.andoffice.activity.framework;

import android.content.DialogInterface;
import android.os.Bundle;

import com.andframe.activity.AfActivity;
import com.andframe.exception.AfToastException;
import com.andframe.feature.AfIntent;
import com.andoffice.R;
import com.andoffice.bean.Permission;

public abstract class AbModeuleActivity extends AfActivity {

	public static final String EXTRA_TITLE = "EXTRA_TITLE";
	public static final String EXTRA_PERMISSION = "EXTRA_PERMISSION";

	protected String mTitleText = "";
	protected Permission mPermission = null;

	protected void onCreate(Bundle bundle,AfIntent intent) throws Exception {
		super.onCreate(bundle, intent);
		mTitleText = intent.getString(EXTRA_TITLE, getString(R.string.app_name));
		mPermission = intent.get(EXTRA_PERMISSION, Permission.class);
		if(mPermission == null){
			throw new AfToastException("没有设置模块权限！");
		}else if(mPermission.IsRead == false){
//			throw new AfToastException("您没有权限浏览本模块！");
			//throw new AfToastException("您没有权限浏览本模块！");
			doShowDialog("警告提示", "您已经被管理员取消本模块的使用权限！", 
					new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int index) {
					getActivity().finish();
				}
			});
			return ;
		}
	}

}
