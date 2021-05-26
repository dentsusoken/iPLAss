/*
 * Copyright (C) 2021 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.gem.command.generic.search.condition;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.query.condition.Condition;
import org.iplass.mtp.entity.query.condition.predicate.Greater;
import org.iplass.mtp.entity.query.condition.predicate.LesserEqual;
import org.iplass.mtp.view.generic.editor.NumericRangePropertyEditor;
import org.iplass.mtp.view.generic.editor.PropertyEditor;

public class NumericRangePropertySearchCondition extends PropertySearchCondition {

	public NumericRangePropertySearchCondition(PropertyDefinition definition,
			PropertyEditor editor, Object value) {
		super(definition, editor, value);
	}

	public NumericRangePropertySearchCondition(PropertyDefinition definition,
			PropertyEditor editor, Object value, String parent) {
		super(definition, editor, value, parent);
	}

	@Override
	public List<Condition> convertNormalCondition() {
		List<Condition> conditions = new ArrayList<Condition>();
		Object[] obl = (Object[]) getValue();
			//From-To検索
			Object val = null;
			if (obl.length > 0 && obl[0] != null) {
				if (obl[0] instanceof Double) {
					val = (Double) obl[0];
				} else if (obl[0] instanceof Long) {
					val = (Long) obl[0];
				} else if (obl[0] instanceof BigDecimal) {
					val = (BigDecimal) obl[0];
				}
			}
			if (val != null) {
				conditions.add(new LesserEqual(getPropertyName(), val));
				if (getPropertyName().indexOf(".") != -1) {
					String[] parentName = getPropertyName().split("\\.",0);
					conditions.add(new Greater(parentName[0] + "." + getEditor().getToPropertyName(), val));
				} else {
					conditions.add(new Greater(getEditor().getToPropertyName(), val));
				}

			}

		return conditions;
	}

	@Override
	public NumericRangePropertyEditor getEditor() {
		return (NumericRangePropertyEditor) super.getEditor();
	}

}
