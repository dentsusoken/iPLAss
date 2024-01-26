/*
 * Copyright (C) 2020 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.adminconsole.client.metadata.ui.webhook.webhookEndpoint;

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
import org.iplass.mtp.webhook.endpoint.definition.WebhookEndpointDefinition;
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

public class WebhookEndpointEditPane extends MetaDataMainEditPane {
	
	
	private final static String BASIC_DISPLAY_NAME = "Basic Authentication";
	private final static String BEARER_DISPLAY_NAME = "Bearer Authentication";
	private final static String CUSTOM_DISPLAY_NAME = "Custom Authentication";
	private final static String NO_HEADER_AUTH_DISPLAY_NAME = "Disabled";
	private final static String BASIC_TYPE_CODE = "WHBA";
	private final static String BEARER_TYPE_CODE = "WHBT";
	private final static String CUSTOM_TYPE_CODE = "WHCT";
	private final static String HMAC_TYPE_CODE = "WHHM";
	
	private final MetaDataServiceAsync service;
	/** 編集対象 */
	private WebhookEndpointDefinition curDefinition;
	private int curVersion;
	private String curDefinitionId;
	private MetaCommonHeaderPane headerPane;
	
	/** 共通属性 */
	private MetaCommonAttributeSection<WebhookEndpointDefinition> commonSection;
	
	/** 個別属性 */
	private WebhookEndpointAttributePane webhookEndpointAttributePane;


	public WebhookEndpointEditPane(MetaDataItemMenuTreeNode targetNode, DefaultMetaDataPlugin plugin) {
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
		commonSection = new MetaCommonAttributeSection<>(targetNode, WebhookEndpointDefinition.class);
		
		webhookEndpointAttributePane = new WebhookEndpointAttributePane();
		SectionStackSection webhookEndpointAttributeSection = createSection("Web End Point Addresses", webhookEndpointAttributePane);
		
		setMainSections(commonSection,webhookEndpointAttributeSection);
		addMember(headerPane);
		addMember(mainStack);
		
		initializeData();
	}

	/**
	 * Definition画面設定内容入り
	 *
	 * @param definition 編集対象
	 */
	protected void setDefinition(DefinitionEntry entry) {
		
		this.curDefinition = (WebhookEndpointDefinition) entry.getDefinition();
		this.curVersion = entry.getDefinitionInfo().getVersion();
		this.curDefinitionId = entry.getDefinitionInfo().getObjDefId();
		
		commonSection.setDefinition(curDefinition);
		webhookEndpointAttributePane.setDefinition(curDefinition);
		
	}

	/**
	 * データ初期化処理
	 */
	private void initializeData() {
//		エラーのクリア
		commonSection.clearErrors();
		webhookEndpointAttributePane.clearErrors();

		service.getDefinitionEntry(TenantInfoHolder.getId(), WebhookEndpointDefinition.class.getName(), defName, new AsyncCallback<DefinitionEntry>() {

			@Override
			public void onFailure(Throwable caught) {
				SC.say(AdminClientMessageUtil.getString("ui_metadata_webhook_WebhookEndpointEditPane_failed"),
						AdminClientMessageUtil.getString("ui_metadata_webhook_WebhookEndpointEditPane_failedGetScreenInfo"));

				GWT.log(caught.toString(), caught);
			}

			@Override
			public void onSuccess(DefinitionEntry result) {

				//画面に反映
				setDefinition(result);
			}

		});

//		ステータスチェック
		StatusCheckUtil.statuCheck(WebhookEndpointDefinition.class.getName(), defName, this);
	}

	/**
	 * 更新処理
	 *
	 * @param definition 更新対象
	 */
	private void updateWebhookEndpointDefinition(final WebhookEndpointDefinition definition, boolean checkVersion) {
		SmartGWTUtil.showSaveProgress();
		service.updateDefinition(TenantInfoHolder.getId(), definition, curVersion, checkVersion, new MetaDataUpdateCallback() {

			@Override
			protected void overwriteUpdate() {
				updateWebhookEndpointDefinition(definition, false);
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
	private void updateComplete(WebhookEndpointDefinition definition) {
		SC.say(AdminClientMessageUtil.getString("ui_metadata_webhook_WebhookEndpointEditPane_completion"),
				AdminClientMessageUtil.getString("ui_metadata_webhook_WebhookEndpointEditPane_saveWebhookTemplate"));

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
			boolean webhookEndpointValidate = webhookEndpointAttributePane.validate();
			if (!commonValidate || !webhookEndpointValidate) {
				return;
			}
			SC.ask(AdminClientMessageUtil.getString("ui_metadata_webhook_WebhookEndpointEditPane_saveConfirm"),
					AdminClientMessageUtil.getString("ui_metadata_webhook_WebhookEndpointEditPane_saveWebhookTemplateComment"), new BooleanCallback() {

				@Override
				public void execute(Boolean value) {
					if (value) {
						WebhookEndpointDefinition definition = curDefinition;
						definition = commonSection.getEditDefinition(definition);
						definition = webhookEndpointAttributePane.getEditDefinition(definition);

						updateWebhookEndpointDefinition(definition, true);
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
			SC.ask(AdminClientMessageUtil.getString("ui_metadata_webhook_WebhookEndpointEditPane_cancelConfirm"),
					AdminClientMessageUtil.getString("ui_metadata_webhook_WebhookEndpointEditPane_cancelConfirmComment")
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
	
	
	private class WebhookEndpointAttributePane extends VLayout {
		
		private DynamicForm urlForm;
		private DynamicForm headerAuthForm;
		private DynamicForm hmacForm;
		private DynamicForm headerAuthCustomSchemeForm;
		private TextItem webhookEndpointUrlField;
		
		//header auth
		private VLayout headerAuthPane = new VLayout();
		private SelectItem authTypeItemField;
		private Label authHeaderSecretStatusLabel;
		private Label hmacSecretStatusLabel;
		private CheckboxItem isHmacEnabledField;

		private TextItem webhookEndpointAuthorizationAltTokenTypeNameField;//scheme for Authorization header

		private VLayout hmacPane = new VLayout();
		private TextItem hmacHeaderNameField;//key for hmac header
		
		public WebhookEndpointAttributePane() {
			setOverflow(Overflow.AUTO);
			VLayout mainPane = new VLayout();
			mainPane.setMargin(5);
			mainPane.setMembersMargin(5);

			urlForm =new DynamicForm();
			urlForm.setPadding(10);
			urlForm.setNumCols(2);
			urlForm.setColWidths(100,"*");
			urlForm.setIsGroup(true);
			urlForm.setGroupTitle("Endpoint Address");
			urlForm.setWidth(800);
			webhookEndpointUrlField = new MtpTextItem("subscriberurl", "Endpoint URL");
			webhookEndpointUrlField.setWidth("100%");
			webhookEndpointUrlField.setBrowserSpellCheck(false);
			webhookEndpointUrlField.setPrompt(AdminClientMessageUtil.getString("ui_metadata_webhook_WebhookEndpointEditPane_webhookEndpointUrlFieldHoverInfo"));
			SmartGWTUtil.setRequired(webhookEndpointUrlField);
			urlForm.setItems(webhookEndpointUrlField);

			/**Authorization header*/
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

			headerAuthCustomSchemeForm =new DynamicForm();
			headerAuthCustomSchemeForm.setPadding(10);
			headerAuthCustomSchemeForm.setNumCols(2);
			headerAuthCustomSchemeForm.setColWidths(100,"*");
			webhookEndpointAuthorizationAltTokenTypeNameField = new MtpTextItem("alterTokenTypeName","Customize Token Type Name");
			webhookEndpointAuthorizationAltTokenTypeNameField.setPrompt(AdminClientMessageUtil.getString("ui_metadata_webhook_WebhookEndpointEditPane_webhookEndpointAuthorizationAltTokenTypeNameField"));
			webhookEndpointAuthorizationAltTokenTypeNameField.setCanEdit(true);
			headerAuthCustomSchemeForm.setItems(webhookEndpointAuthorizationAltTokenTypeNameField);

			authTypeItemField = new SelectItem("tokenType","Token Type");
			authTypeItemField.setWidth("100%");
			authTypeItemField.setValueMap(BASIC_DISPLAY_NAME, BEARER_DISPLAY_NAME, CUSTOM_DISPLAY_NAME, NO_HEADER_AUTH_DISPLAY_NAME);
			authTypeItemField.addChangedHandler(new ChangedHandler() {
				public void onChanged(ChangedEvent event) {
					
					String selectedValue = SmartGWTUtil.getStringValue(authTypeItemField);
					if (BASIC_DISPLAY_NAME.equals(selectedValue)) {
						curDefinition.setHeaderAuthType(BASIC_TYPE_CODE);
					}else if (BEARER_DISPLAY_NAME.equals(selectedValue)) {
						curDefinition.setHeaderAuthType(BEARER_TYPE_CODE);
					}else if (CUSTOM_DISPLAY_NAME.equals(selectedValue)) {
						curDefinition.setHeaderAuthType(CUSTOM_TYPE_CODE);
					} else if (NO_HEADER_AUTH_DISPLAY_NAME.equals(selectedValue)) {
						curDefinition.setHeaderAuthType(null);
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

			ButtonItem editHeaderAuthButton = new ButtonItem(AdminClientMessageUtil.getString("ui_metadata_webhook_WebhookEndpointEditPane_editHeaderAuthButton"));
			editHeaderAuthButton.setWidth(150);
			editHeaderAuthButton.setPrompt(AdminClientMessageUtil.getString("ui_metadata_webhook_WebhookEndpointEditPane_headerAuthFormHoverInfo"));
			editHeaderAuthButton.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
				@Override
				public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
					if (curDefinition.getHeaderAuthType()==null || curDefinition.getHeaderAuthType().isEmpty()) {
						return;
					}
					service.getWebhookEndpointSecurityInfo(TenantInfoHolder.getId(), curDefinition.getName(), curDefinition.getHeaderAuthType(), new AdminAsyncCallback<String>() {
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
			ButtonItem deleteHeaderAuthButton = new ButtonItem(AdminClientMessageUtil.getString("ui_metadata_webhook_WebhookEndpointEditPane_deleteHeaderAuthButton"));
			deleteHeaderAuthButton.setWidth(150);
			deleteHeaderAuthButton.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
				@Override
				public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
					delMap(curDefinition.getHeaderAuthType());
				}
			});

			headerAuthButtonForm.setItems(editHeaderAuthButton, deleteHeaderAuthButton);
			headerAuthContentLayout.setChildren(headerAuthButtonForm, authHeaderSecretStatusLabel);
			headerAuthForm.setItems(authTypeItemField);
			headerAuthPane.setChildren(headerAuthForm, headerAuthCustomSchemeForm, headerAuthContentLayout);

			/**hmac settings*/
			hmacPane.setMargin(5);
			hmacPane.setMembersMargin(5);
			hmacPane.setIsGroup(true);
			hmacPane.setGroupTitle("HMAC Authentication");
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

			ButtonItem generateHmacButton = new ButtonItem(AdminClientMessageUtil.getString("ui_metadata_webhook_WebhookEndpointEditPane_generateHmacButton"));
			generateHmacButton.setWidth(150);
			generateHmacButton.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
				@Override
				public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
					generateHmac();
				}
			});
			ButtonItem editHmacButton = new ButtonItem(AdminClientMessageUtil.getString("ui_metadata_webhook_WebhookEndpointEditPane_editHmacButton"));
			editHmacButton.setWidth(150);
			editHmacButton.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
				@Override
				public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
					service.getWebhookEndpointSecurityInfo(TenantInfoHolder.getId(), curDefinition.getName(), HMAC_TYPE_CODE, new AdminAsyncCallback<String>() {

						@Override
						public void onSuccess(String result) {
							editMap(HMAC_TYPE_CODE, result);
						}

						@Override
						protected void beforeFailure(Throwable caught){
							SmartGWTUtil.hideProgress();
						};
					});
				}
			});
			generateHmacButton.setPrompt(AdminClientMessageUtil.getString("ui_metadata_webhook_WebhookEndpointEditPane_HmacButtonHoverInfo"));
			editHmacButton.setPrompt(AdminClientMessageUtil.getString("ui_metadata_webhook_WebhookEndpointEditPane_HmacButtonHoverInfo"));
			ButtonItem deleteHmacButton = new ButtonItem(AdminClientMessageUtil.getString("ui_metadata_webhook_WebhookEndpointEditPane_deleteHmacButton"));
			deleteHmacButton.setWidth(150);
			deleteHmacButton.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
				@Override
				public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
					delMap(HMAC_TYPE_CODE);
				}
			});
			
			hmacHeaderNameField = new TextItem("webhookTokenName", "Header Name");
			hmacHeaderNameField.setWidth("100%");
			SmartGWTUtil.addHoverToFormItem(hmacHeaderNameField, AdminClientMessageUtil.getString("ui_metadata_webhook_WebhookTemplateEditPane_tokenNameFieldHoverInfo"));
			
			isHmacEnabledField = new CheckboxItem("webhookIsHmacEnabledField","Enable HMAC");
			
			hmacButtonForm.setItems(generateHmacButton,editHmacButton,deleteHmacButton);
			hmacForm.setItems(isHmacEnabledField, hmacHeaderNameField);
			hmacContentLayout.setChildren(hmacButtonForm, hmacSecretStatusLabel);
			hmacPane.setChildren(hmacForm, hmacContentLayout);

			mainPane.addMember(urlForm);
			mainPane.addMember(headerAuthPane);
			mainPane.addMember(hmacPane);
			
			addMember(mainPane);
		}

		/** definition -> pane */
		public void setDefinition(WebhookEndpointDefinition definition) {
			if (definition != null) {
				webhookEndpointUrlField.setValue(definition.getUrl());
				webhookEndpointAuthorizationAltTokenTypeNameField.setValue(definition.getHeaderAuthCustomTypeName());
				if(BASIC_TYPE_CODE.equals(definition.getHeaderAuthType())) {
					authTypeItemField.setValue(BASIC_DISPLAY_NAME);
				} else if(BEARER_TYPE_CODE.equals(definition.getHeaderAuthType())) {
					authTypeItemField.setValue(BEARER_DISPLAY_NAME);
				} else if(CUSTOM_TYPE_CODE.equals(definition.getHeaderAuthType())) {
					authTypeItemField.setValue(CUSTOM_DISPLAY_NAME);
				} else {
					authTypeItemField.setValue(NO_HEADER_AUTH_DISPLAY_NAME);
				}
				hmacHeaderNameField.setValue(definition.getHmacHashHeader());
				isHmacEnabledField.setValue(definition.isHmacEnabled());
				refreshSecurityInfo();
				toggleCustomHeaderName();
			} else {
				hmacHeaderNameField.clearValue();
				webhookEndpointUrlField.clearValue();
				webhookEndpointAuthorizationAltTokenTypeNameField.clearValue();
				authTypeItemField.clearValue();
				isHmacEnabledField.clearValue();
				setHmacLabelStatus(false);
				setHeaderAuthLabelStatus(false);
				toggleCustomHeaderName();
			}
		}
		/** pane -> definition */
		public WebhookEndpointDefinition getEditDefinition(WebhookEndpointDefinition definition) {
			definition.setUrl(SmartGWTUtil.getStringValue(webhookEndpointUrlField));
			//authTypeItemFieldはcurDefinition随時更新のため、ここには何もしないで済む
			definition.setHeaderAuthCustomTypeName(SmartGWTUtil.getStringValue(webhookEndpointAuthorizationAltTokenTypeNameField));
			definition.setHmacHashHeader(SmartGWTUtil.getStringValue(hmacHeaderNameField));
			definition.setHmacEnabled(SmartGWTUtil.getBooleanValue(isHmacEnabledField));
			return definition;
		}
		
		public boolean validate() {
			return true;
		}

		public void clearErrors() {
			urlForm.clearErrors(true);
			headerAuthForm.clearErrors(true);
			hmacForm.clearErrors(true);
			headerAuthCustomSchemeForm.clearErrors(true);
		}
		protected void generateHmac() {
			service.generateHmacTokenString(new AdminAsyncCallback<String>() {

				@Override
				public void onSuccess(String result) {
					editMap(HMAC_TYPE_CODE, result);
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
			final WebhookEndpointSecurityInfoEditDialog dialog = new WebhookEndpointSecurityInfoEditDialog(tokenType,tokenContent);
			dialog.addDataChangeHandler(new DataChangedHandler() {
				@Override
				public void onDataChanged(DataChangedEvent event) {
					String content =(String) event.getValue(String.class, "result");
					 if (content==null||content.replaceAll("\\s", "").isEmpty()) {
						delMap(tokenType);
						dialog.destroy();
					} else {
							service.updateWebhookEndpointSecurityInfo(TenantInfoHolder.getId(), curDefinition.getName(), content, tokenType, new AdminAsyncCallback<Void>() {
								@Override
								public void onSuccess(Void result) {
									refreshSecurityInfo();
									webhookEndpointAttributePane.markForRedraw();
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
			SC.ask(AdminClientMessageUtil.getString("LocaleInfo.ui_metadata_webhook_WebhookEndpointEditPane_deleteSecurityInfoDialogTitle"),
					AdminClientMessageUtil.getString("ui_metadata_webhook_WebhookEndpointEditPane_deleteSecurityInfoComment"), new BooleanCallback() {

				@Override
				public void execute(Boolean value) {
					if (value) {
						if (tokenType!=null) {
							//delete data
							service.updateWebhookEndpointSecurityInfo(TenantInfoHolder.getId(), curDefinition.getName(), null, tokenType, new AdminAsyncCallback<Void>() {
								@Override
								public void onSuccess(Void result) {
								}
								@Override
								protected void beforeFailure(Throwable caught){
									SC.warn("error happened when Communicating with Database");
									SmartGWTUtil.hideProgress();
								};
							});
							if (HMAC_TYPE_CODE.equals(tokenType)) {
								setHmacLabelStatus(false);
							} else {
								setHeaderAuthLabelStatus(false);
							}
						}
					}
				}
			});
			webhookEndpointAttributePane.markForRedraw();
		}

		private void setHmacLabelStatus(boolean isSecretStored) {
			if (isSecretStored) {
				hmacSecretStatusLabel.setContents(AdminClientMessageUtil.getString("ui_metadata_webhook_WebhookEndpointEditPane_hmacSecretStoredTrue"));
			} else {
				hmacSecretStatusLabel.setContents(AdminClientMessageUtil.getString("ui_metadata_webhook_WebhookEndpointEditPane_hmacSecretStoredFalse"));
			}
		}
		private void setHeaderAuthLabelStatus(boolean isSecretStored) {
			if (isSecretStored) {
				authHeaderSecretStatusLabel.setContents(AdminClientMessageUtil.getString("ui_metadata_webhook_WebhookEndpointEditPane_headerAuthSecretStoredTrue"));
			} else {
				authHeaderSecretStatusLabel.setContents(AdminClientMessageUtil.getString("ui_metadata_webhook_WebhookEndpointEditPane_headerAuthSecretStoredFalse"));
			}
		}
		private void refreshSecretIsStoredStatus(String type) {
			service.getWebhookEndpointSecurityInfo(TenantInfoHolder.getId(), curDefinition.getName(), type, new AdminAsyncCallback<String>() {
				@Override
				public void onSuccess(String result) {
					if (result==null||result.isEmpty()) {
						if (HMAC_TYPE_CODE.equals(type)) {
							setHmacLabelStatus(false);
						} else {
							setHeaderAuthLabelStatus(false);
						}
					} else {
						if (HMAC_TYPE_CODE.equals(type)) {
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
			refreshSecretIsStoredStatus(HMAC_TYPE_CODE);
			refreshSecretIsStoredStatus(curDefinition.getHeaderAuthType());
		}

		private void toggleCustomHeaderName() {
			if("WHCT".equals(curDefinition.getHeaderAuthType())) {
				headerAuthCustomSchemeForm.setVisible(true);
			} else {
				headerAuthCustomSchemeForm.setVisible(false);
			}
		}

	}
}
