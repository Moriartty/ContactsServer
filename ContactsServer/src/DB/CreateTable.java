package DB;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import DB.connect_sql;
import DB.hasTable;
import mx4j.log.Log;

public class CreateTable {
	final static int TIEBA=0;
	final static int WEIBO=1;
	final static int RENREN=2;
	private static final String currentTag="CreateTable:";
	public static Statement createTable(ArrayList<HashMap<String, String>> recode,String tableName,int flag){
		Connection dbConn=connect_sql.Connect();
		int Has;
		try {
			Statement stmt =dbConn.createStatement();
			String sql=null;
			switch (flag) {
			case TIEBA:
				Has=hasTable.hastable(dbConn,tableName);    /*判断要创建的表是否存在*/
				if(Has==1){
					Delete_table.delete_table(tableName);
				}
				sql="create table "+ tableName+" (TIME  varchar(MAX)," +
						"ADDRESS  varchar(MAX),TITLE varchar(MAX),"
						+ "CONTENT  varchar(MAX),REPLY_CONTENT varchar(MAX),"
						+ "TITLETXT varchar(MAX),URL varchar(MAX),HEAD_IMG varchar(MAX),SCORE float)";
				stmt.executeUpdate(sql);
				break;
			case WEIBO:
				Has=hasTable.hastable(dbConn,tableName);    /*判断要创建的表是否存在*/
				if(Has==1){
					Delete_table.delete_table(tableName);
				}
				sql="create table "+ tableName+" (TIME  varchar(MAX)," +
						"NAME  varchar(MAX),CONTENT_TEXT varchar(MAX),"
						+ "CONTENT_PIC  varchar(MAX),FOCUS varchar(MAX),"
						+ "HEAD_IMG varchar(MAX),SCORE float)";
				stmt.executeUpdate(sql);
				break;
			case RENREN:
				break;
			}
			return stmt;
		} catch (Exception e) {
			System.out.println(currentTag+"连接失败");
			e.printStackTrace();
		}
		return null;
	}
	
	public static Statement createPersonInfoTable(String tableName){
		Connection dbConn=connect_sql.Connect();
		int Has;
		String table;
		try {
			Statement stmt =dbConn.createStatement();
			String sql=null;
			Has=hasTable.hastable(dbConn,tableName);    /*判断要创建的表是否存在*/
			if(Has==1){
				return stmt;
			}
			else{
				table="create table "+ tableName+" (Name  varchar(MAX)," +
						"Tel  varchar(MAX),Source_Id varchar(MAX),TiebaUrl varchar(MAX),"
						+ "WeiboUrl  varchar(MAX),RenrenUrl varchar(MAX), Emotion_Value float, IsUser int)";
				stmt.executeUpdate(table);
				return stmt;
			}
		}catch (Exception e) {
			System.out.println(currentTag+"连接失败");
			e.printStackTrace();
		}
		return null;
	}
	
	public static Statement createUserConnectedTable(String tableName){
		Connection dbConn=connect_sql.Connect();
		int Has;
		String table;
		try {
			Statement stmt =dbConn.createStatement();
			String sql=null;
			Has=hasTable.hastable(dbConn,tableName);    /*判断要创建的表是否存在*/
			if(Has==1){
				Delete_table.delete_table(tableName);
			}
			sql="create table "+ tableName+" (Tel varchar(MAX))";
			stmt.executeUpdate(sql);
			return stmt;
		}catch (Exception e) {
			System.out.println(currentTag+"连接失败");
			e.printStackTrace();
		}
		return null;
	}
	
}
