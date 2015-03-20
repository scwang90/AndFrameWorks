package com.ontheway.layoutbind;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ontheway.activity.framework.AfViewable;
import com.ontheway.view.multichoice.AfMultiChoiceAdapter;
import com.ontheway.view.multichoice.AfMultiChoiceAdapter.GenericityListener;
import com.ontheway.widget.popupmenu.MenuEntity;
import com.ontheway.widget.popupmenu.OnMenuItemClickListener;
import com.ontheway.widget.popupmenu.PopupMenu;

public abstract class AfSelectorBottombar extends AfLayoutAlpha implements
		OnClickListener, GenericityListener, OnMenuItemClickListener {

	public static final int MAX_ICON = 4;

	protected int function = 0;
	protected ImageView mMore;
	protected LinearLayout mContaint;
	protected OnMenuItemClickListener mListener;
	protected HashMap<String, Integer> mMeuns = new HashMap<String, Integer>();

	protected abstract int getSelectorDrawableResId();
	protected abstract ImageView getFunctionViewMore(AfViewable view);
	protected abstract LinearLayout getFunctionLayout(AfViewable view);
	
	public AfSelectorBottombar(AfViewable view) {
		super(view);
		// TODO Auto-generated constructor stub
		if (isValid()) {
			mMore = getFunctionViewMore(view);
			mContaint = getFunctionLayout(view);
			mContaint.removeAllViews();
			mMore.setOnClickListener(this);
			mLayout.setVisibility(View.GONE);
		}
	}
	
	public void addFunction(int id, int imageid, String detail) {
		function++;
		if (function < MAX_ICON) {
			ImageView view = new ImageView(mMore.getContext());
			int backid = getSelectorDrawableResId();
			view.setId(id);
			view.setScaleType(mMore.getScaleType());
			view.setLayoutParams(mMore.getLayoutParams());
			view.setImageResource(imageid);
			view.setBackgroundResource(backid);
			view.setContentDescription(detail);
			view.setOnClickListener(this);
			mContaint.addView(view);
		} else {
			if (function == MAX_ICON) {
				mContaint.addView(mMore);
			}
			mMeuns.put(detail, id);
		}
	}
	
	public void setMenuItemListener(OnMenuItemClickListener listener) {
		// TODO Auto-generated method stub
		mListener = listener;
	}
	
	public void setAdapter(AfMultiChoiceAdapter<? extends Object> adapter) {
		adapter.addGenericityListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == mMore) {
			PopupMenu pm = new PopupMenu(v.getContext(), v);
			if (!pm.isValid()) {
				return;
			}
			Iterator<Entry<String, Integer>> iter = mMeuns.entrySet()
					.iterator();
			while (iter.hasNext()) {
				Entry<String, Integer> entry = iter.next();
				pm.getMenu().add(1, entry.getValue(), 2, entry.getKey());
			}
			pm.setOnMenuItemClickListener(this);
			pm.show();
		} else if (mListener != null) {
			mListener.onMenuItemClick(new MenuEntity(v.getId(), v.getContentDescription()));
		}
	}

	@Override
	public boolean onMenuItemClick(MenuItem menu) {
		// TODO Auto-generated method stub
		if (mListener != null) {
			return mListener.onMenuItemClick(menu);
		}
		return false;
	}

	@Override
	public void onMultiChoiceChanged(
			AfMultiChoiceAdapter<? extends Object> adapter, Object tag,
			boolean selected, int number) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMultiChoiceChanged(
			AfMultiChoiceAdapter<? extends Object> adapter, int number,
			int total) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void onMultiChoiceAddData(
			AfMultiChoiceAdapter<? extends Object> adapter,
			List<? extends Object> list) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMultiChoiceStarted(
			AfMultiChoiceAdapter<? extends Object> adapter, int number) {
		// TODO Auto-generated method stub
		if (function > 0) {
			this.show();
		}
	}

	@Override
	public void onMultiChoiceClosed(
			AfMultiChoiceAdapter<? extends Object> adapter,
			List<? extends Object> list) {
		// TODO Auto-generated method stub
		if (function > 0) {
			this.hide();
		}
	}
	
}
