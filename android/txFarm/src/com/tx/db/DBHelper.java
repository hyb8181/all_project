package com.tx.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * 数据库管理类
 * @author Crane
 *
 */
public class DBHelper extends SQLiteOpenHelper {
	public static final String dbName = "txdata.db";
	public static final int dbVersion = 1;
	public DBHelper(Context context) {
		super(context, dbName, null, dbVersion);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String userSql = "create table if not exists user(id integer primary key autoincrement,userid integer,imsi char(20),phonenum char(20))";
		String appSql = "create table if not exists app(id integer primary key autoincrement,appid integer,enname char(20),cnname char(20),ver char(10),addr char(100),isfirst integer,type integer)";
		db.execSQL(userSql);
		db.execSQL(appSql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
		 // 仅演示用，所以先删除表然后再创建。
		String userSql = "drop table if exists user";
		String appSql = "drop table if exists app";
		db.execSQL(userSql);
        db.execSQL(appSql);
        this.onCreate(db);
	}

	@Override
	public synchronized void close() {
		super.close();
	}

	
}
