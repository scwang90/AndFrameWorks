package com.andframe.impl.dialog;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.*;

import com.andframe.$;
import com.andframe.api.DialogBuilder;
import com.andframe.api.dialog.DialogBuilder.OnDateSetVerifyListener;
import com.andframe.api.dialog.DialogBuilder.OnTimeSetVerifyListener;
import com.andframe.api.dialog.DialogFactory;
import com.andframe.feature.AfSoftInput;
import com.andframe.listener.SafeListener;
import com.andframe.util.java.AfReflecter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Stack;

@SuppressWarnings("WeakerAccess")
public class SystemDialogFactory implements DialogFactory {

    private static final String TXT_NO_MORE_SHOW = "不再提示";
    private static final String KEY_FILE = "SystemBuilder.keyDialog";

    @Override
    public Dialog build(Context context, DialogBluePrint entity) {
        if (entity.view != null) {
            return buildView(context, entity);
        }
        if (entity.inputListener != null) {
            return buildInputText(context, entity);
        }
        if (entity.timeListener != null) {
            return buildTime(context, entity);
        }
        if (entity.dateListener != null) {
            return buildDate(context, entity);
        }
        if (entity.monthListener != null) {
            return buildMonth(context, entity);
        }
        if (entity.items != null && (entity.itemsChecked != null || entity.itemsMultiListener != null)) {
            return buildChoice(context, entity);
        }
        if (entity.items != null && entity.itemsListener != null ) {
            return buildItems(context, entity);
        }
        return buildAlert(context, entity);
    }

    public Dialog buildTime(Context context, DialogBluePrint entity) {
        Calendar calendar = Calendar.getInstance();
        if (entity.time != null) {
            calendar.setTime(entity.time);
        }
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog.OnTimeSetListener timeListener = new SafeListener(entity.timeListener);
        TimePickerDialog timerDialog = new TimePickerDialog(context, timeListener, hour, minute, true);
        if (entity.timeListener instanceof OnTimeSetVerifyListener) {
            OnTimeSetVerifyListener verifyListener = (OnTimeSetVerifyListener) entity.timeListener;
            timerDialog.setButton(DialogInterface.BUTTON_POSITIVE, context.getString(android.R.string.ok), new SafeListener((dialog, which) -> {
                TimePicker picker = AfReflecter.getMemberByTypeNoException(dialog, TimePicker.class);
                if (picker == null) {
                    timerDialog.onClick(dialog, which);
                } else if (verifyListener.onPreTimeSet(picker, picker.getCurrentHour(), picker.getCurrentMinute())) {
                    timerDialog.onClick(dialog, which);
                }
            }));
        }
        return initDialogCommon(timerDialog, entity);
    }

    public Dialog buildDate(Context context, DialogBluePrint entity) {
        Calendar calendar = Calendar.getInstance();
        if (entity.date != null) {
            calendar.setTime(entity.date);
        }
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog.OnDateSetListener dateListener = new SafeListener(entity.dateListener);
        DatePickerDialog dateDialog = new DatePickerDialog(context, dateListener, year, month, day);
        if (entity.dateListener instanceof OnTimeSetVerifyListener) {
            OnDateSetVerifyListener verifyListener = (OnDateSetVerifyListener) entity.dateListener;
            dateDialog.setButton(DialogInterface.BUTTON_POSITIVE, context.getString(android.R.string.ok), new SafeListener((dialog, which) -> {
                DatePicker picker = AfReflecter.getMemberByTypeNoException(dialog, DatePicker.class);
                if (picker == null) {
                    dateDialog.onClick(dialog, which);
                } else if (verifyListener.onPreDateSet(picker, picker.getYear(), picker.getMonth(), picker.getDayOfMonth())) {
                    dateDialog.onClick(dialog, which);
                }
            }));
        }
        return initDialogCommon(dateDialog, entity);
    }

