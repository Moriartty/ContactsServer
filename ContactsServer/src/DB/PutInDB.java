package DB;

import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.jasper.tagplugins.jstl.core.If;
import org.openqa.selenium.remote.RemoteTouchScreen;

public class PutInDB {
	private static final String currentTag="PutInDB:";
	public static void putIn_TiebaDB(ArrayList<HashMap<String, String>> recode,String tableName,Statement stmt){
		String sql;
		java.util.Random random=new java.util.Random();
		int size=recode.size();
		HashMap<String, String> other_info=new HashMap<>();
		other_info.putAll(recode.get(size-1));
		recode.remove(size-1);
		for(HashMap<String, String> temp:recode){
			int i=random.nextInt(20)-10;
			if(temp.containsKey("title")){
				sql = "insert into " +tableName+" (TIME,ADDRESS,TITLE,CONTENT,URL,HEAD_IMG,SCORE)" +
			    	     "values('"+temp.get("time")+"','"+temp.get("address")+"',"
			    	     		+ "'"+temp.get("title")+"',"
			    	     		+ "'"+temp.get("content")+"','"+temp.get("url")+"','"+other_info.get("head_img")+"','"+i+"')";
				try {
					stmt.executeUpdate(sql);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			else if(temp.containsKey("reply_content")){
				sql = "insert into " +tableName+" (TIME,ADDRESS,TITLETXT,REPLY_CONTENT,URL,HEAD_IMG,SCORE)" +
			    	     "values('"+temp.get("time")+"','"+temp.get("address")+"',"
			    	     		+ "'"+temp.get("titletxt")+"',"
			    	     		+ "'"+temp.get("reply_content")+"','"+temp.get("url")+"','"+other_info.get("head_img")+"','"+i+"')";
				try {
					stmt.executeUpdate(sql);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void putIn_WeiboDB(ArrayList<HashMap<String, String>> recode,String tableName,Statement stmt){
		String sql;
		HashMap<String, String> other_info=new HashMap<>();
		java.util.Random random=new java.util.Random();
		int size=recode.size();
		if(size>0){
			other_info.putAll(recode.get(size-1));
			for(int i=size-1;i>size-5;i--)
				recode.remove(i);
			for(HashMap<String, String> temp:recode){
				int i=random.nextInt(20)-10;
				sql = "insert into " +tableName+" (TIME,NAME,CONTENT_TEXT,CONTENT_PIC,FOCUS,HEAD_IMG,SCORE)" +
			    	     "values('"+temp.get("time")+"','"+other_info.get("name")+"',"
			    	     		+ "'"+temp.get("content_text")+"',"
			    	     		+ "'"+temp.get("content_img")+"','"+temp.get("focus")+"','"+other_info.get("head_img")+"','"+i+"')";
				try {
					stmt.executeUpdate(sql);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public static HashMap<String, String> putPersonInfo2DB(HashMap<String,HashMap<String, String>> data,String tableName,Statement stmt){
		ArrayList<HashMap<String, String>> reserve_data=new ArrayList<>(data.values());
		//System.out.println(reserve_data.size());
		HashMap<String, String> returnData=new HashMap<>();
		String sql;
		for(HashMap<String, String> temp:reserve_data){
			//如果我的这个联系人的source_id为空，则说明是我新加的联系人
			if(temp.get("source_id")==null||temp.get("source_id").equals("")){
				//如果数据库中也没有我的这个联系人的电话，则说明数据库中没有这个人的信息
				if(!QueryDB.hasThisPerson(temp.get("phone"))){  
					String uuid=getData.getUUID();//为这个联系人新建一个source_id
					returnData.put("C"+temp.get("phone"), uuid);//当数据库中没有此人时，为此人生成一个uuid,并加入即将返回的hashmap
					//将这个新联系人的数据插入数据表
					sql = "insert into " +tableName+" (Name,Tel,Source_Id,TiebaUrl,WeiboUrl,RenrenUrl,IsUser)" +
				    	     "values('"+temp.get("name")+"','"+temp.get("phone")+"','"+uuid+"',"
				    	     		+ "'"+temp.get("tiebaurl")+"',"
				    	     		+ "'"+temp.get("weibourl")+"','"+temp.get("renrenurl")+"','"+0+"')";
					try {
						stmt.executeUpdate(sql);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				else{//当数据库中包含此人电话时，说明数据库中包含此人，可以获取到该人的source_id
					//如果不是该应用的使用者,允许修改该联系人数据信息
					String source_id=getData.getSource_IdFromDB(temp.get("phone"));
					if(QueryDB.isUser(source_id)==0){
						System.out.println(currentTag+"不是该应用的使用者");
						sql = "insert into " +tableName+" (Name,Tel,Source_Id,TiebaUrl,WeiboUrl,RenrenUrl,IsUser)" +
					    	     "values('"+temp.get("name")+"','"+temp.get("phone")+"','"+source_id+"',"
					    	     		+ "'"+temp.get("tiebaurl")+"',"
					    	     		+ "'"+temp.get("weibourl")+"','"+temp.get("renrenurl")+"','"+0+"')";
						try {
							stmt.executeUpdate(sql);
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
					else {
						System.out.println(currentTag+"是该应用的使用者");
					}
					returnData.put("C"+temp.get("phone"), getData.getSource_IdFromDB(temp.get("phone")));
				}
			}
			
			else{//如果我上传的该联系人的数据中包含有效的source_id，则说明我之前同步过，数据库中也包含词联系人信息
				if(QueryDB.isUser(temp.get("source_id"))==0){//如果不是该应用的使用者
					//先删除该联系人原始信息
					sql="delete from "+tableName+" where Source_Id='"+temp.get("source_id")+"'";
					try {
						stmt.executeUpdate(sql);
					} catch (SQLException e) {
						e.printStackTrace();
					}
					
					sql = "insert into " +tableName+" (Name,Tel,Source_Id,TiebaUrl,WeiboUrl,RenrenUrl,IsUser)" +
				    	     "values('"+temp.get("name")+"','"+temp.get("phone")+"','"+temp.get("source_id")+"',"
				    	     		+ "'"+temp.get("tiebaurl")+"',"
				    	     		+ "'"+temp.get("weibourl")+"','"+temp.get("renrenurl")+"','"+0+"')";
					try {
						stmt.executeUpdate(sql);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
		System.out.println(currentTag+returnData.size());
		return returnData;
	}
	
	public static HashMap<String, String> putMyInfo2DB(HashMap<String, String> data,String tableName,Statement stmt){
		String sql;
		if(!data.get("source_id").equals("")){  //如果自己之前同步过数据，先删除原来自己的数据记录
			//先删除自己原来的映射表
			String preTel=getData.getPhoneNumberFromDB(data.get("source_id")); //可能为空
			String curTel=data.get("phone");
			deletePreConnection("Contacts_"+preTel);
			if(preTel!=null&&!preTel.equals(curTel))  //如果自己的电话修改了，那么自己的社交数据表名也要改变
			{
				System.out.println(currentTag+"电话已修改");
				try{
					ChangeTableName.changePreName(preTel,curTel);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			sql="delete from "+tableName+" where Source_Id='"+data.get("source_id")+"'";
			try {
				stmt.executeUpdate(sql);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		//如果从客户端传来的数据中source_id 为空，则说明该用户还没数据同步过,
		else {
			if(!QueryDB.hasThisPerson(data.get("phone")))//如果数据库中没有该用户电话，这说明数据库中没有该用户数据
				data.put("source_id", getData.getUUID());
			else {//如果数据库中有该用户电话，这说明有其他用户上传过该用户数据，但该用户之前没有同步过
				//将数据库保留的该用户的source_id,put进data
				data.put("source_id", getData.getSource_IdFromDB(data.get("phone")));
				//删除该用户之前的数据，该用户对自己的数据具有完全的读写权
				sql="delete from "+tableName+" where Source_Id='"+data.get("source_id")+"'";
				try {
					stmt.executeUpdate(sql);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		//重新将自己的数据写入personInfo表中
		sql = "insert into " +tableName+" (Name,Tel,Source_Id,TiebaUrl,WeiboUrl,RenrenUrl,IsUser)" +
	    	     "values('"+data.get("name")+"','"+data.get("phone")+"','"+data.get("source_id")+"',"
	    	     		+ "'"+data.get("tiebaurl")+"',"
	    	     		+ "'"+data.get("weibourl")+"','"+data.get("renrenurl")+"','"+1+"')";
		try {
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return data;
	}
	
	public static void InsertSentimentScore(String personName){
		Connection dbConnection=connect_sql.Connect();
		String tableName="Contacts_"+personName+"_Tieba";
		Statement statement=null;
		ResultSet rs = null;
		String sqlStr="update "+tableName+" set SCORE = "+"1";
		String count="select count(*) totalCount from "+tableName;
		try{
			statement=dbConnection.createStatement();
			rs=statement.executeQuery(count);
			if(rs.next()) { 
				   System.out.println(rs.getInt("totalCount")); 
			}
			//statement.execute(sqlStr);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void putConnection2DB(ArrayList<String> data,String tableName,Statement stmt){
		String sql;
		for(String temp:data){
			sql = "insert into " +tableName+" (Tel)" +
		    	     "values('"+temp+"')";
			try {
				stmt.executeUpdate(sql);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void deletePreConnection(String tableName){
		Delete_table.delete_table(tableName);
	}
}
