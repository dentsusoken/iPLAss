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

package org.iplass.mtp.impl.script;

import org.iplass.mtp.ApplicationException;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.definition.AbstractTypedDefinitionManager;
import org.iplass.mtp.impl.definition.TypedMetaDataService;
import org.iplass.mtp.impl.metadata.RootMetaData;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.utilityclass.definition.UtilityClassDefinition;
import org.iplass.mtp.utilityclass.definition.UtilityClassDefinitionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UtilityClassDefinitionManagerImpl extends AbstractTypedDefinitionManager<UtilityClassDefinition> implements
		UtilityClassDefinitionManager {
	private static final Logger logger = LoggerFactory.getLogger(UtilityClassDefinitionManagerImpl.class);

	private GroovyScriptService service;

	public UtilityClassDefinitionManagerImpl() {
		service = (GroovyScriptService) ServiceRegistry.getRegistry().getService(ScriptService.class);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T createInstanceAs(Class<T> type, String definitionName) throws ClassNotFoundException {

		GroovyScriptEngine se = (GroovyScriptEngine) ExecuteContext.getCurrentContext().getTenantContext().getScriptEngine();
		try {
			Class<?> clazz = Class.forName(definitionName, true, se.getSharedClassLoader());
			return (T)clazz.newInstance();
		} catch (ClassNotFoundException e) {
			logger.debug("not found utility class. name:" + definitionName, e);
			throw e;
		} catch (InstantiationException e) {
			throw new ApplicationException("can not instantiate utility class. name:" + definitionName, e);
		} catch (IllegalAccessException e) {
			throw new ApplicationException("illegal access utility class. name:" + definitionName, e);
		}
	}

	@Override
	public Class<UtilityClassDefinition> getDefinitionType() {
		return UtilityClassDefinition.class;
	}

	@Override
	protected RootMetaData newInstance(UtilityClassDefinition definition) {
		return new MetaUtilityClass();
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected TypedMetaDataService getService() {
		return service;
	}

}
