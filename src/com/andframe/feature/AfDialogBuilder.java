package com.andframe.feature;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnShowListener;
import android.os.Build;
import android.text.InputType;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.andframe.activity.AfActivity;
import com.andframe.activity.framework.AfPageable.InputTextCancelable;
import com.andframe.activity.framework.AfPageable.InputTextListener;
import com.andframe.application.AfExceptionHandler;
import com.andframe.caches.AfPrivateCaches;
import com.andframe.util.java.AfReflecter;
import com.andframe.util.java.AfStringUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.Stack;

@SuppressWarnings("unused")
public class AfDialogBuilder {

    public String TXT_NOMORESHOW = "不再提示";

    protected Context mContext;
    protected Dialog mProgress = null;
    protected int mBuildDelayed = 0;
    protected boolean mBuildNative = false;

    public AfDialogBuilder(Context context) {
        mContext = context;
    }

    /**
     * 显示对话框 并添加默认按钮 "我知道了"
     *
     * @param title   显示标题
     * @param message 显示内容
     */
    public Dialog showDialog(String title, String message) {
        return showDialog(title, message, "我知道了", null);
    }

    /**
     * 显示对话框 并添加默认按钮 "我知道了"
     *
     * @param title     显示标题
     * @param message   显示内容
     * @param lpositive 点击  "我知道了" 响应事件
     */
    public Dialog showDialog(String title, String message, OnClickListener lpositive) {
        return showDialog(title, message, "我知道了", lpositive);
    }

    /**
     * 显示对话框
     *
     * @param title     显示标题
     * @param message   显示内容
     * @param positive  确认 按钮显示信息
     * @param lpositive 点击  确认 按钮 响应事件
     */
    public Dialog showDialog(String title, String message, String positive, OnClickListener lpositive) {
        return showDialog(title, message, positive, lpositive, "", null);
    }

    /**
     * 显示对话框
     *
     * @param title     显示标题
     * @param message   显示内容
     * @param positive  确认 按钮显示信息
     * @param lpositive 点击  确认 按钮 响应事件
     * @param negative  按钮显示信息
     * @param lnegative 点击  拒绝 按钮 响应事件
     */
    public Dialog showDialog(String title, String message, String positive, OnClickListener lpositive, String negative, OnClickListener lnegative) {
        return showDialog(title, message, positive, lpositive, negative, lnegative, "", null);
    }

    /**
     * 显示对话框
     *
     * @param title     显示标题
     * @param message   显示内容
     * @param positive  确认 按钮显示信息
     * @param lpositive 点击  确认 按钮 响应事件
     * @param neutral   详细 按钮显示信息
     * @param lneutral  点击  详细 按钮 响应事件
     * @param negative  按钮显示信息
     * @param lnegative 点击  拒绝 按钮 响应事件
     */
    public Dialog showDialog(String title, String message, String positive, OnClickListener lpositive, String negative, OnClickListener lnegative, String neutral, OnClickListener lneutral) {
        return showDialog(0, title, message, positive, lpositive, negative, lnegative, neutral, lneutral);
    }

    /**
     * 显示对话框
     *
     * @param iconres   对话框图标
     * @param title     显示标题
     * @param message   显示内容
     * @param positive  确认 按钮显示信息
     * @param lpositive 点击  确认 按钮 响应事件
     * @param negative  按钮显示信息
     * @param lnegative 点击  拒绝 按钮 响应事件
     */
    public Dialog showDialog(int iconres, String title, String message, String positive, OnClickListener lpositive, String negative, OnClickListener lnegative) {
        return showDialog(iconres, title, message, positive, lpositive, negative, lnegative, "", null);
    }

    /**
     * 显示对话框
     *
     * @param iconres   对话框图标
     * @param title     显示标题
     * @param message   显示内容
     * @param positive  确认 按钮显示信息
     * @param lpositive 点击  确认 按钮 响应事件
     * @param neutral   详细 按钮显示信息
     * @param lneutral  点击  详细 按钮 响应事件
     * @param negative  按钮显示信息
     * @param lnegative 点击  拒绝 按钮 响应事件
     */
    public Dialog showDialog(int iconres, String title, String message, String positive, OnClickListener lpositive, String negative, OnClickListener lnegative, String neutral, OnClickListener lneutral) {
        return showDialog(-1, iconres, title, message, positive, lpositive, negative, lnegative, neutral, lneutral);
    }

