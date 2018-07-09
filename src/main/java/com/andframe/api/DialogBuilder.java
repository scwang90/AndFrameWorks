package com.andframe.api;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Date;

/**
 * 对话框构建器
 * Created by SCWANG on 2016/9/2.
 */
@SuppressWarnings("unused")
public interface DialogBuilder {

    //<editor-fold desc="接口定义">

    /**
     * inputText 的监听器
     */
    interface InputTextListener {
        /**
         * @return false 表示输入验证错误，需要重新输入
         */
        boolean onInputTextConfirm(EditText input, String value);
    }

    /**
     * 可取消的 InputTextListener
     */
    abstract class InputTextCancelable implements InputTextListener {
        public void onInputTextCancel(EditText input) {
        }
    }

    /**
     * 日期时间监听器
     */
    interface OnDateTimeSetListener{
        void onDateTimeSet(int year, int month, int day, int hour, int minute);
    }

    /**
     * 日期验证
     */
    interface OnDateSetVerifyListener extends DatePickerDialog.OnDateSetListener {
        /**
         * 验证时间是否符合条件
         * @param view 时间控件
         * @return true 符合条件 false 不符合条件（将不会关闭对话框）
         */
        boolean onPreDateSet(DatePicker view, int year, int month, int dayOfMonth);
    }
    /**
     * 时间验证
     */
    interface OnTimeSetVerifyListener extends TimePickerDialog.OnTimeSetListener {
        /**
         * 验证时间是否符合条件
         *
         * @param view 时间控件
         * @param hourOfDay 小时
         * @param minute 分钟
         * @return true 符合条件 false 不符合条件（将不会关闭对话框）
         */
        boolean onPreTimeSet(TimePickerDialog dialog, TimePicker view, int hourOfDay, int minute);
    }

    /**
     * 日期时间验证
     */
    interface OnDateTimeSetVerifyListener extends OnDateTimeSetListener {
        /**
         * 验证时间是否符合条件
         * @param view 时间控件
         * @return true 符合条件 false 不符合条件（将不会关闭对话框）
         */
        boolean onPreDateSet(DatePicker view, int year, int month, int dayOfMonth);
        /**
         * 验证时间是否符合条件
         * @param view 时间控件
         * @param hourOfDay 小时
         * @param minute 分钟
         * @return true 符合条件 false 不符合条件（将不会关闭对话框）
         */
        boolean onPreTimeSet(TimePicker view, int hourOfDay, int minute);
    }

    //</editor-fold>

    //<editor-fold desc="普通按钮对话框">
    /**
     * 显示对话框 并添加默认按钮 "我知道了"
     *
     * @param title   显示标题
     * @param message 显示内容
     */
    Dialog showDialog(CharSequence title, CharSequence message);

    /**
     * 显示对话框 并添加默认按钮 "我知道了"
     *
     * @param title     显示标题
     * @param message   显示内容
     * @param lpositive 点击  "我知道了" 响应事件
     */
    Dialog showDialog(CharSequence title, CharSequence message, DialogInterface.OnClickListener lpositive);

