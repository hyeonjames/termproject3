<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="addressbook.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Send Message</title>
</head>
<body>
<script src="collection.jsp"></script>
<%
// SMS or CALL을 보낼수 있는 JSP입니다.
	String mode = request.getParameter("mode");
	if(mode==null) mode="sms";
	UserDB usdb = new UserDB(session);
	request.setCharacterEncoding("UTF-8");
	String tonum=request.getParameter("sendto");
	if(tonum==null) tonum="";
	String gopage=request.getParameter("page");
	if(gopage==null) gopage="cmulog.jsp?page=1";
	String close = request.getParameter("close");
	if(close==null) close="no";
	if(usdb.getKey()<0)
	{
		%>
			<script type="text/javascript">
			needlogin('sendmsg.jsp?sendto=<%=tonum%>&page=<%=gopage%>&close=<%=close%>');
			</script>
		
		<%
	}
	else{
	
%>
<script type="text/javascript">
	function change_type()
	{
		document.form1.body.disabled = !document.form1.msgtype[0].checked;
		
	}
	
</script>
<center>
	<form name="form1" method=post action="svMessageManager?mode=send&close=<%=close%>&type=<%=1%>&page=<%=gopage%>">
	<table width=400 height=500 border=1>
	<tr height=15>
	<td align="center" colspan=2>
		
		<input type="radio" name="msgtype" onclick="change_type();" value="0" <%if(mode.equals("sms")) { %>checked="checked" <%} %>>문자
		
		<input type="radio" name="msgtype" onclick="change_type();" value="1" <%if(mode.equals("call")) { %>checked="checked" <%} %>>전화
	</td>
	
	</tr>
	<tr height=15>
	<td align="center" width=40>번호:</td>
	<td align="left"><input type="text" name="sendto" value="<%=tonum%>"></td>
	</tr>
	<tr height=15>
	<td align="center" colspan=2>내용</td>
	</tr>
	<tr>
	<td align="center" colspan=2><textarea name="body" cols = "60" rows="30" <%if(!mode.equals("sms")){%> disabled="disabled" <%} %>></textarea></td>
	</tr>
	</table>
	<input type="submit" value="보내기">
	</form>
	<%
	}
	%>
	</center>
</body>
</html>