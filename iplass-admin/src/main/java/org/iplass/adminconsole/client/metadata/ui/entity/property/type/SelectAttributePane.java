/*
 * Copyright (C) 2017 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.entity.property.type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.data.MetaDataNameDS;
import org.iplass.adminconsole.client.metadata.data.MetaDataNameDS.MetaDataNameDSOption;
import org.iplass.adminconsole.client.metadata.ui.entity.property.PropertyAttribute;
import org.iplass.adminconsole.client.metadata.ui.entity.property.PropertyAttributePane;
import org.iplass.adminconsole.client.metadata.ui.entity.property.PropertyAttributePane.NeedsEnableLangMap;
import org.iplass.adminconsole.client.metadata.ui.entity.property.PropertyListGridRecord;
import org.iplass.adminconsole.client.metadata.ui.selectvalue.SelectValueEditDialog;
import org.iplass.adminconsole.client.metadata.ui.selectvalue.SelectValueListGridRecord;
import org.iplass.mtp.definition.LocalizedStringDefinition;
import org.iplass.mtp.entity.SelectValue;
import org.iplass.mtp.entity.definition.LocalizedSelectValueDefinition;
import org.iplass.mtp.entity.definition.properties.selectvalue.SelectValueDefinition;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class SelectAttributePane extends VLayout implements PropertyAttributePane, NeedsEnableLangMap {

	/** SelectValue定義名 */
	private SelectItem selGlobalSelectValue;
	/** SelectValue用のGrid */
	private ListGrid gridLocalSelectValue;

	private IButton btnAdd;
	private IButton btnDel;

	private boolean readOnly;
	private Map<String, String> enableLangMap;

	public SelectAttributePane() {

		setWidth100();
//		setHeight100();
		setAutoHeight();

		selGlobalSelectValue = new SelectItem();
		selGlobalSelectValue.setTitle("Global Value");
		selGlobalSelectValue.setTitleAlign(Alignment.LEFT);
		//_selectValueNameItem.setWidth(200);
		selGlobalSelectValue.setWidth("100%");
		SmartGWTUtil.addHoverToFormItem(selGlobalSelectValue, rs("ui_metadata_entity_PropertyListGrid_globalSelectValueComment"));
		//#19232
//		MetaDataViewButtonItem btnMetaView = new MetaDataViewButtonItem(SelectValueDefinition.class.getName());
//		btnMetaView.setPrompt(SmartGWTUtil.getHoverString("view the selected global value"));
//		btnMetaView.setMetaDataShowClickHandler(new MetaDataViewButtonItem.MetaDataShowClickHandler() {
//			@Override
//			public String targetDefinitionName() {
//				return SmartGWTUtil.getStringValue(selGlobalSelectValue);
//			}
//		});

		DynamicForm formGlobal = new DynamicForm();
		//formGlobal.setMargin(5);	//LocalValueに開始位置を合わせるためMarginなし
		formGlobal.setNumCols(4);
		formGlobal.setColWidths(100, 200, 50, "*");
		formGlobal.setHeight(25);
//		formGlobal.setItems(selGlobalSelectValue, btnMetaView);
		formGlobal.setItems(selGlobalSelectValue);

		gridLocalSelectValue = new ListGrid();
		gridLocalSelectValue.setMargin(5);
		gridLocalSelectValue.setHeight(110);
		gridLocalSelectValue.setWidth100();
		gridLocalSelectValue.setShowAllColumns(true);
		gridLocalSelectValue.setShowAllRecords(true);
		gridLocalSelectValue.setCanResizeFields(true);

		//grid内でのD&Dでの並べ替えを許可
		gridLocalSelectValue.setCanDragRecordsOut(true);
		gridLocalSelectValue.setCanAcceptDroppedRecords(true);
		gridLocalSelectValue.setCanReorderRecords(true);

		ListGridField valueField = new ListGridField("value", "Value");
		ListGridField dispNameField = new ListGridField("dispName", "DisplayName");
		gridLocalSelectValue.setFields(valueField, dispNameField);
		gridLocalSelectValue.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
			@Override
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				startSelectValueEdit(false, gridLocalSelectValue, (SelectValueListGridRecord)event.getRecord());
			}
		});

		btnAdd = new IButton("Add");
		btnAdd.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				startSelectValueEdit(true, gridLocalSelectValue, new SelectValueListGridRecord());
			}
		});
		btnDel = new IButton("Remove");
		btnDel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				gridLocalSelectValue.removeSelectedData();
			}
		});

		HLayout pnlButtons = new HLayout(5);
		pnlButtons.setWidth100();
		pnlButtons.setHeight(30);
		pnlButtons.setAlign(Alignment.LEFT);
		pnlButtons.setMargin(5);
		pnlButtons.addMember(btnAdd);
		pnlButtons.addMember(btnDel);

		addMember(formGlobal);
		addMember(SmartGWTUtil.titleLabel("Local Value"));
		addMember(gridLocalSelectValue);
		addMember(pnlButtons);

		initialize();
	}

	@Override
	public void applyFrom(String defName, PropertyListGridRecord record, PropertyAttribute typeAttribute) {

		if (record.isInherited()) {
			readOnly = true;
			btnAdd.setDisabled(true);
			btnDel.setDisabled(true);
		}

		SelectAttribute selectAttribute = (SelectAttribute)typeAttribute;

		selGlobalSelectValue.setValue(selectAttribute.getSelectValueDefinitionName());
		if (selectAttribute.getSelectValueDefinitionName() == null || selectAttribute.getSelectValueDefinitionName().isEmpty()) {
			//SelectValueDefNameが未指定の場合（Globalが未指定）のみ、LocalValueを設定する
			//SelectValueDefNameが設定されている場合、SelectValueListにその値が設定された状態で渡されるので、
			//SelectValueListの存在チェックでは、GlobalかLocalかは判断できない

			if (selectAttribute.getSelectValueList() != null && selectAttribute.getSelectValueList().size() > 0) {
				List<SelectValue> svList = selectAttribute.getSelectValueList();
				List<LocalizedSelectValueDefinition> lsvdList = selectAttribute.getLocalizedSelectValueList();
				int size = svList.size();
				SelectValueListGridRecord[] selRecords = new SelectValueListGridRecord[size];

				for (int i = 0; i < size; i++) {
					List<LocalizedStringDefinition> lsdList = new ArrayList<LocalizedStringDefinition>();
					SelectValue sv = svList.get(i);

					for (LocalizedSelectValueDefinition lsvd : lsvdList) {

						if (lsvd.getSelectValueList() != null && lsvd.getSelectValueList().size() > 0) {
							for (SelectValue lsv :lsvd.getSelectValueList()) {
								if (sv.getValue().equals(lsv.getValue())) {
									LocalizedStringDefinition lsd = new LocalizedStringDefinition();
									lsd.setLocaleName(lsvd.getLocaleName());
									lsd.setStringValue(lsv.getDisplayName());
									lsdList.add(lsd);
								}
							}
						}
					}
					SelectValueListGridRecord valueRecord = new SelectValueListGridRecord(sv.getValue(), sv.getDisplayName(), lsdList);
					selRecords[i] = valueRecord;
				}

				gridLocalSelectValue.setData(selRecords);
			}
		}

	}

	@Override
	public void applyTo(PropertyListGridRecord record) {

		List<SelectValue> svList = new ArrayList<SelectValue>();
		List<LocalizedSelectValueDefinition> lsvdList = new ArrayList<LocalizedSelectValueDefinition>();

		Map<String, List<SelectValue>> temp = new HashMap<String, List<SelectValue>>();
		for (Map.Entry<String, String> e : enableLangMap.entrySet()) {
			List<SelectValue> tempSvList = new ArrayList<SelectValue>();

			temp.put(e.getKey(), tempSvList);
		}

		for (ListGridRecord valueRecord : gridLocalSelectValue.getRecords()) {
			SelectValueListGridRecord sRecord = (SelectValueListGridRecord)valueRecord;
			SelectValue sv = new SelectValue(sRecord.getSelectValue(), sRecord.getDispName());
			svList.add(sv);

			if (sRecord.getLocalizedDisplayNameList() != null) {
				for (LocalizedStringDefinition def : sRecord.getLocalizedDisplayNameList()) {
					SelectValue localizedSelect = new SelectValue(sRecord.getSelectValue(), def.getStringValue());

					((List<SelectValue>) temp.get(def.getLocaleName())).add(localizedSelect);
				}
			}
		}
		for (Map.Entry<String, String> e : enableLangMap.entrySet()) {
			LocalizedSelectValueDefinition lsvd = new LocalizedSelectValueDefinition();
			lsvd.setLocaleName(e.getKey());
			lsvd.setSelectValueList(temp.get(e.getKey()));
			lsvdList.add(lsvd);
		}

		SelectAttribute selectAttribute = (SelectAttribute)record.getTypeAttribute();

		selectAttribute.setSelectValueList(svList);
		selectAttribute.setLocalizedSelectValueList(lsvdList);

		if (svList.isEmpty()) {
			//LocalValueが未指定の場合のみGlobalValueを有効にする
			if (selGlobalSelectValue.getValue() != null) {
				selectAttribute.setSelectValueDefinitionName(SmartGWTUtil.getStringValue(selGlobalSelectValue));
			} else {
				selectAttribute.setSelectValueDefinitionName("");
			}
		}

	}

	@Override
	public boolean validate() {
		return true;
	}

	@Override
	public int panelHeight() {
		return 220;
	}

	@Override
	public void setEnableLangMap(Map<String, String> enableLangMap) {
		this.enableLangMap = enableLangMap;
	}

	private void initialize() {

		MetaDataNameDS.setDataSource(selGlobalSelectValue, SelectValueDefinition.class, new MetaDataNameDSOption(true, false));
	}

	private void startSelectValueEdit(boolean isNewRow, ListGrid grid, SelectValueListGridRecord target) {
		SelectValueEditDialog dialog = new SelectValueEditDialog(isNewRow, grid, target, readOnly);
		dialog.show();
	}

	private String rs(String key) {
		return AdminClientMessageUtil.getString(key);
	}

}
