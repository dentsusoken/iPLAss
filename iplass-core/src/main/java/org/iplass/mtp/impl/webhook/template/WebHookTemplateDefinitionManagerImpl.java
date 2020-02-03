/*
 * Copyright (C) 2020 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
package org.iplass.mtp.impl.webhook.template;

import org.iplass.mtp.impl.definition.AbstractTypedDefinitionManager;
import org.iplass.mtp.impl.definition.TypedMetaDataService;
import org.iplass.mtp.impl.metadata.RootMetaData;
import org.iplass.mtp.impl.webhook.WebHookService;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.webhook.template.definition.WebHookTemplateDefinition;
import org.iplass.mtp.webhook.template.definition.WebHookTemplateDefinitionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebHookTemplateDefinitionManagerImpl extends AbstractTypedDefinitionManager<WebHookTemplateDefinition> implements
WebHookTemplateDefinitionManager{


	private static final Logger logger = LoggerFactory.getLogger(WebHookTemplateDefinitionManager.class);
	private WebHookService service;
	
	public WebHookTemplateDefinitionManagerImpl() {
		this.service = ServiceRegistry.getRegistry().getService(WebHookService.class);
	}

	@Override
	public Class<WebHookTemplateDefinition> getDefinitionType() {
		return WebHookTemplateDefinition.class;
	}

	@Override
	protected RootMetaData newInstance(WebHookTemplateDefinition definition) {
		return new MetaWebHookTemplate();
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	protected TypedMetaDataService getService() {
		return service;
	}
}
