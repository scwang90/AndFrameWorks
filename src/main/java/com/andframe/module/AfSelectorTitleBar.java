package com.andframe.module;

import android.os.Build;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.PopupMenu.OnMenuItemClickListener;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.andframe.api.viewer.Viewer;
import com.andframe.widget.multichoice.AfMultiChoiceAdapter;
import com.andframe.widget.multichoice.AfMultiChoiceAdapter.GenericityListener;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map.Entry;

public abstract class AfSelectorTitleBar extends AfModuleAlpha
	implements OnClickListener, GenericityListener, OnMenuItemClickListener {

	private static final int ID_SELECTALL = -1;
	private static final int ID_INVERT = -2;
	private static final int ID_UNSELECT = -3;

	protected String TEXT_FORMAT="选择项   %d/%d";
	
	protected View mBtFinish = null;
	protected View mOperate = null;
	protected TextView mTvText = null;
	protected AfMultiChoiceAdapter<?> mAdapter;
	protected OnMenuItemClickListener mListener = null;
	
	protected MenuMap mMeuns = new MenuMap();

	protected class MenuMap extends HashMap<String, Integer>{
		private static final long serialVersionUID = -8159583806449123914L;
		public MenuMap() {
		}
		@SuppressWarnings("unused")
		public MenuMap(HashMap<String, Integer> map) {
			super(map);
		}
	}

	protected AfSelectorTitleBar() {
	}

	protected AfSelectorTitleBar(Viewer viewer, int viewid) {
		initializeComponent(viewer, viewid);
	}

	@Override
	protected void onCreated(Viewer viewer, View view) {
		super.onCreated(viewer, view);
		mTvText = findTitleSelectTvText(viewer);
		mBtFinish = findTitleSelectBtFinish(viewer);
		mOperate = findTitleSelectOperate(viewer);
		if(isValid()){
			mTvText.setText(String.format(Locale.CHINA, TEXT_FORMAT, 0, 1));
			mBtFinish.setOnClickListener(this);
			if (Build.VERSION.SDK_INT >= 11) {
				mOperate.setOnClickListener(this);
				mOperate.setEnabled(false);
			}else {
				mOperate.setVisibility(View.GONE);
			}
		}
	}

	protected abstract View findTitleSelectBtFinish(Viewer view);
	protected abstract View findTitleSelectOperate(Viewer view);
	protected abstract TextView findTitleSelectTvText(Viewer view);

	public void setAdapter(AfMultiChoiceAdapter<?> adapter) {
		if(isValid()){
			mAdapter = adapter;
			mOperate.setEnabled(true);
			mAdapter.addGenericityListener(this);
			if (mAdapter != null){
				if (mAdapter.isMultiChoiceMode()!=isVisibility()){
					if (isVisibility()){
						this.hide();
					} else {
						this.show();
					}
				}
			}
		}
	}
	
	public void setMenuItemListener(OnMenuItemClickListener mListener) {
		this.mListener = mListener;
	}
	
	public void addMeuns(HashMap<String, Integer> map) {
		this.mMeuns.putAll(map);
	}

	@SuppressWarnings("unused")
	public void setFinishOnClickListener(OnClickListener listener) {
		if(isValid()){
			mBtFinish.setOnClickListener(listener);
		}
	}

	@SuppressWarnings("unused")
	public void setOperateOnClickListener(OnClickListener listener) {
		if(isValid()){
			mOperate.setOnClickListener(listener);
			mOperate.setEnabled(true);
		}
	}

	public View getLayout() {
		return view;
	}

	@Override
	public void onClick(View v) {
		if (v == mOperate) {
			PopupMenu pm = new PopupMenu(v.getContext(), v);
			pm.getMenu().add(0,ID_SELECTALL,0,"全选");
			pm.getMenu().add(0,ID_INVERT,1,"反选");
			pm.getMenu().add(0,ID_UNSELECT,2,"全不选");

			for (Entry<String, Integer> entry : mMeuns.entrySet()) {
				pm.getMenu().add(1, entry.getValue(), 2, entry.getKey());
			}  
			
			pm.setOnMenuItemClickListener(this);
			pm.show();
		}else if(v == mBtFinish){
			if(mAdapter!= null&&mAdapter.isMultiChoiceMode()){
				mAdapter.closeMultiChoice();
			}
		}
	}
	
	@Override
	public boolean onMenuItemClick(MenuItem item) {
		switch (item.getItemId()) {
			case ID_SELECTALL:
				mAdapter.selectAll();
				return true;
			case ID_INVERT:
				mAdapter.selectInvert();
				return true;
			case ID_UNSELECT:
				mAdapter.selectNone();
				return true;
		}
		return mListener != null && mListener.onMenuItemClick(item);
	}
	
	@Override
	public void onMultiChoiceStarted(AfMultiChoiceAdapter<?> adapter,
			int number) {
		this.show();
		mTvText.setText(String.format(TEXT_FORMAT, adapter.getChoiceNumber(),adapter.getCount()));
	}
	@Override
	public void onMultiChoiceChanged(AfMultiChoiceAdapter<?> adapter,
			Object tag, boolean selected, int number) {
		mTvText.setText(String.format(TEXT_FORMAT, number,adapter.getCount()));
	}
	@Override
	public void onMultiChoiceChanged(AfMultiChoiceAdapter<?> adapter,
			int number, int total) {
		mTvText.setText(String.format(Locale.CHINA, TEXT_FORMAT, number, total));
	}
	@Override
	public void onMultiChoiceAddData(AfMultiChoiceAdapter<?> adapter, Collection<?> list) {
		mTvText.setText(String.format(Locale.CHINA, TEXT_FORMAT, adapter.getChoiceNumber(),adapter.getCount()));
	}

	@Override
	public void onMultiChoiceClosed(
			AfMultiChoiceAdapter<?> adapter,
			Collection<?> list) {
		this.hide();
	}
	
	@Override
	public boolean isValid() {
		return super.isValid()&&mBtFinish!= null&&mOperate!= null&&mTvText!=null;
	}

	
	public void putMenu(String string, int id) {
		mMeuns.put(string, id);
	}
}
