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

package org.iplass.mtp.view.generic.element.property.validation;

import org.iplass.adminconsole.view.annotation.InputType;
import org.iplass.adminconsole.view.annotation.MetaFieldInfo;
import org.iplass.adminconsole.view.annotation.Refrectable;
import org.iplass.adminconsole.view.annotation.generic.EntityViewField;
import org.iplass.adminconsole.view.annotation.generic.FieldReferenceType;

/**
 * 入力チェックの条件となるプロパティ
 * @author lis3wg
 */
public class PropertyValidationCondition implements Refrectable {

	private static final long serialVersionUID = 7390389987394999176L;

	/** プロパティ名 */
	@MetaFieldInfo(
			displayName="プロパティ名",
			displayNameKey="generic_element_property_validation_PropertyValidationCondition_propertyNameDisplaNameKey",
			inputType=InputType.PROPERTY,
			required=true,
			description="入力チェック対象のプロパティ名を設定してください",
			descriptionKey="generic_element_property_validation_PropertyValidationCondition_propertyNameDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHCONDITION}
	)
	private String propertyName;

	/**
	 * プロパティ名を取得します。
	 * @return プロパティ名
	 */
	public String getPropertyName() {
	    return propertyName;
	}

	/**
	 * プロパティ名を設定します。
	 * @param propertyName プロパティ名
	 */
	public void setPropertyName(String propertyName) {
	    this.propertyName = propertyName;
	}

}
