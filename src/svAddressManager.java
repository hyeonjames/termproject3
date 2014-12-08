import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Enumeration;
import addressbook.*;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import addressbook.Address;
import addressbook.Email;
import addressbook.Phone;
import addressbook.UserDB;

/**
 * Servlet implementation class svDeleteAddress
 */
@WebServlet(description = "delete address servlet", urlPatterns = { "/svAddressManager" })
public class svAddressManager extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public svAddressManager() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

	}

	/**
	 * @throws SQLException
	 * @throws IOException
	 * @throws NamingException
	 * @throws ClassNotFoundException
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	
	protected void doUpdate(HttpServletRequest request,
			HttpServletResponse response) throws SQLException, IOException,
			ClassNotFoundException, NamingException {
		String realFolder = "";
		String file = "";
		String id = null;
		String name = "";
		String emails = "";
		String phones = "";
		int maxSize = 1024 * 1024 * 5;
		
		request.setCharacterEncoding("UTF-8");
		String encType = "utf-8";
		String savefile = "img";
		ServletContext scontext = getServletContext();
		realFolder = scontext.getRealPath(savefile);
		PrintWriter out = response.getWriter();
		UserDB dbhelp = new UserDB(request.getSession());
		if (dbhelp.getKey() >= 0) {

		try {
			MultipartRequest multi = new MultipartRequest(request, realFolder,
					maxSize, encType, new DefaultFileRenamePolicy());

			id = multi.getParameter("id");
			name = (String) multi.getParameter("name");
			String close = request.getParameter("close");
			emails = (String) multi.getParameter("emails");

			phones = (String) multi.getParameter("phones");

			String[] arr_phone = phones.split("\n");
			String[] arr_email = emails.split("\n");
			out.println("<script src=\"collection.js\"></script>");
			out.println("<script language=\"javascript\">");
			

			out.println("function message(str,id){");
			out.println("alert(str);");
			if (close != null && close.equals("yes")) {

				out.println("opener.location.reload(false);");
				out.println("window.close();");
			} else {

				out.println("location.href='info.jsp?id='+id");
			}
			out.println("}");
			out.println("</script>");
			Enumeration<?> files = multi.getFileNames();
			String file1 = (String) files.nextElement();
			file = multi.getFilesystemName(file1);
			int _id;
			if (id == null) {
				dbhelp.insert(new Address(0, name, file));
				_id = dbhelp.getLastID();

			} else {
				_id = Integer.valueOf(id);

				Address ads = new Address(_id, name, file);
				dbhelp.update(ads);
				dbhelp.deleteAllEmails(_id);
				dbhelp.deleteAllPhones(_id);

			}
			for (String p : arr_phone) {
				p = p.trim();
				if (!p.equals("") && Phone.chkPhone(p))
					dbhelp.insert(new Phone(p.trim(), _id, 0));
			}
			for (String e : arr_email) {
				if (e.length() == 0)
					continue;
				Email email = new Email(_id, 0);
				if (email.setEmail(e.trim()))
					dbhelp.insert(email);
			}
			if(id==null) 
				out.println("<script language=\"javascript\"> message('success insert.',"
						+ id + ");</script>");
			else
				out.println("<script language=\"javascript\"> message('success modify.',"
						+ id + ");</script>");
		} catch (Exception e) {
			e.printStackTrace();
		}
		}
	}

	protected void doDelete(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String gopage = request.getParameter("page");
		if (gopage == null)
			gopage = "list.jsp?page=1";
		UserDB dbhelp = null;
		try {
			dbhelp = new UserDB(request.getSession());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (dbhelp.getKey() >= 0) {
			String msg = request.getParameter("msg");
			Enumeration<String> names = request.getParameterNames();

			while (names.hasMoreElements()) {
				String key = names.nextElement();
				try{
					int _id = Integer.valueOf(key);
					String val = request.getParameter(key);
					if (val.equals("on")) {
						try {
							dbhelp.deleteAllEmails(_id);
							dbhelp.deleteAllPhones(_id);
							dbhelp.deleteAddress(_id);
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
			PrintWriter out = response.getWriter();
			out.println("<script type=\"text/javascript\">");
			if (msg == null || msg == "yes") {
				out.println("alert('삭제되었습니다');");
			} else {
				out.println("location.href=\"" + gopage + "\";");

			}
			out.println("</script>");
			out.flush();
		}
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		request.setCharacterEncoding("UTF-8");
		String mode = request.getParameter("mode");
		if (mode.equals("delete"))
			doDelete(request, response);
		else if (mode.equals("update") || mode==null) {
			try {
				doUpdate(request, response);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NamingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
