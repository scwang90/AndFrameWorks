package com.andoffice.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.andframe.activity.AfActivity;
import com.andframe.exception.AfToastException;
import com.andframe.feature.AfIntent;
import com.andframe.util.android.AfExtraIntent;
import com.andframe.view.tableview.AfTable;
import com.andframe.view.tableview.AfTableAdapter;
import com.andframe.view.tableview.AfTableView;
import com.andoffice.R;

public class TableViewActivity extends AfActivity{

	protected AfTable mTable = null;
	protected AfTableView mTableView;
	protected AfTableAdapter mAdapter;
	
	protected static AfTable mDataTable;
	protected static List<?> mltData = new ArrayList<Object>();

	public static void setData(AfTable table) {
		mDataTable = table;
	}

	public static void setData(List<?> ltdata) {
		mltData = ltdata;
	}
	
	@Override
	protected void onCreate(Bundle bundle,AfIntent intent) throws Exception {
		super.onCreate(bundle, intent);
		//setContentView(mTableView = new AfTableView(this));
		setContentView(R.layout.layout_tableview);
		
		mTableView = findViewById(R.id.tableview_horview, AfTableView.class);
		
		findViewById(R.id.tableview_goback).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getActivity().finish();
			}
		});

		AfTable table = intent.get(EXTRA_DATA,AfTable.class);
		if(table == null){
			table = AfExtraIntent.get(EXTRA_DATA, AfTable.class);
		}
		if(table == null){
			table = mDataTable;
		}
		if(table == null){
			throw new AfToastException("找不到表格实体！");
		}
		
		mTable = AfTable.buildTable(this,table.getColumns());
		mAdapter = new AfTableAdapter(this,mTable,mltData);
		mTableView.setAdapter(mAdapter);
	}
}
