<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import ="java.util.*" %>
    <%@ page import ="addressbook.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<center>
<script src="collection.jsp"></script>
	<%
	// 주소록을 수정혹은 추가하는 JSP입니다.
	request.setCharacterEncoding("UTF-8");
	response.setCharacterEncoding("UTF-8");
	
		UserDB dbhelp = new UserDB(session);
			if(dbhelp.getKey() < 0)
			{
	%>
			<script type="text/javascript">
			needlogin('login.jsp?page=modify.jsp');
			</script>
			<%
			return ;
		}
		String close = request.getParameter("close");
		String id = request.getParameter("id");
		String name = "";
		String s_photo =null;
		String emails = "";
		String phones = "";
		if(id!=null)
		{
			ArrayList<Email> arremail;
			ArrayList<Phone> arrphone;
			if(dbhelp.getKey() >=0 ){
			Address ads = dbhelp.load(Integer.valueOf(id));
			name = ads.name;
			s_photo = ads.photofile;
			if(s_photo==null) s_photo="";
			arremail = dbhelp.loadEmails(ads.id);
			arrphone = dbhelp.loadPhones(ads.id);
			for(Email e : arremail)
			{
				emails += e.eID + "@" + e.eServer + "\n";
			}
			for(Phone p : arrphone)
			{
				phones += p.number + "\n";
			}
			}
		}
		
		
	%>
	<form method=post enctype="multipart/form-data" action="svAddressManager?mode=update&<%if(id!=null){%>id=<%=id%>&<%} %>close=<%=close%>">
		<div> 이름 : <input type="text" name="name" value="<%=name%>" maxlength=20></div>
		<div> 사진 : <input type="file" name="photo" value="<%=s_photo%>"></div>
		<div> Phone </div>
		<div> <textarea name="phones" rows=10 cols=50 ><%=phones %></textarea></div>
		<div> Email </div>
		<div> <textarea name="emails" rows=10 cols=50><%=emails %></textarea></div>
		<div> <input type="submit" value="확인"> </div>
	</form>
</center>
</body>
</html>