package com.andframe.widget.tableview;

import android.content.Context;
import android.graphics.Color;
import android.widget.LinearLayout.LayoutParams;

import com.andframe.util.android.AfDensity;

@SuppressWarnings("unused")
public class AfTable{
	
	public int Column;
	public int RowHeight;
	public int HeaderHeight;
	public int BackgroundId = Color.rgb(230, 230, 230);
	public int BackgroundIdCell = Color.argb(20, 100, 100, 100);
	public int BackgroundIdRow = Color.argb(90,180, 210, 235);
	public int BackgroundIdHeader = Color.rgb(50, 160, 200);
	public int BackgroundIdHeaderCell = Color.argb(20, 100, 100, 100);
	public int TextsizeCell = 18;
	public int TextsizeHeader = 22;
	public int TextcolorCell = Color.DKGRAY;
	public int TextcolorHeader = Color.WHITE;
	public LayoutParams[] ltParam;
	
	protected AfColumn[] mColumns = null;
//	protected WeakReference<List<?>> mRefData = null;

//	public AfTable(List<AfColumn> columns, List<?> datas) {
//		mRefData = new WeakReference<List<?>>(datas);
//		mColumns = columns.toArray(new AfColumn[0]);
//	}

	public AfTable(Context context, AfColumn[] columns) {
		this.mColumns = columns;
		this.Column = columns.length;
		this.RowHeight = AfDensity.dp2px(38);
		this.HeaderHeight = AfDensity.dp2px(45);
	}

	public AfColumn[] getColumns() {
		return mColumns;
	}

//	public void setData(List<?> datas){
//		mRefData = new WeakReference<List<?>>(datas);
//	}
//	
//	public List<?> getData(){
//		if(mRefData != null && !mRefData.isEnqueued()){
//			return mRefData.get();
//		}
//		return new ArrayList<Object>();
//	}

//	public List<String[]> buildContent() {
//		List<String[]> list = new ArrayList<String[]>();
//		try {
//			Class<?> clazz = mRefData.get().get(0).getClass();
//			for (Object data : mRefData.get()) {
//				String[] content = new String[mColumns.length];
//				for (int i = 0; i < content.length; i++) {
//					Field field =	clazz.getField(mColumns[i].Binding);
//					Object value = field.get(data);
//					if(value instanceof Date){
//						content[i] = AfDateFormat.SIMPLE.format(value);
//					}else{
//						content[i] = value.toString();
//					}
//				}
//				list.add(content);
//			}
//		} catch (Throwable e) {
//		}
//		return list;
//	}

	/**
	 * @param offset	屏幕宽度偏差
	 */
	public static AfTable buildTable(Context context,AfColumn[] columns,int offset){
		AfTable table = new AfTable(context, columns);
		table.ltParam = new LayoutParams[columns.length];
		int totalwidth = 0;
		for (int i = 0; i < columns.length; i++) {
			AfColumn column = columns[i];
			if(column.fWidth > 0f){
				if(column.mWidth > 0){
					column.mWidth = AfDensity.dp2px(column.mWidth);
				}
				column.Width = AfDensity.proportion(column.fWidth,offset);
				if(column.Width < column.mWidth){
					column.Width = column.mWidth;
				}
			}else{
				column.Width = AfDensity.dp2px(column.dWidth);
			}
			totalwidth += column.Width;
			table.ltParam[i] = new LayoutParams(column.Width,table.RowHeight);
		}
		if (totalwidth < AfDensity.getWidthPixels()) {
			for (int i = 0; i < columns.length; i++) {
				AfColumn column = columns[i];
				column.fWidth = 1.0f*column.Width/totalwidth;
				column.Width = AfDensity.proportion(column.fWidth,offset);
				table.ltParam[i] = new LayoutParams(column.Width,table.RowHeight);
			}
		}
		return table;
	}

	/**
	 *
	 */
	public static AfTable buildTable(Context context, AfColumn[] columns) {
		return buildTable(context, columns, 0);
	}
}
