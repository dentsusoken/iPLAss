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

package org.iplass.mtp.entity.definition;

import java.io.Serializable;
import java.util.List;

import org.iplass.mtp.entity.SelectValue;

public class LocalizedSelectValueDefinition implements Serializable {

	private static final long serialVersionUID = -4998288776358261822L;
	private String localeName;
	private List<SelectValue> selectValueList;
	
	public LocalizedSelectValueDefinition() {
	}
	
	public LocalizedSelectValueDefinition(String localeName,
			List<SelectValue> selectValueList) {
		this.localeName = localeName;
		this.selectValueList = selectValueList;
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

	public String getLocaleName() {
		return localeName;
	}
	public void setLocaleName(String localeName) {
		this.localeName = localeName;
	}
	public List<SelectValue> getSelectValueList() {
		return selectValueList;
	}
	public void setSelectValueList(List<SelectValue> selectValueList) {
		this.selectValueList = selectValueList;
	}

}
