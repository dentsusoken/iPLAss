/*
 * Copyright (C) 2018 DENTSU SOKEN INC. All Rights Reserved.
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
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.query.OrderBy;
import org.iplass.mtp.entity.query.PreparedQuery;
import org.iplass.mtp.entity.query.Select;
import org.iplass.mtp.entity.query.SortSpec;
import org.iplass.mtp.entity.query.SortSpec.SortType;
import org.iplass.mtp.entity.query.Where;
import org.iplass.mtp.entity.query.condition.Condition;
import org.iplass.mtp.entity.query.condition.expr.And;
import org.iplass.mtp.view.filter.EntityFilter;
import org.iplass.mtp.view.filter.EntityFilterItem;
import org.iplass.mtp.view.generic.element.section.SortSetting;

public class SearchNameListContext extends SearchContextBase {

	private EntityFilter filter;

	public void setFilter(EntityFilter filter) {
		this.filter = filter;
	}

	private Optional<EntityFilterItem> getFilterItem() {
		if (filter == null) {
			return Optional.empty();
		}
		return Optional.ofNullable(filter.getItem(getRequest().getParam(Constants.FILTER_NAME)));
	}

	@Override
	public Select getSelect() {
		Select select = new Select();
		select.add(Entity.OID, Entity.NAME, Entity.VERSION);
		return select;
	}

	@Override
	public Where getWhere() {
		Where where = new Where();

		List<Condition> conditions = new ArrayList<>();
		Optional<EntityFilterItem> filter = getFilterItem();

		if (filter.isPresent() && filter.get()
				.getCondition() != null) {
			conditions.add(new PreparedQuery(filter.get()
					.getCondition()).condition(null));
		}
		Condition defaultCond = getDefaultCondition();
		if (defaultCond != null) {
			if (filter.isEmpty()
					|| (getConditionSection().isUseDefaultConditionWithFilterDefinition())) {
				conditions.add(defaultCond);
			}
		}
		if (!conditions.isEmpty()) {
			And and = new And(conditions);
			where.setCondition(and);
		}

		return where;
	}

	@Override
	public OrderBy getOrderBy() {
		Optional<String> requestSortKey = getRequestSortKey();
		Optional<EntityFilterItem> filter = getFilterItem();
		List<SortSetting> sortSettings = getSortSettings();

		SortSpec defaultSortSpec = new SortSpec(Entity.UPDATE_DATE, SortType.DESC);

		if (filter.isEmpty() && requestSortKey.isEmpty() && getConditionSection().isUnsorted()) {
			return null;
		}

		return getOrderBy(requestSortKey, filter, sortSettings, defaultSortSpec);
	}

	@Override
	public boolean checkParameter() {
		// 条件は固定なのでチェックしない
		return true;
	}

	@Override
	public boolean isUseUserPropertyEditor() {
		// 名前のみ検索なので利用しない
		return false;
	}

}
