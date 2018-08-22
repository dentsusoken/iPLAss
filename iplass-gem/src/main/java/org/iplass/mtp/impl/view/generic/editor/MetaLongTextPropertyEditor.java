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
import org.iplass.mtp.view.generic.editor.LongTextPropertyEditor;
import org.iplass.mtp.view.generic.editor.PropertyEditor;

/**
 * 文字列型プロパティエディタのメタデータ
 * @author lis3wg
 */
public class MetaLongTextPropertyEditor extends MetaStringPropertyEditor {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 2472181501963106690L;

	public static MetaLongTextPropertyEditor createInstance(PropertyEditor editor) {
		return new MetaLongTextPropertyEditor();
	}

	@Override
	public void applyConfig(PropertyEditor editor) {
		super.fillFrom(editor);
	}

	@Override
	public PropertyEditor currentConfig(String propertyName) {
		LongTextPropertyEditor editor = new LongTextPropertyEditor();
		super.fillTo(editor);

		return editor;
	}

	@Override
	public MetaLongTextPropertyEditor copy() {
		return ObjectUtil.deepCopy(this);
	}

}
