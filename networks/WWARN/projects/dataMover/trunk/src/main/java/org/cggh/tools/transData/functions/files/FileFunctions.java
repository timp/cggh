package org.cggh.tools.transData.functions.files;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class FileFunctions {
	
	
	public Document convertDataAsFileIntoDocument (File dataAsFile) {
		
		Document fileAsDocument = null;
		
		try {
			fileAsDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(dataAsFile);
		} catch (SAXException e) {
		
			e.printStackTrace();
		} catch (IOException e) {
		
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
		
			e.printStackTrace();
		}
		
		return fileAsDocument;
		
		
	}
	

}
