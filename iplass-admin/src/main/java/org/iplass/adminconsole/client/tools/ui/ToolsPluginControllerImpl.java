/*
 * Copyright (C) 2017 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.client.tools.ui;

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.plugin.AdminPlugin;
import org.iplass.adminconsole.client.tools.ui.auth.builtin.BuiltinAuthExplorerPlugin;
import org.iplass.adminconsole.client.tools.ui.entityexplorer.EntityExplorerPlugin;
import org.iplass.adminconsole.client.tools.ui.eql.EqlWorksheetPlugin;
import org.iplass.adminconsole.client.tools.ui.langexplorer.LangExplorerPlugin;
import org.iplass.adminconsole.client.tools.ui.logexplorer.LogExplorerPlugin;
import org.iplass.adminconsole.client.tools.ui.metaexplorer.MetaDataExplorerPlugin;
import org.iplass.adminconsole.client.tools.ui.openapisupport.OpenApiSupportPlugin;
import org.iplass.adminconsole.client.tools.ui.pack.PackagePlugin;
import org.iplass.adminconsole.client.tools.ui.permissionexplorer.PermissionExplorerPlugin;
import org.iplass.adminconsole.client.tools.ui.queueexplorer.QueueExplorerPlugin;

public class ToolsPluginControllerImpl implements ToolsPluginController {

	@Override
	public List<AdminPlugin> plugins() {

		List<AdminPlugin> plugins = new ArrayList<>();

		plugins.add(new EqlWorksheetPlugin());
		plugins.add(new EntityExplorerPlugin());
		plugins.add(new MetaDataExplorerPlugin());
		plugins.add(new PackagePlugin());
		plugins.add(new PermissionExplorerPlugin());
		plugins.add(new BuiltinAuthExplorerPlugin());
		plugins.add(new QueueExplorerPlugin());
		plugins.add(new LangExplorerPlugin());
		plugins.add(new LogExplorerPlugin());
		plugins.add(new OpenApiSupportPlugin());

		return plugins;
	}

}
