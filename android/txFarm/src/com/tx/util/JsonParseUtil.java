package com.tx.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tx.bean.App;
import com.tx.bean.TxMenu;
/**
 * JSON解析工具类
 * @author Crane
 *
 */

public class JsonParseUtil {
	public static String getSplashAddr(InputStream is){
		byte[] buffer;
		try {
			buffer = new byte[is.available()];
			while (is.read(buffer) != -1){
				String json = new String(buffer);  
				JSONObject obj = new JSONObject(json);
				String url = obj.getString("url");
				return url;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		return "";
	}
	
	public static List<TxMenu> listMenu(InputStream is){
		byte[] buffer;
		try {
			buffer = new byte[is.available()];
			while (is.read(buffer) != -1){
				String json = new String(buffer);  
				JSONArray arr = new JSONArray(json);
				List<TxMenu> list = new ArrayList<TxMenu>()	;
				for(int i=0;i<arr.length();i++){
					JSONObject obj = arr.getJSONObject(i);
					TxMenu menu = new TxMenu();
					menu.setOrder(obj.getInt("order"));
					menu.setLogo(obj.getString("logo"));
					menu.setName(obj.getString("name"));
					menu.setUrl(obj.getString("url"));
					list.add(menu);
				}
				return list;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}  
		return null;
	}
	
	public static List<App> listApp(InputStream is){
		byte[] buffer;
		try {
			buffer = new byte[is.available()];
			while (is.read(buffer) != -1){
				String json = new String(buffer);  
				JSONArray arr = new JSONArray(json);
				List<App> list = new ArrayList<App>()	;
				for(int i=0;i<arr.length();i++){
					JSONObject obj = arr.getJSONObject(i);
					App app = new App();
					app.setAppId(obj.getInt("appId"));
					app.setCnName(obj.getString("cnName"));
					app.setEnName(obj.getString("enName"));
					app.setVer(obj.getString("ver"));
					app.setAddr(obj.getString("addr"));
					app.setIsFirst(obj.getInt("isFirst"));
					app.setType(obj.getInt("type"));
					list.add(app);
				}
				return list;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}  
		return null;
	}
}
