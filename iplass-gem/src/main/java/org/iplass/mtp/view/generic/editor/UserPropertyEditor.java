/*
 * Copyright (C) 2013 DENTSU SOKEN INC. All Rights Reserved.
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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlEnumValue;

import org.iplass.adminconsole.view.annotation.InputType;
import org.iplass.adminconsole.view.annotation.MetaFieldInfo;
import org.iplass.adminconsole.view.annotation.generic.EntityViewField;
import org.iplass.adminconsole.view.annotation.generic.FieldReferenceType;
import org.iplass.mtp.view.generic.Jsp;
import org.iplass.mtp.view.generic.Jsps;
import org.iplass.mtp.view.generic.ViewConst;

/**
 *
 * @author lis3wg
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Jsps({
	@Jsp(path="/jsp/gem/generic/editor/UserPropertyEditor.jsp", key=ViewConst.DESIGN_TYPE_GEM)
})
public class UserPropertyEditor extends CustomPropertyEditor implements LabelablePropertyEditor {

	/** SerialVersionUID */
	private static final long serialVersionUID = 1005967564277184953L;

	/** 表示タイプ */
	public enum UserDisplayType {
		@XmlEnumValue("Label")LABEL,
		@XmlEnumValue("Hidden")HIDDEN
	}

	@MetaFieldInfo(
			displayName="表示タイプ",
			displayNameKey="generic_editor_UserPropertyEditor_displayTypeDisplaNameKey",
			inputType=InputType.ENUM,
			enumClass=UserDisplayType.class,
			required=true,
			displayOrder=100,
			description="画面に表示する方法を選択します。",
			descriptionKey="generic_editor_UserPropertyEditor_displayTypeDescriptionKey"
	)
	private UserDisplayType displayType;

	/** Label形式の場合の登録制御 */
	@MetaFieldInfo(
			displayName="Label形式の場合に表示値を登録する",
			displayNameKey="generic_editor_LabelablePropertyEditor_insertWithLabelValueDisplaNameKey",
			description="表示タイプがLabel形式の場合に表示値をそのまま登録するかを指定します。",
			inputType=InputType.CHECKBOX,
			displayOrder=200,
			descriptionKey="generic_editor_LabelablePropertyEditor_insertWithLabelValueDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.DETAIL}
	)
	private boolean insertWithLabelValue = true;

	/** Label形式の場合の更新制御 */
	@MetaFieldInfo(
			displayName="Label形式の場合に表示値で更新する",
			displayNameKey="generic_editor_LabelablePropertyEditor_updateWithLabelValueDisplaNameKey",
			description="表示タイプがLabel形式の場合に表示値で更新するかを指定します。",
			inputType=InputType.CHECKBOX,
			displayOrder=210,
			descriptionKey="generic_editor_LabelablePropertyEditor_updateWithLabelValueDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.DETAIL}
	)
	private boolean updateWithLabelValue = false;

	@Override
	public UserDisplayType getDisplayType() {
		if (displayType == null) {
			displayType = UserDisplayType.LABEL;
		}
		return displayType;
	}

	/**
	 * 表示タイプを設定します。
	 * @param displayType
	 */
	public void setDisplayType(UserDisplayType displayType) {
		this.displayType = displayType;
	}

	@Override
	public boolean isHide() {
		return displayType == UserDisplayType.HIDDEN;
	}

	@Override
	public boolean isLabel() {
		return displayType == UserDisplayType.LABEL;
	}

	@Override
	public boolean isInsertWithLabelValue() {
		return insertWithLabelValue;
	}

	/**
	 * Label形式の場合の登録制御を設定します。
	 *
	 * @param insertWithLabelValue Label形式の場合の登録制御
	 */
	public void setInsertWithLabelValue(boolean insertWithLabelValue) {
		this.insertWithLabelValue = insertWithLabelValue;
	}

	@Override
	public boolean isUpdateWithLabelValue() {
		return updateWithLabelValue;
	}

	/**
	 * Label形式の場合の更新制御を設定します。
	 *
	 * @param updateWithLabelValue Label形式の場合の更新制御
	 */
	public void setUpdateWithLabelValue(boolean updateWithLabelValue) {
		this.updateWithLabelValue = updateWithLabelValue;
	}

}
