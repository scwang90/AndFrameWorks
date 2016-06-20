package com.andframe.util;

import android.graphics.drawable.Drawable;
import android.widget.AbsListView;

import java.lang.reflect.Field;

@SuppressWarnings("deprecation")
public class ScrollBarUtil {

	public static void bindScrollBar(AbsListView listview, int resid) {
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
