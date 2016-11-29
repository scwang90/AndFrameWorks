package com.andpack.impl;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.andframe.$;
import com.andframe.api.DialogBuilder;
import com.andframe.api.pager.Pager;
import com.andframe.api.view.ViewQuery;
import com.andframe.caches.AfPrivateCaches;
import com.andframe.feature.AfIntent;
import com.andframe.util.java.AfDateFormat;
import com.andpack.activity.ApFragmentActivity;
import com.andpack.application.ApApp;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@SuppressWarnings("unused")
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

    public ViewQuery $(Integer id, int... ids) {
        return query.$(id, ids);
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
        void text(Binder binder, String text, int count, boolean[] checkedItems);
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

    public interface TextVerify {
        void verify(String text) throws VerifyException;
    }

    public interface MultiChoiceVerify {
        void verify(int count, boolean[] checkedItems) throws VerifyException;
    }

    public static class VerifyException extends Exception {
        public VerifyException(String message) {
            super(message);
        }
    }

    public abstract class Binder<T extends Binder, LASTVAL> implements View.OnClickListener{
        public int idvalue;
        public String key = null;
        public CharSequence hint = "请输入";
        public Binder next;
        public LASTVAL lastval;
        public Runnable start;

        Binder(int idvalue) {
            this.idvalue = idvalue;
            $(idvalue).clicked(this);
        }

        public T click(int idclick) {
            $(idclick).clicked(this);
            $(idvalue).clicked(null);
            return self();
        }

        public T hint(String hint) {
            this.hint = hintPrefix + hint;
            return self();
        }

        public T hintTextViewId(int id) {
            this.hint = hintPrefix + $(id).getText();
            return self();
        }

        public T hintResId(int id) {
            this.hint = hintPrefix + pager.getContext().getString(id);
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
            value(i);
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

        public SelectBinder value(int index) {
            if (index >= 0 && index < items.length) {
                onClick(null, index);
            }
            return self();
        }

        public SelectBinder value(String value) {
            for (int i = 0; i < items.length; i++) {
                if (TextUtils.equals(items[i].toString(), value)) {
                    onClick(null, i);
                }
            }
            return self();
        }
    }

    public class MultiChoiceBinder extends Binder<MultiChoiceBinder, Void> implements DialogInterface.OnClickListener {

        private boolean[] checkedItems;
        private CharSequence[] items;
        private MultiChoiceLambda lambda;
        private MultiChoiceVerify verify;

        MultiChoiceBinder(int idvalue, CharSequence[] items) {
            super(idvalue);
            this.items = items;
            this.checkedItems = new boolean[items.length];
        }

        public MultiChoiceBinder verify(MultiChoiceVerify verify) {
            this.verify = verify;
            return self();
        }

        @Override
        public void start() {
            $.dialog(pager).multiChoice(hint, items, checkedItems, null, this);
        }

        @Override
        public void onRestoreCache(String key) {
            List<Boolean> list = caches.getList(key, Boolean.class);
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
                onClick(new Dialog(pager.getContext()), 0);
            }
            return self();
        }

        public MultiChoiceBinder lambda(MultiChoiceLambda lambda) {
            this.lambda = lambda;
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
                    pager.makeToastShort(e.getMessage());
                    return;
                }
            }
            $(idvalue).text(builder);
            if (key != null && dialog != null) {
                List<Boolean> list = new ArrayList<>(checkedItems.length);
                for (boolean bool : checkedItems) {
                    list.add(bool);
                }
                caches.putList(key, list);
            }
            if (lambda != null) {
                lambda.text(this, builder.toString(), count, checkedItems);
            }
        }
    }

    public class TextBinder extends Binder<TextBinder, String> implements DialogBuilder.InputTextListener {

        private int type = InputType.TYPE_CLASS_TEXT;
        private TextLambda lambda;
        private TextVerify verify;
        private String valueSuffix = "";

        TextBinder(int idvalue) {
            super(idvalue);
        }

        @Override
        public void start() {
            $.dialog(pager).inputText(hint, lastval == null ? $(idvalue).getText() : lastval, type, this);
        }

        public TextBinder value(String text) {
            if (!TextUtils.isEmpty(text)) {
                onInputTextComfirm($(idvalue).getEditText(), text);
            }
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
            if (verify != null && input != null) {
                try {
                    verify.verify(value);
                } catch (VerifyException e) {
                    pager.makeToastShort(e.getMessage());
                    return false;
                }
            }
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

        public TextBinder verify(TextVerify verify) {
            this.verify = verify;
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
        public DateBinder initNow() {
            return value(new Date());
        }

        public DateBinder value(Date date) {
            Calendar now = Calendar.getInstance();
            now.setTime(date);
            onDateSet(null, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
            return self();
        }

        public DateBinder format(DateFormat format) {
            this.format = format;
            return self();
        }

        public DateBinder format(String format) {
            return format(new SimpleDateFormat(format, Locale.CHINA));
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
            lastval = $(idvalue).isChecked();
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
        public void onClick(View v) {
            if (v != null && v.getId() != idvalue) {
                lastval = $(idvalue).toggle().isChecked();
            }
            super.onClick(v);
        }

        @Override
        public void start() {
            lastval = $(idvalue).isChecked();
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
        private CropImageView.Style style = CropImageView.Style.RECTANGLE;

        ImageBinder(int idimage) {
            super(idimage);
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
            pager.startActivityForResult(ImageGridActivity.class,request_image);
        }

        public ImageBinder image(String url) {
            $.query(pager).$(idvalue).image(url);
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

        public void onActivityResult(AfIntent intent, int requestcode, int resultcode) {
            if (requestcode == request_image /*&& resultcode == Activity.RESULT_OK*/) {
                new CropImageView(ApApp.get()).setOnBitmapSaveCompleteListener(null);
                //noinspection unchecked
                List<ImageItem> images = (ArrayList<ImageItem>) intent.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (images != null && images.size() > 0) {
                    $.query(pager).$(idvalue).image(images.get(0).path);
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