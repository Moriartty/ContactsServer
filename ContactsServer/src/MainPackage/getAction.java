package MainPackage;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.Statement;
import java.util.HashMap;

import org.dom4j.Document;

import DB.CreateTable;
import DB.PutInDB;
import DataHandling.MapToXML;
import Tieba.TiebaProfileCrawl;
import Tieba.TiebaProfileCrawlThread;
import Weibo.WeiboCrlawer;
import Weibo.WeiboCrlawerThread;

public class getAction {
	final static int TIEBA=0;
    final static int WEIBO=1;
	final static int RENREN=2;
	public static void actionHandling(int flag,String URL,String tel){
		switch (flag) {
		case TIEBA:
			TiebaProfileCrawl crawl=TiebaProfileCrawlThread.returnCrawl(URL);
			TiebaProfileCrawlThread.writeInDB(crawl, tel); 
			break;
		case WEIBO:
			WeiboCrlawer crlawer=WeiboCrlawerThread.returnCrawler(URL);
			WeiboCrlawerThread.writeInDB(crlawer, tel);
			break;
		case RENREN:
			break;
		
		}
	}

}
