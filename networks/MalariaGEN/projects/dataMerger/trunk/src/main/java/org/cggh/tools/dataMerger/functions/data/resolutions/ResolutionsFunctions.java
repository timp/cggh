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
		
		String resolutionsMenuAsDecoratedXHTMLList = "";
		
		resolutionsMenuAsDecoratedXHTMLList += "<ul class=\"resolutions-menu\">";

		if (this.getHttpServletRequest().getServletPath().startsWith("/pages/merges/resolutions/by-column")) {
			resolutionsMenuAsDecoratedXHTMLList += "<li class=\"item\"><a class=\"link selected\" href=\"" + this.getURLBasePath() + "pages/merges/resolutions/by-column?merge_id=" + this.getMergeModel().getId() + "\">By Column</a></li>";
		} else {
			resolutionsMenuAsDecoratedXHTMLList += "<li class=\"item\"><a class=\"link\" href=\"" + this.getURLBasePath() + "pages/merges/resolutions/by-column?merge_id=" + this.getMergeModel().getId() + "\">By Column</a></li>";
		}
		
		if (this.getHttpServletRequest().getServletPath().startsWith("/pages/merges/resolutions/by-row")) {
			resolutionsMenuAsDecoratedXHTMLList += "<li class=\"item\"><a class=\"link selected\" href=\"" + this.getURLBasePath() + "pages/merges/resolutions/by-row?merge_id=" + this.getMergeModel().getId() + "\">By Row</a></li>";
		} else {
			resolutionsMenuAsDecoratedXHTMLList += "<li class=\"item\"><a class=\"link\" href=\"" + this.getURLBasePath() + "pages/merges/resolutions/by-row?merge_id=" + this.getMergeModel().getId() + "\">By Row</a></li>";
		}
		if (this.getHttpServletRequest().getServletPath().startsWith("/pages/merges/resolutions/by-cell")) {
			resolutionsMenuAsDecoratedXHTMLList += "<li class=\"item\"><a class=\"link selected\" href=\"" + this.getURLBasePath() + "pages/merges/resolutions/by-cell?merge_id=" + this.getMergeModel().getId() + "\">By Cell</a></li>";
		} else {
			resolutionsMenuAsDecoratedXHTMLList += "<li class=\"item\"><a class=\"link\" href=\"" + this.getURLBasePath() + "pages/merges/resolutions/by-cell?merge_id=" + this.getMergeModel().getId() + "\">By Cell</a></li>";
		}
		
		resolutionsMenuAsDecoratedXHTMLList += "</ul>";
		
		return resolutionsMenuAsDecoratedXHTMLList;
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