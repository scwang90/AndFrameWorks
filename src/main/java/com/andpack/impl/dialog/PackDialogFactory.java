package com.andpack.impl.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;

import com.andframe.impl.dialog.ButtonEntity;
import com.andframe.impl.dialog.DialogBluePrint;
import com.andframe.impl.dialog.SystemDialogFactory;
import com.andframe.listener.SafeListener;
import com.andpack.$;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.flyco.dialog.widget.NormalDialog;

import java.util.Collections;

public class PackDialogFactory extends SystemDialogFactory {

    @Override
    public Dialog buildAlert(Context context, DialogBluePrint entity) {

        if (buildKayDialogCache(context, entity)) {
            return null;
        }

        String message = entity.message + "";
        if (entity.buttons == null || entity.buttons.size() == 0) {
            String ok = context.getString(android.R.string.ok);
            entity.buttons = Collections.singletonList(new ButtonEntity(ok,0, null));
        }
        int[] buttonColors = new int[entity.buttons.size()];
        for (int i = 0; i < entity.buttons.size() ; i++) {
            buttonColors[i] = entity.buttons.get(i).color;
            if (buttonColors[i] == 0) {
                buttonColors[i] = 0xff333333;
            }
        }

        final NormalDialog dialog = new NormalDialog(context);
        if (!TextUtils.isEmpty(entity.title)) {
            dialog.title(entity.title + "");
        }
        dialog.content(message)//
            .style(NormalDialog.STYLE_TWO)//
            .btnNum(entity.buttons.size())
            .contentGravity(message.contains("\r") ? Gravity.START : Gravity.CENTER)
            .btnText($.query(entity.buttons).map(m -> m.text + "").toArrays())
                .btnTextColor(buttonColors)
            .setOnBtnClickL($.query(entity.buttons).mapIndexed((i, m) -> new SafeOnBtnClickL(dialog, m.listener, i)).toArrays());

        dialog.setCancelable(entity.cancelable);
        return buildKayDialogIfNeed(dialog, context, entity);
    }

    @Override
    public Dialog buildItems(Context context, DialogBluePrint entity) {
        String[] items = $.query(entity.items).map(CharSequence::toString).toArrays();
        final ActionSheetDialog dialog = new ActionSheetDialog(context, items, null);
        dialog.title(entity.title + "").titleTextSize_SP(14.5f);
        dialog.setOnOperItemClickL((AdapterView<?> parent, View view, int position, long id) -> {
            dialog.dismiss();
            new SafeListener(entity.itemsListener).onClick(dialog, position);
        });
        dialog.setCancelable(entity.cancelable);
        dialog.setOnCancelListener(new SafeListener(entity.cancelListener));
        return dialog;
    }

    static class SafeOnBtnClickL implements OnBtnClickL {
        private final int index;
        private final NormalDialog dialog;
        private final DialogInterface.OnClickListener listener;
        SafeOnBtnClickL(NormalDialog dialog, DialogInterface.OnClickListener listener, int index) {
            this.dialog = dialog;
            this.index = index;
            this.listener = new SafeListener(listener);
        }
        @Override
        public void onBtnClick() {
            dialog.dismiss();
            listener.onClick(dialog, index);
        }
    }

}
