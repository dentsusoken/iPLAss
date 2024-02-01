/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.entity.query.value.primary;

import org.iplass.mtp.entity.query.ASTNode;
import org.iplass.mtp.entity.query.ASTTransformer;
import org.iplass.mtp.entity.query.QueryException;
import org.iplass.mtp.entity.query.value.ValueExpressionVisitor;

/**
 * Entityの属性項目を表す。
 * 
 * @author K.Higuchi
 *
 */
public class EntityField extends PrimaryValue {
	private static final long serialVersionUID = -1271662342712631753L;

	private String propertyName;
	
	public EntityField() {
	}
	
	public EntityField(String propertyName) {
		this.propertyName = propertyName;
		checkValidPropertyName();
	}
	
	private void checkValidPropertyName() {
		//簡易チェック
		if (propertyName != null && propertyName.length() > 0) {
			//先頭はアルファベットもしくは_もしくは.に限定
			char c = propertyName.charAt(0);
			if (!(c >= 'A' && c <= 'Z' ||
					c >= 'a' && c <= 'z' ||
					c == '.' ||
					c == '_')) {
				throw new QueryException(propertyName + " is not valid EntityField.");
			}
			for (int i = 1; i < propertyName.length(); i++) {
				c = propertyName.charAt(i);
				if (!(c >= '0' && c <= '9' ||
						c >= 'A' && c <= 'Z' ||
						c >= 'a' && c <= 'z' ||
						c == '.' ||
						c == '_')) {
					throw new QueryException(propertyName + " is not valid EntityField.");
				}
			}
		}
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
		checkValidPropertyName();
	}

	@Override
	public String toString() {
		return propertyName;
	}

	public void accept(ValueExpressionVisitor visitor) {
		visitor.visit(this);
	}

	public ASTNode accept(ASTTransformer transformer) {
		return transformer.visit(this);
	}

	
	public int unnestCount() {
		if (propertyName != null) {
			for (int i = 0; i < propertyName.length(); i++) {
				if (propertyName.charAt(i) != '.') {
					return i;
				}
			}
		}
		return 0;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((propertyName == null) ? 0 : propertyName.hashCode());
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
		EntityField other = (EntityField) obj;
		if (propertyName == null) {
			if (other.propertyName != null)
				return false;
		} else if (!propertyName.equals(other.propertyName))
			return false;
		return true;
	}

}
