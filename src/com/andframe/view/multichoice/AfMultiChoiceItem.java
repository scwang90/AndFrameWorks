package com.andframe.view.multichoice;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build.VERSION;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.andframe.adapter.AfListAdapter.IAfLayoutItem;
import com.andframe.feature.AfDensity;


public abstract class AfMultiChoiceItem<T> implements IAfLayoutItem<T>, OnClickListener{

	public enum SelectStatus{
		NONE,UNSELECT,SELECTED
	}

	protected static final int SD_CHECK = 0;
	protected static final int SD_BACKGROUNG = 1;
	
	protected T mModel = null;
	protected SelectStatus mSelectStatus = SelectStatus.NONE;
	
	protected View mMultiChoiceContent = null;
	protected CheckBox mMultiChoiceCheckBox = null;
	protected LinearLayout mMultiChoiceLayout = null;
	protected AfMultiChoiceAdapter<T> mAdapter = null;

	protected int mSelectDisplay = SD_BACKGROUNG;
	
	@Override
	public void onBinding(T model,int index) {
		// TODO Auto-generated method stub
		if(!onBinding(mModel = model,index,mSelectStatus) && mMultiChoiceCheckBox!=null){
			if(mSelectDisplay == SD_CHECK){
				switch (mSelectStatus) {
				case NONE:
					mMultiChoiceCheckBox.setVisibility(View.GONE);
					break;
				case UNSELECT:
					mMultiChoiceCheckBox.setVisibility(View.VISIBLE);
					mMultiChoiceCheckBox.setChecked(false);
					break;
				case SELECTED:
					mMultiChoiceCheckBox.setVisibility(View.VISIBLE);
					mMultiChoiceCheckBox.setChecked(true);
					break;
				}
			}else if (mSelectDisplay == SD_BACKGROUNG) {
				switch (mSelectStatus) {
				case NONE:
				case UNSELECT:
					mMultiChoiceLayout.setBackgroundColor(0);
					break;
				case SELECTED:
					int color = Color.parseColor("#FF0099E5");
					mMultiChoiceLayout.setBackgroundColor(color);
					break;
				}
			}
		}
	}
	
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public View inflateLayout(View view,AfMultiChoiceAdapter<T> adapter) {
		// TODO Auto-generated method stub
		mAdapter = adapter;
		
		
		mMultiChoiceContent = view;
		mMultiChoiceContent.setFocusable(false);
		mMultiChoiceLayout = new LinearLayout(view.getContext());
		mMultiChoiceLayout.setOrientation(LinearLayout.HORIZONTAL);
		mMultiChoiceLayout.setGravity(Gravity.CENTER_VERTICAL);
		
		
		if(VERSION.SDK_INT < 16){
			mMultiChoiceLayout.setBackgroundDrawable(view.getBackground());
		}else{
			mMultiChoiceLayout.setBackground(view.getBackground());
		}
		view.setBackgroundResource(android.R.color.transparent);

		int parent = LayoutParams.MATCH_PARENT;
		int content = LayoutParams.WRAP_CONTENT;
		LayoutParams lpView = new LayoutParams(parent,content);
		lpView.width = 0;
		lpView.weight = 1;
		mMultiChoiceLayout.addView(view,lpView);

		int margin = AfDensity.dip2px(view.getContext(), 3);
		
		LayoutParams lpcheck = new LayoutParams(content,content);		
		lpcheck.width = LayoutParams.WRAP_CONTENT;
		lpcheck.weight = 0;
		lpcheck.setMargins(margin, margin, margin, margin);
		mMultiChoiceCheckBox = new CheckBox(view.getContext());
		mMultiChoiceCheckBox.setOnClickListener(this);
		mMultiChoiceCheckBox.setFocusable(false);
		mMultiChoiceCheckBox.setVisibility(View.GONE);
		
		mMultiChoiceLayout.addView(mMultiChoiceCheckBox,lpcheck);
		view.setBackgroundResource(0);
		return mMultiChoiceLayout;
	}

	public void setSelectStatus(T model,SelectStatus status){
		mModel = model;
		mSelectStatus = status;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == mMultiChoiceContent){
			mMultiChoiceCheckBox.setChecked(!mMultiChoiceCheckBox.isChecked());
		}else if(v == mMultiChoiceCheckBox){
			notifyItemSelectChanged(mMultiChoiceCheckBox.isChecked());
		}
	}
	
	protected void notifyItemSelectChanged(boolean checked) {
		// TODO Auto-generated method stub
		if(mModel != null){
			mAdapter.setSelect(mModel,checked);
		}
	}

	/**
	 * @param model 布局绑定的 数据 model
	 * @param status 选择状态{NONE,UNSELECT,SELECTED}
	 * @return 绘制选择状态 返回 TRUE 否则 FALSE
	 */
	protected abstract boolean onBinding(T model,int index,SelectStatus status);
}
