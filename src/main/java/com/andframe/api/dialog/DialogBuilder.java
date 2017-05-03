package com.andframe.api.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.support.annotation.ArrayRes;
import android.support.annotation.AttrRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;

/**
 * 对话框 构建器
 * Created by SCWANG on 2017/5/3.
 */

public interface DialogBuilder {

    
    DialogBuilder title(@StringRes int titleId);

    
    DialogBuilder title(CharSequence title);
    
    DialogBuilder customTitle(View customTitleView);

    
    DialogBuilder setMessage(@StringRes int messageId);

    
    DialogBuilder setMessage(CharSequence message);

    
    DialogBuilder icon(@DrawableRes int iconId);


    DialogBuilder icon(Drawable icon);

    
    DialogBuilder iconAttribute(@AttrRes int attrId);

    
    DialogBuilder positive(@StringRes int textId, DialogInterface.OnClickListener listener);

    
    DialogBuilder positive(CharSequence text, DialogInterface.OnClickListener listener);

    
    DialogBuilder negative(@StringRes int textId, DialogInterface.OnClickListener listener);

    
    DialogBuilder negative(CharSequence text, DialogInterface.OnClickListener listener);

    
    DialogBuilder neutraln(@StringRes int textId, DialogInterface.OnClickListener listener);

    
    DialogBuilder neutraln(CharSequence text, DialogInterface.OnClickListener listener);

    
    DialogBuilder cancelable(boolean cancelable);

    DialogBuilder onCancelListener(DialogInterface.OnCancelListener onCancelListener);

    
    DialogBuilder onDismissListener(DialogInterface.OnDismissListener onDismissListener);

    
    DialogBuilder onKeyListener(DialogInterface.OnKeyListener onKeyListener);

    
    DialogBuilder items(@ArrayRes int itemsId, DialogInterface.OnClickListener listener);

    
    DialogBuilder items(CharSequence[] items, DialogInterface.OnClickListener listener);

    
    DialogBuilder setAdapter(ListAdapter adapter, DialogInterface.OnClickListener listener);

    
    DialogBuilder setMultiChoiceItems(@ArrayRes int itemsId, boolean[] checkedItems,
                                                   DialogInterface.OnMultiChoiceClickListener listener);

    
    DialogBuilder setMultiChoiceItems(CharSequence[] items, boolean[] checkedItems,
                                                   DialogInterface.OnMultiChoiceClickListener listener);

    
    DialogBuilder setMultiChoiceItems(Cursor cursor, String isCheckedColumn, String labelColumn,
                                                   DialogInterface.OnMultiChoiceClickListener listener);

    
    DialogBuilder setSingleChoiceItems(@ArrayRes int itemsId, int checkedItem,
                                       DialogInterface.OnClickListener listener);

    
    DialogBuilder setSingleChoiceItems(Cursor cursor, int checkedItem, String labelColumn,
                                                    DialogInterface.OnClickListener listener);
    
    DialogBuilder setSingleChoiceItems(CharSequence[] items, int checkedItem, DialogInterface.OnClickListener listener);

    
    DialogBuilder setSingleChoiceItems(ListAdapter adapter, int checkedItem, DialogInterface.OnClickListener listener);

    
    DialogBuilder onItemSelectedListener(AdapterView.OnItemSelectedListener listener);

    
    DialogBuilder setView(int layoutResId);

    
    DialogBuilder setView(View view);

    
    @Deprecated
    DialogBuilder setView(View view, int viewSpacingLeft, int viewSpacingTop,
                                       int viewSpacingRight, int viewSpacingBottom);

    Dialog create();
    Dialog show();
}
