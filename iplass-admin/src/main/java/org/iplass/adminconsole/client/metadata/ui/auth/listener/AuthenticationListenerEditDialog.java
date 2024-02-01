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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.iplass.adminconsole.client.base.event.DataChangedEvent;
import org.iplass.adminconsole.client.base.event.DataChangedHandler;
import org.iplass.adminconsole.client.base.ui.widget.MtpDialog;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpForm;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpSelectItem;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.mtp.auth.policy.definition.AccountNotificationListenerDefinition;

import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;

public class AuthenticationListenerEditDialog extends MtpDialog {

	/** AuthenticationListenerの種類選択用Map */
	private static LinkedHashMap<String, String> typeMap;
	static {
		typeMap = new LinkedHashMap<String, String>();
		for (AuthenticationListenerType type : AuthenticationListenerType.values()) {
			typeMap.put(type.name(), type.displayName());
		}
	}

	/** 種類 */
	private SelectItem slctTypeField;

	/** 個別属性部分 */
	private AuthenticationListenerTypeEditPane typeEditPane;

	/** 編集対象 */
	private AccountNotificationListenerDefinition curDefinition;

	/** データ変更ハンドラ */
	private List<DataChangedHandler> handlers = new ArrayList<DataChangedHandler>();

	public AuthenticationListenerEditDialog() {

		setHeight(590);
		setTitle("Authentication Listener Setting");
		centerInPage();

		slctTypeField = new MtpSelectItem("type", "Type");
		slctTypeField.setValueMap(typeMap);
		SmartGWTUtil.setRequired(slctTypeField);
		slctTypeField.addChangedHandler(new ChangedHandler() {

			@Override
			public void onChanged(ChangedEvent event) {
				typeChanged(AuthenticationListenerType.valueOf(SmartGWTUtil.getStringValue(slctTypeField, true)));
			}
		});

		final DynamicForm form = new MtpForm();
		form.setAutoHeight();
		form.setItems(slctTypeField);

		container.addMember(form);

		IButton save = new IButton("OK");
		save.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				boolean commonValidate = form.validate();
				boolean typeValidate = typeEditPane.validate();
				if (commonValidate && typeValidate) {
					saveDefinition();
				}
			}
		});

		IButton cancel = new IButton("Cancel");
		cancel.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				destroy();
			}
		});

		footer.setMembers(save, cancel);
	}

	/**
	 * 編集対象の {@link AccountNotificationListenerDefinition} を設定します。
	 * @param definition 編集対象の {@link AccountNotificationListenerDefinition}
	 */
	public void setDefinition(AccountNotificationListenerDefinition definition) {
		this.curDefinition = definition;

		slctTypeField.setValue(AuthenticationListenerType.valueOf(definition).name());

		if (typeEditPane != null) {
			if (container.contains(typeEditPane)) {
				container.removeMember(typeEditPane);
			}
			typeEditPane = null;
		}

		if (definition != null) {
			AuthenticationListenerType type = AuthenticationListenerType.valueOf(definition);
			typeEditPane = AuthenticationListenerType.typeOfEditPane(type);
			typeEditPane.setDefinition(definition);
			container.addMember(typeEditPane);
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

	private void typeChanged(AuthenticationListenerType type) {
		AccountNotificationListenerDefinition newDefinition = null;
		if (type != null) {
			//タイプにあったDefinitionを取得
			newDefinition = AuthenticationListenerType.typeOfDefinition(type);
		}

		setDefinition(newDefinition);

	}

	private void saveDefinition() {
		getEditedDefinition();
	}

	private void getEditedDefinition() {
		//Comboは必須なので必ずcurDefinitionは生成済み
		curDefinition = typeEditPane.getEditDefinition(curDefinition);

		//データ変更を通知
		fireDataChanged(curDefinition);

		//ダイアログ消去
		destroy();
	}

	/**
	 * データ変更通知処理
	 */
	private void fireDataChanged(AccountNotificationListenerDefinition definition) {
		DataChangedEvent event = new DataChangedEvent();
		event.setValueObject(definition);
		for (DataChangedHandler handler : handlers) {
			handler.onDataChanged(event);
		}
	}
}
