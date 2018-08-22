/*
 * Copyright (C) 2013 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import org.iplass.mtp.entity.query.condition.expr.And;
import org.iplass.mtp.entity.query.condition.expr.Not;
import org.iplass.mtp.entity.query.condition.expr.Or;
import org.iplass.mtp.entity.query.condition.expr.Paren;
import org.iplass.mtp.entity.query.condition.predicate.Between;
import org.iplass.mtp.entity.query.condition.predicate.Contains;
import org.iplass.mtp.entity.query.condition.predicate.Equals;
import org.iplass.mtp.entity.query.condition.predicate.Greater;
import org.iplass.mtp.entity.query.condition.predicate.GreaterEqual;
import org.iplass.mtp.entity.query.condition.predicate.In;
import org.iplass.mtp.entity.query.condition.predicate.IsNotNull;
import org.iplass.mtp.entity.query.condition.predicate.IsNull;
import org.iplass.mtp.entity.query.condition.predicate.Lesser;
import org.iplass.mtp.entity.query.condition.predicate.LesserEqual;
import org.iplass.mtp.entity.query.condition.predicate.Like;
import org.iplass.mtp.entity.query.condition.predicate.NotEquals;

public class ConditionVisitorSupport implements ConditionVisitor {

	@Override
	public boolean visit(And and) {
		return true;
	}

	@Override
	public boolean visit(Between between) {
		return true;
	}

	@Override
	public boolean visit(Paren paren) {
		return true;
	}

	@Override
	public boolean visit(Equals equals) {
		return true;
	}

	@Override
	public boolean visit(Greater greater) {
		return true;
	}

	@Override
	public boolean visit(GreaterEqual greaterEqual) {
		return true;
	}

	@Override
	public boolean visit(In in) {
		return true;
	}

	@Override
	public boolean visit(Lesser lesser) {
		return true;
	}

	@Override
	public boolean visit(LesserEqual lesserEqual) {
		return true;
	}

	@Override
	public boolean visit(Like like) {
		return true;
	}

	@Override
	public boolean visit(Not not) {
		return true;
	}

	@Override
	public boolean visit(NotEquals notEquals) {
		return true;
	}

	@Override
	public boolean visit(Or or) {
		return true;
	}

	@Override
	public boolean visit(IsNotNull isNotNull) {
		return true;
	}

	@Override
	public boolean visit(IsNull isNull) {
		return true;
	}

	@Override
	public boolean visit(Contains contains) {
		return true;
	}

}
