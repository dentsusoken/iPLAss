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

package org.iplass.adminconsole.client.base.ui.widget;

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpTextItem;
import org.iplass.adminconsole.client.metadata.ui.common.LocalizedStringSettingDialog;
import org.iplass.mtp.definition.LocalizedStringDefinition;

import com.smartgwt.client.widgets.form.fields.FormItemIcon;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;

public class MetaDataLangTextItem extends MtpTextItem implements MtpWidgetConstants {

	private List<LocalizedStringDefinition> localizedList;

	public MetaDataLangTextItem() {
		super();
		init(true);
	}

	public MetaDataLangTextItem(FormItemIcon... icons) {
		super();
		init(true, icons);
	}

	public MetaDataLangTextItem(String name, FormItemIcon... icons) {
		super(name);
		init(true, icons);
	}

	public MetaDataLangTextItem(String name, String title, FormItemIcon... icons) {
		super(name, title);
		init(true, icons);
	}

	public MetaDataLangTextItem(boolean showLang, FormItemIcon... icons) {
		super();
		init(showLang, icons);
	}

	private void init(boolean showLang, FormItemIcon... icons) {

		List<FormItemIcon> iconList = new ArrayList<FormItemIcon>();
		if (showLang) {
			FormItemIcon langIcon = new FormItemIcon();
			langIcon.setSrc(ICON_LANG);
			langIcon.addFormItemClickHandler(new FormItemClickHandler() {

				@Override
				public void onFormItemClick(FormItemIconClickEvent event) {

					if (localizedList == null) {
						localizedList = new ArrayList<LocalizedStringDefinition>();
					}
					LocalizedStringSettingDialog dialog = new LocalizedStringSettingDialog(localizedList);
					dialog.show();
				}
			});
			langIcon.setPrompt(AdminClientMessageUtil.getString("ui_metadata_common_MetaCommonAttributeSection_eachLangDspName"));
			langIcon.setBaseStyle("adminButtonRounded");
			iconList.add(langIcon);
		}
		if (icons != null) {
			for (FormItemIcon icon : icons) {
				icon.setBaseStyle("adminButtonRounded");
				iconList.add(icon);
			}
		}
		if (!iconList.isEmpty()) {
			setIcons(iconList.toArray(new FormItemIcon[]{}));
		}
	}

	public List<LocalizedStringDefinition> getLocalizedList() {
		return localizedList != null ? localizedList.isEmpty() ? null : localizedList : null;
	}

	public void setLocalizedList(List<LocalizedStringDefinition> localizedList) {
		this.localizedList = localizedList;
	}

}
