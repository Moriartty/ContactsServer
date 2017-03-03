package Recommend;

import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.dom4j.Document;

import DB.getData;
import DataHandling.MapToXML;

public class RecommendThread {
	private final static String currentTag="RecommendThread:";
	private static ArrayList<String> tableList;
	public static void handleRecommendThread(Socket s){
		tableList=getData.getAllTable();
		ArrayList<HashMap<String, String>> tempList=getDataFromSql();
		sendToClient(tempList,s);
	}
	
	public static ArrayList<HashMap<String, String>> getDataFromSql(){
		ArrayList<HashMap<String, String>> recommendList=new ArrayList<>();
		Iterator<String> i=tableList.iterator();
		while(i.hasNext()){
			recommendList.addAll(getData.getOrderData(i.next()));
		}
		return recommendList;
	}
	
	
	public static void sendToClient(ArrayList<HashMap<String, String>> tempList,Socket s){
		try{
			//System.out.println(currentTag+"list size is "+tempList.size());
			HashMap<String,HashMap<String, String>> map=MapToXML.allToMap(tempList);
			//System.out.println(currentTag+"map size is "+map.size());
			Document doc=new MapToXML().map2xml(map, "recommend");
	   	 	String xmlString = MapToXML.formatXml(doc);
	   	 	System.out.println(currentTag+xmlString);
	   	 	System.out.println("\n"+currentTag+xmlString.length());
	   	 	OutputStream outputStream=s.getOutputStream();
	   	 	outputStream.write((xmlString+"[over]"+"\r\n").getBytes("utf-8"));
		}catch(Exception e){
			e.printStackTrace();
		}
	} 
}
