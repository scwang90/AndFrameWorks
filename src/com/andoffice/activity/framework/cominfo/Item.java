package com.andoffice.activity.framework.cominfo;

import java.lang.reflect.Field;
import java.util.Date;

import android.widget.TextView;

import com.andframe.application.AfExceptionHandler;
import com.andframe.util.java.AfDateFormat;
import com.andframe.util.java.AfReflecter;

public class Item {
	public static final int SELECTOR_DATE = AbCominfoActivity.ID_DATE;

	public static final int SELECTOR_TIME = AbCominfoActivity.ID_TIME;

	public static final int SELECTOR_DATETIME = AbCominfoActivity.ID_DATETIME;

	public static final int TEXT = 100;

	public static final int NUMBER = 101;

	public static final int PHONE = 102;

	public static final int DISABLE = 103;

	public static final int PASSWORD = 104;

	public static final int BUTTON = 105;

	public static final int CHECK = 200;

	public static final int CHECKPOWER = 201;

	public static final int TEXTBOXSINGLE = 300;

	public static final int TEXTBOXMULTI = 301;

	public static final int ID_DEFAULT = -1;

	public int id = ID_DEFAULT; // 唯一标识item

	public int type = TEXT;

	public int ivalue = 0;

	public long lvalue = 0;

	public float fvalue = 0;

	public double dvalue = 0;

	public Date vdate = new Date();

	public String value = "";

	public String name = "";

	public String[] values = null;

	public boolean blvalue = false;

	public boolean notnull = false;

	public TextView mTextView = null;

	/**
	 * 新增利用反射绑定值
	 */
	public String binding = "";

	public Object model = "";

	public boolean isEmpty() {
		if (type == CHECK) {
			return false;
		}
		if (mTextView != null) {
			if (type == TEXTBOXSINGLE || type == TEXTBOXMULTI) {
				value = mTextView.getText().toString();
				;
				binding(value);
			}
			return mTextView.getText().length() == 0;
		}
		return value.equals("");
	}

	public Item(String name, boolean value) {
		this.type = CHECK;
		this.name = name;
		this.blvalue = value;
	}

	public Item(String name, boolean value, int type) {
		this(name, value);
		this.type = type;
	}

	public Item(boolean notnull, String name, String value) {
		this.name = name;
		this.value = value;
		this.notnull = notnull;
	}

	public Item(boolean notnull, String name, Object model, String binding) {
		this.name = name;
		this.notnull = notnull;
		initBinding(model, binding);
	}

	public Item(boolean notnull, String name, Object model, String binding,
			int type) {
		this(notnull, name, model, binding);
		if (this.type != type) {
			this.type = type;
			if (type == SELECTOR_DATE) {
				formatdate(vdate);
			} else if (type == SELECTOR_TIME) {
				formattime(vdate);
			}
		}
	}

	public Item(boolean notnull, String name, Object model, String binding,
			String[] values) {
		this.name = name;
		this.values = values;
		this.notnull = notnull;
		initBinding(model, binding);
		if (this.ivalue < values.length
				&& (value.length() == 0 || type == NUMBER)) {
			this.setValue(values[this.ivalue]);
		}
	}

	public Item(boolean notnull, String name, double value) {
		this(notnull, name, value + "");
		this.ivalue = (int) value;
		this.lvalue = (long) value;
		this.fvalue = (float) value;
		this.dvalue = value;
		this.type = NUMBER;
		if (lvalue == dvalue) {
			this.value = String.valueOf(lvalue);
		}
	}

	public Item(boolean notnull, int id, String name, double value) {
		this(notnull, name, value);
		this.id = id;
	}

	public Item(boolean notnull, int id, String name, String value) {
		this(notnull, name, value);
		this.id = id;
	}

	public Item(boolean notnull, String name, String value, String[] values) {
		this(notnull, name, value);
		this.values = values;
		for (int i = 0; i < values.length; i++) {
			if (value.equals(values[i])) {
				ivalue = i;
				lvalue = i;
			}
		}
	}

	public Item(boolean notnull, String name, String value, int type) {
		this(notnull, name, value);
		this.type = type;
		if (type == NUMBER && !value.equals("")) {
			try {
				dvalue = Double.valueOf(value);
				ivalue = (int) dvalue;
				fvalue = (float) dvalue;
			} catch (Throwable ex) {
				AfExceptionHandler.handle(ex, "Item.Double.valueOf 出现异常");
			}
		}
	}

	public Item(boolean notnull, String name, double value, int type) {
		this(notnull, name, value);
		this.type = type;
		if (type == NUMBER && !(value + "").equals("")) {
			try {
				dvalue = Double.valueOf(value);
				ivalue = (int) dvalue;
				fvalue = (float) dvalue;
			} catch (Throwable ex) {
				AfExceptionHandler.handle(ex, "Item.Double.valueOf 出现异常");
			}
		}
	}

