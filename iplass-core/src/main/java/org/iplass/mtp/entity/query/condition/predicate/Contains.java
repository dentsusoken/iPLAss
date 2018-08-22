/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.entity.query.condition.predicate;

import org.iplass.mtp.entity.query.ASTNode;
import org.iplass.mtp.entity.query.ASTTransformer;
import org.iplass.mtp.entity.query.condition.ConditionVisitor;

/**
 * CONTAINS条件文を表す。
 *
 * @author K.Higuchi
 *
 */
public class Contains extends Predicate {
	private static final long serialVersionUID = 1946093340942491141L;

	private String searchText;

	@Override
	public ASTNode accept(ASTTransformer transformer) {
		return transformer.visit(this);
	}

	@Override
	public void accept(ConditionVisitor visitor) {
		visitor.visit(this);
	}

	public Contains() {
	}

	public Contains(String searchText) {
		setSearchText(searchText);
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	public String getSearchText() {
		return searchText;
	}

	public void setProperty(String searchText) {
		this.searchText = searchText;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("contains('");

		if (searchText == null) {
			sb.append("null");
		} else {
			for (int i = 0; i < searchText.length(); i++) {
				char c = searchText.charAt(i);
				if (c == '\'') {
					sb.append("'");
				}
				sb.append(c);
			}
		}

		sb.append("')");
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((searchText == null) ? 0 : searchText.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Contains other = (Contains) obj;
		if (searchText == null) {
			if (other.searchText != null)
				return false;
		} else if (!searchText.equals(other.searchText))
			return false;
		return true;
	}

}
