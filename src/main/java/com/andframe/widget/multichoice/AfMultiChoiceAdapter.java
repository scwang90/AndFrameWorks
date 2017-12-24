package com.andframe.widget.multichoice;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.andframe.adapter.AfListAdapter;
import com.andframe.api.adapter.ItemViewer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SuppressWarnings("unused")
public abstract class AfMultiChoiceAdapter<T> extends AfListAdapter<T>{

	public interface MultiChoiceListener<T>{
		void onMultiChoiceChanged(AfMultiChoiceAdapter<T> adapter, T tag, boolean selected, int number);
		void onMultiChoiceChanged(AfMultiChoiceAdapter<T> adapter, int number, int total);
		void onMultiChoiceStarted(AfMultiChoiceAdapter<T> adapter, int number);
		void onMultiChoiceClosed(AfMultiChoiceAdapter<T> adapter, List<T> list);
	}

	public interface GenericityListener{
		void onMultiChoiceAddData(AfMultiChoiceAdapter<?> adapter, Collection<?> list);
		void onMultiChoiceChanged(AfMultiChoiceAdapter<?> adapter, Object tag, boolean selected, int number);
		void onMultiChoiceChanged(AfMultiChoiceAdapter<?> adapter, int number, int total);
		void onMultiChoiceStarted(AfMultiChoiceAdapter<?> adapter, int number);
		void onMultiChoiceClosed(AfMultiChoiceAdapter<?> adapter, Collection<?> list);
	}
	
	protected int mChoiceNumber = 0;
	protected boolean mIsSingle = false;
	protected boolean[] mIsSelecteds = null;
	protected List<MultiChoiceListener<T>> mListeners = new ArrayList<>();
	protected List<GenericityListener> mGenericityListeners = new ArrayList<>();

	public AfMultiChoiceAdapter() {
	}

	public AfMultiChoiceAdapter(List<T> ltdata) {
		super(ltdata);
	}

	public AfMultiChoiceAdapter(List<T> ltdata, boolean dataSync) {
		super(ltdata, dataSync);
	}

	public void addListener(MultiChoiceListener<T> listener) {
		if(mListeners.indexOf(listener) < 0){
			mListeners.add(listener);
		}
	}
	
	public void addGenericityListener(GenericityListener listener) {
		if(mGenericityListeners.indexOf(listener) < 0){
			mGenericityListeners.add(listener);
		}
	}
	
	public int getChoiceNumber() {
		return mChoiceNumber;
	}

	@Override
	public boolean addAll(@NonNull Collection<? extends T> ltdata) {
		if(isMultiChoiceMode() && ltdata.size() > 0){
			boolean[] old = mIsSelecteds;
			mIsSelecteds = new boolean[old.length+ltdata.size()];
			System.arraycopy(old, 0, mIsSelecteds, 0, old.length);
			boolean ret = super.addAll(ltdata);
			for (GenericityListener listener : mGenericityListeners) {
				listener.onMultiChoiceAddData(this, ltdata);
			}
			return ret;
		}else{
			return super.addAll(ltdata);
		}
	}
	
	@Override
	public void set(@NonNull List<T> ltdata) {
		if(isMultiChoiceMode()){
			super.set(ltdata);
			mChoiceNumber = 0;
			mIsSelecteds = new boolean[ltdata.size()];
			for (GenericityListener listener : mGenericityListeners) {
				listener.onMultiChoiceChanged(this, 0, mIsSelecteds.length);
			}
		}else{
			super.set(ltdata);
		}
	}
	
	@Override
	public T set(int index, T obj) {
		//closeMultiChoice();
		return super.set(index, obj);
	}
	
	@Override
	public void add(int index, T object) {
		if(isMultiChoiceMode()){
			super.add(index, object);
			mChoiceNumber = 0;
			mIsSelecteds = new boolean[getCount()];
			for (GenericityListener listener : mGenericityListeners) {
				listener.onMultiChoiceChanged(this, 0, mIsSelecteds.length);
			}
		}else{
			super.add(index, object);
		}
	}
	
	@Override
	public T remove(int index) {
		if(isMultiChoiceMode()){
			T model = super.remove(index);
			mChoiceNumber = 0;
			mIsSelecteds = new boolean[getCount()];
			for (GenericityListener listener : mGenericityListeners) {
				listener.onMultiChoiceChanged(this, 0, mIsSelecteds.length);
			}
			return model;
		}else{
			return super.remove(index);
		}
	}

