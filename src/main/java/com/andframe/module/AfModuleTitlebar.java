package com.andframe.module;

import android.app.Activity;
import android.app.Dialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.PopupMenu.OnMenuItemClickListener;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.andframe.api.Viewer;
import com.andframe.application.AfApp;
import com.andframe.exception.AfExceptionHandler;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings("unused")
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

	protected Map<Integer, String> mMeuns = new LinkedHashMap<>();
	protected OnMenuItemClickListener mListener = null;
	protected WeakReference<Dialog> mWeakRefDialog = null;
	protected WeakReference<Activity> mWeakRefActivity = null;
	protected OnClickListener mBtCustomClickListener;

	protected AfModuleTitlebar(){

	}


	protected AfModuleTitlebar(Viewer view) {
		super(view);
	}

	public AfModuleTitlebar(Viewer view, int id) {
		this(view, FUNCTION_NONE, id);
	}

	public AfModuleTitlebar(Viewer view, String title, int id) {
		this(view, FUNCTION_NONE, id);
		setTitle(title);
	}
	public AfModuleTitlebar(Viewer view, int function, int id) {
		super(view, id);
		mFunction = function;
		initializeComponent(view);
	}

	private void bindWeakReference(Viewer view) {
		if (view instanceof Dialog) {
			mWeakRefDialog = new WeakReference<>(((Dialog) view));
		} else if (view instanceof Activity) {
			mWeakRefActivity = new WeakReference<>(((Activity) view));
		} else if (wrapped != null && wrapped.getContext() instanceof Activity) {
			mWeakRefActivity = new WeakReference<>(((Activity) wrapped.getContext()));
		}
	}

	@Override
	protected void onCreated(Viewer viewable, View view) {
		super.onCreated(viewable, view);
		initView(viewable, mFunction);
	}

	protected void initView(Viewer view, int function) {
		bindWeakReference(view);
		mBtGoBack = view.findViewById(getBtGoBackId());
		mBtRightImg = view.findViewById(getRightImgId(), ImageView.class);
		mBtRightTxt = view.findViewById(getRightTxtId(), TextView.class);
		mTvTitle = view.findViewById(getTitleTextId(), TextView.class);
		mMeuns = new HashMap<>();
		$(mBtGoBack).clicked(this);
		$(mBtRightImg).clicked(this);
		$(mBtRightTxt).clicked(this);
//		mBtRightImg.setOnClickListener(this);
//		mBtGoBack.setOnClickListener(this);
//		mBtRightTxt.setOnClickListener(this);
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
				for (Map.Entry<Integer, String> entry : mMeuns.entrySet()) {
					pm.getMenu().add(1, entry.getKey(), 0, entry.getValue());
				}
				pm.setOnMenuItemClickListener(this);
				pm.show();
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
			$(mBtRightImg).visibility(function == FUNCTION_NONE ? View.GONE : View.VISIBLE);
		} else if (AfApp.get().isDebug()) {
			AfExceptionHandler.doShowDialog(AfApp.get().getCurActivity(), "设置自定义失败", "请使用setCustomFunction设置自定义功能", "" + this.hashCode());
		}
	}

	public void setCustomFunction(int imageId) {
		mFunction = FUNCTION_CUSTOM;
		mBtRightImg.setImageResource(imageId);
		mBtRightImg.setVisibility(VISIBLE);
		$(mBtRightTxt).gone();
	}

	public void setCustomFunction(String text) {
		mFunction = FUNCTION_CUSTOM;
		mBtRightTxt.setText(text);
		mBtRightTxt.setVisibility(VISIBLE);
		$(mBtRightImg).gone();
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
		mBtGoBack.setOnClickListener(v -> {
            try {
                listener.onClick(v);
            } catch (Throwable e) {
                AfExceptionHandler.handle(e, "AfModuleTitlebar.mBtGoBack.onClick");
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
