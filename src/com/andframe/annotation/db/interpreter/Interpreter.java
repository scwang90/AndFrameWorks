package com.andframe.annotation.db.interpreter;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.andframe.annotation.db.Column;
import com.andframe.annotation.db.DbIgnore;
import com.andframe.annotation.db.Id;
import com.andframe.annotation.db.Table;
import com.andframe.util.java.AfStringUtil;
/**
 * db.annotation 解释器
 * @author 树朾
 */
public class Interpreter {
	
	/**
	 * 获取field是否是数据库列
	 * @param field
	 * @return
	 */
	public static boolean isColumn(Field field) {
		// TODO Auto-generated method stub
		int modify = field.getModifiers();
		return !Modifier.isFinal(modify) && !Modifier.isStatic(modify) 
				&& !Modifier.isTransient(modify) 
				&& !field.isAnnotationPresent(DbIgnore.class);
	}
	/**
	 * 获取field的列名称
	 * @param field
	 * @return
	 */
	public static String getColumnName(Field field) {
		// TODO Auto-generated method stub
		if (field.isAnnotationPresent(Column.class)) {
			Column column = field.getAnnotation(Column.class);
			if (AfStringUtil.isNotEmpty(column.value())) {
				return column.value();
			}
		}
		if (field.isAnnotationPresent(Id.class)) {
			Id id = field.getAnnotation(Id.class);
			if (AfStringUtil.isNotEmpty(id.value())) {
				return id.value();
			}
		}
		return field.getName();
	}
	/**
	 * 获取clazz数据表名称
	 * @param clazz
	 * @return
	 */
	public static String getTableName(Class<?> clazz) {
		// TODO Auto-generated method stub
		if (clazz.isAnnotationPresent(Table.class)) {
			Table table = clazz.getAnnotation(Table.class);
			if (table.value() != null && table.value().length() > 0) {
				return table.value();
			}
		}
		return clazz.getSimpleName();
	}
	/**
	 * 获取clazz的主键ID名称
	 * @param clazz
	 * @return
	 */
	public static String getIdName(Class<?> clazz) {
		// TODO Auto-generated method stub
		Field field = getIdField(clazz);
		if (field != null) {
			Id id = field.getAnnotation(Id.class);
			if (id == null || id.value().trim().length() == 0) {
				return field.getName();
			}
			return id.value();
		}
		return "ID";
	}

	/**
	 * 获取clazz的主键ID名称
	 * @param clazz
	 * @return
	 */
	public static Field getIdField(Class<?> clazz) {
		// TODO Auto-generated method stub
		List<Field> fields = new ArrayList<Field>();
		while (!clazz.equals(Object.class)) {
			for (Field field : clazz.getDeclaredFields()) {
				fields.add(field);
			}
			clazz = clazz.getSuperclass();
		}
		List<Field> cloumns = new ArrayList<Field>();
		for (Field field : fields) {
			if (isColumn(field)) {
				if (isPrimaryKey(field)) {
					return field;
				}
				cloumns.add(field);
			}
		}
		for (Field field : cloumns) {
			String name = field.getName().toLowerCase(Locale.ENGLISH);
			if (name.endsWith("id")) {
				return field;
			}
		}
		return null;
	}

	/**
	 * 判断 Field 是否为 ID字段
	 * @param field
	 * @return
	 */
	public static boolean isPrimaryKey(Field field) {
		// TODO Auto-generated method stub
		return field.isAnnotationPresent(Id.class)
				|| (field.isAnnotationPresent(Column.class)
				&& field.getAnnotation(Column.class).id());
	}

}
