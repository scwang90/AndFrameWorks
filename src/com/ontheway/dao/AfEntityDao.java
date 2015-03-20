package com.ontheway.dao;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import android.content.Context;
import android.database.Cursor;

import com.ontheway.application.AfExceptionHandler;
import com.ontheway.database.AfDbOpenHelper;

public class AfEntityDao<T> extends AfDao<T>{

	public AfEntityDao(Context context, Class<T> table) {
		super(context, table);
		// TODO Auto-generated constructor stub
	}

	public AfEntityDao(Context context, Class<T> table, String dbname) {
		super(context, table, dbname);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 获取全部
	 * @return
	 */
	public List<T> getAll(){
		return getListEntity(super.getCursorAll("*"));
	}

	/**
	 * 获取全部
	 * 
	 * @param order
	 * @return
	 */
	public List<T> getAll(String order) {
		return getListEntity(super.getCursorAll("*",order));
	}
	
	/**
	 * 分页查询
	 * @param num
	 * @param offset
	 * @return
	 */
	public List<T> getLimit(int num, int offset) {
		return getListEntity(super.getCursorLimit("*", num, offset));
	}

	/**
	 * 分页查询 带排序
	 * @param order
	 * @param num
	 * @param offset
	 * @return
	 */
	public List<T> getLimit(String order, int num,int offset) {
		return getListEntity(super.getCursorLimit("*",order, num, offset));
	}

	/**
	 * 分页查询 带排序 条件
	 * 
	 * @param where
	 * @param order
	 * @param num
	 * @param offset
	 * @return
	 */
	public List<T> getLimit(String where, String order,
			int num, int offset) {
		return getListEntity(super.getCursorLimit("*",where,order, num, offset));
	}

	/**
	 * 分页查询 带排序 条件
	 * 
	 * @param where
	 * @param order
	 * @return
	 */
	public List<T> getWhere(String where, String order) {
		return getListEntity(super.getCursorWhere("*",where,order));
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
	public List<T> getWhere(String where, int num,int offset) {
		return getListEntity(super.getCursorWhere("*",where,num,offset));
	}

	/**
	 * 条件查询
	 * 
	 * @param where
	 * @return
	 */
	public final List<T> getWhere(String where) {
		return getListEntity(super.getCursorWhere("*",where));
	}

	
	protected List<T> getListEntity(Cursor cur) {
		List<T> ltEntity = new ArrayList<T>();
		try {
			Field[] fields = mHelper.getFields(mClazz);
			while (cur.moveToNext()) {
				ltEntity.add(getEntity(cur,fields));
			}
		} catch (Throwable ex) {
			// TODO Auto-generated catch block
			AfExceptionHandler.handler(ex, "AfEntityDao.getListEntity.getEntity-Exception");
		}
		return ltEntity;
	}
	
	protected T getEntity(Cursor cur){
		try {
			if(cur.moveToNext()){
				return getEntity(cur, mHelper.getFields(mClazz));
			}
		} catch (Throwable ex) {
			// TODO Auto-generated catch block
			AfExceptionHandler.handler(ex, "AfEntityDao.getEntity.getEntity-Exception");
		}
		return null;
	}

	protected T getEntity(Cursor cur,Field[] fields) throws Exception {
		// TODO Auto-generated method stub
		T entity = mClazz.newInstance();
		for (Field field : fields) {
			Class<?> type = field.getType();
			if (type.equals(Short.class) || type.equals(short.class)) {
				field.set(entity, AfDbOpenHelper.getShort(cur, field.getName()));
			} else if (type.equals(Integer.class) || type.equals(int.class)) {
				field.set(entity, AfDbOpenHelper.getInt(cur, field.getName()));
			} else if (type.equals(Long.class) || type.equals(long.class)) {
				field.set(entity, AfDbOpenHelper.getLong(cur, field.getName()));
			} else if (type.equals(Float.class) || type.equals(float.class)) {
				field.set(entity, AfDbOpenHelper.getFloat(cur, field.getName()));
			} else if (type.equals(Double.class) || type.equals(double.class)) {
				field.set(entity, AfDbOpenHelper.getDouble(cur, field.getName()));
			} else if (type.equals(Boolean.class) || type.equals(boolean.class)) {
				field.set(entity, AfDbOpenHelper.getBoolean(cur, field.getName()));
			} else if (type.equals(Date.class)) {
				field.set(entity, AfDbOpenHelper.getDate(cur, field.getName()));
			} else if (type.equals(UUID.class)) {
				field.set(entity, AfDbOpenHelper.getUUID(cur, field.getName()));
			} else if (type.equals(String.class)) {
				field.set(entity, AfDbOpenHelper.getString(cur, field.getName()));
			}
		}
		return entity;
	}
}
