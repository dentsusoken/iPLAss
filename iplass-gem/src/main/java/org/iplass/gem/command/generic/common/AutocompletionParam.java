/*
 * Copyright (C) 2018 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
 *
 * Unless you have purchased a commercial license,
 * the following license terms apply:
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package org.iplass.gem.command.generic.common;

import java.util.Map;

import org.iplass.gem.command.GemWebApiParameter;

public class AutocompletionParam implements GemWebApiParameter {

	private String defName;

	private String viewName;

	private String viewType;

	private String propName;

	private String autocompletionKey;

	private Integer referenceSectionIndex;

	private Map<String, String[]> params;

	public String getDefName() {
		return defName;
	}

	public void setDefName(String defName) {
		this.defName = defName;
	}

	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	public String getViewType() {
		return viewType;
	}

	public void setViewType(String viewType) {
		this.viewType = viewType;
	}

	public String getPropName() {
		return propName;
	}

	public void setPropName(String propName) {
		this.propName = propName;
	}

	public String getAutocompletionKey() {
		return autocompletionKey;
	}

	public void setAutocompletionKey(String autocompletionKey) {
		this.autocompletionKey = autocompletionKey;
	}

	/**
	 * @return referenceSectionIndex
	 */
	public Integer getReferenceSectionIndex() {
		return referenceSectionIndex;
	}

	/**
	 * @param referenceSectionIndex セットする referenceSectionIndex
	 */
	public void setReferenceSectionIndex(Integer referenceSectionIndex) {
		this.referenceSectionIndex = referenceSectionIndex;
	}

	public Map<String, String[]> getParams() {
		return params;
	}

	public void setParams(Map<String, String[]> params) {
		this.params = params;
	}
}
