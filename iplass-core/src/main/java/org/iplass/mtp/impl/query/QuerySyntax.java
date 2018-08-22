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

package org.iplass.mtp.impl.query;

import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.entity.query.From;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.Refer;
import org.iplass.mtp.entity.query.Select;
import org.iplass.mtp.entity.query.Where;
import org.iplass.mtp.impl.parser.ParseContext;
import org.iplass.mtp.impl.parser.ParseException;
import org.iplass.mtp.impl.parser.Syntax;
import org.iplass.mtp.impl.parser.SyntaxContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class QuerySyntax implements Syntax<Query>, QueryConstants {
	
	private static Logger logger = LoggerFactory.getLogger(QuerySyntax.class);
	
	private SelectSyntax select;
	private FromSyntax from;
	private ReferSyntax refer;
	private WhereSyntax where;
	private GroupBySyntax groupBy;
	private HavingSyntax having;
	private OrderBySyntax orderBy;
	private LimitSyntax limit;

	public void init(SyntaxContext context) {
		select = context.getSyntax(SelectSyntax.class);
		from = context.getSyntax(FromSyntax.class);
		refer = context.getSyntax(ReferSyntax.class);
		where = context.getSyntax(WhereSyntax.class);
		groupBy = context.getSyntax(GroupBySyntax.class);
		having = context.getSyntax(HavingSyntax.class);
		orderBy = context.getSyntax(OrderBySyntax.class);
		limit = context.getSyntax(LimitSyntax.class);
	}
	
	public Query parse(ParseContext str) throws ParseException {
		
		//FIXME 空白位置のチェックがしっかりできているかどうかを再確認。
		
		long start = 0;
		if (logger.isTraceEnabled()) {
			start = System.nanoTime();
		}
		
		Query q = new Query();
		Select s = select.parse(str);
		q.setSelect(s);
		From f = from.parse(str);
		q.setFrom(f);
		
		List<Refer> referList = null;
		while (str.equalsNextToken(REFER, ParseContext.TOKEN_DELIMITERS)) {
			if (referList == null) {
				referList = new ArrayList<Refer>();
			}
			Refer r = refer.parse(str);
			referList.add(r);
			str.consumeChars(ParseContext.WHITE_SPACES);
		}
		if (referList != null) {
			q.setRefer(referList);
		}
		
		if (str.equalsNextToken(WHERE, ParseContext.TOKEN_DELIMITERS)) {
			Where w = where.parse(str);
			q.setWhere(w);
		}
		str.consumeChars(ParseContext.WHITE_SPACES);
		if (!str.isEnd()) {
			if (str.equalsNextToken(GROUP, ParseContext.TOKEN_DELIMITERS)) {
				q.setGroupBy(groupBy.parse(str));
			}
		}
		str.consumeChars(ParseContext.WHITE_SPACES);
		if (!str.isEnd()) {
			if (str.equalsNextToken(HAVING, ParseContext.TOKEN_DELIMITERS)) {
				q.setHaving(having.parse(str));
			}
		}
		str.consumeChars(ParseContext.WHITE_SPACES);
		if (!str.isEnd()) {
			if (str.equalsNextToken(ORDER, ParseContext.TOKEN_DELIMITERS)) {
				q.setOrderBy(orderBy.parse(str));
			}
		}
		str.consumeChars(ParseContext.WHITE_SPACES);
		if (!str.isEnd()) {
			if (str.equalsNextToken(LIMIT, ParseContext.TOKEN_DELIMITERS)) {
				q.setLimit(limit.parse(str));
			}
		}
		
		str.consumeChars(ParseContext.WHITE_SPACES);
		if (!str.isEnd()) {
			if (str.equalsNextToken(VERSIONED, ParseContext.TOKEN_DELIMITERS)) {
				q.setVersiond(true);
				str.consumeChars(VERSIONED.length());
			}
		}
		
		str.consumeChars(ParseContext.WHITE_SPACES);
		if (!str.isEnd()) {
			if (str.equalsNextToken(LOCALIZED, ParseContext.TOKEN_DELIMITERS)) {
				q.setLocalized(true);
				str.consumeChars(LOCALIZED.length());
			}
		}
		
		if (logger.isTraceEnabled()) {
			long time = System.nanoTime() - start;
			logger.trace("parse time:{}ms query:{}", ((double)time)/1000000, q);
		}
		
		str.consumeChars(ParseContext.WHITE_SPACES);
		
		//isEndチェックはここでは行わない。SyntaxParser経由でのパースのみisEnd()チェックする
		
		return q;
	}


}
