/*
 * Copyright (C) 2022 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.adminconsole.client.base.screen;

import org.iplass.adminconsole.client.metadata.data.tenant.BaseTenantDS;
import org.iplass.adminconsole.client.metadata.ui.MetaDataPluginController;
import org.iplass.adminconsole.client.metadata.ui.tenant.BaseTenantPropertyEditDialog;
import org.iplass.adminconsole.client.tools.ui.entityexplorer.entityview.EntityExplorerViewPaneController;
import org.iplass.adminconsole.client.tools.ui.langexplorer.LangExplorerSettingController;
import org.iplass.mtp.tenant.Tenant;

import com.smartgwt.client.data.Record;

/**
 * 画面モジュールに依存したUIクラスを生成するFactoryのインタフェース
 * 
 * @author Y.Ishida
 *
 */
public interface ScreenModuleBasedUIFactory {

	/**
	 * {@link MetaDataPluginController} のインスタンスを生成する
	 * 
	 * @return {@link MetaDataPluginController} のインスタンス
	 */
	MetaDataPluginController createMetaDataPluginController();

	/**
	 * { @link BaseTenantDS } のインスタンスを生成する
	 * 
	 * @return { @link BaseTenantDS } のインスタンス
	 */
	BaseTenantDS createTenantDataSource();

	/**
	 * { @link BaseTenantDS } のインスタンスを生成する
	 * 
	 * @return { @link BaseTenantDS } のインスタンス
	 */
	BaseTenantDS createTenantImportSelectDataSource(Tenant tenant);
	
	/**
	 * { @link BaseTenantPropertyEditDialog } のインスタンスを生成する
	 * 
	 * @return { @link BaseTenantPropertyEditDialog } のインスタンス
	 */
	BaseTenantPropertyEditDialog createTenantPropertyEditDialog(Record record);
	
	/**
	 * {@link LangExplorerSettingController}  のインスタンスを生成する
	 * 
	 * @return {@link LangExplorerSettingController} のインスタンス
	 */
	LangExplorerSettingController createLangExplorerSettingController();

	/**
	 * {@link EntityExplorerViewPaneController}  のインスタンスを生成する
	 *
	 * @return {@link EntityExplorerViewPaneController} のインスタンス
	 */
	EntityExplorerViewPaneController createEntityExplorerViewPaneController();
}
