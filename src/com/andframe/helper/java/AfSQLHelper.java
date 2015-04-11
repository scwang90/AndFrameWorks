package com.andframe.helper.java;

import java.util.Locale;

public class AfSQLHelper {
	
	public String where(String where) {
		return Where(where);
	}

	public String andwhere(String where) {
		// TODO Auto-generated method stub
		return andWhere(where);
	}

	public String orwhere(String where) {
		// TODO Auto-generated method stub
		return orWhere(where);
	}

	public static String Where(String where) {
		if(where != null){
			String lower = where.toLowerCase(Locale.ENGLISH);
			String trim = lower.trim();
			if(!trim.equals("")){
				if(trim.startsWith("where")){
					return where;
				}
				return " where " + where;
			}
			return "";
		}else{
			return "";
		}
	}

	public static String andWhere(String where) {
		// TODO Auto-generated method stub
		if(where != null){
			String lower = where.toLowerCase(Locale.ENGLISH);
			String trim = lower.trim();
			if(!trim.equals("")){
				if(trim.startsWith("where")){
					where = where.substring(lower.indexOf('w')+5);
				}
				return " and " + where;
			}
			return "";
		}else{
			return "";
		}
	}

	public static String orWhere(String where) {
		// TODO Auto-generated method stub
		if(where != null ){
			String lower = where.toLowerCase(Locale.ENGLISH);
			String trim = lower.trim();
			if(!trim.equals("")){
				if(trim.startsWith("where")){
					where = where.substring(lower.indexOf('w')+5);
				}
				return " or " + where;
			}
			return "";
		}else{
			return "";
		}
	}
}
