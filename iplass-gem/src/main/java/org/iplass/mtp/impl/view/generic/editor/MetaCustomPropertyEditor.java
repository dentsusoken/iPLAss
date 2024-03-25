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

package org.iplass.mtp.impl.view.generic.editor;

import jakarta.xml.bind.annotation.XmlSeeAlso;

import org.iplass.mtp.view.generic.editor.DateRangePropertyEditor;
import org.iplass.mtp.view.generic.editor.JoinPropertyEditor;
import org.iplass.mtp.view.generic.editor.NumericRangePropertyEditor;
import org.iplass.mtp.view.generic.editor.PropertyEditor;
import org.iplass.mtp.view.generic.editor.TemplatePropertyEditor;
import org.iplass.mtp.view.generic.editor.UserPropertyEditor;

@XmlSeeAlso( {MetaDateRangePropertyEditor.class, MetaNumericRangePropertyEditor.class, MetaJoinPropertyEditor.class, MetaTemplatePropertyEditor.class, MetaUserPropertyEditor.class})
public abstract class MetaCustomPropertyEditor extends MetaPropertyEditor {

	/** SerialVersionUID */
	private static final long serialVersionUID = -8800362701211983169L;

	public static MetaCustomPropertyEditor createInstance(PropertyEditor editor) {
		if (editor instanceof DateRangePropertyEditor) {
			return MetaDateRangePropertyEditor.createInstance(editor);
		} else if (editor instanceof JoinPropertyEditor) {
			return MetaJoinPropertyEditor.createInstance(editor);
		} else if (editor instanceof TemplatePropertyEditor) {
			return MetaTemplatePropertyEditor.createInstance(editor);
		} else if (editor instanceof UserPropertyEditor) {
			return MetaUserPropertyEditor.createInstance(editor);
		} else if (editor instanceof NumericRangePropertyEditor) {
			return MetaNumericRangePropertyEditor.createInstance(editor);
		}
		return null;
	}

}
