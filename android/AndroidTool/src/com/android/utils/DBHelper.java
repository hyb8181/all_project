package com.android.utils;

/**
 * 数据库帮助类
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	public static final String CALL_LOG = "call_log";
	
	public DBHelper(Context context) {
		super(context, "mydb", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "create table if not exists favorite(favorite_id integer primary key autoincrement," +
				" shop_id varchar(20),shop_name varchar(50),city varchar(20),area varchar(20)," +
				" first_sort varchar(20),second_sort varchar(20),website varchar(20),address varchar(100)," +
				" latitude varchar(50),longitude varchar(50));";
		db.execSQL(sql);
		sql = "create table if not exists tels(_id integer primary key autoincrement,shop_id varchar(20),tel varchar(20));";
		db.execSQL(sql);
		db.execSQL("create table blacknumber(_id integer primary key autoincrement, number varchar,name varchar)");
		sql="CREATE TABLE IF NOT EXISTS "+CALL_LOG+" (id INTEGER PRIMARY KEY AUTOINCREMENT,phone VARCHAR,type INTEGER,date VARCHAR,duration VARCHAR)";
		db.execSQL(sql);
		//创建反馈建议表
		db.execSQL("create table suggest(_id integer primary key autoincrement,suggest varchar,feedback varchar)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}
}
