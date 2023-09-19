/*
 * Copyright (C) 2023 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
package org.iplass.mtp.impl.view.generic.element.section;

import org.iplass.mtp.impl.view.generic.editor.MetaPropertyEditor.PropertyEditorRuntime;

/**
 * セクションプロパティランタイムインターフェース
 *
 * <p>
 * 内部管理用のランタイム情報インターフェース
 * </p>
 *
 * <p>package private</p>
 *
 * @author SEKIGUCHI Naoya
 */
interface SectionPropertyRuntime {
	/**
	 * プロパティ名を取得する
	 * @return プロパティ名
	 */
	String getPropertyName();

	/**
	 * プロパティエディタを取得する
	 * @return プロパティエディタ
	 */
	PropertyEditorRuntime getEditor();
}
