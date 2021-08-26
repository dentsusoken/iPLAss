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

package org.iplass.mtp.impl.view.generic.editor;

import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.properties.StringProperty;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.metadata.MetaDataRuntime;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.impl.view.generic.EntityViewRuntime;
import org.iplass.mtp.impl.view.generic.FormViewRuntime;
import org.iplass.mtp.impl.view.generic.element.property.MetaPropertyLayout;
import org.iplass.mtp.view.generic.editor.PropertyEditor;
import org.iplass.mtp.view.generic.editor.UserPropertyEditor;
import org.iplass.mtp.view.generic.editor.UserPropertyEditor.UserDisplayType;

public class MetaUserPropertyEditor extends MetaCustomPropertyEditor {

	/** SerialVersionUID */
	private static final long serialVersionUID = -3636767642233881066L;

	/** 表示タイプ */
	private UserDisplayType displayType;

	/** Label形式の場合の登録制御 */
	private boolean insertWithLabelValue = true;

	/** Label形式の場合の更新制御 */
	private boolean updateWithLabelValue = false;

	public static MetaUserPropertyEditor createInstance(PropertyEditor editor) {
		return new MetaUserPropertyEditor();
	}

	/**
	 * 表示タイプを取得します。
	 * @return 表示タイプ
	 */
	public UserDisplayType getDisplayType() {
	    return displayType;
	}

	/**
	 * 表示タイプを設定します。
	 * @param displayType 表示タイプ
	 */
	public void setDisplayType(UserDisplayType displayType) {
	    this.displayType = displayType;
	}

	/**
	 * 表示タイプがLabel形式の場合に、登録時に登録対象にするかを返します。
	 *
	 * @return true：登録対象
	 */
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

	/**
	 * 表示タイプがLabel形式の場合に、更新時に更新対象にするかを返します。
	 *
	 * @return true：更新対象
	 */
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

	@Override
	public void applyConfig(PropertyEditor editor) {
		super.fillFrom(editor);

		UserPropertyEditor upe = (UserPropertyEditor) editor;
		displayType = upe.getDisplayType();
		insertWithLabelValue = upe.isInsertWithLabelValue();
		updateWithLabelValue = upe.isUpdateWithLabelValue();
	}

	@Override
	public PropertyEditor currentConfig(String propertyName) {
		UserPropertyEditor editor = new UserPropertyEditor();
		super.fillTo(editor);

		if (displayType == null) {
			editor.setDisplayType(UserDisplayType.LABEL);
		} else {
			editor.setDisplayType(displayType);
		}
		editor.setInsertWithLabelValue(insertWithLabelValue);
		editor.setUpdateWithLabelValue(updateWithLabelValue);
		return editor;
	}

	@Override
	public MetaUserPropertyEditor copy() {
		return ObjectUtil.deepCopy(this);
	}

	@Override
	public MetaDataRuntime createRuntime(EntityViewRuntime entityView, FormViewRuntime formView,
			MetaPropertyLayout propertyLayout, EntityContext context, EntityHandler eh) {
		return new PropertyEditorRuntime(entityView, formView, propertyLayout, context, eh) {
			@Override
			protected boolean checkPropertyType(PropertyDefinition pd) {
				return pd == null || pd instanceof StringProperty;
			}

		};
	}

}
