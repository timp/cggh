package org.cggh.tools.transData.functions.data;

import java.util.HashMap;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DataFunctions {

	
	public HashMap<String, Integer> convertDataAsDocumentIntoHashMap (Document dataAsDocument) {
		
		HashMap<String, Integer> dataAsHashMap = new HashMap<String, Integer>();
		
		     NodeList nodes = dataAsDocument.getElementsByTagName("topic");
		     
		     for (int i = 0; i < nodes.getLength(); i++) {
		    	 
		       Element element = (Element) nodes.item(i);

		       NodeList title = element.getElementsByTagName("title");
		       
		       Element line = (Element) title.item(0);

		       System.out.println("Title: " + getCharacterDataFromElement(line));

		       NodeList url = element.getElementsByTagName("url");
		       line = (Element) url.item(0);
		       System.out.println("Url: " + getCharacterDataFromElement(line));

		     }
		   
		
		return dataAsHashMap;
	}
	
	 public static String getCharacterDataFromElement(Element e) {
		 
		   Node child = e.getFirstChild();
		   if (child instanceof CharacterData) {
		     CharacterData cd = (CharacterData) child;
		       return cd.getData();
		     }
		   return "?";
		 }
	
	
}
