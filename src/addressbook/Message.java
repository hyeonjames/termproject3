package addressbook;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class Message extends Date{ // SMS 혹은 CALL 정보를 담는 클래스

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String r_id="_id";
	public static final String r_deleted="deleted";
	public static final String r_time="dtime";
	public static final String r_from="num_from";
	public static final String r_to="num_to";
	public static final String table="log_msg";
	public static final String r_body="body";
	public static final String r_type="type";
	public static final String elements=r_time+","+r_from+","+r_to+","+r_type+"," +r_body;
	public static final String _elements="?,?,?,?,?";
	public static final String orderby=r_time+" desc";
	public String from,to,body;
	public int type;
	public static final int SMS=0;
	public static final int Call=1;
	public String id; // null=> sql 에 추가되지 않음 아직.
	public Message(String id,long time,String from,String to,int type,String bdy)
	{
		
		super(time);
		this.id = id;
		this.from = from;
		this.to = to;
		this.type = type;
		if(bdy!=null)this.body = bdy.replace("<", "%3C");
	}
	public String getBody()
	{
		if(body==null) return "";
		else return body;
	}
	public String getBody(int max)
	{
		if(body == null )return "";
		if(body.length() < max) return body;
		else return body.substring(0, max-1) + "...";
	}
	public String getType()
	{
		if(type == SMS) return "SMS";
		else if(type==Call) return "Call";
		return null;
	}
	public String getNum(String num,boolean type)
	{
		if(from.equals(num))
		{
			if(type) return "(send)"+ to;
			else return to;
		}
		else
		{
			if(type) return "(recv)"+ from;
			else return from;
		}
	}
	public Message(ResultSet set) throws SQLException
	{
		this(set.getString(set.findColumn(r_id)),set.getLong(set.findColumn(r_time)),set.getString(set.findColumn(r_from)),set.getString(set.findColumn(r_to)),set.getInt(set.findColumn(r_type)),set.getString(set.findColumn(r_body)));
		
	}
	public void setStatement(PreparedStatement pstmt) throws SQLException
	{
		pstmt.setLong(1, getTime());
		pstmt.setString(2, from);
		pstmt.setString(3, to);
		pstmt.setInt(4,type);
		pstmt.setString(5, body);
	}
}
