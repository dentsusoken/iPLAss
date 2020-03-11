/*
 * Copyright (C) 2020 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
package org.iplass.adminconsole.client.metadata.ui.webhook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import org.iplass.mtp.webhook.template.definition.WebHookHeaderDefinition;
import org.iplass.mtp.webhook.template.definition.WebHookTemplateDefinition;
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
import com.smartgwt.client.widgets.form.fields.SelectItem;
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
public class WebHookTemplateEditPane extends MetaDataMainEditPane {

	
	private enum HEADER_FIELD_NAME{
		HEADERNAME,
		HEADERVALUE,
	}
	
	private final MetaDataServiceAsync service;
	
	/** 編集対象 */
	private WebHookTemplateDefinition curDefinition;
	private int curVersion;
	private String curDefinitionId;
	private MetaCommonHeaderPane headerPane;
	
	
	
	/** 共通属性 */
	private MetaCommonAttributeSection<WebHookTemplateDefinition> commonSection;
	
	/** 個別属性部分（デフォルト） */
	private WebHookTemplateAttributePane webHookTemplateAttrPane;
	
	public WebHookTemplateEditPane(MetaDataItemMenuTreeNode targetNode, DefaultMetaDataPlugin plugin) {
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
		
		commonSection = new MetaCommonAttributeSection<>(targetNode, WebHookTemplateDefinition.class);
		
		webHookTemplateAttrPane = new WebHookTemplateAttributePane();
		
		SectionStackSection defaultWebHookSection = createSection("Default WebHookTemplate", webHookTemplateAttrPane);		
		setMainSections(commonSection, defaultWebHookSection);
		addMember(headerPane);
		addMember(mainStack);
		
		initializeData();
	}

	/**
	 * データ初期化処理
	 */
	private void initializeData() {
		//エラーのクリア
		commonSection.clearErrors();
		webHookTemplateAttrPane.clearErrors();

		service.getDefinitionEntry(TenantInfoHolder.getId(), WebHookTemplateDefinition.class.getName(), defName, new AsyncCallback<DefinitionEntry>() {

			@Override
			public void onFailure(Throwable caught) {
				SC.say(AdminClientMessageUtil.getString("ui_metadata_webhook_WebHookTemplateEditPane_failed"),
						AdminClientMessageUtil.getString("ui_metadata_webhook_WebHookTemplateEditPane_failedGetScreenInfo"));

				GWT.log(caught.toString(), caught);
			}

			@Override
			public void onSuccess(DefinitionEntry result) {

				//画面に反映
				setDefinition(result);
			}

		});

		//ステータスチェック
		StatusCheckUtil.statuCheck(WebHookTemplateDefinition.class.getName(), defName, this);
	}
	
	/**
	 * Definition画面設定内容入り
	 *
	 * @param definition 編集対象
	 */
	protected void setDefinition(DefinitionEntry entry) {
		
		this.curDefinition = (WebHookTemplateDefinition) entry.getDefinition();
		this.curVersion = entry.getDefinitionInfo().getVersion();
		this.curDefinitionId = entry.getDefinitionInfo().getObjDefId();
		
		commonSection.setDefinition(curDefinition);
		webHookTemplateAttrPane.setDefinition(curDefinition);
		
	}

	/**
	 * 更新処理
	 *
	 * @param definition 更新対象
	 */
	private void updateWebHookTemplate(final WebHookTemplateDefinition definition, boolean checkVersion) {
		SmartGWTUtil.showSaveProgress();
		service.updateDefinition(TenantInfoHolder.getId(), definition, curVersion, checkVersion, new MetaDataUpdateCallback() {

			@Override
			protected void overwriteUpdate() {
				updateWebHookTemplate(definition, false);
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
	private void updateComplete(WebHookTemplateDefinition definition) {
		SC.say(AdminClientMessageUtil.getString("ui_metadata_webhook_WebHookTemplateEditPane_completion"),
				AdminClientMessageUtil.getString("ui_metadata_webhook_WebHookTemplateEditPane_saveWebHookTemplate"));

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
	
	public class WebHookTemplateAttributePane extends VLayout {
		
		//テンプレに関する情報
		private DynamicForm templateInfoForm;
		private TextItem contentTypeField;
        private TextItem tokenNameField;
		
        //ヘッダー関連
		public HeaderMapGrid headerGrid;
		
		//url query
		private DynamicForm urlQueryForm;
		private TextAreaItem urlQueryField;
		
		//送る情報を編集
		private TabSet messageTabSet;
		//request method
		private SelectItem webHookMethodField; 
		private Tab plainContentTab;
		private ScriptEditorPane plainEditor;
		
		public WebHookTemplateAttributePane () {
			setOverflow(Overflow.AUTO);
			
			VLayout mainPane = new VLayout();
			mainPane.setMargin(5);
			mainPane.setMembersMargin(5);
			
			HLayout topPane = new HLayout();
			topPane.setMembersMargin(5);
			topPane.setHeight(200);
			
			VLayout headerPane = new VLayout();
			headerPane.setMargin(5);
			headerPane.setMembersMargin(5);
			headerPane.setWidth(500);
			headerPane.setHeight100();

			templateInfoForm = new DynamicForm();
			templateInfoForm.setWidth(300);
			templateInfoForm.setPadding(10);
			templateInfoForm.setNumCols(2);
			templateInfoForm.setColWidths(100,"*");
			templateInfoForm.setIsGroup(true);
			templateInfoForm.setGroupTitle("Template Settings");
			contentTypeField = new TextItem("webhookContentType", "Content-Type");
			contentTypeField.setWidth(150);
			SmartGWTUtil.addHoverToFormItem(contentTypeField, AdminClientMessageUtil.getString("ui_metadata_webhook_WebHookTemplateEditPane_contentTypeFieldHoverInfo"));
			tokenNameField = new TextItem("webhookTokenName", "Security Token Name");
			tokenNameField.setWidth(150);
			SmartGWTUtil.addHoverToFormItem(tokenNameField, AdminClientMessageUtil.getString("ui_metadata_webhook_WebHookTemplateEditPane_tokenNameFieldHoverInfo"));

			webHookMethodField = new SelectItem("webHookMethodField","Http Request Method");
			webHookMethodField.setValueMap("GET", "POST", "DELETE", "PUT","PATCH","HEAD","OPTIONS","TRACE");
			webHookMethodField.setWidth(150);

			headerGrid = new HeaderMapGrid();
			headerGrid.setHeight100();
			headerGrid.setTitle(AdminClientMessageUtil.getString("ui_metadata_webhook_WebHookTemplateEditPane_headerGridTitle"));
			headerGrid.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
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
					ListGridRecord record = headerGrid.getSelectedRecord();
					if (record == null) {
						return;
					}
					headerGrid.removeSelectedData();

					ArrayList<WebHookHeaderDefinition> headers = new ArrayList<WebHookHeaderDefinition>();
					for (ListGridRecord  records: headerGrid.getRecords()) {
						headers.add(new WebHookHeaderDefinition(records.getAttribute(HEADER_FIELD_NAME.HEADERNAME.name()),records.getAttribute(HEADER_FIELD_NAME.HEADERVALUE.name())));
					}
					curDefinition.setHeaders(headers);
				}
			});
			
			HLayout mapButtonPane = new HLayout(5);
			mapButtonPane.setMargin(5);
			mapButtonPane.addMember(addMap);
			mapButtonPane.addMember(delMap);
			mapButtonPane.setWidth100();
			
			headerPane.addMember(headerGrid);
			headerPane.addMember(mapButtonPane);
			headerPane.setIsGroup(true);
			headerPane.setPadding(10);
			headerPane.setGroupTitle("Custom Header");
			
			templateInfoForm.setItems(contentTypeField, tokenNameField,webHookMethodField);
			
			urlQueryForm = new DynamicForm();
			urlQueryForm.setWidth(500);
			urlQueryForm.setPadding(10);
			urlQueryForm.setNumCols(1);
			urlQueryForm.setIsGroup(true);
			urlQueryForm.setGroupTitle("Url Query String");
			urlQueryField = new TextAreaItem();
			urlQueryField.setPrompt(AdminClientMessageUtil.getString("ui_metadata_webhook_WebHookTemplateEditPane_urlQueryFieldHoverInfo"));
			urlQueryField.setWidth("*");
			urlQueryField.setHeight(180);
			urlQueryField.setShowTitle(false);
			urlQueryForm.setItems(urlQueryField);
			
			topPane.addMember(templateInfoForm);
			topPane.addMember(headerPane);
			topPane.addMember(urlQueryForm);
			
			
			
			messageTabSet = new TabSet();
			messageTabSet.setWidth100();
			messageTabSet.setHeight(550);
			messageTabSet.setPaneContainerOverflow(Overflow.HIDDEN);	
			plainContentTab = new Tab();
			plainContentTab.setPrompt(AdminClientMessageUtil.getString("ui_metadata_webhook_WebHookTemplateEditPane_webhookContentTabHoverInfo"));
			plainContentTab.setTitle(AdminClientMessageUtil.getString("ui_metadata_webhook_WebHookTemplateEditPane_webhookContentTabTitle"));
			
			plainEditor = new ScriptEditorPane();
			plainEditor.setMode(EditorMode.TEXT);
			plainContentTab.setPane(plainEditor);
	        messageTabSet.addTab(plainContentTab);
	        
	        mainPane.addMember(topPane);
	        mainPane.addMember(messageTabSet);
			
	        addMember(mainPane);
	        
	        
		}
		protected void addMap() {
			editMap(null);
		}

		protected void editMap(final ListGridRecord record) {
			WebHookHeaderDefinition temp = null;
			
			//dialog のためのtemp
			if (record == null) {

			} else {
				temp = new WebHookHeaderDefinition(
						record.getAttributeAsString(HEADER_FIELD_NAME.HEADERNAME.name()),
						record.getAttributeAsString(HEADER_FIELD_NAME.HEADERVALUE.name()));
			}
			final WebHookHeaderDialog dialog = new WebHookHeaderDialog(temp);
			dialog.addDataChangeHandler(new DataChangedHandler() {
				@Override
				public void onDataChanged(DataChangedEvent event) {
					WebHookHeaderDefinition param = event.getValueObject(WebHookHeaderDefinition.class);
					if (record != null) {//既存
						headerGrid.removeData(record);	
					}
					ArrayList<WebHookHeaderDefinition> headers = new ArrayList<WebHookHeaderDefinition>();
					for (ListGridRecord  records: headerGrid.getRecords()) {
						headers.add(new WebHookHeaderDefinition(records.getAttribute(HEADER_FIELD_NAME.HEADERNAME.name()),records.getAttribute(HEADER_FIELD_NAME.HEADERVALUE.name())));
					}
					headers.add(param);
					curDefinition.setHeaders(headers);
					ListGridRecord[] temp = getHeaderRecordList(curDefinition);
					headerGrid.setData(temp);
					headerGrid.refreshFields();
				}
			});
			dialog.show();
			
		}
		
		protected ListGridRecord createRecord(WebHookHeaderDefinition param, ListGridRecord record, boolean init) {
			if (record == null) {
				record = new ListGridRecord();

				if (!init) {
					curDefinition.addHeaders(param);
				}
			} else {
				ArrayList<WebHookHeaderDefinition>_headers = curDefinition.getHeaders();
				HashMap<String, WebHookHeaderDefinition> tempMap = new HashMap<String, WebHookHeaderDefinition>();
				if (_headers !=null) {
					for (WebHookHeaderDefinition entry :_headers) {
						tempMap.put(entry.getKey(), entry);
					}
				}
				tempMap.remove(param.getKey());
				tempMap.put(param.getKey(), param);
				ArrayList<WebHookHeaderDefinition> newList = new ArrayList<WebHookHeaderDefinition>(); 
				for (WebHookHeaderDefinition headerEntry: tempMap.values()) {
					newList.add(headerEntry);
				}
				curDefinition.setHeaders(newList);
			}
			record.setAttribute(HEADER_FIELD_NAME.HEADERNAME.name(), param.getKey());
			record.setAttribute(HEADER_FIELD_NAME.HEADERVALUE.name(), param.getValue());
			return record;
		}
		public boolean validate() {
			return true;
		}

		public void clearErrors() {
//			templateInfoForm.clearErrors(true);
//			contentTypeField.clearErrors();
//	        tokenNameField.clearErrors();
//	        webHookMethodField.clearErrors();
	    }

		//pane -> definition
		public WebHookTemplateDefinition getEditDefinition(WebHookTemplateDefinition definition) {
			
			definition.setContentType(SmartGWTUtil.getStringValue(contentTypeField));
			definition.setWebHookContent(plainEditor.getText());
			definition.setTokenHeader(SmartGWTUtil.getStringValue(tokenNameField));
			definition.setHttpMethod(SmartGWTUtil.getStringValue(webHookMethodField));
			definition.setUrlQuery(SmartGWTUtil.getStringValue(urlQueryField));

			return definition;
		}
	
		//Definition -> Pane
		public void setDefinition(WebHookTemplateDefinition definition) {
			headerGrid.setData(new ListGridRecord[] {});
			if (definition != null) {
				contentTypeField.setValue(definition.getContentType());
				tokenNameField.setValue(definition.getTokenHeader());
				webHookMethodField.setValue(definition.getHttpMethod());
				if (definition.getWebHookContent()==null) {
					plainEditor.setText("");
				} else {
					plainEditor.setText(definition.getWebHookContent());
				}
				
				ListGridRecord[] temp = getHeaderRecordList(definition);
				headerGrid.setData(temp);
				urlQueryField.setValue(definition.getUrlQuery());
			} else {
				contentTypeField.clearValue();
				tokenNameField.clearValue();
				webHookMethodField.clearValue();
				messageTabSet.selectTab(plainContentTab);
				urlQueryField.clearErrors();
			}
		}
		/**
		 * @param WebHookTemplateDefinition 
		 * @return ListGridRecord[] of headers
		 */
		private ListGridRecord[] getHeaderRecordList(WebHookTemplateDefinition definition) {
			List<WebHookHeaderDefinition> definitionList = definition.getHeaders();
			ListGridRecord[] temp = new ListGridRecord[definitionList.size()];

			int cnt = 0;
			for (WebHookHeaderDefinition header : definitionList) {
				ListGridRecord newRecord = createRecord(header, null, true);
				temp[cnt] = newRecord;
				cnt ++;
			}
			return temp;
		}
	}


	public class HeaderMapGrid extends ListGrid{
		public HeaderMapGrid() {
			setWidth(500);
			setHeight(1);

			setShowAllColumns(true);
			setShowAllRecords(true);
			setCanResizeFields(true);
			setCanSort(true);	
			setCanPickFields(false);
			setCanGroupBy(false);
			setAutoFitWidthApproach(AutoFitWidthApproach.BOTH);	
			setLeaveScrollbarGap(false);
			setBodyOverflow(Overflow.VISIBLE);
			setOverflow(Overflow.VISIBLE);

			ListGridField headerNameField = new ListGridField(HEADER_FIELD_NAME.HEADERNAME.name(), "Name Key");
			ListGridField headerValueField = new ListGridField(HEADER_FIELD_NAME.HEADERVALUE.name(), "Value");
			
			setFields(headerNameField, headerValueField);
		}
	}
	
	
	/**
	 * 保存ボタンイベント
	 */
	private final class SaveClickHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			boolean commonValidate = commonSection.validate();
			boolean webHookValidate = webHookTemplateAttrPane.validate();
			if (!commonValidate || !webHookValidate) {
				return;
			}
			SC.ask(AdminClientMessageUtil.getString("ui_metadata_webhook_WebHookTemplateEditPane_saveConfirm"),
					AdminClientMessageUtil.getString("ui_metadata_webhook_WebHookTemplateEditPane_saveWebHookTemplateComment"), new BooleanCallback() {

				@Override
				public void execute(Boolean value) {
					if (value) {
						WebHookTemplateDefinition definition = curDefinition;
						definition = commonSection.getEditDefinition(definition);
						definition = webHookTemplateAttrPane.getEditDefinition(definition);

						updateWebHookTemplate(definition, true);
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
			SC.ask(AdminClientMessageUtil.getString("ui_metadata_webhook_WebHookTemplateEditPane_cancelConfirm"),
					AdminClientMessageUtil.getString("ui_metadata_webhook_WebHookTemplateEditPane_cancelConfirmComment")
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
