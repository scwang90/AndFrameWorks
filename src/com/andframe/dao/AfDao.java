package com.andframe.dao;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.andframe.annotation.db.interpreter.Interpreter;
import com.andframe.application.AfExceptionHandler;
import com.andframe.database.AfDbOpenHelper;
import com.andframe.helper.java.AfSQLHelper;
import com.andframe.util.UUIDUtil;
import com.andframe.util.java.AfReflecter;

public abstract class AfDao<T> {

	protected Class<T> mClazz;
	protected SQLiteDatabase mDbWriteable = null;
	protected SQLiteDatabase mDbReadable = null;
	protected AfDbOpenHelper mHelper = null;
	protected String mTableName;
	protected String mTableColumns;
	protected String mTableValues;

	@SuppressWarnings("unchecked")
	public AfDao(Context context) {
		this.mClazz = (Class<T>)AfReflecter.getActualTypeArgument(this, AfDao.class, 0);
		this.mHelper = AfDbOpenHelper.getInstance(context,mClazz);
		this.mDbWriteable = mHelper.getWritableDatabase();
		this.mDbReadable = mHelper.getReadableDatabase();
		this.mTableName = Interpreter.getTableName(mClazz);
		this.Initialized(mClazz);
	}

	@SuppressWarnings("unchecked")
	public AfDao(Context context,String dbname) {
		this.mClazz = (Class<T>)AfReflecter.getActualTypeArgument(this, AfDao.class, 0);
		this.mHelper = AfDbOpenHelper.getInstance(context,mClazz,dbname);
		this.mDbWriteable = mHelper.getWritableDatabase();
		this.mDbReadable = mHelper.getReadableDatabase();
		this.mTableName = Interpreter.getTableName(mClazz);
		this.Initialized(mClazz);
	}

	public AfDao(Context context,Class<T> clazz) {
		this.mClazz = clazz;
		this.mHelper = AfDbOpenHelper.getInstance(context,mClazz);
		this.mDbWriteable = mHelper.getWritableDatabase();
		this.mDbReadable = mHelper.getReadableDatabase();
		this.mTableName = Interpreter.getTableName(mClazz);
		this.Initialized(mClazz);
	}

	public AfDao(Context context,Class<T> clazz,String dbname) {
		this.mClazz = clazz;
		this.mHelper = AfDbOpenHelper.getInstance(context,mClazz,dbname);
		this.mDbWriteable = mHelper.getWritableDatabase();
		this.mDbReadable = mHelper.getReadableDatabase();
		this.mTableName = Interpreter.getTableName(mClazz);
		this.Initialized(mClazz);
	}

	@SuppressWarnings("unchecked")
	public AfDao(Context context,String path,String dbname) {
		this.mClazz = (Class<T>)AfReflecter.getActualTypeArgument(this, AfDao.class, 0);
		this.mHelper = AfDbOpenHelper.getInstance(context,mClazz,path,dbname);
		this.mDbWriteable = mHelper.getWritableDatabase();
		this.mDbReadable = mHelper.getReadableDatabase();
		this.mTableName = Interpreter.getTableName(mClazz);
		this.Initialized(mClazz);
	}

	public AfDao(Context context,Class<T> clazz,String path,String dbname) {
		this.mClazz = clazz;
		this.mHelper = AfDbOpenHelper.getInstance(context,mClazz,path,dbname);
		this.mDbWriteable = mHelper.getWritableDatabase();
		this.mDbReadable = mHelper.getReadableDatabase();
		this.mTableName = Interpreter.getTableName(mClazz);
		this.Initialized(mClazz);
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		this.close();
	}

	private void Initialized(Class<T> clazz) {
		boolean isfrist = true;
		StringBuilder values = new StringBuilder();
		StringBuilder columns = new StringBuilder();

		for (Field field : AfReflecter.getField(clazz)) {
			if (Interpreter.isColumn(field)) {
				if (isfrist == false) {
					values.append(',');
					columns.append(',');
				} else {
					isfrist = false;
				}
				values.append('?');
				columns.append(Interpreter.getColumnName(field));
			}
		}
		mTableValues = values.toString();
		mTableColumns = columns.toString();
	}

	private String setValue(T obj) throws Exception {
		boolean isfrist = true;
		StringBuilder tColumns = new StringBuilder();
		for (Field field : AfReflecter.getField(mClazz)) {
			if (Interpreter.isColumn(field)) {
				if (isfrist == false) {
					tColumns.append(',');
				} else {
					isfrist = false;
				}
				tColumns.append(setValue(obj, field));
			}
		}
		return tColumns.toString();
	}

