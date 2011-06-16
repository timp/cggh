package org.cggh.tools.dataMerger.functions.data.resolutions;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.cggh.tools.dataMerger.data.merges.MergeModel;

public class ResolutionsFunctions implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7663105154570885669L;
	private final Logger logger = Logger.getLogger("org.cggh.tools.dataMerger.functions.data.resolutions");
	private String urlBasePath;
	private MergeModel mergeModel;
	private HttpServletRequest httpServletRequest;
	
	
	public ResolutionsFunctions () {
		
	}
	
	public void setURLBasePath (String urlBasePath) {
		
		this.urlBasePath = urlBasePath;
	}
	
	public void setMergeModel (MergeModel mergeModel) {
		
		this.mergeModel = mergeModel;
	}
	
	public String getResolutionsMenuAsDecoratedXHTMLList () {
		
		StringBuffer resolutionsMenuAsDecoratedXHTMLList = new StringBuffer();
		
		resolutionsMenuAsDecoratedXHTMLList.append("<ul class=\"resolutions-menu\">");

		if (this.getHttpServletRequest().getServletPath().startsWith("/pages/merges/resolutions/by-column")) {
			resolutionsMenuAsDecoratedXHTMLList.append("<li class=\"item\"><a class=\"link selected\" href=\"").append(this.getURLBasePath()).append("pages/merges/resolutions/by-column?merge_id=").append(this.getMergeModel().getId()).append("\">By Column</a></li>");
		} else {
			resolutionsMenuAsDecoratedXHTMLList.append("<li class=\"item\"><a class=\"link\" href=\"").append(this.getURLBasePath()).append("pages/merges/resolutions/by-column?merge_id=").append(this.getMergeModel().getId()).append("\">By Column</a></li>");
		}
		
		if (this.getHttpServletRequest().getServletPath().startsWith("/pages/merges/resolutions/by-row")) {
			resolutionsMenuAsDecoratedXHTMLList.append("<li class=\"item\"><a class=\"link selected\" href=\"").append(this.getURLBasePath()).append("pages/merges/resolutions/by-row?merge_id=").append(this.getMergeModel().getId()).append("\">By Row</a></li>");
		} else {
			resolutionsMenuAsDecoratedXHTMLList.append("<li class=\"item\"><a class=\"link\" href=\"").append(this.getURLBasePath()).append("pages/merges/resolutions/by-row?merge_id=").append(this.getMergeModel().getId()).append("\">By Row</a></li>");
		}
		if (this.getHttpServletRequest().getServletPath().startsWith("/pages/merges/resolutions/by-cell")) {
			resolutionsMenuAsDecoratedXHTMLList.append("<li class=\"item\"><a class=\"link selected\" href=\"").append(this.getURLBasePath()).append("pages/merges/resolutions/by-cell?merge_id=").append(this.getMergeModel().getId()).append("\">By Cell</a></li>");
		} else {
			resolutionsMenuAsDecoratedXHTMLList.append("<li class=\"item\"><a class=\"link\" href=\"").append(this.getURLBasePath()).append("pages/merges/resolutions/by-cell?merge_id=").append(this.getMergeModel().getId()).append("\">By Cell</a></li>");
		}
		
		resolutionsMenuAsDecoratedXHTMLList.append("</ul>");
		
		return resolutionsMenuAsDecoratedXHTMLList.toString();
	}

	public MergeModel getMergeModel() {
		return this.mergeModel;
	}

	public String getURLBasePath() {
		return this.urlBasePath;
	}

	public void setHttpServletRequest(HttpServletRequest httpServletRequest) {
		this.httpServletRequest = httpServletRequest;
	}

	public HttpServletRequest getHttpServletRequest() {
		return this.httpServletRequest;
	}

	public Logger getLogger() {
		return logger;
	}
}