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

import java.util.List;

import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.entity.property.PropertyAttribute;
import org.iplass.adminconsole.client.metadata.ui.entity.property.PropertyListGridRecord;
import org.iplass.mtp.entity.SelectValue;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.LocalizedSelectValueDefinition;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.properties.SelectProperty;

public class SelectAttribute implements PropertyAttribute {

	private SelectProperty attribute;

	public SelectAttribute() {
		attribute = new SelectProperty();
	}

	@Override
	public void applyFrom(PropertyDefinition property, EntityDefinition entity) {

		SelectProperty select = (SelectProperty)property;

		setSelectValueDefinitionName(select.getSelectValueDefinitionName());
		if (select.getSelectValueDefinitionName() == null || select.getSelectValueDefinitionName().isEmpty()) {
			setSelectValueList(select.getSelectValueList());
			setLocalizedSelectValueList(select.getLocalizedSelectValueList());
		}

	}

	@Override
	public void applyTo(PropertyDefinition property, EntityDefinition entity) {

		SelectProperty select = (SelectProperty)property;

		select.setSelectValueList(getSelectValueList());
		select.setLocalizedSelectValueList(getLocalizedSelectValueList());

		//LocalValueが未指定の場合のみGlobalValueを有効にする
		if (SmartGWTUtil.isEmpty(getSelectValueList())) {
			select.setSelectValueDefinitionName(getSelectValueDefinitionName());
		} else {
			select.setSelectValueDefinitionName(null);
		}
	}

	@Override
	public void applyTo(PropertyListGridRecord record) {
		if (SmartGWTUtil.isNotEmpty(getSelectValueDefinitionName())) {
			record.setRemarks(getSelectValueDefinitionName());
		}
	}

	public List<SelectValue> getSelectValueList() {
		return attribute.getSelectValueList();
	}

	public void setSelectValueList(List<SelectValue> selectValueList) {
		attribute.setSelectValueList(selectValueList);
	}

	public List<LocalizedSelectValueDefinition> getLocalizedSelectValueList() {
		return attribute.getLocalizedSelectValueList();
	}

	public void setLocalizedSelectValueList(List<LocalizedSelectValueDefinition> localizedSelectValueList) {
		attribute.setLocalizedSelectValueList(localizedSelectValueList);
	}

	public String getSelectValueDefinitionName() {
		return attribute.getSelectValueDefinitionName();
	}

	public void setSelectValueDefinitionName(String name) {
		attribute.setSelectValueDefinitionName(name);
	}

}
