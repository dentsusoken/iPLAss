/*
 * Copyright (C) 2013 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.i18n;

import org.iplass.mtp.definition.LocalizedStringDefinition;
import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.util.ObjectUtil;

public class MetaLocalizedString implements MetaData {

	private static final long serialVersionUID = -771114939152676884L;

	private String localeName;
	private String stringValue;

	public MetaLocalizedString() {
	}

	public MetaLocalizedString(String localeName, String stringValue) {
		this.localeName = localeName;
		this.stringValue = stringValue;
	}


	@Override
	public MetaLocalizedString copy() {
		return ObjectUtil.deepCopy(this);
	}

	// Definition → Meta
	public void applyConfig(LocalizedStringDefinition definition) {
		this.localeName = definition.getLocaleName();
		this.stringValue = definition.getStringValue();
	}

	// Meta → Definition
	public LocalizedStringDefinition currentConfig() {
		LocalizedStringDefinition definition = new LocalizedStringDefinition();
		definition.setLocaleName(getLocaleName());
		definition.setStringValue(getStringValue());
		return definition;
	}

	public String getLocaleName() {
		return localeName;
	}

	public void setLocaleName(String localeName) {
		this.localeName = localeName;
	}

	public String getStringValue() {
		return stringValue;
	}

	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

}
