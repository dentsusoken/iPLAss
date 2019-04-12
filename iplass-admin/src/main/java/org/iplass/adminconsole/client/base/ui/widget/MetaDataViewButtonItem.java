/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;

/**
 * メタデータの編集画面を表示するためのButtonItem
 *
 * メタデータの相互参照時に対象メタデータの編集画面を開くためのボタン。
 *
 * 使い方はTemplateAttributePaneのLayoutActionを参考に。
 */
public class MetaDataViewButtonItem extends ButtonItem implements MtpWidgetConstants {

	private String definitionClassName;
	private MetaDataShowClickHandler handler;

	public MetaDataViewButtonItem(String definitionClassName) {
		this.definitionClassName = definitionClassName;

		setTitle("");
		setShowTitle(false);
		setStartRow(false);
		setEndRow(false);
		setIcon(ICON_SHOW_META_DATA);

		addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				doClick();
			}
		});
	}

	public void setMetaDataShowClickHandler(MetaDataShowClickHandler handler) {
		this.handler = handler;
	}

	private void doClick() {
		if (handler != null) {
			String defName = handler.targetDefinitionName();
			if (SmartGWTUtil.isNotEmpty(defName)) {
				ViewMetaDataEvent.fire(definitionClassName, defName, AdminConsoleGlobalEventBus.getEventBus());
			}
		}
	}

	public interface MetaDataShowClickHandler {

		/**
		 * クリックイベントです。
		 * MetaDataを表示する場合、そのNameを返してください。
		 */
		String targetDefinitionName();
	}

}
