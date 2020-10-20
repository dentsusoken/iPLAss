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

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.entity.SelectValue;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.PropertyDefinitionType;
import org.iplass.mtp.entity.definition.properties.ExpressionProperty;
import org.iplass.mtp.entity.query.condition.Condition;
import org.iplass.mtp.entity.query.condition.predicate.Equals;
import org.iplass.mtp.impl.util.ConvertUtil;
import org.iplass.mtp.view.generic.editor.BooleanPropertyEditor;
import org.iplass.mtp.view.generic.editor.DatePropertyEditor;
import org.iplass.mtp.view.generic.editor.DecimalPropertyEditor;
import org.iplass.mtp.view.generic.editor.ExpressionPropertyEditor;
import org.iplass.mtp.view.generic.editor.FloatPropertyEditor;
import org.iplass.mtp.view.generic.editor.IntegerPropertyEditor;
import org.iplass.mtp.view.generic.editor.PropertyEditor;
import org.iplass.mtp.view.generic.editor.SelectPropertyEditor;
import org.iplass.mtp.view.generic.editor.SelectPropertyEditor.SelectDisplayType;
import org.iplass.mtp.view.generic.editor.StringPropertyEditor;
import org.iplass.mtp.view.generic.editor.TimePropertyEditor;
import org.iplass.mtp.view.generic.editor.TimestampPropertyEditor;
import org.iplass.mtp.web.template.TemplateUtil;

public class ExpressionPropertySearchCondition extends PropertySearchCondition {

	private String parent;

	public ExpressionPropertySearchCondition(PropertyDefinition definition,
			PropertyEditor editor, Object value) {
		super(definition, editor, value);
	}

	public ExpressionPropertySearchCondition(PropertyDefinition definition,
			PropertyEditor editor, Object value, String parent) {
		super(definition, editor, value, parent);
		this.parent = parent;
	}

	@Override
	public List<Condition> convertNormalCondition() {
		ExpressionProperty ep = (ExpressionProperty) getDefinition();
		PropertyEditor editor = ((ExpressionPropertyEditor) getEditor()).getEditor();
		ExpressionSearchConditionType type = ExpressionSearchConditionType.createSearchCondition(ep, editor);

		if (type == ExpressionSearchConditionType.EXPRESSION) {
			//ただのテキストからの入力
			List<Condition> ret = new ArrayList<>();
			String val = null;
			if (getValue() instanceof String[]) {
				val = ((String[]) getValue())[0];
			} else if (getValue() instanceof String) {
				val = (String) getValue();
			}
			if (val != null && !val.isEmpty()) {
				ret.add(new Equals(getPropertyName(), val));
			}
			return ret;
		} else {
			//型にあわせた条件作成
			Object value = getNormalTypeValue(type);
			PropertySearchCondition searchCondition = createSearchCondition(type, value);
			if (searchCondition != null) {
				return searchCondition.convertNormalCondition();
			} else {
				//値がない場合は条件をつくらない
				return new ArrayList<>();
			}
		}
	}

	@Override
	public List<Condition> convertDetailCondition() {
		ExpressionProperty ep = (ExpressionProperty) getDefinition();
		PropertyEditor editor = ((ExpressionPropertyEditor) getEditor()).getEditor();
		ExpressionSearchConditionType type = ExpressionSearchConditionType.createSearchCondition(ep, editor);

		if (type != ExpressionSearchConditionType.EXPRESSION) {
			PropertySearchCondition searchCondition = createSearchCondition(type, getValue());
			if (searchCondition != null) {
				return searchCondition.convertDetailCondition();
			}
		}
		return super.convertDetailCondition();
	}

