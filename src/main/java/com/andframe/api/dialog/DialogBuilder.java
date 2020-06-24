package com.andframe.api.dialog;

import android.app.Dialog;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.graphics.drawable.Drawable;
import android.support.annotation.ArrayRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.EditText;

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
    DialogBuilder cancelable(boolean cancelable);
    DialogBuilder cancelListener(OnCancelListener onCancelListener);
    DialogBuilder dismissListener(OnDismissListener onDismissListener);
    DialogBuilder items(@ArrayRes int itemsId, OnClickListener listener);
    DialogBuilder items(CharSequence[] items, OnClickListener listener);
    DialogBuilder multiChoice(@ArrayRes int itemsId, boolean[] checkedItems, OnMultiChoiceClickListener listener);
    DialogBuilder multiChoice(CharSequence[] items, boolean[] checkedItems, OnMultiChoiceClickListener listener);
    DialogBuilder singleChoice(@ArrayRes int itemsId, int checkedItem, OnClickListener listener);
    DialogBuilder singleChoice(CharSequence[] items, int checkedItem, OnClickListener listener);

    DialogBuilder view(View view);
    DialogBuilder view(@LayoutRes int layoutResId);

    Dialog create();
    Dialog show();



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
