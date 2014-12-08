<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@page import="java.util.*" %>
    <%@page import="addressbook.*" %>
<html>
<head>
<script type="text/javascript">


</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body bgcolor="#B0C4DE">
<a href="javascript:parent.location.href='index.jsp'"><img src="Logo.png"></a>
<table border=1>
	<TR height=30>
	<TD width=200 align="center">
		<a href="list.jsp?page=1" target="main"> <font size=3>주소록 관리</font> </a>
	</TD>
	</TR>
	<TR height=30>
	<TD align="center"><a href="cmulog.jsp?page=1&smsto=on&smsfrom=on&callto=on&callfrom=on" target="main"><font size=3>메세지 보관함</font></a></TD>
	</TR>
	<TR height=30>
	<%
	request.setCharacterEncoding("UTF-8");
	UserDB usdb = new UserDB(session);
	if(usdb.getKey() < 0)
	{
		%>
		
		<TD align="center"> <a href="login.jsp" target="main"><font size=3>로그인</font> </a>
		<%
	}
	else
	{
	%>
	
		<TD align="center"> <a href="logout.jsp" target="main"><font size=3>로그아웃</font> </a>
	
	<%} %>
	
	</TD>
	</TR>
</table>
</body>
</html>