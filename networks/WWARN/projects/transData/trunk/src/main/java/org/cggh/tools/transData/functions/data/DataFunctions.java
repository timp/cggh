package org.cggh.tools.transData.functions.data;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.TrustManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.cggh.tools.transData.data.fields.FieldModel;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class DataFunctions {
	
	private final Logger logger = Logger.getLogger(this.getClass().getPackage().getName());
	
	public Document convertDataAsInputStreamIntoDocument (InputStream inputStream) {
		
		Document dataAsDocument = null;
		
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        
        //These really speed-up the parsing
        documentBuilderFactory.setValidating(false);
        documentBuilderFactory.setNamespaceAware(false);

            
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

	public List<String> convertAcceptHeaderAsStringIntoHeaderAcceptsAsStringList(String acceptHeaderAsString) {
		
		
		List<String> headerAcceptsAsStringList = null;
		
		//
		//this.getLogger().info(request.getHeader("Accept"));
		//expecting "text/plain, */*; q=0.01"
		
		if (acceptHeaderAsString != null) {
			
			String[] headerAcceptsAsStringArray = acceptHeaderAsString.split(",");
			
			for (int i = 0; i < headerAcceptsAsStringArray.length; i++) {
				
				if (headerAcceptsAsStringArray[i].contains(";")) {
				
					headerAcceptsAsStringArray[i] = headerAcceptsAsStringArray[i].substring(0, headerAcceptsAsStringArray[i].indexOf(";"));
				}
				
				headerAcceptsAsStringArray[i] = headerAcceptsAsStringArray[i].trim();
				
				//
				//this.getLogger().info("will handle: " + headerAcceptsAsStringArray[i]);
			}
			
			headerAcceptsAsStringList = Arrays.asList(headerAcceptsAsStringArray);
			
		} else {
			headerAcceptsAsStringList = new ArrayList<String>();
		}
		
			
		
		return headerAcceptsAsStringList;
	}

	public String convertXMLFromURLIntoCSVRowsWithXPathFieldLabelsAsStringUsingUrlAsString(String urlAsString) {
		
		String dataAsCSVRowsString = null;

		
		 try {
				URL urlAsUrl = new URL(urlAsString);
				
				InputStream urlAsInputStream = urlAsUrl.openStream();
				Document xmlAsDocument = this.convertDataAsInputStreamIntoDocument(urlAsInputStream);
				
				//Don't need this any more
				urlAsInputStream.close();
				
//				var parentNodeBaseXPath = "atom:entry[1]";
//				
//				var nodeNamesToIgnoreAsAssociativeArray = {
//						"#text" : true,
//					    "atom:link" : true, 
//					    "wizard-pane-to-show" : true, 
//					    "ar:comment" : true,
//					    "app:control" : true
//				};
//				
//				studyAsObjectArrayWithXPathFieldLabels = convertXmlNodesIntoObjectArrayWithXPathFieldLabels(studyAsAtomEntryXml.documentElement.childNodes, parentNodeBaseXPath, nodeNamesToIgnoreAsAssociativeArray);
//
//				
				////////////////////////////////
				
				//TODO: Remove unwanted nodes (and ones with no value) afterwards (separate concern)
				
				//String parentNodeBaseXPathAsString = "atom:entry[1]";
				String parentNodeBaseXPathAsString = xmlAsDocument.getNodeName();
				
				ArrayList<FieldModel> dataAsFieldModelArrayListWithXpathFieldLabels = this.convertNodeListIntoFieldModelArrayListWithXPathFieldLabels(xmlAsDocument.getChildNodes(), parentNodeBaseXPathAsString);
				
				//var studyAsObjectArrayWithStudyCustomFieldLabels = addCustomFieldLabelPropertiesToObjectArrayWithXPathFieldLabelsUsingRegExpMapAsAssociativeArray(studyAsObjectArrayWithXPathFieldLabels, retrieveStudyCustomFieldLabelsRegExMapAsAssociativeArray());
				
				//TODO: code
				//DataCRUD dataCRUD = new DataCRUD();
				//HashMap<String, String> regExpMapAsXpathFieldLabelPatternKeyedHashMap = new HashMap<String, String>();
				//ArrayList<FieldModel> dataAsFieldModelArrayListWithMappedFieldLabels = addMappedFieldLabelsToFieldModelArrayListUsingRegExpMapAsXpathFieldLabelPatternKeyedHashMap(dataAsFieldModelArrayListWithXpathFieldLabels, regExpMapAsXpathFieldLabelPatternKeyedHashMap);

				//TEMP: Check that the basic conversion is working. 
				
				dataAsCSVRowsString = this.convertDataAsFieldModelArrayListIntoCSVRowsString(dataAsFieldModelArrayListWithXpathFieldLabels); 
				
				///////////////////////////////
				
				
		    } catch (Exception e) {
				e.printStackTrace();
			}
		
		return dataAsCSVRowsString;
	}

	public String convertDataAsFieldModelArrayListIntoCSVRowsString(
			ArrayList<FieldModel> dataAsFieldModelArrayListWithXpathFieldLabels) {

		String dataAsCSVRowsString = null;

		
		StringBuilder dataAsCSVStringBuilder = new StringBuilder();
		
		for (int i = 0; i < dataAsFieldModelArrayListWithXpathFieldLabels.size(); i ++) {
			
			//TODO: change to use mapped field name
			
			dataAsCSVStringBuilder.append(dataAsFieldModelArrayListWithXpathFieldLabels.get(i).getParentNodeName()).append(",").append(dataAsFieldModelArrayListWithXpathFieldLabels.get(i).getNodeValue());
			
			dataAsCSVStringBuilder.append("\n");
		}
		
		dataAsCSVRowsString = dataAsCSVStringBuilder.toString();		
		
		return dataAsCSVRowsString;
	}

	public ArrayList<FieldModel> convertNodeListIntoFieldModelArrayListWithXPathFieldLabels(NodeList nodeList, String parentNodeBaseXPathAsString) {
		
		
		ArrayList<FieldModel> fieldModelArrayList = null;
		
		fieldModelArrayList = new ArrayList<FieldModel>();
		
		if (parentNodeBaseXPathAsString == null) {
			parentNodeBaseXPathAsString = "";
		}

		if (nodeList.getLength() == 1 && nodeList.item(0).getNodeType() == 3) {
			
			//TODO: consider this instead of the mysterious node type 3 
			//!nodeList.item(0).hasChildNodes()
			// or NodeName = #text ?
			
			// node is a leaf
			
			//Note: don't ignore empty nodes, remove later (separate concern)
			
			FieldModel fieldModel = new FieldModel();
			
			//TODO:remove
			this.getLogger().info(nodeList.item(0).getNodeName());
			this.getLogger().info(nodeList.item(0).getParentNode().getNodeName());
			
			
			fieldModel.setParentNodeName(nodeList.item(0).getParentNode().getNodeName());
			fieldModel.setNodeValue(nodeList.item(0).getNodeValue());
			fieldModel.setXPathFieldLabel(parentNodeBaseXPathAsString);
			
			fieldModelArrayList.add(fieldModel);
			
		} else {
			
			// node is a branch
			
			HashMap<String, Integer> nodeSiblingCountAsNodeNameKeyedHashMap = new HashMap<String, Integer>();
			
			for (int i = 0; i < nodeList.getLength(); i++) {
				
				if (nodeSiblingCountAsNodeNameKeyedHashMap.containsKey(nodeList.item(i).getParentNode().getNodeName())) {
					
					nodeSiblingCountAsNodeNameKeyedHashMap.put(nodeList.item(i).getParentNode().getNodeName(), nodeSiblingCountAsNodeNameKeyedHashMap.get(nodeList.item(i).getParentNode().getNodeName()).intValue() + 1);
					
				} else {

					nodeSiblingCountAsNodeNameKeyedHashMap.put(nodeList.item(i).getParentNode().getNodeName(), 1);
					
				}
				
				String nodeBaseXPathAsString = parentNodeBaseXPathAsString + "/" + nodeList.item(i).getParentNode().getNodeName() + "[" + nodeSiblingCountAsNodeNameKeyedHashMap.get(nodeList.item(i).getParentNode().getNodeName()).intValue() + "]";
				
				// More verbosely...
				//ArrayList<FieldModel> childNodesAsFieldModelArrayListWithXPathFieldLabels = convertNodeListIntoFieldModelArrayListWithXPathFieldLabels(nodeList.item(i).getChildNodes(), nodeBaseXPathAsString);
				
				fieldModelArrayList.addAll(convertNodeListIntoFieldModelArrayListWithXPathFieldLabels(nodeList.item(i).getChildNodes(), nodeBaseXPathAsString));
				
			}
			
			
		}

		return fieldModelArrayList;
	}


	public Boolean isInvalidURL(String urlAsString) {
		
		Boolean isInvalidURL = null; 
		
		try {
			URL urlAsUrl = new URL(urlAsString);
			@SuppressWarnings("unused")
			InputStream urlAsInputStream = urlAsUrl.openStream();
			isInvalidURL = false;
			
		} catch (UnknownHostException unknownHostException) {

			isInvalidURL = true;
			
		} catch (MalformedURLException e) {

			isInvalidURL = true;
			
		} catch (SSLHandshakeException e) {
			
			//
			//e.printStackTrace();
			
			//FIXME: This could be dangerous.
			isInvalidURL = false;
			
		} catch (IOException e) {

			//FIXME: This could be an IO error, not a URL error.
			isInvalidURL = true;
		}
		
		return isInvalidURL;
	}

	public String convertURLAsStringIntoFileName(String urlAsString) {

		String fileName = null;
		
		String originalFileName = urlAsString.substring(urlAsString.lastIndexOf("/") + 1, urlAsString.length());
		
		String originalFileNameWithoutExtension = originalFileName.substring(0, originalFileName.lastIndexOf("."));
		
		fileName = originalFileNameWithoutExtension + ".csv";

		return fileName;
	}
	
	
}
