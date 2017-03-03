package MainPackage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.HashMap;

import org.dom4j.Document;

import DB.CreateTable;
import DataHandling.MapToXML;
import DataHandling.XMLToMap;
import Recommend.RecommendThread;
import Sync_Reserve.ClientReader;
import Tieba.TiebaCrawler;
import Tieba.TiebaCrawlerThread;
import Tieba.TiebaProfileCrawl;
import Tieba.TiebaProfileCrawlThread;
import Weibo.WeiboCrlawer;
import Weibo.WeiboCrlawerThread;
import mx4j.tools.config.DefaultConfigurationBuilder.Create;

public class ServerThread implements Runnable {
	private static final String currentTag="ServerThread:";
	Socket s=null;
	BufferedReader br=null;
	public ServerThread(Socket s) throws UnsupportedEncodingException, IOException{
		this.s=s;
		br=new BufferedReader(new InputStreamReader(s.getInputStream(), "utf-8"));
	}
	@Override
	public void run(){
		String temp=null;
		StringBuffer stringBuffer=new StringBuffer();
		try {
			while((temp=readFromClient(br))!=null&&!temp.equals("[over]")){
				stringBuffer.append(temp+"\r\n");
				System.out.println(currentTag+temp);
				if(temp.contains("TiebaProfileCrawl")){
					TiebaProfileCrawlThread.handleTiebaThread(temp, s);
				}
				else if(temp.contains("TiebaCrawler")){
					TiebaCrawlerThread.handleTiebaThread(temp,s);
				}
				else if(temp.contains("WeiboCrlawer")){
					WeiboCrlawerThread.handleWeiboThread(temp, s);
				}
				else if(temp.contains("Recommend")){
					System.out.println("aaaaaaaa");
					RecommendThread.handleRecommendThread(s);
				}
			}
			
			String data=stringBuffer.toString();
			if(data.contains("reserve")){
				ClientReader.getDataFromClient(data,s);
			}
			/*else if(data.contains("mydata")){
				ClientReader.getMyDataFromClient(data);
				//System.out.println(data);
			}*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String readFromClient(BufferedReader br){
		try{
			return br.readLine();
		}catch(IOException e){
			SocketServer.list.remove(s);    //如果出现异常，则表明该连接已断开
			System.out.println(currentTag+"连接已断开");
		}
		return null;
	}
}
