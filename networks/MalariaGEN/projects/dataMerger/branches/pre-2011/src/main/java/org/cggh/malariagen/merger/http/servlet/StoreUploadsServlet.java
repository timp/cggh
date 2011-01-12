package org.cggh.malariagen.merger.http.servlet;


import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.abdera.Abdera;
import org.apache.abdera.model.Document;
import org.apache.abdera.model.Entry;
import org.apache.abdera.protocol.client.AbderaClient;
import org.apache.abdera.protocol.client.ClientResponse;
import org.apache.abdera.protocol.client.RequestOptions;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class StoreUploadsServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3242704358147122627L;
	private Abdera abdera = new Abdera();
	
	
	
	private static final DocumentBuilderFactory factory;
	private static DocumentBuilder builder;
	
	static {

		factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} 

	}

	@Override
	public void init(ServletConfig config) throws ServletException {

		if (builder == null) 
			throw new ServletException("document build is not initialised");

	}

	public StoreUploadsServlet() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse res) {
		
		try {
			
			org.w3c.dom.Document requestDocument = builder.parse(request.getInputStream());
			
			// get target collection uri
			Element requestDocumentElement = requestDocument.getDocumentElement();
			String targetCollectionUri = requestDocumentElement.getAttribute("target");
			
			// process uploads
			NodeList uploadNodeList = requestDocument.getElementsByTagName("upload");
			
			List<org.w3c.dom.Document> responseDocumentsList = new ArrayList<org.w3c.dom.Document>();
			
			for (int i=0; i<uploadNodeList.getLength(); i++) {
				
				Element uploadElement = (Element) uploadNodeList.item(i);
				org.w3c.dom.Document responseDocument = storeUpload(request, uploadElement, targetCollectionUri);
				responseDocumentsList.add(responseDocument);
				
			}
			
			// aggregate responses into a single document

			org.w3c.dom.Document aggregatedResponseDocument = builder.newDocument();
			Element aggregatedResponseDocumentRoot = aggregatedResponseDocument.createElementNS("http://www.w3.org/2005/Atom", "feed");
			aggregatedResponseDocument.appendChild(aggregatedResponseDocumentRoot);
			
			for (org.w3c.dom.Document responseDocument : responseDocumentsList) {
				Element documentElement = responseDocument.getDocumentElement();
				Node importedDocumentElement = aggregatedResponseDocument.importNode(documentElement, true);
				aggregatedResponseDocumentRoot.appendChild(importedDocumentElement);
			}
			
			// return aggregated document
			res.setContentType("application/atom+xml");
			res.setStatus(200);

			// Prepare the DOM document for writing 
			Source source = new DOMSource(aggregatedResponseDocument); 
			
			// Prepare the output  
			Result result = new StreamResult(res.getOutputStream()); 
			
			// Write the DOM document to the output 
			Transformer xformer = TransformerFactory.newInstance().newTransformer(); 
			xformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
	        xformer.setOutputProperty(OutputKeys.INDENT, "yes");
			xformer.transform(source, result); 
			
		} catch (BadRequestException e) {
		
			sendBadRequest(e.getLocalizedMessage(), res);
			
		} catch (SAXException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
		
		} catch (IOException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (Throwable t) {
			
			// TODO Auto-generated catch block
			t.printStackTrace();

		}

	}

	private org.w3c.dom.Document storeUpload(HttpServletRequest req, Element ue, String targetCollectionUri) throws BadRequestException {
		
		org.w3c.dom.Document returnDocument = null;
		
		String fileName = ue.getAttribute("filename");
		String mediaType = ue.getAttribute("mediatype");
				
		try {
			
			URL tempFileUrl = new URL(ue.getTextContent());
			InputStream uploadInputStream = tempFileUrl.openStream();
			
			AbderaClient atomClient = new AbderaClient();
			RequestOptions requestOptions = createRequestOptions(req);
            
            if (fileName != null) {
            	requestOptions.setHeader("Slug", fileName);
            }

            InputStreamRequestEntity uploadEntity = new InputStreamRequestEntity(uploadInputStream, mediaType);
            
            ClientResponse response = atomClient.post(targetCollectionUri, uploadEntity, requestOptions);
            
            if (response.getStatus() == 201) {
            	
    			// if success, assume atom, try to append type category and put back
            	Document<Entry> mediaLinkEntryDocument = response.getDocument(abdera.getParser());
            	Entry mediaLinkEntry = mediaLinkEntryDocument.getRoot();
            	String editLocation = mediaLinkEntry.getEditLink().getHref().toASCIIString();
            	
            	response = atomClient.put(editLocation, mediaLinkEntryDocument, createRequestOptions(req));
        		returnDocument = builder.parse(response.getInputStream());
            }
            
            else {

            	// error document
            	returnDocument = builder.parse(response.getInputStream());
            	
            }
            			
		} catch (MalformedURLException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (IOException e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return returnDocument;
	}

	private void sendBadRequest(String message, HttpServletResponse res) {
		// TODO Auto-generated method stub
		try {
			res.sendError(500, "TODO");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	
	private RequestOptions createRequestOptions(HttpServletRequest req) {
		RequestOptions requestOptions = new RequestOptions();
        String authorizationHeader = req.getHeader("Authorization");
        if (authorizationHeader != null) {
	        requestOptions.setAuthorization(authorizationHeader);
        }
        String cookieHeader = req.getHeader("Cookie");
        if (cookieHeader != null) {
        	requestOptions.setHeader("Cookie", cookieHeader);
        }
        return requestOptions;
	}
	
	
}
