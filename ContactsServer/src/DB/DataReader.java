package DB;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import DB.connect_sql;

public class DataReader {
	public static ArrayList<String> getDataFromSQL(String personName){
		Connection dbConn=connect_sql.Connect();
		Statement stmt = null;
		ResultSet rs = null;
		ArrayList<String> uid=new ArrayList<>();     /*������¼��Ҫ����������*/
		try {
			stmt = dbConn.createStatement();
			//String sqlStr = "select reply from "+name+" where position='"+username+"'";
			String sqlStr=null;
			sqlStr = "select CONTENT from "+personName;
			
			rs = stmt.executeQuery(sqlStr);
			for(int i=0;rs.next();i++){
				uid.add(rs.getString("CONTENT"));
			}
			//file.Createfile(name,uid);        /*������д���ļ������ڷ���*/
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return uid;
	}
}
