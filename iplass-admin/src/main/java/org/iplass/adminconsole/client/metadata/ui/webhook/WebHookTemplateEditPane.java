package org.iplass.adminconsole.client.metadata.ui.webhook;

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
import org.iplass.mtp.webhook.template.definition.WebHookContent;
import org.iplass.mtp.webhook.template.definition.WebHookHeader;
import org.iplass.mtp.webhook.template.definition.WebHookSubscriber;
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
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.IntegerItem;
import com.smartgwt.client.widgets.form.fields.RadioGroupItem;
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
import com.smartgwt.client.widgets.form.validator.CustomValidator;
public class WebHookTemplateEditPane extends MetaDataMainEditPane {

	private enum FIELD_NAME {
		SUBSCRIBER,
		SUBSCRIBERURL,
		SECURITYUSERNAME,
		SECURITYPASSWORD,
		SECURITYTOKEN,
		
	}
	
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

	private WebHookTemplateSubscriberPane webHookTemplateSubscriberPane;
	
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
		webHookTemplateSubscriberPane = new WebHookTemplateSubscriberPane();
		
		//TODO subscribe した方のリストも一つsectionにするかと
		SectionStackSection defaultWebHookSection = createSection("Default WebHookTemplate", webHookTemplateAttrPane);		
		SectionStackSection subScrioberSection = createSection("Subscribers", webHookTemplateSubscriberPane);
		setMainSections(commonSection, defaultWebHookSection, subScrioberSection);
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
			public void onFailure(Throwable caught) {//TODO add these message
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
		webHookTemplateSubscriberPane.setDefinition(curDefinition);
		
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
		//TODO add message
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
		private TextItem charsetField;//ほぼ常にutf-8
		private RadioGroupItem contentTypeField;//Json,xml,formなど
        private CheckboxItem isSynchronous;
		
        //ヘッダー関連
        //private DynamicForm headerForm;//TODO:!
		public HeaderMapGrid headerGrid;
		
		//リトライ関連
		private DynamicForm retryForm;
		private IntegerItem retryLimitField;
		private IntegerItem retryIntervalField;
		private CheckboxItem isRetryField;
		
		//送る情報を編集
		private TabSet messageTabSet;
		
		private Tab plainContentTab;
		private ScriptEditorPane plainEditor;
		
		//TODO:check
		/**forget why I set urlconfig here.
		private Tab urlConfigTab; 
		private ScriptEditorPane urlEditor;
		*/
		
		
				
		public WebHookTemplateAttributePane () {
			setOverflow(Overflow.AUTO);//FIXME: overflow必要ないgrid/paneを探して修正してください
			
			VLayout mainPane = new VLayout();
			mainPane.setMargin(5);
			mainPane.setMembersMargin(5);
			
			HLayout topPane = new HLayout();
			topPane.setMembersMargin(5);
			
			VLayout headerPane = new VLayout();
			headerPane.setMargin(5);
			headerPane.setMembersMargin(5);
			headerPane.setWidth(500);
			headerPane.setHeight100();
			
			retryForm = new DynamicForm();
			retryForm.setWidth(270);
			retryForm.setPadding(10);
			retryForm.setNumCols(2);
			retryForm.setColWidths(100,"*");
			retryForm.setIsGroup(true);
			retryForm.setGroupTitle("Retry");
			
			//TODO: add the message to AdminClientMessageUtil
			isRetryField = new CheckboxItem("webhookIsRetry", "Enable Retry");//AdminClientMessageUtil.getString("ui_metadata_webhook_WebHookTemplateEditPane_isRetry")
			isRetryField.setColSpan(2);
			SmartGWTUtil.addHoverToFormItem(isRetryField, "Whether to resend the message if the inital attempt fails or receives unsuccessful response");//AdminClientMessageUtil.getString("ui_metadata_webhook_WebHookTemplateEditPane_isRetryTooltip")
			isRetryField.setWidth(150);
			
			retryLimitField = new IntegerItem("webhookRetryLimit", "Maximum Retry Attempts");//AdminClientMessageUtil.getString("ui_metadata_webhook_WebHookTemplateEditPane_retryLimit")
			retryLimitField.setWidth(150);
			
			retryIntervalField =  new IntegerItem("webhookRetryInterval", "Retry Interval(ms)");//AdminClientMessageUtil.getString("ui_metadata_webhook_WebHookTemplateEditPane_retryInterval")
			SmartGWTUtil.addHoverToFormItem(retryIntervalField, "The waiting time between each attempt of retry");//AdminClientMessageUtil.getString("ui_metadata_webhook_WebHookTemplateEditPane_retryIntervalTooltip")
			retryIntervalField.setWidth(150);
			retryIntervalField.setKeyPressFilter("^[0-9]*$");
			
			retryForm.setItems(isRetryField, retryLimitField, retryIntervalField);
			
			
			templateInfoForm = new DynamicForm();
			templateInfoForm.setWidth(300);
			templateInfoForm.setPadding(10);
			templateInfoForm.setNumCols(2);
			templateInfoForm.setColWidths(100,"*");
			templateInfoForm.setIsGroup(true);
			templateInfoForm.setGroupTitle("Template Settings");
			
			isSynchronous = new CheckboxItem("webhookIsSynchronous", "Synchronous");//add message
			isSynchronous.setColSpan(2);
			isSynchronous.setWidth(150);
			SmartGWTUtil.addHoverToFormItem(isSynchronous, "Whether to resend the message in synchronous mode");//AdminClientMessageUtil.getString("ui_metadata_webhook_WebHookTemplateEditPane_isSynchronous")
			
			charsetField = new TextItem("webhookCharset", "charset");//add message
			charsetField.setWidth(150);
			
			contentTypeField = new RadioGroupItem(); 
			contentTypeField.setShowTitle(false);
			contentTypeField.setValueMap("JSON", "XML", "FORM", "PLAINTEXT", "MULTIPART");  
			contentTypeField.setDefaultValue("JSON");
			
//			headerForm = new DynamicForm();
//			headerForm.setWidth100();
//			headerForm.setNumCols(4);
//			headerForm.setColWidths(100, "*", "*", "*");
			
			
			
			headerGrid = new HeaderMapGrid();
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
					String _headerName = record.getAttribute(HEADER_FIELD_NAME.HEADERNAME.name());
					headerGrid.removeSelectedData();

					ArrayList<WebHookHeader> _headers = new ArrayList<WebHookHeader>();

					for (WebHookHeader hd : curDefinition.getHeaders() ) {
						String _key = hd.getKey();
						if (_key.equals(_headerName)) {
							continue;
						}
						String _value = hd.getValue();
						WebHookHeader temp= new WebHookHeader(_key, _value);
						_headers.add(temp);
					}
					curDefinition.setHeaders(_headers);
				}
			});
			
			HLayout mapButtonPane = new HLayout(5);
			mapButtonPane.setMargin(5);
			mapButtonPane.addMember(addMap);
			mapButtonPane.addMember(delMap);
			mapButtonPane.setWidth100();
			
			headerPane.addMember(headerGrid);
			headerPane.addMember(mapButtonPane);
			
			templateInfoForm.setItems(isSynchronous, charsetField, contentTypeField);
			
			topPane.addMember(templateInfoForm);
			topPane.addMember(retryForm);
			topPane.addMember(headerPane);
			
			messageTabSet = new TabSet();
			messageTabSet.setWidth100();
			messageTabSet.setHeight(450);
			messageTabSet.setPaneContainerOverflow(Overflow.HIDDEN);	
			
			plainContentTab = new Tab();
			plainContentTab.setTitle("Webhook Content");//add message
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

		protected void editMap(final ListGridRecord record) {//FIXME　同じヘッダー複数にならないようにしてください。
			WebHookHeader temp = null;
			
			//dialog のためのtemp
			if (record == null) {

			} else {
				temp = new WebHookHeader(
						record.getAttributeAsString(HEADER_FIELD_NAME.HEADERNAME.name()),
						record.getAttributeAsString(HEADER_FIELD_NAME.HEADERVALUE.name()));
			}
			final WebHookHeaderDialog dialog = new WebHookHeaderDialog(temp, curDefinition.getHeaders());
			//FIXME: no effect
//			dialog.setValidatorForHeader(
//					new CustomValidator() {
//
//						@Override
//						protected boolean condition(Object value) {
//							for (WebHookHeader wh: curDefinition.getHeaders()) {
//								if (value == wh.getKey()) {
//									return false;
//								}
//							}
//							return true;
//						}
//					}
//					);
			dialog.addDataChangeHandler(new DataChangedHandler() {
				@Override
				public void onDataChanged(DataChangedEvent event) {
					WebHookHeader param = event.getValueObject(WebHookHeader.class);
					if (record != null) {
						ArrayList<WebHookHeader>_headers = curDefinition.getHeaders();
						HashMap<String, WebHookHeader> tempMap = new HashMap<String, WebHookHeader>();
						for (WebHookHeader entry :_headers) {
							if (entry.getKey()==param.getKey()
									||entry.getKey()==null
									||entry.getKey().replaceAll("\\s+","").isEmpty()) {
								continue;
							}

							tempMap.put(entry.getKey(), entry);
						} 
						
						if (param.getKey()!=null
								&&!param.getKey().replaceAll("\\s+","").isEmpty()) {
							tempMap.put(param.getKey(), param);
						}

						ArrayList<WebHookHeader> newList = new ArrayList<WebHookHeader>(); 
						for (WebHookHeader headerEntry: tempMap.values()) {
							newList.add(headerEntry);
						}
						curDefinition.setHeaders(newList);
					} else {
						ArrayList<WebHookHeader>_headers = curDefinition.getHeaders();
						HashMap<String, WebHookHeader> tempMap = new HashMap<String, WebHookHeader>();
						for (WebHookHeader entry :_headers) {
							if (entry.getKey()==param.getKey()
									||entry.getKey()==null
									||entry.getKey().replaceAll("\\s+","").isEmpty()) {
								continue;
							}	
							tempMap.put(entry.getKey(), entry);
						} 
						tempMap.remove(param.getKey());
						
						if (param.getKey()!=null
								&&!param.getKey().replaceAll("\\s+","").isEmpty()) {
							tempMap.put(param.getKey(), param);
						}
						
						ArrayList<WebHookHeader> newList = new ArrayList<WebHookHeader>(); 
						for (WebHookHeader headerEntry: tempMap.values()) {
							newList.add(headerEntry);
						}
						curDefinition.setHeaders(newList);
						
						//headerGrid.addData(newRecord);
					}
					ListGridRecord[] temp = getHeaderRecordList(curDefinition);
					headerGrid.setData(temp);
					headerGrid.refreshFields();
				}
			});
		
			dialog.show();
			
		}
		protected ListGridRecord createRecord(WebHookHeader param, ListGridRecord record, boolean init) {
			if (record == null) {
				record = new ListGridRecord();

				if (!init) {
					curDefinition.addHeaders(param);
				}
			} else {
				ArrayList<WebHookHeader>_headers = curDefinition.getHeaders();
				HashMap<String, WebHookHeader> tempMap = new HashMap<String, WebHookHeader>();
				for (WebHookHeader entry :_headers) {
					tempMap.put(entry.getKey(), entry);
				} 
				tempMap.remove(param.getKey());
				tempMap.put(param.getKey(), param);
				ArrayList<WebHookHeader> newList = new ArrayList<WebHookHeader>(); 
				for (WebHookHeader headerEntry: tempMap.values()) {
					newList.add(headerEntry);
				}
				curDefinition.setHeaders(newList);
			}
			record.setAttribute(HEADER_FIELD_NAME.HEADERNAME.name(), param.getKey());
			record.setAttribute(HEADER_FIELD_NAME.HEADERVALUE.name(), param.getValue());
			return record;
		}
		public boolean validate() {
			// TODO add input related forms
			return true;
		}

		public void clearErrors() {
			// TODO add input related forms
			
		}

		//pane -> definition
		public WebHookTemplateDefinition getEditDefinition(WebHookTemplateDefinition definition) {
			
			WebHookContent wc = new WebHookContent(
					plainEditor.getText(), 
					SmartGWTUtil.getStringValue(contentTypeField), 
					SmartGWTUtil.getStringValue(charsetField)
					);
			definition.setContentBody(wc);
			
			
			definition.setRetryInterval(SmartGWTUtil.getIntegerValue(retryIntervalField)!=null?SmartGWTUtil.getIntegerValue(retryIntervalField):0);
			definition.setRetryLimit(SmartGWTUtil.getIntegerValue(retryLimitField)!=null?SmartGWTUtil.getIntegerValue(retryLimitField):0);
			definition.setRetry(SmartGWTUtil.getBooleanValue(isRetryField));
			definition.setSynchronous(SmartGWTUtil.getBooleanValue(isSynchronous));
			//TODO header
			return definition;
		}
	
		//Definition -> Pane
		public void setDefinition(WebHookTemplateDefinition definition) {
			headerGrid.setData(new ListGridRecord[] {});
			if (definition != null) {
				charsetField.setValue(definition.getContentBody().getCharset());
				isSynchronous.setValue(definition.isSynchronous());
				retryLimitField.setValue(definition.getRetryLimit());
				retryIntervalField.setValue(definition.getRetryInterval());
				isRetryField.setValue(definition.isRetry());
				contentTypeField.setValue(definition.getContentBody().getContentTypeString().toString());
				if (definition.getContentBody().getContent()==null) {
					plainEditor.setText("");
				} else {
					plainEditor.setText(definition.getContentBody().getContent());
				}
				
				ListGridRecord[] temp = getHeaderRecordList(definition);
				headerGrid.setData(temp);
				
			} else {
				charsetField.clearValue();
				contentTypeField.clearValue();
				isSynchronous.clearValue();
				retryLimitField.clearValue();
				retryIntervalField.clearValue();
				isRetryField.clearValue();
				
				messageTabSet.selectTab(plainContentTab);
			}
		}
		/**
		 * @param WebHookTemplateDefinition 
		 * @return ListGridRecord[] of headers
		 */
		private ListGridRecord[] getHeaderRecordList(WebHookTemplateDefinition definition) {
			List<WebHookHeader> definitionList = definition.getHeaders();
			ListGridRecord[] temp = new ListGridRecord[definitionList.size()];

			int cnt = 0;
			for (WebHookHeader header : definitionList) {
				ListGridRecord newRecord = createRecord(header, null, true);
				temp[cnt] = newRecord;
				cnt ++;
			}
			return temp;
		}
	}
	

	
	/**
	 * 
	 * 当フックをサブスクライブした方のリスト
	 * この方が管理し易いかと
	 * */
	public class WebHookTemplateSubscriberPane extends VLayout {

		//セキュリティ設定、サブのそれぞれ対応
		
		//private SelectItem securityMethodField;//bearer,BASIC,
		
		//private CheckboxItem isPublic;//サブスクライブを公開するかどうか。TODO：公開サブ、今後作る
		//private DynamicForm subscriberForm;
		public SubscriberMapGrid grid;
		
		
		public WebHookTemplateSubscriberPane() {
			setMembersMargin(5);
			
//			subscriberForm = new DynamicForm();
//			subscriberForm.setWidth100();
//			subscriberForm.setNumCols(4);
//			subscriberForm.setColWidths(100, "*", "*", "*");
//			subscriberForm.setHeight(400);
			
			
			grid = new SubscriberMapGrid();
			grid.setWidth100();
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
					ListGridRecord record = grid.getSelectedRecord();
					if (record == null) {
						return;
					}
					//TODO: decide how to determine and distinguish , right now use both url and subname
					String url = record.getAttribute(FIELD_NAME.SUBSCRIBERURL.name());
					String subscriberName = record.getAttribute(FIELD_NAME.SUBSCRIBER.name());
					grid.removeSelectedData();

					ArrayList<WebHookSubscriber> subscriberList = new ArrayList<WebHookSubscriber>();

					for (WebHookSubscriber sub : curDefinition.getSubscribers() ) {
						if (sub.getUrl().equals(url)) {
							if (sub.getSubscriberName().equals(subscriberName)) {
								continue;
							}
						}
						subscriberList.add(sub);
					}
					curDefinition.setSubscribers(subscriberList);
				}
			});
			
			IButton testHook = new IButton("TestHook");
			addMap.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					//add handler!! TODO:
				}
			});
			
			HLayout mapButtonPane = new HLayout(5);
			mapButtonPane.setMargin(5);
			mapButtonPane.addMember(addMap);
			mapButtonPane.addMember(delMap);
			mapButtonPane.addMember(testHook);
			
			addMember(grid);
			addMember(mapButtonPane);
			
		}
		
		protected void addMap() {
			editMap(null);
		}

		protected void editMap(final ListGridRecord record) {
			WebHookSubscriber temp = null;
			
			//dialog のためのtemp
			if (record == null) {

			} else {
				String _subscriberURL=record.getAttributeAsString(FIELD_NAME.SUBSCRIBERURL.name());
				String _subscriberName=record.getAttributeAsString(FIELD_NAME.SUBSCRIBER.name());
				temp =curDefinition.getSubscriberByNameURL(_subscriberName, _subscriberURL);
			}
			final WebHookSubscriberDialog dialog = new WebHookSubscriberDialog(temp);
			dialog.addDataChangeHandler(new DataChangedHandler() {
				@Override
				public void onDataChanged(DataChangedEvent event) {
					WebHookSubscriber param = event.getValueObject(WebHookSubscriber.class);
					if (record != null) {
						ArrayList<WebHookSubscriber> subscriberList = new ArrayList<WebHookSubscriber>();
						String _subscriberURL=record.getAttributeAsString(FIELD_NAME.SUBSCRIBERURL.name());
						String _subscriberName=record.getAttributeAsString(FIELD_NAME.SUBSCRIBER.name());
						for (WebHookSubscriber sub : curDefinition.getSubscribers() ) {
							if (sub.getUrl().equals(_subscriberURL)) {
								if (sub.getSubscriberName().equals(_subscriberName)) {
									continue;
								}
							}
							subscriberList.add(sub);
						}
						subscriberList.add(param);
						curDefinition.setSubscribers(subscriberList);
					} else {
						//追加
						curDefinition.getSubscribers().add(param);
					}
					grid.setData(getSubscriberRecordList(curDefinition));
					grid.refreshFields();
				}
			});
		
			dialog.show();
			
		}

		public boolean validate() {
			// TODO Auto-generated method stub
			return true;
		}

		public void clearErrors() {
			// TODO Auto-generated method stub
			
		}

		public WebHookTemplateDefinition getEditDefinition(WebHookTemplateDefinition definition) {
			return definition;
		}
	
		public void setDefinition(WebHookTemplateDefinition definition) {
			grid.setData(new ListGridRecord[] {});
			
			if (definition != null) {
				grid.setData(getSubscriberRecordList(definition));
			} 
		}

		private ListGridRecord[] getSubscriberRecordList(WebHookTemplateDefinition definition) {
			List<WebHookSubscriber> definitionList = definition.getSubscribers();
			if (definitionList != null && definitionList.size() > 0) {
				ListGridRecord[] temp = new ListGridRecord[definitionList.size()];

				int cnt = 0;
				for (WebHookSubscriber subscriber : definitionList) {
					ListGridRecord newRecord = createRecord(subscriber, null, true);
					temp[cnt] = newRecord;
					cnt ++;
				}
				return temp;
			} else {
				return null;
			}
		}
		
		public ListGridRecord createRecord(WebHookSubscriber param, ListGridRecord record, boolean init) {
			if (record == null) {
				record = new ListGridRecord();

				if (!init) {
					curDefinition.addSubscriber(param);
				}
			} else {
				//may need modification later TODO:
				String key = record.getAttribute(FIELD_NAME.SUBSCRIBERURL.name())+record.getAttribute(FIELD_NAME.SUBSCRIBER.name());

				// 一旦更新レコードのDefinitionを削除
				Map<String, WebHookSubscriber> map = new HashMap<String, WebHookSubscriber>();
				for (WebHookSubscriber def : curDefinition.getSubscribers()) {
					map.put((def.getUrl()+def.getSubscriberName()), def);
				}

				map.remove(key);

				// 更新した内容を追加
				map.put(key, param);

				ArrayList<WebHookSubscriber> newList = new ArrayList<WebHookSubscriber>();

				for(Map.Entry<String, WebHookSubscriber> e : map.entrySet()) {
					newList.add(e.getValue());
				}

				curDefinition.setSubscribers(newList);

			}
			record.setAttribute(FIELD_NAME.SUBSCRIBER.name(), param.getSubscriberName());
			record.setAttribute(FIELD_NAME.SUBSCRIBERURL.name(), param.getUrl());

			return record;
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

			//TODO:名前をlocale に追加改変
			ListGridField headerNameField = new ListGridField(HEADER_FIELD_NAME.HEADERNAME.name(), "Name Key");
			ListGridField headerValueField = new ListGridField(HEADER_FIELD_NAME.HEADERVALUE.name(), "Value");
			//後はセキュリティの追加設定をdialogして、ボタンを追加しようかと
			
			setFields(headerNameField, headerValueField);
		}
