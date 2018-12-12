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

package org.iplass.adminconsole.client.metadata.ui.mail;

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
import org.iplass.adminconsole.shared.metadata.dto.MetaDataConstants;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;
import org.iplass.gwt.ace.client.EditorMode;
import org.iplass.mtp.definition.DefinitionEntry;
import org.iplass.mtp.mail.template.definition.HtmlBodyPart;
import org.iplass.mtp.mail.template.definition.LocalizedMailTemplateDefinition;
import org.iplass.mtp.mail.template.definition.MailTemplateDefinition;
import org.iplass.mtp.mail.template.definition.PlainTextBodyPart;

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
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
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

public class MailTemplateEditPane extends MetaDataMainEditPane {

	private enum FIELD_NAME {
		NAME,
		SUBJECT,
	}

	/** メタデータサービス */
	private final MetaDataServiceAsync service;

	/** 編集対象 */
	private MailTemplateDefinition curDefinition;

	/** 編集対象バージョン */
	private int curVersion;

	/** 編集対象ID */
	private String curDefinitionId;

	/** ヘッダ部分 */
	private MetaCommonHeaderPane headerPane;
	/** 共通属性部分 */
	private MetaCommonAttributeSection commonSection;

	/** 個別属性部分（デフォルト） */
	private MailTemplateAttributePane mailTemplateAttrPane;

	/** 個別属性部分（多言語） */
	private MultiMailTemplateAttributePane multiMailTemplateAttrPane;

	private Map<String, String> enableLang;

