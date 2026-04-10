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
import org.iplass.mtp.SystemException;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.properties.ReferenceProperty;
import org.iplass.mtp.entity.query.OrderBy;
import org.iplass.mtp.entity.query.PreparedQuery;
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
import org.iplass.mtp.view.generic.EntityViewUtil;
import org.iplass.mtp.view.generic.element.section.SortSetting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SearchListContext extends SearchContextBase {

	private static Logger logger = LoggerFactory.getLogger(SearchListContext.class);

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

	/**
	 * TODO: {@link #getRequestSortSpec(String)} を使うように
	 * TODO: {@link #getSettingSortSpec(String)} を使うように
	 */
	@Override
	public OrderBy getOrderBy() {
		Optional<String> requestSortKey = getRequestSortKey();

		if (requestSortKey.isPresent()) {
			String sortKey = requestSortKey.get();
			PropertyDefinition pd = EntityViewUtil.getPropertyDefinition(sortKey, getEntityDefinition());
			String propName = sortKey;
			if (pd instanceof ReferenceProperty) {
				propName = sortKey + "." + Entity.OID;
			}
			return new OrderBy().add(new SortSpec(propName, getSortType()));
		}

		Optional<OrderBy> filterOrderBy = getOrderByFromFilterSortSetting();
		if (filterOrderBy.isPresent()) {
			return filterOrderBy.get();
		}

		if (!getSortSettings().isEmpty()) {
			OrderBy orderBy = null;

			for (SortSetting ss : getSortSettings()) {
				if (ss.getSortKey() != null) {
					String key = null;
					PropertyDefinition pd = getPropertyDefinition(ss.getSortKey());
					if (pd instanceof ReferenceProperty) {
						key = ss.getSortKey() + "." + Entity.OID;
					} else {
						key = ss.getSortKey();
					}
					SortType type = SortType.valueOf(ss.getSortType()
							.name());
					NullOrderingSpec nullOrderingSpec = getNullOrderingSpec(ss.getNullOrderType());
					if (orderBy == null)
						orderBy = new OrderBy();
					orderBy.add(key, type, nullOrderingSpec);
				}
			}
			return orderBy;
		}
		return new OrderBy().add(new SortSpec(Entity.UPDATE_DATE, SortType.DESC));

	}

	/**
	 * フィルタ設定のソート設定からOrderByを取得します。
	 */
	private Optional<OrderBy> getOrderByFromFilterSortSetting() {
		EntityFilterItem filter = getFilterItem();

		if (filter != null && StringUtil.isNotBlank(filter.getSort())) {
			SyntaxService service = ServiceRegistry.getRegistry()
					.getService(SyntaxService.class);
			OrderBySyntax syntax = service.getSyntaxContext(QuerySyntaxRegister.QUERY_CONTEXT)
					.getSyntax(OrderBySyntax.class);
			try {
				return Optional.of(syntax.parse(new ParseContext("order by " + filter.getSort())));
			} catch (ParseException e) {
				throw new SystemException(e.getMessage(), e);
			}
		}
		return Optional.empty();

	}

	@Override
	protected Integer getOffset() {
		String _offset = getRequest().getParam(Constants.SEARCH_OFFSET);
		String topViewListOffset = getRequest().getParam(Constants.TOPVIEW_LIST_OFFSET);

		int offset = 0;
		try {
			offset = Integer.parseInt(_offset);
		} catch (NumberFormatException e) {
			if (logger.isDebugEnabled()) {
				logger.debug("parse number faild.", e);
			}
		}

		if (StringUtil.isNotEmpty(topViewListOffset)) {
			offset = Integer.parseInt(topViewListOffset);
		}

		return offset;
	}

	@Override
	public boolean checkParameter() {
		// 条件は固定なのでチェックしない
		return true;
	}

}
