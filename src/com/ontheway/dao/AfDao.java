package com.ontheway.dao;

import java.lang.reflect.Field;
import java.util.Date;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ontheway.application.AfExceptionHandler;
import com.ontheway.database.AfDbOpenHelper;
import com.ontheway.db.annotation.Table;
import com.ontheway.util.AfSQLHelper;

public abstract class AfDao<T> {
	protected Class<T> mClazz;
	protected SQLiteDatabase mDatabase = null;
	protected AfDbOpenHelper mHelper = null;
	protected String mTableName;
	protected String mTableColumns;
	protected String mTableValues;
	private String mTablePrefix = "t";

	public AfDao(Context context, Class<T> clazz) {
		this.mHelper = AfDbOpenHelper.getInstance(context,clazz);
		this.Initialized(clazz);
	}
	
	public AfDao(Context context, Class<T> table,String dbname) {
		this.mHelper = AfDbOpenHelper.getInstance(context,table,dbname);
		this.Initialized(table);
	}

	private void Initialized(Class<T> clazz) {
		this.mClazz = clazz;
		this.mTableName = mTablePrefix + clazz.getSimpleName();
		if (clazz.isAnnotationPresent(Table.class)) {
			Table table = clazz.getAnnotation(Table.class);
			if (table.name() != null && table.name().length() > 0) {
				mTableName = table.name();
			}
		}
		boolean isfrist = true;
		StringBuilder tValues = new StringBuilder();
		StringBuilder tColumns = new StringBuilder();
		for (Field field : mHelper.getFields(clazz)) {
			if (isfrist == false) {
				tValues.append(',');
				tColumns.append(',');
			} else {
				isfrist = false;
			}
			tValues.append('?');
			tColumns.append(field.getName());
		}
		mTableValues = tValues.toString();
		mTableColumns = tColumns.toString();
	}

	private String setValue(T obj) throws Exception {
		boolean isfrist = true;
		StringBuilder tColumns = new StringBuilder();
		for (Field field : mHelper.getFields(mClazz)) {
			if (isfrist == false) {
				tColumns.append(',');
			} else {
				isfrist = false;
			}
			tColumns.append(setValue(obj, field));
		}
		return tColumns.toString();
	}

	private String setValue(T obj, Field field) throws Exception {
		// TODO Auto-generated method stub
		Object data = field.get(obj);
		Class<?> tType = field.getType();
		if (tType.equals(Date.class)) {
			Date tDate = (Date) data;
			return field.getName() + "=" + tDate.getTime();
		} else if (tType.equals(Integer.class) || tType.equals(int.class)
				|| tType.equals(Short.class) || tType.equals(short.class)
				|| tType.equals(Long.class) || tType.equals(long.class)
				|| tType.equals(Float.class) || tType.equals(float.class)
				|| tType.equals(Double.class) || tType.equals(double.class)) {
			return field.getName() + "=" + data + "";
		} else if (tType.equals(Boolean.class) || tType.equals(boolean.class)) {
			Boolean tBoolean = (Boolean) data;
			if (tBoolean == null) {
				tBoolean = false;
			}
			return field.getName() + "='" + (tBoolean ? 1 : 0) + "'";
		} else if(data == null){
			return field.getName() + "=null";
		}else {
			String value = data==null?"null":data.toString();
			return field.getName() + "='" + value.replace("'", "") + "'";
		}
	}

	public final void close() {
		// TODO Auto-generated method stub
		synchronized(mHelper){
			mHelper.close();
		}
	}

	/**
	 * 添加一条记录
	 * 
	 * @param obj
	 * @return
	 */
	public final boolean add(T obj) {
		synchronized(mHelper){
			StringBuilder sql = new StringBuilder("insert into ");
			sql.append(mTableName);
			sql.append(" (");
			sql.append(mTableColumns);
			sql.append(") values(");
			sql.append(mTableValues);
			sql.append(")");
			try {
				Object[] tObjects = mHelper.getObjectFromFields(mClazz, obj);
				mDatabase = mHelper.getWritableDatabase();
				mDatabase.execSQL(sql.toString(), tObjects);
				return true;
			} catch (Throwable e) {
				// TODO: handle exception
				return false;
			}
		}
	}

