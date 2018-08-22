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

import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.view.generic.editor.EditorValue;
import org.iplass.mtp.view.generic.editor.PropertyEditor;
import org.iplass.mtp.view.generic.editor.SelectPropertyEditor;
import org.iplass.mtp.view.generic.editor.SelectPropertyEditor.SelectDisplayType;

/**
 * 選択型プロパティエディタのメタデータ
 * @author lis3wg
 */
public class MetaSelectPropertyEditor extends MetaPrimitivePropertyEditor {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 5771547352037643009L;

	public static MetaSelectPropertyEditor createInstance(PropertyEditor editor) {
		return new MetaSelectPropertyEditor();
	}

	/** 表示タイプ */
	private SelectDisplayType displayType;

	/** セレクトボックスの値 */
	private List<EditorValue> values;

	/** CSV出力時に選択肢順でソート */
	private boolean sortCsvOutputValue;

	/**
	 * 表示タイプを取得します。
	 * @return 表示タイプ
	 */
	public SelectDisplayType getDisplayType() {
		return displayType;
	}

	/**
	 * 表示タイプを設定します。
	 * @param displayType 表示タイプ
	 */
	public void setDisplayType(SelectDisplayType displayType) {
		this.displayType = displayType;
	}

	/**
	 * セレクトボックスの値を取得します。
	 * @return セレクトボックスの値
	 */
	public List<EditorValue> getValues() {
		if (values == null) values = new ArrayList<EditorValue>();
		return values;
	}

	/**
	 * セレクトボックスの値を設定します。
	 * @param values セレクトボックスの値
	 */
	public void setValues(List<EditorValue> values) {
		this.values = values;
	}

	/**
	 * CSV出力時に選択肢順でソートするかを取得します。
	 *
	 * @return true:CSV出力時に選択肢順でソート
	 */
	public boolean isSortCsvOutputValue() {
		return sortCsvOutputValue;
	}

	/**
	 * CSV出力時に選択肢順でソートするかを設定します。
	 *
	 * @param sortCsvOutputValue true:CSV出力時に選択肢順でソート
	 */
	public void setSortCsvOutputValue(boolean sortCsvOutputValue) {
		this.sortCsvOutputValue = sortCsvOutputValue;
	}

	@Override
	public MetaSelectPropertyEditor copy() {
		return ObjectUtil.deepCopy(this);
	}

	@Override
	public void applyConfig(PropertyEditor editor) {
		super.fillFrom(editor);

		SelectPropertyEditor pe = (SelectPropertyEditor) editor;
		displayType = pe.getDisplayType();
		values = pe.getValues();
		sortCsvOutputValue = pe.isSortCsvOutputValue();
	}

	@Override
	public PropertyEditor currentConfig(String propertyName) {
		SelectPropertyEditor editor = new SelectPropertyEditor();
		super.fillTo(editor);

		editor.setDisplayType(displayType);
		editor.setValues(values);
		editor.setSortCsvOutputValue(sortCsvOutputValue);
		return editor;
	}
}
