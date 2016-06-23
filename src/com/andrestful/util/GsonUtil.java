// Copyright (c) 2003-2013, LogMeIn, Inc. All rights reserved.
// This is part of Xively4J library, it is under the BSD 3-Clause license.
package com.andrestful.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Helper class for parsing to and from json for API calls
 * @author s0pau
 */
public class GsonUtil {
	
	private static Gson mGson = getGson();

	/**
	 * 日期显示格式 "yyyy-MM-dd HH:mm:ss"
	 */
	public static final String FORMAT_DATE = "yyyy-MM-dd HH:mm:ss";
	public static final DateFormat format = new SimpleDateFormat(FORMAT_DATE,
			Locale.US);
	/**
	 * Get the list of model objects and create json as expected by the API.
	 * @return json string suitable for Xively API consumption
	 */
	public static  String toJson(Object model) {
		String json = null;

		// ADD ROOT
		// Setting SerializationConfig.Feature.WRAP_ROOT_VALUE at mapper
		// did not read annotated label properly, use withRootName
		json = mGson.toJson(model);
		// ApiKey needs to be wrapped in a root node without the array
		// container, hack the standards!

		return json;
	}

	public static <T> List<T> toObjects(
			String body, Class<T> clazz) throws Exception {
		if (body == null || body.toLowerCase(Locale.ENGLISH).trim().equals("null")) {
			return null;
		}
		List<T> list = new ArrayList<>();
		JSONArray array = new JSONArray(body);
		for (int i = 0; i < array.length(); i++) {
			JSONObject element = array.getJSONObject(i);
			list.add(mGson.fromJson(element.toString(), clazz));
		}
		return list;
	}

	public static <T> T toObject(String body,
			Class<T> clazz) {
		return mGson.fromJson(body, clazz);
	}

	public static Gson getGson() {
		if (mGson == null) {
			mGson = new GsonBuilder()
			.registerTypeAdapter(Date.class,
					new TimestampTypeAdapter())
			.setDateFormat(FORMAT_DATE).create();
		}
		return mGson;
	}

	public static class TimestampTypeAdapter implements
			JsonSerializer<Date>, JsonDeserializer<Date> {

		Pattern pattern = Pattern.compile("new Date\\((\\d+)\\+\\d+\\)",Pattern.CASE_INSENSITIVE);

		public JsonElement serialize(Date src, Type arg1,
				JsonSerializationContext arg2) {
			String dateFormatAsString = format.format(new Date(src.getTime()));
			return new JsonPrimitive(dateFormatAsString);
		}

		public Date deserialize(JsonElement json, Type typeOfT,
				JsonDeserializationContext context) throws JsonParseException {
			if (!(json instanceof JsonPrimitive)) {
				throw new JsonParseException(
						"The date should be a string value");
			}
			try {
				String sjson = json.getAsString();
				Matcher matcher = pattern.matcher(sjson);
				if (matcher.find()) {
					Long time = Long.valueOf(matcher.group(1));
					return new Timestamp(time);
				} else if (sjson.matches("\\d+")) {
					return new Timestamp(Long.valueOf(sjson));
				}

				Date date = format.parse(json.getAsString());
				return new Timestamp(date.getTime());
			} catch (ParseException e) {
				try {
					return new Timestamp(Long.parseLong(json.getAsString()));
				} catch (Throwable ex) {
					throw new JsonParseException(ex);
				}
			}
		}

	}
}
