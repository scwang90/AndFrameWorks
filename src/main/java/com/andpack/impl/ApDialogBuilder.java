package com.andpack.impl;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;

import com.andframe.feature.AfDialogBuilder;
import com.andframe.listener.SafeListener;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.flyco.dialog.widget.NormalDialog;

import java.util.ArrayList;
import java.util.List;

//import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * 对话框构建器
 * Created by SCWANG on 2016/8/9.
 */
public class ApDialogBuilder extends AfDialogBuilder {

    public ApDialogBuilder(Context context) {
        super(context);
        mBuildNative = true;
    }

    @Override
    public Dialog selectItem(CharSequence title, CharSequence[] items, OnClickListener listener, OnCancelListener oncancel) {
        String[] sitems = new String[items.length];
        for (int i = 0; i < items.length; i++) {
            sitems[i] = items[i].toString();
        }
        final ActionSheetDialog dialog = new ActionSheetDialog(mContext, sitems, null);
        dialog.title(title.toString())//
                .titleTextSize_SP(14.5f)//
                .show();
        dialog.setOnOperItemClickL((AdapterView<?> parent, View view, int position, long id) -> {
            dialog.dismiss();
            new SafeListener(listener).onClick(dialog, position);
        });
        dialog.setCancelable(oncancel != null);
        dialog.setOnCancelListener(new SafeListener(oncancel));
        return dialog;
    }

//    @Override
//    public Dialog showProgressDialog(CharSequence message, OnCancelListener listener, int textsize) {
//        SweetAlertDialog sweet = new SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE);
//        sweet.setTitleText(message.toString());
//        if (listener != null) {
//            sweet.setCancelable(true);
//            sweet.setOnCancelListener(listener);
//        } else {
//            sweet.setCancelable(false);
//        }
//        sweet.show();
//        return mProgress = sweet;
//    }

    @Override
    public Dialog showDialog(int theme, int iconres, CharSequence title, CharSequence message, CharSequence positive, OnClickListener lpositive, CharSequence negative, OnClickListener lnegative, CharSequence neutral, OnClickListener lneutral) {
        int btnNum = (TextUtils.isEmpty(negative) ? 0 : 1) + (TextUtils.isEmpty(neutral) ? 0 : 1) + (TextUtils.isEmpty(positive) ? 0 : 1);
        CharSequence[] btnTexts = new CharSequence[]{positive, negative, neutral};
        List<String> textList = new ArrayList<>();
        for (int i = 0; i < btnTexts.length && i < btnNum; i++) {
            textList.add(btnTexts[i].toString());
        }

        final NormalDialog dialog = new NormalDialog(mContext);
        dialog.title(title.toString()).content(message.toString())//
                .style(NormalDialog.STYLE_TWO)//
                .btnNum(btnNum)
                .btnText(textList.toArray(new String[btnNum]))//
//                .showAnim(mBasIn)//
//                .dismissAnim(mBasOut)//
                .show();
        SafeOnBtnClickL[] ls = new SafeOnBtnClickL[]{new SafeOnBtnClickL(dialog, lpositive, 0),new SafeOnBtnClickL(dialog, lnegative, 1),new SafeOnBtnClickL(dialog, lneutral, 2)};
        List<SafeOnBtnClickL> clickLs = new ArrayList<>();
        for (int i = 0; i < ls.length && i < btnNum; i++) {
            clickLs.add(ls[i]);
        }

        SafeOnBtnClickL[] array = clickLs.toArray(new SafeOnBtnClickL[btnNum]);
        dialog.setOnBtnClickL((OnBtnClickL[]) array);
        return dialog;
    }


    class SafeOnBtnClickL implements OnBtnClickL {
        private final int index;
        private final NormalDialog dialog;
        private final OnClickListener lnegative;
        public SafeOnBtnClickL(NormalDialog dialog, OnClickListener lnegative, int index) {
            this.dialog = dialog;
            this.index = index;
            this.lnegative = new SafeListener(lnegative);
        }
        @Override
        public void onBtnClick() {
            dialog.dismiss();
            lnegative.onClick(dialog, index);
        }
    }

}
