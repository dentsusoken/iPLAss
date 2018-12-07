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

package org.iplass.adminconsole.client.metadata.ui.action;

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
import org.iplass.mtp.web.actionmapping.definition.ActionMappingDefinition;
import org.iplass.mtp.web.staticresource.definition.StaticResourceDefinition;
import org.iplass.mtp.web.template.definition.TemplateDefinition;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.SectionStackSection;

public class ActionEditPane extends MetaDataMainEditPane {

	/** メタデータサービス */
	private final MetaDataServiceAsync service = MetaDataServiceFactory.get();;

	/** 編集対象 */
	private ActionMappingDefinition curDefinition;

	/** 編集対象バージョン */
	private int curVersion;

	/** 編集対象ID */
	private String curDefinitionId;

	/** ヘッダ部分 */
	private MetaCommonHeaderPane headerPane;
	/** 共通属性部分 */
	private MetaCommonAttributeSection commonSection;

	/** Action属性部分 */
	private ActionAttributePane actionAttributePane;

	/** CommandConfig部分 */
	private CommandConfigGridPane commandConfigPane;

	/** ParamMap部分 */
	private ParamMapGridPane paramMapPane;

	/** Result部分 */
	private ResultGridPane resultPane;

	/** Result部分 */
	private CacheCriteriaPane cacheCriteriaPane;

	public ActionEditPane(MetaDataItemMenuTreeNode targetNode, DefaultMetaDataPlugin plugin) {
		super(targetNode, plugin);

		//レイアウト設定
		setMembersMargin(5);
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
		commonSection = new MetaCommonAttributeSection(targetNode, ActionMappingDefinition.class, true);

		//Action編集部分
		actionAttributePane = new ActionAttributePane();

		//CommandConfig編集部分
		commandConfigPane = new CommandConfigGridPane();

		//ParamMap部分
		paramMapPane = new ParamMapGridPane();

		//Result部分
		resultPane = new ResultGridPane();

		//Section設定
		MetaDataSectionStackSection actionSection = createSection("Action Attribute", actionAttributePane, paramMapPane, commandConfigPane, resultPane);

		//Drop設定
		new AbstractMetaDataDropHandler() {

			@Override
			protected void onMetaDataDrop(MetaDataItemMenuTreeNode itemNode) {
				if (itemNode.getDefinitionClassName().equals(CommandDefinition.class.getName())) {
					commandConfigPane.addCommand(itemNode);
				} else if (itemNode.getDefinitionClassName().equals(TemplateDefinition.class.getName())
						|| itemNode.getDefinitionClassName().equals(StaticResourceDefinition.class.getName())) {
					resultPane.addResult(itemNode);
				}
			}

			@Override
			protected boolean canAcceptDrop(MetaDataItemMenuTreeNode itemNode) {
				if (itemNode.getDefinitionClassName().equals(CommandDefinition.class.getName())
						|| itemNode.getDefinitionClassName().equals(TemplateDefinition.class.getName())
						|| itemNode.getDefinitionClassName().equals(StaticResourceDefinition.class.getName())) {
					return true;
				}
				return false;
			}
		}.setTarget(actionSection.getLayout());


		//CacheCriteria部分
		cacheCriteriaPane = new CacheCriteriaPane(this);

		//Section設定
		SectionStackSection serverCacheSection = createSection("Server Cache Criteria", false, cacheCriteriaPane);

		setMainSections(commonSection, actionSection, serverCacheSection);

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

		service.getDefinitionEntry(TenantInfoHolder.getId(), ActionMappingDefinition.class.getName(), defName, new AsyncCallback<DefinitionEntry>() {

			@Override
			public void onFailure(Throwable caught) {
				SC.say(AdminClientMessageUtil.getString("ui_metadata_action_ActionEditPane_failed"),
						AdminClientMessageUtil.getString("ui_metadata_action_ActionEditPane_failedGetScreenInfo"));

				GWT.log(caught.toString(), caught);
			}

			@Override
			public void onSuccess(DefinitionEntry result) {
				//画面に反映
				setDefinition(result);
			}

		});
		StatusCheckUtil.statuCheck(ActionMappingDefinition.class.getName(), defName, this);
	}

	/**
	 * Definition画面設定処理
	 *
	 * @param definition 編集対象
	 */
	private void setDefinition(DefinitionEntry entry) {
		this.curDefinition = (ActionMappingDefinition) entry.getDefinition();
		this.curVersion = entry.getDefinitionInfo().getVersion();
		this.curDefinitionId = entry.getDefinitionInfo().getObjDefId();

		//共通属性
		commonSection.setName(curDefinition.getName());
		commonSection.setDisplayName(curDefinition.getDisplayName());
		commonSection.setLocalizedDisplayNameList(curDefinition.getLocalizedDisplayNameList());
		commonSection.setDescription(curDefinition.getDescription());

		actionAttributePane.setDefinition(curDefinition);
		commandConfigPane.setConfig(curDefinition.getCommandConfig());
		paramMapPane.setParamMap(curDefinition.getParamMap());
		resultPane.setResults(curDefinition.getResult());
		cacheCriteriaPane.setCacheCriteria(curDefinition.getCacheCriteria());
	}

	public String getActionName() {
		return this.defName;
	}

	/**
	 * 更新処理
	 *
	 * @param definition 更新対象
	 */
	private void updateAction(final ActionMappingDefinition definition, boolean checkVersion) {
		SmartGWTUtil.showSaveProgress();
		service.updateDefinition(TenantInfoHolder.getId(), definition, curVersion, checkVersion, new MetaDataUpdateCallback() {

			@Override
			protected void overwriteUpdate() {
				updateAction(definition, false);
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
	private void updateComplete(ActionMappingDefinition definition) {

		SC.say(AdminClientMessageUtil.getString("ui_metadata_action_ActionEditPane_completion"),
				AdminClientMessageUtil.getString("ui_metadata_action_ActionEditPane_saveActionInfo"));

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
			boolean attrValidate = actionAttributePane.validate();
			boolean commandValidate = commandConfigPane.validate();
			boolean paramMapValidate = paramMapPane.validate();
			boolean resultValidate = resultPane.validate();
			boolean cacheCriteriaValidate = cacheCriteriaPane.validate();
			if (!commonValidate || !attrValidate || !commandValidate || !paramMapValidate || !resultValidate || !cacheCriteriaValidate) {
				return;
			}

			SC.ask(AdminClientMessageUtil.getString("ui_metadata_action_ActionEditPane_saveConfirm"),
					AdminClientMessageUtil.getString("ui_metadata_action_ActionEditPane_saveConfirmComment"), new BooleanCallback() {

				@Override
				public void execute(Boolean value) {
					if (value) {
						ActionMappingDefinition definition = new ActionMappingDefinition();

						definition.setName(commonSection.getName());
						definition.setDisplayName(commonSection.getDisplayName());
						definition.setLocalizedDisplayNameList(commonSection.getLocalizedDisplayNameList());
						definition.setDescription(commonSection.getDescription());

						definition = actionAttributePane.getEditDefinition(definition);
						definition.setCommandConfig(commandConfigPane.getEditCommandConfig());
						definition = paramMapPane.getEditDefinition(definition);
						definition = resultPane.getEditDefinition(definition);
						definition = cacheCriteriaPane.getEditDefinition(definition);

						updateAction(definition, true);
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

			SC.ask(AdminClientMessageUtil.getString("ui_metadata_action_ActionEditPane_cancelConfirm"),
					AdminClientMessageUtil.getString("ui_metadata_action_ActionEditPane_cancelConfirmComment")
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
