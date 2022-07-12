/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import java.util.Map;

public class FulltextSearchViewParts extends TopViewParts {

	/** SerialVersionUID */
	private static final long serialVersionUID = -7149698789315728389L;

	private Map<String, String> viewNames;

	private Map<String, Boolean> dispEntities;

	private boolean dispSearchWindow;

	private boolean showUserNameWithPrivilegedValue;
	
	public boolean isDispSearchWindow() {
		return dispSearchWindow;
	}

	public void setDispSearchWindow(boolean dispSearchWindow) {
		this.dispSearchWindow = dispSearchWindow;
	}
	
	public boolean isShowUserNameWithPrivilegedValue() {
		return showUserNameWithPrivilegedValue;
	}

	public void setShowUserNameWithPrivilegedValue(boolean showUserNameWithPrivilegedValue) {
		this.showUserNameWithPrivilegedValue = showUserNameWithPrivilegedValue;
	}

	public Map<String, String> getViewNames() {
		return viewNames;
	}

	public void setViewNames(Map<String, String> viewNames) {
		this.viewNames = viewNames;
	}

	public Map<String, Boolean> getDispEntities() {
		return dispEntities;
	}

	public void setDispEntities(Map<String, Boolean> dispEntities) {
		this.dispEntities = dispEntities;
	}

}
