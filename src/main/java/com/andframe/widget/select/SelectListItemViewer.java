package com.andframe.widget.select;

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

import com.andframe.adapter.item.ListItemViewer;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * 多选ITEM模板
 * @param <T>
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class SelectListItemViewer<T> extends ListItemViewer<T> implements OnClickListener{

	public enum SelectStatus {
		NONE, UN_SELECT, SELECTED,
	}

	protected static final int SD_NONE = -1;
	protected static final int SD_CHECK = 0;
	protected static final int SD_BACKGROUND = 1;
	protected static final int SD_CHECK_LEFT = 2;
	
	protected T mModel = null;
	protected SelectStatus mSelectStatus = SelectStatus.NONE;
	
	protected View mSelectContent = null;
	protected CheckBox mSelectCheckBox = null;
	protected LinearLayout mSelectLayout = null;
	protected SelectListItemAdapter<T> mAdapter = null;
	protected int mSelectedColor = Color.parseColor("#FF0099E5");

	protected int mSelectDisplay = SD_BACKGROUND;
	
	public SelectListItemViewer() {
	}

	public SelectListItemViewer(int layoutId) {
		super(layoutId);
	}

	@Override
	public void onBinding(T model, int index) {
		if(!onBinding(mModel = model,index,mSelectStatus) && mSelectCheckBox !=null) {
			if(mSelectDisplay == SD_CHECK || mSelectDisplay == SD_CHECK_LEFT){
				switch (mSelectStatus) {
				case NONE:
					mSelectCheckBox.setVisibility(View.GONE);
					break;
				case UN_SELECT:
					mSelectCheckBox.setVisibility(View.VISIBLE);
					mSelectCheckBox.setChecked(false);
					break;
				case SELECTED:
					mSelectCheckBox.setVisibility(View.VISIBLE);
					mSelectCheckBox.setChecked(true);
					break;
				}
			}else if (mSelectDisplay == SD_BACKGROUND) {
				switch (mSelectStatus) {
				case NONE:
				case UN_SELECT:
					mSelectLayout.setBackgroundColor(0);
					break;
				case SELECTED:
					mSelectLayout.setBackgroundColor(mSelectedColor);
					break;
				}
			}
		}
	}
	
	@SuppressLint("NewApi")
	public View inflateLayout(View view, SelectListItemAdapter<T> adapter) {
		mAdapter = adapter;

		if (mSelectDisplay == SD_NONE) {
			return view;
		} else if (mSelectLayout != null && mSelectDisplay == SD_BACKGROUND) {
			return view;
		} else if (mSelectCheckBox != null && mSelectDisplay != SD_BACKGROUND) {
			return view;
		}
		
		mSelectContent = view;
		mSelectContent.setFocusable(false);
		mSelectLayout = new LinearLayout(view.getContext());
		mSelectLayout.setOrientation(LinearLayout.HORIZONTAL);
		mSelectLayout.setGravity(Gravity.CENTER_VERTICAL);
		
		
		if(VERSION.SDK_INT < 16){
			mSelectLayout.setBackgroundDrawable(view.getBackground());
		}else{
			mSelectLayout.setBackground(view.getBackground());
		}
		view.setBackgroundResource(android.R.color.transparent);

		ViewGroup.LayoutParams params = view.getLayoutParams();
		mSelectLayout.addView(view, MATCH_PARENT, WRAP_CONTENT);
		if (params != null) {
			mSelectLayout.setLayoutParams(params);
		}

		float scale = view.getContext().getResources().getDisplayMetrics().density;
		int margin = (int) (scale * 3 + 5.0f);
		
		LayoutParams lpcheck = new LayoutParams(WRAP_CONTENT,WRAP_CONTENT);
		lpcheck.weight = 0;
		lpcheck.setMargins(margin, margin, margin, margin);
		try {
			mSelectCheckBox = new CheckBox(view.getContext());
			mSelectCheckBox.setOnClickListener(this);
			mSelectCheckBox.setFocusable(false);
			mSelectCheckBox.setVisibility(View.GONE);

			if (mSelectDisplay == SD_CHECK_LEFT) {
				mSelectCheckBox.setLayoutParams(lpcheck);
				mSelectLayout.addView(mSelectCheckBox, 0);
			} else {
				mSelectLayout.addView(mSelectCheckBox, lpcheck);
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		view.setBackgroundResource(0);
		mSelectLayout.setLayoutParams(view.getLayoutParams());
		return mSelectLayout;
	}

	public void setSelectStatus(T model,SelectStatus status){
		mModel = model;
		mSelectStatus = status;
	}

	@Override
	public void onClick(View v) {
		if(v == mSelectContent){
			mSelectCheckBox.setChecked(!mSelectCheckBox.isChecked());
		}else if(v == mSelectCheckBox){
			notifyItemSelectChanged(mSelectCheckBox.isChecked());
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
