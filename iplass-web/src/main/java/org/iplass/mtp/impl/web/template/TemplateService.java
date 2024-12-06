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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.definition.TypedDefinitionManager;
import org.iplass.mtp.impl.definition.AbstractTypedMetaDataService;
import org.iplass.mtp.impl.definition.DefinitionMetaDataTypeMap;
import org.iplass.mtp.impl.web.template.MetaTemplate.TemplateRuntime;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;
import org.iplass.mtp.spi.ServiceConfigrationException;
import org.iplass.mtp.web.template.definition.TemplateDefinition;
import org.iplass.mtp.web.template.definition.TemplateDefinitionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TemplateService extends AbstractTypedMetaDataService<MetaTemplate, TemplateRuntime>
		implements Service {
	private static Logger logger = LoggerFactory.getLogger(TemplateService.class);

	public static final String TEMPLATE_META_PATH = "/template/";
	private Map<Class<? extends TemplateDefinition>, Class<? extends MetaTemplate>> metaTemplateClassMap;

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

	@SuppressWarnings("unchecked")
	public void init(Config config) {
		metaTemplateClassMap = new HashMap<>();

		// configからDefinitionとMetaTemplateのMapを作成
		Map<String, String> classPathMap = config.getValue("templateClassMap", Map.class);
		if (classPathMap == null) {
			logger.debug("No template classes config found.");
			return;
		}

		for (Entry<String, String> defMetaEntry : classPathMap.entrySet()) {
			try {
				// Definition
				Class<?> defClass = Class.forName(defMetaEntry.getKey());
				if (!TemplateDefinition.class.isAssignableFrom(defClass)) {
					throw new ClassCastException("Incompatible class: " + defMetaEntry.getKey() + " is not a subclass of TemplateDefinition.");
				}
				// Meta
				Class<?> metaClass = Class.forName(defMetaEntry.getValue());
				if (!MetaTemplate.class.isAssignableFrom(metaClass)) {
					throw new ClassCastException("Incompatible class: " + defMetaEntry.getValue() + " is not a subclass of MetaTemplate.");
				}

				metaTemplateClassMap.put((Class<TemplateDefinition>) defClass, (Class<MetaTemplate>) metaClass);

			} catch (ClassNotFoundException | ClassCastException e) {
				throw new ServiceConfigrationException("templateClass: Failed to get class entries from the service config.", e);
			}
		}
	}

	public Class<? extends MetaTemplate> getMetaTemplateClassByDef(TemplateDefinition definition) {
		return metaTemplateClassMap.get(definition.getClass());
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
