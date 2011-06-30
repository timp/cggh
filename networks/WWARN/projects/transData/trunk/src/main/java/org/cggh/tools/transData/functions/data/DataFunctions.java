package org.cggh.tools.transData.functions.data;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import javax.net.ssl.SSLHandshakeException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.cggh.tools.transData.data.fields.FieldModel;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class DataFunctions {
	
	private final Logger logger = Logger.getLogger(this.getClass().getPackage().getName());


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


	public String convertDataAsFieldModelArrayListIntoCSVRowsWithMappedCustomFieldLabelsAsString(
			ArrayList<FieldModel> dataAsFieldModelArrayListWithXpathFieldLabels) {

		String dataAsCSVRowsString = null;

		//NOTE: Enclose value in quotes.
		//NOTE: Replace EOL characters with spaces. 
		//NOTE: Use Unix EOL character.
		
		
		//TODO: Get the mapped values as a HashMap from the config collection using URL (hard-coded?)
		//https://wwarn-app3.nsms.ox.ac.uk/repository/service/content/config/explorer-display-types
		//https://wwarn-app3.nsms.ox.ac.uk/repository/service/content/config/study-field-mappings
		
		///////////
		HashMap<String, String> fieldLabelRegExpMappingsAsPatternKeyedHashMap = new HashMap<String, String>();
		
		//TODO: replace with sourced values
		fieldLabelRegExpMappingsAsPatternKeyedHashMap.put("/breakfast_menu\\[(\\d+)\\]/food\\[(\\d+)\\]/name\\[(\\d+)\\]", "Food$2Name");
		
		
		//////////
		
		StringBuilder dataAsCSVStringBuilder = new StringBuilder();
		
		for (int i = 0; i < dataAsFieldModelArrayListWithXpathFieldLabels.size(); i ++) {
			
			StringBuffer labelAsStringBuffer = new StringBuffer("\"");
			StringBuffer valueAsStringBuffer = new StringBuffer("\"");
			
			
			//TODO: translate into mapped values
			
			/////////
			
			Boolean matchFound = null;
		
			
			for (String regExpKey : fieldLabelRegExpMappingsAsPatternKeyedHashMap.keySet()) {

				
				Pattern pattern = Pattern.compile(regExpKey);
				Matcher matcher = pattern.matcher(dataAsFieldModelArrayListWithXpathFieldLabels.get(i).getXPathFieldLabel());

				if (matcher.matches()) {
					
					matchFound = true;
					
					labelAsStringBuffer.append(matcher.replaceAll(fieldLabelRegExpMappingsAsPatternKeyedHashMap.get(regExpKey))).append("\"");
					
					
				}
				
			}
			
			
			if (matchFound == null || matchFound != true) {
				matchFound = false;
			}
			
			//NOTE: Keep unmatched xPathFieldLabels
			if (matchFound == false) {
				
				labelAsStringBuffer.append(dataAsFieldModelArrayListWithXpathFieldLabels.get(i).getXPathFieldLabel()).append("\"");
			}
			
			/////////
			
			
			
			valueAsStringBuffer.append(dataAsFieldModelArrayListWithXpathFieldLabels.get(i).getNodeValue().replaceAll("\\n|\\r", " ").replaceAll("\"", "\"\"")).append("\"");
			
			dataAsCSVStringBuilder.append(labelAsStringBuffer.toString()).append(",").append(valueAsStringBuffer);
			
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
			
			//Note: Old javascript condition didn't work for java: nodeList.getLength() == 1 && nodeList.item(0).getNodeType() == 3
		
			// node is a leaf
			
			//Note: don't ignore empty nodes, remove later (separate concern)
			
			FieldModel fieldModel = new FieldModel();
			
			fieldModel.setParentNodeName(nodeList.item(0).getParentNode().getNodeName());
			fieldModel.setNodeName(nodeList.item(0).getNodeName());
			fieldModel.setNodeValue(nodeList.item(0).getNodeValue());
			fieldModel.setXPathFieldLabel(parentNodeBaseXPathAsString);
			
			fieldModelArrayList.add(fieldModel);
			
		} else {
			
			// node is a branch
			
			HashMap<String, Integer> nodeSiblingCountAsNodeNameKeyedHashMap = new HashMap<String, Integer>();
			
			for (int i = 0; i < nodeList.getLength(); i++) {
				
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
			
			//TODO: comment-out
			e.printStackTrace();
			
			//Note: This happens when the certificate is invalid.
			
			isInvalidURL = true;
			
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

	public String convertUrlAsStringIntoUrlResponseAsString(String urlAsString) throws IOException {

		StringBuffer urlResponseAsStringBuffer = new StringBuffer();
		
			
			URL urlAsURL = new URL(urlAsString);
			
			URLConnection urlConnection = urlAsURL.openConnection();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
			String line;

	        while ((line = bufferedReader.readLine()) != null) 
	        	urlResponseAsStringBuffer.append(line);
	        bufferedReader.close();
			
		
		return urlResponseAsStringBuffer.toString();
	}

	public Document convertXmlAsStringIntoXmlAsDocument(String urlResponseAsString) throws ParserConfigurationException, SAXException, IOException {

		Document xmlAsDocument = null;
		
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        InputSource inputSource = new InputSource(new StringReader(urlResponseAsString));
        xmlAsDocument = documentBuilder.parse(inputSource);
        
        return xmlAsDocument;
        
		
	}
	
	
}
