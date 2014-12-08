import addressbook.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Date;
import java.util.Enumeration;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class svMessageManager
 */
@WebServlet("/svMessageManager")
public class svMessageManager extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public svMessageManager() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		String mode = request.getParameter("mode");
		if(mode.equals("send"))
		{
			try {
				doSend(request,response);
			} catch (ClassNotFoundException | SQLException | NamingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(mode.equals("delete"))
		{
		}
	}
	protected void ErrorMessage(PrintWriter out,String msg)
	{
		out.println("<script type=\"text/javascript\">");
		out.println("error('" + msg + "');</script>");
		out.flush();
	}
	protected void JScript(PrintWriter out,String msg)
	{
		out.println("<script type=\"text/javascript\">");
		out.println(msg + "</script>");
		out.flush();
	}
	protected void doDelete(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		UserDB usdb=null;
		try {
			usdb = new UserDB(request.getSession());
			PrintWriter out = response.getWriter();
			String gopage = request.getParameter("page");
			String smsto = request.getParameter("smsto");
			String smsfrom = request.getParameter("smsfrom");
			String callto = request.getParameter("callto");
			String callfrom = request.getParameter("callfrom");
			if(gopage==null) gopage="cmulog.jsp?page=1";
			gopage += "&smsto=" + smsto  + "&smsfrom=" + smsfrom + "&callto=" + callto + "&callfrom=" + callfrom;
			out.println("<script src=\"collection.js\"></script>");
			if(usdb.getKey() <0)
			{
				ErrorMessage(out,"you need login");
			}
			else{
				Enumeration<String> names = request.getParameterNames();

				while (names.hasMoreElements()) {
					String key = names.nextElement();
					try{
						int _id = Integer.valueOf(key);
						String val = request.getParameter(key);
						if (val.equals("on")) {
							try {
								usdb.deleteMessage(_id);
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}catch (Exception e)
					{
						e.printStackTrace();
					}
				}
				JScript(out,"goPage('" + gopage + "');");
			}
		} catch (ClassNotFoundException | SQLException | NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	protected void doSend(HttpServletRequest request,HttpServletResponse response) throws ClassNotFoundException, SQLException, NamingException, IOException
	{
		UserDB usdb = new UserDB(request.getSession());
		PrintWriter out = response.getWriter();
		out.println("<script src=\"collection.js\"></script>");
		int type = Integer.valueOf(request.getParameter("msgtype"));
		String body=null;
		if(type==Message.SMS) body=request.getParameter("body");
		String gopage = request.getParameter("page");
		if(gopage==null) gopage="cmulog.jsp?page=1";
		String close = request.getParameter("close");
		if(close==null ) close="no";
		if(usdb.getKey() <0)
		{
			JScript(out,"alert('you need login'); window.close();");
		}
		else
		{
			String tonum = request.getParameter("sendto").trim();
			if(tonum.equals(usdb.number))
			{
				JScript(out,"alert('you can't send to you'); window.close();");
			}
			else if(!Phone.chkPhone(tonum))
			{
				JScript(out,"alert('not right number'); window.close();");
			}
			else{
				usdb.insert(new Message(null,new Date().getTime(),usdb.number,tonum,type,body));
				if(close.equals("no")) JScript(out,"alert('success send');goPage('" + gopage+"');");
				else if(close.equals("yes")) JScript(out,"alert('success send');opener.location.reload(false); window.close();");
			}
		}
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		String mode = request.getParameter("mode");
		if(mode.equals("send"))
		{
			try {
				doSend(request,response);
			} catch (ClassNotFoundException | SQLException | NamingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(mode.equals("delete"))
		{
			doDelete(request,response);
		}
	}

}
