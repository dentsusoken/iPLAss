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

import org.iplass.mtp.entity.query.ASTNode;
import org.iplass.mtp.entity.query.ASTTransformer;
import org.iplass.mtp.entity.query.QueryException;

/**
 * EQLが変換されたデータベースネイティブのSQL文に付与するヒントを指定するヒント句です。<br>
 * 
 * <code>native('ORDERED USE_NL_WITH_INDEX(...)')</code>
 * <br>
 * のように、SQLに付与したいヒント句を''で囲って指定します。 <br>
 * 
 * テーブルに対するヒント句（MySQLのindex hintなど）を指定する場合は、 第一引数にテーブル名を指定します。<br>
 * 
 * <code>native(q0, 'FORCE INDEX(...)')</code>
 * 
 * 
 * @author K.Higuchi
 *
 */
public class NativeHint extends Hint {
	private static final long serialVersionUID = -7024532266130797312L;

	//生SQLに対するヒント
	private String table;
	private String hintExpression;
	
	public NativeHint() {
	}
	
	public NativeHint(String hintExpression) {
		this.hintExpression = hintExpression;
		checkValidHint();
	}
	
	public NativeHint(String table, String hintExpression) {
		this.table = table;
		this.hintExpression = hintExpression;
		checkValidHint();
	}
	
	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public String getHintExpression() {
		return hintExpression;
	}

	public void setHintExpression(String hintExpression) {
		this.hintExpression = hintExpression;
		checkValidHint();
	}
	
	private void checkValidHint() {
		if (hintExpression != null) {
			if (hintExpression.contains(";")
					|| hintExpression.contains("--")
					|| hintExpression.contains("/*")
					|| hintExpression.contains("*/")) {
				throw new QueryException("NativeHint expression:'" + hintExpression + "' is not valid.");
			}
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((hintExpression == null) ? 0 : hintExpression.hashCode());
		result = prime * result + ((table == null) ? 0 : table.hashCode());
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
		NativeHint other = (NativeHint) obj;
		if (hintExpression == null) {
			if (other.hintExpression != null)
				return false;
		} else if (!hintExpression.equals(other.hintExpression))
			return false;
		if (table == null) {
			if (other.table != null)
				return false;
		} else if (!table.equals(other.table))
			return false;
		return true;
	}

	@Override
	public String toString() {
		if (table == null) {
			return "native('" + hintExpression + "')";
		} else {
			return "native(" + table + ",'" + hintExpression + "')";
		}
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
