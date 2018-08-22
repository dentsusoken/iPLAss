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

package org.iplass.mtp.impl.view.top;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.definition.TypedDefinitionManager;
import org.iplass.mtp.impl.definition.AbstractTypedMetaDataService;
import org.iplass.mtp.impl.definition.DefinitionMetaDataTypeMap;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;
import org.iplass.mtp.view.top.TopViewDefinition;
import org.iplass.mtp.view.top.TopViewDefinitionManager;

/**
 * TOP画面定義を扱うサービス。
 * @author lis3wg
 */
public class TopViewDefinitionService extends AbstractTypedMetaDataService<MetaTopView, TopViewHandler> implements Service {
	/** メタデータのパス */
	public static final String META_PATH = "/view/top/";

	public static class TypeMap extends DefinitionMetaDataTypeMap<TopViewDefinition, MetaTopView> {
		public TypeMap() {
			super(getFixedPath(), MetaTopView.class, TopViewDefinition.class);
		}
		@Override
		public TypedDefinitionManager<TopViewDefinition> typedDefinitionManager() {
			return ManagerLocator.getInstance().getManager(TopViewDefinitionManager.class);
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
	public Class<MetaTopView> getMetaDataType() {
		return MetaTopView.class;
	}

	@Override
	public Class<TopViewHandler> getRuntimeType() {
		return TopViewHandler.class;
	}
}
