/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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

import jakarta.xml.bind.annotation.XmlSeeAlso;

/**
 *
 * @author lis3wg
 */
@XmlSeeAlso({AutoNumberPropertyEditor.class, BinaryPropertyEditor.class, BooleanPropertyEditor.class, DateTimePropertyEditor.class,
	ExpressionPropertyEditor.class, NumberPropertyEditor.class, SelectPropertyEditor.class,
	StringPropertyEditor.class})
public abstract class PrimitivePropertyEditor extends PropertyEditor {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -7844751488921316842L;

	/**
	 * 初期値を取得します。
	 * @return 初期値
	 */
	public abstract String getDefaultValue();

	/**
	 * 初期値を設定します。
	 * @param defaultValue 初期値
	 */
	public abstract void setDefaultValue(String defaultValue);
}
