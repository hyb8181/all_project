<%@page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@page import="java.util.*"%>
<%@page import="com.tx.mtk.client.utils.*"%>
<%@page import="java.net.URLDecoder"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>查看所有相册分类</title>
	</head>

	<body>
		<%
			String content = request.getParameter("content");
			String showStr = "";
			if(content==null||content.equals("")){
				content = "";
			}else{
				String[] arrStr = content.split("#");
				Map<String,String> map = new HashMap<String,String>();
				for(String str:arrStr){
					String[] arr = str.split("=");
					String name = arr[0];
					String value = arr[1];
					map.put(name,value);
				}
				String url = "http://mobile.tx.com.cn:8082/mtkclient/viewallalbum.do";
				HttpClientUtil util = new HttpClientUtil();
				showStr = util.postStr(url, map);
			}
		 %>
		<table>
		DEMO:viewerId=60815935#ownerId=60815935#imsi=310260000000000#phonenum=15026807270<br/>
			<form action="allalbum.jsp" method="post">
				<input type="text" name="content"/>
				<input type="submit" name="submit" value="查看"/>
			</form>
		</table>
		<table>
			<%=showStr %>
		</table>
		<a href="/test/index.jsp">返回首页</a>
	</body>
</html>