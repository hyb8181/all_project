package com.AndroidTool.suggest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.android.model.SettingFeedbackInfo;
import com.android.model.SettingSuggestResponseInfo;
import com.android.utils.DBHelper;


public class SettingSuggestService {
	private static final String TAG = "SettingSuggestService";
	private DBHelper dbOpenHelper;
	public SettingSuggestService(Context context){
		dbOpenHelper = new DBHelper(context);
	}
	/**
	 * 获取所有建议反馈
	 */
	public List<SettingSuggestResponseInfo> getAllSuggestInfo(){
		List<SettingSuggestResponseInfo> list = new ArrayList<SettingSuggestResponseInfo>();
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		if(db.isOpen()){
			Cursor cursor = db.rawQuery("select * from suggest order by _id desc", null);
			while(cursor.moveToNext()){
				SettingSuggestResponseInfo item = new SettingSuggestResponseInfo();
				item.suggest = cursor.getString(cursor.getColumnIndex("suggest"));
				item.feedback = cursor.getString(cursor.getColumnIndex("feedback"));
				list.add(item);
			}
			cursor.close();
			db.close();
		}
		return list;
	}
	
	/**
	 * 添加新记录
	 * @param number 号码
	 */
	public boolean insert(String suggest,String feedback){
		
		try {
			SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
			if(db.isOpen()){
				db.execSQL("insert into suggest(suggest,feedback) values(?,?)", new Object[]{suggest,feedback});
				db.close();
			}
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 更新数据库
	 * @param suggest 建议
	 * @param feedback 反馈
	 */
	public int update(String suggest, String feedback){
		int result = 0;
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        String where = "suggest=?";
        String[] whereValue = {suggest};
        ContentValues cv = new ContentValues();
        cv.put("feedback", feedback);
        result = db.update("suggest", cv, where, whereValue);
        db.close();
        return result;
}
	 
	/**
	 * 删除记录
	 * @param id 数据id
	 */
	public boolean delete(int id){
		try {
			SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
			if(db.isOpen()){
				db.execSQL("delete from suggest where _id=?", new Object[]{id});
				db.close();
			}
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * 将轮询获取到的JSON存入数据库中
	 * @param strResponse
	 * @return
	 */
	public boolean pollupdate(String strResponse){
		Log.i(TAG, "轮询获取到服务器的数据:"+strResponse);
		try {
			String json = new String(strResponse);  
			JSONArray arr = new JSONArray(json);
			List<SettingFeedbackInfo> list = new ArrayList<SettingFeedbackInfo>();
			
			for(int i=0;i<arr.length();i++){
				JSONObject obj = arr.getJSONObject(i);
				SettingFeedbackInfo info = new SettingFeedbackInfo();
				info.title = obj.getString("title");
				info.answer = obj.getString("answer");
				info.status = obj.getString("status");
				Log.i(TAG, "问题:" +info.title);
				Log.i(TAG, "答复:" +info.answer);
				Log.i(TAG, "状态:" +info.status);
				if((!info.answer.equals(""))&&(!info.answer.equals("null"))){
					update(info.title,info.answer);
				}
			}
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
}
