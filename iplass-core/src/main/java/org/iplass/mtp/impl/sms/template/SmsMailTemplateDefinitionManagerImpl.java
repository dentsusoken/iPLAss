/*
 * Copyright (C) 2015 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.sms.template;

import org.iplass.mtp.impl.definition.AbstractTypedDefinitionManager;
import org.iplass.mtp.impl.definition.TypedMetaDataService;
import org.iplass.mtp.impl.metadata.RootMetaData;
import org.iplass.mtp.impl.sms.SmsService;
import org.iplass.mtp.sms.template.definition.SmsMailTemplateDefinition;
import org.iplass.mtp.sms.template.definition.SmsMailTemplateDefinitionManager;
import org.iplass.mtp.spi.ServiceRegistry;

/**
 * SmsMailTemplateDefinitionManagerの実装.
 */
public class SmsMailTemplateDefinitionManagerImpl extends AbstractTypedDefinitionManager<SmsMailTemplateDefinition> implements SmsMailTemplateDefinitionManager {

	private SmsService service;

	public SmsMailTemplateDefinitionManagerImpl() {
		service = ServiceRegistry.getRegistry().getService(SmsService.class);
	}

	@Override
	public Class<SmsMailTemplateDefinition> getDefinitionType() {
		return SmsMailTemplateDefinition.class;
	}

	@Override
	protected RootMetaData newInstance(SmsMailTemplateDefinition definition) {
		return new MetaSmsMailTemplate();
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected TypedMetaDataService getService() {
		return service;
	}
}
