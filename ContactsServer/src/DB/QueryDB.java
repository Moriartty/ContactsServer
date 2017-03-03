package DB;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.bouncycastle.asn1.cms.IssuerAndSerialNumber;

public class QueryDB {
	public static boolean hasThisPerson(String tel){
		Connection dbConn=connect_sql.Connect();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = dbConn.createStatement();
			//String sqlStr = "select reply from "+name+" where position='"+username+"'";
			String sqlStr=null;
			sqlStr = "select count(*) from personInfo where Tel='"+tel+"'";
			rs = stmt.executeQuery(sqlStr);
			rs.next();
			int count = rs.getInt(1);
			if(count>0){
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean hasThisPersonBySourceId(String source_id){
		Connection dbConn=connect_sql.Connect();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = dbConn.createStatement();
			//String sqlStr = "select reply from "+name+" where position='"+username+"'";
			String sqlStr=null;
			sqlStr = "select count(*) from personInfo where Source_Id='"+source_id+"'";
			rs = stmt.executeQuery(sqlStr);
			rs.next();
			int count = rs.getInt(1);
			if(count>0){
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static int isUser(String source_id){
		Connection dbConn=connect_sql.Connect();
		Statement stmt = null;
		ResultSet rs = null;
		int isUser=0;
		try {
			stmt = dbConn.createStatement();
			String sqlStr=null;
			sqlStr = "select IsUser from personInfo where Source_Id='"+source_id+"'";
			rs = stmt.executeQuery(sqlStr);
			for(int i=0;rs.next();i++){
				isUser=rs.getInt("IsUser");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return isUser;
	}

}
