package com.andframe.widget.table;


public class Column {
	
	public interface ValueBinder{
		String onBinder(Object value, Column column);
	}

	public static final int STRING = 0;
	public static final int CHECKBOX = 1;
	
	public String Header = "";
	public String Binding = "";
	public String Format = "";
	public int Type = STRING;
	public int Width = 0;//列的宽度（像素，内部计算得到）
	public int dWidth = 0;//指定列宽（dip ）
	public int mWidth = 0;//指定最小列宽（dip ）
	public float fWidth = 0f;//指定列宽（和屏幕比例 ）
	public ValueBinder Binder = null;

	public Column(String title, String bind, float fw) {
		this(title,bind,fw,STRING);
	}
	public Column(String title, String bind, float fw, int type) {
		this.Header = title;
		this.Binding = bind;
		this.fWidth = fw;
		this.Type = type;
	}
	public Column(String title, String bind, float fw, int min, int type) {
		this(title,bind,fw,type);
		this.mWidth = min;
	}
	public Column(String title, String bind, float fw, String format) {
		this(title,bind,fw,STRING);
		this.Format = format;
	}
	public Column(String title, String bind, int dw) {
		this.Header = title;
		this.Binding = bind;
		this.dWidth = dw;
		this.Type = STRING;
	}
	public Column(String title, String bind, int dw, String format) {
		this(title,bind,dw);
		this.Format = format;
	}
	public Column(String title, String bind, int dw, ValueBinder binder) {
		this(title,bind,dw);
		this.Binder = binder;
	}
	public Column(String title, String bind, float fw, ValueBinder binder) {
		this(title,bind,fw);
		this.Binder = binder;
	}
}
