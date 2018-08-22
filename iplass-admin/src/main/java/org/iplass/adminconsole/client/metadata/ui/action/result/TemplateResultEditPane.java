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

package org.iplass.adminconsole.client.metadata.ui.action.result;

import java.util.LinkedHashMap;

import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.data.MetaDataNameDS;
import org.iplass.adminconsole.client.metadata.data.MetaDataNameDS.MetaDataNameDSOption;
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

		//レイアウト設定
		setWidth100();
		setHeight100();	//OK、Cancelボタンを下に持っていくため

		//入力部分
		form = new DynamicForm();
		form.setMargin(5);
		form.setAutoHeight();
		form.setWidth100();
		form.setNumCols(4);	//間延びしないように最後に１つ余分に作成
		form.setColWidths(100, 250, 40, "*");

		templateField = new SelectItem("template", "Template");
		templateField.setWidth(250);

		MetaDataNameDS.setDataSource(templateField, TemplateDefinition.class);
		SmartGWTUtil.setRequired(templateField);

		//#19232
//		MetaDataViewButtonItem templateMetaButton = new MetaDataViewButtonItem(TemplateDefinition.class.getName());
//		templateMetaButton.setPrompt(SmartGWTUtil.getHoverString("view the selected template"));
//		templateMetaButton.setMetaDataShowClickHandler(
//				new MetaDataViewButtonItem.MetaDataShowClickHandler() {
//			@Override
//			public String targetDefinitionName() {
//				return SmartGWTUtil.getStringValue(templateField);
//			}
//		});

		layoutActionNameField = new SelectItem("layoutAction", "Layout Action");
		layoutActionNameField.setWidth(250);
		layoutActionNameField.setStartRow(true);
		MetaDataNameDS.setDataSource(layoutActionNameField, ActionMappingDefinition.class, new MetaDataNameDSOption(true, false));

		//#19232
//		MetaDataViewButtonItem layoutActionMetaButton = new MetaDataViewButtonItem(ActionMappingDefinition.class.getName());
//		layoutActionMetaButton.setPrompt(SmartGWTUtil.getHoverString("view the selected layout action"));
//		layoutActionMetaButton.setMetaDataShowClickHandler(
//				new MetaDataViewButtonItem.MetaDataShowClickHandler() {
//			@Override
//			public String targetDefinitionName() {
//				return SmartGWTUtil.getStringValue(layoutActionNameField);
//			}
//		});

//		form.setItems(templateField, templateMetaButton, layoutActionNameField, layoutActionMetaButton);
		form.setItems(templateField, layoutActionNameField);

		contentDispositionForm = new DynamicForm();
		contentDispositionForm.setMargin(5);
		contentDispositionForm.setAutoHeight();
		contentDispositionForm.setWidth100();
		contentDispositionForm.setIsGroup(true);
		contentDispositionForm.setGroupTitle("Content Disposition Setting");

		useContentDispositionField = new CheckboxItem("useContentDisposition", "Set ContentDisposition");

		contentDispositionTypeField = new SelectItem();
		contentDispositionTypeField.setTitle("Content Disposition Type");
		contentDispositionTypeField.setWidth(150);

		LinkedHashMap<String, String> contentDispositionTypeMap = new LinkedHashMap<String, String>();
		contentDispositionTypeMap.put("", "Default");
		contentDispositionTypeMap.put(ContentDispositionType.ATTACHMENT.name(), "Attachment");
		contentDispositionTypeMap.put(ContentDispositionType.INLINE.name(), "Inline");
		contentDispositionTypeField.setValueMap(contentDispositionTypeMap);

		fileNameAttributeNameField = new TextItem("fileNameAttributeName", "FileName AttributeName");
		fileNameAttributeNameField.setWidth(250);

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
