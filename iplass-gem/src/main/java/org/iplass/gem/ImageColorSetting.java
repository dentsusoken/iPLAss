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

package org.iplass.gem;

import java.util.List;

public class ImageColorSetting  {

	/** 色名 */
	private String colorName;

	/** CSS設定 */
	private List<CssSetting> cssSettings;

	/**
	 * @return colorName
	 */
	public String getColorName() {
		return colorName;
	}

	/**
	 * @param colorName セットする colorName
	 */
	public void setColorName(String colorName) {
		this.colorName = colorName;
	}

	/**
	 * @return cssSettings
	 */
	public List<CssSetting> getCssSettings() {
		return cssSettings;
	}

	/**
	 * @param cssSettings セットする cssSettings
	 */
	public void setCssSettings(List<CssSetting> cssSettings) {
		this.cssSettings = cssSettings;
	}
}
