<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<%
// 로그아웃 하는 JSP페이지입니다.
	session.removeAttribute("usid");
	session.removeAttribute("uspwd");
%>
</head>
<body>
	<script type="text/javascript">
	alert('로그아웃 되었습니다.');
	location.href="login.jsp";
	parent.menu.location.reload();
	</script>
</body>
</html>