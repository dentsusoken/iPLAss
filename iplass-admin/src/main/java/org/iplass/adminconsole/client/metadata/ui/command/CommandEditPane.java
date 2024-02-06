/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.command;

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
import org.iplass.mtp.command.definition.CommandDefinition;
import org.iplass.mtp.definition.DefinitionEntry;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;

public class CommandEditPane extends MetaDataMainEditPane {

	/** メタデータサービス */
	private final MetaDataServiceAsync service;

	/** 編集対象 */
	private CommandDefinition curDefinition;

	/** 編集対象バージョン */
	private int curVersion;

	/** 編集対象ID */
	private String curDefinitionId;

	/** ヘッダ部分 */
	private MetaCommonHeaderPane headerPane;
	/** 共通属性部分 */
	private MetaCommonAttributeSection<CommandDefinition> commonSection;

	/** Command属性部分 */
	private CommandAttributePane commandAttributePane;

	/** 個別属性部分 */
	private VLayout commandTypeMainPane;
	private CommandTypeEditPane typeEditPane;


	public CommandEditPane(MetaDataItemMenuTreeNode targetNode, DefaultMetaDataPlugin plugin) {
		super(targetNode, plugin);

		service = MetaDataServiceFactory.get();

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
		commonSection = new MetaCommonAttributeSection<>(targetNode, CommandDefinition.class, true);

		//属性編集部分
		commandAttributePane = new CommandAttributePane();
		commandAttributePane.setTypeChangedHandler(new ChangedHandler() {

			@Override
			public void onChanged(ChangedEvent event) {
				typeChanged(commandAttributePane.selectedType());
			}
		});

		commandTypeMainPane = new VLayout();

		//Section設定
		SectionStackSection commandSection = createSection("Command Attribute", commandAttributePane, commandTypeMainPane);
		setMainSections(commonSection, commandSection);

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

		service.getDefinitionEntry(TenantInfoHolder.getId(), CommandDefinition.class.getName(), defName, new AsyncCallback<DefinitionEntry>() {

			@Override
			public void onFailure(Throwable caught) {
				SC.say(AdminClientMessageUtil.getString("ui_metadata_command_CommandEditPane_failed"),
						AdminClientMessageUtil.getString("ui_metadata_command_CommandEditPane_failedGetScreenInfo") + caught.getMessage());

				GWT.log(caught.toString(), caught);
			}

			@Override
			public void onSuccess(DefinitionEntry result) {
				//画面に反映
				setDefinition(result, true);
			}

		});

		//ステータスチェック
		StatusCheckUtil.statuCheck(CommandDefinition.class.getName(), defName, this);
	}

	/**
	 * Definition画面設定処理
	 *
	 * @param definition 編集対象
	 */
	private void setDefinition(DefinitionEntry entry, boolean useHistoryInfo) {
		this.curDefinition = (CommandDefinition) entry.getDefinition();

		if (useHistoryInfo) {
			this.curVersion = entry.getDefinitionInfo().getVersion();
			this.curDefinitionId = entry.getDefinitionInfo().getObjDefId();
		}

		commonSection.setDefinition(curDefinition);
		commonSection.setLocalizedDisplayNameList(curDefinition.getLocalizedDisplayNameList());
		commandAttributePane.setDefinition(curDefinition);

		if (typeEditPane != null) {
			if (commandTypeMainPane.contains(typeEditPane)) {
				commandTypeMainPane.removeMember(typeEditPane);
			}
			typeEditPane = null;
		}

		CommandType type = CommandType.valueOf(curDefinition);
		typeEditPane = CommandType.typeOfEditPane(type);
		typeEditPane.setDefinition(curDefinition);
		commandTypeMainPane.addMember(typeEditPane);
	}

	/**
	 * タイプ変更処理
	 *
	 * @param type 選択タイプ
	 */
	private void typeChanged(CommandType type) {
		//タイプにあったDefinitionを取得
		CommandDefinition newDefinition = CommandType.typeOfDefinition(type);

		//共通属性をコピー
		newDefinition.setName(curDefinition.getName());
		newDefinition.setDisplayName(curDefinition.getDisplayName());
		newDefinition.setDescription(curDefinition.getDescription());

		//アトリビュート属性をコピー
		commandAttributePane.getEditDefinition(newDefinition);

		DefinitionEntry newEntry = new DefinitionEntry();
		newEntry.setDefinition(newDefinition);

		setDefinition(newEntry, false);
	}

	/**
	 * 更新処理
	 *
	 * @param definition 更新対象
	 */
	private void updateCommand(final CommandDefinition definition, boolean checkVersion) {
		SmartGWTUtil.showSaveProgress();
		service.updateDefinition(TenantInfoHolder.getId(), definition, curVersion, checkVersion, new MetaDataUpdateCallback() {

			@Override
			protected void overwriteUpdate() {
				updateCommand(definition, false);
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
	private void updateComplete(CommandDefinition definition) {

		SC.say(AdminClientMessageUtil.getString("ui_metadata_command_CommandEditPane_completion"),
				AdminClientMessageUtil.getString("ui_metadata_command_CommandEditPane_saveCommand"));

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
			boolean attrValidate = commandAttributePane.validate();
			boolean typeValidate = typeEditPane.validate();
			if (!commonValidate || !attrValidate || !typeValidate) {
				return;
			}

			SC.ask(AdminClientMessageUtil.getString("ui_metadata_command_CommandEditPane_saveConfirm"),
					AdminClientMessageUtil.getString("ui_metadata_command_CommandEditPane_saveConfirmComment"), new BooleanCallback() {

				@Override
				public void execute(Boolean value) {
					if (value) {
						CommandDefinition definition = CommandType.typeOfDefinition(commandAttributePane.selectedType());

						definition = commonSection.getEditDefinition(definition);
						definition.setLocalizedDisplayNameList(commonSection.getLocalizedDisplayNameList());
						definition = commandAttributePane.getEditDefinition(definition);
						definition = typeEditPane.getEditDefinition(definition);

						updateCommand(definition, true);
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

			SC.ask(AdminClientMessageUtil.getString("ui_metadata_command_CommandEditPane_cancelConfirm"),
					AdminClientMessageUtil.getString("ui_metadata_command_CommandEditPane_cancelConfirmComment")
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
