<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="addressbook.*"%>
    <%@ page import="java.text.SimpleDateFormat"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<script src="collection.js"></script>
<body>
<center>
<%
// 메세지 리스트를 보여주는 JSP 입니다.
	SimpleDateFormat dformat=new SimpleDateFormat("YYYY-MM-dd a hh:mm");
	request.setCharacterEncoding("UTF-8");
	UserDB usdb = new UserDB(session);
		int startpage = Integer.valueOf(request.getParameter("page"));
	
	if(usdb.getKey()<0)
	{
		String url = HttpUtils.getRequestURL(request).toString();
		url = url.substring(url.indexOf('/')+1);
        			//error page
	    %>
    	<script type="text/javascript">
    	alert('로그인하세요');
   		location.href = "login.jsp?page=<%=url%>";
    	</script>		
    	<%
	}
	else
	{
		boolean b_smsto,b_smsfrom,b_callto,b_callfrom;
		String smsto=request.getParameter("smsto");
		if(smsto==null) smsto="off";
		String smsfrom = request.getParameter("smsfrom");
		if(smsfrom==null) smsfrom = "off";
		String callto=request.getParameter("callto");
		if(callto==null) callto="off";
		String callfrom = request.getParameter("callfrom");
		if(callfrom==null) callfrom = "off";
		b_smsto = smsto.equals("on"); b_smsfrom = smsfrom.equals("on"); b_callto=callto.equals("on"); b_callfrom = callfrom.equals("on");
		
		Message[] datas=null;
		String cnt = request.getParameter("count");
		int count=15;
		if(cnt != null) count=Integer.valueOf(cnt);
		if(startpage<1) startpage=1;
		int cntpage=0;
		cntpage = usdb.getCountOfMSG(b_smsto,b_smsfrom,b_callto,b_callfrom);
		if(cntpage%count==0 && cntpage>0) cntpage/=count;
		else cntpage = (cntpage/count)+1;
		if(startpage > cntpage) startpage=cntpage;
		datas = usdb.loadMSG((startpage-1)*count,count,b_smsto,b_smsfrom,b_callto,b_callfrom);

		%>
		<form method=post action="svMessageManager?mode=delete&page=cmulog.jsp?page=<%=startpage%>">
		<input type="hidden" name="smsto" value="<%=smsto %>">
		<input type="hidden" name="smsfrom" value="<%=smsfrom %>">
		<input type="hidden" name="callto" value="<%=callto %>">
		<input type="hidden" name="callfrom" value="<%=callfrom %>">
		
		<table border=1>
		<tr height=15>
		
			<td align="center"><input type="checkbox" id="allchk" onclick="allclick(this.form.chkbox,this.form.allchk.checked)"> </td>
			<td align="center">TYPE</td>
			<td align="center">FROM/TO</td>
			<td align="center">NUMBER</td>
			<td align="center">WHO</td>
			<td align="center">BODY</td>
			<td align="center">DATE</td>
		</tr>
		<%
		if(datas!=null){
			for(int i=0;i<count;i++)
			{
				Address ads;
				if(datas[i]!=null){
					%>
					
				<tr height=15>
					<td align="center"><input type="checkbox" id="chkbox" name="<%=datas[i].id%>"></td>
					<td align="center"><%=datas[i].getType()%></td>
					<%
						if(datas[i].from.equals(usdb.number)){
							ads = usdb.load(datas[i].to);
							%>
							<td align="center">TO</td>
							<td align="center"><a href="javascript:popup('sendmsg.jsp?close=yes&sendto=<%=datas[i].to%>');"><%=datas[i].to%></a></td>
					
							<%
						}
						else{
							ads = usdb.load(datas[i].from);
							%>
							<td align="center">FROM</td>
							<td align="center"><a href="javascript:popup('sendmsg.jsp?close=yes&sendto=<%=datas[i].from%>');"><%=datas[i].from%></a></td>
					
							<%
						}
				%>
					<td align="center">
						<%if(ads!=null){%>
							<a href="javascript:popup('info.jsp?id=<%=ads.id %>');"><%=ads.name %></a>
						<%} else { %>
						<pre>?</pre>
						<%} %>
					</td>
					<td align="center" width=300><a href="javascript:popup('msginfo.jsp?id=<%=datas[i].id%>');"><%=datas[i].getBody(20)%></a></td>
					<td align="center"><%=dformat.format(datas[i])%></td>
				</tr>
				<%
				}
			}
		}%>
		</table>
		<input type="button" onclick="popup('sendmsg.jsp?close=yes').focus();" value="메세지 보내기">
		<input type="submit" value="메세지 삭제"><hr>
		<input type="button" onclick="goPage('cmulog.jsp?page=<%=startpage-1 %>');" <% if(startpage<=1) { %> disabled="disabled" <%} %> value="<"> <%=startpage + " / " + cntpage %> <input type="button" onclick="goPage('cmulog.jsp?page=<%=startpage+1 %>');" <% if(startpage>=cntpage) { %> disabled="disabled" <%} %> value=">">
		</form>
		<form method=post action="cmulog.jsp?page=<%=startpage%>">
			<input id="allchk" type="checkbox" onclick="allclick(this.form.chkbox,this.form.allchk.checked); this.form.submit();" <%if(b_callfrom&&b_callto&&b_smsto&&b_smsfrom){ %>checked="checked" <%} %>>ALL
			<input id="chkbox" type="checkbox" name="smsto" onclick="this.form.submit();" <%if(b_smsto){ %>checked="checked" <%} %>> SMS(TO)
			<input id="chkbox" type="checkbox" name="smsfrom" onclick="this.form.submit();" <%if(b_smsfrom){ %>checked="checked" <%} %>> SMS(FROM)
			<input id="chkbox" type="checkbox" name="callto" onclick="this.form.submit();" <%if(b_callto){ %>checked="checked" <%} %>> CALL(TO)
			<input id="chkbox" type="checkbox" name="callfrom" onclick="this.form.submit();" <%if(b_callfrom){ %>checked="checked" <%} %>> CALL(FROM)
		</form>
	<%	
	}
%>
</center>
</body>
</html>