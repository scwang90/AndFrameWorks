package com.andframe.layoutbind;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.widget.FrameLayout;

import com.andframe.activity.framework.AfView;
import com.andframe.activity.framework.AfViewable;
import com.andframe.layoutbind.framework.IAfLayoutModule;

public class AfFrameSelector extends AfLayoutModule {

	private FrameLayout mFrameLayout = null;

	public AfFrameSelector(AfViewable view, int id) {
		super(view);
		// TODO Auto-generated constructor stub
		mFrameLayout = view.findFrameLayoutById(id);
		mLayout = findLayout(view);
		mIsValid = mLayout != null;
	}

	public AfFrameSelector(FrameLayout frameLayout) {
		super(new AfView(frameLayout));
		// TODO Auto-generated constructor stub
		mFrameLayout = frameLayout;
		mLayout = findLayout(new AfView(frameLayout));
		mIsValid = mLayout != null;
	}

	@Override
	protected View findLayout(AfViewable view) {
		// TODO Auto-generated method stub
		return mFrameLayout;
	}
	/**
	 * 在 FrameLayout 中选择 view 的布局 隐藏其他的布局
	 * 
	 * @param id
	 * @return
	 */
	public boolean SelectFrame(View view) {
		if (mIsValid) {
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

	public boolean SelectFrame(IAfLayoutModule view) {
		return SelectFrame(view.getLayout());
	}

	/**
	 * 在 FrameLayout 中选择 id 的布局 隐藏其他的布局
	 * 
	 * @param id
	 * @return
	 */
	public boolean SelectFrame(int id) {
		if (mIsValid) {
			View view = mFrameLayout.findViewById(id);
			if (view != null) {
				return SelectFrame(view);
			}
		}
		return false;
	}

}
