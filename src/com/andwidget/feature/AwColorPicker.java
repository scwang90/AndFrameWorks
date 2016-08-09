package com.andwidget.feature;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;

import com.andframe.activity.framework.AfView;
import com.andframe.activity.framework.AfViewable;
import com.andframe.feature.AfDialogBuilder;
import com.andwidget.R;
import com.andwidget.holocolorpicker.ColorPicker;
import com.andwidget.holocolorpicker.OpacityBar;
import com.andwidget.holocolorpicker.SVBar;
import com.andwidget.holocolorpicker.SaturationBar;
import com.andwidget.holocolorpicker.ValueBar;
/**
 * AwColorPicker 
 * 颜色选择器
 * @author 树朾
 */
public class AwColorPicker {

	private ColorPicker mColorPicker;
	private SVBar mSVBar;
	private OpacityBar mOpacityBar;
	private SaturationBar mSaturationBar;
	private ValueBar mValueBar;
	private View mLayout;

	@SuppressLint("InflateParams")
	public AwColorPicker(Context context) {
		LayoutInflater inflater = LayoutInflater.from(context);
		mLayout = inflater.inflate(R.layout.colorpicker_dialogview, null);
		findLayout(new AfView(mLayout));
	}

	/**
	 * 显示颜色选择对话框
	 * @param title
	 * @param positive
	 * @param lpositive
	 * @param negative
	 * @param lnegative
	 */
	public void show(String title,
			String positive, OnClickListener lpositive,
			String negative,OnClickListener lnegative) {
		AfDialogBuilder dailog = new AfDialogBuilder(mLayout.getContext());
		dailog.doShowViewDialog(android.R.style.Theme_Translucent,0,
				title, mLayout, positive,lpositive, null, null, negative, lnegative);
	}
	
	protected View findLayout(AfViewable iview) {

		if (iview instanceof AfView) {
			AfView view = AfView.class.cast(iview);
			mColorPicker = (ColorPicker) view.findViewById(R.id.colorPicker);
			mSVBar = (SVBar) view.findViewById(R.id.sVBar);
			mOpacityBar = (OpacityBar) view.findViewById(R.id.opacityBar);
			mSaturationBar = (SaturationBar) view.findViewById(R.id.saturationBar);
			mValueBar = (ValueBar) view.findViewById(R.id.valueBar);

			mColorPicker.addSVBar(mSVBar);
			mColorPicker.addOpacityBar(mOpacityBar);
			mColorPicker.addSaturationBar(mSaturationBar);
			mColorPicker.addValueBar(mValueBar);
			return view.getView();
		}
		return null;
	}

	public int getColor() {
		if (mColorPicker != null) {
			return mColorPicker.getColor();
		}
		return 0;
	}

	public void setOldCenterColor(int color) {
		if (mColorPicker != null) {
			mColorPicker.setOldCenterColor(color);
		}
	}

	public View getLayout() {
		return mLayout;
	}
}
