package com.andframe.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import com.andframe.activity.framework.AfView;
import com.andframe.application.AfExceptionHandler;

public abstract class AfExpandableAdapter<G, C> extends
		BaseExpandableListAdapter {

	public static class AfChild<T> {

		T mValue;

		public AfChild(T child) {
			// TODO Auto-generated constructor stub
			mValue = child;
		}

		public T value() {
			// TODO Auto-generated method stub
			return mValue;
		}
	}

	public static class AfGroup<T, C> {

		T mValue;
		List<AfChild<C>> mChilds;

		public AfGroup(T value, List<C> list) {
			// TODO Auto-generated constructor stub
			mValue = value;
			setChildren(list);
		}

		public AfChild<C> get(int child) {
			// TODO Auto-generated method stub
			return mChilds.get(child);
		}

		public T value() {
			// TODO Auto-generated method stub
			return mValue;
		}
		
		public int getCount() {
			// TODO Auto-generated method stub
			return mChilds.size();
		}

		public void setChildren(List<C> list) {
			// TODO Auto-generated method stub
			mChilds = new ArrayList<AfChild<C>>();
			for (C child : list) {
				mChilds.add(new AfChild<C>(child));
			}
		}
	}

	public interface IAfGroupItem<T> {
		public abstract void onHandle(AfView view);
		public abstract void onBinding(T model, boolean isExpanded);
		public abstract int getLayoutId();
	}

	public interface IAfChildItem<T> {
		public abstract void onHandle(AfView view);
		public abstract void onBinding(T model);
		public abstract int getLayoutId();
	}

	protected List<AfGroup<G, C>> mGroups;
	protected LayoutInflater mInflater;

	public AfExpandableAdapter(Context context, List<AfGroup<G, C>> groups) {
		// TODO Auto-generated method stub
		mGroups = groups;
		mInflater = LayoutInflater.from(context);
	}

	public AfExpandableAdapter(Context context,List<G> groups,List<List<C>> childs) {
		// TODO Auto-generated method stub
		mGroups = new ArrayList<AfGroup<G,C>>();
		for (int i = 0; i < groups.size(); i++) {
			mGroups.add(new AfGroup<G, C>(groups.get(i),childs.get(i)));
		}
		mInflater = LayoutInflater.from(context);
	}

	public void setList(List<AfGroup<G, C>> groups) {
		// TODO Auto-generated method stub
		mGroups = groups;
		notifyDataSetChanged();
	}

	public void setList(List<G> groups,List<List<C>> childs) {
		// TODO Auto-generated method stub
		mGroups = new ArrayList<AfGroup<G,C>>();
		for (int i = 0; i < groups.size(); i++) {
			mGroups.add(new AfGroup<G, C>(groups.get(i),childs.get(i)));
		}
		notifyDataSetChanged();
	}

	public C getChildItem(int group, int child) {
		// TODO Auto-generated method stub
		return mGroups.get(group).get(child).mValue;
	}

	@Override
	public Object getChild(int group, int child) {
		// TODO Auto-generated method stub
		return mGroups.get(group).get(child).mValue;
	}

	@Override
	public long getChildId(int group, int child) {
		// TODO Auto-generated method stub
		return ((0L & group) << 32) & child;
	}

	@Override
	public int getChildrenCount(int group) {
		// TODO Auto-generated method stub
		return mGroups.get(group).getCount();
	}

	@Override
	@SuppressWarnings("unchecked")
	public View getChildView(int group, int child, boolean isLastChild,
			View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		// 列表视图获取必须检查 view 是否为空 不能每次都 inflate 否则手机内存负载不起
		IAfChildItem<C> item = null;
		try {
			if (view == null) {
				item = getItemLayout(group, child);
				item.onHandle(new AfView(view = onInflateItem(item, parent)));
				view.setTag(item);
			} else {
				item = (IAfChildItem<C>) view.getTag();
			}
			bindingItem(item, group, child);
		} catch (Throwable e) {
			// TODO: handle exception
			String remark = "AfExpandableListAdapter("+getClass().getName()+").getChildView\r\n";
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

	protected boolean bindingItem(IAfChildItem<C> item, int group, int child) {
		// TODO Auto-generated method stub
		item.onBinding(mGroups.get(group).get(child).mValue);
		return true;
	}

	protected View onInflateItem(IAfChildItem<C> item, ViewGroup parent) {
		// TODO Auto-generated method stub
		return mInflater.inflate(item.getLayoutId(), null);
	}

	protected abstract IAfChildItem<C> getItemLayout(int group, int child);

	@Override
	public Object getGroup(int group) {
		// TODO Auto-generated method stub
		return mGroups.get(group);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return mGroups.size();
	}

	@Override
	public long getGroupId(int group) {
		// TODO Auto-generated method stub
		return ((0L & group) << 32);
	}

	@Override
	@SuppressWarnings("unchecked")
	public View getGroupView(int group, boolean isExpanded, View view,
			ViewGroup parent) {
		// TODO Auto-generated method stub
		// 列表视图获取必须检查 view 是否为空 不能每次都 inflate 否则手机内存负载不起
		IAfGroupItem<G> item = null;
		try {
			if (view == null) {
				item = getItemLayout(group);
				item.onHandle(new AfView(view = onInflateItem(item, parent)));
				view.setTag(item);
			} else {
				item = (IAfGroupItem<G>) view.getTag();
			}
			bindingItem(item, group, isExpanded);
		} catch (Throwable e) {
			// TODO: handle exception
			String remark = "AfExpandableListAdapter("+getClass().getName()+").getGroupView\r\n";
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

	private void bindingItem(IAfGroupItem<G> item, int group, boolean isExpanded) {
		// TODO Auto-generated method stub
		item.onBinding(mGroups.get(group).mValue, isExpanded);
	}

	protected abstract IAfGroupItem<G> getItemLayout(int group);

	protected View onInflateItem(IAfGroupItem<G> item, ViewGroup parent) {
		// TODO Auto-generated method stub
		return mInflater.inflate(item.getLayoutId(), null);
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int group, int child) {
		// TODO Auto-generated method stub
		return true;
	}
}
