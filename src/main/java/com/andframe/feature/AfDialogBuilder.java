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
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.DialogInterface.OnShowListener;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
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

import com.andframe.$;
import com.andframe.activity.AfActivity;
import com.andframe.api.DialogBuilder;
import com.andframe.exception.AfExceptionHandler;
import com.andframe.listener.SafeListener;
import com.andframe.util.android.AfDensity;
import com.andframe.util.java.AfReflecter;

import java.util.Calendar;
import java.util.Date;
import java.util.Stack;

@SuppressWarnings("unused")
public class AfDialogBuilder implements DialogBuilder {

    //<editor-fold desc="字段定义">
    public CharSequence TXT_NOMORESHOW = "不再提示";

    protected Context mContext;
    protected Dialog mProgress = null;
    protected int mBuildDelayed = 0;
    protected boolean mBuildNative = true;
    //</editor-fold>

    //<editor-fold desc="构造方法">
    public AfDialogBuilder(Context context) {
        mContext = context;
    }
    //</editor-fold>

    //<editor-fold desc="普通按钮对话框">
    /**
     * 显示对话框 并添加默认按钮 "我知道了"
     *
     * @param title   显示标题
     * @param message 显示内容
     */
    @Override
    public Dialog showDialog(CharSequence title, CharSequence message) {
        return showDialog(title, message, "我知道了", null);
    }

