package com.andframe.layoutbind;

import android.app.Activity;
import android.app.Dialog;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.andframe.activity.framework.AfViewable;
import com.andframe.application.AfApplication;
import com.andframe.application.AfExceptionHandler;
import com.andframe.widget.popupmenu.OnMenuItemClickListener;
import com.andframe.widget.popupmenu.PopupMenu;
import com.google.gson.internal.LinkedHashTreeMap;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public abstract class AfModuleTitlebar extends AfModuleAlpha implements OnClickListener, OnMenuItemClickListener {

	public static final int FUNCTION_NONE = 0;
	public static final int FUNCTION_MENU = 1;
	public static final int FUNCTION_CUSTOM = 2;

	protected View mBtGoBack = null;
	protected TextView mBtRightTxt = null;
	protected ImageView mBtRightImg = null;
	protected TextView mTvTitle = null;
	/**
	 * 功能状态
	 */
	protected int mFunction = FUNCTION_NONE;

	protected Map<Integer, String> mMeuns = new LinkedHashTreeMap<>();
	protected OnMenuItemClickListener mListener = null;
	protected WeakReference<Dialog> mWeakRefDialog = null;
	protected WeakReference<Activity> mWeakRefActivity = null;
	protected OnClickListener mBtCustomClickListener;

	public AfModuleTitlebar(AfViewable view, int id) {
		this(view, FUNCTION_NONE, id);
	}

	public AfModuleTitlebar(AfViewable view, String title, int id) {
		this(view, FUNCTION_NONE, id);
		setTitle(title);
	}
	public AfModuleTitlebar(AfViewable view, int function, int id) {
		super(view, id);
		if (isValid()) {
			initView(view, function);
		}
	}

	private void bindWeakReference(AfViewable view) {
		if (view instanceof Dialog) {
			mWeakRefDialog = new WeakReference<>(((Dialog) view));
		} else if (view instanceof Activity) {
			mWeakRefActivity = new WeakReference<>(((Activity) view));
		} else if (target != null && target.getContext() instanceof Activity) {
			mWeakRefActivity = new WeakReference<>(((Activity) target.getContext()));
		}
	}

	@Override
	protected void onCreated(AfViewable viewable, View view) {
		super.onCreated(viewable, view);
		if (target != null) {
			bindWeakReference(viewable);
			mBtGoBack = viewable.findViewById(getBtGoBackId());
			mBtRightImg = viewable.findViewById(getRightImgId(), ImageView.class);
			mBtRightTxt = viewable.findViewById(getRightTxtId(), TextView.class);
			mTvTitle = viewable.findViewById(getTitleTextId(), TextView.class);
			mMeuns = new HashMap<>();
			mBtRightImg.setOnClickListener(this);
			mBtGoBack.setOnClickListener(this);
			mBtRightTxt.setOnClickListener(this);
			setFunction(FUNCTION_NONE);
		}
	}

	protected void initView(AfViewable view, int function) {
		bindWeakReference(view);
		mBtGoBack = view.findViewById(getBtGoBackId());
		mBtRightImg = view.findViewById(getRightImgId(), ImageView.class);
		mBtRightTxt = view.findViewById(getRightTxtId(), TextView.class);
		mTvTitle = view.findViewById(getTitleTextId(), TextView.class);
		mMeuns = new HashMap<>();
		mBtRightImg.setOnClickListener(this);
		mBtGoBack.setOnClickListener(this);
		mBtRightTxt.setOnClickListener(this);
		setFunction(function);
	}

	/**
	 * 子类获取标题控件ID TextView
	 */
	public abstract int getTitleTextId();

	/**
	 * 子类获取右图片控件ID ImageView
	 */
	public abstract int getRightImgId();

	/**
	 * 子类获取右文本控件ID TextView
	 */
	public abstract int getRightTxtId();

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
		if (v.getId() == getBtGoBackId()) {
			if (mWeakRefDialog != null) {
				Dialog dialog = mWeakRefDialog.get();
				if (dialog != null) {
					dialog.dismiss();
				}
			}
			if (mWeakRefActivity != null) {
				Activity activity = mWeakRefActivity.get();
				if (activity != null) {
					activity.finish();
				}
			}
		} else /*if (v.getId() == getRightImgId())*/ {
			if (mFunction == FUNCTION_MENU) {
				PopupMenu pm = new PopupMenu(v.getContext(), v);
				if (pm.isValid()) {
					for (Map.Entry<Integer, String> entry : mMeuns.entrySet()) {
						pm.getMenu().add(1, entry.getKey(), 0, entry.getValue());
					}
					pm.setOnMenuItemClickListener(this);
					pm.show();
				}
			} else if (mFunction == FUNCTION_CUSTOM && mBtCustomClickListener != null) {
				try {
					mBtCustomClickListener.onClick(v);
				} catch (Throwable e) {
					AfExceptionHandler.handle(e, "AfModuleTitlebarImpl.mBtAdd.onClick");
				}
			}
		}
	}

	public void setFunction(int function) {
		if (function != FUNCTION_CUSTOM) {
			mFunction = function;
			mBtRightImg.setVisibility(function == FUNCTION_NONE ? View.GONE : View.VISIBLE);
		} else if (AfApplication.getApp().isDebug()) {
			AfExceptionHandler.doShowDialog(AfApplication.getApp().getCurActivity(), "设置自定义失败", "请使用setCustomFunction设置自定义功能", "" + this.hashCode());
		}
	}

	public void setCustomFunction(int imageId) {
		mFunction = FUNCTION_CUSTOM;
		mBtRightImg.setImageResource(imageId);
		mBtRightImg.setVisibility(VISIBLE);
		mBtRightTxt.setVisibility(GONE);
	}

	public void setCustomFunction(String text) {
		mFunction = FUNCTION_CUSTOM;
		mBtRightTxt.setText(text);
		mBtRightTxt.setVisibility(VISIBLE);
		mBtRightImg.setVisibility(GONE);
	}

	public void setCustomFunction(int imageId, OnClickListener listener) {
		setCustomFunction(imageId);
		setOnCustomListener(listener);
	}

	public void addMeuns(Map<Integer, String> map) {
		this.mMeuns.putAll(map);
	}

	public void setOnMenuItemListener(OnMenuItemClickListener mListener) {
		this.mListener = mListener;
	}

	public void setOnGoBackListener(final OnClickListener listener) {
		mBtGoBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					listener.onClick(v);
				} catch (Throwable e) {
					AfExceptionHandler.handle(e, "AfModuleTitlebar.mBtGoBack.onClick");
				}
			}
		});
	}

	public void setOnCustomListener(OnClickListener listener) {
		mBtCustomClickListener = listener;
	}

	public void setFunctionOnClickListener(int function, OnClickListener listener) {
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
		this.mMeuns.put(id, text);
	}

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		if (mListener != null) {
			try {
				mListener.onMenuItemClick(item);
			} catch (Throwable e) {
				AfExceptionHandler.handle(e, "AfModuleTitlebar.onMenuItemClick");
			}
		}
		return false;
	}

}
