/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.view.generic;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.definition.TypedDefinitionManager;
import org.iplass.mtp.impl.definition.AbstractTypedMetaDataService;
import org.iplass.mtp.impl.definition.DefinitionMetaDataTypeMap;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;
import org.iplass.mtp.view.generic.EntityView;
import org.iplass.mtp.view.generic.EntityViewManager;

/**
 * Entityの画面定義を扱うサービス。
 * @author lis3wg
 */
public class EntityViewService extends AbstractTypedMetaDataService<MetaEntityView, EntityViewRuntime> implements Service {

	/** メタデータのパス */
	public static final String ENTITY_META_PATH = "/view/generic/";

	public static class TypeMap extends DefinitionMetaDataTypeMap<EntityView, MetaEntityView> {
		public TypeMap() {
			super(getFixedPath(), MetaEntityView.class, EntityView.class);
		}
		@Override
		public TypedDefinitionManager<EntityView> typedDefinitionManager() {
			return ManagerLocator.getInstance().getManager(EntityViewManager.class);
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

	/** EntityView定義時にプロパティの絞り込みを有効にするか */
	private boolean filterSettingProperty;

	public static String getFixedPath() {
		return ENTITY_META_PATH;
	}

	public boolean isFilterSettingProperty() {
		return filterSettingProperty;
	}

	@Override
	public void destroy() {
	}

	@Override
	public void init(Config config) {
		filterSettingProperty = Boolean.valueOf(config.getValue("filterSettingProperty"));
	}

	@Override
	public Class<MetaEntityView> getMetaDataType() {
		return MetaEntityView.class;
	}

	@Override
	public Class<EntityViewRuntime> getRuntimeType() {
		return EntityViewRuntime.class;
	}
}
