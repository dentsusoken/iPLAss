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

package org.iplass.mtp.impl.web.template;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.definition.TypedDefinitionManager;
import org.iplass.mtp.impl.definition.AbstractTypedMetaDataService;
import org.iplass.mtp.impl.definition.DefinitionMetaDataTypeMap;
import org.iplass.mtp.impl.web.template.MetaTemplate.TemplateRuntime;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;
import org.iplass.mtp.web.template.definition.TemplateDefinition;
import org.iplass.mtp.web.template.definition.TemplateDefinitionManager;


public class TemplateService extends AbstractTypedMetaDataService<MetaTemplate, TemplateRuntime> implements Service {
	public static final String TEMPLATE_META_PATH = "/template/";

	public static class TypeMap extends DefinitionMetaDataTypeMap<TemplateDefinition, MetaTemplate> {
		public TypeMap() {
			super(getFixedPath(), MetaTemplate.class, TemplateDefinition.class);
		}
		@Override
		public TypedDefinitionManager<TemplateDefinition> typedDefinitionManager() {
			return ManagerLocator.getInstance().getManager(TemplateDefinitionManager.class);
		}
	}

	public void destroy() {
	}

	public void init(Config config) {
	}

	public static String getFixedPath() {
		return TEMPLATE_META_PATH;
	}

	@Override
	public Class<MetaTemplate> getMetaDataType() {
		return MetaTemplate.class;
	}

	@Override
	public Class<TemplateRuntime> getRuntimeType() {
		return TemplateRuntime.class;
	}
}
