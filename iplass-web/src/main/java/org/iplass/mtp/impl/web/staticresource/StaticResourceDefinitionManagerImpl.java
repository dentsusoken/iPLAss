/*
 * Copyright (C) 2016 DENTSU SOKEN INC. All Rights Reserved.
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

import org.iplass.mtp.impl.definition.AbstractTypedDefinitionManager;
import org.iplass.mtp.impl.definition.TypedMetaDataService;
import org.iplass.mtp.impl.metadata.RootMetaData;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.web.staticresource.definition.StaticResourceDefinition;
import org.iplass.mtp.web.staticresource.definition.StaticResourceDefinitionManager;

public class StaticResourceDefinitionManagerImpl extends AbstractTypedDefinitionManager<StaticResourceDefinition> implements StaticResourceDefinitionManager {

	private StaticResourceService service;

	/**
	 * コンストラクタ
	 */
	public StaticResourceDefinitionManagerImpl() {
		service = ServiceRegistry.getRegistry().getService(StaticResourceService.class);
	}

	@Override
	public Class<StaticResourceDefinition> getDefinitionType() {
		return StaticResourceDefinition.class;
	}

	@Override
	protected RootMetaData newInstance(StaticResourceDefinition definition) {
		return new MetaStaticResource();
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected TypedMetaDataService getService() {
		return service;
	}
}
