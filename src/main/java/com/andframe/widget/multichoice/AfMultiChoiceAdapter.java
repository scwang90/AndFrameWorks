package com.andframe.widget.multichoice;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.andframe.adapter.AfListAdapter;
import com.andframe.api.adapter.ItemViewer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("unused")
public abstract class AfMultiChoiceAdapter<T> extends AfListAdapter<T>{

	public interface MultiChoiceListener<T> {
		void onMultiChoiceMax(AfMultiChoiceAdapter<T> adapter, int max);
		void onMultiChoiceChanged(AfMultiChoiceAdapter<T> adapter, T tag, boolean selected, int number);
		void onMultiChoiceChanged(AfMultiChoiceAdapter<T> adapter, int number, int total);
		void onMultiChoiceStarted(AfMultiChoiceAdapter<T> adapter, int number);
		void onMultiChoiceClosed(AfMultiChoiceAdapter<T> adapter, List<T> list);
	}

	public static class SampleMultiChoiceListener<T> implements MultiChoiceListener<T> {
		@Override
		public void onMultiChoiceMax(AfMultiChoiceAdapter<T> adapter, int max) {
		}
		@Override
		public void onMultiChoiceChanged(AfMultiChoiceAdapter<T> adapter, T tag, boolean selected, int number) {
		}
		@Override
		public void onMultiChoiceChanged(AfMultiChoiceAdapter<T> adapter, int number, int total) {
		}
		@Override
		public void onMultiChoiceStarted(AfMultiChoiceAdapter<T> adapter, int number) {
		}
		@Override
		public void onMultiChoiceClosed(AfMultiChoiceAdapter<T> adapter, List<T> list) {
		}
	}

	public interface GenericityListener{
		void onMultiChoiceAddData(AfMultiChoiceAdapter<?> adapter, Collection<?> list);
		void onMultiChoiceChanged(AfMultiChoiceAdapter<?> adapter, Object tag, boolean selected, int number);
		void onMultiChoiceChanged(AfMultiChoiceAdapter<?> adapter, int number, int total);
		void onMultiChoiceStarted(AfMultiChoiceAdapter<?> adapter, int number);
		void onMultiChoiceClosed(AfMultiChoiceAdapter<?> adapter, Collection<?> list);
	}
	
	protected int mChoiceNumber = 0;
	protected int mMaxChoiceNumber = Integer.MAX_VALUE;
	protected boolean mIsSingle = false;
	protected boolean mAutoCancel = false;
	protected boolean[] mIsSelectedArray = null;
	protected List<MultiChoiceListener<T>> mListeners = new ArrayList<>();
	protected List<GenericityListener> mGenericityListeners = new ArrayList<>();

	public AfMultiChoiceAdapter() {
	}

	public AfMultiChoiceAdapter(List<T> list) {
		super(list);
	}

