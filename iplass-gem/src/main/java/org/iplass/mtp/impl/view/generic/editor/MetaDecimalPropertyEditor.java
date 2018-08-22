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

import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.view.generic.editor.DecimalPropertyEditor;
import org.iplass.mtp.view.generic.editor.PropertyEditor;

/**
 * 固定小数点型プロパティエディタのメタデータ
 * @author lis3wg
 */
public class MetaDecimalPropertyEditor extends MetaNumberPropertyEditor {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 4763170157328079377L;

	public static MetaDecimalPropertyEditor createInstance(PropertyEditor editor) {
		return new MetaDecimalPropertyEditor();
	}

	@Override
	public void applyConfig(PropertyEditor editor) {
		super.fillFrom(editor);
	}

	@Override
	public PropertyEditor currentConfig(String propertyName) {
		DecimalPropertyEditor editor = new DecimalPropertyEditor();
		super.fillTo(editor);
		return editor;
	}

	@Override
	public MetaData copy() {
		return ObjectUtil.deepCopy(this);
	}

}
