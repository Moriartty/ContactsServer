package Weibo;


import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import javax.print.attribute.standard.PrinterLocation;
import javax.swing.text.AbstractDocument.Content;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import DataHandling.DataFormat;
import cn.edu.hfut.dmic.webcollector.crawler.DeepCrawler;  
import cn.edu.hfut.dmic.webcollector.model.Links;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.net.HttpRequesterImpl;


public class WeiboCrlawer extends DeepCrawler{
	private static final String currentTag="WeiboCrlawer:";
	String CONTENT_TEXT="content_text";//发表的文字内容
	String CONTENT_PIC="content_img";//发表的图片内容
	String TIME="time";
	String FOCUS="focus";
	String NAME="name";
	String HEAD_IMG="head_img";
	ArrayList<HashMap<String, String>> content_list=new ArrayList<>();
	ArrayList<HashMap<String, String>> SQLList=new ArrayList<>();
	HashMap<String, String> other_info=new HashMap<>();
	
    public WeiboCrlawer(String crawlPath) throws Exception {
    	super(crawlPath); 
    	String cookie="_T_WM=fb51836e36a0577e91c3a0b1d3561db3; "
    			+ "SUB=_2A251P6l3DeTxGeNN6lMQ9y3IzT2IHXVWwzc_rDV6PUJbkdANLRLWkW07NXs1Mcvzw5MYo7hb_gZRgumUEA..; "
    			+ "gsid_CTandWM=4uKDa6911VWSiHCTAlVX9mhG57r; "
    			+ "PHPSESSID=c0b77e78fe72ab45e79a6bdbc1b82e7e";
	    HttpRequesterImpl myRequester=(HttpRequesterImpl) this.getHttpRequester();  
	    myRequester.setCookie(cookie);
    }  

    public Links visitAndGetNextLinks(Page page) { 
        Elements n=page.getDoc().select("div[class=c]");
        analyse(n);
        Elements head=page.getDoc().select("div[class=u]");
        getHead_img(head);
        getName(head);
        System.out.println(currentTag+getList().size());
        return null;
    }
    
    public void analyse(Elements elements){
    	HashMap<String, String> temp;
    	for(Element element:elements){
    		HashMap<String, String> recode=new HashMap<>();
    		String content_text=DataFormat.Format(getContent_Text(element));
    		recode.put(CONTENT_TEXT, content_text);
    		String content_img=getContent_Imgs(element);
    		recode.put(CONTENT_PIC, content_img);
    		String time=getTime(element);
    		recode.put(TIME, time);
    		String focus=getFocus(element);
    		if(focus!=null)
    			recode.put(FOCUS, focus);
    		content_list.add(recode);
    		recode.put("platform_type", "weibo");   //关于这里是传值还是传引用有待细究
    		//temp=recode;
    		//temp=recode.put("platform_type", "weibo");
    		SQLList.add(recode);
    	}
    }
    
    public String getContent_Text(Element element){
    	String content_text=element.select(".ctt").text();
    	//System.out.println(content_text);
    	return content_text;
    }
    
    public String getContent_Imgs(Element element){
    	Elements elements=element.select("img[class=ib]");
    	String content_img=elements.attr("src");
    	//System.out.println(elements);
    	return content_img;
    }
    
    public void getHead_img(Elements elements){
    	Elements elements2=elements.select("img");
    	String head_img=elements2.attr("src");
    	//System.out.println(head_img);
    	other_info.put(HEAD_IMG, head_img);
    	content_list.add(other_info);
    }
    
    public void getName(Elements elements){
    	Elements elements2=elements.select("span[class=ctt]");
    	String name=elements2.text();
    	//System.out.println(name);
    	other_info.put(NAME, name);
    	content_list.add(other_info);
    }
    
    public String getTime(Element element){
    	return element.select("span[class=ct]").text();
    }
    
    public ArrayList<HashMap<String, String>> getList(){
    	return content_list;
    }
    
    public ArrayList<HashMap<String, String>> getSQLList(){
    	return SQLList;
    }
    
    public String getFocus(Element element){
    	Elements elements=element.select("div");
    	String focus=null;
    	try{
    		if(elements.size()>=3)
    			focus=elements.get(2).select("a").text();
    	}catch(IndexOutOfBoundsException e){
    		e.printStackTrace();
    	}
    	//System.out.println(focus);
    	return focus;
    }
    /*public static void main(String[] args){
    	try{
    		WeiboCrlawer crawler=new WeiboCrlawer("a.txt");  
            crawler.setThreads(1);      
           
            crawler.addSeed("http://weibo.cn/u/3154080892");
            //crawler.addSeed("http://weibo.cn/?PHPSESSID=&vt=4");
            crawler.start(1);  
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	
    }*/
    
}
