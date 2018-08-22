/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.view.generic.element;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.iplass.adminconsole.view.annotation.InputType;
import org.iplass.adminconsole.view.annotation.MetaFieldInfo;
import org.iplass.adminconsole.view.annotation.Refrectable;
import org.iplass.adminconsole.view.annotation.generic.EntityViewField;
import org.iplass.adminconsole.view.annotation.generic.FieldReferenceType;
import org.iplass.mtp.view.generic.element.property.PropertyBase;
import org.iplass.mtp.view.generic.element.section.Section;

/**
 * 画面を構成する要素
 * @author lis3wg
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({Section.class, PropertyBase.class, Button.class,
	TemplateElement.class, ScriptingElement.class, Link.class, BlankSpace.class, VirtualPropertyItem.class})
public abstract class Element implements Refrectable {

	public enum EditDisplayType {
		INSERT,
		UPDATE,
		BOTH
	}

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -5644975822149407087L;

	/** 表示フラグ */
	@MetaFieldInfo(
			displayName="表示/非表示",
			displayNameKey="generic_element_Element_dispFlagDisplaNameKey",
			inputType=InputType.CHECKBOX,
			description="画面に表示するかを設定します。",
			descriptionKey="generic_element_Element_dispFlagDescriptionKey"
	)
	private boolean dispFlag;

	/** 編集時の表示タイプ */
	@MetaFieldInfo(
			displayName="新規/編集時の表示可否",
			displayNameKey="generic_element_Element_editDisplayTypeDisplaNameKey",
			inputType=InputType.ENUM,
			enumClass=EditDisplayType.class,
			description="新規登録または編集時に項目を表示するかを設定します。<br>" +
					"INSERT:新規登録時のみ表示します。<br>" +
					"UPDATE:編集時のみ表示します。<br>" +
					"BOTH  :常に表示します、未選択時もBOTHと同様になります。",
			descriptionKey="generic_element_Element_editDisplayTypeDescriptionKey"
	)
	@EntityViewField(referenceTypes=FieldReferenceType.DETAIL)
	private EditDisplayType editDisplayType;

	/**
	 * 表示フラグを取得します。
	 * @return 表示フラグ
	 */
	public boolean isDispFlag() {
		return dispFlag;
	}

	/**
	 * 表示フラグを設定します。
	 * @param dispFlag 表示フラグ
	 */
	public void setDispFlag(boolean dispFlag) {
		this.dispFlag = dispFlag;
	}

	/**
	 * 編集時の表示タイプを取得します。
	 * @return 編集時の表示タイプ
	 */
	public EditDisplayType getEditDisplayType() {
	    return editDisplayType;
	}

	/**
	 * 編集時の表示タイプを設定します。
	 * @param editDisplayType 編集時の表示タイプ
	 */
	public void setEditDisplayType(EditDisplayType editDisplayType) {
	    this.editDisplayType = editDisplayType;
	}

}
