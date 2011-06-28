package org.cggh.tools.transData.data.fields;

public class FieldModel {

	private String parentNodeName;
	private String nodeName;
	private String nodeValue;
	private String xPathFieldLabel;
	
	

	public void setNodeValue(String nodeValue) {
		this.nodeValue = nodeValue;
	}
	public String getNodeValue() {
		return nodeValue;
	}
	public void setXPathFieldLabel(String xPathFieldLabel) {
		this.xPathFieldLabel = xPathFieldLabel;
	}
	public String getXPathFieldLabel() {
		return xPathFieldLabel;
	}
	public void setParentNodeName(String parentNodeName) {
		this.parentNodeName = parentNodeName;
	}
	public String getParentNodeName() {
		return parentNodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	public String getNodeName() {
		return nodeName;
	}

	
}
