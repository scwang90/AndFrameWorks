package com.andframe.module;

import androidx.appcompat.view.menu.ActionMenuItem;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.PopupMenu.OnMenuItemClickListener;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.andframe.api.viewer.Viewer;
import com.andframe.widget.select.SelectListItemAdapter;
import com.andframe.widget.select.SelectListItemAdapter.GenericListener;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public abstract class SelectorBottomBarModule extends AlphaModule implements
		OnClickListener, GenericListener, OnMenuItemClickListener {

	public static final int MAX_ICON = 4;

	protected int function = 0;
	protected ImageView mMore;
	protected LinearLayout mContain;
	protected OnMenuItemClickListener mListener;
	protected HashMap<String, Integer> mMenus = new HashMap<>();

	protected abstract int getSelectorDrawableResId();
	protected abstract ImageView getFunctionViewMore(Viewer view);
	protected abstract LinearLayout getFunctionLayout(Viewer view);

	protected SelectorBottomBarModule() {
	}

	protected SelectorBottomBarModule(Viewer view, int viewid) {
		initializeComponent(view, viewid);
	}

	private void initView(Viewer viewer) {
		mMore = getFunctionViewMore(viewer);
		mMore.setVisibility(GONE);
		mContain = getFunctionLayout(viewer);
		mMore.setOnClickListener(this);
		view.setVisibility(GONE);
	}

	@Override
	protected void onCreated(Viewer viewable, View view) {
		super.onCreated(viewable, view);
		initView(viewable);
	}

	public void addFunction(int id, int imageid, String detail) {
		if (mContain.findViewById(id) == null) {
			if (function < MAX_ICON) {
				ImageView view = new ImageView(mMore.getContext());
				int backId = getSelectorDrawableResId();
				view.setId(id);
				view.setScaleType(mMore.getScaleType());
				view.setLayoutParams(mMore.getLayoutParams());
				view.setImageResource(imageid);
				view.setBackgroundResource(backId);
				view.setContentDescription(detail);
				view.setOnClickListener(this);
				mContain.addView(view, mContain.indexOfChild(mMore));
				if (function == MAX_ICON - 1) {
					putMeun(id, detail);
				}
			} else {
				if (mMore.getVisibility() != VISIBLE) {
					mMore.setVisibility(VISIBLE);
					mContain.removeViewAt(mContain.indexOfChild(mMore) - 1);
	//				mContain.addView(mMore);
				}
				putMeun(id, detail);
			}
			function++;
		}
	}

	private void putMeun(int id, String detail) {
		if (mMenus == null) {
            mMenus = new HashMap<>();
        }
		mMenus.put(detail, id);
	}

	public void setMenuItemListener(OnMenuItemClickListener listener) {
		mListener = listener;
	}
	
	public void setAdapter(SelectListItemAdapter<?> adapter) {
		if (adapter != null){
			adapter.addGenericityListener(this);
			if (adapter.isSelectMode()!=isVisibility()){
				if (isVisibility()){
					this.hide();
				} else {
					this.show();
				}
			}
		}
	}

	@Override
	public void onClick(View v) {
		if (v == mMore) {
			PopupMenu pm = new PopupMenu(v.getContext(), v);
			for (Entry<String, Integer> entry : mMenus.entrySet()) {
				pm.getMenu().add(1, entry.getValue(), 2, entry.getKey());
			}
			pm.setOnMenuItemClickListener(this);
			pm.show();
		} else if (mListener != null) {
			onMenuItemClick(new ActionMenuItem(getContext(), 0, v.getId(), 0, 0, ""));
		}
	}

	@Override
	public boolean onMenuItemClick(MenuItem menu) {
		return mListener != null && mListener.onMenuItemClick(menu);
	}

	@Override
	public void onSelectChanged(SelectListItemAdapter<?> adapter, Object tag, boolean selected, int number) {

	}

	@Override
	public void onSelectChanged(SelectListItemAdapter<?> adapter, int number, int total) {
	}
	
	@Override
	public void onSelectAddData(SelectListItemAdapter<?> adapter, Collection<?> list) {
		
	}

	@Override
	public void onSelectStarted(SelectListItemAdapter<?> adapter, int number) {
		if (function > 0) {
			this.show();
		}
	}

	@Override
	public void onSelectClosed(SelectListItemAdapter<?> adapter, Collection<?> list) {
		if (function > 0) {
			this.hide();
		}
	}
	
}
