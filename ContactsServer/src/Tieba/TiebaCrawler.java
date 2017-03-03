package Tieba;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.el.ELParseException;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cn.edu.hfut.dmic.webcollector.crawler.BreadthCrawler;
import cn.edu.hfut.dmic.webcollector.model.Links;
import cn.edu.hfut.dmic.webcollector.model.Page;


public class TiebaCrawler extends BreadthCrawler {
	private static final String currentTag="TiebaCrawler:";
	String ti;
	String url;
	String[] temp;
	HashMap<String,String> map=new HashMap();
	public TiebaCrawler(String crawlPath, boolean autoParse,String url) {
		super(crawlPath, autoParse);
		this.url=url;
		temp=url.split("#");
	}

	public void visit(Page page, Links arg1) {
		Elements n=page.getDoc().select(".l_post.l_post_bright.j_l_post.clearfix");
		Element topic=n.get(1);
		Elements title=page.getDoc().select("div[id=j_core_title_wrap]");
		String topic_text=getTitleText(title)+"。"+getTopicContent_Text(topic);
		map.put("topic_text", topic_text);
		System.out.println(currentTag+"Topic_text:"+topic_text);
		
		ArrayList<String> topic_img=getTopicContent_Img(topic);
		putTopic_imgList2Map(topic_img);
		System.out.println(currentTag+topic_img.size());
		
		Element targetElement=getTarget(n);
		getContentText(targetElement);
		ArrayList<String> reply_img=getReplyContent_Img(targetElement);
		putReply_imgList2Map(reply_img);
		//getMyReply(targetElement);
	}
	public void putTopic_imgList2Map(ArrayList<String> topic_img){    //将帖子主题中的图片从attaiList放入hashmap中
		for(int i=0;i<topic_img.size();i++){
			String key="topic_img"+Integer.toString(i);
			System.out.println(currentTag+key);
			map.put(key, topic_img.get(i));
		}
	}  
	public void putReply_imgList2Map(ArrayList<String> reply_img){  //将回复内容中的图片从arrayList放入hashmap中
		for(int i=0;i<reply_img.size();i++){
			String key="reply_img"+Integer.toString(i);
			map.put(key, reply_img.get(i));
		}
	}
	public Element getTarget(Elements elements){
		int num=0;
		Elements elements2;
		for(Element element:elements){
			if(element.select("[name]").attr("name").equals(temp[1])){
				return element;
			}
		}
		return null;
	}
	
	public String getTitleText(Elements elements){
		//System.out.println(elements.text());
		Elements title=elements.select("h3");
		return title.text();
	}
	public void getContentText(Element element){   //获得回复的文字内容
		String tmp="div[id=post_content_"+temp[1]+"]";
		String text=element.select(tmp).text();
		map.put("content_text", text);
		System.out.println(currentTag+"content_txt:"+text);
	}
	public ArrayList<String> getTopicContent_Img(Element topic){    //获得帖子的话题和图片
		Elements elements2=topic.select("cc");
		Elements pic=elements2.select("img");
		ArrayList<String> topic_img=new ArrayList<>();
		for(Element element:pic){
			topic_img.add(element.attr("src"));
		}
		return topic_img;
	}
	public ArrayList<String> getReplyContent_Img(Element element){
		Elements elements=element.select("cc");
		Elements elements2=elements.select("img[class=BDE_Image]");
		ArrayList<String> reply_img_list=new ArrayList<>();
		for(Element element2:elements2){
			reply_img_list.add(element2.attr("src"));
		}
		//System.out.println(reply_img_list.size());
		return reply_img_list;
	}
	public String getTopicContent_Text(Element element){
		return element.select("cc").text();
	}
	public void getMyReply(Element element){
		//String temp=element.select(".lzl_content_main").text();
		Elements elements=element.select(".d_post_content_main ");
		Elements elements2=elements.select(".j_lzl_container.core_reply_wrapper");
		Elements elements3=elements2.select(".j_lzl_c_b_a.core_reply_content");
		System.out.println(currentTag+elements2);
	}
	public HashMap<String, String> getMap(){
		return map;
	}
	/*public static void main(String[] args) {
		// TODO Auto-generated method stub
		String URL="http://tieba.baidu.com/p/4833672077?pid=99465452963&cid=#99465452963";
		TiebaCrawler crawler=new TiebaCrawler("a",true,URL);
        crawler.setThreads(1);
        crawler.addSeed(URL);
        try{
        	 crawler.start(1);
        }catch(Exception e){
        	e.printStackTrace();
        }
	}*/
	
	
}
