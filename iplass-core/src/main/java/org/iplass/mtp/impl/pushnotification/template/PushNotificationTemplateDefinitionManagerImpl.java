/*
 * Copyright (C) 2016 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.impl.pushnotification.template;

import org.iplass.mtp.impl.definition.AbstractTypedDefinitionManager;
import org.iplass.mtp.impl.definition.TypedMetaDataService;
import org.iplass.mtp.impl.metadata.RootMetaData;
import org.iplass.mtp.impl.pushnotification.PushNotificationService;
import org.iplass.mtp.pushnotification.template.definition.PushNotificationTemplateDefinition;
import org.iplass.mtp.pushnotification.template.definition.PushNotificationTemplateDefinitionManager;
import org.iplass.mtp.spi.ServiceRegistry;

public class PushNotificationTemplateDefinitionManagerImpl extends AbstractTypedDefinitionManager<PushNotificationTemplateDefinition> implements
		PushNotificationTemplateDefinitionManager {

	private PushNotificationService service = ServiceRegistry.getRegistry().getService(PushNotificationService.class);

	@Override
	public Class<PushNotificationTemplateDefinition> getDefinitionType() {
		return PushNotificationTemplateDefinition.class;
	}

	@Override
	protected RootMetaData newInstance(PushNotificationTemplateDefinition definition) {
		return new MetaPushNotificationTemplate();
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected TypedMetaDataService getService() {
		return service;
	}

}
