package com.andframe.view.multichoice;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.andframe.adapter.AfListAdapter;

public abstract class AfMultiChoiceAdapter<T> extends AfListAdapter<T>{

	public interface MultiChoiceListener<T>{
		void onMultiChoiceChanged(AfMultiChoiceAdapter<T> adapter,T tag,boolean selected,int number);
		void onMultiChoiceChanged(AfMultiChoiceAdapter<T> adapter,int number,int total);
		void onMultiChoiceStarted(AfMultiChoiceAdapter<T> adapter, int number);
		void onMultiChoiceClosed(AfMultiChoiceAdapter<T> adapter, List<T> list);
	}

	public interface GenericityListener{
		void onMultiChoiceAddData(AfMultiChoiceAdapter<? extends Object> adapter,List<? extends Object> list);
		void onMultiChoiceChanged(AfMultiChoiceAdapter<? extends Object> adapter,Object tag,boolean selected,int number);
		void onMultiChoiceChanged(AfMultiChoiceAdapter<? extends Object> adapter,int number,int total);
		void onMultiChoiceStarted(AfMultiChoiceAdapter<? extends Object> adapter, int number);
		void onMultiChoiceClosed(AfMultiChoiceAdapter<? extends Object> adapter, List<? extends Object> list);
	}
	
	protected int mChoiceNumber = 0;
	protected Context mContext = null;
	protected boolean mIsSingle = false;
	protected boolean[] mIsSelecteds = null;
	protected List<MultiChoiceListener<T>> mListeners = new ArrayList<MultiChoiceListener<T>>();
	protected List<GenericityListener> mGenericityListeners = new ArrayList<GenericityListener>();
	
	public AfMultiChoiceAdapter(Context context, List<T> ltdata) {
		super(context, ltdata);
		// TODO Auto-generated constructor stub
		mContext = context;
	}

	public void addListener(MultiChoiceListener<T> listener) {
		if(mListeners.indexOf(listener) < 0){
			mListeners.add(listener);
		}
	}
	
	public void addGenericityListener(GenericityListener listener) {
		if(mListeners.indexOf(listener) < 0){
			mGenericityListeners.add(listener);
		}
	}
	
	public int getChoiceNumber() {
		return mChoiceNumber;
	}
	
	@Override
	public void addData(List<T> ltdata) {
		// TODO Auto-generated method stub
		if(isMultiChoiceMode() && ltdata.size() > 0){
			boolean[] old = mIsSelecteds;
			mIsSelecteds = new boolean[old.length+ltdata.size()];
			for (int i = 0; i < old.length; i++) {
				mIsSelecteds[i] = old[i];
			}
			super.addData(ltdata);
			for (GenericityListener listener : mGenericityListeners) {
				listener.onMultiChoiceAddData(this, ltdata);
			}
		}else{
			super.addData(ltdata);
		}
	}
	
	@Override
	public void setData(List<T> ltdata) {
		// TODO Auto-generated method stub
		if(isMultiChoiceMode()){
			super.setData(ltdata);
			mChoiceNumber = 0;
			mIsSelecteds = new boolean[ltdata.size()];
			for (GenericityListener listener : mGenericityListeners) {
				listener.onMultiChoiceChanged(this, 0, mIsSelecteds.length);
			}
		}else{
			super.setData(ltdata);
		}
	}
	
	@Override
	public void setData(int index, T obj) {
		// TODO Auto-generated method stub
		//closeMultiChoice();
		super.setData(index, obj);
	}
	
	@Override
	public void insert(int index, T object) {
		// TODO Auto-generated method stub
		if(isMultiChoiceMode()){
			super.insert(index, object);
			mChoiceNumber = 0;
			mIsSelecteds = new boolean[getCount()];
			for (GenericityListener listener : mGenericityListeners) {
				listener.onMultiChoiceChanged(this, 0, mIsSelecteds.length);
			}
		}else{
			super.insert(index, object);
		}
	}
	
