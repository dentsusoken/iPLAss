/*
 * Copyright (C) 2015 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
import java.util.List;

import org.iplass.mtp.definition.LocalizedStringDefinition;

public class MultiLangFieldInfo implements Serializable {

	private static final long serialVersionUID = -540655594102469353L;

	private String defaultString;
	private List<LocalizedStringDefinition> localizedStringList;


	public String getDefaultString() {
		return defaultString;
	}
	public void setDefaultString(String defaultString) {
		this.defaultString = defaultString;
	}
	public List<LocalizedStringDefinition> getLocalizedStringList() {
		return localizedStringList;
	}
	public void setLocalizedStringList(List<LocalizedStringDefinition> localizedStringList) {
		this.localizedStringList = localizedStringList;
	}
}
