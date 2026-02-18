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

package org.iplass.adminconsole.client.metadata.ui.message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.iplass.adminconsole.client.base.event.DataChangedEvent;
import org.iplass.adminconsole.client.base.event.DataChangedHandler;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.io.download.PostDownloadFrame;
import org.iplass.adminconsole.client.base.rpc.AdminAsyncCallback;
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
import org.iplass.mtp.definition.DefinitionEntry;
import org.iplass.mtp.definition.LocalizedStringDefinition;
import org.iplass.mtp.message.MessageCategory;
import org.iplass.mtp.message.MessageItem;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.CanvasItem;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.ResetItem;
import com.smartgwt.client.widgets.form.fields.SpacerItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.EditCompleteEvent;
import com.smartgwt.client.widgets.grid.events.EditCompleteHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.LayoutSpacer;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;


/**
 * メッセージカテゴリ編集パネル
 *
 * メッセージカテゴリ編集の最上位パネルです。
 * {@link org.iplass.adminconsole.client.base.ui.layout.MainWorkspaceTab}により生成されます。
 *
 */
public class MessageCategoryEditPane extends MetaDataMainEditPane {

	private static final String EXPORT_ICON = "[SKIN]/actions/download.png";
	private static final String IMPORT_ICON = "[SKIN]/SchemaViewer/operation.png";

	/** メタデータサービス */
	private final MetaDataServiceAsync service;

	/** 編集対象 */
	private MessageCategory curDefinition;

	/** 編集対象バージョン */
	private int curVersion;

	/** 編集対象ID */
	private String curDefinitionId;

	/** ヘッダ部分 */
	private MetaCommonHeaderPane headerPane;
	/** 共通属性部分 */
	private MetaCommonAttributeSection<MessageCategory> commonSection;

	/** 個別属性部分 */
	private MessageItemEditPane messageItemPane;

