<%@page import="java.sql.SQLException"%>
<%@page import="addressbook.*" %>
<%@page import="java.text.SimpleDateFormat" %>
<%@page import="java.util.ArrayList" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<script src="collection.js">

	

</script>
    <%
    // 주소록 리스트를 보여주는 JSP페이지 입니다.
	final SimpleDateFormat dformat=new SimpleDateFormat("YYYY-MM-dd");
	request.setCharacterEncoding("UTF-8");
	String search = request.getParameter("search");
	if(search==null)  search="";
    	int length=0;
    	int startpage=0;
    	String sc = request.getParameter("count");
    	int count=15;
    	if(sc!=null) count = Integer.valueOf(sc);
    	int cntpage=0;
        	Address[] list=null;
        	try{
        	startpage = Integer.valueOf(request.getParameter("page"));
        	} catch(Exception e){
        	startpage=1;
        	}
        	// Database Read
        	try{
        	
        		UserDB dbhelp = new UserDB(session);
        		if(dbhelp.getKey() < 0)
        		{
        			//error page
    %>
    			<script type="text/javascript">
    			needlogin('login.jsp?page=list.jsp?page=<%=startpage%>');
    			</script>
    			
    			<%
    			}
    			else 
    			{
    				cntpage = dbhelp.getCountOfAddresses(search);
    				if(cntpage%count == 0) cntpage/=count;
    				else cntpage = (cntpage/count)+1;
    				if(cntpage==0) cntpage=1;
    				if(startpage>0 && startpage <= cntpage)
    				{
    				
    					list = dbhelp.load((startpage-1)*count,count,search);
    				
    				}
    			}
    		
    	}
    	catch(SQLException ex)
    	{
    		
    	}
    	
    %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<style type="text/css">
	table{background-color:#CED; font-size:13px; border:1;}
</style>
</head>

<body>
<center>
	<%
	if( list != null){%>
	<form method="post" name="form1" action="svAddressManager?mode=delete&msg=yes">
	<input type="hidden" name="page" value="list.jsp?page=<%=startpage%>">
	<table width=300 >
	<tr height="20"> 
		<td><input type="checkbox" id="allchk" onclick="this.value=allclick(this.form.chkbox,this.form.allchk.checked)"> </td>
		<td align="center" colspan="3">이름</td>
	</tr>
	<%
			for(int i=0;i<list.length;i++)
			{
				if(list[i]!=null){
				%>
				<tr height=15>
				<td align="center"><input id="chkbox" type="checkbox" name="<%=list[i].id %>"> </td>
				<td align="center"><a href="javascript:popup('info.jsp?id=<%=list[i].id%>','Information').focus()"><%=list[i].name%></a> </td>
				<td align="center"><a href="javascript:popup('modify.jsp?id=<%=list[i].id%>&close=yes','modify').focus();location.reload();">수정</a></td>
				<td align="center"><a href="svAddressManager?mode=delete&size=1&<%=list[i].id %>=on&msg=no?page=list.jsp?page=<%=startpage%>">삭제</a></td>
				<td align="center"><%=dformat.format(list[i].time) %></td>
				</tr>
				<%
				}
				else
				{
					%>
					<tr height=15>
					<td align="center"></td>
					<td align="center"></td>
					<td align="center"></td>
					<td align="center"></td>
					<td align="center"></td>
					</tr>
					<%
				}
			}
	%>
	
	</table>
	<input type="hidden" name="size" value="<%=length%>">
	<input type="button" value="추가" onclick="popup('modify.jsp?close=yes','add').focus();location.reload();">
	<input type="submit" value="삭제"><hr>
	<input type="button" onclick="goPage('list.jsp?page=<%=startpage-1 %>');" <% if(startpage<=1) { %> disabled="disabled" <%} %> value="<"> <%=startpage + " / " + cntpage %> <input type="button" onclick="goPage('list.jsp?page=<%=startpage+1 %>');" <% if(startpage>=cntpage) { %> disabled="disabled" <%} %> value=">">
	</form>
	<form method=post action="list.jsp?page=<%=startpage%>">
		<input type="text" name="search" value=<%=search %>>
		<input type="submit" value="검색">
	</form>
	<%} %>
</center>


</body>
</html>