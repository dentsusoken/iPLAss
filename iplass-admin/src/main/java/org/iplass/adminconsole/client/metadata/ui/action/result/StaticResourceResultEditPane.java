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

package org.iplass.adminconsole.client.metadata.ui.action.result;

import java.util.LinkedHashMap;

import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.data.MetaDataNameDS;
import org.iplass.mtp.web.actionmapping.definition.result.ContentDispositionType;
import org.iplass.mtp.web.actionmapping.definition.result.ResultDefinition;
import org.iplass.mtp.web.actionmapping.definition.result.StaticResourceResultDefinition;
import org.iplass.mtp.web.staticresource.definition.StaticResourceDefinition;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;

public class StaticResourceResultEditPane extends ResultTypeEditPane {

	/** フォーム */
	private DynamicForm form;

	/** 静的リソース名 */
	private SelectItem staticResourceField;

	private DynamicForm contentDispositionForm;

	/** ContentDispotisionを利用するかのフラグ */
	private CheckboxItem useContentDispositionField;

	/** ContentDispotision Type */
	private SelectItem contentDispositionTypeField;

	/** EntryPathAttributeName */
	private TextItem entryPathAttributeNameField;

	/**
	 * コンストラクタ
	 */
	public StaticResourceResultEditPane() {
		setWidth100();
		setHeight100();	//OK、Cancelボタンを下に持っていくため

		//入力部分
		form = new DynamicForm();
		form.setMargin(5);
		form.setAutoHeight();
		form.setWidth100();
		form.setNumCols(4);	//間延びしないように最後に１つ余分に作成
		form.setColWidths(100, 250, 40, "*");

		staticResourceField = new SelectItem("staticResource", "StaticResource");
		staticResourceField.setWidth(250);

		MetaDataNameDS.setDataSource(staticResourceField, StaticResourceDefinition.class);
		SmartGWTUtil.setRequired(staticResourceField);

		//#19232
//		MetaDataViewButtonItem staticResourceMetaButton = new MetaDataViewButtonItem(StaticResourceDefinition.class.getName());
//		staticResourceMetaButton.setPrompt(SmartGWTUtil.getHoverString("view the selected static resource"));
//		staticResourceMetaButton.setMetaDataShowClickHandler(
//				new MetaDataViewButtonItem.MetaDataShowClickHandler() {
//			@Override
//			public String targetDefinitionName() {
//				return SmartGWTUtil.getStringValue(staticResourceField);
//			}
//		});

//		form.setItems(staticResourceField, staticResourceMetaButton);
		form.setItems(staticResourceField);

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

		entryPathAttributeNameField = new TextItem("entryPathAttributeName", "EntryPath AttributeName");
		entryPathAttributeNameField.setWidth(250);

		contentDispositionForm.setItems(useContentDispositionField, contentDispositionTypeField, entryPathAttributeNameField);

		//配置
		addMember(form);
		addMember(contentDispositionForm);
	}

	@Override
	public void setDefinition(ResultDefinition definition) {
		StaticResourceResultDefinition staticResource = (StaticResourceResultDefinition)definition;

		staticResourceField.setValue(staticResource.getStaticResourceName());
		useContentDispositionField.setValue(staticResource.isUseContentDisposition());

		if (staticResource.getContentDispositionType() != null) {
			contentDispositionTypeField.setValue(staticResource.getContentDispositionType().name());
		} else {
			contentDispositionTypeField.setValue("");
		}

		entryPathAttributeNameField.setValue(staticResource.getEntryPathAttributeName());
	}

	@Override
	public ResultDefinition getEditDefinition(ResultDefinition definition) {
		StaticResourceResultDefinition staticResource = (StaticResourceResultDefinition)definition;

		staticResource.setStaticResourceName(SmartGWTUtil.getStringValue(staticResourceField));
		staticResource.setUseContentDisposition(SmartGWTUtil.getBooleanValue(useContentDispositionField));

		String contentDispositionTypeName = SmartGWTUtil.getStringValue(contentDispositionTypeField);
		if (contentDispositionTypeName != null && !contentDispositionTypeName.isEmpty()) {
			staticResource.setContentDispositionType(ContentDispositionType.valueOf(contentDispositionTypeName));
		} else {
			staticResource.setContentDispositionType(null);
		}

		staticResource.setEntryPathAttributeName(SmartGWTUtil.getStringValue(entryPathAttributeNameField));

		return staticResource;
	}

	@Override
	public boolean validate() {
		boolean formValidate = form.validate();
		boolean contentDispositionFormValidate = contentDispositionForm.validate();
		return formValidate && contentDispositionFormValidate;
	}

}
