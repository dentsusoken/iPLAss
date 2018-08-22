/*
 * Copyright (C) 2013 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.entity.definition.properties.selectvalue;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.iplass.adminconsole.annotation.MultiLang;
import org.iplass.mtp.definition.Definition;
import org.iplass.mtp.entity.SelectValue;
import org.iplass.mtp.entity.definition.LocalizedSelectValueDefinition;

/**
 * SelectValueのグローバル定義。
 * グローバル定義されたSelectValueは
 * それぞれのEntity定義のSelectPropertyから共通的に利用することが可能。
 *
 * @author K.Higuchi
 *
 */
@XmlRootElement
public class SelectValueDefinition implements Definition {
	private static final long serialVersionUID = 7442258946492974274L;

	private String name;
	private String displayName;
	private String description;

	@MultiLang(isMultiLangValue = false, itemKey = "selectValue", itemGetter = "getSelectValueList", itemSetter = "setSelectValueList", multiLangGetter = "getLocalizedSelectValueList", multiLangSetter = "setLocalizedSelectValueList", isSelectValue = true)
	private List<SelectValue> selectValueList;
	private List<LocalizedSelectValueDefinition> localizedSelectValueList;

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
		if (localizedSelectValueList != null) {
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

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<SelectValue> getSelectValueList() {
		return selectValueList;
	}
	public void setSelectValueList(List<SelectValue> selectValueList) {
		this.selectValueList = selectValueList;
	}
	public List<LocalizedSelectValueDefinition> getLocalizedSelectValueList() {
		return localizedSelectValueList;
	}
	public void setLocalizedSelectValueList(
			List<LocalizedSelectValueDefinition> localizedSelectValueList) {
		this.localizedSelectValueList = localizedSelectValueList;
	}
	public void addSelectValue(SelectValue value) {
		if (selectValueList == null) {
			selectValueList = new ArrayList<SelectValue>();
		}
		selectValueList.add(value);
	}
	public void addLocalizedSelectValue(LocalizedSelectValueDefinition localizedSelectValue) {
		if (localizedSelectValueList == null) {
			localizedSelectValueList = new ArrayList<LocalizedSelectValueDefinition>();
		}
		localizedSelectValueList.add(localizedSelectValue);
	}
}
