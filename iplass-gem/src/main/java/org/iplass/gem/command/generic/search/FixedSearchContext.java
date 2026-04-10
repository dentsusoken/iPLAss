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
import java.util.stream.Stream;

import org.iplass.gem.command.Constants;
import org.iplass.mtp.ManagerLocator;
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
import org.iplass.mtp.view.filter.EntityFilterManager;
import org.iplass.mtp.view.generic.element.property.PropertyColumn;

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
		Optional<SortSpec> requestSortSpec = getSortSpec();
		List<SortSpec> settingSortSpecs = getSettingSortSpecs();

		if (settingSortSpecs != null) {
			OrderBy orderBy = new OrderBy();
			Stream.concat(requestSortSpec.stream(), settingSortSpecs.stream())
					.forEach(orderBy::add);
			return orderBy;
		}

		if (!requestSortSpec.isPresent()) {
			return null;
		}
		OrderBy orderBy = new OrderBy();
		orderBy.add(requestSortSpec.get());
		return orderBy;
	}

	/**
	 * ソート設定を取得します。
	 */
	private List<SortSpec> getSettingSortSpecs() {
		EntityFilterManager efm = ManagerLocator.getInstance()
				.getManager(EntityFilterManager.class);
		EntityFilter entityFilter = efm.get(getDefName());
		String filterName = getFilterName();

		if (entityFilter != null && filterName != null && !filterName.isEmpty()) {
			EntityFilterItem item = entityFilter.getItem(filterName);
			// TODO: item がnullの場合はエラーとしたい
			if (item != null && StringUtil.isNotEmpty(item.getSort())) {
				SyntaxService service = ServiceRegistry.getRegistry()
						.getService(SyntaxService.class);
				OrderBySyntax syntax = service.getSyntaxContext(QuerySyntaxRegister.QUERY_CONTEXT)
						.getSyntax(OrderBySyntax.class);

				try {
					return syntax.parse(new ParseContext("order by " + item.getSort()))
							.getSortSpecList();
				} catch (ParseException e) {
					throw new SystemException(e.getMessage(), e);
				}
			}
		}
		return null; //TODO: emptyを返したい
	}

	/**
	 * ソート設定を取得します。
	 * @return ソート設定
	 * TODO: {@link #getRequestSortSpec(String)} と共通化してよいか？
	 */
	private Optional<SortSpec> getSortSpec() {
		return getRequestSortKey().map(sortKey -> {
			PropertyColumn property = getLayoutPropertyColumn(sortKey);
			if (property == null) {
				throw new RuntimeException();
			}

			PropertyDefinition pd = getPropertyDefinition(sortKey);
			String key;
			if (pd instanceof ReferenceProperty) {
				key = sortKey + "." + Entity.OID;
			} else {
				key = sortKey;
			}

			String sortType = getRequest().getParam(Constants.SEARCH_SORTTYPE);
			SortType type;
			if (StringUtil.isBlank(sortType)) {
				type = SortType.DESC;
			} else {
				type = SortType.valueOf(sortType);
			}

			NullOrderingSpec nullOrderingSpec = getNullOrderingSpec(property.getNullOrderType());
			SortSpec sortSpec = new SortSpec(key, type);
			sortSpec.setNullOrderingSpec(nullOrderingSpec);
			return sortSpec;
		});

	}

	private String getFilterName() {
		return getRequest().getParam(Constants.FILTER_NAME);
	}

}
