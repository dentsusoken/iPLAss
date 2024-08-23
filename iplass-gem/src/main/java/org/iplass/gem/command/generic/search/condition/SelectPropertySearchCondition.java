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

import org.apache.commons.text.StringTokenizer;
import org.iplass.gem.command.Constants;
import org.iplass.gem.command.generic.search.SearchConditionDetail;
import org.iplass.mtp.entity.SelectValue;
import org.iplass.mtp.entity.definition.LocalizedSelectValueDefinition;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.properties.SelectProperty;
import org.iplass.mtp.entity.query.condition.Condition;
import org.iplass.mtp.entity.query.condition.predicate.Equals;
import org.iplass.mtp.entity.query.condition.predicate.In;
import org.iplass.mtp.entity.query.condition.predicate.IsNull;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.tenant.TenantI18nInfo;
import org.iplass.mtp.view.generic.editor.EditorValue;
import org.iplass.mtp.view.generic.editor.PropertyEditor;
import org.iplass.mtp.view.generic.editor.SelectPropertyEditor;
import org.iplass.mtp.view.generic.editor.SelectPropertyEditor.SelectDisplayType;
import org.iplass.mtp.web.template.TemplateUtil;

public class SelectPropertySearchCondition extends PropertySearchCondition {

	public SelectPropertySearchCondition(PropertyDefinition definition,
			PropertyEditor editor, Object value) {
		super(definition, editor, value);
	}

	public SelectPropertySearchCondition(PropertyDefinition definition,
			PropertyEditor editor, Object value, String parent) {
		super(definition, editor, value, parent);
	}

	@Override
	public List<Condition> convertNormalCondition() {
		List<Condition> conditions = new ArrayList<>();
		Object value = getValue();
		SelectPropertyEditor editor = (SelectPropertyEditor) getEditor();
		if (value != null) {
			if (editor != null) {
				if (editor.getDisplayType() == SelectDisplayType.CHECKBOX
						|| editor.getDisplayType() == SelectDisplayType.HIDDEN) {
					SelectValue[] array = (SelectValue[]) value;
					if (array != null) {
						if (array.length == 1) {
							conditions.add(new Equals(getPropertyName(), array[0].getValue()));
						} else if (array.length > 1) {
							List<String> valueList = new ArrayList<>();
							for (SelectValue tmp : array) {
								valueList.add(tmp.getValue());
							}
							conditions.add(new In(getPropertyName(), valueList.toArray()));
						}
					}
				}
				// SELECT形式の場合、「is null」を検索条件として指定可能か
				else if (SelectDisplayType.SELECT == editor.getDisplayType()) {
					SelectValue sv = (SelectValue) value;
					if (editor.isIsNullSearchEnabled() && Constants.ISNULL.equals(sv.getValue())) {
						conditions.add(new IsNull(getPropertyName()));
					} else {
						conditions.add(new Equals(getPropertyName(), sv.getValue()));
					}
				}
				else {
					SelectValue sv = (SelectValue) value;
					conditions.add(new Equals(getPropertyName(), sv.getValue()));
				}
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
			StringTokenizer st = StringTokenizer.getCSVInstance(detail.getValue());
			String[] values = st.getTokenArray();
			String[] ret = new String[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = string2Value(values[i]);
			}
			return ret;
		} else {
			return string2Value(detail.getValue());
		}
	}

	private String string2Value(String str) {
		//値or表示ラベルが一致する場合に値を返す
		SelectPropertyEditor editor = (SelectPropertyEditor) getEditor();
		if (!editor.getValues().isEmpty()) {
			for (EditorValue ev : editor.getValues()) {
				if (ev.getValue().equals(str)) return str;

				String label = TemplateUtil.getMultilingualString(ev.getLabel(), ev.getLocalizedLabelList());
				if (label.equals(str)) return ev.getValue();
			}
		}

		//Expressionの可能性あるので念のためチェック
		if (getDefinition() instanceof SelectProperty) {
			SelectProperty sp = (SelectProperty) getDefinition();
			if (sp.getSelectValue(str) != null) return str;

			if (ExecuteContext.getCurrentContext().getCurrentTenant().getTenantConfig(TenantI18nInfo.class).isUseMultilingual()
					&& sp.getLocalizedSelectValueList() != null && !sp.getLocalizedSelectValueList().isEmpty()) {
				String lang = ExecuteContext.getCurrentContext().getLanguage();
				for (LocalizedSelectValueDefinition lsd : sp.getLocalizedSelectValueList()) {
					if (lsd.getLocaleName().equals(lang)) {
						if (lsd.getSelectValueList() != null && !lsd.getSelectValueList().isEmpty()) {
							//多言語設定あり
							for (SelectValue sv : lsd.getSelectValueList()) {
								if (sv.getDisplayName().equals(str)) return sv.getValue();
							}
						} else {
							//多言語設定なし
							for (SelectValue sv : sp.getSelectValueList()) {
								if (sv.getDisplayName().equals(str)) return sv.getValue();
							}
						}
					}
				}
			} else {
				//多言語設定なし
				for (SelectValue sv : sp.getSelectValueList()) {
					if (sv.getDisplayName().equals(str)) return sv.getValue();
				}
			}
		}
		return null;
	}

}