    /**
     * 显示对话框 并添加默认按钮 "我知道了"
     *
     * @param title     显示标题
     * @param message   显示内容
     * @param lpositive 点击  "我知道了" 响应事件
     */
    @Override
    public Dialog showDialog(CharSequence title, CharSequence message, OnClickListener lpositive) {
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
    @Override
    public Dialog showDialog(CharSequence title, CharSequence message, CharSequence positive, OnClickListener lpositive) {
        return showDialog(title, message, "", null, positive, lpositive);
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
    @Override
    public Dialog showDialog(CharSequence title, CharSequence message, CharSequence negative, OnClickListener lnegative, CharSequence positive, OnClickListener lpositive) {
        return showDialog(title, message, negative, lnegative, "", null, positive, lpositive);
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
    @Override
    public Dialog showDialog(CharSequence title, CharSequence message, CharSequence negative, OnClickListener lnegative, CharSequence neutral, OnClickListener lneutral, CharSequence positive, OnClickListener lpositive) {
        return showDialog(0, title, message, negative, lnegative, neutral, lneutral, positive, lpositive);
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
    @Override
    public Dialog showDialog(int iconres, CharSequence title, CharSequence message, CharSequence negative, OnClickListener lnegative, CharSequence positive, OnClickListener lpositive) {
        return showDialog(iconres, title, message, negative, lnegative, "", null, positive, lpositive);
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
    @Override
    public Dialog showDialog(int iconres, CharSequence title, CharSequence message, CharSequence negative, OnClickListener lnegative, CharSequence neutral, OnClickListener lneutral, CharSequence positive, OnClickListener lpositive) {
        return showDialog(-1, iconres, title, message, negative, lnegative, neutral, lneutral, positive, lpositive);
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
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public Dialog showDialog(int theme, int iconres,
                             CharSequence title, CharSequence message, CharSequence negative, OnClickListener lnegative, CharSequence neutral, OnClickListener lneutral, CharSequence positive, OnClickListener lpositive) {
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
            builder.setPositiveButton(positive, new SafeListener(lpositive));
        }
        if (negative != null && negative.length() > 0) {
            builder.setNegativeButton(negative, new SafeListener(lnegative));
        }
        if (neutral != null && neutral.length() > 0) {
            builder.setNeutralButton(neutral, new SafeListener(lneutral));
        }
        builder.setCancelable(false);
        builder.create();
        return builder.show();
    }
    //</editor-fold>

    //<editor-fold desc="自定义视图对话框">
    /**
     * 显示视图对话框
     *
     * @param title     显示标题
     * @param view      显示内容
     * @param positive  确认 按钮显示信息
     * @param lpositive 点击  确认 按钮 响应事件
     */
    @Override
    public Dialog showViewDialog(CharSequence title, View view, CharSequence positive, OnClickListener lpositive) {
        return showViewDialog(title, view, "", null, positive, lpositive);
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
    @Override
    public Dialog showViewDialog(CharSequence title, View view, CharSequence negative, OnClickListener lnegative, CharSequence positive, OnClickListener lpositive) {
        return showViewDialog(title, view, negative, lnegative, "", null, positive, lpositive);
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
    @Override
    public Dialog showViewDialog(CharSequence title, View view, CharSequence negative, OnClickListener lnegative, CharSequence neutral, OnClickListener lneutral, CharSequence positive, OnClickListener lpositive) {
        return showViewDialog(0, title, view, negative, lnegative, neutral, lneutral, positive, lpositive);
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
    @Override
    public Dialog showViewDialog(int iconres, CharSequence title, View view, CharSequence negative, OnClickListener lnegative, CharSequence positive, OnClickListener lpositive) {
        return showViewDialog(iconres, title, view, negative, lnegative, "", null, positive, lpositive);
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
    @Override
    public Dialog showViewDialog(int iconres, CharSequence title, View view, CharSequence negative, OnClickListener lnegative, CharSequence neutral, OnClickListener lneutral, CharSequence positive, OnClickListener lpositive) {
        return showViewDialog(-1, iconres, title, view, negative, lnegative, neutral, lneutral, positive, lpositive);
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
    @Override
    @SuppressLint("NewApi")
    public Dialog showViewDialog(int theme,
                                 int iconres, CharSequence title, View view, CharSequence negative, OnClickListener lnegative, CharSequence neutral, OnClickListener lneutral, CharSequence positive, OnClickListener lpositive) {
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
            builder.setPositiveButton(positive, new SafeListener(lpositive));
        }
        if (negative != null && negative.length() > 0) {
            builder.setNegativeButton(negative, new SafeListener(lnegative));
        }
        if (neutral != null && neutral.length() > 0) {
            builder.setNeutralButton(neutral, new SafeListener(lneutral));
        }
        builder.setCancelable(false);
        builder.create();
        return builder.show();
    }
    //</editor-fold>

    //<editor-fold desc="单选对话框">
    /**
     * 显示一个单选对话框 （设置可取消）
     *
     * @param title    对话框标题
     * @param items    选择菜单项
     * @param listener 选择监听器
     * @param cancel   取消选择监听器
     */
    @Override
    public Dialog selectItem(CharSequence title, CharSequence[] items, OnClickListener listener, boolean cancel) {
        return selectItem(title, items, listener, cancel ? new SafeListener() : null);
    }

    /**
     * 显示一个单选对话框
     *
     * @param title    对话框标题
     * @param items    选择菜单项
     * @param listener 选择监听器
     * @param oncancel 取消选择监听器
     */
    @Override
    public Dialog selectItem(CharSequence title, CharSequence[] items, OnClickListener listener, final OnCancelListener oncancel) {
        Builder dialog = new Builder(mContext);
        if (title != null) {
            dialog.setTitle(title);
            if (oncancel != null) {
                dialog.setNegativeButton("取消", new SafeListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        oncancel.onCancel(dialog);
                    }
                });
            }
        }
        if (oncancel != null) {
            dialog.setCancelable(true);
            dialog.setOnCancelListener(new SafeListener(oncancel));
        } else {
            dialog.setCancelable(false);
        }
        dialog.setItems(items, new SafeListener(listener));
        return dialog.show();
    }

    /**
     * 显示一个单选对话框 （默认可取消）
     *
     * @param title    对话框标题
     * @param items    选择菜单项
     * @param listener 选择监听器
     */
    @Override
    public Dialog selectItem(CharSequence title, CharSequence[] items, OnClickListener listener) {
        return selectItem(title, items, listener, true);
    }
    //</editor-fold>

    /**
     * 显示一个多选对话框 （默认可取消）
     *
     * @param title    对话框标题
     * @param items    选择菜单项
     * @param listener 选择监听器
     */
    @Override
    public Dialog multiChoice(CharSequence title, CharSequence[] items, OnMultiChoiceClickListener listener) {
        return multiChoice(title,items,new boolean[items.length],listener);
    }

    /**
     * 显示一个多选对话框 （默认可取消）
     *
     * @param title         对话框标题
     * @param items         选择菜单项
     * @param checkedItems  选择结果
     * @param listener      选择监听器
     */
    @Override
    public Dialog multiChoice(CharSequence title, CharSequence[] items, boolean[] checkedItems, OnMultiChoiceClickListener listener) {
        return multiChoice(title, items, new boolean[items.length], listener, null);
    }

    /**
     * 显示一个多选对话框 （默认可取消）
     *
     * @param title         对话框标题
     * @param items         选择菜单项
     * @param checkedItems  选择结果
     * @param listener      选择监听器
     * @param lpositive     完成选择器
     */
    @Override
    public Dialog multiChoice(CharSequence title, CharSequence[] items, boolean[] checkedItems, OnMultiChoiceClickListener listener, OnClickListener lpositive) {
        Builder dialog = new Builder(mContext);
        if (title != null) {
            dialog.setTitle(title);
            dialog.setNegativeButton("确定", new SafeListener(lpositive));
        }
        dialog.setMultiChoiceItems(items, checkedItems, new SafeListener(listener));
        return dialog.show();
    }

    //<editor-fold desc="输入对话框">
    /**
     * 弹出一个文本输入框
     *
     * @param title    标题
     * @param listener 监听器
     */
    @Override
    public Dialog inputText(CharSequence title, InputTextListener listener) {
        return inputText(title, "", InputType.TYPE_CLASS_TEXT, listener);
    }

    /**
     * 弹出一个文本输入框
     *
     * @param title    标题
     * @param type     android.text.InputType
     * @param listener 监听器
     */
    @Override
    public Dialog inputText(CharSequence title, int type, InputTextListener listener) {
        return inputText(title, "", type, listener);
    }

    /**
     * 弹出一个文本输入框
     *
     * @param title    标题
     * @param defaul   默认值
     * @param listener 监听器
     */
    @Override
    public Dialog inputText(CharSequence title, CharSequence defaul, InputTextListener listener) {
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
    @Override
    public Dialog inputText(CharSequence title, final CharSequence defaul, int type, final InputTextListener listener) {
        final EditText input = new EditText(mContext);
        final int defaultLength = defaul != null ? defaul.length() : 0;
        input.setText(defaul);
        input.clearFocus();
        input.setInputType(type);

        final CharSequence oKey = "确定";
        final CharSequence msgKey = "$inputText$";

        OnClickListener cancelListener = (dialog, which) -> {
            AfSoftInput.hideSoftInput(input);
            if (listener instanceof InputTextCancelable) {
                ((InputTextCancelable) listener).onInputTextCancel(input);
            }
            if (dialog != null) {
                dialog.dismiss();
            }
        };
        final OnClickListener okListener = (dialog, which) -> {
            if (listener.onInputTextConfirm(input, input.getText().toString())) {
                AfSoftInput.hideSoftInput(input);
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        };
        final OnShowListener showListener = dialog -> {
            AfSoftInput.showSoftInput(input);
            if (defaultLength > 3 && defaul.toString().matches("[^.]+\\.[a-zA-Z]\\w{1,3}")) {
                input.setSelection(0, defaul.toString().lastIndexOf('.'));
            } else {
                input.setSelection(0, defaultLength);
            }
        };

        if (mBuildNative) {
            Builder builder = new AlertDialog.Builder(mContext);
            builder.setView(input);
            builder.setCancelable(false);
            builder.setTitle(title);
            builder.setPositiveButton("确定", new SafeListener());
            builder.setNegativeButton("取消", cancelListener);
            final AlertDialog dialog = builder.create();
            dialog.setOnShowListener(showListener);
            dialog.show();
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> new SafeListener(okListener).onClick(dialog, 0));
            return dialog;
        } else {
            final Dialog dialog = showDialog(title, msgKey, oKey, okListener, "取消", cancelListener);
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                FindTextViewWithText builderHelper = FindTextViewWithText.invoke((ViewGroup) dialog.getWindow().getDecorView(), msgKey);
                if (builderHelper != null) {
                    builderHelper.parent.removeViewAt(builderHelper.index);
                    builderHelper.parent.addView(input,builderHelper.index,builderHelper.textView.getLayoutParams());
                    showListener.onShow(dialog);
                    builderHelper = FindTextViewWithText.invoke((ViewGroup) dialog.getWindow().getDecorView(), oKey);
                    if (builderHelper != null) {
                        builderHelper.textView.setOnClickListener(v -> new SafeListener(okListener).onClick(dialog, 0));
                    }
                }
            }, mBuildDelayed);
            return dialog;
        }
    }
    //</editor-fold>

    //<editor-fold desc="多行输入框">
    /**
     * 弹出一个文本输入框
     *
     * @param title    标题
     * @param listener 监听器
     */
    @Override
    public Dialog inputLines(CharSequence title, InputTextListener listener) {
        return inputLines(title, "", InputType.TYPE_CLASS_TEXT, listener);
    }

    /**
     * 弹出一个文本输入框
     *
     * @param title    标题
     * @param type     android.text.InputType
     * @param listener 监听器
     */
    @Override
    public Dialog inputLines(CharSequence title, int type, InputTextListener listener) {
        return inputLines(title, "", type, listener);
    }

    /**
     * 弹出一个文本输入框
     *
     * @param title    标题
     * @param defaul   默认值
     * @param listener 监听器
     */
    @Override
    public Dialog inputLines(CharSequence title, CharSequence defaul, InputTextListener listener) {
        return inputLines(title, defaul, InputType.TYPE_CLASS_TEXT, listener);
    }

    /**
     * 弹出一个文本输入框
     *
     * @param title    标题
     * @param defaul   默认值
     * @param type     android.text.InputType
     * @param listener 监听器
     */
    @Override
    public Dialog inputLines(CharSequence title, final CharSequence defaul, int type, final InputTextListener listener) {
        final EditText input = new EditText(mContext);
        final int defaullength = defaul != null ? defaul.length() : 0;
        input.setText(defaul);
        input.clearFocus();
        input.setInputType(type|InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        input.setGravity(Gravity.TOP);
        input.setSingleLine(false);
        input.setHorizontallyScrolling(false);
        input.setMinHeight(AfDensity.dp2px(100));

        final CharSequence oKey = "确定";
        final CharSequence msgKey = "$inputLines$";

        OnClickListener cancleListener = (dialog, which) -> {
            AfSoftInput.hideSoftInput(input);
            if (listener instanceof InputTextCancelable) {
                ((InputTextCancelable) listener).onInputTextCancel(input);
            }
            if (dialog != null) {
                dialog.dismiss();
            }
        };
        final OnClickListener okListener = (dialog, which) -> {
            if (listener.onInputTextConfirm(input, input.getText().toString())) {
                AfSoftInput.hideSoftInput(input);
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        };
        final OnShowListener showListener = dialog -> {
            AfSoftInput.showSoftInput(input);
            if (defaullength > 3 && defaul.toString().matches("[^.]+\\.[a-zA-Z]\\w{1,3}")) {
                input.setSelection(0, defaul.toString().lastIndexOf('.'));
            } else {
                input.setSelection(0, defaullength);
            }
        };

        if (mBuildNative) {
            Builder builder = new AlertDialog.Builder(mContext);
            builder.setView(input);
            builder.setCancelable(false);
            builder.setTitle(title);
            builder.setPositiveButton("确定", new SafeListener());
            builder.setNegativeButton("取消", cancleListener);
            final AlertDialog dialog = builder.create();
            dialog.setOnShowListener(showListener);
            dialog.show();
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> new SafeListener(okListener).onClick(dialog, 0));
            return dialog;
        } else {
            final Dialog dialog = showDialog(title, msgKey, oKey, okListener, "取消", cancleListener);
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                FindTextViewWithText builderHelper = FindTextViewWithText.invoke((ViewGroup) dialog.getWindow().getDecorView(), msgKey);
                if (builderHelper != null) {
                    builderHelper.parent.removeViewAt(builderHelper.index);
                    builderHelper.parent.addView(input,builderHelper.index,builderHelper.textView.getLayoutParams());
                    showListener.onShow(dialog);
                    builderHelper = FindTextViewWithText.invoke((ViewGroup) dialog.getWindow().getDecorView(), oKey);
                    if (builderHelper != null) {
                        builderHelper.textView.setOnClickListener(v -> new SafeListener(okListener).onClick(dialog, 0));
                    }
                }
            }, mBuildDelayed);
            return dialog;
        }
    }
    //</editor-fold>

    //<editor-fold desc="不再提示对话框">
    /**
     * 显示对话框(不再提示) 并添加默认按钮 "我知道了"
     *
     * @param key       不再显示KEY
     * @param title   显示标题
     * @param message 显示内容
     */
    @Override
    public Dialog showKeyDialog(String key, CharSequence title, CharSequence message) {
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
    @Override
    public Dialog showKeyDialog(String key, CharSequence title, CharSequence message, CharSequence positive, OnClickListener lpositive) {
        return showKeyDialog(key, 0, title, message, "", null, positive, lpositive);
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
    @Override
    public Dialog showKeyDialog(String key, int defclick,
                                CharSequence title, CharSequence message, CharSequence negative, OnClickListener lnegative, CharSequence positive, OnClickListener lpositive) {
        return showKeyDialog(key, defclick, title, message, negative, lnegative, "", null, positive, lpositive);
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
    @Override
    public Dialog showKeyDialog(String key, int defclick,
                                CharSequence title, CharSequence message, CharSequence negative, OnClickListener lnegative, CharSequence neutral, OnClickListener lneutral, CharSequence positive, OnClickListener lpositive) {
        return showKeyDialog(key, defclick, 0, title, message, negative, lnegative, neutral, lneutral, positive, lpositive);
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
    @Override
    public Dialog showKeyDialog(String key, int defclick,
                                int iconres, CharSequence title, CharSequence message, CharSequence negative, OnClickListener lnegative, CharSequence positive, OnClickListener lpositive) {
        return showKeyDialog(key, defclick, iconres, title, message, negative, lnegative, "", null, positive, lpositive);
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
    @Override
    public Dialog showKeyDialog(String key, int defclick,
                                int iconres, CharSequence title, CharSequence message, CharSequence negative, OnClickListener lnegative, CharSequence neutral, OnClickListener lneutral, CharSequence positive, OnClickListener lpositive) {
        return showKeyDialog(key, defclick, -1, iconres, title, message, negative, lnegative, neutral, lneutral, positive, lpositive);
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
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public Dialog showKeyDialog(final String key, final int defclick,
                                int theme, int iconres,
                                CharSequence title, final CharSequence message, CharSequence negative, OnClickListener lnegative, CharSequence neutral, OnClickListener lneutral, CharSequence positive, OnClickListener lpositive) {
        if (!TextUtils.isEmpty(key) && defclick > -1) {
            int click = $.cache(key).getInt(key, -1);
            if (click == defclick) {
                OnClickListener[] clicks = {new SafeListener(lnegative), new SafeListener(lneutral), new SafeListener(lpositive)};
                for (int i = 0; i < clicks.length; i++) {
                    if (i == defclick && clicks[i] != null) {
                        clicks[i].onClick(null, i);
                    }
                }
                return null;
            }
        }
        final Dialog dialog = showDialog(theme, iconres, title, message, negative, lnegative, neutral, lneutral, positive, lpositive);
        if (dialog != null && !TextUtils.isEmpty(key)) {
            final View decor = dialog.getWindow().getDecorView();
            decor.postDelayed(() -> {
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
                    dialog.setOnDismissListener(dialog1 -> {
                        if (finalCbTip.isChecked()) {
                            $.cache(key).put(key, defclick);
                        }
                    });
                }
            }, mBuildDelayed);
        }
        return dialog;
    }
    //</editor-fold>

    //<editor-fold desc="日期时间选择对话框">
    /**
     * 选择日期时间
     * @param listener 监听器
     */
    @Override
    public Dialog selectDateTime(OnDateTimeSetListener listener) {
        return selectDateTime("", new Date(), listener);
    }

    /**
     * 选择日期时间
     * @param title 标题
     * @param listener 监听器
     */
    @Override
    public Dialog selectDateTime(CharSequence title, OnDateTimeSetListener listener) {
        return selectDateTime(title, new Date(), listener);
    }

    /**
     * 选择日期时间
     * @param value 默认时间
     * @param listener 监听器
     */
    @Override
    public Dialog selectDateTime(Date value, OnDateTimeSetListener listener) {
        return selectDateTime("", value, listener);
    }

    /**
     * 选择日期时间
     * @param title 标题
     * @param value 默认时间
     * @param listener 监听器
     */
    @Override
    public Dialog selectDateTime(final CharSequence title, final Date value, final OnDateTimeSetListener listener) {
        final Calendar calender = Calendar.getInstance();
        calender.setTime(value);
        int year = calender.get(Calendar.YEAR);
        final int month = calender.get(Calendar.MONTH);
        final int day = calender.get(Calendar.DAY_OF_MONTH);
        final AlertDialog tDialog = new DatePickerDialog(mContext, new SafeListener((view, year1, month1, day1) -> {
            int hour = calender.get(Calendar.HOUR_OF_DAY);
            int minute = calender.get(Calendar.MINUTE);
            Dialog tDialog1 = new TimePickerDialog(mContext, new SafeListener((OnTimeSetListener)(view1, hour1, minute1) -> listener.onDateTimeSet(year1, month1, day1, hour1, minute1)), hour, minute, true){
                @Override
                public void show() {
                    super.show();
                    if (listener instanceof OnDateTimeSetVerifyListener) {
                        getButton(BUTTON_POSITIVE).setOnClickListener(v -> {
                            try {
                                TimePicker picker = AfReflecter.getMemberByType(this, TimePicker.class);
                                if (picker == null) {
                                    this.dismiss();
                                    super.onClick(this, BUTTON_POSITIVE);
                                } else if (((OnDateTimeSetVerifyListener) listener).onPreTimeSet(picker, picker.getCurrentHour(), picker.getCurrentMinute())) {
                                    this.dismiss();
                                    super.onClick(this, BUTTON_POSITIVE);
                                }
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        });
                    }
                }
            };;
            if (title != null && title.length() > 0) {
                tDialog1.setTitle(title);
            }
            tDialog1.show();
            tDialog1.setCancelable(true);
        }), year, month, day) {
            @Override
            public void show() {
                super.show();
                if (listener instanceof OnDateTimeSetVerifyListener) {
                    getButton(BUTTON_POSITIVE).setOnClickListener(v -> {
                        try {
                            DatePicker picker = AfReflecter.getMemberByType(this, DatePicker.class);
                            if (picker == null) {
                                this.dismiss();
                                super.onClick(this, BUTTON_POSITIVE);
                            } else if (((OnDateTimeSetVerifyListener) listener).onPreDateSet(picker, picker.getYear(), picker.getMonth(), picker.getDayOfMonth())) {
                                this.dismiss();
                                super.onClick(this, BUTTON_POSITIVE);
                            }
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        };
        if (title != null && title.length() > 0) {
            tDialog.setTitle(title);
        }
        tDialog.show();
        tDialog.setCancelable(true);
        return tDialog;
    }
    //</editor-fold>

    //<editor-fold desc="时间选择对话框">
    /**
     * 选择时间
     * @param listener 监听器
     */
    @Override
    public Dialog selectTime(OnTimeSetListener listener) {
        return selectTime("", new Date(), listener);
    }

    /**
     * 选择时间
     * @param title 标题
     * @param listener 监听器
     */
    @Override
    public Dialog selectTime(CharSequence title, OnTimeSetListener listener) {
        return selectTime(title, new Date(), listener);
    }

    /**
     * 选择时间
     * @param value 默认时间
     * @param listener 监听器
     */
    @Override
    public Dialog selectTime(Date value, OnTimeSetListener listener) {
        return selectTime("", value, listener);
    }

    /**
     * 选择时间
     * @param title 标题
     * @param value 默认时间
     * @param listener 监听器
     */
    @Override
    public Dialog selectTime(CharSequence title, Date value, final OnTimeSetListener listener) {
        Calendar calender = Calendar.getInstance();
        calender.setTime(value);
        int hour = calender.get(Calendar.HOUR_OF_DAY);
        int minute = calender.get(Calendar.MINUTE);
        AlertDialog tDialog = new TimePickerDialog(mContext, new SafeListener(listener) , hour, minute, true) {
            @Override
            public void show() {
                super.show();
                if (listener instanceof OnTimeSetVerifyListener) {
                    getButton(BUTTON_POSITIVE).setOnClickListener(v -> {
                        try {
                            TimePicker picker = AfReflecter.getMemberByType(this, TimePicker.class);
                            if (picker == null) {
                                this.dismiss();
                                super.onClick(this, BUTTON_POSITIVE);
                            } else if (((OnTimeSetVerifyListener) listener).onPreTimeSet(this, picker, picker.getCurrentHour(), picker.getCurrentMinute())) {
                                this.dismiss();
                                super.onClick(this, BUTTON_POSITIVE);
                            }
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        };
        if (title != null && title.length() > 0) {
            tDialog.setTitle(title);
        }
        tDialog.show();
        tDialog.setCancelable(true);
        return tDialog;
    }
    //</editor-fold>

    //<editor-fold desc="日期选择对话框">
    /**
     * 选择日期
     * @param listener 监听器
     */
    @Override
    public Dialog selectDate(OnDateSetListener listener) {
        return selectDate("", new Date(), listener);
    }

    /**
     * 选择日期
     * @param title 标题
     * @param listener 监听器
     */
    @Override
    public Dialog selectDate(CharSequence title, OnDateSetListener listener) {
        return selectDate(title, new Date(), listener);
    }

    /**
     * 选择日期
     * @param value 默认时间
     * @param listener 监听器
     */
    @Override
    public Dialog selectDate(Date value, OnDateSetListener listener) {
        return selectDate("", value, listener);
    }

    /**
     * 选择日期
     * @param title 标题
     * @param value 默认时间
     * @param listener 监听器
     */
    @Override
    public Dialog selectDate(CharSequence title, Date value, final OnDateSetListener listener) {
        Calendar calender = Calendar.getInstance();
        calender.setTime(value);
        int year = calender.get(Calendar.YEAR);
        int month = calender.get(Calendar.MONTH);
        int day = calender.get(Calendar.DAY_OF_MONTH);
        AlertDialog tDialog = new DatePickerDialog(mContext, new SafeListener(listener) , year, month, day) {
            @Override
            public void show() {
                super.show();
                if (listener instanceof OnDateSetVerifyListener) {
                    getButton(BUTTON_POSITIVE).setOnClickListener(v -> {
                        try {
                            DatePicker picker = AfReflecter.getMemberByType(this, DatePicker.class);
                            if (picker == null) {
                                this.dismiss();
                                super.onClick(this, BUTTON_POSITIVE);
                            } else if (((OnDateSetVerifyListener) listener).onPreDateSet(picker, picker.getYear(), picker.getMonth(), picker.getDayOfMonth())) {
                                this.dismiss();
                                super.onClick(this, BUTTON_POSITIVE);
                            }
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        };
        if (title != null && title.length() > 0) {
            tDialog.setTitle(title);
        }
        tDialog.show();
        tDialog.setCancelable(true);
        return tDialog;
    }
    //</editor-fold>

    //<editor-fold desc="进度显示对话框">
    /**
     * 显示 进度对话框
     *
     * @param message 消息
     */
    @Override
    public Dialog showProgressDialog(CharSequence message) {
        return showProgressDialog(message, false, 25);
    }

    /**
     * 显示 进度对话框
     *
     * @param message 消息
     * @param cancel  是否可取消
     */
    @Override
    public Dialog showProgressDialog(CharSequence message, boolean cancel) {
        return showProgressDialog(message, cancel, 25);
    }

    /**
     * 显示 进度对话框
     *
     * @param message  消息
     * @param cancel   是否可取消
     * @param textsize 字体大小
     */
    @Override
    public Dialog showProgressDialog(CharSequence message, boolean cancel, int textsize) {
        return showProgressDialog(message, cancel ? new SafeListener() : null, 25);
    }

    /**
     * 显示 进度对话框
     *
     * @param message  消息
     * @param listener 字体大小
     */
    @Override
    public Dialog showProgressDialog(CharSequence message, OnCancelListener listener) {
        return showProgressDialog(message, listener, 25);
    }

    /**
     * 显示 进度对话框
     *
     * @param message  消息
     * @param listener 是否可取消
     * @param textsize 字体大小
     */
    @Override
    public Dialog showProgressDialog(CharSequence message, OnCancelListener listener, int textsize) {
        try {
            if (mProgress != null) {
                hideProgressDialog();
            }
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
    @Override
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
    @Override
    public void setProgressDialogText(CharSequence text) {
        if (mProgress != null) {
            setProgressDialogText(mProgress, text);
        }
    }

    /**
     * 是否正在显示进度对话框
     */
    @Override
    public boolean isProgressDialogShowing() {
        return mProgress != null && mProgress.isShowing();
    }

    //</editor-fold>

    //<editor-fold desc="对话框功能">
    /**
     * 动态改变等待对话框的文字
     *
     * @param dialog 等待对话框
     * @param text   更新的文字
     */
    public static void setProgressDialogText(Dialog dialog, CharSequence text) {
        Window window = dialog.getWindow();
        View view = window.getDecorView();
        setViewFontText(view, text);
    }

    public static void setViewFontText(View view, CharSequence text) {
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

    //</editor-fold>

    //<editor-fold desc="内部类">
    /**
     * 根据 文本信息 匹配在布局中找出 相匹配的 TextView
     */
    protected static class FindTextViewWithText {
        public int index;
        public TextView textView;
        public ViewGroup parent;

        public static FindTextViewWithText invoke(ViewGroup root,CharSequence text) {
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
    //</editor-fold>
}
