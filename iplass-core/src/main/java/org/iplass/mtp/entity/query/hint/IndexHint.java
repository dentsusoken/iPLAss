/*
 * Copyright (C) 2014 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.entity.query.hint;

import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.entity.query.ASTNode;
import org.iplass.mtp.entity.query.ASTTransformer;

/**
 * EQLに対して、propertyNameListで指定されるプロパティのINDEXの利用を明示的に指定するヒント句です。
 * ただし、実際の物理テーブルにおいて、当該INDEXから駆動されるかどうかは、データベースのオプティマイザの判断によります。
 * 
 * @author K.Higuchi
 *
 */
public class IndexHint extends EQLHint {
	private static final long serialVersionUID = -6307067004560551763L;

	private List<String> propertyNameList;
	
	public IndexHint() {
	}
	
	public IndexHint(String... propertyName) {
		if (propertyName != null) {
			propertyNameList = new ArrayList<>(propertyName.length);
			for (String p: propertyName) {
				propertyNameList.add(p);
			}
		}
	}
	
	public IndexHint(List<String> propertyNameList) {
		this.propertyNameList = propertyNameList;
	}
	
	
	public List<String> getPropertyNameList() {
		return propertyNameList;
	}

	public void setPropertyNameList(List<String> propertyNameList) {
		this.propertyNameList = propertyNameList;
	}
	
	public IndexHint add(String propertyName) {
		if (propertyNameList == null) {
			propertyNameList = new ArrayList<>();
		}
		propertyNameList.add(propertyName);
		return this;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((propertyNameList == null) ? 0 : propertyNameList.hashCode());
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
		IndexHint other = (IndexHint) obj;
		if (propertyNameList == null) {
			if (other.propertyNameList != null)
				return false;
		} else if (!propertyNameList.equals(other.propertyNameList))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("index");
		if (propertyNameList != null && propertyNameList.size() > 0) {
			sb.append("(");
			boolean first = true;
			for (String p: propertyNameList) {
				if (first) {
					first = false;
				} else {
					sb.append(",");
				}
				sb.append(p);
			}
			sb.append(")");
		}
		
		return sb.toString();
	}

	@Override
	public ASTNode accept(ASTTransformer transformer) {
		return transformer.visit(this);
	}

	@Override
	public void accept(HintVisitor visitor) {
		visitor.visit(this);
	}

}
