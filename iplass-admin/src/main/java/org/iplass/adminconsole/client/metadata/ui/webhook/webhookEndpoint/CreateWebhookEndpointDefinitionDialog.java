/*
 * Copyright (C) 2020 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.adminconsole.client.metadata.ui.webhook.webhookEndpoint;

import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.metadata.ui.common.Callable;
import org.iplass.adminconsole.client.metadata.ui.common.MetaDataCreateDialog;
import org.iplass.mtp.webhook.endpoint.definition.WebhookEndpointDefinition;

public class CreateWebhookEndpointDefinitionDialog extends MetaDataCreateDialog{
	
	public CreateWebhookEndpointDefinitionDialog(String definitionClassName, String nodeDisplayName, String folderPath,
			boolean isCopyMode) {
		super(definitionClassName, nodeDisplayName, folderPath, isCopyMode);
	}

	@Override
	protected void saveAction(final SaveInfo saveInfo, final boolean isCopyMode) {
		
		checkExist(saveInfo.getName(), new Callable<Void>() {
			@Override
			public Void call() {
				createWebhookEndpointDefinition(saveInfo, isCopyMode);
				return null;
			}
		});
	}
	
	
	private void createWebhookEndpointDefinition(SaveInfo saveInfo, boolean isCopyMode) {
		if (isCopyMode) {
			service.copyDefinition(TenantInfoHolder.getId(), getDefinitionClassName(), getSourceName(), saveInfo.getName(), saveInfo.getDisplayName(), saveInfo.getDescription(), new SaveResultCallback());
		} else {
			WebhookEndpointDefinition definition = new WebhookEndpointDefinition();
			
			definition.setName(saveInfo.getName());
			definition.setDisplayName(saveInfo.getDisplayName());
			definition.setDescription(saveInfo.getDescription());
			
			service.createDefinition(TenantInfoHolder.getId(), definition, new SaveResultCallback());
		}
	}


}
