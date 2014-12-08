package addressbook;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import javax.naming.NamingException;
import javax.servlet.http.*;
import java.sql.Statement;
public class UserDB { // 유저 정보, 그에 대한 데이터 베이스 담당
	Connection myConn;
	public static final String dbid="root"; // 데이터 베이스 아이디
	public static final String dbpwd="qoohk6830"; //데이터베이스 비밀번호
	public static final String table="users";
	public static final String r_key="_key";
	public static final String r_uskey="userkey";
	public static final String r_lastlg="lastlogin";
	public static final String r_id = "id";
	public static final String r_pwd = "pwd";
	public static final String r_num = "number";
	public static final String elements=r_id + "," + r_pwd + "," +r_num +"," + r_lastlg;
	public static final String _elements="?,?,?,?";
	private int uskey;
	public String id,number;
	public final static String encryption(String pw,String key) throws NoSuchAlgorithmException
	{
		  MessageDigest md = MessageDigest.getInstance("SHA-256");
		  String password = key+pw;
		  byte[] mdResult = md.digest(password.getBytes());
		  sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
		  return encoder.encode(mdResult);
	}
	
	public void getUserData(HttpSession req,String id,String pwd) throws ClassNotFoundException, SQLException, NamingException, NoSuchAlgorithmException
	{
		this.id = id;
		uskey = getUserLogin(req,id, pwd);
	}
	public int getKey()
	{
		return uskey;
	}
	public void getUserData(HttpSession datas) throws ClassNotFoundException, SQLException, NamingException
	{
		id = (String) datas.getAttribute("usid");
		String pwd = (String) datas.getAttribute("uspwd");
		if(pwd == null || id == null)
		{
			uskey=-1;
			return;
		}
		uskey=getUserData(id, pwd);
		
	}
	public UserDB(HttpSession request) throws ClassNotFoundException, SQLException, NamingException
	{		
		this();
		getUserData(request);
	}
	public UserDB(HttpSession req,String id,String pwd) throws NamingException, NoSuchAlgorithmException, SQLException, ClassNotFoundException
	{
		this();
		uskey=getUserLogin(req,id,pwd);
	}
	public UserDB() throws SQLException, NamingException, ClassNotFoundException
	{
	    Class.forName("com.mysql.jdbc.Driver");
		myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/addressbook?useUnicode=true&characterEncoding=utf8",dbid,dbpwd);
		Statement smt = myConn.createStatement();
		smt.execute("SET CHARACTER SET utf8");
		smt.execute("set names utf8");
		smt.close();
		
		uskey=-3;
	}
	public boolean existID(String id) throws SQLException
	{
		ResultSet result;
		boolean ret=false;
		PreparedStatement pstmt = myConn.prepareStatement("select null from "+table + " where " + r_id + "=?");
		pstmt.setString(1, id);
		result = pstmt.executeQuery();
		ret = result.first();
		result.close();
		pstmt.close();
		return ret;
	}
	public void join(HttpSession request,String id,String pwd,String num) throws SQLException, NoSuchAlgorithmException{
		long date = new Date().getTime();
		String crypted=encryption(pwd,String.valueOf(date));
		PreparedStatement pstmt = myConn.prepareStatement("insert into " + table + " (" + elements+ ") values (" + _elements+")");
		pstmt.setString(1, id);
		pstmt.setString(2, crypted);
		pstmt.setString(3, num);
		pstmt.setLong(4, date);
		pstmt.executeUpdate();
		pstmt.close();
		request.setAttribute("usid",id);
		request.setAttribute("uspwd",crypted);
	}
	public boolean existNum(String number) throws SQLException
	{
		ResultSet result;
		boolean ret=false;
		PreparedStatement pstmt = myConn.prepareStatement("select null from "+table + " where " + r_num + "=?");
		pstmt.setString(1, number);
		result = pstmt.executeQuery();
		ret = result.first();
		result.close();
		pstmt.close();
		return ret;
	}
	public int insert(Address add) throws SQLException
	{
		int ret = -1;
		PreparedStatement pstmt = null;
		
		pstmt=myConn.prepareStatement("insert into " + Address.table + "(" + Address.elements + ") values(" + Address._elements + ")");
		add.setStatement(pstmt,uskey);
		ret = pstmt.executeUpdate();
		
		pstmt.close();
		return ret;
	}
	public Address load(int id) throws SQLException
	{
		Address ret=null;
		PreparedStatement pstmt = myConn.prepareStatement("select * from " + Address.table + " where " + Address.r_id + " = ? AND " + Address.r_userkey + "=?");
		pstmt.setInt(1, id);
		pstmt.setInt(2, uskey);
		ResultSet set = pstmt.executeQuery();
		if(set.first()) ret=new Address(set);
		
		pstmt.close();
		return ret;
	}
	public ArrayList<Phone> loadPhones(int id) throws SQLException
	{
		ArrayList<Phone> ret = new ArrayList<Phone>();
		PreparedStatement pstmt = myConn.prepareStatement("select * from " + Phone.table + " where " + Phone.r_personid + " = ? AND " + Phone.r_userkey + "=?");
		pstmt.setInt(1, id);
		pstmt.setInt(2,uskey);
		ResultSet set = pstmt.executeQuery();
		while(set.next())
		{
			ret.add(new Phone(set));
		}
		pstmt.close();
		return ret;
	}
	public ArrayList<Email> loadEmails(int id) throws SQLException
	{
		ArrayList<Email> ret = new ArrayList<Email>();
		PreparedStatement pstmt = myConn.prepareStatement("select * from " + Email.table + " where " + Email.r_personid + " = ?");
		pstmt.setInt(1, id);
		ResultSet set = pstmt.executeQuery();
		while(set.next())
		{
			ret.add(new Email(set));
		}
		pstmt.close();
		return ret;
	}
	public int insert(Phone p) throws SQLException
	{
		int ret = -1;
		PreparedStatement pstmt = null;
		pstmt=myConn.prepareStatement("insert into " + Phone.table + "(" + Phone.elements + ") values(" + Phone._elements + ")");
		p.setStatement(pstmt,uskey);
		ret = pstmt.executeUpdate();
		pstmt.close();
		return ret;
	}
	public int getLastID() throws SQLException
	{
		PreparedStatement pstmt = myConn.prepareStatement("select " + Address.r_id + " from " + Address.table + " order by " + Address.r_date + " desc limit 1");
		ResultSet set = pstmt.executeQuery();
		int ret  = -1;
		if(set.first()) ret = set.getInt(1);

		pstmt.close();
		return ret;
	}
	public int deleteAllEmails(int id) throws SQLException
	{
		PreparedStatement pstmt = myConn.prepareStatement("delete from " + Email.table + " where " + Email.r_personid + " =? AND " + Email.r_userkey + "=?");
		pstmt.setInt(1, id);
		pstmt.setInt(2, uskey);
		int ret = pstmt.executeUpdate();
		pstmt.close();
		return ret;
	}
	public int deleteAllPhones(int id) throws SQLException
	{
		PreparedStatement pstmt = myConn.prepareStatement("delete from " + Phone.table + " where " + Phone.r_personid + " =? AND " + Phone.r_userkey + "=?");
		pstmt.setInt(1, id);
		pstmt.setInt(2, uskey);
		int ret = pstmt.executeUpdate();
		pstmt.close();
		return ret;
	}
	public int update(Address ads) throws SQLException, UnsupportedEncodingException
	{
		return ads.update(myConn,uskey);
	}
	public int insert(Message msg) throws SQLException
	{
		int ret = -1;
		PreparedStatement pstmt = null;
		pstmt=myConn.prepareStatement("insert into " + Message.table + "(" + Message.elements + ") values(" + Message._elements + ")");
		msg.setStatement(pstmt);
		ret = pstmt.executeUpdate();
		pstmt.close();
		return ret;
	}
	public int insert(Email e) throws SQLException
	{
		int ret = -1;
		PreparedStatement pstmt = null;
		pstmt=myConn.prepareStatement("insert into " + Email.table + "(" + Email.elements + ") values(" + Email._elements + ")");
		e.setStatement(pstmt,uskey);
		ret = pstmt.executeUpdate();
		pstmt.close();
		return ret;
	}
	public int deleteAddress(int id) throws SQLException
	{
		int ret = -1;
		PreparedStatement pstmt = null;
		pstmt=myConn.prepareStatement("delete from " + Address.table + " where " + Address.r_id + "=? AND " + Address.r_userkey + "=?");
		pstmt.setInt(1, id);
		pstmt.setInt(2, uskey);
		ret = pstmt.executeUpdate();
		pstmt.close();
		return ret;	
	}
	public int getUserLogin(HttpSession req,String id,String pwd) throws SQLException, NoSuchAlgorithmException
	{
		long lastlg;
		ResultSet ret;
		int key=-1;
		PreparedStatement pstmt = myConn.prepareStatement("select " + r_pwd + ","+ r_lastlg + "," + r_num + "," + r_key +  " from " + table + " where " + r_id + "=?");
		pstmt.setString(1, id);
		ret = pstmt.executeQuery();
		if(ret.first())
		{
			String cryption = ret.getString(1);
			lastlg = ret.getLong(2);
			String crypted = encryption(pwd, String.valueOf(lastlg));
			if(cryption.equals(crypted))
			{
				number = ret.getString(3);
				key = ret.getInt(4);
				PreparedStatement pstmt2 = myConn.prepareStatement("update " + table + " set " + r_lastlg + " =? , " + r_pwd + "=?  where " + r_key + "=?");
				pstmt2.setLong(1, lastlg=new Date().getTime());
				crypted = encryption(pwd, String.valueOf(lastlg));
				pstmt2.setString(2, crypted);
				pstmt2.setInt(3,key);
				pstmt2.executeUpdate();
				req.setAttribute("usid", id);
				req.setAttribute("uspwd", crypted);
			}
		}
		else key = -2;
		ret.close();
		pstmt.close();
		
		return key;
		
	}
	public int getCountOfAddresses(String search) throws SQLException
	{
		PreparedStatement pstmt = myConn.prepareStatement("select null from " + Address.table + " where " + Address.r_userkey + "=? AND " + Address.r_name + " LIKE ?");
		pstmt.setInt(1, uskey);
		pstmt.setString(2, "%" + search + "%");
		ResultSet set = pstmt.executeQuery();
		
		if(set.last()) return set.getRow();
		return 0;
		
	}
	public int getCountOfMSG(int type) throws SQLException
	{
		PreparedStatement pstmt = myConn.prepareStatement("select null from " + Message.table + " where ((" + Message.r_from + "=? AND " + Message.r_deleted+"&1 = 0 ) OR (" + Message.r_to + "=? AND "  + Message.r_deleted+"&2 = 0)) AND " + Message.r_type + "=?");
		pstmt.setString(1,number);
		pstmt.setString(2, number);
		pstmt.setInt(3, type);
		ResultSet set = pstmt.executeQuery();
		
		if(set.last()) return set.getRow();
		return 0;
	}
	public String getNameFromNumber(String num) throws SQLException
	{
		String ret=null;
		int pskey=0;
		PreparedStatement pstmt = myConn.prepareStatement("select " + Phone.r_personid + " from " + Phone.table + " where " + Phone.r_userkey + "=? AND " + Phone.r_number + "=?");
		pstmt.setInt(1, uskey);
		pstmt.setString(2, num);
		ResultSet set = pstmt.executeQuery();
		if(set.first())
		{
			pskey = set.getInt(1);
			PreparedStatement pstmt2 = myConn.prepareStatement("select " + Address.r_name + " from " + Address.table + " where " +  Address.r_id + "=? AND " + Address.r_userkey + "=?");
			pstmt2.setInt(1,pskey);
			pstmt2.setInt(2, uskey);
			ResultSet set2=pstmt2.executeQuery();
			if(set2.first()) ret = set2.getString(1);
			set2.close();
			pstmt2.close();
		}
		set.close();
		pstmt.close();
		return ret;
	}
	public String getNameFromEmail(String mail) throws SQLException
	{
		String ret=null;
		int pskey=0;
		PreparedStatement pstmt = myConn.prepareStatement("select " + Email.r_personid + " from " + Email.table + " where " + Email.r_userkey + "=? AND " + Email.r_address + "=?");
		pstmt.setInt(1, uskey);
		pstmt.setString(2, mail);
		ResultSet set = pstmt.executeQuery();
		if(set.first())
		{
			pskey = set.getInt(1);
			PreparedStatement pstmt2 = myConn.prepareStatement("select " + Address.r_name + " from " + Address.table + " where " +  Address.r_id + "=? AND " + Address.r_userkey + "=?");
			pstmt2.setInt(1,pskey);
			pstmt2.setInt(2, uskey);
			ResultSet set2=pstmt2.executeQuery();
			if(set2.first()) ret = set2.getString(1);
			set2.close();
			pstmt2.close();
		}
		set.close();
		pstmt.close();
		return ret;
	}
	public Message[] loadMSG(int type,int start,int count) throws SQLException
	{
		int i=0;
		Message[] ret = new Message[count];
		PreparedStatement pstmt = myConn.prepareStatement("select * from " + Message.table + " where ((" + Message.r_from + "=? AND " + Message.r_deleted+"&1 = 0 ) OR (" + Message.r_to + "=? AND "  + Message.r_deleted+"&2 = 0)) AND " + Message.r_type + "=?" + " order by " + Message.orderby+" limit " + start + "," + count);
		pstmt.setString(1, number);
		pstmt.setString(2, number);
		pstmt.setInt(3, type);
		ResultSet set = pstmt.executeQuery();
		while(set.next())
			ret[i++] = new Message(set);
		pstmt.close();
		return ret;
	}
	public Message[] loadMSG(int start,int count) throws SQLException
	{
		int i=0;
		Message[] ret = new Message[count];
		PreparedStatement pstmt = myConn.prepareStatement("select * from " + Message.table + " where (" + Message.r_from + "=? AND " + Message.r_deleted+"&1 = 0 ) OR (" + Message.r_to + "=? AND "  + Message.r_deleted+"&2 = 0) order by " + Message.orderby+" limit " + start + "," + count);
		pstmt.setString(1, number);
		pstmt.setString(2, number);
		ResultSet set = pstmt.executeQuery();
		while(set.next())
			ret[i++] = new Message(set);
		pstmt.close();
		return ret;
	}
	public Message loadMSG(int id) throws SQLException
	{
		int i=0;
		Message ret=null;
		PreparedStatement pstmt = myConn.prepareStatement("select * from " + Message.table + " where "  + Message.r_id + "=? AND (" + Message.r_from + "=? AND " + Message.r_deleted+"&1 = 0 ) OR (" + Message.r_to + "=? AND "  + Message.r_deleted+"&2 = 0) limit 1");
		pstmt.setInt(1,id);
		pstmt.setString(2, number);
		pstmt.setString(3, number);
		ResultSet set = pstmt.executeQuery();
		if(set.first()) ret = new Message(set);
		pstmt.close();
		return ret;
	}
	public int getCountOfMSG() throws SQLException
	{
		PreparedStatement pstmt = myConn.prepareStatement("select null from " + Message.table + " where (" + Message.r_from + "=? AND " + Message.r_deleted+"&1 = 0 ) OR (" + Message.r_to + "=? AND "  + Message.r_deleted+"&2 = 0)" );
		pstmt.setString(1,number);
		pstmt.setString(2, number);
		ResultSet set = pstmt.executeQuery();
		
		if(set.last()) return set.getRow();
		return 0;
	}
	public int getUserData(String id,String pwd) throws SQLException
	{
		int key=-1;
		PreparedStatement pstmt = myConn.prepareStatement("select " + r_key + "," + r_num + " from " + table + " where " + r_id + "=? AND " + r_pwd + "=?");
		pstmt.setString(1, id);
		pstmt.setString(2, pwd);
		ResultSet ret = pstmt.executeQuery();
		if(ret.first())
		{
			key = ret.getInt(1);
			number = ret.getString(2);
		}
		ret.close();
		pstmt.close();
		return key;
	}
	public int deleteMessage(int id) throws SQLException
	{
		PreparedStatement pstmt = myConn.prepareStatement("select null from " + Message.table +  " where " + Message.r_id + "=? AND " + Message.r_from + "=?");
		pstmt.setInt(1, id);
		pstmt.setString(2,number);
		ResultSet ret = pstmt.executeQuery();
		if(ret.first())
		{
			ret.close();
			pstmt.close();
			return myConn.prepareStatement("update " + Message.table + " set " + Message.r_deleted +"= " + Message.r_deleted + "|1 where " + Message.r_id + "=" + id).executeUpdate();
		}
		ret.close();
		pstmt.close();
		pstmt = myConn.prepareStatement("select null from " + Message.table +  " where " + Message.r_id + "=? AND " + Message.r_to + "=?");
		pstmt.setInt(1, id);
		pstmt.setString(2,number);
		ret = pstmt.executeQuery();
		if(ret.first())
		{
			ret.close();
			pstmt.close();
			return myConn.prepareStatement("update " + Message.table + " set " + Message.r_deleted +"= " + Message.r_deleted + "|2 where " + Message.r_id + "=" + id).executeUpdate();
		}
		ret.close();
		pstmt.close();
		
		return 0;
	}
	public Address load(String number) throws SQLException
	{
		PreparedStatement pstmt = myConn.prepareStatement("select " + Phone.r_personid +" from " + Phone.table + " where " + Phone.r_number + "=? AND " + Phone.r_userkey + "=?");
		
		pstmt.setString(1, number);
		pstmt.setInt(2,uskey);
		ResultSet set = pstmt.executeQuery();
		if(set.first())
		{
			int pid = set.getInt(1);
			set.close();
			pstmt.close();
			pstmt = myConn.prepareStatement("select * from " + Address.table + " where " + Address.r_id + "=?");
			pstmt.setInt(1,pid);
			set = pstmt.executeQuery();
			Address rsult=null;
			if(set.first()) rsult = new Address(set);
			set.close();
			pstmt.close();
			return rsult;
		}
		set.close();
		pstmt.close();
		return null;
	}
	public Address[] load(int start,int count) throws SQLException
	{
		int i=0;
		Address[] ret = new Address[count];
		PreparedStatement pstmt = myConn.prepareStatement("select * from " + Address.table + " where " + Address.r_userkey + "=?" + " order by " + Address.orderby+" limit " + start + "," + count);
		pstmt.setInt(1, uskey);
		ResultSet set = pstmt.executeQuery();
		while(set.next())
			ret[i++] = new Address(set);
		pstmt.close();
		return ret;
	}

