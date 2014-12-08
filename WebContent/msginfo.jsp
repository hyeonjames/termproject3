<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="addressbook.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="collection.js"></script>
<%
// SMS 혹은 CALL의 정보를 보여주는 JSP입니다.
	int id = Integer.valueOf(request.getParameter("id"));
	Message msg=null;
	UserDB usdb = new UserDB(session);
	if(usdb.getKey()<0)
	{
		%>
			<script type="text/javascript">
				needlogin('msginfo.jsp?id=<%=id%>');
			</script>
		<%
	}
	else
	{
		msg = usdb.loadMSG(id);
		if(msg==null)
		{
			%>
				
			<script type="text/javascript">
				error('메세지 정보를 찾을 수 없습니다.');
			</script>
			<%
		}
%>
	
	<%} %>
<title>Message Information</title>
</head>
<body>
<center>
<%
	if(msg!=null)
	{
		%>
			<table border=1 width=400 height=500>
				<tr height=15>
					<%
						if(msg.from.equals(usdb.number))
						{
							%>
							<td align="center" width=8>To</td>
							<td align="center"><%=msg.to %></td>
							<%
						}
						else
						{
							%>
							<td align="center" width=16>From</td>
							<td align="center"><%=msg.from %></td>
							<%
						}
					%>
				</tr>
				<tr height=15>
				<td align="center" colspan=2>내 용</td>
				</tr>
				<tr>
				
				<td align="center" colspan=2>
					<textarea readonly="readonly" rows=30 cols=60><%=msg.getBody()%></textarea>
				</td>
				</tr>
			</table>
			<input type="button" value="확인" onclick="window.close();">
		<%
	}
%>
</center>
</body>
</html>