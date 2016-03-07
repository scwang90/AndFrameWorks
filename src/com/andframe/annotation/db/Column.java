/*
 * Copyright (C) 2013 www.418log.org
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.andframe.annotation.db;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The Interface Column.
 * 表示列
 */
@Target( { ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
	/**
	 * 是否是主键ID
	 * @return the boolean
	 */
	boolean id() default false;
	/**
	 * 列名.
	 *
	 * @return the string
	 */
	String value() default "";

	/**
	 * 列类型.
	 *
	 * @return the string
	 */
	String type() default "";

	/**
	 * 长度.
	 *
	 * @return the int
	 */
	int length() default 0;
	
}
