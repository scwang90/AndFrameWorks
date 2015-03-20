package com.ontheway.view.pulltorefresh;

import android.content.Context;
import android.util.AttributeSet;


public class PullRefreshFooterImpl extends AfPullFooterLayout{

	public PullRefreshFooterImpl(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public PullRefreshFooterImpl(Context context, AttributeSet attrs)
    {
		super(context,attrs);
        // TODO Auto-generated method stub
    }

	@Override
	protected String getFooterString(Context context, EnumFooterString string) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		return new PullRefreshHeaderImpl(getContext());
	}

}
