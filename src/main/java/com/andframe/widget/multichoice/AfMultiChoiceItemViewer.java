package com.andframe.widget.multichoice;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build.VERSION;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.andframe.adapter.itemviewer.AfItemViewer;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * 多选ITEM模板
 * @param <T>
 */
@SuppressWarnings("unused")
public abstract class AfMultiChoiceItemViewer<T> extends AfItemViewer<T> implements OnClickListener{

	public enum SelectStatus {
		NONE,UNSELECT,SELECTED
	}

	protected static final int SD_NONE = -1;
	protected static final int SD_CHECK = 0;
	protected static final int SD_BACKGROUNG = 1;
	protected static final int SD_CHECK_LEFT = 2;
	
	protected T mModel = null;
	protected SelectStatus mSelectStatus = SelectStatus.NONE;
	
	protected View mMultiChoiceContent = null;
	protected CheckBox mMultiChoiceCheckBox = null;
	protected LinearLayout mMultiChoiceLayout = null;
	protected AfMultiChoiceAdapter<T> mAdapter = null;
	protected int mSelectedColor = Color.parseColor("#FF0099E5");

	protected int mSelectDisplay = SD_BACKGROUNG;
	
	public AfMultiChoiceItemViewer() {
	}

	public AfMultiChoiceItemViewer(int layoutId) {
		super(layoutId);
	}

	@Override
	public void onBinding(T model, int index) {
		if(!onBinding(mModel = model,index,mSelectStatus) && mMultiChoiceCheckBox!=null) {
			if(mSelectDisplay == SD_CHECK || mSelectDisplay == SD_CHECK_LEFT){
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
					mMultiChoiceLayout.setBackgroundColor(mSelectedColor);
					break;
				}
			}
		}
	}
	
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public View inflateLayout(View view, AfMultiChoiceAdapter<T> adapter) {
		mAdapter = adapter;

		if (mSelectDisplay == SD_NONE) {
			return view;
		} else if (mMultiChoiceLayout != null && mSelectDisplay == SD_BACKGROUNG) {
			return view;
		} else if (mMultiChoiceCheckBox != null && mSelectDisplay != SD_BACKGROUNG) {
			return view;
		}
		
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

		ViewGroup.LayoutParams params = view.getLayoutParams();
		mMultiChoiceLayout.addView(view, MATCH_PARENT, WRAP_CONTENT);
		if (params != null) {
			mMultiChoiceLayout.setLayoutParams(params);
		}

		float scale = view.getContext().getResources().getDisplayMetrics().density;
		int margin = (int) (scale * 3 + 5.0f);
		
		LayoutParams lpcheck = new LayoutParams(WRAP_CONTENT,WRAP_CONTENT);
		lpcheck.weight = 0;
		lpcheck.setMargins(margin, margin, margin, margin);
		try {
			mMultiChoiceCheckBox = new CheckBox(view.getContext());
			mMultiChoiceCheckBox.setOnClickListener(this);
			mMultiChoiceCheckBox.setFocusable(false);
			mMultiChoiceCheckBox.setVisibility(View.GONE);

			if (mSelectDisplay == SD_CHECK_LEFT) {
				mMultiChoiceCheckBox.setLayoutParams(lpcheck);
				mMultiChoiceLayout.addView(mMultiChoiceCheckBox, 0);
			} else {
				mMultiChoiceLayout.addView(mMultiChoiceCheckBox, lpcheck);
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		view.setBackgroundResource(0);
		mMultiChoiceLayout.setLayoutParams(view.getLayoutParams());
		return mMultiChoiceLayout;
	}

	public void setSelectStatus(T model,SelectStatus status){
		mModel = model;
		mSelectStatus = status;
	}

	@Override
	public void onClick(View v) {
		if(v == mMultiChoiceContent){
			mMultiChoiceCheckBox.setChecked(!mMultiChoiceCheckBox.isChecked());
		}else if(v == mMultiChoiceCheckBox){
			notifyItemSelectChanged(mMultiChoiceCheckBox.isChecked());
		}
	}
	
	protected void notifyItemSelectChanged(boolean checked) {
		if(mModel != null){
			mAdapter.setSelect(mModel,checked);
		}
	}

	/**
	 * @param model 布局绑定的 数据 model
	 * @param status 选择状态{NONE,UNSELECT,SELECTED}
	 * @return 绘制选择状态 返回 TRUE 否则 FALSE
	 */
	protected abstract boolean onBinding(T model, int index, SelectStatus status);
}