    /**
     * 显示视图对话框
     *
     * @param theme     主题
     * @param iconres   对话框图标
     * @param title     显示标题
     * @param message   显示内容
     * @param positive  确认 按钮显示信息
     * @param lpositive 点击  确认 按钮 响应事件
     * @param neutral   详细 按钮显示信息
     * @param lneutral  点击  详细 按钮 响应事件
     * @param negative  按钮显示信息
     * @param lnegative 点击  拒绝 按钮 响应事件
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public Dialog showDialog(int theme, int iconres,
                             String title, String message, String positive, OnClickListener lpositive, String negative, OnClickListener lnegative, String neutral, OnClickListener lneutral) {
        Builder builder = null;
        if (theme > 0) {
            try {
                builder = new Builder(mContext, theme);
            } catch (Throwable ignored) {
            }
        }
        if (builder == null) {
            try {
                builder = new Builder(mContext);
            } catch (Throwable ex) {
                return null;
            }
        }
        builder.setTitle(title);
        builder.setMessage(message);
        if (iconres > 0) {
            builder.setIcon(iconres);
        }
        if (positive != null && positive.length() > 0) {
            builder.setPositiveButton(positive, new SafeOnClickListener(lpositive));
        }
        if (negative != null && negative.length() > 0) {
            builder.setNegativeButton(negative, new SafeOnClickListener(lnegative));
        }
        if (neutral != null && neutral.length() > 0) {
            builder.setNeutralButton(neutral, new SafeOnClickListener(lneutral));
        }
        builder.setCancelable(false);
        builder.create();
        return builder.show();
    }

    /**
     * 显示视图对话框
     *
     * @param title     显示标题
     * @param view      显示内容
     * @param positive  确认 按钮显示信息
     * @param lpositive 点击  确认 按钮 响应事件
     */
    public Dialog showViewDialog(String title, View view, String positive, OnClickListener lpositive) {
        return showViewDialog(title, view, positive, lpositive, "", null);
    }

    /**
     * 显示视图对话框
     *
     * @param title     显示标题
     * @param view      显示内容
     * @param positive  确认 按钮显示信息
     * @param lpositive 点击  确认 按钮 响应事件
     * @param negative  按钮显示信息
     * @param lnegative 点击  拒绝 按钮 响应事件
     */
    public Dialog showViewDialog(String title, View view, String positive, OnClickListener lpositive, String negative, OnClickListener lnegative) {
        return showViewDialog(title, view, positive, lpositive, negative, lnegative, "", null);
    }

    /**
     * 显示视图对话框
     *
     * @param title     显示标题
     * @param view      显示内容
     * @param positive  确认 按钮显示信息
     * @param lpositive 点击  确认 按钮 响应事件
     * @param neutral   详细 按钮显示信息
     * @param lneutral  点击  详细 按钮 响应事件
     * @param negative  按钮显示信息
     * @param lnegative 点击  拒绝 按钮 响应事件
     */
    public Dialog showViewDialog(String title, View view, String positive, OnClickListener lpositive, String negative, OnClickListener lnegative, String neutral, OnClickListener lneutral) {
        return showViewDialog(0, title, view, positive, lpositive, negative, lnegative, neutral, lneutral);
    }

    /**
     * 显示视图对话框
     *
     * @param iconres   对话框图标
     * @param title     显示标题
     * @param view      显示内容
     * @param positive  确认 按钮显示信息
     * @param lpositive 点击  确认 按钮 响应事件
     * @param negative  按钮显示信息
     * @param lnegative 点击  拒绝 按钮 响应事件
     */
    public Dialog showViewDialog(int iconres, String title, View view, String positive, OnClickListener lpositive, String negative, OnClickListener lnegative) {
        return showViewDialog(iconres, title, view, positive, lpositive, negative, lnegative, "", null);
    }

    /**
     * 显示视图对话框
     *
     * @param iconres   对话框图标
     * @param title     显示标题
     * @param view      显示内容
     * @param positive  确认 按钮显示信息
     * @param lpositive 点击  确认 按钮 响应事件
     * @param neutral   详细 按钮显示信息
     * @param lneutral  点击  详细 按钮 响应事件
     * @param negative  按钮显示信息
     * @param lnegative 点击  拒绝 按钮 响应事件
     */
    public Dialog showViewDialog(int iconres, String title, View view, String positive, OnClickListener lpositive, String negative, OnClickListener lnegative, String neutral, OnClickListener lneutral) {
        return showViewDialog(-1, iconres, title, view, positive, lpositive, negative, lnegative, neutral, lneutral);
    }

