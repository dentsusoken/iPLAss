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

package org.iplass.adminconsole.client.metadata.ui.command;

import java.util.LinkedHashMap;

import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.common.Callable;
import org.iplass.adminconsole.client.metadata.ui.common.MetaDataCreateDialog;
import org.iplass.mtp.command.definition.CommandDefinition;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;

public class CreateCommandDialog extends MetaDataCreateDialog {

	/** Commandの種類選択用Map */
	private static LinkedHashMap<String, String> typeMap;
	static {
		typeMap = new LinkedHashMap<String, String>();
		for (CommandType type : CommandType.values()) {
			typeMap.put(type.name(), type.displayName());
		}
	}

	private DynamicForm commandForm;

	private SelectItem typeField;

	public CreateCommandDialog(String definitionClassName, String nodeDisplayName, String folderPath, boolean isCopyMode) {
		super(definitionClassName, nodeDisplayName, folderPath, isCopyMode);

		if (!isCopyMode) {
			setHeight(DEFAULT_HEIGHT + 60);

			//typeField = new SelectItem("type", MultilingualUtil.getInstance().getString("ui_metadata_command_CreateCommandDialog_type"));
			typeField = new SelectItem("type", "Type");
			typeField.setValueMap(typeMap);
			SmartGWTUtil.setRequired(typeField);

			commandForm = createDefaultForm();
			commandForm.setIsGroup(true);
			commandForm.setGroupTitle("Command Settings");
			commandForm.setItems(typeField);

			addCustomParts(commandForm);
		}
	}

	@Override
	protected void saveAction(final SaveInfo saveInfo, final boolean isCopyMode) {
		if (commandForm != null && !commandForm.validate()) {
			return;
		}

		// 存在チェック
		checkExist(saveInfo.getName(), new Callable<Void>() {

			@Override
			public Void call() {
				createCommand(saveInfo, isCopyMode);
				return null;
			}
		});
	}

	private void createCommand(final SaveInfo saveInfo, final boolean isCopyMode) {

		if (isCopyMode) {
			service.copyDefinition(TenantInfoHolder.getId(), getDefinitionClassName(), getSourceName(),
					saveInfo.getName(), saveInfo.getDisplayName(), saveInfo.getDescription(), new SaveResultCallback());
		} else {
			CommandDefinition definition = CommandType.typeOfDefinition(CommandType.valueOf(SmartGWTUtil.getStringValue(typeField)));

			definition.setName(saveInfo.getName());
			definition.setDisplayName(saveInfo.getDisplayName());
			definition.setDescription(saveInfo.getDescription());

			service.createDefinition(TenantInfoHolder.getId(), definition, new SaveResultCallback());
		}
	}

}
