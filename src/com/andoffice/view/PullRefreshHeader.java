package com.andoffice.view;

import android.content.Context;
import android.util.AttributeSet;

import com.andoffice.R;
import com.andframe.view.pulltorefresh.AfPullHeaderLayout;

public class PullRefreshHeader extends AfPullHeaderLayout{

	public PullRefreshHeader(Context context) {
		super(context);
	}

	public PullRefreshHeader(Context context, AttributeSet attrs)
    {
		super(context,attrs);
    }

	@Override
	protected String getString(Context context, EnumString string) {
		if(string == EnumString.header_loading)
		{
			return "正在加载";
		}
		else if(string == EnumString.header_release)
		{
			return "释放刷新";
		}
		else if(string == EnumString.header_updatetime)
		{
			return "上次更新";
		}
		else if(string == EnumString.header_pulldown)
		{
			return "下拉刷新";
		}
		return "";
	}

	@Override
	protected int getViewID(EnumViewID id) {
		if(id == EnumViewID.header_image)
		{
			return R.id.refresh_list_header_pull_down;
		}
		else if(id == EnumViewID.header_progress)
		{
			return R.id.refresh_list_header_progressbar;
		}
		else if(id == EnumViewID.header_text)
		{
			return R.id.refresh_list_header_text;
		}
		else if(id == EnumViewID.update_text)
		{
			return R.id.refresh_list_header_last_update;
		}
		else if(id == EnumViewID.header_view)
		{
			return R.layout.af_refresh_list_header;
		}
		return 0;
	}

}
