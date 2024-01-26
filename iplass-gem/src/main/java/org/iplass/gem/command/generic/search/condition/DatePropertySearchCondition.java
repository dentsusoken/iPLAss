/*
 * Copyright (C) 2012 DENTSU SOKEN INC. All Rights Reserved.
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

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.text.StrTokenizer;
import org.iplass.gem.command.CommandUtil;
import org.iplass.gem.command.Constants;
import org.iplass.gem.command.generic.search.SearchConditionDetail;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.query.condition.Condition;
import org.iplass.mtp.entity.query.condition.predicate.Between;
import org.iplass.mtp.entity.query.condition.predicate.Equals;
import org.iplass.mtp.entity.query.condition.predicate.GreaterEqual;
import org.iplass.mtp.entity.query.condition.predicate.LesserEqual;
import org.iplass.mtp.view.generic.editor.DatePropertyEditor;
import org.iplass.mtp.view.generic.editor.PropertyEditor;

public class DatePropertySearchCondition extends PropertySearchCondition {

	public DatePropertySearchCondition(PropertyDefinition definition,
			PropertyEditor editor, Object value) {
		super(definition, editor, value);
	}

	public DatePropertySearchCondition(PropertyDefinition definition,
			PropertyEditor editor, Object value, String parent) {
		super(definition, editor, value, parent);
	}

	@Override
	public List<Condition> convertNormalCondition() {
		List<Condition> conditions = new ArrayList<Condition>();
		Date[] d = (Date[]) getValue();
		if (getEditor().isSingleDayCondition()) {
			if (d.length > 0 && d[0] != null) {
				conditions.add(new Equals(getPropertyName(), d[0]));
			}
		} else {
			//From-To検索
			Date from = null;
			Date to = null;
			if (d.length > 0 && d[0] != null) {
				from = d[0];
			}
			if (d.length > 1 && d[1] != null) {
				to = d[1];
			}
			if (from != null && to != null) {
				conditions.add(new Between(getPropertyName(), from, to));
			} else if (from != null && to == null) {
				conditions.add(new GreaterEqual(getPropertyName(), from));
			} else if (from == null && to != null) {
				conditions.add(new LesserEqual(getPropertyName(), to));
			}
		}
		return conditions;
	}

	@Override
	protected Object convertDetailValue(SearchConditionDetail detail) {
		if (Constants.NOTNULL.equals(detail.getPredicate())
				|| Constants.NULL.equals(detail.getPredicate())) {
			return null;
		} else if (Constants.IN.equals(detail.getPredicate())) {
			StrTokenizer st = StrTokenizer.getCSVInstance(detail.getValue());
			String[] values = st.getTokenArray();
			Date[] ret = new Date[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = CommandUtil.getDate(values[i]);
			}
			return ret;
		} else {
			return CommandUtil.getDate(detail.getValue());
		}
	}

	@Override
	public DatePropertyEditor getEditor() {
		return (DatePropertyEditor) super.getEditor();
	}
}
