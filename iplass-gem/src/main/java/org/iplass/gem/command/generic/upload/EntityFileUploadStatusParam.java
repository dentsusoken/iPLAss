/*
 * Copyright (C) 2026 DENTSU SOKEN INC. All Rights Reserved.
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
