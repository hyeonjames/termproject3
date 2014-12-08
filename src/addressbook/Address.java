package addressbook;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;


public class Address { // 주소록 정보를 담는 클래스
	public static final String table="person";
	public static final String r_userkey="userkey";
	public static final String r_id = "_id";
	public static final String r_date="dtime";
	public static final String r_name="name";
	public static final String r_photo="photofile";
	public static final String elements=r_name+","+r_photo + "," +r_date + "," + r_userkey;
	public static final String _elements="?,?,?,?";
	public static final String orderby="name asc";
	public String name;
	public Date time;
	public int id;
	public String photofile;
	public Address(int id,String name,String photo)
	{
		this.id = id;
		this.name = name;
		this.photofile = photo;
	}
	public int update(Connection myConn,int uskey) throws SQLException, UnsupportedEncodingException
	{
		int cnt=1;
		PreparedStatement pstmt = null;
		if(photofile==null){
			pstmt=myConn.prepareStatement("update " + table + " set " + r_name + "= ?" + " where " + r_id + "=? AND " + r_userkey + "=? limit 1");
		}
		else{
			pstmt=myConn.prepareStatement("update " + table + " set " + r_name + "= ?," + r_photo + "=? where " + r_id + "=? AND " + r_userkey + "=? limit 1");
		}

		//name = new String(name.getBytes(),"UTF-8");
		pstmt.setString(cnt++, name);
		if(photofile!=null) pstmt.setString(cnt++,photofile);
		pstmt.setInt(cnt++, id);
		pstmt.setInt(cnt, uskey);
		int ret = pstmt.executeUpdate();
		pstmt.close();
		return ret;
	}
	public Address(ResultSet set) throws SQLException
	{
		id = Integer.valueOf(set.getString(set.findColumn(r_id)));
		name = set.getString(set.findColumn(r_name));
		photofile = set.getString(set.findColumn(r_photo));
		time = new Date(set.getLong(set.findColumn(r_date)));
	}
	public void setStatement(PreparedStatement pstmt,int uskey) throws SQLException
	{
		
		pstmt.setString(1, name);
		pstmt.setString(2, photofile);

		pstmt.setLong(3, new Date().getTime());
		pstmt.setInt(4, uskey);
	}
}
