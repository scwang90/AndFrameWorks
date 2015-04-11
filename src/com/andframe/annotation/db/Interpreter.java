package com.andframe.annotation.db;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import com.andframe.util.java.AfStringUtil;

public class Interpreter {
	
	public static boolean isColumn(Field field) {
		// TODO Auto-generated method stub
		int modify = field.getModifiers();
		return !Modifier.isFinal(modify) && !Modifier.isStatic(modify) 
				&& !Modifier.isTransient(modify) 
				&& !field.isAnnotationPresent(DbIgnore.class);
	}

	public static String getColumnName(Field field) {
		// TODO Auto-generated method stub
		if (field.isAnnotationPresent(Column.class)) {
			Column column = field.getAnnotation(Column.class);
			if (AfStringUtil.isNotEmpty(column.name())) {
				return column.name();
			}
		}
		if (field.isAnnotationPresent(Id.class)) {
			Id id = field.getAnnotation(Id.class);
			if (AfStringUtil.isNotEmpty(id.column())) {
				return id.column();
			}
		}
		return field.getName();
	}

	public static String getTableName(Class<?> clazz) {
		// TODO Auto-generated method stub
		if (clazz.isAnnotationPresent(Table.class)) {
			Table table = clazz.getAnnotation(Table.class);
			if (table.name() != null && table.name().length() > 0) {
				return table.name();
			}
		}
		return "t"+clazz.getSimpleName();
	}

	public static boolean isPrimaryKey(Field field) {
		// TODO Auto-generated method stub
		return field.isAnnotationPresent(Id.class)
				|| (field.isAnnotationPresent(Column.class)
				&& field.getAnnotation(Column.class).id());
	}

}
