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

package org.iplass.adminconsole.client.metadata.ui.message;

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.event.DataChangedEvent;
import org.iplass.adminconsole.client.base.event.DataChangedHandler;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.io.upload.AdminSingleUploader;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.ui.widget.AbstractWindow;
import org.iplass.adminconsole.client.base.ui.widget.MessageTabSet;
import org.iplass.adminconsole.shared.base.dto.io.upload.UploadProperty;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.ui.Hidden;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class MessageItemCsvUploadDialog extends AbstractWindow {

	private static final String RESOURCE_PREFIX = "ui_metadata_message_MessageItemCsvUploadDialog_";
	private static final String UPLOAD_SERVICE = "service/messageupload";

	private IButton upload;
	private IButton cancel;

	private MessageTabSet messageTabSet;

	private List<DataChangedHandler> handlers = new ArrayList<DataChangedHandler>();

	/**
	 * コンストラクタ
	 */
	public MessageItemCsvUploadDialog(final String definitionName) {

		setWidth(700);
		setMinWidth(500);
		setHeight(500);
		setMinHeight(400);
		setTitle("Upload Message Csv Data");
		setCanDragResize(true);
		setShowMinimizeButton(false);
		setShowMaximizeButton(true);
		setIsModal(true);
		setShowModalMask(true);
		centerInPage();

		//------------------------
		//Input Items
		//------------------------
		Label title = new Label(getResourceString("descriptionMessage"));

		title.setHeight(40);
		title.setWidth100();

		HLayout fileComposit = new HLayout(5);
		fileComposit.setWidth100();
		fileComposit.setHeight(25);

		Label fileLabel = new Label("File :");
		fileLabel.setWrap(false);
		fileComposit.addMember(fileLabel);

		final AdminSingleUploader uploader = new AdminSingleUploader(UPLOAD_SERVICE);
		uploader.setValidExtensions("csv");
		uploader.addOnStartUploadHandler((result) -> {
			if (!result.isCanceled()) {
				uploader.debugUploader("onStart");
				startUpload();
			} else {
				uploader.removeHidden();
			}
		});
		uploader.addOnFinishUploadHandler((result) -> {
			uploader.debugUploader("onFinish");

			if (uploader.getLastUploadState().isSuccess()) {
				showResult(uploader.getLastUploadState().getData());
			} else {
				errorUpload(uploader.getLastUploadState().getErrorMessage());
			}

			//Hidden項目の削除
			uploader.removeHidden();
		});

		fileComposit.addMember(uploader);

		//------------------------
		//Buttons
		//------------------------
		upload = new IButton("Upload");
		upload.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (uploader.getFileName() == null || uploader.getFileName().isEmpty()) {
					SC.warn(getResourceString("selectImportFile"));
					return;
				}
				uploader.add(new Hidden(UploadProperty.TENANT_ID, Integer.toString(TenantInfoHolder.getId())));
				uploader.add(new Hidden("definitionName", definitionName));
				uploader.submit();
			}
		});
		cancel = new IButton("Cancel");
		cancel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				destroy();
			}
		});

		HLayout execButtons = new HLayout(5);
		execButtons.setMargin(5);
		execButtons.setHeight(20);
		execButtons.setWidth100();
		execButtons.setAlign(VerticalAlignment.CENTER);
		execButtons.setMembers(upload, cancel);

		//------------------------
		//Input Layout
		//------------------------
		VLayout inputLayout = new VLayout(5);
		inputLayout.setMargin(5);
		inputLayout.setHeight(100);

		inputLayout.addMember(title);
		inputLayout.addMember(fileComposit);
		inputLayout.addMember(execButtons);

		//------------------------
		//MessagePane
		//------------------------
		messageTabSet = new MessageTabSet();

		//------------------------
		//Main Layout
		//------------------------
		VLayout mainLayout = new VLayout();
		mainLayout.setWidth100();
		mainLayout.setHeight100();
		mainLayout.setMargin(10);

		mainLayout.addMember(inputLayout);
		mainLayout.addMember(messageTabSet);

		addItem(mainLayout);
	}

	private void startUpload() {
		disableComponent(true);
		messageTabSet.clearMessage();
		messageTabSet.setTabTitleProgress();
	}

	private void showResult(JSONValue message) {
		showResponse(message);

		disableComponent(false);
		messageTabSet.setTabTitleNormal();
	}

	private void showResponse(JSONValue rootValue) {
		GWT.log("Import Response:" + rootValue);
		if (rootValue == null) {
			return;
		}

		List<String> logs = new ArrayList<String>();

		String status = getStatus(rootValue);
		logs.add(getStatusMessage(status));

		List<String> messages = getMessageInfo(rootValue);
		if (!messages.isEmpty()) {
			logs.add("-----------------------------------");
			logs.addAll(messages);
		}
		if (isStatusSuccess(status)) {
			messageTabSet.setMessage(logs);

			//データ変更通知
			fireDataChanged();

		} else {
			messageTabSet.setErrorMessage(logs);
		}
	}
	private String getStatusMessage(String status) {
		if (UploadProperty.Status.SUCCESS.name().equals(status)) {
			return getResourceString("importSuccessful");
		} else if (UploadProperty.Status.WARN.name().equals(status)) {
			return getResourceString("importWarning");
		} else if (UploadProperty.Status.ERROR.name().equals(status)) {
			return getResourceString("importErr");
		} else {
			return getResourceString("couldNotRetImportResult");
		}
	}

	private List<String> getMessageInfo(JSONValue root) {
		List<String> messages = new ArrayList<String>();
		JSONArray messageArray = getValue("messages", root).isArray();
		for (int i = 0; i < messageArray.size(); i++) {
			JSONValue child = messageArray.get(i);
			messages.add(snipQuote(child.toString()));
		}
		return messages;
	}

	private JSONValue getValue(String key, JSONValue root) {
		JSONObject jsonObject = root.isObject();
		return jsonObject.get(key);
	}
	private String getStatus(JSONValue root) {
		return snipQuote(getValue("status", root).toString());
	}

	private String snipQuote(String value) {
		if (value.startsWith("\"") && value.endsWith("\"")) {
			return value.substring(1, value.length() - 1);
		} else {
			return value;
		}
	}

	private boolean isStatusSuccess(String status) {
		return UploadProperty.Status.SUCCESS.name().equals(status);
	}

	private String getResourceString(String key) {
		return AdminClientMessageUtil.getString(RESOURCE_PREFIX + key);
	}

	private void errorUpload(String message) {
		messageTabSet.setErrorMessage(getResourceString("uploadingErr"));

		disableComponent(false);
		messageTabSet.setTabTitleNormal();
	}

	private void disableComponent(boolean disabled) {
		upload.setDisabled(disabled);
		cancel.setDisabled(disabled);
	}

	/**
	 * 変更完了時のイベント
	 * @param handler
	 */
	public void addDataChangedHandler(DataChangedHandler handler) {
		handlers.add(handler);
	}

	private void fireDataChanged() {
		DataChangedEvent event = new DataChangedEvent();
		for (DataChangedHandler handler : handlers) {
			handler.onDataChanged(event);
		}
	}

}