    /**
     * 显示视图对话框
     *
     * @param theme     主题
     * @param iconres   对话框图标
     * @param title     显示标题
     * @param view      显示内容
     * @param positive  确认 按钮显示信息
     * @param lpositive 点击  确认 按钮 响应事件
     * @param neutral   详细 按钮显示信息
     * @param lneutral  点击  详细 按钮 响应事件
     * @param negative  按钮显示信息
     * @param lnegative 点击  拒绝 按钮 响应事件
     */
    @SuppressLint("NewApi")
    public Dialog showViewDialog(int theme,
                                 int iconres, String title, View view, String positive, OnClickListener lpositive, String negative, OnClickListener lnegative, String neutral, OnClickListener lneutral) {
        Builder builder = null;
        if (theme > 0) {
            try {
                builder = new Builder(mContext, theme);
            } catch (Throwable ignored) {
            }
        }
        if (builder == null) {
            try {
                builder = new Builder(mContext);
            } catch (Throwable ex) {
                return null;
            }
        }
        builder.setTitle(title);
        RelativeLayout.LayoutParams lp;
        lp = new RelativeLayout.LayoutParams(AfActivity.LP_WC, AfActivity.LP_WC);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        RelativeLayout layout = new RelativeLayout(mContext);
        layout.addView(view, lp);
        builder.setView(layout);
        if (iconres > 0) {
            builder.setIcon(iconres);
        }
        if (positive != null && positive.length() > 0) {
            builder.setPositiveButton(positive, new SafeOnClickListener(lpositive));
        }
        if (negative != null && negative.length() > 0) {
            builder.setNegativeButton(negative, new SafeOnClickListener(lnegative));
        }
        if (neutral != null && neutral.length() > 0) {
            builder.setNeutralButton(neutral, new SafeOnClickListener(lneutral));
        }
        builder.setCancelable(false);
        builder.create();
        return builder.show();
    }

    /**
     * 显示一个单选对话框 （设置可取消）
     *
     * @param title    对话框标题
     * @param items    选择菜单项
     * @param listener 选择监听器
     * @param cancel   取消选择监听器
     */
    public Dialog selectItem(String title, String[] items, OnClickListener listener, boolean cancel) {
        return selectItem(title, items, listener, cancel ? new SafeOnCancelListener() : null);
    }

