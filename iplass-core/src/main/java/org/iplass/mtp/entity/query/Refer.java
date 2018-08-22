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

package org.iplass.mtp.entity.query;

import org.iplass.mtp.entity.query.condition.Condition;
import org.iplass.mtp.entity.query.value.primary.EntityField;

/**
 * 参照項目(referenceName)を結合して取得する際、
 * 結合条件(condition)を付与したい場合に利用します。
 * 
 * @author K.Higuchi
 *
 */
public class Refer implements ASTNode {
	private static final long serialVersionUID = 8596841126564202143L;

	private EntityField referenceName;
	private AsOf asOf;
	private Condition condition;
	
	public Refer() {
	}
	
	public Refer(EntityField referenceName, Condition condition) {
		this.referenceName = referenceName;
		this.condition = condition;
	}

	public Refer(EntityField referenceName, AsOf asOf, Condition condition) {
		this.referenceName = referenceName;
		this.asOf = asOf;
		this.condition = condition;
	}
	
	@Override
	public ASTNode accept(ASTTransformer transformer) {
		return transformer.visit(this);
	}

	public void accept(QueryVisitor visitor) {
		if (visitor.visit(this)) {
			if (referenceName != null) {
				referenceName.accept(visitor);
			}
			if (asOf != null) {
				asOf.accept(visitor);
			}
			if (condition != null) {
				condition.accept(visitor);
			}
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("refer ");
		if (referenceName != null) {
			sb.append(referenceName);
		}
		if (asOf != null) {
			sb.append(" ").append(asOf);
		}
		if (condition != null) {
			sb.append(" on ");
			sb.append(condition);
		}
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((asOf == null) ? 0 : asOf.hashCode());
		result = prime * result
				+ ((condition == null) ? 0 : condition.hashCode());
		result = prime * result
				+ ((referenceName == null) ? 0 : referenceName.hashCode());
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
		Refer other = (Refer) obj;
		if (asOf == null) {
			if (other.asOf != null)
				return false;
		} else if (!asOf.equals(other.asOf))
			return false;
		if (condition == null) {
			if (other.condition != null)
				return false;
		} else if (!condition.equals(other.condition))
			return false;
		if (referenceName == null) {
			if (other.referenceName != null)
				return false;
		} else if (!referenceName.equals(other.referenceName))
			return false;
		return true;
	}

	public EntityField getReferenceName() {
		return referenceName;
	}

	public void setReferenceName(EntityField referenceName) {
		this.referenceName = referenceName;
	}

	public AsOf getAsOf() {
		return asOf;
	}

	public void setAsOf(AsOf asOf) {
		this.asOf = asOf;
	}

	public Condition getCondition() {
		return condition;
	}

	public void setCondition(Condition condition) {
		this.condition = condition;
	}
	
}
