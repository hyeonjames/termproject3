<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="addressbook.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script src="collection.js"> </script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Join the site</title>
</head>
<body>
<center>
<%
// 회원가입을 담당하는 JSP페이지 입니다.
	request.setCharacterEncoding("UTF-8");
	
	boolean error=false;
	String id = request.getParameter("id");
	String pwd = request.getParameter("pwd");
	String number = request.getParameter("number");
	String gopage = request.getParameter("page");
	if(gopage==null) gopage="main.jsp";
	if(id!=null&&pwd!=null&&number!=null)
	{
		if(id.length() <5)
		{
			%>
			<script type="text/javascript">
			alert('아이디는 5글자 이상으로 해주세요.');
			</script>
			<%
			error = true;
		}
		else if(pwd.length() < 8)
		{

			%>
			<script type="text/javascript">
			alert('비밀번호는 8글자 이상으로 해주세요.');
			</script>
			<%
			error = true;
		}
		else if(number.length() < 5)
		{
			%>
			<script type="text/javascript">
			alert('전화번호는 5글자 이상으로 해주세요.');
			</script>
			<%
			error = true;
		}
		else
		{
			for(char k : id.toCharArray())
			{
				if(!((k>='0'&&k<='9')||(k>='a'&&k<='z')||(k>='A'&&k<='Z')))
				{

					%>
					<script type="text/javascript">
					alert('아이디에는 특수문자나 한글이 들어가면 안됩니다.');
					</script>
					<%
					error = true;
					break;
				}
			}
			if(!error)
			{
				for(char k : number.toCharArray())
				{
					if(!((k>='0'&&k<='9')))
					{

						%>
						<script type="text/javascript">
						alert('전화번호는 숫자로만');
						</script>
						<%
						error = true;
						break;
					}
				}
			}
		}
	} else error=true;
	if(!error)
	{
		UserDB usdb = new UserDB();
		if(usdb.existID(id))
		{
			%>
			<script type="text/javascript">
			alert('이미 존재하는 아이디입니다.');
			</script>
			<%
			error =true;
		}
		else if(usdb.existNum(number))
		{
			%>
			<script type="text/javascript">
			alert('이미 존재하는 번호입니다.');
			</script>
			<%
			error = true;
		}
		else
		{
			usdb.join(session, id, pwd, number);
			%>
			<script type="text/javascript">
			alert('가입을 축하합니다');
			goPage('<%=gopage%>');
			parent.menu.location.reload();
		
			</script>
			<%
		}
	}
	if(error)
	{
		if(id==null) id="";
		if(number==null) number="";
		%>
		<form method=post action="join.jsp?page=<%=gopage %>">
			<pre>ID:	</pre><input type="text" name="id" value="<%=id %>"><br>
			<pre>PWD:	</pre><input type="password" name="pwd"><br>
			<pre>NUMBER:</pre><input type="text" name="number" value="<%=number %>"><br>
			<input type="submit" value="가입">
			<input type="button" value="취소" onclick="javascript:goPage('<%=gopage%>')">
		</form>
		<%
	}
%>
</center>

</body>
</html>