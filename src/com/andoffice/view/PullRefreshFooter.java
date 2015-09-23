package com.andoffice.view;

import android.content.Context;
import android.util.AttributeSet;

import com.andframe.view.pulltorefresh.AfPullFooterLayout;
import com.andframe.view.pulltorefresh.AfPullHeaderLayout;

public class PullRefreshFooter extends AfPullFooterLayout{

	public PullRefreshFooter(Context context) {
		super(context);
	}

	public PullRefreshFooter(Context context, AttributeSet attrs)
    {
		super(context,attrs);
    }

	@Override
	protected String getFooterString(Context context, EnumFooterString string) {
		if(string == EnumFooterString.footer_loading)
		{
			return "正在加载";
		}
		else if(string == EnumFooterString.footer_release)
		{
			return "释放获取更多";
		}
		else if(string == EnumFooterString.footer_updatetime)
		{
			return "上次更新";
		}
		else if(string == EnumFooterString.footer_pullup)
		{
			return "上拉获取更多";
		}
		return "";
	}

	@Override
	protected AfPullHeaderLayout getHeader() {
		return new PullRefreshHeader(getContext());
	}

}
