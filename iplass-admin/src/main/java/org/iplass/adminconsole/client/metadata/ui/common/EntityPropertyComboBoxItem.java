/*
 * Copyright (C) 2019 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.common;

import org.iplass.adminconsole.client.base.ui.widget.MtpWidgetConstants;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpComboBoxItem;
import org.iplass.adminconsole.client.metadata.data.entity.PropertyDS;

import com.smartgwt.client.widgets.form.fields.FormItemIcon;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;

public class EntityPropertyComboBoxItem extends MtpComboBoxItem implements MtpWidgetConstants {

	public EntityPropertyComboBoxItem(final String defName) {
		this(defName, null, null, null);
	}

	public EntityPropertyComboBoxItem(final String defName, String[] excludeTypes) {
		this(defName, null, null, excludeTypes);
	}

	public EntityPropertyComboBoxItem(final String defName, final String refPropertyName) {
		this(defName, refPropertyName, null, null);
	}

	public EntityPropertyComboBoxItem(final String defName, final String refPropertyName, final String title, String[] excludeTypes) {
		super();

		if (title != null) {
			setTitle(title);
		}

		FormItemIcon iconRefresh = new FormItemIcon();
		iconRefresh.setSrc(ICON_REFRESH);
		iconRefresh.addFormItemClickHandler(new FormItemClickHandler() {

			@Override
			public void onFormItemClick(FormItemIconClickEvent event) {
				fetchData();
			}
		});
		iconRefresh.setPrompt("refresh property list");
		iconRefresh.setBaseStyle("adminButtonRounded");

		setIcons(iconRefresh);

		PropertyDS.setDataSource(this, defName, refPropertyName, excludeTypes);
	}

	public void resetDataSource(String defName) {
		resetDataSource(defName, null);
	}

	public void resetDataSource(String defName, String refPropertyName) {
		PropertyDS.setDataSource(this, defName, refPropertyName);
	}

}
