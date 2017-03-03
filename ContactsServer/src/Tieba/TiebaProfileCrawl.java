package Tieba;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.bcel.generic.RETURN;
import org.apache.xpath.operations.Div;
import org.bouncycastle.jcajce.provider.asymmetric.dsa.DSASigner.stdDSA;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



import cn.edu.hfut.dmic.webcollector.crawler.BreadthCrawler;
import cn.edu.hfut.dmic.webcollector.model.Links;
import cn.edu.hfut.dmic.webcollector.model.Page;


public class TiebaProfileCrawl extends BreadthCrawler {
	    public final static String TIME=".n_post_time";
	    public final static String ADDRESS=".n_name";
	    public final static String TITLE=".title";
	    public final static String CONTENT=".n_txt";
	    public final static String REPLY_CONTENT=".reply_content";
	    public final static String TITLETXT=".titletxt";
	    public final static String URL="href";
	    private static final String currentTag="TiebaProfileCrawl:";
	    
	    ArrayList<HashMap<String, String>> tiebaList=new ArrayList<>();
	    ArrayList<HashMap<String,String>> SQLList=new ArrayList<>();
	    HashMap<String, String> other_info=new HashMap<>();
	    public TiebaProfileCrawl(String crawlPath, boolean autoParse) {
		    super(crawlPath, autoParse);
    	}  
	
		public void visit(Page page, Links arg1) {
			Elements first_contain=page.getDoc().select("div [class=n_right n_right_first clearfix]");
			Elements all_contains=page.getDoc().select("div [class=n_right clearfix]");
			analyse(first_contain);
			analyse(all_contains);
			//System.out.println(tiebaList.size());
			Elements head_img=page.getDoc().select("a[class=userinfo_head]");
			getImg(head_img);
		}
		public boolean isTitleNote(Element element){
			if(!element.select(TITLE).text().equals(""))
				return true;
			else
				return false;
		}
		
		public void getImg(Elements elements){
			String img;
			Elements elements2=elements.select("img");
			//System.out.println(elements2);
			img=elements2.attr("src");
			other_info.put("head_img", img);
			tiebaList.add(other_info);
		}
		
		public ArrayList<HashMap<String, String>> getList(){
			System.out.println(currentTag+tiebaList.size());
			return tiebaList;
		}
		public ArrayList<HashMap<String, String>> getSQLList(){
			return SQLList;
		}
		
		public void analyse(Elements elements){
			for(Element element:elements){
				HashMap<String, String> temp1;     //ArrayList.add是添加引用地址而不是值
				HashMap<String, String> temp2=new HashMap<>();
				if(isTitleNote(element)){
					temp1=analyse_TitleNote(element);
					temp2.putAll(temp1);
					tiebaList.add(temp1);
				}
				else{
					temp1=analyse_ReplyNote(element);
					temp2.putAll(temp1);
					tiebaList.add(temp1);
				}
				temp2.remove("url");
				temp2.put("platform_type", "tieba");
				SQLList.add(temp2);
			}
		}
		
		public HashMap<String, String> analyse_TitleNote(Element element){
			String time_text=element.select(TIME).text();
			if(!time_text.contains("-")){
				time_text=getCurrentTime()+time_text;
				System.out.println(currentTag+time_text);
			}
			String title_text=element.select(TITLE).text();
			String address_text=element.select(ADDRESS).text();
			String content_text=element.select(CONTENT).text();
			String url=getHrefWithTitle(element);
			HashMap<String, String> recode_first=new HashMap<>();
			recode_first.put("time",time_text);
			recode_first.put("address", address_text);
			recode_first.put("title", title_text);
			recode_first.put("content", content_text);
			recode_first.put("url", url);
			return recode_first;
		}
		
		public HashMap<String, String> analyse_ReplyNote(Element element){
			String time_text=element.select(TIME).text();
			if(!time_text.contains("-")){
				time_text=getCurrentTime()+time_text;
				System.out.println(currentTag+time_text);
			}
			String reply_content_text=element.select(REPLY_CONTENT).text();
			String address_text=element.select(ADDRESS).text();
			String titletxt_text=element.select(TITLETXT).text();
			String url=getHrefWithReply(element);
			HashMap<String, String> recode=new HashMap<>();
			recode.put("time",time_text);
			recode.put("address", address_text);
			recode.put("reply_content",reply_content_text);
			recode.put("titletxt",titletxt_text);
			recode.put("url", url);
			return recode;
		}
		
		private String getHrefWithReply(Element element){
			Elements elements=element.select(REPLY_CONTENT);
			String url=elements.attr(URL);
			return "http://tieba.baidu.com"+url;
			//System.out.println(result);
		}
		private String getHrefWithTitle(Element element){
			Elements elements=element.select(TITLE);
			String url=element.attr(URL);
			return "http://tieba.baidu.com"+url;
		}
		public String getCurrentTime(){
			Date day=new Date();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd ");
			//System.out.println(df.format(day));
			return df.format(day);
		}
		
		
		/*public static void main(String[] args){
			String URL="http://tieba.baidu.com/home/main?un=LOL%E6%88%91%E7%9A%84%E9%94%85&ie=utf-8&fr=pb";
			TiebaProfileCrawl crawler=new TiebaProfileCrawl("a",true);
	        crawler.setThreads(1);
	        crawler.addSeed(URL);
	        try{
	        	 crawler.start(1);
	        }catch(Exception e){
	        	e.printStackTrace();
	        }
	    }*/
}
