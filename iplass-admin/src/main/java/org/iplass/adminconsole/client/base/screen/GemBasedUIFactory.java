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
package org.iplass.adminconsole.client.base.screen;

import org.iplass.adminconsole.client.metadata.data.tenant.BaseTenantDS;
import org.iplass.adminconsole.client.metadata.data.tenant.GemTenantDS;
import org.iplass.adminconsole.client.metadata.ui.GemMetaDataPluginController;
import org.iplass.adminconsole.client.metadata.ui.MetaDataPluginController;
import org.iplass.adminconsole.client.tools.data.metaexplorer.GemTenantImportSelectDS;
import org.iplass.adminconsole.client.tools.ui.langexplorer.GemLangExplorerSettingController;
import org.iplass.adminconsole.client.tools.ui.langexplorer.LangExplorerSettingController;
import org.iplass.mtp.tenant.Tenant;

/**
 * GEMモジュールに依存したUIクラスを生成するFactory
 *
 * @author Y.Ishida
 *
 */
public class GemBasedUIFactory implements ScreenModuleBasedUIFactory {

	@Override
	public MetaDataPluginController createMetaDataPluginController() {
		return new GemMetaDataPluginController();
	}

	@Override
	public BaseTenantDS createTenantDataSource() {
		return GemTenantDS.getInstance();
	}

	@Override
	public BaseTenantDS createTenantImportSelectDataSource(Tenant tenant) {
		return GemTenantImportSelectDS.getInstance(tenant);
	}

	@Override
	public LangExplorerSettingController createLangExplorerSettingController() {
		return new GemLangExplorerSettingController();
	}
}
