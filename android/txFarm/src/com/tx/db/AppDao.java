package com.tx.db;

import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.tx.bean.App;
/**
 * 应用信息存储类
 * @author Crane
 *
 */
public class AppDao {
	private static final String TAG = "TxAppDao";
	private DBHelper helper;
	public AppDao(Context context){
		helper = new DBHelper(context);
	}
	
	/*private int appId;
	private String appCnName;
	private String appEnName;
	private String ver;
	// 是否是第一次安装，是：1；否：其他
	private int isFirst;*/
	
	public void saveApp(App app){
		try {
			String sql = "insert into app(appid,enname,cnname,ver,addr,isfirst,type) values(?,?,?,?,?,?,?)";
			helper.getWritableDatabase().execSQL(sql, new Object[]{app.getAppId(),app.getEnName(),app.getCnName(),app.getVer(),app.getAddr(),app.getIsFirst(),app.getType()});
		} catch (Exception e) {
			Log.i(TAG, e.getMessage());
		}finally{
			helper.close();
		}
	}
	
	public void saveApps(List<App> apps){
		try {
			for(App app:apps){
				String sql = "insert into app(appid,enname,cnname,ver,addr,isfirst,type) values(?,?,?,?,?,?,?)";
				helper.getWritableDatabase().execSQL(sql, new Object[]{app.getAppId(),app.getEnName(),app.getCnName(),app.getVer(),app.getAddr(),app.getIsFirst(),app.getType()});
			}
		} catch (Exception e) {
			Log.i(TAG, e.getMessage());
		}finally{
			helper.close();
		}
	}
	
	public boolean isExist(){
		Cursor cursor = null;
		try {
			String sql = "select * from app";
			cursor = helper.getReadableDatabase().rawQuery(sql, null);
			if(cursor.moveToNext()){
				return true;
			}
		} catch (Exception e) {
			Log.i(TAG,e.getMessage());
		}finally{
			if(!cursor.isClosed()){
				cursor.close();
			}
			helper.close();
		}
		return false;
	}
	
	public App getApp(int appId){
		Cursor cursor = null;
		try {
			String sql = "select appid,enname,cnname,ver,addr,isfirst,type from app where appid=?";
			cursor = helper.getReadableDatabase().rawQuery(sql, new String[]{""+appId});
			while(cursor.moveToNext()){
				App app = new App();
				app.setAppId(cursor.getInt(0));
				app.setEnName(cursor.getString(1));
				app.setCnName(cursor.getString(2));
				app.setVer(cursor.getString(3));
				app.setAddr(cursor.getString(4));
				app.setIsFirst(cursor.getInt(5));
				app.setType(cursor.getInt(6));
				return app;
			}
		} catch (Exception e) {
			Log.i(TAG, e.getMessage());
		}finally{
			if(!cursor.isClosed()){
				cursor.close();
			}
			helper.close();
		}
		return null;
	}
	public void updateAppInstState(int appId){
		try {
			String sql = "update app set isfirst=? where appid=?";
			helper.getWritableDatabase().execSQL(sql, new Object[]{0,appId});
		} catch (Exception e) {
			Log.i(TAG,e.getMessage());
		}finally{
			helper.close();
		}
	}
}
