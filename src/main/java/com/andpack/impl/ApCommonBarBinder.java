package com.andpack.impl;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;

import com.andframe.$;
import com.andframe.api.Cacher;
import com.andframe.api.DialogBuilder;
import com.andframe.api.DialogBuilder.OnDateSetVerifyListener;
import com.andframe.api.DialogBuilder.OnDateTimeSetVerifyListener;
import com.andframe.api.DialogBuilder.OnTimeSetVerifyListener;
import com.andframe.api.task.builder.Builder;
import com.andframe.api.task.builder.WaitBuilder;
import com.andframe.api.viewer.ViewQuery;
import com.andframe.api.viewer.Viewer;
import com.andframe.feature.AfIntent;
import com.andframe.listener.SafeListener;
import com.andframe.task.AfDispatcher;
import com.andframe.util.java.AfDateFormat;
import com.andpack.application.ApApp;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;

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

    public interface SelectBind {
        void text(Binder binder, String text, int which);
    }

    public interface NumberBind {
        void text(Binder binder, int value);
    }

    public interface MultiChoiceBind {
        void text(Binder binder, String text, int count, boolean[] checkedItems);
    }

    public interface TextBind {
        void text(Binder binder, String text);
    }

    public interface DateBind {
        void date(Binder binder, Date date);
    }

    public interface CheckBind {
        void check(Binder binder, boolean isChecked);
    }

    public interface SeekBind {
        void seek(Binder binder, int value, boolean fromUser);
    }
    public interface InputBind {
        void onBind(Binder binder, String value);
    }
    public interface RadioGroupBind {
        void onBind(Binder binder, RadioGroup group, @IdRes int checkedId, int index);
    }
    public interface ImageBind {
        /**
         * @param binder Binder 对象
         * @param path 图片路径
         * @return true 已经显示图片（Binder 将不会自动显示） false （Binder 将会自动显示）
         */
        boolean image(Binder binder, String path);
    }

    public interface TextVerify {
        String verify(String text) throws VerifyException;
    }

    public interface DateVerify {
        void verify(Date date) throws VerifyException;
    }

    public interface MultiChoiceVerify {
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

    public ApCommonBarBinder(Viewer viewer) {
        this.viewer = viewer;
        this.$$ = $.query(viewer);
        this.cacher = $.cache(viewer.getClass().getName());
    }

    public void setHintPrefix(String hintPrefix) {
        this.hintPrefix = hintPrefix;
    }

    public ViewQuery<? extends ViewQuery> $(Integer id, int... ids) {
        return $$.$(id, ids);
    }
    public ViewQuery<? extends ViewQuery> $(View... views) {
        return $$.$(views);
    }

    public TextBinder text(@IdRes int idValue) {
        return new TextBinder(idValue);
    }

    public TextBinder textLines(@IdRes int idValue) {
        return new TextLinesBinder(idValue);
    }

    public SelectNumber number(@IdRes int idValue) {
        return new SelectNumber(idValue);
    }

    public InputBinder input(@IdRes int idValue) {
        return new InputBinder(idValue);
    }

    public SelectBinder select(@IdRes int idValue, CharSequence... items) {
        return new SelectBinder(idValue, items);
    }

    public CheckBinder check(@IdRes int idValue) {
        return new CheckBinder(idValue);
    }

    public SwitchBinder switcher(@IdRes int idValue) {
        return new SwitchBinder(idValue);
    }

    public SeekBarBinder seek(@IdRes int idValue) {
        return new SeekBarBinder(idValue);
    }

    public DateBinder date(@IdRes int idValue) {
        return new DateBinder(idValue);
    }

    public MonthBinder month(@IdRes int idValue) {
        return new MonthBinder(idValue);
    }

    public TimeBinder time(@IdRes int idValue) {
        return new TimeBinder(idValue);
    }

    public DateTimeBinder datetime(@IdRes int idValue) {
        return new DateTimeBinder(idValue);
    }

    public MultiChoiceBinder multiChoice(@IdRes int idValue, CharSequence[] items) {
        return new MultiChoiceBinder(idValue, items);
    }

    public ActivityBinder activity(@IdRes int idValue, Class<? extends Activity> clazz, Object... args) {
        return new ActivityBinder(idValue, clazz, args);
    }

    public FragmentBinder fragment(@IdRes int idValue, Class<? extends Fragment> clazz, Object... args) {
        return new FragmentBinder(idValue, clazz, args);
    }

    public ImageBinder image(@IdRes int idImage) {
        return new ImageBinder(idImage);
    }

    public RadioGroupBinder radioGroup(int id) {
        return new RadioGroupBinder(id);
    }

    public abstract class Binder<T extends Binder, LASTVAL> implements View.OnClickListener{
        public int idValue;
        public String key = null;
        public CharSequence hintPrefix = ApCommonBarBinder.this.hintPrefix;
        public CharSequence hint = hintPrefix;
        public CharSequence name = "";
        public Binder next;
        public LASTVAL lastval;
        public Runnable start;
        public ClickHook clickHook;
        public TaskBuilder<LASTVAL> taskBuilder;

        Binder(int idValue) {
            this.idValue = idValue;
            $(idValue).clicked(this);
        }

        public T click(int idclick) {
            $(idclick).clicked(this);
            $(idValue).clicked(null).clickable(false);
            return self();
        }

        public T click(View view) {
            $(view).clicked(this);
            $(idValue).clicked(null).clickable(false);
            return self();
        }

        public T clickHook(ClickHook clickHook) {
            this.clickHook = clickHook;
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
            performStart();
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

        public void task(TaskBuilder<LASTVAL> builder) {
            taskBuilder = builder;
        }

        protected abstract void start();

        T self() {
            //noinspection unchecked
            return (T)this;
        }
    }

    public class SelectBinder extends Binder<SelectBinder, Void> implements DialogInterface.OnClickListener {

        private SelectBind bind;
        private final CharSequence[] items;

        SelectBinder(int idValue, CharSequence... items) {
            super(idValue);
            this.items = items;
            this.hintPrefix("请选择");
        }

        @Override
        public void start() {
            $.dialog(viewer).selectItem(hint, items, this);
        }

        @Override
        public void onRestoreCache(String key) {
            int i = cacher.getInt(key, -1);
            value(i);
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            $(idValue).text(items[which]);
            if (key != null && dialog != null) {
                cacher.put(key, which);
            }
            if (bind != null) {
                bind.text(this, items[which].toString(), which);
            }
        }

        public SelectBinder bind(SelectBind bind) {
            this.bind = bind;
            return self();
        }

        public SelectBinder value(int index) {
            if (index >= 0 && index < items.length) {
                onClick(null, index);
            }
            return self();
        }

        public SelectBinder value(CharSequence value) {
            for (int i = 0; i < items.length; i++) {
                if (TextUtils.equals(items[i].toString(), value)) {
                    onClick(null, i);
                }
            }
            return self();
        }
    }

    public class SelectNumber extends Binder<SelectNumber, Integer> {

        private int minValue = 14;
        private int maxValue = 100;
        private String unit;
        private NumberBind bind;

        public SelectNumber(int idValue) {
            super(idValue);
            this.hintPrefix("请选择");
        }

        public void bind(NumberBind bind) {
            this.bind = bind;
        }

        @Override
        protected void start() {
            TextView textview = new TextView(viewer.getContext());
            NumberPicker picker = new NumberPicker(viewer.getContext());
            picker.setMinValue(minValue);
            picker.setMaxValue(maxValue);
            if (lastval != null) {
                picker.setValue(lastval);
            }
            textview.setText(TextUtils.isEmpty(unit) ? "" : unit);
            View view = $(new LinearLayout(viewer.getContext()))
                    .addView(picker).addView(textview).gravity(Gravity.CENTER_VERTICAL).view();
            $.dialog(viewer).showViewDialog(hint, view, "取消", null, "确定", (d, i) -> onNumberSelected(picker, picker.getValue()));
        }

        public SelectNumber value(int value) {
            onNumberSelected(null, value);
            return self();
        }

        public SelectNumber unit(String unit) {
            this.unit = unit;
            return self();
        }

        public SelectNumber range(int min, int max) {
            minValue = Math.min(min,max);
            maxValue = Math.max(min,max);
            lastval = lastval == null ? minValue : lastval;
            return self();
        }

        public SelectNumber rangeAge() {
            minValue = 14;
            maxValue = 100;
            lastval = lastval == null ? minValue : lastval;
            return self();
        }

        private void onNumberSelected(NumberPicker picker, int value) {
            lastval = value;
            $(idValue).text(String.valueOf(value)+(TextUtils.isEmpty(unit) ? "" : unit));
            if (key != null && picker != null) {
                cacher.put(key, value);
            }
            if (bind != null) {
                bind.text(this, value);
            }
        }
    }

    public class MultiChoiceBinder extends Binder<MultiChoiceBinder, Void> implements DialogInterface.OnClickListener {

        private boolean[] checkedItems;
        private CharSequence[] items;
        private MultiChoiceBind bind;
        private MultiChoiceVerify verify;

        MultiChoiceBinder(int idValue, CharSequence[] items) {
            super(idValue);
            this.items = items;
            this.checkedItems = new boolean[items.length];
            this.hintPrefix("请选择");
        }

        public MultiChoiceBinder verify(MultiChoiceVerify verify) {
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
            if (list != null && list.size() == checkedItems.length) {
                for (int i = 0; i < checkedItems.length; i++) {
                    checkedItems[i] = list.get(i);
                }
                onClick(null, 0);
            }
        }

        public MultiChoiceBinder value(String text) {
            for (int i = 0; i < items.length && text != null; i++) {
                checkedItems[i] = text.contains(items[i]);
                onClick(null, 0);
            }
            return self();
        }

        public MultiChoiceBinder value(boolean... checkedItems) {
            if (checkedItems.length == this.checkedItems.length) {
                this.checkedItems = checkedItems;
                onClick(new Dialog(viewer.getContext()), 0);
            }
            return self();
        }

        public MultiChoiceBinder bind(MultiChoiceBind bind) {
            this.bind = bind;
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
                    $.toast(viewer).makeToastShort(e.getMessage());
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

    public class InputBinder extends Binder<InputBinder, String> implements TextWatcher {

        private InputBind bind;

        InputBinder(int idValue) {
            super(idValue);
        }

        public InputBinder inputType(int type) {
            $(idValue).inputType(type);
            return self();
        }

        public InputBinder bind(InputBind bind) {
            this.bind = bind;
            $(idValue).textChanged(this);
            return self();
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
            AfDispatcher.dispatch(() -> {
                if (bind != null) {
                    bind.onBind(this, s.toString());
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

    public class TextBinder extends Binder<TextBinder, String> implements DialogBuilder.InputTextListener {

        private TextBind bind;
        private TextVerify verify;
        protected String valueSuffix = "";
        protected int type = InputType.TYPE_CLASS_TEXT;

        TextBinder(int idValue) {
            super(idValue);
            if (TextUtils.isEmpty(hintPrefix)) {
                this.hintPrefix("请输入");
            }
        }

        @Override
        public void start() {
            $.dialog(viewer).inputText(hint, lastval == null ? $(idValue).text().replace(valueSuffix,"") : lastval, type, this);
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
                    value = verify.verify(value);
                } catch (VerifyException e) {
                    $.toast(viewer).makeToastShort(e.getMessage());
                    return false;
                }
            }
            lastval = value;
            $(idValue).text(value + valueSuffix);
            if (key != null && input != null) {
                cacher.put(key, value);
            }
            if (bind != null) {
                bind.text(this, value);
            }
            return true;
        }

        public TextBinder inputType(int type) {
            this.type = type;
            return self();
        }

        public TextBinder suffix(String valueSuffix) {
            this.valueSuffix = valueSuffix;
            return self();
        }

        public TextBinder bind(TextBind bind) {
            this.bind = bind;
            return self();
        }

        //<editor-fold desc="输入验证">
        /**
         * 自定义验证规则
         */
        public TextBinder verify(TextVerify verify) {
            this.verify = verify;
            return self();
        }

        public TextBinder verifyMaxByte(int max, String... names) {
            CharSequence name = getName("值", names);
            return this.verify(text -> {
                if (text.getBytes(Charset.forName("gbk")).length > max) {
                    throw new VerifyException(name + "不能超过" + max + "个字符");
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
                if (!text.matches("1[345789]\\d{9}")) {
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
                    Double v = Double.parseDouble(text);
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
                int y = Integer.valueOf(o.substring(6, 10));
                int m = Integer.valueOf(o.substring(10, 12)) - 1;
                int d = Integer.valueOf(o.substring(12, 14));
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
                    h = h + Integer.valueOf(o.charAt(g)+"") * b[g];
                }
                o += ""+n[h %= 11];
                if (text.length() == 18 && !text.toLowerCase(Locale.ENGLISH).equals(o)) {
                    throw new VerifyException(name + "最后一位校验码输入错误，正确校验码为：" + o.substring(17, 18) + "！");
                }
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
            $.dialog(viewer).inputLines(hint, lastval == null ? $(idValue).text().replace(valueSuffix,"") : lastval, type, this);
        }
    }

    public abstract class AbstractDateBinder<T extends Binder> extends Binder<T, Date> {

        DateBind bind;
        List<DateVerify> verifies = new ArrayList<>();
        DateFormat format = AfDateFormat.DATE;
        boolean isManual = false;

        AbstractDateBinder(int idValue) {
            super(idValue);
        }

        public abstract T value(Date date);

        public T format(DateFormat format) {
            this.format = format;
            return self();
        }

        public T format(String format) {
            return format(new SimpleDateFormat(format, Locale.CHINA));
        }

        public T initNow() {
            return value(new Date());
        }

        public T bind(DateBind bind) {
            this.bind = bind;
            return self();
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
            return this.verify(date -> {
                if (date.getTime() < System.currentTimeMillis()) {
                    throw new VerifyException(name + "不能是现在之前");
                }
            });
        }

        /**
         * 指定为今天之后的时间
         */
        public T verifyAfterToday(String... names) {
            CharSequence name = getName("日期", names);
            return this.verify(date -> {
                long today = AfDateFormat.roundDate(new Date(new Date().getTime() + 24L * 60 * 60 * 1000)).getTime() - 1;
                if (date.getTime() < today) {
                    throw new VerifyException(name + "必须是今天以后");
                }
            });
        }

        /**
         * 指定为今天之后的时间
         */
        public T verifyAfterWithToday(String... names) {
            CharSequence name = getName("日期", names);
            return this.verify(date -> {
                long today = AfDateFormat.roundDate(new Date()).getTime() - 1;
                if (date.getTime() < today) {
                    throw new VerifyException(name + "不能早于今天");
                }
            });
        }

        public T verifyBefore(AbstractDateBinder<? extends AbstractDateBinder> binder, String... names) {
            CharSequence name = getName("时间", names);
            return this.verify(date -> {
                if (binder.lastval != null && date.getTime() >= binder.lastval.getTime()) {
                    throw new VerifyException(name + "必须早于" + binder.getName("时间", names));
                }
            });
        }

        public T verifyBeforeWith(AbstractDateBinder<? extends AbstractDateBinder> binder, String... names) {
            CharSequence name = getName("时间", names);
            return this.verify(date -> {
                if (binder.lastval != null && date.getTime() > binder.lastval.getTime()) {
                    if (binder.isManual) {
                        throw new VerifyException(name + "不能晚于" + binder.getName("时间", names));
                    } else {
                        binder.value(date);
                    }
                }
            });
        }

        public T verifyAfter(AbstractDateBinder<? extends AbstractDateBinder> binder, String... names) {
            CharSequence name = getName("时间", names);
            return this.verify(date -> {
                if (binder.lastval != null && date.getTime() <= binder.lastval.getTime()) {
                    throw new VerifyException(name + "必须晚于" + binder.getName("时间", names));
                }
            });
        }

        public T verifyAfterWith(AbstractDateBinder<? extends AbstractDateBinder> binder, String... names) {
            CharSequence name = getName("时间", names);
            return this.verify(date -> {
                if (binder.lastval != null && date.getTime() < binder.lastval.getTime()) {
                    if (binder.isManual) {
                        throw new VerifyException(name + "不能早于" + binder.getName("时间", names));
                    } else {
                        binder.value(date);
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
            $.dialog(viewer).selectDate(hint, lastval == null ? new Date() : lastval, this);
        }

        public DateBinder value(@Nullable Date date) {
            if (date != null) {
                lastval = AfDateFormat.roundDate(date);
                Calendar now = Calendar.getInstance();
                now.setTime(date);
                onDateSet(null, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
            }
            return self();
        }

        @Override
        public boolean onPreDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            if (verifies != null && view != null) {
                try {
                    for (DateVerify verify : verifies) {
                        verify.verify(AfDateFormat.parser(year,month,dayOfMonth));
                    }
                } catch (VerifyException e) {
                    $.toast(viewer).makeToastShort(e.getMessage());
                    return false;
                }
            }
//            onDateSet(view, year, month, dayOfMonth);
            return true;
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            isManual = view != null;
            lastval = AfDateFormat.parser(year, month, day);
            $(idValue).text(format.format(lastval));
            if (bind != null) {
                bind.date(this, lastval);
            }
        }

    }

    public class MonthBinder extends AbstractDateBinder<MonthBinder> {

        private static final int YEAR_MIN = 2000;
        private static final int YEAR_MAX = 3000;

        MonthBinder(int idValue) {
            super(idValue);
            format = AfDateFormat.DATE;
        }

        @Override
        public void start() {
            Calendar calendar = Calendar.getInstance();
            if (lastval != null) {
                calendar.setTime(lastval);
            }
            TextView txtYear = new TextView(viewer.getContext());
            TextView txtMonth = new TextView(viewer.getContext());
            NumberPicker year = new NumberPicker(viewer.getContext());
            NumberPicker month = new NumberPicker(viewer.getContext());
            year.setMinValue(YEAR_MIN);
            year.setMaxValue(YEAR_MAX);
            month.setMinValue(1);
            month.setMaxValue(12);
            year.setValue(calendar.get(Calendar.YEAR));
            month.setValue(calendar.get(Calendar.MONTH) + 1);
            txtYear.setText("年");
            txtMonth.setText("月");
            View view = $(new LinearLayout(viewer.getContext()))
                    .addView(year).addView(txtYear)
                    .addView(month).addView(txtMonth)
                    .gravity(Gravity.CENTER_VERTICAL).view();
            Dialog dialog = $.dialog(viewer).showViewDialog(hint, view, "取消", null, "选择", null);
            if (dialog instanceof AlertDialog) {
                ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE)
                        .setOnClickListener(new SafeListener((View.OnClickListener) v -> {
                            if (onPreDateSet(view, year.getValue(), month.getValue() - 1)) {
                                dialog.dismiss();
                            }
                        }));
            }
        }

        public MonthBinder value(Date date) {
            lastval = AfDateFormat.roundDate(date);
            Calendar now = Calendar.getInstance();
            now.setTime(date);
            onDateSet(null, now.get(Calendar.YEAR), now.get(Calendar.MONTH));
            return self();
        }

        public boolean onPreDateSet(View view, int year, int month) {
            if (verifies != null && view != null) {
                try {
                    for (DateVerify verify : verifies) {
                        verify.verify(AfDateFormat.parser(year, month, 1));
                    }
                } catch (VerifyException e) {
                    $.toast(viewer).makeToastShort(e.getMessage());
                    return false;
                }
            }
            onDateSet(view, year, month);
            return true;
        }

        public void onDateSet(View view, int year, int month) {
            isManual = view != null;
            lastval = AfDateFormat.parser(year, month, 1);
            $(idValue).text(format.format(lastval));
            if (bind != null) {
                bind.date(this, lastval);
            }
        }

    }

    public class TimeBinder extends AbstractDateBinder<TimeBinder> implements OnTimeSetVerifyListener {

        TimeBinder(int idValue) {
            super(idValue);
            format = new SimpleDateFormat("yyyy年MM月", Locale.CHINA);
        }

        @Override
        public void start() {
            $.dialog(viewer).selectTime(hint, lastval == null ? new Date() : lastval, this);
        }

        public TimeBinder value(Date date) {
            lastval = date;
            Calendar now = Calendar.getInstance();
            now.setTime(date);
            onTimeSet(null, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE));
            return self();
        }

        @Override
        public boolean onPreTimeSet(TimePickerDialog dialog, TimePicker view, int hourOfDay, int minute) {
            if (verifies != null && view != null) {
                try {
                    for (DateVerify verify : verifies) {
                        verify.verify(AfDateFormat.parser(hourOfDay,minute));
                    }
                } catch (VerifyException e) {
                    $.toast(viewer).makeToastShort(e.getMessage());
                    return false;
                }
            }
            if (taskBuilder != null) {
                Builder builded = taskBuilder.builder(AfDateFormat.parser(hourOfDay, minute));
                if (builded instanceof WaitBuilder) {
                    WaitBuilder builder = (WaitBuilder) builded;
                    Runnable success = builder.success();
                    builder.success(() -> {
                        if (success != null) {
                            success.run();
                        }
                        onTimeSet(view, hourOfDay, minute);
                        dialog.dismiss();
                    });
                    builded.post();
                    return false;
                } else if (builded != null) {
                    builded.post();
                }
            }
//            onTimeSet(view, hourOfDay, minute);
            return true;
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            isManual = view != null;
            lastval = AfDateFormat.parser(hourOfDay, minute);
            $(idValue).text(format.format(lastval));
            if (bind != null) {
                bind.date(this, lastval);
            }
        }
    }

    public class DateTimeBinder extends AbstractDateBinder<DateTimeBinder> implements OnDateTimeSetVerifyListener {

        DateTimeBinder(int idValue) {
            super(idValue);
            format = AfDateFormat.STANDARD;
        }

        @Override
        public void start() {
            $.dialog(viewer).selectDateTime(hint, lastval == null ? new Date() : lastval, this);
        }

        public DateTimeBinder value(Date date) {
            lastval = date;
            Calendar now = Calendar.getInstance();
            now.setTime(date);
            onDateTimeSet(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH),
                    now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE));
            return self();
        }

        @Override
        public boolean onPreDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            if (verifies != null && view != null) {
                try {
                    for (DateVerify verify : verifies) {
                        verify.verify(AfDateFormat.parser(year,month,dayOfMonth));
                    }
                } catch (VerifyException e) {
                    $.toast(viewer).makeToastShort(e.getMessage());
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
                        verify.verify(AfDateFormat.parser(hourOfDay, minute));
                    }
                    isManual = true;
                } catch (VerifyException e) {
                    $.toast(viewer).makeToastShort(e.getMessage());
                    return false;
                }
            }
            return true;
        }

        @Override
        public void onDateTimeSet(int year, int month, int day, int hour, int minute) {
            lastval = AfDateFormat.parser(year, month, day, hour, minute);
            $(idValue).text(format.format(lastval));
            if (bind != null) {
                bind.date(this, lastval);
            }
        }
    }

    public class CheckBinder extends Binder<CheckBinder, Boolean> {

        private CheckBind bind;

        CheckBinder(int idValue) {
            super(idValue);
            lastval = $(idValue).isChecked();
        }

        @Override
        public void onRestoreCache(String key) {
            Boolean bool = cacher.get(key, null, Boolean.class);
            if (bool != null) {
                value(bool);
            }
        }

        public CheckBinder value(boolean isChecked) {
            lastval = isChecked;
            $(idValue).checked(isChecked);
            if (bind != null) {
                bind.check(this, isChecked);
            }
            return self();
        }

        @Override
        public void onClick(View v) {
            if (v != null && v.getId() != idValue) {
                lastval = $(idValue).toggle().isChecked();
            }
            super.onClick(v);
        }

        @Override
        public void start() {
            lastval = $(idValue).isChecked();
            if (key != null) {
                cacher.put(key, lastval);
            }
            if (bind != null) {
                bind.check(this, lastval);
            }
        }

        public CheckBinder bind(CheckBind bind) {
            this.bind = bind;
            return self();
        }
    }

    public class SwitchBinder extends CheckBinder {
        SwitchBinder(int idValue) {
            super(idValue);
        }
    }

    public class SeekBarBinder extends Binder<SeekBarBinder, Integer> implements SeekBar.OnSeekBarChangeListener {

        private SeekBind bind;

        SeekBarBinder(int idValue) {
            super(idValue);
            SeekBar view = $(idValue).view(SeekBar.class);
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

        public SeekBarBinder bind(SeekBind bind) {
            this.bind = bind;
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

    public class ActivityBinder extends Binder<ActivityBinder, Void> {

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

    public class FragmentBinder extends Binder<FragmentBinder, Void> {

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

    public class ImageBinder extends Binder<ImageBinder, Void> {

        protected int outPutX = 0;           //裁剪保存宽度
        protected int outPutY = 0;           //裁剪保存高度
        protected int request_image = 1000;
        protected ImageBind bind;
        protected CropImageView.Style style = CropImageView.Style.RECTANGLE;

        protected ImageBinder(int idImage) {
            super(idImage);
        }

        @Override
        protected void start() {
            ImagePicker picker = ImagePicker.getInstance();
            picker.setMultiMode(false);
            picker.setShowCamera(true);
            picker.setStyle(style);
            if (outPutX > 0 && outPutY > 0) {
                picker.setCrop(true);
                picker.setOutPutX(outPutX);
                picker.setOutPutY(outPutY);

                DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();

                int focusWidth = metrics.widthPixels * 3 / 4;
                int focusHeight = focusWidth * outPutY / outPutX;

                if (focusHeight > metrics.heightPixels * 3 / 4) {
                    focusHeight = metrics.heightPixels * 3 / 4;
                    focusWidth = focusHeight * outPutX / outPutY;
                }

                picker.setFocusWidth(focusWidth);
                picker.setFocusHeight(focusHeight);
            } else {
                picker.setCrop(false);
            }
            $.pager().startActivityForResult(ImageGridActivity.class, request_image);
            //viewer.startActivityForResult(ImageGridActivity.class,request_image);
        }

        public ImageBinder image(String url) {
            $.query(viewer).$(idValue).image(url);
            return self();
        }

        public void requestimage(int request_image) {
            this.request_image = request_image;
        }

        public ImageBinder circle() {
            style = CropImageView.Style.CIRCLE;
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
            if (requestCode == request_image /*&& resultCode == Activity.RESULT_OK*/) {
                new CropImageView(ApApp.get()).setOnBitmapSaveCompleteListener(null);
                //noinspection unchecked
                List<ImageItem> images = (ArrayList<ImageItem>) intent.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (images != null && images.size() > 0) {
                    if (bind != null && !bind.image(this, images.get(0).path)) {
                        $.query(viewer).$(idValue).image(images.get(0).path);
                    }
//                } else {
//                    $.toast(viewer).makeToastShort("没有数据");
                }
            }
        }

        public ImageBinder bind(ImageBind bind) {
            this.bind = bind;
            return self();
        }

    }

    public class RadioGroupBinder extends Binder<RadioGroupBinder,Integer> implements RadioGroup.OnCheckedChangeListener {

        private RadioGroupBind bind;

        RadioGroupBinder(int idValue) {
            super(idValue);
            $(idValue).clicked(null).foreach(RadioGroup.class, (ViewQuery.ViewEacher<RadioGroup>) view -> {
                view.setOnCheckedChangeListener(this);
            });
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

        public RadioGroupBinder bind(RadioGroupBind bind) {
            this.bind = bind;
            return self();
        }
    }
}