	public MailTemplateEditPane(MetaDataItemMenuTreeNode targetNode, DefaultMetaDataPlugin plugin) {
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
		commonSection = new MetaCommonAttributeSection(targetNode, MailTemplateDefinition.class);

		//個別属性
		mailTemplateAttrPane = new MailTemplateAttributePane();
		multiMailTemplateAttrPane = new MultiMailTemplateAttributePane();

		//Section設定
		SectionStackSection defaultMailSection = createSection("Default MailTemplate", mailTemplateAttrPane);
		SectionStackSection multiMailSection = createSection("Multilingual MailTemplate", false, multiMailTemplateAttrPane);
		setMainSections(commonSection, defaultMailSection, multiMailSection);

		//全体配置
		addMember(headerPane);
		addMember(mainStack);

		service.getEnableLanguages(TenantInfoHolder.getId(), new AsyncCallback<Map<String, String>>() {

			@Override
			public void onFailure(Throwable caught) {
				SC.say(AdminClientMessageUtil.getString("ui_metadata_mail_MailTemplateEditPane_failed"),
						AdminClientMessageUtil.getString("ui_metadata_mail_MailTemplateEditPane_failedGetScreenInfo"));
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
		mailTemplateAttrPane.clearErrors();
		multiMailTemplateAttrPane.clearErrors();

		service.getDefinitionEntry(TenantInfoHolder.getId(), MailTemplateDefinition.class.getName(), defName, new AsyncCallback<DefinitionEntry>() {

			@Override
			public void onFailure(Throwable caught) {
				SC.say(AdminClientMessageUtil.getString("ui_metadata_mail_MailTemplateEditPane_failed"),
						AdminClientMessageUtil.getString("ui_metadata_mail_MailTemplateEditPane_failedGetScreenInfo"));

				GWT.log(caught.toString(), caught);
			}

			@Override
			public void onSuccess(DefinitionEntry result) {
				//画面に反映
				setDefinition(result);
			}

		});

		//ステータスチェック
		StatusCheckUtil.statuCheck(MailTemplateDefinition.class.getName(), defName, this);
	}

	/**
	 * Definition画面設定処理
	 *
	 * @param definition 編集対象
	 */
	private void setDefinition(DefinitionEntry entry) {
		this.curDefinition = (MailTemplateDefinition) entry.getDefinition();
		this.curVersion = entry.getDefinitionInfo().getVersion();
		this.curDefinitionId = entry.getDefinitionInfo().getObjDefId();

		//共通属性
		commonSection.setName(curDefinition.getName());
		commonSection.setDisplayName(curDefinition.getDisplayName());
		commonSection.setDescription(curDefinition.getDescription());

		mailTemplateAttrPane.setDefinition(curDefinition);
		multiMailTemplateAttrPane.setDefinition(curDefinition);

	}


	/**
	 * 更新処理
	 *
	 * @param definition 更新対象
	 */
	private void updateMailTemplate(final MailTemplateDefinition definition, boolean checkVersion) {
		SmartGWTUtil.showSaveProgress();
		service.updateDefinition(TenantInfoHolder.getId(), definition, curVersion, checkVersion, new MetaDataUpdateCallback() {

			@Override
			protected void overwriteUpdate() {
				updateMailTemplate(definition, false);
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
	private void updateComplete(MailTemplateDefinition definition) {

		SC.say(AdminClientMessageUtil.getString("ui_metadata_mail_MailTemplateEditPane_completion"),
				AdminClientMessageUtil.getString("ui_metadata_mail_MailTemplateEditPane_saveMailTemplate"));

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

	private class MultiMailTemplateAttributePane extends VLayout {

		private DynamicForm form;
		private TextItem langOrUserBindingName;

		public LanguageMapGrid grid;

		public MultiMailTemplateAttributePane() {

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
					String lang = grid.getSelectedRecord().getAttribute(FIELD_NAME.NAME.name());
					grid.removeSelectedData();

					Map<String, LocalizedMailTemplateDefinition> map = new HashMap<String, LocalizedMailTemplateDefinition>();
					for (LocalizedMailTemplateDefinition def : curDefinition.getLocalizedMailTemplateList()) {
						map.put(enableLang.get(def.getLocaleName()), def);
					}

					map.remove(lang);

					List<LocalizedMailTemplateDefinition> newList = new ArrayList<LocalizedMailTemplateDefinition>();

					for(Map.Entry<String, LocalizedMailTemplateDefinition> e : map.entrySet()) {
						newList.add(e.getValue());
					}

					curDefinition.setLocalizedMailTemplateList(newList);
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

		public ListGridRecord createRecord(LocalizedMailTemplateDefinition param, ListGridRecord record, boolean init) {
			if (record == null) {
				record = new ListGridRecord();

				if (!init) {
					curDefinition.addLocalizedMailTemplate(param);
				}
			} else {
				String lang = record.getAttribute(FIELD_NAME.NAME.name());

				// 一旦更新レコードのDefinitionを削除
				Map<String, LocalizedMailTemplateDefinition> map = new HashMap<String, LocalizedMailTemplateDefinition>();
				for (LocalizedMailTemplateDefinition def : curDefinition.getLocalizedMailTemplateList()) {
					map.put(enableLang.get(def.getLocaleName()), def);
				}

				map.remove(lang);

				// 更新した内容を追加
				map.put(lang, param);

				List<LocalizedMailTemplateDefinition> newList = new ArrayList<LocalizedMailTemplateDefinition>();

				for(Map.Entry<String, LocalizedMailTemplateDefinition> e : map.entrySet()) {
					newList.add(e.getValue());
				}

				curDefinition.setLocalizedMailTemplateList(newList);

			}
			record.setAttribute(FIELD_NAME.NAME.name(), enableLang.get(param.getLocaleName()));
			record.setAttribute(FIELD_NAME.SUBJECT.name(), param.getSubject());

			return record;
		}

		private void editMap(final ListGridRecord record) {
			LocalizedMailTemplateDefinition temp = null;

			if (record == null) {

			} else {
				if (curDefinition != null && curDefinition.getLocalizedMailTemplateList() != null && curDefinition.getLocalizedMailTemplateList().size() > 0) {

					Map<String, LocalizedMailTemplateDefinition> map = new HashMap<String, LocalizedMailTemplateDefinition>();
					for (LocalizedMailTemplateDefinition def : curDefinition.getLocalizedMailTemplateList()) {
						map.put(enableLang.get(def.getLocaleName()), def);
					}

					String lang = record.getAttribute(FIELD_NAME.NAME.name());
					temp = map.get(lang);
				}
			}

			final MailTemplateSettingByLocaleDialog dialog = new MailTemplateSettingByLocaleDialog(temp);
			dialog.addDataChangeHandler(new DataChangedHandler() {

				@Override
				public void onDataChanged(DataChangedEvent event) {
					LocalizedMailTemplateDefinition param = event.getValueObject(LocalizedMailTemplateDefinition.class);
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
		 * MailTemplateDefinitionを展開します。
		 *
		 * @param definition MailTemplateDefinition
		 */
		public void setDefinition(MailTemplateDefinition definition) {

			grid.setData(new ListGridRecord[]{});

			if (definition != null) {
				langOrUserBindingName.setValue(definition.getLangOrUserBindingName());

				List<LocalizedMailTemplateDefinition> definitionList = definition.getLocalizedMailTemplateList();
				if (definitionList != null && definitionList.size() > 0) {
					ListGridRecord[] temp = new ListGridRecord[definitionList.size()];

					int cnt = 0;
					for (LocalizedMailTemplateDefinition localDefinition : definitionList) {
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
		 * 編集されたMailTemplateDefinition情報を返します。
		 *
		 * @return 編集MailTemplateDefinition情報
		 */
		public MailTemplateDefinition getEditDefinition(MailTemplateDefinition definition) {
			definition.setLangOrUserBindingName(SmartGWTUtil.getStringValue(langOrUserBindingName));
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
			ListGridField templateField = new ListGridField(FIELD_NAME.SUBJECT.name(), "Subject");

			setFields(languageField, templateField);
		}
	}

	private class MailTemplateAttributePane extends VLayout {

//		private static final String PLAIN_TAB_TITLE = "テキストメッセージ";
//		private static final String HTML_TAB_TITLE = "HTMLメッセージ";

		private DynamicForm charsetForm;
		private TextItem charsetField;
		private TextItem htmlCharsetField;

		private DynamicForm sendFromForm;
		private TextItem fromField;
		private TextItem replyToField;
		private TextItem returnPathField;

		private DynamicForm smimeForm;
		private CheckboxItem smimeSignField;
		private CheckboxItem smimeEncryptField;

		private DynamicForm subjectForm;
		private TextItem subjectField;

		private TabSet massageTabSet;
		private Tab plainMessageTab;
		private Tab htmlMessageTab;
		private ScriptEditorPane plainEditor;
		private ScriptEditorPane htmlEditor;

		public MailTemplateAttributePane() {

			setOverflow(Overflow.AUTO);	//Stack上の表示領域が小さい場合にスクロールができるようにAUTO設定

			VLayout mainPane = new VLayout();
			mainPane.setMargin(5);
			mainPane.setMembersMargin(5);

			HLayout topPane = new HLayout();
			topPane.setMembersMargin(5);

			charsetForm = new DynamicForm();
			charsetForm.setWidth(270);
			charsetForm.setPadding(10);
			charsetForm.setNumCols(2);
			charsetForm.setColWidths(100, "*");
			charsetForm.setIsGroup(true);
			charsetForm.setGroupTitle("Charset");

			charsetField = new TextItem("charset", AdminClientMessageUtil.getString("ui_metadata_mail_MailTemplateEditPane_charaCode"));
			charsetField.setWidth(150);

			htmlCharsetField = new TextItem("htmlCharset", AdminClientMessageUtil.getString("ui_metadata_mail_MailTemplateEditPane_htmlCharaCode"));
			htmlCharsetField.setWidth(150);

			charsetForm.setItems(charsetField, htmlCharsetField);

			sendFromForm = new DynamicForm();
			sendFromForm.setWidth(430);
			sendFromForm.setPadding(10);
			sendFromForm.setNumCols(2);
			sendFromForm.setColWidths(100, "*");
			sendFromForm.setIsGroup(true);
			sendFromForm.setGroupTitle("Send From");

			fromField = new TextItem("from", AdminClientMessageUtil.getString("ui_metadata_mail_MailTemplateEditPane_sendFrom"));
			fromField.setWidth(MetaDataConstants.DEFAULT_FORM_ITEM_WIDTH);

			replyToField = new TextItem("replyTo", AdminClientMessageUtil.getString("ui_metadata_mail_MailTemplateEditPane_replyTo"));
			replyToField.setWidth(MetaDataConstants.DEFAULT_FORM_ITEM_WIDTH);

			returnPathField = new TextItem("returnPath", AdminClientMessageUtil.getString("ui_metadata_mail_MailTemplateEditPane_returnPath"));
			returnPathField.setWidth(MetaDataConstants.DEFAULT_FORM_ITEM_WIDTH);

			sendFromForm.setItems(fromField, replyToField, returnPathField);

			smimeForm = new DynamicForm();
			smimeForm.setWidth(180);
			smimeForm.setPadding(10);
			smimeForm.setNumCols(1);
			smimeForm.setColWidths("*");
			smimeForm.setIsGroup(true);
			smimeForm.setGroupTitle("S/MIME");

			smimeSignField = new CheckboxItem("smimeSign", AdminClientMessageUtil.getString("ui_metadata_mail_MailTemplateEditPane_smimeSign"));
			SmartGWTUtil.addHoverToFormItem(smimeSignField, AdminClientMessageUtil.getString("ui_metadata_mail_MailTemplateEditPane_smimeSignTooltip"));
			smimeSignField.setWidth(150);

			smimeEncryptField = new CheckboxItem("smimeEncrypt", AdminClientMessageUtil.getString("ui_metadata_mail_MailTemplateEditPane_smimeEncrypt"));
			SmartGWTUtil.addHoverToFormItem(smimeEncryptField, AdminClientMessageUtil.getString("ui_metadata_mail_MailTemplateEditPane_smimeEncryptTooltip"));
			smimeEncryptField.setWidth(150);

			smimeForm.setItems(smimeSignField, smimeEncryptField);

			topPane.addMember(charsetForm);
			topPane.addMember(sendFromForm);
			topPane.addMember(smimeForm);

			subjectForm = new DynamicForm();
			subjectForm.setWidth100();
			subjectForm.setNumCols(3);	//間延びしないように最後に１つ余分に作成
			subjectForm.setColWidths(70, "*", "*");

			subjectField = new TextItem("subject", AdminClientMessageUtil.getString("ui_metadata_mail_MailTemplateEditPane_subject"));
			//subjectField.setWidth(MetaDataConstants.DEFAULT_FORM_ITEM_WIDTH);
			subjectField.setWidth("100%");
			subjectField.setColSpan(4);
			SmartGWTUtil.setRequired(subjectField);

			subjectForm.setItems(subjectField);

			//メッセージ編集領域
			massageTabSet = new TabSet();
			massageTabSet.setWidth100();
			//massageTabSet.setHeight100();
			massageTabSet.setHeight(450);	//高さは固定でないとうまくいかないため指定
			massageTabSet.setPaneContainerOverflow(Overflow.HIDDEN);	//Editor側のスクロールを利用するため非表示に設定（Editor側のサイズを調整）

	        plainMessageTab = new Tab();
	        plainMessageTab.setTitle(AdminClientMessageUtil.getString("ui_metadata_mail_MailTemplateEditPane_textMessage"));

			plainEditor = new ScriptEditorPane();
			plainEditor.setMode(EditorMode.TEXT);

			plainMessageTab.setPane(plainEditor);
	        massageTabSet.addTab(plainMessageTab);

	        htmlMessageTab = new Tab();
	        htmlMessageTab.setTitle(AdminClientMessageUtil.getString("ui_metadata_mail_MailTemplateEditPane_htmlMessage"));

			htmlEditor = new ScriptEditorPane();
			htmlEditor.setMode(EditorMode.HTML);

			htmlMessageTab.setPane(htmlEditor);
			massageTabSet.addTab(htmlMessageTab);

			mainPane.addMember(topPane);
			mainPane.addMember(subjectForm);
			mainPane.addMember(massageTabSet);

			addMember(mainPane);
		}

		/**
		 * MailTemplateDefinitionを展開します。
		 *
		 * @param definition MailTemplateDefinition
		 */
		public void setDefinition(MailTemplateDefinition definition) {
			String plainMessage = null;
			String htmlMessage = null;
			if (definition != null) {
				subjectField.setValue(definition.getSubject());
				charsetField.setValue(definition.getCharset());
				fromField.setValue(definition.getFrom());
				replyToField.setValue(definition.getReplyTo());
				returnPathField.setValue(definition.getReturnPath());
				smimeSignField.setValue(definition.isSmimeSign());
				smimeEncryptField.setValue(definition.isSmimeEncrypt());
				boolean existPlainMessage = false;
				boolean existHtmlMessage = false;
				if (definition.getPlainMessage() == null) {
					plainEditor.setText("");
				} else {
					PlainTextBodyPart part = definition.getPlainMessage();
					plainEditor.setText(part.getContent());
					existPlainMessage
						= (part.getContent() != null && !part.getContent().isEmpty());
					plainMessage = part.getContent();
				}

				if (definition.getHtmlMessage() == null) {
					htmlEditor.setText("");
					htmlCharsetField.clearValue();
				} else {
					HtmlBodyPart part = definition.getHtmlMessage();
					htmlEditor.setText(part.getContent());
					htmlCharsetField.setValue(part.getCharset());
					existHtmlMessage
						= (part.getContent() != null && !part.getContent().isEmpty());
					htmlMessage = part.getContent();
				}

				if (existPlainMessage) {
					massageTabSet.selectTab(plainMessageTab);
				} else if (existHtmlMessage) {
					massageTabSet.selectTab(htmlMessageTab);
				} else {
					massageTabSet.selectTab(plainMessageTab);
				}
			} else {
				subjectField.clearValue();
				charsetField.clearValue();
				fromField.clearValue();
				replyToField.clearValue();
				returnPathField.clearValue();
				plainEditor.setText("");
				htmlEditor.setText("");
				htmlCharsetField.clearValue();
				smimeSignField.clearValue();
				smimeEncryptField.clearValue();

				massageTabSet.selectTab(plainMessageTab);
			}

			setTabTitle(plainMessageTab, AdminClientMessageUtil.getString("ui_metadata_mail_MailTemplateEditPane_textMessage"), plainMessage);
			setTabTitle(htmlMessageTab, AdminClientMessageUtil.getString("ui_metadata_mail_MailTemplateEditPane_htmlMessage"), htmlMessage);
		}

		/**
		 * 編集されたMailTemplateDefinition情報を返します。
		 *
		 * @return 編集MailTemplateDefinition情報
		 */
		public MailTemplateDefinition getEditDefinition(MailTemplateDefinition definition) {

			definition.setSubject(SmartGWTUtil.getStringValue(subjectField));
			definition.setCharset(SmartGWTUtil.getStringValue(charsetField));
			definition.setFrom(SmartGWTUtil.getStringValue(fromField));
			definition.setReplyTo(SmartGWTUtil.getStringValue(replyToField));
			definition.setReturnPath(SmartGWTUtil.getStringValue(returnPathField));
			PlainTextBodyPart plainPart = new  PlainTextBodyPart();
			plainPart.setContent(plainEditor.getText());
			definition.setPlainMessage(plainPart);
			HtmlBodyPart htmlPart = new  HtmlBodyPart();
			htmlPart.setContent(htmlEditor.getText());
			htmlPart.setCharset(SmartGWTUtil.getStringValue(htmlCharsetField));
			definition.setHtmlMessage(htmlPart);
			definition.setSmimeSign(SmartGWTUtil.getBooleanValue(smimeSignField));
			definition.setSmimeEncrypt(SmartGWTUtil.getBooleanValue(smimeEncryptField));
			return definition;
		}

		/**
		 * 入力チェックを実行します。
		 *
		 * @return 入力チェック結果
		 */
		public boolean validate() {
			return charsetForm.validate() && sendFromForm.validate() && smimeSignField.validate() && subjectForm.validate();
		}

		/**
		 * エラー表示をクリアします。
		 */
		public void clearErrors() {
			charsetForm.clearErrors(true);
			sendFromForm.clearErrors(true);
			smimeForm.clearErrors(true);
			subjectForm.clearErrors(true);
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

	/**
	 * 保存ボタンイベント
	 */
	private final class SaveClickHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			boolean commonValidate = commonSection.validate();
			boolean mailValidate = mailTemplateAttrPane.validate();
			boolean multiValidate = multiMailTemplateAttrPane.validate();
			if (!commonValidate || !mailValidate || !multiValidate) {
				return;
			}

			SC.ask(AdminClientMessageUtil.getString("ui_metadata_mail_MailTemplateEditPane_saveConfirm"),
					AdminClientMessageUtil.getString("ui_metadata_mail_MailTemplateEditPane_saveMailTemplateComment"), new BooleanCallback() {

				@Override
				public void execute(Boolean value) {
					if (value) {
						MailTemplateDefinition definition = curDefinition;
						definition.setName(commonSection.getName());
						definition.setDisplayName(commonSection.getDisplayName());
						definition.setDescription(commonSection.getDescription());
						definition = mailTemplateAttrPane.getEditDefinition(definition);
						definition = multiMailTemplateAttrPane.getEditDefinition(definition);

						updateMailTemplate(definition, true);
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

			SC.ask(AdminClientMessageUtil.getString("ui_metadata_mail_MailTemplateEditPane_cancelConfirm"),
					AdminClientMessageUtil.getString("ui_metadata_mail_MailTemplateEditPane_cancelConfirmComment")
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
