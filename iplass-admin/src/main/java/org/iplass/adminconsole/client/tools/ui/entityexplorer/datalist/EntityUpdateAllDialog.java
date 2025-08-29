/*
 * Copyright (C) 2013 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.client.tools.ui.entityexplorer.datalist;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.iplass.adminconsole.client.base.event.DataChangedEvent;
import org.iplass.adminconsole.client.base.event.DataChangedHandler;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.ui.widget.AbstractWindow;
import org.iplass.adminconsole.client.base.ui.widget.AnimationFullScreenCallback;
import org.iplass.adminconsole.client.base.ui.widget.MessageTabSet;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.entity.property.PropertyListGridRecord;
import org.iplass.adminconsole.shared.tools.dto.entityexplorer.EntityDataCountResultInfo;
import org.iplass.adminconsole.shared.tools.rpc.entityexplorer.EntityExplorerServiceAsync;
import org.iplass.adminconsole.shared.tools.rpc.entityexplorer.EntityExplorerServiceFactory;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.SelectValue;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.PropertyDefinitionType;
import org.iplass.mtp.entity.definition.properties.BinaryProperty;
import org.iplass.mtp.entity.definition.properties.ExpressionProperty;
import org.iplass.mtp.entity.definition.properties.LongTextProperty;
import org.iplass.mtp.entity.definition.properties.ReferenceProperty;
import org.iplass.mtp.entity.definition.properties.SelectProperty;
import org.iplass.mtp.impl.tools.entity.EntityUpdateAllCondition;
import org.iplass.mtp.impl.tools.entity.EntityUpdateAllResultInfo;
import org.iplass.mtp.impl.tools.entity.UpdateAllValue;
import org.iplass.mtp.impl.tools.entity.UpdateAllValue.UpdateAllValueType;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

public class EntityUpdateAllDialog extends AbstractWindow {

	private static final String NULL_VALUE = "EntityUpdateAll@NULL_MARK";

	private static final int MIN_WIDTH = 800;
	private static final int MIN_HEIGHT = 600;

	private EntityDefinition definition;

	/** update value */
	private UpdateValueGridPane gridPane;

	/** update condition */
	private TextAreaItem whereField;

	/** message panel */
	private MessageTabSet messageTabSet;

	private IButton cancel;
	private IButton update;

	public static void showFullScreen(
			final EntityDefinition definition
			, final String whereClause) {

		SmartGWTUtil.showAnimationFullScreen(new AnimationFullScreenCallback() {
			@Override
			public void execute(boolean earlyFinish) {
              animateOutline.hide();
              EntityUpdateAllDialog dialog = new EntityUpdateAllDialog(width, height, definition, whereClause);
              dialog.show();
			}
		});
	}

	private EntityUpdateAllDialog(int width, int height
			, final EntityDefinition definition
			, final String whereClause) {

		this.definition = definition;

		setMinWidth(MIN_WIDTH);
		setMinHeight(MIN_HEIGHT);
		setWidth(width);
		setHeight(height);
		setTitle("UpdateAll : " + definition.getName());
		setShowMinimizeButton(false);
		setShowMaximizeButton(true);
		setIsModal(true);
		setShowModalMask(true);
		centerInPage();

		gridPane = new UpdateValueGridPane();

		DynamicForm form = new DynamicForm();
		form.setWidth100();
		form.setHeight(150);
		form.setNumCols(1);
		form.setColWidths("*");

		whereField = new TextAreaItem();
		whereField.setTitle("Where");
		whereField.setShowTitle(false);
		whereField.setWidth("100%");
		whereField.setHeight("100%");
		whereField.setSelectOnFocus(true);
		whereField.setValue(whereClause);

		form.setFields(whereField);

		update = new IButton("Update");
		update.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (!gridPane.existsTarget()) {
					SC.warn(AdminClientMessageUtil.getString("ui_tools_entityexplorer_EntityUpdateAllDialog_nonSelectUpdateProp"));
					return;
				}

				SC.ask(AdminClientMessageUtil.getString("ui_tools_entityexplorer_EntityUpdateAllDialog_confirm"),
						AdminClientMessageUtil.getString("ui_tools_entityexplorer_EntityUpdateAllDialog_updateConf"), new BooleanCallback() {

					@Override
					public void execute(Boolean value) {
						if (!value) {
							return;
						}

						updateAll();
					}
				});
			}
		});

		cancel = new IButton("Cancel");
		cancel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				destroy();
			}
		});

		HLayout footer = new HLayout(5);
		footer.setMargin(5);
		footer.setHeight(20);
		footer.setWidth100();
		//footer.setAlign(Alignment.LEFT);
		footer.setAlign(VerticalAlignment.CENTER);
		footer.setMembers(update, cancel);

		//message panel
		messageTabSet = new MessageTabSet();
		messageTabSet.setHeight(120);

		VLayout mainLayout = new VLayout();
		mainLayout.setWidth100();
		mainLayout.setHeight100();
		mainLayout.setMargin(10);

		mainLayout.addMember(SmartGWTUtil.titleLabel("Update Values"));
		mainLayout.addMember(gridPane);
		mainLayout.addMember(SmartGWTUtil.titleLabel("Update Condition"));
		mainLayout.addMember(form);
		mainLayout.addMember(footer);
		mainLayout.addMember(messageTabSet);

		addItem(mainLayout);
	}

	private void updateAll() {
		startExecute();

		EntityUpdateAllCondition cond = createUpdateCondition();

		//該当件数のチェック
		checkDataCount(cond);
	}

	private EntityUpdateAllCondition createUpdateCondition() {
		EntityUpdateAllCondition cond = new EntityUpdateAllCondition();
		cond.setDefinitionName(definition.getName());

		gridPane.apply(cond);

		cond.setWhere(SmartGWTUtil.getStringValue(whereField));
		return cond;
	}

	private void checkDataCount(final EntityUpdateAllCondition cond) {
		EntityExplorerServiceAsync service =  EntityExplorerServiceFactory.get();
		service.getConditionDataCount(TenantInfoHolder.getId(), cond.getDefinitionName(), cond.getWhere(), false,
				new AsyncCallback<EntityDataCountResultInfo>() {

			@Override
			public void onSuccess(EntityDataCountResultInfo result) {
				checkDataCountComplete(result, cond);
			}

			@Override
			public void onFailure(Throwable caught) {
				updateError(caught);
			}
		});
	}

	private void checkDataCountComplete(EntityDataCountResultInfo result, final EntityUpdateAllCondition cond) {
		messageTabSet.clearMessage();

		if (result.isError()) {
			messageTabSet.addErrorMessage("Check DataCount failed.");
		} else {
			messageTabSet.addMessage("Check DataCount completed.");
		}
		messageTabSet.addMessage("------------------------------------");

		for (String message : result.getMessages()) {
			if (result.isError()) {
				messageTabSet.addErrorMessage(message);
			} else {
				messageTabSet.addMessage(message);
			}
		}

		if (!result.isError()) {

			//確認
			SC.ask(AdminClientMessageUtil.getString("ui_tools_entityexplorer_EntityUpdateAllDialog_confirm"),
					AdminClientMessageUtil.getString("ui_tools_entityexplorer_EntityUpdateAllDialog_targetDataCountConf", Integer.toString(result.getTargetCount()), Integer.toString(result.getAllCount())), new BooleanCallback() {

				@Override
				public void execute(Boolean value) {
					if (value) {
						executeAllUpdate(cond);
					} else {
						disableComponent(false);
						messageTabSet.setTabTitleNormal();
					}
				}
			});
		} else {
			disableComponent(false);
			messageTabSet.setTabTitleNormal();
		}
	}

	private void executeAllUpdate(final EntityUpdateAllCondition cond) {
		EntityExplorerServiceAsync service = EntityExplorerServiceFactory.get();
		service.updateAll(TenantInfoHolder.getId(), cond, new AsyncCallback<EntityUpdateAllResultInfo>() {

			@Override
			public void onFailure(Throwable caught) {
				updateError(caught);
			}

			@Override
			public void onSuccess(EntityUpdateAllResultInfo result) {
				updateAllComplete(result);
			}
		});

	}

	private void updateAllComplete(EntityUpdateAllResultInfo result) {
		messageTabSet.clearMessage();

		if (result.isError()) {
			messageTabSet.addErrorMessage("Update failed.");
		} else {
			messageTabSet.addMessage("Update completed.");
		}
		messageTabSet.addMessage("------------------------------------");

		for (String message : result.getLogMessages()) {
			if (result.isError()) {
				messageTabSet.addErrorMessage(message);
			} else {
				messageTabSet.addMessage(message);
			}
		}

		disableComponent(false);
		messageTabSet.setTabTitleNormal();
	}

	private void updateError(Throwable caught) {
		messageTabSet.clearMessage();
		messageTabSet.addErrorMessage("Update failed.");
		messageTabSet.addMessage("------------------------------------");

		if (caught.getMessage() != null) {
			messageTabSet.addErrorMessage(caught.getMessage());
		} else {
			messageTabSet.addErrorMessage(caught.getClass().getName());
		}

		disableComponent(false);
		messageTabSet.setTabTitleNormal();
	}

	private void startExecute() {
		messageTabSet.clearMessage();
		messageTabSet.setTabTitleProgress();
	}

