/*
 * Copyright (C) 2017 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui;

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.plugin.AdminPlugin;
import org.iplass.adminconsole.client.metadata.ui.action.ActionPlugin;
import org.iplass.adminconsole.client.metadata.ui.asynccommand.AsyncCommandPlugin;
import org.iplass.adminconsole.client.metadata.ui.auth.AuthenticationPolicyPlugin;
import org.iplass.adminconsole.client.metadata.ui.calendar.CalendarPlugin;
import org.iplass.adminconsole.client.metadata.ui.command.CommandPlugin;
import org.iplass.adminconsole.client.metadata.ui.entity.EntityPlugin;
import org.iplass.adminconsole.client.metadata.ui.mail.MailTemplatePlugin;
import org.iplass.adminconsole.client.metadata.ui.menu.MenuPlugin;
import org.iplass.adminconsole.client.metadata.ui.message.MessagePlugin;
import org.iplass.adminconsole.client.metadata.ui.prefs.PrefsPlugin;
import org.iplass.adminconsole.client.metadata.ui.pushnotification.PushNotificationTemplatePlugin;
import org.iplass.adminconsole.client.metadata.ui.selectvalue.SelectValuePlugin;
import org.iplass.adminconsole.client.metadata.ui.sms.SmsMailTemplatePlugin;
import org.iplass.adminconsole.client.metadata.ui.staticresource.StaticResourcePlugin;
import org.iplass.adminconsole.client.metadata.ui.template.TemplatePlugin;
import org.iplass.adminconsole.client.metadata.ui.tenant.TenantPlugin;
import org.iplass.adminconsole.client.metadata.ui.top.TopViewPlugin;
import org.iplass.adminconsole.client.metadata.ui.treeview.TreeViewPlugin;
import org.iplass.adminconsole.client.metadata.ui.utilityclass.UtilityClassPlugin;
import org.iplass.adminconsole.client.metadata.ui.webapi.WebApiPlugin;

public class MetaDataPluginControllerImpl implements MetaDataPluginController {

	@Override
	public List<AdminPlugin> plugins() {

		List<AdminPlugin> plugins = new ArrayList<AdminPlugin>();

		plugins.add(new TenantPlugin());
		plugins.add(new PrefsPlugin());
		plugins.add(new AuthenticationPolicyPlugin());

		plugins.add(new EntityPlugin());
		plugins.add(new SelectValuePlugin());
		plugins.add(new MessagePlugin());

		plugins.add(new TopViewPlugin());
		plugins.add(new MenuPlugin());
		plugins.add(new TreeViewPlugin());
		plugins.add(new CalendarPlugin());

		plugins.add(new ActionPlugin());
		plugins.add(new CommandPlugin());
		plugins.add(new TemplatePlugin());
		plugins.add(new StaticResourcePlugin());
		plugins.add(new AsyncCommandPlugin());
		plugins.add(new WebApiPlugin());
		plugins.add(new UtilityClassPlugin());

		plugins.add(new MailTemplatePlugin());
		plugins.add(new SmsMailTemplatePlugin());
		plugins.add(new PushNotificationTemplatePlugin());

		return plugins;
	}

}
