package com.andframe.layoutbind;

import com.andframe.activity.framework.AfView;
import com.andframe.adapter.AfListAdapter.IAfLayoutItem;
/**
 * 相似列表布局适配器
 * @author scwang
 * @param <T> 实际需要布局模板
 * @param <TT> 可以适配的模板
 */
public abstract class AfLayoutItemAdapter <T,TT> implements IAfLayoutItem<T>{
	
	private IAfLayoutItem<TT> item;
	/**
	 * 一个可以适配页面的布局
	 * @param item
	 */
	public AfLayoutItemAdapter(IAfLayoutItem<TT> item) {
		// TODO Auto-generated constructor stub
		this.item = item;
	}

	@Override
	public void onHandle(AfView view) {
		// TODO Auto-generated method stub
		item.onHandle(view);
	}

	@Override
	public void onBinding(T model, int index) {
		// TODO Auto-generated method stub
		item.onBinding(this.convert(model), index);
	}

	@Override
	public int getLayoutId() {
		// TODO Auto-generated method stub
		return item.getLayoutId();
	}
	/**
	 * 把模板数据 T 转成 TT
	 * @param model
	 * @return
	 */
	protected abstract TT convert(T model);
}
