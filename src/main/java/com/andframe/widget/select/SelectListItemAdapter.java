package com.andframe.widget.select;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.andframe.adapter.ListItemAdapter;
import com.andframe.api.adapter.ItemViewer;

import java.util.*;

@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class SelectListItemAdapter<T> extends ListItemAdapter<T> {

	public interface SelectListener<T> {
		void onSelectMax(@NonNull SelectListItemAdapter<T> adapter, int max);
		void onSelectChanged(@NonNull SelectListItemAdapter<T> adapter, T tag, boolean selected, int number);
		void onSelectChanged(@NonNull SelectListItemAdapter<T> adapter, int number, int total);
		void onSelectStarted(@NonNull SelectListItemAdapter<T> adapter, int number);
		void onSelectClosed(@NonNull SelectListItemAdapter<T> adapter, List<T> list);
	}

	public static class SampleSelectListener<T> implements SelectListener<T> {
		@Override
		public void onSelectMax(@NonNull SelectListItemAdapter<T> adapter, int max) {
		}
		@Override
		public void onSelectChanged(@NonNull SelectListItemAdapter<T> adapter, T tag, boolean selected, int number) {
		}
		@Override
		public void onSelectChanged(@NonNull SelectListItemAdapter<T> adapter, int number, int total) {
		}
		@Override
		public void onSelectStarted(@NonNull SelectListItemAdapter<T> adapter, int number) {
		}
		@Override
		public void onSelectClosed(@NonNull SelectListItemAdapter<T> adapter, List<T> list) {
		}
	}


	public interface GenericListener {
		void onSelectAddData(SelectListItemAdapter<?> adapter, Collection<?> list);
		void onSelectChanged(SelectListItemAdapter<?> adapter, Object tag, boolean selected, int number);
		void onSelectChanged(SelectListItemAdapter<?> adapter, int number, int total);
		void onSelectStarted(SelectListItemAdapter<?> adapter, int number);
		void onSelectClosed(SelectListItemAdapter<?> adapter, Collection<?> list);
	}
	
	protected int mChoiceNumber = 0;
	protected int mMaxChoiceNumber = Integer.MAX_VALUE;
	protected boolean mIsSingle = false;
	protected boolean mAutoCancel = false;
	protected boolean[] mIsSelectedArray = null;
	protected List<SelectListener<T>> mListeners = new ArrayList<>();
	protected List<GenericListener> mGenericListeners = new ArrayList<>();

	public SelectListItemAdapter() {
	}

	public SelectListItemAdapter(List<T> list) {
		super(list);
	}

	public SelectListItemAdapter(List<T> list, boolean dataSync) {
		super(list, dataSync);
	}

	public void addListener(SelectListener<T> listener) {
		if(mListeners.indexOf(listener) < 0){
			mListeners.add(listener);
		}
	}
	
	public void addGenericityListener(GenericListener listener) {
		if(mGenericListeners.indexOf(listener) < 0){
			mGenericListeners.add(listener);
		}
	}
	
	public int getChoiceNumber() {
		return mChoiceNumber;
	}

	@Override
	public boolean addAll(@NonNull Collection<? extends T> list) {
		if(isSelectMode() && list.size() > 0){
			boolean[] old = mIsSelectedArray;
			mIsSelectedArray = new boolean[old.length+list.size()];
			System.arraycopy(old, 0, mIsSelectedArray, 0, old.length);
			boolean ret = super.addAll(list);
			for (GenericListener listener : mGenericListeners) {
				listener.onSelectAddData(this, list);
			}
			return ret;
		} else {
			return super.addAll(list);
		}
	}
	
	@Override
	public void set(@NonNull List<T> list) {
		if(isSelectMode() && list.size() != mIsSelectedArray.length){
			super.set(list);
			mChoiceNumber = 0;
			mIsSelectedArray = new boolean[list.size()];
			for (GenericListener listener : mGenericListeners) {
				listener.onSelectChanged(this, 0, mIsSelectedArray.length);
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
		if(isSelectMode()){
			super.add(index, object);
			mChoiceNumber = 0;
			mIsSelectedArray = new boolean[getItemCount()];
			for (GenericListener listener : mGenericListeners) {
				listener.onSelectChanged(this, 0, mIsSelectedArray.length);
			}
		}else{
			super.add(index, object);
		}
	}
	
	@Override
	public T remove(int index) {
		if(isSelectMode()){
			T model = super.remove(index);
			mChoiceNumber = 0;
			mIsSelectedArray = new boolean[getItemCount()];
			for (GenericListener listener : mGenericListeners) {
				listener.onSelectChanged(this, 0, mIsSelectedArray.length);
			}
			return model;
		}else{
			return super.remove(index);
		}
	}

	@NonNull
	@Override
	public ItemViewer<T> newItemViewer(int viewType) {
		return newSelectItem(viewType);
	}
	
	protected abstract SelectListItemViewer<T> newSelectItem(int viewType);

	@Override
	public void bindingItem(View view, ItemViewer<T> item, int index) {
		SelectListItemViewer<T> mcitem = (SelectListItemViewer<T>)item;
		SelectListItemViewer.SelectStatus status = SelectListItemViewer.SelectStatus.NONE;
		if(mIsSelectedArray != null){
			if(mIsSelectedArray[index]){
				status = SelectListItemViewer.SelectStatus.SELECTED;
			}else{
				status = SelectListItemViewer.SelectStatus.UN_SELECT;
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

	public boolean beginSelectMode(int index, boolean notify) {
		if (mIsSelectedArray == null) {
			mIsSelectedArray = new boolean[getItemCount()];
			if (index > -1) {
				mIsSelectedArray[index] = true;
				mChoiceNumber = 1;
			}
			if (notify) {
				notifyDataSetChanged();
			}
			for (GenericListener listener : mGenericListeners) {
				listener.onSelectStarted(this, mChoiceNumber);
			}
			for (SelectListener<T> listener : mListeners) {
				listener.onSelectStarted(this, mChoiceNumber);
			}
		}
		return true;
	}
	
	public boolean beginSelectMode() {
		return beginSelectMode(true);
	}

	public boolean beginSelectMode(int index) {
		return beginSelectMode(index, true);
	}

	public boolean beginSelectMode(boolean notify) {
		return beginSelectMode(-1, notify);
	}

	public boolean isSelectMode() {
		return mIsSelectedArray != null;
	}
	
	public boolean closeSelectMode() {
		if(mIsSelectedArray != null){
			for (GenericListener listener : mGenericListeners) {
				listener.onSelectClosed(this,peekSelectedItems());
			}
			for (SelectListener<T> listener : mListeners) {
				listener.onSelectClosed(this,peekSelectedItems());
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

	public void setSelectedItems(Collection<T> collection) {
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
			for (GenericListener listener : mGenericListeners) {
				listener.onSelectChanged(this, mChoiceNumber, getCount());
			}
			for (SelectListener<T> listener : mListeners) {
				listener.onSelectChanged(this, mChoiceNumber, getCount());
			}
		} else {
			selectNone();
		}
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
			closeSelectMode();
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
			Arrays.fill(mIsSelectedArray, true);
//			for (int i = 0; i < mIsSelectedArray.length; i++) {
//				mIsSelectedArray[i] = true;
//			}
			notifyDataSetChanged();
			for (GenericListener listener : mGenericListeners) {
				listener.onSelectChanged(this, mChoiceNumber,getCount());
			}
			for (SelectListener<T> listener : mListeners) {
				listener.onSelectChanged(this, mChoiceNumber,getCount());
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
			for (GenericListener listener : mGenericListeners) {
				listener.onSelectChanged(this, mChoiceNumber,getCount());
			}
			for (SelectListener<T> listener : mListeners) {
				listener.onSelectChanged(this, mChoiceNumber,getCount());
			}
		}
	}

	public void selectNone() {
		if(mIsSelectedArray != null){
			mChoiceNumber = 0;
			mIsSelectedArray = new boolean[getCount()];
			notifyDataSetChanged();
			for (GenericListener listener : mGenericListeners) {
				listener.onSelectChanged(this, mChoiceNumber,getCount());
			}
			for (SelectListener<T> listener : mListeners) {
				listener.onSelectChanged(this, mChoiceNumber,getCount());
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
		if (item instanceof SelectListItemViewer) {
			return ((SelectListItemViewer<T>)item).inflateLayout(view,this);
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
					for (SelectListener<T> listener : mListeners) {
						listener.onSelectMax(this, mMaxChoiceNumber);
					}
					return;
				}
				mIsSelectedArray[index] = checked;
				mChoiceNumber += checked ? 1 : -1;
			}
			notifyDataSetChanged();
			for (GenericListener listener : mGenericListeners) {
				listener.onSelectChanged(this, mltArray.get(index), checked, mChoiceNumber);
			}
			for (SelectListener<T> listener : mListeners) {
				listener.onSelectChanged(this, mltArray.get(index), checked, mChoiceNumber);
			}
		}
	}
	
	void setSelect(T tag, boolean checked) {
		this.onItemClick(mltArray.indexOf(tag));
	}
	
	public void setSingle(boolean single) {
		mIsSingle = single;
	}

	public void setMaxChoiceNumber(int max) {
		this.mMaxChoiceNumber = max;
	}
}
