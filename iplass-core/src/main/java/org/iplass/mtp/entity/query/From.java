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

package org.iplass.mtp.entity.query;

/**
 * FROM句を表す。
 * 
 * @author K.Higuchi
 *
 */
public class From implements ASTNode {
	private static final long serialVersionUID = 7328311094744595840L;

	private String entityName;
	private AsOf asOf;

	public From() {
	}
	
	public From(String entityName) {
		this.entityName = entityName;
		checkValidEntityName();
	}
	
	private void checkValidEntityName() {
		//簡易チェック
		if (entityName != null && entityName.length() > 0) {
			//先頭はアルファベットもしくは_に限定
			char c = entityName.charAt(0);
			if (!(c >= 'A' && c <= 'Z' ||
					c >= 'a' && c <= 'z' ||
					c == '_')) {
				throw new QueryException(entityName + " is not valid EntityName.");
			}
			for (int i = 1; i < entityName.length(); i++) {
				c = entityName.charAt(i);
				if (!(c >= '0' && c <= '9' ||
						c >= 'A' && c <= 'Z' ||
						c >= 'a' && c <= 'z' ||
						c == '.' ||
						c == '_' ||
						c == ':')) {
					throw new QueryException(entityName + " is not valid EntityName.");
				}
			}
		}
	}

	
//	public From(String entityName, String alias) {
//		this.entityName = entityName;
//		this.alias = alias;
//	}
//	
//	public String getAlias() {
//		return alias;
//	}
//
//	public void setAlias(String alias) {
//		this.alias = alias;
//	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
		checkValidEntityName();
	}
	
	public AsOf getAsOf() {
		return asOf;
	}

	public void setAsOf(AsOf asOf) {
		this.asOf = asOf;
	}

	public void accept(QueryVisitor visitor) {
		visitor.visit(this);
	}
	
	@Override
	public String toString() {
		if (asOf == null) {
			return "from " + entityName;
		} else {
			return "from " + entityName + " " + asOf.toString();
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((asOf == null) ? 0 : asOf.hashCode());
		result = prime * result
				+ ((entityName == null) ? 0 : entityName.hashCode());
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
		From other = (From) obj;
		if (asOf == null) {
			if (other.asOf != null)
				return false;
		} else if (!asOf.equals(other.asOf))
			return false;
		if (entityName == null) {
			if (other.entityName != null)
				return false;
		} else if (!entityName.equals(other.entityName))
			return false;
		return true;
	}

	public ASTNode accept(ASTTransformer transformer) {
		return transformer.visit(this);
	}
	
}
