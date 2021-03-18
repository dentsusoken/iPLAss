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

package org.iplass.adminconsole.client.tools.ui.pack.operation;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.io.download.PostDownloadFrame;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.ui.widget.MessageTabSet;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.tools.ui.pack.CreateOperationPane;
import org.iplass.adminconsole.client.tools.ui.pack.PackageCreateDialog;
import org.iplass.adminconsole.shared.metadata.dto.MetaDataConstants;
import org.iplass.adminconsole.shared.tools.dto.pack.PackageCreateInfo;
import org.iplass.adminconsole.shared.tools.dto.pack.PackageCreateResultInfo;
import org.iplass.adminconsole.shared.tools.dto.pack.PackageDownloadProperty;
import org.iplass.adminconsole.shared.tools.rpc.pack.PackageRpcServiceAsync;
import org.iplass.adminconsole.shared.tools.rpc.pack.PackageRpcServiceFactory;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class CreatePackagePane extends VLayout implements CreateOperationPane {

	private PackageCreateInfo createInfo;

	private TextItem nameField;
	private TextAreaItem descriptionField;

	private CheckboxItem chkAsyncField;

	private IButton executeButton;
	private IButton downloadButton;

	private MessageTabSet messageTab;

	private PackageCreateDialog owner;

	private String packOid;

	public CreatePackagePane(PackageCreateDialog owner) {
		this.owner = owner;

		// レイアウト設定
		setWidth100();
		setHeight100();

		setOverflow(Overflow.VISIBLE);

		//------------------------
		//Info
		//------------------------
		final DynamicForm infoForm = new DynamicForm();
		//infoForm.setMargin(10);
		//form.setHeight100();
//		infoForm.setHeight("30%");
		infoForm.setWidth100();
		infoForm.setPadding(5);
		infoForm.setNumCols(2);
		infoForm.setColWidths(90, "*");
		infoForm.setIsGroup(true);
		infoForm.setGroupTitle("Package Information:");

		nameField = new TextItem("name","Name");
		nameField.setWidth(MetaDataConstants.DEFAULT_FORM_ITEM_WIDTH);
		SmartGWTUtil.setRequired(nameField);

		descriptionField = new TextAreaItem("description", "Comment");
//		descriptionField.setWidth("100%");
//		descriptionField.setWidth(MetaDataConstants.DEFAULT_FORM_ITEM_WIDTH);
		descriptionField.setWidth(450);
		descriptionField.setHeight(100);

		infoForm.setItems(nameField, descriptionField);

		//------------------------
		//Exec
		//------------------------
		final DynamicForm execForm = new DynamicForm();
		//execForm.setMargin(10);
		execForm.setWidth100();
		execForm.setPadding(5);
		execForm.setNumCols(2);
		execForm.setColWidths(90, "*");
		execForm.setIsGroup(true);
		execForm.setGroupTitle("Package Execute Setting:");

		chkAsyncField = new CheckboxItem();
//		chkAsyncField.setTitle("Package作成処理を非同期で実行する（Not Released）");
		chkAsyncField.setTitle(AdminClientMessageUtil.getString("ui_tools_pack_operation_CreatePackagePane_runAsynchronously"));
		chkAsyncField.setShowTitle(false);
		chkAsyncField.setColSpan(2);
//		chkAsyncField.setDisabled(true);	//暫定

		execForm.setItems(chkAsyncField);

		//------------------------
		//Footer Layout
		//------------------------

		executeButton = new IButton("Create Package");
		executeButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (infoForm.validate() && validate()){
					executePack();
				}
			}
		});

		downloadButton = new IButton("Download Package");
		downloadButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				downloadPack();
			}
		});
		downloadButton.setVisible(false);	//初期非表示

		HLayout footer = new HLayout(5);
		footer.setMargin(5);
		footer.setHeight(20);
		footer.setWidth100();
		//footer.setAlign(Alignment.LEFT);
		footer.setAlign(VerticalAlignment.CENTER);
		footer.setMembers(executeButton, downloadButton);

		//------------------------
		//MessagePane
		//------------------------
		messageTab = new MessageTabSet();

		//------------------------
		//Main Layout
		//------------------------
		VLayout mainLayout = new VLayout();
		mainLayout.setWidth100();
		mainLayout.setHeight100();
		mainLayout.setMargin(10);
		mainLayout.setMembersMargin(5);

		mainLayout.addMember(infoForm);
		mainLayout.addMember(execForm);
		mainLayout.addMember(footer);
		mainLayout.addMember(messageTab);

		addMember(mainLayout);
	}

	private void executePack() {
		SC.ask(AdminClientMessageUtil.getString("ui_tools_pack_operation_CreatePackagePane_confirm"),
				AdminClientMessageUtil.getString("ui_tools_pack_operation_CreatePackagePane_startPackageConf"), new BooleanCallback() {

			@Override
			public void execute(Boolean value) {
				if (value) {
					disableComponent(true);
					messageTab.clearMessage();
					messageTab.setTabTitleProgress();

					storePack();
				}
			}

		});
	}

	private void storePack() {
		//設定情報を反映
		applyTo(createInfo);

		//登録
		PackageRpcServiceAsync service = PackageRpcServiceFactory.get();
		service.storePackage(TenantInfoHolder.getId(), createInfo, new AsyncCallback<String>() {

			@Override
			public void onSuccess(String result) {
				packOid = result;
				messageTab.addMessage(AdminClientMessageUtil.getString("ui_tools_pack_operation_CreatePackagePane_registPackageInfo") + packOid + ")");

				//追加されたので一覧を更新
				owner.refreshGrid();

				//続けてPackage作成の実行
				startPack(result);
			}

			@Override
			public void onFailure(Throwable caught) {
				GWT.log(AdminClientMessageUtil.getString("ui_tools_pack_operation_CreatePackagePane_failedToCreatePackageInfo"), caught);
				errorPack(AdminClientMessageUtil.getString("ui_tools_pack_operation_CreatePackagePane_failedToCreatePackageInfoCause") + caught.getMessage());
			}
		});
	}

	private void startPack(String oid) {

		if (SmartGWTUtil.getBooleanValue(chkAsyncField)) {
			//非同期
			messageTab.addMessage(AdminClientMessageUtil.getString("ui_tools_pack_operation_CreatePackagePane_startAsynchronouslyPackage"));

			PackageRpcServiceAsync service = PackageRpcServiceFactory.get();
			service.asyncCreatePackage(TenantInfoHolder.getId(), oid, new AsyncCallback<Void>() {

				@Override
				public void onSuccess(Void result) {
					messageTab.addMessage(AdminClientMessageUtil.getString("ui_tools_pack_operation_CreatePackagePane_startedPackageCreateProcess"));
					messageTab.addMessage(AdminClientMessageUtil.getString("ui_tools_pack_operation_CreatePackagePane_processPackageListCheck"));

					//作成終了後は「Create」ボタンは使用不可にする
					//disableComponent(false);
					messageTab.setTabTitleNormal();
				}

				@Override
				public void onFailure(Throwable caught) {
					GWT.log(AdminClientMessageUtil.getString("ui_tools_pack_operation_CreatePackagePane_failedToStartPackageProcess"), caught);
					errorPack(AdminClientMessageUtil.getString("ui_tools_pack_operation_CreatePackagePane_failedToStartPackageProcessCause") + caught.getMessage());
				}
			});
		} else {
			//同期
			messageTab.addMessage(AdminClientMessageUtil.getString("ui_tools_pack_operation_CreatePackagePane_startPackageCreateProcess"));

			PackageRpcServiceAsync service = PackageRpcServiceFactory.get();
			service.syncCreatePackage(TenantInfoHolder.getId(), oid, new AsyncCallback<PackageCreateResultInfo>() {

				@Override
				public void onSuccess(PackageCreateResultInfo result) {
					if (result.isError()) {
						messageTab.addErrorMessage(AdminClientMessageUtil.getString("ui_tools_pack_operation_CreatePackagePane_failedToCreatePackage"));
						messageTab.addErrorMessage(result.getMessages());

						disableComponent(false);
						messageTab.setTabTitleNormal();
					} else {
						messageTab.addMessage(result.getMessages());
						messageTab.addMessage(AdminClientMessageUtil.getString("ui_tools_pack_operation_CreatePackagePane_createPackageComp"));

						//作成終了後は「Create」ボタンは使用不可にする
						//disableComponent(false);

						//追加されたので一覧を更新
						owner.refreshGrid();

						//ダウンロードボタン表示
						downloadButton.setVisible(true);

						messageTab.setTabTitleNormal();
					}
				}

				@Override
				public void onFailure(Throwable caught) {
					GWT.log(AdminClientMessageUtil.getString("ui_tools_pack_operation_CreatePackagePane_failedToCreatePackage"), caught);
					errorPack(AdminClientMessageUtil.getString("ui_tools_pack_operation_CreatePackagePane_failedToCreatePackageCause") + caught.getMessage());
				}
			});

		}
	}

	private void errorPack(String message) {
		messageTab.addErrorMessage(message);

		disableComponent(false);
		messageTab.setTabTitleNormal();
	}

	private void downloadPack() {
		PostDownloadFrame frame = new PostDownloadFrame();
		frame.setAction(GWT.getModuleBaseURL() + PackageDownloadProperty.ACTION_URL)
			.addParameter(PackageDownloadProperty.TENANT_ID, String.valueOf(TenantInfoHolder.getId()))
			.addParameter(PackageDownloadProperty.FILE_OID, packOid)
			.execute();
	}

	private boolean validate() {
		int metaDataCount = 0;
		int entityCount = 0;
		if (createInfo.getMetaDataPaths() != null) {
			metaDataCount = createInfo.getMetaDataPaths().size();
		}
		if (createInfo.getEntityPaths() != null) {
			entityCount = createInfo.getEntityPaths().size();
		}

		if (metaDataCount == 0 && entityCount == 0) {
			SC.warn(AdminClientMessageUtil.getString("ui_tools_pack_operation_CreatePackagePane_notSelect"));
			return false;
		}
		return true;
	}

	private void disableComponent(boolean disabled) {
		executeButton.setDisabled(disabled);
	}

	@Override
	public void applyTo(PackageCreateInfo info) {
		info.setName(SmartGWTUtil.getStringValue(nameField));
		info.setDescription(SmartGWTUtil.getStringValue(descriptionField));
	}

	@Override
	public void setCurrentCreateInfo(PackageCreateInfo info) {
		this.createInfo = info;
	}
}
