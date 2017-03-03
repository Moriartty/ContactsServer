package Weibo;

import java.io.OutputStream;
import java.net.Socket;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import org.dom4j.Document;

import DB.CreateTable;
import DB.PutInDB;
import DataHandling.MapToXML;

public class WeiboCrlawerThread {
	private static final String currentTag="WeiboCrlawerThread:";
	public static void handleWeiboThread(String temp,Socket s) throws Exception{
		String[] splitedData=temp.split("[*]");
		String url=splitedData[0];
		String personName=splitedData[1];
		
		String URL=url.replace("WeiboCrlawer", "");
		System.out.println(currentTag+URL);
		
		WeiboCrlawer crawler=returnCrawler(URL);
		try{
			writeInDB(crawler, personName);
			sendToClient(crawler, s);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static WeiboCrlawer returnCrawler(String url){
		try{
			WeiboCrlawer crawler = new WeiboCrlawer("c");
			crawler.setThreads(1);
			crawler.addSeed(url);
			crawler.start(1);
			return crawler;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static void writeInDB(WeiboCrlawer crawler,String personName){
		ArrayList<HashMap<String, String>> recode=new ArrayList<>();
		String tableName="Contacts_"+personName+"_Weibo";
		recode.addAll(crawler.getList());
    	Statement statement=CreateTable.createTable(recode, tableName,1);
    	if(recode!=null&&recode.size()>0)
    		PutInDB.putIn_WeiboDB(recode, tableName, statement);
	}
	
	public static void sendToClient(WeiboCrlawer crawler,Socket s){
		try{
			HashMap<String,HashMap<String, String>> map=MapToXML.weibolistToMap(crawler.getList());
			Document document=new MapToXML().map2xml(map, "weibo");
			String xmlString = MapToXML.formatXml(document);
        	System.out.println(currentTag+xmlString);
        	System.out.println("\n"+currentTag+xmlString.length());
        	//System.out.println(MapToXML.xml2map(xmlString, false).size());
        	OutputStream outputStream=s.getOutputStream();
        	outputStream.write((xmlString).getBytes("utf-8"));  
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
