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
import org.iplass.mtp.entity.query.condition.predicate.Like;
import org.iplass.mtp.view.generic.editor.LongTextPropertyEditor;
import org.iplass.mtp.view.generic.editor.PropertyEditor;

public class LongTextPropertySearchCondition extends PropertySearchCondition {

	public LongTextPropertySearchCondition(PropertyDefinition definition,
			PropertyEditor editor, Object value) {
		super(definition, editor, value);
	}

	public LongTextPropertySearchCondition(PropertyDefinition definition,
			PropertyEditor editor, Object value, String parent) {
		super(definition, editor, value, parent);
	}

	@Override
	public List<Condition> convertNormalCondition() {
		List<Condition> conditions = new ArrayList<Condition>();
		PropertyEditor editor = getEditor();
		//like検索
		if (editor instanceof LongTextPropertyEditor) {
			//like検索
			//conditions.add(new Like(getPropertyName(), "%" + StringUtil.escapeEqlForLike(getValue().toString()) + "%"));
			conditions.add(new Like(getPropertyName(), getValue().toString(), Like.MatchPattern.PARTIAL));
		} else {
			//Template等のケース、likeで検索
			//conditions.add(new Like(getPropertyName(), "%" + StringUtil.escapeEqlForLike(getValue().toString()) + "%"));
			conditions.add(new Like(getPropertyName(), getValue().toString(), Like.MatchPattern.PARTIAL));
		}
		return conditions;
	}

}
