<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="addressbook.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script src="collection.js"></script>


<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>LOGIN</title>
</head>
<body>
	<%
	// 로그인을 담당하는 JSP페이지 입니다.
	request.setCharacterEncoding("UTF-8");
		String txtID = request.getParameter("id");
			String txtPwd = request.getParameter("pwd");
			String txtGo = request.getParameter("page");
			if(txtGo==null) txtGo="main.jsp";
			if(txtID==null){
				txtID = (String)session.getAttribute("usid");
		if(txtID==null) txtID="";
			}
			
			if(txtPwd!=null && txtID.length() > 0)
			{
		UserDB dbhelp = new UserDB(session,txtID,txtPwd);
		if(dbhelp.getKey() < 0)
		{
	%>
				<script type="text/javascript">
				error("아이디혹은 비밀번호가 올바르지 않습니다.");
				</script>
				<%
			}
			else
			{
				%>
				<script type="text/javascript">
				location.href = "<%=txtGo%>";
				parent.menu.location.reload();
				
				</script>
				<%
			}
		}
		else
		{
			
	%>
	<center>
	<form method=get action="login.jsp">
		ID : <input type="text" name="id" value="<%=txtID %>"><br>
		PWD : <input type="password" name="pwd" value="">
		<input type="hidden" value="<%=txtGo%>"><br>
		<input type="button" value="회원가입" onclick="goPage('join.jsp?page=<%=txtGo%>');">
		<input type="submit" value="로그인">
	</form>
	</center>
	<%} %>
</body>
</html>