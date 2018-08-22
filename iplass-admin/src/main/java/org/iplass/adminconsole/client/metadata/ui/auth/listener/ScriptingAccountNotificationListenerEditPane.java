/*
 * Copyright (C) 2014 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.auth.listener;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogCondition;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogHandler;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogMode;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.MetaDataUtil;
import org.iplass.mtp.auth.policy.definition.AccountNotificationListenerDefinition;
import org.iplass.mtp.auth.policy.definition.NotificationType;
import org.iplass.mtp.auth.policy.definition.listeners.ScriptingAccountNotificationListenerDefinition;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.SpacerItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;

public class ScriptingAccountNotificationListenerEditPane extends AuthenticationListenerTypeEditPane {

	private DynamicForm scriptForm;
	private DynamicForm notificationTypeForm;

	private TextAreaItem txtScriptField;

	private Map<NotificationType, CheckboxItem> mapTypeCheckBox;

	public ScriptingAccountNotificationListenerEditPane() {

		setWidth100();
		setHeight100();

		scriptForm = new DynamicForm();
		scriptForm.setMargin(5);
		scriptForm.setAutoHeight();
		scriptForm.setWidth100();
		scriptForm.setHeight100();
		scriptForm.setNumCols(3);
		scriptForm.setColWidths(100, "*", 105);

		ButtonItem editScript = new ButtonItem("editScript", "Edit");
		editScript.setWidth(100);
		editScript.setStartRow(false);
		editScript.setPrompt(SmartGWTUtil.getHoverString(AdminClientMessageUtil.getString("ui_metadata_auth_listener_ScriptingAccountNotificationListenerEditPane_dispScriptEditDialog")));
		editScript.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

			@Override
			public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
				MetaDataUtil.showScriptEditDialog(ScriptEditorDialogMode.GROOVY_SCRIPT,
				SmartGWTUtil.getStringValue(txtScriptField, true),
				ScriptEditorDialogCondition.AUTHENTICATION_LISTENER_NOTIFICATION_SCRIPT, null,
				AdminClientMessageUtil.getString("ui_metadata_auth_listener_ScriptingAccountNotificationListenerEditPane_scriptComment"),
				new ScriptEditorDialogHandler() {

					@Override
					public void onSave(String text) {
						txtScriptField.setValue(text);
					}

					@Override
					public void onCancel() {
					}
				});
			}
		});

		txtScriptField = new TextAreaItem("script", "Script");
		txtScriptField.setColSpan(2);
		txtScriptField.setWidth("100%");
		txtScriptField.setHeight("100%");
		SmartGWTUtil.setReadOnlyTextArea(txtScriptField);

		scriptForm.setItems(new SpacerItem(), new SpacerItem(), editScript, txtScriptField);

		notificationTypeForm = new DynamicForm();
		notificationTypeForm.setMargin(5);
		notificationTypeForm.setAutoHeight();
		notificationTypeForm.setWidth100();
		notificationTypeForm.setNumCols(3);
		notificationTypeForm.setColWidths(150, 190, "*");
		notificationTypeForm.setIsGroup(true);
		notificationTypeForm.setGroupTitle("Notification Type");

		mapTypeCheckBox = new LinkedHashMap<>();
		for (NotificationTypeInfo info : NotificationTypeInfo.values()) {
			CheckboxItem checkbox = new CheckboxItem();
			checkbox.setShowTitle(false);
			checkbox.setTitle(info.displayName);
			mapTypeCheckBox.put(info.notificationType, checkbox);
		}
		notificationTypeForm.setItems(mapTypeCheckBox.values().toArray(new CheckboxItem[0]));

		addMember(scriptForm);
		addMember(notificationTypeForm);
	}

	@Override
	public void setDefinition(AccountNotificationListenerDefinition definition) {
		ScriptingAccountNotificationListenerDefinition listener = (ScriptingAccountNotificationListenerDefinition)definition;
		txtScriptField.setValue(listener.getScript());

		if (listener.getListenNotification() != null) {
			for (NotificationType type : listener.getListenNotification()) {
				mapTypeCheckBox.get(type).setValue(true);
			}
		}
	}

	@Override
	public AccountNotificationListenerDefinition getEditDefinition(AccountNotificationListenerDefinition definition) {
		ScriptingAccountNotificationListenerDefinition listener = (ScriptingAccountNotificationListenerDefinition)definition;
		listener.setScript(SmartGWTUtil.getStringValue(txtScriptField, true));

		List<NotificationType> listenNotification = new ArrayList<NotificationType>();

		for (Entry<NotificationType, CheckboxItem> entry : mapTypeCheckBox.entrySet()) {
			if (SmartGWTUtil.getBooleanValue(entry.getValue())) {
				listenNotification.add(entry.getKey());
			}
		}
		listener.setListenNotification(listenNotification);

		return listener;
	}

	@Override
	public boolean validate() {
		return scriptForm.validate();
	}

	private enum NotificationTypeInfo {

		CREATED("Created", NotificationType.CREATED),
		CREDENTIAL_RESET("Credential Reset", NotificationType.CREDENTIAL_RESET),
		ROCKEDOUT("RockedOut", NotificationType.ROCKEDOUT),
		CREDENTIAL_UPDATED("Credential Updated", NotificationType.CREDENTIAL_UPDATED),
		PROPERTY_UPDATED("Property Updated", NotificationType.PROPERTY_UPDATED),
		REMOVE("Remove", NotificationType.REMOVE),
		LOGIN_SUCCESS("Login Success", NotificationType.LOGIN_SUCCESS),
		LOGIN_FAILED("Login Failed", NotificationType.LOGIN_FAILED)
		;

		private String displayName;
		private NotificationType notificationType;

		private NotificationTypeInfo(String displayName, NotificationType notificationType) {
			this.displayName = displayName;
			this.notificationType = notificationType;
		}

	}

}
