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

package org.iplass.mtp.impl.view.filter;

import org.iplass.mtp.impl.definition.AbstractTypedDefinitionManager;
import org.iplass.mtp.impl.definition.TypedMetaDataService;
import org.iplass.mtp.impl.metadata.RootMetaData;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.view.filter.EntityFilter;
import org.iplass.mtp.view.filter.EntityFilterManager;

/**
 * Entityのフィルタ情報を管理するクラス
 * @author lis3wg
 */
public class EntityFilterManagerImpl extends AbstractTypedDefinitionManager<EntityFilter> implements EntityFilterManager {

	private EntityFilterService service = ServiceRegistry.getRegistry().getService(EntityFilterService.class);

	@Override
	public Class<EntityFilter> getDefinitionType() {
		return EntityFilter.class;
	}

	@Override
	protected RootMetaData newInstance(EntityFilter definition) {
		return new MetaEntityFilter();
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected TypedMetaDataService getService() {
		return service;
	}
}
