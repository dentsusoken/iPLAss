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

package org.iplass.adminconsole.client.tools.ui.entityexplorer.entityview.gem;

import org.iplass.adminconsole.client.base.io.download.PostDownloadFrame;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.ui.widget.AbstractWindow;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.shared.base.dto.io.download.DownloadProperty.ENCODE;
import org.iplass.adminconsole.shared.base.dto.io.download.DownloadProperty.FILETYPE;
import org.iplass.adminconsole.shared.tools.dto.entityexplorer.EntityConfigDownloadProperty.TARGET;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class GemEntityConfigDownloadDialog extends AbstractWindow {

	private SelectItem fileTypeField;

	private DynamicForm optionForm;

	public GemEntityConfigDownloadDialog(final String defName) {
		this(defName, null);
	}

	public GemEntityConfigDownloadDialog(final String[] defNames) {
		this(null, defNames);
	}

	public GemEntityConfigDownloadDialog(final String defName, final String[] defNames) {

		setWidth(400);
		setHeight(180);
		String title = "Export Entity Definition : ";
		if (defName != null) {
			title += defName;
		} else if (defNames != null && defNames.length > 0){
			title += defNames.length + " Entities";
		} else {
		}
		setTitle(title);

		setShowMinimizeButton(false);
		setIsModal(true);
		setShowModalMask(true);
		centerInPage();

		fileTypeField = new SelectItem();
		fileTypeField.setTitle("Output Type");
		fileTypeField.setValueMap(FILETYPE.CSV.name(), FILETYPE.XML.name());
		fileTypeField.setValue(FILETYPE.CSV.name());
		fileTypeField.addChangedHandler(new ChangedHandler() {
			@Override
			public void onChanged(ChangedEvent event) {
				dispTypeOption();
			}
		});

		SelectItem csvEncodeField = new SelectItem();
		csvEncodeField.setTitle("Encode");
		csvEncodeField.setValueMap(ENCODE.UTF8.name(), ENCODE.MS932.name());
		csvEncodeField.setValue(ENCODE.UTF8.name());

		final DynamicForm form = new DynamicForm();
		form.setWidth100();
		form.setItems(fileTypeField);

		optionForm = new DynamicForm();
		optionForm.setHeight100();
		optionForm.setWidth100();
		optionForm.setNumCols(2);
		optionForm.setColWidths(80, "*");
		optionForm.setItems(csvEncodeField);
		optionForm.setIsGroup(true);
		optionForm.setGroupTitle("Type Settings");

		IButton download = new IButton("Export");
		download.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				//POST化
				PostDownloadFrame frame = new PostDownloadFrame();
				frame.setAction(GWT.getModuleBaseURL() + "service/entityconfigdownload")
					.addParameter("tenantId", String.valueOf(TenantInfoHolder.getId()))
					.addParameter("definitionName", defName)
					.addParameter("definitionNames", defNamesString(defNames))
					.addParameter("fileType", FILETYPE.valueOf(SmartGWTUtil.getStringValue(fileTypeField)).name())
					.addParameter("csvOutputTarget", TARGET.VIEW.name())
					.addParameter("csvEncode", ENCODE.valueOf(SmartGWTUtil.getStringValue(csvEncodeField)).getValue())
					.addParameter("xmlEntity", "false")
					.addParameter("xmlEntityView", "true")
					.addParameter("xmlEntityFilter", "false")
					.addParameter("xmlEntityMenuItem", "false")
					.addParameter("xmlEntityWebAPI", "false")
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

		VLayout lauout = new VLayout(10);
		lauout.setWidth100();
		lauout.setHeight100();
		lauout.setPadding(10);
		lauout.setMembers(form, optionForm);

		HLayout footer = new HLayout(5);
		footer.setMargin(5);
		footer.setHeight(20);
		footer.setWidth100();
		footer.setAlign(VerticalAlignment.CENTER);
		footer.setMembers(download, cancel);

		addItem(lauout);
		addItem(SmartGWTUtil.separator());
		addItem(footer);

		dispTypeOption();
	}

	private void dispTypeOption() {
		FILETYPE type = FILETYPE.valueOf(fileTypeField.getValueAsString());
		if (FILETYPE.CSV == type) {
			optionForm.show();
		} else {
			optionForm.hide();
		}
	}

	private String defNamesString(String[] defNames) {
		if (defNames == null || defNames.length == 0) {
			return "";
		}

		StringBuilder builder = new StringBuilder();
		for (String defName : defNames) {
			builder.append(defName + ",");
		}
		builder.deleteCharAt(builder.length() - 1);
		return builder.toString();
	}
}
