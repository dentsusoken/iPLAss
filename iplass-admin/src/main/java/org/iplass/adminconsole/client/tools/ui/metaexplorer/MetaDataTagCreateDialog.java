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

package org.iplass.adminconsole.client.tools.ui.metaexplorer;

import java.sql.Timestamp;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.ui.widget.AbstractWindow;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.shared.tools.rpc.metaexplorer.MetaDataExplorerServiceAsync;
import org.iplass.adminconsole.shared.tools.rpc.metaexplorer.MetaDataExplorerServiceFactory;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class MetaDataTagCreateDialog extends AbstractWindow {

	private DynamicForm form;

	protected TextItem nameField;
	protected TextAreaItem descriptionField;

	public MetaDataTagCreateDialog() {

		setWidth(500);
		setHeight(280);
		setTitle("Create Tag");
		setShowMinimizeButton(false);
		setShowMaximizeButton(false);
		setCanDragResize(true);
		setIsModal(true);
		setShowModalMask(true);
		centerInPage();

		//------------------------
		//Title
		//------------------------
		Label title = new Label(AdminClientMessageUtil.getString("ui_tools_metaexplorer_MetaDataTagCreateDialog_createTagLocalMetaData"));
		title.setHeight(40);
		title.setWidth100();

		//------------------------
		//Input
		//------------------------

		nameField = new TextItem("name","Tag Name");
		nameField.setWidth("100%");
		SmartGWTUtil.setRequired(nameField);

		descriptionField = new TextAreaItem("description", "Comment");
		descriptionField.setWidth("100%");
		descriptionField.setHeight(100);

		form = new DynamicForm();
		form.setMargin(10);
		form.setHeight100();
		form.setWidth100();
		form.setNumCols(2);
		form.setColWidths(70, "*");
		form.setAutoFocus(true);

		form.setItems(nameField, descriptionField);

		//------------------------
		//Footer Layout
		//------------------------

		IButton backupButton = new IButton("Create");
		backupButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (form.validate()){
					executeBackup();
				}
			}
		});

		IButton cancelButton = new IButton("Cancel");
		cancelButton.addClickHandler(new ClickHandler() {
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
		footer.setMembers(backupButton, cancelButton);

		//------------------------
		//Main Layout
		//------------------------
		VLayout mainLayout = new VLayout();
		mainLayout.setWidth100();
		mainLayout.setHeight100();
		mainLayout.setMargin(10);

		mainLayout.addMember(title);
		mainLayout.addMember(form);

		addItem(mainLayout);
		addItem(SmartGWTUtil.separator());
		addItem(footer);

		initialize();
	}

	private void initialize() {
		Timestamp date = new Timestamp(System.currentTimeMillis());
		String dateStr = SmartGWTUtil.formatTimestamp(date);
		String nameValue = dateStr.replace("/", "").replace(":", "").replace(" ", "_");
		nameField.setValue("Tag_" + nameValue);
		descriptionField.setValue(AdminClientMessageUtil.getString("ui_tools_metaexplorer_MetaDataTagCreateDialog_defPoint", dateStr));
	}

	private void executeBackup() {
		SmartGWTUtil.showProgress();

		MetaDataExplorerServiceAsync service = MetaDataExplorerServiceFactory.get();
		service.createTag(TenantInfoHolder.getId(),
				SmartGWTUtil.getStringValue(nameField),
				SmartGWTUtil.getStringValue(descriptionField), new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				SmartGWTUtil.hideProgress();

				SC.warn(AdminClientMessageUtil.getString("ui_tools_metaexplorer_MetaDataTagCreateDialog_failedToCreateTag") + caught.getMessage());
			}

			@Override
			public void onSuccess(Void result) {
				SmartGWTUtil.hideProgress();

				SC.say(AdminClientMessageUtil.getString("ui_tools_metaexplorer_MetaDataTagCreateDialog_completion"),
						AdminClientMessageUtil.getString("ui_tools_metaexplorer_MetaDataTagCreateDialog_tagCreateComp"), new BooleanCallback() {

					@Override
					public void execute(Boolean value) {
						//ダイアログ消去
						destroy();
					}
				});
			}
		});
	}
}
