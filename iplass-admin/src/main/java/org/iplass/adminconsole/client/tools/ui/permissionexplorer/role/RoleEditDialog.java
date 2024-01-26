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

package org.iplass.adminconsole.client.tools.ui.permissionexplorer.role;

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.event.DataChangedEvent;
import org.iplass.adminconsole.client.base.event.DataChangedHandler;
import org.iplass.adminconsole.client.base.ui.widget.AbstractWindow;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.shared.metadata.dto.MetaDataConstants;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.GenericEntity;

import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;

public class RoleEditDialog extends AbstractWindow {

	/** フッター */
	private Canvas footerLine;
	private HLayout footer;

	/** データ変更ハンドラ */
	private List<DataChangedHandler> handlers = new ArrayList<DataChangedHandler>();

	private TextItem txtRoleField;
	private TextItem txtNameField;
	private TextItem txtPriorityField;
	private TextAreaItem areaDescriptionField;
	private ListGrid listGrid;

	//編集データ
	private GenericEntity roleEntity;

	public RoleEditDialog() {

		setTitle("Edit Role");
		setWidth(550);
		setHeight(570);
		setMinWidth(550);
		setMinHeight(570);

		setShowMinimizeButton(false);
		setShowMaximizeButton(true);
		setCanDragResize(true);

		setIsModal(true);
		setShowModalMask(true);

		centerInPage();

		final DynamicForm form = new DynamicForm();
		form.setMargin(5);
		form.setAutoHeight();
		form.setWidth100();

		txtRoleField = new TextItem("roleCode", "Role Code");
		txtRoleField.setWidth(370);
		SmartGWTUtil.setRequired(txtRoleField);

		txtNameField = new TextItem("name", "Name");
		txtNameField.setWidth(370);
		SmartGWTUtil.setRequired(txtNameField);

		txtPriorityField = new TextItem("priority", "Priority");
		txtPriorityField.setWidth(370);

		areaDescriptionField = new TextAreaItem("description", "Description");
		areaDescriptionField.setWidth(370);

		form.setItems(txtRoleField, txtNameField, txtPriorityField, areaDescriptionField);

		IButton save = new IButton("OK");
		save.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				boolean commonValidate = form.validate();
				boolean gridValidate = true;

				for (int cnt = 0; cnt < listGrid.getRecords().length; cnt ++) {
					gridValidate = gridValidate && listGrid.validateRow(cnt);
				}

				if (commonValidate && gridValidate) {
					saveRole();
				}
			}
		});

		IButton cancel = new IButton("Cancel");
		cancel.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				destroy();
			}
		});

		footerLine = SmartGWTUtil.separator();
		footer = new HLayout(5);
		footer.setMargin(5);
		footer.setHeight(20);
		footer.setWidth100();
		footer.setAlign(VerticalAlignment.CENTER);
		footer.setMembers(save, cancel);

		addItem(form);

		listGrid = new ListGrid();

		listGrid.setCanResizeFields(true);	//列幅変更可
		listGrid.setCanSort(false);			//ソート不可
		listGrid.setCanGroupBy(false);		//Group化不可
		listGrid.setCanPickFields(false);	//列の選択不可
		listGrid.setCanAutoFitFields(false);	//自動列幅調整不可
		listGrid.setCanFreezeFields(false);

		listGrid.setCanEdit(true);

		ListGridField roleConditionName = new ListGridField("name", "Condition Name");
		roleConditionName.setRequired(true);
		roleConditionName.setWidth(150);
		ListGridField roleCondition = new ListGridField("expression", "Condition");
		roleCondition.setRequired(true);
		TextAreaItem conditionField = new TextAreaItem();
		roleCondition.setEditorProperties(conditionField);
		listGrid.setFields(roleConditionName, roleCondition);

		SectionStackSection section1 = new SectionStackSection();
		section1.setTitle("RoleCondition");
		section1.setItems(listGrid);
		section1.setExpanded(true);
		section1.setCanCollapse(false);

		ImgButton addButton = new ImgButton();
		addButton.setSrc("[SKIN]actions/add.png");
		addButton.setSize(16);
		addButton.setShowFocused(false);
		addButton.setShowRollOver(false);
		addButton.setShowDown(false);
		addButton.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
			public void onClick(
					com.smartgwt.client.widgets.events.ClickEvent event) {
				listGrid.startEditingNew();
			}
		});

		ImgButton removeButton = new ImgButton();
		removeButton.setSrc("[SKIN]actions/remove.png");
		removeButton.setSize(16);
		removeButton.setShowFocused(false);
		removeButton.setShowRollOver(false);
		removeButton.setShowDown(false);
		removeButton.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
			public void onClick(
					com.smartgwt.client.widgets.events.ClickEvent event) {
				listGrid.removeSelectedData();
			}
		});
		section1.setControls(addButton, removeButton);

		SectionStack sectionStack = new SectionStack();
		sectionStack.setSections(section1);
		sectionStack.setVisibilityMode(VisibilityMode.MULTIPLE);
		sectionStack.setAnimateSections(true);
		sectionStack.setWidth100();
		sectionStack.setHeight100();
		sectionStack.setOverflow(Overflow.HIDDEN);

		VLayout tempLayout = new VLayout();
		tempLayout.setMargin(5);
		tempLayout.addMember(sectionStack);

		addItem(tempLayout);
		addItem(footerLine);
		addItem(footer);
	}

	/**
	 * {@link DataChangedHandler} を追加します。
	 *
	 * @param handler {@link DataChangedHandler}
	 */
	public void addDataChangeHandler(DataChangedHandler handler) {
		handlers.add(0, handler);
	}

	public void setEditRoleEntity(Entity roleEntity) {
		this.roleEntity = (GenericEntity)roleEntity;

		txtNameField.setValue(roleEntity.getName());
		txtRoleField.setValue(roleEntity.getValueAs(String.class, "code"));
		txtPriorityField.setValue(roleEntity.getValueAs(Long.class, "priority"));
		areaDescriptionField.setValue(roleEntity.getDescription());

		if (roleEntity.getValue("condition") != null) {
			Entity[] conditionArray = roleEntity.getValue("condition");
			List<ListGridRecord> records = new ArrayList<ListGridRecord>();
			for (Entity condition : conditionArray) {
				ListGridRecord record = new ListGridRecord();
				record.setAttribute("oid", condition.getOid());
				record.setAttribute("name", condition.getName());
				record.setAttribute("expression", condition.getValueAs(String.class, "expression"));
				records.add(record);
			}
			listGrid.setData(records.toArray(new ListGridRecord[]{}));
			listGrid.refreshFields();
		}
	}

	private void saveRole() {

		GenericEntity editEntity = new GenericEntity();

		if (roleEntity != null) {
			editEntity.setOid(roleEntity.getOid());
		}

		editEntity.setName(SmartGWTUtil.getStringValue(txtNameField));
		editEntity.setValue("code", SmartGWTUtil.getStringValue(txtRoleField));

		String priority = SmartGWTUtil.getStringValue(txtPriorityField);
		if (!SmartGWTUtil.isEmpty(priority)) {
			editEntity.setValue("priority", Long.valueOf(priority));
		}
		editEntity.setDescription(SmartGWTUtil.getStringValue(areaDescriptionField));
		editEntity.setDefinitionName(MetaDataConstants.ENTITY_NAME_ROLE);

		if (listGrid.getRecords().length > 0) {
			Entity[] conditionEntityArray = new GenericEntity[listGrid.getRecords().length];
			int cnt = 0;
			for (ListGridRecord record : listGrid.getRecords()) {

				Entity conditionEntity = new GenericEntity();

				conditionEntity.setOid(record.getAttribute("oid"));
				conditionEntity.setName(record.getAttribute("name"));
				conditionEntity.setValue("expression", record.getAttribute("expression"));
				conditionEntity.setDefinitionName(MetaDataConstants.ENTITY_NAME_ROLE_CONDITION);

				conditionEntityArray[cnt] = conditionEntity;
				cnt ++;
			}
			editEntity.setValue("condition", conditionEntityArray);
		}

		if (isChange(editEntity)) {
			fireDataChanged(editEntity);
		}

		//ダイアログ消去
		destroy();
	}

	private boolean isChange(Entity editEntity) {
		if (roleEntity == null) {
			return true;
		}

		if (!roleEntity.getName().equals(editEntity.getName())) {
			return true;
		}
		if (!roleEntity.getValue("code").equals(editEntity.getValue("code"))) {
			return true;
		}
		if (isChangeProperty(roleEntity.getValue("priority"), editEntity.getValue("priority"))) {
			return true;
		}
		if (isChangeProperty(roleEntity.getDescription(), editEntity.getDescription())) {
			return true;
		}

		if (roleEntity.getValue("condition") == null && editEntity.getValue("condition") != null) {
			return true;
		}
		if (roleEntity.getValue("condition") != null && editEntity.getValue("condition") == null) {
			return true;
		}
		if (roleEntity.getValue("condition") != null && editEntity.getValue("condition") != null) {
			Entity[] storeConditionArray = roleEntity.getValue("condition");
			Entity[] editConditionArray = editEntity.getValue("condition");
			if (storeConditionArray.length != editConditionArray.length) {
				return true;
			}
			for (Entity storeCondition : storeConditionArray) {
				if (storeCondition.getOid() == null) {
					//新規がある場合は比較できないのでtrue
					return true;
				}
				boolean isMatch = false;
				for (Entity editCondition : editConditionArray) {
					if (editCondition.getOid() == null) {
						//新規がある場合は比較できないのでtrue
						return true;
					}
					if (storeCondition.getOid().equals(editCondition.getOid())) {
						isMatch = true;
						if (!storeCondition.getName().equals(editCondition.getName())
								|| !storeCondition.getValue("expression").equals(editCondition.getValue("expression"))) {
							return true;
						}
						break;
					}
				}
				if (!isMatch) {
					return true;
				}
			}
		}

		return false;

	}

	private boolean isChangeProperty(Object from, Object to) {
		if (from == null && to != null) {
			return true;
		}
		if (from != null && to == null) {
			return true;
		}
		if (from != null && to != null) {
			if (!from.equals(to)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * データ変更通知処理
	 */
	private void fireDataChanged(GenericEntity entity) {
		DataChangedEvent event = new DataChangedEvent();
		event.setValueObject(entity);
		for (DataChangedHandler handler : handlers) {
			handler.onDataChanged(event);
		}
	}
}
