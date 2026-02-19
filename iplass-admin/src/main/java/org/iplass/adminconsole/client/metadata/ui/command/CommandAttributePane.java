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

package org.iplass.adminconsole.client.metadata.ui.command;

import java.util.LinkedHashMap;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.mtp.command.definition.CommandDefinition;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.layout.HLayout;

/**
 * コマンド属性編集パネル
 *
 */
public class CommandAttributePane extends HLayout {

	/** Commandの種類選択用Map */
	private static LinkedHashMap<String, String> typeMap;
	static {
		typeMap = new LinkedHashMap<String, String>();
		for (CommandType type : CommandType.values()) {
			typeMap.put(type.name(), type.displayName());
		}
	}

	/** フォーム */
	private DynamicForm form;

	/** 種類 */
	private SelectItem typeField;

	/** 読み取り専用(Connenction) */
	private CheckboxItem readOnlyField;

	/** リクエストごとにインスタンス */
	private CheckboxItem newInstancePerRequestField;

	/**
	 * コンストラクタ
	 */
	public CommandAttributePane() {
		setHeight(20);
		setMargin(5);

		//入力部分
		form = new DynamicForm();
		form.setWidth100();
		form.setNumCols(9);	//間延びしないように最後に１つ余分に作成
		form.setColWidths(50, "*", "*", "*", "*", "*", "*", "*", "*");

		typeField = new SelectItem("type", "Type");
		typeField.setValueMap(typeMap);
		SmartGWTUtil.setRequired(typeField);

		readOnlyField = new CheckboxItem();
		readOnlyField.setTitle(AdminClientMessageUtil.getString("ui_metadata_command_CommandAttributePane_readOnly"));
		readOnlyField.setTooltip(SmartGWTUtil.getHoverString(AdminClientMessageUtil.getString("ui_metadata_command_CommandAttributePane_readOnlyTooltip")));

		newInstancePerRequestField = new CheckboxItem();
		newInstancePerRequestField.setTitle(AdminClientMessageUtil.getString("ui_metadata_command_CommandAttributePane_newInstancePerRequest"));
		newInstancePerRequestField.setTooltip(SmartGWTUtil.getHoverString(AdminClientMessageUtil.getString("ui_metadata_command_CommandAttributePane_newInstanceTooltip")));

		form.setFields(typeField, readOnlyField, newInstancePerRequestField);

		//配置
		addMember(form);

	}

	/**
	 * Commandを展開します。
	 *
	 * @param definition CommandDefinition
	 */
	public void setDefinition(CommandDefinition definition) {
		CommandType type = CommandType.valueOf(definition);
		typeField.setValue(type.name());
		readOnlyField.setValue(definition.isReadOnly());
		newInstancePerRequestField.setValue(definition.isNewInstancePerRequest());
	}

	/**
	 * 編集されたCommandDefinition情報を返します。
	 *
	 * @return 編集CommandDefinition情報
	 */
	public CommandDefinition getEditDefinition(CommandDefinition definition) {
		definition.setReadOnly(SmartGWTUtil.getBooleanValue(readOnlyField));
		definition.setNewInstancePerRequest(SmartGWTUtil.getBooleanValue(newInstancePerRequestField));
		return definition;
	}

	/**
	 * 入力チェックを実行します。
	 *
	 * @return 入力チェック結果
	 */
	public boolean validate() {
		return form.validate();
	}

	/**
	 * タイプ変更イベントを設定します。
	 * @param handler
	 */
	public void setTypeChangedHandler(ChangedHandler handler) {
		typeField.addChangedHandler(handler);
	}

	/**
	 * 選択されているタイプを返します。
	 * @return
	 */
	public CommandType selectedType() {
		return CommandType.valueOf(typeField.getValueAsString());
	}
}
