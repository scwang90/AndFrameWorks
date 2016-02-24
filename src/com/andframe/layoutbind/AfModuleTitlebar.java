package com.andframe.layoutbind;

import android.app.Activity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.andframe.activity.framework.AfPageable;
import com.andframe.activity.framework.AfView;
import com.andframe.application.AfApplication;
import com.andframe.application.AfExceptionHandler;
import com.andframe.layoutbind.framework.AfViewModule;
import com.andframe.widget.popupmenu.OnMenuItemClickListener;
import com.andframe.widget.popupmenu.PopupMenu;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public abstract class AfModuleTitlebar extends AfLayoutAlpha implements View.OnClickListener, OnMenuItemClickListener {

	public static final int FUNCTION_NONE = 0;
	public static final int FUNCTION_MENU = 1;
	public static final int FUNCTION_CUSTOM = 2;

	protected View mBtGoBack = null;
	protected ImageView mBtMenu = null;
	protected TextView mTvTitle = null;
	/**
	 * 功能状态
	 */
	protected int mFunction = FUNCTION_NONE;

	protected Map<String, Integer> mMeuns = new HashMap<>();
	protected OnMenuItemClickListener mListener = null;
	protected WeakReference<Activity> mWeakRefActivity = null;
	protected View.OnClickListener mBtCustomClickListener;

	public AfModuleTitlebar(AfPageable page, int id) {
		this(page, FUNCTION_NONE, id);
	}

	public AfModuleTitlebar(AfPageable page, String title, int id) {
		this(page, FUNCTION_NONE, id);
		setTitle(title);
	}
	public AfModuleTitlebar(AfPageable page, int function, int id) {
		super(page, id);
		if (isValid()) {
			initView(page, function);
		}
	}

	@Override
	protected void onCreated(View target) {
		super.onCreated(target);
		if (target != null) {
			mBtGoBack = target.findViewById(getBtGoBackId());
			mBtMenu = new AfView(target).findViewById(getBtMeunId(), ImageView.class);
			mTvTitle = new AfView(target).findViewById(getTitleTextId(), TextView.class);
			if (target.getContext() instanceof Activity) {
				mWeakRefActivity = new WeakReference<>(((Activity) target.getContext()));
			} else {
				mWeakRefActivity = new WeakReference<>(null);
			}
			mMeuns = new HashMap<>();
			mBtMenu.setOnClickListener(this);
			mBtGoBack.setOnClickListener(this);
			setFunction(FUNCTION_NONE);
		}
	}

	protected void initView(AfPageable page, int function) {
		mBtGoBack = page.findViewById(getBtGoBackId());
		mBtMenu = page.findViewById(getBtMeunId(), ImageView.class);
		mTvTitle = page.findViewById(getTitleTextId(), TextView.class);
		mWeakRefActivity = new WeakReference<>(page.getActivity());
		mMeuns = new HashMap<>();
		mBtMenu.setOnClickListener(this);
		mBtGoBack.setOnClickListener(this);
		setFunction(function);
	}

	/**
	 * 子类获取标题控件ID TextView
	 */
	public abstract int getTitleTextId();

	/**
	 * 子类获取菜单控件ID ImageView
	 */
	public abstract int getBtMeunId();

	/**
	 * 子类获取标题控件ID
	 */
	public abstract int getBtGoBackId();

	public void hideLeftButton() {
		if (mBtGoBack != null) {
			mBtGoBack.setVisibility(GONE);
		}
	}

	@Override
	public void onClick(View v) {
		if (mWeakRefActivity != null) {
			if (v.getId() == getBtGoBackId()) {
				Activity activity = mWeakRefActivity.get();
				if (activity != null) {
					activity.finish();
				}
			} else if (v.getId() == getBtMeunId()) {
				if (mFunction == FUNCTION_MENU) {
					PopupMenu pm = new PopupMenu(v.getContext(), v);
					if (pm.isValid()) {
						for (Map.Entry<String, Integer> entry : mMeuns.entrySet()) {
							pm.getMenu().add(1, entry.getValue(), 0, entry.getKey());
						}
						pm.setOnMenuItemClickListener(this);
						pm.show();
					}
				} else if (mFunction == FUNCTION_CUSTOM && mBtCustomClickListener != null) {
					try {
						mBtCustomClickListener.onClick(v);
					} catch (Throwable e) {
						AfExceptionHandler.handler(e, "AfModuleTitlebarImpl.mBtAdd.onClick");
					}
				}
			}
		}
	}

	public void setFunction(int function) {
		if (function != FUNCTION_CUSTOM) {
			mFunction = function;
			mBtMenu.setVisibility(function == FUNCTION_NONE ? View.GONE : View.VISIBLE);
		} else if (AfApplication.getApp().isDebug()) {
			AfExceptionHandler.doShowDialog(AfApplication.getApp().getCurActivity(), "设置自定义失败", "请使用setCustomFunction设置自定义功能", "" + this.hashCode());
		}
	}

	public void setCustomFunction(int imageId) {
		mFunction = FUNCTION_CUSTOM;
		mBtMenu.setImageResource(imageId);
	}

	public void setCustomFunction(int imageId, View.OnClickListener listener) {
		setCustomFunction(imageId);
		setOnCustomListener(listener);
	}

	public void addMeuns(Map<String, Integer> map) {
		this.mMeuns.putAll(map);
	}

	public void setMenuItemListener(OnMenuItemClickListener mListener) {
		this.mListener = mListener;
	}

	public void setOnGoBackListener(final View.OnClickListener listener) {
		mBtGoBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					listener.onClick(v);
				} catch (Throwable e) {
					AfExceptionHandler.handler(e, "AfModuleTitlebar.mBtGoBack.onClick");
				}
			}
		});
	}

	public void setOnCustomListener(View.OnClickListener listener) {
		mBtCustomClickListener = listener;
	}

	public void setFunctionOnClickListener(int function, View.OnClickListener listener) {
		switch (function) {
			case FUNCTION_CUSTOM:
				mBtCustomClickListener = listener;
				break;
		}
	}

	public void setTitle(String description) {
		mTvTitle.setText(description);
	}

	public void setTitle(int id) {
		mTvTitle.setText(id);
	}

	public String getTitle() {
		return mTvTitle.getText().toString();
	}

	public void putMenu(String text, int id) {
		this.mMeuns.put(text, id);
	}

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		if (mListener != null) {
			try {
				mListener.onMenuItemClick(item);
			} catch (Throwable e) {
				AfExceptionHandler.handler(e, "AfModuleTitlebar.onMenuItemClick");
			}
		}
		return false;
	}
}
