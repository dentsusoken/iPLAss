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

package org.iplass.adminconsole.client.metadata.ui.utilityclass;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogCondition;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogHandler;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogMode;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.DefaultMetaDataPlugin;
import org.iplass.adminconsole.client.metadata.ui.MetaDataItemMenuTreeNode;
import org.iplass.adminconsole.client.metadata.ui.MetaDataMainEditPane;
import org.iplass.adminconsole.client.metadata.ui.MetaDataUtil;
import org.iplass.adminconsole.client.metadata.ui.common.MetaCommonAttributeSection;
import org.iplass.adminconsole.client.metadata.ui.common.MetaCommonHeaderPane;
import org.iplass.adminconsole.client.metadata.ui.common.MetaDataHistoryDialog;
import org.iplass.adminconsole.client.metadata.ui.common.MetaDataUpdateCallback;
import org.iplass.adminconsole.client.metadata.ui.common.StatusCheckUtil;
import org.iplass.adminconsole.shared.metadata.dto.AdminDefinitionModifyResult;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;
import org.iplass.mtp.definition.DefinitionEntry;
import org.iplass.mtp.utilityclass.definition.UtilityClassDefinition;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.SpacerItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;

public class UtilityClassEditPane extends MetaDataMainEditPane {

	/** メタデータサービス */
	private final MetaDataServiceAsync service;

	/** 編集対象 */
	private UtilityClassDefinition curDefinition;

	/** 編集対象バージョン */
	private int curVersion;

	/** 編集対象ID */
	private String curDefinitionId;

	/** ヘッダ部分 */
	private MetaCommonHeaderPane headerPane;
	/** 共通属性部分 */
	private MetaCommonAttributeSection commonSection;

	/** 個別属性部分 */
	private UtilityClassAttributePane utilityClassAttrPane;

	public UtilityClassEditPane(MetaDataItemMenuTreeNode targetNode, DefaultMetaDataPlugin plugin) {
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
		commonSection = new MetaCommonAttributeSection(targetNode, UtilityClassDefinition.class);

		//個別属性
		utilityClassAttrPane = new UtilityClassAttributePane();

		//Section設定
		SectionStackSection mailSection = createSection("UtilityClass Attribute", utilityClassAttrPane);
		setMainSections(commonSection, mailSection);

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
		utilityClassAttrPane.clearErrors();

		service.getDefinitionEntry(TenantInfoHolder.getId(), UtilityClassDefinition.class.getName(), defName, new AsyncCallback<DefinitionEntry>() {

			@Override
			public void onFailure(Throwable caught) {
				SC.say(AdminClientMessageUtil.getString("ui_metadata_utilityclass_UtilityClassEditPane_failed"),
						AdminClientMessageUtil.getString("ui_metadata_utilityclass_UtilityClassEditPane_failedGetScreenInfo"));

				GWT.log(caught.toString(), caught);
			}

			@Override
			public void onSuccess(DefinitionEntry result) {
				//画面に反映
				setDefinition(result);
			}

		});

		//ステータスチェック
		StatusCheckUtil.statuCheck(UtilityClassDefinition.class.getName(), defName, this);
	}

	/**
	 * Definition画面設定処理
	 *
	 * @param definition 編集対象
	 */
	private void setDefinition(DefinitionEntry entry) {
		this.curDefinition = (UtilityClassDefinition) entry.getDefinition();
		this.curVersion = entry.getDefinitionInfo().getVersion();
		this.curDefinitionId = entry.getDefinitionInfo().getObjDefId();

		//共通属性
		commonSection.setName(curDefinition.getName());
		commonSection.setDisplayName(curDefinition.getDisplayName());
		commonSection.setDescription(curDefinition.getDescription());

		utilityClassAttrPane.setDefinition(curDefinition);
	}


