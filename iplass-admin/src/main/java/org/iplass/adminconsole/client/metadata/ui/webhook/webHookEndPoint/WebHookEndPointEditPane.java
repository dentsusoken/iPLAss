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
package org.iplass.adminconsole.client.metadata.ui.webhook.webHookEndPoint;

import org.iplass.adminconsole.client.base.event.DataChangedEvent;
import org.iplass.adminconsole.client.base.event.DataChangedHandler;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.rpc.AdminAsyncCallback;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpTextItem;
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
import org.iplass.mtp.webhook.template.endpointaddress.WebHookEndPointDefinition;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;

public class WebHookEndPointEditPane extends MetaDataMainEditPane {
	
	private final MetaDataServiceAsync service;
	/** 編集対象 */
	private WebHookEndPointDefinition curDefinition;
	private int curVersion;
	private String curDefinitionId;
	private MetaCommonHeaderPane headerPane;
	
	/** 共通属性 */
	private MetaCommonAttributeSection<WebHookEndPointDefinition> commonSection;
	
	/** 個別属性 */
	private WebHookEndPointAttributePane webHookEndPointAttributePane;
	
	private final String BASIC_DISPLAY_NAME = "Basic Authentication";
	private final String BEARER_DISPLAY_NAME = "Bearer Authentication";
	private final String CUSTOM_DISPLAY_NAME = "Custom Authentication";
	
	//FIXME use enum?
	private final String BASIC_TYPE_CODE = "WHBA";
	private final String BEARER_TYPE_CODE = "WHBT";
	private final String CUSTOM_TYPE_CODE = "WHCT";
	private final String HMAC_TYPE_CODE = "WHHM";
	
	public WebHookEndPointEditPane(MetaDataItemMenuTreeNode targetNode, DefaultMetaDataPlugin plugin) {
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
		commonSection = new MetaCommonAttributeSection<>(targetNode, WebHookEndPointDefinition.class);
		
		webHookEndPointAttributePane = new WebHookEndPointAttributePane();
		SectionStackSection webHookEndPointAttributeSection = createSection("Web End Point Addresses", webHookEndPointAttributePane);
		
		setMainSections(commonSection,webHookEndPointAttributeSection);
		addMember(headerPane);
		addMember(mainStack);
		
		initializeData();
	}
	/**
	 * データ初期化処理
	 */
	private void initializeData() {
//		エラーのクリア
		commonSection.clearErrors();
		webHookEndPointAttributePane.clearErrors();

		service.getDefinitionEntry(TenantInfoHolder.getId(), WebHookEndPointDefinition.class.getName(), defName, new AsyncCallback<DefinitionEntry>() {

			@Override
			public void onFailure(Throwable caught) {
				SC.say(AdminClientMessageUtil.getString("ui_metadata_webhook_WebHookEndPointEditPane_failed"),
						AdminClientMessageUtil.getString("ui_metadata_webhook_WebHookEndPointEditPane_failedGetScreenInfo"));

				GWT.log(caught.toString(), caught);
			}

			@Override
			public void onSuccess(DefinitionEntry result) {

				//画面に反映
				setDefinition(result);
			}

		});

//		ステータスチェック
		StatusCheckUtil.statuCheck(WebHookEndPointDefinition.class.getName(), defName, this);
	}
	
	/**
	 * Definition画面設定内容入り
	 *
	 * @param definition 編集対象
	 */
	protected void setDefinition(DefinitionEntry entry) {
		
		this.curDefinition = (WebHookEndPointDefinition) entry.getDefinition();
		this.curVersion = entry.getDefinitionInfo().getVersion();
		this.curDefinitionId = entry.getDefinitionInfo().getObjDefId();
		
		commonSection.setDefinition(curDefinition);
		webHookEndPointAttributePane.setDefinition(curDefinition);
		
	}

