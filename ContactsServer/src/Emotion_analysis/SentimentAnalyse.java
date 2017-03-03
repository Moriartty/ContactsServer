package Emotion_analysis;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class SentimentAnalyse {
	public static final String SENTIMENT_URL =
	        "http://api.bosonnlp.com/sentiment/analysis";
	public static final String SENTIMENT_URL2 =
			"http://api.bosonnlp.com/sentiment/analysis?weibo";
	    
	public static ArrayList<Float> analyse(ArrayList<String> recodeList) throws JSONException, UnirestException,java.io.IOException
	    {
	    	ArrayList<Float> result=new ArrayList<>();
	    	for(String temp:recodeList){
	    		String body = new JSONArray(new String[]{temp}).toString();   
		        HttpResponse<JsonNode> jsonResponse = Unirest.post(SENTIMENT_URL2)
		                .header("Accept", "application/json")
		                .header("X-Token", "QYdRdKZN.4094.0LmppHcluq1S")     //������Ҫ�Լ���ע����˺�
		                .body(body)
		                .asJson();
		        
		        System.out.println(jsonResponse.getBody());
		        result.add(getResult(jsonResponse.getBody().toString()));
	    	}
	    	
	        Unirest.shutdown();   //����ر��������߳������˳�
	        return result;
	        // Unirest starts a background event loop and your Java
	        // application won't be able to exit until you manually
	        // shutdown all the threads
	    }	
	public static float getResult(String s){
	    	s=s.replace("[","");
	        s=s.replace("]","");
	        String temp[]=s.split(",");
	        float num[]=new float[2];
	        for(int i=0;i<2;i++){
	        	num[i]=Float.parseFloat(temp[i]);
	        }
	        return num[0]-num[1];
	    }
	public static void main(String[] args) throws JSONException, UnirestException,
        java.io.IOException
	{
		ArrayList<String> recodeList=new ArrayList<>();
		recodeList.add("�Ҳ��������㣬��Ϊ��ϲ����");
		recodeList.add("����Ǹ�ɵ��");
		recodeList.add("�Ҿ�ϲ��ɵ��");
		recodeList.add("��ϲ����");
		for(float temp:analyse(recodeList))
			System.out.println(temp);

	}
}
