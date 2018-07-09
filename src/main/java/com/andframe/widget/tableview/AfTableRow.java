package com.andframe.widget.tableview;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andframe.util.java.AfDateFormat;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AfTableRow extends LinearLayout implements OnClickListener {

	protected static final int TYPEVALUE = TypedValue.COMPLEX_UNIT_SP;
	protected static final int PARENT = LayoutParams.MATCH_PARENT;
	protected static final int CONTENT = LayoutParams.WRAP_CONTENT;

	protected Field[] mFiled = null;
	protected View[] mViews = null;
	protected Object mObject = null;

	public AfTableRow(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AfTableRow(Context context, AfTable table) {
		super(context);
		this.setOrientation(HORIZONTAL);
		AfColumn[] columns = table.getColumns();
		mFiled = new Field[columns.length];
		mViews = new View[columns.length];
		for (int i = 0; i < mViews.length; i++) {
			if (columns[i].Type == AfColumn.CHECKBOX) {
				LinearLayout layout = new LinearLayout(context);
				CheckBox checkbox = new CheckBox(context);
				layout.setGravity(Gravity.CENTER);
				checkbox.setFocusable(false);
				checkbox.setOnClickListener(this);
				checkbox.setTag(i);
				layout.addView(checkbox);
				mViews[i] = layout;
			} else {
				TextView textcell = new TextView(context);
				textcell.setSingleLine(true);
				textcell.setGravity(Gravity.CENTER);
				textcell.setTextColor(table.TextcolorCell);
				textcell.setTextSize(TYPEVALUE, table.TextsizeCell);
				mViews[i] = textcell;
			}
			if ((i & 1) == 1) {
				this.setBackground(mViews[i], table.BackgroundIdCell);
			}
			this.addView(mViews[i], table.ltParam[i]);
		}
	}

	private void setBackground(View view, int back) {
		try {
			view.setBackgroundResource(back);
		} catch (NotFoundException e) {
			view.setBackgroundColor(back);
		}
	}

	public void Binding(AfTable table, List<?> list, int position)
			throws Exception {
		if ((position & 1) == 1) {
			this.setBackground(this, table.BackgroundIdRow);
		} else {
			this.setBackgroundColor(0);
		}
		Object data = list.get(position);
		Class<?> clazz = data.getClass();
		AfColumn[] column = table.getColumns();
		for (int i = 0; i < column.length; i++) {
			if(column[i].Binding.length() > 0){
				String[] paths = column[i].Binding.split("\\.");
				Object value = InvokeMember(column,i, clazz, paths, data, 0);
				if (column[i].Type == AfColumn.CHECKBOX) {
					BindingCheck(column[i], (LinearLayout) mViews[i], value);
				} else {
					BindingText(column[i], (TextView) mViews[i], value);
				}
			}else if (column[i].Binder != null && mViews[i] instanceof TextView) {
				String bind = column[i].Binder.onBinder(data, column[i]);
				if(bind != null){
					((TextView)mViews[i]).setText(bind);
				}
			}
		}
	}

	private void BindingCheck(AfColumn afColumn, LinearLayout layout,
			Object value) {
		CheckBox check = (CheckBox) layout.getChildAt(0);
		boolean vcheck = false;
		if (value instanceof Boolean) {
			vcheck = value.equals(true);
		} else if (value instanceof String) {
			vcheck = ((String) value).toLowerCase(Locale.ENGLISH)
					.equals("true");
		} else if (value instanceof Integer) {
			vcheck = value.equals(1);
		} else if (value instanceof Long) {
			vcheck = value.equals(1l);
		}
		check.setChecked(vcheck);
	}

	private void BindingText(AfColumn column, TextView textview, Object value) {
		if (value == null) {
			textview.setText("");
		} else {
			if(column.Binder != null){
				String bind = column.Binder.onBinder(value, column);
				if(bind != null){
					textview.setText(bind);
					return ;
				}
			}
			else if (!column.Format.equals("")) {
				if (value instanceof Date) {
					textview.setText(AfDateFormat.format(column.Format, value));
					return;// "yyyy-MM-DD"
				} else if (value instanceof Boolean) {
					String[] formats = column.Format.split(",");
					if (formats.length == 2) {
						textview.setText(formats[value.equals(false) ? 0 : 1]);
						return;// "×,√"
					}
				} else if (value instanceof Double || value instanceof Float
						|| value instanceof Long || value instanceof Integer) {
					textview.setText(new DecimalFormat(column.Format)
							.format(value));
					return;// "#0.00"
				}
			}
			textview.setText(value.toString());
		}
	}

	// / <summary>
	// / 利用反射设置 对象obj的属性path为 value
	// / </summary>
	private Object InvokeMember(AfColumn[] columns,int col, Class<?> type, String[] path,
			Object obj, int index) throws Exception {
		AfColumn column = columns[col];
		Field field = type.getField(path[index]);
		Object value = field.get(obj);
		if (path.length == index + 1) {
			if (column.Type == AfColumn.CHECKBOX) {
				mObject = obj;
				mFiled[col] = field;
				mFiled[col].setAccessible(true);
			}
			return value;
		} else if (path.length > 0 && value != null) {
			return InvokeMember(columns,col, value.getClass(), path, value,
					index + 1);
		}
		return value;
	}

	@Override
	public void onClick(View v) {
		if (v instanceof CheckBox) {
			CheckBox check = (CheckBox) v;
			try {
				int col = (Integer)check.getTag();
				Class<?> clazz = mFiled[col].getType();
				if (clazz.equals(Boolean.class) || clazz.equals(boolean.class)) {
					mFiled[col].set(mObject, check.isChecked());
				} else if (clazz.equals(String.class)) {
					mFiled[col].set(mObject, "true");
				} else if (clazz.equals(Integer.class)
						|| clazz.equals(int.class)) {
					mFiled[col].set(mObject, 1);
				} else if (clazz.equals(Long.class) || clazz.equals(long.class)) {
					mFiled[col].set(mObject, 1l);
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}
}
