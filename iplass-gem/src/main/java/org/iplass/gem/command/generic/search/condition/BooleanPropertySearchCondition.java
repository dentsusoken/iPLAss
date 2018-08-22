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

import org.apache.commons.text.StrTokenizer;
import org.iplass.gem.command.Constants;
import org.iplass.gem.command.GemResourceBundleUtil;
import org.iplass.gem.command.generic.search.SearchConditionDetail;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.view.generic.editor.BooleanPropertyEditor;
import org.iplass.mtp.view.generic.editor.PropertyEditor;
import org.iplass.mtp.web.template.TemplateUtil;

public class BooleanPropertySearchCondition extends PropertySearchCondition {

	/**
	 * コンストラクタ
	 * @param definition
	 * @param property
	 * @param value
	 */
	public BooleanPropertySearchCondition(PropertyDefinition definition,
			PropertyEditor editor, Object value) {
		super(definition, editor, value);
	}

	public BooleanPropertySearchCondition(PropertyDefinition definition,
			PropertyEditor editor, Object value, String parent) {
		super(definition, editor, value, parent);
	}

	@Override
	protected Object convertDetailValue(SearchConditionDetail detail) {
		if (Constants.NOTNULL.equals(detail.getPredicate())
				|| Constants.NULL.equals(detail.getPredicate())) {
			return null;
		} else if (Constants.IN.equals(detail.getPredicate())) {
			StrTokenizer st = StrTokenizer.getCSVInstance(detail.getValue());
			String[] values = st.getTokenArray();
			Boolean[] ret = new Boolean[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = string2Boolean(values[i]);
			}
			return ret;
		} else {
			return string2Boolean(detail.getValue());
		}
	}

	private boolean string2Boolean(String str) {
		//nullは無い筈
		if (StringUtil.isBlank(str)) return false;

		//true=1、false=0
		if ("true".equals(str.toLowerCase())) {
			return true;
		} else if ("false".equals(str.toLowerCase())) {
			return false;
		}

		BooleanPropertyEditor editor = getEditor();
		String trueLabel = TemplateUtil.getMultilingualString(editor.getTrueLabel(), editor.getLocalizedTrueLabelList());
		if (StringUtil.isNotBlank(trueLabel)) {
			if (trueLabel.equals(str)) {
				return true;
			}
		} else {
			//表示ラベル未指定時は検索条件（ラジオ）の表示文言を利用
			trueLabel = TemplateUtil.getMultilingualString(
					GemResourceBundleUtil.resourceString("generic.editor.boolean.BooleanPropertyEditor_Condition.enable"),
					GemResourceBundleUtil.resourceList("generic.editor.boolean.BooleanPropertyEditor_Condition.enable"));
			if (StringUtil.isNotBlank(trueLabel) && trueLabel.equals(str)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public BooleanPropertyEditor getEditor() {
		return (BooleanPropertyEditor) super.getEditor();
	}
}
