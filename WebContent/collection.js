function error(str)
{
	alert(str);
	history.back();
}
function needlogin(str)
{
	alert('로그인 하세여');
	location.href = "login.jsp?page=" + str;
	parent.menu.location.reload();
}
function goPage(str)
{
	location.href=str;
}
function popup(str,title)
{
	return window.open(str,title,'width=480 height=640 left=200 top=300 scrollbars=yes resizable=no');
	
}
function goPage(str)
{
	location.href=str;
}


function allclick(field,chk)
{
	for(var i=0;i<field.length;i++)
	{
		field[i].checked = chk;
	}

}