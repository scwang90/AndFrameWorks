package com.ontheway.widget.popupmenu;

import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class PopupMenu {

	private android.widget.PopupMenu mPopupMenu;

	public PopupMenu(Context context, View v) {
		// TODO Auto-generated constructor stub
		try {
			mPopupMenu = new android.widget.PopupMenu(context, v);
		} catch (Throwable e) {
			// TODO: handle exception
		}
	}

	public Menu getMenu() {
		// TODO Auto-generated method stub
		if (mPopupMenu == null) {
			return null;
		}
		return mPopupMenu.getMenu();
	}

	public void show() {
		// TODO Auto-generated method stub
		if (mPopupMenu == null) {
			return;
		}
		mPopupMenu.show();
	}

	public void setOnMenuItemClickListener(final OnMenuItemClickListener listener) {
		// TODO Auto-generated method stub
		if (mPopupMenu == null) {
			return;
		}
		mPopupMenu.setOnMenuItemClickListener(new android.widget.PopupMenu.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// TODO Auto-generated method stub
				return listener.onMenuItemClick(item);
			}
		});
	}

	public boolean isValid() {
		// TODO Auto-generated method stub
		return mPopupMenu != null;
	}
	
}
