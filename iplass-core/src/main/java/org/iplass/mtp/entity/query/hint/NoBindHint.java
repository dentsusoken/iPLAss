/*
 * Copyright (C) 2017 DENTSU SOKEN INC. All Rights Reserved.
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

/**
 * EQLに対して、実際にDataStoreにクエリー発行する際にバインド変数（JDBCの場合PrepareStatement）を利用しないことを指定するヒント句です。
 * ServiceConfigの設定により、常時バインドするように設定されている場合、特定のEQLをバインドせずに実行するために指定可能です。
 * 
 * <h5>no bindヒントを追加したEQL例：</h5>
 * select /*+ no_bind &#42;/ a, b from SampleEntity where c.x='hoge' and a=1 and b=15
 * 
 * @author K.Higuchi
 *
 */
public class NoBindHint extends EQLHint {
	private static final long serialVersionUID = 5942562046025811485L;

	public NoBindHint() {
	}
	
	@Override
	public ASTNode accept(ASTTransformer transformer) {
		return transformer.visit(this);
	}

	@Override
	public void accept(HintVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public int hashCode() {
		return "NoBindHint".hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "no_bind";
	}

}
