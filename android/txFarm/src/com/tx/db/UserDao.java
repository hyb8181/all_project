package com.tx.db;

import java.util.ArrayList;
import java.util.List;

import com.tx.bean.User;

import android.content.Context;
import android.database.Cursor;
/**
 * 用户信息存储类
 * @author Crane
 *
 */
public class UserDao {
	private DBHelper userHelper;
	public UserDao(Context context){
		userHelper = new DBHelper(context);
	}
	public void saveOrUpdateUser(User user){
		Cursor cursor = null;
		try {
			String sql = "select * from user where imsi=?";
			cursor = userHelper.getReadableDatabase().rawQuery(sql, new String[]{user.getImsi()});
			if(cursor.moveToNext()){//说明此用户已经存在
				sql = "update user set userid=?,phonenum=? where imsi=?";
				userHelper.getWritableDatabase().execSQL(sql, new Object[]{user.getUserId(),user.getPhoneNum(),user.getImsi()});
			}else{
				sql = "insert into user(userid ,imsi ,phonenum ) values(?,?,?)";
				userHelper.getWritableDatabase().execSQL(sql, new Object[]{user.getUserId(),user.getImsi(),user.getPhoneNum()});
			}
		} catch (Exception e) {
		}finally{
			if(!cursor.isClosed()){
				cursor.close();
			}
			userHelper.close();
		}
	}
	
	public User getUser(String imsi){
		Cursor cursor = null;
		try {
			String sql = "select userid,imsi,phonenum from user where imsi=?";
			cursor = userHelper.getReadableDatabase().rawQuery(sql, new String[]{imsi});
			if(cursor.moveToNext()){
				User user = new User();
				user.setUserId(cursor.getInt(0));
				user.setImsi(cursor.getString(1));
				user.setPhoneNum(cursor.getString(2));
				return user;
			}
		} catch (Exception e) {
		}finally{
			if(!cursor.isClosed()){
				cursor.close();
			}
			userHelper.close();
		}
		return null;
	}
	
	
	/**
	 * 添加用户
	 * @param user
	 */
	public void saveUser(User user){
		try {
			String delSql = "delete from user";
			userHelper.getWritableDatabase().execSQL(delSql);
			Object[] params = new Object[]{user.getUserId(),user.getImsi(),user.getPhoneNum()};
			String sql = "insert into user(userid ,imsi ,phonenum ) values(?,?,?)";
			userHelper.getWritableDatabase().execSQL(sql, params);
		} catch (Exception e) {
		}finally{
			userHelper.close();
		}
	}
	/**
	 * 更新用户
	 * @param user
	 */
	public void updateUser(User user){
		try {
			Object[] params = new Object[]{user.getImsi(),user.getPhoneNum(),user.getUserId()};
			String sql = "update user set imsi=?,phonenum=? where userid=?";
			userHelper.getWritableDatabase().execSQL(sql, params);
		} catch (Exception e) {
		}finally{
			userHelper.close();
		}
	}
	public User getUser(){
		Cursor cursor = null;
		try {
			String sql = "select userid,imsi,phonenum from user";
			cursor = userHelper.getReadableDatabase().rawQuery(sql, null);
			if(cursor.moveToFirst()){
				User user = new User();
				user.setUserId(cursor.getInt(0));
				user.setImsi(cursor.getString(1));
				user.setPhoneNum(cursor.getString(2));
				return user;
			}
		} catch (Exception e) {
		}finally{
			if(!cursor.isClosed()){
				cursor.close();
			}
			userHelper.close();
		}
		return null;
	}
	/*******************************************以下不会使用到********************************************/
	/**
	 * 删除用户
	 * @param id
	 */
	public void delUser(int userId){
		try {
			Object[] params = new Object[]{userId};
			String sql = "delete from user where userid=?";
			userHelper.getReadableDatabase().execSQL(sql, params);
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			userHelper.close();
		}
	}
	public long getUserNum(){
		Cursor cursor = null;
		try {
			String sql = "select count(0) from user";
			cursor = userHelper.getReadableDatabase().rawQuery(sql, null);
			cursor.moveToFirst();
			return cursor.getLong(0);
		} catch (Exception e) {
		}finally{
			if(!cursor.isClosed()){
				cursor.close();
			}
			userHelper.close();
		}
		return 0;
	}
	/**
	 * 查询用户列表
	 * @param startIndex
	 * @param pageNum
	 * @return
	 */
	public List<User> listUser(long startIndex,long pageNum){
		Cursor cursor = null;
		try {
			String[] params = new String[]{String.valueOf(startIndex),String.valueOf(pageNum)};
			String sql = "select userid,imsi,phonenum from user limit ?,? ";
			cursor = userHelper.getReadableDatabase().rawQuery(sql, params);
			List<User> list = new ArrayList<User>();
			while(cursor.moveToNext()){
				User user = new User();
				user.setUserId(cursor.getInt(0));
				user.setImsi(cursor.getString(1));
				user.setPhoneNum(cursor.getString(2));
				list.add(user);
			}
			return list;
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			if(!cursor.isClosed()){
				cursor.close();
			}
			userHelper.close();
		}
		return null;
	}
	
	
}
