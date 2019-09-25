/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.webapi;

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.DefaultMetaDataPlugin;
import org.iplass.adminconsole.client.metadata.ui.MetaDataItemMenuTreeNode;
import org.iplass.adminconsole.client.metadata.ui.MetaDataMainEditPane;
import org.iplass.adminconsole.client.metadata.ui.command.config.CommandConfigGridPane;
import org.iplass.adminconsole.client.metadata.ui.common.AbstractMetaDataDropHandler;
import org.iplass.adminconsole.client.metadata.ui.common.MetaCommonAttributeSection;
import org.iplass.adminconsole.client.metadata.ui.common.MetaCommonHeaderPane;
import org.iplass.adminconsole.client.metadata.ui.common.MetaDataHistoryDialog;
import org.iplass.adminconsole.client.metadata.ui.common.MetaDataUpdateCallback;
import org.iplass.adminconsole.client.metadata.ui.common.StatusCheckUtil;
import org.iplass.adminconsole.shared.metadata.dto.AdminDefinitionModifyResult;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;
import org.iplass.mtp.command.definition.CommandDefinition;
import org.iplass.mtp.definition.DefinitionEntry;
import org.iplass.mtp.webapi.definition.RequestType;
import org.iplass.mtp.webapi.definition.WebApiDefinition;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class WebApiEditPane extends MetaDataMainEditPane {

	/** メタデータサービス */
	private final MetaDataServiceAsync service = MetaDataServiceFactory.get();

	/** 編集対象 */
	private WebApiDefinition curDefinition;

	/** 編集対象バージョン */
	private int curVersion;

	/** 編集対象ID */
	private String curDefinitionId;

	/** ヘッダ部分 */
	private MetaCommonHeaderPane headerPane;
	/** 共通属性部分 */
	private MetaCommonAttributeSection<WebApiDefinition> commonSection;

	/** 共通属性部分 */
	private WebApiAttributePane attributePane;

	/** RestrictionRequest属性部分 */
	private RestrictionRequestAttributePane restrictionRequestAttributePane;

	/** CORS属性部分 */
	private CorsAttributePane corsAttributePane;

	/** OAuth属性部分 */
	private OAuthAttributePane oauthAttributePane;

	/** 受付種別部分 */
	private RequestTypePane requestTypeGridPane;

	private VLayout requestTypePane;

	/** CommandConfig部分 */
	private CommandConfigGridPane commandConfigPane;

	/** Result部分 */
	private ResultPane resultPane;

	RestJsonParamPane jsonParamPane;
	RestXmlParamPane xmlParamPane;

	ResponseTypePane responseTypePane;

	public WebApiEditPane(MetaDataItemMenuTreeNode targetNode, DefaultMetaDataPlugin plugin) {
		super(targetNode, plugin);

		//レイアウト設定
		setWidth100();

		//ヘッダ（ボタン）部分
		headerPane = new MetaCommonHeaderPane(targetNode);
		headerPane.setSaveClickHandler(new SaveClickHandler());
		headerPane.setCancelClickHandler(new CancelClickHandler());

		headerPane.setHistoryClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				MetaDataHistoryDialog metaDataHistoryDialog = new MetaDataHistoryDialog(curDefinition.getClass().getName(), curDefinitionId, curVersion);
				metaDataHistoryDialog.show();
			}
		});

		//共通属性
		commonSection = new MetaCommonAttributeSection<>(targetNode, WebApiDefinition.class);

		//属性編集部分
		attributePane = new WebApiAttributePane();

		//RestrictionRequest属性部分
		restrictionRequestAttributePane = new RestrictionRequestAttributePane();

		//CORS編集部分
		corsAttributePane = new CorsAttributePane();

		//OAuth編集部分
		oauthAttributePane = new OAuthAttributePane();

		// 受付種別選択部分
		requestTypeGridPane = new RequestTypePane();
		requestTypeGridPane.setTypeChangedHandler(new ChangedHandler() {

			@Override
			public void onChanged(ChangedEvent event) {
				typeChanged(requestTypeGridPane.selectedType());
			}
		});

		// 受付種別別変動部分
		requestTypePane = new VLayout();
		requestTypePane.setAutoHeight();

		responseTypePane = new ResponseTypePane();

		//CommandConfig編集部分
		commandConfigPane = new CommandConfigGridPane();

		//Result部分
		resultPane = new ResultPane();

		//Section設定
		MetaDataSectionStackSection webApiSection = createSection("WebApi Attribute", attributePane,
				restrictionRequestAttributePane, corsAttributePane, oauthAttributePane,
				requestTypeGridPane, requestTypePane, responseTypePane,
				commandConfigPane, resultPane);

		//Drop設定
		new AbstractMetaDataDropHandler() {

			@Override
			protected void onMetaDataDrop(MetaDataItemMenuTreeNode itemNode) {
				if (itemNode.getDefinitionClassName().equals(CommandDefinition.class.getName())) {
					commandConfigPane.addCommand(itemNode);
				}
			}

			@Override
			protected boolean canAcceptDrop(MetaDataItemMenuTreeNode itemNode) {
				if (itemNode.getDefinitionClassName().equals(CommandDefinition.class.getName())) {
					return true;
				}
				return false;
			}
		}.setTarget(webApiSection.getLayout());

		setMainSections(commonSection, webApiSection);

		//全体配置
		addMember(headerPane);
		addMember(mainStack);

		//表示データの取得
		initializeData();
	}

	/**
	 * データ初期化処理
	 */
	private void initializeData() {
		//エラーのクリア
		commonSection.clearErrors();

		service.getDefinitionEntry(TenantInfoHolder.getId(), WebApiDefinition.class.getName(), defName, new AsyncCallback<DefinitionEntry>() {

			@Override
			public void onFailure(Throwable caught) {
				SC.say(AdminClientMessageUtil.getString("ui_metadata_webapi_WebAPIEditPane_failed"),
						AdminClientMessageUtil.getString("ui_metadata_webapi_WebAPIEditPane_failedGetScreenInfo") + caught.getMessage());

				GWT.log(caught.toString(), caught);
			}

			@Override
			public void onSuccess(DefinitionEntry result) {
				//画面に反映
				setDefinition(result);
			}

		});

		//ステータスチェック
		StatusCheckUtil.statuCheck(WebApiDefinition.class.getName(), defName, this);
	}

	/**
	 * タイプ変更処理
	 *
	 * @param type 選択タイプ
	 */
	private void typeChanged(List<RequestType> list) {

		List<HLayout> paramPaneList = new ArrayList<>();

		if (list.contains(RequestType.REST_JSON)) {
			if (jsonParamPane == null || requestTypePane.getMember(requestTypePane.getMemberNumber(jsonParamPane)) == null) {
				jsonParamPane = new RestJsonParamPane();
			}
			paramPaneList.add(jsonParamPane);
		} else {
			if (jsonParamPane != null && requestTypePane.getMember(requestTypePane.getMemberNumber(jsonParamPane)) != null) {
				requestTypePane.removeMember(jsonParamPane);
			}
		}

		if (list.contains(RequestType.REST_XML)) {
			if (xmlParamPane == null || requestTypePane.getMember(requestTypePane.getMemberNumber(xmlParamPane)) == null) {
				xmlParamPane = new RestXmlParamPane();
			}
			paramPaneList.add(xmlParamPane);
		} else {
			if (xmlParamPane != null && requestTypePane.getMember(requestTypePane.getMemberNumber(xmlParamPane)) != null) {
				requestTypePane.removeMember(xmlParamPane);
			}
		}

		for (HLayout layout : paramPaneList) {
			requestTypePane.addMember(layout);
		}

	}

	/**
	 * Definition画面設定処理
	 *
	 * @param definition 編集対象
	 */
	private void setDefinition(DefinitionEntry entry) {
		this.curDefinition = (WebApiDefinition) entry.getDefinition();
		this.curVersion = entry.getDefinitionInfo().getVersion();
		this.curDefinitionId = entry.getDefinitionInfo().getObjDefId();

		commonSection.setDefinition(curDefinition);
		attributePane.setDefinition(curDefinition);
		restrictionRequestAttributePane.setDefinition(curDefinition);
		corsAttributePane.setDefinition(curDefinition);
		oauthAttributePane.setDefinition(curDefinition);
		requestTypeGridPane.setDefinition(curDefinition);

		typeChanged(requestTypeGridPane.selectedType());

		responseTypePane.setDefinition(curDefinition);

		if (jsonParamPane != null) {
			jsonParamPane.setDefinition(curDefinition);
		}

		if (xmlParamPane != null) {
			xmlParamPane.setDefinition(curDefinition);
		}

		commandConfigPane.setConfig(curDefinition.getCommandConfig());
		resultPane.setResults(curDefinition.getResults());
	}

	/**
	 * 更新処理
	 *
	 * @param definition 更新対象
	 */
	private void updateWebAPI(final WebApiDefinition definition, boolean checkVersion) {
		SmartGWTUtil.showSaveProgress();
		service.updateDefinition(TenantInfoHolder.getId(), definition, curVersion, checkVersion, new MetaDataUpdateCallback() {

			@Override
			protected void overwriteUpdate() {
				updateWebAPI(definition, false);
			}

			@Override
			protected void afterUpdate(AdminDefinitionModifyResult result) {
				updateComplete(definition);

			}
		});
	}

	/**
	 * 更新完了処理
	 *
	 * @param definition 更新対象
	 */
	private void updateComplete(WebApiDefinition definition) {

		SC.say(AdminClientMessageUtil.getString("ui_metadata_webapi_WebAPIEditPane_completion"),
				AdminClientMessageUtil.getString("ui_metadata_webapi_WebAPIEditPane_saveWebApiInfoComp"));

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

	/**
	 * 保存ボタンイベント
	 */
	private final class SaveClickHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			boolean commonValidate = commonSection.validate();
			boolean attrValidate = attributePane.validate();
			boolean restrictionValidate = restrictionRequestAttributePane.validate();
			boolean corsValidate = corsAttributePane.validate();
			boolean oauthValidate = oauthAttributePane.validate();
			boolean commandValidate = commandConfigPane.validate();
			if (!commonValidate || !attrValidate || !restrictionValidate || !corsValidate || !oauthValidate || !commandValidate) {
				return;
			}
			SC.ask(AdminClientMessageUtil.getString("ui_metadata_webapi_WebAPIEditPane_saveConfirm"),
					AdminClientMessageUtil.getString("ui_metadata_webapi_WebAPIEditPane_saveConfirmComment"), new BooleanCallback() {

				@Override
				public void execute(Boolean value) {
					if (value) {
						WebApiDefinition definition = new WebApiDefinition();

						definition = commonSection.getEditDefinition(definition);
						definition = attributePane.getEditDefinition(definition);
						definition = restrictionRequestAttributePane.getEditDefinition(definition);
						definition = corsAttributePane.getEditDefinition(definition);
						definition = oauthAttributePane.getEditDefinition(definition);
						definition.setCommandConfig(commandConfigPane.getEditCommandConfig());
						definition = requestTypeGridPane.getEditDefinition(definition);
						definition = resultPane.getEditDefinition(definition);
						if (jsonParamPane != null) {
							definition = jsonParamPane.getEditDefinition(definition);
						}
						if (xmlParamPane != null) {
							definition = xmlParamPane.getEditDefinition(definition);
						}
						definition = responseTypePane.getEditDefinition(definition);
						updateWebAPI(definition, true);
					}
				}
			});
		}
	}

	/**
	 * キャンセルボタンイベント
	 */
	private final class CancelClickHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {

			SC.ask(AdminClientMessageUtil.getString("ui_metadata_webapi_WebAPIEditPane_cancelConfirm"),
					AdminClientMessageUtil.getString("ui_metadata_webapi_WebAPIEditPane_cancelConfirmComment")
					, new BooleanCallback() {
				@Override
				public void execute(Boolean value) {
					if (value) {
						initializeData();
						commonSection.refreshSharedConfig();
					}
				}
			});
		}
	}
}
