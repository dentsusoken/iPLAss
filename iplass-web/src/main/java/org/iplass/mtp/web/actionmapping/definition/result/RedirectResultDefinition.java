/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.web.actionmapping.definition.result;

/**
 * 実行結果として、リダイレクトするResult定義です。
 * 
 * @author K.Higuchi
 *
 */
public class RedirectResultDefinition extends ResultDefinition {

	private static final long serialVersionUID = 632004285059492173L;

	private String redirectPath;

	private boolean allowExternalLocation = false;

	public boolean isAllowExternalLocation() {
		return allowExternalLocation;
	}

	public void setAllowExternalLocation(boolean allowExternalLocation) {
		this.allowExternalLocation = allowExternalLocation;
	}

	/**
	 * @return redirectPath
	 */
	public String getRedirectPath() {
		return redirectPath;
	}

	/**
	 * @param redirectPath セットする redirectPath
	 */
	public void setRedirectPath(String redirectPath) {
		this.redirectPath = redirectPath;
	}

	@Override
	public String summaryInfo() {
		return "redirect path = " + redirectPath;
	}
}