	private String setValue(T obj, Field field) throws Exception {
		Object data = field.get(obj);
		Class<?> type = field.getType();
		if (type.equals(Date.class)) {
			Date date = (Date) data;
			return Interpreter.getColumnName(field) + "=" + date.getTime();
		} else if (type.equals(Integer.class) || type.equals(int.class)
				|| type.equals(Short.class) || type.equals(short.class)
				|| type.equals(Long.class) || type.equals(long.class)
				|| type.equals(Float.class) || type.equals(float.class)
				|| type.equals(Double.class) || type.equals(double.class)) {
			return Interpreter.getColumnName(field) + "=" + data + "";
		} else if (type.equals(Boolean.class) || type.equals(boolean.class)) {
			Boolean bool = (Boolean) data;
			if (bool == null) {
				bool = false;
			}
			return Interpreter.getColumnName(field) + "='" + (bool ? 1 : 0) + "'";
		} else if (data == null){
			return Interpreter.getColumnName(field) + "=null";
		}else {
			String value = data==null?"null":data.toString();
			return Interpreter.getColumnName(field) + "='" + value.replace("'", "") + "'";
		}
	}
	/**
	 * 关闭之后会出现各种问题
	 */
	public final void close() {
//		synchronized(mHelper){
//			mDbReadable.close();
//			mDbWriteable.close();
//			mHelper.close();
//		}
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
				Object[] tObjects = this.getObjectFromFields(obj);
				mDbWriteable.execSQL(sql.toString(), tObjects);
				return true;
			} catch (Throwable e) {
				return false;
			}
		}
	}

	protected Object[] getObjectFromFields(T obj) throws Exception {
		Model model = new Model(obj);
		List<Object> list = new ArrayList<Object>();
		Field[] fields = AfReflecter.getField(mClazz);
		for (Field field : fields) {
			if (Interpreter.isColumn(field)) {
				list.add(model.getFieldObject(field));
			}
		}
		return list.toArray(new Object[0]);
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
				mDbWriteable.execSQL(sql.toString());
				return true;
			} catch (Throwable e) {
				String remark = "AfDao("+getClass().getName()+").update("+obj+","+where+")\r\n";
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
			StringBuilder sql = new StringBuilder();
			sql.append("select count(*) from ");
			sql.append(mTableName);
			Cursor cur = mDbReadable.rawQuery(sql.toString(), null);
			try {
				if (cur.moveToNext()) {
					return cur.getInt(0);
				}
			} finally {
				cur.close();
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
			StringBuilder sql = new StringBuilder();
			sql.append("select count(*) from ");
			sql.append(mTableName);
			sql.append(' ');
			sql.append(AfSQLHelper.Where(where));
			Cursor cur = mDbReadable.rawQuery(sql.toString(), null);
			try {
				if (cur.moveToNext()) {
					return cur.getInt(0);
				}
			} finally {
				cur.close();
			}
			return 0;
		}
	}

	/**
	 * 删除所有
	 */
	public final void delAll() {
		synchronized(mHelper){
			mDbWriteable.execSQL("delete from " + mTableName);
		}
	}

	/**
	 * 删除符合Where条件的数据
	 *
	 * @param where
	 */
	public final void delWhere(String where) {
		synchronized(mHelper){
			mDbWriteable.execSQL("delete from " + mTableName + " " + AfSQLHelper.Where(where));
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
	protected final List<Model> getModelsLimit(String column, int num, int offset) {
		synchronized(mHelper){
			StringBuilder sql = new StringBuilder();
			sql.append("select ");
			sql.append(column);
			sql.append(" from ");
			sql.append(mTableName);
			sql.append(" limit ");
			sql.append(num);
			sql.append(" offset ");
			sql.append(offset);
			return getModels(mDbReadable.rawQuery(sql.toString(), null));
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
	protected final List<Model> getModelsLimit(String column, String order, int num,
											   int offset) {
		synchronized(mHelper){
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
			return getModels(mDbReadable.rawQuery(sql.toString(), null));
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
	protected final List<Model> getModelsLimit(String column, String where, String order,
											   int num, int offset) {
		synchronized(mHelper){
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
			return getModels(mDbReadable.rawQuery(sql.toString(), null));
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
	protected final List<Model> getModelsWhere(String column, String where, String order) {
		synchronized(mHelper){
			StringBuilder sql = new StringBuilder();
			sql.append("select ");
			sql.append(column);
			sql.append(" from ");
			sql.append(mTableName);
			sql.append(' ');
			sql.append(AfSQLHelper.Where(where));
			sql.append(" order by ");
			sql.append(order);
			return getModels(mDbReadable.rawQuery(sql.toString(), null));
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
	protected final List<Model> getModelsWhere(String column, String where, int num,
											   int offset) {
		synchronized(mHelper){
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
			return getModels(mDbReadable.rawQuery(sql.toString(), null));
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
	protected final List<Model> getModelsWhere(String column, String where) {
		synchronized(mHelper){
			StringBuilder sql = new StringBuilder();
			sql.append("select ");
			sql.append(column);
			sql.append(" from ");
			sql.append(mTableName);
			sql.append(' ');
			sql.append(AfSQLHelper.Where(where));
			return getModels(mDbReadable.rawQuery(sql.toString(), null));
		}
	}

	/**
	 * 获取全部
	 *
	 * @param column
	 * @return
	 */
	protected final List<Model> getModelsAll(String column) {
		synchronized(mHelper){
			return getModels(mDbReadable.rawQuery("select " + column + " from " + mTableName,
					null));
		}
	}

	/**
	 * 获取全部
	 *
	 * @param column
	 * @return
	 */
	protected final List<Model> getModelsAll(String column, String order) {
		synchronized(mHelper){
			StringBuilder sql = new StringBuilder();
			sql.append("select ");
			sql.append(column);
			sql.append(" from ");
			sql.append(mTableName);
			sql.append(" order by ");
			sql.append(order);
			return getModels(mDbReadable.rawQuery(sql.toString(), null));
		}
	}

	protected Model getModel(Cursor cursor) {
		try {
			Model model = new Model();
			if (cursor.moveToNext()) {
				int columnCount = cursor.getColumnCount();
				for (int i = 0; i < columnCount; i++) {
					model.set(cursor.getColumnName(i), cursor.getString(i));
				}
			}
			return model;
		} finally{
			cursor.close();
		}
	}

	protected List<Model> getModels(Cursor cursor) {
		try {
			List<Model> models = new ArrayList<AfDao.Model>();
			while (cursor.moveToNext()) {
				Model model = new Model();
				int count = cursor.getColumnCount();
				for (int i = 0; i < count; i++) {
					model.set(cursor.getColumnName(i), cursor.getString(i));
				}
				models.add(model);
			}
			return models;
		} finally{
			cursor.close();
		}
	}

	@SuppressWarnings("serial")
	public static class Model extends HashMap<String, Object>{

		private Object model;

		public Model() {
		}

		public Model(Object obj) {
			this.model = obj;
		}

		public Object get(String column) {
			return super.get(column);
		}

		public String getString(String column) {
			return String.valueOf(get(column));
		}

		public Short getShort(String columnName) {
			return Short.valueOf(getString(columnName));
		}

		public int getInt(String column) {
			return Integer.valueOf(getString(column));
		}

		public boolean getBoolean(String column) {
			return Boolean.valueOf(getString(column));
		}

		public double getDouble(String column) {
			return Double.valueOf(getString(column));
		}

		public float getFloat(String column) {
			return Float.valueOf(getString(column));
		}

		public long getLong(String column) {
			return Long.valueOf(getString(column));
		}

		public UUID getUUID(String column) {
			try {
				return UUID.fromString(getString(column));
			} catch (Exception e) {
				return UUID.fromString("00000000-0000-0000-0000-000000000000");
			}
		}

		public Date getDate(String column) {
			return new Date(getLong(column));
		}

		public void set(String key, Object value) {
			put(key, value);
		}

		public Object getFieldObject(Field field) throws Exception {
			field.setAccessible(true);
			Class<?> type = field.getType();
			if (type.equals(Date.class)) {
				Date val = (Date) field.get(model);
				val = (val==null)?new Date():val;
				return val.getTime();
			} else if (type.equals(UUID.class)) {
				UUID val = (UUID) field.get(model);
				val = (val==null)?UUIDUtil.Empty:val;
				return val.toString();
			} else if (type.equals(Boolean.class) || type.equals(boolean.class)) {
				Boolean val = (Boolean) field.get(model);
				val = (val==null)?false:val;
				return val ? 1 : 0;
			} else {
				return field.get(model);
			}
		}
	}
}
