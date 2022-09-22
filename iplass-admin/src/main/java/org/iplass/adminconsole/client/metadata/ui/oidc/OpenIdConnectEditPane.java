/*
 * Copyright 2022 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
 */

package org.iplass.adminconsole.client.metadata.ui.oidc;

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
import org.iplass.mtp.auth.oidc.definition.OpenIdConnectDefinition;
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

public class OpenIdConnectEditPane extends MetaDataMainEditPane {

	/** メタデータサービス */
	private final MetaDataServiceAsync service;

	/** 編集対象 */
	private OpenIdConnectDefinition curDefinition;

	/** 編集対象バージョン */
	private int curVersion;

	/** 編集対象ID */
	private String curDefinitionId;

	/** ヘッダ部分 */
	private MetaCommonHeaderPane headerPane;
	/** 共通属性部分 */
	private MetaCommonAttributeSection<OpenIdConnectDefinition> commonSection;

	/** 個別属性部分 */
	private OpenIdConnectAttributePane attrPane;

	public OpenIdConnectEditPane(MetaDataItemMenuTreeNode targetNode, DefaultMetaDataPlugin plugin) {
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
		
		LayoutSpacer space = new LayoutSpacer();
		space.setWidth(95);
		headerPane.addMember(space);
		IButton btnSaveClientSecret = new IButton("ClientSecret Setting");
		btnSaveClientSecret.setWidth(150);
		btnSaveClientSecret.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				ClientSecretDialog clientSecretDialog = new ClientSecretDialog(commonSection.getName());
				clientSecretDialog.show();
			}
		});

		headerPane.addMember(btnSaveClientSecret);

		// 共通属性
		commonSection = new MetaCommonAttributeSection<>(targetNode, OpenIdConnectDefinition.class, true);

		// 個別属性
		attrPane = new OpenIdConnectAttributePane();

		// Section設定
		SectionStackSection openIdConnectAttributeSection = createSection("OpenIDConnect (RP) Attribute", attrPane);
		setMainSections(commonSection, openIdConnectAttributeSection);

		// 全体配置
		addMember(headerPane);
		addMember(mainStack);

		// 表示データの取得
		initializeData();

	}

	private void initializeData() {
		// エラーのクリア
		commonSection.clearErrors();

		service.getDefinitionEntry(TenantInfoHolder.getId(), OpenIdConnectDefinition.class.getName(), defName, new AsyncCallback<DefinitionEntry>() {

			@Override
			public void onSuccess(DefinitionEntry result) {
				// 画面に反映
				setDefinition(result);
			}

			@Override
			public void onFailure(Throwable caught) {
				SC.say(AdminClientMessageUtil.getString("ui_metadata_oidc_OpenIdConnectEditPane_failed"),
						AdminClientMessageUtil.getString("ui_metadata_oidc_OpenIdConnectEditPane_failedGetScreenInfo") + caught.getMessage());

				GWT.log(caught.toString(), caught);
			}
		});

		// ステータスチェック
		StatusCheckUtil.statuCheck(OpenIdConnectDefinition.class.getName(), defName, this);
	}

	/**
	 * Definition画面設定処理
	 *
	 * @param definition 編集対象
	 */
	private void setDefinition(DefinitionEntry entry) {
		this.curDefinition = (OpenIdConnectDefinition) entry.getDefinition();
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

		SC.ask(AdminClientMessageUtil.getString("ui_metadata_oidc_OpenIdConnectEditPane_saveConfirm"),
				AdminClientMessageUtil.getString("ui_metadata_oidc_OpenIdConnectEditPane_saveOpenIdConnectComment"), new BooleanCallback() {

			@Override
			public void execute(Boolean value) {
				if (value) {
					final OpenIdConnectDefinition definition = curDefinition;
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

		SC.ask(AdminClientMessageUtil.getString("ui_metadata_oidc_OpenIdConnectEditPane_cancelConfirm"),
				AdminClientMessageUtil.getString("ui_metadata_oidc_OpenIdConnectEditPane_cancelConfirmComment")
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
	private void updateDefinition(final OpenIdConnectDefinition definition, boolean checkVersion) {
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
	private void updateDefinitionComplete(OpenIdConnectDefinition definition) {

		SC.say(AdminClientMessageUtil.getString("ui_metadata_oidc_OpenIdConnectEditPane_completion"),
				AdminClientMessageUtil.getString("ui_metadata_oidc_OpenIdConnectEditPane_saveOpenIdConnect"));

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
