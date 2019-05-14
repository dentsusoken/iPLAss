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

package org.iplass.adminconsole.client.metadata.ui.entity.property.common;

import org.iplass.adminconsole.client.metadata.ui.entity.property.PropertyAttribute;
import org.iplass.adminconsole.client.metadata.ui.entity.property.PropertyAttributePane;
import org.iplass.adminconsole.client.metadata.ui.entity.property.PropertyAttributePane.HasNotNullHandler;
import org.iplass.adminconsole.client.metadata.ui.entity.property.PropertyListGridRecord;
import org.iplass.adminconsole.client.metadata.ui.entity.property.common.PropertyCommonAttributeParts.TypeChangeHandler;
import org.iplass.mtp.entity.definition.PropertyDefinitionType;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.layout.VLayout;

public class PropertyCommonAttributePane extends VLayout implements PropertyAttributePane, TypeChangeHandler, HasNotNullHandler {

	private PropertyCommonAttributePaneHandler handler;

	private PropertyCommonAttributeController controller = GWT.create(PropertyCommonAttributeController.class);

	public interface PropertyCommonAttributePaneHandler {

		void onChangeNotNull(boolean isNotNull);

		void onChangeType(PropertyDefinitionType type);
	}

	public PropertyCommonAttributePane(final PropertyCommonAttributePaneHandler handler) {
		this.handler = handler;

		setWidth100();
		setAutoHeight();

		initialize();
	}

	@Override
	public void applyFrom(String defName, PropertyListGridRecord record, PropertyAttribute typeAttribute) {

		for (PropertyCommonAttributeParts parts : controller.partsList()) {
			parts.applyFrom(defName, record, typeAttribute);
		}
	}

	@Override
	public void applyTo(PropertyListGridRecord record) {

		for (PropertyCommonAttributeParts parts : controller.partsList()) {
			parts.applyTo(record);
		}

	}

	@Override
	public boolean validate() {

		boolean isValidate = true;

		for (PropertyCommonAttributeParts parts : controller.partsList()) {
			isValidate = isValidate & parts.validate();
		}

		return isValidate;
	}

	@Override
	public int panelHeight() {

		int height = 0;
		for (PropertyCommonAttributeParts parts : controller.partsList()) {
			height += parts.panelHeight();
		}
		return height;
	}

	@Override
	public void onChangeNotNullFromList(boolean isNotNull) {
		controller.notNullParts().onChangeNotNullFromList(isNotNull);
	}

	@Override
	public boolean canEditNotNull() {
		return controller.notNullParts().canEditNotNull();
	}

	@Override
	public void onTypeChanged(PropertyDefinitionType type) {

		for (PropertyCommonAttributeParts parts : controller.partsList()) {
			if (parts instanceof TypeChangeHandler) {
				((TypeChangeHandler)parts).onTypeChanged(type);
			}
		}

		handler.onChangeType(type);
	}

	private void initialize() {

		for (PropertyCommonAttributeParts parts : controller.partsList()) {
			addMember(parts);
			parts.initialize(this, handler);
		}

	}

}
