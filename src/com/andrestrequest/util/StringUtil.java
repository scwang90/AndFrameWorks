// Copyright (c) 2003-2013, LogMeIn, Inc. All rights reserved.
package com.andrestrequest.util;

import java.util.Date;
import java.util.List;

import com.andrestrequest.AndRestException;

/**
 * Utility class for String
 * 
 * @author s0pau
 * 
 */
public class StringUtil {
	/**
	 * @param toCheck
	 * @return true if the given string is null or empty
	 */
	public static boolean isNullOrEmpty(String toCheck) {
		if (toCheck == null || toCheck.length() == 0) {
			return true;
		}
		return false;
	}

	/**
	 * @param list
	 * @return comma delimited string of elements in list, {@see toString} which
	 *         is used to convert each element
	 */
	public static String toCommaDelimitedString(List<?> list) {
		StringBuilder retval = null;
		boolean isFirstToken = true;
		for (Object o : list) {
			if (o == null) {
				continue;
			}

			String token = null;
			token = toString(o);

			if (token.length() > 0) {
				if (isFirstToken) {
					isFirstToken = false;
					retval = new StringBuilder();

				} else {
					retval.append(",");
				}
				retval.append(token);
			}
		}

		return retval == null ? null : retval.toString();
	}

	/**
	 * 
	 * @param o
	 * @return string of the object
	 * @exception UnsupportedOperationException
	 *                if the object is not String/Number/Boolean of a list of
	 *                String/Number/Boolean
	 */
	public static String toString(Object o) {
		if (o instanceof String) {
			return (String) o;
		} else if (o instanceof Date) {
			return String.valueOf(((Date) o).getTime());
		} else if (o instanceof Boolean) {
			return String.valueOf(o);
		} else if (Number.class.isAssignableFrom(o.getClass())) {
			return String.valueOf((Number) o);
		} else if (List.class.isAssignableFrom(o.getClass())) {
			return toCommaDelimitedString((List<?>) o);
		} else {
			throw new AndRestException(
					String.format(
							"Cannot create comma delimited string for %s",
							o.getClass()));
		}
	}
}
