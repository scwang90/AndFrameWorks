package com.andframe.database;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.andframe.annotation.db.interpreter.Interpreter;
import com.andframe.application.AfExceptionHandler;
import com.andframe.entity.VersionEntity;
import com.andframe.util.java.AfReflecter;

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
	private SQLiteDatabase mDatabase;

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

	public AfDbOpenHelper(Context context, String path, String dbname) {
		super(context, dbname, null, VERSION);
		// TODO Auto-generated constructor stub
		this.mContext = context;
		this.mDatabase = createOnPath(path, dbname);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		for (Class<?> tclass : Tables) {
			createTable(tclass, db);
		}
	}
	
	@Override
	public SQLiteDatabase getWritableDatabase() {
		// TODO Auto-generated method stub
		if (mDatabase != null) {
			return mDatabase;
		}
		return super.getWritableDatabase();
	}
	
	@Override
	public SQLiteDatabase getReadableDatabase() {
		// TODO Auto-generated method stub
		if (mDatabase != null) {
			return mDatabase;
		}
		return super.getReadableDatabase();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	private void createTable(Class<?> clazz, SQLiteDatabase db) {
		String tname = Interpreter.getTableName(clazz);
		boolean isfrist = true;
		StringBuilder sql = new StringBuilder("create table if not exists ");
		sql.append(tname);
		sql.append(" (");
		for (Field field : AfReflecter.getField(clazz)) {
			if(Interpreter.isColumn(field)){
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
				if (name.equals("ID") || Interpreter.isPrimaryKey(field)) {
					sql.append(" NOT NULL PRIMARY KEY");
				}
			}
		}
		sql.append(')');
		try {
			db.execSQL(sql.toString());
		} catch (Throwable e) {
			// TODO: handle exception
			AfExceptionHandler.handler(e, "AfDbOpenHelper.createTable");
		}
	}

	@Override
	public synchronized void close() {
		// TODO Auto-generated method stub
		if (mHelper == this) {
			mHelper = null;
		}
		super.close();
	}
	
	/**
     * 在SD卡的指定目录上创建文件 否则在手机创建
     * @param path
     * @param dbname
     * @return
     */
    private SQLiteDatabase createOnPath(String path,String dbname) {
        File dbfile = new File(path, dbname);
        if (!dbfile.exists()) {
            try {
                if (dbfile.createNewFile()) {
                    return SQLiteDatabase.openOrCreateDatabase(dbfile, null);
                }
            } catch (IOException ioex) {
                return null;
            }
        } else {
            return SQLiteDatabase.openOrCreateDatabase(dbfile, null);
        }
        return null;
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

	public static AfDbOpenHelper getInstance(Context context,Class<?> clazz,String path,String dbname) {
		// TODO Auto-generated method stub
		AfDbOpenHelper helper = mHelperMap.get(path+dbname);
		if (helper == null) {
			helper = new AfDbOpenHelper(context,path,dbname);
			mHelperMap.put(path+dbname, helper);
		}
		helper.createTable(clazz, helper.getWritableDatabase());
		return helper;
	}
	
}