    public Dialog buildMonth(Context context, DialogBluePrint entity) {
        Calendar calendar = Calendar.getInstance();
        if (entity.month != null) {
            calendar.setTime(entity.month);
        }

        TextView txtYear = new TextView(context);
        TextView txtMonth = new TextView(context);
        NumberPicker yearPicker = new NumberPicker(context);
        NumberPicker monthPicker = new NumberPicker(context);
        
        yearPicker.setMinValue(1000);
        yearPicker.setMaxValue(9999);
        monthPicker.setMinValue(1);
        monthPicker.setMaxValue(12);
        yearPicker.setValue(calendar.get(Calendar.YEAR));
        monthPicker.setValue(calendar.get(Calendar.MONTH) + 1);
        txtYear.setText("年");
        txtMonth.setText("月");

        entity.view =$.query(new LinearLayout(context))
                .addView(yearPicker).addView(txtYear)
                .addView(monthPicker).addView(txtMonth)
                .gravity(Gravity.CENTER).view();

        Dialog dialogView = buildView(context, entity);

        AlertDialog monthDialog;
        if ((dialogView instanceof AlertDialog)) {
            monthDialog = (AlertDialog) dialogView;
        } else {
            AlertDialog.Builder builder = buildSystemBase(context, entity);
            builder.setView(entity.view);
            initDialogCommon(monthDialog = builder.create(), entity);
        }

        if (entity.cancelable) {
            monthDialog.setButton(DialogInterface.BUTTON_NEGATIVE, context.getString(android.R.string.cancel), (DialogInterface.OnClickListener) null);
        }
        monthDialog.setButton(DialogInterface.BUTTON_POSITIVE, context.getString(android.R.string.ok), new SafeListener((dialog, which) -> {
            int year = yearPicker.getValue();
            int month = monthPicker.getValue() - 1;
            DatePicker picker = new DatePicker(context);
            picker.init(year, month, 1, null);
            if (entity.monthListener instanceof OnDateSetVerifyListener) {
                OnDateSetVerifyListener verifyListener = (OnDateSetVerifyListener) entity.monthListener;
                if (verifyListener.onPreDateSet(picker, picker.getYear(), picker.getMonth(), picker.getDayOfMonth())) {
                    verifyListener.onDateSet(picker, picker.getYear(), picker.getMonth(), picker.getDayOfMonth());
                    dialog.dismiss();
                }
            } else {
                entity.monthListener.onDateSet(picker, picker.getYear(), picker.getMonth(), picker.getDayOfMonth());
                dialog.dismiss();
            }

        }));

        return monthDialog;
    }

    public Dialog buildChoice(Context context, DialogBluePrint entity) {
        boolean[] checkeds = entity.itemsChecked;
        if (checkeds == null) {
            checkeds = new boolean[entity.items.length];
        }
        AlertDialog.Builder builder = buildSystemBase(context, entity);
        builder.setMultiChoiceItems(entity.items, checkeds, new SafeListener(entity.itemsMultiListener));
        builder.setNegativeButton("确定", new SafeListener(entity.itemsListener));
        return initDialogCommon(builder.create(), entity);
    }

    public Dialog buildItems(Context context, DialogBluePrint entity) {
        AlertDialog.Builder builder = buildSystemBase(context, entity);
        builder.setItems(entity.items, new SafeListener(entity.itemsListener));
        return initDialogCommon(builder.create(), entity);
    }

    public Dialog buildAlert(Context context, DialogBluePrint entity) {
        if (buildKayDialogCache(context, entity)) {
            return null;
        }
        AlertDialog.Builder builder = buildSystemBase(context, entity);
        builder.setMessage(entity.message);
        buildSystemButtons(builder, context, entity);
        Dialog dialog = builder.create();
        return buildKayDialogIfNeed(dialog, context, entity);
    }

    public Dialog buildView(Context context, DialogBluePrint entity) {
        if (buildKayDialogCache(context, entity)) {
            return null;
        }
        if (!entity.buildNative) {
            entity.message = "$inputText$";
            final Dialog dialog = buildAlert(context, entity);
            if (dialog != null) {
                $.dispatch(entity.builderDelayed, new Runnable() {
                    private long startTime = System.currentTimeMillis();
                    @Override
                    public void run() {
                        long now = System.currentTimeMillis();
                        Window window = dialog.getWindow();
                        if (window != null) {
                            FindTextViewWithText help = FindTextViewWithText.invoke((ViewGroup) window.getDecorView(), entity.message);
                            if (help != null) {
                                help.parent.removeViewAt(help.index);
                                help.parent.addView(entity.view, help.index, help.textView.getLayoutParams());
                            } else if (now - startTime < entity.builderTimeout) {
                                $.dispatch(entity.builderDelayed, this);
                            }
                        } else if (now - startTime < entity.builderTimeout) {
                            $.dispatch(entity.builderDelayed, this);
                        }
                    }
                });
            }
            return dialog;
        }
        AlertDialog.Builder builder = buildSystemBase(context, entity);
        builder.setView(entity.view);
        buildSystemButtons(builder, context, entity);
        return initDialogCommon(builder.create(), entity);
    }

