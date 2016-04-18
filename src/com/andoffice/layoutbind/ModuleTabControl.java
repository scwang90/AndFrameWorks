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

import com.andframe.layoutbind.framework.AfViewModule;
import com.andoffice.R;
import com.andframe.activity.framework.AfPageable;
import com.andframe.activity.framework.AfViewable;

public class ModuleTabControl extends AfViewModule implements OnCheckedChangeListener {
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
		super(page,R.id.tabcontrol_lyt);
		if(isValid()){
			mResources = page.getContext().getResources();
			mInfalter = LayoutInflater.from(page.getContext());
			mRadioGroup = page.findViewByID(R.id.tabcontrol_radiogroup);
			mScrollView = page.findViewByID(R.id.tabcontrol_hscrollview);
			mRadioGroup.removeAllViews();
		}
	}

	public void setItems(List<ITabControlItem> ltitem) {
		if (isValid()) {
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
