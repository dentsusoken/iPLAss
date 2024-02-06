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

package org.iplass.mtp.impl.web.template;

import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.web.template.definition.LocalizedBinaryDefinition;

public class MetaLocalizedBinary implements MetaData {

	private static final long serialVersionUID = -8881086592359990340L;

	private String localeName;
	private String fileName;
	private byte[] binaryValue;

	public MetaLocalizedBinary() {
	}

	public MetaLocalizedBinary(String localeName, String fileName, byte[] binaryValue) {
		this.localeName = localeName;
		this.fileName = fileName;
		this.binaryValue = binaryValue;
	}


	@Override
	public MetaLocalizedBinary copy() {
		return ObjectUtil.deepCopy(this);
	}

	// Definition → Meta
	public void applyConfig(LocalizedBinaryDefinition definition) {
		this.localeName = definition.getLocaleName();
		this.fileName = definition.getFileName();
		this.binaryValue = definition.getBinaryValue();
	}

	// Meta → Definition
	public LocalizedBinaryDefinition currentConfig() {
		LocalizedBinaryDefinition definition = new LocalizedBinaryDefinition();
		definition.setLocaleName(getLocaleName());
		definition.setFileName(getFileName());
		definition.setBinaryValue(getBinaryValue());
		return definition;
	}

	public String getLocaleName() {
		return localeName;
	}

	public void setLocaleName(String localeName) {
		this.localeName = localeName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public byte[] getBinaryValue() {
		return binaryValue;
	}

	public void setBinaryValue(byte[] binaryValue) {
		this.binaryValue = binaryValue;
	}

}
