package com.andframe.adapter.itemviewer;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.andframe.util.java.AfDateFormat;
import com.andframe.util.java.AfReflecter;
import com.andframe.widget.treeview.AfTreeViewItemViewer;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Set;

@SuppressWarnings("unused")
public class AfBindItemViewer<T> extends AfTreeViewItemViewer<T> {

    View[] bindViews = null;
    BindItemMap bindMap;

    public AfBindItemViewer(int layoutId, BindItemMap bindMap) {
        super(layoutId);
        this.bindMap = bindMap;
    }

    public AfBindItemViewer(int layoutId, Object... binds) {
        super(layoutId);
        this.bindMap = new BindItemMap(binds);
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        View view = mLayout;
        if (mTreeViewContent != null) {
            view = mTreeViewContent;
        }
        if (mMultiChoiceContent != null) {
            view = mMultiChoiceContent;
        }
        int index = 0;
        bindViews = new View[bindMap.size()];
        for (Entry<String, Integer> entry : bindMap.entrySet()) {
            if (entry.getValue() <= 0) {
                bindViews[index] = view;
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
                        if ("time".equals(format)) {
                            textView.setText(AfDateFormat.formatTime(date));
                        } else if ("date".equals(format)) {
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
                        $(imageView).image(value.toString());
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

        @SuppressWarnings("unused")
        public BindItemMap() {
            this.bindMap = new LinkedHashMap<>();
        }

        public BindItemMap(Object... args) {
            this.bindMap = new LinkedHashMap<>();
            for (int i = 0; i < args.length / 2; i++) {
                if (args[2 * i] instanceof String && args[2 * i + 1] instanceof Integer) {
                    putBindMap((String) args[2 * i], (Integer) args[2 * i + 1]);
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
