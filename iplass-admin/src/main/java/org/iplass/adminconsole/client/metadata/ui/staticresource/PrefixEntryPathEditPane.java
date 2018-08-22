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
import org.iplass.mtp.web.staticresource.definition.PrefixEntryPathTranslatorDefinition;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;

public class PrefixEntryPathEditPane extends EntryPathTypeEditPane<PrefixEntryPathTranslatorDefinition> {

	/** フォーム */
	private DynamicForm form;

	/** クラス名 */
	private TextItem prefixField;

	/**
	 * コンストラクタ
	 */
	public PrefixEntryPathEditPane() {
		//レイアウト設定
		setWidth100();
		setMargin(5);
		setMembersMargin(10);

		//入力部分
		form = new DynamicForm();
		form.setWidth100();
		form.setNumCols(3);	//間延びしないように最後に１つ余分に作成
		form.setColWidths(100, "*", "*");

		prefixField = new TextItem("prefix", "Prefix");
		prefixField.setWidth("100%");
		prefixField.setColSpan(2);

		form.setItems(prefixField);

		//配置
		addMember(form);
	}

	@Override
	public void setDefinition(PrefixEntryPathTranslatorDefinition definition) {
		prefixField.setValue(definition.getPrefix());
	}

	@Override
	public void setDefinition(StaticResourceInfo definition) {
		setDefinition((PrefixEntryPathTranslatorDefinition) definition.getEntryPathTranslator());
	}

	@Override
	public PrefixEntryPathTranslatorDefinition getEditDefinition(PrefixEntryPathTranslatorDefinition definition) {
		definition.setPrefix(SmartGWTUtil.getStringValue(prefixField));
		return definition;
	}

	@Override
	public StaticResourceInfo getEditDefinition(StaticResourceInfo definition) {
		definition.setEntryPathTranslator(getEditDefinition(new PrefixEntryPathTranslatorDefinition()));
		return definition;
	}

	@Override
	public boolean validate() {
		return form.validate();
	}

}
