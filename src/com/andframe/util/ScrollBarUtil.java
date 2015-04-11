package com.andframe.util;

import java.lang.reflect.Field;

import android.graphics.drawable.Drawable;
import android.widget.AbsListView;

public class ScrollBarUtil {

	public static void bindScrollBar(AbsListView listview, int resid) {
		// TODO Auto-generated constructor stub
		try {
			listview.setFastScrollEnabled(true);
			Field f = AbsListView.class.getDeclaredField("mFastScroller");
			f.setAccessible(true);
			Object o = f.get(listview);
			f = f.getType().getDeclaredField("mThumbDrawable");
			f.setAccessible(true);
			Drawable drawable = (Drawable) f.get(o);
			drawable = listview.getContext().getResources().getDrawable(resid);
			f.set(o, drawable);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}
