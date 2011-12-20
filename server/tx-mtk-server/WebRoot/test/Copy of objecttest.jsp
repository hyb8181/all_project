<%@page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@page import="java.io.ObjectOutputStream"%>
<%@page import="java.io.OutputStream"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>

<%

			String test = "ffffffffffffkj;i38989089";
			response.getWriter().write(test);
			//response.getWriter().close();
	/**
		//字符串
			String test = "a一花一世界33334444一叶一如来a";
			OutputStream os = response.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(os);
			oos.writeObject(test);
			oos.close();
			
			//单个对象
			Admin admin = new Admin();
			admin.setId(123456);
			admin.setPassword("password123");
			admin.setEmail("qiuxiluoying@tx.com.cn");
			admin.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			OutputStream os = response.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(os);
			oos.writeObject(admin);
			oos.close();
			
			//对象列表
			List<Admin> list = new ArrayList<Admin>();
			for(int i=0;i<3;i++){
				Admin admin = new Admin();
				admin.setId(123456+i);
				admin.setPassword("password123");
				admin.setEmail("qiuxiluoying@tx.com.cn");
				admin.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				list.add(admin);
			}
			OutputStream bos = response.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(list);
			oos.close();
			
			//当个对象封装
			Admin admin = new Admin();
			admin.setId(123456);
			admin.setPassword("password123");
			admin.setEmail("qiuxiluoying@tx.com.cn");
			admin.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			SingleObj obj = new SingleObj<Admin>();
			obj.setEntry(admin);
			OutputStream os = response.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(os);
			oos.writeObject(obj);
			oos.close();
			
			//对个对象封装
			List<Admin> list = new ArrayList<Admin>();
			for(int i=0;i<3;i++){
				Admin admin = new Admin();
				admin.setId(123456+i);
				admin.setPassword("password123");
				admin.setEmail("qiuxiluoying@tx.com.cn");
				admin.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				list.add(admin);
			}
			MultiObj obj = new MultiObj<Admin>();
			obj.setEntry(list);
			obj.setPageNum(3);
			obj.setStartIndex(2);
			obj.setTotal(100);
			OutputStream os = response.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(os);
			oos.writeObject(obj);
			oos.close();
		
			
			
			List<String[]> list = new ArrayList<String[]>();
			list.add(new String[]{"a","b","d","e"});
			list.add(new String[]{"1","2","3","4"});
			OutputStream os = response.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(os);
			oos.writeObject(list);
			oos.close();**/
			%>