    /**
     * 显示一个单选对话框
     *
     * @param title    对话框标题
     * @param items    选择菜单项
     * @param listener 选择监听器
     * @param oncancel 取消选择监听器
     */
    public Dialog selectItem(String title, String[] items, OnClickListener listener, final OnCancelListener oncancel) {
        Builder dialog = new Builder(mContext);
        if (title != null) {
            dialog.setTitle(title);
            if (oncancel != null) {
                dialog.setNegativeButton("取消", new SafeOnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        oncancel.onCancel(dialog);
                    }
                });
            }
        }
        if (oncancel != null) {
            dialog.setCancelable(true);
            dialog.setOnCancelListener(new SafeOnCancelListener(oncancel));
        } else {
            dialog.setCancelable(false);
        }
        dialog.setItems(items, listener);
        return dialog.show();
    }

    /**
     * 显示一个单选对话框 （默认可取消）
     *
     * @param title    对话框标题
     * @param items    选择菜单项
     * @param listener 选择监听器
     */
    public Dialog selectItem(String title, String[] items, OnClickListener listener) {
        return selectItem(title, items, listener, true);
    }

    /**
     * 弹出一个文本输入框
     *
     * @param title    标题
     * @param listener 监听器
     */
    public Dialog inputText(String title, InputTextListener listener) {
        return inputText(title, "", InputType.TYPE_CLASS_TEXT, listener);
    }

    /**
     * 弹出一个文本输入框
     *
     * @param title    标题
     * @param type     android.text.InputType
     * @param listener 监听器
     */
    public Dialog inputText(String title, int type, InputTextListener listener) {
        return inputText(title, "", type, listener);
    }

    /**
     * 弹出一个文本输入框
     *
     * @param title    标题
     * @param defaul   默认值
     * @param listener 监听器
     */
    public Dialog inputText(String title, String defaul, InputTextListener listener) {
        return inputText(title, defaul, InputType.TYPE_CLASS_TEXT, listener);
    }

    /**
     * 弹出一个文本输入框
     *
     * @param title    标题
     * @param defaul   默认值
     * @param type     android.text.InputType
     * @param listener 监听器
     */
    public Dialog inputText(String title, final String defaul, int type, final InputTextListener listener) {
        final EditText input = new EditText(mContext);
        final int defaullength = defaul != null ? defaul.length() : 0;
        input.setText(defaul);
        input.clearFocus();
        input.setInputType(type);

        final String oKey = "确定";
        final String msgKey = "$inputText$";

        OnClickListener cancleListener = new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AfSoftKeyboard.hideSoftKeyboard(input);
                if (listener instanceof InputTextCancelable) {
                    ((InputTextCancelable) listener).onInputTextCancel(input);
                }
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        };
        final OnClickListener okListener = new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (listener.onInputTextComfirm(input, input.getText().toString())) {
                    AfSoftKeyboard.hideSoftKeyboard(input);
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                }
            }
        };
        final OnShowListener showListener = new OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                AfSoftKeyboard.showSoftkeyboard(input);
                if (defaullength > 3 && defaul.matches("[^.]+\\.[a-zA-Z]\\w{1,3}")) {
                    input.setSelection(0, defaul.lastIndexOf('.'));
                } else {
                    input.setSelection(0, defaullength);
                }
            }
        };

        if (mBuildNative) {
            Builder builder = new AlertDialog.Builder(mContext);
            builder.setView(input);
            builder.setCancelable(false);
            builder.setTitle(title);
            builder.setPositiveButton("确定", new SafeOnClickListener());
            builder.setNegativeButton("取消", cancleListener);
            final AlertDialog dialog = builder.create();
            dialog.setOnShowListener(showListener);
            dialog.show();
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new SafeOnClickListener(okListener).onClick(dialog, 0);
                }
            });
            return dialog;
        } else {
            final Dialog dialog = showDialog(title, msgKey, oKey, okListener, "取消", cancleListener);
            dialog.getWindow().getDecorView().postDelayed(new Runnable() {
                @Override
                public void run() {
                    FindTextViewWithText builderHelper = FindTextViewWithText.invoke((ViewGroup) dialog.getWindow().getDecorView(), msgKey);
                    if (builderHelper != null) {
                        builderHelper.parent.removeViewAt(builderHelper.index);
                        builderHelper.parent.addView(input,builderHelper.index,builderHelper.textView.getLayoutParams());
                        showListener.onShow(dialog);
                        builderHelper = FindTextViewWithText.invoke((ViewGroup) dialog.getWindow().getDecorView(), oKey);
                        if (builderHelper != null) {
                            builderHelper.textView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    new SafeOnClickListener(okListener).onClick(dialog, 0);
                                }
                            });
                        }
                    }
                }
            }, mBuildDelayed);
            return dialog;
        }
    }

    /**
     * 显示对话框(不再提示) 并添加默认按钮 "我知道了"
     *
     * @param key       不再显示KEY
     * @param title   显示标题
     * @param message 显示内容
     */
    public Dialog showKeyDialog(String key, String title, String message) {
        return showKeyDialog(key, title, message, "我知道了", null);
    }

    /**
     * 显示对话框(不再提示)
     *
     * @param key       不再显示KEY
     * @param title     显示标题
     * @param message   显示内容
     * @param positive  确认 按钮显示信息
     * @param lpositive 点击  确认 按钮 响应事件
     */
    public Dialog showKeyDialog(String key, String title, String message, String positive, OnClickListener lpositive) {
        return showKeyDialog(key, 0, title, message, positive, lpositive, "", null);
    }

    /**
     * 显示对话框(不再提示)
     *
     * @param key       不再显示KEY
     * @param defclick  不再显示之后默认执行index
     * @param title     显示标题
     * @param message   显示内容
     * @param positive  确认 按钮显示信息
     * @param lpositive 点击  确认 按钮 响应事件
     * @param negative  按钮显示信息
     * @param lnegative 点击  拒绝 按钮 响应事件
     */
    public Dialog showKeyDialog(String key, int defclick,
                                String title, String message, String positive, OnClickListener lpositive, String negative, OnClickListener lnegative) {
        return showKeyDialog(key, defclick, title, message, positive, lpositive, negative, lnegative, "", null);
    }

    /**
     * 显示对话框(不再提示)
     *
     * @param key       不再显示KEY
     * @param defclick  不再显示之后默认执行index
     * @param title     显示标题
     * @param message   显示内容
     * @param positive  确认 按钮显示信息
     * @param lpositive 点击  确认 按钮 响应事件
     * @param neutral   详细 按钮显示信息
     * @param lneutral  点击  详细 按钮 响应事件
     * @param negative  按钮显示信息
     * @param lnegative 点击  拒绝 按钮 响应事件
     */
    public Dialog showKeyDialog(String key, int defclick,
                                String title, String message, String positive, OnClickListener lpositive, String negative, OnClickListener lnegative, String neutral, OnClickListener lneutral) {
        return showKeyDialog(key, defclick, 0, title, message, positive, lpositive, negative, lnegative, neutral, lneutral);
    }

    /**
     * 显示对话框(不再提示)
     *
     * @param key       不再显示KEY
     * @param defclick  不再显示之后默认执行index
     * @param iconres   对话框图标
     * @param title     显示标题
     * @param message   显示内容
     * @param positive  确认 按钮显示信息
     * @param lpositive 点击  确认 按钮 响应事件
     * @param negative  按钮显示信息
     * @param lnegative 点击  拒绝 按钮 响应事件
     */
    public Dialog showKeyDialog(String key, int defclick,
                                int iconres, String title, String message, String positive, OnClickListener lpositive, String negative, OnClickListener lnegative) {
        return showKeyDialog(key, defclick, iconres, title, message, positive, lpositive, negative, lnegative, "", null);
    }

    /**
     * 显示对话框(不再提示)
     *
     * @param key       不再显示KEY
     * @param defclick  不再显示之后默认执行index
     * @param iconres   对话框图标
     * @param title     显示标题
     * @param message   显示内容
     * @param positive  确认 按钮显示信息
     * @param lpositive 点击  确认 按钮 响应事件
     * @param neutral   详细 按钮显示信息
     * @param lneutral  点击  详细 按钮 响应事件
     * @param negative  按钮显示信息
     * @param lnegative 点击  拒绝 按钮 响应事件
     */
    public Dialog showKeyDialog(String key, int defclick,
                                int iconres, String title, String message, String positive, OnClickListener lpositive, String negative, OnClickListener lnegative, String neutral, OnClickListener lneutral) {
        return showKeyDialog(key, defclick, -1, iconres, title, message, positive, lpositive, negative, lnegative, neutral, lneutral);
    }


    /**
     * 显示视图对话框(不再提示)
     *
     * @param key       不再显示KEY
     * @param defclick  不再显示之后默认执行index
     * @param theme     主题
     * @param iconres   对话框图标
     * @param title     显示标题
     * @param message   显示内容
     * @param positive  确认 按钮显示信息
     * @param lpositive 点击  确认 按钮 响应事件
     * @param neutral   详细 按钮显示信息
     * @param lneutral  点击  详细 按钮 响应事件
     * @param negative  按钮显示信息
     * @param lnegative 点击  拒绝 按钮 响应事件
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public Dialog showKeyDialog(final String key, final int defclick,
                                int theme, int iconres,
                                String title, final String message, String positive, OnClickListener lpositive, String negative, OnClickListener lnegative, String neutral, OnClickListener lneutral) {
        if (AfStringUtil.isNotEmpty(key) && defclick > -1) {
            int click = AfPrivateCaches.getInstance(key).getInt(key, -1);
            if (click == defclick) {
                OnClickListener[] clicks = {new SafeOnClickListener(lpositive), new SafeOnClickListener(lnegative), new SafeOnClickListener(lneutral)};
                for (int i = 0; i < clicks.length; i++) {
                    if (i == defclick && clicks[i] != null) {
                        clicks[i].onClick(null, i);
                    }
                }
                return null;
            }
        }
        final Dialog dialog = showDialog(theme, iconres, title, message, positive, lpositive, negative, lnegative, neutral, lneutral);
        if (dialog != null && AfStringUtil.isNotEmpty(key)) {
            final View decor = dialog.getWindow().getDecorView();
            decor.postDelayed(new Runnable() {
                @Override
                public void run() {
                    CheckBox cbTip = null;
                    FindTextViewWithText builderHelper = FindTextViewWithText.invoke((ViewGroup) decor, message);
                    if (builderHelper != null) {
                        int index = builderHelper.index;
                        ViewGroup parent = builderHelper.parent;
                        TextView textView = builderHelper.textView;
                        parent.removeView(textView);
                        LinearLayout ll = new LinearLayout(mContext);
                        ll.setOrientation(LinearLayout.VERTICAL);
                        LinearLayout.LayoutParams lp;
                        LayoutParams olp = textView.getLayoutParams();
                        if (olp instanceof ViewGroup.MarginLayoutParams) {
                            lp = new LinearLayout.LayoutParams((ViewGroup.MarginLayoutParams)olp);
                        } else {
                            lp = new LinearLayout.LayoutParams(olp);
                        }
                        ll.setPadding(textView.getPaddingLeft(),textView.getPaddingTop(),textView.getPaddingRight(),textView.getPaddingBottom());
                        textView.setPadding(0,0,0,0);
                        ll.addView(textView, lp);
                        cbTip = new CheckBox(mContext);
                        cbTip.setText(TXT_NOMORESHOW);
                        if (olp instanceof ViewGroup.MarginLayoutParams) {
                            lp = new LinearLayout.LayoutParams((ViewGroup.MarginLayoutParams)olp);
                            Integer leftMargin = AfReflecter.getMemberNoException(lp, "leftMargin", int.class);
                            Integer topMargin = AfReflecter.getMemberNoException(lp, "topMargin", int.class);
                            Integer rightMargin = AfReflecter.getMemberNoException(lp, "rightMargin", int.class);
                            Integer bottomMargin = AfReflecter.getMemberNoException(lp, "bottomMargin", int.class);
                            if (leftMargin != null && topMargin != null && rightMargin != null && bottomMargin != null) {
                                lp.setMargins(leftMargin, topMargin + textView.getPaddingTop(), rightMargin, bottomMargin);
                            } else {
                                lp.setMargins(0, textView.getPaddingTop(), 0, 0);
                            }
                        } else {
                            lp = new LinearLayout.LayoutParams(olp);
                            lp.setMargins(0, textView.getPaddingTop(), 0, 0);
                        }
                        ll.addView(cbTip, lp);
                        parent.addView(ll, index, olp);
                    }
                    if (cbTip != null) {
                        final CheckBox finalCbTip = cbTip;
                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                if (finalCbTip.isChecked()) {
                                    AfPrivateCaches.getInstance(key).put(key, defclick);
                                }
                            }
                        });
                    }
                }
            }, mBuildDelayed);
        }
        return dialog;
    }

    /**
     * 选择日期时间
     * @param listener 监听器
     */
    public Dialog selectDateTime(OnDateTimeSetListener listener) {
        return selectDateTime("", new Date(), listener);
    }

    /**
     * 选择日期时间
     * @param title 标题
     * @param listener 监听器
     */
    public Dialog selectDateTime(String title, OnDateTimeSetListener listener) {
        return selectDateTime(title, new Date(), listener);
    }

    /**
     * 选择日期时间
     * @param value 默认时间
     * @param listener 监听器
     */
    public Dialog selectDateTime(Date value, OnDateTimeSetListener listener) {
        return selectDateTime("", value, listener);
    }

    /**
     * 选择日期时间
     * @param title 标题
     * @param value 默认时间
     * @param listener 监听器
     */
    public Dialog selectDateTime(final String title, final Date value, final OnDateTimeSetListener listener) {
        final Calendar calender = Calendar.getInstance();
        calender.setTime(value);
        int year = calender.get(Calendar.YEAR);
        final int month = calender.get(Calendar.MONTH);
        final int day = calender.get(Calendar.DAY_OF_MONTH);
        final AlertDialog tDialog = new DatePickerDialog(mContext, new SafeOnDateSetListener(new OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, final int year, final int month, final int day) {
                int hour = calender.get(Calendar.HOUR_OF_DAY);
                int minute = calender.get(Calendar.MINUTE);
                Dialog tDialog = new TimePickerDialog(mContext, new SafeOnTimeSetListener(new OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hour, int minute) {
                        listener.onDateTimeSet(year, month, day, hour, minute);
                    }
                }), hour, minute, true);
                if (title != null && title.length() > 0) {
                    tDialog.setTitle(title);
                }
                tDialog.show();
                tDialog.setCancelable(true);
            }
        }), year, month, day);
        if (title != null && title.length() > 0) {
            tDialog.setTitle(title);
        }
        tDialog.show();
        tDialog.setCancelable(true);
        return tDialog;
    }

    /**
     * 选择时间
     * @param listener 监听器
     */
    public Dialog selectTime(OnTimeSetListener listener) {
        return selectTime("", new Date(), listener);
    }

    /**
     * 选择时间
     * @param title 标题
     * @param listener 监听器
     */
    public Dialog selectTime(String title, OnTimeSetListener listener) {
        return selectTime(title, new Date(), listener);
    }

    /**
     * 选择时间
     * @param value 默认时间
     * @param listener 监听器
     */
    public Dialog selectTime(Date value, OnTimeSetListener listener) {
        return selectTime("", value, listener);
    }

    /**
     * 选择时间
     * @param title 标题
     * @param value 默认时间
     * @param listener 监听器
     */
    public Dialog selectTime(String title, Date value, final OnTimeSetListener listener) {
        Calendar calender = Calendar.getInstance();
        calender.setTime(value);
        int hour = calender.get(Calendar.HOUR_OF_DAY);
        int minute = calender.get(Calendar.MINUTE);
        AlertDialog tDialog = new TimePickerDialog(mContext, new SafeOnTimeSetListener(listener) , hour, minute, true);
        if (title != null && title.length() > 0) {
            tDialog.setTitle(title);
        }
        tDialog.show();
        tDialog.setCancelable(true);
        return tDialog;
    }

    /**
     * 选择日期
     * @param listener 监听器
     */
    public Dialog selectDate(OnDateSetListener listener) {
        return selectDate("", new Date(), listener);
    }

    /**
     * 选择日期
     * @param title 标题
     * @param listener 监听器
     */
    public Dialog selectDate(String title, OnDateSetListener listener) {
        return selectDate(title, new Date(), listener);
    }

    /**
     * 选择日期
     * @param value 默认时间
     * @param listener 监听器
     */
    public Dialog selectDate(Date value, OnDateSetListener listener) {
        return selectDate("", value, listener);
    }

    /**
     * 选择日期
     * @param title 标题
     * @param value 默认时间
     * @param listener 监听器
     */
    public Dialog selectDate(String title, Date value, final OnDateSetListener listener) {
        Calendar calender = Calendar.getInstance();
        calender.setTime(value);
        int year = calender.get(Calendar.YEAR);
        int month = calender.get(Calendar.MONTH);
        int day = calender.get(Calendar.DAY_OF_MONTH);
        AlertDialog tDialog = new DatePickerDialog(mContext, new SafeOnDateSetListener(listener) , year, month, day);
        if (title != null && title.length() > 0) {
            tDialog.setTitle(title);
        }
        tDialog.show();
        tDialog.setCancelable(true);
        return tDialog;
    }


    /**
     * 显示 进度对话框
     *
     * @param message 消息
     */
    public Dialog showProgressDialog(String message) {
        return showProgressDialog(message, false, 25);
    }

    /**
     * 显示 进度对话框
     *
     * @param message 消息
     * @param cancel  是否可取消
     */
    public Dialog showProgressDialog(String message, boolean cancel) {
        return showProgressDialog(message, cancel, 25);
    }

    /**
     * 显示 进度对话框
     *
     * @param message  消息
     * @param cancel   是否可取消
     * @param textsize 字体大小
     */
    public Dialog showProgressDialog(String message, boolean cancel, int textsize) {
        return showProgressDialog(message, cancel ? new SafeOnCancelListener() : null, 25);
    }

    /**
     * 显示 进度对话框
     *
     * @param message  消息
     * @param listener 字体大小
     */
    public Dialog showProgressDialog(String message, OnCancelListener listener) {
        return showProgressDialog(message, listener, 25);
    }

    /**
     * 显示 进度对话框
     *
     * @param message  消息
     * @param listener 是否可取消
     * @param textsize 字体大小
     */
    public Dialog showProgressDialog(String message, OnCancelListener listener, int textsize) {
        try {
            ProgressDialog progress = new ProgressDialog(mContext);
            progress.setMessage(message);
            if (listener != null) {
                progress.setCancelable(true);
                progress.setOnCancelListener(listener);
            } else {
                progress.setCancelable(false);
            }
            progress.show();
            setDialogFontSize(progress, textsize);
            mProgress = progress;
        } catch (Throwable e) {
            //进过日志验证，这个异常会发送，但是概率非常小，注释掉异常通知
//			AfExceptionHandler.handle(e, "AfDialogBuilder.showProgressDialog");
        }
        return mProgress;
    }

    /**
     * 隐藏 进度对话框
     */
    public void hideProgressDialog() {
        try {
            if (mProgress != null) {
                if (mProgress.isShowing()) {
                    mProgress.dismiss();
                }
                mProgress = null;
            }
        } catch (Throwable e) {
            AfExceptionHandler.handle(e, "AfDialogBuilder.hideProgressDialog");
        }
    }

    /**
     * 动态改变等待对话框的文字
     *
     * @param text   更新的文字
     */
    public void setProgressDialogText(String text) {
        if (mProgress != null) {
            setProgressDialogText(mProgress, text);
        }
    }

    /**
     * 动态改变等待对话框的文字
     *
     * @param dialog 等待对话框
     * @param text   更新的文字
     */
    public void setProgressDialogText(Dialog dialog, String text) {
        Window window = dialog.getWindow();
        View view = window.getDecorView();
        setViewFontText(view, text);
    }

    public static void setViewFontText(View view, String text) {
        if (view instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) view;
            int count = parent.getChildCount();
            for (int i = 0; i < count; i++) {
                setViewFontText(parent.getChildAt(i), text);
            }
        } else if (view instanceof TextView) {
            TextView textview = (TextView) view;
            textview.setText(text);
        }
    }

    public static void setDialogFontSize(Dialog dialog, int size) {
        Window window = dialog.getWindow();
        View view = window.getDecorView();
        setViewFontSize(view, size);
    }

    public static void setViewFontSize(View view, int size) {
        if (view instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) view;
            int count = parent.getChildCount();
            for (int i = 0; i < count; i++) {
                setViewFontSize(parent.getChildAt(i), size);
            }
        } else if (view instanceof TextView) {
            TextView textview = (TextView) view;
            textview.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        }
    }

    /**
     * 安全点击监听器
     */
    public static class SafeOnClickListener implements OnClickListener{

        private OnClickListener listener;

        public SafeOnClickListener() {

        }

        public SafeOnClickListener(OnClickListener listener) {
            this.listener = listener;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            try {
                if (listener != null) {
                    listener.onClick(dialog, which);
                }
            } catch (Throwable e) {
                AfExceptionHandler.handle(e, "AfDialogBuilder.SafeOnClickListener.onClick");
            }
        }
    }

    /**
     * 安全取消监听器
     */
    public static class SafeOnCancelListener implements OnCancelListener{

        private OnCancelListener listener;

        public SafeOnCancelListener() {
        }

        public SafeOnCancelListener(OnCancelListener listener) {
            this.listener = listener;
        }
        @Override
        public void onCancel(DialogInterface dialog) {
            try {
                if (listener != null) {
                    listener.onCancel(dialog);
                }
            } catch (Throwable e) {
                AfExceptionHandler.handle(e, "AfDialogBuilder.SafeOnCancelListener.onCancel");
            }
        }
    }

    /**
     * 安全日期监听器
     */
    public static class SafeOnDateSetListener implements OnDateSetListener {

        private boolean fdealwith = false;
        private OnDateSetListener listener;

        public SafeOnDateSetListener(OnDateSetListener listener) {
            this.listener = listener;
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            if(!fdealwith && listener != null){
                fdealwith = true;
                try {
                    listener.onDateSet(view, year, month, day);
                } catch (Throwable e) {
                    AfExceptionHandler.handle(e, "AfDialogBuilder.SafeOnDateSetListener.onDateSet");
                }
            }
        }
    }

    /**
     * 简单时间监听器
     */
    public static abstract class OnSimpleDateSetListener implements OnDateSetListener{
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1 && view != null) {
                onDateSet(new Date(view.getCalendarView().getDate()), year, month, day);
            } else {
                Calendar calender = Calendar.getInstance();
                calender.setTime(new Date(0));
                calender.set(Calendar.YEAR, year);
                calender.set(Calendar.MONTH, month);
                calender.set(Calendar.DAY_OF_MONTH, day);
                onDateSet(calender.getTime(), year, month, day);
            }
        }
        protected abstract void onDateSet(Date date, int year, int month, int day);
    }

    /**
     * 安全时间监听器
     */
    private class SafeOnTimeSetListener implements OnTimeSetListener {

        private boolean fdealwith = false;
        private OnTimeSetListener listener;

        public SafeOnTimeSetListener(OnTimeSetListener listener) {
            this.listener = listener;
        }

        @Override
        public void onTimeSet(TimePicker view, int hour, int minute) {
            if(!fdealwith && listener != null){
                fdealwith = true;
                try {
                    listener.onTimeSet(view, hour, minute);
                } catch (Throwable e) {
                    AfExceptionHandler.handle(e, "AfDialogBuilder.SafeOnTimeSetListener.onTimeSet");
                }
            }
        }
    }

    /**
     * 简单时间监听器
     */
    public static abstract class OnSimpleTimeSetListener implements OnTimeSetListener{
        @Override
        public void onTimeSet(TimePicker view, int hour, int minute) {
            Calendar calender = Calendar.getInstance();
            calender.setTime(new Date(0));
            calender.set(Calendar.HOUR_OF_DAY, hour);
            calender.set(Calendar.MINUTE, minute);
            onTimeSet(calender.getTime(), hour, minute);
        }
        protected abstract void onTimeSet(Date time, int hour, int minute);
    }

    /**
     * 日期时间监听器
     */
    public interface OnDateTimeSetListener{
        void onDateTimeSet(int year, int month, int day, int hour, int minute);
    }

    /**
     * 简单日期时间监听器
     */
    public static abstract class OnSimpleDateTimeSetListener implements OnDateTimeSetListener{
        @Override
        public void onDateTimeSet(int year, int month, int day, int hour, int minute) {
            Calendar calender = Calendar.getInstance();
            calender.setTime(new Date(0));
            calender.set(Calendar.YEAR, year);
            calender.set(Calendar.MONTH, month);
            calender.set(Calendar.DAY_OF_MONTH, day);
            calender.set(Calendar.HOUR_OF_DAY, hour);
            calender.set(Calendar.MINUTE,minute);
            onDateTimeSet(calender.getTime(), year, month, day, hour, minute);
        }

        protected abstract void onDateTimeSet(Date time, int year, int month, int day, int hour, int minute);
    }

    /**
     * 根据 文本信息 匹配在布局中找出 相匹配的 TextView
     */
    protected static class FindTextViewWithText {
        public int index;
        public TextView textView;
        public ViewGroup parent;

        public static FindTextViewWithText invoke(ViewGroup root,String text) {
            FindTextViewWithText helper = new FindTextViewWithText();
            Stack<ViewGroup> stack = new Stack<>();
            stack.add(root);
            helper.index = -1;
            helper.textView = null;
            helper.parent = null;
            while (helper.textView == null && !stack.empty()) {
                ViewGroup pop = stack.pop();
                int count = pop.getChildCount();
                for (int i = 0; i < count; i++) {
                    View childAt = pop.getChildAt(i);
                    if (childAt instanceof ViewGroup) {
                        stack.push((ViewGroup) childAt);
                    } else if (childAt instanceof TextView) {
                        TextView tv = (TextView) childAt;
                        if (tv.getText().equals(text)) {
                            helper.index = i;
                            helper.textView = tv;
                            helper.parent = pop;
                            break;
                        }
                    }
                }
            }
            if (helper.textView == null) {
                return null;
            }
            return helper;
        }
    }
}
