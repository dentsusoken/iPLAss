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
import java.util.List;

import org.iplass.adminconsole.client.base.event.DataChangedEvent;
import org.iplass.adminconsole.client.base.event.DataChangedHandler;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.ui.widget.MtpDialog;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpForm2Column;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpTextItem;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.mtp.webhook.template.definition.WebHookSubscriber;
import com.google.gwt.user.client.ui.TextArea;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * @author lisf06
 *
 */
public class WebHookSubscriberDialog extends MtpDialog {
	
	/** メインダイアログ */
	private SubscriberAttributePane subscriberAttrEditPane;
	
	/** データ変更ハンドラ */
	private List<DataChangedHandler> handlers = new ArrayList<DataChangedHandler>();
	
	/** 当編集対象 */
	private WebHookSubscriber curWebHookSubscriber; 
	
	/** イニシャライザー、ダイアログ開ける時に呼ばれる */
	public WebHookSubscriberDialog (WebHookSubscriber definition) {

		curWebHookSubscriber = definition;
		if (curWebHookSubscriber == null) {
			curWebHookSubscriber = new WebHookSubscriber();
		}

		setTitle("Subscriber Editor");
		setHeight("80%");
		centerInPage();

		subscriberAttrEditPane = new SubscriberAttributePane();
		subscriberAttrEditPane.setHeight100();
		container.addMember(subscriberAttrEditPane);

		IButton save = new IButton("Save");
		save.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
				WebHookSubscriber definition = curWebHookSubscriber;
				if ( subscriberAttrEditPane.isSubscriberUrlFieldEmpty()) {//urlは必要,nullか""ならwarnして、ダイアログはそのまま
					SC.warn(AdminClientMessageUtil.getString("ui_metadata_webhook_WebHookTemplateEditPane_emptySubscriberUrl"));//TODO:add message
					return;
				}
				definition = subscriberAttrEditPane.getEditDefinition(definition);
				fireDataChanged(definition);
				destroy();
			}
		});

		IButton cancel = new IButton("Cancel");
		cancel.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
				destroy();
			}
		});
		footer.setMembers(save, cancel);
	}
	
	private class SubscriberAttributePane extends VLayout {
		
		private DynamicForm form;
		private TextItem subscriberNameField;
		private TextItem subscriberUrlField;
		//basic authentication
		private TextItem subscriberSecurityUsernameField;
		private TextItem subscriberSecurityPasswordField;

		
		//token authentication
		private TextAreaItem subscriberSecurityTokenField;//will be passed to sha256 in UTF8 and then passed to the user as iplass-token:[xxxx]
		private TextAreaItem subscriberSecurityBearerTokenField;

		public SubscriberAttributePane() {
			
			form = new MtpForm2Column();
			subscriberNameField = new MtpTextItem("subscribername", "Subscriber");//TODO: add message AdminClientMessageUtil.getString("ui_metadata_webhook_WebHookSubscriberDialog_subscriberName"));
			subscriberUrlField = new MtpTextItem("subscriberurl", "Subscriber's URL");//TODO: add message AdminClientMessageUtil.getString("ui_metadata_webhook_WebHookSubscriberDialog_subscriberUrl"));
			
			subscriberSecurityUsernameField = new MtpTextItem("securityusername","Security Username");
			subscriberSecurityPasswordField = new MtpTextItem("securitypassword","Security Password");
			
			subscriberSecurityTokenField = new TextAreaItem("subscribersecuritytoken","Security Token");
			subscriberSecurityTokenField.setWidth("*");
			subscriberSecurityTokenField.setHeight(200);
			
			subscriberSecurityBearerTokenField = new TextAreaItem("subscribersecuritybearertoken","Bearer Token");
			subscriberSecurityBearerTokenField.setWidth("*");
			subscriberSecurityBearerTokenField.setHeight(200);
			
			form.setItems(
					subscriberNameField, 
					subscriberUrlField, 
					subscriberSecurityUsernameField,
					subscriberSecurityPasswordField,
					subscriberSecurityTokenField,
					subscriberSecurityBearerTokenField
					);
			addMember(form);
			
			setDefinition(curWebHookSubscriber);
		}
		
		/** definition -> dialog */
		public void setDefinition(WebHookSubscriber definition) {
			if (definition != null) {
				subscriberNameField.setValue(definition.getSubscriberName());
				subscriberUrlField.setValue(definition.getUrl());
				subscriberSecurityUsernameField.setValue(definition.getSecurityUsername());
				subscriberSecurityPasswordField.setValue(definition.getSecurityPassword());
				subscriberSecurityTokenField.setValue(definition.getSecurityToken());
				subscriberSecurityBearerTokenField.setValue(definition.getSecurityBearerToken());
			} else {
				subscriberNameField.clearValue();
				subscriberUrlField.clearValue();
				subscriberSecurityUsernameField.clearValue();
				subscriberSecurityPasswordField.clearValue();
				subscriberSecurityTokenField.clearValue();
				subscriberSecurityBearerTokenField.clearValue();
			}
		}

		/** dialog -> definition */
		public WebHookSubscriber getEditDefinition(WebHookSubscriber definition) {
			definition.setSubscriberName(SmartGWTUtil.getStringValue(subscriberNameField));
			definition.setUrl(SmartGWTUtil.getStringValue(subscriberUrlField));
			definition.setSecurityUsername(SmartGWTUtil.getStringValue(subscriberSecurityUsernameField)==null?"":SmartGWTUtil.getStringValue(subscriberSecurityUsernameField));
			definition.setSecurityPassword(SmartGWTUtil.getStringValue(subscriberSecurityPasswordField)==null?"":SmartGWTUtil.getStringValue(subscriberSecurityPasswordField));
			definition.setSecurityToken(SmartGWTUtil.getStringValue(subscriberSecurityTokenField)==null?"":SmartGWTUtil.getStringValue(subscriberSecurityTokenField));
			definition.setSecurityBearerToken(SmartGWTUtil.getStringValue(subscriberSecurityBearerTokenField)==null?"":SmartGWTUtil.getStringValue(subscriberSecurityBearerTokenField));
			return definition;
		}
		
		public boolean isSubscriberUrlFieldEmpty() {
			if (SmartGWTUtil.getStringValue(subscriberUrlField)==null||SmartGWTUtil.getStringValue(subscriberUrlField).replaceAll("\\s+","").isEmpty()) {
				return true;
			}
			return false;
		}
	}
	
	/**
	 * {@link DataChangedHandler} を追加します。
	 *
	 * @param handler {@link DataChangedHandler}
	 */
	public void addDataChangeHandler(DataChangedHandler handler) {
		handlers.add(0, handler);
	}
	
	/**
	 * データ変更通知処理
	 */
	private void fireDataChanged(WebHookSubscriber paramMap) {
		DataChangedEvent event = new DataChangedEvent();
		event.setValueObject(paramMap);
		for (DataChangedHandler handler : handlers) {
			handler.onDataChanged(event);
		}
	}

}