    /**
     * 显示对话框
     *
     * @param title     显示标题
     * @param message   显示内容
     * @param positive  确认 按钮显示信息
     * @param lpositive 点击  确认 按钮 响应事件
     */
    Dialog showDialog(CharSequence title, CharSequence message, CharSequence positive, DialogInterface.OnClickListener lpositive);

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
    Dialog showDialog(CharSequence title, CharSequence message, CharSequence negative, DialogInterface.OnClickListener lnegative, CharSequence positive, DialogInterface.OnClickListener lpositive);

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
    Dialog showDialog(CharSequence title, CharSequence message, CharSequence negative, DialogInterface.OnClickListener lnegative, CharSequence neutral, DialogInterface.OnClickListener lneutral, CharSequence positive, DialogInterface.OnClickListener lpositive);

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
    Dialog showDialog(int iconres, CharSequence title, CharSequence message, CharSequence negative, DialogInterface.OnClickListener lnegative, CharSequence positive, DialogInterface.OnClickListener lpositive);

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
    Dialog showDialog(int iconres, CharSequence title, CharSequence message, CharSequence negative, DialogInterface.OnClickListener lnegative, CharSequence neutral, DialogInterface.OnClickListener lneutral, CharSequence positive, DialogInterface.OnClickListener lpositive);

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
    Dialog showDialog(int theme, int iconres,
                             CharSequence title, CharSequence message, CharSequence negative, DialogInterface.OnClickListener lnegative, CharSequence neutral, DialogInterface.OnClickListener lneutral, CharSequence positive, DialogInterface.OnClickListener lpositive);
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
    Dialog showViewDialog(CharSequence title, View view, CharSequence positive, DialogInterface.OnClickListener lpositive);

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
    Dialog showViewDialog(CharSequence title, View view, CharSequence negative, DialogInterface.OnClickListener lnegative, CharSequence positive, DialogInterface.OnClickListener lpositive);

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
    Dialog showViewDialog(CharSequence title, View view, CharSequence negative, DialogInterface.OnClickListener lnegative, CharSequence neutral, DialogInterface.OnClickListener lneutral, CharSequence positive, DialogInterface.OnClickListener lpositive);

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
    Dialog showViewDialog(int iconres, CharSequence title, View view, CharSequence negative, DialogInterface.OnClickListener lnegative, CharSequence positive, DialogInterface.OnClickListener lpositive);

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
    Dialog showViewDialog(int iconres, CharSequence title, View view, CharSequence negative, DialogInterface.OnClickListener lnegative, CharSequence neutral, DialogInterface.OnClickListener lneutral, CharSequence positive, DialogInterface.OnClickListener lpositive);

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
    Dialog showViewDialog(int theme,
                                 int iconres, CharSequence title, View view, CharSequence negative, DialogInterface.OnClickListener lnegative, CharSequence neutral, DialogInterface.OnClickListener lneutral, CharSequence positive, DialogInterface.OnClickListener lpositive);
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
    Dialog selectItem(CharSequence title, CharSequence[] items, DialogInterface.OnClickListener listener, boolean cancel);

    /**
     * 显示一个单选对话框
     *
     * @param title    对话框标题
     * @param items    选择菜单项
     * @param listener 选择监听器
     * @param oncancel 取消选择监听器
     */
    Dialog selectItem(CharSequence title, CharSequence[] items, DialogInterface.OnClickListener listener, final DialogInterface.OnCancelListener oncancel);

    /**
     * 显示一个单选对话框 （默认可取消）
     *
     * @param title    对话框标题
     * @param items    选择菜单项
     * @param listener 选择监听器
     */
    Dialog selectItem(CharSequence title, CharSequence[] items, DialogInterface.OnClickListener listener);
    //</editor-fold>

    //<editor-fold desc="多选对话框">

    /**
     * 显示一个多选对话框 （默认可取消）
     *
     * @param title         对话框标题
     * @param items         选择菜单项
     * @param listener      选择监听器
     */
    Dialog multiChoice(CharSequence title, CharSequence[] items, DialogInterface.OnMultiChoiceClickListener listener);

    /**
     * 显示一个多选对话框 （默认可取消）
     *
     * @param title         对话框标题
     * @param items         选择菜单项
     * @param checkedItems  选择结果
     * @param listener      选择监听器
     */
    Dialog multiChoice(CharSequence title, CharSequence[] items, boolean[] checkedItems, DialogInterface.OnMultiChoiceClickListener listener);
    /**
     * 显示一个多选对话框 （默认可取消）
     *
     * @param title         对话框标题
     * @param items         选择菜单项
     * @param checkedItems  选择结果
     * @param listener      选择监听器
     * @param lpositive     完成选择器
     */
    Dialog multiChoice(CharSequence title, CharSequence[] items, boolean[] checkedItems, DialogInterface.OnMultiChoiceClickListener listener, DialogInterface.OnClickListener lpositive);

