package com.andframe.api.dialog;

import android.app.Dialog;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.graphics.drawable.Drawable;
import android.support.annotation.ArrayRes;
import android.support.annotation.AttrRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListAdapter;

/**
 * 对话框 构建器
 * Created by SCWANG on 2017/5/3.
 */

public interface DialogBuilder {

    DialogBuilder title(@StringRes int titleId);
    DialogBuilder title(CharSequence title);
    DialogBuilder customTitle(View customTitleView);
    DialogBuilder message(@StringRes int messageId);
    DialogBuilder message(CharSequence message);
    DialogBuilder icon(@DrawableRes int iconId);
    DialogBuilder icon(Drawable icon);
    DialogBuilder iconAttribute(@AttrRes int attrId);
    DialogBuilder positive(@StringRes int textId, OnClickListener listener);
    DialogBuilder positive(CharSequence text, OnClickListener listener);
    DialogBuilder negative(@StringRes int textId, OnClickListener listener);
    DialogBuilder negative(CharSequence text, OnClickListener listener);
    DialogBuilder neutraln(@StringRes int textId, OnClickListener listener);
    DialogBuilder neutraln(CharSequence text, OnClickListener listener);
    DialogBuilder cancelable(boolean cancelable);
    DialogBuilder cancelListener(OnCancelListener onCancelListener);
    DialogBuilder dismissListener(OnDismissListener onDismissListener);
    DialogBuilder keyListener(OnKeyListener onKeyListener);
    DialogBuilder items(@ArrayRes int itemsId, OnClickListener listener);
    DialogBuilder items(CharSequence[] items, OnClickListener listener);
    DialogBuilder adapter(ListAdapter adapter, OnClickListener listener);
    DialogBuilder multiChoice(@ArrayRes int itemsId, boolean[] checkedItems, OnMultiChoiceClickListener listener);
    DialogBuilder multiChoice(CharSequence[] items, boolean[] checkedItems, OnMultiChoiceClickListener listener);
    DialogBuilder singleChoice(@ArrayRes int itemsId, int checkedItem, OnClickListener listener);
    DialogBuilder singleChoice(CharSequence[] items, int checkedItem, OnClickListener listener);
    DialogBuilder singleChoice(ListAdapter adapter, int checkedItem, OnClickListener listener);
    DialogBuilder selectedListener(OnItemSelectedListener listener);

    DialogBuilder view(int layoutResId);
    DialogBuilder view(View view);

    Dialog create();
    Dialog show();
}
