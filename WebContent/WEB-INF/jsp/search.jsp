<%@page import="com.artwell.erp.common.BasicAction.MessageInfo"%>
<%@page import="java.util.List"%>
<%@page import="com.artwell.erp.techquote.entity.QuoteInfo"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<base href="<%=request.getContextPath()+"/WebErp"%>">
<title>报价查询</title>
<link rel="stylesheet" type="text/css" href="css/common.css" />
<link rel="stylesheet" type="text/css" href="css/search.css" />
<script type="text/javascript" src="js/jquery-1.11.1.js"></script>
<script type="text/javascript" src="js/common.js"></script>
<script type="text/javascript" src="js/search.js"></script>
</head>
<body>
<%@include file="/checkLogin.jsp" %>
	<div id="searchDiv">
		<form method="get" action="techquote/search">
			<table>
				<tbody>
					<tr>
						<td>样板单号/条形码/款号</td>
						<td><input type="text" name="search.searchValue" placeholder="样板单号/条形码/款号"/></td>
						<td>客户</td>
						<td><input type="text" name="search.client" /></td>
						<td><input type="submit"  value="查询" class="submit" /></td>
					</tr>
					<tr>
						<td>报价生成时间范围</td>
						<td><input type="date" name="search.begDate" />--<input type="date" name="search.endDate" /></td>
						<td>状态选择</td>
						<td>
						<select name="search.statusInt">
							<option value="0">全部</option>
							<option value="1">未报价</option>
							<option value="2">已报价</option>
							<!-- 
							<option value="3">已审核</option>
							 -->
						</select></td>
					</tr>
				</tbody>
			</table>
		</form>
	</div>

	<div id="detailDiv">
		<form method="post" action="#">
			<table border="1" style="border-collapse: collapse;margin-bottom: 2px;">
				<thead>
					<tr>
						<td>客户</td><td>款号</td><td>样板单号</td><td>样板类型</td><td>版本</td><td>最近修改时间</td><td>状态</td><td><input type="checkbox" id="selectAll" />全选</td>
					</tr>
				</thead>
				<tbody>
				<% List<QuoteInfo> quoteInfoList=(List<QuoteInfo>) request.getAttribute("quoteInfoList");
					if(quoteInfoList!= null){
				 	  for (QuoteInfo quoteInfo : quoteInfoList) {%>
						<tr>
						<td><%=quoteInfo.getClient() %></td>
						<td><%=quoteInfo.getStyleNo() %></td>
						<td><%=quoteInfo.getSampleOrderNo() %></td>
						<td><%=quoteInfo.getSampleType() %></td>
						<td><%=quoteInfo.getVersion() %></td>
						<td><%=quoteInfo.getQuoteDate() %></td>
						<td><%=quoteInfo.getStatus() %></td>
						<td><input type="checkbox" /></td>
						</tr>
				<%}
				   }else{%>
				 	  <tr><td colspan="8"><div style="width: 10ox;height: 25px;"></div></td></tr>
				   <%} %>
				</tbody>
			</table>
			<div class="buttonBar">
				<input type="button" name="加入查看列表" value="加入查看列表" id="addCart" class="button"/>
			</div>
		</form>
	</div>

	<div id="cartListDiv">
		<table border="1" style="border-collapse: collapse;">
			<thead>
				<tr>
					<td colspan="7" class="title">查看列表</td>
				</tr>
				<tr>
					<td>客户</td><td>款号</td><td>样板单号</td><td>样板类型</td><td>版本</td><td>最近修改时间</td><td>状态</td>
				</tr>
			</thead>
			<tbody>
			</tbody>
		</table>
		<div class="buttonBar">
			<input type="button" value="查看列表" id="view"  />
			<input type="button" value="清空列表" id="clear" />
		</div>
	</div>
<div id="messageBox" class="messageBox">
	 	<ul>
	 	 <% MessageInfo messageInfo= (MessageInfo)request.getAttribute("messageInfo");
		if(messageInfo!= null){%> 
	 	  <li><%=messageInfo.getMsg() %></li>
		<% }%> 
	 	</ul>
</div>
</body>
</html>