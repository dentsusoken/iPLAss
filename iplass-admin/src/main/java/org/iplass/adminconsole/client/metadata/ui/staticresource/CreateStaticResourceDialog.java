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

package org.iplass.adminconsole.client.metadata.ui.staticresource;

import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.metadata.ui.common.Callable;
import org.iplass.adminconsole.client.metadata.ui.common.MetaDataCreateDialog;
import org.iplass.mtp.definition.DefinitionSummary;

public class CreateStaticResourceDialog extends MetaDataCreateDialog {

	/**
	 * コンストラクタ
	 *
	 * @param definitionClassName
	 * @param nodeDisplayName
	 * @param folderPath
	 * @param isCopyMode
	 */
	public CreateStaticResourceDialog(String definitionClassName, String nodeDisplayName, String folderPath, boolean isCopyMode) {
		super(definitionClassName, nodeDisplayName, folderPath, isCopyMode);
	}

	@Override
	protected void saveAction(final SaveInfo saveInfo, final boolean isCopyMode) {
		// 存在チェック
		checkExist(saveInfo.getName(), new Callable<Void>() {
			@Override
			public Void call() {
				createStaticResource(saveInfo, isCopyMode);
				return null;
			}
		});
	}

	private void createStaticResource(final SaveInfo saveInfo, final boolean isCopyMode) {
		// Definitionとtransに差異があるのでTypedDefinitionManagerを使う共通処理が呼べない
		DefinitionSummary definitionName = new DefinitionSummary();
		definitionName.setName(saveInfo.getName());
		definitionName.setDisplayName(saveInfo.getDisplayName());
		definitionName.setDescription(saveInfo.getDescription());

		if (isCopyMode) {
			service.copyStaticResourceDefinition(TenantInfoHolder.getId(), getSourceName(), definitionName, new SaveResultCallback());
		} else {
			service.createStaticResourceDefinition(TenantInfoHolder.getId(), definitionName, new SaveResultCallback());
		}
	}

}
