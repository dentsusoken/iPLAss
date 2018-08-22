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
			descriptionKey="generic_editor_LinkProperty_linkFromPropertyNameDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHCONDITION, FieldReferenceType.DETAIL}
	)
	private String linkFromPropertyName;

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

	public String getLinkFromPropertyName() {
		return linkFromPropertyName;
	}

	public void setLinkFromPropertyName(String linkFromPropertyName) {
		this.linkFromPropertyName = linkFromPropertyName;
	}

	public String getLinkToPropertyName() {
		return linkToPropertyName;
	}

	public void setLinkToPropertyName(String linkToPropertyName) {
		this.linkToPropertyName = linkToPropertyName;
	}
}
