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

import org.iplass.adminconsole.client.base.ui.widget.MetaDataSelectItem;
import org.iplass.adminconsole.client.base.ui.widget.MetaDataSelectItem.ItemOption;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpForm;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpSelectItem;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpTextItem;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.mtp.web.actionmapping.definition.ActionMappingDefinition;
import org.iplass.mtp.web.actionmapping.definition.result.ContentDispositionType;
import org.iplass.mtp.web.actionmapping.definition.result.ResultDefinition;
import org.iplass.mtp.web.actionmapping.definition.result.TemplateResultDefinition;
import org.iplass.mtp.web.template.definition.TemplateDefinition;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;

public class TemplateResultEditPane extends ResultTypeEditPane {

	/** フォーム */
	private DynamicForm form;

	/** テンプレート名 */
	private SelectItem templateField;
	/** レイアウトAction */
	private SelectItem layoutActionNameField;

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
	public TemplateResultEditPane() {

		setWidth100();

		//入力部分
		form = new MtpForm();
		form.setAutoHeight();

		templateField = new MetaDataSelectItem(TemplateDefinition.class);
		templateField.setTitle("Template");
		SmartGWTUtil.setRequired(templateField);

		layoutActionNameField = new MetaDataSelectItem(ActionMappingDefinition.class, new ItemOption(true, false));
		layoutActionNameField.setTitle("Layout Action");

		form.setItems(templateField, layoutActionNameField);

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
	 * @param definition TemplateResultDefinition
	 */
	@Override
	public void setDefinition(ResultDefinition definition) {
		TemplateResultDefinition template = (TemplateResultDefinition)definition;
		templateField.setValue(template.getTemplateName());
		layoutActionNameField.setValue(template.getLayoutActionName());
		useContentDispositionField.setValue(template.isUseContentDisposition());
		if (template.getContentDispositionType() != null) {
			contentDispositionTypeField.setValue(template.getContentDispositionType().name());
		} else {
			contentDispositionTypeField.setValue("");
		}
		fileNameAttributeNameField.setValue(template.getFileNameAttributeName());
	}

	/**
	 * 編集されたResultDefinition情報を返します。
	 *
	 * @return 編集ResultDefinition情報
	 */
	@Override
	public ResultDefinition getEditDefinition(ResultDefinition definition) {
		TemplateResultDefinition template = (TemplateResultDefinition)definition;
		template.setTemplateName(SmartGWTUtil.getStringValue(templateField));
		template.setLayoutActionName(SmartGWTUtil.getStringValue(layoutActionNameField));
		template.setUseContentDisposition(SmartGWTUtil.getBooleanValue(useContentDispositionField));
		String contentDispositionTypeName = SmartGWTUtil.getStringValue(contentDispositionTypeField);
		if (contentDispositionTypeName != null && !contentDispositionTypeName.isEmpty()) {
			template.setContentDispositionType(ContentDispositionType.valueOf(contentDispositionTypeName));
		} else {
			template.setContentDispositionType(null);
		}
		template.setFileNameAttributeName(SmartGWTUtil.getStringValue(fileNameAttributeNameField));
		return template;
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
