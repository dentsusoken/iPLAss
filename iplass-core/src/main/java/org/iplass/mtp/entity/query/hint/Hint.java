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

import java.util.List;

import org.iplass.mtp.entity.query.ASTNode;
import org.iplass.mtp.impl.query.QueryServiceHolder;

/**
 * ヒント句を表す抽象クラスです。
 * 
 * @author K.Higuchi
 *
 */
public abstract class Hint implements ASTNode {
	private static final long serialVersionUID = -829401135062686316L;

	/**
	 * 外部プロパティファイル（QueryServiceで定義）に定義されているヒントを読み込みます。
	 * 
	 * @param key 外部プロパティファイルに定義されるキー名
	 * @return
	 */
	public static List<Hint> externalHint(String key) {
		return QueryServiceHolder.getInstance().getExternalHint(key);
	}

	public abstract void accept(HintVisitor visitor);

}
