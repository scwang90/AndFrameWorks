package com.andframe.module;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.FrameLayout;

import com.andframe.api.viewer.ViewModuler;
import com.andframe.api.viewer.Viewer;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ViewConstructor")
@SuppressWarnings("unused")
public class AfFrameSelector extends AfViewModuler {

	private FrameLayout mFrameLayout = null;

	public AfFrameSelector(Viewer viewer, int id) {
		view = mFrameLayout = viewer.findViewById(id);
	}

	public AfFrameSelector(FrameLayout frameLayout) {
		view = mFrameLayout = frameLayout;
	}

	@Override
	protected void onCreated(Viewer viewable, View view) {
		super.onCreated(viewable, view);
		if (view instanceof FrameLayout) {
			mFrameLayout = ((FrameLayout) view);
		}
	}

	public FrameLayout getFrameLayout() {
		return mFrameLayout;
	}

	/**
	 * 在 FrameLayout 中选择 view 的布局 隐藏其他的布局
	 */
	public boolean selectFrame(View view) {
		if (isValid()) {
			if (view instanceof ViewModuler) {
				view = ((ViewModuler) view).getView();
			}
			int count = mFrameLayout.getChildCount();
			List<View> lthide = new ArrayList<>();
			for (int i = 0; i < count; i++) {
				View tView = mFrameLayout.getChildAt(i);
				if (view == tView) {
					view.setVisibility(View.VISIBLE);
				} else {
					lthide.add(tView);
				}
				//tView.setVisibility(tView == view ? View.VISIBLE : View.GONE);
			}
			if (lthide.size() + 1 == count) {
				for (View hide : lthide) {
					hide.setVisibility(View.INVISIBLE);
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * 在 FrameLayout 中选择 id 的布局 隐藏其他的布局
	 * @param id 子布局ID
	 * @return 是否转换成功
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

	public boolean isCurrent(View view) {
		if (isValid()) {
			if (view instanceof ViewModuler) {
				view = ((ViewModuler) view).getView();
			}
			Boolean find = false;
			int count = mFrameLayout.getChildCount();
			for (int i = 0; i < count; i++) {
				View tView = mFrameLayout.getChildAt(i);
				if (tView.getVisibility() == View.VISIBLE) {
					if (tView != view) {
						return false;
					} else {
						find = true;
					}
				}
			}
			return find;
		}
		return false;
	}
}
