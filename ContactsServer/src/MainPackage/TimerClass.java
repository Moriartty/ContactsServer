package MainPackage;
import java.util.ArrayList;

import DB.getData;

public class TimerClass {
	private static final String currentTag="TimerClass:";
	public static class MyTask extends java.util.TimerTask{
		@Override
		public void run() {
			ArrayList<String> teList=getData.getPerson_Tel();
			for(String tel:teList){
				System.out.println(currentTag+tel);
				for(int i=0;i<2;i++){  //目前先针对两个平台
					String url=getData.GetPerson_info(tel, i);
					if(url.length()>0)   //如果url不为空，则进行爬取
						getAction.actionHandling(i, url, tel);
					else{
						System.out.println(currentTag+i+":is null");
					}
				}
			}
		}
	}

}
