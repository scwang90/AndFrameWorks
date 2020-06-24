package com.andframe.impl.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andframe.$;
import com.andframe.api.DialogBuilder;
import com.andframe.api.dialog.DialogFactory;
import com.andframe.feature.AfSoftInput;
import com.andframe.listener.SafeListener;
import com.andframe.util.java.AfReflecter;

import java.util.ArrayList;
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
        if (entity.itemsListener != null && entity.items != null) {
            return buildSelectItems(context, entity);
        }
        if (entity.inputListener != null) {
            return buildInputText(context, entity);
        }
        return buildAlert(context, entity);
    }

    public Dialog buildSelectItems(Context context, DialogBluePrint entity) {
        AlertDialog.Builder builder = buildSystemBase(context, entity);
        builder.setItems(entity.items, new SafeListener(entity.itemsListener));
        return builder.create();
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
        return builder.create();
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
        return dialog;
    }

    //<editor-fold desc="build system">

    private void buildSystemButtons(AlertDialog.Builder builder, Context context, DialogBluePrint entity) {
        if (entity.buttons == null || entity.buttons.size() == 0) {
            builder.setPositiveButton("确定", null);
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
            ButtonEntity positive = new ButtonEntity(oKey, new SafeListener());
            ButtonEntity negative = new ButtonEntity("取消", cancelListener);
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
