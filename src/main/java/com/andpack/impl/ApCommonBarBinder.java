package com.andpack.impl;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.andframe.$;
import com.andframe.api.DialogBuilder;
import com.andframe.api.page.Pager;
import com.andframe.api.view.ViewQuery;
import com.andframe.feature.AfIntent;
import com.andframe.util.java.AfDateFormat;
import com.andpack.activity.ApFragmentActivity;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ApCommonBarBinder {

    private Pager pager;
    private ViewQuery query;
    private String hintPrefix = "";

    public ApCommonBarBinder(Pager pager) {
        this.pager = pager;
        this.query = $.query(pager);
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

    public CheckBinder check(int idvalue, boolean isChecked) {
        return new CheckBinder(idvalue, isChecked);
    }

    public DateBinder date(int idvalue) {
        return new DateBinder(idvalue);
    }

    public MultiChoiceBinder multiChoice(int idvalue, CharSequence[] items) {
        return new MultiChoiceBinder(idvalue, items);
    }

    public ActivityBinder activity(int idvalue, Class<? extends Activity> clazz) {
        return new ActivityBinder(idvalue, clazz);
    }

    public FragmentBinder fragment(int idvalue, Class<? extends Fragment> clazz) {
        return new FragmentBinder(idvalue, clazz);
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

    public interface DateDefaulter {
        Date date();
    }

    public interface TextDefaulter {
        CharSequence text();
    }

    public abstract class Binder<T extends Binder> implements View.OnClickListener{
        int idvalue;
        public CharSequence hint = "请输入";
        public Binder next;

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

    public class SelectBinder extends Binder<SelectBinder> implements DialogInterface.OnClickListener {

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
        public void onClick(DialogInterface dialog, int which) {
            $(idvalue).text(items[which]);
            if (lambda != null) {
                lambda.text(this, items[which].toString(), which);
            }
        }

        public SelectBinder lambda(SelectLambda lambda) {
            this.lambda = lambda;
            return self();
        }
    }

    public class MultiChoiceBinder extends Binder<MultiChoiceBinder> implements DialogInterface.OnMultiChoiceClickListener {

        private final boolean[] checkedItems;
        private final CharSequence[] items;
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
            if (lambda != null) {
                lambda.text(this, builder.toString(), checkedItems);
            }
        }

        public MultiChoiceBinder lambda(MultiChoiceLambda lambda) {
            this.lambda = lambda;
            return self();
        }
    }

    public class TextBinder extends Binder<TextBinder> implements DialogBuilder.InputTextListener {

        private int type = InputType.TYPE_CLASS_TEXT;
        private TextLambda lambda;
        private String valueSuffix = "";
        private TextDefaulter defaulter = () -> $.query(pager).id(idvalue).getText();

        TextBinder(int idvalue) {
            super(idvalue);
        }

        @Override
        public void start() {
            $.dialog(pager).inputText(hint, defaulter.text(), type, this);
        }

        @Override
        public boolean onInputTextComfirm(EditText input, String value) {
            $(idvalue).text(value + valueSuffix);
            if (lambda != null) {
                lambda.text(this, value);
            }
            return true;
        }

        public TextBinder type(int type) {
            this.type = type;
            return self();
        }

        public TextBinder defaulter(TextDefaulter defaulter) {
            this.defaulter = () -> TextUtils.isEmpty(defaulter.text())?"":defaulter.text();
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

    public class DateBinder extends Binder<DateBinder> implements DatePickerDialog.OnDateSetListener {

        private DateLambda lambda;
        private DateFormat format = AfDateFormat.DATE;
        private DateDefaulter defaulter = Date::new;

        DateBinder(int idvalue) {
            super(idvalue);
        }

        @Override
        public void start() {
            $.dialog(pager).selectDate(hint, defaulter.date(), this);
        }

        public DateBinder format(DateFormat format) {
            this.format = format;
            return self();
        }

        public DateBinder defaulter(DateDefaulter defaulter) {
            this.defaulter = () -> defaulter.date() == null ? new Date() : defaulter.date();
            return self();
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            Date date = AfDateFormat.parser(year, month, day);
            $(idvalue).text(format.format(date));
            if (lambda != null) {
                lambda.text(this, date);
            }
        }

        public DateBinder lambda(DateLambda lambda) {
            this.lambda = lambda;
            return self();
        }
    }

    public class CheckBinder extends Binder<CheckBinder> {

        private CheckLambda lambda;

        CheckBinder(int idvalue) {
            super(idvalue);
        }

        CheckBinder(int idvalue, boolean isChecked) {
            super(idvalue);
            $(idvalue).checked(isChecked);
        }

        @Override
        public void start() {
            if (lambda != null) {
                lambda.check(this, $(idvalue).toggel().isChecked());
            } else {
                $(idvalue).toggel();
            }
        }

        public CheckBinder lambda(CheckLambda lambda) {
            this.lambda = lambda;
            return self();
        }
    }

    public class ActivityBinder extends Binder<ActivityBinder> {

        private Class<? extends Activity> activity;

        ActivityBinder(int idvalue, Class<? extends Activity> activity) {
            super(idvalue);
            this.activity = activity;
        }

        @Override
        public void start() {
            pager.startActivity(activity);
        }

    }

    public class FragmentBinder extends Binder<FragmentBinder> {

        private Class<? extends Fragment> fragment;

        FragmentBinder(int idvalue, Class<? extends Fragment> fragment) {
            super(idvalue);
            this.fragment = fragment;
        }

        @Override
        public void start() {
            ApFragmentActivity.start(fragment);
        }

    }

    public class ImageBinder extends Binder<ImageBinder> {

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