package com.andframe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout.LayoutParams;

import com.andframe.activity.framework.AfView;
import com.andframe.application.AfExceptionHandler;

import java.util.ArrayList;
import java.util.List;

public abstract class AfListAdapter<T> extends BaseAdapter {
	
	protected static final int LP_MP = LayoutParams.MATCH_PARENT;
	protected static final int LP_WC = LayoutParams.WRAP_CONTENT;
	
	protected LayoutInflater mInflater;
	protected List<T> mltArray = new ArrayList<T>();

	protected abstract IAfLayoutItem<T> getItemLayout(T data);

	public AfListAdapter(Context context, List<T> ltdata) {
		mltArray = ltdata;
		mInflater = LayoutInflater.from(context);
	}

	/**
	 * 获取数据列表
	 */
	public List<T> getList(){
		return mltArray;
	}

	/**
	 * 适配器新增 点击更多 数据追加接口
	 */
	public void addData(T data) {
		mltArray.add(data);
		notifyDataSetChanged();
	}

	/**
	 * 适配器新增 点击更多 数据追加接口
	 */
	public void addData(List<T> ltdata) {
		mltArray.addAll(ltdata);
		notifyDataSetChanged();
	}

	/**
	 * 适配器新增 数据刷新 接口
	 */
	public void setData(List<T> ltdata) {
		mltArray = new ArrayList<>(ltdata);
		notifyDataSetChanged();
	}

	/**
	 * 适配器新增 单个数据刷新 接口
	 */
	public void setData(int index, T obj) {
		if (mltArray.size() > index) {
			mltArray.set(index, obj);
			notifyDataSetChanged();
		}
	}

	/**
	 * 适配器新增 数据删除 接口
	 */
	public void remove(int index) {
		if (mltArray.size() > index) {
			mltArray.remove(index);
			notifyDataSetChanged();
		}
	}

	/**
	 * 适配器新增 数据插入 接口
	 */
	public void insert(int index, T object) {
		if (mltArray.size() >= index) {
			mltArray.add(index, object);
			notifyDataSetChanged();
		}
	}

	/**
	 * 适配器新增 数据插入 接口
	 */
	public void insert(int index, List<T> lsit) {
		if (mltArray.size() >= index) {
			mltArray.addAll(index, lsit);
			notifyDataSetChanged();
		}
	}

	@Override
	public int getCount() {
		return mltArray.size();
	}

	@Override
	public Object getItem(int arg0) {
		return getItemAt(arg0);
	}

	public T getItemAt(int index) {
		return mltArray.get(index);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	@SuppressWarnings("unchecked")
	public View getView(int position, View view, ViewGroup parent) {
		// 列表视图获取必须检查 view 是否为空 不能每次都 inflate 否则手机内存负载不起
		IAfLayoutItem<T> item;
		try {
			if (view == null) {
				item = getItemLayout(mltArray,position);
				item.onHandle(new AfView(view = onInflateItem(item, parent)));
				view.setTag(item);
			} else {
				item = (IAfLayoutItem<T>) view.getTag();
			}
			bindingItem(item, position);
		} catch (Throwable e) {
			String remark = "AfListAdapter("+getClass().getName()+").getView\r\n";
			View cview = view;
			if (parent != null){
				cview = parent;
			}
			if (cview != null && cview.getContext() != null){
				remark += "class = " + cview.getContext().getClass().toString();
			}
			if (view == null) {
				view = new View(mInflater.getContext());
			}
			AfExceptionHandler.handler(e, remark);
		}
		return view;
	}

	protected IAfLayoutItem<T> getItemLayout(List<T> ltarray, int position) {
		return getItemLayout(getItemAt(position));
	}

	protected View onInflateItem(IAfLayoutItem<T> item, ViewGroup parent) {
		return mInflater.inflate(item.getLayoutId(), null);
	}

	protected boolean bindingItem(IAfLayoutItem<T> item, int index) {
		item.onBinding(getItemAt(index),index);
		return true;
	}

	public interface IAfLayoutItem<T> {
		/**
		 * 从视图中取出控件
		 */
		void onHandle(AfView view);
		/**
		 * 将数据绑定到控件显示
		 */
		void onBinding(T model, int index);
		/**
		 * 获取 Item 关联的 InjectLayout ID
		 */
		int getLayoutId();
	}
}
