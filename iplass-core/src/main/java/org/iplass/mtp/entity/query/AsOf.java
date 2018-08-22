/*
 * Copyright (C) 2015 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import org.iplass.mtp.entity.query.value.ValueExpression;

/**
 * バージョン管理しているEntityを検索する際に利用可能な、いつ時点のバージョンを検索するかを指示するための構文。<br>
 * From句および、Refer句に指定可能。
 * 
 * 
 * @author K.Higuchi
 *
 */
public class AsOf implements ASTNode {
	private static final long serialVersionUID = 5164214522449613176L;

	public enum AsOfSpec {
		/** 登録・更新時点を指し示す。（※Refer句のみで有効） */
		UPDATE_TIME,
		/** 最新を指し示す。 */
		NOW,
		/** 特定の時点（別途AsOfのvalueにて指定）を指し示す。 */
		SPEC_VALUE
	}
	
	private AsOfSpec spec;
	private ValueExpression value;
	
	public AsOf() {
	}
	
	public AsOf(AsOfSpec spec) {
		this.spec = spec;
	}
	
	public AsOf(ValueExpression value) {
		this.spec = AsOfSpec.SPEC_VALUE;
		this.value = value;
	}

	public AsOfSpec getSpec() {
		return spec;
	}

	public void setSpec(AsOfSpec spec) {
		this.spec = spec;
	}

	public ValueExpression getValue() {
		return value;
	}

	public void setValue(ValueExpression value) {
		this.value = value;
	}

	@Override
	public ASTNode accept(ASTTransformer transformer) {
		return transformer.visit(this);
	}
	
	
	public void accept(QueryVisitor visitor) {
		if (visitor.visit(this)) {
			if (value != null) {
				value.accept(visitor);
			}
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("as of ");
		switch (spec) {
		case NOW:
			sb.append("now");
			break;
		case UPDATE_TIME:
			sb.append("update time");
			break;
		case SPEC_VALUE:
			sb.append(value);
			break;
		default:
			break;
		}
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((spec == null) ? 0 : spec.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		AsOf other = (AsOf) obj;
		if (spec != other.spec)
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

}
