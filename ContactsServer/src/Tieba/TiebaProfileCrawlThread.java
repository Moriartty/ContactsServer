package Tieba;

import java.io.OutputStream;
import java.net.Socket;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import org.dom4j.Document;

import DB.CreateTable;
import DB.PutInDB;
import DataHandling.MapToXML;
import Emotion_analysis.SentimentAnalyseTest;

public class TiebaProfileCrawlThread {
	private static final String currentTag="TiebaProfileCrawlThread:";
	public static void handleTiebaThread(String temp,Socket s){
		String[] splitedData=temp.split("[*]");
		String url=splitedData[0];
		String personName=splitedData[1];
		
		String URL=url.replace("TiebaProfileCrawl", "");
		String name=URL.replace("http://tieba.baidu.com/home/main?un=", "").replace("&ie=utf-8&fr=pb", "");
		System.out.println(currentTag+URL);
		TiebaProfileCrawl crawler=returnCrawl(URL);
        try{
        	 System.out.println(currentTag+crawler.getList().size());
        	 writeInDB(crawler,personName);
        	 SentimentAnalyseTest sentimentAnalyseTest=new SentimentAnalyseTest();
        	 //测试将情感数据写入
        	// sentimentAnalyseTest.writeInDB(personName);
        	 
        	 sendToClient(crawler, s,name);
        }catch(Exception e){
        	e.printStackTrace();
        }
	}
	
	public static TiebaProfileCrawl returnCrawl(String url) {
		TiebaProfileCrawl crawler=new TiebaProfileCrawl("a",true);
		try{
			crawler.setThreads(1);
	        crawler.addSeed(url);
	        crawler.start(1);
		}catch(Exception e){
			e.printStackTrace();
		}
        return crawler;
	}
	
	public static void writeInDB(TiebaProfileCrawl crawler,String personName){
		ArrayList<HashMap<String, String>> recode=new ArrayList<>();
		String tableName="Contacts_"+personName+"_Tieba";
		recode.addAll(crawler.getList());   //将贴吧数据写入数据库
   	 	Statement statement=CreateTable.createTable(recode, tableName,0);
   	 	if(recode!=null&&recode.size()>0)
   	 		PutInDB.putIn_TiebaDB(recode, tableName,statement);
	}
	
	public static void sendToClient(TiebaProfileCrawl crawler,Socket s,String name){
		try{
			HashMap<String,HashMap<String, String>> map=MapToXML.tiebalistToMap(crawler.getList(),name);
			Document doc=new MapToXML().map2xml(map, "tieba");
	   	 	String xmlString = MapToXML.formatXml(doc);
	   	 	System.out.println(currentTag+xmlString);
	   	 	System.out.println("\n"+currentTag+xmlString.length());
	   	 	OutputStream outputStream=s.getOutputStream();
	   	 	outputStream.write((xmlString).getBytes("utf-8"));
	   	 	
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