	@Override
	public void remove(int index) {
		// TODO Auto-generated method stub
		if(isMultiChoiceMode()){
			super.remove(index);
			mChoiceNumber = 0;
			mIsSelecteds = new boolean[getCount()];
			for (GenericityListener listener : mGenericityListeners) {
				listener.onMultiChoiceChanged(this, 0, mIsSelecteds.length);
			}
		}else{
			super.remove(index);
		}
	}
    
	@Override
	protected final IAfLayoutItem<T> getItemLayout(T data) {
		// TODO Auto-generated method stub
		return getMultiChoiceItem(data);
	}
	
	protected abstract AfMultiChoiceItem<T> getMultiChoiceItem(T data);

	@Override
	protected boolean bindingItem(IAfLayoutItem<T> item, int index) {
		// TODO Auto-generated method stub
		//return super.bindingItem(item, index);
		AfMultiChoiceItem<T> mcitem = (AfMultiChoiceItem<T>)item;
		AfMultiChoiceItem.SelectStatus status = AfMultiChoiceItem.SelectStatus.NONE;
		if(mIsSelecteds != null){
			if(true == mIsSelecteds[index]){
				status = AfMultiChoiceItem.SelectStatus.SELECTED;
			}else{
				status = AfMultiChoiceItem.SelectStatus.UNSELECT;
			}
		}
		mcitem.setSelectStatus(mltArray.get(index), status);
		return super.bindingItem(mcitem, index);
	}


	public boolean beginMultiChoice(int index) {
		// TODO Auto-generated method stub
		if(getCount() > 0){
			mIsSelecteds = new boolean[getCount()];
			if(index > -1){
				mIsSelecteds[index] = true;
				mChoiceNumber = 1;
			}
			notifyDataSetChanged();
			for (GenericityListener listener : mGenericityListeners) {
				listener.onMultiChoiceStarted(this,mChoiceNumber);
			}
			for (MultiChoiceListener<T> listener : mListeners) {
				listener.onMultiChoiceStarted(this,mChoiceNumber);
			}
			
		}
		return true;
	}
	
	public boolean beginMultiChoice() {
		return beginMultiChoice(true);
	}
	
	public boolean beginMultiChoice(boolean notify) {
		// TODO Auto-generated method stub
		if(mIsSelecteds == null && getCount() > 0){
			mIsSelecteds = new boolean[getCount()];
			if(notify){
				notifyDataSetChanged();
			}
			for (GenericityListener listener : mGenericityListeners) {
				listener.onMultiChoiceStarted(this,0);
			}
			for (MultiChoiceListener<T> listener : mListeners) {
				listener.onMultiChoiceStarted(this,0);
			}
		}
		return true;
	}
	
	public boolean isMultiChoiceMode() {
		return mIsSelecteds != null;
	}
	
	public boolean closeMultiChoice() {
		// TODO Auto-generated method stub
		if(mIsSelecteds != null){
			for (GenericityListener listener : mGenericityListeners) {
				listener.onMultiChoiceClosed(this,peekSelectedItems());
			}
			for (MultiChoiceListener<T> listener : mListeners) {
				listener.onMultiChoiceClosed(this,peekSelectedItems());
			}
			mIsSelecteds = null;
			mChoiceNumber = 0;
			notifyDataSetChanged();
			return true;
		}
		return false;
	}

	public List<T> getSelectedItems() {
		// TODO Auto-generated method stub
		List<T> list = new ArrayList<T>();
		if(mIsSelecteds != null && mIsSelecteds.length == mltArray.size()){
			for (int i = 0; i < mIsSelecteds.length; i++) {
				if(true == mIsSelecteds[i]){
					list.add(mltArray.get(i));
				}
			}
			closeMultiChoice();
		}
		return list;
	}

	public List<T> peekSelectedItems() {
		// TODO Auto-generated method stub
		List<T> list = new ArrayList<T>();
		if(mIsSelecteds != null && mIsSelecteds.length == mltArray.size()){
			for (int i = 0; i < mIsSelecteds.length; i++) {
				if(true == mIsSelecteds[i]){
					list.add(mltArray.get(i));
				}
			}
		}
		return list;
	}

