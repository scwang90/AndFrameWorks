package com.andpack.impl;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.andframe.$;
import com.andframe.api.DialogBuilder;
import com.andframe.api.page.Pager;
import com.andframe.api.view.ViewQuery;
import com.andframe.caches.AfPrivateCaches;
import com.andframe.feature.AfIntent;
import com.andframe.util.java.AfDateFormat;
import com.andpack.activity.ApFragmentActivity;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ApCommonBarBinder {

    private Pager pager;
    private ViewQuery query;
    private String hintPrefix = "";
    private AfPrivateCaches caches;

    public ApCommonBarBinder(Pager pager) {
        this.pager = pager;
        this.query = $.query(pager);
        this.caches = AfPrivateCaches.getInstance(pager.getClass().getName());
    }

    public void setHintPrefix(String hintPrefix) {
        this.hintPrefix = hintPrefix;
    }

    public ViewQuery $(int... id) {
        return query.id(id);
    }

    public TextBinder text(int idvalue) {
        return new TextBinder(idvalue);
    }

    public SelectBinder select(int idvalue, CharSequence[] items) {
        return new SelectBinder(idvalue, items);
    }

    public CheckBinder check(int idvalue) {
        return new CheckBinder(idvalue);
    }

    public DateBinder date(int idvalue) {
        return new DateBinder(idvalue);
    }

    public MultiChoiceBinder multiChoice(int idvalue, CharSequence[] items) {
        return new MultiChoiceBinder(idvalue, items);
    }

    public ActivityBinder activity(int idvalue, Class<? extends Activity> clazz, Object... args) {
        return new ActivityBinder(idvalue, clazz, args);
    }

    public FragmentBinder fragment(int idvalue, Class<? extends Fragment> clazz, Object... args) {
        return new FragmentBinder(idvalue, clazz, args);
    }

    public ImageBinder image(int idimage) {
        return new ImageBinder(idimage);
    }

    public interface SelectLambda {
        void text(Binder binder, String text, int which);
    }

    public interface MultiChoiceLambda {
        void text(Binder binder, String text, boolean[] checkedItems);
    }

    public interface TextLambda {
        void text(Binder binder, String text);
    }

    public interface DateLambda {
        void text(Binder binder, Date date);
    }

    public interface CheckLambda {
        void check(Binder binder, boolean isChecked);
    }

    public interface ImageLambda {
        void image(Binder binder, String path);
    }

    public abstract class Binder<T extends Binder, LASTVAL> implements View.OnClickListener{
        int idvalue;
        public String key = null;
        public CharSequence hint = "请输入";
        public Binder next;
        public LASTVAL lastval;

        Binder(int idvalue) {
            this.idvalue = idvalue;
            $(idvalue).clicked(this);
        }

        public T click(int idclick) {
            $(idclick).clicked(this);
            return self();
        }

        public T hint(int idhint) {
            this.hint = hintPrefix + $(idhint).getText();
            return self();
        }

        public T hint(String hint) {
            this.hint = hintPrefix + hint;
            return self();
        }

        public T cache(Object... keys) {
            if (keys.length == 0) {
                key = String.valueOf(idvalue);
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
            start();
            if (next != null) {
                next.performStart();
            }
        }

        protected abstract void start();

        T self() {
            //noinspection unchecked
            return (T)this;
        }
    }

    public class SelectBinder extends Binder<SelectBinder, Void> implements DialogInterface.OnClickListener {

        private SelectLambda lambda;
        private final CharSequence[] items;

        SelectBinder(int idvalue, CharSequence[] items) {
            super(idvalue);
            this.items = items;
        }

        @Override
        public void start() {
            $.dialog(pager).selectItem(hint, items, this);
        }

        @Override
        public void onRestoreCache(String key) {
            int i = caches.getInt(key, -1);
            if (i >= 0 && i < items.length) {
                onClick(null, i);
            }
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            $(idvalue).text(items[which]);
            if (key != null && dialog != null) {
                caches.put(key, which);
            }
            if (lambda != null) {
                lambda.text(this, items[which].toString(), which);
            }
        }

        public SelectBinder lambda(SelectLambda lambda) {
            this.lambda = lambda;
            return self();
        }
    }

    public class MultiChoiceBinder extends Binder<MultiChoiceBinder, Void> implements DialogInterface.OnMultiChoiceClickListener {

        private boolean[] checkedItems;
        private CharSequence[] items;
        private MultiChoiceLambda lambda;

        MultiChoiceBinder(int idvalue, CharSequence[] items) {
            super(idvalue);
            this.items = items;
            this.checkedItems = new boolean[items.length];
        }

        @Override
        public void start() {
            $.dialog(pager).multiChoice(hint, items, checkedItems, this);
        }

        @Override
        public void onRestoreCache(String key) {
            List<Boolean> list = caches.getList(key, Boolean.class);
            if (list != null && list.size() == checkedItems.length) {
                for (int i = 0; i < checkedItems.length; i++) {
                    checkedItems[i] = list.get(i);
                }
                onClick(null, 0, checkedItems[0]);
            }
//            boolean[] booleen = caches.get(key, null, boolean[].class);
//            if (booleen != null && booleen.length == items.length) {
//                checkedItems = booleen;
//                onClick(null, 0, booleen[0]);
//            }
        }

        @Override
        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < items.length; i++) {
                if (checkedItems[i]) {
                    builder.append(',');
                    builder.append(items[i].toString());
                }
            }
            if (builder.length() > 0) {
                $(idvalue).text(builder.substring(1));
            } else {
                $(idvalue).text("");
            }
            if (key != null && dialog != null) {
                List<Boolean> list = new ArrayList<>(checkedItems.length);
                for (boolean bool : checkedItems) {
                    list.add(bool);
                }
                caches.putList(key, list);
//                caches.put(key, checkedItems);
            }
            if (lambda != null) {
                lambda.text(this, builder.toString(), checkedItems);
            }
        }

        public MultiChoiceBinder lambda(MultiChoiceLambda lambda) {
            this.lambda = lambda;
            return self();
        }
    }

    public class TextBinder extends Binder<TextBinder, String> implements DialogBuilder.InputTextListener {

        private int type = InputType.TYPE_CLASS_TEXT;
        private TextLambda lambda;
        private String valueSuffix = "";

        TextBinder(int idvalue) {
            super(idvalue);
        }

        @Override
        public void start() {
            $.dialog(pager).inputText(hint, lastval == null ? $(idvalue).getText() : lastval, type, this);
        }

        public TextBinder value(String text) {
            onInputTextComfirm($(idvalue).getEditText(), text);
            return self();
        }

        @Override
        public void onRestoreCache(String key) {
            String text = caches.getString(key, null);
            if (text != null) {
                onInputTextComfirm(null, text);
            }
        }

        @Override
        public boolean onInputTextComfirm(EditText input, String value) {
            lastval = value;
            $(idvalue).text(value + valueSuffix);
            if (key != null && input != null) {
                caches.put(key, value);
            }
            if (lambda != null) {
                lambda.text(this, value);
            }
            return true;
        }

        public TextBinder type(int type) {
            this.type = type;
            return self();
        }

        public TextBinder valueSuffix(String valueSuffix) {
            this.valueSuffix = valueSuffix;
            return self();
        }

        public TextBinder lambda(TextLambda lambda) {
            this.lambda = lambda;
            return self();
        }
    }

    public class DateBinder extends Binder<DateBinder, Date> implements DatePickerDialog.OnDateSetListener {

        private DateLambda lambda;
        private DateFormat format = AfDateFormat.DATE;

        DateBinder(int idvalue) {
            super(idvalue);
        }

        @Override
        public void start() {
            $.dialog(pager).selectDate(hint, lastval == null ? new Date() : lastval, this);
        }

        @SuppressWarnings("unused")
        public void initNow() {
            value(new Date());
        }

        private void value(Date date) {
            Calendar now = Calendar.getInstance();
            now.setTime(date);
            onDateSet(null, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
        }

        public DateBinder format(DateFormat format) {
            this.format = format;
            return self();
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            lastval = AfDateFormat.parser(year, month, day);
            $(idvalue).text(format.format(lastval));
            if (lambda != null) {
                lambda.text(this, lastval);
            }
        }

        public DateBinder lambda(DateLambda lambda) {
            this.lambda = lambda;
            return self();
        }
    }

    public class CheckBinder extends Binder<CheckBinder, Boolean> {

        private CheckLambda lambda;

        CheckBinder(int idvalue) {
            super(idvalue);
        }

        @Override
        public void onRestoreCache(String key) {
            Boolean bool = caches.get(key, null, Boolean.class);
            if (bool != null) {
                value(bool);
            }
        }

        public CheckBinder value(boolean isChecked) {
            lastval = isChecked;
            $(idvalue).checked(isChecked);
            if (lambda != null) {
                lambda.check(this, isChecked);
            }
            return self();
        }

        @Override
        public void start() {
            lastval = $(idvalue).toggel().isChecked();
            if (key != null) {
                caches.put(key, lastval);
            }
            if (lambda != null) {
                lambda.check(this, lastval);
            }
        }

        public CheckBinder lambda(CheckLambda lambda) {
            this.lambda = lambda;
            return self();
        }
    }

    public class ActivityBinder extends Binder<ActivityBinder, Void> {

        private final Object[] args;
        private Class<? extends Activity> activity;

        ActivityBinder(int idvalue, Class<? extends Activity> activity, Object... args) {
            super(idvalue);
            this.args = args;
            this.activity = activity;
        }

        @Override
        public void start() {
            pager.startActivity(activity, args);
        }

    }

    public class FragmentBinder extends Binder<FragmentBinder, Void> {

        private final Object[] args;
        private Class<? extends Fragment> fragment;

        FragmentBinder(int idvalue, Class<? extends Fragment> fragment, Object... args) {
            super(idvalue);
            this.args = args;
            this.fragment = fragment;
        }

        @Override
        public void start() {
            ApFragmentActivity.start(fragment,args);
        }

    }

    public class ImageBinder extends Binder<ImageBinder, Void> {

        private int outPutX = 0;           //裁剪保存宽度
        private int outPutY = 0;           //裁剪保存高度
        private int request_image = 1000;
        private ImageLambda lambda;

        ImageBinder(int idimage) {
            super(idimage);
        }

        @Override
        protected void start() {
            ImagePicker picker = ImagePicker.getInstance();
            picker.setMultiMode(false);
            picker.setShowCamera(true);
            if (outPutX > 0 && outPutY > 0) {
                picker.setOutPutX(outPutX);
                picker.setOutPutY(outPutY);
                picker.setCrop(true);
            } else {
                picker.setCrop(false);
            }
            pager.startActivityForResult(ImageGridActivity.class,request_image);
        }

        public ImageBinder image(String url) {
            $.query(pager).id(idvalue).image(url);
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

        public void onActivityResult(AfIntent intent, int requestcode, int resultcode) {
            if (requestcode == request_image /*&& resultcode == Activity.RESULT_OK*/) {
                //noinspection unchecked
                List<ImageItem> images = (ArrayList<ImageItem>) intent.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (images != null && images.size() > 0) {
                    $.query(pager).id(idvalue).image(images.get(0).path);
                    if (lambda != null) {
                        lambda.image(this, images.get(0).path);
                    }
                } else {
                    pager.makeToastShort("没有数据");
                }
            }
        }

        public ImageBinder lambda(ImageLambda lambda) {
            this.lambda = lambda;
            return self();
        }

    }

}