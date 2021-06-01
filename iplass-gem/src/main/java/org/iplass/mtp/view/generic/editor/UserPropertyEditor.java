/*
 * Copyright (C) 2013 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
public class UserPropertyEditor extends CustomPropertyEditor {

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

}
