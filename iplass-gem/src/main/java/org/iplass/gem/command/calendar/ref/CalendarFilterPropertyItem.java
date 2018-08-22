/*
 * Copyright (C) 2014 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.gem.command.calendar.ref;

import java.util.List;

import org.iplass.gem.command.GemResourceBundleUtil;
import org.iplass.mtp.definition.LocalizedStringDefinition;
import org.iplass.mtp.entity.SelectValue;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.PropertyDefinitionType;
import org.iplass.mtp.entity.definition.properties.SelectProperty;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.view.generic.editor.BooleanPropertyEditor;
import org.iplass.mtp.view.generic.editor.EditorValue;
import org.iplass.mtp.view.generic.editor.PropertyEditor;
import org.iplass.mtp.view.generic.editor.SelectPropertyEditor;
import org.iplass.mtp.web.template.TemplateUtil;

/**
 * カレンダーエンティティフィルタープロパティ情報
 * @author lis7zi
 */
public class CalendarFilterPropertyItem {

	/** プロパティ名 */
	private String propertyName;

	/** プロパティ表示名 */
	private String displayName;

	/** プロパティタイプ */
	private String propertyType;

	/** プロパティエディター */
	private PropertyEditor propertyEditor;

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getPropertyType() {
		return propertyType;
	}

	public void setPropertyType(String propertyType) {
		this.propertyType = propertyType;
	}

	public PropertyEditor getPropertyEditor() {
		return propertyEditor;
	}

	public void setPropertyEditor(PropertyEditor propertyEditor, PropertyDefinition pd) {
		if (pd.getType().equals(PropertyDefinitionType.SELECT)
				&& propertyEditor instanceof SelectPropertyEditor) {
			SelectPropertyEditor spe = (SelectPropertyEditor) propertyEditor;
			List<EditorValue> evList = spe.getValues();
			if (spe.getValues().isEmpty()) {
				SelectProperty sp = (SelectProperty) pd;
				String lang = TemplateUtil.getLanguage();
				for (SelectValue sv : sp.getSelectValueList()) {
					SelectValue lsv = sp.getLocalizedSelectValue(sv.getValue(), lang);
					EditorValue ev = new EditorValue();
					ev.setLabel(lsv.getDisplayName());
					ev.setValue(lsv.getValue());
					evList.add(ev);
				}
			}
		} else if (pd.getType().equals(PropertyDefinitionType.BOOLEAN)
					&&  propertyEditor instanceof BooleanPropertyEditor) {
			BooleanPropertyEditor bpe = (BooleanPropertyEditor) propertyEditor;
			if (StringUtil.isEmpty(bpe.getTrueLabel())) {
				bpe.setTrueLabel(TemplateUtil.getMultilingualString(
						resourceString("generic.editor.boolean.BooleanPropertyEditor_Condition.enable"),
						resourceList("generic.editor.boolean.BooleanPropertyEditor_Condition.enable")));
			}
			if (StringUtil.isEmpty(bpe.getFalseLabel())) {
				bpe.setFalseLabel(TemplateUtil.getMultilingualString(
						resourceString("generic.editor.boolean.BooleanPropertyEditor_Condition.invalid"),
						resourceList("generic.editor.boolean.BooleanPropertyEditor_Condition.invalid")));
			}
		}

		this.propertyEditor = propertyEditor;
	}

	private static String resourceString(String key, Object... arguments) {
		return GemResourceBundleUtil.resourceString(key, arguments);
	}

	private static List<LocalizedStringDefinition> resourceList(String key, Object... arguments) {
		return GemResourceBundleUtil.resourceList(key, arguments);
	}
}