	@NonNull
	@Override
	public ItemViewer<T> newItemViewer(int viewType) {
		return newMultiChoiceItem(viewType);
	}
	
	protected abstract AfMultiChoiceItemViewer<T> newMultiChoiceItem(int viewType);

	@Override
	public void bindingItem(View view, ItemViewer<T> item, int index) {
		//return super.bindingItem(item, index);
		AfMultiChoiceItemViewer<T> mcitem = (AfMultiChoiceItemViewer<T>)item;
		AfMultiChoiceItemViewer.SelectStatus status = AfMultiChoiceItemViewer.SelectStatus.NONE;
		if(mIsSelecteds != null){
			if(mIsSelecteds[index]){
				status = AfMultiChoiceItemViewer.SelectStatus.SELECTED;
			}else{
				status = AfMultiChoiceItemViewer.SelectStatus.UNSELECT;
			}
		}
		mcitem.setSelectStatus(mltArray.get(index), status);
		super.bindingItem(view, mcitem, index);
	}

	public boolean isChoiced(int index) {
		return mIsSelecteds != null && index < mIsSelecteds.length && index >= 0 && mIsSelecteds[index];
	}


	public void restoreSelect() {
		if (mIsSelecteds != null) {
			mChoiceNumber = 0;
			mIsSelecteds = new boolean[getCount()];
			notifyDataSetChanged();
		}
	}

	public boolean beginMultiChoice(int index, boolean notify) {
		if (mIsSelecteds == null) {
			mIsSelecteds = new boolean[getCount()];
			if (index > -1) {
				mIsSelecteds[index] = true;
				mChoiceNumber = 1;
			}
			if(notify){
				notifyDataSetChanged();
			}
			for (GenericityListener listener : mGenericityListeners) {
				listener.onMultiChoiceStarted(this, mChoiceNumber);
			}
			for (MultiChoiceListener<T> listener : mListeners) {
				listener.onMultiChoiceStarted(this, mChoiceNumber);
			}

		}
		return true;
	}
	
	public boolean beginMultiChoice() {
		return beginMultiChoice(true);
	}

	public boolean beginMultiChoice(boolean notify) {
		return beginMultiChoice(-1, notify);
	}

	public boolean beginMultiChoice(int index) {
		return beginMultiChoice(index, true);
	}
	
	public boolean isMultiChoiceMode() {
		return mIsSelecteds != null;
	}
	
	public boolean closeMultiChoice() {
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

	public T getSelectedItem() {
		List<T> list = getSelectedItems();
		if (list.size() == 0) {
			return null;
		}
		return list.get(0);
	}

	public List<T> getSelectedItems() {
		List<T> list = new ArrayList<>();
		if(mIsSelecteds != null && mIsSelecteds.length == mltArray.size()){
			for (int i = 0; i < mIsSelecteds.length; i++) {
				if(mIsSelecteds[i]){
					list.add(mltArray.get(i));
				}
			}
			closeMultiChoice();
		}
		return list;
	}

	public List<T> peekSelectedItems() {
		List<T> list = new ArrayList<>();
		if(mIsSelecteds != null && mIsSelecteds.length == mltArray.size()){
			for (int i = 0; i < mIsSelecteds.length; i++) {
				if(mIsSelecteds[i]){
					list.add(mltArray.get(i));
				}
			}
		}
		return list;
	}

	public void selectAll() {
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

	public int selectCount() {
		int count = 0;
		if(mIsSelecteds != null && mIsSelecteds.length == mltArray.size()){
			for (boolean mIsSelected : mIsSelecteds) {
				if (mIsSelected) {
					count++;
				}
			}
		}
		return count;
	}

	@NonNull
	@Override
	public View inflateItem(ItemViewer<T> item, ViewGroup parent) {
		View view = super.inflateItem(item, parent);
		if (item instanceof AfMultiChoiceItemViewer) {
			return ((AfMultiChoiceItemViewer<T>)item).inflateLayout(view,this);
		}
		return view;
	}

	public void onItemClick(int index) {
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
		mIsSingle  = single; 
	}

}
