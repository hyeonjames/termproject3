<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="addressbook.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script src="collection.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Welcome</title>
</head>
<body>
<center>
	<%
		UserDB usdb = new UserDB(session);
		if(usdb.getKey() < 0)
		{
			%>
			<script type="text/javascript">
				needlogin('main.jsp');
			</script>
			<%
		}
		else
		{
			%>
			<font size=3>안녕하세요 <%=usdb.id %> 님!</font><br>
			<font size=2>내 번호 : <%=usdb.number %></font>
			<%
		}
	%>
	</center>
</body>
</html>