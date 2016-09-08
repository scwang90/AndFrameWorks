package com.andframe.feature;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.andframe.api.view.Viewer;

/**
 * AfView 框架视图类
 * @author 树朾
 * 实现了 Viewer 接口 优化 findViewById 方法
 */
@SuppressWarnings("unused")
public class AfView extends AfViewQuery<AfView> implements Viewer {

	public AfView(View view) {
		super(view);
	}

	/**
	 * 使AfView 承载的 view 从父视图中脱离出来成为独立的 view 
	 * 	主要用于用于view 的转移
	 * 返回 null 标识 转移失败 否则返回脱离独立的 view
	 */
	public View breakaway() {
		if (mTargetViews != null && mTargetViews.length > 0) {
			ViewParent parent = mTargetViews[0].getParent();
			if (parent instanceof ViewGroup) {
				ViewGroup group = ViewGroup.class.cast(parent);
				group.removeView(mTargetViews[0]);
				return mTargetViews[0];
			}
		}
		return null;
	}

	@SuppressWarnings("unused")
	public AfView getParentView() {
		if (mRootView != null) {
			return new AfView((View)mRootView.getParent());
		}
		return null;
	}

	@Override
	public <T extends View> T findViewById(int id, Class<T> clazz) {
		View view = findViewById(id);
		if (clazz.isInstance(view)) {
			return clazz.cast(view);
		}
		return null;
	}

	@Override
	public View getView() {
		return super.getView();
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends View> T findViewByID(int id) {
		return (T)findViewById(id);
	}

}
