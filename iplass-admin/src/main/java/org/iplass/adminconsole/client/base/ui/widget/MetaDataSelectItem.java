/*
 * Copyright (C) 2019 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.base.ui.widget;

import org.iplass.adminconsole.client.base.event.AdminConsoleGlobalEventBus;
import org.iplass.adminconsole.client.base.event.ViewMetaDataEvent;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.data.MetaDataNameDS;
import org.iplass.adminconsole.client.metadata.data.MetaDataNameDS.MetaDataNameDSOption;
import org.iplass.mtp.definition.Definition;

import com.smartgwt.client.widgets.form.fields.FormItemIcon;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;

public class MetaDataSelectItem extends SelectItem {

	public MetaDataSelectItem(final Class<? extends Definition> definition) {
		this(definition, new MetaDataNameDSOption(true, false));
	}

	public MetaDataSelectItem(final Class<? extends Definition> definition, MetaDataNameDSOption option) {
		super();

		FormItemIcon icon = new FormItemIcon();
		icon.setSrc(CommonIconConstants.COMMON_ICON_SHOW_META_DATA);
		icon.addFormItemClickHandler(new FormItemClickHandler() {

			@Override
			public void onFormItemClick(FormItemIconClickEvent event) {

				String defName = SmartGWTUtil.getStringValue(MetaDataSelectItem.this);
				if (SmartGWTUtil.isNotEmpty(defName)) {
					ViewMetaDataEvent.fire(definition.getName(), defName, AdminConsoleGlobalEventBus.getEventBus());
				}

			}
		});
		icon.setPrompt("view the selected MetaData");
		icon.setBaseStyle("adminButtonRounded");
		setIcons(icon);

		MetaDataNameDS.setDataSource(this, definition, option);
	}

}
