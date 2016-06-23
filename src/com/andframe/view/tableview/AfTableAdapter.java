package com.andframe.view.tableview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.andframe.adapter.AfListAdapter;
import com.andframe.application.AfExceptionHandler;

import java.util.ArrayList;
import java.util.List;

public class AfTableAdapter extends AfListAdapter<Object> {

	protected AfTable mTable = null;

	public AfTableAdapter(Context context, AfTable table, List<?> ltdata) {
		super(context, new ArrayList<Object>(ltdata));
		mTable = table;
	}

//	@Override
//	public int getCount() {
//		return 0;
//	}

	/**
	 * @deprecated
	 */
	@Override
	protected final IAfLayoutItem<Object> getItemLayout(Object data) {
		return null;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) { // 列表视图获取必须检查 view 是否为空 不能每次都 inflate
		// 否则手机内存负载不起
		AfTableRow row = null;
		try {
			if (view == null) {
				row = new AfTableRow(mInflater.getContext(), mTable);
				view = row;
			} else {
				row = (AfTableRow) view;
			}
			row.Binding(mTable, mltArray, position);
		} catch (Throwable e) {
			String remark = "AfTableAdapter("+getClass().getName()+").getView";
			AfExceptionHandler.handle(e, remark);
		}
		return view;
	}

	public AfTable getTable() {
		return mTable;
	}
}
