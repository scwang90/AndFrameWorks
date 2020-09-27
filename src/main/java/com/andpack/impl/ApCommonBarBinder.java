package com.andpack.impl;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;

import com.andframe.$;
import com.andframe.api.Cacher;
import com.andframe.api.DialogBuilder;
import com.andframe.api.dialog.DialogBuilder.OnDateSetVerifyListener;
import com.andframe.api.dialog.DialogBuilder.OnDateTimeSetVerifyListener;
import com.andframe.api.dialog.DialogBuilder.OnTimeSetVerifyListener;
import com.andframe.api.pager.Pager;
import com.andframe.api.task.builder.Builder;
import com.andframe.api.task.builder.WaitBuilder;
import com.andframe.api.query.ViewQuery;
import com.andframe.api.viewer.Viewer;
import com.andframe.feature.AfIntent;
import com.andframe.listener.SafeListener;
import com.andframe.task.Dispatcher;
import com.andframe.util.java.AfDateFormat;
import com.andpack.R;
import com.andpack.api.ApPager;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.scwang.smartrefresh.layout.util.SmartUtil;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.ImageEngine;

import java.io.File;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public class ApCommonBarBinder {

    //<editor-fold desc="接口定义">
    public interface ClickHook {
        /**
         * @return true 将会拦截点击事件 false 不会拦截
         */
        boolean onBinderClick(Binder binder);
    }

    public interface TaskBuilder<T> {
        Builder builder(T value);
    }

    public interface Bind<T> {

    }

    public interface BindData<T> {
        T bind();
    }

    public interface CommonBind<T> extends Bind<T> {
        void bind(@NonNull Binder binder, @NonNull T value);
    }

    public interface SelectBind extends Bind<Void>  {
        void text(@NonNull Binder binder, String text, int which);
    }

    public interface MultiSelectBind extends Bind<Void>  {
        void text(@NonNull Binder binder, String text, int count, boolean[] checkedItems);
    }

    public interface SeekBind extends Bind<Integer>  {
        void seek(@NonNull Binder binder, @NonNull Integer value, boolean fromUser);
    }

    public interface RadioGroupBind extends Bind<Integer> {
        void onBind(@NonNull Binder binder, RadioGroup group, @IdRes int checkedId, @NonNull Integer index);
    }

    public interface TextVerify {
        String verify(String text) throws VerifyException;
    }

    public interface DateVerify {
        void verify(Date date, boolean verifyDateOnly) throws VerifyException;
    }

    public interface MultiSelectVerify {
        void verify(int count, boolean[] checkedItems) throws VerifyException;
    }

    public static class VerifyException extends Exception {
        public VerifyException(String message) {
            super(message);
        }
    }
    //</editor-fold>

    private Viewer viewer;
    private String hintPrefix = "";
    private Cacher cacher;
    private ViewQuery<? extends ViewQuery> $$;
    private boolean smart = false;
    private boolean readOnly = false;
    private List<Binder> binders = new ArrayList<>();

    public List<Binder> binders() {
        return new ArrayList<>(binders);
    }

    public ApCommonBarBinder(Viewer viewer) {
        this.viewer = viewer;
        this.$$ = $.query(viewer);
        this.cacher = $.cache(viewer.getClass().getName());
    }

    public ApCommonBarBinder setHintPrefix(String hintPrefix) {
        this.hintPrefix = hintPrefix;
        return this;
    }

    /**
     * 智能获取与绑定
     * 如果，CommonBar 的界面定义符合标准定义如下：
     * 1.左边是 TextView 并且文本为 name 如 姓名
     * 2.右边是 箭头图标、表示可点击
     * 智能操作：
     * 1.自动从左边的 TextView 获取 name，并智能设置 hint
     * 2.自动绑定 parentView 的点击事件为触发事件
     */
    public ApCommonBarBinder setSmart(boolean smart) {
        this.smart = smart;
        return this;
    }

    public ApCommonBarBinder setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
        return this;
    }


    public ViewQuery<? extends ViewQuery> $(Integer id, int... ids) {
        return $$.query(id, ids);
    }
    public ViewQuery<? extends ViewQuery> $(View... views) {
        return $$.with(views);
    }

    public TextBinder text(@IdRes int idValue) {
        TextBinder binder = new TextBinder(idValue);
        binders.add(binder);
        return binder;
    }

    public TextBinder textLines(@IdRes int idValue) {
        TextLinesBinder binder = new TextLinesBinder(idValue);
        binders.add(binder);
        return binder;
    }

    public SelectNumber number(@IdRes int idValue) {
        SelectNumber binder = new SelectNumber(idValue);
        binders.add(binder);
        return binder;
    }

    public SelectFloat numberFloat(@IdRes int idValue) {
        SelectFloat binder = new SelectFloat(idValue);
        binders.add(binder);
        return binder;
    }

    public SelectNumberPicker numberPicker(@IdRes int idValue) {
        SelectNumberPicker binder = new SelectNumberPicker(idValue);
        binders.add(binder);
        return binder;
    }

    public InputBinder input(@IdRes int idValue) {
        InputBinder binder = new InputBinder(idValue);
        binders.add(binder);
        return binder;
    }

    public SelectBinder select(@IdRes int idValue, CharSequence... items) {
        SelectBinder binder = new SelectBinder(idValue, items);
        binders.add(binder);
        return binder;
    }

    public SelectBinder select(@IdRes int idValue, BindData<CharSequence[]> items) {
        SelectBinder binder = new SelectBinder(idValue, items);
        binders.add(binder);
        return binder;
    }

    public CheckBinder check(@IdRes int idValue) {
        CheckBinder binder = new CheckBinder(idValue);
        binders.add(binder);
        return binder;
    }

    public SwitchBinder switcher(@IdRes int idValue) {
        SwitchBinder binder = new SwitchBinder(idValue);
        binders.add(binder);
        return binder;
    }

    public SeekBarBinder seek(@IdRes int idValue) {
        SeekBarBinder binder = new SeekBarBinder(idValue);
        binders.add(binder);
        return binder;
    }

    public DateBinder date(@IdRes int idValue) {
        DateBinder binder = new DateBinder(idValue);
        binders.add(binder);
        return binder;
    }

    public MonthBinder month(@IdRes int idValue) {
        MonthBinder binder = new MonthBinder(idValue);
        binders.add(binder);
        return binder;
    }

    public TimeBinder time(@IdRes int idValue) {
        TimeBinder binder = new TimeBinder(idValue);
        binders.add(binder);
        return binder;
    }

    public DateTimeBinder datetime(@IdRes int idValue) {
        DateTimeBinder binder = new DateTimeBinder(idValue);
        binders.add(binder);
        return binder;
    }

    public MultiSelectBinder multiSelect(@IdRes int idValue, CharSequence[] items) {
        MultiSelectBinder binder = new MultiSelectBinder(idValue, items);
        binders.add(binder);
        return binder;
    }

    public ActivityBinder activity(@IdRes int idValue, Class<? extends Activity> clazz, Object... args) {
        ActivityBinder binder = new ActivityBinder(idValue, clazz, args);
        binders.add(binder);
        return binder;
    }

    public FragmentBinder fragment(@IdRes int idValue, Class<? extends Fragment> clazz, Object... args) {
        FragmentBinder binder = new FragmentBinder(idValue, clazz, args);
        binders.add(binder);
        return binder;
    }

    public ImageBinder image(@IdRes int idImage) {
        return new ImageBinder(idImage);
    }

    public RadioGroupBinder radioGroup(int id) {
        return new RadioGroupBinder(id);
    }

    @SuppressWarnings("WeakerAccess")
    public abstract class Binder<T extends Binder, BT extends Bind<VT>, VT> implements View.OnClickListener{
        protected int idValue;
        protected String key = null;
        protected CharSequence hintPrefix = ApCommonBarBinder.this.hintPrefix;
        protected CharSequence hint = hintPrefix;
        protected CharSequence name = "";
        protected CharSequence actionTitle = "";

        protected Binder next;
        protected VT lastValue;
        protected BT bind;
        protected Runnable start;
        protected Runnable action;
        protected ClickHook clickHook;
        protected TaskBuilder<VT> taskBuilder;
        protected boolean readOnly = ApCommonBarBinder.this.readOnly;

        Binder(int idValue) {
            this.idValue = idValue;
            if (!readOnly) {
                $(idValue).clicked(this);
            }
        }

        public int idValue() {
            return idValue;
        }

        public boolean readOnly() {
            return readOnly;
        }

        public T readOnly(boolean value) {
            readOnly = value;
            return self();
        }

        public ViewQuery<? extends ViewQuery> query() {
            return $(idValue);
        }

        /**
         * 添加点击事件绑定
         */
        public T click(int idClick) {
            return click($(idClick).view());
        }

        /**
         * 添加点击事件绑定
         */
        public T click(View view) {
            if (!readOnly) {
                $(view).clicked(this);
                $(idValue).clicked(null).clickable(false);
            }
            return self();
        }

        public T clickHook(ClickHook clickHook) {
            this.clickHook = clickHook;
            return self();
        }

        public T bind(BT bind) {
            if (smart) {
                View prev = $(idValue).toPrev().view();
                View next = $(idValue).toNext().view();
                if(prev instanceof TextView
                        && (next instanceof ImageView
                        || $(idValue).drawableRight() != null
                        || (next != null && View.class.equals(next.getClass()))
                        || $(idValue).view() instanceof Button)) {
                    if ("".contentEquals(name) && hintPrefix.equals(hint)) {
                        this.name = $(prev).text();
                        hint(hintPrefix + name.toString());
                    }
                    if ($(idValue).clickable()) {
                        click($(idValue).toParent().view());
//                        $(idValue).clicked(null).clickable(false).toParent().clicked(this);
                    }
                }
            }
            this.bind = bind;
            return self();
        }

        //<editor-fold desc="提示信息">
        CharSequence getName(String dname, String[] names) {
            return names.length > 0 ? names[0] : (TextUtils.isEmpty(name) ? dname : name);
        }

        public T hintPrefix(CharSequence hintPrefix) {
            this.hintPrefix = hintPrefix;
            return hint(hintPrefix + name.toString());
        }

        public T name(CharSequence name) {
            this.name = name;
            return hint(hintPrefix + name.toString());
        }

        public T name(@StringRes int id) {
            this.name = viewer.getContext().getString(id);
            return hint(hintPrefix + name.toString());
        }

        public T nameTxtId(@IdRes int id) {
            this.name = $(id).text();
            return hint(hintPrefix + name.toString());
        }

        public T hint(CharSequence hint) {
            this.hint = hint;
            return self();
        }

        public T hintResId(@StringRes int id) {
            this.hint = viewer.getContext().getString(id);
            return self();
        }
        //</editor-fold>

        public T cache(Object... keys) {
            if (keys.length == 0) {
                key = String.valueOf(idValue);
            } else {
                key = String.valueOf(keys[0]);
            }
            onRestoreCache(key);
            return self();
        }

        @SuppressWarnings("unused")
        public void onRestoreCache(String key) {

        }

        @SuppressWarnings("unused")
        public <Next extends Binder> Next next(Next next) {
            this.next = next;
            return next;
        }

        @Override
        public void onClick(View v) {
            if (!readOnly) {
                performStart();
            }
        }

        private void performStart() {
            if (clickHook != null && clickHook.onBinderClick(this)) {
                return;
            }
            if (start == null) {
                start();
            } else {
                start.run();
            }
            if (next != null) {
                next.performStart();
            }
        }

        public T start(Runnable start) {
            this.start = start;
            return self();
        }

        public void action(@NonNull String title, @NonNull Runnable action) {
            this.action = action;
            this.actionTitle = title;
        }

        public void task(TaskBuilder<VT> builder) {
            taskBuilder = builder;
        }

        protected abstract void start();

        T self() {
            //noinspection unchecked
            return (T)this;
        }
    }

    public class SelectBinder extends Binder<SelectBinder, SelectBind, Void> implements DialogInterface.OnClickListener {

        private final BindData<CharSequence[]> itemsHandler;

        SelectBinder(int idValue, CharSequence... items) {
            this(idValue, () -> items);
        }

        SelectBinder(int idValue, BindData<CharSequence[]> items) {
            super(idValue);
            this.itemsHandler = items;
            if (TextUtils.isEmpty(hintPrefix)) {
                this.hintPrefix("请选择");
            }
        }

        @Override
        public void start() {
            $.dialog(viewer).selectItem(hint, itemsHandler.bind(), this);
        }

        @Override
        public void onRestoreCache(String key) {
            int i = cacher.getInt(key, -1);
            value(i);
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            CharSequence[] items = itemsHandler.bind();
            $(idValue).text(items[which]);
            if (key != null && dialog != null) {
                cacher.put(key, which);
            }
            if (this.bind != null) {
                this.bind.text(this, items[which].toString(), which);
            }
        }

        public SelectBinder value(Integer index) {
            CharSequence[] items = itemsHandler.bind();
            if (index != null && index >= 0 && index < items.length) {
                onClick(null, index);
            }
            return self();
        }

        public SelectBinder value(CharSequence value) {
            CharSequence[] items = itemsHandler.bind();
            for (int i = 0; i < items.length; i++) {
                if (TextUtils.equals(items[i].toString(), value)) {
                    onClick(null, i);
                }
            }
            return self();
        }
    }

    public class SelectNumber extends Binder<SelectNumber, CommonBind<Integer>, Integer> {

        private int minValue = 0;
        private int maxValue = 1000;
        private String unit;
        private NumberPicker.Formatter formatter;

        public SelectNumber(int idValue) {
            super(idValue);
            this.hintPrefix("请选择");
        }

        @Override
        protected void start() {
            TextView textview = new TextView(viewer.getContext());
            NumberPicker picker = new NumberPicker(viewer.getContext());
            picker.setMinValue(minValue);
            picker.setMaxValue(maxValue);
            if (lastValue != null) {
                picker.setValue(lastValue);
            }
            picker.setFormatter(formatter);
            textview.setText(TextUtils.isEmpty(unit) ? "" : unit);
            View view = $(new LinearLayout(viewer.getContext()))
                    .addView(picker).addView(textview).gravity(Gravity.CENTER).view();
            DialogInterface.OnClickListener click = TextUtils.isEmpty(actionTitle) ? null : (dialog, which) -> {
                if (action != null) {
                    action.run();
                }
            };
            //$.dialog(viewer).showViewDialog(hint, view, "取消", null, "确定", (d, i) -> onNumberSelected(picker, picker.getValue()), actionTitle, click);
            $.dialog(viewer).builder().title(hint).view(view)
                    .button("取消").button(actionTitle, click)
                    .button("确定", (d, i) -> onNumberSelected(picker, picker.getValue()))
                    .show();
        }

        public SelectNumber formatter(NumberPicker.Formatter formatter) {
            this.formatter = formatter;
            return self();
        }

        public SelectNumber value(Integer value) {
            if (value != null && value >= minValue && value <= maxValue) {
                onNumberSelected(null, value);
            }
            return self();
        }

        public SelectNumber unit(String unit) {
            this.unit = unit;
            return self();
        }

        public SelectNumber range(int min, int max) {
            minValue = Math.min(min,max);
            maxValue = Math.max(min,max);
            lastValue = lastValue == null ? minValue : lastValue;
            return self();
        }

        public SelectNumber rangeAge() {
            minValue = 14;
            maxValue = 100;
            lastValue = lastValue == null ? minValue : lastValue;
            return self();
        }

        private void onNumberSelected(NumberPicker picker, int value) {
            lastValue = value;
            $(idValue).text(String.valueOf(value)+(TextUtils.isEmpty(unit) ? "" : unit));
            if (key != null && picker != null) {
                cacher.put(key, value);
            }
            if (bind != null) {
                bind.bind(this, value);
            }
        }
    }

    public class SelectFloat extends Binder<SelectFloat, CommonBind<Float>, Float> {

        private float minValue = 0f;
        private float maxValue = 1000f;
        private int partInteger = 0;
        private int partDecimal1 = 0;
        private int partDecimal2 = 0;
        private boolean doublePrecision = false;
        private String unit;

        public SelectFloat(int idValue) {
            super(idValue);
            this.hintPrefix("请选择");
        }

        @Override
        protected void start() {
            TextView textview = new TextView(viewer.getContext());
            TextView textPoint = new TextView(viewer.getContext());
            NumberPicker pickerInteger = new NumberPicker(viewer.getContext());
            NumberPicker pickerDecimal1 = new NumberPicker(viewer.getContext());
            NumberPicker pickerDecimal2 = new NumberPicker(viewer.getContext());
            pickerInteger.setMinValue((int)minValue);
            pickerInteger.setMaxValue((int)maxValue);
            pickerDecimal1.setMinValue(0);
            pickerDecimal2.setMinValue(0);
            pickerDecimal1.setMaxValue(9);
            pickerDecimal2.setMaxValue(9);
            if (lastValue != null) {
                pickerInteger.setValue(lastValue.intValue());
                pickerDecimal1.setValue(Float.valueOf(lastValue * 10).intValue() % 10);
                pickerDecimal2.setValue(Float.valueOf(lastValue * 100).intValue() % 10);
            }
            textPoint.setText(".");
            textPoint.setTextSize(30);
            textview.setText(TextUtils.isEmpty(unit) ? "" : unit);
            View view = $(new LinearLayout(viewer.getContext()))
                    .addView(pickerInteger, 50f, ViewGroup.LayoutParams.WRAP_CONTENT*1f)
                    .addView(textPoint)
                    .addView(pickerDecimal1, 35f, ViewGroup.LayoutParams.WRAP_CONTENT*1f)
                    .addView(pickerDecimal2, 35f, ViewGroup.LayoutParams.WRAP_CONTENT*1f)
                    .addView(textview).gravity(Gravity.CENTER_VERTICAL).view();
            int width = $(textPoint).measure().x;
            $(textPoint).margin(-width / 2);
            if (!doublePrecision) {
                pickerDecimal2.setValue(0);
                pickerDecimal2.setVisibility(View.GONE);
                $(pickerInteger).width(50f);
            }
            NumberPicker.OnValueChangeListener listenerDecimal1 = (picker, oldVal, newVal) -> {
                int value = pickerInteger.getValue();
                if (value == ((int)minValue)) {
                    value = picker.getValue();
                    int minValue = Float.valueOf(this.minValue * 10).intValue() % 10;
                    if (value == minValue) {
                        pickerDecimal2.setMinValue(Float.valueOf(this.minValue * 100).intValue() % 10);
                        pickerDecimal2.setMaxValue(9);
                        return;
                    }
                } else if (value == ((int) maxValue)) {
                    value = picker.getValue();
                    int maxValue = Float.valueOf(this.maxValue * 10).intValue() % 10;
                    if (value == maxValue) {
                        pickerDecimal2.setMinValue(0);
                        pickerDecimal2.setMaxValue(Float.valueOf(this.maxValue * 100).intValue() % 10);
                        return;
                    }
                }
                pickerDecimal2.setMinValue(0);
                pickerDecimal2.setMaxValue(9);
            };
            pickerInteger.setOnValueChangedListener((picker, oldVal, newVal) -> {
                int value = picker.getValue();
                if (value == ((int)minValue)) {
                    int old = pickerDecimal1.getValue();
                    int minValue = Float.valueOf(this.minValue * 10).intValue() % 10;
                    pickerDecimal1.setMinValue(minValue);
                    pickerDecimal1.setMaxValue(9);
                    listenerDecimal1.onValueChange(pickerDecimal1, old, pickerDecimal1.getValue());
                } else if (value == ((int) maxValue)) {
                    int old = pickerDecimal1.getValue();
                    int maxValue = Float.valueOf(this.maxValue * 10).intValue() % 10;
                    pickerDecimal1.setMinValue(0);
                    pickerDecimal1.setMaxValue(maxValue);
                    listenerDecimal1.onValueChange(pickerDecimal1, old, pickerDecimal1.getValue());
                } else {
                    pickerDecimal1.setMinValue(0);
                    pickerDecimal1.setMaxValue(9);
                    pickerDecimal2.setMinValue(0);
                    pickerDecimal2.setMaxValue(9);
                }
            });
            pickerDecimal1.setOnValueChangedListener(listenerDecimal1);
            DialogInterface.OnClickListener click = TextUtils.isEmpty(actionTitle) ? null : (dialog, which) -> {
                if (action != null) {
                    action.run();
                }
            };
            //$.dialog(viewer).showViewDialog(hint, view, "取消", actionTitle, click, "确定", (d, i) -> onNumberSelected(pickerInteger, pickerDecimal1, pickerDecimal2, pickerInteger.getValue(), pickerDecimal1.getValue(), pickerDecimal2.getValue()));
            $.dialog(viewer).builder().title(hint).button("取消").button(actionTitle, click).button("确定", (d, i) -> onNumberSelected(pickerInteger, pickerDecimal1, pickerDecimal2, pickerInteger.getValue(), pickerDecimal1.getValue(), pickerDecimal2.getValue())).show();
        }

        public SelectFloat value(Float value) {
            if (value != null && value >= minValue && value <= maxValue) {
                onNumberSelected(null, null, null,
                        value.intValue(),
                        Float.valueOf(lastValue * 10).intValue() % 10,
                        Float.valueOf(lastValue * 100).intValue() % 10);
            }
            return self();
        }

        public SelectFloat doublePrecision(boolean doublePrecision) {
            this.doublePrecision = doublePrecision;
            return self();
        }

        public SelectFloat unit(String unit) {
            this.unit = unit;
            return self();
        }

        public SelectFloat range(float min, float max) {
            minValue = Math.min(min,max);
            maxValue = Math.max(min,max);
            lastValue = lastValue == null ? minValue : lastValue;
            return self();
        }

        private void onNumberSelected(NumberPicker pickerInteger, NumberPicker pickerDecimal1, NumberPicker pickerDecimal2, int valueInteger, int valueDecimal1, int valueDecimal2) {
            lastValue = (valueInteger * 100 + valueDecimal1 * 10 + valueDecimal2) / 100f;
            $(idValue).text(String.valueOf(lastValue)+(TextUtils.isEmpty(unit) ? "" : unit));
            if (key != null && pickerInteger != null) {
                cacher.put(key, lastValue);
            }
            if (bind != null) {
                bind.bind(this, lastValue);
            }
        }
    }

    public class SelectNumberPicker extends Binder<SelectNumberPicker, CommonBind<Float>, Float> {

        private int accuracyInteger = 3;
        private int accuracyDecimal = 0;
        private String unit;

        public SelectNumberPicker(int idValue) {
            super(idValue);
            this.hintPrefix("请选择");
        }

        public SelectNumberPicker accuracy(int integer, int decimal) {
            this.accuracyInteger = integer;
            this.accuracyDecimal = decimal;
            return self();
        }

        @Override
        protected void start() {
            TextView textview = new TextView(viewer.getContext());
            TextView textPoint = new TextView(viewer.getContext());
            List<NumberPicker> pickers = new ArrayList<>(accuracyDecimal + accuracyInteger);
            while (pickers.size() < accuracyInteger + accuracyDecimal) {
                NumberPicker picker = new NumberPicker(viewer.getContext());
                picker.setMinValue(0);
                picker.setMaxValue(9);
                pickers.add(picker);
                if (lastValue != null) {
                    if (pickers.size() <= accuracyInteger) {
                        picker.setValue(Double.valueOf(lastValue / Math.pow(10, accuracyInteger - pickers.size())).intValue() % 10);
                    } else {
                        picker.setValue(Double.valueOf(lastValue * Math.pow(10, pickers.size() - accuracyInteger)).intValue() % 10);
                    }
                }
            }
            textPoint.setText(".");
            textPoint.setTextSize(30);
            textview.setText(TextUtils.isEmpty(unit) ? "" : unit);
            ViewQuery<? extends ViewQuery> query = $(new LinearLayout(viewer.getContext())).gravity(Gravity.CENTER_VERTICAL);
            for (int i = 0 ; i < accuracyInteger ; i++) {
                query.addView(pickers.get(i), Math.min(200f / pickers.size(), 60f), ViewGroup.LayoutParams.WRAP_CONTENT * 1f);
            }
            if (accuracyDecimal > 0) {
                query.addView(textPoint);
                int width = $(textPoint).measure().x;
                $(textPoint).margin(-width / 2);
                for (int i = 0 ; i < accuracyDecimal ; i++) {
                    query.addView(pickers.get(i + accuracyInteger), Math.min(200f / pickers.size(), 60f), ViewGroup.LayoutParams.WRAP_CONTENT * 1f);
                }
            }
            query.addView(textview);
            DialogInterface.OnClickListener click = TextUtils.isEmpty(actionTitle) ? null : (dialog, which) -> {
                if (action != null) {
                    action.run();
                }
            };
            //$.dialog(viewer).showViewDialog(hint, query.view(), "取消", actionTitle, click, "确定", (d, n) -> {
            $.dialog(viewer).builder().title(hint).view(query.view()).button("取消").button(actionTitle, click).button("确定", (d, n) -> {
                float value = 0f;
                for (int i = 0 ; i < accuracyInteger + accuracyDecimal ; i++) {
                    value += pickers.get(i).getValue() * Math.pow(10, accuracyInteger + accuracyDecimal - i - 1);
                }
//                for (int i = 0 ; i < accuracyInteger ; i++) {
//                    value += pickers.get(i).getValue() * Math.pow(10, accuracyInteger - i - 1);
//                }
                if (accuracyDecimal > 0) {
                    value /= Math.pow(10,accuracyDecimal);
//                    for (int i = 0 ; i < accuracyDecimal ; i++) {
//                        value += pickers.get(i + accuracyInteger).getValue() * Math.pow(10,-i-1);
//                    }
                }
                onNumberSelected(value, pickers);
            }).show();
        }

        public SelectNumberPicker value(Float value) {
            if (value != null && value >= 0) {
                onNumberSelected(value, null);
            }
            return self();
        }

        public SelectNumberPicker unit(String unit) {
            this.unit = unit;
            return self();
        }

        private void onNumberSelected(float value, List<NumberPicker> pickers) {
            lastValue = value;
            $(idValue).text(String.valueOf(lastValue)+(TextUtils.isEmpty(unit) ? "" : unit));
            if (key != null && pickers != null) {
                cacher.put(key, lastValue);
            }
            if (bind != null) {
                bind.bind(this, lastValue);
            }
        }
    }

    public class MultiSelectBinder extends Binder<MultiSelectBinder, MultiSelectBind, Void> implements DialogInterface.OnClickListener {

        private boolean[] checkedItems;
        private CharSequence[] items;
        private MultiSelectVerify verify;

        MultiSelectBinder(int idValue, CharSequence[] items) {
            super(idValue);
            this.items = items;
            this.checkedItems = new boolean[items.length];
            this.hintPrefix("请选择");
        }

        public MultiSelectBinder verify(MultiSelectVerify verify) {
            this.verify = verify;
            return self();
        }

        @Override
        public void start() {
            $.dialog(viewer).multiChoice(hint, items, checkedItems, null, this);
        }

        @Override
        public void onRestoreCache(String key) {
            List<Boolean> list = cacher.getList(key, Boolean.class);
            if (list.size() == checkedItems.length) {
                for (int i = 0; i < checkedItems.length; i++) {
                    checkedItems[i] = list.get(i);
                }
                onClick(null, 0);
            }
        }

        public MultiSelectBinder value(String text) {
            for (int i = 0; i < items.length && text != null; i++) {
                checkedItems[i] = text.contains(items[i]);
                onClick(null, 0);
            }
            return self();
        }

        public MultiSelectBinder value(boolean... checkedItems) {
            if (checkedItems.length == this.checkedItems.length) {
                this.checkedItems = checkedItems;
                onClick(new Dialog(viewer.getContext()), 0);
            }
            return self();
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            int count = 0;
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < items.length; i++) {
                if (checkedItems[i]) {
                    count++;
                    if (builder.length() > 0) {
                        builder.append(',');
                    }
                    builder.append(items[i].toString());
                }
            }
            if (verify != null) {
                try {
                    verify.verify(count, checkedItems);
                } catch (VerifyException e) {
                    $.toaster(viewer).toast(e.getMessage());
                    return;
                }
            }
            $(idValue).text(builder);
            if (key != null && dialog != null) {
                List<Boolean> list = new ArrayList<>(checkedItems.length);
                for (boolean bool : checkedItems) {
                    list.add(bool);
                }
                cacher.putList(key, list);
            }
            if (bind != null) {
                bind.text(this, builder.toString(), count, checkedItems);
            }
        }
    }

    public class InputBinder extends Binder<InputBinder, CommonBind<String>, String> implements TextWatcher {

        InputBinder(int idValue) {
            super(idValue);
            if (readOnly) {
                $(idValue).enabled(false);
            }
        }

        public InputBinder inputType(int type) {
            $(idValue).inputType(type);
            return self();
        }

        public InputBinder bind(CommonBind<String> bind) {
            $(idValue).textChanged(this);
            return super.bind(bind);
        }
        @Override
        protected void start() {

        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }
        @Override
        public void afterTextChanged(Editable s) {
            cacher.put(key, s.toString());
            Dispatcher.dispatch(() -> {
                if (bind != null) {
                    bind.bind(this, s.toString());
                }
            });
        }

        @Override
        public void onRestoreCache(String key) {
            value(cacher.getString(key, ""));
        }

        public InputBinder value(String value) {
            $(idValue).text(value);
            return self();
        }
    }

    public class TextBinder extends Binder<TextBinder, CommonBind<String>, String> implements DialogBuilder.InputTextListener {

        protected List<TextVerify> verify;
        protected String valueSuffix = "";
        protected String valuePrefix = "";
        protected int type = InputType.TYPE_CLASS_TEXT;

        TextBinder(int idValue) {
            super(idValue);
            if (TextUtils.isEmpty(hintPrefix)) {
                this.hintPrefix("请输入");
            }
        }

        @Override
        public void start() {
            //todo 实现 action
            DialogInterface.OnClickListener click = TextUtils.isEmpty(actionTitle) ? null : (dialog, which) -> {
                if (action != null) {
                    action.run();
                }
            };
            $.dialog(viewer).inputText(hint, lastValue == null ? $(idValue).text()
                    .replaceAll("^" + valuePrefix,"")
                    .replaceAll(valueSuffix + "$","") : lastValue, type, this);
        }

        public TextBinder value(Object text) {
            if (text != null && !TextUtils.isEmpty(text.toString())) {
                onInputTextConfirm(null, text.toString());
            }
            return self();
        }

        @Override
        public void onRestoreCache(String key) {
            String text = cacher.getString(key, null);
            if (text != null) {
                onInputTextConfirm(null, text);
            }
        }

        @Override
        public boolean onInputTextConfirm(EditText input, String value) {
            if (verify != null && input != null) {
                try {
                    for (TextVerify verify : this.verify) {
                        value = verify.verify(value);
                    }
                } catch (VerifyException e) {
                    $.toaster(viewer).toast(e.getMessage());
                    return false;
                }
            }
            lastValue = value;
            $(idValue).text(valuePrefix + value + valueSuffix);
            if (key != null && input != null) {
                cacher.put(key, value);
            }
            if (bind != null) {
                bind.bind(this, value);
            }
            return true;
        }

        public TextBinder inputType(int type) {
            this.type = type;
            return self();
        }

        public TextBinder prefix(String valuePrefix) {
            this.valuePrefix = valuePrefix;
            return self();
        }

        public TextBinder suffix(String valueSuffix) {
            this.valueSuffix = valueSuffix;
            return self();
        }

        //<editor-fold desc="输入验证">
        /**
         * 自定义验证规则
         */
        public TextBinder verify(TextVerify verify) {
            if (this.verify == null) {
                this.verify = new ArrayList<>();
            }
            this.verify.add(verify);
            return self();
        }

        public TextBinder verifyMaxByte(int max, String... names) {
            CharSequence name = getName("值", names);
            return this.verify(text -> {
                if (text.getBytes(Charset.forName("gbk")).length > max) {
                    throw new VerifyException(name + "不能超过" + max + "个字符或" + (max / 2) + "个汉字");
                }
                return text;
            });
        }

        public TextBinder verifyMinByte(int min, String... names) {
            CharSequence name = getName("值", names);
            return this.verify(text -> {
                if (text.getBytes(Charset.forName("gbk")).length < min) {
                    throw new VerifyException(name + "不能少于" + min + "个字符或" + (min / 2) + "个汉字");
                }
                return text;
            });
        }

        public TextBinder verifyMaxChinese(int max, String... names) {
            CharSequence name = getName("值", names);
            return this.verify(text -> {
                if (text.getBytes(Charset.forName("gbk")).length > max*2) {
                    throw new VerifyException(name + "不能超过" + max + "个汉字或" + (max * 2) + "个字符");
                }
                return text;
            });
        }

        public TextBinder verifyMinChinese(int min, String... names) {
            CharSequence name = getName("值", names);
            return this.verify(text -> {
                if (text.getBytes(Charset.forName("gbk")).length < min * 2) {
                    throw new VerifyException(name + "不能少于" + min + "个汉字或" + (min * 2) + "个字符");
                }
                return text;
            });
        }

        public TextBinder verifyChinese(String... names) {
            CharSequence name = getName("值", names);
            return this.verify(text -> {
                if (!text.matches("[\\u4E00-\\u9FA5]+")) {
                    throw new VerifyException(name + "只能是中文");
                }
                return text;
            });
        }
        /**
         * 指定不为空
         */
        public TextBinder verifyNoEmpty(String... names) {
            CharSequence name = getName("值", names);
            inputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
            return this.verify(text -> {
                if (TextUtils.isEmpty(text.trim())) {
                    throw new VerifyException(name + "不能为空");
                }
                return text;
            });
        }

        /**
         * 指定为姓名的验证格式
         */
        public TextBinder verifyPersonName(String... names) {
            CharSequence name = getName("姓名", names);
            inputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
            return this.verify(text -> {
                if (TextUtils.isEmpty(text)) {
                    throw new VerifyException(name + "不能为空");
                }
                Pattern numex = Pattern.compile("\\d");
                if (numex.matcher(text).find()) {
                    throw new VerifyException(name + "中不能有数字");
                }
                boolean hasch = Pattern.compile("[\\u4e00-\\u9fa5]").matcher(text).find();
                boolean hasen = Pattern.compile("[a-zA-Z]").matcher(text).find();
                if (hasch && hasen) {
                    throw new VerifyException("中文" + name + "不能有混有英文");
                }
                if (text.getBytes(Charset.forName("gbk")).length > 16) {
                    throw new VerifyException(name + "不能超过8个汉字或16个字符");
                }
                return text;
            });
        }

        /**
         * 指定为手机号码验证格式
         */
        public TextBinder verifyPhone(String... names) {
            CharSequence name = getName("手机号码", names);
            inputType(InputType.TYPE_CLASS_PHONE);
            return this.verify(text -> {
                if (TextUtils.isEmpty(text)) {
                    throw new VerifyException("请输入" + name);
                }
                if (!text.matches("1\\d{10}")) {
                    throw new VerifyException("请输入正确的" + name);
                }
                return text;
            });
        }
        /**
         * 指定为Int
         */
        public TextBinder verifyInt(String... names) {
            CharSequence name = getName("数值", names);
            inputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_SIGNED);
            return this.verify(text -> {
                if (TextUtils.isEmpty(text)) {
                    throw new VerifyException("请输入" + name);
                }
                try {
                    text = String.valueOf(Integer.parseInt(text));
                } catch (NumberFormatException e) {
                    throw new VerifyException("请输入正确的" + name);
                }
                return text;
            });
        }
        /**
         * 指定为UInt(无符号整形，0和正数)
         */
        public TextBinder verifyUInt(String... names) {
            CharSequence name = getName("数值", names);
            inputType(InputType.TYPE_CLASS_NUMBER);
            return this.verify(text -> {
                if (TextUtils.isEmpty(text)) {
                    throw new VerifyException("请输入" + name);
                }
                try {
                    int v = Integer.parseInt(text);
                    if (v < 0) {
                        throw new VerifyException(name + "不能是负数");
                    }
                    text = String.valueOf(v);
                } catch (NumberFormatException e) {
                    throw new VerifyException("请输入正确的" + name);
                }
                return text;
            });
        }
        /**
         * 指定为PInt(正数)
         */
        public TextBinder verifyPInt(String... names) {
            CharSequence name = getName("数值", names);
            inputType(InputType.TYPE_CLASS_NUMBER);
            return this.verify(text -> {
                if (TextUtils.isEmpty(text)) {
                    throw new VerifyException("请输入" + name);
                }
                try {
                    int v = Integer.parseInt(text);
                    if (v <= 0) {
                        throw new VerifyException(name + "必须大于0");
                    }
                    text = String.valueOf(v);
                } catch (NumberFormatException e) {
                    throw new VerifyException("请输入正确的" + name);
                }
                return text;
            });
        }
        /**
         * 指定为Float
         */
        public TextBinder verifyFloat(String... names) {
            CharSequence name = getName("数值", names);
            inputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL|InputType.TYPE_NUMBER_FLAG_SIGNED);
            return this.verify(text -> {
                if (TextUtils.isEmpty(text)) {
                    throw new VerifyException("请输入" + name);
                }
                try {
                    text = String.valueOf(Float.parseFloat(text)).replace(".0", "");;
                } catch (NumberFormatException e) {
                    throw new VerifyException("请输入正确的" + name);
                }
                return text;
            });
        }
        /**
         * 指定为UFloat(无符号浮点型，0和正数)
         */
        public TextBinder verifyUFloat(String... names) {
            CharSequence name = getName("数值", names);
            inputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
            return this.verify(text -> {
                if (TextUtils.isEmpty(text)) {
                    throw new VerifyException("请输入" + name);
                }
                try {
                    float v = Float.parseFloat(text);
                    if (v < 0) {
                        throw new VerifyException(name + "不能是负数");
                    }
                    text = String.valueOf(v).replace(".0", "");
                } catch (NumberFormatException e) {
                    throw new VerifyException("请输入正确的" + name);
                }
                return text;
            });
        }
        /**
         * 指定为PFloat(正数)
         */
        public TextBinder verifyPFloat(String... names) {
            CharSequence name = getName("数值", names);
            inputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
            return this.verify(text -> {
                if (TextUtils.isEmpty(text)) {
                    throw new VerifyException("请输入" + name);
                }
                try {
                    float v = Float.parseFloat(text);
                    if (v <= 0) {
                        throw new VerifyException(name + "必须大于0");
                    }
                    text = String.valueOf(v).replace(".0", "");;
                } catch (NumberFormatException e) {
                    throw new VerifyException("请输入正确的" + name);
                }
                return text;
            });
        }

        /**
         * 指定为Float
         */
        public TextBinder verifyDouble(String... names) {
            CharSequence name = getName("数值", names);
            inputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL|InputType.TYPE_NUMBER_FLAG_SIGNED);
            return this.verify(text -> {
                if (TextUtils.isEmpty(text)) {
                    throw new VerifyException("请输入" + name);
                }
                try {
                    text = String.valueOf(Double.parseDouble(text)).replace(".0", "");;
                } catch (NumberFormatException e) {
                    throw new VerifyException("请输入正确的" + name);
                }
                return text;
            });
        }
        /**
         * 指定为UDouble(无符号浮点型，0和正数)
         */
        public TextBinder verifyUDouble(String... names) {
            CharSequence name = getName("数值", names);
            inputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
            return this.verify(text -> {
                if (TextUtils.isEmpty(text)) {
                    throw new VerifyException("请输入" + name);
                }
                try {
                    double v = Double.parseDouble(text);
                    if (v < 0) {
                        throw new VerifyException(name + "不能是负数");
                    }
                    text = String.valueOf(v).replace(".0", "");;
                } catch (NumberFormatException e) {
                    throw new VerifyException("请输入正确的" + name);
                }
                return text;
            });
        }
        /**
         * 指定为PDouble(正数)
         */
        public TextBinder verifyPDouble(String... names) {
            CharSequence name = getName("数值", names);
            inputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
            return this.verify(text -> {
                if (TextUtils.isEmpty(text)) {
                    throw new VerifyException("请输入" + name);
                }
                try {
                    double v = Double.parseDouble(text);
                    if (v <= 0) {
                        throw new VerifyException(name + "必须大于0");
                    }
                    text = String.valueOf(v).replace(".0", "");;
                } catch (NumberFormatException e) {
                    throw new VerifyException("请输入正确的" + name);
                }
                return text;
            });
        }
        /**
         * 指定为身份证的验证格式
         */
        public TextBinder verifyIdNumber(String... names) {
            CharSequence name = getName("身份证号", names);
            inputType(InputType.TYPE_CLASS_TEXT);
            return this.verify(text -> {
                if (TextUtils.isEmpty(text)) {
                    throw new VerifyException("请输入" + name);
                }
                int[] n = new int[]{1, 0, (int)'x', 9, 8, 7, 6, 5, 4, 3, 2};
                int[] b = new int[]{7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
                if (text.length() != 15 && text.length() != 18) {
                    throw new VerifyException(name + "必须为 15 位或18位");
                }
                String o = text.length() == 18 ? text.substring(0, 17) : text.substring(0, 6) + "19" + text.substring(6, 14);//id.substring(6, 16);
                if(!o.matches("^\\d+$")){//if (!/^\d+$/.test(o)) {
                    throw new VerifyException(name + "除最后一位外，必须为数字！");
                }
                int y = Integer.parseInt(o.substring(6, 10));
                int m = Integer.parseInt(o.substring(10, 12)) - 1;
                int d = Integer.parseInt(o.substring(12, 14));
                Calendar birth = Calendar.getInstance();
                birth.set(Calendar.YEAR, y);
                birth.set(Calendar.MONTH,m);
                birth.set(Calendar.DAY_OF_MONTH, d);
                int ly = birth.get(Calendar.YEAR);
                int lm = birth.get(Calendar.MONTH);
                int ld = birth.get(Calendar.DAY_OF_MONTH);
                Calendar now = Calendar.getInstance();
                if (ly != y || lm != m || ld != d || birth.after(now) || now.get(Calendar.YEAR) - ly > 140) {
                    throw new VerifyException(name + "出生年月输入错误！");
                }
                int g = 0,h = 0;
                for (; g < 17; g++) {
                    h = h + Integer.parseInt(o.charAt(g)+"") * b[g];
                }
                o += ""+n[h %= 11];
                if (text.length() == 18 && !text.toLowerCase(Locale.ENGLISH).equals(o)) {
                    throw new VerifyException(name + "最后一位校验码输入错误，正确校验码为：" + o.substring(17, 18) + "！");
                }
                return text;
            });
        }

        /**
         * 指定为残疾证的验证格式
         */
        public TextBinder verifyIdDisability(String... names) {
            CharSequence name = getName("有效证件号", names);
            inputType(InputType.TYPE_CLASS_TEXT);
            return this.verify(text -> {
                if (TextUtils.isEmpty(text)) {
                    throw new VerifyException("请输入" + name);
                }
                int[] n = new int[]{1, 0, (int)'x', 9, 8, 7, 6, 5, 4, 3, 2};
                int[] b = new int[]{7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
                if (text.length() != 15 && text.length() != 18 && text.length() != 20 ) {
                    throw new VerifyException(name + "必须为 15 位或 18 位或 20 位");
                }
                if (text.length() == 20) {
                    char type = text.charAt(18);
                    char grade = text.charAt(19);
                    if (type < '1' || type > '7') {
                        throw new VerifyException(name + "中残疾类别代码必须位 1~7");
                    }
                    if (grade < '1' || grade > '4') {
                        throw new VerifyException(name + "中残疾等级代码必须位 1~4");
                    }
                }
                String idText = text.length() == 20 ? text.substring(0,18) : text;
                String o = idText.length() == 18 ? idText.substring(0, 17) : idText.substring(0, 6) + "19" + idText.substring(6, 14);//id.substring(6, 16);
                if(!o.matches("^\\d+$")){//if (!/^\d+$/.test(o)) {
                    throw new VerifyException(name + "除最后一位外，必须为数字！");
                }
                int y = Integer.parseInt(o.substring(6, 10));
                int m = Integer.parseInt(o.substring(10, 12)) - 1;
                int d = Integer.parseInt(o.substring(12, 14));
                Calendar birth = Calendar.getInstance();
                birth.set(Calendar.YEAR, y);
                birth.set(Calendar.MONTH, m);
                birth.set(Calendar.DAY_OF_MONTH, d);
                int ly = birth.get(Calendar.YEAR);
                int lm = birth.get(Calendar.MONTH);
                int ld = birth.get(Calendar.DAY_OF_MONTH);
                Calendar now = Calendar.getInstance();
                if (ly != y || lm != m || ld != d || birth.after(now) || now.get(Calendar.YEAR) - ly > 140) {
                    throw new VerifyException(name + "出生年月输入错误！");
                }
//                int g = 0,h = 0;
//                for (; g < 17; g++) {
//                    h = h + Integer.parseInt(o.charAt(g)+"") * b[g];
//                }
//                o += ""+n[h %= 11];
//                if (idText.length() == 18 && !idText.toLowerCase(Locale.ENGLISH).equals(o)) {
//                    throw new VerifyException(name + "最后一位校验码输入错误，正确校验码为：" + o.substring(17, 18) + "！");
//                }
                return text;
            });
        }
        /**
         * 验证 Float 的金额范围
         */
        public TextBinder verifyFloatMoney(String... names) {
            CharSequence name = getName("金额", names);
            inputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
            return this.verify(text -> {
                if (TextUtils.isEmpty(text)) {
                    throw new VerifyException("请输入" + name);
                }
                for (int i = text.length() - 1, accuracy = 0; i >= 0; i--) {
                    char c = text.charAt(i);
                    if (c <= '9' && (c > '0' || (accuracy > 0 && c == '0'))) {
                        accuracy++;
                        if (accuracy > 7) {
                            throw new VerifyException(name + "有效数字位数不能超过7位");
                        }
                    }
                }
                try {
                    Float v = Float.parseFloat(text);
                    if (v <= 0) {
                        throw new VerifyException(name + "必须大于0");
                    }
                    text = String.valueOf(v).replace(".0", "");;
                } catch (NumberFormatException e) {
                    throw new VerifyException("请输入正确的" + name);
                }
                return text;
            });
        }

        /**
         * 验证 Double 的金额范围
         */
        public TextBinder verifyDoubleMoney(String... names) {
            CharSequence name = getName("金额", names);
            inputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
            return this.verify(text -> {
                if (TextUtils.isEmpty(text)) {
                    throw new VerifyException("请输入" + name);
                }
                for (int i = text.length() - 1, accuracy = 0; i >= 0; i--) {
                    char c = text.charAt(i);
                    if (c <= '9' && (c > '0' || (accuracy > 0 && c == '0'))) {
                        accuracy++;
                        if (accuracy > 15) {
                            throw new VerifyException(name + "有效数字位数不能超过15位");
                        }
                    }
                }
                try {
                    Double v = Double.parseDouble(text);
                    if (v <= 0) {
                        throw new VerifyException(name + "必须大于0");
                    }
                    text = String.valueOf(v).replace(".0", "");;
                } catch (NumberFormatException e) {
                    throw new VerifyException("请输入正确的" + name);
                }
                return text;
            });
        }

        //</editor-fold>
    }

    public class TextLinesBinder extends TextBinder {

        TextLinesBinder(int idValue) {
            super(idValue);
        }

        @Override
        public void start() {
            $.dialog(viewer).inputLines(hint, lastValue == null ? $(idValue).text().replace(valueSuffix,"") : lastValue, type, this);
        }
    }

    public abstract class AbstractDateBinder<T extends Binder> extends Binder<T, CommonBind<Date>, Date> {

        List<DateVerify> verifies = new ArrayList<>();
        DateFormat format = AfDateFormat.DATE;
        boolean isManual = false;//标记是否用户手动设置过

        AbstractDateBinder(int idValue) {
            super(idValue);
        }

        public abstract T value(Date date);

        public T format(DateFormat format) {
            this.format = format;
            return self();
        }

        public T format(String format) {
            return format(new SimpleDateFormat(format, Locale.ENGLISH));
        }

        public T initNow() {
            return value(new Date());
        }

        /**
         * 自定义验证规则
         */
        public T verify(DateVerify verify) {
            this.verifies.add(verify);
            return self();
        }

        /**
         * 指定为之后的时间
         */
        public T verifyAfterNow(String... names) {
            CharSequence name = getName("时间", names);
            return this.verify((date,verifyDateOnly) -> {
                Date now = new Date(System.currentTimeMillis());
                date = verifyDateOnly ? AfDateFormat.roundDate(date) : date;
                now = verifyDateOnly ? AfDateFormat.roundDate(now) : now;
                if (date.getTime() <= now.getTime()) {
                    if (verifyDateOnly && date.getTime() == now.getTime()) {
                        return;
                    }
                    throw new VerifyException(name + "不能是现在之前");
                }
            });
        }

        /**
         * 指定为之后的时间
         */
        public T verifyBeforeNow(String... names) {
            CharSequence name = getName("时间", names);
            return this.verify((date,verifyDateOnly) -> {
                Date now = new Date(System.currentTimeMillis());
                date = verifyDateOnly ? AfDateFormat.roundDate(date) : date;
                now = verifyDateOnly ? AfDateFormat.roundDate(now) : now;
                if (date.getTime() >= now.getTime()) {
                    if (verifyDateOnly && date.getTime() == now.getTime()) {
                        return;
                    }
                    throw new VerifyException(name + "只能是现在之前");
                }
            });
        }

        /**
         * 指定为今天之后的时间（今天23：59之后）
         */
        public T verifyAfterToday(String... names) {
            CharSequence name = getName("日期", names);
            return this.verify((date,verifyDateOnly) -> {
                //计算 明天00：00
                long today = AfDateFormat.roundDate(new Date(new Date().getTime() + 24L * 60 * 60 * 1000)).getTime();
                if (date.getTime() < today) {
                    throw new VerifyException(name + "必须是今天以后");
                }
            });
        }

        /**
         * 指定为今天之后的时间（今天00：00之后）
         */
        public T verifyAfterWithToday(String... names) {
            CharSequence name = getName("日期", names);
            return this.verify((date,verifyDateOnly) -> {
                //计算 今天00：00
                long today = AfDateFormat.roundDate(new Date()).getTime();
                if (date.getTime() < today) {
                    throw new VerifyException(name + "不能早于今天");
                }
            });
        }

        /**
         * 指定为今天之后的时间（今天00：00以前）
         */
        public T verifyBeforeToday(String... names) {
            CharSequence name = getName("日期", names);
            return this.verify((date,verifyDateOnly) -> {
                //计算 今天00：00
                long today = AfDateFormat.roundDate(new Date()).getTime();
                if (date.getTime() >= today) {
                    throw new VerifyException(name + "必须是今天之前");
                }
            });
        }

        /**
         * 指定为今天之后的时间（今天23：59以前）
         */
        public T verifyBeforeWithToday(String... names) {
            CharSequence name = getName("日期", names);
            return this.verify((date,verifyDateOnly) -> {
                //计算 明天00：00
                long today = AfDateFormat.roundDate(new Date(new Date().getTime() + 24L * 60 * 60 * 1000)).getTime();
                if (date.getTime() >= today) {
                    throw new VerifyException(name + "不能晚于今天");
                }
            });
        }


        @SuppressWarnings("UnusedReturnValue")
        public T verifyBefore(AbstractDateBinder<? extends AbstractDateBinder> binder, String... names) {
            CharSequence name = getName("时间", names);
            return this.verify((date,verifyDateOnly) -> {
                if (binder.lastValue != null) {
                    date = verifyDateOnly ? AfDateFormat.roundDate(date) : date;
                    Date value = verifyDateOnly ? AfDateFormat.roundDate(binder.lastValue) : binder.lastValue;
                    if (date.getTime() >= value.getTime()) {
                        if (verifyDateOnly && date.getTime() == value.getTime()) {
                            return;
                        }
                        throw new VerifyException(name + "必须早于" + binder.getName("时间", names));
                    }
                }
            });
        }

        public T verifyBeforeWith(AbstractDateBinder<? extends AbstractDateBinder> binder, String... names) {
            CharSequence name = getName("时间", names);
            return this.verify((date,verifyDateOnly) -> {
                if (binder.lastValue != null) {
                    date = verifyDateOnly ? AfDateFormat.roundDate(date) : date;
                    Date value = verifyDateOnly ? AfDateFormat.roundDate(binder.lastValue) : binder.lastValue;
                    if (date.getTime() > value.getTime()) {
                        if (binder.isManual || verifyDateOnly) {
                            throw new VerifyException(name + "不能晚于" + binder.getName("时间", names));
                        } else {
                            binder.value(date);
                        }
                    }
                }
            });
        }

        @SuppressWarnings("UnusedReturnValue")
        public T verifyAfter(AbstractDateBinder<? extends AbstractDateBinder> binder, String... names) {
            CharSequence name = getName("时间", names);
            return this.verify((date,verifyDateOnly) -> {
                if (binder.lastValue != null) {
                    date = verifyDateOnly ? AfDateFormat.roundDate(date) : date;
                    Date value = verifyDateOnly ? AfDateFormat.roundDate(binder.lastValue) : binder.lastValue;
                    if (date.getTime() <= value.getTime()) {
                        if (verifyDateOnly && date.getTime() == value.getTime()) {
                            return;
                        }
                        throw new VerifyException(name + "必须晚于" + binder.getName("时间", names));
                    }
                }
            });
        }

        public T verifyAfterWith(AbstractDateBinder<? extends AbstractDateBinder> binder, String... names) {
            CharSequence name = getName("时间", names);
            return this.verify((date,verifyDateOnly) -> {
                if (binder.lastValue != null) {
                    date = verifyDateOnly ? AfDateFormat.roundDate(date) : date;
                    Date value = verifyDateOnly ? AfDateFormat.roundDate(binder.lastValue) : binder.lastValue;
                    if (date.getTime() < value.getTime()) {
                        if (binder.isManual || verifyDateOnly) {
                            throw new VerifyException(name + "不能早于" + binder.getName("时间", names));
                        } else {
                            binder.value(date);
                        }
                    }
                }
            });
        }
    }

    public class DateBinder extends AbstractDateBinder<DateBinder> implements OnDateSetVerifyListener {

        DateBinder(int idValue) {
            super(idValue);
            format = AfDateFormat.DATE;
        }

        @Override
        public void start() {
            $.dialog(viewer).selectDate(hint, lastValue == null ? new Date() : lastValue, this);
        }

        public DateBinder value(@Nullable Date date) {
            if (date != null) {
                lastValue = AfDateFormat.roundDate(date);
                Calendar now = Calendar.getInstance();
                now.setTime(date);
                onDateSet(null, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
            } else {
                lastValue = null;
                $(idValue).text("");
            }
            return self();
        }

        @Override
        public boolean onPreDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            if (verifies != null && view != null) {
                try {
                    for (DateVerify verify : verifies) {
                        verify.verify(AfDateFormat.parser(year,month,dayOfMonth), false);
                    }
                } catch (VerifyException e) {
                    $.toaster(viewer).toast(e.getMessage());
                    return false;
                }
            }
//            onDateSet(view, year, month, dayOfMonth);
            return true;
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            isManual = view != null;
            lastValue = AfDateFormat.parser(year, month, day);
            $(idValue).text(format.format(lastValue));
            if (bind != null) {
                bind.bind(this, lastValue);
            }
        }

    }

    public class MonthBinder extends AbstractDateBinder<MonthBinder> implements OnDateSetVerifyListener{

        private static final int YEAR_MIN = 2000;
        private static final int YEAR_MAX = 3000;

        MonthBinder(int idValue) {
            super(idValue);
            format = new SimpleDateFormat("yyyy-MM", Locale.CHINA);
        }

        @Override
        public void start() {
            //Calendar calendar = Calendar.getInstance();
            //if (lastValue != null) {
            //    calendar.setTime(lastValue);
            //}
            //TextView txtYear = new TextView(viewer.getContext());
            //TextView txtMonth = new TextView(viewer.getContext());
            //NumberPicker year = new NumberPicker(viewer.getContext());
            //NumberPicker month = new NumberPicker(viewer.getContext());
            //year.setMinValue(YEAR_MIN);
            //year.setMaxValue(YEAR_MAX);
            //month.setMinValue(1);
            //month.setMaxValue(12);
            //year.setValue(calendar.get(Calendar.YEAR));
            //month.setValue(calendar.get(Calendar.MONTH) + 1);
            //txtYear.setText("年");
            //txtMonth.setText("月");
            //View view = $(new LinearLayout(viewer.getContext()))
            //        .addView(year).addView(txtYear)
            //        .addView(month).addView(txtMonth)
            //        .gravity(Gravity.CENTER).view();
            //Dialog dialog = $.dialog(viewer).showViewDialog(hint, view, "取消", "选择", null);
            //if (dialog instanceof AlertDialog) {
            //    ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE)
            //            .setOnClickListener(new SafeListener((View.OnClickListener) v -> {
            //                if (onPreDateSet(view, year.getValue(), month.getValue() - 1)) {
            //                    dialog.dismiss();
            //                }
            //            }));
            //}

            $.dialog(viewer).builder().title(hint).month(lastValue, this);
        }

        public MonthBinder value(Date date) {
            if (date != null) {
                lastValue = AfDateFormat.roundDate(date);
                Calendar now = Calendar.getInstance();
                now.setTime(date);
                onDateSet(null, now.get(Calendar.YEAR), now.get(Calendar.MONTH), 1);
            } else {
                lastValue = null;
                $(idValue).text("");
            }
            return self();
        }

        @Override
        public boolean onPreDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            if (verifies != null && view != null) {
                try {
                    for (DateVerify verify : verifies) {
                        verify.verify(AfDateFormat.parser(year, month, 1), false);
                    }
                } catch (VerifyException e) {
                    $.toaster(viewer).toast(e.getMessage());
                    return false;
                }
            }
            return true;
        }

        //public boolean onPreDateSet(View view, int year, int month) {
        //    if (verifies != null && view != null) {
        //        try {
        //            for (DateVerify verify : verifies) {
        //                verify.verify(AfDateFormat.parser(year, month, 1), false);
        //            }
        //        } catch (VerifyException e) {
        //            $.toaster(viewer).toast(e.getMessage());
        //            return false;
        //        }
        //    }
        //    onDateSet(view, year, month);
        //    return true;
        //}

        //public void onDateSet(View view, int year, int month) {
        //    isManual = view != null;
        //    lastValue = AfDateFormat.parser(year, month, 1);
        //    $(idValue).text(format.format(lastValue));
        //    if (bind != null) {
        //        bind.bind(this, lastValue);
        //    }
        //}

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            isManual = view != null;
            lastValue = AfDateFormat.parser(year, month, 1);
            $(idValue).text(format.format(lastValue));
            if (bind != null) {
                bind.bind(this, lastValue);
            }
        }
    }

    public class TimeBinder extends AbstractDateBinder<TimeBinder> implements OnTimeSetVerifyListener {

        private Dialog dialog = null;

        TimeBinder(int idValue) {
            super(idValue);
            format = new SimpleDateFormat("HH:mm", Locale.CHINA);
        }

        @Override
        public void start() {
            dialog = $.dialog(viewer).selectTime(hint, lastValue == null ? new Date() : lastValue, this);
        }

        public TimeBinder value(Date date) {
            if (date != null) {
                lastValue = date;
                Calendar now = Calendar.getInstance();
                now.setTime(date);
                onTimeSet(null, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE));
            } else {
                lastValue = null;
                $(idValue).text("");
            }
            return self();
        }

        @Override
        public boolean onPreTimeSet(TimePicker view, int hourOfDay, int minute) {
            if (verifies != null && view != null) {
                try {
                    for (DateVerify verify : verifies) {
                        verify.verify(AfDateFormat.parser(hourOfDay,minute), false);
                    }
                } catch (VerifyException e) {
                    $.toaster(viewer).toast(e.getMessage());
                    return false;
                }
            }
            if (taskBuilder != null) {
                Builder taskBuilder = this.taskBuilder.builder(AfDateFormat.parser(hourOfDay, minute));
                if (taskBuilder instanceof WaitBuilder) {
                    WaitBuilder builder = (WaitBuilder) taskBuilder;
                    Runnable success = builder.success();
                    builder.success(() -> {
                        if (success != null) {
                            success.run();
                        }
                        onTimeSet(view, hourOfDay, minute);
                        if (dialog != null) dialog.dismiss();
                    });
                    taskBuilder.post();
                    return false;
                } else if (taskBuilder != null) {
                    taskBuilder.post();
                }
            }
//            onTimeSet(view, hourOfDay, minute);
            return true;
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            isManual = view != null;
            lastValue = AfDateFormat.parser(hourOfDay, minute);
            $(idValue).text(format.format(lastValue));
            if (bind != null) {
                bind.bind(this, lastValue);
            }
        }
    }

    public class DateTimeBinder extends AbstractDateBinder<DateTimeBinder> implements OnDateTimeSetVerifyListener {

        private int tempYear = 0;
        private int tempMonth = 0;
        private int tempDay = 0;

        DateTimeBinder(int idValue) {
            super(idValue);
            format = AfDateFormat.STANDARD;
        }

        @Override
        public void start() {
            $.dialog(viewer).selectDateTime(hint, lastValue == null ? new Date() : lastValue, this);
        }

        public DateTimeBinder value(Date date) {
            if (date != null) {
                lastValue = date;
                Calendar now = Calendar.getInstance();
                now.setTime(date);
                onDateTimeSet(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH),
                        now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE));
            } else {
                lastValue = null;
                $(idValue).text("");
            }
            return self();
        }

        @Override
        public boolean onPreDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            if (verifies != null && view != null) {
                try {
                    for (DateVerify verify : verifies) {
                        verify.verify(AfDateFormat.parser(year,month,dayOfMonth), true);
                    }
                    tempYear = year;
                    tempMonth = month;
                    tempDay = dayOfMonth;
                } catch (VerifyException e) {
                    $.toaster(viewer).toast(e.getMessage());
                    return false;
                }
            }
            return true;
        }

        @Override
        public boolean onPreTimeSet(TimePicker view, int hourOfDay, int minute) {
            if (verifies != null && view != null) {
                try {
                    for (DateVerify verify : verifies) {
                        verify.verify(AfDateFormat.parser(tempYear, tempMonth, tempDay, hourOfDay, minute), false);
                    }
                    isManual = true;
                } catch (VerifyException e) {
                    $.toaster(viewer).toast(e.getMessage());
                    return false;
                }
            }
            return true;
        }

        @Override
        public void onDateTimeSet(int year, int month, int day, int hour, int minute) {
            lastValue = AfDateFormat.parser(year, month, day, hour, minute);
            $(idValue).text(format.format(lastValue));
            if (bind != null) {
                bind.bind(this, lastValue);
            }
        }
    }

    public class CheckBinder extends Binder<CheckBinder, CommonBind<Boolean>, Boolean> {

        CheckBinder(int idValue) {
            super(idValue);
            lastValue = $(idValue).isChecked();
        }

        public CheckBinder click(View view) {
            if (!readOnly) {
                $(view).clicked(this, 0);
                $(idValue).clicked(null).clickable(false);
            }
            return self();
        }

        @Override
        public void onRestoreCache(String key) {
            Boolean bool = cacher.get(key, null, Boolean.class);
            if (bool != null) {
                value(bool);
            }
        }

        public CheckBinder value(Boolean isChecked) {
            if (isChecked != null) {
                lastValue = isChecked;
                $(idValue).checked(isChecked);
                if (bind != null) {
                    bind.bind(this, isChecked);
                }
            } else {
                lastValue = null;
                $(idValue).text("");
            }
            return self();
        }

        @Override
        public void onClick(View v) {
            if (clickHook != null && clickHook.onBinderClick(this)) {
                return;
            }
            if (v != null && v.getId() != idValue) {
                lastValue = $(idValue).toggle().isChecked();
            }
            super.onClick(v);
        }

        private void performStart() {
            if (start == null) {
                start();
            } else {
                start.run();
            }
            if (next != null) {
                next.performStart();
            }
        }

        @Override
        public void start() {
            lastValue = $(idValue).isChecked();
            if (key != null) {
                cacher.put(key, lastValue);
            }
            if (bind != null) {
                bind.bind(this, lastValue);
            }
        }

    }

    public class SwitchBinder extends CheckBinder {
        SwitchBinder(int idValue) {
            super(idValue);
        }
    }

    public class SeekBarBinder extends Binder<SeekBarBinder, SeekBind, Integer> implements SeekBar.OnSeekBarChangeListener {

        SeekBarBinder(int idValue) {
            super(idValue);
            SeekBar view = $(idValue).view();
            if (view != null) {
                view.setOnSeekBarChangeListener(new SafeListener((SeekBar.OnSeekBarChangeListener) this));
            }
        }

        public SeekBarBinder max(int max) {
            $(idValue).max(max);
            return self();
        }

        public SeekBarBinder value(int value) {
            $(idValue).progress(value);
            return self();
        }

        @Override
        protected void start() {
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (bind != null) {
                bind.seek(this, progress, fromUser);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }

    public class ActivityBinder extends Binder<ActivityBinder, CommonBind<Void>, Void> {

        private final Object[] args;
        private Class<? extends Activity> activity;

        ActivityBinder(int idValue, Class<? extends Activity> activity, Object... args) {
            super(idValue);
            this.args = args;
            this.activity = activity;
        }

        @Override
        public void start() {
//            viewer.startActivity(activity, args);
            $.pager().startActivity(activity, args);
        }

    }

    public class FragmentBinder extends Binder<FragmentBinder, CommonBind<Void>, Void> {

        private final Object[] args;
        private Class<? extends Fragment> fragment;

        FragmentBinder(int idValue, Class<? extends Fragment> fragment, Object... args) {
            super(idValue);
            this.args = args;
            this.fragment = fragment;
        }

        @Override
        public void start() {
            $.pager().startFragment(fragment, args);
        }

    }

    public static class ImageLoaderEngine implements ImageEngine {

        @Override
        public void loadThumbnail(Context context, int resize, Drawable placeholder, ImageView imageView, Uri uri) {
            int px = SmartUtil.dp2px(150);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ImageLoader.getInstance().displayImage(uri.toString(), imageView, new ImageSize(px, px));
        }

        @Override
        public void loadGifThumbnail(Context context, int resize, Drawable placeholder, ImageView imageView, Uri uri) {

        }

        @Override
        public void loadImage(Context context, int resizeX, int resizeY, ImageView imageView, Uri uri) {
            ImageLoader.getInstance().displayImage(uri.toString(), imageView);
        }

        @Override
        public void loadGifImage(Context context, int resizeX, int resizeY, ImageView imageView, Uri uri) {

        }

        @Override
        public boolean supportAnimatedGif() {
            return false;
        }
    }

    public class ImageBinder extends Binder<ImageBinder, CommonBind<String>, String> {

        protected int outPutX = 0;           //裁剪保存宽度
        protected int outPutY = 0;           //裁剪保存高度
        protected int request_image = 1000;

        protected ImageBinder(int idImage) {
            super(idImage);
        }

        @Override
        protected void start() {
            Pager pager = $.pager().currentPager();
            if (pager instanceof ApPager) {
                ((ApPager) pager).doStorageWithPermissionCheck(()->{
                    Viewer p = viewer;
                    Matisse matisse = (p instanceof Fragment) ? Matisse.from((Fragment) p) : Matisse.from((Activity) p);
                    matisse.choose(MimeType.ofImage())
                            .countable(true)
                            .maxSelectable(1)
                            .theme(R.style.Matisse_Dracula)
                            .gridExpectedSize(SmartUtil.dp2px(120))
                            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                            .thumbnailScale(0.85f)
                            .imageEngine(new ImageLoaderEngine())
                            .forResult(request_image);
                });
            }
        }

        public ImageBinder image(String url) {
            $.query(viewer).query(idValue).image(url);
            return self();
        }

        public void requestCode(int request_image) {
            this.request_image = request_image;
        }

        public ImageBinder circle() {
//            style = CropImageView.Style.CIRCLE;
            return self();
        }

        public ImageBinder cut(int... xy) {
            if (xy.length == 0) {
                outPutX = 800;
                outPutY = 800;
            } else if (xy.length == 1) {
                outPutX = xy[0];
                outPutY = xy[0];
            } else {
                outPutX = xy[0];
                outPutY = xy[1];
            }
            return self();
        }

        public void onActivityResult(AfIntent intent, int requestCode, int resultCode) {
            if (requestCode == request_image && resultCode == Activity.RESULT_OK) {
                List<String> images = Matisse.obtainPathResult(intent);
                if (images != null && images.size() > 0) {
                    Pager pager = $.pager().currentPager();
                    if (images.size() == 1 && outPutX > 0 && outPutY > 0 && pager != null) {
                        CropImage.ActivityBuilder builderCrop = CropImage.activity(Uri.fromFile(new File(images.get(0))))
                                .setRequestedSize(outPutX, outPutY)
                                .setGuidelines(CropImageView.Guidelines.ON);
                        if (pager instanceof Fragment) {
                            builderCrop.start(pager.getContext(), (Fragment) pager);
                        } else {
                            builderCrop.start(pager.getActivity());
                        }
                    } else {
                        bind.bind(this, images.get(0));
                        $.query(viewer).query(idValue).image(images.get(0));
                    }
                } else {
                    $.toaster(viewer).toast("没有数据");
                }
            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(intent);
                if (resultCode == Activity.RESULT_OK) {
                    Uri resultUri = result.getUri();
                    bind.bind(this, resultUri.getPath() + "");
                    $.query(viewer).query(idValue).image(resultUri.getPath() + "");
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                    $.toaster().toast(error.getMessage());
                }
            }
        }

    }

    public class RadioGroupBinder extends Binder<RadioGroupBinder, RadioGroupBind,Integer> implements RadioGroup.OnCheckedChangeListener {

        RadioGroupBinder(int idValue) {
            super(idValue);
            $(idValue).clicked(null).foreach(RadioGroup.class, (ViewQuery.ViewIterator<RadioGroup>) view -> view.setOnCheckedChangeListener(this));
        }

        @Override
        protected void start() {

        }

        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            if (bind != null) {
                int index = group.indexOfChild(group.findViewById(checkedId));
                bind.onBind(this, group, checkedId, index);
            }
        }

    }
}