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

package org.iplass.mtp.impl.webapi;

import org.iplass.mtp.impl.definition.AbstractTypedDefinitionManager;
import org.iplass.mtp.impl.definition.TypedMetaDataService;
import org.iplass.mtp.impl.metadata.RootMetaData;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.webapi.definition.WebApiDefinition;
import org.iplass.mtp.webapi.definition.WebApiDefinitionManager;

public class WebApiDefinitionManagerImpl extends AbstractTypedDefinitionManager<WebApiDefinition> implements WebApiDefinitionManager {

//	private static final Logger logger = LoggerFactory.getLogger(WebAPIDefinitionManagerImpl.class);

	private WebApiService service;
//	private DefinitionManager dm;

	public WebApiDefinitionManagerImpl() {
		service = ServiceRegistry.getRegistry().getService(WebApiService.class);
//		dm = ManagerLocator.getInstance().getManager(DefinitionManager.class);
	}

//	@Override
//	public WebAPIDefinition get(String definitionName) {
//
//		WebAPIRuntime handler = service.get(definitionName);
//		if (handler == null) {
//			return null;
//		}
//		return handler.getMetaData().currentConfig();
//	}

//	@Override
//	public List<String> definitionList() {
//		return service.nameList();
//	}

//	@Override
//	public List<DefinitionName> definitionNameList() {
//		return definitionNameList("");
//	}

//	@Override
//	public List<DefinitionName> definitionNameList(String filterPath) {
//		return dm.listName(WebAPIDefinition.class, filterPath);
//	}

//	@Override
//	public WebAPIDefinitionModifyResult create(WebAPIDefinition definition) {
//
//		MetaWebAPI metaData = new MetaWebAPI();
//		metaData.applyConfig(definition);
//
//		try {
//			service.store(metaData);
//		} catch (Exception e) {
//			setRollbackOnly();
//			if (e.getCause() != null) {
//				logger.error("exception occured during web api definition create:" + e.getCause().getMessage(), e.getCause());
//				return new WebAPIDefinitionModifyResult(false, "exception occured during web api definition create:" + e.getCause().getMessage());
//			} else {
//				logger.error("exception occured during web api definition create:" + e.getMessage(), e);
//				return new WebAPIDefinitionModifyResult(false, "exception occured during web api definition create:" + e.getMessage());
//			}
//		}
//
//		return new WebAPIDefinitionModifyResult(true);
//	}

//	@Override
//	public WebAPIDefinitionModifyResult update(WebAPIDefinition definition) {
//		WebAPIRuntime handler = service.get(definition.getName());
//		MetaWebAPI metaData = handler.getMetaData();
//		metaData.applyConfig(definition);
//
//		try {
//			service.update(metaData);
//		} catch (Exception e) {
//			setRollbackOnly();
//			if (e.getCause() != null) {
//				logger.error("exception occured during web api definition update:" + e.getCause().getMessage(), e.getCause());
//				return new WebAPIDefinitionModifyResult(false, "exception occured during web api definition update:" + e.getCause().getMessage());
//			} else {
//				logger.error("exception occured during web api definition update:" + e.getMessage(), e);
//				return new WebAPIDefinitionModifyResult(false, "exception occured during web api definition update:" + e.getMessage());
//			}
//		}
//
//		return new WebAPIDefinitionModifyResult(true);
//	}

//	@Override
//	public WebAPIDefinitionModifyResult remove(String definitionName) {
//		try {
//			service.remove(definitionName);
//		} catch (Exception e) {
//			setRollbackOnly();
//			if (e.getCause() != null) {
//				logger.error("exception occured during web api definition remove:" + e.getCause().getMessage(), e.getCause());
//				return new WebAPIDefinitionModifyResult(false, "exception occured during web api definition remove:" + e.getCause().getMessage());
//			} else {
//				logger.error("exception occured during web api definition remove:" + e.getMessage(), e);
//				return new WebAPIDefinitionModifyResult(false, "exception occured during web api definition remove:" + e.getMessage());
//			}
//		}
//
//		return new WebAPIDefinitionModifyResult(true);
//	}

	@Override
	public Class<WebApiDefinition> getDefinitionType() {
		return WebApiDefinition.class;
	}

	@Override
	protected RootMetaData newInstance(WebApiDefinition definition) {
		return new MetaWebApi();
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected TypedMetaDataService getService() {
		return service;
	}
}
