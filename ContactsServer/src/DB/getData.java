package DB;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import DB.connect_sql;
import Tieba.TiebaProfileCrawl;

public class getData {
	public static ArrayList<String> getPerson_Tel(){
		Connection dbConn=connect_sql.Connect();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = dbConn.createStatement();
			String sqlStr = "select Tel from personInfo";
			rs = stmt.executeQuery(sqlStr);
			//String uid[]=new String[1000];     /*逐条记录需要分析的数据*/
			ArrayList<String> uid=new ArrayList<>();
			for(int i=0;rs.next();i++){
				uid.add(rs.getString("Tel"));
			}
			return uid;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String GetPerson_info(String tel,int flag){
		Connection dbConn=connect_sql.Connect();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = dbConn.createStatement();
			String sqlStr = null;
			switch(flag){
			case 0:sqlStr = "select TiebaUrl from personInfo where Tel='"+tel+"'";
			       break;
			       
			case 1:sqlStr = "select WeiboUrl from personInfo where Tel='"+tel+"'";
		           break;
		           
			case 2:sqlStr = "select RenrenUrl from personInfo where Tel='"+tel+"'";
	               break;
			}
			rs = stmt.executeQuery(sqlStr);
			String uid=null;                     //记录需要分析的数据
			for(@SuppressWarnings("unused")
			int i=0;rs.next();i++){
				switch(flag){
				case 0:uid=rs.getString("TiebaUrl");;
				       break;
				       
				case 1:uid=rs.getString("WeiboUrl");;
			           break;
			           
				case 2:uid=rs.getString("RenrenUrl");;
		               break;
				}
			}
			return uid;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getUUID(){
		UUID uuid=UUID.randomUUID();
		return uuid.toString();
	}
	
	public static String getSource_IdFromDB(String tel){
		Connection dbConn=connect_sql.Connect();
		String source_id=null;
		ResultSet rs = null;
		Statement stmt = null;
		String sqlStr="select Source_Id from personInfo where Tel='"+tel+"'";
		try{
			stmt = dbConn.createStatement();
			rs = stmt.executeQuery(sqlStr);
			for(int i=0;rs.next();i++){
				source_id=rs.getString("Source_Id");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return source_id;
	}
	
	public static String getPhoneNumberFromDB(String source_id){
		Connection dbConn=connect_sql.Connect();
		String tel=null;
		ResultSet rs = null;
		Statement stmt = null;
		String sqlStr="select Tel from personInfo where Source_Id='"+source_id+"'";
		try{
			stmt = dbConn.createStatement();
			rs = stmt.executeQuery(sqlStr);
			for(int i=0;rs.next();i++){
				tel=rs.getString("Tel");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return tel;
	}
	
	public static ArrayList<String> getAllTable(){
		Connection dbConn=connect_sql.Connect();
		ArrayList<String> tableList=new ArrayList<>();
		ResultSet rs = null;
		Statement stmt = null;
		try{
			DatabaseMetaData data=dbConn.getMetaData();
			rs=data.getTables("ContactsInfo", null, null, new String[]{"TABLE"});
			while(rs.next()){
				if(rs.getString("TABLE_NAME").contains("Tieba")||rs.getString("TABLE_NAME").contains("Weibo"))
					tableList.add(rs.getString("TABLE_NAME"));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return tableList;
	}
	
	public static ArrayList<HashMap<String, String>> getOrderData(String tableName){
		Connection connection=connect_sql.Connect();
		ResultSet rSet=null;
		Statement statement=null;
		ArrayList<HashMap<String, String>> tempList=new ArrayList<>();
		String personName=tableName.split("[_]")[1];
		String flag=tableName.split("[_]")[2];
		String sqlStr="select * from "+tableName+" order by SCORE";
		try{
			statement=connection.createStatement();
			rSet=statement.executeQuery(sqlStr);
			if(flag.equals("Tieba")){
				for(int i=0;rSet.next()&&i<5;i++){
					HashMap<String, String> tempMap=new HashMap<>();
					tempMap.put("time", rSet.getString("TIME"));
					tempMap.put("address", rSet.getString("ADDRESS"));
					tempMap.put("url", rSet.getString("URL"));
					tempMap.put("head_img",rSet.getString("HEAD_IMG"));
					tempMap.put("name", personName);
					tempMap.put("flag", flag);
					if(rSet.getString("TITLE")!=null){
						tempMap.put("title", rSet.getString("TITLE"));
						tempMap.put("content", rSet.getString("CONTENT"));
					}
					else{
						tempMap.put("reply_content", rSet.getString("REPLY_CONTENT"));
						tempMap.put("titletxt", rSet.getString("TITLETXT"));
					}
					tempList.add(tempMap);
					System.out.println(rSet.getString("TITLETXT")+" "+rSet.getString("SCORE"));
				}
			}
			else if(flag.equals("Weibo")){
				for(int i=0;rSet.next()&&i<5;i++){
					HashMap<String, String> tempMap=new HashMap<>();
					tempMap.put("name", personName);
					tempMap.put("head_img", rSet.getString("HEAD_IMG"));
					tempMap.put("time", rSet.getString("TIME"));
					tempMap.put("content_text",rSet.getString("CONTENT_TEXT"));
					tempMap.put("content_img", rSet.getString("CONTENT_PIC"));
					tempMap.put("focus", rSet.getString("FOCUS"));
					tempMap.put("flag", flag);
					tempList.add(tempMap);
				}
			}
			connection.close();
			return tempList;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

}
