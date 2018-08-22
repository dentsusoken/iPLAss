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

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.metadata.ui.entity.property.PropertyAttributePane.HasNotNullHandler;

public class PropertyCommonAttributeControllerImpl implements PropertyCommonAttributeController {

	private List<PropertyCommonAttributeParts> partsList;
	private HasNotNullHandler notNullHandler;

	public PropertyCommonAttributeControllerImpl() {
		partsList = new ArrayList<>();

		partsList.add(new CommonAttributeParts1());

		notNullHandler = new CommonAttributeParts2();
		partsList.add((PropertyCommonAttributeParts)notNullHandler);

		partsList.add(new CommonAttributeParts3());
	}

	@Override
	public List<PropertyCommonAttributeParts> partsList() {
		return partsList;
	}

	@Override
	public HasNotNullHandler notNullParts() {
		return notNullHandler;
	}

	@Override
	public CommonAttribute createCommonAttribute() {
		return new CommonAttribute();
	}

}
