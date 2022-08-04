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
import org.iplass.mtp.impl.util.CoreResourceBundleUtil;
import org.iplass.mtp.util.StringUtil;

/**
 * Filter条件式からConditionを生成します。
 */
public class FilterExpressionParser {

	private String expression;
	private FilterItemHandler itemHandler;

	/**
	 * コンストラクタ
	 *
	 * @param expression 条件式
	 * @param itemHandler Noに対するConditionを生成するクラス
	 */
	public FilterExpressionParser(String expression, FilterItemHandler itemHandler) {
		this.expression = expression;
		this.itemHandler = itemHandler;
	}

	/**
	 * 条件式を解析して、Conditionを返します。
	 *
	 * @return 条件
	 */
	public Condition parse() {

		if (StringUtil.isEmpty(expression)) {
			return null;
		}

		//$1,$11は区別する必要がある
		//条件値に$1がある場合は変換してはだめ

		StringBuilder sb = new StringBuilder();

		//$nを検索
		Matcher m = Pattern.compile("\\$\\d+").matcher(expression);
		int curIndex = 0;
		while(m.find()) {
			//対象のFilter番号を取得(1始まり)
			String numStr = expression.substring(m.start() + 1, m.end());
			int condIndex = Integer.parseInt(numStr) - 1;

			//$より前の条件式を出力
			if (curIndex < m.start()) {
				sb.append(expression.substring(curIndex, m.start()));
			}

			//$nを条件値に置き換え
			if (condIndex >= 0 && !itemHandler.isIndexOutOfBounds(condIndex)) {
				Condition c = itemHandler.getCondition(condIndex);
				if (c != null) {
					sb.append(c.toString());
				} else {
					//条件は設定されているが条件値が未指定(エラー)
					throw new UnsupportedFilterOperationException(resourceString("view.filter.nullValueErr", "$" + numStr));
				}
			} else {
				//指定された条件番号に対する条件がない(エラー)
				throw new UnsupportedFilterOperationException(resourceString("view.filter.numberNotFoundErr", "$" + numStr));
			}

			curIndex = m.end();
		}

		//残りの条件式を出力
		if (curIndex < expression.length()) {
			sb.append(expression.substring(curIndex));
		}

		try {
			return Condition.newCondition(sb.toString());
		} catch (QueryException e) {
			throw new UnsupportedFilterOperationException(resourceString("view.filter.incorrectErr"), e);
		}
	}

	/**
	 * Noに該当するConditionを生成します。
	 * 各機能ごとに指定されたnumに該当するConditionを返すよう実装します。
	 */
	public interface FilterItemHandler {

		/**
		 * 指定されたIndexに該当する条件があるかを返します。
		 *
		 * @param index Index(0始まり)
		 * @return true：存在する
		 */
		boolean isIndexOutOfBounds(int index);

		/**
		 * 指定されたIndexに該当する条件を返します。
		 *
		 * @param index Index(0始まり)
		 * @return 条件
		 */
		Condition getCondition(int index);

	}

	private static String resourceString(String key, Object... arguments) {
		return CoreResourceBundleUtil.resourceString(key, arguments);
	}
}
