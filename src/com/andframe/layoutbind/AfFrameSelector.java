package com.andframe.layoutbind;

import android.view.View;
import android.widget.FrameLayout;

import com.andframe.activity.framework.AfView;
import com.andframe.activity.framework.AfViewable;
import com.andframe.layoutbind.framework.AfViewModule;
import com.andframe.layoutbind.framework.IAfLayoutModule;

import java.util.ArrayList;
import java.util.List;

public class AfFrameSelector extends AfViewModule {

	private FrameLayout mFrameLayout = null;

	public AfFrameSelector(AfViewable view, int id) {
		super(view,id);
		mFrameLayout = view.findViewByID(id);
	}

	public AfFrameSelector(FrameLayout frameLayout) {
		super(new AfView(frameLayout),0);
		target = frameLayout;
		mFrameLayout = frameLayout;
	}

	public FrameLayout getFrameLayout() {
		return mFrameLayout;
	}

	/**
	 * 在 FrameLayout 中选择 view 的布局 隐藏其他的布局
	 */
	public boolean selectFrame(View view) {
		if (isValid()) {
			int count = mFrameLayout.getChildCount();
			List<View> lthide = new ArrayList<View>();
			for (int i = 0; i < count; i++) {
				View tView = mFrameLayout.getChildAt(i);
				if (view == tView) {
					view.setVisibility(View.VISIBLE);
				}else {
					lthide.add(tView);
				}
				//tView.setVisibility(tView == view ? View.VISIBLE : View.GONE);
			}
			if (lthide.size() + 1 == count) {
				for (View hide : lthide) {
					hide.setVisibility(View.GONE);
				}
				return true;
			}
		}
		return false;
	}

	public boolean selectFrame(IAfLayoutModule view) {
		return selectFrame(view.getLayout());
	}

	/**
	 * 在 FrameLayout 中选择 id 的布局 隐藏其他的布局
	 * @param id
	 * @return
	 */
	public boolean selectFrame(int id) {
		if (isValid()) {
			View view = mFrameLayout.findViewById(id);
			if (view != null) {
				return selectFrame(view);
			}
		}
		return false;
	}

}