    //</editor-fold>

    //<editor-fold desc="输入对话框">
    /**
     * 弹出一个文本输入框
     *
     * @param title    标题
     * @param listener 监听器
     */
    Dialog inputText(CharSequence title, InputTextListener listener);

    /**
     * 弹出一个文本输入框
     *
     * @param title    标题
     * @param type     android.text.InputType
     * @param listener 监听器
     */
    Dialog inputText(CharSequence title, int type, InputTextListener listener);

    /**
     * 弹出一个文本输入框
     *
     * @param title    标题
     * @param defaul   默认值
     * @param listener 监听器
     */
    Dialog inputText(CharSequence title, CharSequence defaul, InputTextListener listener);

    /**
     * 弹出一个文本输入框
     *
     * @param title    标题
     * @param defaul   默认值
     * @param type     android.text.InputType
     * @param listener 监听器
     */
    Dialog inputText(CharSequence title, final CharSequence defaul, int type, final InputTextListener listener);
    //</editor-fold>

    //<editor-fold desc="多行输入框">
    /**
     * 弹出一个文本输入框
     *
     * @param title    标题
     * @param listener 监听器
     */
    Dialog inputLines(CharSequence title, InputTextListener listener);

    /**
     * 弹出一个文本输入框
     *
     * @param title    标题
     * @param type     android.text.InputType
     * @param listener 监听器
     */
    Dialog inputLines(CharSequence title, int type, InputTextListener listener);

    /**
     * 弹出一个文本输入框
     *
     * @param title    标题
     * @param defaul   默认值
     * @param listener 监听器
     */
    Dialog inputLines(CharSequence title, CharSequence defaul, InputTextListener listener);

    /**
     * 弹出一个文本输入框
     *
     * @param title    标题
     * @param defaul   默认值
     * @param type     android.text.InputType
     * @param listener 监听器
     */
    Dialog inputLines(CharSequence title, final CharSequence defaul, int type, final InputTextListener listener);
    //</editor-fold>

    //<editor-fold desc="不再提示对话框">
    /**
     * 显示对话框(不再提示) 并添加默认按钮 "我知道了"
     *
     * @param key       不再显示KEY
     * @param title   显示标题
     * @param message 显示内容
     */
    Dialog showKeyDialog(String key, CharSequence title, CharSequence message);

    /**
     * 显示对话框(不再提示)
     *
     * @param key       不再显示KEY
     * @param title     显示标题
     * @param message   显示内容
     * @param positive  确认 按钮显示信息
     * @param lpositive 点击  确认 按钮 响应事件
     */
    Dialog showKeyDialog(String key, CharSequence title, CharSequence message, CharSequence positive, DialogInterface.OnClickListener lpositive);

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
    Dialog showKeyDialog(String key, int defclick,
                                CharSequence title, CharSequence message, CharSequence negative, DialogInterface.OnClickListener lnegative, CharSequence positive, DialogInterface.OnClickListener lpositive);

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
    Dialog showKeyDialog(String key, int defclick,
                                CharSequence title, CharSequence message, CharSequence negative, DialogInterface.OnClickListener lnegative, CharSequence neutral, DialogInterface.OnClickListener lneutral, CharSequence positive, DialogInterface.OnClickListener lpositive);

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
    Dialog showKeyDialog(String key, int defclick,
                                int iconres, CharSequence title, CharSequence message, CharSequence negative, DialogInterface.OnClickListener lnegative, CharSequence positive, DialogInterface.OnClickListener lpositive);

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
    Dialog showKeyDialog(String key, int defclick,
                                int iconres, CharSequence title, CharSequence message, CharSequence negative, DialogInterface.OnClickListener lnegative, CharSequence neutral, DialogInterface.OnClickListener lneutral, CharSequence positive, DialogInterface.OnClickListener lpositive);


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
    Dialog showKeyDialog(final String key, final int defclick,
                                int theme, int iconres,
                                CharSequence title, final CharSequence message, CharSequence negative, DialogInterface.OnClickListener lnegative, CharSequence neutral, DialogInterface.OnClickListener lneutral, CharSequence positive, DialogInterface.OnClickListener lpositive);
    //</editor-fold>

