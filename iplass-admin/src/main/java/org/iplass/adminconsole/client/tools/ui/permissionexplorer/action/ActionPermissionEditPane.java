/*
 * Copyright (C) 2015 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.client.tools.ui.permissionexplorer.action;

import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.tools.ui.permissionexplorer.PermissionEditPane;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.GenericEntity;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;

public class ActionPermissionEditPane extends PermissionEditPane {

	//フォーム
	private DynamicForm permissionTargetForm;
	private DynamicForm permissionConditionForm;

	//権限の対象
	private TextItem txtNameField;
	private TextAreaItem areaDescriptionField;
	private TextItem txtTargetActionField;
	private TextItem txtRoleField;

	//権限情報
	private TextAreaItem areaAllowConditionField;

	private GenericEntity editEntity;

	/**
	 * コンストラクタ
	 *
	 * @param target 対象Action
	 * @param roleEntity 対象ロールEntity
	 * @param permissionEntity 対象PermissionEntity(新規時はnull)
	 */
	public ActionPermissionEditPane(String target, Entity roleEntity, Entity permissionEntity) {

		//レイアウト設定
		setWidth100();
		setHeight100();

		// 権限の対象
		permissionTargetForm = new DynamicForm();
		permissionTargetForm.setMargin(5);
		permissionTargetForm.setAutoHeight();
		permissionTargetForm.setWidth(500);
		permissionTargetForm.setIsGroup(true);
		permissionTargetForm.setGroupTitle("Permission Target");
		permissionTargetForm.setPadding(5);

		txtNameField = new TextItem("permissionName", "Name");
		txtNameField.setWidth(370);
		SmartGWTUtil.setRequired(txtNameField);

		areaDescriptionField = new TextAreaItem("description", "Description");
		areaDescriptionField.setWidth(370);

		txtTargetActionField = new TextItem("targetAction", "Target Action");
		txtTargetActionField.setWidth(370);
		txtTargetActionField.setCanEdit(false);
		txtTargetActionField.setDisabled(true);

		txtRoleField = new TextItem("role", "Role");
		txtRoleField.setWidth(370);
		txtRoleField.setCanEdit(false);
		txtRoleField.setDisabled(true);

		permissionTargetForm.setItems(txtNameField, areaDescriptionField, txtTargetActionField, txtRoleField);

		// 権限条件
		permissionConditionForm = new DynamicForm();
		permissionConditionForm.setMargin(5);
		permissionConditionForm.setAutoHeight();
		permissionConditionForm.setWidth(500);
		permissionConditionForm.setIsGroup(true);
		permissionConditionForm.setGroupTitle("Permission Condition");
		permissionConditionForm.setPadding(5);

		areaAllowConditionField = new TextAreaItem("areaAllowCondition", "Allow Condition");
		areaAllowConditionField.setWidth(370);

		permissionConditionForm.setItems(areaAllowConditionField);


		//配置
		addMember(permissionTargetForm);
		addMember(permissionConditionForm);

		initializeData(target, roleEntity, permissionEntity);
	}

	private void initializeData(final String target, final Entity roleEntity, final Entity permissionEntity) {
		if (permissionEntity == null) {
			editEntity = new GenericEntity();
			//ここが個別
			editEntity.setValue("targetAction", target);
			editEntity.setValue("role", roleEntity);
			editEntity.setDefinitionName("mtp.auth.ActionPermission");
		} else {
			editEntity = (GenericEntity)permissionEntity;
		}
		createFieldData();
	}

	private void createFieldData() {

		txtNameField.setValue(editEntity.getName());
		areaDescriptionField.setValue(editEntity.getDescription());
		txtTargetActionField.setValue(editEntity.getValueAs(String.class, "targetAction"));
		txtRoleField.setValue(editEntity.getValueAs(String.class, "role.name"));

		areaAllowConditionField.setValue(editEntity.getValueAs(String.class, "conditionExpression"));
	}

	@Override
	public boolean validate() {
		return permissionTargetForm.validate();
	}

	@Override
	public GenericEntity getEditEntity() {

		editEntity.setName(SmartGWTUtil.getStringValue(txtNameField));
		editEntity.setDescription(SmartGWTUtil.getStringValue(areaDescriptionField));

		editEntity.setValue("conditionExpression", SmartGWTUtil.getStringValue(areaAllowConditionField));

		return editEntity;
	}
}
