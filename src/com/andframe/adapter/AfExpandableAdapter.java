package com.andframe.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import com.andframe.activity.framework.AfView;
import com.andframe.activity.framework.AfViewable;
import com.andframe.annotation.view.BindLayout;
import com.andframe.application.AfExceptionHandler;

public abstract class AfExpandableAdapter<G, C> extends
		BaseExpandableListAdapter {

	public static class AfChild<T> {

		T mValue;

		public AfChild(T child) {
			mValue = child;
		}

		public T value() {
			return mValue;
		}
	}

	public static class AfGroup<T, C> {

		T mValue;
		List<AfChild<C>> mChilds;

		public AfGroup(T value, List<C> list) {
			mValue = value;
			setChildren(list);
		}

		public AfChild<C> get(int child) {
			return mChilds.get(child);
		}

		public T value() {
			return mValue;
		}

		public int getCount() {
			return mChilds.size();
		}

		public void setChildren(List<C> list) {
			mChilds = new ArrayList<AfChild<C>>();
			for (C child : list) {
				mChilds.add(new AfChild<C>(child));
			}
		}
	}

	public static abstract class IAfGroupItem<T> {
		private int layoutId = -1;
		public IAfGroupItem(){}
		public IAfGroupItem(int layoutId){ this.layoutId = layoutId;}
		public abstract void onHandle(AfViewable view);
		public abstract void onBinding(T model, boolean isExpanded);
		public View onInflateItem(LayoutInflater inflater, ViewGroup parent) {
			if (layoutId >= 0) {
				return inflater.inflate(layoutId, parent, false);
			} else if (this.getClass().isAnnotationPresent(BindLayout.class)) {
				BindLayout layout = this.getClass().getAnnotation(BindLayout.class);
				if (layout != null) {
					return inflater.inflate(layout.value(), parent, false);
				}
			}
			return null;
		}
	}

	public static abstract class IAfChildItem<T> {
		private int layoutId = -1;
		public IAfChildItem(){}
		public IAfChildItem(int layoutId){ this.layoutId = layoutId;}
		public abstract void onHandle(AfViewable view);
		public abstract void onBinding(T model);
		public View onInflateItem(LayoutInflater inflater, ViewGroup parent) {
			if (layoutId >= 0) {
				return inflater.inflate(layoutId, parent, false);
			} else if (this.getClass().isAnnotationPresent(BindLayout.class)) {
				BindLayout layout = this.getClass().getAnnotation(BindLayout.class);
				if (layout != null) {
					return inflater.inflate(layout.value(), parent, false);
				}
			}
			return null;
		}
	}

	protected List<AfGroup<G, C>> mGroups;
	protected LayoutInflater mInflater;

	public AfExpandableAdapter(Context context, List<AfGroup<G, C>> groups) {
		mGroups = groups;
		mInflater = LayoutInflater.from(context);
	}

	public AfExpandableAdapter(Context context, List<G> groups, List<List<C>> childs) {
		mGroups = new ArrayList<AfGroup<G, C>>();
		for (int i = 0; i < groups.size(); i++) {
			mGroups.add(new AfGroup<G, C>(groups.get(i), childs.get(i)));
		}
		mInflater = LayoutInflater.from(context);
	}

	public void setList(List<AfGroup<G, C>> groups) {
		mGroups = groups;
		notifyDataSetChanged();
	}

	public void setList(List<G> groups, List<List<C>> childs) {
		mGroups = new ArrayList<AfGroup<G, C>>();
		for (int i = 0; i < groups.size(); i++) {
			mGroups.add(new AfGroup<G, C>(groups.get(i), childs.get(i)));
		}
		notifyDataSetChanged();
	}

	public C getChildItem(int group, int child) {
		return mGroups.get(group).get(child).mValue;
	}

	@Override
	public Object getChild(int group, int child) {
		return mGroups.get(group).get(child).mValue;
	}

	@Override
	public long getChildId(int group, int child) {
		return ((0L & group) << 32) & child;
	}

	@Override
	public int getChildrenCount(int group) {
		return mGroups.get(group).getCount();
	}

	@Override
	@SuppressWarnings("unchecked")
	public View getChildView(int group, int child, boolean isLastChild,
							 View view, ViewGroup parent) {
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
			String remark = "AfExpandableListAdapter(" + getClass().getName() + ").getChildView\r\n";
			View cview = view;
			if (parent instanceof View) {
				cview = (View) parent;
			}
			if (cview != null && cview.getContext() != null) {
				remark += "class = " + cview.getContext().getClass().toString();
			}
			AfExceptionHandler.handler(e, remark);
		}
		return view;
	}

	protected boolean bindingItem(IAfChildItem<C> item, int group, int child) {
		item.onBinding(mGroups.get(group).get(child).mValue);
		return true;
	}

	protected View onInflateItem(IAfChildItem<C> item, ViewGroup parent) {
		return item.onInflateItem(mInflater, parent);
	}

	protected abstract IAfChildItem<C> getItemLayout(int group, int child);

	@Override
	public Object getGroup(int group) {
		return mGroups.get(group);
	}

	@Override
	public int getGroupCount() {
		return mGroups.size();
	}

	@Override
	public long getGroupId(int group) {
		return ((0L & group) << 32);
	}

	@Override
	@SuppressWarnings("unchecked")
	public View getGroupView(int group, boolean isExpanded, View view,
							 ViewGroup parent) {
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
			String remark = "AfExpandableListAdapter(" + getClass().getName() + ").getGroupView\r\n";
			View cview = view;
			if (parent instanceof View) {
				cview = (View) parent;
			}
			if (cview != null && cview.getContext() != null) {
				remark += "class = " + cview.getContext().getClass().toString();
			}
			AfExceptionHandler.handler(e, remark);
		}
		return view;
	}

	private void bindingItem(IAfGroupItem<G> item, int group, boolean isExpanded) {
		item.onBinding(mGroups.get(group).mValue, isExpanded);
	}

	protected abstract IAfGroupItem<G> getItemLayout(int group);

	protected View onInflateItem(IAfGroupItem<G> item, ViewGroup parent) {
		return item.onInflateItem(mInflater, parent) ;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int group, int child) {
		return true;
	}
}
