/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.query.condition.Condition;
import org.iplass.mtp.entity.query.condition.predicate.Between;
import org.iplass.mtp.entity.query.condition.predicate.Equals;
import org.iplass.mtp.entity.query.condition.predicate.GreaterEqual;
import org.iplass.mtp.entity.query.condition.predicate.LesserEqual;
import org.iplass.mtp.view.generic.editor.FloatPropertyEditor;
import org.iplass.mtp.view.generic.editor.PropertyEditor;

public class FloatPropertySearchCondition extends PropertySearchCondition {

	public FloatPropertySearchCondition(PropertyDefinition definition,
			PropertyEditor editor, Object value) {
		super(definition, editor, value);
	}

	public FloatPropertySearchCondition(PropertyDefinition definition,
			PropertyEditor editor, Object value, String parent) {
		super(definition, editor, value, parent);
	}

	@Override
	public List<Condition> convertNormalCondition() {
		List<Condition> conditions = new ArrayList<Condition>();
		Double[] dbl = (Double[]) getValue();
		if (getEditor().isSearchInRange()) {
			//From-To検索
			Double from = null;
			Double to = null;
			if (dbl.length > 0 && dbl[0] != null) {
				from = dbl[0];
			}
			if (dbl.length > 1 && dbl[1] != null) {
				to = dbl[1];
			}
			if (from != null && to != null) {
				conditions.add(new Between(getPropertyName(), from, to));
			} else if (from != null && to == null) {
				conditions.add(new GreaterEqual(getPropertyName(), from));
			} else if (from == null && to != null) {
				conditions.add(new LesserEqual(getPropertyName(), to));
			}
		} else {
			if (dbl.length > 0 && dbl[0] != null) {
				conditions.add(new Equals(getPropertyName(), dbl[0]));
			}
		}

		return conditions;
	}

	@Override
	public FloatPropertyEditor getEditor() {
		return (FloatPropertyEditor) super.getEditor();
	}
}
