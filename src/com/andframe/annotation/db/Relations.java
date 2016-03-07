package com.andframe.annotation.db;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The Interface Relations.
 * 表示关联表
 */
@Target( { ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Relations {
	
	/**
	 * 关联名,对象内唯一即可.
	 *
	 * @return the string
	 */
	String name();
	
	/**
	 * 外键.
	 *
	 * @return the string
	 */
	String foreignKey();
	
	/**
	 * 关联类型.
	 *
	 * @return the string  one2one  one2many many2many
	 */
	String type();
	
	/**
	 * 关联类型.
	 *
	 * @return the string  query insert query_insert
	 */
	String action() default "query_insert";
}
