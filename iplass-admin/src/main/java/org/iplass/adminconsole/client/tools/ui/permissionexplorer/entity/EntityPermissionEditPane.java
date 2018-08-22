/*
 * Copyright (C) 2015 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.tools.ui.permissionexplorer.entity;

import java.util.LinkedHashMap;

import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.tools.ui.permissionexplorer.PermissionEditPane;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.GenericEntity;
import org.iplass.mtp.entity.SelectValue;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;

public class EntityPermissionEditPane extends PermissionEditPane {

	//フォーム
	private DynamicForm permissionTargetForm;
	private DynamicForm referencePermissionForm;
	private DynamicForm createPermissionForm;
	private DynamicForm updatePermissionForm;
	private DynamicForm deletePermissionForm;

	//権限の対象
	private TextItem txtNameField;
	private TextAreaItem areaDescriptionField;
	private TextItem txtTargetEntityField;
	private TextItem txtRoleField;

	//参照権限
	private SelectItem slctReferenceAllowFiled;
	private TextAreaItem areaReferenceAllowRangeField;
	private SelectItem slctReferencePropertyControlField;
	private TextAreaItem areaReferencePropertyListField;

	//登録権限
	private SelectItem slctCreateAllowFiled;
	private TextAreaItem areaCreateAllowRangeField;
	private SelectItem slctCreatePropertyControlField;
	private TextAreaItem areaCreatePropertyListField;

	//更新権限
	private SelectItem slctUpdateAllowFiled;
	private TextAreaItem areaUpdateAllowRangeField;
	private SelectItem slctUpdatePropertyControlField;
	private TextAreaItem areaUpdatePropertyListField;

	//削除権限
	private SelectItem slctDeleteAllowFiled;
	private TextAreaItem areaDeleteAllowRangeField;

	private GenericEntity editEntity;

	/**
	 * コンストラクタ
	 *
	 * @param target 対象Entity
	 * @param roleEntity 対象ロールEntity
	 * @param permissionEntity 対象PermissionEntity(新規時はnull)
	 */
	public EntityPermissionEditPane(String target, Entity roleEntity, Entity permissionEntity) {

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

		txtTargetEntityField = new TextItem("targetEntity", "Target Entity");
		txtTargetEntityField.setWidth(370);
		txtTargetEntityField.setCanEdit(false);
		txtTargetEntityField.setDisabled(true);

		txtRoleField = new TextItem("role", "Role");
		txtRoleField.setWidth(370);
		txtRoleField.setCanEdit(false);
		txtRoleField.setDisabled(true);

		permissionTargetForm.setItems(txtNameField, areaDescriptionField, txtTargetEntityField, txtRoleField);

		// 参照権限
		referencePermissionForm = new DynamicForm();
		referencePermissionForm.setMargin(5);
		referencePermissionForm.setAutoHeight();
		referencePermissionForm.setWidth(500);
		referencePermissionForm.setIsGroup(true);
		referencePermissionForm.setGroupTitle("Reference Permission");
		referencePermissionForm.setPadding(5);

		slctReferenceAllowFiled = new SelectItem();
		slctReferenceAllowFiled.setWidth(370);
		slctReferenceAllowFiled.setTitle("Entity Reference Allow");
		LinkedHashMap<String, String> referenceAllowMap = new LinkedHashMap<String, String>();
		referenceAllowMap.put("true", "Enable");
		referenceAllowMap.put("false", "Disable");
		slctReferenceAllowFiled.setValueMap(referenceAllowMap);
		slctReferenceAllowFiled.setDefaultValue("false");

		areaReferenceAllowRangeField = new TextAreaItem();
		areaReferenceAllowRangeField.setTitle("Reference Allow Range");
		areaReferenceAllowRangeField.setWidth(370);

		slctReferencePropertyControlField = new SelectItem();
		slctReferencePropertyControlField.setWidth(370);
		slctReferencePropertyControlField.setTitle("Property Reference Allow");
		LinkedHashMap<String, String> referencePropertyControlMap = new LinkedHashMap<String, String>();
		referencePropertyControlMap.put("", "Please select.");
		referencePropertyControlMap.put("E", "The following properties are reference allowed.");
		referencePropertyControlMap.put("D", "The following properties are not reference allowed.");
		slctReferencePropertyControlField.setValueMap(referencePropertyControlMap);
		slctReferencePropertyControlField.setDefaultValue("");

		areaReferencePropertyListField = new TextAreaItem();
		areaReferencePropertyListField.setTitle("Reference Property List");
		areaReferencePropertyListField.setWidth(370);

		referencePermissionForm.setItems(slctReferenceAllowFiled, areaReferenceAllowRangeField, slctReferencePropertyControlField, areaReferencePropertyListField);

		// 登録権限
		createPermissionForm = new DynamicForm();
		createPermissionForm.setMargin(5);
		createPermissionForm.setAutoHeight();
		createPermissionForm.setWidth(500);
		createPermissionForm.setIsGroup(true);
		createPermissionForm.setGroupTitle("Create Permission");
		createPermissionForm.setPadding(5);

		slctCreateAllowFiled = new SelectItem();
		slctCreateAllowFiled.setWidth(370);
		slctCreateAllowFiled.setTitle("Create Allow");
		LinkedHashMap<String, String> createAllowMap = new LinkedHashMap<String, String>();
		createAllowMap.put("true", "Enable");
		createAllowMap.put("false", "Disable");
		slctCreateAllowFiled.setValueMap(createAllowMap);
		slctCreateAllowFiled.setDefaultValue("false");

		areaCreateAllowRangeField = new TextAreaItem();
		areaCreateAllowRangeField.setWidth(370);
		areaCreateAllowRangeField.setTitle("Create Allow Range");

		slctCreatePropertyControlField = new SelectItem();
		slctCreatePropertyControlField.setWidth(370);
		slctCreatePropertyControlField.setTitle("Create Allow");
		LinkedHashMap<String, String> createPropertyControlMap = new LinkedHashMap<String, String>();
		createPropertyControlMap.put("", "Please select.");
		createPropertyControlMap.put("E", "The following properties are creation Allowed.");
		createPropertyControlMap.put("D", "The following properties are not creation Allowed.");
		slctCreatePropertyControlField.setValueMap(createPropertyControlMap);
		slctCreatePropertyControlField.setDefaultValue("");

		areaCreatePropertyListField = new TextAreaItem();
		areaCreatePropertyListField.setWidth(370);
		areaCreatePropertyListField.setTitle("Create Property List");

		createPermissionForm.setItems(slctCreateAllowFiled, areaCreateAllowRangeField, slctCreatePropertyControlField, areaCreatePropertyListField);

		// 更新権限
		updatePermissionForm = new DynamicForm();
		updatePermissionForm.setMargin(5);
		updatePermissionForm.setAutoHeight();
		updatePermissionForm.setWidth(500);
		updatePermissionForm.setIsGroup(true);
		updatePermissionForm.setGroupTitle("Update Permission");
		updatePermissionForm.setPadding(5);

		slctUpdateAllowFiled = new SelectItem();
		slctUpdateAllowFiled.setWidth(370);
		slctUpdateAllowFiled.setTitle("Update Allow");
		LinkedHashMap<String, String> updateAllowMap = new LinkedHashMap<String, String>();
		updateAllowMap.put("true", "Enable");
		updateAllowMap.put("false", "Disable");
		slctUpdateAllowFiled.setValueMap(updateAllowMap);
		slctUpdateAllowFiled.setDefaultValue("false");

		areaUpdateAllowRangeField = new TextAreaItem();
		areaUpdateAllowRangeField.setWidth(370);
		areaUpdateAllowRangeField.setTitle("Update Allow Range");

		slctUpdatePropertyControlField = new SelectItem();
		slctUpdatePropertyControlField.setWidth(370);
		slctUpdatePropertyControlField.setTitle("Update Allow");
		LinkedHashMap<String, String> updatePropertyControlMap = new LinkedHashMap<String, String>();
		updatePropertyControlMap.put("", "Please select.");
		updatePropertyControlMap.put("E", "The following properties are update Allowed.");
		updatePropertyControlMap.put("D", "The following properties are not update Allowed.");
		slctUpdatePropertyControlField.setValueMap(createPropertyControlMap);
		slctUpdatePropertyControlField.setDefaultValue("");

		areaUpdatePropertyListField = new TextAreaItem();
		areaUpdatePropertyListField.setWidth(370);
		areaUpdatePropertyListField.setTitle("Update Property List");

		updatePermissionForm.setItems(slctUpdateAllowFiled, areaUpdateAllowRangeField, slctUpdatePropertyControlField, areaUpdatePropertyListField);

		// 更新権限
		deletePermissionForm = new DynamicForm();
		deletePermissionForm.setMargin(5);
		deletePermissionForm.setAutoHeight();
		deletePermissionForm.setWidth(500);
		deletePermissionForm.setIsGroup(true);
		deletePermissionForm.setGroupTitle("Delete Permission");
		deletePermissionForm.setPadding(5);

		slctDeleteAllowFiled = new SelectItem();
		slctDeleteAllowFiled.setWidth(370);
		slctDeleteAllowFiled.setTitle("Delete Allow");
		LinkedHashMap<String, String> deleteAllowMap = new LinkedHashMap<String, String>();
		deleteAllowMap.put("true", "Enable");
		deleteAllowMap.put("false", "Disable");
		slctDeleteAllowFiled.setValueMap(deleteAllowMap);
		slctDeleteAllowFiled.setDefaultValue("false");

		areaDeleteAllowRangeField = new TextAreaItem();
		areaDeleteAllowRangeField.setWidth(370);
		areaDeleteAllowRangeField.setTitle("Delete Allow Range");

		deletePermissionForm.setItems(slctDeleteAllowFiled, areaDeleteAllowRangeField);

		//配置
		addMember(permissionTargetForm);
		addMember(referencePermissionForm);
		addMember(createPermissionForm);
		addMember(updatePermissionForm);
		addMember(deletePermissionForm);

		initializeData(target, roleEntity, permissionEntity);
	}

	private void initializeData(final String target, final Entity roleEntity, final Entity permissionEntity) {
		if (permissionEntity == null) {
			editEntity = new GenericEntity();
			editEntity.setValue("targetEntity", target);
			editEntity.setValue("role", roleEntity);
			editEntity.setDefinitionName("mtp.auth.EntityPermission");
		} else {
			editEntity = (GenericEntity)permissionEntity;
		}
		createFieldData();
	}

	private void createFieldData() {

		txtNameField.setValue(editEntity.getName());
		areaDescriptionField.setValue(editEntity.getDescription());
		txtTargetEntityField.setValue(editEntity.getValueAs(String.class, "targetEntity"));
		txtRoleField.setValue(editEntity.getValueAs(String.class, "role.name"));

		// 参照権限
		if (editEntity.getValue("canReference") != null) {
			//com.google.gwt.dev.jjs.InternalCompilerException: Error constructing Java AST
			//slctReferenceAllowFiled.setValue(Boolean.toString((boolean)editEntity.getValue("canReference")));
			slctReferenceAllowFiled.setValue(editEntity.getValueAs(Boolean.class, "canReference").toString());
		}
		areaReferenceAllowRangeField.setValue(editEntity.getValueAs(String.class, "referenceCondition"));
		if (editEntity.getValue("referencePropertyControlType") != null) {
			SelectValue selectValue = (SelectValue) editEntity.getValue("referencePropertyControlType");
			slctReferencePropertyControlField.setValue(selectValue.getValue());
		}
		areaReferencePropertyListField.setValue(editEntity.getValueAs(String.class, "referencePropertyList"));

		// 登録権限
		if (editEntity.getValue("canCreate") != null) {
			//com.google.gwt.dev.jjs.InternalCompilerException: Error constructing Java AST
			//slctCreateAllowFiled.setValue(Boolean.toString((boolean)editEntity.getValue("canCreate")));
			slctCreateAllowFiled.setValue(editEntity.getValueAs(Boolean.class, "canCreate").toString());
		}
		areaCreateAllowRangeField.setValue(editEntity.getValueAs(String.class, "createCondition"));
		if (editEntity.getValue("createPropertyControlType") != null) {
			SelectValue selectValue = (SelectValue) editEntity.getValue("createPropertyControlType");
			slctCreatePropertyControlField.setValue(selectValue.getValue());
		}
		areaCreatePropertyListField.setValue(editEntity.getValueAs(String.class, "createPropertyList"));

		// 更新権限
		if (editEntity.getValue("canUpdate") != null) {
			//com.google.gwt.dev.jjs.InternalCompilerException: Error constructing Java AST
			//slctUpdateAllowFiled.setValue(Boolean.toString((boolean)editEntity.getValue("canUpdate")));
			slctUpdateAllowFiled.setValue(editEntity.getValueAs(Boolean.class, "canUpdate").toString());
		}
		areaUpdateAllowRangeField.setValue(editEntity.getValueAs(String.class, "updateCondition"));
		if (editEntity.getValue("updatePropertyControlType") != null) {
			SelectValue selectValue = (SelectValue) editEntity.getValue("updatePropertyControlType");
			slctUpdatePropertyControlField.setValue(selectValue.getValue());
		}
		areaUpdatePropertyListField.setValue(editEntity.getValueAs(String.class, "updatePropertyList"));

		// 削除権限
		if (editEntity.getValue("canDelete") != null) {
			//com.google.gwt.dev.jjs.InternalCompilerException: Error constructing Java AST
			//slctDeleteAllowFiled.setValue(Boolean.toString((boolean)editEntity.getValue("canDelete")));
			slctDeleteAllowFiled.setValue(editEntity.getValueAs(Boolean.class, "canDelete").toString());
		}
		areaDeleteAllowRangeField.setValue(editEntity.getValueAs(String.class, "deleteCondition"));
	}

	@Override
	public boolean validate() {
		return permissionTargetForm.validate();
	}

	@Override
	public GenericEntity getEditEntity() {

		editEntity.setName(SmartGWTUtil.getStringValue(txtNameField));
		editEntity.setDescription(SmartGWTUtil.getStringValue(areaDescriptionField));

		// 参照権限
		if (!SmartGWTUtil.isEmpty(slctReferenceAllowFiled.getValueAsString())) {
			editEntity.setValue("canReference", Boolean.valueOf(SmartGWTUtil.getStringValue(slctReferenceAllowFiled)));
		} else {
			editEntity.setValue("canReference", null);
		}
		editEntity.setValue("referenceCondition", SmartGWTUtil.getStringValue(areaReferenceAllowRangeField));
		String referenceSelectValueString = SmartGWTUtil.getStringValue(slctReferencePropertyControlField);
		if (SmartGWTUtil.isEmpty(referenceSelectValueString)) {
			editEntity.setValue("referencePropertyControlType", null);
		} else {
			SelectValue selectValue = new SelectValue();
			selectValue.setValue(SmartGWTUtil.getStringValue(slctReferencePropertyControlField));
			editEntity.setValue("referencePropertyControlType", selectValue);
		}
		editEntity.setValue("referencePropertyList", SmartGWTUtil.getStringValue(areaReferencePropertyListField));

		// 登録権限
		if (!SmartGWTUtil.isEmpty(slctCreateAllowFiled.getValueAsString())) {
			editEntity.setValue("canCreate", Boolean.valueOf(SmartGWTUtil.getStringValue(slctCreateAllowFiled)));
		} else {
			editEntity.setValue("canCreate", null);
		}
		editEntity.setValue("createCondition", SmartGWTUtil.getStringValue(areaCreateAllowRangeField));
		String createSelectValueString = SmartGWTUtil.getStringValue(slctCreatePropertyControlField);
		if (SmartGWTUtil.isEmpty(createSelectValueString)) {
			editEntity.setValue("createPropertyControlType", null);
		} else {
			SelectValue selectValue = new SelectValue();
			selectValue.setValue(SmartGWTUtil.getStringValue(slctCreatePropertyControlField));
			editEntity.setValue("createPropertyControlType", selectValue);
		}
		editEntity.setValue("createPropertyList", SmartGWTUtil.getStringValue(areaCreatePropertyListField));

		// 更新権限
		if (!SmartGWTUtil.isEmpty(slctUpdateAllowFiled.getValueAsString())) {
			editEntity.setValue("canUpdate", Boolean.valueOf(SmartGWTUtil.getStringValue(slctUpdateAllowFiled)));
		} else {
			editEntity.setValue("canUpdate", null);
		}
		editEntity.setValue("updateCondition", SmartGWTUtil.getStringValue(areaUpdateAllowRangeField));
		String updateSelectValueString = SmartGWTUtil.getStringValue(slctUpdatePropertyControlField);
		if (SmartGWTUtil.isEmpty(updateSelectValueString)) {
			editEntity.setValue("updatePropertyControlType", null);
		} else {
			SelectValue selectValue = new SelectValue();
			selectValue.setValue(SmartGWTUtil.getStringValue(slctUpdatePropertyControlField));
			editEntity.setValue("updatePropertyControlType", selectValue);
		}
		editEntity.setValue("updatePropertyList", SmartGWTUtil.getStringValue(areaUpdatePropertyListField));

		// 削除権限
		if (!SmartGWTUtil.isEmpty(slctDeleteAllowFiled.getValueAsString())) {
			editEntity.setValue("canDelete", Boolean.valueOf(SmartGWTUtil.getStringValue(slctDeleteAllowFiled)));
		} else {
			editEntity.setValue("canDelete", null);
		}
		editEntity.setValue("deleteCondition", SmartGWTUtil.getStringValue(areaDeleteAllowRangeField));

		return editEntity;
	}

}
