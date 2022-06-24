/*
 * Copyright (C) 2022 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
package org.iplass.adminconsole.client.metadata.ui.entity;

import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.shared.metadata.dto.AdminDefinitionModifyResult;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;
import org.iplass.mtp.entity.definition.EntityDefinition;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Gem用のEntity定義作成時の処理を制御するController
 *
 * @author Y.Yasuda
 */
public class GemEntityOperationController implements EntityOperationController {

	private MetaDataServiceAsync service = MetaDataServiceFactory.get();

	@Override
	public void createEntityDefinition(EntityDefinition definition,
			AsyncCallback<AdminDefinitionModifyResult> callback) {

		service.createEntityDefinition(TenantInfoHolder.getId(), definition, callback);
	}

	@Override
	public void copyEntityDefinition(String sourceName, String newName, String displayName, String description,
			boolean isCopyEntityView, boolean isCopyEntityFilter, boolean isCopyEntityWebAPI,
			AsyncCallback<AdminDefinitionModifyResult> callback) {

		service.copyEntityDefinition(TenantInfoHolder.getId(), sourceName,	newName, displayName, description,
				isCopyEntityView, isCopyEntityFilter, isCopyEntityWebAPI, callback);

	}

}
