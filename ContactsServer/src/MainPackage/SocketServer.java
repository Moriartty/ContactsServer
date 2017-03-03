package MainPackage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Timer;

import DB.CreateTable;

public class SocketServer {
	private static final String currentTag="SocketServer:";
	public static ArrayList<Socket> list=new ArrayList<>();
	public SocketServer(){
		
	}
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		CreateTable.createPersonInfoTable("personInfo");  //在服务器启动后就要建立personInfo表
		try{
			//Timer timer=new Timer();
			//timer.schedule(new TimerClass.MyTask(), 0, 40*1000);   //设定抓取的周期*/
		}catch(Exception e){
			System.out.println(currentTag+"服务器启动异常，请重试");
		}
		ServerSocket serverSocket=new ServerSocket(10000);
		while(true){
			Socket socket=serverSocket.accept();
			list.add(socket);
			System.out.println(currentTag+"当前有"+list.size()+"条连接");
			new Thread(new ServerThread(socket)).start();;
		}
		
	}

}
