package com.andframe.feature;

import android.app.Activity;
import android.content.Context;
import android.os.IBinder;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.andframe.activity.framework.AfPageListener;
/**
 * 软键盘输入类
 * @author SCWANG
 */
public class AfSoftInputer implements OnGlobalLayoutListener {

	private AfPageListener mPageListener;
	private View mRootView;
	private Context mContext;

	public AfSoftInputer(Context context) {
		// TODO Auto-generated constructor stub
		mContext = context;
	}

	public void setBindListener(View view, AfPageListener pageListener) {
		// TODO Auto-generated method stub
		if (view != null && pageListener != null) {
			mRootView = view;
			mPageListener = pageListener;
			view.getViewTreeObserver().addOnGlobalLayoutListener(this);
		}
	}
	/**
	 * 实现 onGlobalLayout 
	 * 	用于计算 软键盘的弹出和隐藏
	 * 	子类在对 onGlobalLayout 重写的时候请调用 
	 * 		super.onGlobalLayout();
	 * 	否则不能对软键盘进行监听
	 */
	private int lastdiff = -1;
	@Override
	public void onGlobalLayout() {
		// TODO Auto-generated method stub
		if(mRootView != null){
			int diff = mRootView.getRootView().getHeight() - mRootView.getHeight();
			if(lastdiff > -1){
				if(lastdiff < diff){
					mPageListener .onSoftInputShown();
				}else if(lastdiff > diff){
					mPageListener .onSoftInputHiden();
				}
			}
			lastdiff = diff;	
		}
//		if (diff > 100) {
//			// 大小超过100时，一般为显示虚拟键盘事件
//			this.onSoftInputShown();
//		} else {
//			// 大小小于100时，为不显示虚拟键盘或虚拟键盘隐藏
//			this.onSoftInputHiden();
//		}

	}
	

	/**
	 * 获取软键盘大打开状态
	 * @return true 打开 false 关闭
	 */
	public boolean getSoftInputStatus() {
		// TODO Auto-generated method stub
		InputMethodManager imm = null;
		String Server = Context.INPUT_METHOD_SERVICE;
		imm = (InputMethodManager)mContext.getSystemService(Server);
		return imm.isActive();
	}

	/**
	 * 获取软键盘大打开状态
	 * @param view 关联view
	 * @return true 打开 false 关闭
	 */
	public boolean getSoftInputStatus(View view) {
		// TODO Auto-generated method stub
		InputMethodManager imm = null;
		String Server = Context.INPUT_METHOD_SERVICE;
		imm = (InputMethodManager)mContext.getSystemService(Server);
		return imm.isActive(view);
	}

	/**
	 * 
	 * @param editview
	 * @param enable
	 */
	public void setSoftInputEnable(EditText editview, boolean enable) {
		// TODO Auto-generated method stub
		InputMethodManager imm = null;
		String Server = Context.INPUT_METHOD_SERVICE;
		imm = (InputMethodManager) mContext.getSystemService(Server);
		if (enable) {
			editview.setFocusable(true);
			editview.setFocusableInTouchMode(true);
			editview.requestFocus();
			imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
		} else {
			IBinder token;
			if(editview != null){
				token = editview.getWindowToken();
			}else if (mContext instanceof Activity) {
				View focus = Activity.class.cast(mContext).getCurrentFocus();
				if(focus!=null){
					token = focus.getWindowToken();
				}else{
					return;
				}
			}else{
				return;
			}
			imm.hideSoftInputFromWindow(token, 0);
		}
	}
}
