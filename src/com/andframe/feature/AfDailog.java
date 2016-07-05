package com.andframe.feature;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
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
import com.andframe.util.java.AfStringUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.Stack;

public class AfDailog {

    private Context mContext;

    public AfDailog(Context context) {
        mContext = context;
    }

    /**
     * 显示对话框 并添加默认按钮 "我知道了"
     *
     * @param title   显示标题
     * @param message 显示内容
     */
    public AlertDialog doShowDialog(String title, String message) {
        return doShowDialog(0, title, message, "我知道了", null, "", null);
    }

    /**
     * 显示对话框 并添加默认按钮 "我知道了"
     *
     * @param title     显示标题
     * @param message   显示内容
     * @param lpositive 点击  "我知道了" 响应事件
     */
    public AlertDialog doShowDialog(String title, String message, OnClickListener lpositive) {
        return doShowDialog(0, title, message, "我知道了", lpositive, "", null);
    }

    /**
     * 显示对话框
     *
     * @param title     显示标题
     * @param message   显示内容
     * @param positive  确认 按钮显示信息
     * @param lpositive 点击  确认 按钮 响应事件
     */
    public AlertDialog doShowDialog(String title, String message, String positive, OnClickListener lpositive) {
        return doShowDialog(0, title, message, positive, lpositive, "", null);
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
    public AlertDialog doShowDialog(String title, String message,
                             String positive, OnClickListener lpositive, String negative,
                             OnClickListener lnegative) {
        return doShowDialog(0, title, message, positive, lpositive, negative, lnegative);
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
    public AlertDialog doShowDialog(String title, String message,
                             String positive, OnClickListener lpositive,
                             String neutral, OnClickListener lneutral,
                             String negative, OnClickListener lnegative) {
        return doShowDialog(0, title, message, positive, lpositive, neutral, lneutral, negative, lnegative);
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
    public AlertDialog doShowDialog(int iconres, String title, String message,
                             String positive, OnClickListener lpositive, String negative,
                             OnClickListener lnegative) {
        return doShowDialog(iconres, title, message, positive, lpositive, "", null, negative, lnegative);
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
    public AlertDialog doShowDialog(int iconres, String title, String message,
                             String positive, OnClickListener lpositive,
                             String neutral, OnClickListener lneutral,
                             String negative, OnClickListener lnegative) {
        return doShowDialog(-1, iconres, title, message, positive, lpositive, neutral, lneutral, negative, lnegative);
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
    @SuppressLint("NewApi")
    public AlertDialog doShowDialog(int theme, int iconres,
                             String title, String message,
                             String positive, OnClickListener lpositive,
                             String neutral, OnClickListener lneutral,
                             String negative, OnClickListener lnegative) {
        return doShowDialog(null, 0, theme, iconres, title, message, positive, lpositive, neutral, lneutral, negative, lnegative);
    }

    /**
     * 显示视图对话框
     *
     * @param title     显示标题
     * @param view      显示内容
     * @param positive  确认 按钮显示信息
     * @param lpositive 点击  确认 按钮 响应事件
     */
    public AlertDialog doShowViewDialog(String title, View view, String positive,
                                 OnClickListener lpositive) {
        return doShowViewDialog(title, view, positive, lpositive, "", null);
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
    public AlertDialog doShowViewDialog(String title, View view, String positive,
                                 OnClickListener lpositive, String negative,
                                 OnClickListener lnegative) {
        return doShowViewDialog(0, title, view, positive, lpositive, negative, lnegative);
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
    public AlertDialog doShowViewDialog(String title, View view,
                                 String positive, OnClickListener lpositive,
                                 String neutral, OnClickListener lneutral,
                                 String negative, OnClickListener lnegative) {
        return doShowViewDialog(0, title, view, positive, lpositive, neutral, lneutral, negative, lnegative);
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
    public AlertDialog doShowViewDialog(int iconres, String title, View view,
                                 String positive, OnClickListener lpositive,
                                 String negative, OnClickListener lnegative) {
        return doShowViewDialog(iconres, title, view, positive, lpositive, "", null, negative, lnegative);
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
    public AlertDialog doShowViewDialog(int iconres, String title, View view,
                                 String positive, OnClickListener lpositive,
                                 String neutral, OnClickListener lneutral,
                                 String negative, OnClickListener lnegative) {
        return doShowViewDialog(-1, iconres, title, view, positive, lpositive, neutral, lneutral, negative, lnegative);
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
    public AlertDialog doShowViewDialog(int theme,
                                 int iconres, String title, View view,
                                 String positive, OnClickListener lpositive,
                                 String neutral, OnClickListener lneutral,
                                 String negative, OnClickListener lnegative) {
        Builder builder = null;
        if (theme > 0) {
            try {
                builder = new Builder(mContext, theme);
            } catch (Throwable e) {
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
            builder.setPositiveButton(positive, lpositive);
        }
        if (neutral != null && neutral.length() > 0) {
            builder.setNeutralButton(neutral, lneutral);
        }
        if (negative != null && negative.length() > 0) {
            builder.setNegativeButton(negative, lnegative);
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
    public AlertDialog doSelectItem(String title, String[] items, OnClickListener listener,
                             boolean cancel) {
        Builder dialog = new Builder(mContext);
        dialog.setItems(items, listener);
        if (title != null) {
            dialog.setTitle(title);
            dialog.setCancelable(false);
            if (cancel) {
                dialog.setNegativeButton("取消", null);
            }
        } else {
            dialog.setCancelable(cancel);
        }
        return dialog.show();
    }

    /**
     * 显示一个单选对话框
     *
     * @param title    对话框标题
     * @param items    选择菜单项
     * @param listener 选择监听器
     * @param oncancel 取消选择监听器
     */
    public AlertDialog doSelectItem(String title, String[] items, OnClickListener listener,
                             final OnClickListener oncancel) {
        Builder dialog = new Builder(mContext);
        if (title != null) {
            dialog.setTitle(title);
            dialog.setCancelable(false);
            dialog.setNegativeButton("取消", oncancel);
        } else if (oncancel != null) {
            dialog.setCancelable(true);
            dialog.setOnCancelListener(new OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    oncancel.onClick(dialog, 0);
                }
            });
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
    public AlertDialog doSelectItem(String title, String[] items, OnClickListener listener) {
        return doSelectItem(title, items, listener, null);
    }

    /**
     * 弹出一个文本输入框
     *
     * @param title    标题
     * @param listener 监听器
     */
    public AlertDialog doInputText(String title, InputTextListener listener) {
        return doInputText(title, "", InputType.TYPE_CLASS_TEXT, listener);
    }

    /**
     * 弹出一个文本输入框
     *
     * @param title    标题
     * @param type     android.text.InputType
     * @param listener 监听器
     */
    public AlertDialog doInputText(String title, int type, InputTextListener listener) {
        return doInputText(title, "", type, listener);
    }

    /**
     * 弹出一个文本输入框
     *
     * @param title    标题
     * @param defaul   默认值
     * @param type     android.text.InputType
     * @param listener 监听器
     */
    public AlertDialog doInputText(String title, final String defaul, int type, InputTextListener listener) {
        final EditText input = new EditText(mContext);
        final int defaullength = defaul != null ? defaul.length() : 0;
        final InputTextListener flistener = listener;
        input.setText(defaul);
        input.clearFocus();
        input.setInputType(type);
        Builder builder = new AlertDialog.Builder(mContext);
        builder.setView(input);
        builder.setCancelable(false);
        builder.setTitle(title);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new AfSoftInputer(mContext).setSoftInputEnable(input, false);
                dialog.dismiss();
                flistener.onInputTextComfirm(input);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new AfSoftInputer(mContext).setSoftInputEnable(input, false);
                dialog.dismiss();
                if (flistener instanceof InputTextCancelable) {
                    InputTextCancelable cancel = (InputTextCancelable) flistener;
                    cancel.onInputTextCancel(input);
                }
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            public void onShow(DialogInterface dialog) {
                new AfSoftInputer(mContext).setSoftInputEnable(input, true);
                if (defaullength > 3 && defaul.matches("[^.]+\\.[a-zA-Z]\\w{1,3}")) {
                    input.setSelection(0, defaul.lastIndexOf('.'));
                } else {
                    input.setSelection(0, defaullength);
                }
            }
        });
        dialog.show();
        return dialog;
    }

    /**
     * 显示对话框 并添加默认按钮 "我知道了"
     *
     * @param key       不再显示KEY
     * @param title   显示标题
     * @param message 显示内容
     */
    public AlertDialog doShowDialog(String key, String title, String message) {
        return doShowDialog(key, 0, 0, title, message, "我知道了", null, "", null);
    }

    /**
     * 显示对话框
     *
     * @param key       不再显示KEY
     * @param title     显示标题
     * @param message   显示内容
     * @param positive  确认 按钮显示信息
     * @param lpositive 点击  确认 按钮 响应事件
     */
    public AlertDialog doShowDialog(String key, String title, String message, String positive, OnClickListener lpositive) {
        return doShowDialog(key, 0, 0, title, message, positive, lpositive, "", null);
    }

    /**
     * 显示对话框
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
    public AlertDialog doShowDialog(String key, int defclick,
                             String title, String message,
                             String positive, OnClickListener lpositive,
                             String negative, OnClickListener lnegative) {
        return doShowDialog(key, defclick, 0, title, message, positive, lpositive, negative, lnegative);
    }

    /**
     * 显示对话框
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
    public AlertDialog doShowDialog(String key, int defclick,
                             String title, String message,
                             String positive, OnClickListener lpositive,
                             String neutral, OnClickListener lneutral,
                             String negative, OnClickListener lnegative) {
        return doShowDialog(key, defclick, 0, title, message, positive, lpositive, neutral, lneutral, negative, lnegative);
    }

    /**
     * 显示对话框
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
    public AlertDialog doShowDialog(String key, int defclick,
                             int iconres, String title, String message,
                             String positive, OnClickListener lpositive,
                             String negative, OnClickListener lnegative) {
        if (defclick == 1){
            defclick = 2;
        }
        return doShowDialog(key, defclick, iconres, title, message, positive, lpositive, "", null, negative, lnegative);
    }

    /**
     * 显示对话框
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
    public AlertDialog doShowDialog(String key, int defclick,
                             int iconres, String title, String message,
                             String positive, OnClickListener lpositive,
                             String neutral, OnClickListener lneutral,
                             String negative, OnClickListener lnegative) {
        return doShowDialog(key, defclick, -1, iconres, title, message, positive, lpositive, neutral, lneutral, negative, lnegative);
    }


    /**
     * 显示视图对话框
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
    @SuppressLint("NewApi")
    public AlertDialog doShowDialog(final String key, final int defclick,
                             int theme, int iconres,
                             String title, String message,
                             String positive, OnClickListener lpositive,
                             String neutral, OnClickListener lneutral,
                             String negative, OnClickListener lnegative) {
        Builder builder = null;
        if (theme > 0) {
            try {
                builder = new Builder(mContext, theme);
            } catch (Throwable e) {
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
            builder.setPositiveButton(positive, lpositive);
        }
        if (neutral != null && neutral.length() > 0) {
            builder.setNeutralButton(neutral, lneutral);
        }
        if (negative != null && negative.length() > 0) {
            builder.setNegativeButton(negative, lnegative);
        }
        if (AfStringUtil.isNotEmpty(key) && defclick > -1) {
            int click = AfPrivateCaches.getInstance(key).getInt(key, -1);
            if (click == defclick) {
                OnClickListener[] clicks = {lpositive, lneutral, lnegative};
                for (int i = 0; i < clicks.length; i++) {
                    if (i == defclick && clicks[i] != null) {
                        clicks[i].onClick(null, i);
                    }
                }
                return null;
            }
        }
        builder.setCancelable(false);
        builder.create();
        AlertDialog show = builder.show();
        if (AfStringUtil.isNotEmpty(key)) {
            View decor = show.getWindow().getDecorView();
            Stack<ViewGroup> stack = new Stack<ViewGroup>();
            stack.add((ViewGroup) decor);
            int iMessage = -1;
            TextView tvMessage = null;
            ViewGroup vgMessage = null;
            while (tvMessage == null && !stack.empty()) {
                ViewGroup pop = stack.pop();
                int count = pop.getChildCount();
                for (int i = 0; i < count; i++) {
                    View childAt = pop.getChildAt(i);
                    if (childAt instanceof ViewGroup) {
                        stack.push((ViewGroup) childAt);
                    } else if (childAt instanceof TextView) {
                        TextView tv = (TextView) childAt;
                        if (tv.getText().equals(message)) {
                            iMessage = i;
                            tvMessage = tv;
                            vgMessage = pop;
                            break;
                        }
                    }
                }
            }
            CheckBox cbTip = null;
            if (tvMessage != null) {
                vgMessage.removeView(tvMessage);
                LinearLayout ll = new LinearLayout(mContext);
                ll.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams lp;
                LayoutParams olp = tvMessage.getLayoutParams();
                lp = new LinearLayout.LayoutParams(olp);
                ll.addView(tvMessage, lp);
                cbTip = new CheckBox(mContext);
                cbTip.setText("不再提示");
                ll.addView(cbTip, lp);
                vgMessage.addView(ll, iMessage, olp);
            }
            if (cbTip != null) {
                final CheckBox finalCbTip = cbTip;
                show.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (finalCbTip.isChecked()) {
                            AfPrivateCaches.getInstance(key).put(key, defclick);
                        }
                    }
                });
            }
        }
        return show;
    }

    public interface OnDateTimeSetListener{
        void onDateTimeSet(int year, int month, int day, int hour, int minute);
    }

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
            onDateTimeSet(calender.getTime());
        }
        protected abstract void onDateTimeSet(Date time);
    }

    /**
     * 选择日期时间
     * @param listener 监听器
     */
    public AlertDialog doSelectDateTime(OnDateTimeSetListener listener) {
        return doSelectDateTime("", new Date(), listener);
    }

    /**
     * 选择日期时间
     * @param title 标题
     * @param listener 监听器
     */
    public AlertDialog doSelectDateTime(String title, OnDateTimeSetListener listener) {
        return doSelectDateTime(title, new Date(), listener);
    }

    /**
     * 选择日期时间
     * @param value 默认时间
     * @param listener 监听器
     */
    public AlertDialog doSelectDateTime(Date value, OnDateTimeSetListener listener) {
        return doSelectDateTime("", value, listener);
    }

    /**
     * 选择日期时间
     * @param title 标题
     * @param value 默认时间
     * @param listener 监听器
     */
    public AlertDialog doSelectDateTime(final String title, final Date value, final OnDateTimeSetListener listener) {
        final Calendar calender = Calendar.getInstance();
        calender.setTime(value);
        int year = calender.get(Calendar.YEAR);
        int month = calender.get(Calendar.MONTH);
        int day = calender.get(Calendar.DAY_OF_MONTH);
        final AlertDialog tDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
            private boolean fdealwith = false;
            @Override
            public void onDateSet(DatePicker view, final int year, final int month,final int day) {
                if(fdealwith){
                    return ;
                }
                fdealwith = true;
                int hour = calender.get(Calendar.HOUR_OF_DAY);
                int minute = calender.get(Calendar.MINUTE);
                Dialog tDialog = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
                    private boolean fdealwith = false;
                    @Override
                    public void onTimeSet(TimePicker view, int hour, int minute) {
                        if(fdealwith){
                            return ;
                        }
                        fdealwith = true;
                        try {
                            listener.onDateTimeSet(year, month, day, hour, minute);
                        } catch (Throwable e) {
                            AfExceptionHandler.handle(e, "doSelectDateTime.listener.onDateTimeSet");
                        }
                    }
                }, hour, minute, true);
                if (title != null && title.length() > 0) {
                    tDialog.setTitle(title);
                }
                tDialog.show();
                tDialog.setCancelable(true);
            }
        }, year, month, day);
        if (title != null && title.length() > 0) {
            tDialog.setTitle(title);
        }
        tDialog.show();
        tDialog.setCancelable(true);
        return tDialog;
    }

    public interface OnTimeSetListener {
        void onTimeSet(int hour, int minute);
    }

    public static abstract class OnSimpleTimeSetListener implements OnTimeSetListener{
        @Override
        public void onTimeSet(int hour, int minute) {
            Calendar calender = Calendar.getInstance();
            calender.setTime(new Date(0));
            calender.set(Calendar.HOUR_OF_DAY, hour);
            calender.set(Calendar.MINUTE,minute);
            onTimeSet(calender.getTime());
        }
        protected abstract void onTimeSet(Date time);
    }

    /**
     * 选择时间
     * @param listener 监听器
     */
    public AlertDialog doSelectTime(OnTimeSetListener listener) {
        return doSelectTime("", new Date(), listener);
    }

    /**
     * 选择时间
     * @param title 标题
     * @param listener 监听器
     */
    public AlertDialog doSelectTime(String title, OnTimeSetListener listener) {
        return doSelectTime(title, new Date(), listener);
    }

    /**
     * 选择时间
     * @param value 默认时间
     * @param listener 监听器
     */
    public AlertDialog doSelectTime(Date value, OnTimeSetListener listener) {
        return doSelectTime("", value, listener);
    }

    /**
     * 选择时间
     * @param title 标题
     * @param value 默认时间
     * @param listener 监听器
     */
    public AlertDialog doSelectTime(String title, Date value, final OnTimeSetListener listener) {
        Calendar calender = Calendar.getInstance();
        calender.setTime(value);
        int hour = calender.get(Calendar.HOUR_OF_DAY);
        int minute = calender.get(Calendar.MINUTE);
        AlertDialog tDialog = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
            private boolean fdealwith = false;
            @Override
            public void onTimeSet(TimePicker view, int hour, int minute) {
                if(fdealwith){
                    return ;
                }
                fdealwith = true;

                try {
                    listener.onTimeSet(hour, minute);
                } catch (Throwable e) {
                    AfExceptionHandler.handle(e, "doSelectTime.listener.onTimeSet");
                }
            }
        }, hour, minute, true);
        if (title != null && title.length() > 0) {
            tDialog.setTitle(title);
        }
        tDialog.show();
        tDialog.setCancelable(true);
        return tDialog;
    }


    public interface OnDateSetListener {
        void onDateSet(int year, int month, int day);
    }

    public static abstract class OnSimpleDateSetListener implements OnDateSetListener{
        @Override
        public void onDateSet(int year, int month, int day) {
            Calendar calender = Calendar.getInstance();
            calender.setTime(new Date(0));
            calender.set(Calendar.YEAR, year);
            calender.set(Calendar.MONTH, month);
            calender.set(Calendar.DAY_OF_MONTH, day);
            onDateSet(calender.getTime());
        }
        protected abstract void onDateSet(Date date);
    }

    /**
     * 选择日期
     * @param listener 监听器
     */
    public AlertDialog doSelectDate(OnDateSetListener listener) {
        return doSelectDate("", new Date(), listener);
    }

    /**
     * 选择日期
     * @param title 标题
     * @param listener 监听器
     */
    public AlertDialog doSelectDate(String title, OnDateSetListener listener) {
        return doSelectDate(title, new Date(), listener);
    }

    /**
     * 选择日期
     * @param value 默认时间
     * @param listener 监听器
     */
    public AlertDialog doSelectDate(Date value, OnDateSetListener listener) {
        return doSelectDate("", value, listener);
    }

    /**
     * 选择日期
     * @param title 标题
     * @param value 默认时间
     * @param listener 监听器
     */
    public AlertDialog doSelectDate(String title, Date value, final OnDateSetListener listener) {
        Calendar calender = Calendar.getInstance();
        calender.setTime(value);
        int year = calender.get(Calendar.YEAR);
        int month = calender.get(Calendar.MONTH);
        int day = calender.get(Calendar.DAY_OF_MONTH);
        AlertDialog tDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
            private boolean fdealwith = false;
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                if(fdealwith){
                    return ;
                }
                fdealwith = true;
                try {
                    listener.onDateSet(year, month, day);
                } catch (Throwable e) {
                    AfExceptionHandler.handle(e, "doSelectDate.listener.onDateSet");
                }
            }
        }, year, month, day);
        if (title != null && title.length() > 0) {
            tDialog.setTitle(title);
        }
        tDialog.show();
        tDialog.setCancelable(true);
        return tDialog;
    }
}
