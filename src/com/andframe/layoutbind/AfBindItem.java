package com.andframe.layoutbind;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Set;

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

public class AfBindItem<T> extends AfTreeViewItem<T> {

	int layoutId = 0;
	View[] bindViews = null;
	BindItemMap bindMap;

	public AfBindItem(int layoutId, BindItemMap bindMap) {
		// TODO Auto-generated constructor stub
		this.layoutId = layoutId;
		this.bindMap = bindMap;
	}

	@Override
	public void onHandle(AfView view) {
		// TODO Auto-generated method stub
		int index = 0;
		bindViews = new View[bindMap.size()];
		for (Entry<String, Integer> entry : bindMap.entrySet()) {
			bindViews[index] = view.findViewById(entry.getValue());
			index++;
		}
	}

	@Override
	public int getLayoutId() {
		// TODO Auto-generated method stub
		return layoutId;
	}

	@Override
	protected boolean onBinding(T model, int level, boolean isExpanded,
			SelectStatus status) {
		// TODO Auto-generated method stub
		int index = 0;
		for (Entry<String, Integer> entry : bindMap.entrySet()) {
			View view = bindViews[index];
			Object value = AfReflecter.getMemberNoException(model,
					entry.getKey());
			if (view instanceof TextView) {
				TextView textView = (TextView) view;
				if (value == null) {
					textView.setText("");
				} else if(value instanceof Date) {
					Date date = (Date) value;
					textView.setText(AfDateFormat.FULL.format(date));
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
		public void putBindMap(String field,int viewId){
			bindMap.put(field, viewId);
		}
		public int size(){
			return bindMap.size();
		}
		public Set<Entry<String,Integer>> entrySet(){
			return bindMap.entrySet();
		}
	}
}
