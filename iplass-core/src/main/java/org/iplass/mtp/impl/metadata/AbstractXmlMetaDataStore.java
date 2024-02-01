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

package org.iplass.mtp.impl.metadata;


import java.util.List;

import javax.xml.bind.JAXBContext;

import org.iplass.mtp.spi.Config;


public abstract class AbstractXmlMetaDataStore implements MetaDataStore {
	protected JAXBContext context;

	@Override
	public void inited(MetaDataRepository service, Config config) {
		MetaDataJAXBService jaxbService = config.getDependentService(MetaDataJAXBService.class);
		context = jaxbService.getJAXBContext();
	}

	@Override
	public void destroyed() {
		context = null;
	}

	@Override
	public List<MetaDataEntryInfo> definitionList(int tenantId, String prefixPath) throws MetaDataRuntimeException {
		return definitionList(tenantId, prefixPath, false);
	}
	
	

}
