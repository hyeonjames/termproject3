<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<%
// �α׾ƿ� �ϴ� JSP�������Դϴ�.
	session.removeAttribute("usid");
	session.removeAttribute("uspwd");
%>
</head>
<body>
	<script type="text/javascript">
	alert('�α׾ƿ� �Ǿ����ϴ�.');
	location.href="login.jsp";
	parent.menu.location.reload();
	</script>
</body>
</html>