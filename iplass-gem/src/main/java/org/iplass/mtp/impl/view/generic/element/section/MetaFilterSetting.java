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

package org.iplass.mtp.impl.view.generic.element.section;

import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.view.generic.element.section.FilterSetting;

public class MetaFilterSetting implements MetaData {

	private static final long serialVersionUID = 4344132148591127941L;

	/** フィルタ名 */
	private String filterName;

	@Override
	public MetaData copy() {
		return ObjectUtil.deepCopy(this);
	}

	public void applyConfig(FilterSetting setting, EntityContext context, EntityHandler entity) {
		filterName = setting.getFilterName();
	}

	public FilterSetting currentConfig(EntityContext context, EntityHandler entity) {
		FilterSetting def = new FilterSetting();
		def.setFilterName(filterName);
		return def;
	}

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
