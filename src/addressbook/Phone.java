package addressbook;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class Phone { // 폰 번호 정보를 담는 클래스

	public static final String table="phones";
	public static final String r_userkey="userkey";
	public static final String r_number="number";
	public static final String r_personid="person_id";
	public static final String r_type="type";
	public static final String elements=r_personid+","+r_number+","+r_type+","+r_userkey;
	public static final String _elements="?,?,?,?";
	public String number;
	int person_id,type;
	public Phone(String num,int pid,int type)
	{
		number="";
		for(char k : num.toCharArray())
			if(k >= '0' && k <='9') number+=k;
		person_id = pid;
		this.type = type;
	}
	public boolean setStatement(PreparedStatement pstmt,int uskey) throws SQLException
	{
		if(number.length() == 0) return false;
		pstmt.setInt(1, person_id);
		pstmt.setString(2, number);
		pstmt.setInt(3,type);
		pstmt.setInt(4, uskey);
		return true;
	}
	public static boolean chkPhone(String p)
	{
		if(p.length()==0) return false;
		for(char k : p.toCharArray())
		{
			if(k<'0' || k>'9') return false;
		}
		return true;
	}
	public Phone(ResultSet set) throws SQLException
	{
		this.number = set.getString(set.findColumn(r_number));
		this.person_id = set.getInt(set.findColumn(r_personid));
		this.type = set.getInt(set.findColumn(r_type));
	}
}
