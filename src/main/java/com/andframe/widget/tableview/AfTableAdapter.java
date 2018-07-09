package com.andframe.widget.tableview;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.andframe.adapter.AfListAdapter;
import com.andframe.api.adapter.ItemViewer;
import com.andframe.exception.AfExceptionHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * 表格适配器
 */
public class AfTableAdapter extends AfListAdapter<Object> {

	protected AfTable mTable = null;

	public AfTableAdapter(AfTable table, List<?> list) {
		super(new ArrayList<>(list));
		mTable = table;
	}

//	@Override
//	public int getCount() {
//		return 0;
//	}

	/**
	 * @deprecated
	 */
	@NonNull
	@Override
	public ItemViewer<Object> newItemViewer(int viewType) {
		return null;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		// 列表视图获取必须检查 view 是否为空 不能每次都 inflate
		// 否则手机内存负载不起
		AfTableRow row ;
		try {
			if (view == null) {
				row = new AfTableRow(parent.getContext(), mTable);
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
