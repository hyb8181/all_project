<%@page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@page import="java.util.*"%>
<%@page import="com.tx.mtk.client.utils.*"%>
<%@page import="java.net.URLDecoder"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>相册分类</title>
	</head>

	<body>
		<%
			String showStr = "";
			String url = "http://mobile.tx.com.cn:8082/mtkclient/uploadphone.do";
			//String url = "http://127.0.0.1:8080/tx-mtk-server/mtkclient/uploadphone.do";
			int viewerId = 60815935;
			String imsi = "310260000000000";
			String phonenum = "15026807270";
		 %>
		<table>
			<form action="<%=url %>" method="post" enctype="multipart/form-data">
				上传文件：<input type="file" name="file1" value=""/><br/>
				文件名称：<input type="text" name="text"/><br/>
				相册类别：<input type="text" name="categoryId"/><br/>
				<input type="hidden" name="viewerId" value="<%=viewerId %>"/> 
				<input type="hidden" name="imsi" value="<%=imsi %>"/> 
				<input type="hidden" name="phonenum" value="<%=phonenum %>"/> 
				<input type="submit" name="submit" value="上传"/><br/>
			</form>
		</table>
		<table>
			<%=showStr %>
		</table>
		<a href="/test/index.jsp">返回首页</a>
	</body>
</html>