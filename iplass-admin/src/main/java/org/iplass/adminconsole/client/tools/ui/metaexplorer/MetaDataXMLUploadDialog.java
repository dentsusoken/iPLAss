/*
 * Copyright (C) 2012 DENTSU SOKEN INC. All Rights Reserved.
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

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.io.upload.AdminSingleUploader;
import org.iplass.adminconsole.client.base.io.upload.UploadResultInfo;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.ui.widget.AbstractWindow;
import org.iplass.adminconsole.client.base.ui.widget.MessageTabSet;
import org.iplass.adminconsole.shared.base.dto.io.upload.UploadProperty;
import org.iplass.adminconsole.shared.tools.dto.metaexplorer.ConfigUploadProperty;

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

/**
 * MetaData XMLファイルをアップロードするダイアログ
 */
public class MetaDataXMLUploadDialog extends AbstractWindow {

	private static final String UPLOAD_SERVICE = "service/metaconfigupload";

	private IButton upload;
	private IButton cancel;

	private MessageTabSet messageTabSet;

	private MetaDataListPane owner;

	/**
	 * コンストラクタ
	 */
	public MetaDataXMLUploadDialog(MetaDataListPane owner) {
		this.owner = owner;

		setWidth(700);
		setMinWidth(500);
		setHeight(500);
		setMinHeight(400);
		setTitle("Upload MetaData");
		setCanDragResize(true);
		setShowMinimizeButton(false);
		setShowMaximizeButton(true);
		setIsModal(true);
		setShowModalMask(true);
		centerInPage();

		//------------------------
		//Input Items
		//------------------------
		Label title = new Label(AdminClientMessageUtil.getString("ui_tools_metaexplorer_MetaDataXMLUploadDialog_importLocalMetaDataXmlFile"));
		title.setHeight(40);
		title.setWidth100();

		HLayout fileComposit = new HLayout(5);
		fileComposit.setWidth100();
		fileComposit.setHeight(25);

		Label fileLabel = new Label("File :");
		fileLabel.setWrap(false);
		fileComposit.addMember(fileLabel);

		final AdminSingleUploader uploader = new AdminSingleUploader(UPLOAD_SERVICE);
		uploader.setValidExtensions("xml");
		uploader.addOnStartUploadHandler((result) -> {
			uploader.debugUploader("onStart");
			startUpload();
		});
		uploader.addOnFinishUploadHandler((result) -> {
			uploader.debugUploader("onFinish");
			if (uploader.getLastUploadState().isSuccess()) {
				finishUpload(uploader.getLastUploadState().getData());
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
					SC.warn(AdminClientMessageUtil.getString("ui_tools_metaexplorer_MetaDataXMLUploadDialog_selectImportFile"));
					return;
				}
				uploader.add(new Hidden(UploadProperty.TENANT_ID, Integer.toString(TenantInfoHolder.getId())));
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

	private void finishUpload(JSONValue message) {
		//JSON->Result
		XMLUploadResultInfo result = new XMLUploadResultInfo(message);

		//画面に結果を表示
		if (!result.isFileUploadStatusSuccess()) {
			messageTabSet.setErrorMessage(result.getLog());
			disableComponent(false);
			messageTabSet.setTabTitleNormal();
			return;
		}
		messageTabSet.setMessage(result.getLog());

		//Uploadファイルの確認
		List<String> messages = new ArrayList<String>();
		messages.add("-----------------------------------");
		messages.add(AdminClientMessageUtil.getString("ui_tools_metaexplorer_MetaDataXMLUploadDialog_dispImportDialog"));
		messageTabSet.addMessage(messages);

		showImportDialog(result);
	}

	private void errorUpload(String message) {
		messageTabSet.setErrorMessage(AdminClientMessageUtil.getString("ui_tools_metaexplorer_MetaDataXMLUploadDialog_uploadingErr"));

		disableComponent(false);
		messageTabSet.setTabTitleNormal();
	}

	private void showImportDialog(XMLUploadResultInfo result) {
		disableComponent(false);
		messageTabSet.setTabTitleNormal();

		MetaDataImportDialog.showFullScreen(result.fileOid, owner);
		destroy();
	}

	private void disableComponent(boolean disabled) {
		upload.setDisabled(disabled);
		cancel.setDisabled(disabled);
	}

	private class XMLUploadResultInfo extends UploadResultInfo{

		String fileOid;

		public XMLUploadResultInfo(JSONValue json) {
			super(json);

			fileOid = getFileUploadFileOid();
		}

		public List<String> getLog() {
			List<String> logs = new ArrayList<String>();
			logs.add(getFileUploadStatusMessage(getStatus()));
			if (getStatusMessages() != null && !getStatusMessages().isEmpty()) {
				logs.add("-----------------------------------");
				logs.addAll(getStatusMessages());
			}
			if (getMessages() != null && !getMessages().isEmpty()) {
				logs.add("-----------------------------------");
				logs.addAll(getMessages());
			}
			logs.add("-----------------------------------");
			return logs;
		}

		private String getFileUploadFileOid() {
			return getValue(ConfigUploadProperty.FILE_OID);
		}

		private String getFileUploadStatusMessage(String status) {
			if (ConfigUploadProperty.Status.SUCCESS.name().equals(status)) {
				return AdminClientMessageUtil.getString("ui_tools_metaexplorer_MetaDataXMLUploadDialog_uploadSuccessful");
			} else if (ConfigUploadProperty.Status.WARN.name().equals(status)) {
				return AdminClientMessageUtil.getString("ui_tools_metaexplorer_MetaDataXMLUploadDialog_uploadWarn");
			} else if (ConfigUploadProperty.Status.ERROR.name().equals(status)) {
				return AdminClientMessageUtil.getString("ui_tools_metaexplorer_MetaDataXMLUploadDialog_uploadErr");
			} else {
				return AdminClientMessageUtil.getString("ui_tools_metaexplorer_MetaDataXMLUploadDialog_notGetUploadResult");
			}
		}
	}

}
