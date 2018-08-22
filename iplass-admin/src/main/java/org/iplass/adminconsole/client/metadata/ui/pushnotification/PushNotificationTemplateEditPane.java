/*
 * Copyright (C) 2016 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.pushnotification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.iplass.adminconsole.client.base.event.DataChangedEvent;
import org.iplass.adminconsole.client.base.event.DataChangedHandler;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogCondition;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogHandler;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogMode;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorPane;
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
import org.iplass.adminconsole.shared.metadata.dto.MetaDataConstants;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;
import org.iplass.gwt.ace.client.EditorMode;
import org.iplass.mtp.definition.DefinitionEntry;
import org.iplass.mtp.pushnotification.template.definition.LocalizedNotificationDefinition;
import org.iplass.mtp.pushnotification.template.definition.PushNotificationTemplateDefinition;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.AutoFitWidthApproach;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.SpacerItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;

public class PushNotificationTemplateEditPane extends MetaDataMainEditPane {

	private enum FIELD_NAME {
		NAME,
		TITLE
	}

	/** メタデータサービス */
	private final MetaDataServiceAsync service;

	/** 編集対象 */
	private PushNotificationTemplateDefinition curDefinition;

	/** 編集対象バージョン */
	private int curVersion;

	/** 編集対象ID */
	private String curDefinitionId;

	/** ヘッダ部分 */
	private MetaCommonHeaderPane headerPane;

	/** 共通属性部分 */
	private MetaCommonAttributeSection commonSection;

	/** 個別属性部分（デフォルト） */
	private PushNotificationTemplateAttributePane pushNotificationTemplateAttrPane;

	/** 個別属性部分（多言語） */
	private MultiPushNotificationTemplateAttributePane multiPushNotificationTemplateAttrPane;

	private Map<String, String> enableLang;

	public PushNotificationTemplateEditPane(MetaDataItemMenuTreeNode targetNode, DefaultMetaDataPlugin plugin) {
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
		commonSection = new MetaCommonAttributeSection(targetNode, PushNotificationTemplateDefinition.class);

		//個別属性
		pushNotificationTemplateAttrPane = new PushNotificationTemplateAttributePane();
		multiPushNotificationTemplateAttrPane = new MultiPushNotificationTemplateAttributePane();

		//Section設定
		SectionStackSection defaultPushNotificationSection = createSection("Default PushNotificationTemplate", pushNotificationTemplateAttrPane);
		SectionStackSection multiPushNotificationSection = createSection("Multilingual PushNotificationTemplate", false, multiPushNotificationTemplateAttrPane);
		setMainSections(commonSection, defaultPushNotificationSection, multiPushNotificationSection);

		//全体配置
		addMember(headerPane);
		addMember(mainStack);

		service.getEnableLanguages(TenantInfoHolder.getId(), new AsyncCallback<Map<String, String>>() {
			@Override
			public void onFailure(Throwable caught) {
				SC.say(AdminClientMessageUtil.getString("ui_metadata_pushnotification_PushNotificationTemplateEditPane_failed"),
						AdminClientMessageUtil.getString("ui_metadata_pushnotification_PushNotificationTemplateEditPane_failedGetScreenInfo"));
				GWT.log(caught.toString(), caught);
			}

			@Override
			public void onSuccess(Map<String, String> result) {
				enableLang = result;

				//表示データの取得
				initializeData();
			}
		});
	}

	/**
	 * データ初期化処理
	 */
	private void initializeData() {
		//エラーのクリア
		commonSection.clearErrors();
		pushNotificationTemplateAttrPane.clearErrors();
		multiPushNotificationTemplateAttrPane.clearErrors();

		service.getDefinitionEntry(TenantInfoHolder.getId(), PushNotificationTemplateDefinition.class.getName(), defName, new AsyncCallback<DefinitionEntry>() {
			@Override
			public void onFailure(Throwable caught) {
				SC.say(AdminClientMessageUtil.getString("ui_metadata_pushnotification_PushNotificationTemplateEditPane_failed"),
						AdminClientMessageUtil.getString("ui_metadata_pushnotification_PushNotificationTemplateEditPane_failedGetScreenInfo"));
				GWT.log(caught.toString(), caught);
			}

			@Override
			public void onSuccess(DefinitionEntry result) {
				//画面に反映
				setDefinition(result);
			}
		});

		//ステータスチェック
		StatusCheckUtil.statuCheck(PushNotificationTemplateDefinition.class.getName(), defName, this);
	}

	/**
	 * Definition画面設定処理
	 *
	 * @param definition 編集対象
	 */
	private void setDefinition(DefinitionEntry entry) {
		this.curDefinition = (PushNotificationTemplateDefinition) entry.getDefinition();
		this.curVersion = entry.getDefinitionInfo().getVersion();
		this.curDefinitionId = entry.getDefinitionInfo().getObjDefId();

		//共通属性
		commonSection.setName(curDefinition.getName());
		commonSection.setDisplayName(curDefinition.getDisplayName());
		commonSection.setDescription(curDefinition.getDescription());

		pushNotificationTemplateAttrPane.setDefinition(curDefinition);
		multiPushNotificationTemplateAttrPane.setDefinition(curDefinition);
	}

	/**
	 * 更新処理
	 *
	 * @param definition 更新対象
	 */
	private void updatePushNotificationTemplate(final PushNotificationTemplateDefinition definition, boolean checkVersion) {
		SmartGWTUtil.showSaveProgress();
		service.updateDefinition(TenantInfoHolder.getId(), definition, curVersion, checkVersion, new MetaDataUpdateCallback() {

			@Override
			protected void overwriteUpdate() {
				updatePushNotificationTemplate(definition, false);
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
	private void updateComplete(PushNotificationTemplateDefinition definition) {
		SC.say(AdminClientMessageUtil.getString("ui_metadata_pushnotification_PushNotificationTemplateEditPane_completion"),
				AdminClientMessageUtil.getString("ui_metadata_pushnotification_PushNotificationTemplateEditPane_savePushNotificationTemplate"));

		//再表示
		initializeData();
		commonSection.refreshSharedConfig();

		//ツリー再表示
		plugin.refreshWithSelect(definition.getName(), new AsyncCallback<MetaDataItemMenuTreeNode>() {
			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(MetaDataItemMenuTreeNode result) {
				headerPane.setTargetNode(result);
				commonSection.setTargetNode(result);
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
			boolean pushNotificationValidate = pushNotificationTemplateAttrPane.validate();
			boolean multiValidate = multiPushNotificationTemplateAttrPane.validate();

			if (!commonValidate || !pushNotificationValidate || !multiValidate) {
				return;
			}

			SC.ask(AdminClientMessageUtil.getString("ui_metadata_pushnotification_PushNotificationTemplateEditPane_saveConfirm"),
					AdminClientMessageUtil.getString("ui_metadata_pushnotification_PushNotificationTemplateEditPane_savePushNotificationTemplateComment"), new BooleanCallback() {

				@Override
				public void execute(Boolean value) {
					if (value) {
						PushNotificationTemplateDefinition definition = curDefinition;
						definition.setName(commonSection.getName());
						definition.setDisplayName(commonSection.getDisplayName());
						definition.setDescription(commonSection.getDescription());
						definition = pushNotificationTemplateAttrPane.getEditDefinition(definition);
						definition = multiPushNotificationTemplateAttrPane.getEditDefinition(definition);

						updatePushNotificationTemplate(definition, true);
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
			SC.ask(AdminClientMessageUtil.getString("ui_metadata_pushnotification_PushNotificationTemplateEditPane_cancelConfirm"),
					AdminClientMessageUtil.getString("ui_metadata_pushnotification_PushNotificationTemplateEditPane_cancelConfirmComment"), new BooleanCallback() {
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

	private class PushNotificationTemplateAttributePane extends VLayout {
		private DynamicForm form;
		private DynamicForm scriptForm;

		private TextItem icon;
		private TextItem title;

		private TabSet tabSet;
		private Tab bodyTab;

		private TextAreaItem configScript;
		private ScriptEditorPane bodyEditor;

		public PushNotificationTemplateAttributePane() {
			setOverflow(Overflow.AUTO);	//Stack上の表示領域が小さい場合にスクロールができるようにAUTO設定

			VLayout mainPane = new VLayout();
			mainPane.setMargin(5);

			title = new TextItem("title", AdminClientMessageUtil.getString("ui_metadata_pushnotification_PushNotificationTemplateEditPane_title"));
			title.setWidth("100%");
			title.setColSpan(2);

			icon = new TextItem("icon", AdminClientMessageUtil.getString("ui_metadata_pushnotification_PushNotificationTemplateEditPane_icon"));
			icon.setWidth("100%");

			form = new DynamicForm();
			form.setWidth100();
			form.setNumCols(5);	//間延びしないように最後に１つ余分に作成
			form.setColWidths(70, "*", "*", 100, "*");

			form.setItems(title, icon);

			//メッセージ編集領域
			tabSet = new TabSet();
			tabSet.setWidth100();
			tabSet.setHeight(400);	//高さは固定でないとうまくいかないため指定
			tabSet.setPaneContainerOverflow(Overflow.HIDDEN);	//Editor側のスクロールを利用するため非表示に設定（Editor側のサイズを調整）

			bodyTab = new Tab();
			bodyTab.setTitle(AdminClientMessageUtil.getString("ui_metadata_pushnotification_PushNotificationTemplateEditPane_body"));

			bodyEditor = new ScriptEditorPane();
			bodyEditor.setMode(EditorMode.HTML);
			bodyTab.setPane(bodyEditor);
			tabSet.addTab(bodyTab);

			scriptForm = new DynamicForm();
			scriptForm.setWidth100();
			scriptForm.setHeight100();
			scriptForm.setNumCols(3);	//間延びしないように最後に１つ余分に作成
			scriptForm.setColWidths(70, "*", "*");

			mainPane.addMember(form);
			mainPane.addMember(tabSet);
			mainPane.addMember(scriptForm);

			ButtonItem editScript = new ButtonItem("editScript", "Edit");
			editScript.setWidth(100);
			editScript.setStartRow(false);
			editScript.setColSpan(3);
			editScript.setAlign(Alignment.RIGHT);
			editScript.setPrompt(SmartGWTUtil.getHoverString(AdminClientMessageUtil.getString("ui_metadata_pushnotification_PushNotificationTemplateEditPane_displayDialogEditScript")));
			editScript.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
				@Override
				public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
					MetaDataUtil.showScriptEditDialog(ScriptEditorDialogMode.GROOVY_SCRIPT,
					SmartGWTUtil.getStringValue(configScript),
					ScriptEditorDialogCondition.PUSHNOTIFICATION_TEMPLATE_CONFIGSCRIPT,
					"ui_metadata_pushnotification_PushNotificationTemplateEditPane_scriptHint",
					null,
					new ScriptEditorDialogHandler() {
						@Override
						public void onSave(String text) {
							configScript.setValue(text);
						}
						@Override
						public void onCancel() {
						}
					});
				}
			});

			configScript = new TextAreaItem("configScript", "ConfigScript");
			configScript.setStartRow(true);
			configScript.setColSpan(2);
			configScript.setWidth("100%");
			configScript.setHeight("100%");

			scriptForm.setItems(new SpacerItem(), new SpacerItem(), editScript, configScript);

			SmartGWTUtil.setReadOnlyTextArea(configScript);

			addMember(mainPane);
		}

		/**
		 * PushNotificationTemplateDefinitionを展開します。
		 *
		 * @param definition PushNotificationTemplateDefinition
		 */
		public void setDefinition(PushNotificationTemplateDefinition definition) {
			String body = null;

			if (definition != null) {
				icon.setValue(definition.getIcon());
				title.setValue(definition.getTitle());
				configScript.setValue(definition.getConfigScript());

				body = definition.getBody();
				bodyEditor.setText(body);
			} else {
				bodyEditor.setText("");
			}

			tabSet.selectTab(bodyTab);
			setTabTitle(bodyTab, AdminClientMessageUtil.getString("ui_metadata_pushnotification_PushNotificationTemplateEditPane_body"), body);
		}

		/**
		 * 編集されたPushNotificationTemplateDefinition情報を返します。
		 *
		 * @return 編集PushNotificationTemplateDefinition情報
		 */
		public PushNotificationTemplateDefinition getEditDefinition(PushNotificationTemplateDefinition definition) {
			definition.setIcon(SmartGWTUtil.getStringValue(icon));
			definition.setTitle(SmartGWTUtil.getStringValue(title));
			definition.setConfigScript(SmartGWTUtil.getStringValue(configScript));

			definition.setBody(bodyEditor.getText());

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

		/**
		 * メッセージ入力の状態からタブのタイトルを設定します。
		 *
		 * @param target 対象タブ
		 * @param title  タイトル
		 * @param message メッセージ
		 */
		private void setTabTitle(Tab target, String title, String message) {
			if (message != null && !message.isEmpty()) {
				target.setTitle(title + "(*)");
			} else {
				target.setTitle(title);
			}
		}
	}

	private class MultiPushNotificationTemplateAttributePane extends VLayout {
		private DynamicForm form;
		private TextItem langOrUserBindingName;
		private LanguageMapGrid grid;

		public MultiPushNotificationTemplateAttributePane() {
			setMembersMargin(5);

			form = new DynamicForm();
			form.setWidth100();
			form.setNumCols(3);	//間延びしないように最後に１つ余分に作成
			form.setColWidths(100, "*", "*");

			langOrUserBindingName = new TextItem("bindKey", AdminClientMessageUtil.getString("ui_metadata_mail_MailTemplateEditPane_langConfigInfoBindName"));
			langOrUserBindingName.setWidth(MetaDataConstants.DEFAULT_FORM_ITEM_WIDTH);

			form.setItems(langOrUserBindingName);

			grid = new LanguageMapGrid();
			grid.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
				public void onRecordDoubleClick(RecordDoubleClickEvent event) {
					editMap((ListGridRecord)event.getRecord());
				}
			});

			IButton addMap = new IButton("Add");
			addMap.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					addMap();
				}
			});

			IButton delMap = new IButton("Remove");
			delMap.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					if (grid.getSelectedRecord() == null) {
						return;
					}

					String lang = grid.getSelectedRecord().getAttribute(FIELD_NAME.NAME.name());
					grid.removeSelectedData();

					Map<String, LocalizedNotificationDefinition> map = new HashMap<String, LocalizedNotificationDefinition>();
					for (LocalizedNotificationDefinition def : curDefinition.getLocalizedNotificationList()) {
						map.put(enableLang.get(def.getLocaleName()), def);
					}

					map.remove(lang);

					List<LocalizedNotificationDefinition> newList = new ArrayList<LocalizedNotificationDefinition>();

					for(Map.Entry<String, LocalizedNotificationDefinition> e : map.entrySet()) {
						newList.add(e.getValue());
					}

					curDefinition.setLocalizedNotificationList(newList);
				}
			});

			HLayout mapButtonPane = new HLayout(5);
			mapButtonPane.setMargin(5);
			mapButtonPane.addMember(addMap);
			mapButtonPane.addMember(delMap);

			addMember(form);
			addMember(grid);
			addMember(mapButtonPane);
		}

		private void addMap() {
			editMap(null);
		}

		public ListGridRecord createRecord(LocalizedNotificationDefinition param, ListGridRecord record, boolean init) {
			if (record == null) {
				record = new ListGridRecord();

				if (!init) {
					curDefinition.addLocalizedNotification(param);
				}
			} else {
				String lang = record.getAttribute(FIELD_NAME.NAME.name());

				// 一旦更新レコードのDefinitionを削除
				Map<String, LocalizedNotificationDefinition> map = new HashMap<String, LocalizedNotificationDefinition>();
				for (LocalizedNotificationDefinition def : curDefinition.getLocalizedNotificationList()) {
					map.put(enableLang.get(def.getLocaleName()), def);
				}

				map.remove(lang);

				// 更新した内容を追加
				map.put(lang, param);

				List<LocalizedNotificationDefinition> newList = new ArrayList<LocalizedNotificationDefinition>();

				for (Map.Entry<String, LocalizedNotificationDefinition> e : map.entrySet()) {
					newList.add(e.getValue());
				}

				curDefinition.setLocalizedNotificationList(newList);

			}
			record.setAttribute(FIELD_NAME.NAME.name(), enableLang.get(param.getLocaleName()));
			record.setAttribute(FIELD_NAME.TITLE.name(), param.getTitle());

			return record;
		}

		private void editMap(final ListGridRecord record) {
			LocalizedNotificationDefinition temp = null;

			if (record != null) {
				if (curDefinition != null && curDefinition.getLocalizedNotificationList() != null && curDefinition.getLocalizedNotificationList().size() > 0) {
					Map<String, LocalizedNotificationDefinition> map = new HashMap<String, LocalizedNotificationDefinition>();
					for (LocalizedNotificationDefinition def : curDefinition.getLocalizedNotificationList()) {
						map.put(enableLang.get(def.getLocaleName()), def);
					}

					String lang = record.getAttribute(FIELD_NAME.NAME.name());
					temp = map.get(lang);
				}
			}

			final PushNotificationTemplateSettingByLocaleDialog dialog = new PushNotificationTemplateSettingByLocaleDialog(temp);
			dialog.addDataChangeHandler(new DataChangedHandler() {
				@Override
				public void onDataChanged(DataChangedEvent event) {
					LocalizedNotificationDefinition param = event.getValueObject(LocalizedNotificationDefinition.class);
					ListGridRecord newRecord = createRecord(param, record, false);
					if (record != null) {
						grid.updateData(newRecord);
					} else {
						//追加
						grid.addData(newRecord);
					}
					grid.refreshFields();
				}
			});

			dialog.show();
		}

		/**
		 * PushNotificationTemplateDefinitionを展開します。
		 *
		 * @param definition PushNotificationTemplateDefinition
		 */
		public void setDefinition(PushNotificationTemplateDefinition definition) {
			grid.setData(new ListGridRecord[]{});

			if (definition != null) {
				langOrUserBindingName.setValue(definition.getLangOrUserBindingName());

				List<LocalizedNotificationDefinition> definitionList = definition.getLocalizedNotificationList();
				if (definitionList != null && definitionList.size() > 0) {
					ListGridRecord[] temp = new ListGridRecord[definitionList.size()];

					int cnt = 0;
					for (LocalizedNotificationDefinition localDefinition : definitionList) {
						ListGridRecord newRecord = createRecord(localDefinition, null, true);
						temp[cnt] = newRecord;
						cnt ++;

					}
					grid.setData(temp);
				}
			} else {
				langOrUserBindingName.clearValue();
			}
		}

		/**
		 * 編集されたPushNotificationTemplateDefinition情報を返します。
		 *
		 * @return 編集PushNotificationTemplateDefinition情報
		 */
		public PushNotificationTemplateDefinition getEditDefinition(PushNotificationTemplateDefinition definition) {
			definition.setLangOrUserBindingName(SmartGWTUtil.getStringValue(langOrUserBindingName));
			return definition;
		}

		/**
		 * 入力チェックを実行します。
		 *
		 * @return 入力チェック結果
		 */
		public boolean validate() {
			if (!form.validate()) {
				return false;
			}

			Set<String> check = new HashSet<String>();
			for (ListGridRecord record : grid.getRecords()) {
				String locale = record.getAttribute(FIELD_NAME.NAME.name());
				if (check.contains(locale)) {
					SC.warn(AdminClientMessageUtil.getString("ui_metadata_pushnotification_PushNotificationTemplateEditPane_duplicateLocaleNameErr"));
					return false;
				}
				check.add(locale);
			}
			return true;
		}

		/**
		 * エラー表示をクリアします。
		 */
		public void clearErrors() {
			form.clearErrors(true);
		}
	}

	private class LanguageMapGrid extends ListGrid {
		public LanguageMapGrid() {
			setWidth100();
			setHeight(1);

			setShowAllColumns(true);							//列を全て表示
			setShowAllRecords(true);							//レコードを全て表示
			setCanResizeFields(true);							//列幅変更可能
			setCanSort(false);									//ソート不可
			setCanPickFields(false);							//表示フィールドの選択不可
			setCanGroupBy(false);								//GroupByの選択不可
			setAutoFitWidthApproach(AutoFitWidthApproach.BOTH);	//AutoFit時にタイトルと値を参照
			setLeaveScrollbarGap(false);						//縦スクロールバー自動表示制御
			setBodyOverflow(Overflow.VISIBLE);
			setOverflow(Overflow.VISIBLE);

			ListGridField languageField = new ListGridField(FIELD_NAME.NAME.name(), "Language");
			ListGridField titleField = new ListGridField(FIELD_NAME.TITLE.name(), "Title");

			setFields(languageField, titleField);
		}
	}

}
