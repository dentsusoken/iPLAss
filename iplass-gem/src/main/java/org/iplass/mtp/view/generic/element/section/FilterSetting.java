/*
 * Copyright (C) 2017 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.view.generic.element.section;

import org.iplass.adminconsole.view.annotation.InputType;
import org.iplass.adminconsole.view.annotation.MetaFieldInfo;
import org.iplass.adminconsole.view.annotation.Refrectable;

public class FilterSetting implements Refrectable {

	private static final long serialVersionUID = -4918427800133631957L;

	/** フィルタ名 */
	@MetaFieldInfo(
			displayName="フィルタ名",
			displayNameKey="generic_element_section_FilterSetting_filterNameDisplayNameKey",
			inputType=InputType.FILTER,
			description="定型検索に表示するフィルタを指定します。",
			descriptionKey="generic_element_section_FilterSetting_filterNameDescriptionKey",
			required=true
	)
	private String filterName;

	/**
	 * フィルタ名を取得します。
	 * @return フィルタ名
	 */
	public String getFilterName() {
	    return filterName;
	}

	/**
	 * フィルタ名を設定します。
	 * @param filterName フィルタ名
	 */
	public void setFilterName(String filterName) {
	    this.filterName = filterName;
	}

}
