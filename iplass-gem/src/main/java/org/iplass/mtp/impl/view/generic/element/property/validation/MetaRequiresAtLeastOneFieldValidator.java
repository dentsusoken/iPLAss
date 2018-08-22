/*
 * Copyright (C) 2016 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.view.generic.element.property.validation;

import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.view.generic.element.property.validation.PropertyValidationCondition;
import org.iplass.mtp.view.generic.element.property.validation.RequiresAtLeastOneFieldValidator;
import org.iplass.mtp.view.generic.element.property.validation.ViewValidatorBase;

/**
 * いずれか必須
 * @author lis3wg
 */
public class MetaRequiresAtLeastOneFieldValidator extends MetaViewValidator {

	private static final long serialVersionUID = 4978461949172482103L;

	public static MetaViewValidator createInstance(ViewValidatorBase validator) {
		return new MetaRequiresAtLeastOneFieldValidator();
	}

	/** プロパティのリスト */
	private List<MetaPropertyValidationCondition> propertyList;

	@Override
	protected void fillFrom(ViewValidatorBase validator, String definitionId) {
		RequiresAtLeastOneFieldValidator arv = (RequiresAtLeastOneFieldValidator) validator;

		propertyList = new ArrayList<MetaPropertyValidationCondition>();
		if (arv.getPropertyList() != null) {
			for (PropertyValidationCondition condition : arv.getPropertyList()) {
				MetaPropertyValidationCondition meta = new MetaPropertyValidationCondition();
				meta.applyConfig(condition, definitionId);
				propertyList.add(meta);
			}
		}
	}

	@Override
	protected ViewValidatorBase fillTo(String definitionId) {
		RequiresAtLeastOneFieldValidator validator = new RequiresAtLeastOneFieldValidator();

		validator.setPropertyList(new ArrayList<PropertyValidationCondition>());
		if (propertyList != null) {
			for (MetaPropertyValidationCondition meta : propertyList) {
				PropertyValidationCondition condition = meta.currentConfig(definitionId);
				if (condition != null) {
					validator.getPropertyList().add(condition);
				}
			}
		}

		return validator;
	}

}
