/*
 * Copyright (C) 2017 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.view.generic.common;

import org.iplass.adminconsole.view.annotation.InputType;
import org.iplass.adminconsole.view.annotation.MetaFieldInfo;
import org.iplass.adminconsole.view.annotation.Refrectable;
import org.iplass.adminconsole.view.annotation.generic.EntityViewField;
import org.iplass.adminconsole.view.annotation.generic.FieldReferenceType;

public class AutocompletionProperty implements Refrectable {

	private static final long serialVersionUID = -4148852727421163757L;

	/** プロパティ名 */
	@MetaFieldInfo(
		displayName="プロパティ名",
		displayNameKey="generic_common_AutocompletionProperty_propertyNameDisplaNameKey",
		inputType=InputType.PROPERTY,
		required=true,
		description="連動元のプロパティ名を指定してください。",
		descriptionKey="generic_common_AutocompletionProperty_propertyNameDescriptionKey"
	)
	@EntityViewField()
	private String propertyName;

	/** ネストプロパティか */
	@MetaFieldInfo(
		displayName="ネストプロパティ同士の自動補完",
		displayNameKey="generic_common_AutocompletionProperty_nestPropertyDisplaNameKey",
		inputType=InputType.CHECKBOX,
		description="イベントの発生元と自動補完先が同一のNestTable、ReferenceSection内の場合にチェックします。",
		descriptionKey="generic_common_AutocompletionProperty_nestPropertyDescriptionKey"
	)
	@EntityViewField(
		referenceTypes={FieldReferenceType.DETAIL}
	)
	private boolean nestProperty;

	/** 参照プロパティのインデックス */
	@MetaFieldInfo(
		displayName="参照プロパティのインデックス",
		displayNameKey="generic_common_AutocompletionProperty_referencePropertyIndexDisplaNameKey",
		inputType=InputType.NUMBER,
		description="参照先の項目(NestTable、ReferenceSectionの項目)の変更をトリガーにして参照元の項目を自動補完する際に、<br>"
				+ "NestTable内の特定の行や、特定のReferenceSectionのみを対象にする場合、そのインデックスを指定します。<br>"
				+ "未指定の場合、全ての行やセクションの変更イベントがトリガーになります。",
		descriptionKey="generic_common_AutocompletionProperty_referencePropertyIndexDescriptionKey"
	)
	@EntityViewField(
		referenceTypes={FieldReferenceType.DETAIL}
	)
	private Integer referencePropertyIndex;

	/**
	 * @return propertyName
	 */
	public String getPropertyName() {
		return propertyName;
	}

	/**
	 * @param propertyName セットする propertyName
	 */
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	/**
	 * @return nestProperty
	 */
	public boolean isNestProperty() {
		return nestProperty;
	}

	/**
	 * @param nestProperty セットする nestProperty
	 */
	public void setNestProperty(boolean nestProperty) {
		this.nestProperty = nestProperty;
	}

	/**
	 * @return referencePropertyIndex
	 */
	public Integer getReferencePropertyIndex() {
		return referencePropertyIndex;
	}

	/**
	 * @param referencePropertyIndex セットする referencePropertyIndex
	 */
	public void setReferencePropertyIndex(Integer referencePropertyIndex) {
		this.referencePropertyIndex = referencePropertyIndex;
	}

}
