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

import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.view.generic.editor.PropertyEditor;
import org.iplass.mtp.view.generic.editor.UserPropertyEditor;

public class MetaUserPropertyEditor extends MetaCustomPropertyEditor {

	/** SerialVersionUID */
	private static final long serialVersionUID = -3636767642233881066L;

	public static MetaUserPropertyEditor createInstance(PropertyEditor editor) {
		return new MetaUserPropertyEditor();
	}

	@Override
	public void applyConfig(PropertyEditor _editor) {
		super.fillFrom(_editor);
	}

	@Override
	public PropertyEditor currentConfig(String propertyName) {
		UserPropertyEditor _editor = new UserPropertyEditor();
		super.fillTo(_editor);

		return _editor;
	}

	@Override
	public MetaUserPropertyEditor copy() {
		return ObjectUtil.deepCopy(this);
	}

}
