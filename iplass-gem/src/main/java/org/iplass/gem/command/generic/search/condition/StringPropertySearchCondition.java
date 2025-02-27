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

import java.util.ArrayList;
import java.util.List;

import org.iplass.gem.command.Constants;
import org.iplass.gem.command.generic.search.SearchConditionDetail;
import org.iplass.mtp.auth.User;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.SubQuery;
import org.iplass.mtp.entity.query.condition.Condition;
import org.iplass.mtp.entity.query.condition.expr.And;
import org.iplass.mtp.entity.query.condition.predicate.Between;
import org.iplass.mtp.entity.query.condition.predicate.Equals;
import org.iplass.mtp.entity.query.condition.predicate.GreaterEqual;
import org.iplass.mtp.entity.query.condition.predicate.In;
import org.iplass.mtp.entity.query.condition.predicate.IsNull;
import org.iplass.mtp.entity.query.condition.predicate.LesserEqual;
import org.iplass.mtp.entity.query.condition.predicate.Like;
import org.iplass.mtp.view.generic.editor.PropertyEditor;
import org.iplass.mtp.view.generic.editor.StringPropertyEditor;
import org.iplass.mtp.view.generic.editor.StringPropertyEditor.StringDisplayType;
import org.iplass.mtp.view.generic.editor.UserPropertyEditor;

public class StringPropertySearchCondition extends PropertySearchCondition {

	public StringPropertySearchCondition(PropertyDefinition definition,
			PropertyEditor editor, Object value) {
		super(definition, editor, value);
	}

	public StringPropertySearchCondition(PropertyDefinition definition,
			PropertyEditor editor, Object value, String parent) {
		super(definition, editor, value, parent);
	}

	@Override
	public List<Condition> convertNormalCondition() {
		List<Condition> conditions = new ArrayList<>();
		PropertyEditor editor = getEditor();
		//like検索
		if (editor instanceof StringPropertyEditor) {
			StringPropertyEditor sp = (StringPropertyEditor) editor;
			if ((sp.getDisplayType() != null && sp.getDisplayType() == StringDisplayType.SELECT)
					|| sp.isSearchExactMatchCondition()) {
				// 「値なし」を検索条件の選択肢に追加するか
				if (sp.isIsNullSearchEnabled() && Constants.ISNULL_VALUE.equals(getValue())) {
					conditions.add(new IsNull(getPropertyName()));
				} else {
					//選択時または完全一致有無にチェック時はEquals
					conditions.add(new Equals(getPropertyName(), getValue()));
				}
			} else {
				String[] values = (String[]) getValue();
				if (sp.isSearchInRange()) {
					//範囲検索
					// From-To検索
					// 2番目、3番目を利用
					String from = null;
					String to = null;
					if (values.length > 1 && values[1] != null) {
						from = values[1];
					}
					if (values.length > 2 && values[2] != null) {
						to = values[2];
					}
					if (from != null && !("").equals(from) && to != null && !("").equals(to)) {
						conditions.add(new Between(getPropertyName(), from, to));
					} else if ((from != null && !("").equals(from)) && (to == null || ("").equals(to))) {
						conditions.add(new GreaterEqual(getPropertyName(), from));
					} else if ((from == null || ("").equals(from)) && (to != null && !("").equals(to))) {
						conditions.add(new LesserEqual(getPropertyName(), to));
					}
				} else {
					// like検索
					// 1番目を利用
					//conditions.add(new Like(getPropertyName(), "%" + StringUtil.escapeEqlForLike(getValue().toString()) + "%"));
					if (values.length > 0 && values[0] != null) {
						String strValue = values[0];
						//複数の場合は部分一致
						conditions.add(new Like(getPropertyName(), strValue.toString(), Like.MatchPattern.PARTIAL));
					}
				}
			}
		} else if (editor instanceof UserPropertyEditor) {
			//入力された内容(名前)でoid取得
			//Like like = new Like(Entity.NAME, "%" + StringUtil.escapeEqlForLike(getValue().toString()) + "%");
			Like like = new Like(Entity.NAME, getValue().toString(), Like.MatchPattern.PARTIAL);
			conditions.add(new In(getPropertyName(), new SubQuery(
					new Query().select(Entity.OID).from(User.DEFINITION_NAME).where(like))));
		} else {
			//Template等のケース、likeで検索
			//conditions.add(new Like(getPropertyName(), "%" + StringUtil.escapeEqlForLike(getValue().toString()) + "%"));
			conditions.add(new Like(getPropertyName(), getValue().toString(), Like.MatchPattern.PARTIAL));
		}
		return conditions;
	}

	@Override
	public List<Condition> convertDetailCondition() {
		PropertyEditor editor = getEditor();
		if (editor instanceof UserPropertyEditor) {
			//画面で指定された方法でユーザー検索
			List<Condition> conditions = new ArrayList<>();

			//propertyName入れ替えて条件作成
			SearchConditionDetail detail = (SearchConditionDetail) getValue();
			String _propName = detail.getPropertyName();
			detail.setPropertyName(Entity.NAME);
			List<Condition> userSearchCondition = super.convertDetailCondition();
			detail.setPropertyName(_propName);

			conditions.add(new In(getPropertyName(), new SubQuery(
					new Query().select(Entity.OID).from(User.DEFINITION_NAME).where(new And(userSearchCondition)))));

			return conditions;
		}
		return super.convertDetailCondition();
	}
}
