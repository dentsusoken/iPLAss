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

import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.view.generic.editor.AutoNumberPropertyEditor;
import org.iplass.mtp.view.generic.editor.PropertyEditor;

/**
 * AutoNumber型プロパティエディタのメタデータ
 * @author Y.Kazama
 */
public class MetaAutoNumberPropertyEditor extends MetaPrimitivePropertyEditor {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 3288271112712679281L;

	/** 検索条件完全一致設定有無 */
	private boolean searchExactMatchCondition = true;

	public boolean isSearchExactMatchCondition() {
		return searchExactMatchCondition;
	}

	public void setSearchExactMatchCondition(boolean searchExactMatchCondition) {
		this.searchExactMatchCondition = searchExactMatchCondition;
	}

	public static MetaAutoNumberPropertyEditor createInstance(PropertyEditor editor) {
		return new MetaAutoNumberPropertyEditor();
	}

	@Override
	public MetaAutoNumberPropertyEditor copy() {
		return ObjectUtil.deepCopy(this);
	}

	@Override
	public void applyConfig(PropertyEditor editor) {
		super.fillFrom(editor);

		AutoNumberPropertyEditor e = (AutoNumberPropertyEditor) editor;

		searchExactMatchCondition = e.isSearchExactMatchCondition();
	}

	@Override
	public PropertyEditor currentConfig(String propertyName) {
		AutoNumberPropertyEditor editor = new AutoNumberPropertyEditor();
		super.fillTo(editor);

		editor.setSearchExactMatchCondition(searchExactMatchCondition);
		return editor;
	}

}
