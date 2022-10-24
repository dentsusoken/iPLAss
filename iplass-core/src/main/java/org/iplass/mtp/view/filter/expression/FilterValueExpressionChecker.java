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

package org.iplass.mtp.view.filter.expression;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.iplass.mtp.entity.query.QueryException;
import org.iplass.mtp.entity.query.condition.Condition;
import org.iplass.mtp.entity.query.condition.ConditionVisitor;
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
import org.iplass.mtp.impl.util.CoreResourceBundleUtil;

/**
 * Filter条件式がサポートされた式かをチェックします。
 */
public class FilterValueExpressionChecker implements ConditionVisitor {

	private static final String MSG_KEY = "view.filter.unsupportErr";

	/**
	 * 条件式がサポートされているかをチェックします。
	 *
	 * @param expression 条件式
	 */
	public void execute(String expression) {
		try {
			Condition.newCondition(convCheckExpression(expression)).accept(this);
		} catch (QueryException e) {
			throw new UnsupportedFilterOperationException(resourceString("view.filter.incorrectErr"), e);
		}
	}

	/**
	 * 条件式をConditionとして有効な形式に変換します。
	 *
	 * @param expression 条件式
	 * @return 変換結果
	 */
	private String convCheckExpression(String expression) {
		//$nを1=1に変更して、FilterExpressionを有効な条件文に変換
		StringBuilder sb = new StringBuilder();

		//$nを検索
		Matcher m = Pattern.compile("\\$\\d+").matcher(expression);
		int curIndex = 0;
		while(m.find()) {
			//対象のFilter番号を取得
			String numStr = expression.substring(m.start() + 1, m.end());
			int condNum = Integer.parseInt(numStr);

			//$より前の条件式を出力
			if (curIndex < m.start()) {
				sb.append(expression.substring(curIndex, m.start()));
			}

			//$nをn=nに置き換え
			sb.append(condNum + "=" + condNum);

			curIndex = m.end();
		}

		//残りの条件式を出力
		if (curIndex < expression.length()) {
			sb.append(expression.substring(curIndex));
		}
		return sb.toString();
	}

	@Override
	public boolean visit(And and) {
		return true;
	}

	@Override
	public boolean visit(Paren paren) {
		return true;
	}

	@Override
	public boolean visit(Or or) {
		return true;
	}

	@Override
	public boolean visit(Equals equals) {
		//内部的に変換したn=nの形式のみ許可する

		String strVal1 = equals.getPropertyName();
		String strVal2 = equals.getValue().toString();

		try {
			int val1 = Integer.parseInt(strVal1);
			int val2 = Integer.parseInt(strVal2);

			if (val1 == val2) {
				//falseを返すことで子供(Literal)のチェックをしない
				return false;
			}
		} catch (NumberFormatException e) {
			//エラーが発生した場合は、想定していない式が設定されている
		}

		throw new UnsupportedFilterOperationException(resourceString(MSG_KEY, equals.toString()));
	}

	@Override
	public boolean visit(Not not) {
		return true;
	}

	@Override
	public boolean visit(Between between) {
		throw new UnsupportedFilterOperationException(resourceString(MSG_KEY, between.toString()));
	}

	@Override
	public boolean visit(Greater greater) {
		throw new UnsupportedFilterOperationException(resourceString(MSG_KEY, greater.toString()));
	}

	@Override
	public boolean visit(GreaterEqual greaterEqual) {
		throw new UnsupportedFilterOperationException(resourceString(MSG_KEY, greaterEqual.toString()));
	}

	@Override
	public boolean visit(In in) {
		throw new UnsupportedFilterOperationException(resourceString(MSG_KEY, in.toString()));
	}

	@Override
	public boolean visit(Lesser lesser) {
		throw new UnsupportedFilterOperationException(resourceString(MSG_KEY, lesser.toString()));
	}

	@Override
	public boolean visit(LesserEqual lesserEqual) {
		throw new UnsupportedFilterOperationException(resourceString(MSG_KEY, lesserEqual.toString()));
	}

	@Override
	public boolean visit(Like like) {
		throw new UnsupportedFilterOperationException(resourceString(MSG_KEY, like.toString()));
	}

	@Override
	public boolean visit(NotEquals notEquals) {
		throw new UnsupportedFilterOperationException(resourceString(MSG_KEY, notEquals.toString()));
	}

	@Override
	public boolean visit(IsNotNull isNotNull) {
		throw new UnsupportedFilterOperationException(resourceString(MSG_KEY, isNotNull.toString()));
	}

	@Override
	public boolean visit(IsNull isNull) {
		throw new UnsupportedFilterOperationException(resourceString(MSG_KEY, isNull.toString()));
	}

	@Override
	public boolean visit(Contains contains) {
		throw new UnsupportedFilterOperationException(resourceString(MSG_KEY, contains.toString()));
	}

	private static String resourceString(String key, Object... arguments) {
		return CoreResourceBundleUtil.resourceString(key, arguments);
	}
}
