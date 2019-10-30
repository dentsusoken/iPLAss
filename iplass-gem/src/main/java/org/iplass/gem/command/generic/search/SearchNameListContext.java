/*
 * Copyright (C) 2018 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import org.iplass.gem.command.Constants;
import org.iplass.mtp.SystemException;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.properties.ReferenceProperty;
import org.iplass.mtp.entity.query.OrderBy;
import org.iplass.mtp.entity.query.PreparedQuery;
import org.iplass.mtp.entity.query.Select;
import org.iplass.mtp.entity.query.SortSpec;
import org.iplass.mtp.entity.query.SortSpec.NullOrderingSpec;
import org.iplass.mtp.entity.query.SortSpec.SortType;
import org.iplass.mtp.entity.query.Where;
import org.iplass.mtp.entity.query.condition.Condition;
import org.iplass.mtp.entity.query.condition.expr.And;
import org.iplass.mtp.impl.parser.ParseContext;
import org.iplass.mtp.impl.parser.ParseException;
import org.iplass.mtp.impl.parser.SyntaxService;
import org.iplass.mtp.impl.query.OrderBySyntax;
import org.iplass.mtp.impl.query.QuerySyntaxRegister;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.view.filter.EntityFilter;
import org.iplass.mtp.view.filter.EntityFilterItem;
import org.iplass.mtp.view.generic.element.section.SortSetting;

public class SearchNameListContext extends SearchContextBase {

	private EntityFilter filter;

	public void setFilter(EntityFilter filter) {
		this.filter = filter;
	}

	private EntityFilterItem getFilterItem() {
		if (filter == null) {
			return null;
		}
		return filter.getItem(getRequest().getParam(Constants.FILTER_NAME));
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
		EntityFilterItem filter = getFilterItem();

		if (filter != null && filter.getCondition() != null) {
			conditions.add(new PreparedQuery(filter.getCondition()).condition(null));
		}
		Condition defaultCond = getDefaultCondition();
		if (defaultCond != null) {
			if (filter == null
					|| (filter != null && getConditionSection().isUseDefaultConditionWithFilterDefinition())) {
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
		EntityFilterItem filter = getFilterItem();

		OrderBy orderBy = null;
		if (filter != null && StringUtil.isNotBlank(filter.getSort())) {
			SyntaxService service = ServiceRegistry.getRegistry().getService(SyntaxService.class);
			OrderBySyntax syntax = service.getSyntaxContext(QuerySyntaxRegister.QUERY_CONTEXT).getSyntax(OrderBySyntax.class);
			ParseContext context = new ParseContext("order by " + filter.getSort());
			try {
				orderBy = syntax.parse(context);
			} catch (ParseException e) {
				throw new SystemException(e.getMessage(), e);
			}
		} else if (hasSortSetting()) {
			List<SortSetting> setting = getSortSetting();
			for (SortSetting ss : setting) {
				if (ss.getSortKey() != null) {
					String key = null;
					PropertyDefinition pd = getPropertyDefinition(ss.getSortKey());
					if (pd instanceof ReferenceProperty) {
						key = ss.getSortKey() + "." + Entity.OID;
					} else {
						key = ss.getSortKey();
					}
					SortType type = SortType.valueOf(ss.getSortType().name());
					NullOrderingSpec nullOrderingSpec = getNullOrderingSpec(ss.getNullOrderType());
					if (orderBy == null) orderBy = new OrderBy();
					orderBy.add(key, type, nullOrderingSpec);
				}
			}
		} else {
			orderBy = new OrderBy().add(new SortSpec(Entity.UPDATE_DATE, SortType.DESC));
		}

		return orderBy;
	}

	@Override
	public boolean checkParameter() {
		// 条件は固定なのでチェックしない
		return true;
	}
}
