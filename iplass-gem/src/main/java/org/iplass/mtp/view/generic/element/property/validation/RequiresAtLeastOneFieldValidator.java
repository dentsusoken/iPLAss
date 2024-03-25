/*
 * Copyright (C) 2016 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.view.generic.element.property.validation;

import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;

import org.iplass.adminconsole.view.annotation.InputType;
import org.iplass.adminconsole.view.annotation.MetaFieldInfo;
import org.iplass.adminconsole.view.annotation.generic.EntityViewField;
import org.iplass.adminconsole.view.annotation.generic.FieldReferenceType;

/**
 * いずれか必須
 * @author lis3wg
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class RequiresAtLeastOneFieldValidator extends ViewValidatorBase {

	/** serialVersionUID */
	private static final long serialVersionUID = -2381982337496185594L;

	/** プロパティのリスト */
	@MetaFieldInfo(displayName="対象プロパティ",
			displayNameKey="generic_element_property_validation_RequiresAtLeastOneFieldValidator_propertyListDisplaNameKey",
			inputType=InputType.REFERENCE,
			referenceClass=PropertyValidationCondition.class,
			multiple=true,
			description="組み合わせて必須チェックを行うプロパティを指定します。",
			descriptionKey="generic_element_property_validation_RequiresAtLeastOneFieldValidator_propertyListDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHCONDITION}
	)
	private List<PropertyValidationCondition> propertyList;

	/**
	 * プロパティのリストを取得します。
	 * @return プロパティのリスト
	 */
	public List<PropertyValidationCondition> getPropertyList() {
	    return propertyList;
	}

	/**
	 * プロパティのリストを設定します。
	 * @param propertyList プロパティのリスト
	 */
	public void setPropertyList(List<PropertyValidationCondition> propertyList) {
	    this.propertyList = propertyList;
	}

}