	public Item(boolean notnull, String name, Date date, int type) {
		this(notnull, name, "");
		this.type = type;
		if (date != null) {
			this.vdate = date;
			if (type == SELECTOR_DATE) {
				this.formatdate(date);
			} else if (type == SELECTOR_TIME) {
				this.formattime(date);
			} else if (type == SELECTOR_DATETIME || type == DISABLE) {
				this.formatdatetime(date);
			}
		}
	}

	public void formatdate(Date date) {
		value = AfDateFormat.DATE.format(date);
		if (mTextView != null) {
			mTextView.setText(value);
		}
	}

	public void formattime(Date date) {
		value = AfDateFormat.TIME.format(date);
		if (mTextView != null) {
			mTextView.setText(value);
		}
	}

	public void formatdatetime(Date date) {
		value = AfDateFormat.SIMPLE.format(date);
		if (mTextView != null) {
			mTextView.setText(value);
		}
	}

	public void setValue(String value) {
		this.value = value;
		if (mTextView != null) {
			mTextView.setText(value);
		}
		// 如果绑定字符串不成功
		if (!binding(value)) {
			// 试着绑定 数字
			Field field = AfReflecter.getFieldNoException(model, binding);
			try {
				Class<?> clazz = field != null ? field.getType() : null;
				if (clazz != null) {
					if (int.class.equals(clazz) || Integer.class.equals(clazz)) {
						this.ivalue = Integer.valueOf(value);
						binding(this.ivalue);
					} else if (long.class.equals(clazz)
							|| Long.class.equals(clazz)) {
						this.lvalue = Long.valueOf(value);
						binding(this.lvalue);
					} else if (float.class.equals(clazz)
							|| Float.class.equals(clazz)) {
						this.fvalue = Float.valueOf(value);
						binding(this.fvalue);
					} else if (double.class.equals(clazz)
							|| Double.class.equals(clazz)) {
						this.dvalue = Double.valueOf(value);
						binding(this.dvalue);
					}
				}
			} catch (Throwable e) {
			}
		}
	}

	/**
	 * 绑定 数据到 Item 值
	 * @param object
	 * @return
	 */
	public boolean binding(Object object) {
		if (model != null && !binding.equals("")) {
			return AfReflecter.setMemberNoException(model, binding, object);
		}
		return false;
	}

	protected void initBinding(Object model, String binding) {
		this.model = model;
		this.binding = binding;
		Field field = AfReflecter.getFieldNoException(model, binding);
		Class<?> clazz = field != null ? field.getType() : null;
		if (boolean.class.equals(clazz) || Boolean.class.equals(clazz)) {
			this.type = CHECK;
			Boolean value = AfReflecter.getMemberNoException(model,binding, Boolean.class);
			this.blvalue = value != null ? value : false;
			this.ivalue = blvalue ? 1 : 0;
			this.value = String.valueOf(blvalue);
		} else if (int.class.equals(clazz) || Integer.class.equals(clazz)) {
			this.type = NUMBER;
			Integer value = AfReflecter.getMemberNoException(model,binding, Integer.class);
			this.ivalue = value != null ? value : 0;
			this.value = String.valueOf(ivalue);
		} else if (long.class.equals(clazz) || Long.class.equals(clazz)) {
			this.type = NUMBER;
			Long value = AfReflecter.getMemberNoException(model, binding,Long.class);
			this.lvalue = value != null ? value : 0;
			this.value = String.valueOf(lvalue);
		} else if (float.class.equals(clazz) || Float.class.equals(clazz)) {
			this.type = NUMBER;
			Float value = AfReflecter.getMemberNoException(model, binding,Float.class);
			this.fvalue = value != null ? value : 0;
			this.value = String.valueOf(fvalue);
		} else if (double.class.equals(clazz) || Double.class.equals(clazz)) {
			this.type = NUMBER;
			Double value = AfReflecter.getMemberNoException(model, binding,Double.class);
			this.dvalue = value != null ? value : 0;
			this.value = String.valueOf(dvalue);
		} else if (String.class.equals(clazz)) {
			this.type = TEXT;
			String value = AfReflecter.getMemberNoException(model, binding,String.class);
			this.value = value != null ? value : "";
		} else if (Date.class.equals(clazz)) {
			this.type = SELECTOR_DATETIME;
			Date value = AfReflecter.getMemberNoException(model, binding,Date.class);
			this.vdate = value != null ? value : new Date();
			this.formatdatetime(vdate);
		}
	}

}
