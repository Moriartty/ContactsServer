package Tieba;

import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import org.dom4j.Document;

import DataHandling.MapToXML;

public class TiebaCrawlerThread {
	private static final String currentTag="TiebaCrawlerThread:";
	public static void handleTiebaThread(String temp,Socket s){
		String[] splitedData=temp.split("[*]");
		String url=splitedData[0];
		String personName="Contacts_"+splitedData[1];
		ArrayList<HashMap<String, String>> recode;
		
		String URL=url.replace("TiebaCrawler", "");
		System.out.println(currentTag+URL);
		TiebaCrawler crawler=returnCrawler(URL);
		try{
			sendToClient(crawler, s);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static TiebaCrawler returnCrawler(String url){
		TiebaCrawler crawler=new TiebaCrawler("b", true, url);
		try{
			crawler.setThreads(1);
			crawler.addSeed(url);
			crawler.start(1);
		}catch(Exception e){
			e.printStackTrace();
		}
		return crawler;
	}
	
	public static void sendToClient(TiebaCrawler crawler,Socket s){
		try{
			Document document=new MapToXML().map2xml2(crawler.getMap(), "tieba");
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
