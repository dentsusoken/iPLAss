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

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.event.AdminConsoleGlobalEventBus;
import org.iplass.adminconsole.client.base.event.ViewMetaDataEvent;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpComboBoxItem;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.data.MetaDataNameDS;
import org.iplass.adminconsole.client.metadata.data.MetaDataNameDS.MetaDataNameDSOption;
import org.iplass.mtp.definition.Definition;

import com.smartgwt.client.widgets.form.fields.FormItemIcon;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;

public class MetaDataComboBoxItem extends MtpComboBoxItem implements MtpWidgetConstants {

	public MetaDataComboBoxItem(final Class<? extends Definition> definition) {
		this(definition, null, null);
	}

	public MetaDataComboBoxItem(final Class<? extends Definition> definition, String title) {
		this(definition, title, null);
	}

	public MetaDataComboBoxItem(final Class<? extends Definition> definition, ItemOption option) {
		this(definition, null, option);
	}

	public MetaDataComboBoxItem(final Class<? extends Definition> definition, String title, ItemOption option) {
		super();

		if (title != null) {
			setTitle(title);
		}
		if (option == null) {
			option = new ItemOption();
		}

		List<FormItemIcon> icons = new ArrayList<FormItemIcon>();
		if (option.isShowJump()) {
			FormItemIcon iconJump = new FormItemIcon();
			iconJump.setSrc(ICON_SHOW_META_DATA);
			iconJump.addFormItemClickHandler(new FormItemClickHandler() {

				@Override
				public void onFormItemClick(FormItemIconClickEvent event) {

					String defName = SmartGWTUtil.getStringValue(MetaDataComboBoxItem.this);
					if (SmartGWTUtil.isNotEmpty(defName)) {
						ViewMetaDataEvent.fire(definition.getName(), defName, AdminConsoleGlobalEventBus.getEventBus());
					}

				}
			});
			iconJump.setPrompt("view the selected MetaData");
			iconJump.setBaseStyle("adminButtonRounded");
			icons.add(iconJump);
		}

		FormItemIcon iconRefresh = new FormItemIcon();
		iconRefresh.setSrc(ICON_REFRESH);
		iconRefresh.addFormItemClickHandler(new FormItemClickHandler() {

			@Override
			public void onFormItemClick(FormItemIconClickEvent event) {
				fetchData();
			}
		});
		iconRefresh.setPrompt("refresh MetaData list");
		iconRefresh.setBaseStyle("adminButtonRounded");
		icons.add(iconRefresh);

		setIcons(icons.toArray(new FormItemIcon[]{}));

		MetaDataNameDS.setDataSource(this, definition, option.toMetaDataNameDSOption());
	}

	public static class ItemOption {

		private boolean addBlank;
		private boolean addDefault;
		private String tooltip;
		private boolean showJump;

		public ItemOption() {
			this(false, false, false);
		}

		public ItemOption(boolean addBlank, boolean addDefault) {
			this(addBlank, addDefault, false);
		}

		public ItemOption(boolean addBlank, boolean addDefault, boolean showJump) {
			addBlank(addBlank);
			this.addDefault(addDefault);
			this.showJump(showJump);
		}

		public boolean isAddBlank() {
			return addBlank;
		}

		public ItemOption addBlank(boolean addBlank) {
			this.addBlank = addBlank;
			return this;
		}

		public boolean isAddDefault() {
			return addDefault;
		}

		public ItemOption addDefault(boolean addDefault) {
			this.addDefault = addDefault;
			return this;
		}

		public String getTooltip() {
			return tooltip;
		}

		public ItemOption tooltip(String tooltip) {
			this.tooltip = tooltip;
			return this;
		}

		public boolean isShowJump() {
			return showJump;
		}

		public ItemOption showJump(boolean showJump) {
			this.showJump = showJump;
			return this;
		}

		public MetaDataNameDSOption toMetaDataNameDSOption() {
			return new MetaDataNameDSOption(addBlank, addDefault, tooltip);
		}

	}

}
