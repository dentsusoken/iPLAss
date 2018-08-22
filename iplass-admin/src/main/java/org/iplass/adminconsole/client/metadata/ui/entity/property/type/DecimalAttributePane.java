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
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.entity.property.PropertyAttribute;
import org.iplass.adminconsole.client.metadata.ui.entity.property.PropertyAttributePane;
import org.iplass.adminconsole.client.metadata.ui.entity.property.PropertyListGridRecord;
import org.iplass.mtp.entity.definition.properties.RoundingMode;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.VLayout;

public class DecimalAttributePane extends VLayout implements PropertyAttributePane {

	private DynamicForm form = new DynamicForm();

	/** スケール */
	private TextItem txtScale;
	/** 丸めモード */
	private SelectItem selRoundMode;

	public DecimalAttributePane() {

		setWidth100();
//		setHeight100();
		setAutoHeight();

		txtScale = new TextItem();
		txtScale.setTitle(rs("ui_metadata_entity_PropertyListGrid_numberDecimal"));
		txtScale.setKeyPressFilter(KEYFILTER_NUM);
		txtScale.setWidth(40);

		selRoundMode = new SelectItem();
		selRoundMode.setTitle(rs("ui_metadata_entity_PropertyListGrid_roundMode"));
		selRoundMode.setWidth(200);

		form.setMargin(5);
		form.setNumCols(6);
		form.setWidth100();
		form.setHeight(30);
		form.setItems(txtScale, selRoundMode);

		addMember(form);

		initialize();
	}

	@Override
	public void applyFrom(String defName, PropertyListGridRecord record, PropertyAttribute typeAttribute) {

		DecimalAttribute decimalAttribute = (DecimalAttribute)typeAttribute;

		txtScale.setValue(decimalAttribute.getScale());
		if (decimalAttribute.getRoundingMode() != null) {
			selRoundMode.setValue(decimalAttribute.getRoundingMode().name());
		}
	}

	@Override
	public void applyTo(PropertyListGridRecord record) {

		DecimalAttribute decimalAttribute = (DecimalAttribute)record.getTypeAttribute();

		if (txtScale.getValueAsString() != null) {
			decimalAttribute.setScale(Integer.parseInt(SmartGWTUtil.getStringValue(txtScale)));
		}
		if (selRoundMode.getValue() != null) {
			decimalAttribute.setRoundingMode(RoundingMode.valueOf(SmartGWTUtil.getStringValue(selRoundMode)));
		}
	}

	@Override
	public boolean validate() {
		return form.validate();
	}

	@Override
	public int panelHeight() {
		return 50;
	}

	private void initialize() {

		LinkedHashMap<String, String> roundModeMap = new LinkedHashMap<String, String>();
		roundModeMap.put(RoundingMode.UP.name(), rs("ui_metadata_entity_PropertyListGrid_roundModeUp"));
		roundModeMap.put(RoundingMode.DOWN.name(), rs("ui_metadata_entity_PropertyListGrid_roundModeDown"));
		roundModeMap.put(RoundingMode.CEILING.name(), rs("ui_metadata_entity_PropertyListGrid_roundModeCeiling"));
		roundModeMap.put(RoundingMode.FLOOR.name(), rs("ui_metadata_entity_PropertyListGrid_roundModeFloor"));
		roundModeMap.put(RoundingMode.HALF_UP.name(), rs("ui_metadata_entity_PropertyListGrid_roundModeHalfUp"));
		roundModeMap.put(RoundingMode.HALF_DOWN.name(), rs("ui_metadata_entity_PropertyListGrid_roundModeHalfDown"));
		roundModeMap.put(RoundingMode.HALF_EVEN.name(), rs("ui_metadata_entity_PropertyListGrid_roundModeHalfEven"));
		selRoundMode.setValueMap(roundModeMap);
	}


	private String rs(String key) {
		return AdminClientMessageUtil.getString(key);
	}
}
