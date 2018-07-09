package com.andframe.impl.viewer;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.andframe.api.viewer.Viewer;

/**
 * AfView 框架视图类
 * @author 树朾
 * 实现了 Viewer 接口 优化 findViewById 方法
 */
public class AfView extends ViewerWrapper implements Viewer {

	private View mRootView;

	public AfView(View view) {
		super(view);
		mRootView = view;
	}

	/**
	 * 使AfView 承载的 view 从父视图中脱离出来成为独立的 view 
	 * 	主要用于用于view 的转移
	 * 返回 null 标识 转移失败 否则返回脱离独立的 view
	 */
	public View breakaway() {
		if (mRootView != null) {
			ViewParent parent = mRootView.getParent();
			if (parent instanceof ViewGroup) {
				((ViewGroup) parent).removeView(mRootView);
				return mRootView;
			}
		}
		return null;
	}

	/**
	 * 替换调 View 中的 id 为 item
	 */
	public boolean replace(int id, View target) {
		return replace(findViewById(id), target);
	}

	/**
	 * 替换调 view 为 item
	 */
	public boolean replace(View view, View target) {
		if (view != null) {
			ViewGroup parent = (ViewGroup)view.getParent();
			if (parent != null) {
				ViewParent viewParent = target.getParent();
				if (viewParent instanceof ViewGroup) {
					((ViewGroup) viewParent).removeView(target);
				}

				int i = parent.indexOfChild(view);
				parent.removeViewAt(i);
				parent.addView(target, i, view.getLayoutParams());
			}
		}
		return false;
	}

	@SuppressWarnings("unused")
	public AfView getParentView() {
		return mRootView != null ? new AfView((View) mRootView.getParent()) : null;
	}

}
