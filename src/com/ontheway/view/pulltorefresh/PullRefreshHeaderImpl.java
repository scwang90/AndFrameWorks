package com.ontheway.view.pulltorefresh;

import android.content.Context;
import android.util.AttributeSet;

import com.ontheway.R;

public class PullRefreshHeaderImpl extends AfPullHeaderLayout{

	public PullRefreshHeaderImpl(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public PullRefreshHeaderImpl(Context context, AttributeSet attrs)
    {
		super(context,attrs);
        // TODO Auto-generated method stub
    }

	@Override
	protected String getString(Context context, EnumString string) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
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
			return R.layout.refresh_list_header;
		}
		return 0;
	}

}
