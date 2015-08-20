package com.andframe.util;

import java.io.File;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Build.VERSION;

import com.andframe.dao.AfVersionDao;
import com.andframe.database.AfDbOpenHelper;
import com.andframe.entity.VersionEntity;

public class DatabaseUtil
{
    private File file;
    private Context context;
    public static String dbName = AfDbOpenHelper.DBNAME;// 数据库的名字
    public static String DATABASE_PATH;// 数据库在手机里的路径

    public DatabaseUtil(Context context) {
        this.context = context;
        this.file = context.getDatabasePath(dbName);
        DatabaseUtil.DATABASE_PATH = file.getPath();
        //String packageName = context.getPackageName();
        //DATABASE_PATH = "/data/data/" + packageName + "/databases/";
    }

    /**
     * 删除数据库
     * @param file
     */
    @SuppressLint("NewApi")
	public void deleteDatabase(File file) 
    {
        if(VERSION.SDK_INT >= 16){
            SQLiteDatabase.deleteDatabase(file);
        }else{
            file.delete();
        }
    }
    /**
     * 检查数据库版本
     *  如果版本过旧或者不存在 删除再添加
     */
    public void checkDataBaseVersion() {
    	checkDataBaseVersion(0);
    }
    public void checkDataBaseVersion(int index) {
        try
        {
            AfVersionDao dao = new AfVersionDao(context);
            VersionEntity version = dao.getVersion();
            if(version.Version != AfDbOpenHelper.DATABASE_VERSION)
            {
                dao.close();
//                XmlCacheUtil.clear();
                deleteDatabase(file);
                if (index == 0) {
                    checkDataBaseVersion(1);
				}
                //String msg = "成功更新数据库｛"+version.Version+"->"+DatabaseOpenHelper.DATABASE_VERSION+"｝";
                //Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
            }
        }
        catch (Throwable e)//版本过旧无法读取
        {
//        	AfExceptionHandler.handleAttach(e, "数据库版本过旧无法读取");
        	if (file != null && file.exists()) {
        		file.delete();
                if (index == 0) {
                    checkDataBaseVersion(1);
				}
			}
            //deleteDatabase(file);
            //checkDataBaseVersion();
            return;
        }
        

    }
    /**
     * 判断数据库是否存在
     * 
     * @return false or true
     */
    public boolean checkDataBase() {
        SQLiteDatabase db = null;
        try {
            db = SQLiteDatabase.openDatabase(DATABASE_PATH, null,
                    SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {

        }
        if (db != null) {
            db.close();
        }
        return db != null;
    }

    /**
     * 复制数据库到手机指定文件夹下
     * 
     * @throws IOException
     */
    public void copyDataBase() throws IOException {
//        File dir = new File(DATABASE_PATH);
//        if (!dir.getParentFile().exists()){
//            // 判断文件夹是否存在，不存在就新建一个
//            dir.getParentFile().mkdir();
//        }
//        FileOutputStream os = new FileOutputStream(DATABASE_PATH);// 得到数据库文件的写入流
//        InputStream is = context.getResources().openRawResource(R.raw.data);// 得到数据库文件的数据流
//        byte[] buffer = new byte[8192];
//        int count = 0;
//        while ((count = is.read(buffer)) > 0) {
//            os.write(buffer, 0, count);
//            os.flush();
//        }
//        is.close();
//        os.close();
    }
}