	private PropertySearchCondition createSearchCondition(ExpressionSearchConditionType type, Object value) {
		if (value == null) return null;

		PropertyEditor editor = ((ExpressionPropertyEditor) getEditor()).getEditor();
		//SelectProperty以外DefinitionをcastしてないのでとりあえずExpressionを渡しても問題はないはず
		if (type == ExpressionSearchConditionType.BOOLEAN) {
			return new BooleanPropertySearchCondition(getDefinition(), editor, value, parent);
		} else if (type == ExpressionSearchConditionType.DATE) {
			return new DatePropertySearchCondition(getDefinition(), editor, value, parent);
		} else if (type == ExpressionSearchConditionType.DATETIME) {
			return new TimestampPropertySearchCondition(getDefinition(), editor, value, parent);
		} else if (type == ExpressionSearchConditionType.DECIMAL) {
			return new DecimalPropertySearchCondition(getDefinition(), editor, value, parent);
		} else if (type == ExpressionSearchConditionType.FLOAT) {
			return new FloatPropertySearchCondition(getDefinition(), editor, value, parent);
		} else if (type == ExpressionSearchConditionType.INTEGER) {
			return new IntegerPropertySearchCondition(getDefinition(), editor, value, parent);
		} else if (type == ExpressionSearchConditionType.SELECT) {
			return new SelectPropertySearchCondition(getDefinition(), editor, value, parent);
		} else if (type == ExpressionSearchConditionType.STRING) {
			return new StringPropertySearchCondition(getDefinition(), editor, value, parent);
		} else if (type == ExpressionSearchConditionType.TIME) {
			return new TimePropertySearchCondition(getDefinition(), editor, value, parent);
		}
		return null;
	}

	private Object getNormalTypeValue(ExpressionSearchConditionType type) {
		PropertyEditor editor = ((ExpressionPropertyEditor) getEditor()).getEditor();
		if (type == ExpressionSearchConditionType.BOOLEAN) {
			String value = (String) getValue();
			if (value == null || value.trim().length() == 0) return null;
			return Boolean.parseBoolean((String) getValue());
		} else if (type == ExpressionSearchConditionType.DATE) {
			String[] value = (String[]) getValue();
			//2番目(From)、3番目(To)を利用
			Date from = null;
			Date to = null;
			if (value.length > 1 && value[1] != null) {
				from = ConvertUtil.convertToDate(Date.class, value[1], TemplateUtil.getLocaleFormat().getServerDateFormat(), false);
			}
			if (value.length > 2 && value[2] != null) {
				to = ConvertUtil.convertToDate(Date.class, value[2], TemplateUtil.getLocaleFormat().getServerDateFormat(), false);
			}
			return (from == null && to == null) ? null : new Date[]{from, to};
		} else if (type == ExpressionSearchConditionType.DATETIME) {
			String[] value = (String[]) getValue();
			//2番目(From)、3番目(To)を利用
			Timestamp from = null;
			Timestamp to = null;
			if (value.length > 1 && value[1] != null) {
				from = ConvertUtil.convertToDate(Timestamp.class, value[1], TemplateUtil.getLocaleFormat().getServerDateTimeFormat(), false);
			}
			if (value.length > 2 && value[2] != null) {
				to = ConvertUtil.convertToDate(Timestamp.class, value[2], TemplateUtil.getLocaleFormat().getServerDateTimeFormat(), false);
			}
			return (from == null && to == null) ? null : new Timestamp[]{from, to};
		} else if (type == ExpressionSearchConditionType.DECIMAL) {
			String[] value = (String[]) getValue();
			BigDecimal from = null;
			BigDecimal to = null;
			if (value.length > 0 && value[0] != null) {
				from = ConvertUtil.convertFromString(BigDecimal.class, value[0]);
			}
			if (value.length > 1 && value[1] != null) {
				to = ConvertUtil.convertFromString(BigDecimal.class, value[1]);
			}
			return (from == null && to == null) ? null : new BigDecimal[]{from, to};
		} else if (type == ExpressionSearchConditionType.FLOAT) {
			String[] value = (String[]) getValue();
			Double from = null;
			Double to = null;
			if (value.length > 0 && value[0] != null) {
				from = ConvertUtil.convertFromString(Double.class, value[0]);
			}
			if (value.length > 1 && value[1] != null) {
				to = ConvertUtil.convertFromString(Double.class, value[1]);
			}
			return (from == null && to == null) ? null : new Double[]{from, to};
		} else if (type == ExpressionSearchConditionType.INTEGER) {
			String[] value = (String[]) getValue();
			Long from = null;
			Long to = null;
			if (value.length > 0 && value[0] != null) {
				from = ConvertUtil.convertFromString(Long.class, value[0]);
			}
			if (value.length > 1 && value[1] != null) {
				to = ConvertUtil.convertFromString(Long.class, value[1]);
			}
			return (from == null && to == null) ? null : new Long[]{from, to};
		} else if (type == ExpressionSearchConditionType.SELECT) {
			String[] value = (String[]) getValue();
			if (value == null || value.length == 0) return null;
			SelectPropertyEditor se = (SelectPropertyEditor) editor;
			if (se.getDisplayType() == SelectDisplayType.CHECKBOX
					|| se.getDisplayType() == SelectDisplayType.HIDDEN) {
				List<SelectValue> list = new ArrayList<>();
				for (String val : value) {
					if (val != null && !val.isEmpty()) {
						list.add(new SelectValue(val));
					}
				}
				return list.toArray(new SelectValue[list.size()]);
			} else {
				if (value[0] != null && !value[0].isEmpty()) {
					return new SelectValue(value[0]);
				}
			}
		} else if (type == ExpressionSearchConditionType.STRING) {
			String value = (String) getValue();
			if (value == null || value.trim().length() == 0) return null;
			return value;
		} else if (type == ExpressionSearchConditionType.TIME) {
			String[] value = (String[]) getValue();
			//2番目(From)、3番目(To)を利用
			Time from = null;
			Time to = null;
			if (value.length > 1 && value[1] != null) {
				from = ConvertUtil.convertToDate(Time.class, value[1], TemplateUtil.getLocaleFormat().getServerTimeFormat(), false);
			}
			if (value.length > 2 && value[2] != null) {
				to = ConvertUtil.convertToDate(Time.class, value[2], TemplateUtil.getLocaleFormat().getServerTimeFormat(), false);
			}
			return (from == null && to == null) ? null : new Time[]{from, to};
		}
		return null;
	}