	/**
	 * 更新一条记录
	 * 
	 * @param obj
	 * @return
	 */
	public boolean update(T obj, String where) {
		synchronized(mHelper){
		try {
			StringBuilder sql = new StringBuilder("update ");
			sql.append(mTableName);
			sql.append(" set ");
			sql.append(setValue(obj));
			sql.append(' ');
			sql.append(AfSQLHelper.Where(where));
			mDatabase = mHelper.getWritableDatabase();
			mDatabase.execSQL(sql.toString());
			return true;
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			String remark = "AfDao，update("+obj+","+where+") 出现异常\r\n";
			remark += "class = " + getClass().toString();
			AfExceptionHandler.handler(e, remark);
		}
		return false;
		}
	}

	/**
	 * 统计 全部 数据条数
	 * @return
	 */
	public int getCount() {
		synchronized(mHelper){
		mDatabase = mHelper.getReadableDatabase();
		StringBuilder sql = new StringBuilder();
		sql.append("select count(*) from ");
		sql.append(mTableName);
		Cursor cur = mDatabase.rawQuery(sql.toString(), null);
		if (cur.moveToNext()) {
			return cur.getInt(0);
		}
		return 0;
		}
	}

	/**
	 * 统计条件符合where 的 数据条数
	 * 
	 * @param column
	 * @return
	 */
	public int getCount(String where) {
		synchronized(mHelper){
		mDatabase = mHelper.getReadableDatabase();
		StringBuilder sql = new StringBuilder();
		sql.append("select count(*) from ");
		sql.append(mTableName);
		sql.append(' ');
		sql.append(AfSQLHelper.Where(where));
		Cursor cur = mDatabase.rawQuery(sql.toString(), null);
		if (cur.moveToNext()) {
			return cur.getInt(0);
		}
		return 0;
		}
	}

	/**
	 * 删除所有
	 */
	public final void delAll() {
		// TODO Auto-generated method stub
		synchronized(mHelper){
		mDatabase = mHelper.getWritableDatabase();
		mDatabase.execSQL("delete from " + mTableName);
		}
	}

	/**
	 * 删除符合Where条件的数据
	 * 
	 * @param where
	 */
	public final void delWhere(String where) {
		// TODO Auto-generated method stub
		synchronized(mHelper){
		mDatabase = mHelper.getWritableDatabase();
		mDatabase.execSQL("delete from " + mTableName + " " + AfSQLHelper.Where(where));
		}
	}

	/**
	 * 分页查询
	 * 
	 * @param column
	 * @param num
	 * @param offset
	 * @return
	 */
	protected final Cursor getCursorLimit(String column, int num, int offset) {
		synchronized(mHelper){
		mDatabase = mHelper.getReadableDatabase();
		StringBuilder sql = new StringBuilder();
		sql.append("select ");
		sql.append(column);
		sql.append(" from ");
		sql.append(mTableName);
		sql.append(" limit ");
		sql.append(num);
		sql.append(" offset ");
		sql.append(offset);
		return mDatabase.rawQuery(sql.toString(), null);
		}
	}

	/**
	 * 分页查询 带排序
	 * 
	 * @param column
	 * @param order
	 * @param num
	 * @param offset
	 * @return
	 */
	protected final Cursor getCursorLimit(String column, String order, int num,
			int offset) {
		synchronized(mHelper){
		mDatabase = mHelper.getReadableDatabase();
		StringBuilder sql = new StringBuilder();
		sql.append("select ");
		sql.append(column);
		sql.append(" from ");
		sql.append(mTableName);
		sql.append(" order by ");
		sql.append(order);
		sql.append(" limit ");
		sql.append(num);
		sql.append(" offset ");
		sql.append(offset);
		return mDatabase.rawQuery(sql.toString(), null);
		}
	}

