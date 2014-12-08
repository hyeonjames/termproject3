<%@page import="java.sql.SQLException"%>
<%@page import="addressbook.*" %>
<%@page import="java.util.ArrayList" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Information</title>
</head>
<body>
<script src="collection.js"></script>
	<center>
	<%
	// 주소록 정보를 보여주는 JSP페이지입니다.
		int id = Integer.valueOf(request.getParameter("id"));
		try{
			UserDB dbhelp = new UserDB(session);
			if(dbhelp.getKey() >=0){
			Address ads = dbhelp.load(id);
			if(ads!=null)
			{
		String semail="";
		String sphone="";
		ArrayList<Email> emails = dbhelp.loadEmails(id);
		ArrayList<Phone> phones = dbhelp.loadPhones(id);
	%>
	<table border="1">
		<tr>
		<td rowspan=5>
			<img src="img/<%=ads.photofile %>" width=200 height=300 />
		</td>
		<td align="center">
			<pre> 이름 : <%=ads.name %></pre>
		</td>
		</tr>
		<tr>
			<td align="center"> Phone </td>
		</tr>
		<%
			for(Phone p : phones)
			{
				sphone += p.number + "\n";
			}
		%>
		<tr><td align="center"><%=sphone %></td></tr>
		<tr><td align="center"> Email </td></tr>
		<%
			
			for(Email e : emails)
			{
				semail += e.eID + "@" + e.eServer +"\n";
			}
		%>
		<tr><td align="center"><%=semail %></td></tr>
	</table>
			
			<form method=post action="modify.jsp?id=<%=id %>">
				<input type="submit" value="수정">
				<input type="button" value="확인" onclick="window.close();">
			</form>
			
			<%
			

		}
		}
			else
			{
				%>
					<script type="text/javascript">
						needlogin('info.jsp?id=<%=id%>');
					</script>
				<%
			}
	}
	catch(SQLException ex)
	{
		
	}
	%>
	</center>
</body>
</html>