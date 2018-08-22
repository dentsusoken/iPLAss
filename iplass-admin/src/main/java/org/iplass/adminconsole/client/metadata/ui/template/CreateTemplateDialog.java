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

import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.common.Callable;
import org.iplass.adminconsole.client.metadata.ui.common.MetaDataCreateDialog;
import org.iplass.mtp.web.template.definition.TemplateDefinition;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;

public class CreateTemplateDialog extends MetaDataCreateDialog {

	/** Templateの種類選択用Map */
	private static LinkedHashMap<String, String> typeMap;
	static {
		typeMap = new LinkedHashMap<String, String>();
		for (TemplateType type : TemplateType.values()) {
			typeMap.put(type.name(), type.displayName());
		}
	}

	private DynamicForm templateForm;

	private SelectItem typeField;

	public CreateTemplateDialog(String definitionClassName, String nodeDisplayName, String folderPath, boolean isCopyMode) {
		super(definitionClassName, nodeDisplayName, folderPath, isCopyMode);

		if (!isCopyMode) {
			setHeight(DEFAULT_HEIGHT + 60);

			typeField = new SelectItem("type", "Type");
			typeField.setValueMap(typeMap);
			SmartGWTUtil.setRequired(typeField);

			templateForm = createDefaultForm();
			templateForm.setIsGroup(true);
			templateForm.setGroupTitle("Template Settings");
			templateForm.setItems(typeField);

			addCustomParts(templateForm);
		}
	}

	@Override
	protected void saveAction(final SaveInfo saveInfo, final boolean isCopyMode) {
		if (templateForm != null && !templateForm.validate()) {
			return;
		}

		// 存在チェック
		checkExist(saveInfo.getName(), new Callable<Void>() {

			@Override
			public Void call() {
				createTemplate(saveInfo, isCopyMode);
				return null;
			}
		});
	}

	private void createTemplate(final SaveInfo saveInfo, final boolean isCopyMode) {

		if (isCopyMode) {
			service.copyDefinition(TenantInfoHolder.getId(), getDefinitionClassName(), getSourceName(),
					saveInfo.getName(), saveInfo.getDisplayName(), saveInfo.getDescription(), new SaveResultCallback());
		} else {
			TemplateDefinition definition = TemplateType.typeOfDefinition(TemplateType.valueOf(SmartGWTUtil.getStringValue(typeField)));

			definition.setName(saveInfo.getName());
			definition.setDisplayName(saveInfo.getDisplayName());
			definition.setDescription(saveInfo.getDescription());

			service.createDefinition(TenantInfoHolder.getId(), definition, new SaveResultCallback());
		}
	}

}
