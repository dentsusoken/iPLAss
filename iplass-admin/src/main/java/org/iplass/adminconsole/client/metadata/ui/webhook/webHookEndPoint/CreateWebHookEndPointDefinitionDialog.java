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
package org.iplass.adminconsole.client.metadata.ui.webhook.webHookEndPoint;

import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.metadata.ui.common.Callable;
import org.iplass.adminconsole.client.metadata.ui.common.MetaDataCreateDialog;
import org.iplass.mtp.webhook.template.endpointaddress.WebHookEndPointDefinition;

public class CreateWebHookEndPointDefinitionDialog extends MetaDataCreateDialog{
	
	public CreateWebHookEndPointDefinitionDialog(String definitionClassName, String nodeDisplayName, String folderPath,
			boolean isCopyMode) {
		super(definitionClassName, nodeDisplayName, folderPath, isCopyMode);
	}

	@Override
	protected void saveAction(final SaveInfo saveInfo, final boolean isCopyMode) {
		
		checkExist(saveInfo.getName(), new Callable<Void>() {
			@Override
			public Void call() {
				createWebHookEndPointDefinition(saveInfo, isCopyMode);
				return null;
			}
		});
	}
	
	
	private void createWebHookEndPointDefinition(SaveInfo saveInfo, boolean isCopyMode) {
		if (isCopyMode) {
			service.copyDefinition(TenantInfoHolder.getId(), getDefinitionClassName(), getSourceName(), saveInfo.getName(), saveInfo.getDisplayName(), saveInfo.getDescription(), new SaveResultCallback());
		} else {
			WebHookEndPointDefinition definition = new WebHookEndPointDefinition();
			
			definition.setName(saveInfo.getName());
			definition.setDisplayName(saveInfo.getDisplayName());
			definition.setDescription(saveInfo.getDescription());
			
			service.createDefinition(TenantInfoHolder.getId(), definition, new SaveResultCallback());
		}
	}


}
