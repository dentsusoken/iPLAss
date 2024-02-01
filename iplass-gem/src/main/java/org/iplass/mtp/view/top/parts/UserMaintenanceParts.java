/*
 * Copyright (C) 2012 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.view.top.parts;

public class UserMaintenanceParts extends TopViewParts {

	/** SerialVersionUID */
	private static final long serialVersionUID = 5003430874417028239L;

	/** ビュー名 */
	private String viewName;

	/**
	 * ビュー名を取得します。
	 * @return ビュー名
	 */
	public String getViewName() {
	    return viewName;
	}

	/**
	 * ビュー名を設定します。
	 * @param viewName ビュー名
	 */
	public void setViewName(String viewName) {
	    this.viewName = viewName;
	}
}
