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

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.definition.TypedDefinitionManager;
import org.iplass.mtp.impl.definition.AbstractTypedMetaDataService;
import org.iplass.mtp.impl.definition.DefinitionMetaDataTypeMap;
import org.iplass.mtp.impl.view.filter.MetaEntityFilter.EntityFilterHandler;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;
import org.iplass.mtp.view.filter.EntityFilter;
import org.iplass.mtp.view.filter.EntityFilterManager;

/**
 * フィルタ定義を扱うサービス
 * @author lis3wg
 */
public class EntityFilterService extends AbstractTypedMetaDataService<MetaEntityFilter, EntityFilterHandler> implements Service {

	/** メタデータのパス */
	public static final String META_PATH = "/view/filter/";

	public static class TypeMap extends DefinitionMetaDataTypeMap<EntityFilter, MetaEntityFilter> {
		public TypeMap() {
			super(getFixedPath(), MetaEntityFilter.class, EntityFilter.class);
		}
		@Override
		public TypedDefinitionManager<EntityFilter> typedDefinitionManager() {
			return ManagerLocator.getInstance().getManager(EntityFilterManager.class);
		}
		@Override
		public String toPath(String defName) {
			return pathPrefix + defName.replace('.', '/');
		}
		@Override
		public String toDefName(String path) {
			return path.substring(pathPrefix.length()).replace("/", ".");
		}
	}

	@Override
	public void destroy() {
	}

	@Override
	public void init(Config config) {
	}

	public static String getFixedPath() {
		return META_PATH;
	}

	@Override
	public Class<MetaEntityFilter> getMetaDataType() {
		return MetaEntityFilter.class;
	}

	@Override
	public Class<EntityFilterHandler> getRuntimeType() {
		return EntityFilterHandler.class;
	}
}
