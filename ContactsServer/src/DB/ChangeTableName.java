package DB;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class ChangeTableName {
	final static String preTag="Contacts_";
	final static String[] Tag=new String[]{"_Tieba","_Weibo","_Renren"};
	
	public static void changePreName(String preTel,String curTel) throws Exception{
		Connection dbConn=connect_sql.Connect();
		for(int i=0;i<3;i++){  //对该联系人的社交平台数据表进行改名
			String preTableName=preTag+preTel+Tag[i];
			String curTableName=preTag+curTel+Tag[i];
			if(hasTable.hastable(dbConn,preTableName)==1){
				Statement stmt = null;
				String sql="EXEC sp_rename "+"'"+preTableName+"','"+curTableName+"'";
				try {
					stmt = dbConn.createStatement();
					stmt.executeUpdate(sql);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
