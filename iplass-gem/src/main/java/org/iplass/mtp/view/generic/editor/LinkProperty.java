/*
 * Copyright (C) 2015 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.view.generic.editor;

import org.iplass.adminconsole.view.annotation.InputType;
import org.iplass.adminconsole.view.annotation.MetaFieldInfo;
import org.iplass.adminconsole.view.annotation.Refrectable;
import org.iplass.adminconsole.view.annotation.generic.EntityViewField;
import org.iplass.adminconsole.view.annotation.generic.FieldReferenceType;

/**
 * 参照型の場合の連動元のプロパティ定義
 */
public class LinkProperty implements Refrectable {

	private static final long serialVersionUID = -2468792250227327929L;

	/** 連動元プロパティ名 */
	@MetaFieldInfo(
			displayName="連動元プロパティ名",
			displayNameKey="generic_editor_LinkProperty_linkFromPropertyNameDisplaNameKey",
			inputType=InputType.PROPERTY,
			description="連動元となるEntityのプロパティを指定します。",
			descriptionKey="generic_editor_LinkProperty_linkFromPropertyNameDescriptionKey",
			useRootEntityName=true
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHCONDITION, FieldReferenceType.DETAIL}
	)
	private String linkFromPropertyName;

	/** ネストプロパティ同士の連動 */
	@MetaFieldInfo(
			displayName="ネストプロパティ同士の連動",
			displayNameKey="generic_editor_LinkProperty_withNestPropertyDisplaNameKey",
			inputType=InputType.CHECKBOX,
			description="連動元が同一のNestTable、ReferenceSection内の場合にチェックします。",
			descriptionKey="generic_editor_LinkProperty_withNestPropertyDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHCONDITION, FieldReferenceType.DETAIL}
	)
	private boolean withNestProperty;

	/** 連動対象プロパティ名 */
	@MetaFieldInfo(
			displayName="連動対象プロパティ名",
			displayNameKey="generic_editor_LinkProperty_linkToPropertyNameDisplaNameKey",
			inputType=InputType.PROPERTY,
			description="連動元プロパティに対応する参照先Entity(自身)のプロパティを指定します。",
			descriptionKey="generic_editor_LinkProperty_linkToPropertyNameDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHCONDITION, FieldReferenceType.DETAIL}
	)
	private String linkToPropertyName;

	public LinkProperty(){
	}

	/**
	 * 連動元プロパティ名を取得します。
	 * @return 連動元プロパティ名
	 */
	public String getLinkFromPropertyName() {
		return linkFromPropertyName;
	}

	/**
	 * 連動元プロパティ名を設定します。
	 * @param linkFromPropertyName 連動元プロパティ名
	 */
	public void setLinkFromPropertyName(String linkFromPropertyName) {
		this.linkFromPropertyName = linkFromPropertyName;
	}

	/**
	 * ネストプロパティ同士の連動かを取得します。
	 * @return ネストプロパティ同士の連動か
	 */
	public boolean isWithNestProperty() {
		return withNestProperty;
	}

	/**
	 * ネストプロパティ同士の連動かを設定します。
	 * @param withNestProperty ネストプロパティ同士の連動か
	 */
	public void setWithNestProperty(boolean withNestProperty) {
		this.withNestProperty = withNestProperty;
	}

	/**
	 * 連動対象プロパティ名を取得します。
	 * @return 連動対象プロパティ名
	 */
	public String getLinkToPropertyName() {
		return linkToPropertyName;
	}

	/**
	 * 連動対象プロパティ名を設定します。
	 * @param linkToPropertyName 連動対象プロパティ名
	 */
	public void setLinkToPropertyName(String linkToPropertyName) {
		this.linkToPropertyName = linkToPropertyName;
	}

}
