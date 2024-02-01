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

package org.iplass.adminconsole.client.metadata.ui.oauth.resource;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.rpc.AdminAsyncCallback;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.DefaultMetaDataPlugin;
import org.iplass.adminconsole.client.metadata.ui.MetaDataItemMenuTreeNode;
import org.iplass.adminconsole.client.metadata.ui.MetaDataMainEditPane;
import org.iplass.adminconsole.client.metadata.ui.common.MetaCommonAttributeSection;
import org.iplass.adminconsole.client.metadata.ui.common.MetaCommonHeaderPane;
import org.iplass.adminconsole.client.metadata.ui.common.MetaDataHistoryDialog;
import org.iplass.adminconsole.client.metadata.ui.common.MetaDataUpdateCallback;
import org.iplass.adminconsole.client.metadata.ui.common.StatusCheckUtil;
import org.iplass.adminconsole.client.metadata.ui.oauth.CredentialResultDialog;
import org.iplass.adminconsole.shared.metadata.dto.AdminDefinitionModifyResult;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;
import org.iplass.mtp.auth.oauth.definition.OAuthResourceServerDefinition;
import org.iplass.mtp.definition.DefinitionEntry;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.LayoutSpacer;
import com.smartgwt.client.widgets.layout.SectionStackSection;

/**
 * OAuthResourceServerDefinition編集パネル
 */
public class OAuthResourceServerEditPane extends MetaDataMainEditPane {

	/** メタデータサービス */
	private final MetaDataServiceAsync service;

	/** 編集対象 */
	private OAuthResourceServerDefinition curDefinition;

	/** 編集対象バージョン */
	private int curVersion;

	/** 編集対象ID */
	private String curDefinitionId;

	/** ヘッダ部分 */
	private MetaCommonHeaderPane headerPane;
	/** 共通属性部分 */
	private MetaCommonAttributeSection<OAuthResourceServerDefinition> commonSection;

	/** 個別属性部分 */
	private OAuthResourceServerAttributePane attributePane;

