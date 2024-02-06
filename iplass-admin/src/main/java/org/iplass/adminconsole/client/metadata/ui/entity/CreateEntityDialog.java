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

package org.iplass.adminconsole.client.metadata.ui.entity;

import java.util.ArrayList;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.common.Callable;
import org.iplass.adminconsole.client.metadata.ui.common.MetaDataCreateDialog;
import org.iplass.adminconsole.shared.metadata.dto.MetaDataConstants;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.PropertyDefinition;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.validator.RegExpValidator;

/**
 *
 * @author lis3wg
 */
public class CreateEntityDialog extends MetaDataCreateDialog {

	private CheckboxItem copyEntityView;
	private CheckboxItem copyEntityFilter;
	private CheckboxItem copyEntityWebAPI;

	public CreateEntityDialog(String definitionClassName, String nodeDisplayName, String folderPath, boolean isCopyMode) {
		super(definitionClassName, nodeDisplayName, folderPath, isCopyMode);

		//名前のPolicyをカスタマイズ
		RegExpValidator nameRegExpValidator = new RegExpValidator();
		nameRegExpValidator.setExpression(MetaDataConstants.ENTITY_NAME_REG_EXP_PATH_PERIOD);
		nameRegExpValidator.setErrorMessage(AdminClientMessageUtil.getString("ui_metadata_entity_CreateEntityDialog_nameErr"));
		setCustomNameValidator(nameRegExpValidator);

		if (isCopyMode) {
			setHeight(DEFAULT_HEIGHT + 120);

			copyEntityView = new CheckboxItem("copyEntityView", "copy entity view (Search and Detail)") ;
			copyEntityFilter = new CheckboxItem("copyEntityFilter", "copy entity filter") ;
			copyEntityWebAPI = new CheckboxItem("copyEntityWebAPI", "copy entity webapi") ;

			DynamicForm copyForm = createDefaultForm();
			copyForm.setIsGroup(true);
			copyForm.setGroupTitle("Copy Options");
			copyForm.setItems(copyEntityView, copyEntityFilter, copyEntityWebAPI);

			addCustomParts(copyForm);
		}

	}

	@Override
	protected void saveAction(final SaveInfo saveInfo, final boolean isCopyMode) {

		// 存在チェック
		checkExist(saveInfo.getName(), new Callable<Void>() {

			@Override
			public Void call() {
				createEntity(saveInfo, isCopyMode);
				return null;
			}
		});
	}

	private void createEntity(final SaveInfo saveInfo, final boolean isCopyMode) {

		if (isCopyMode) {
			service.copyEntityDefinition(TenantInfoHolder.getId(), getSourceName(),
					saveInfo.getName(), saveInfo.getDisplayName(), saveInfo.getDescription(),
					SmartGWTUtil.getBooleanValue(copyEntityView), SmartGWTUtil.getBooleanValue(copyEntityFilter), SmartGWTUtil.getBooleanValue(copyEntityWebAPI),
					new SaveResultCallback());

		} else {
			final EntityDefinition definition = new EntityDefinition();

			definition.setName(saveInfo.getName());
			definition.setDisplayName(saveInfo.getDisplayName());
			definition.setDescription(saveInfo.getDescription());

			definition.setInheritedDefinition(EntityDefinition.SYSTEM_DEFAULT_DEFINITION_NAME);
			definition.setPropertyList(new ArrayList<PropertyDefinition>());

			service.createEntityDefinition(TenantInfoHolder.getId(), definition, new SaveResultCallback());
		}
	}

}
