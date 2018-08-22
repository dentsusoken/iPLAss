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

package org.iplass.adminconsole.client.metadata.ui.template;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogCondition;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogHandler;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogMode;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.MetaDataUtil;
import org.iplass.mtp.definition.LocalizedStringDefinition;
import org.iplass.mtp.web.template.definition.HtmlTemplateDefinition;
import org.iplass.mtp.web.template.definition.TemplateDefinition;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.SpacerItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;

public class HtmlTemplateEditPane extends TemplateTypeEditPane implements HasEditLocalizedStringDefinition {

	/** フォーム */
	private DynamicForm form;

	/** ソース */
	private TextAreaItem sourceField;

	/**
	 * コンストラクタ
	 */
	public HtmlTemplateEditPane() {

		//レイアウト設定
		setWidth100();
		setMargin(5);

		//入力部分
		form = new DynamicForm();
		form.setWidth100();
		form.setHeight100();
		form.setNumCols(3);
		form.setColWidths(100, "*", "*");
		form.setMargin(5);

		ButtonItem editScript = new ButtonItem("editScript", "Edit");
		editScript.setWidth(100);
		editScript.setStartRow(false);
		editScript.setColSpan(3);
		editScript.setAlign(Alignment.RIGHT);
		editScript.setPrompt(SmartGWTUtil.getHoverString(AdminClientMessageUtil.getString("ui_metadata_template_HtmlTemplateEditPane_displayEditDialogSource")));
		editScript.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				MetaDataUtil.showScriptEditDialog(ScriptEditorDialogMode.HTML,
						SmartGWTUtil.getStringValue(sourceField),
						ScriptEditorDialogCondition.TEMPLATE_HTML,
						"ui_metadata_template_HtmlTemplateEditPane_scriptHint",
						null,
						new ScriptEditorDialogHandler() {

							@Override
							public void onSave(String text) {
								sourceField.setValue(text);
							}
							@Override
							public void onCancel() {
							}
						});
			}
		});

		sourceField = new TextAreaItem("source", "Source");
		sourceField.setColSpan(2);
		sourceField.setWidth("100%");
		sourceField.setHeight("100%");
		SmartGWTUtil.setRequired(sourceField);

		form.setItems(new SpacerItem(), new SpacerItem(), editScript, sourceField);

		//配置
		addMember(form);

		SmartGWTUtil.setReadOnlyTextArea(sourceField);
	}

	@Override
	public void setDefinition(TemplateDefinition definition) {
		HtmlTemplateDefinition htmlDefinition = (HtmlTemplateDefinition)definition;
		sourceField.setValue(htmlDefinition.getSource());
	}

	@Override
	public TemplateDefinition getEditDefinition(TemplateDefinition definition) {
		HtmlTemplateDefinition htmlDefinition = (HtmlTemplateDefinition)definition;
		htmlDefinition.setSource(SmartGWTUtil.getStringValue(sourceField));
		return htmlDefinition;
	}

	@Override
	public boolean validate() {
		return form.validate();
	}

	@Override
	public boolean isFileUpload() {
		return false;
	}

	@Override
	public void setLocalizedStringDefinition(LocalizedStringDefinition definition) {
		sourceField.setValue(definition.getStringValue());
	}

	@Override
	public LocalizedStringDefinition getEditLocalizedStringDefinition(LocalizedStringDefinition definition) {
		definition.setStringValue(SmartGWTUtil.getStringValue(sourceField));
		return definition;
	}

}
