package DataHandling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class XMLToMap {
	 public static Map xml2map(String xmlStr, boolean needRootKey) throws DocumentException {  
	        Document doc = DocumentHelper.parseText(xmlStr);  
	        Element root = doc.getRootElement();  
	        Map<String, Object> map = (Map<String, Object>) xml2map(root);  
	        if(root.elements().size()==0 && root.attributes().size()==0){  
	            return map;  
	        }  
	        if(needRootKey){  
	            //在返回的map里加根节点键（如果需要）  
	            Map<String, Object> rootMap = new HashMap<String, Object>();  
	            rootMap.put(root.getName(), map);  
	            return rootMap;  
	        }  
	        return map;  
	   }  
	 
	 private static Map xml2map(Element e) {  
	        Map map = new LinkedHashMap();  
	        List list = e.elements();  
	        if (list.size() > 0) {  
	            for (int i = 0; i < list.size(); i++) {  
	                Element iter = (Element) list.get(i);  
	                List mapList = new ArrayList();  
	  
	                if (iter.elements().size() > 0) {  
	                    Map m = xml2map(iter);  
	                    if (map.get(iter.getName()) != null) {  
	                        Object obj = map.get(iter.getName());  
	                        if (!(obj instanceof List)) {  
	                            mapList = new ArrayList();  
	                            mapList.add(obj);  
	                            mapList.add(m);  
	                        }  
	                        if (obj instanceof List) {  
	                            mapList = (List) obj;  
	                            mapList.add(m);  
	                        }  
	                        map.put(iter.getName(), mapList);  
	                    } else  
	                        map.put(iter.getName(), m);  
	                } else {  
	                    if (map.get(iter.getName()) != null) {  
	                        Object obj = map.get(iter.getName());  
	                        if (!(obj instanceof List)) {  
	                            mapList = new ArrayList();  
	                            mapList.add(obj);  
	                            mapList.add(iter.getText());  
	                        }  
	                        if (obj instanceof List) {  
	                            mapList = (List) obj;  
	                            mapList.add(iter.getText());  
	                        }  
	                        map.put(iter.getName(), mapList);  
	                    } else  
	                        map.put(iter.getName(), iter.getText());  
	                }  
	            }  
	        } else  
	            map.put(e.getName(), e.getText());  
	        return map;  
	    }  

}
