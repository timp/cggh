package org.cggh.tools.transData.functions.data;


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


import javax.net.ssl.SSLHandshakeException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.cggh.tools.transData.data.fields.FieldModel;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class DataFunctions {
	
	private final Logger logger = Logger.getLogger(this.getClass().getPackage().getName());
	
	//DOM method
	public Document convertDataAsInputStreamIntoDocument (InputStream inputStream) throws SAXException, IOException, ParserConfigurationException {
		
		Document dataAsDocument = null;
		
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        
        //These really speed-up the parsing
        documentBuilderFactory.setValidating(false);
        documentBuilderFactory.setNamespaceAware(false);

    	DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		dataAsDocument = documentBuilder.parse(inputStream);


        return dataAsDocument;
	}
	
	//SAX method
	public XMLReader convertDataAsInputStreamIntoXMLReader (InputStream inputStream) throws SAXException, IOException {
		
		XMLReader dataAsXMLReader = null;
		
		dataAsXMLReader = XMLReaderFactory.createXMLReader();
		//dataAsXMLReader.setContentHandler(handler);
		dataAsXMLReader.parse(new InputSource(inputStream));


        return dataAsXMLReader;
	}
	

	public Logger getLogger() {
		return logger;
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
				
				//TODO: improve
				String parseMethod = "DOM";
				
				if (parseMethod.equals("DOM")) {
					
					try {
						Document xmlAsDocument = this.convertDataAsInputStreamIntoDocument(urlAsInputStream);
						
						
						if (xmlAsDocument != null) {
	
							//TODO: Remove unwanted nodes (and ones with no value) afterwards (separate concern)
							
							//String parentNodeBaseXPathAsString = "atom:entry[1]";
			
							//FIXME: get off on the right foot
							String parentNodeBaseXPathAsString = null;//xmlAsDocument.getParentNode().getNodeName();
							
							ArrayList<FieldModel> dataAsFieldModelArrayListWithXpathFieldLabels = this.convertNodeListIntoFieldModelArrayListWithXPathFieldLabels(xmlAsDocument.getChildNodes(), parentNodeBaseXPathAsString);
							
							//var studyAsObjectArrayWithStudyCustomFieldLabels = addCustomFieldLabelPropertiesToObjectArrayWithXPathFieldLabelsUsingRegExpMapAsAssociativeArray(studyAsObjectArrayWithXPathFieldLabels, retrieveStudyCustomFieldLabelsRegExMapAsAssociativeArray());
							
							//TODO: code
							//DataCRUD dataCRUD = new DataCRUD();
							//HashMap<String, String> regExpMapAsXpathFieldLabelPatternKeyedHashMap = new HashMap<String, String>();
							//ArrayList<FieldModel> dataAsFieldModelArrayListWithMappedFieldLabels = addMappedFieldLabelsToFieldModelArrayListUsingRegExpMapAsXpathFieldLabelPatternKeyedHashMap(dataAsFieldModelArrayListWithXpathFieldLabels, regExpMapAsXpathFieldLabelPatternKeyedHashMap);
			
							dataAsCSVRowsString = this.convertDataAsFieldModelArrayListIntoCSVRowsWithXpathFieldLabelsAsString(dataAsFieldModelArrayListWithXpathFieldLabels); 
							
							
						} else {
							
							this.getLogger().severe("xmlAsDocument is null");
						}
						
					} catch (SAXException e) {
						
						this.getLogger().severe("SAX Exception");
						
					} catch (IOException e) {
						
						this.getLogger().severe("IO Exception");
						
					} catch (ParserConfigurationException e) {
	
						this.getLogger().severe("Parser Configuration Exception");
					}

				} 
				else if (parseMethod.equals("SAX")) {
					
					
					XMLReader xmlAsXMLReader = this.convertDataAsInputStreamIntoXMLReader(urlAsInputStream);
					
					//TODO: code
					
				} else {
					
					this.getLogger().severe("Unhandled parse method.");
				}
				
				urlAsInputStream.close();
				
				
					
		    } catch (Exception e) {
				e.printStackTrace();
			}
		
		return dataAsCSVRowsString;
	}

	public String convertDataAsFieldModelArrayListIntoCSVRowsWithXpathFieldLabelsAsString(
			ArrayList<FieldModel> dataAsFieldModelArrayListWithXpathFieldLabels) {

		String dataAsCSVRowsString = null;

		
		StringBuilder dataAsCSVStringBuilder = new StringBuilder();
		
		for (int i = 0; i < dataAsFieldModelArrayListWithXpathFieldLabels.size(); i ++) {
			
			dataAsCSVStringBuilder.append(dataAsFieldModelArrayListWithXpathFieldLabels.get(i).getXPathFieldLabel()).append(",").append(dataAsFieldModelArrayListWithXpathFieldLabels.get(i).getNodeValue());
			
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

		if (nodeList.getLength() == 1 && nodeList.item(0).getNodeName().equals("#text")) {
			
			//Old condition, didn't work: nodeList.getLength() == 1 && nodeList.item(0).getNodeType() == 3
			
			//TODO: consider this instead of the mysterious node type 3 
			//!nodeList.item(0).hasChildNodes()
			// or NodeName = #text ?
			
			// node is a leaf
			
			//Note: don't ignore empty nodes, remove later (separate concern)
			
			FieldModel fieldModel = new FieldModel();
			
			//TODO:com-out
			this.getLogger().info("leaf: " + nodeList.item(0).getNodeName());
			this.getLogger().info("leaf parent: " + nodeList.item(0).getParentNode().getNodeName());
			
			
			fieldModel.setParentNodeName(nodeList.item(0).getParentNode().getNodeName());
			fieldModel.setNodeName(nodeList.item(0).getNodeName());
			fieldModel.setNodeValue(nodeList.item(0).getNodeValue());
			fieldModel.setXPathFieldLabel(parentNodeBaseXPathAsString);
			
			fieldModelArrayList.add(fieldModel);
			
		} else {
			
			// node is a branch
			
			HashMap<String, Integer> nodeSiblingCountAsNodeNameKeyedHashMap = new HashMap<String, Integer>();
			
			for (int i = 0; i < nodeList.getLength(); i++) {
				
				
				//TODO:com-out
				this.getLogger().info("branch: " + nodeList.item(i).getNodeName());
				this.getLogger().info("branch parent: " + nodeList.item(i).getParentNode().getNodeName());
				
				
				if (nodeSiblingCountAsNodeNameKeyedHashMap.containsKey(nodeList.item(i).getNodeName())) {
					
					nodeSiblingCountAsNodeNameKeyedHashMap.put(nodeList.item(i).getNodeName(), nodeSiblingCountAsNodeNameKeyedHashMap.get(nodeList.item(i).getNodeName()).intValue() + 1);
					
				} else {

					nodeSiblingCountAsNodeNameKeyedHashMap.put(nodeList.item(i).getNodeName(), 1);
					
				}
				
				String nodeBaseXPathAsString = parentNodeBaseXPathAsString + "/" + nodeList.item(i).getNodeName() + "[" + nodeSiblingCountAsNodeNameKeyedHashMap.get(nodeList.item(i).getNodeName()).intValue() + "]";
				
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
