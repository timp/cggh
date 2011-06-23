package org.cggh.tools.transData.functions.files;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class FileFunctions {
	
	private final Logger logger = Logger.getLogger(this.getClass().getPackage().getName());
	
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
	
	public Document convertDataAsInputStreamIntoDocument (InputStream inputStream) {
		
			Document dataAsDocument = null;
			
	        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            //documentBuilderFactory.setValidating(false);
            //documentBuilderFactory.setNamespaceAware(false);
	            
	            try {
	            	
	            	DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
					dataAsDocument = documentBuilder.parse(inputStream);
	            
				} catch (ParserConfigurationException e) {
					e.printStackTrace();
				} catch (SAXException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

	        return dataAsDocument;
	}

	public Logger getLogger() {
		return logger;
	}
	
	
}