//	private void finishExecute() {
//		messageTabSet.setTabTitleNormal();
//	}

	private void disableComponent(boolean disabled) {
		update.setDisabled(disabled);
		cancel.setDisabled(disabled);
	}

	private class UpdateValueGridPane extends VLayout {

		private CheckboxItem updateDisupdatablePropertyItem;
		private ListGrid grid;

		public UpdateValueGridPane() {
			setWidth100();
			setHeight100();

			ToolStrip toolStrip = new ToolStrip();
			toolStrip.setWidth100();
			toolStrip.setMembersMargin(5);
			toolStrip.setAlign(VerticalAlignment.BOTTOM);

			updateDisupdatablePropertyItem = new CheckboxItem();
			updateDisupdatablePropertyItem.setTitle("enable unupdatable properties");
			updateDisupdatablePropertyItem.addChangedHandler(new ChangedHandler() {

				@Override
				public void onChanged(ChangedEvent event) {
					changeUpdatableTarget();
				}
			});
			toolStrip.addFormItem(updateDisupdatablePropertyItem);
			toolStrip.addFill();

			grid = new ListGrid();
			grid.setWidth100();
			grid.setHeight100();

			grid.setShowAllRecords(true);

			grid.setLeaveScrollbarGap(false);	//falseで縦スクロールバー領域が自動表示制御される
			grid.setCanDragSelectText(true);	//セルの値をドラッグで選択可能（コピー用）にする

			grid.setCanSort(false);				//ソート不可
			grid.setCanGroupBy(false);			//Group化不可
			grid.setCanPickFields(false);		//列の選択不可

			grid.setCanAutoFitFields(false);	//自動サイズ調整不可
			grid.setCanFreezeFields(false);		//列固定不可

			createGridField();
			createGridRecord();

//			grid.setEditByCell(true);
//			grid.setWrapCells(true);
//			grid.setCanEdit(true);
//			grid.setEditEvent(ListGridEditEvent.CLICK);

			//選択可能設定
			grid.setSelectionType(SelectionStyle.SIMPLE);
			grid.setSelectionAppearance(SelectionAppearance.CHECKBOX);

			//編集ダイアログ表示
			grid.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {

				@Override
				public void onRecordDoubleClick(RecordDoubleClickEvent event) {
					UpdateValueGridRecord record = (UpdateValueGridRecord)event.getRecord();
					editValue(record);
				}

			});

			addMember(toolStrip);
			addMember(grid);
		}

		private void createGridField() {
			ListGridField nameField = new ListGridField(UpdateValueGridRecord.NAME, "Name");
			ListGridField dispNameField = new ListGridField(UpdateValueGridRecord.DISPNAME, "Display Name");
			ListGridField typeField = new ListGridField(UpdateValueGridRecord.TYPE_DISP_VAL, "Type");
			typeField.setWidth(90);
			ListGridField multiplicity = new ListGridField(UpdateValueGridRecord.MULTIPLE_DISP_VAL, "Multi");
			multiplicity.setWidth(35);
			multiplicity.setAlign(Alignment.RIGHT);
			ListGridField mustField = new ListGridField(UpdateValueGridRecord.NOT_NULL_DISP_VAL, "Required");
			mustField.setWidth(60);
			mustField.setAlign(Alignment.CENTER);
			ListGridField updatableField = new ListGridField(UpdateValueGridRecord.UPDATABLE_DISP_VAL, "CanEdit");
			updatableField.setWidth(55);
			updatableField.setAlign(Alignment.CENTER);
			ListGridField indexField = new ListGridField(UpdateValueGridRecord.INDEXTYPE_DISP_VAL, "Index");
			indexField.setWidth(40);
			indexField.setAlign(Alignment.CENTER);

			ListGridField valueTypeField = new ListGridField(UpdateValueGridRecord.VALUE_TYPE, "Value Type");
			valueTypeField.setType(ListGridFieldType.TEXT);
			valueTypeField.setWidth(130);

			ListGridField valueField = new ListGridField(UpdateValueGridRecord.UPDATE_VALUE, "Update Value");
			valueField.setType(ListGridFieldType.TEXT);
			//valueField.setWidth(300);
			valueField.setWidth("*");

			// 各フィールドをListGridに設定
			grid.setFields(nameField, dispNameField, typeField, multiplicity, mustField, updatableField, indexField, valueTypeField, valueField);
		}

		private void createGridRecord() {

			List<PropertyDefinition> lstProp = definition.getPropertyList();

			List<UpdateValueGridRecord> records = new ArrayList<>();

			for (PropertyDefinition property : lstProp) {
				//表示対象かをチェック
				if (!isShowRecord(property)) {
					continue;
				}

				UpdateValueGridRecord record = new UpdateValueGridRecord(property, definition);

				record.setValueType(UpdateAllValueType.LITERAL.name());	//初期値はLITERAL
				record.setPropertyDefinition(property);

				//レコードの入力可否を設定
				record.setEnabled(isEnable(property));

				records.add(record);
			}

			grid.setData(records.toArray(new UpdateValueGridRecord[]{}));
		}

		private boolean isShowRecord(PropertyDefinition prop) {
			//キー項目、audit項目は更新不可のため表示しない
			if (prop.isInherited()) {
				if (prop.getName().equals(Entity.OID) || prop.getName().equals(Entity.VERSION)
						|| prop.getName().equals(Entity.CREATE_BY) || prop.getName().equals(Entity.CREATE_DATE)
						|| prop.getName().equals(Entity.UPDATE_BY) || prop.getName().equals(Entity.UPDATE_DATE)) {
					return false;
				}
			}
			return true;
		}

		private boolean isEnable(PropertyDefinition prop) {

			if (prop.isReadOnly()) {
				return false;
			} else if (prop instanceof BinaryProperty
					|| prop instanceof LongTextProperty
					|| prop instanceof ReferenceProperty
					|| prop instanceof ExpressionProperty) {
				return false;
			} else if (!prop.isUpdatable()) {
				if (SmartGWTUtil.getBooleanValue(updateDisupdatablePropertyItem)) {
					return true;
				}
				return false;
			}
			return true;
		}

		private void changeUpdatableTarget() {
			for (ListGridRecord record : grid.getRecords()) {
				UpdateValueGridRecord uvRecord = (UpdateValueGridRecord)record;
				if (!uvRecord.isUpdatable()) {
					boolean isEnable = isEnable(uvRecord.getPropertyDefinition());
					if (!isEnable) {
						//入力不可になったら選択を解除
						grid.selectRecord(uvRecord, false);
					}
					uvRecord.setEnabled(isEnable);
					grid.refreshRow(grid.getRowNum(record));
				}
			}
		}

		private void editValue(final UpdateValueGridRecord record) {
			final UpdateValueEditDialog dialog = new UpdateValueEditDialog();

			//編集ダイアログでの変更結果取得
			dialog.addDataChangeHandler(new DataChangedHandler() {

				@Override
				public void onDataChanged(DataChangedEvent event) {
					String valueType = event.getValue(String.class, UpdateValueGridRecord.VALUE_TYPE);
					String updateValue = event.getValue(String.class, UpdateValueGridRecord.UPDATE_VALUE);

					record.setValueType(valueType);
					record.setUpdateValue(updateValue);

					grid.refreshRow(grid.getRowNum(record));
				}
			});

			dialog.setEditValue(record);
			dialog.show();
		}

		public boolean existsTarget() {

			ListGridRecord[] records = grid.getSelectedRecords();
			return records != null && records.length > 0;
		}

		public void apply(EntityUpdateAllCondition cond) {

			//Update可能かをチェックするかは更新不可項目を編集可能にしていない場合に行う
			cond.setCheckUpdatable(!SmartGWTUtil.getBooleanValue(updateDisupdatablePropertyItem));

			for (ListGridRecord record : grid.getSelectedRecords()) {
				UpdateValueGridRecord uvRecord = (UpdateValueGridRecord)record;

				String strValue = uvRecord.getUpdateValue();
				if (NULL_VALUE.equals(strValue)) {
					strValue = null;
				}

				UpdateAllValue updateValue = new UpdateAllValue();
				updateValue.setPropertyName(uvRecord.getRecordName());
				updateValue.setValue(strValue);
				updateValue.setValueType(UpdateAllValueType.valueOf(uvRecord.getValueType()));
				cond.addValue(updateValue);
			}
		}
	}

	private class UpdateValueGridRecord extends PropertyListGridRecord {

		public static final String VALUE_TYPE = "valueType";
		public static final String UPDATE_VALUE = "updateValue";
		public static final String PROPERTY_DEF = "propertyDefinition";

		public UpdateValueGridRecord(PropertyDefinition property, EntityDefinition entity) {
			super();
			applyFrom(property, entity, null);
		}

		public String getValueType() {
			return getAttribute(VALUE_TYPE);
		}

		public void setValueType(String type) {
			setAttribute(VALUE_TYPE, type);
		}

		public String getUpdateValue() {
			return getAttribute(UPDATE_VALUE);
		}

		public void setUpdateValue(String value) {
			setAttribute(UPDATE_VALUE, value);
		}

		public PropertyDefinition getPropertyDefinition() {
			return (PropertyDefinition)getAttributeAsObject(PROPERTY_DEF);
		}

		public void setPropertyDefinition(PropertyDefinition value) {
			setAttribute(PROPERTY_DEF, value);
		}
	}

	private class UpdateValueEditDialog extends AbstractWindow {

		private SelectItem valueTypeField;

		private TextAreaItem areaUpdateValueField;
		private TextItem textUpdateValueField;
		private SelectItem selectUpdateValueField;

		/** データ変更ハンドラ */
		private List<DataChangedHandler> handlers = new ArrayList<>();

		private PropertyDefinitionType propertyType;

		public UpdateValueEditDialog() {
			setTitle("Edit Update Value");
			setWidth(550);
			setHeight(300);
			setMinWidth(550);
			setMinHeight(300);

			setShowMinimizeButton(false);
			setShowMaximizeButton(true);
			setCanDragResize(true);

			setIsModal(true);
			setShowModalMask(true);

			centerInPage();

			valueTypeField = new SelectItem();
			valueTypeField.setTitle("Value Type");
			valueTypeField.setWidth(150);
			LinkedHashMap<String, String> valueMap = new LinkedHashMap<>();
			for (UpdateAllValueType valueType : UpdateAllValueType.values()) {
				valueMap.put(valueType.name(), valueType.name());
			}
			valueTypeField.setValueMap(valueMap);
			SmartGWTUtil.setRequired(valueTypeField);
			valueTypeField.addChangedHandler(new ChangedHandler() {
				@Override
				public void onChanged(ChangedEvent event) {
					setUpdateValue(null);
				}
			});

			areaUpdateValueField = new TextAreaItem();
			areaUpdateValueField.setTitle("Update Value");
			areaUpdateValueField.setWidth("100%");
			areaUpdateValueField.setHeight("100%");
			areaUpdateValueField.setVisible(false);

			textUpdateValueField = new TextItem();
			textUpdateValueField.setTitle("Update Value");
			textUpdateValueField.setWidth(300);
			textUpdateValueField.setVisible(false);

			selectUpdateValueField = new SelectItem();
			selectUpdateValueField.setTitle("Update Value");
			selectUpdateValueField.setWidth(300);
			textUpdateValueField.setVisible(false);

			final DynamicForm form = new DynamicForm();
			form.setMargin(5);
			form.setHeight100();
			form.setWidth100();

			form.setItems(valueTypeField, areaUpdateValueField, textUpdateValueField, selectUpdateValueField);

			IButton save = new IButton("OK");
			save.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					if (form.validate()) {
						fireDataChanged();

						//ダイアログ消去
						destroy();
					}
				}
			});

			IButton cancel = new IButton("Cancel");
			cancel.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					destroy();
				}
			});

			HLayout footer = new HLayout(5);
			footer.setMargin(5);
			footer.setHeight(20);
			footer.setWidth100();
			footer.setAlign(VerticalAlignment.CENTER);
			footer.setMembers(save, cancel);

			addItem(form);
			addItem(SmartGWTUtil.separator());
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

		public void setEditValue(UpdateValueGridRecord record) {
			propertyType = record.getRecordType();

			if (PropertyDefinitionType.BOOLEAN == propertyType) {
				LinkedHashMap<String, String> valueMap = new LinkedHashMap<>();
				valueMap.put(NULL_VALUE, "NULL");
				valueMap.put("true", "TRUE");
				valueMap.put("false", "FALSE");
				selectUpdateValueField.setValueMap(valueMap);
			} else if (PropertyDefinitionType.SELECT == propertyType) {
				SelectProperty pd = (SelectProperty) record.getPropertyDefinition();
				LinkedHashMap<String, String> valueMap = new LinkedHashMap<>();
				valueMap.put(NULL_VALUE, "NULL");
				for (SelectValue value : pd.getSelectValueList()) {
					valueMap.put(value.getValue(), value.getValue() + " (" + value.getDisplayName() + ")");
				}
				selectUpdateValueField.setValueMap(valueMap);
			}
			valueTypeField.setValue(record.getValueType());

			setUpdateValue(record.getUpdateValue());
		}

		/**
		 * Update Valueの入力用TextField制御
		 */
		private void setUpdateValue(String value) {
			UpdateAllValueType valueType = UpdateAllValueType.valueOf(SmartGWTUtil.getStringValue(valueTypeField));

			areaUpdateValueField.hide();
			textUpdateValueField.hide();
			selectUpdateValueField.hide();

			if (valueType == UpdateAllValueType.VALUE_EXPRESSION && PropertyDefinitionType.SELECT != propertyType) {
				areaUpdateValueField.setValue(value);
				areaUpdateValueField.show();
			} else {
				switch (propertyType) {
				case BOOLEAN:
					selectUpdateValueField.setValue(value);
					selectUpdateValueField.show();
					break;
				case DATE:
				case DATETIME:
				case TIME:
					textUpdateValueField.setValue(value);
					textUpdateValueField.show();
					break;
				case DECIMAL:
				case FLOAT:
				case INTEGER:
					textUpdateValueField.setValue(value);
					textUpdateValueField.show();
					break;
				case AUTONUMBER:
					textUpdateValueField.setValue(value);
					textUpdateValueField.show();
					break;
				case SELECT:
					selectUpdateValueField.setValue(value);
					selectUpdateValueField.show();
					break;
				case STRING:
					areaUpdateValueField.setValue(value);
					areaUpdateValueField.show();
					break;
				default:
				}
			}
		}

		/**
		 * データ変更通知処理
		 */
		private void fireDataChanged() {
			DataChangedEvent event = new DataChangedEvent();
			event.setValue(UpdateValueGridRecord.VALUE_TYPE, SmartGWTUtil.getStringValue(valueTypeField));

			if (areaUpdateValueField.isVisible()) {
				event.setValue(UpdateValueGridRecord.UPDATE_VALUE, SmartGWTUtil.getStringValue(areaUpdateValueField));
			}
			if (textUpdateValueField.isVisible()) {
				event.setValue(UpdateValueGridRecord.UPDATE_VALUE, SmartGWTUtil.getStringValue(textUpdateValueField));
			}
			if (selectUpdateValueField.isVisible()) {
				event.setValue(UpdateValueGridRecord.UPDATE_VALUE, SmartGWTUtil.getStringValue(selectUpdateValueField));
			}
			for (DataChangedHandler handler : handlers) {
				handler.onDataChanged(event);
			}
		}

	}
}
