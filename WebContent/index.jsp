<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="addressbook.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<!-- 프로그램 메인페이지입니다. -->
	<FRAMESET cols="220,*">
		<FRAME frameborder="0" SRC="menu.jsp" name="menu" scrolling="auto" noresize>
	<%
		UserDB usdb= new UserDB(session);
		if(usdb.getKey()>=0){
	%>
		<FRAME frameborder="0" SRC="main.jsp" name="main" scrolling="auto" noresize>
		<%}
		else
		{
		%>
		<FRAME frameborder="0" SRC="login.jsp" name="main" scrolling="auto" noresize>
		<%} %>		
	</FRAMESET>
	
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

</body>
</html>