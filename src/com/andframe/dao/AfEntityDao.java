package com.andframe.dao;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import android.content.Context;

import com.andframe.annotation.db.interpreter.Interpreter;
import com.andframe.application.AfExceptionHandler;
import com.andframe.util.java.AfReflecter;

public class AfEntityDao<T> extends AfDao<T>{

	public AfEntityDao(Context context) {
		super(context);
	}

	public AfEntityDao(Context context, String dbname) {
		super(context, dbname);
	}

	public AfEntityDao(Context context,String path, String dbname) {
		super(context, path, dbname);
	}


	public AfEntityDao(Context context,Class<T> clazz) {
		super(context,clazz);
	}

	public AfEntityDao(Context context,Class<T> clazz, String dbname) {
		super(context,clazz, dbname);
	}

	public AfEntityDao(Context context,Class<T> clazz,String path, String dbname) {
		super(context,clazz, path, dbname);
	}

	/**
	 * 获取全部
	 * @return
	 */
	public List<T> getAll(){
		return getEntitys(super.getModelsAll("*"));
	}

	/**
	 * 获取全部
	 *
	 * @param order
	 * @return
	 */
	public List<T> getAll(String order) {
		return getEntitys(super.getModelsAll("*",order));
	}

	/**
	 * 分页查询
	 * @param num
	 * @param offset
	 * @return
	 */
	public List<T> getLimit(int num, int offset) {
		return getEntitys(super.getModelsLimit("*", num, offset));
	}

	/**
	 * 分页查询 带排序
	 * @param order
	 * @param num
	 * @param offset
	 * @return
	 */
	public List<T> getLimit(String order, int num,int offset) {
		return getEntitys(super.getModelsLimit("*",order, num, offset));
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
		return getEntitys(super.getModelsLimit("*",where,order, num, offset));
	}

	/**
	 * 分页查询 带排序 条件
	 *
	 * @param where
	 * @param order
	 * @return
	 */
	public List<T> getWhere(String where, String order) {
		return getEntitys(super.getModelsWhere("*",where,order));
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
		return getEntitys(super.getModelsWhere("*",where,num,offset));
	}

	/**
	 * 条件查询
	 *
	 * @param where
	 * @return
	 */
	public final List<T> getWhere(String where) {
		return getEntitys(super.getModelsWhere("*",where));
	}


	protected List<T> getEntitys(List<Model> models) {
		List<T> ltEntity = new ArrayList<T>();
		try {
			for (Model model : models) {
				ltEntity.add(getEntity(model));
			}
		} catch (Throwable ex) {
			AfExceptionHandler.handler(ex, "AfEntityDao.getEntitys.getEntity-Exception");
		}
		return ltEntity;
	}

	protected T getEntity(List<Model> models) throws Exception {
		for (Model model : models) {
			return getEntity(model);
		}
		return null;
	}

	protected T getEntity(Model model) throws Exception {
		T entity = mClazz.newInstance();
		for (Field field : AfReflecter.getField(mClazz)) {
			if (Interpreter.isColumn(field)) {
				String column = Interpreter.getColumnName(field);
				Class<?> type = field.getType();
				field.setAccessible(true);
				if (type.equals(Short.class) || type.equals(short.class)) {
					field.set(entity, model.getShort(column));
				} else if (type.equals(Integer.class) || type.equals(int.class)) {
					field.set(entity, model.getInt(column));
				} else if (type.equals(Long.class) || type.equals(long.class)) {
					field.set(entity, model.getLong(column));
				} else if (type.equals(Float.class) || type.equals(float.class)) {
					field.set(entity, model.getFloat(column));
				} else if (type.equals(Double.class) || type.equals(double.class)) {
					field.set(entity, model.getDouble(column));
				} else if (type.equals(Boolean.class) || type.equals(boolean.class)) {
					field.set(entity, model.getBoolean(column));
				} else if (type.equals(Date.class)) {
					field.set(entity, model.getDate(column));
				} else if (type.equals(UUID.class)) {
					field.set(entity, model.getUUID(column));
				} else if (type.equals(String.class)) {
					field.set(entity, model.getString(column));
				}
			}
		}
		return entity;
	}
}
