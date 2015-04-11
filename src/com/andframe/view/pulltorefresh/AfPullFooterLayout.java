package com.andframe.view.pulltorefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public abstract class AfPullFooterLayout extends AfPullHeaderLayout
{
	protected AfPullHeaderLayout mHeader = null;
	
	protected String pullLabel;
    protected String refreshingLabel;
    protected String releaseLabel;

    protected enum EnumFooterString{
    	footer_pullup,footer_updatetime,footer_loading,footer_release
    }
    
    protected abstract String getFooterString(Context context,EnumFooterString string);

    protected abstract AfPullHeaderLayout getHeader();

    public AfPullFooterLayout(Context context)
    {
        super(context);
        // TODO Auto-generated constructor stub
        this.initailize(context);
    }
    
    public AfPullFooterLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        this.initailize(context);
    }
    
    private void initailize(Context context)
    {
        pullLabel = getFooterString(context, EnumFooterString.footer_pullup);
        refreshingLabel = getFooterString(context, EnumFooterString.footer_loading);
        releaseLabel = getFooterString(context, EnumFooterString.footer_release);
        
        //mHeaderImage.setImageResource(R.drawable.refresh_list_release_up);

        mUpdateText.setVisibility(View.GONE);
        mHeaderImage.startAnimation(animation);
    }
    
    public void setImageUnused(){
        mHeaderImage.setVisibility(View.GONE);
    	mHeaderImage = null;
    }
    
    public void reset()
    {
    	mHeaderText.setText(pullLabel);
        mHeaderProgress.setVisibility(View.GONE);
        if(mHeaderImage != null){
        	mHeaderImage.setVisibility(View.VISIBLE);
        }
    }

    public void releaseToRefresh()
    {
        mHeaderText.setText(releaseLabel);
        if(mHeaderImage != null){
        	mHeaderImage.clearAnimation();
        	mHeaderImage.startAnimation(animationreset);
        }
    }

    public void refreshing()
    {
        mHeaderText.setText(refreshingLabel);
        mHeaderProgress.setVisibility(View.VISIBLE);
        if(mHeaderImage != null){
        	mHeaderImage.clearAnimation();
        	mHeaderImage.setVisibility(View.INVISIBLE);
        }
    }

    public void setLabelPull(String pullLabel)
    {
        this.pullLabel = pullLabel;
        if(mHeaderProgress.getVisibility() != View.VISIBLE){
        	mHeaderText.setText(pullLabel);
        }
    }
    
    public void setLabelRefreshing(String refreshingLabel)
    {
        this.refreshingLabel = refreshingLabel;
        if(mHeaderProgress.getVisibility() == View.VISIBLE){
        	mHeaderText.setText(refreshingLabel);
        }
    }

    public void setLabelRelease(String releaseLabel)
    {
        this.releaseLabel = releaseLabel;
    }

    public void pullToRefresh()
    {
        mHeaderText.setText(pullLabel);
        mHeaderImage.clearAnimation();
        mHeaderImage.startAnimation(animation);
    }

    public void setTextColor(int color)
    {
        mHeaderText.setTextColor(color);
    }

    public void setText(String text)
    {
        mHeaderText.setText(text);
    }

	@Override
	protected int getViewID(EnumViewID id) {
		// TODO Auto-generated method stub
		if(mHeader == null)
		{
			mHeader = getHeader();
		}
		return mHeader.getViewID(id);
	}

	@Override
	protected String getString(Context context, EnumString string) {
		// TODO Auto-generated method stub
//		if(string == EnumString.header_loading)
//		{
//			return getFooterString(context,EnumFooterString.footer_loading);
//		}
//		else if(string == EnumString.header_release)
//		{
//			return getFooterString(context,EnumFooterString.footer_release);
//		}
//		else if(string == EnumString.header_updatetime)
//		{
//			return getFooterString(context,EnumFooterString.footer_updatetime);
//		}
//		else if(string == EnumString.header_pulldown)
//		{
//			return getFooterString(context,EnumFooterString.footer_pullup);
//		}
		return "";
	}
}
