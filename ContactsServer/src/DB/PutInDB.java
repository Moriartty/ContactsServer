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
			//����ҵ������ϵ�˵�source_idΪ�գ���˵�������¼ӵ���ϵ��
			if(temp.get("source_id")==null||temp.get("source_id").equals("")){
				//������ݿ���Ҳû���ҵ������ϵ�˵ĵ绰����˵�����ݿ���û������˵���Ϣ
				if(!QueryDB.hasThisPerson(temp.get("phone"))){  
					String uuid=getData.getUUID();//Ϊ�����ϵ���½�һ��source_id
					returnData.put("C"+temp.get("phone"), uuid);//�����ݿ���û�д���ʱ��Ϊ��������һ��uuid,�����뼴�����ص�hashmap
					//���������ϵ�˵����ݲ������ݱ�
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
				else{//�����ݿ��а������˵绰ʱ��˵�����ݿ��а������ˣ����Ի�ȡ�����˵�source_id
					//������Ǹ�Ӧ�õ�ʹ����,�����޸ĸ���ϵ��������Ϣ
					String source_id=getData.getSource_IdFromDB(temp.get("phone"));
					if(QueryDB.isUser(source_id)==0){
						System.out.println(currentTag+"���Ǹ�Ӧ�õ�ʹ����");
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
						System.out.println(currentTag+"�Ǹ�Ӧ�õ�ʹ����");
					}
					returnData.put("C"+temp.get("phone"), getData.getSource_IdFromDB(temp.get("phone")));
				}
			}
			
			else{//������ϴ��ĸ���ϵ�˵������а�����Ч��source_id����˵����֮ǰͬ���������ݿ���Ҳ��������ϵ����Ϣ
				if(QueryDB.isUser(temp.get("source_id"))==0){//������Ǹ�Ӧ�õ�ʹ����
					//��ɾ������ϵ��ԭʼ��Ϣ
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
		if(!data.get("source_id").equals("")){  //����Լ�֮ǰͬ�������ݣ���ɾ��ԭ���Լ������ݼ�¼
			//��ɾ���Լ�ԭ����ӳ���
			String preTel=getData.getPhoneNumberFromDB(data.get("source_id")); //����Ϊ��
			String curTel=data.get("phone");
			deletePreConnection("Contacts_"+preTel);
			if(preTel!=null&&!preTel.equals(curTel))  //����Լ��ĵ绰�޸��ˣ���ô�Լ����罻���ݱ���ҲҪ�ı�
			{
				System.out.println(currentTag+"�绰���޸�");
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
		//����ӿͻ��˴�����������source_id Ϊ�գ���˵�����û���û����ͬ����,
		else {
			if(!QueryDB.hasThisPerson(data.get("phone")))//������ݿ���û�и��û��绰����˵�����ݿ���û�и��û�����
				data.put("source_id", getData.getUUID());
			else {//������ݿ����и��û��绰����˵���������û��ϴ������û����ݣ������û�֮ǰû��ͬ����
				//�����ݿⱣ���ĸ��û���source_id,put��data
				data.put("source_id", getData.getSource_IdFromDB(data.get("phone")));
				//ɾ�����û�֮ǰ�����ݣ����û����Լ������ݾ�����ȫ�Ķ�дȨ
				sql="delete from "+tableName+" where Source_Id='"+data.get("source_id")+"'";
				try {
					stmt.executeUpdate(sql);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		//���½��Լ�������д��personInfo����
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
