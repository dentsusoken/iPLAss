/*
 * Copyright (C) 2021 DENTSU SOKEN INC. All Rights Reserved.
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

import java.util.List;

import org.iplass.mtp.definition.LocalizedStringDefinition;
import org.iplass.mtp.view.generic.editor.PropertyEditor;

/**
 * CSV出力アイテム
 */
public interface CsvItem {

	/**
	 * CSV出力対象とするか
	 *
	 * @return true CSV出力する
	 */
	public boolean isOutputCsv();

	/**
	 * プロパティ名を取得します。
	 * @return プロパティ名
	 */
	public String getPropertyName();

	/**
	 * プロパティエディタを取得します。
	 * @return プロパティエディタ
	 */
	public PropertyEditor getEditor();

	/**
	 * 表示ラベルを取得します。
	 * @return 表示時のラベル
	 */
	public String getDisplayLabel();

	/**
	 * 表示ラベルの多言語設定情報を取得します。
	 * @return 表示ラベルの多言語設定情報
	 */
	public List<LocalizedStringDefinition> getLocalizedDisplayLabelList();

}