	public OAuthResourceServerEditPane(MetaDataItemMenuTreeNode targetNode, DefaultMetaDataPlugin plugin) {
		super(targetNode, plugin);

		service = MetaDataServiceFactory.get();

		// レイアウト設定
		setWidth100();

		// ヘッダ（ボタン）部分
		headerPane = new MetaCommonHeaderPane(targetNode);
		headerPane.setSaveClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				saveDefinition();
			}
		});
		headerPane.setCancelClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				cancelDefinition();
			}
		});

		headerPane.setHistoryClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				MetaDataHistoryDialog metaDataHistoryDialog = new MetaDataHistoryDialog(curDefinition.getClass().getName(), curDefinitionId, curVersion);
				metaDataHistoryDialog.show();
			}
		});

		LayoutSpacer space = new LayoutSpacer();
		space.setWidth(95);
		headerPane.addMember(space);
		IButton btnGenerateCredential = new IButton("Generate Credential");
		btnGenerateCredential.setWidth(150);
		btnGenerateCredential.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				generateCredential();
			}
		});
		headerPane.addMember(btnGenerateCredential);
		IButton btnDeleteOldCredential = new IButton("Delete Old Credential");
		btnDeleteOldCredential.setWidth(150);
		btnDeleteOldCredential.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				deleteOldCredential();
			}
		});
		headerPane.addMember(btnDeleteOldCredential);


		// 共通属性
		commonSection = new MetaCommonAttributeSection<>(targetNode, OAuthResourceServerDefinition.class, false);

		//共通属性を開く
		commonSection.setExpanded(true);

		// 個別属性
		attributePane = new OAuthResourceServerAttributePane();
		SectionStackSection attributeSection = createSection("OAuthResourceServer Attribute", attributePane);

		// Section設定
		setMainSections(commonSection, attributeSection);

		// 全体配置
		addMember(headerPane);
		addMember(mainStack);

		// 表示データの取得
		initializeData();
	}

	/**
	 * データ初期化処理
	 */
	private void initializeData() {
		// エラーのクリア
		commonSection.clearErrors();

		service.getDefinitionEntry(TenantInfoHolder.getId(), OAuthResourceServerDefinition.class.getName(), defName, new AsyncCallback<DefinitionEntry>() {

			@Override
			public void onSuccess(DefinitionEntry result) {
				// 画面に反映
				setDefinition(result);
			}

			@Override
			public void onFailure(Throwable caught) {
				SC.say(AdminClientMessageUtil.getString("ui_metadata_oauth_resource_OAuthResourceServerEditPane_failed"),
						AdminClientMessageUtil.getString("ui_metadata_oauth_resource_OAuthResourceServerEditPane_failedGetScreenInfo") + caught.getMessage());

				GWT.log(caught.toString(), caught);
			}
		});

		// ステータスチェック
		StatusCheckUtil.statuCheck(OAuthResourceServerDefinition.class.getName(), defName, this);
	}

	/**
	 * Definition画面設定処理
	 *
	 * @param definition 編集対象
	 */
	private void setDefinition(DefinitionEntry entry) {
		this.curDefinition = (OAuthResourceServerDefinition) entry.getDefinition();
		this.curVersion = entry.getDefinitionInfo().getVersion();
		this.curDefinitionId = entry.getDefinitionInfo().getObjDefId();

		commonSection.setDefinition(curDefinition);
		attributePane.setDefinition(curDefinition);
	}

	/**
	 * 保存ボタン処理
	 */
	private void saveDefinition() {

		boolean commonValidate = commonSection.validate();
		boolean attributeValidate = attributePane.validate();
		if (!commonValidate || !attributeValidate) {
			return;
		}

		SC.ask(AdminClientMessageUtil.getString("ui_metadata_oauth_resource_OAuthResourceServerEditPane_saveConfirm"),
				AdminClientMessageUtil.getString("ui_metadata_oauth_resource_OAuthResourceServerEditPane_saveComment"), new BooleanCallback() {

			@Override
			public void execute(Boolean value) {
				if (value) {
					final OAuthResourceServerDefinition definition = curDefinition;
					commonSection.getEditDefinition(definition);
					attributePane.getEditDefinition(definition);

					updateDefinition(definition, true);
				}
			}
		});
	}

	/**
	 * キャンセルボタン処理
	 */
	private void cancelDefinition() {

		SC.ask(AdminClientMessageUtil.getString("ui_metadata_oauth_resource_OAuthResourceServerEditPane_cancelConfirm"),
				AdminClientMessageUtil.getString("ui_metadata_oauth_resource_OAuthResourceServerEditPane_cancelConfirmComment")
				, new BooleanCallback() {
			@Override
			public void execute(Boolean value) {
				if (value) {
					//再表示
					initializeData();
					commonSection.refreshSharedConfig();
				}
			}
		});
	}

	/**
	 * 更新処理
	 *
	 * @param definition 更新対象
	 */
	private void updateDefinition(final OAuthResourceServerDefinition definition, boolean checkVersion) {
		SmartGWTUtil.showSaveProgress();
		service.updateDefinition(TenantInfoHolder.getId(), definition, curVersion, checkVersion, new MetaDataUpdateCallback() {

			@Override
			protected void overwriteUpdate() {
				updateDefinition(definition, false);
			}

			@Override
			protected void afterUpdate(AdminDefinitionModifyResult result) {
				updateDefinitionComplete(definition);

			}
		});
	}

	/**
	 * 更新完了処理
	 *
	 * @param definition 更新対象
	 */
	private void updateDefinitionComplete(OAuthResourceServerDefinition definition) {

		SC.say(AdminClientMessageUtil.getString("ui_metadata_oauth_resource_OAuthResourceServerEditPane_completion"),
				AdminClientMessageUtil.getString("ui_metadata_oauth_resource_OAuthResourceServerEditPane_saveComp"));

		//再表示
		initializeData();
		commonSection.refreshSharedConfig();

		//ツリー再表示
		plugin.refreshWithSelect(definition.getName(), new AsyncCallback<MetaDataItemMenuTreeNode>() {
			@Override
			public void onSuccess(MetaDataItemMenuTreeNode result) {
				headerPane.setTargetNode(result);
				commonSection.setTargetNode(result);
			}

			@Override
			public void onFailure(Throwable caught) {
			}
		});
	}

	private void generateCredential() {

		SC.ask(AdminClientMessageUtil.getString("ui_metadata_oauth_resource_OAuthResourceServerEditPane_generateCredentialConfirm"), new BooleanCallback() {

			@Override
			public void execute(Boolean value) {
				if (value) {
					SmartGWTUtil.showProgress();
					service.generateCredentialOAuthResourceServer(TenantInfoHolder.getId(), commonSection.getName(), new AdminAsyncCallback<String>() {

						@Override
						public void onSuccess(String result) {
							SmartGWTUtil.hideProgress();
							CredentialResultDialog dialog = new CredentialResultDialog(result);
							dialog.show();
						}

						@Override
						protected void beforeFailure(Throwable caught){
							SmartGWTUtil.hideProgress();
						};
					});
				}
			}
		});
	}

	private void deleteOldCredential() {

		SC.ask(AdminClientMessageUtil.getString("ui_metadata_oauth_resource_OAuthResourceServerEditPane_deleteOldCredentialConfirm"),
				AdminClientMessageUtil.getString("ui_metadata_oauth_resource_OAuthResourceServerEditPane_deleteOldCredentialConfirm2"), new BooleanCallback() {

			@Override
			public void execute(Boolean value) {
				if (value) {
					SmartGWTUtil.showProgress();
					service.deleteOldCredentialOAuthResourceServer(TenantInfoHolder.getId(), commonSection.getName(), new AdminAsyncCallback<Void>() {

						@Override
						public void onSuccess(Void result) {
							SmartGWTUtil.hideProgress();
						}

						@Override
						protected void beforeFailure(Throwable caught){
							SmartGWTUtil.hideProgress();
						};
					});
				}
			}
		});
	}

}
