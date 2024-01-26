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

package org.iplass.adminconsole.client.metadata.ui.action.result;

import java.util.LinkedHashMap;

import org.iplass.adminconsole.client.base.ui.widget.form.MtpForm;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpSelectItem;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpTextItem;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.mtp.web.actionmapping.definition.result.ContentDispositionType;
import org.iplass.mtp.web.actionmapping.definition.result.DynamicTemplateResultDefinition;
import org.iplass.mtp.web.actionmapping.definition.result.ResultDefinition;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;

public class DynamicTemplateResultEditPane extends ResultTypeEditPane {

	/** フォーム */
	private DynamicForm form;

	/** テンプレート名が入っているAttribute名 */
	private TextItem attributeNameField;
	/** レイアウトAction名が入っているAttribute名 */
	private TextItem  layoutActionAttributeNameField;

	private DynamicForm contentDispositionForm;

	/** ContentDispotisionを利用するかのフラグ */
	private CheckboxItem useContentDispositionField;

	/** ContentDispotision Type */
	private SelectItem contentDispositionTypeField;

	/** ファイル名が入っているAttribute名 */
	private TextItem fileNameAttributeNameField;

	/**
	 * コンストラクタ
	 */
	public DynamicTemplateResultEditPane() {

		setWidth100();

		//入力部分
		form = new MtpForm();
		form.setAutoHeight();

		attributeNameField = new MtpTextItem("attributeName", "Template AttributeName");
		SmartGWTUtil.setRequired(attributeNameField);

		layoutActionAttributeNameField = new MtpTextItem();
		layoutActionAttributeNameField.setTitle("Layout Action AttributeName");

		form.setItems(attributeNameField, layoutActionAttributeNameField);

		contentDispositionForm = new MtpForm();
		contentDispositionForm.setAutoHeight();
		contentDispositionForm.setIsGroup(true);
		contentDispositionForm.setGroupTitle("Content Disposition Setting");

		useContentDispositionField = new CheckboxItem("useContentDisposition", "Set ContentDisposition");

		contentDispositionTypeField = new MtpSelectItem();
		contentDispositionTypeField.setTitle("Content Disposition Type");

		LinkedHashMap<String, String> contentDispositionTypeMap = new LinkedHashMap<String, String>();
		contentDispositionTypeMap.put("", "Default");
		contentDispositionTypeMap.put(ContentDispositionType.ATTACHMENT.name(), "Attachment");
		contentDispositionTypeMap.put(ContentDispositionType.INLINE.name(), "Inline");
		contentDispositionTypeField.setValueMap(contentDispositionTypeMap);

		fileNameAttributeNameField = new MtpTextItem("fileNameAttributeName", "FileName AttributeName");

		contentDispositionForm.setItems(useContentDispositionField, contentDispositionTypeField, fileNameAttributeNameField);

		//配置
		addMember(form);
		addMember(contentDispositionForm);
	}

	/**
	 * ResultDefinitionを展開します。
	 *
	 * @param definition DynamicTemplateResultDefinition
	 */
	@Override
	public void setDefinition(ResultDefinition definition) {
		DynamicTemplateResultDefinition dynamic = (DynamicTemplateResultDefinition)definition;
		attributeNameField.setValue(dynamic.getTemplatePathAttributeName());
		layoutActionAttributeNameField.setValue(dynamic.getLayoutActionAttributeName());
		useContentDispositionField.setValue(dynamic.isUseContentDisposition());
		if (dynamic.getContentDispositionType() != null) {
			contentDispositionTypeField.setValue(dynamic.getContentDispositionType().name());
		} else {
			contentDispositionTypeField.setValue("");
		}
		fileNameAttributeNameField.setValue(dynamic.getFileNameAttributeName());
	}

	/**
	 * 編集されたResultDefinition情報を返します。
	 *
	 * @return 編集ResultDefinition情報
	 */
	@Override
	public ResultDefinition getEditDefinition(ResultDefinition definition) {
		DynamicTemplateResultDefinition dynamic = (DynamicTemplateResultDefinition)definition;
		dynamic.setTemplatePathAttributeName(SmartGWTUtil.getStringValue(attributeNameField));
		dynamic.setLayoutActionAttributeName(SmartGWTUtil.getStringValue(layoutActionAttributeNameField));
		dynamic.setUseContentDisposition(SmartGWTUtil.getBooleanValue(useContentDispositionField));
		String contentDispositionTypeName = SmartGWTUtil.getStringValue(contentDispositionTypeField);
		if (contentDispositionTypeName != null && !contentDispositionTypeName.isEmpty()) {
			dynamic.setContentDispositionType(ContentDispositionType.valueOf(contentDispositionTypeName));
		} else {
			dynamic.setContentDispositionType(null);
		}
		dynamic.setFileNameAttributeName(SmartGWTUtil.getStringValue(fileNameAttributeNameField));
		return dynamic;
	}

	/**
	 * 入力チェックを実行します。
	 *
	 * @return 入力チェック結果
	 */
	@Override
	public boolean validate() {
		boolean formValidate = form.validate();
		boolean contentDispositionFormValidate = contentDispositionForm.validate();
		return formValidate && contentDispositionFormValidate;
	}

}
