<%@page import="com.artwell.erp.common.BasicAction.MessageInfo"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户登入</title>
<base href="<%=request.getContextPath() + "/WebErp"%>">
<link rel="stylesheet" type="text/css" href="css/login.css" />
<script type="text/javascript" src="js/jquery-1.11.1.js"></script>
<script type="text/javascript" src="js/common.js"></script>
<script type="text/javascript" src="js/login.js"></script>
</head>
<body>

	<div id="mainDiv">
		<form method="post" action="login">
			<table>
				<tbody>
					<tr>
						<td>用户名</td>
						<td><input id="userName" type="text" name="user.userName" /></td>
					</tr>
					<tr>
						<td>密码</td>
						<td><input id="pwd" type="password" name="user.pwd" /></td>
					</tr>
					<tr>
						<td><input type="checkbox" id="remember" />记住</td>
						<td><input type="submit" value="登入" /></td>
					</tr>
				</tbody>
			</table>
		</form>
	</div>
	<div class="msg">
	<%MessageInfo messageInfo = (MessageInfo) request.getAttribute("messageInfo");
								if (messageInfo != null) {%> <%=messageInfo.getMsg()%>
							 <%}%>
	</div>
</body>
</html>