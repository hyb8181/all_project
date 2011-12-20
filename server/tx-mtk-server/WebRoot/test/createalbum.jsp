<%@page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@page import="java.util.*"%>
<%@page import="com.tx.mtk.client.utils.*"%>
<%@page import="java.net.URLDecoder"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>创建相夹</title>
	</head>

	<body>
		<%
			String showStr = "";
			Enumeration<String> names = request.getParameterNames();
			if(names.hasMoreElements()){
				Map<String,String> map = new HashMap<String,String>();
				while(names.hasMoreElements()){
					String name = names.nextElement();
					String value = request.getParameter(name);
					map.put(name,value);
				}
				String url = "http://mobile.tx.com.cn:8082/mtkclient/createalbum.do";
				HttpClientUtil util = new HttpClientUtil();
				showStr = util.postStr(url, map);
			}
			
		 %>
		<table>
		DEMO:<br/>
		viewerId<br/>60815935<br/>imsi=310260000000000<br/>phonenum=15026807270<br/>categoryName=你好<br/>
		<br/>-----------------------------------------------------------------------<br/>
			<form action="createalbum.jsp" method="post">
				浏览者ID:<input type="text" name="viewerId" value="60815935"/><br/>
				ISMI：<input type="text" name="imsi" value="310260000000000"/><br/>
				手机号：<input type="text" name="phonenum" value="15026807270"/><br/>
				相册名称：<input type="text" name="categoryName" value="测试测试"/><br/>
				<input type="submit" name="submit" value="创建相夹"/>
			</form>
		</table>
		<table>
			<%=showStr %>
		</table>
		<a href="/test/index.jsp">返回首页</a>
	</body>
</html>