	public Address[] load(int start,int count,String search) throws SQLException
	{
		int i=0;
		Address[] ret = new Address[count];

		PreparedStatement pstmt = myConn.prepareStatement("select * from " + Address.table + " where " + Address.r_userkey + "=? AND " + Address.r_name + " LIKE ? order by " + Address.orderby+" limit " + start + "," + count);
		pstmt.setInt(1, uskey);
		pstmt.setString(2, "%" + search + "%");
		ResultSet set = pstmt.executeQuery();
		while(set.next())
			ret[i++] = new Address(set);
		pstmt.close();
		return ret;
	}

	public int getCountOfMSG(boolean smsto,boolean smsfrom,boolean callto,boolean callfrom) throws SQLException
	{
		int ret=0;
		String sql = "select null from " + Message.table;
		if(smsto||smsfrom||callto||callfrom) sql += " where ";
		if(smsto){
			sql += "( " + Message.r_from + "='" + number + "' AND " + Message.r_type + "=0 AND " + Message.r_deleted + "& 1 = 0)";
			if(callfrom || callto || smsfrom) sql+=" OR ";
		}
		if(smsfrom){
			sql += "( " + Message.r_to + "='" + number + "' AND " + Message.r_type + "=0 AND "+ Message.r_deleted + "& 2 = 0)";
			if(callfrom || callto) sql+=" OR ";
		}
		if(callto){
			sql += "( " + Message.r_from + "='" + number + "' AND " + Message.r_type + "=1 AND "+Message.r_deleted + "& 1 = 0)";
			if(callfrom) sql+=" OR ";
		}
		if(callfrom){
			sql += "( " + Message.r_to + "='" + number + "' AND " + Message.r_type + "=1 AND " + Message.r_deleted + "& 2 = 0)";
		}
		
		PreparedStatement pstmt = myConn.prepareStatement(sql);
		ResultSet set=pstmt.executeQuery();
		if(set.last()) ret=set.getRow();
		set.close();
		pstmt.close();
		return ret;
	}
	public Message[] loadMSG(int start,int count,boolean smsto,boolean smsfrom,boolean callto,boolean callfrom) throws SQLException
	{
		int i=0;
		Message[] ret = new Message[count];
		String sql=null;
		if(smsto||smsfrom||callto||callfrom)
			sql = "select * from " + Message.table + " where ";
		else return ret;
		if(smsto){
			sql += "( " + Message.r_from + "='" + number + "' AND " + Message.r_type + "=0 AND " + Message.r_deleted + "& 1 = 0)";
			if(callfrom || callto || smsfrom) sql+=" OR ";
		}
		if(smsfrom){
			sql += "( " + Message.r_to + "='" + number + "' AND " + Message.r_type + "=0 AND "+ Message.r_deleted + "& 2 = 0)";
			if(callfrom || callto) sql+=" OR ";
		}
		if(callto){
			sql += "( " + Message.r_from + "='" + number + "' AND " + Message.r_type + "=1 AND "+Message.r_deleted + "& 1 = 0)";
			if(callfrom) sql+=" OR ";
		}
		if(callfrom){
			sql += "( " + Message.r_to + "='" + number + "' AND " + Message.r_type + "=1 AND " + Message.r_deleted + "& 2 = 0)";
		}
		
		sql += " order by " + Message.orderby + " limit " + start + "," + count;
		PreparedStatement pstmt = myConn.prepareStatement(sql);
		ResultSet set=pstmt.executeQuery();
		while(set.next())
			ret[i++] = new Message(set);
		set.close();
		pstmt.close();
		return ret;
		
	}
	
	public ArrayList<Address> loadAll() throws SQLException
	{
		ArrayList<Address> ret;
		PreparedStatement pstmt = myConn.prepareStatement("select * from " + Address.table + " where " + Address.r_userkey + "=?" + " order by " + Address.orderby);
		pstmt.setInt(1, uskey);
		ResultSet set = pstmt.executeQuery();
		ret = new ArrayList<Address>();
		while(set.next())
			ret.add(new Address(set));
		pstmt.close();
		return ret;
	}
	
	@Override
	public void finalize() throws Throwable
	{
		myConn.close();
		super.finalize();
	}
}
