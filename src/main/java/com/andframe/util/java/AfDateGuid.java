package com.andframe.util.java;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

@SuppressWarnings("unused")
public class AfDateGuid {

	private static Random random = new Random();
	private static DecimalFormat decimalformat = new DecimalFormat("000");
	private static SimpleDateFormat simpleformat = new SimpleDateFormat("yyyyMMddHHmmss",Locale.US);
	
	public static String NewID(){
		Calendar car = Calendar.getInstance();
		StringBuilder sb = new StringBuilder();
		sb.append(simpleformat.format(car.getTime()));
		sb.append(decimalformat.format(car.get(Calendar.MILLISECOND)));
		sb.append(random.nextInt(10));
		sb.append(random.nextInt(10));
		sb.append(random.nextInt(10));
		return sb.toString();
	}

	public static String NewReverseID(){
		Calendar car = Calendar.getInstance();
		StringBuilder sb = new StringBuilder();
		sb.append(simpleformat.format(car.getTime()));
		sb.append(decimalformat.format(car.get(Calendar.MILLISECOND)));
		sb.append(random.nextInt(10));
		sb.append(random.nextInt(10));
		sb.append(random.nextInt(10));
		return sb.reverse().toString();
	}
	
}
