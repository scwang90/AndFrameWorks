package com.andframe.impl.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;
import android.view.View;

import com.andframe.$;
import com.andframe.api.dialog.DialogBuilder;
import com.andframe.api.dialog.DialogFactory;

import java.sql.Time;
import java.util.Date;

public class DefaultDialogBuilder implements DialogBuilder {

    protected DialogBluePrint entity = new DialogBluePrint();

    protected Context context;
    protected boolean mCreateCalled;
    protected DialogFactory factory;

    public DefaultDialogBuilder(Context context, DialogFactory factory, boolean autoShow) {
        this.context = context;
        this.factory = factory;
        if (autoShow) {
            $.dispatch(()->{
                if (!mCreateCalled) {
                    show();
                }
            });
        }
    }

    @Override
    public DialogBuilder doNotAsk() {
        return doNotAsk(-1);
    }

    @Override
    public DialogBuilder doNotAsk(int defButtonIndex) {
        entity.key = "";
        entity.keyButtonIndex = defButtonIndex;
        return this;
    }

    @Override
    public DialogBuilder doNotAskKey(CharSequence key) {
        return doNotAskKey(key, -1);
    }

    @Override
    public DialogBuilder doNotAskKey(CharSequence key, int defButtonIndex) {
        entity.key = key;
        entity.keyButtonIndex = defButtonIndex;
        return this;
    }

    @Override
    public DialogBuilder title(int titleId) {
        entity.title = context.getString(titleId);
        return this;
    }

    @Override
    public DialogBuilder title(CharSequence title) {
        entity.title = title;
        return this;
    }

    @Override
    public DialogBuilder message(int messageId) {
        entity.message = context.getString(messageId);
        return this;
    }

    @Override
    public DialogBuilder message(CharSequence message) {
        entity.message = message;
        return this;
    }

    @Override
    public DialogBuilder icon(int iconId) {
        entity.iconId = iconId;
        entity.iconDrawable = ContextCompat.getDrawable(context, iconId);
        return this;
    }

    @Override
    public DialogBuilder icon(Drawable icon) {
        entity.iconDrawable = icon;
        return this;
    }

    @Override
    public DialogBuilder button(int textId) {
        return this.button(textId, null);
    }

    @Override
    public DialogBuilder button(CharSequence text) {
        return this.button(text, null);
    }

    @Override
    public DialogBuilder button(@StringRes int textId, DialogInterface.OnClickListener listener) {
        if (textId != View.NO_ID || listener != null) {
            ButtonEntity button = new ButtonEntity();
            button.listener = listener;
            button.text = context.getString(textId);
            entity.buttons.add(button);
        }
        return this;
    }

    @Override
    public DialogBuilder button(CharSequence text, DialogInterface.OnClickListener listener) {
        if (!TextUtils.isEmpty(text) || listener != null) {
            ButtonEntity button = new ButtonEntity();
            button.text = text;
            button.listener = listener;
            entity.buttons.add(button);
        }
        return this;
    }

    @Override
    public DialogBuilder button(CharSequence text, int color, DialogInterface.OnClickListener listener) {
        if (!TextUtils.isEmpty(text) || listener != null) {
            ButtonEntity button = new ButtonEntity();
            button.text = text;
            button.color = color;
            button.listener = listener;
            entity.buttons.add(button);
        }
        return this;
    }

    @Override
    public DialogBuilder cancelable(boolean cancelable) {
        entity.cancelable = cancelable;
        return this;
    }

    @Override
    public DialogBuilder cancelListener(DialogInterface.OnCancelListener cancelListener) {
        entity.cancelable = false;
        entity.cancelListener = cancelListener;
        return this;
    }

    @Override
    public DialogBuilder dismissListener(DialogInterface.OnDismissListener dismissListener) {
        entity.dismissListener = dismissListener;
        return this;
    }

    @Override
    public DialogBuilder items(int itemsId, DialogInterface.OnClickListener listener) {
        entity.itemsListener = listener;
        entity.items = context.getResources().getTextArray(itemsId);
        return this;
    }

    @Override
    public DialogBuilder items(CharSequence[] items, DialogInterface.OnClickListener listener) {
        entity.items = items;
        entity.itemsListener = listener;
        return this;
    }