	public MessageCategoryEditPane(MetaDataItemMenuTreeNode targetNode, DefaultMetaDataPlugin plugin) {
		super(targetNode, plugin);

		service = MetaDataServiceFactory.get();

		//レイアウト設定
		setWidth100();

		//ヘッダ（ボタン）部分
		headerPane = new MetaCommonHeaderPane(targetNode);
		headerPane.setSaveClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				save();
			}
		});
		headerPane.setCancelClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				cancel();
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
		IButton exportCSV = new IButton("Export CSV");
		exportCSV.setIcon(EXPORT_ICON);
		exportCSV.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				exportCSV();
			}
		});
		headerPane.addMember(exportCSV);
		IButton importCSV = new IButton("Import CSV");
		importCSV.setIcon(IMPORT_ICON);
		importCSV.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				importCSV();
			}
		});
		headerPane.addMember(importCSV);

		//共通属性
		commonSection = new MetaCommonAttributeSection<>(targetNode, MessageCategory.class);

		//個別属性
		messageItemPane = new MessageItemEditPane();

		//Section設定
		SectionStackSection messageSection = createSection(AdminClientMessageUtil.getString("ui_metadata_message_MessageCategoryEditPane_messageAttribute"), messageItemPane);
		setMainSections(commonSection, messageSection);

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

		service.getDefinitionEntry(TenantInfoHolder.getId(), MessageCategory.class.getName(), defName, new AsyncCallback<DefinitionEntry>() {

			@Override
			public void onSuccess(DefinitionEntry result) {
				//画面に反映
				setDefinition(result);
			}

			@Override
			public void onFailure(Throwable caught) {
				SC.say(AdminClientMessageUtil.getString("ui_metadata_message_MessageCategoryEditPane_failed"),
						AdminClientMessageUtil.getString("ui_metadata_message_MessageCategoryEditPane_failedGetScreenInfo") + caught.getMessage());

				GWT.log(caught.toString(), caught);
			}
		});

		//ステータスチェック
		StatusCheckUtil.statuCheck(MessageCategory.class.getName(), defName, this);
	}

	/**
	 * Definition画面設定処理
	 *
	 * @param definition 編集対象
	 */
	private void setDefinition(DefinitionEntry entry) {
		this.curDefinition = (MessageCategory) entry.getDefinition();
		this.curVersion = entry.getDefinitionInfo().getVersion();
		this.curDefinitionId = entry.getDefinitionInfo().getObjDefId();

		commonSection.setDefinition(curDefinition);
		messageItemPane.setMessageItems(curDefinition.getMessageItems());
	}

	/**
	 * 保存ボタン処理
	 */
	private void save() {

		boolean commonValidate = commonSection.validate();
		if (!commonValidate) {
			return;
		}

		SC.ask(AdminClientMessageUtil.getString("ui_metadata_message_MessageCategoryEditPane_saveConfirm"),
				AdminClientMessageUtil.getString("ui_metadata_message_MessageCategoryEditPane_saveConfirmComment"), new BooleanCallback() {

			@Override
			public void execute(Boolean value) {
				if (value) {
					commonSection.getEditDefinition(curDefinition);
					curDefinition.setMessageItems(messageItemPane.getEditMessageItems());

					updateDefinition(curDefinition, true);
				}
			}
		});
	}

	/**
	 * キャンセルボタン処理
	 */
	private void cancel() {

		SC.ask(AdminClientMessageUtil.getString("ui_metadata_message_MessageCategoryEditPane_cancelConfirm"),
				AdminClientMessageUtil.getString("ui_metadata_message_MessageCategoryEditPane_cancelConfirmComment")
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
	 * CSV Export処理
	 */
	private void exportCSV() {

		PostDownloadFrame frame = new PostDownloadFrame();
		frame.setAction(GWT.getModuleBaseURL() + "service/messagedownload")
			.addParameter("tenantId", String.valueOf(TenantInfoHolder.getId()))
			.addParameter("definitionName", defName)
			.execute();
	}

	/**
	 * CSV Import処理
	 */
	private void importCSV() {

		SC.ask(AdminClientMessageUtil.getString("ui_metadata_message_MessageCategoryEditPane_cancelConfirm"),
				AdminClientMessageUtil.getString("ui_metadata_message_MessageCategoryEditPane_cancelConfirmComment")
				, new BooleanCallback() {
			@Override
			public void execute(Boolean value) {
				if (value) {
					MessageItemCsvUploadDialog dialog = new MessageItemCsvUploadDialog(defName);
					dialog.addDataChangedHandler(new DataChangedHandler() {

						@Override
						public void onDataChanged(DataChangedEvent event) {
							//再表示
							initializeData();
							commonSection.refreshSharedConfig();
						}
					});
					dialog.show();
				}
			}
		});
	}

	/**
	 * 更新処理
	 *
	 * @param definition 更新対象
	 */
	private void updateDefinition(final MessageCategory definition, boolean checkVersion) {
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
	private void updateDefinitionComplete(MessageCategory definition) {

		SC.say(AdminClientMessageUtil.getString("ui_metadata_message_MessageCategoryEditPane_completion"),
				AdminClientMessageUtil.getString("ui_metadata_message_MessageCategoryEditPane_saveMessageCategoryComp"));

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

	private class MessageItemEditPane extends VLayout {

		private FilterPane filterPane;
		private MessageItemListGrid grid;

		private Map<String, String> enableLanguages;

		public MessageItemEditPane() {
			setMargin(5);
		}

		public void setMessageItems(final Map<String, MessageItem> messageItems) {

			if (enableLanguages == null) {
				//言語検索
				service.getEnableLanguages(TenantInfoHolder.getId(), new AdminAsyncCallback<Map<String, String>>() {
					@Override
					public void onSuccess(Map<String, String> result) {
						MessageItemEditPane.this.enableLanguages = result;

						//画面生成
						createPane();

						//一覧表示
						filterPane.clearFilter();
						grid.setMessageItems(messageItems);
					}
				});
			} else {
				//一覧表示
				filterPane.clearFilter();
				grid.setMessageItems(messageItems);
			}
		}

		public void filterMessageItem(Set<String> showLanguages, String filter) {
			grid.filterMessageItem(showLanguages, filter);
		}

		public Map<String, MessageItem> getEditMessageItems() {
			return grid.getEditMessageItems();
		}

		/**
		 * 一覧作成処理
		 */
		private void createPane() {

			filterPane = new FilterPane(this, enableLanguages);

			MessageItemOperationPane topOperationPane = new MessageItemOperationPane();
			topOperationPane.addAddClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					addMessageItem();
				}
			});
			topOperationPane.addRemoveClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					removeMessageItem();
				}
			});
			topOperationPane.addUndoClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					undoMessageItem();
				}
			});

			grid = new MessageItemListGrid(enableLanguages);

			MessageItemOperationPane bottomOperationPane = new MessageItemOperationPane();
			bottomOperationPane.addAddClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					addMessageItem();
				}
			});
			bottomOperationPane.addRemoveClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					removeMessageItem();
				}
			});
			bottomOperationPane.addUndoClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					undoMessageItem();
				}
			});

			addMember(filterPane);
			addMember(topOperationPane);
			addMember(grid);
			addMember(bottomOperationPane);
		}

		private void addMessageItem() {
			grid.addMessageItem();
		}

		private void removeMessageItem() {
			grid.removeSelectedMessageItem();
		}

		private void undoMessageItem() {
			grid.undoSelectedMessageItem();
		}

	}

	private class MessageItemOperationPane extends HLayout {

		private IButton addItem;
		private IButton removeItem;
		private IButton undoItem;

		public MessageItemOperationPane() {

			setMargin(5);
			setMembersMargin(5);
			setAutoHeight();

			addItem = new IButton("Add");
			removeItem = new IButton("Remove");
			undoItem = new IButton("Undo");

			addMember(addItem);
			addMember(removeItem);
			addMember(undoItem);
		}

		public HandlerRegistration addAddClickHandler(ClickHandler handler) {
			return addItem.addClickHandler(handler);
		}

		public HandlerRegistration addRemoveClickHandler(ClickHandler handler) {
			return removeItem.addClickHandler(handler);
		}

		public HandlerRegistration addUndoClickHandler(ClickHandler handler) {
			return undoItem.addClickHandler(handler);
		}
	}

	private class FilterPane extends HLayout {

		private MessageItemEditPane owner;

		private List<CheckboxItem> langFields = new ArrayList<>();
		private TextItem filterItem;

		public FilterPane(MessageItemEditPane owner, Map<String, String> enableLanguages) {
			this.owner = owner;

			setMargin(5);
			setMembersMargin(5);
			setAutoHeight();

			DynamicForm form = new DynamicForm();
			form.setPadding(10);
			form.setIsGroup(true);
			form.setGroupTitle("List Filter");

			form.setNumCols(enableLanguages.size() + 2);

			List<FormItem> fileds = new ArrayList<>();
			List<Object> coWidths = new ArrayList<>();

			if (enableLanguages.size() > 0) {

				CanvasItem langCaption = new CanvasItem();
				Label langLabel = new Label("View Languages:");
				langLabel.setAlign(Alignment.RIGHT);
				langLabel.addStyleName("formTitle");
				langCaption.setCanvas(langLabel);
				langCaption.setShowTitle(false);
				langCaption.setHeight("*");
				fileds.add(langCaption);
				coWidths.add(100);

				for(Map.Entry<String, String> entry : enableLanguages.entrySet()) {
					CheckboxItem showLangField = new CheckboxItem();
					showLangField.setTitle(entry.getValue());
					//ValueはBooleanで設定して、langKeyに値を設定
					showLangField.setValue(true);
					showLangField.setDefaultValue(true);
					showLangField.setAttribute("langKey", entry.getKey());
					showLangField.setShowTitle(false);
					showLangField.setWidth(100);

					fileds.add(showLangField);
					coWidths.add(100);
					langFields.add(showLangField);
				}

				SpacerItem space2 = new SpacerItem();
				space2.setWidth("*");
				space2.setEndRow(true);
				fileds.add(space2);
				coWidths.add("*");
			} else {
				//Langがない場合はFilterテキスト用に幅設定
				coWidths.add(100);
				coWidths.add(200);
			}

			filterItem = new TextItem();
			filterItem.setTitle(AdminClientMessageUtil.getString("ui_metadata_message_MessageCategoryEditPane_filter"));
			filterItem.setDefaultValue("");
			filterItem.setWidth("100%");
			filterItem.setColSpan(enableLanguages.size());
			fileds.add(filterItem);

			ButtonItem btnApply = new ButtonItem();
			btnApply.setTitle(AdminClientMessageUtil.getString("ui_metadata_message_MessageCategoryEditPane_apply"));
			btnApply.setShowTitle(false);
			btnApply.setWidth(100);
			btnApply.setEndRow(false);
			btnApply.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
				@Override
				public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
					filter();
				}
			});
			fileds.add(btnApply);

			ResetItem btnReset = new ResetItem();
			btnReset.setTitle(AdminClientMessageUtil.getString("ui_metadata_message_MessageCategoryEditPane_reset"));
			btnReset.setShowTitle(false);
			btnReset.setWidth(100);
			btnReset.setStartRow(false);
			btnReset.setEndRow(false);
			btnReset.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
				@Override
				public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
					reset();
				}
			});
			fileds.add(btnReset);


			form.setColWidths(coWidths.toArray(new Object[0]));
			form.setFields(fileds.toArray(new FormItem[0]));

			addMember(form);
		}

		public void clearFilter() {
			filterItem.clearValue();
		}

		private void filter() {
			//選択しているLanguageの取得
			Set<String> showLanguages = new HashSet<>();
			for (CheckboxItem langField : langFields) {
				if (SmartGWTUtil.getBooleanValue(langField)) {
					showLanguages.add(langField.getAttribute("langKey"));
				}
			}

			String filter = SmartGWTUtil.getStringValue(filterItem);

			//通知
			owner.filterMessageItem(showLanguages, filter);
		}

		private void reset() {
			//選択しているLanguageの取得
			Set<String> showLanguages = new HashSet<>();
			for (CheckboxItem langField : langFields) {
				showLanguages.add(langField.getAttribute("langKey"));
			}

			//通知
			owner.filterMessageItem(showLanguages, null);
		}

	}

	private class MessageItemListGrid extends ListGrid {

		private Map<String, String> enableLanguages;
		private List<ListGridField> languageFields = new ArrayList<>();

		//Filterのため、Recordは変数で保持
		private List<MessageItemListGridRecord> allRecords = new ArrayList<>();

		public MessageItemListGrid(Map<String, String> enableLanguages) {
			this.enableLanguages = enableLanguages;

			setWidth100();
			setHeight(1);

			setShowAllColumns(true);	// 列を全て表示
			setShowAllRecords(true);	// レコードを全て表示

			setOverflow(Overflow.VISIBLE);
			setBodyOverflow(Overflow.VISIBLE);

			setLeaveScrollbarGap(false);	//falseで縦スクロールバー領域が自動表示制御される

			setShowRowNumbers(true);		//行番号表示

			setCanResizeFields(true);	//列幅変更可
			setCanSort(false);			//ソート不可
			setCanGroupBy(false);		//Group化不可
			setCanPickFields(false);	//列の選択不可
			setCanAutoFitFields(false);	//列幅の自動調整不可(崩れるので)

			setCanDragRecordsOut(false);				//grid内でのD&Dでの並べ替え不可
			setCanAcceptDroppedRecords(false);
			setCanReorderRecords(false);

			setCanEdit(true);						//編集可
			setEditEvent(ListGridEditEvent.DOUBLECLICK);	//DoubleClickで編集開始
			setEditByCell(true);					//Cell単位で編集

			// 各フィールド初期化
			List<ListGridField> fields = new ArrayList<ListGridField>();

			ListGridField statusField = new ListGridField(MessageItemListGridRecord.STATUS, "*");
			statusField.setWidth(30);
			statusField.setAlign(Alignment.CENTER);
			statusField.setCanEdit(false);
			fields.add(statusField);

			ListGridField messageIdField = new ListGridField(MessageItemListGridRecord.MESSAGE_ID, "ID");
			messageIdField.setWidth(100);
			messageIdField.setCanEdit(true);

			fields.add(messageIdField);

			ListGridField defaultMessageField = new ListGridField(MessageItemListGridRecord.DEFAULT_MESSAGE, "Message");
			defaultMessageField.setCanEdit(true);
			defaultMessageField.setEscapeHTML(true);
			//改行も許可するためTextAreaで入力
			TextAreaItem defaultMessageInputField = new TextAreaItem();
			defaultMessageField.setEditorProperties(defaultMessageInputField);
			fields.add(defaultMessageField);

			//多言語Field
			for(Map.Entry<String, String> entry : enableLanguages.entrySet()) {
				ListGridField langMessageField = new ListGridField(entry.getKey(), entry.getValue());
				langMessageField.setCanEdit(true);
				langMessageField.setEscapeHTML(true);
				//改行も許可するためTextAreaで入力
				TextAreaItem langMessageInputField = new TextAreaItem();
				langMessageField.setEditorProperties(langMessageInputField);
				fields.add(langMessageField);

				languageFields.add(langMessageField);
			}

			addEditCompleteHandler(new EditCompleteHandler() {
				@Override
				public void onEditComplete(EditCompleteEvent event) {
					//変更があった場合のみEvent発生

					int rowNum = event.getRowNum();
					itemValueChanged(rowNum);
				}
			});

			// 各フィールドをListGridに設定
			setFields(fields.toArray(new ListGridField[0]));

		}

		@Override
	    protected String getBaseStyle(ListGridRecord record, int rowNum, int colNum) {
			MessageItemListGridRecord itemRecord = (MessageItemListGridRecord)record;

			//削除対象レコードのみカスタムスタイル設定
			String status = itemRecord.getStatus();
			if (status.equals(MessageItemListGridRecord.DELETE)) {
				return "deleteMessageItemGridRow";
			} else {
				return super.getBaseStyle(record, rowNum, colNum);
			}
		}

		public void setMessageItems(Map<String, MessageItem> messageItems) {

			allRecords.clear();
			if (messageItems != null) {
				//IDでソート
				List<MessageItem> sortedList = new ArrayList<>(messageItems.values());
				Collections.sort(sortedList, new Comparator<MessageItem>() {
					@Override
					public int compare(MessageItem o1, MessageItem o2) {
						String id1 = o1.getMessageId() != null ? o1.getMessageId() : "";
						String id2 = o2.getMessageId() != null ? o2.getMessageId() : "";
						return id1.compareTo(id2);
					}
				});

				for (MessageItem item : sortedList) {
					allRecords.add(new MessageItemListGridRecord(item));
				}
			}
			setData(allRecords.toArray(new MessageItemListGridRecord[0]));
		}

		public Map<String, MessageItem> getEditMessageItems() {

			if (allRecords.isEmpty()) {
				return null;
			}

			//IDでソートするためTreeMapに格納
			Map<String, MessageItem> items = new TreeMap<>();
			for (MessageItemListGridRecord itemRecord : allRecords) {

				String status = itemRecord.getStatus();
				if (status.equals(MessageItemListGridRecord.DELETE)) {
					continue;
				} else  {
					String messageId = itemRecord.getMessageId();
					//メッセージIDが未指定は除外
					if (SmartGWTUtil.isEmpty(messageId)) {
						continue;
					}
					MessageItem item = new MessageItem();
					item.setMessageId(messageId);
					item.setMessage(itemRecord.getMessage());

					if (!enableLanguages.isEmpty()) {
						List<LocalizedStringDefinition> localizedMessageList = new ArrayList<>();
						for (String localeName : enableLanguages.keySet()) {
							String localMessage = itemRecord.getAttribute(localeName);
							if (!SmartGWTUtil.isEmpty(localMessage)) {
								LocalizedStringDefinition localDefinition = new LocalizedStringDefinition();
								localDefinition.setLocaleName(localeName);
								localDefinition.setStringValue(localMessage);
								localizedMessageList.add(localDefinition);
							}
						}
						if (!localizedMessageList.isEmpty()) {
							item.setLocalizedMessageList(localizedMessageList);
						}
					}

					items.put(item.getMessageId(), item);
				}
			}

			if (items.isEmpty()) {
				return null;
			} else {
				return items;
			}
		}

		public void addMessageItem() {
			MessageItemListGridRecord itemRecord = new MessageItemListGridRecord(null);
			allRecords.add(itemRecord);
			addData(itemRecord);
		}

		public void removeSelectedMessageItem() {
			ListGridRecord[] records = getSelectedRecords();
			if (records == null || records.length == 0) {
				return;
			}

			for (ListGridRecord record : records) {
				MessageItemListGridRecord itemRecord = (MessageItemListGridRecord)record;

				String status = itemRecord.getStatus();
				if (status.equals(MessageItemListGridRecord.INSERT)) {
					//Insertデータは削除
					allRecords.remove(itemRecord);
					removeData(itemRecord);
				} else {
					//既存データはステータス変更
					itemRecord.setStatus(MessageItemListGridRecord.DELETE);
					refreshRow(getRowNum(itemRecord));
				}
			}
		}

		public void undoSelectedMessageItem() {
			ListGridRecord[] records = getSelectedRecords();
			if (records == null || records.length == 0) {
				return;
			}

			for (ListGridRecord record : records) {
				MessageItemListGridRecord itemRecord = (MessageItemListGridRecord)record;

				//新規以外は元に戻す
				String status = itemRecord.getStatus();
				if (!status.equals(MessageItemListGridRecord.INSERT)) {
					itemRecord.undo(enableLanguages);
					refreshRow(getRowNum(itemRecord));
				}
			}
		}

		public void filterMessageItem(Set<String> showLanguages, String filter) {

			//Language制御
			List<ListGridField> showFields = new ArrayList<>();
			List<ListGridField> hideFields = new ArrayList<>();
			for (ListGridField langField : languageFields) {
				if (showLanguages.contains(langField.getName())) {
					showFields.add(langField);
				} else {
					hideFields.add(langField);
				}
			}
			if (!showFields.isEmpty()) {
				showFields(showFields.toArray(new ListGridField[0]));
			}
			if (!hideFields.isEmpty()) {
				hideFields(hideFields.toArray(new ListGridField[0]));
			}

			//レコード制御
			if (!SmartGWTUtil.isEmpty(filter)) {
				List<MessageItemListGridRecord> filterRecords = new ArrayList<>();
				for (MessageItemListGridRecord itemRecord : allRecords) {
					if (itemRecord.getMessageId() != null && itemRecord.getMessageId().contains(filter)) {
						filterRecords.add(itemRecord);
						continue;
					}
					if (itemRecord.getMessage() != null && itemRecord.getMessage().contains(filter)) {
						filterRecords.add(itemRecord);
						continue;
					}
					if (!enableLanguages.isEmpty()) {
						for (String localeName : enableLanguages.keySet()) {
							String localMessage = itemRecord.getAttribute(localeName);
							if (localMessage != null && localMessage.contains(filter)) {
								filterRecords.add(itemRecord);
								break;
							}
						}
					}
				}
				setData(filterRecords.toArray(new MessageItemListGridRecord[0]));
			} else {
				setData(allRecords.toArray(new MessageItemListGridRecord[0]));
			}
		}

		private void itemValueChanged(int rowNum) {

			MessageItemListGridRecord record = (MessageItemListGridRecord)getRecord(rowNum);

			//変更がない状態のもののみ変更ありに更新(オリジナルとの値チェックまではしない)
			String status = record.getStatus();
			if (status.equals(MessageItemListGridRecord.NO_CHANGE)) {
				record.setStatus(MessageItemListGridRecord.UPDATE);
			}
		}

	}

	private class MessageItemListGridRecord extends ListGridRecord {

		/** ステータス */
		public static final String STATUS = "status";
		/** メッセージID */
		public static final String MESSAGE_ID = "messageId";
		/** メッセージ */
		public static final String DEFAULT_MESSAGE = "defaultMessage";

		public static final String INSERT = "I";
		public static final String UPDATE = "U";
		public static final String DELETE = "D";
		public static final String NO_CHANGE = "";

		private MessageItem original;

		public MessageItemListGridRecord(MessageItem item) {
			this.original = item;

			if (item == null) {
				setStatus(INSERT);
			} else {
				applyItem();
			}
		}

		private void applyItem() {
			if (original != null) {
				setMessageId(original.getMessageId());
				setMessage(original.getMessage());
				setLocalizedMessage(original.getLocalizedMessageList());

				setStatus(NO_CHANGE);
			}
		}

		private void setLocalizedMessage(List<LocalizedStringDefinition> localizedList) {
			if (localizedList != null) {
				for (LocalizedStringDefinition local : localizedList) {
					setAttribute(local.getLocaleName(), local.getStringValue());
				}
			}
		}
		private void clearLocalizedMessage(Map<String, String> enableLanguages) {
			for (String locale : enableLanguages.keySet()) {
				setAttribute(locale, (String)null);
			}
		}

		public void setStatus(String status) {
			setAttribute(STATUS, status);
		}
		public String getStatus() {
			return getAttribute(STATUS);
		}

		public void setMessageId(String messageId) {
			setAttribute(MESSAGE_ID, messageId);
		}
		public String getMessageId() {
			return getAttribute(MESSAGE_ID);
		}

		public void setMessage(String message) {
			setAttribute(DEFAULT_MESSAGE, message);
		}
		public String getMessage() {
			return getAttribute(DEFAULT_MESSAGE);
		}

		public void undo(Map<String, String> enableLanguages) {
			clearLocalizedMessage(enableLanguages);
			applyItem();
		}

	}

}
