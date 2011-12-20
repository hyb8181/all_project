<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<!DOCTYPE html PUBdivC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>登录</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	</head>
	<body>
		<%if(request.getAttribute("msg")!=null){ %>
		<%=request.getAttribute("msg") %>
		<%} %>
		<form action="/tx-mtk-server/mtkserver/login.do" method="post"
			id="reg">
			<table width="100%" border="0" cellpadding="" cellspacing="4">
				<tr>
					<td width="20%" align="right">
						邮箱
					</td>
					<td width="44%">
						<input type="text" name="email" size="50" />
					</td>
					<td width="44%">
						<span id="emailMessage">(*必填)</span>
					</td>
				</tr>

				<tr>
					<td width="20%" align="right">
						密码
					</td>
					<td width="44%">
						<input type="password" name="password" size="50" />
					</td>
					<td width="44%">
						<span id="passwordMessage">(*必填)</span>
					</td>
				</tr>

				<tr>
					<td colspan="2" align="center">
						<input type="submit" name="Submit" value="提交申请" />
					</td>
				</tr>
			</table>
		</form>
	</body>
</html>