    @Override
    public DialogBuilder choice(int itemsId, DialogInterface.OnMultiChoiceClickListener itemsListener, DialogInterface.OnClickListener listener) {
        entity.items = context.getResources().getTextArray(itemsId);
        entity.itemsListener = listener;
        entity.itemsMultiListener = itemsListener;
        return this;
    }

    @Override
    public DialogBuilder choice(CharSequence[] items, DialogInterface.OnMultiChoiceClickListener itemsListener, DialogInterface.OnClickListener listener) {
        entity.items = items;
        entity.itemsListener = listener;
        entity.itemsMultiListener = itemsListener;
        return this;
    }

    @Override
    public DialogBuilder choice(int itemsId, boolean[] checkedItems, DialogInterface.OnMultiChoiceClickListener itemsListener, DialogInterface.OnClickListener listener) {
        entity.items = context.getResources().getTextArray(itemsId);
        entity.itemsChecked = checkedItems;
        entity.itemsListener = listener;
        entity.itemsMultiListener = itemsListener;
        return this;
    }

    @Override
    public DialogBuilder choice(CharSequence[] items, boolean[] checkedItems, DialogInterface.OnMultiChoiceClickListener itemsListener, DialogInterface.OnClickListener listener) {
        entity.items = items;
        entity.itemsChecked = checkedItems;
        entity.itemsListener = listener;
        entity.itemsMultiListener = itemsListener;
        return this;
    }

    @Override
    public DialogBuilder month(DatePickerDialog.OnDateSetListener listener) {
        entity.month = null;
        entity.monthListener = listener;
        return this;
    }

    @Override
    public DialogBuilder month(Date value, DatePickerDialog.OnDateSetListener listener) {
        entity.month = value;
        entity.monthListener = listener;
        return this;
    }

    @Override
    public DialogBuilder date(DatePickerDialog.OnDateSetListener listener) {
        entity.date = null;
        entity.dateListener = listener;
        return this;
    }

    @Override
    public DialogBuilder date(Date value, DatePickerDialog.OnDateSetListener listener) {
        entity.date = value;
        entity.dateListener = listener;
        return this;
    }

    @Override
    public DialogBuilder time(TimePickerDialog.OnTimeSetListener listener) {
        entity.time = null;
        entity.timeListener = listener;
        return this;
    }

    @Override
    public DialogBuilder time(Date value, TimePickerDialog.OnTimeSetListener listener) {
        entity.time = value;
        entity.timeListener = listener;
        return this;
    }

    @Override
    public DialogBuilder view(View view) {
        entity.view = view;
        return this;
    }

    @Override
    public DialogBuilder view(int layoutResId) {
        entity.view = View.inflate(context, layoutResId, null);
        return this;
    }

    @Override
    public final Dialog create() {
        mCreateCalled = true;
        if (factory == null) {
            factory = new SystemDialogFactory();
        }
        return factory.build(context, entity);
    }

    @Override
    public Dialog show() {
        Dialog dialog = create();
        if (dialog != null) {
            if (!entity.cancelable) {
                dialog.setCanceledOnTouchOutside(false);
            }
            dialog.show();
        }
        return dialog;
    }

    @Override
    public DialogBuilder inputText(InputTextListener listener) {
        entity.inputListener = listener;
        return this;
    }

    @Override
    public DialogBuilder inputText(CharSequence value, InputTextListener listener) {
        entity.inputText = value;
        entity.inputListener = listener;
        return this;
    }

    /**
     * @see android.text.InputType
     */
    @Override
    public DialogBuilder inputText(int type, CharSequence value, InputTextListener listener) {
        entity.inputType = type;
        entity.inputText = value;
        entity.inputListener = listener;
        return this;
    }

    @Override
    public DialogBuilder inputTextHint(String hint) {
        entity.inputHint = hint;
        return this;
    }

    @Override
    public DialogBuilder inputTextType(int type) {
        entity.inputType = type;
        return this;
    }

    @Override
    public DialogBuilder inputTextValue(String value) {
        entity.inputText = value;
        return this;
    }
}
