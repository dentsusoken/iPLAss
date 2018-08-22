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

import java.util.LinkedHashMap;

import org.iplass.adminconsole.client.base.ui.widget.MetaDataViewButtonItem;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.data.MetaDataNameDS;
import org.iplass.adminconsole.client.metadata.data.MetaDataNameDS.MetaDataNameDSOption;
import org.iplass.mtp.web.actionmapping.definition.ActionMappingDefinition;
import org.iplass.mtp.web.template.definition.TemplateDefinition;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * テンプレート属性編集パネル
 *
 */
public class TemplateAttributePane extends VLayout {

	/** Templateの種類選択用Map */
	private static LinkedHashMap<String, String> typeMap;
	static {
		typeMap = new LinkedHashMap<String, String>();
		for (TemplateType type : TemplateType.values()) {
			typeMap.put(type.name(), type.displayName());
		}
	}

	private DynamicForm commonForm;
	/** コンテキストタイプ */
	private TextItem contentTypeField;
	/** レイアウトAction */
	private SelectItem layoutActionNameField;

	private DynamicForm typeForm;
	/** 種類 */
	private SelectItem typeField;

	/**
	 * コンストラクタ
	 */
	public TemplateAttributePane() {

		setAutoHeight();
		setMargin(5);

		//入力部分
		commonForm = new DynamicForm();
		commonForm.setWidth100();
		commonForm.setNumCols(5);	//間延びしないように最後に１つ余分に作成
		commonForm.setColWidths(100, "*", 100, "*", "*");
		commonForm.setMargin(5);

		contentTypeField = new TextItem("contentType", "Content Type");
		contentTypeField.setWidth("100%");

		layoutActionNameField = new SelectItem("layoutAction", "Layout Action");
		layoutActionNameField.setWidth("100%");
		MetaDataNameDS.setDataSource(layoutActionNameField, ActionMappingDefinition.class, new MetaDataNameDSOption(true, false));

		MetaDataViewButtonItem layoutActionMetaButton = new MetaDataViewButtonItem(ActionMappingDefinition.class.getName());
		layoutActionMetaButton.setPrompt(SmartGWTUtil.getHoverString("view the selected layout action"));
		layoutActionMetaButton.setMetaDataShowClickHandler(new MetaDataViewButtonItem.MetaDataShowClickHandler() {
			@Override
			public String targetDefinitionName() {
				return SmartGWTUtil.getStringValue(layoutActionNameField);
			}
		});

		commonForm.setItems(contentTypeField, layoutActionNameField, layoutActionMetaButton);

		typeForm = new DynamicForm();
		typeForm.setWidth100();
		typeForm.setNumCols(5);	//間延びしないように最後に１つ余分に作成
		typeForm.setColWidths(100, "*", 100, "*", "*");
		typeForm.setMargin(5);

		typeField = new SelectItem("type", "Type");
		typeField.setValueMap(typeMap);
		SmartGWTUtil.setRequired(typeField);

		typeForm.setItems(typeField);

		//配置
		addMember(commonForm);
		addMember(SmartGWTUtil.separator());
		addMember(typeForm);

	}

	/**
	 * Templateを展開します。
	 *
	 * @param definition TemplateDefinition
	 */
	public void setDefinition(TemplateDefinition definition) {
		if (definition != null) {
			contentTypeField.setValue(definition.getContentType());
			layoutActionNameField.setValue(definition.getLayoutActionName());

			TemplateType type = TemplateType.valueOf(definition);
			typeField.setValue(type.name());
		} else {
			contentTypeField.clearValue();
			layoutActionNameField.clearValue();
			typeField.clearValue();
		}
	}

	/**
	 * 編集されたTemplateDefinition情報を返します。
	 *
	 * @return 編集TemplateDefinition情報
	 */
	public TemplateDefinition getEditDefinition(TemplateDefinition definition) {

		definition.setContentType(SmartGWTUtil.getStringValue(contentTypeField));
		definition.setLayoutActionName(SmartGWTUtil.getStringValue(layoutActionNameField));

		return definition;
	}

	/**
	 * 入力チェックを実行します。
	 *
	 * @return 入力チェック結果
	 */
	public boolean validate() {
		boolean commonValidate = commonForm.validate();
		boolean typeValidate = typeForm.validate();
		return commonValidate && typeValidate;
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
	public TemplateType selectedType() {
		return TemplateType.valueOf(SmartGWTUtil.getStringValue(typeField));
	}

	public void disableLayoutAction(boolean disabled) {
		if (disabled) {
			layoutActionNameField.clearValue();
		}
		layoutActionNameField.setDisabled(disabled);
	}

}