	public void selectAll() {
		// TODO Auto-generated method stub
		if(mIsSelecteds != null){
			mChoiceNumber = getCount();
			for (int i = 0; i < mIsSelecteds.length; i++) {
				mIsSelecteds[i] = true;
			}
			notifyDataSetChanged();
			for (GenericityListener listener : mGenericityListeners) {
				listener.onMultiChoiceChanged(this, mChoiceNumber,getCount());
			}
			for (MultiChoiceListener<T> listener : mListeners) {
				listener.onMultiChoiceChanged(this, mChoiceNumber,getCount());
			}
		}
	}

	public void selectInvert() {
		// TODO Auto-generated method stub
		if(mIsSelecteds != null){
			mChoiceNumber = getCount() - mChoiceNumber;
			for (int i = 0; i < mIsSelecteds.length; i++) {
				mIsSelecteds[i] = !mIsSelecteds[i];
			}
			notifyDataSetChanged();
			for (GenericityListener listener : mGenericityListeners) {
				listener.onMultiChoiceChanged(this, mChoiceNumber,getCount());
			}
			for (MultiChoiceListener<T> listener : mListeners) {
				listener.onMultiChoiceChanged(this, mChoiceNumber,getCount());
			}
		}
	}

	public void selectNone() {
		// TODO Auto-generated method stub
		if(mIsSelecteds != null){
			mChoiceNumber = 0;
			mIsSelecteds = new boolean[getCount()];
			notifyDataSetChanged();
			for (GenericityListener listener : mGenericityListeners) {
				listener.onMultiChoiceChanged(this, mChoiceNumber,getCount());
			}
			for (MultiChoiceListener<T> listener : mListeners) {
				listener.onMultiChoiceChanged(this, mChoiceNumber,getCount());
			}
		}
	}
	
	@Override
	protected View onInflateItem(IAfLayoutItem<T> item, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = super.onInflateItem(item, parent);
		return ((AfMultiChoiceItem<T>)item).inflateLayout(view,this);
	}

	public void onItemClick(int index) {
		// TODO Auto-generated method stub
		if(mIsSelecteds != null && index >= 0 && index < mltArray.size()){
			boolean checked = !mIsSelecteds[index];
			if(mIsSingle){
				checked = true;
				mIsSelecteds = new boolean[getCount()];
				mIsSelecteds[index] = true;
				mChoiceNumber = 1;
			}else{
				mIsSelecteds[index] = checked;
				mChoiceNumber += checked ? 1 : -1;
			}
			notifyDataSetChanged();
			for (GenericityListener listener : mGenericityListeners) {
				listener.onMultiChoiceChanged(this, mltArray.get(index), checked, mChoiceNumber);
			}
			for (MultiChoiceListener<T> listener : mListeners) {
				listener.onMultiChoiceChanged(this, mltArray.get(index), checked, mChoiceNumber);
			}
		}
	}
	
	void setSelect(T tag, boolean checked) {
		// TODO Auto-generated method stub
		this.onItemClick(mltArray.indexOf(tag));
//		int index = mltData.indexOf(tag);
//		if(index > -1 && mIsSelecteds != null){
//			if(mIsSingle){
//				checked = true;
//				mIsSelecteds = new boolean[getCount()];
//				mIsSelecteds[index] = true;
//				mChoiceNumber = 1;
//			}else{
//				mIsSelecteds[index] = checked;
//				mChoiceNumber += checked ? 1 : -1;
//			}
//			for (GenericityListener listener : mGenericityListeners) {
//				listener.onMultiChoiceChanged(this, tag, checked, mChoiceNumber);
//			}
//			for (MultiChoiceListener<T> listener : mListeners) {
//				listener.onMultiChoiceChanged(this, tag, checked, mChoiceNumber);
//			}
//		}
	}
	
	public void setSingle(boolean single) {
		// TODO Auto-generated method stub
		mIsSingle  = single; 
	}

}
