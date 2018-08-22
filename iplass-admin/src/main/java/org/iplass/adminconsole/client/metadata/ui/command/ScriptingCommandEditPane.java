/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogCondition;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogHandler;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogMode;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.MetaDataUtil;
import org.iplass.mtp.command.definition.CommandDefinition;
import org.iplass.mtp.command.definition.ScriptingCommandDefinition;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.SpacerItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;

public class ScriptingCommandEditPane extends CommandTypeEditPane {

	/** フォーム */
	private DynamicForm form;

	/** スクリプト */
	private TextAreaItem scriptField;

	/**
	 * コンストラクタ
	 */
	public ScriptingCommandEditPane() {

		//レイアウト設定
		setWidth100();
		setMargin(5);
		setMembersMargin(10);

		//入力部分
		form = new DynamicForm();
		form.setWidth100();
		form.setHeight100();
//		form.setNumCols(5);	//間延びしないように最後に１つ余分に作成
//		form.setColWidths(100, "*", 100, "*", "*");
		form.setNumCols(3);	//間延びしないように最後に１つ余分に作成
		form.setColWidths(50, "*", "*");

		ButtonItem editScript = new ButtonItem("editScript", "Edit");
		editScript.setWidth(100);
		editScript.setStartRow(false);
		editScript.setColSpan(3);
		editScript.setAlign(Alignment.RIGHT);
		editScript.setPrompt(SmartGWTUtil.getHoverString(AdminClientMessageUtil.getString("ui_metadata_command_ScriptingCommandEditPane_displayDialogEditScript")));
		editScript.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				MetaDataUtil.showScriptEditDialog(ScriptEditorDialogMode.GROOVY_SCRIPT,
						SmartGWTUtil.getStringValue(scriptField),
						ScriptEditorDialogCondition.COMMAND_SCRIPT,
						"ui_metadata_command_ScriptingCommandEditPane_scriptHint",
						null,
						new ScriptEditorDialogHandler() {

							@Override
							public void onSave(String text) {
								scriptField.setValue(text);
							}
							@Override
							public void onCancel() {
							}
						});
			}
		});

		scriptField = new TextAreaItem("script", "Script");
		scriptField.setStartRow(true);
		scriptField.setColSpan(2);
		scriptField.setWidth("100%");
		scriptField.setHeight("100%");
		SmartGWTUtil.setRequired(scriptField);

		form.setItems(new SpacerItem(), new SpacerItem(), editScript, scriptField);

		//配置
		addMember(form);

		SmartGWTUtil.setReadOnlyTextArea(scriptField);
	}

	/**
	 * Commandを展開します。
	 *
	 * @param definition CommandDefinition
	 */
	@Override
	public void setDefinition(CommandDefinition definition) {
		ScriptingCommandDefinition scrDefinition = (ScriptingCommandDefinition)definition;
		scriptField.setValue(scrDefinition.getScript());
	}

	/**
	 * 編集されたCommandDefinition情報を返します。
	 *
	 * @return 編集CommandDefinition情報
	 */
	@Override
	public CommandDefinition getEditDefinition(CommandDefinition definition) {
		ScriptingCommandDefinition scrDefinition = (ScriptingCommandDefinition)definition;
		scrDefinition.setScript(SmartGWTUtil.getStringValue(scriptField));
		return scrDefinition;
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
