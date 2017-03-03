package Sync_Reserve;

import java.io.OutputStream;
import java.net.Socket;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentException;

import DB.CreateTable;
import DB.PutInDB;
import DataHandling.MapToXML;
import DataHandling.XMLToMap;
import Weibo.WeiboCrlawer;

public class ClientReader {
	private static final String currentTag="ClientReader:";
	static String tableName="personInfo";
	static String MyKey;
	public static void getDataFromClient(String data,Socket s) throws DocumentException{
		data=data.replace("reserve", "");
		HashMap<String, String> returnData;
		HashMap<String, HashMap<String, String>> reserve_data=
				new HashMap<>(XMLToMap.xml2map(data, false));
		HashMap<String, String> myself_data=new HashMap<>();
		for(String myKey:reserve_data.keySet()){
			if(myKey.contains("M")){
				MyKey=myKey;  //记录我的hashmap key作为识别码
				break;
			}
		}
		myself_data.putAll(reserve_data.get(MyKey));  //将我的信息重新加入一个hashmap
		reserve_data.remove(MyKey);  //将我的信息从所有联系人信息中移除
		returnData=writeMyContactsInDB(reserve_data);  //写入我的联系人信息
		HashMap<String, String> teMap=writeMySelfInDB(myself_data);   //写入我自己的信息
		returnData.put("M"+teMap.get("phone"), teMap.get("source_id"));  
		connectedMyContactsWithMyself(myself_data, reserve_data);  //建立我与我的联系人的索引表
		sendToClient(returnData, s);
	}
	
	public static HashMap<String, String> writeMyContactsInDB(HashMap<String, HashMap<String, String>> reserve_data){
		Statement statement=CreateTable.createPersonInfoTable(tableName);
		System.out.println(currentTag+reserve_data.size());
		return PutInDB.putPersonInfo2DB(reserve_data, tableName, statement);
	}
	
	public static HashMap<String, String> writeMySelfInDB(HashMap<String, String> myself_data){
		Statement statement=CreateTable.createPersonInfoTable(tableName);
		System.out.println(currentTag+"mySource_id is"+myself_data.get("source_id"));
		return PutInDB.putMyInfo2DB(myself_data, tableName, statement);
	}
	
	public static void connectedMyContactsWithMyself(HashMap<String, String> myself_data,HashMap<String, HashMap<String, String>> reserve_data){
		String tableName="Contacts_"+getMyTel(myself_data);
		Statement statement=CreateTable.createUserConnectedTable(tableName);
		PutInDB.putConnection2DB(getMyContactsTel(reserve_data), tableName, statement);
	}
	
	public static void getMyDataFromClient(String data) throws DocumentException{
		data=data.replace("mydata", "");
		HashMap<String , String> reserve_data=new HashMap<>(XMLToMap.xml2map(data, false));
		//System.out.println(reserve_data.size());
		Statement statement=CreateTable.createPersonInfoTable(tableName);
		PutInDB.putMyInfo2DB(reserve_data, tableName, statement);
	}
	
	public static String getMyTel(HashMap<String, String> myself_data){
		return myself_data.get("phone");
	}
	
	public static ArrayList<String> getMyContactsTel(HashMap<String, HashMap<String, String>> reserve_data){
		ArrayList<String> contactsTel=new ArrayList<>();
		for(HashMap<String, String> temp:reserve_data.values()){
			contactsTel.add(temp.get("phone"));
		}
		return contactsTel;
	}
	
	public static void sendToClient(HashMap<String, String> returnData,Socket s){
		try{
			Document document=new MapToXML().map2xml2(returnData, "return");
			String xmlString = MapToXML.formatXml(document);
        	System.out.println(currentTag+xmlString);
        	OutputStream outputStream=s.getOutputStream();
        	outputStream.write((xmlString+"[over]"+"\r\n").getBytes("utf-8"));  
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
