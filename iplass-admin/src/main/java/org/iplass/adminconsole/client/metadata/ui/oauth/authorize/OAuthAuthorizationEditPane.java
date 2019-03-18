/*
 * Copyright (C) 2019 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.oauth.authorize;

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
import org.iplass.mtp.auth.oauth.definition.OAuthAuthorizationDefinition;
import org.iplass.mtp.definition.DefinitionEntry;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.SectionStackSection;

/**
 * OAuthAuthorizationDefinition編集パネル
 */
public class OAuthAuthorizationEditPane extends MetaDataMainEditPane {

	/** メタデータサービス */
	private final MetaDataServiceAsync service;

	/** 編集対象 */
	private OAuthAuthorizationDefinition curDefinition;

	/** 編集対象バージョン */
	private int curVersion;

	/** 編集対象ID */
	private String curDefinitionId;

	/** ヘッダ部分 */
	private MetaCommonHeaderPane headerPane;
	/** 共通属性部分 */
	private MetaCommonAttributeSection commonSection;

	/** 個別属性部分 */
	private OAuthAuthorizationAttributePane attributePane;

	public OAuthAuthorizationEditPane(MetaDataItemMenuTreeNode targetNode, DefaultMetaDataPlugin plugin) {
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

		// 共通属性
		commonSection = new MetaCommonAttributeSection(targetNode, OAuthAuthorizationDefinition.class, false);

		// 個別属性
		attributePane = new OAuthAuthorizationAttributePane();

		// Section設定
		SectionStackSection authenticationPolicyAttributeSection = createSection("OAuthAuthorization Attribute", attributePane);
		setMainSections(commonSection, authenticationPolicyAttributeSection);

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

		service.getDefinitionEntry(TenantInfoHolder.getId(), OAuthAuthorizationDefinition.class.getName(), defName, new AsyncCallback<DefinitionEntry>() {

			@Override
			public void onSuccess(DefinitionEntry result) {
				// 画面に反映
				setDefinition(result);
			}

			@Override
			public void onFailure(Throwable caught) {
				SC.say(AdminClientMessageUtil.getString("ui_metadata_oauth_authorize_OAuthAuthorizationEditPane_failed"),
						AdminClientMessageUtil.getString("ui_metadata_oauth_authorize_OAuthAuthorizationEditPane_failedGetScreenInfo") + caught.getMessage());

				GWT.log(caught.toString(), caught);
			}
		});

		// ステータスチェック
		StatusCheckUtil.statuCheck(OAuthAuthorizationDefinition.class.getName(), defName, this);
	}

	/**
	 * Definition画面設定処理
	 *
	 * @param definition 編集対象
	 */
	private void setDefinition(DefinitionEntry entry) {
		this.curDefinition = (OAuthAuthorizationDefinition) entry.getDefinition();
		this.curVersion = entry.getDefinitionInfo().getVersion();
		this.curDefinitionId = entry.getDefinitionInfo().getObjDefId();

		// 共通属性
		commonSection.setName(curDefinition.getName());
		commonSection.setDisplayName(curDefinition.getDisplayName());
//		commonSection.setLocalizedDisplayNameList(curDefinition.getLocalizedDisplayNameList());
		commonSection.setDescription(curDefinition.getDescription());

		// OAuthAuthorizationDefinition属性
		attributePane.setDefinition(curDefinition);
	}

	/**
	 * 保存ボタン処理
	 */
	private void saveDefinition() {

		boolean commonValidate = commonSection.validate();
		if (!commonValidate) {
			return;
		}

		if (!attributePane.validate()) {
			return;
		}

		SC.ask(AdminClientMessageUtil.getString("ui_metadata_oauth_authorize_OAuthAuthorizationEditPane_saveConfirm"),
				AdminClientMessageUtil.getString("ui_metadata_oauth_authorize_OAuthAuthorizationEditPane_saveComment"), new BooleanCallback() {

			@Override
			public void execute(Boolean value) {
				if (value) {
					final OAuthAuthorizationDefinition definition = curDefinition;
					definition.setName(commonSection.getName());
					definition.setDisplayName(commonSection.getDisplayName());
//					definition.setLocalizedDisplayNameList(commonSection.getLocalizedDisplayNameList());
					definition.setDescription(commonSection.getDescription());

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

		SC.ask(AdminClientMessageUtil.getString("ui_metadata_oauth_authorize_OAuthAuthorizationEditPane_cancelConfirm"),
				AdminClientMessageUtil.getString("ui_metadata_oauth_authorize_OAuthAuthorizationEditPane_cancelConfirmComment")
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
	private void updateDefinition(final OAuthAuthorizationDefinition definition, boolean checkVersion) {
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
	private void updateDefinitionComplete(OAuthAuthorizationDefinition definition) {

		SC.say(AdminClientMessageUtil.getString("ui_metadata_oauth_authorize_OAuthAuthorizationEditPane_completion"),
				AdminClientMessageUtil.getString("ui_metadata_oauth_authorize_OAuthAuthorizationEditPane_saveComp"));

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
