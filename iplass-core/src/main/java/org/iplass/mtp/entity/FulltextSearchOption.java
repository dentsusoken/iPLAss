/*
 * Copyright (C) 2020 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.entity;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.iplass.mtp.entity.query.OrderBy;

/**
 * 全文検索時のオプション
 */
public class FulltextSearchOption {

	private Map<String, FulltextSearchCondition> conditions;

	public FulltextSearchOption() {
	}

	public Map<String, FulltextSearchCondition> getConditions() {
		if (conditions == null) {
			conditions = new LinkedHashMap<>();
		}
		return conditions;
	}

	public void setConditions(Map<String, FulltextSearchCondition> conditions) {
		this.conditions = conditions;
	}

	public static class FulltextSearchCondition {

		private List<String> properties;

		private OrderBy order;

		public FulltextSearchCondition() {
		}

		public FulltextSearchCondition(List<String> properties) {
			this(properties, null);
		}

		public FulltextSearchCondition(List<String> properties, OrderBy order) {
			this.properties = properties;
			this.order = order;
		}

		public List<String> getProperties() {
			return properties;
		}

		public void setProperties(List<String> properties) {
			this.properties = properties;
		}

		public OrderBy getOrder() {
			return order;
		}

		public void setOrder(OrderBy order) {
			this.order = order;
		}
	}
}
