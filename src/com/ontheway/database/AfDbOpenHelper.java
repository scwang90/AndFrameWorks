package com.ontheway.database;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ontheway.application.AfExceptionHandler;
import com.ontheway.db.annotation.Column;
import com.ontheway.db.annotation.Table;
import com.ontheway.entity.VersionEntity;
import com.ontheway.util.UUIDUtil;

public class AfDbOpenHelper extends SQLiteOpenHelper {

	// 系统SQLite版本
	public static final int VERSION = 3;
	// 自定义数据库关系版本
	public static int DATABASE_VERSION = 0;
	// 数据库文件名
	public static String DBNAME = "data.db";

	private static AfDbOpenHelper mHelper = null;
	private static HashMap<String, AfDbOpenHelper> mHelperMap = new HashMap<String, AfDbOpenHelper>();

	public static List<Class<?>> Tables = new ArrayList<Class<?>>() {
		private static final long serialVersionUID = 1L;
		{
			add(VersionEntity.class);
		}
	};

	public Context mContext = null;

	private AfDbOpenHelper(Context context) {
		super(context, DBNAME, null, VERSION);
		// TODO Auto-generated constructor stub
		this.mContext = context;
	}

	public AfDbOpenHelper(Context context, String dbname) {
		super(context, dbname, null, VERSION);
		// TODO Auto-generated constructor stub
		this.mContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		for (Class<?> tclass : Tables) {
			createTable(tclass, db);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	private void createTable(Class<?> clazz, SQLiteDatabase db) {
		String tname = "t"+clazz.getSimpleName();
		if (clazz.isAnnotationPresent(Table.class)) {
			Table table = clazz.getAnnotation(Table.class);
			if (table.name() != null && table.name().length() > 0) {
				tname = table.name();
			}
		}
		boolean isfrist = true;
		StringBuilder sql = new StringBuilder("create table if not exists ");
		sql.append(tname);
		sql.append(" (");
		if (getFields(clazz).length < 1) {
			return;
		}
//		for (Field field : clazz.getFields()) {
		for (Field field : getFields(clazz)) {
			if (isfrist == false) {
				sql.append(',');
			}
			isfrist = false;
			Class<?> type = field.getType();
			String name = field.getName();
			sql.append(name);
			sql.append(' ');
			if (type.equals(Date.class) || type.equals(int.class)
					|| type.equals(Long.class) || type.equals(long.class)
					|| type.equals(Short.class) || type.equals(short.class)
					|| type.equals(Boolean.class) || type.equals(boolean.class)
					|| type.equals(Integer.class)) {
				sql.append("integer");
			} else if (type.equals(float.class) || type.equals(Float.class)) {
				sql.append("float");
			} else if (type.equals(double.class) || type.equals(Double.class)) {
				sql.append("double");
			} else if (type.equals(UUID.class)) {
				sql.append("text");
			} else {
				sql.append("text");
			}
			if (name.equals("ID") || field.isAnnotationPresent(Column.class)
					&& field.getAnnotation(Column.class).id()) {
				sql.append(" NOT NULL PRIMARY KEY");
			}
		}
		sql.append(')');
		try {
			db.execSQL(sql.toString());
		} catch (Throwable e) {
			// TODO: handle exception
			e.printStackTrace();// handled
			AfExceptionHandler
					.handler(e, "DatabaseOpenHelper，createTable 出现异常");
		}
	}

	public Object[] getObjectFromFields(Class<?> tclass, Object obj) {
		// TODO Auto-generated method stub
		Field[] tFields = getFields(tclass);
		Object[] tObjects = new Object[tFields.length];
		try {
			for (int i = 0; i < tFields.length; i++) {
				Field tField = tFields[i];
				Class<?> tType = tField.getType();
				if (tType.equals(Date.class)) {
					Date tDate = (Date) tField.get(obj);
					if (tDate == null) {
						tDate = new Date();
					}
					tObjects[i] = tDate.getTime();
				} else if (tType.equals(UUID.class)) {
					UUID tUUID = (UUID) tField.get(obj);
					if (tUUID == null) {
						tUUID = UUIDUtil.Empty;
					}
					tObjects[i] = tUUID.toString();
				} else if (tType.equals(Boolean.class)
						|| tType.equals(boolean.class)) {
					Boolean tBoolean = (Boolean) tField.get(obj);
					if (tBoolean == null) {
						tBoolean = false;
					}
					tObjects[i] = tBoolean ? 1 : 0;
				} else {
					tObjects[i] = tField.get(obj);
				}
			}
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();// handled
			AfExceptionHandler.handler(e,
					"DatabaseOpenHelper，getObjectFromFields 出现异常");
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();// handled
			AfExceptionHandler.handler(e,
					"DatabaseOpenHelper，getObjectFromFields 出现异常");
		}
		return tObjects;
	}

	public static boolean getBoolean(Cursor cur, String column) {
		// TODO Auto-generated method stub
		int index = cur.getColumnIndex(column);
		if (index < 0) {
			return false;
		}
		return cur.getLong(index) == 1;
	}

	public static short getShort(Cursor cur, String column) {
		// TODO Auto-generated method stub
		int index = cur.getColumnIndex(column);
		if (index < 0) {
			return 0;
		}
		return cur.getShort(index);
	}
	
	public static int getInt(Cursor cur, String column) {
		// TODO Auto-generated method stub
		int index = cur.getColumnIndex(column);
		if (index < 0) {
			return 0;
		}
		return cur.getInt(index);
	}

	public static long getLong(Cursor cur, String column) {
		// TODO Auto-generated method stub
		int index = cur.getColumnIndex(column);
		if (index < 0) {
			return 0;
		}
		return cur.getLong(index);
	}

	public static float getFloat(Cursor cur, String column) {
		// TODO Auto-generated method stub
		int index = cur.getColumnIndex(column);
		if (index < 0) {
			return 0;
		}
		return cur.getFloat(index);
	}

	public static double getDouble(Cursor cur, String column) {
		// TODO Auto-generated method stub
		int index = cur.getColumnIndex(column);
		if (index < 0) {
			return 0;
		}
		return cur.getDouble(index);
	}

	public static String getString(Cursor cur, String column) {
		// TODO Auto-generated method stub
		int index = cur.getColumnIndex(column);
		if (index < 0) {
			return "";
		}
		return cur.getString(index);
	}

	public static UUID getUUID(Cursor cur, String column) {
		// TODO Auto-generated method stub
		int index = cur.getColumnIndex(column);
		if (index < 0) {
			return UUID.fromString("00000000-0000-0000-0000-000000000000");
		}
		return UUID.fromString(cur.getString(index));
	}

	public static Date getDate(Cursor cur, String column) {
		// TODO Auto-generated method stub
		int index = cur.getColumnIndex(column);
		if (index < 0) {
			return new Date(0);
		}
		return new Date(cur.getLong(index));
	}

	@Override
	public synchronized void close() {
		// TODO Auto-generated method stub
		if (mHelper == this) {
			mHelper = null;
		}
		super.close();
	}

	public Field[] getFields(Class<?> table) {
		// TODO Auto-generated method stub
		Field[] fields = table.getFields();
		List<Field> list = new LinkedList<Field>();
		for (Field field : fields) {
			int modify = field.getModifiers();
			if(!Modifier.isFinal(modify) && !Modifier.isStatic(modify)){
				list.add(field);
			}
		}
		return list.toArray(new Field[0]);
	}

	public static AfDbOpenHelper getInstance(Context context) {
		// TODO Auto-generated method stub
		if (mHelper == null) {
			mHelper = new AfDbOpenHelper(context);
		}
		return mHelper;
	}

	public static AfDbOpenHelper getInstance(Context context,Class<?> clazz) {
		// TODO Auto-generated method stub
		if (mHelper == null) {
			mHelper = new AfDbOpenHelper(context);
		}
		mHelper.createTable(clazz, mHelper.getWritableDatabase());
		return mHelper;
	}
	
	public static AfDbOpenHelper getInstance(Context context,Class<?> clazz,String dbname) {
		// TODO Auto-generated method stub
		AfDbOpenHelper helper = mHelperMap.get(dbname);
		if (helper == null) {
			helper = new AfDbOpenHelper(context,dbname);
			mHelperMap.put(dbname, helper);
		}
		helper.createTable(clazz, helper.getWritableDatabase());
		return helper;
	}
}
