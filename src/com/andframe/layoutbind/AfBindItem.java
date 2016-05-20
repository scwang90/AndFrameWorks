package com.andframe.layoutbind;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.andframe.activity.framework.AfView;
import com.andframe.network.AfImageService;
import com.andframe.util.java.AfDateFormat;
import com.andframe.util.java.AfReflecter;
import com.andframe.view.treeview.AfTreeViewItem;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Set;

public class AfBindItem<T> extends AfTreeViewItem<T> {

    View[] bindViews = null;
    BindItemMap bindMap;

    public AfBindItem(int layoutId, BindItemMap bindMap) {
        super(layoutId);
        this.bindMap = bindMap;
    }

    public AfBindItem(int layoutId, Object... binds) {
        super(layoutId);
        this.bindMap = new BindItemMap();
        for (int i = 0; i < binds.length / 2; i++) {
            if (binds[2 * i] instanceof String && binds[2 * i + 1] instanceof Integer) {
                bindMap.putBindMap((String) binds[2 * i], (Integer) binds[2 * i + 1]);
            }
        }
    }

    @Override
    public void onHandle(AfView view) {
        super.onHandle(view);
        int index = 0;
        bindViews = new View[bindMap.size()];
        for (Entry<String, Integer> entry : bindMap.entrySet()) {
            if (entry.getValue() <= 0) {
                bindViews[index] = view.getView();
            } else {
                bindViews[index] = view.findViewById(entry.getValue());
            }
            index++;
        }
    }

    @Override
    protected boolean onBinding(T model, int i, int level, boolean isExpanded, SelectStatus status) {
        int index = 0;
        for (Entry<String, Integer> entry : bindMap.entrySet()) {
            View view = bindViews[index];
            String key = entry.getKey().replaceAll(":.*", "");
            String format = entry.getKey().substring(key.length());
            Object value;
            if ("".equals(key)) {
                value = model;
            } else {
                value = AfReflecter.getMemberNoException(model, key);
            }
            if (format.length() > 0) {
                format = format.substring(1);
            }
            if (view instanceof TextView) {
                TextView textView = (TextView) view;
                if (value == null) {
                    textView.setText("");
                } else if (value instanceof Date) {
                    Date date = (Date) value;
                    if (format.length() > 0) {
                        if (format.equals("time")) {
                            textView.setText(AfDateFormat.formatTime(date));
                        } else if (format.equals("date")) {
                            textView.setText(AfDateFormat.formatDate(date));
                        } else {
                            textView.setText(AfDateFormat.format(format, date));
                        }
                    } else {
                        textView.setText(AfDateFormat.FULL.format(date));
                    }
                } else {
                    textView.setText(value.toString());
                }
            } else if (view instanceof ImageView) {
                ImageView imageView = (ImageView) view;
                if (value == null) {
                    imageView.setImageBitmap(null);
                } else {
                    if (value instanceof String) {
                        AfImageService.bindImage(value.toString(), imageView);
                    } else if (value instanceof Integer) {
                        imageView.setImageResource((int) value);
                    } else if (value instanceof Bitmap) {
                        Bitmap bitmap = (Bitmap) value;
                        imageView.setImageBitmap(bitmap);
                    } else if (value instanceof Drawable) {
                        Drawable drawable = (Drawable) value;
                        imageView.setImageDrawable(drawable);
                    }
                }
            }
            index++;
        }
        return false;
    }

    public static class BindItemMap {

        LinkedHashMap<String, Integer> bindMap;

        public BindItemMap() {
            this.bindMap = new LinkedHashMap<String, Integer>();
        }

        public BindItemMap(Object... args) {
            this.bindMap = new LinkedHashMap<String, Integer>();
            for (int i = 0; i < args.length / 2; i++) {
                if (args[2 * i] instanceof String && args[2 * i + 1] instanceof Integer) {
                    bindMap.put((String) args[2 * i], (Integer) args[2 * i + 1]);
                }
            }
        }

        public BindItemMap putBindMap(String field, int viewId) {
            bindMap.put(field, viewId);
            return this;
        }

        public int size() {
            return bindMap.size();
        }

        public Set<Entry<String, Integer>> entrySet() {
            return bindMap.entrySet();
        }
    }
}
