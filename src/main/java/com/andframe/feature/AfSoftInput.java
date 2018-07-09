package com.andframe.feature;

import android.app.Activity;
import android.content.Context;
import android.os.ResultReceiver;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

/**
 * 软键盘工具类
 * Created by SCWANG on 2016/6/12.
 */
@SuppressWarnings("unused")
public class AfSoftInput {
    /**
     * 	键盘显示和隐藏监听器
     */
    public interface OnSoftInputToggleListener {
        void onSoftInputShow();
        void onSoftInputHide();
    }

    /**
     * 键盘显示听器
     */
    public static abstract class OnSoftInputShowListener implements OnSoftInputToggleListener {
        @Override
        public void onSoftInputHide() {
        }
    }

    /**
     * 键盘隐藏监听器
     */
    public static abstract class OnSoftInputHideListener implements OnSoftInputToggleListener {
        @Override
        public void onSoftInputShow() {
        }
    }
    /**
     * 	绑定 软键盘的弹出和隐藏 监听器
     * 	页面需要设置 <activity android:windowSoftInputMode="adjustResize" …… />
     * 	@param rootView 页面根视图
     */
    public static void bindSoftKeyboardToggleListener(final View rootView, final OnSoftInputToggleListener toggleListener) {
        if (rootView != null && toggleListener != null) {
            if (rootView.getContext() instanceof Activity) {
                Activity activity = (Activity) rootView.getContext();
            }
            rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                private int lastDiff = -1;
                public View mRootView = rootView;
                @Override
                public void onGlobalLayout() {
                    if(mRootView != null){
                        int diff = mRootView.getRootView().getHeight() - mRootView.getHeight();
                        if(lastDiff > -1){
                            if(lastDiff < diff){
                                toggleListener.onSoftInputShow();
                            }else if(lastDiff > diff){
                                toggleListener.onSoftInputHide();
                            }
                        }
                        lastDiff = diff;
                    }
                }
            });
        }
    }

    /**
     * 隐藏软键盘
     */
    public static void hideSoftInput(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    /**
     * 隐藏软键盘
     */
    public static void hideSoftInput(Context context) {
        if (context instanceof Activity) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            View view = ((Activity) context).getCurrentFocus();
            if (view != null) {
                if (imm != null) {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        }
    }

    public static void toggleSoftInput(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.toggleSoftInput(0,0);
            }
        }
    }

    public static void toggleSoftInput(Context context) {
        if (context != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.toggleSoftInput(0,0);
            }
        }
    }



    /**
     * 显示软键盘
     */
    public static void showSoftInput(View view) {
        showSoftInput(view, null);
    }

    /**
     * 显示软键盘
     */
    public static void showSoftInput(View view, ResultReceiver resultReceiver) {
        if (view != null) {
//            Configuration config = view.getContext().getResources().getConfiguration();
//            if (config.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
                InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                view.setFocusable(true);
                view.setFocusableInTouchMode(true);
                view.requestFocus();
                if (resultReceiver != null) {
                    imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT, resultReceiver);
                } else {
                    imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
                }
            }
//            }
        }
    }
    /**
     * 获取软键盘大打开状态
     * @return true 打开 false 关闭
     */
    public static boolean isSoftInputActive(Context context) {
        InputMethodManager imm;
        String Server = Context.INPUT_METHOD_SERVICE;
        imm = (InputMethodManager) context.getSystemService(Server);
        return imm != null && imm.isActive();
    }

    /**
     * 获取软键盘打开状态
     * @param view 关联view
     * @return true 打开 false 关闭
     */
    public static boolean isSoftInputActive(View view) {
        InputMethodManager imm;
        String Server = Context.INPUT_METHOD_SERVICE;
        imm = (InputMethodManager) view.getContext().getSystemService(Server);
        return imm != null && imm.isActive(view);
    }


}
