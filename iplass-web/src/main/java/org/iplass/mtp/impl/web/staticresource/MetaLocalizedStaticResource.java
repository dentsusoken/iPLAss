/*
 * Copyright (C) 2016 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
package org.iplass.mtp.impl.web.staticresource;

import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.metadata.binary.BinaryMetaData;
import org.iplass.mtp.impl.util.ObjectUtil;

public class MetaLocalizedStaticResource implements MetaData {
	private static final long serialVersionUID = -1907019121689537822L;

	private String localeName;
	private BinaryMetaData resource;
	
	public MetaLocalizedStaticResource() {
	}
	
	public MetaLocalizedStaticResource(String localeName, BinaryMetaData resource) {
		this.localeName = localeName;
		this.resource = resource;
	}
	
	public String getLocaleName() {
		return localeName;
	}

	public void setLocaleName(String localeName) {
		this.localeName = localeName;
	}

	public BinaryMetaData getResource() {
		return resource;
	}

	public void setResource(BinaryMetaData resource) {
		this.resource = resource;
	}

	@Override
	public MetaData copy() {
		return ObjectUtil.deepCopy(this);
	}
	
}
