package tx.phonebook.data.proc;

import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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

	public static String person2Json(List<PersonInfo> person){
		if(person==null){
			return null;
		}
		Type type = new TypeToken<List<PersonInfo>>(){}.getType();
		String json = JsonUtils.toJson(person, type);
		return json;

	}

	public static String book2Json(List<PhoneBook> phoneBook){
		if(phoneBook==null){
			return null;
		}
		Type type = new TypeToken<List<PhoneBook>>(){}.getType();
		String json = JsonUtils.toJson(phoneBook, type);
		return json;

	}
	public static String psUpdate2Json(List<PersonUpdate> phoneBook){
		if(phoneBook==null){
			return null;
		}
		Type type = new TypeToken<List<PersonUpdate>>(){}.getType();
		String json = JsonUtils.toJson(phoneBook, type);
		return json;

	}
	public static String psUpdatesigle2Json(PersonUpdate phoneBook){
		if(phoneBook==null){
			return null;
		}
		Type type = new TypeToken<PersonUpdate>(){}.getType();
		String json = JsonUtils.toJson(phoneBook, type);
		return json;

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

	public void parseUserFromJson(String jsonData){
		Type listType = new TypeToken<LinkedList<PersonInfo>>(){}.getType();
		Gson gson = new Gson();

		LinkedList<PersonInfo> users = gson.fromJson(jsonData, listType);
		for(Iterator iterator = users.iterator();iterator.hasNext();){
			PersonInfo user = (PersonInfo)iterator.next();
			System.out.println("name--->"+user.getStrName());
			System.out.println("age--->"+user.getStrId());
		}
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
	public static String book2Json(PersonInfo person){
		if(person==null){
			return null;
		}
		Type type = new TypeToken<PersonInfo>(){}.getType();
		String json = JsonUtils.toJson(person, type);
		return json;

	}

}