//		public WebHookTemplateDefinition getEditDefinition(WebHookTemplateDefinition definition) {
//			ListGridRecord[] records = getRecords();
//			for (ListGridRecord record : records) {
//				//definition.addSubscriber(new WebHookSubscriber());
//			}
//			return null;
//		}
	}
	
	private class SubscriberMapGrid extends ListGrid{
		public SubscriberMapGrid() {
			setWidth100();
			setHeight(500);

			setShowAllColumns(true);
			setShowAllRecords(true);
			setCanResizeFields(true);
			setCanSort(true);	
			setCanPickFields(false);
			setCanGroupBy(false);
			setAutoFitWidthApproach(AutoFitWidthApproach.BOTH);	
			setLeaveScrollbarGap(false);
			setBodyOverflow(Overflow.SCROLL);
			setOverflow(Overflow.VISIBLE);

			//url	name	pass	id	security related
			//名前をlocale に追加改変
			ListGridField subscriberNameField = new ListGridField(FIELD_NAME.SUBSCRIBER.name(), "Subscriber");
			ListGridField subscriberUrlField = new ListGridField(FIELD_NAME.SUBSCRIBERURL.name(), "URL");
			//後はセキュリティの追加設定をdialogして、ボタンを追加しようかと
			
			setFields(subscriberNameField, subscriberUrlField);
		}
//		public WebHookTemplateDefinition getEditDefinition(WebHookTemplateDefinition definition) {
//			ListGridRecord[] records = getRecords();
//			for (ListGridRecord record : records) {
//				definition.addSubscriber(new WebHookSubscriber());
//			}
//			return null;
//		}
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
//TODO add these message
			SC.ask(AdminClientMessageUtil.getString("ui_metadata_webhook_WebHookTemplateEditPane_saveConfirm"),
					AdminClientMessageUtil.getString("ui_metadata_webhook_WebHookTemplateEditPane_saveWebHookTemplateComment"), new BooleanCallback() {

				@Override
				public void execute(Boolean value) {
					if (value) {
						WebHookTemplateDefinition definition = curDefinition;
						definition = commonSection.getEditDefinition(definition);
						definition = webHookTemplateAttrPane.getEditDefinition(definition);
						definition = webHookTemplateSubscriberPane.getEditDefinition(definition);

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
			//TODO add these messages
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
