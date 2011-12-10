package com.android.utils;
import java.util.List;
import java.util.Map;
import java.util.Set;
/**
 * 该类用来把确定的java对象转换成相应的json字符串
 * @author User
 */
public class ObjectToJson {
	/**
	 * 把字符串转换成json字符串
	 * @param s
	 * @return
	 */
	private static String stringToJson(String s) {
		StringBuilder sb = new StringBuilder(s.length() + 20);
		sb.append('\"');
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			switch (c) {
			case '\"':
				sb.append("\\\"");
				break;
			case '\\':
				sb.append("\\\\");
				break;
			case '/':
				sb.append("\\/");
				break;
			case '\b':
				sb.append("\\b");
				break;
			case '\f':
				sb.append("\\f");
				break;
			case '\n':
				sb.append("\\n");
				break;
			case '\r':
				sb.append("\\r");
				break;
			case '\t':
				sb.append("\\t");
				break;
			default:
				sb.append(c);
			}
		}
		sb.append('\"');
		return sb.toString();
	}
	
	public static String toJson(Object obj){
		StringBuffer sb=new StringBuffer();
		if(obj instanceof List){
			sb.append("[");
			List list=(List)obj;
			for(Object object:list){
				if(object instanceof List){
					sb.append(toJson(object));
					sb.append(",");
				}else if(object instanceof Object[]){
					sb.append(toJson(object));
					sb.append(",");
				}else if(object instanceof Map){
					sb.append(toJson(object));
					sb.append(",");
				}else {
					if(object instanceof String){
						sb.append("\"").append(object).append("\"").append(",");
					}else{
						sb.append(object).append(",");
					}
				}
			}
			sb.deleteCharAt(sb.length()-1);
			sb.append("]");
		}
		if(obj instanceof Object[]){
			sb.append("[");
			Object[] objs=(Object[])obj;
			for(Object value:objs){
				if(value instanceof List){
					sb.append(toJson(value));
					sb.append(",");
				}else if(value instanceof Object[]){
					sb.append(toJson(value));
					sb.append(",");
				}else if(value instanceof Map){
					sb.append(toJson(value));
					sb.append(",");
				}else {
					if(value instanceof String){
						sb.append("\"").append(value).append("\"").append(",");
					}else{
						sb.append(value).append(",");
					}
				}
			}
			sb.deleteCharAt(sb.length()-1);
			sb.append("]");
		}
		if(obj instanceof Map){
			sb.append("{");
			Map map=(Map)obj;
			for(Object key:map.keySet()){
				Object value=map.get(key);
				if(value instanceof List){
					sb.append("\"").append(key).append("\"").append(":");
					sb.append(toJson(value));
					sb.append(",");
				}else if(value instanceof Object[]){
					sb.append("\"").append(key).append("\"").append(":");
					sb.append(toJson(value));
					sb.append(",");
				}else if(value instanceof Map){
					sb.append("\"").append(key).append("\"").append(":");
					sb.append(toJson(value));
					sb.append(",");
				}else {
					if(value instanceof String){
						sb.append("\"").append(key).append("\"").append(":").append("\"").append(value).append("\"").append(",");
					}else{
						sb.append("\"").append(key).append("\"").append(":").append(value).append(",");
					}
					
				}
			}
			sb.deleteCharAt(sb.length()-1);
			sb.append("}");
		}
		return sb.toString();
	}
}
