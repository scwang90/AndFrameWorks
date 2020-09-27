package com.andframe.api.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.graphics.drawable.Drawable;
import android.widget.DatePicker;
import android.widget.TimePicker;
import androidx.annotation.*;
import android.view.View;
import android.widget.EditText;

import java.util.Date;

/**
 * 对话框 构建器
 * Created by SCWANG on 2017/5/3.
 */

public interface DialogBuilder {

    DialogBuilder doNotAsk();
    DialogBuilder doNotAsk(int defButtonIndex);
    DialogBuilder doNotAskKey(CharSequence key);
    DialogBuilder doNotAskKey(CharSequence key, int defButtonIndex);
    DialogBuilder title(CharSequence title);
    DialogBuilder title(@StringRes int titleId);
    DialogBuilder message(@StringRes int messageId);
    DialogBuilder message(CharSequence message);
    DialogBuilder icon(@DrawableRes int iconId);
    DialogBuilder icon(Drawable icon);
    DialogBuilder button(@StringRes int textId);
    DialogBuilder button(CharSequence text);
    DialogBuilder button(@StringRes int textId, OnClickListener listener);
    DialogBuilder button(CharSequence text, OnClickListener listener);
    DialogBuilder button(CharSequence text, @ColorInt int color, OnClickListener listener);
    DialogBuilder cancelable(boolean cancelable);
    DialogBuilder cancelListener(OnCancelListener onCancelListener);
    DialogBuilder dismissListener(OnDismissListener onDismissListener);
    DialogBuilder items(@ArrayRes int itemsId, OnClickListener listener);
    DialogBuilder items(CharSequence[] items, OnClickListener listener);
    DialogBuilder choice(@ArrayRes int itemsId, OnMultiChoiceClickListener itemsListener, OnClickListener listener);
    DialogBuilder choice(CharSequence[] items, OnMultiChoiceClickListener itemsListener, OnClickListener listener);
    DialogBuilder choice(@ArrayRes int itemsId, boolean[] checkedItems, OnMultiChoiceClickListener itemsListener, OnClickListener listener);
    DialogBuilder choice(CharSequence[] items, boolean[] checkedItems, OnMultiChoiceClickListener itemsListener, OnClickListener listener);

    DialogBuilder view(View view);
    DialogBuilder view(@LayoutRes int layoutResId);

    Dialog create();
    Dialog show();


    //<editor-fold desc="input date time">

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
        boolean onPreTimeSet(TimePicker view, int hourOfDay, int minute);
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

    DialogBuilder month(DatePickerDialog.OnDateSetListener listener);
    DialogBuilder month(Date value, DatePickerDialog.OnDateSetListener listener);

    DialogBuilder date(DatePickerDialog.OnDateSetListener listener);
    DialogBuilder date(Date value, DatePickerDialog.OnDateSetListener listener);

    DialogBuilder time(TimePickerDialog.OnTimeSetListener listener);
    DialogBuilder time(Date value, TimePickerDialog.OnTimeSetListener listener);
    //</editor-fold>


    //<editor-fold desc="input text">
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
    DialogBuilder inputTextType(int type);
    DialogBuilder inputTextHint(String hint);
    DialogBuilder inputTextValue(String value);
    DialogBuilder inputText(InputTextListener listener);
    DialogBuilder inputText(CharSequence value, InputTextListener listener);
    DialogBuilder inputText(int typp, CharSequence value, InputTextListener listener);
    //</editor-fold>
}
