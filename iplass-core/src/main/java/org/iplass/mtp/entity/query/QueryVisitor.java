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

package org.iplass.mtp.entity.query;

import org.iplass.mtp.entity.query.condition.ConditionVisitor;
import org.iplass.mtp.entity.query.hint.HintVisitor;
import org.iplass.mtp.entity.query.value.ValueExpressionVisitor;

public interface QueryVisitor extends ConditionVisitor, ValueExpressionVisitor, HintVisitor {
	
	public boolean visit(Query query);
	public boolean visit(Select select);
	public boolean visit(Where where);
	public boolean visit(From from);
	public boolean visit(AsOf asOf);
	public boolean visit(GroupBy groupBy);
	public boolean visit(Having having);
	public boolean visit(OrderBy orderBy);
	public boolean visit(SortSpec order);
	public boolean visit(Limit limit);
	public boolean visit(Refer refer);
	
	public boolean visit(SubQuery subQuery);

}
