package com.andoffice.layoutbind;

import java.util.ArrayList;
import java.util.List;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.HorizontalScrollView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.andoffice.R;
import com.andframe.activity.framework.AfPageable;
import com.andframe.activity.framework.AfViewable;
import com.andframe.layoutbind.AfLayoutModule;
import com.andframe.layoutbind.framework.IAfLayoutModule;

public class ModuleTabControl extends AfLayoutModule implements
		IAfLayoutModule, OnCheckedChangeListener {
	public interface ITabControlItem {
		String getName();
		void onCheckedChanged(boolean isChecked);
	}

	private RadioGroup mRadioGroup = null;
	private HorizontalScrollView mScrollView = null;

	private Resources mResources = null;
	private RadioButton mLastButton = null;
	private LayoutInflater mInfalter = null;
	private List<ITabControlItem> mltItem = new ArrayList<ITabControlItem>();

	public ModuleTabControl(AfPageable page) {
		super(page);
		// TODO Auto-generated constructor stub
		if(isValid()){
			mResources = page.getContext().getResources();
			mInfalter = LayoutInflater.from(page.getContext());
			mRadioGroup = page.findViewByID(R.id.tabcontrol_radiogroup);
			mScrollView = page.findViewByID(R.id.tabcontrol_hscrollview);
			mRadioGroup.removeAllViews();
		}
	}

	
	@Override
	protected View findLayout(AfViewable view) {
		// TODO Auto-generated method stub
		View layout = view.findViewById(R.id.tabcontrol_hscrollview);
		if(layout != null){
			layout = (View)layout.getParent();
		}
		return layout;
	}


	public void setItems(List<ITabControlItem> ltitem) {
		if (mIsValid) {
			mltItem.clear();
			mltItem.addAll(ltitem);
			mRadioGroup.removeAllViews();
			mLastButton = null;
			int id = R.layout.module_tabcontrolitem;
			for (ITabControlItem item : ltitem) {
				RadioButton button = (RadioButton) mInfalter.inflate(id, null);
				button.setText(item.getName());
				button.setTag(item);
				button.setOnCheckedChangeListener(this);
				mRadioGroup.addView(button);
				if (mLastButton == null) {
					button.setChecked(true);
					mLastButton = button;
				}
			}
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton button, boolean isChecked) {
		// TODO Auto-generated method stub
		if (isChecked) {
			if (mLastButton != null) {
				mLastButton.setBackgroundColor(0);
				ITabControlItem item = (ITabControlItem) mLastButton.getTag();
				item.onCheckedChanged(false);
			}
			mLastButton = (RadioButton) button;
			mLastButton.setBackgroundColor(mResources
					.getColor(R.color.theme_gray_dark));

			mScrollView.smoothScrollTo(
					mLastButton.getLeft() + mLastButton.getWidth() / 2
							- mScrollView.getWidth() / 2, 0);

			ITabControlItem item = (ITabControlItem) mLastButton.getTag();
			item.onCheckedChanged(isChecked);
		}
	}

}
