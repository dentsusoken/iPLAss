/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.entity.query.condition;

import org.iplass.mtp.entity.query.ASTNode;
import org.iplass.mtp.entity.query.QueryException;
import org.iplass.mtp.impl.parser.ParseException;
import org.iplass.mtp.impl.query.QueryServiceHolder;
import org.iplass.mtp.impl.query.condition.expr.OrSyntax;


/**
 * 検索条件を表す抽象クラス。
 * 
 * @author K.Higuchi
 *
 */
public abstract class Condition implements ASTNode {
	private static final long serialVersionUID = 7777497275666525341L;

	public static Condition newCondition(String condition) {
		try {
			return QueryServiceHolder.getInstance().getQueryParser().parse(condition, OrSyntax.class);
		} catch (ParseException e) {
			throw new QueryException(e.getMessage(), e);
		}
	}
	
	public abstract void accept(ConditionVisitor visitor);
	
}
