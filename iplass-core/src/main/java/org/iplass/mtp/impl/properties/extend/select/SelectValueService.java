/*
 * Copyright (C) 2013 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.properties.extend.select;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.definition.TypedDefinitionManager;
import org.iplass.mtp.entity.definition.properties.selectvalue.SelectValueDefinition;
import org.iplass.mtp.entity.definition.properties.selectvalue.SelectValueDefinitionManager;
import org.iplass.mtp.impl.definition.AbstractTypedMetaDataService;
import org.iplass.mtp.impl.definition.DefinitionMetaDataTypeMap;
import org.iplass.mtp.impl.properties.extend.select.MetaSelectValue.SelectValueRuntime;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;

public class SelectValueService extends AbstractTypedMetaDataService<MetaSelectValue, SelectValueRuntime> implements Service {

	public static final String META_PATH = "/property/select/";

	public static class TypeMap extends DefinitionMetaDataTypeMap<SelectValueDefinition, MetaSelectValue> {
		public TypeMap() {
			super(getFixedPath(), MetaSelectValue.class, SelectValueDefinition.class);
		}
		@Override
		public TypedDefinitionManager<SelectValueDefinition> typedDefinitionManager() {
			return ManagerLocator.getInstance().getManager(SelectValueDefinitionManager.class);
		}
	}

	@Override
	public void init(Config config) {
	}

	@Override
	public void destroy() {
	}

	public static String getFixedPath() {
		return META_PATH;
	}

	@Override
	public Class<MetaSelectValue> getMetaDataType() {
		return MetaSelectValue.class;
	}

	@Override
	public Class<SelectValueRuntime> getRuntimeType() {
		return SelectValueRuntime.class;
	}

}
