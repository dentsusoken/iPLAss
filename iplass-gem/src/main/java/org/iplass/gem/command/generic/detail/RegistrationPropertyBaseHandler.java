/*
 * Copyright (C) 2018 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.gem.command.generic.detail;

import org.iplass.mtp.view.generic.editor.PropertyEditor;
import org.iplass.mtp.view.generic.element.property.PropertyBase;

/**
 * プロパティ判断処理ハンドラ
 */
public interface RegistrationPropertyBaseHandler<T extends PropertyBase> {

	/**
	 * プロパティが表示対象になるか
	 * @param property プロパティ
	 * @return プロパティが表示対象になるか
	 */
	public boolean isDispProperty(T property);

	/**
	 * プロパティ編集するためのエディタを取得します。
	 * @param property プロパティ
	 * @return プロパティ編集するためのエディタ
	 */
	public PropertyEditor getEditor(T property);
}
