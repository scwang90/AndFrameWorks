package com.andframe.feature;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.ResultReceiver;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

/**
 * 软键盘工具类
 * Created by SCWANG on 2016/6/12.
 */
@SuppressWarnings("unused")
public class AfSoftKeyboard {
    /**
     * 	键盘显示和隐藏监听器
     */
    public interface OnSoftKeyboardToggleListener {
        void onSoftKeyboardShow();
        void onSoftKeyboardHide();
    }

    /**
     * 键盘显示听器
     */
    public static abstract class OnSoftKeyboardShowListener implements OnSoftKeyboardToggleListener {
        @Override
        public void onSoftKeyboardHide() {
        }
    }

    /**
     * 键盘隐藏监听器
     */
    public static abstract class OnSoftKeyboardHideListener implements OnSoftKeyboardToggleListener {
        @Override
        public void onSoftKeyboardShow() {
        }
    }
    /**
     * 	绑定 软键盘的弹出和隐藏 监听器
     * 	页面需要设置 <activity android:windowSoftInputMode="adjustResize" …… />
     * 	@param rootView 页面根视图
     */
    public void bindSoftKeyboardToggleListener(final View rootView, final OnSoftKeyboardToggleListener toggleListener) {
        if (rootView != null && toggleListener != null) {
            if (rootView.getContext() instanceof Activity) {
                Activity activity = (Activity) rootView.getContext();
            }
            rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                private int lastdiff = -1;
                public View mRootView = rootView;
                @Override
                public void onGlobalLayout() {
                    if(mRootView != null){
                        int diff = mRootView.getRootView().getHeight() - mRootView.getHeight();
                        if(lastdiff > -1){
                            if(lastdiff < diff){
                                toggleListener.onSoftKeyboardShow();
                            }else if(lastdiff > diff){
                                toggleListener.onSoftKeyboardHide();
                            }
                        }
                        lastdiff = diff;
                    }
                }
            });
        }
    }

    /**
     * 隐藏软键盘
     */
    public static void hideSoftKeyboard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 显示软键盘
     */
    public static void showSoftkeyboard(View view) {
        showSoftkeyboard(view, null);
    }

    /**
     * 显示软键盘
     */
    public static void showSoftkeyboard(View view, ResultReceiver resultReceiver) {
        if (view != null) {
            Configuration config = view.getContext().getResources().getConfiguration();
            if (config.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
                InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

                if (resultReceiver != null) {
                    imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT, resultReceiver);
                } else {
                    imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        }
    }
    /**
     * 获取软键盘大打开状态
     * @return true 打开 false 关闭
     */
    public static boolean isSoftkeyboardActive(Context context) {
        InputMethodManager imm;
        String Server = Context.INPUT_METHOD_SERVICE;
        imm = (InputMethodManager)context.getSystemService(Server);
        return imm.isActive();
    }

    /**
     * 获取软键盘大打开状态
     * @param view 关联view
     * @return true 打开 false 关闭
     */
    public static boolean isSoftkeyboardActive(View view) {
        InputMethodManager imm;
        String Server = Context.INPUT_METHOD_SERVICE;
        imm = (InputMethodManager)view.getContext().getSystemService(Server);
        return imm.isActive(view);
    }


}
