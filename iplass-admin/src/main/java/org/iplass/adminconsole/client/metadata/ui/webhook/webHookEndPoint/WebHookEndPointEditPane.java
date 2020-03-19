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

import java.io.Serializable;
import java.util.Map;

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
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
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
	 * version更新
	 */
	private void refreshVersion() {
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
				curVersion = result.getDefinitionInfo().getVersion();
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
		
		private TextAreaItem webHookEndPointUrlField;
		
		//basic authentication
		private TextItem webHookEndPointBasicUsernameField;
		private TextItem webHookEndPointBasicPasswordField;

		private TextItem webHookEndPointAuthorizationAltTokenTypeNameField;
		
		//token authentication
		private TextAreaItem webHookEndPointHmacTokenField;
		private TextAreaItem webHookEndPointBearerTokenField;

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
			urlForm.setWidth(850);
			webHookEndPointUrlField = new TextAreaItem("subscriberurl", "EndPoint URL Template");
			webHookEndPointUrlField.setWidth("100%");
			webHookEndPointUrlField.setHeight(80);
			webHookEndPointUrlField.setBrowserSpellCheck(false);
			webHookEndPointUrlField.setPrompt(AdminClientMessageUtil.getString("ui_metadata_webhook_WebHookEndPointEditPane_webHookEndPointUrlFieldHoverInfo"));
			SmartGWTUtil.setRequired(webHookEndPointUrlField);
			urlForm.setItems(webHookEndPointUrlField);
			
			VLayout authPane = new VLayout();
			authPane.setMargin(5);
			authPane.setMembersMargin(5);

			headerAuthForm =new DynamicForm();
			headerAuthForm.setPadding(10);
			headerAuthForm.setNumCols(2);
			headerAuthForm.setColWidths(100,"*");
			headerAuthForm.setIsGroup(true);
			headerAuthForm.setGroupTitle("Header Authentication");
			headerAuthForm.setWidth(800);
			
			webHookEndPointAuthorizationAltTokenTypeNameField = new MtpTextItem("alterTokenTypeName","Customize Token Type Name");
			webHookEndPointAuthorizationAltTokenTypeNameField.setPrompt(AdminClientMessageUtil.getString("ui_metadata_webhook_WebHookEndPointEditPane_webHookEndPointAuthorizationAltTokenTypeNameField"));
			webHookEndPointAuthorizationAltTokenTypeNameField.setCanEdit(true);
			
			
			webHookEndPointBasicUsernameField = new MtpTextItem("basicusername","Basic Username");
			webHookEndPointBasicUsernameField.setCanEdit(false);
			webHookEndPointBasicUsernameField.setCanFocus(false);
			webHookEndPointBasicUsernameField.setTextBoxStyle("textItemDisabled");
			
			webHookEndPointBasicPasswordField = new MtpTextItem("basicpassword","Basic Password");
			webHookEndPointBasicPasswordField.setCanEdit(false);
			webHookEndPointBasicPasswordField.setCanFocus(false);
			webHookEndPointBasicPasswordField.setTextBoxStyle("textItemDisabled");

			webHookEndPointBearerTokenField = new TextAreaItem("subscribersecuritybearertoken","Bearer Token");
			webHookEndPointBearerTokenField.setWidth("*");
			webHookEndPointBearerTokenField.setHeight(100);
			webHookEndPointBearerTokenField.setCanEdit(false);
			webHookEndPointBearerTokenField.setCanFocus(false);
			webHookEndPointBearerTokenField.setTextBoxStyle("textItemDisabled");
			ButtonItem editHeaderAuthButton = new ButtonItem(AdminClientMessageUtil.getString("ui_metadata_webhook_WebHookEndPointEditPane_editHeaderAuthButton"));
			editHeaderAuthButton.setWidth(150);
			editHeaderAuthButton.setPrompt(AdminClientMessageUtil.getString("ui_metadata_webhook_WebHookEndPointEditPane_headerAuthFormHoverInfo"));
			editHeaderAuthButton.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
				@Override
				public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
					if (curDefinition.getHeaderAuthType()==null || curDefinition.getHeaderAuthType().isEmpty()) {
						editMap(null, null);
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
					if (curDefinition.getHeaderAuthType()==null||!(curDefinition.getHeaderAuthType().equals("WHBT")||curDefinition.getHeaderAuthType().equals("WHBA"))) {
						curDefinition.setHeaderAuthType(null);
					}
					delMap(curDefinition.getHeaderAuthType());
				}
			});
			headerAuthForm.setItems(webHookEndPointAuthorizationAltTokenTypeNameField, webHookEndPointBasicUsernameField,webHookEndPointBasicPasswordField, webHookEndPointBearerTokenField, editHeaderAuthButton,deleteHeaderAuthButton);

			hmacForm =new DynamicForm();
			hmacForm.setPadding(10);
			hmacForm.setNumCols(2);
			hmacForm.setColWidths(100,"*");
			hmacForm.setIsGroup(true);
			hmacForm.setGroupTitle("Hmac Token Authentication");
			hmacForm.setWidth(800);
			webHookEndPointHmacTokenField = new TextAreaItem("securityhmactoken","HMAC Token");
			webHookEndPointHmacTokenField.setWidth("*");
			webHookEndPointHmacTokenField.setHeight(100);
			SmartGWTUtil.setReadOnlyTextArea(webHookEndPointHmacTokenField);
			
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
			hmacForm.setItems(webHookEndPointHmacTokenField,generateHmacButton,editHmacButton,deleteHmacButton);


			
			authPane.addMember(headerAuthForm);
			authPane.addMember(hmacForm);
			
			mainPane.addMember(urlForm);
			mainPane.addMember(authPane);
			
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
			final WebHookEndPointSecurityInfoEditDialog dialog = new WebHookEndPointSecurityInfoEditDialog(tokenType,tokenContent);
			
			dialog.addDataChangeHandler(new DataChangedHandler() {
				@Override
				public void onDataChanged(DataChangedEvent event) {
					Map<String, Serializable> valueMap = event.getValueMap();
					String type =(String) valueMap.get("type");
					String content =(String) valueMap.get("content");
					
					if (type == null || type.isEmpty()) {
					} else if (content==null||content.replaceAll("\\s", "").isEmpty()) {
						delMap(type);
						dialog.destroy();
					} else if (type.equals("WHHM")) {
						service.updateWebHookEndPointSecurityInfo(TenantInfoHolder.getId(), curDefinition.getName(), content, type,new AdminAsyncCallback<Void>() {

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
					} else {
						
						//dbに新情報の登録
						service.updateWebHookEndPointSecurityInfo(TenantInfoHolder.getId(), curDefinition.getName(), content, type,new AdminAsyncCallback<Void>() {

							@Override
							public void onSuccess(Void result) {
								if (curDefinition.getHeaderAuthType()!=type) {//タイプ変わったら旧タイプの削除
									service.updateWebHookEndPointSecurityInfo(TenantInfoHolder.getId(), curDefinition.getName(), null, 
											type=="WHBA"?"WHBT":"WHBA",
											new AdminAsyncCallback<Void>() {
										@Override
										public void onSuccess(Void result) {
										}
										@Override
										protected void beforeFailure(Throwable caught){
											SC.warn("error happened when deleting old token");
										};
									});
								}
								curDefinition.setHeaderAuthType(type);
								service.updateDefinition(TenantInfoHolder.getId(), curDefinition, curVersion, true, new MetaDataUpdateCallback() {
									@Override
									protected void overwriteUpdate() {
										updateWebHookEndPointDefinition(curDefinition, false);
									}
									@Override
									protected void afterUpdate(AdminDefinitionModifyResult result) {
										refreshVersion();
										refreshSecurityInfo();
										webHookEndPointAttributePane.markForRedraw();
										SmartGWTUtil.hideProgress();
										setHeaderAuthTokenVisibility();
										dialog.destroy();
									}
								});
							}
							@Override
							protected void beforeFailure(Throwable caught){
								SC.warn("error happened when registering new token");
								SmartGWTUtil.hideProgress();
							}
						});
					}
				}
			});
			dialog.show();
		}
		
		protected void delMap(String tokenType) {
			//confirmation
			SC.ask(AdminClientMessageUtil.getString("Warning"),
					AdminClientMessageUtil.getString("ui_metadata_webhook_WebHookEndPointEditPane_deleteSecurityInfoComment"), new BooleanCallback() {

				@Override
				public void execute(Boolean value) {
					if (value) {
						if (tokenType!=null) {
							if (tokenType!="WHHM") {
								if (curDefinition.getHeaderAuthType()!=null&&!curDefinition.getHeaderAuthType().replaceAll("\\s", "").isEmpty()) {
									if (tokenType!=curDefinition.getHeaderAuthType()) {
										return;
									}
								}
							}
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
							
							//refresh curdef and redraw
							if(tokenType.equals("WHBA")||tokenType.equals("WHBT")) {
								String tempUrl= SmartGWTUtil.getStringValue(webHookEndPointUrlField);//編集中のurlに影響しないため
								curDefinition.setHeaderAuthType(null);
								service.updateDefinition(TenantInfoHolder.getId(), curDefinition, curVersion, true, new MetaDataUpdateCallback() {

									@Override
									protected void overwriteUpdate() {
										updateWebHookEndPointDefinition(curDefinition, false);
									}

									@Override
									protected void afterUpdate(AdminDefinitionModifyResult result) {
										refreshVersion();
									}
								});
								webHookEndPointUrlField.setValue(tempUrl);
								setHeaderAuthTokenVisibility();
							} else if (tokenType.equals("WHHM")) {
								webHookEndPointHmacTokenField.clearValue();
							}
						}
					}
				}
			});
			webHookEndPointAttributePane.markForRedraw();
		}
		
		public void setHeaderAuthTokenVisibility() {
			if (curDefinition.getHeaderAuthType()!=null) {
				if (curDefinition.getHeaderAuthType().equals("WHBA")){
					webHookEndPointBearerTokenField.setVisible(false);
					webHookEndPointBearerTokenField.clearValue();
					webHookEndPointBasicPasswordField.setVisible(true);
					webHookEndPointBasicUsernameField.setVisible(true);
				}
				if (curDefinition.getHeaderAuthType().equals("WHBT")){
					webHookEndPointBearerTokenField.setVisible(true);
					webHookEndPointBasicPasswordField.setVisible(false);
					webHookEndPointBasicUsernameField.setVisible(false);
					webHookEndPointBasicPasswordField.clearValue();
					webHookEndPointBasicUsernameField.clearValue();
				}
			} else {
				webHookEndPointBearerTokenField.setVisible(true);
				webHookEndPointBasicPasswordField.setVisible(true);
				webHookEndPointBasicUsernameField.setVisible(true);
				webHookEndPointBearerTokenField.clearValue();
				webHookEndPointBasicPasswordField.clearValue();
				webHookEndPointBasicUsernameField.clearValue();
			}
			webHookEndPointAttributePane.markForRedraw();
		}
		/** definition -> pane */
		public void setDefinition(WebHookEndPointDefinition definition) {
			if (definition != null) {
				webHookEndPointUrlField.setValue(curDefinition.getUrl());
				webHookEndPointAuthorizationAltTokenTypeNameField.setValue(curDefinition.getHeaderAuthTypeName());
				refreshSecurityInfo();
			} else {
				webHookEndPointUrlField.clearValue();
				webHookEndPointBasicUsernameField.clearValue();
				webHookEndPointBasicPasswordField.clearValue();
				webHookEndPointHmacTokenField.clearValue();
				webHookEndPointBearerTokenField.clearValue();
				webHookEndPointAuthorizationAltTokenTypeNameField.clearValue();
			}
			setHeaderAuthTokenVisibility();
		}
		
		private void refreshSecurityInfo() {
			service.getWebHookEndPointSecurityInfo(TenantInfoHolder.getId(), curDefinition.getName(), "WHHM", new AdminAsyncCallback<String>() {
				@Override
				public void onSuccess(String result) {
					if (result==null||result.isEmpty()) {
						webHookEndPointHmacTokenField.clearValue();
					}
					else {
						webHookEndPointHmacTokenField.setValue(result);
					}
				}
				@Override
				protected void beforeFailure(Throwable caught){
					SmartGWTUtil.hideProgress();
				};
			});
			if (curDefinition.getHeaderAuthType()!=null||curDefinition.getHeaderAuthType().isEmpty()) {
				if (curDefinition.getHeaderAuthType().equals("WHBA")) {
					service.getWebHookEndPointSecurityInfo(TenantInfoHolder.getId(), curDefinition.getName(), "WHBA", new AdminAsyncCallback<String>() {
						@Override
						public void onSuccess(String result) {
							if (result==null||result.isEmpty()) {
								webHookEndPointBasicUsernameField.clearValue();
								webHookEndPointBasicPasswordField.clearValue();
							}
							else {
								String[] basics = result.split(":");
								if (basics.length < 2) {
									webHookEndPointBasicUsernameField.clearValue();
									webHookEndPointBasicPasswordField.clearValue();
								} else {
									webHookEndPointBasicUsernameField.setValue(basics[0]);
									webHookEndPointBasicPasswordField.setValue(basics[1]);
								}
							}
						}
						@Override
						protected void beforeFailure(Throwable caught){
							SmartGWTUtil.hideProgress();
						};
					});
				} else if (curDefinition.getHeaderAuthType().equals("WHBT")) {
					service.getWebHookEndPointSecurityInfo(TenantInfoHolder.getId(),curDefinition.getName(), "WHBT", new AdminAsyncCallback<String>() {
						@Override
						public void onSuccess(String result) {
							if (result==null||result.isEmpty()) {
								webHookEndPointBearerTokenField.clearValue();
							}
							else {
								webHookEndPointBearerTokenField.setValue(result);
							}
						}
						@Override
						protected void beforeFailure(Throwable caught){
							SmartGWTUtil.hideProgress();
						};
					});
				}
			}
		}

		/** pane -> definition */
		public WebHookEndPointDefinition getEditDefinition(WebHookEndPointDefinition definition) {
			definition.setUrl(SmartGWTUtil.getStringValue(webHookEndPointUrlField));
			definition.setHeaderAuthTypeName(SmartGWTUtil.getStringValue(webHookEndPointAuthorizationAltTokenTypeNameField));
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
