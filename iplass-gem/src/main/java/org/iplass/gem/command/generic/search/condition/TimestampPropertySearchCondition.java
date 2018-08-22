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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.text.StrTokenizer;
import org.iplass.gem.command.Constants;
import org.iplass.gem.command.generic.search.SearchConditionDetail;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.query.condition.Condition;
import org.iplass.mtp.entity.query.condition.predicate.Between;
import org.iplass.mtp.entity.query.condition.predicate.GreaterEqual;
import org.iplass.mtp.entity.query.condition.predicate.LesserEqual;
import org.iplass.mtp.impl.util.ConvertUtil;
import org.iplass.mtp.util.DateUtil;
import org.iplass.mtp.view.generic.editor.DateTimePropertyEditor.TimeDispRange;
import org.iplass.mtp.view.generic.editor.PropertyEditor;
import org.iplass.mtp.view.generic.editor.TimestampPropertyEditor;
import org.iplass.mtp.web.template.TemplateUtil;

public class TimestampPropertySearchCondition extends PropertySearchCondition {

	public TimestampPropertySearchCondition(PropertyDefinition definition,
			PropertyEditor editor, Object value) {
		super(definition, editor, value);
	}

	public TimestampPropertySearchCondition(PropertyDefinition definition,
			PropertyEditor editor, Object value, String parent) {
		super(definition, editor, value, parent);
	}

	@Override
	public List<Condition> convertNormalCondition() {
		List<Condition> conditions = new ArrayList<Condition>();
		Timestamp[] t = (Timestamp[]) getValue();
		TimestampPropertyEditor editor = getEditor();
		if (editor.isSingleDayCondition()) {
			if (t.length > 0 && t[0] != null) {
//				Calendar base = Calendar.getInstance();
				Calendar base = DateUtil.getCalendar(true);
				base.setTime(t[0]);
//				Calendar from = Calendar.getInstance();
				Calendar from = DateUtil.getCalendar(true);
				from.setTime(base.getTime());
//				Calendar to = Calendar.getInstance();
				Calendar to = DateUtil.getCalendar(true);
				to.setTime(base.getTime());
				//ミリ秒は固定
				from.set(Calendar.MILLISECOND, 0);
				to.set(Calendar.MILLISECOND, 999);
				if (!TimeDispRange.isDispSec(editor.getDispRange())) {
					//秒が非表示
					from.set(Calendar.SECOND, 0);
					to.set(Calendar.SECOND, 59);
				}
				if (!TimeDispRange.isDispMin(editor.getDispRange())) {
					//分が非表示
					from.set(Calendar.MINUTE, 0);
					to.set(Calendar.MINUTE, 59);
				}
				if (!TimeDispRange.isDispHour(editor.getDispRange())) {
					//時が非表示
					from.set(Calendar.HOUR, 0);
					to.set(Calendar.HOUR, 23);
				}

				conditions.add(new GreaterEqual(getPropertyName(), new Timestamp(from.getTimeInMillis())));
				conditions.add(new LesserEqual(getPropertyName(), new Timestamp(to.getTimeInMillis())));
			}
		} else {
			Timestamp from = null;
			Timestamp to = null;
			if (t.length > 0 && t[0] != null) {
				from = t[0];
			}
			if (t.length > 1 && t[1] != null) {
				to = t[1];
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
			Timestamp[] ret = new Timestamp[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = string2Timestamp(values[i]);
			}
			return ret;
		} else {
			return string2Timestamp(detail.getValue());
		}
	}

	private Timestamp string2Timestamp(String str) {

		if (str.length() == 8) {
			return ConvertUtil.convertToDate(Timestamp.class, str, TemplateUtil.getLocaleFormat().getServerDateFormat(), true);
		} else if (str.length() == 14) {
			return ConvertUtil.convertToDate(Timestamp.class, str, TemplateUtil.getLocaleFormat().getServerDateFormat() + "HHmmss", true);
		} else {
			return ConvertUtil.convertToDate(Timestamp.class, str, TemplateUtil.getLocaleFormat().getServerDateTimeFormat(), true);
		}
	}

	@Override
	public TimestampPropertyEditor getEditor() {
		return (TimestampPropertyEditor) super.getEditor();
	}
}
