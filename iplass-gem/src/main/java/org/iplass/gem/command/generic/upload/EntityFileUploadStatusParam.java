package org.iplass.gem.command.generic.upload;

import org.iplass.gem.command.GemWebApiParameter;

public class EntityFileUploadStatusParam implements GemWebApiParameter {

	private String defName;

	private String viewName;

	/**
	 * @return defName
	 */
	public String getDefName() {
		return defName;
	}

	/**
	 * @param defName セットする defName
	 */
	public void setDefName(String defName) {
		this.defName = defName;
	}

	/**
	 * @return viewName
	 */
	public String getViewName() {
		return viewName;
	}

	/**
	 * @param viewName セットする viewName
	 */
	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

}