    public boolean buildKayDialogCache(Context context, DialogBluePrint entity) {
        if (entity.key != null) {
            final String key = TextUtils.isEmpty(entity.key) ? String.valueOf(TextUtils.concat(entity.title, entity.message).hashCode()) : entity.key.toString();
            int click = $.cache(KEY_FILE).getInt(key, -2);
            if (click > -2) {
                if (entity.keyButtonIndex > -1) {
                    click = entity.keyButtonIndex;
                }
                if (click > -1 && click < entity.buttons.size()) {
                    new SafeListener(entity.buttons.get(click).listener).onClick(null, click);
                }
                return true;
            }
        }
        return false;
    }

    public Dialog buildKayDialogIfNeed(Dialog dialog, Context context, DialogBluePrint entity) {
        if (dialog != null && entity.key != null) {
            final Window window = dialog.getWindow();
            if (window == null) {
                return dialog;
            }
            final View decor = window.getDecorView();
            decor.postDelayed(() -> {
                CheckBox cbTip = null;
                FindTextViewWithText builderHelper = FindTextViewWithText.invoke((ViewGroup) decor, entity.message);
                if (builderHelper != null) {
                    int index = builderHelper.index;
                    ViewGroup parent = builderHelper.parent;
                    TextView textView = builderHelper.textView;
                    parent.removeView(textView);
                    LinearLayout ll = new LinearLayout(context);
                    ll.setOrientation(LinearLayout.VERTICAL);
                    LinearLayout.LayoutParams lp;
                    ViewGroup.LayoutParams olp = textView.getLayoutParams();
                    if (olp instanceof ViewGroup.MarginLayoutParams) {
                        lp = new LinearLayout.LayoutParams((ViewGroup.MarginLayoutParams)olp);
                    } else {
                        lp = new LinearLayout.LayoutParams(olp);
                    }
                    ll.setPadding(textView.getPaddingLeft(),textView.getPaddingTop(),textView.getPaddingRight(),textView.getPaddingBottom());
                    textView.setPadding(0,0,0,0);
                    ll.addView(textView, lp);
                    cbTip = new CheckBox(context);
                    cbTip.setText(TXT_NO_MORE_SHOW);
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
                            $.cache(KEY_FILE).put(entity.key+"", entity.keyButtonIndex);
                        }
                    });
                }
            }, 0);
        }
        return initDialogCommon(dialog, entity);
    }

    //<editor-fold desc="build system">

    private void buildSystemButtons(AlertDialog.Builder builder, Context context, DialogBluePrint entity) {
        if (entity.buttons == null || entity.buttons.size() == 0) {
            builder.setPositiveButton(context.getString(android.R.string.ok), null);
        } else {
            ButtonEntity button = entity.buttons.get(0);
            builder.setPositiveButton(button.text, new SafeListener(button.listener) {
                @Override
                public void onClick(View v) {
                    if (entity.keyButtonIndex < 0) {
                        entity.keyButtonIndex = 0;
                    }
                    super.onClick(v);
                }
            });
        }
        if (entity.buttons != null && entity.buttons.size() > 1) {
            ButtonEntity button = entity.buttons.get(1);
            builder.setNegativeButton(button.text, new SafeListener(button.listener) {
                @Override
                public void onClick(View v) {
                    if (entity.keyButtonIndex < 0) {
                        entity.keyButtonIndex = 1;
                    }
                    super.onClick(v);
                }
            });
        }
        if (entity.buttons != null && entity.buttons.size() > 2) {
            ButtonEntity button = entity.buttons.get(2);
            builder.setNeutralButton(button.text, new SafeListener(button.listener) {
                @Override
                public void onClick(View v) {
                    if (entity.keyButtonIndex < 0) {
                        entity.keyButtonIndex = 2;
                    }
                    super.onClick(v);
                }
            });
        }
    }

    private AlertDialog.Builder buildSystemBase(Context context, DialogBluePrint entity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (!TextUtils.isEmpty(entity.title)) {
            builder.setTitle(entity.title);
        }
        builder.setCancelable(entity.cancelable);
        builder.setOnCancelListener(new SafeListener(entity.cancelListener));
        return builder;
    }

    private Dialog initDialogCommon(Dialog dialog, DialogBluePrint entity) {
        if (!TextUtils.isEmpty(entity.title)) {
            dialog.setTitle(entity.title);
        }
        dialog.setCancelable(entity.cancelable);
        dialog.setCanceledOnTouchOutside(entity.cancelable);
        dialog.setOnCancelListener(new SafeListener(entity.cancelListener));
        dialog.setOnDismissListener(new SafeListener(entity.dismissListener));
        return dialog;
    }

    //</editor-fold>

    public Dialog buildInputText(Context context, DialogBluePrint entity) {
        final EditText input = new EditText(context);
        final int defaultLength = entity.inputText != null ? entity.inputText.length() : 0;
        input.setText(entity.inputText);
        input.setHint(entity.inputHint);
        input.clearFocus();
        if (entity.inputType > 0) {
            input.setInputType(entity.inputType);
        }

        final CharSequence oKey = "确定";
        final CharSequence msgKey = "$inputText$";

        DialogInterface.OnClickListener cancelListener = (dialog, which) -> {
            AfSoftInput.hideSoftInput(input);
            if (entity.inputListener instanceof DialogBuilder.InputTextCancelable) {
                ((DialogBuilder.InputTextCancelable) entity.inputListener).onInputTextCancel(input);
            }
            if (dialog != null) {
                dialog.dismiss();
            }
        };
        final DialogInterface.OnClickListener okListener = (dialog, which) -> {
            if (entity.inputListener.onInputTextConfirm(input, input.getText().toString())) {
                AfSoftInput.hideSoftInput(input);
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        };
        final DialogInterface.OnShowListener showListener = dialog -> {
            AfSoftInput.showSoftInput(input);
            if (defaultLength > 3 && entity.inputText.toString().matches("[^.]+\\.[a-zA-Z]\\w{1,3}")) {
                input.setSelection(0, entity.inputText.toString().lastIndexOf('.'));
            } else {
                input.setSelection(0, defaultLength);
            }
        };

        if (entity.buildNative) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setView(input);
            builder.setCancelable(false);
            builder.setTitle(entity.title);
            builder.setPositiveButton("确定", new SafeListener());
            builder.setNegativeButton("取消", cancelListener);
            final AlertDialog dialog = builder.create();
            dialog.setOnShowListener(o -> {
                showListener.onShow(dialog);
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> new SafeListener(okListener).onClick(dialog, 0));
            });
            return dialog;
        } else {
            entity.buttons = entity.buttons == null ? new ArrayList<>(2) : entity.buttons;
            ButtonEntity positive = new ButtonEntity(oKey,0, new SafeListener());
            ButtonEntity negative = new ButtonEntity("取消",0, cancelListener);
            entity.buttons.add(positive);
            entity.buttons.add(negative);
            entity.message = msgKey;
            entity.cancelable = false;

            final Dialog dialog = buildAlert(context, entity);
            dialog.setOnShowListener(o -> {
                Window window = dialog.getWindow();
                if (window != null) {
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
                }
            });

//            new Handler(Looper.getMainLooper()).postDelayed(() -> {
//                Window window = dialog.getWindow();
//                if (window != null) {
//                    FindTextViewWithText builderHelper = FindTextViewWithText.invoke((ViewGroup) dialog.getWindow().getDecorView(), msgKey);
//                    if (builderHelper != null) {
//                        builderHelper.parent.removeViewAt(builderHelper.index);
//                        builderHelper.parent.addView(input,builderHelper.index,builderHelper.textView.getLayoutParams());
//                        showListener.onShow(dialog);
//                        builderHelper = FindTextViewWithText.invoke((ViewGroup) dialog.getWindow().getDecorView(), oKey);
//                        if (builderHelper != null) {
//                            builderHelper.textView.setOnClickListener(v -> new SafeListener(okListener).onClick(dialog, 0));
//                        }
//                    }
//                }
//            }, entity.builderDelayed == 0 ? 10 : entity.builderDelayed);
            return dialog;
        }
    }

    //<editor-fold desc="内部类">
    /**
     * 根据 文本信息 匹配在布局中找出 相匹配的 TextView
     */
    protected static class FindTextViewWithText {
        public int index;
        public TextView textView;
        public ViewGroup parent;

        public static FindTextViewWithText invoke(ViewGroup root, CharSequence text) {
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
