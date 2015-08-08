package com.andframe.layoutbind;

import android.os.Build;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.andframe.activity.framework.AfViewable;
import com.andframe.view.multichoice.AfMultiChoiceAdapter;
import com.andframe.view.multichoice.AfMultiChoiceAdapter.GenericityListener;
import com.andframe.widget.popupmenu.OnMenuItemClickListener;
import com.andframe.widget.popupmenu.PopupMenu;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public abstract class AfSelectorTitlebar extends AfLayoutAlpha 
	implements OnClickListener, GenericityListener, OnMenuItemClickListener {

	private static final int ID_SELECTALL = -1;
	private static final int ID_INVERT = -2;
	private static final int ID_UNSELECT = -3;

	protected String TEXT_FORMAT="选择项   %d/%d";
	
	protected View mBtFinish = null;
	protected View mOperate = null;
	protected TextView mTvText = null;
	protected AfMultiChoiceAdapter<? extends Object> mAdapter;
	protected OnMenuItemClickListener mListener = null;
	
	protected MenuMap mMeuns = new MenuMap();

	protected class MenuMap extends HashMap<String, Integer>{
		private static final long serialVersionUID = -8159583806449123914L;
		public MenuMap() {
			// TODO Auto-generated constructor stub
		}
		public MenuMap(HashMap<String, Integer> map) {
			super(map);
			// TODO Auto-generated constructor stub
		}
	}
	
	public AfSelectorTitlebar(AfViewable view,int viewid) {
		super(view,viewid);
		// TODO Auto-generated constructor stub
		mTvText = findTitleSelectTvText(view);
		mBtFinish = findTitleSelectBtFinish(view);
		mOperate = findTitleSelectOperate(view);
		if(isValid()){
			mTvText.setText(String.format(TEXT_FORMAT, 0,1));
			mBtFinish.setOnClickListener(this);
			if (Build.VERSION.SDK_INT >= 11) {
				mOperate.setOnClickListener(this);
				mOperate.setEnabled(false);
			}else {
				mOperate.setVisibility(View.GONE);
			}
		}
	}
	
	protected abstract View findTitleSelectBtFinish(AfViewable view);
	protected abstract View findTitleSelectOperate(AfViewable view);
	protected abstract TextView findTitleSelectTvText(AfViewable view);

	public void setAdapter(AfMultiChoiceAdapter<? extends Object> adapter) {
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

	public void setFinishOnClickListener(OnClickListener listener) {
		if(isValid()){
			mBtFinish.setOnClickListener(listener);
		}
	}

	public void setOperateOnClickListener(OnClickListener listener) {
		if(isValid()){
			mOperate.setOnClickListener(listener);
			mOperate.setEnabled(true);
		}
	}

	public View getLayout() {
		return target;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == mOperate) {
			PopupMenu pm = new PopupMenu(v.getContext(), v);
			if (!pm.isValid()) {
				return;
			}
			pm.getMenu().add(0,ID_SELECTALL,0,"全选");
			pm.getMenu().add(0,ID_INVERT,1,"反选");
			pm.getMenu().add(0,ID_UNSELECT,2,"全不选");
			
			Iterator<Entry<String, Integer>> iter = mMeuns.entrySet().iterator();  
			while (iter.hasNext()) {  
				Entry<String, Integer> entry = iter.next();  
				pm.getMenu().add(1,entry.getValue(),2,entry.getKey());
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
		// TODO Auto-generated method stub
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
		if(mListener != null){
			return mListener.onMenuItemClick(item);
		}
		return false;
	}
	
	@Override
	public void onMultiChoiceStarted(AfMultiChoiceAdapter<? extends Object> adapter,
			int number) {
		// TODO Auto-generated method stub
		this.show();
		mTvText.setText(String.format(TEXT_FORMAT, adapter.getChoiceNumber(),adapter.getCount()));
	}
	@Override
	public void onMultiChoiceChanged(AfMultiChoiceAdapter<? extends Object> adapter,
			Object tag, boolean selected, int number) {
		// TODO Auto-generated method stub
		mTvText.setText(String.format(TEXT_FORMAT, number,adapter.getCount()));
	}
	@Override
	public void onMultiChoiceChanged(AfMultiChoiceAdapter<? extends Object> adapter,
			int number, int total) {
		// TODO Auto-generated method stub
		mTvText.setText(String.format(TEXT_FORMAT, number,total));
	}
	@Override
	public void onMultiChoiceAddData(
			AfMultiChoiceAdapter<? extends Object> adapter,
			List<? extends Object> list) {
		// TODO Auto-generated method stub
		mTvText.setText(String.format(TEXT_FORMAT, adapter.getChoiceNumber(),adapter.getCount()));
	}

	@Override
	public void onMultiChoiceClosed(
			AfMultiChoiceAdapter<? extends Object> adapter,
			List<? extends Object> list) {
		// TODO Auto-generated method stub
		this.hide();
	}
	
	@Override
	public boolean isValid() {
		// TODO Auto-generated method stub
		return super.isValid()&&mBtFinish!= null&&mOperate!= null&&mTvText!=null;
	}

	
	public void putMenu(String string, int id) {
		// TODO Auto-generated method stub
		mMeuns.put(string, id);
	}
}
