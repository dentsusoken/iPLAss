/*
 * Copyright (C) 2015 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.shared.base.dto.i18n;

import java.io.Serializable;

/**
 * Localeに対応したMetaData表示情報
 *
 */
public class I18nMetaDisplayInfo implements Serializable {

	private static final long serialVersionUID = 1545331007295355218L;

	private String i18nDisplayName;
	private String i18nDescription;

	public I18nMetaDisplayInfo() {
	}

	public String getI18nDisplayName() {
		return i18nDisplayName;
	}
	public void setI18nDisplayName(String i18nDisplayName) {
		this.i18nDisplayName = i18nDisplayName;
	}

	public String getI18nDescription() {
		return i18nDescription;
	}
	public void setI18nDescription(String i18nDescription) {
		this.i18nDescription = i18nDescription;
	}

}
