/*
 * Copyright (C) 2014 DENTSU SOKEN INC. All Rights Reserved.
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
 * EQLに対して、実際にDataStoreにクエリー発行する際にバインド変数（JDBCの場合PrepareStatement）を
 * 利用することを指定するヒント句です。
 * 
 * <h5>bindヒントを追加したEQL例（リテラル指定をすべてバインド変数とする場合）：</h5>
 * select /*+ bind &#42;/ a, b from SampleEntity where c.x='hoge' and a=1 and b=15
 * 
 * <h5>Bindヒントを追加したEQL例（バインド変数とするリテラルを選択する場合。バインドしないリテラルの前に/*+no_bind&#42;/を付与）：</h5>
 * select /*+ bind &#42;/ a, b from SampleEntity where c.x=/*+no_bind&#42;/'hoge' and a=1 and b=15<br>
 * ※上記例だと、'hoge'と15がバインド変数として実行されます。
 * 
 * @author K.Higuchi
 *
 */
public class BindHint extends EQLHint {
	private static final long serialVersionUID = 3006227867207974229L;

	public BindHint() {
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
		return 1003779204;//BindHintの文字列のhash
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
		return "bind";
	}

}