	/**
	 * 分页查询 带排序 条件
	 * 
	 * @param column
	 * @param where
	 * @param order
	 * @param num
	 * @param offset
	 * @return
	 */
	protected final Cursor getCursorLimit(String column, String where, String order,
			int num, int offset) {
		synchronized(mHelper){
		mDatabase = mHelper.getReadableDatabase();
		StringBuilder sql = new StringBuilder();
		sql.append("select ");
		sql.append(column);
		sql.append(" from ");
		sql.append(mTableName);
		sql.append(' ');
		sql.append(AfSQLHelper.Where(where));
		sql.append(" order by ");
		sql.append(order);
		sql.append(" limit ");
		sql.append(num);
		sql.append(" offset ");
		sql.append(offset);
		return mDatabase.rawQuery(sql.toString(), null);
		}
	}

	/**
	 * 分页查询 带排序 条件
	 * 
	 * @param column
	 * @param where
	 * @param order
	 * @return
	 */
	protected final Cursor getCursorWhere(String column, String where, String order) {
		synchronized(mHelper){
		mDatabase = mHelper.getReadableDatabase();
		StringBuilder sql = new StringBuilder();
		sql.append("select ");
		sql.append(column);
		sql.append(" from ");
		sql.append(mTableName);
		sql.append(' ');
		sql.append(AfSQLHelper.Where(where));
		sql.append(" order by ");
		sql.append(order);
		return mDatabase.rawQuery(sql.toString(), null);
		}
	}
	/**
	 * 条件查询 带分页
	 * 
	 * @param column
	 * @param where
	 * @param num
	 * @param offset
	 * @return
	 */
	protected final Cursor getCursorWhere(String column, String where, int num,
			int offset) {
		synchronized(mHelper){
		mDatabase = mHelper.getReadableDatabase();
		StringBuilder sql = new StringBuilder();
		sql.append("select ");
		sql.append(column);
		sql.append(" from ");
		sql.append(mTableName);
		sql.append(' ');
		sql.append(AfSQLHelper.Where(where));
		sql.append(" limit ");
		sql.append(num);
		sql.append(" offset ");
		sql.append(offset);
		return mDatabase.rawQuery(sql.toString(), null);
		}
	}

	/**
	 * 条件查询
	 * 
	 * @param column
	 * @param where
	 * @param num
	 * @param offset
	 * @return
	 */
	protected final Cursor getCursorWhere(String column, String where) {
		synchronized(mHelper){
		mDatabase = mHelper.getReadableDatabase();
		StringBuilder sql = new StringBuilder();
		sql.append("select ");
		sql.append(column);
		sql.append(" from ");
		sql.append(mTableName);
		sql.append(' ');
		sql.append(AfSQLHelper.Where(where));
		return mDatabase.rawQuery(sql.toString(), null);
		}
	}

	/**
	 * 获取全部
	 * 
	 * @param column
	 * @return
	 */
	protected final Cursor getCursorAll(String column) {
		synchronized(mHelper){
		mDatabase = mHelper.getReadableDatabase();
		return mDatabase.rawQuery("select " + column + " from " + mTableName,
				null);
		}
	}

	/**
	 * 获取全部
	 * 
	 * @param column
	 * @return
	 */
	protected final Cursor getCursorAll(String column, String order) {
		synchronized(mHelper){
		mDatabase = mHelper.getReadableDatabase();
		StringBuilder sql = new StringBuilder();
		sql.append("select ");
		sql.append(column);
		sql.append(" from ");
		sql.append(mTableName);
		sql.append(" order by ");
		sql.append(order);
		return mDatabase.rawQuery(sql.toString(), null);
		}
	}
}
