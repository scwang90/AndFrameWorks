package com.andframe.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout.LayoutParams;

import com.andframe.activity.framework.AfView;
import com.andframe.application.AfExceptionHandler;

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
	 * @return
	 */
	public List<T> getList(){
		return mltArray;
	}

	/**
	 * 适配器新增 点击更多 数据追加接口
	 * @param ltdata
	 */
	public void addData(T data) {
		// TODO Auto-generated method stub
		mltArray.add(data);
		notifyDataSetChanged();
	}

	/**
	 * 适配器新增 点击更多 数据追加接口
	 * @param ltdata
	 */
	public void addData(List<T> ltdata) {
		// TODO Auto-generated method stub
		mltArray.addAll(ltdata);
		notifyDataSetChanged();
	}

	/**
	 * 适配器新增 数据刷新 接口
	 * @param ltdata
	 */
	public void setData(List<T> ltdata) {
		// TODO Auto-generated method stub
		mltArray = new ArrayList<T>(ltdata);
		notifyDataSetChanged();
	}

	/**
	 * 适配器新增 单个数据刷新 接口
	 * @param index
	 * @param obj
	 */
	public void setData(int index, T obj) {
		// TODO Auto-generated method stub
		if (mltArray.size() > index) {
			mltArray.set(index, obj);
			notifyDataSetChanged();
		}
	}

	/**
	 * 适配器新增 数据删除 接口
	 * @param ltdata
	 */
	public void remove(int index) {
		// TODO Auto-generated method stub
		if (mltArray.size() > index) {
			mltArray.remove(index);
			notifyDataSetChanged();
		}
	}

	/**
	 * 适配器新增 数据插入 接口
	 * @param ltdata
	 */
	public void insert(int index, T object) {
		// TODO Auto-generated method stub
		if (mltArray.size() >= index) {
			mltArray.add(index, object);
			notifyDataSetChanged();
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mltArray.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mltArray.get(arg0);
	}

	public T getItemAt(int index) {
		// TODO Auto-generated method stub
		return mltArray.get(index);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	@SuppressWarnings("unchecked")
	public View getView(int position, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		// 列表视图获取必须检查 view 是否为空 不能每次都 inflate 否则手机内存负载不起
		IAfLayoutItem<T> item = null;
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
			// TODO: handle exception
			String remark = "AfListAdapter("+getClass().getName()+").getView\r\n";
			View cview = view;
			if (parent instanceof View){
				cview = (View) parent;
			}
			if (cview != null && cview.getContext() != null){
				remark += "class = " + cview.getContext().getClass().toString();
			}
			AfExceptionHandler.handler(e, remark);
		}
		return view;
	}

	protected IAfLayoutItem<T> getItemLayout(List<T> ltarray, int position) {
		// TODO Auto-generated method stub
		return getItemLayout(ltarray.get(position));
	}

	protected View onInflateItem(IAfLayoutItem<T> item, ViewGroup parent) {
		return mInflater.inflate(item.getLayoutId(), null);
	}

	protected boolean bindingItem(IAfLayoutItem<T> item, int index) {
		item.onBinding(mltArray.get(index),index);
		return true;
	}

	public static interface IAfLayoutItem<T> {
		/**
		 * 从视图中取出控件
		 * @param view
		 */
		public abstract void onHandle(AfView view);
		/**
		 * 将数据绑定到控件显示
		 * @param model
		 */
		public abstract void onBinding(T model,int index);
		/**
		 * 获取 Item 关联的 Layout ID
		 * @return
		 */
		public abstract int getLayoutId();
	}
}
