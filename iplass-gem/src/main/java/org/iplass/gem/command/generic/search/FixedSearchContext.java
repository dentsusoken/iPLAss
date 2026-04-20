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

package org.iplass.gem.command.generic.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.iplass.gem.command.Constants;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.query.OrderBy;
import org.iplass.mtp.entity.query.PreparedQuery;
import org.iplass.mtp.entity.query.SortSpec;
import org.iplass.mtp.entity.query.SortSpec.SortType;
import org.iplass.mtp.entity.query.Where;
import org.iplass.mtp.entity.query.condition.Condition;
import org.iplass.mtp.entity.query.condition.expr.And;
import org.iplass.mtp.view.filter.EntityFilter;
import org.iplass.mtp.view.filter.EntityFilterItem;
import org.iplass.mtp.view.filter.EntityFilterManager;

public class FixedSearchContext extends SearchContextBase {

	@Override
	public Where getWhere() {
		Where where = null;
		EntityFilterManager efm = ManagerLocator.getInstance()
				.getManager(EntityFilterManager.class);
		EntityFilter entityFilter = efm.get(getDefName());
		EntityFilterItem item = null;
		String filterName = getFilterName();
		if (entityFilter != null && filterName != null && !filterName.isEmpty()) {
			item = entityFilter.getItem(filterName);
		}

		List<Condition> conditions = new ArrayList<Condition>();
		if (item != null && item.getCondition() != null) {
			conditions.add(new PreparedQuery(item.getCondition()).condition(null));
		}
		if (getConditionSection().isUseDefaultConditionWithFilterDefinition()) {
			Condition defaultCond = getDefaultCondition();
			if (defaultCond != null) {
				conditions.add(defaultCond);
			}
		}
		if (!conditions.isEmpty()) {
			And and = new And(conditions);
			where = new Where(and);
		}
		return where;
	}

	@Override
	public boolean checkParameter() {
		// 検索条件固定なのでチェックしない
		return true;
	}

	@Override
	public OrderBy getOrderBy() {
		Optional<String> requestSortKey = getRequestSortKey();
		Optional<EntityFilterItem> filter = getFilterItem();

		return getOrderByWithFilter(requestSortKey, filter, new SortSpec(Entity.OID, SortType.DESC));
	}

	/**
	 * ソート設定を取得します。
	 */
	private Optional<EntityFilterItem> getFilterItem() {
		EntityFilterManager efm = ManagerLocator.getInstance()
				.getManager(EntityFilterManager.class);
		EntityFilter entityFilter = efm.get(getDefName());
		String filterName = getFilterName();

		if (entityFilter != null && filterName != null && !filterName.isEmpty()) {
			// TODO: item がnullの場合はエラーとしたい
			return Optional.ofNullable(entityFilter.getItem(filterName));
		}
		return Optional.empty();
	}

	private String getFilterName() {
		return getRequest().getParam(Constants.FILTER_NAME);
	}

}
