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

package org.iplass.mtp.impl.query;

import org.iplass.mtp.impl.parser.Syntax;
import org.iplass.mtp.impl.parser.SyntaxRegister;
import org.iplass.mtp.impl.query.condition.expr.AndSyntax;
import org.iplass.mtp.impl.query.condition.expr.NotSyntax;
import org.iplass.mtp.impl.query.condition.expr.OrSyntax;
import org.iplass.mtp.impl.query.condition.expr.ParenSyntax;
import org.iplass.mtp.impl.query.condition.predicate.BetweenSyntax;
import org.iplass.mtp.impl.query.condition.predicate.ComparisonPredicateSyntax;
import org.iplass.mtp.impl.query.condition.predicate.ContainsSyntax;
import org.iplass.mtp.impl.query.condition.predicate.InSyntax;
import org.iplass.mtp.impl.query.condition.predicate.IsNotNullSyntax;
import org.iplass.mtp.impl.query.condition.predicate.IsNullSyntax;
import org.iplass.mtp.impl.query.condition.predicate.LikeSyntax;
import org.iplass.mtp.impl.query.condition.predicate.PredicateSyntax;
import org.iplass.mtp.impl.query.hint.HintCommentSyntax;
import org.iplass.mtp.impl.query.hint.HintSyntax;
import org.iplass.mtp.impl.query.value.RowValueListSyntax;
import org.iplass.mtp.impl.query.value.aggregate.AggregateSyntax;
import org.iplass.mtp.impl.query.value.aggregate.WithinGroupSyntax;
import org.iplass.mtp.impl.query.value.controlflow.CaseSyntax;
import org.iplass.mtp.impl.query.value.controlflow.ElseSyntax;
import org.iplass.mtp.impl.query.value.controlflow.WhenSyntax;
import org.iplass.mtp.impl.query.value.expr.MinusSignSyntax;
import org.iplass.mtp.impl.query.value.expr.PolynomialSyntax;
import org.iplass.mtp.impl.query.value.expr.TermSyntax;
import org.iplass.mtp.impl.query.value.primary.ArrayValueSyntax;
import org.iplass.mtp.impl.query.value.primary.CastSyntax;
import org.iplass.mtp.impl.query.value.primary.EntityFieldSyntax;
import org.iplass.mtp.impl.query.value.primary.FunctionSyntax;
import org.iplass.mtp.impl.query.value.primary.LiteralSyntax;
import org.iplass.mtp.impl.query.value.primary.ParenValueSyntax;
import org.iplass.mtp.impl.query.value.primary.PrimaryValueSyntax;
import org.iplass.mtp.impl.query.value.subquery.ScalarSubQuerySyntax;
import org.iplass.mtp.impl.query.value.window.PartitionBySyntax;
import org.iplass.mtp.impl.query.value.window.WindowFunctionSyntax;
import org.iplass.mtp.impl.query.value.window.WindowOrderBySyntax;

public class QuerySyntaxRegister implements SyntaxRegister {

	public static String QUERY_CONTEXT = "query";


	public String getContextName() {
		return QUERY_CONTEXT;
	}

	public Syntax<?>[] getSyntax() {
		return new Syntax[] {
			new QuerySyntax(),
			new SelectSyntax(),
			new FromSyntax(),
			new ReferSyntax(),
			new AsOfSyntax(),
			new WhereSyntax(),
			new GroupBySyntax(),
			new HavingSyntax(),
			new OrderBySyntax(),
			new SortSpecSyntax(),
			new LimitSyntax(),
			new SubQuerySyntax(),

			//ValueExpression
			new MinusSignSyntax(),
			new PolynomialSyntax(),
			new TermSyntax(),
			new PrimaryValueSyntax(),
			new AggregateSyntax(),
			new WithinGroupSyntax(),
			new ScalarSubQuerySyntax(),
			new FunctionSyntax(),
			new CastSyntax(),
			new ParenValueSyntax(),
			new EntityFieldSyntax(),
			new LiteralSyntax(),
			new ArrayValueSyntax(),
			new CaseSyntax(),
			new WhenSyntax(),
			new ElseSyntax(),
			new WindowFunctionSyntax(),
			new PartitionBySyntax(),
			new WindowOrderBySyntax(),
			new RowValueListSyntax(),

			//Condition
			new AndSyntax(),
			new ParenSyntax(),
			new NotSyntax(),
			new OrSyntax(),
			new BetweenSyntax(),
			new ComparisonPredicateSyntax(),
			new InSyntax(),
			new IsNotNullSyntax(),
			new IsNullSyntax(),
			new LikeSyntax(),
			new ContainsSyntax(),
			new PredicateSyntax(),
			
			//Hint
			new HintCommentSyntax(),
			new HintSyntax()
		};

	}

}
