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
import org.iplass.mtp.entity.query.condition.predicate.Greater;
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
			//From-To検索
			Object val = null;
			if (obl.length > 0 && obl[0] != null) {
				val = (Object) obl[0];
			}
			if (val != null) {
				conditions.add(new LesserEqual(getPropertyName(), val));

				String parentName = "";
				if (getParent() != null && getEditor().getToPropertyName().indexOf(".") == -1) {
					parentName =  getParent()  + ".";
				}

				conditions.add(new Greater(parentName + getEditor().getToPropertyName(), val));
			}

		return conditions;
	}

	@Override
	public NumericRangePropertyEditor getEditor() {
		return (NumericRangePropertyEditor) super.getEditor();
	}

}
