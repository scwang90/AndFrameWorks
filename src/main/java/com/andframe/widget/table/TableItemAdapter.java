package com.andframe.widget.table;

import androidx.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.andframe.$;
import com.andframe.adapter.ListItemAdapter;
import com.andframe.api.adapter.ItemViewer;

import java.util.ArrayList;
import java.util.List;

/**
 * 表格适配器
 */
@SuppressWarnings("WeakerAccess")
public class TableItemAdapter extends ListItemAdapter<Object> {

	protected Table mTable = null;

	public TableItemAdapter(Table table, List<?> list) {
		super(new ArrayList<>(list));
		mTable = table;
	}

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
		TableRow row ;
		try {
			if (view == null) {
				row = new TableRow(parent.getContext(), mTable);
				view = row;
			} else {
				row = (TableRow) view;
			}
			row.Binding(mTable, mltArray, position);
		} catch (Throwable e) {
			String remark = "AfTableAdapter("+getClass().getName()+").getView";
			$.error().handle(e, remark);
		}
		return view;
	}

	public Table getTable() {
		return mTable;
	}
}
