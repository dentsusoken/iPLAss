/*
 * Copyright (C) 2022 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.web;

import java.util.Map;

import org.iplass.mtp.command.RequestContext;

public class CompositeErrorUrlSelector implements ErrorUrlSelector {

	/** SelectorMap */
	private Map<String, ErrorUrlSelector> selectorMap;

	@Override
	public String getErrorTemplateName(Throwable exception, RequestContext request, String path) {
		for (Map.Entry<String, ErrorUrlSelector> entry : selectorMap.entrySet()) {
			if (path.startsWith(entry.getKey())) {
				return entry.getValue().getErrorTemplateName(exception, request, path);
			}
		}

		return selectorMap.get("default").getErrorTemplateName(exception, request, path);
	}

	public Map<String, ErrorUrlSelector> getSelectorMap() {
		return selectorMap;
	}

	public void setSelectorMap(Map<String, ErrorUrlSelector> selectorMap) {
		this.selectorMap = selectorMap;
	}

}
