/*
 * Copyright (C) 2014 DENTSU SOKEN INC. All Rights Reserved.
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

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpForm;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpTextItem;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.mtp.auth.policy.definition.AccountNotificationListenerDefinition;
import org.iplass.mtp.auth.policy.definition.listeners.JavaClassAccountNotificationListenerDefinition;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;

public class JavaClassAccountNotificationListenerEditPane extends AuthenticationListenerTypeEditPane {

	/** フォーム */
	private DynamicForm form;

	/** JavaClass名 */
	private TextItem txtClassNameField;

	/**
	 * コンストラクタ
	 */
	public JavaClassAccountNotificationListenerEditPane() {

		setWidth100();

		//入力部分
		form = new MtpForm();
		form.setAutoHeight();

		txtClassNameField = new MtpTextItem();
		txtClassNameField.setTitle(AdminClientMessageUtil.getString("ui_metadata_auth_JavaClassAccountNotificationListenerEditPane_className"));
		SmartGWTUtil.setRequired(txtClassNameField);

		form.setItems(txtClassNameField);

		//配置
		addMember(form);
	}

	/**
	 * AccountNotificationListenerDefinitionを展開します。
	 *
	 * @param definition AccountNotificationListenerDefinition
	 */
	@Override
	public void setDefinition(AccountNotificationListenerDefinition definition) {
		JavaClassAccountNotificationListenerDefinition listener = (JavaClassAccountNotificationListenerDefinition)definition;
		txtClassNameField.setValue(listener.getClassName());
	}

	/**
	 * 編集されたAccountNotificationListenerDefinition情報を返します。
	 *
	 * @return 編集AccountNotificationListenerDefinition情報
	 */
	@Override
	public AccountNotificationListenerDefinition getEditDefinition(AccountNotificationListenerDefinition definition) {
		JavaClassAccountNotificationListenerDefinition listener = (JavaClassAccountNotificationListenerDefinition)definition;
		listener.setClassName(SmartGWTUtil.getStringValue(txtClassNameField, true));
		return listener;
	}

	/**
	 * 入力チェックを実行します。
	 *
	 * @return 入力チェック結果
	 */
	@Override
	public boolean validate() {
		return form.validate();
	}

}
