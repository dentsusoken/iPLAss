/*
 * Copyright (C) 2013 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.entity.definition.properties;

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.annotation.MultiLang;
import org.iplass.mtp.entity.SelectValue;
import org.iplass.mtp.entity.definition.LocalizedSelectValueDefinition;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.PropertyDefinitionType;
import org.iplass.mtp.entity.definition.properties.selectvalue.SelectValueDefinition;


/**
 * 選択肢からのセレクトを行うプロパティを表す定義。
 * SelectPropertyの定義方法は2つ。
 * <ol>
 * <li>ローカル定義：SelectPropertyに直接選択肢(SelectValue)を定義(selectValueList,localizedSelectValueList)</li>
 * <li>グローバル定義：共通に定義されているSelectValueDefinitionを指定(selectValueDefinitionName)。</li>
 * </ol>
 * 両方設定時にはローカル定義より、グローバル定義の方が優先される。
 *
 * @author K.Higuchi
 *
 */
public class SelectProperty extends PropertyDefinition {
	private static final long serialVersionUID = 3614342763254943340L;

	private String selectValueDefinitionName;

	@MultiLang(isMultiLangValue = false, itemKey = "selectValue", itemGetter = "getSelectValueList", itemSetter = "setSelectValueList", multiLangGetter = "getLocalizedSelectValueList", multiLangSetter = "setLocalizedSelectValueList", isSelectValue = true)
	private List<SelectValue> selectValueList;

	private List<LocalizedSelectValueDefinition> localizedSelectValueList;

	public SelectProperty() {
	}

	public SelectProperty(String name) {
		setName(name);
	}

	public SelectProperty(String name, SelectValue... value) {
		setName(name);
		if (value != null) {
			for (SelectValue v: value) {
				addSelectValue(v);
			}
		}
	}

	public SelectProperty(SelectValueDefinition selectValueDefinition, boolean deepCopy) {
		applySelectValueDefinition(selectValueDefinition, deepCopy);
	}

	/**
	 * 指定のvalueのSelectValue定義を返却
	 *
	 * @param value
	 * @return
	 */
	public SelectValue getSelectValue(String value) {
		if (selectValueList != null) {
			for (SelectValue s: selectValueList) {
				if (value.equals(s.getValue())) {
					return s;
				}
			}
		}
		return null;
	}

	/**
	 * 指定のvalueで、指定のlocaleのSelectValue定義を返却。
	 * 指定のlocaleで定義されていなかった場合は、デフォルトのSelectValue定義を返却。
	 *
	 * @param value
	 * @param locale
	 * @return
	 */
	public SelectValue getLocalizedSelectValue(String value, String locale) {
		if (localizedSelectValueList != null && locale != null) {
			for (LocalizedSelectValueDefinition ls: localizedSelectValueList) {
				if (locale.equals(ls.getLocaleName())) {
					SelectValue s = ls.getSelectValue(value);
					if (s != null) {
						return s;
					}

					break;
				}
			}
		}

		//default
		return getSelectValue(value);
	}

	public void applySelectValueDefinition(SelectValueDefinition selectValueDefinition, boolean deepCopy) {
		selectValueDefinitionName = selectValueDefinition.getName();
		if (selectValueDefinition.getSelectValueList() != null) {
			if (deepCopy) {
				selectValueList = new ArrayList<SelectValue>(selectValueDefinition.getSelectValueList().size());
				for (SelectValue sv: selectValueDefinition.getSelectValueList()) {
					selectValueList.add(sv.copy());
				}
			} else {
				selectValueList = selectValueDefinition.getSelectValueList();
			}
		}
		if (selectValueDefinition.getLocalizedSelectValueList() != null) {
			if (deepCopy) {
				localizedSelectValueList = new ArrayList<LocalizedSelectValueDefinition>(selectValueDefinition.getLocalizedSelectValueList().size());
				for (LocalizedSelectValueDefinition lsvd: selectValueDefinition.getLocalizedSelectValueList()) {
					LocalizedSelectValueDefinition copyLsvd = new LocalizedSelectValueDefinition();
					copyLsvd.setLocaleName(lsvd.getLocaleName());
					if (lsvd.getSelectValueList() != null) {
						ArrayList<SelectValue> copyLsvl = new ArrayList<SelectValue>(lsvd.getSelectValueList());
						for (SelectValue sv: lsvd.getSelectValueList()) {
							copyLsvl.add(sv.copy());
						}
						copyLsvd.setSelectValueList(copyLsvl);
					}
					localizedSelectValueList.add(copyLsvd);
				}
			} else {
				localizedSelectValueList = selectValueDefinition.getLocalizedSelectValueList();
			}
		}
	}

	public String getSelectValueDefinitionName() {
		return selectValueDefinitionName;
	}

	public void setSelectValueDefinitionName(String selectValueDefinitionName) {
		this.selectValueDefinitionName = selectValueDefinitionName;
	}

	public void addSelectValue(SelectValue value) {
		if (selectValueList == null) {
			selectValueList = new ArrayList<SelectValue>();
		}
		selectValueList.add(value);
	}

	public List<SelectValue> getSelectValueList() {
		return selectValueList;
	}

	public void setSelectValueList(List<SelectValue> selectValueList) {
		this.selectValueList = selectValueList;
	}


	public void addLocalizedSelectValue(LocalizedSelectValueDefinition localizedSelectValue) {
		if (localizedSelectValueList == null) {
			localizedSelectValueList = new ArrayList<LocalizedSelectValueDefinition>();
		}
		localizedSelectValueList.add(localizedSelectValue);
	}

	public List<LocalizedSelectValueDefinition> getLocalizedSelectValueList() {
		return localizedSelectValueList;
	}

	public void setLocalizedSelectValueList(List<LocalizedSelectValueDefinition> localizedSelectValueList) {
		this.localizedSelectValueList = localizedSelectValueList;
	}

	@Override
	public Class<?> getJavaType() {
		return SelectValue.class;
	}

	@Override
	public PropertyDefinitionType getType() {
		return PropertyDefinitionType.SELECT;
	}


}
