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

import java.util.LinkedHashMap;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.rpc.AdminAsyncCallback;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.entity.property.PropertyAttribute;
import org.iplass.adminconsole.client.metadata.ui.entity.property.PropertyAttributePane;
import org.iplass.adminconsole.client.metadata.ui.entity.property.PropertyListGridRecord;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.properties.AutoNumberProperty;
import org.iplass.mtp.entity.definition.properties.NumberingType;

import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.VLayout;

public class AutoNumberAttributePane extends VLayout implements PropertyAttributePane {

	private DynamicForm form = new DynamicForm();

	/** 開始値 */
	private TextItem txtStartValue;
	/** 桁数 */
	private TextItem txtFixedNumber;
	/** 採番タイプ */
	private SelectItem selNumberingType;
	/** Format */
	private TextItem txtFormatScript;

	private ButtonItem btnCurrent;

	private String defName;
	private String savedPropertyName;

	private final MetaDataServiceAsync service = MetaDataServiceFactory.get();

	public AutoNumberAttributePane() {

		setWidth100();
//		setHeight100();
		setAutoHeight();

		txtStartValue = new TextItem();
		txtStartValue.setTitle(rs("ui_metadata_entity_PropertyListGrid_startVal"));
		txtStartValue.setKeyPressFilter(KEYFILTER_NUM);
		txtStartValue.setWidth(40);
		SmartGWTUtil.addHoverToFormItem(txtStartValue, rs("ui_metadata_entity_PropertyListGrid_startValComment"));
		txtFixedNumber = new TextItem();
		txtFixedNumber.setTitle(rs("ui_metadata_entity_PropertyListGrid_fixedNumDig"));
		txtFixedNumber.setKeyPressFilter(KEYFILTER_NUM);
		txtFixedNumber.setWidth(40);
		SmartGWTUtil.addHoverToFormItem(txtFixedNumber, rs("ui_metadata_entity_PropertyListGrid_fixedNumDigComment"));
		selNumberingType = new SelectItem();
		selNumberingType.setTitle(rs("ui_metadata_entity_PropertyListGrid_numberingRules"));
		selNumberingType.setWidth(200);
		SmartGWTUtil.addHoverToFormItem(selNumberingType, rs("ui_metadata_entity_PropertyListGrid_numberingRulesComment1"));
		txtFormatScript = new TextItem();
		txtFormatScript.setTitle(rs("ui_metadata_entity_PropertyListGrid_formatScript"));
		txtFormatScript.setWidth(450);
		txtFormatScript.setColSpan(4);
		SmartGWTUtil.addHoverToFormItem(txtFormatScript, rs("ui_metadata_entity_PropertyListGrid_numberingRulesComment2"));
		//ちょっとヒントが大きいので別ダイアログで表示
		String contentsStyle = "style=\"margin-left:5px;\"";
		String tableStyle = "style=\"border: thin gray solid;padding:5px;white-space:nowrap;\"";
		SmartGWTUtil.addHintToFormItem(txtFormatScript,
				"<br/>"

				+ "<div>" + rs("ui_metadata_entity_PropertyListGrid_exampleTerm")
				+ "<p " + contentsStyle + ">" + rs("ui_metadata_entity_PropertyListGrid_exampleFormat") + "</p>"
				+ "</div>"

				+ "<div><b>" + rs("ui_metadata_entity_PropertyListGrid_availBindVariable") + "</b><br/>"
				+ "<p " + contentsStyle + ">"
				+ "<table style=\"border-collapse:collapse;\">"

				+ "<tr><th " + tableStyle + ">" + rs("ui_metadata_entity_PropertyListGrid_format")
				+ "</th><th "+ tableStyle + ">" + rs("ui_metadata_entity_PropertyListGrid_outputContent") + "</th></tr>"

				+ "<tr><td " + tableStyle + ">nextVal()</td><td "+ tableStyle + ">" + rs("ui_metadata_entity_PropertyListGrid_nextNumberingNum") + "</td></tr>"
				+ "<tr><td " + tableStyle + ">yyyy</td><td "+ tableStyle + ">" + rs("ui_metadata_entity_PropertyListGrid_year") + "</td></tr>"
				+ "<tr><td " + tableStyle + ">MM</td><td "+ tableStyle + ">" + rs("ui_metadata_entity_PropertyListGrid_month") + "</td></tr>"
				+ "<tr><td " + tableStyle + ">dd</td><td "+ tableStyle + ">" + rs("ui_metadata_entity_PropertyListGrid_day") + "</td></tr>"
				+ "<tr><td " + tableStyle + ">HH</td><td "+ tableStyle + ">" + rs("ui_metadata_entity_PropertyListGrid_hour") + "</td></tr>"
				+ "<tr><td " + tableStyle + ">mm</td><td "+ tableStyle + ">" + rs("ui_metadata_entity_PropertyListGrid_minute") + "</td></tr>"
				+ "<tr><td " + tableStyle + ">ss</td><td "+ tableStyle + ">" + rs("ui_metadata_entity_PropertyListGrid_second") + "</td></tr>"
				+ "<tr><td " + tableStyle + ">date</td><td "+ tableStyle + ">" + rs("ui_metadata_entity_PropertyListGrid_instansTimestamp") + "</td></tr>"
				+ "<tr><td " + tableStyle + ">user</td><td "+ tableStyle + ">" + rs("ui_metadata_entity_PropertyListGrid_userInfo") + "</td></tr>"
				+ "<tr><td " + tableStyle + ">entity</td><td "+ tableStyle + ">" + rs("ui_metadata_entity_PropertyListGrid_entity") + "</td></tr>"

				+ "</table></p></div>"
				);

		btnCurrent = new ButtonItem();
		btnCurrent.setWidth(100);
		btnCurrent.setStartRow(false);
		btnCurrent.setTitle("Current Value");
		btnCurrent.setPrompt(SmartGWTUtil.getHoverString(rs("ui_metadata_entity_PropertyListGrid_showCurrentValueList")));
		btnCurrent.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

			@Override
			public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
				getCurrentCounter();
			}

		});

		form.setMargin(5);
		form.setNumCols(6);
		form.setWidth100();
		form.setHeight(60);
		form.setItems(txtStartValue, txtFixedNumber, selNumberingType, txtFormatScript, btnCurrent);

		addMember(form);

		initialize();
	}

	@Override
	public void applyFrom(String defName, PropertyListGridRecord record, PropertyAttribute typeAttribute) {
		this.defName = defName;

		if (record.isInherited()) {
			btnCurrent.setDisabled(true);
		}
		if (record.isInsert() || record.isDelete()) {
			btnCurrent.setDisabled(true);
		}

		savedPropertyName = record.getSavedName();

		AutoNumberAttribute autoNumberAttribute = (AutoNumberAttribute)typeAttribute;

		txtStartValue.setValue(autoNumberAttribute.getAutoNumberStartWith());
		txtFixedNumber.setValue(autoNumberAttribute.getAutoNumberFixedNumber());
		selNumberingType.setValue(autoNumberAttribute.getAutoNumberNumberingType().name());
		txtFormatScript.setValue(autoNumberAttribute.getAutoNumberFormatScript());
	}

	@Override
	public void applyTo(PropertyListGridRecord record) {

		AutoNumberAttribute autoNumberAttribute = (AutoNumberAttribute)record.getTypeAttribute();

		if (txtStartValue.getValue() != null) {
			autoNumberAttribute.setAutoNumberStartWith(Long.parseLong(SmartGWTUtil.getStringValue(txtStartValue)));
		}
		if (txtFixedNumber.getValue() != null) {
			autoNumberAttribute.setAutoNumberFixedNumber(Integer.parseInt(SmartGWTUtil.getStringValue(txtFixedNumber)));
		}
		if (selNumberingType.getValue() != null) {
			autoNumberAttribute.setAutoNumberNumberingType(NumberingType.valueOf(SmartGWTUtil.getStringValue(selNumberingType)));
		}
		if (txtFormatScript.getValue() != null) {
			autoNumberAttribute.setAutoNumberFormatScript(SmartGWTUtil.getStringValue(txtFormatScript));
		}
	}

	@Override
	public boolean validate() {
		return form.validate();
	}

	@Override
	public int panelHeight() {
		return 80;
	}

	private void initialize() {

		LinkedHashMap<String, String> numberingTypeMap = new LinkedHashMap<String, String>();
		numberingTypeMap.put(NumberingType.ALLOW_SKIPPING.name(), rs("ui_metadata_entity_PropertyListGrid_allowSkip"));
		numberingTypeMap.put(NumberingType.STRICT_SEQUENCE.name(), rs("ui_metadata_entity_PropertyListGrid_strictSeq"));
		selNumberingType.setValueMap(numberingTypeMap);
	}

	private void getCurrentCounter() {

		checkEntityDefinition(new AdminAsyncCallback<PropertyDefinition>() {

			@Override
			public void onSuccess(PropertyDefinition pd) {

				if (!(pd instanceof AutoNumberProperty)) {
					SC.say(rs("ui_metadata_entity_PropertyListGrid_propertyCannotGetTypeNotAutoNumberErr"));
					return;
				}
				AutoNumberValueListDialog dialog = new AutoNumberValueListDialog(defName, savedPropertyName);
				dialog.show();
			}

		});

	}

	private void checkEntityDefinition(AdminAsyncCallback<PropertyDefinition> callback) {

		if (savedPropertyName == null) {
			SC.say(rs("ui_metadata_entity_PropertyListGrid_propertyNotSavedErr"));
			return;
		}

		service.getEntityDefinition(TenantInfoHolder.getId(), defName, new AdminAsyncCallback<EntityDefinition>() {

			@Override
			public void onSuccess(EntityDefinition result) {
				PropertyDefinition pd = result.getProperty(savedPropertyName);
				if (pd == null) {
					SC.say(rs("ui_metadata_entity_PropertyListGrid_propertyNotSavedErr"));
					return;
				}

				callback.onSuccess(pd);
			}

		});
	}

	private String rs(String key) {
		return AdminClientMessageUtil.getString(key);
	}
}
