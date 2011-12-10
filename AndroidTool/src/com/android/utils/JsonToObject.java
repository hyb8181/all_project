package com.android.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 改类用来把相应的json字符串转换成确定的java对象
 * @author User
 * 		char a='[' ;//91
		char b='{';//123
		char c=']';//93
		char d='}';//125
		char e=',';//44
		char f='"';//34
		char g=':';//58
 */
public class JsonToObject {
	private static List<Object> stringToList(String s){
		List<Object> lists=new ArrayList<Object>();
		int intBef=0;
		int intAft=0;
		int count=0;
		boolean flag=false;
		String ret=null;
		char c1=0;
		char c2=0;
		for(int i=0;i<s.length();i++){
			char ch=s.charAt(i);
			int result=(int)ch;
			if((result==34)){
				count++;
				if(count==1){
					intBef=i;
					flag=true;
				}
				if(count==2){
					intAft=i;
					flag=false;
					count=0;
					ret=s.substring(intBef+1, intAft);
					intBef=0;
					intAft=0;
					lists.add(ret);
				}
			}else if((result==91||result==123||result==44||result==58)&&!flag){
				if((int)(s.charAt(i+1))!=34&&(int)(s.charAt(i+1))!=91&&(int)(s.charAt(i+1))!=123){
					intBef=i;
					flag=true;
					c1=ch;
				}
				lists.add(ch);
			}else if((result==93||result==125||result==44||result==58)&&flag){
				c2=ch;
				intAft=i;
				ret=s.substring(intBef+1, intAft);
				lists.add(ret);
				if((int)c1==44&&(int)c2==44){
					i=i-1;
				}else if((int)c1==44&&((int)c2==44||(int)c2==125)){
					i=i-1;
				}else if((int)c1==91&&(int)c2==44){
					i=i-1;
				}
				intBef=0;
				intAft=0;
				c1=0;
				c2=0;
				flag=false;
				lists.add(ch);
			}else{
				if(!flag){
					lists.add(ch);
				}
			}
		}
		return lists;
	}
	public static void main(String[] args) {
		/*List<Map<Object, Object[]>> lists=new ArrayList<Map<Object,Object[]>>();
		Map<Object, Object[]> map1=new HashMap<Object, Object[]>();
		Map<Object, Object[]> map2=new HashMap<Object, Object[]>();
		Object[] obj1=new Object[]{"a","b","c","d"};
		Object[] obj2=new Object[]{"e","f",23,true,false};
		Object[] obj3=new Object[]{"h",true};
		Object[] obj4=new Object[]{23.5f};
		map1.put("obj1", obj1);
		map1.put("obj2", obj2);
		map2.put("obj3", obj3);
		map2.put("obj4", obj4);
		lists.add(map1);
		lists.add(map2);
		String res=ObjectToJson.toJson(lists);
		System.out.println(res);
		System.out.println("------------------");
		List<Object> list5=stringToList(res);
		System.out.println(list5);
		toObject(res);*/
		
		
		/*List<Object[]> lists=new ArrayList<Object[]>();
		Object[] obj1=new Object[]{"a","b",23.5f,true,false};
		Object[] obj2=new Object[]{"ccc",12};
		lists.add(obj1);
		lists.add(obj2);
		String res=ObjectToJson.toJson(lists);
		System.out.println(res);
		System.out.println("------------------");
		List<Object> list5=stringToList(res);
		System.out.println(list5);
		System.out.println();
		toObject(res);*/
		
		/*Map<Object, Map<Object, List<Object>>> map=new HashMap<Object, Map<Object, List<Object>>>();
		Map<Object, List<Object>> map1=new HashMap<Object, List<Object>>();
		Map<Object, List<Object>> map2=new HashMap<Object, List<Object>>();
		List<Object> list1=new ArrayList<Object>();
		List<Object> list2=new ArrayList<Object>();
		list1.add("aaa");
		list1.add(true);
		list1.add(12.5f);
		list1.add(24);
		list2.add("ccc");
		list2.add(true);
		map1.put("list1", list1);
		map2.put("list2", list2);
		map.put("map1", map1);
		map.put("map2", map2);
		String res=ObjectToJson.toJson(map);
		System.out.println(res);
		System.out.println("------------------");
		List<Object> list5=stringToList(res);
		System.out.println(list5);
		System.out.println();
		toObject(res);*/
		
		
		/*List<Map<Object, Object>> lists=new ArrayList<Map<Object,Object>>();
		Map<Object, Object> map1=new HashMap<Object, Object>();
		Map<Object, Object> map2=new HashMap<Object, Object>();
		map1.put("key1", "aaa");
		map1.put("key2", true);
		map1.put("key3", false);
		map1.put("key4", 12.5f);
		map2.put("key1", false);
		map2.put("key2", false);
		map2.put("key3", "bbb");
		lists.add(map1);
		lists.add(map2);
		String res=ObjectToJson.toJson(lists);
		System.out.println(res);
		System.out.println("------------------");
		List<Object> list5=stringToList(res);
		System.out.println(list5);
		System.out.println();
		toObject(res);*/
		
		/*Map<Object, List<Map<Object,Object>>> map=new HashMap<Object, List<Map<Object,Object>>>();
		List<Map<Object,Object>> list1=new ArrayList<Map<Object,Object>>();
		List<Map<Object,Object>> list2=new ArrayList<Map<Object,Object>>();
		Map<Object,Object> map1=new HashMap<Object, Object>();
		Map<Object,Object> map2=new HashMap<Object, Object>();
		Map<Object,Object> map3=new HashMap<Object, Object>();
		Map<Object,Object> map4=new HashMap<Object, Object>();
		map1.put("key1","aaa");
		map1.put("key2",true);
		map1.put("key3",false);
		map1.put("key4",22.5f);
		map2.put("key1",12);
		map2.put("key2",true);
		map2.put("key3",22.5f);
		map3.put("key1",69);
		map3.put("key2",25.6f);
		map4.put("key1","zzz");
		list1.add(map1);
		list1.add(map2);
		list2.add(map3);
		list2.add(map4);
		map.put("list1", list1);
		map.put("list2", list2);
		String res=ObjectToJson.toJson(map);
		System.out.println(res);
		System.out.println("------------------");
		List<Object> list5=stringToList(res);
		System.out.println(list5);
		System.out.println();
		toObject(res);*/
		
		/*Map<Object, List<Object[]>> map=new HashMap<Object, List<Object[]>>();
		List<Object[]> list1=new ArrayList<Object[]>();
		List<Object[]> list2=new ArrayList<Object[]>();
		List<Object[]> list3=new ArrayList<Object[]>();
		Object[] obj1=new Object[]{"aaa",true,false,25};
		Object[] obj2=new Object[]{"xxx",22,"bbb",10};
		Object[] obj3=new Object[]{true,false,"ddd"};
		Object[] obj4=new Object[]{true};
		Object[] obj5=new Object[]{false,true,22.5f,23};
		Object[] obj6=new Object[]{"eee",true};
		list1.add(obj1);
		list1.add(obj2);
		list1.add(obj3);
		list2.add(obj4);
		list2.add(obj5);
		list3.add(obj6);
		map.put("list1", list1);
		map.put("list2", list2);
		map.put("list3", list3);
		String res=ObjectToJson.toJson(map);
		System.out.println(res);
		System.out.println("------------------");
		List<Object> list5=stringToList(res);
		System.out.println(list5);
		System.out.println();
		toObject(res);*/
		
		/*List<Object> lists=new ArrayList<Object>();
		lists.add(true);
		lists.add(false);
		lists.add("aaa");
		lists.add(12.5);
		String res=ObjectToJson.toJson(lists);
		System.out.println(res);
		System.out.println("------------------");
		List<Object> list5=stringToList(res);
		System.out.println(list5);
		System.out.println();
		toObject(res);*/
		
		/*Map<Object, Object> map=new HashMap<Object, Object>();
		map.put("key1", true);
		map.put("key2", "ccc");
		map.put("key3", 12.5f);
		map.put("key4", 24);
		String res=ObjectToJson.toJson(map);
		System.out.println(res);
		System.out.println("------------------");
		List<Object> list5=stringToList(res);
		System.out.println(list5);
		System.out.println();
		toObject(res);*/
		
		/*Object[] object=new Object[]{true,false,25.5f,23};
		String res=ObjectToJson.toJson(object);
		System.out.println(res);
		System.out.println("------------------");
		List<Object> list5=stringToList(res);
		System.out.println(list5);
		System.out.println();
		toObject(res);*/
		
		
		/*Map<Object, Object> map1=new HashMap<Object, Object>();
		Map<Object, Object> map2=new HashMap<Object, Object>();
		Map<Object, Object> map3=new HashMap<Object, Object>();
		map1.put("key1", "aaa");
		map1.put("key2", true);
		map1.put("key3", false);
		map1.put("key4", 12.5f);
		
		map2.put("key1", "bbb");
		map2.put("key2", true);
		map2.put("key3", false);
		
		map3.put("key1", "ccc");
		map3.put("key2", true);
		Object[] obj=new Object[]{map1,map2,map3};
		String res=ObjectToJson.toJson(obj);
		System.out.println(res);
		System.out.println("------------------");
		List<Object> list5=stringToList(res);
		System.out.println(list5);
		System.out.println();
		toObject(res);*/
		
		/*Map<Object,List<Object>> map1=new HashMap<Object, List<Object>>();
		Map<Object,List<Object>> map2=new HashMap<Object, List<Object>>();
		Map<Object,List<Object>> map3=new HashMap<Object, List<Object>>();
		List<Object> list1=new ArrayList<Object>();
		list1.add("aaa");
		list1.add(true);
		list1.add(false);
		list1.add(23);
		List<Object> list2=new ArrayList<Object>();
		list2.add("bbb");
		list2.add(23.5f);
		List<Object> list3=new ArrayList<Object>();
		list3.add("ccc");
		list3.add(true);
		list3.add(false);
		List<Object> list4=new ArrayList<Object>();
		list4.add("ddd");
		list4.add("fff");
		List<Object> list5=new ArrayList<Object>();
		list5.add("ggg");
		list5.add(15);
		list5.add(16);
		List<Object> list6=new ArrayList<Object>();
		list6.add("hhh");
		map1.put("list1", list1);
		map1.put("list2", list2);
		map1.put("list3", list3);
		map2.put("list4", list4);
		map3.put("list5", list5);
		map3.put("list6", list6);
		Object[] obj=new Object[]{map1,map2,map3};
		String res=ObjectToJson.toJson(obj);
		System.out.println(res);
		System.out.println("------------------");
		List<Object> list7=stringToList(res);
		System.out.println(list7);
		System.out.println();
		toObject(res);*/
	}
	public static Object toObject(String s){
		List<Object> colls=new ArrayList<Object>();
		List<Object> results=new ArrayList<Object>();
		List<Object> lists=stringToList(s);
		boolean flag=false;
		String key=null;
		String outerKey=null;
		for(int i=0;i<lists.size();i++){
			Object obj=lists.get(i);
			if(obj.toString().equals("[")){
				List<Object> list=new ArrayList<Object>();
				colls.add(list);
			}else if(obj.toString().equals("{")){
				Map<Object,Object> map=new HashMap<Object, Object>();
				colls.add(map);
			}else if(obj.toString().equals("}")){
				if(flag){
					Map<Object, Object> map=(Map<Object, Object>)colls.get(colls.size()-1);
					if(colls.get(colls.size()-2) instanceof Map){
						((Map)colls.get(colls.size()-2)).put(key, map);
					}else if(colls.get(colls.size()-2) instanceof List){
						((List)colls.get(colls.size()-2)).add(map);
					}
					colls.remove(colls.size()-1);
					flag=false;
				}else{
					if((colls.size()-2)>=0){
						if(colls.get(colls.size()-2) instanceof Map){
							((Map)colls.get(colls.size()-2)).put(outerKey, colls.get(colls.size()-1));
						}else if(colls.get(colls.size()-2) instanceof List){
							((List)colls.get(colls.size()-2)).add(colls.get(colls.size()-1));
						}
					}else{
						results.add(colls.get(colls.size()-1));
					}
					colls.remove(colls.size()-1);
				}
			}else if(obj.toString().equals("]")){
				if(flag){
					List<Object> list=(List<Object>)colls.get(colls.size()-1);
					if((colls.size()-2)>0){
						if(colls.get(colls.size()-2) instanceof Map){
							((Map)colls.get(colls.size()-2)).put(outerKey, list);
						}else if(colls.get(colls.size()-2) instanceof List){
							((List)colls.get(colls.size()-2)).add(list);
						}
					}
					colls.remove(colls.size()-1);
					flag=false;
				}else{
					if((colls.size()-2)>=0){
						if(colls.get(colls.size()-2) instanceof Map){
							((Map)colls.get(colls.size()-2)).put(outerKey, colls.get(colls.size()-1));
						}else if(colls.get(colls.size()-2) instanceof List){
							((List)colls.get(colls.size()-2)).add(colls.get(colls.size()-1));
						}
					}else{
						results.add(colls.get(colls.size()-1));
					}
					colls.remove(colls.size()-1);
				}
			}else if(obj.toString().equals(",")){
			}else if(obj.toString().equals(":")){
			}else{
				if(colls.get(colls.size()-1) instanceof List){
					((List<Object>)colls.get(colls.size()-1)).add(obj);
				}
				if(colls.get(colls.size()-1) instanceof Map){
					String string=lists.get(i+2).toString();
					char ch=string.charAt(0);
					String str=obj.toString();
					if(!((int)ch==91)&&!((int)ch==123)){
						((Map)colls.get(colls.size()-1)).put(obj, lists.get(i+2));
						i=i+2;
					}else if((int)ch==123){
						flag=true;
						key=lists.get(i).toString();
					}else if((int)ch==91&&((int)(lists.get(i+3).toString().charAt(0))==123)){
						flag=true;
						key=lists.get(i).toString();
					}
				}
				if(colls.get(colls.size()-1) instanceof Map){
					String string=null;
					if((i+2)<=(lists.size()-1)){
						string=lists.get(i+2).toString();
					}
					char ch=0;
					if(string!=null){
						ch=string.charAt(0);
					}
					if((int)ch==91&&((int)(lists.get(i+3).toString().charAt(0))!=123)){
						flag=true;
						outerKey=lists.get(i).toString();
					}
				}
			}
		}
		if(results.size()==1){
			return results.get(0);
		}
		return null;
	}
}