	/**
	 * 更新処理
	 *
	 * @param definition 更新対象
	 */
	private void updateWebHookEndPointDefinition(final WebHookEndPointDefinition definition, boolean checkVersion) {
		SmartGWTUtil.showSaveProgress();
		service.updateDefinition(TenantInfoHolder.getId(), definition, curVersion, checkVersion, new MetaDataUpdateCallback() {

			@Override
			protected void overwriteUpdate() {
				updateWebHookEndPointDefinition(definition, false);
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
	private void updateComplete(WebHookEndPointDefinition definition) {
		SC.say(AdminClientMessageUtil.getString("ui_metadata_webhook_WebHookEndPointEditPane_completion"),
				AdminClientMessageUtil.getString("ui_metadata_webhook_WebHookEndPointEditPane_saveWebHookTemplate"));

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
			boolean webHookEndPointValidate = webHookEndPointAttributePane.validate();
			if (!commonValidate || !webHookEndPointValidate) {
				return;
			}
			SC.ask(AdminClientMessageUtil.getString("ui_metadata_webhook_WebHookEndPointEditPane_saveConfirm"),
					AdminClientMessageUtil.getString("ui_metadata_webhook_WebHookEndPointEditPane_saveWebHookTemplateComment"), new BooleanCallback() {

				@Override
				public void execute(Boolean value) {
					if (value) {
						WebHookEndPointDefinition definition = curDefinition;
						definition = commonSection.getEditDefinition(definition);
						definition = webHookEndPointAttributePane.getEditDefinition(definition);

						updateWebHookEndPointDefinition(definition, true);
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
			SC.ask(AdminClientMessageUtil.getString("ui_metadata_webhook_WebHookEndPointEditPane_cancelConfirm"),
					AdminClientMessageUtil.getString("ui_metadata_webhook_WebHookEndPointEditPane_cancelConfirmComment")
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
	
	
	private class WebHookEndPointAttributePane extends VLayout {
		
		private DynamicForm urlForm;
		private DynamicForm headerAuthForm;
		private DynamicForm hmacForm;
		
		private TextItem webHookEndPointUrlField;
		
		//header auth
		private SelectItem authTypeItemField;
		private Label authHeaderSecretStatusLabel;
		private Label hmacSecretStatusLabel;
		private CheckboxItem isHmacEnabledField;
		
		
		private TextItem webHookEndPointAuthorizationAltTokenTypeNameField;//scheme for Authorization header
		private TextItem hmacHeaderNameField;//key for hmac header
		
		public WebHookEndPointAttributePane() {
			setOverflow(Overflow.AUTO);
			VLayout mainPane = new VLayout();
			mainPane.setMargin(5);
			mainPane.setMembersMargin(5);

			urlForm =new DynamicForm();
			urlForm.setPadding(10);
			urlForm.setNumCols(2);
			urlForm.setColWidths(100,"*");
			urlForm.setIsGroup(true);
			urlForm.setGroupTitle("EndPoint Address");
			urlForm.setWidth(800);
			webHookEndPointUrlField = new MtpTextItem("subscriberurl", "EndPoint URL");
			webHookEndPointUrlField.setWidth("100%");
			webHookEndPointUrlField.setBrowserSpellCheck(false);
			webHookEndPointUrlField.setPrompt(AdminClientMessageUtil.getString("ui_metadata_webhook_WebHookEndPointEditPane_webHookEndPointUrlFieldHoverInfo"));
			SmartGWTUtil.setRequired(webHookEndPointUrlField);
			urlForm.setItems(webHookEndPointUrlField);

			/**Authorization header*/
			VLayout headerAuthPane = new VLayout();
			headerAuthPane.setMargin(5);
			headerAuthPane.setMembersMargin(5);
			headerAuthPane.setIsGroup(true);
			headerAuthPane.setGroupTitle("Header Authentication");
			headerAuthPane.setWidth(800);
			headerAuthPane.setHeight(200);

			HLayout headerAuthContentLayout = new HLayout();
			headerAuthContentLayout.setMargin(5);
			headerAuthContentLayout.setMembersMargin(5);
			headerAuthContentLayout.setAlign(Alignment.CENTER);
			
			headerAuthForm =new DynamicForm();
			headerAuthForm.setPadding(10);
			headerAuthForm.setNumCols(2);
			headerAuthForm.setColWidths(100,"*");

			webHookEndPointAuthorizationAltTokenTypeNameField = new MtpTextItem("alterTokenTypeName","Customize Token Type Name");
			webHookEndPointAuthorizationAltTokenTypeNameField.setPrompt(AdminClientMessageUtil.getString("ui_metadata_webhook_WebHookEndPointEditPane_webHookEndPointAuthorizationAltTokenTypeNameField"));
			webHookEndPointAuthorizationAltTokenTypeNameField.setCanEdit(true);

			authTypeItemField = new SelectItem("tokenType","Token Type");
			authTypeItemField.setWidth("100%");
			authTypeItemField.setValueMap(BASIC_DISPLAY_NAME, BEARER_DISPLAY_NAME, CUSTOM_DISPLAY_NAME);
			authTypeItemField.addChangedHandler(new ChangedHandler() {
				public void onChanged(ChangedEvent event) {
					
					String selectedValue = SmartGWTUtil.getStringValue(authTypeItemField);
					if (BASIC_DISPLAY_NAME.equals(selectedValue)) {
						curDefinition.setHeaderAuthType(BASIC_TYPE_CODE);
					}else if (BEARER_DISPLAY_NAME.equals(selectedValue)) {
						curDefinition.setHeaderAuthType(BEARER_TYPE_CODE);
					}else if (CUSTOM_DISPLAY_NAME.equals(selectedValue)) {
						curDefinition.setHeaderAuthType(CUSTOM_TYPE_CODE);
					}
					refreshSecretIsStoredStatus(curDefinition.getHeaderAuthType());
					toggleCustomHeaderName();
				}
			});

			authHeaderSecretStatusLabel = new Label();  
			authHeaderSecretStatusLabel.setWidth("75%");
	        authHeaderSecretStatusLabel.setHeight(30);  
	        authHeaderSecretStatusLabel.setPadding(10);  
	        authHeaderSecretStatusLabel.setAlign(Alignment.CENTER);  
	        authHeaderSecretStatusLabel.setValign(VerticalAlignment.CENTER);  
	        authHeaderSecretStatusLabel.setWrap(false);  
//	        label.setIcon("approved.png");  
	        authHeaderSecretStatusLabel.setShowEdges(true);  

	        DynamicForm headerAuthButtonForm =new DynamicForm();
			headerAuthButtonForm.setPadding(10);
			headerAuthButtonForm.setNumCols(1);
			headerAuthButtonForm.setColWidths(200);
			headerAuthButtonForm.setAlign(Alignment.CENTER);

			ButtonItem editHeaderAuthButton = new ButtonItem(AdminClientMessageUtil.getString("ui_metadata_webhook_WebHookEndPointEditPane_editHeaderAuthButton"));
			editHeaderAuthButton.setWidth(150);
			editHeaderAuthButton.setPrompt(AdminClientMessageUtil.getString("ui_metadata_webhook_WebHookEndPointEditPane_headerAuthFormHoverInfo"));
			editHeaderAuthButton.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
				@Override
				public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
					if (curDefinition.getHeaderAuthType()==null || curDefinition.getHeaderAuthType().isEmpty()) {
						return;
					}
					service.getWebHookEndPointSecurityInfo(TenantInfoHolder.getId(), curDefinition.getName(), curDefinition.getHeaderAuthType(), new AdminAsyncCallback<String>() {
						@Override
						public void onSuccess(String result) {
							editMap(curDefinition.getHeaderAuthType(), result);
						}

						@Override
						protected void beforeFailure(Throwable caught){
							SmartGWTUtil.hideProgress();
						};
					});
				}
			});
			ButtonItem deleteHeaderAuthButton = new ButtonItem(AdminClientMessageUtil.getString("ui_metadata_webhook_WebHookEndPointEditPane_deleteHeaderAuthButton"));
			deleteHeaderAuthButton.setWidth(150);
			deleteHeaderAuthButton.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
				@Override
				public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
					delMap(curDefinition.getHeaderAuthType());
				}
			});

			headerAuthButtonForm.setItems(editHeaderAuthButton, deleteHeaderAuthButton);
			headerAuthContentLayout.setChildren(headerAuthButtonForm, authHeaderSecretStatusLabel);
			headerAuthForm.setItems(authTypeItemField, webHookEndPointAuthorizationAltTokenTypeNameField);
			headerAuthPane.setChildren(headerAuthForm, headerAuthContentLayout);

			/**hmac settings*/
			VLayout hmacPane = new VLayout();
			hmacPane.setMargin(5);
			hmacPane.setMembersMargin(5);
			hmacPane.setIsGroup(true);
			hmacPane.setGroupTitle("Hmac Token Authentication");
			hmacPane.setWidth(800);
			hmacPane.setHeight(230);

			HLayout hmacContentLayout = new HLayout();
			hmacContentLayout.setMargin(5);
			hmacContentLayout.setMembersMargin(5);
			hmacContentLayout.setAlign(Alignment.CENTER);
			
			hmacForm =new DynamicForm();
			hmacForm.setPadding(10);
			hmacForm.setNumCols(2);
			hmacForm.setColWidths(100,"*");

			hmacSecretStatusLabel = new Label();  
			hmacSecretStatusLabel.setWidth("75%");
	        hmacSecretStatusLabel.setHeight(30);  
	        hmacSecretStatusLabel.setPadding(10);  
	        hmacSecretStatusLabel.setAlign(Alignment.CENTER);  
	        hmacSecretStatusLabel.setValign(VerticalAlignment.CENTER);  
	        hmacSecretStatusLabel.setWrap(false);  
//	        label.setIcon("icons/16/approved.png");  
	        hmacSecretStatusLabel.setShowEdges(true);

	        DynamicForm hmacButtonForm = new DynamicForm(); 
	        hmacButtonForm.setPadding(10);
	        hmacButtonForm.setNumCols(1);
	        hmacButtonForm.setColWidths(200);

			ButtonItem generateHmacButton = new ButtonItem(AdminClientMessageUtil.getString("ui_metadata_webhook_WebHookEndPointEditPane_generateHmacButton"));
			generateHmacButton.setWidth(150);
			generateHmacButton.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
				@Override
				public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
					generateHmac();
				}
			});
			ButtonItem editHmacButton = new ButtonItem(AdminClientMessageUtil.getString("ui_metadata_webhook_WebHookEndPointEditPane_editHmacButton"));
			editHmacButton.setWidth(150);
			editHmacButton.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
				@Override
				public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
					service.getWebHookEndPointSecurityInfo(TenantInfoHolder.getId(), curDefinition.getName(), "WHHM", new AdminAsyncCallback<String>() {

						@Override
						public void onSuccess(String result) {
							editMap("WHHM", result);
						}

						@Override
						protected void beforeFailure(Throwable caught){
							SmartGWTUtil.hideProgress();
						};
					});
				}
			});
			generateHmacButton.setPrompt(AdminClientMessageUtil.getString("ui_metadata_webhook_WebHookEndPointEditPane_HmacButtonHoverInfo"));
			editHmacButton.setPrompt(AdminClientMessageUtil.getString("ui_metadata_webhook_WebHookEndPointEditPane_HmacButtonHoverInfo"));
			ButtonItem deleteHmacButton = new ButtonItem(AdminClientMessageUtil.getString("ui_metadata_webhook_WebHookEndPointEditPane_deleteHmacButton"));
			deleteHmacButton.setWidth(150);
			deleteHmacButton.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
				@Override
				public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
					delMap("WHHM");
				}
			});
			
			hmacHeaderNameField = new TextItem("webhookTokenName", "Security Token Name");
			hmacHeaderNameField.setWidth("100%");
			SmartGWTUtil.addHoverToFormItem(hmacHeaderNameField, AdminClientMessageUtil.getString("ui_metadata_webhook_WebHookTemplateEditPane_tokenNameFieldHoverInfo"));
			
			isHmacEnabledField = new CheckboxItem("webHookIsHmacEnabledField","Enable Hmac");
			
			hmacButtonForm.setItems(generateHmacButton,editHmacButton,deleteHmacButton);
			hmacForm.setItems(isHmacEnabledField, hmacHeaderNameField);
			hmacContentLayout.setChildren(hmacButtonForm, hmacSecretStatusLabel);
			hmacPane.setChildren(hmacForm, hmacContentLayout);

			mainPane.addMember(urlForm);
			mainPane.addMember(headerAuthPane);
			mainPane.addMember(hmacPane);
			
			addMember(mainPane);
		}
		protected void generateHmac() {
			service.generateHmacTokenString(new AdminAsyncCallback<String>() {

				@Override
				public void onSuccess(String result) {
					editMap("WHHM", result);
				}

				@Override
				protected void beforeFailure(Throwable caught){
					SmartGWTUtil.hideProgress();
				};
			});
		}
		protected void editMap(String tokenType, String tokenContent) {
			if(tokenType==null || tokenType.isEmpty()) {
				return;//タイプが選択してないなら何もしない。
			}
			final WebHookEndPointSecurityInfoEditDialog dialog = new WebHookEndPointSecurityInfoEditDialog(tokenType,tokenContent);
			dialog.addDataChangeHandler(new DataChangedHandler() {
				@Override
				public void onDataChanged(DataChangedEvent event) {
					String content =(String) event.getValue(String.class, "result");
					 if (content==null||content.replaceAll("\\s", "").isEmpty()) {
						delMap(tokenType);
						dialog.destroy();
					} else {
							service.updateWebHookEndPointSecurityInfo(TenantInfoHolder.getId(), curDefinition.getName(), content, tokenType, new AdminAsyncCallback<Void>() {
								@Override
								public void onSuccess(Void result) {
									refreshSecurityInfo();
									webHookEndPointAttributePane.markForRedraw();
									dialog.destroy();
									SmartGWTUtil.hideProgress();
								}
	
								@Override
								protected void beforeFailure(Throwable caught){
									SmartGWTUtil.hideProgress();
								};
							});
						}
					}
			});
			dialog.show();
		}
		
		protected void delMap(String tokenType) {
			//confirmation
			SC.ask(AdminClientMessageUtil.getString("LocaleInfo.ui_metadata_webhook_WebHookEndPointEditPane_deleteSecurityInfoDialogTitle"),
					AdminClientMessageUtil.getString("ui_metadata_webhook_WebHookEndPointEditPane_deleteSecurityInfoComment"), new BooleanCallback() {

				@Override
				public void execute(Boolean value) {
					if (value) {
						if (tokenType!=null) {
							//delete data
							service.updateWebHookEndPointSecurityInfo(TenantInfoHolder.getId(), curDefinition.getName(), null, tokenType, new AdminAsyncCallback<Void>() {
								@Override
								public void onSuccess(Void result) {
								}
								@Override
								protected void beforeFailure(Throwable caught){
									SC.warn("error happened when Communicating with Database");
									SmartGWTUtil.hideProgress();
								};
							});
							if ("WHHM".equals(tokenType)) {
								setHmacLabelStatus(false);
							} else {
								setHeaderAuthLabelStatus(false);
							}
						}
					}
				}
			});
			webHookEndPointAttributePane.markForRedraw();
		}

		/** definition -> pane */
		public void setDefinition(WebHookEndPointDefinition definition) {
			if (definition != null) {
				webHookEndPointUrlField.setValue(definition.getUrl());
				webHookEndPointAuthorizationAltTokenTypeNameField.setValue(definition.getHeaderAuthCustomTypeName());
				if(BASIC_TYPE_CODE.equals(definition.getHeaderAuthType())) {
					authTypeItemField.setValue(BASIC_DISPLAY_NAME);
				} else if(BEARER_TYPE_CODE.equals(definition.getHeaderAuthType())) {
					authTypeItemField.setValue(BEARER_DISPLAY_NAME);
				} else if(CUSTOM_TYPE_CODE.equals(definition.getHeaderAuthType())) {
					authTypeItemField.setValue(CUSTOM_DISPLAY_NAME);
				} else {
					authTypeItemField.clearValue();
				}
				hmacHeaderNameField.setValue(definition.getHmacHashHeader());
				isHmacEnabledField.setValue(definition.isHmacEnabled());
				refreshSecurityInfo();
				toggleCustomHeaderName();
			} else {
				hmacHeaderNameField.clearValue();
				webHookEndPointUrlField.clearValue();
				webHookEndPointAuthorizationAltTokenTypeNameField.clearValue();
				authTypeItemField.clearValue();
				isHmacEnabledField.clearValue();
				setHmacLabelStatus(false);
				setHeaderAuthLabelStatus(false);
			}
		}

		private void setHmacLabelStatus(boolean isSecretStored) {
			if (isSecretStored) {
				hmacSecretStatusLabel.setContents(AdminClientMessageUtil.getString("ui_metadata_webhook_WebHookEndPointEditPane_hmacSecretStoredTrue"));
			} else {
				hmacSecretStatusLabel.setContents(AdminClientMessageUtil.getString("ui_metadata_webhook_WebHookEndPointEditPane_hmacSecretStoredFalse"));
			}
		}
		private void setHeaderAuthLabelStatus(boolean isSecretStored) {
			if (isSecretStored) {
				authHeaderSecretStatusLabel.setContents(AdminClientMessageUtil.getString("ui_metadata_webhook_WebHookEndPointEditPane_headerAuthSecretStoredTrue"));
			} else {
				authHeaderSecretStatusLabel.setContents(AdminClientMessageUtil.getString("ui_metadata_webhook_WebHookEndPointEditPane_headerAuthSecretStoredFalse"));
			}
		}
		private void refreshSecretIsStoredStatus(String type) {
			service.getWebHookEndPointSecurityInfo(TenantInfoHolder.getId(), curDefinition.getName(), type, new AdminAsyncCallback<String>() {
				@Override
				public void onSuccess(String result) {
					if (result==null||result.isEmpty()) {
						if ("WHHM".equals(type)) {
							setHmacLabelStatus(false);
						} else {
							setHeaderAuthLabelStatus(false);
						}
					} else {
						if ("WHHM".equals(type)) {
							setHmacLabelStatus(true);
						} else {
							setHeaderAuthLabelStatus(true);
						}
					}
				}
				@Override
				protected void beforeFailure(Throwable caught){
					SmartGWTUtil.hideProgress();
				};
			});
		}
		private void refreshSecurityInfo() {
			refreshSecretIsStoredStatus("WHHM");
			refreshSecretIsStoredStatus(curDefinition.getHeaderAuthType());
		}

		private void toggleCustomHeaderName() {
			if("WHCT".equals(curDefinition.getHeaderAuthType())) {
				webHookEndPointAuthorizationAltTokenTypeNameField.setVisible(true);
			} else {
				webHookEndPointAuthorizationAltTokenTypeNameField.setVisible(false);
			}
		}
		/** pane -> definition */
		public WebHookEndPointDefinition getEditDefinition(WebHookEndPointDefinition definition) {
			definition.setUrl(SmartGWTUtil.getStringValue(webHookEndPointUrlField));
			//authTypeItemFieldはcurDefinition随時更新のため、ここには何もしないで済む
			definition.setHeaderAuthCustomTypeName(SmartGWTUtil.getStringValue(webHookEndPointAuthorizationAltTokenTypeNameField));
			definition.setHmacHashHeader(SmartGWTUtil.getStringValue(hmacHeaderNameField));
			definition.setHmacEnabled(SmartGWTUtil.getBooleanValue(isHmacEnabledField));
			return definition;
		}
		
		public boolean validate() {
			return true;
		}

		public void clearErrors() {
//			urlForm.clearErrors(true);
//			headerAuthForm.clearErrors(true);
//			hmacForm.clearErrors(true);
		}
	}
}
