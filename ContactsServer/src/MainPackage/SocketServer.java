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
		CreateTable.createPersonInfoTable("personInfo");  //�ڷ������������Ҫ����personInfo��
		try{
			//Timer timer=new Timer();
			//timer.schedule(new TimerClass.MyTask(), 0, 40*1000);   //�趨ץȡ������*/
		}catch(Exception e){
			System.out.println(currentTag+"�����������쳣��������");
		}
		ServerSocket serverSocket=new ServerSocket(10000);
		while(true){
			Socket socket=serverSocket.accept();
			list.add(socket);
			System.out.println(currentTag+"��ǰ��"+list.size()+"������");
			new Thread(new ServerThread(socket)).start();;
		}
		
	}

}
