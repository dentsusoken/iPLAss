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

import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.query.condition.Condition;
import org.iplass.mtp.entity.query.condition.expr.And;
import org.iplass.mtp.entity.query.condition.expr.Or;
import org.iplass.mtp.entity.query.condition.predicate.Greater;
import org.iplass.mtp.entity.query.condition.predicate.GreaterEqual;
import org.iplass.mtp.entity.query.condition.predicate.IsNull;
import org.iplass.mtp.entity.query.condition.predicate.Lesser;
import org.iplass.mtp.entity.query.condition.predicate.LesserEqual;
import org.iplass.mtp.view.generic.editor.NumericRangePropertyEditor;
import org.iplass.mtp.view.generic.editor.PropertyEditor;

/**
 * 数値範囲型の検索条件
 * @author ICOM Shojima
 */

public class NumericRangePropertySearchCondition extends PropertySearchCondition {

	/**
	 * コンストラクタ
	 * @param definition プロパティ定義
	 * @param editor プロパティエディタ
	 * @param value 検索用の値
	 */
	public NumericRangePropertySearchCondition(PropertyDefinition definition,
			PropertyEditor editor, Object value) {
		super(definition, editor, value);
	}

	/**
	 * コンストラクタ
	 * @param definition プロパティ定義
	 * @param editor プロパティエディタ
	 * @param value 検索用の値
	 * @param parent 親ノード
	 */
	public NumericRangePropertySearchCondition(PropertyDefinition definition,
			PropertyEditor editor, Object value, String parent) {
		super(definition, editor, value, parent);
	}

	/**
	 * 数値範囲型の検索条件設定
	 * @return conditions 検索条件
	 */
	@Override
	public List<Condition> convertNormalCondition() {
		List<Condition> conditions = new ArrayList<Condition>();
		Object[] obl = (Object[]) getValue();

		boolean inputNullFrom = getEditor().isInputNullFrom();
		boolean inputNullTo = getEditor().isInputNullTo();
		boolean equivalentInput = getEditor().isEquivalentInput();

		if (obl.length > 0 && obl[0] != null) {
			Object val = obl[0];

			String parentName = "";
			if (getParent() != null && getEditor().getToPropertyName().indexOf(".") == -1) {
				parentName =  getParent()  + ".";
			}
			String fromPropertyName = getPropertyName();
			String toPropertyName = parentName + getEditor().getToPropertyName();

			Or or = new Or();
			or.addExpression(new And(fromCondition(fromPropertyName, val), toCondition(toPropertyName, val)));

			if (inputNullFrom) {
				or.addExpression(new And(new IsNull(fromPropertyName), toCondition(toPropertyName, val)));
			}

			if (inputNullTo) {
				or.addExpression(new And(fromCondition(fromPropertyName, val), new IsNull(toPropertyName)));
			}

			if (equivalentInput) {
				or.addExpression(new And().eq(fromPropertyName, val).eq(toPropertyName, val));
			}
			conditions.add(or);
		}

		return conditions;
	}

	/**
	 * Fromの比較条件を返します。
	 *
	 * @param propertyName プロパティ名
	 * @param value 値
	 * @return Fromの比較条件
	 */
	private Condition fromCondition(String propertyName, Object value) {
		if (getEditor().isFromConditionAsLesserEqual()) {
			return new LesserEqual(propertyName, value);
		} else {
			return new Lesser(propertyName, value);
		}
	}

	/**
	 * Toの比較条件を返します。
	 *
	 * @param propertyName プロパティ名
	 * @param value 値
	 * @return Toの比較条件
	 */
	private Condition toCondition(String propertyName, Object value) {
		if (getEditor().isToConditionAsGreaterEqual()) {
			return new GreaterEqual(propertyName, value);
		} else {
			return new Greater(propertyName, value);
		}
	}

	@Override
	public NumericRangePropertyEditor getEditor() {
		return (NumericRangePropertyEditor) super.getEditor();
	}

}
