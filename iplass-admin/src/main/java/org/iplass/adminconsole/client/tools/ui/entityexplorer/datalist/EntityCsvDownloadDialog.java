/*
 * Copyright (C) 2013 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.tools.ui.entityexplorer.datalist;

import org.iplass.adminconsole.client.base.io.download.PostDownloadFrame;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.ui.widget.AbstractWindow;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.layout.HLayout;

public class EntityCsvDownloadDialog extends AbstractWindow {

	public enum ENCODE {
		UTF8,
		MS932
	}

	public EntityCsvDownloadDialog(final String defName
			, final String whereClause
			, final String orderByClause
			, final boolean isSearchAllVersion) {

		setWidth(400);
		setHeight(150);
		setTitle("Export Entity Data : " + defName);
		setShowMinimizeButton(false);
		setIsModal(true);
		setShowModalMask(true);
		centerInPage();

		final SelectItem csvEncodeField = new SelectItem();
		csvEncodeField.setTitle("Encode");
		csvEncodeField.setValueMap(ENCODE.UTF8.name(), ENCODE.MS932.name());
		csvEncodeField.setValue(ENCODE.UTF8.name());
		csvEncodeField.setDisabled(true);	//UTF-8固定

		final DynamicForm form = new DynamicForm();
		form.setMargin(5);
		form.setHeight100();
		form.setWidth100();
		form.setItems(csvEncodeField);

		IButton save = new IButton("Export");
		save.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				//POST化
				PostDownloadFrame frame = new PostDownloadFrame();
				frame.setAction(GWT.getModuleBaseURL() + "service/entitydownload")
					.addParameter("tenantId", String.valueOf(TenantInfoHolder.getId()))
					.addParameter("definitionName", defName)
					.addParameter("whereClause", whereClause)
					.addParameter("orderByClause", orderByClause)
					.addParameter("isSearchAllVersion", isSearchAllVersion + "")
					.execute();
				destroy();
			}
		});

		IButton cancel = new IButton("Cancel");
		cancel.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				destroy();
			}
		});

		HLayout footer = new HLayout(5);
		footer.setMargin(5);
		footer.setHeight(20);
		footer.setWidth100();
		//footer.setAlign(Alignment.LEFT);
		footer.setAlign(VerticalAlignment.CENTER);
		footer.setMembers(save, cancel);

		addItem(form);
		addItem(SmartGWTUtil.separator());
		addItem(footer);
	}
}
