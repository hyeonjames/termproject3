package addressbook;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Email { // 이메일 정보를 담는 클래스
	public static final String table="emails";
	public static final String r_userkey="userkey";
	public static final String r_address="address";
	public static final String r_personid="person_id";
	public static final String r_type="type";
	public static final String elements=r_personid+","+r_address+","+r_type+","+r_userkey;
	public static final String _elements="?,?,?,?";
	public String eID,eServer;
	int person_id,type,id;
	public Email(int pid,int type)
	{
		person_id = pid;
		this.type = type;
	}
	public static boolean chkEmail(String email)
	{
		return email.split("@").length == 2;
	}
	public boolean setStatement(PreparedStatement pstmt,int uskey) throws SQLException
	{
		if(eID==null || eServer == null) return false;
		pstmt.setInt(1, person_id);
		pstmt.setString(2, eID+"@"+eServer);
		pstmt.setInt(3, type);
		pstmt.setInt(4, uskey);
		return true;
	}
	public boolean setEmail(String email)
	{
		String[] arr_ads = email.split("@");
		if(arr_ads.length == 2)
		{
			eID = arr_ads[0].trim().replace("\r", "");
			eServer = arr_ads[1].trim().replace("\r", "");
			return true;	
		}
		return false;
	}
	public Email(ResultSet set) throws SQLException
	{
		setEmail(set.getString(set.findColumn(r_address)));
		this.person_id = set.getInt(set.findColumn(r_personid));
		this.type = set.getInt(set.findColumn(r_type));
	}
}
