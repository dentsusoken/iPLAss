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

package org.iplass.adminconsole.client.metadata.ui.menu;

import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.metadata.ui.common.Callable;
import org.iplass.adminconsole.client.metadata.ui.common.MetaDataCreateDialog;
import org.iplass.mtp.view.menu.MenuTree;

/**
 * メニューツリー新規作成ダイアログ
 */
public class CreateMenuTreeDialog extends MetaDataCreateDialog {

	public CreateMenuTreeDialog(String definitionClassName, String nodeDisplayName, String folderPath, boolean isCopyMode) {
		super(definitionClassName, nodeDisplayName, folderPath, isCopyMode);
	}

	@Override
	protected void saveAction(final SaveInfo saveInfo, final boolean isCopyMode) {

		// 存在チェック
		checkExist(saveInfo.getName(), new Callable<Void>() {

			@Override
			public Void call() {
				createMenuTree(saveInfo, isCopyMode);
				return null;
			}
		});
	}

	/**
	 * メニューツリーを登録します。
	 */
	private void createMenuTree(final SaveInfo saveInfo, final boolean isCopyMode) {

		if (isCopyMode) {
			service.copyDefinition(TenantInfoHolder.getId(), getDefinitionClassName(), getSourceName(),
					saveInfo.getName(), saveInfo.getDisplayName(), saveInfo.getDescription(), new SaveResultCallback());
		} else {
			MenuTree definition = new MenuTree();

			definition.setName(saveInfo.getName());
			definition.setDisplayName(saveInfo.getDisplayName());
			definition.setDescription(saveInfo.getDescription());
			definition.setShowMenuDisplayName(true);

			service.createDefinition(TenantInfoHolder.getId(), definition, new SaveResultCallback());
		}
	}

}
