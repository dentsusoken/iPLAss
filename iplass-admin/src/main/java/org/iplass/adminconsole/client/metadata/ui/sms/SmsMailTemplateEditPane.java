/*
 * Copyright (C) 2015 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.sms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iplass.adminconsole.client.base.event.DataChangedEvent;
import org.iplass.adminconsole.client.base.event.DataChangedHandler;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorPane;
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
import org.iplass.gwt.ace.client.EditorMode;
import org.iplass.mtp.definition.DefinitionEntry;
import org.iplass.mtp.mail.template.definition.PlainTextBodyPart;
import org.iplass.mtp.sms.template.definition.LocalizedSmsMailTemplateDefinition;
import org.iplass.mtp.sms.template.definition.SmsMailTemplateDefinition;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.AutoFitWidthApproach;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
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

public class SmsMailTemplateEditPane extends MetaDataMainEditPane {

	private enum FIELD_NAME {
		NAME,
	}

	/** メタデータサービス */
	private final MetaDataServiceAsync service;

	/** 編集対象 */
	private SmsMailTemplateDefinition curDefinition;

	/** 編集対象バージョン */
	private int curVersion;

	/** 編集対象ID */
	private String curDefinitionId;

	/** ヘッダ部分 */
	private MetaCommonHeaderPane headerPane;
	/** 共通属性部分 */
	private MetaCommonAttributeSection commonSection;

	/** 個別属性部分（デフォルト） */
	private SmsMailTemplateAttributePane smsMailTemplateAttrPane;

	/** 個別属性部分（多言語） */
	private MultiSmsMailTemplateAttributePane multiSmsMailTemplateAttrPane;

	private Map<String, String> enableLang;

	public SmsMailTemplateEditPane(MetaDataItemMenuTreeNode targetNode, DefaultMetaDataPlugin plugin) {
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
		commonSection = new MetaCommonAttributeSection(targetNode, SmsMailTemplateDefinition.class);

		//個別属性
		smsMailTemplateAttrPane = new SmsMailTemplateAttributePane();
		multiSmsMailTemplateAttrPane = new MultiSmsMailTemplateAttributePane();

		//Section設定
		SectionStackSection defaultSmsSection = createSection("Default SmsTemplate", smsMailTemplateAttrPane);
		SectionStackSection multiSmsSection = createSection("Multilingual SmsTemplate", false, multiSmsMailTemplateAttrPane);
		setMainSections(commonSection, defaultSmsSection, multiSmsSection);

		//全体配置
		addMember(headerPane);
		addMember(mainStack);

		service.getEnableLanguages(TenantInfoHolder.getId(), new AsyncCallback<Map<String, String>>() {
			@Override
			public void onFailure(Throwable caught) {
				SC.say(AdminClientMessageUtil.getString("ui_metadata_sms_SmsMailTemplateEditPane_failed"),
						AdminClientMessageUtil.getString("ui_metadata_sms_SmsMailTemplateEditPane_failedGetScreenInfo"));
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
		smsMailTemplateAttrPane.clearErrors();

		service.getDefinitionEntry(TenantInfoHolder.getId(), SmsMailTemplateDefinition.class.getName(), defName, new AsyncCallback<DefinitionEntry>() {

			@Override
			public void onFailure(Throwable caught) {
				SC.say(AdminClientMessageUtil.getString("ui_metadata_sms_SmsMailTemplateEditPane_failed"),
						AdminClientMessageUtil.getString("ui_metadata_sms_SmsMailTemplateEditPane_failedGetScreenInfo"));
				GWT.log(caught.toString(), caught);
			}

			@Override
			public void onSuccess(DefinitionEntry result) {
				//画面に反映
				setDefinition(result);
			}
		});

		//ステータスチェック
		StatusCheckUtil.statuCheck(SmsMailTemplateDefinition.class.getName(), defName, this);
	}

	/**
	 * Definition画面設定処理
	 *
	 * @param definition 編集対象
	 */
	private void setDefinition(DefinitionEntry entry) {
		this.curDefinition = (SmsMailTemplateDefinition) entry.getDefinition();
		this.curVersion = entry.getDefinitionInfo().getVersion();
		this.curDefinitionId = entry.getDefinitionInfo().getObjDefId();

		//共通属性
		commonSection.setName(curDefinition.getName());
		commonSection.setDisplayName(curDefinition.getDisplayName());
		commonSection.setDescription(curDefinition.getDescription());

		smsMailTemplateAttrPane.setDefinition(curDefinition);

		multiSmsMailTemplateAttrPane.init(curDefinition.getLocalizedSmsMailTemplateList());
	}

	/**
	 * 更新処理
	 *
	 * @param definition 更新対象
	 */
	private void updateSmsMailTemplate(final SmsMailTemplateDefinition definition, boolean checkVersion) {
		SmartGWTUtil.showSaveProgress();
		service.updateDefinition(TenantInfoHolder.getId(), definition, curVersion, checkVersion, new MetaDataUpdateCallback() {

			@Override
			protected void overwriteUpdate() {
				updateSmsMailTemplate(definition, false);
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
	private void updateComplete(SmsMailTemplateDefinition definition) {
		SC.say(AdminClientMessageUtil.getString("ui_metadata_sms_SmsMailTemplateEditPane_completion"),
				AdminClientMessageUtil.getString("ui_metadata_sms_SmsMailTemplateEditPane_saveSmsTemplate"));

		//再表示
		initializeData();
		commonSection.refreshSharedConfig();

		//ツリー再表示
		plugin.refreshWithSelect(definition.getName(), new AsyncCallback<MetaDataItemMenuTreeNode>() {
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

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
			boolean mailValidate = smsMailTemplateAttrPane.validate();
			if (!commonValidate || !mailValidate) {
				return;
			}
			SC.ask(AdminClientMessageUtil.getString("ui_metadata_sms_SmsMailTemplateEditPane_saveConfirm"),
					AdminClientMessageUtil.getString("ui_metadata_sms_SmsMailTemplateEditPane_saveSmsTemplateComment"),
					new BooleanCallback() {

				@Override
				public void execute(Boolean value) {
					if (value) {
						SmsMailTemplateDefinition definition = curDefinition;
						definition.setName(commonSection.getName());
						definition.setDisplayName(commonSection.getDisplayName());
						definition.setDescription(commonSection.getDescription());
						definition = smsMailTemplateAttrPane.getEditDefinition(definition);

						updateSmsMailTemplate(definition, true);
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
			SC.ask(AdminClientMessageUtil.getString("ui_metadata_sms_SmsMailTemplateEditPane_cancelConfirm"),
					AdminClientMessageUtil.getString("ui_metadata_sms_SmsMailTemplateEditPane_cancelConfirmComment")
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



	private class SmsMailTemplateAttributePane extends VLayout {
		private DynamicForm form;

		private TextItem langOrUserBindingName;

		private TabSet massageTabSet;
		private Tab plainMessageTab;
		private ScriptEditorPane plainEditor;

		public SmsMailTemplateAttributePane() {
			setOverflow(Overflow.AUTO);	//Stack上の表示領域が小さい場合にスクロールができるようにAUTO設定

			VLayout mainPane = new VLayout();
			mainPane.setMargin(5);

			form = new DynamicForm();
			form.setWidth100();
			form.setNumCols(5);	//間延びしないように最後に１つ余分に作成
			form.setColWidths(100, "*", 100, "*", "*");

			langOrUserBindingName = new TextItem("bindKey", AdminClientMessageUtil.getString("ui_metadata_sms_SmsMailTemplateEditPane_langConfigInfoBindName"));
			form.setItems(langOrUserBindingName);

			//メッセージ編集領域
			massageTabSet = new TabSet();
			massageTabSet.setWidth100();
			//massageTabSet.setHeight100();
			massageTabSet.setHeight(450);	//高さは固定でないとうまくいかないため指定
			massageTabSet.setPaneContainerOverflow(Overflow.HIDDEN);	//Editor側のスクロールを利用するため非表示に設定（Editor側のサイズを調整）

			plainMessageTab = new Tab();
			plainMessageTab.setTitle(AdminClientMessageUtil.getString("ui_metadata_sms_SmsMailTemplateEditPane_textMessage"));

			plainEditor = new ScriptEditorPane();
			plainEditor.setMode(EditorMode.TEXT);
			plainMessageTab.setPane(plainEditor);
			massageTabSet.addTab(plainMessageTab);

			mainPane.addMember(form);
			mainPane.addMember(massageTabSet);

			addMember(mainPane);
		}

		/**
		 * MailTemplateDefinitionを展開します。
		 *
		 * @param definition MailTemplateDefinition
		 */
		public void setDefinition(SmsMailTemplateDefinition definition) {
			String plainMessage = null;
			if (definition != null) {
				langOrUserBindingName.setValue(definition.getLangOrUserBindingName());

				if (definition.getPlainMessage() == null) {
					plainEditor.setText("");
				} else {
					PlainTextBodyPart part = definition.getPlainMessage();
					plainEditor.setText(part.getContent());
					plainMessage = part.getContent();
				}
			} else {
				langOrUserBindingName.clearValue();
				plainEditor.setText("");
			}

			massageTabSet.selectTab(plainMessageTab);
			setTabTitle(plainMessageTab, AdminClientMessageUtil.getString("ui_metadata_sms_SmsMailTemplateEditPane_textMessage"), plainMessage);
		}

		/**
		 * 編集されたMailTemplateDefinition情報を返します。
		 *
		 * @return 編集MailTemplateDefinition情報
		 */
		public SmsMailTemplateDefinition getEditDefinition(SmsMailTemplateDefinition definition) {
			definition.setLangOrUserBindingName(SmartGWTUtil.getStringValue(langOrUserBindingName));
			PlainTextBodyPart plainPart = new  PlainTextBodyPart();
			plainPart.setContent(plainEditor.getText());
			definition.setPlainMessage(plainPart);
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

	private class MultiSmsMailTemplateAttributePane extends VLayout {
		public LanguageMapGrid grid;

		public MultiSmsMailTemplateAttributePane() {
			grid = new LanguageMapGrid();
			grid.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
				public void onRecordDoubleClick(RecordDoubleClickEvent event) {
					editMap((ListGridRecord)event.getRecord());
				}
			});

			IButton addBtn = new IButton("Add");
			addBtn.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					addMap();
				}
			});

			IButton delBtn = new IButton("Remove");
			delBtn.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					String lang = grid.getSelectedRecord().getAttribute(FIELD_NAME.NAME.name());
					grid.removeSelectedData();

					Map<String, LocalizedSmsMailTemplateDefinition> map = new HashMap<String, LocalizedSmsMailTemplateDefinition>();
					for (LocalizedSmsMailTemplateDefinition def : curDefinition.getLocalizedSmsMailTemplateList()) {
						map.put(enableLang.get(def.getLocaleName()), def);
					}

					map.remove(lang);

					List<LocalizedSmsMailTemplateDefinition> newList = new ArrayList<LocalizedSmsMailTemplateDefinition>();

					for(Map.Entry<String, LocalizedSmsMailTemplateDefinition> e : map.entrySet()) {
						newList.add(e.getValue());
					}

					curDefinition.setLocalizedSmsMailTemplateList(newList);
				}
			});

			HLayout mapButtonPane = new HLayout(5);
			mapButtonPane.setMargin(5);
			mapButtonPane.addMember(addBtn);
			mapButtonPane.addMember(delBtn);

			addMember(grid);
			addMember(mapButtonPane);
		}

		private void addMap() {
			editMap(null);
		}

		public ListGridRecord createRecord(LocalizedSmsMailTemplateDefinition param, ListGridRecord record, boolean init) {
			if (record == null) {
				record = new ListGridRecord();

				if (!init) {
					curDefinition.addLocalizedSmsMailTemplate(param);
				}
			} else {
				String lang = record.getAttribute(FIELD_NAME.NAME.name());

				// 一旦更新レコードのDefinitionを削除
				Map<String, LocalizedSmsMailTemplateDefinition> map = new HashMap<String, LocalizedSmsMailTemplateDefinition>();
				for (LocalizedSmsMailTemplateDefinition def : curDefinition.getLocalizedSmsMailTemplateList()) {
					map.put(enableLang.get(def.getLocaleName()), def);
				}

				map.remove(lang);

				// 更新した内容を追加
				map.put(lang, param);

				List<LocalizedSmsMailTemplateDefinition> newList = new ArrayList<LocalizedSmsMailTemplateDefinition>();

				for(Map.Entry<String, LocalizedSmsMailTemplateDefinition> e : map.entrySet()) {
					newList.add(e.getValue());
				}

				curDefinition.setLocalizedSmsMailTemplateList(newList);

			}
			record.setAttribute(FIELD_NAME.NAME.name(), enableLang.get(param.getLocaleName()));

			return record;
		}

		private void editMap(final ListGridRecord record) {
			LocalizedSmsMailTemplateDefinition temp = null;

			if (record == null) {

			} else {
				if (curDefinition != null && curDefinition.getLocalizedSmsMailTemplateList() != null && curDefinition.getLocalizedSmsMailTemplateList().size() > 0) {

					Map<String, LocalizedSmsMailTemplateDefinition> map = new HashMap<String, LocalizedSmsMailTemplateDefinition>();
					for (LocalizedSmsMailTemplateDefinition def : curDefinition.getLocalizedSmsMailTemplateList()) {
						map.put(enableLang.get(def.getLocaleName()), def);
					}

					String lang = record.getAttribute(FIELD_NAME.NAME.name());
					temp = map.get(lang);
				}
			}

			final SmsMailTemplateSettingByLocaleDialog dialog = new SmsMailTemplateSettingByLocaleDialog(temp);
			dialog.addDataChangeHandler(new DataChangedHandler() {

				@Override
				public void onDataChanged(DataChangedEvent event) {
					LocalizedSmsMailTemplateDefinition param = event.getValueObject(LocalizedSmsMailTemplateDefinition.class);
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

		public void init(List<LocalizedSmsMailTemplateDefinition> definitionList) {
			grid.setData(new ListGridRecord[]{});

			if (definitionList != null && definitionList.size() > 0) {
				ListGridRecord[] temp = new ListGridRecord[definitionList.size()];

				int cnt = 0;
				for (LocalizedSmsMailTemplateDefinition definition : definitionList) {
					ListGridRecord newRecord = createRecord(definition, null, true);
					temp[cnt] = newRecord;
					cnt ++;

				}
				grid.setData(temp);
			}
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

			setFields(languageField);
		}
	}
}