	enum ExpressionSearchConditionType{
		BOOLEAN,
		DATE,
		DATETIME,
		DECIMAL,
		EXPRESSION,
		FLOAT,
		INTEGER,
		SELECT,
		STRING,
		TIME;

		public static ExpressionSearchConditionType createSearchCondition(
				ExpressionProperty ep, PropertyEditor editor) {
			if (ep.getResultType() == PropertyDefinitionType.BOOLEAN
					&& editor instanceof BooleanPropertyEditor) {
				return BOOLEAN;
			} else if (ep.getResultType() == PropertyDefinitionType.DATE
					&& editor instanceof DatePropertyEditor) {
				return DATE;
			} else if (ep.getResultType() == PropertyDefinitionType.DATETIME
					&& editor instanceof TimestampPropertyEditor) {
				return DATETIME;
			} else if (ep.getResultType() == PropertyDefinitionType.DECIMAL
					&& editor instanceof DecimalPropertyEditor) {
				return DECIMAL;
			} else if (ep.getResultType() == PropertyDefinitionType.FLOAT
					&& editor instanceof FloatPropertyEditor) {
				return FLOAT;
			} else if (ep.getResultType() == PropertyDefinitionType.INTEGER
					&& editor instanceof IntegerPropertyEditor) {
				return INTEGER;
			} else if (ep.getResultType() == PropertyDefinitionType.SELECT
					&& editor instanceof SelectPropertyEditor) {
				return SELECT;
			} else if (ep.getResultType() == PropertyDefinitionType.STRING
					&& editor instanceof StringPropertyEditor) {
				return STRING;
			} else if (ep.getResultType() == PropertyDefinitionType.TIME
					&& editor instanceof TimePropertyEditor) {
				return TIME;
			}
			return EXPRESSION;
		}

	}
}
