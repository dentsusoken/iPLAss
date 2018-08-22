/*
 * Copyright (C) 2016 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.shared.metadata.dto.staticresource.StaticResourceInfo;
import org.iplass.mtp.web.staticresource.definition.JavaClassEntryPathTranslatorDefinition;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;

public class JavaClassEntryPathEditPane extends EntryPathTypeEditPane<JavaClassEntryPathTranslatorDefinition> {

	/** フォーム */
	private DynamicForm form;

	/** クラス名 */
	private TextItem classNameField;

	/**
	 * コンストラクタ
	 */
	public JavaClassEntryPathEditPane() {
		//レイアウト設定
		setWidth100();
		setMargin(5);
		setMembersMargin(10);

		//入力部分
		form = new DynamicForm();
		form.setWidth100();
		form.setNumCols(3);	//間延びしないように最後に１つ余分に作成
		form.setColWidths(100, "*", "*");

		classNameField = new TextItem("className", "Java ClassName");
		classNameField.setWidth("100%");
		classNameField.setColSpan(2);
		SmartGWTUtil.setRequired(classNameField);

		form.setItems(classNameField);

		//配置
		addMember(form);
	}

	@Override
	public void setDefinition(JavaClassEntryPathTranslatorDefinition definition) {
		this.classNameField.setValue(definition.getClassName());
	}

	@Override
	public void setDefinition(StaticResourceInfo definition) {
		setDefinition((JavaClassEntryPathTranslatorDefinition) definition.getEntryPathTranslator());
	}

	@Override
	public JavaClassEntryPathTranslatorDefinition getEditDefinition(JavaClassEntryPathTranslatorDefinition definition) {
		definition.setClassName(SmartGWTUtil.getStringValue(classNameField));
		return definition;
	}

	@Override
	public StaticResourceInfo getEditDefinition(StaticResourceInfo definition) {
		definition.setEntryPathTranslator(getEditDefinition(new JavaClassEntryPathTranslatorDefinition()));
		return definition;
	}

	@Override
	public boolean validate() {
		return form.validate();
	}

}
