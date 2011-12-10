package com.android.utils;

import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.android.model.SettingUpdateLocalInfo;
import com.android.model.SettingUpdateServerInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

public class JsonUtils {

//	private static final Logger logger = Logger.getLogger(JSONUtil.class);
	/** 空的 {@code JSON} 数据 - <code>"{}"</code>。 */
	public static final String EMPTY_JSON = "{}";
	/** 空的 {@code JSON} 数组(集合)数据 - {@code "[]"}。 */
	public static final String EMPTY_JSON_ARRAY = "[]";
	/** 默认的 {@code JSON} 日期/时间字段的格式化模式。 */
	public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss SSS";
	/** {@code Google Gson} 的 {@literal @Since} 注解常用的版本号常量 - {@code 1.0}。 */
	public static final Double SINCE_VERSION_10 = 1.0d;
	/** {@code Google Gson} 的 {@literal @Since} 注解常用的版本号常量 - {@code 1.1}。 */
	public static final Double SINCE_VERSION_11 = 1.1d;
	/** {@code Google Gson} 的 {@literal @Since} 注解常用的版本号常量 - {@code 1.2}。 */
	public static final Double SINCE_VERSION_12 = 1.2d;

	public static final String EMPTY = "";

	/**
	 * 本机数据转为json格式
	 * @param localinfo
	 * @return
	 */
	public static String localInfo2Json(SettingUpdateLocalInfo localinfo){
		if(localinfo==null){
			return null;
		}
		Type type = new TypeToken<SettingUpdateLocalInfo>(){}.getType();
		String json = JsonUtils.toJson(localinfo, type);
		return json;
	}
	
	/**
	 * 服务器信息转为json
	 * @param serverinfo
	 * @return
	 */
	public static String serverVersion2Json(SettingUpdateServerInfo serverinfo){
		if(serverinfo==null){
			return null;
		}
		Type type = new TypeToken<SettingUpdateServerInfo>(){}.getType();
		String json = JsonUtils.toJson(serverinfo, type);
		return json;

	}
	
	
	/**
	 * 本机信息转为gson格式
	 * @param localinfo
	 * @return
	 */
	public static String localInfo2gson(SettingUpdateLocalInfo localinfo){
		String json = "";
		Gson gson=new Gson();
		json = gson.toJson(localinfo);
		return json;
	}
	/**
	 * 服务器信息转为gson信息
	 * @param serverinfo
	 * @return
	 */
	public static String serverlInfo2gson(SettingUpdateLocalInfo serverinfo){
		String json = "";
		Gson gson=new Gson();
		json = gson.toJson(serverinfo);
		return json;
	}
	/**
	 * gson转为本机信息对象
	 */
	public static SettingUpdateLocalInfo gson2LocalInfo(String input){
		Type type = new TypeToken<SettingUpdateLocalInfo>(){}.getType();
		Gson localInfoGson = new Gson();
		SettingUpdateLocalInfo lInfo = localInfoGson.fromJson(input, type);
		return lInfo;
	}
	/**
	 * gson转为服务器信息对象
	 * @param input
	 */
	public static SettingUpdateLocalInfo gson2ServerInfo(String input){
		Type type = new TypeToken<SettingUpdateLocalInfo>(){}.getType();
		Gson serverInfoGson = new Gson();
		SettingUpdateLocalInfo sinfo = serverInfoGson.fromJson(input, type);
		return sinfo;
	}

	public void parseJson(String jsonData){
		try{
			//如果要解析json数据，需要先生成一个JsonReader对象
			JsonReader reader = new JsonReader(new StringReader(jsonData));
			reader.beginArray();
			while(reader.hasNext()){
				reader.beginObject();
				while(reader.hasNext()){
					String tagName = reader.nextName();
					if(tagName.equals("name")){
						System.out.println("name--->"+reader.nextString());
					}
					else if(tagName.equals("age")){
						System.out.println("age--->"+reader.nextString());
					}
				}
				reader.endObject();
			}
			reader.endArray();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public static String toJson(Object target, Type targetType) {
		return toJson(target, targetType, false, null, null, true);
	}


	private static boolean isEmpty(String json) {
		return json == null || json.length() == 0;
	}

	public static String toJson(Object target, Type targetType,
			boolean isSerializeNulls, Double version, String datePattern,
			boolean excludesFieldsWithoutExpose) {
		if (target == null)
			return EMPTY_JSON;
		GsonBuilder builder = new GsonBuilder();
		if (isSerializeNulls)
			builder.serializeNulls();
		if (version != null)
			builder.setVersion(version.doubleValue());
		if (isEmpty(datePattern))
			datePattern = DEFAULT_DATE_PATTERN;
		builder.setDateFormat(datePattern);
		if (excludesFieldsWithoutExpose)
			builder.excludeFieldsWithoutExposeAnnotation();
		String result = EMPTY;
		Gson gson = builder.create();
		try {
			if (targetType != null) {
				result = gson.toJson(target, targetType);
			} else {
				result = gson.toJson(target);
			}
		} catch (Exception ex) {
	//		logger.warn("目标对象 " + target.getClass().getName()
	//				+ " 转换 JSON 字符串时，发生异常！", ex);
			if (target instanceof Collection || target instanceof Iterator
					|| target instanceof Enumeration
					|| target.getClass().isArray()) {
				result = EMPTY_JSON_ARRAY;
			} else
				result = EMPTY_JSON;
		}
		return result;
	}

}
