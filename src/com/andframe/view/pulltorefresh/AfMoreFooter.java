package com.andframe.view.pulltorefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.andframe.R;

public class AfMoreFooter extends FrameLayout
{
    public interface OnMoreListener {
        boolean onMore();
    }

    protected ProgressBar mFooterProgress;
    protected TextView mFooterText;

	protected String moreLabel;
    protected String refreshingLabel;

    protected enum EnumFooterString{
    	footer_more,footer_loading
    }

    public AfMoreFooter(Context context)
    {
        super(context);
        this.initailize(context);
    }

    public AfMoreFooter(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.initailize(context);
    }
    
    private void initailize(Context context)
    {
        View header = LayoutInflater.from(context).inflate(R.layout.af_refresh_list_footer, this);
        mFooterText = (TextView) header.findViewById(R.id.refresh_list_footer_text);
        mFooterProgress = (ProgressBar) header.findViewById(R.id.refresh_list_footer_progressbar);

        moreLabel = getFooterString(context, EnumFooterString.footer_more);
        refreshingLabel = getFooterString(context, EnumFooterString.footer_loading);
    }

    public void setOnMoreListener(final OnMoreListener listener){
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (!isRefreshing() && listener.onMore()){
                        refreshing();
                    }
                } catch (Throwable e){

                }
            }
        });
    }

    protected String getFooterString(Context context,EnumFooterString string){
        if(string == EnumFooterString.footer_loading)
        {
            return "正在加载";
        }
        else if(string == EnumFooterString.footer_more)
        {
            return "点击获取更多";
        }
        return "";
    }

    public boolean isRefreshing(){
        return mFooterProgress.getVisibility() == VISIBLE;
    }

    public void reset()
    {
    	mFooterText.setText(moreLabel);
        mFooterProgress.setVisibility(View.GONE);
    }

    public void refreshing()
    {
        mFooterText.setText(refreshingLabel);
        mFooterProgress.setVisibility(View.VISIBLE);
    }

    public void setLabelPull(String pullLabel)
    {
        this.moreLabel = pullLabel;
        if(mFooterProgress.getVisibility() != View.VISIBLE){
        	mFooterText.setText(pullLabel);
        }
    }
    
    public void setLabelRefreshing(String refreshingLabel)
    {
        this.refreshingLabel = refreshingLabel;
        if(mFooterProgress.getVisibility() == View.VISIBLE){
        	mFooterText.setText(refreshingLabel);
        }
    }

    public void pullToRefresh()
    {
        mFooterText.setText(moreLabel);
    }

    public void setTextColor(int color)
    {
        mFooterText.setTextColor(color);
    }

    public void setText(String text)
    {
        mFooterText.setText(text);
    }

}
