/*
 * Copyright (C) 2025 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.adminconsole.client.metadata.ui.webauthn;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
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
import org.iplass.adminconsole.shared.metadata.dto.AdminDefinitionModifyResult;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;
import org.iplass.mtp.auth.webauthn.definition.WebAuthnDefinition;
import org.iplass.mtp.definition.DefinitionEntry;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.SectionStackSection;

public class WebAuthnEditPane extends MetaDataMainEditPane {

	/** メタデータサービス */
	private final MetaDataServiceAsync service;

	/** 編集対象 */
	private WebAuthnDefinition curDefinition;

	/** 編集対象バージョン */
	private int curVersion;

	/** 編集対象ID */
	private String curDefinitionId;

	/** ヘッダ部分 */
	private MetaCommonHeaderPane headerPane;
	/** 共通属性部分 */
	private MetaCommonAttributeSection<WebAuthnDefinition> commonSection;

	/** 個別属性部分 */
	private WebAuthnAttributePane attrPane;

	public WebAuthnEditPane(MetaDataItemMenuTreeNode targetNode, DefaultMetaDataPlugin plugin) {
		super(targetNode, plugin);

		service = MetaDataServiceFactory.get();

		//レイアウト設定
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
		
		// 共通属性
		commonSection = new MetaCommonAttributeSection<>(targetNode, WebAuthnDefinition.class, true);

		// 個別属性
		attrPane = new WebAuthnAttributePane();

		// Section設定
		SectionStackSection webAuthnAttributeSection = createSection("WebAuthn Attribute", attrPane);
		setMainSections(commonSection, webAuthnAttributeSection);

		// 全体配置
		addMember(headerPane);
		addMember(mainStack);

		// 表示データの取得
		initializeData();

	}

	private void initializeData() {
		// エラーのクリア
		commonSection.clearErrors();

		service.getDefinitionEntry(TenantInfoHolder.getId(), WebAuthnDefinition.class.getName(), defName, new AsyncCallback<DefinitionEntry>() {

			@Override
			public void onSuccess(DefinitionEntry result) {
				// 画面に反映
				setDefinition(result);
			}

			@Override
			public void onFailure(Throwable caught) {
				SC.say(AdminClientMessageUtil.getString("ui_metadata_webauthn_WebAuthnEditPane_failed"),
						AdminClientMessageUtil.getString("ui_metadata_webauthn_WebAuthnEditPane_failedGetScreenInfo") + caught.getMessage());

				GWT.log(caught.toString(), caught);
			}
		});

		// ステータスチェック
		StatusCheckUtil.statuCheck(WebAuthnDefinition.class.getName(), defName, this);
	}

	/**
	 * Definition画面設定処理
	 *
	 * @param definition 編集対象
	 */
	private void setDefinition(DefinitionEntry entry) {
		this.curDefinition = (WebAuthnDefinition) entry.getDefinition();
		this.curVersion = entry.getDefinitionInfo().getVersion();
		this.curDefinitionId = entry.getDefinitionInfo().getObjDefId();

		commonSection.setDefinition(curDefinition);
		commonSection.setLocalizedDisplayNameList(curDefinition.getLocalizedDisplayNameList());
		attrPane.setDefinition(curDefinition);
	}

	/**
	 * 保存ボタン処理
	 */
	private void saveDefinition() {

		boolean commonValidate = commonSection.validate();
		if (!commonValidate) {
			return;
		}

		if (!attrPane.validate()) {
			return;
		}

		SC.ask(AdminClientMessageUtil.getString("ui_metadata_webauthn_WebAuthnConnectEditPane_saveConfirm"),
				AdminClientMessageUtil.getString("ui_metadata_webauthn_WebAuthnConnectEditPane_saveWebAuthnComment"), new BooleanCallback() {

			@Override
			public void execute(Boolean value) {
				if (value) {
							final WebAuthnDefinition definition = curDefinition;
					commonSection.getEditDefinition(definition);
					definition.setLocalizedDisplayNameList(commonSection.getLocalizedDisplayNameList());
					attrPane.getEditDefinition(definition);

					updateDefinition(definition, true);
				}
			}
		});
	}

	/**
	 * キャンセルボタン処理
	 */
	private void cancelDefinition() {

		SC.ask(AdminClientMessageUtil.getString("ui_metadata_webauthn_WebAuthnConnectEditPane_cancelConfirm"),
				AdminClientMessageUtil.getString("ui_metadata_webauthn_WebAuthnConnectEditPane_cancelConfirmComment")
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
	private void updateDefinition(final WebAuthnDefinition definition, boolean checkVersion) {
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
	private void updateDefinitionComplete(WebAuthnDefinition definition) {

		SC.say(AdminClientMessageUtil.getString("ui_metadata_webauthn_WebAuthnConnectEditPane_completion"),
				AdminClientMessageUtil.getString("ui_metadata_webauthn_WebAuthnConnectEditPane_saveWebAuthn"));

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
}