	public AfMultiChoiceAdapter(List<T> list, boolean dataSync) {
		super(list, dataSync);
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
	public boolean addAll(@NonNull Collection<? extends T> list) {
		if(isMultiChoiceMode() && list.size() > 0){
			boolean[] old = mIsSelectedArray;
			mIsSelectedArray = new boolean[old.length+list.size()];
			System.arraycopy(old, 0, mIsSelectedArray, 0, old.length);
			boolean ret = super.addAll(list);
			for (GenericityListener listener : mGenericityListeners) {
				listener.onMultiChoiceAddData(this, list);
			}
			return ret;
		} else {
			return super.addAll(list);
		}
	}
	
	@Override
	public void set(@NonNull List<T> list) {
		if(isMultiChoiceMode() && list.size() != mIsSelectedArray.length){
			super.set(list);
			mChoiceNumber = 0;
			mIsSelectedArray = new boolean[list.size()];
			for (GenericityListener listener : mGenericityListeners) {
				listener.onMultiChoiceChanged(this, 0, mIsSelectedArray.length);
			}
		}else{
			super.set(list);
		}
	}
	
	@Override
	public T set(int index, T obj) {
		//closeMultiChoice();
		return super.set(index, obj);
	}

	@Override
	public boolean add(T data) {
		return addAll(Collections.singletonList(data));
	}

	@Override
	public void add(int index, T object) {
		if(isMultiChoiceMode()){
			super.add(index, object);
			mChoiceNumber = 0;
			mIsSelectedArray = new boolean[getItemCount()];
			for (GenericityListener listener : mGenericityListeners) {
				listener.onMultiChoiceChanged(this, 0, mIsSelectedArray.length);
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
			mIsSelectedArray = new boolean[getItemCount()];
			for (GenericityListener listener : mGenericityListeners) {
				listener.onMultiChoiceChanged(this, 0, mIsSelectedArray.length);
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
		AfMultiChoiceItemViewer<T> mcitem = (AfMultiChoiceItemViewer<T>)item;
		AfMultiChoiceItemViewer.SelectStatus status = AfMultiChoiceItemViewer.SelectStatus.NONE;
		if(mIsSelectedArray != null){
			if(mIsSelectedArray[index]){
				status = AfMultiChoiceItemViewer.SelectStatus.SELECTED;
			}else{
				status = AfMultiChoiceItemViewer.SelectStatus.UNSELECT;
			}
		}
		mcitem.setSelectStatus(get(index), status);
		super.bindingItem(view, mcitem, index);
	}

	public boolean isSelected(int index) {
		return mIsSelectedArray != null && index < mIsSelectedArray.length && index >= 0 && mIsSelectedArray[index];
	}


	public void restoreSelect() {
		if (mIsSelectedArray != null) {
			mChoiceNumber = 0;
			mIsSelectedArray = new boolean[getItemCount()];
			notifyDataSetChanged();
		}
	}

	public boolean beginMultiChoice(int index, boolean notify) {
		if (mIsSelectedArray == null) {
			mIsSelectedArray = new boolean[getItemCount()];
			if (index > -1) {
				mIsSelectedArray[index] = true;
				mChoiceNumber = 1;
			}
			if (notify) {
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
		return mIsSelectedArray != null;
	}
	
	public boolean closeMultiChoice() {
		if(mIsSelectedArray != null){
			for (GenericityListener listener : mGenericityListeners) {
				listener.onMultiChoiceClosed(this,peekSelectedItems());
			}
			for (MultiChoiceListener<T> listener : mListeners) {
				listener.onMultiChoiceClosed(this,peekSelectedItems());
			}
			mIsSelectedArray = null;
			mChoiceNumber = 0;
			notifyDataSetChanged();
			return true;
		}
		return false;
	}

	@Nullable
	public T getSelectedItem() {
		List<T> list = getSelectedItems();
		if (list.size() == 0) {
			return null;
		}
		return list.get(0);
	}

	public boolean setSelectedItems(Collection<T> collection) {
		if (collection != null) {
			mChoiceNumber = 0;
			mIsSelectedArray = new boolean[getCount()];
			List<T> list = new ArrayList<>(collection);
			for (int i = 0; i < mltArray.size(); i++) {
				for (int j = 0; j < list.size(); j++) {
					if (mltArray.get(i) == list.get(j)) {
						mIsSelectedArray[i] = true;
						list.remove(j);
						break;
					}
				}
			}
			notifyDataSetChanged();
			for (GenericityListener listener : mGenericityListeners) {
				listener.onMultiChoiceChanged(this, mChoiceNumber, getCount());
			}
			for (MultiChoiceListener<T> listener : mListeners) {
				listener.onMultiChoiceChanged(this, mChoiceNumber, getCount());
			}
		} else {
			selectNone();
		}
		return true;
	}

	@NonNull
	public List<T> getSelectedItems() {
		List<T> list = new ArrayList<>();
		if(mIsSelectedArray != null && mIsSelectedArray.length == mltArray.size()){
			for (int i = 0; i < mIsSelectedArray.length; i++) {
				if(mIsSelectedArray[i]){
					list.add(mltArray.get(i));
				}
			}
			closeMultiChoice();
		}
		return list;
	}

	@NonNull
	public List<T> peekSelectedItems() {
		List<T> list = new ArrayList<>();
		if(mIsSelectedArray != null && mIsSelectedArray.length == mltArray.size()){
			for (int i = 0; i < mIsSelectedArray.length; i++) {
				if(mIsSelectedArray[i]){
					list.add(mltArray.get(i));
				}
			}
		}
		return list;
	}

	public void selectAll() {
		if(mIsSelectedArray != null){
			mChoiceNumber = getCount();
			for (int i = 0; i < mIsSelectedArray.length; i++) {
				mIsSelectedArray[i] = true;
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
		if(mIsSelectedArray != null){
			mChoiceNumber = getCount() - mChoiceNumber;
			for (int i = 0; i < mIsSelectedArray.length; i++) {
				mIsSelectedArray[i] = !mIsSelectedArray[i];
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
		if(mIsSelectedArray != null){
			mChoiceNumber = 0;
			mIsSelectedArray = new boolean[getCount()];
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
		if(mIsSelectedArray != null && mIsSelectedArray.length == mltArray.size()){
			for (boolean mIsSelected : mIsSelectedArray) {
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
		if(mIsSelectedArray != null && index >= 0 && index < mltArray.size()){
			boolean checked = !mIsSelectedArray[index];
			if(mIsSingle){
				if (mAutoCancel) {
					if (checked) {
						for (int i = 0; i < mIsSelectedArray.length; i++) {
							mIsSelectedArray[i] = false;
						}
						mIsSelectedArray[index] = true;
						mChoiceNumber = 1;
					} else {
						mIsSelectedArray[index] = false;
						mChoiceNumber = 0;
					}
				} else {
					checked = true;
					mIsSelectedArray = new boolean[getItemCount()];
					mIsSelectedArray[index] = true;
					mChoiceNumber = 1;
				}
			} else {
				if (mChoiceNumber >= mMaxChoiceNumber && checked) {
					for (MultiChoiceListener<T> listener : mListeners) {
						listener.onMultiChoiceMax(this, mMaxChoiceNumber);
					}
					return;
				}
				mIsSelectedArray[index] = checked;
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
//		if(index > -1 && mIsSelectedArray != null){
//			if(mIsSingle){
//				checked = true;
//				mIsSelectedArray = new boolean[getCount()];
//				mIsSelectedArray[index] = true;
//				mChoiceNumber = 1;
//			}else{
//				mIsSelectedArray[index] = checked;
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

	public void setMaxChoiceNumber(int max) {
		this.mMaxChoiceNumber = max;
	}
}
