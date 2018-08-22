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

package org.iplass.mtp.impl.view.generic.editor;

import javax.xml.bind.annotation.XmlSeeAlso;

import org.iplass.mtp.view.generic.editor.AutoNumberPropertyEditor;
import org.iplass.mtp.view.generic.editor.BinaryPropertyEditor;
import org.iplass.mtp.view.generic.editor.BooleanPropertyEditor;
import org.iplass.mtp.view.generic.editor.DateTimePropertyEditor;
import org.iplass.mtp.view.generic.editor.ExpressionPropertyEditor;
import org.iplass.mtp.view.generic.editor.NumberPropertyEditor;
import org.iplass.mtp.view.generic.editor.PrimitivePropertyEditor;
import org.iplass.mtp.view.generic.editor.PropertyEditor;
import org.iplass.mtp.view.generic.editor.SelectPropertyEditor;
import org.iplass.mtp.view.generic.editor.StringPropertyEditor;

/**
 *
 * @author lis3wg
 */
@XmlSeeAlso( {MetaAutoNumberPropertyEditor.class, MetaBinaryPropertyEditor.class, MetaBooleanPropertyEditor.class,
	MetaDateTimePropertyEditor.class, MetaExpressionPropertyEditor.class,
	MetaNumberPropertyEditor.class, MetaSelectPropertyEditor.class,
	MetaStringPropertyEditor.class})
public abstract class MetaPrimitivePropertyEditor extends MetaPropertyEditor {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -117786010987290670L;

	public static MetaPrimitivePropertyEditor createInstance(PropertyEditor editor) {
		if (editor instanceof AutoNumberPropertyEditor) {
			return MetaAutoNumberPropertyEditor.createInstance(editor);
		} else if (editor instanceof BinaryPropertyEditor) {
			return MetaBinaryPropertyEditor.createInstance(editor);
		} else if (editor instanceof BooleanPropertyEditor) {
			return MetaBooleanPropertyEditor.createInstance(editor);
		} else if (editor instanceof DateTimePropertyEditor) {
			return MetaDateTimePropertyEditor.createInstance(editor);
		} else if (editor instanceof ExpressionPropertyEditor) {
			return MetaExpressionPropertyEditor.createInstance(editor);
		} else if (editor instanceof NumberPropertyEditor) {
			return MetaNumberPropertyEditor.createInstance(editor);
		} else if (editor instanceof SelectPropertyEditor) {
			return MetaSelectPropertyEditor.createInstance(editor);
		} else if (editor instanceof StringPropertyEditor) {
			return MetaStringPropertyEditor.createInstance(editor);
		}
		return null;
	}

	/** 初期値 */
	private String defaultValue;

	/**
	 * 初期値を取得します。
	 * @return 初期値
	 */
	public String getDefaultValue() {
		return defaultValue;
	}

	/**
	 * 初期値を設定します。
	 * @param defaultValue 初期値
	 */
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	@Override
	protected void fillFrom(PropertyEditor editor) {
		super.fillFrom(editor);

		PrimitivePropertyEditor pe = (PrimitivePropertyEditor) editor;
		defaultValue = pe.getDefaultValue();
	}

	@Override
	protected void fillTo(PropertyEditor editor) {
		super.fillTo(editor);

		PrimitivePropertyEditor pe = (PrimitivePropertyEditor) editor;
		pe.setDefaultValue(defaultValue);
	}
}
