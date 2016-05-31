<%
String userName = (String)session.getAttribute("userInfo");
if(userName == null){
	response.sendRedirect("/WebErp/login.jsp");
}
%>