	/**
	 * 更新処理
	 *
	 * @param definition 更新対象
	 */
	private void updateUtilityClass(final UtilityClassDefinition definition, boolean checkVersion) {
		SmartGWTUtil.showSaveProgress();
		service.updateDefinition(TenantInfoHolder.getId(), definition, curVersion, checkVersion, new MetaDataUpdateCallback() {

			@Override
			protected void overwriteUpdate() {
				updateUtilityClass(definition, false);
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
	private void updateComplete(UtilityClassDefinition definition) {

		SC.say(AdminClientMessageUtil.getString("ui_metadata_utilityclass_UtilityClassEditPane_completion"),
				AdminClientMessageUtil.getString("ui_metadata_utilityclass_UtilityClassEditPane_saveUtilityClassComp"));

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

	private class UtilityClassAttributePane extends VLayout {

		/** フォーム */
		private DynamicForm form;

		/** スクリプト */
		private TextAreaItem scriptField;

		public UtilityClassAttributePane() {

			setWidth100();
			setMargin(5);
			setMembersMargin(10);

			setOverflow(Overflow.AUTO);	//Stack上の表示領域が小さい場合にスクロールができるようにAUTO設定

			//入力部分
			form = new DynamicForm();
			form.setWidth100();
			form.setHeight100();
//			form.setNumCols(5);	//間延びしないように最後に１つ余分に作成
//			form.setColWidths(100, "*", 100, "*", "*");
			form.setNumCols(3);	//間延びしないように最後に１つ余分に作成
			form.setColWidths(50, "*", "*");

			ButtonItem editScript = new ButtonItem("editScript", "Edit");
			editScript.setWidth(100);
			editScript.setStartRow(false);
			editScript.setColSpan(3);
			editScript.setAlign(Alignment.RIGHT);
			editScript.setPrompt(SmartGWTUtil.getHoverString(AdminClientMessageUtil.getString("ui_metadata_utilityclass_UtilityClassEditPane_displayDialogEditScript")));
			editScript.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

				@Override
				public void onClick(
						com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
					MetaDataUtil.showScriptEditDialog(ScriptEditorDialogMode.GROOVY_SCRIPT,
							SmartGWTUtil.getStringValue(scriptField),
							ScriptEditorDialogCondition.UTILITY_SCRIPT,
							"ui_metadata_utilityclass_UtilityClassEditPane_scriptHint",
							null,
							new ScriptEditorDialogHandler() {

								@Override
								public void onSave(String text) {
									scriptField.setValue(text);
								}
								@Override
								public void onCancel() {
								}
							});
				}
			});

			scriptField = new TextAreaItem("script", "Script");
			scriptField.setStartRow(true);
			scriptField.setColSpan(2);
			scriptField.setWidth("100%");
			scriptField.setHeight("100%");
			SmartGWTUtil.setRequired(scriptField);

			form.setItems(new SpacerItem(), new SpacerItem(), editScript, scriptField);

			//配置
			addMember(form);

			SmartGWTUtil.setReadOnlyTextArea(scriptField);
		}

		/**
		 * UtilityClassDefinitionを展開します。
		 *
		 * @param definition UtilityClassDefinition
		 */
		public void setDefinition(UtilityClassDefinition definition) {
			if (definition != null) {
				if (definition.getScript() != null) {
					scriptField.setValue(definition.getScript());
				}
			} else {
				scriptField.setValue("");
			}
		}

		/**
		 * 編集されたUtilityClassDefinition情報を返します。
		 *
		 * @return 編集UtilityClassDefinition情報
		 */
		public UtilityClassDefinition getEditDefinition(UtilityClassDefinition definition) {

			definition.setScript(SmartGWTUtil.getStringValue(scriptField));
			return definition;
		}

		/**
		 * 入力チェックを実行します。
		 *
		 * @return 入力チェック結果
		 */
		public boolean validate() {
			return form.validate();
		}

		/**
		 * エラー表示をクリアします。
		 */
		public void clearErrors() {
			form.clearErrors(true);
		}
	}

	/**
	 * 保存ボタンイベント
	 */
	private final class SaveClickHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			boolean commonValidate = commonSection.validate();
			boolean mailValidate = utilityClassAttrPane.validate();
			if (!commonValidate || !mailValidate) {
				return;
			}

			SC.ask(AdminClientMessageUtil.getString("ui_metadata_utilityclass_UtilityClassEditPane_saveConfirm"),
					AdminClientMessageUtil.getString("ui_metadata_utilityclass_UtilityClassEditPane_saveConfirmComment"), new BooleanCallback() {

				@Override
				public void execute(Boolean value) {
					if (value) {
						UtilityClassDefinition definition = curDefinition;
						definition.setName(commonSection.getName());
						definition.setDisplayName(commonSection.getDisplayName());
						definition.setDescription(commonSection.getDescription());
						definition = utilityClassAttrPane.getEditDefinition(definition);

						updateUtilityClass(definition, true);
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

			SC.ask(AdminClientMessageUtil.getString("ui_metadata_utilityclass_UtilityClassEditPane_cancelConfirm"),
					AdminClientMessageUtil.getString("ui_metadata_utilityclass_UtilityClassEditPane_cancelConfirmComment")
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
