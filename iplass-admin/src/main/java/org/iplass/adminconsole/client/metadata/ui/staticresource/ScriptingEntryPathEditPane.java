/*
 * Copyright (C) 2016 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.staticresource;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogCondition;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogHandler;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogMode;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.MetaDataUtil;
import org.iplass.adminconsole.shared.metadata.dto.staticresource.StaticResourceInfo;
import org.iplass.mtp.web.staticresource.definition.ScriptingEntryPathTranslatorDefinition;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.SpacerItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;

public class ScriptingEntryPathEditPane extends EntryPathTypeEditPane<ScriptingEntryPathTranslatorDefinition> {

	/** フォーム */
	private DynamicForm form;

	/** スクリプト */
	private TextAreaItem scriptField;

	/**
	 * コンストラクタ
	 */
	public ScriptingEntryPathEditPane() {
		//レイアウト設定
		setWidth100();
		setMargin(5);
		setMembersMargin(10);

		//入力部分
		form = new DynamicForm();
		form.setWidth100();
		form.setHeight100();
		form.setNumCols(3);	//間延びしないように最後に１つ余分に作成
		form.setColWidths(50, "*", "*");

		ButtonItem editScript = new ButtonItem("editScript", "Edit");
		editScript.setWidth(100);
		editScript.setStartRow(false);
		editScript.setColSpan(3);
		editScript.setAlign(Alignment.RIGHT);
		editScript.setPrompt(SmartGWTUtil.getHoverString(AdminClientMessageUtil.getString("ui_metadata_staticresource_ScriptingEntryPathEditPane_displayDialogEditScript")));
		editScript.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				MetaDataUtil.showScriptEditDialog(ScriptEditorDialogMode.GROOVY_SCRIPT,
						SmartGWTUtil.getStringValue(scriptField),
						ScriptEditorDialogCondition.COMMAND_SCRIPT,
						"ui_metadata_staticresource_ScriptingEntryPathEditPane_scriptHint",
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

		form.setItems(new SpacerItem(), new SpacerItem(), editScript, scriptField);

		//配置
		addMember(form);

		SmartGWTUtil.setReadOnlyTextArea(scriptField);
	}

	@Override
	public void setDefinition(ScriptingEntryPathTranslatorDefinition definition) {
		scriptField.setValue(definition.getScript());
	}

	@Override
	public void setDefinition(StaticResourceInfo definition) {
		setDefinition((ScriptingEntryPathTranslatorDefinition) definition.getEntryPathTranslator());
	}

	@Override
	public ScriptingEntryPathTranslatorDefinition getEditDefinition(ScriptingEntryPathTranslatorDefinition definition) {
		definition.setScript(SmartGWTUtil.getStringValue(scriptField));
		return definition;
	}

	@Override
	public StaticResourceInfo getEditDefinition(StaticResourceInfo definition) {
		definition.setEntryPathTranslator(getEditDefinition(new ScriptingEntryPathTranslatorDefinition()));
		return definition;
	}

	@Override
	public boolean validate() {
		return form.validate();
	}

}
