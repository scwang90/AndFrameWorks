package com.andframe.widget.popupmenu;

import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class PopupMenu {

	private android.widget.PopupMenu mPopupMenu;

	public PopupMenu(Context context, View v) {
		try {
			mPopupMenu = new android.widget.PopupMenu(context, v);
		} catch (Throwable e) {
		}
	}

	public Menu getMenu() {
		if (mPopupMenu == null) {
			return null;
		}
		return mPopupMenu.getMenu();
	}

	public void show() {
		if (mPopupMenu == null) {
			return;
		}
		mPopupMenu.show();
	}

	public void setOnMenuItemClickListener(final OnMenuItemClickListener listener) {
		if (mPopupMenu == null) {
			return;
		}
		mPopupMenu.setOnMenuItemClickListener(new android.widget.PopupMenu.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				return listener.onMenuItemClick(item);
			}
		});
	}

	public boolean isValid() {
		return mPopupMenu != null;
	}
	
}