    //<editor-fold desc="日期时间选择对话框">
    /**
     * 选择日期时间
     * @param listener 监听器
     */
    Dialog selectDateTime(OnDateTimeSetListener listener);

    /**
     * 选择日期时间
     * @param title 标题
     * @param listener 监听器
     */
    Dialog selectDateTime(CharSequence title, OnDateTimeSetListener listener);

    /**
     * 选择日期时间
     * @param value 默认时间
     * @param listener 监听器
     */
    Dialog selectDateTime(Date value, OnDateTimeSetListener listener);

    /**
     * 选择日期时间
     * @param title 标题
     * @param value 默认时间
     * @param listener 监听器
     */
    Dialog selectDateTime(final CharSequence title, final Date value, final OnDateTimeSetListener listener);
    //</editor-fold>

    //<editor-fold desc="时间选择对话框">
    /**
     * 选择时间
     * @param listener 监听器
     */
    Dialog selectTime(TimePickerDialog.OnTimeSetListener listener);

    /**
     * 选择时间
     * @param title 标题
     * @param listener 监听器
     */
    Dialog selectTime(CharSequence title, TimePickerDialog.OnTimeSetListener listener);

    /**
     * 选择时间
     * @param value 默认时间
     * @param listener 监听器
     */
    Dialog selectTime(Date value, TimePickerDialog.OnTimeSetListener listener);

    /**
     * 选择时间
     * @param title 标题
     * @param value 默认时间
     * @param listener 监听器
     */
    Dialog selectTime(CharSequence title, Date value, final TimePickerDialog.OnTimeSetListener listener);
    //</editor-fold>

    //<editor-fold desc="日期选择对话框">
    /**
     * 选择日期
     * @param listener 监听器
     */
    Dialog selectDate(DatePickerDialog.OnDateSetListener listener);

    /**
     * 选择日期
     * @param title 标题
     * @param listener 监听器
     */
    Dialog selectDate(CharSequence title, DatePickerDialog.OnDateSetListener listener);

    /**
     * 选择日期
     * @param value 默认时间
     * @param listener 监听器
     */
    Dialog selectDate(Date value, DatePickerDialog.OnDateSetListener listener);

    /**
     * 选择日期
     * @param title 标题
     * @param value 默认时间
     * @param listener 监听器
     */
    Dialog selectDate(CharSequence title, Date value, DatePickerDialog.OnDateSetListener listener);
    //</editor-fold>

    //<editor-fold desc="进度显示对话框">
    /**
     * 显示 进度对话框
     *
     * @param message 消息
     */
    Dialog showProgressDialog(CharSequence message);

    /**
     * 显示 进度对话框
     *
     * @param message 消息
     * @param cancel  是否可取消
     */
    Dialog showProgressDialog(CharSequence message, boolean cancel);

    /**
     * 显示 进度对话框
     *
     * @param message  消息
     * @param cancel   是否可取消
     * @param textsize 字体大小
     */
    Dialog showProgressDialog(CharSequence message, boolean cancel, int textsize);

    /**
     * 显示 进度对话框
     *
     * @param message  消息
     * @param listener 字体大小
     */
    Dialog showProgressDialog(CharSequence message, DialogInterface.OnCancelListener listener);

    /**
     * 显示 进度对话框
     *
     * @param message  消息
     * @param listener 是否可取消
     * @param textsize 字体大小
     */
    Dialog showProgressDialog(CharSequence message, DialogInterface.OnCancelListener listener, int textsize);

    /**
     * 隐藏 进度对话框
     */
    void hideProgressDialog();

    /**
     * 动态改变等待对话框的文字
     *
     * @param text   更新的文字
     */
    void setProgressDialogText(CharSequence text);

    /**
     * 是否正在显示进度对话框
     */
    boolean isProgressDialogShowing();
    //</editor-fold>

}
