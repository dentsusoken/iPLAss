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

package org.iplass.mtp.impl.rdb.sqlserver.function;

import java.util.List;
import java.util.function.Consumer;

import org.iplass.mtp.entity.query.QueryException;
import org.iplass.mtp.entity.query.value.primary.Function;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.function.FunctionAdapter;

public class SqlServerSubstrFunctionAdapter implements FunctionAdapter<Function> {
	/** 引数位置 - 文字列 */
	private static final int INDEX_STR = 0;
	/** 引数位置 - 開始位置 */
	private static final int INDEX_IDX = 1;
	/** 引数位置 - 長さ */
	private static final int INDEX_LN = 2;

	private String field;

	public SqlServerSubstrFunctionAdapter(String field) {
		this.field = field;
	}

	@Override
	public void toSQL(FunctionContext context, Function function, RdbAdapter rdb) {
		if (function.getArguments() == null || function.getArguments().size() < 2 || function.getArguments().size() > 3) {
			throw new QueryException(function.getName() + " must have two or three arguments.");
		}

		buildSql(context::append, context::appendArgument, function.getArguments());
	}

	@Override
	public void toSQL(StringBuilder context, List<CharSequence> args,
			RdbAdapter rdb) {
		if (args == null || args.size() < 2 || args.size() > 3) {
			throw new QueryException(getFunctionName() + " must have two or three arguments.");
		}

		buildSql(context::append, context::append, args);
	}

	@Override
	public String getFunctionName() {
		return field;
	}

	@Override
	public Class<?> getType(Function function, ArgumentTypeResolver typeResolver) {
		return String.class;
	}

	/**
	 * SUBSTR用のSQLを生成する
	 *
	 * @param <T> 引数データ型
	 * @param appendString 文字列追加メソッド
	 * @param appendArg 引数追加メソッド
	 * @param args 引数
	 */
	private <T> void buildSql(Consumer<String> appendString, Consumer<T> appendArg, List<T> args) {
		if (args.size() == 3) {
			// パラメータ３つパターン

			// CASE WHEN 0 <= IDX
			// THEN SUBSTRING(STR, IDX, LN)
			// ELSE LEFT(RIGHT(STR, ABS(IDX)), LN)
			// END

			appendString.accept("CASE WHEN 0 <= ");
			appendArg.accept(args.get(INDEX_IDX));
			appendString.accept(" THEN SUBSTRING(");
			appendArg.accept(args.get(INDEX_STR));
			appendString.accept(",");
			appendArg.accept(args.get(INDEX_IDX));
			appendString.accept(",");
			appendArg.accept(args.get(INDEX_LN));
			appendString.accept(") ELSE LEFT(RIGHT(");
			appendArg.accept(args.get(INDEX_STR));
			appendString.accept(",ABS(");
			appendArg.accept(args.get(INDEX_IDX));
			appendString.accept(")),");
			appendArg.accept(args.get(INDEX_LN));
			appendString.accept(") END");

		} else {
			// パラメータ２つパターン

			// CASE WHEN 0 <= IDX
			// THEN SUBSTRING(STR, IDX, LEN(STR))
			// ELSE RIGHT(STR, ABS(IDX))
			// END

			appendString.accept("CASE WHEN 0 <= ");
			appendArg.accept(args.get(INDEX_IDX));
			appendString.accept(" THEN SUBSTRING(");
			appendArg.accept(args.get(INDEX_STR));
			appendString.accept(",");
			appendArg.accept(args.get(INDEX_IDX));
			appendString.accept(",LEN(");
			appendArg.accept(args.get(INDEX_STR));
			appendString.accept(")) ELSE RIGHT(");
			appendArg.accept(args.get(INDEX_STR));
			appendString.accept(",ABS(");
			appendArg.accept(args.get(INDEX_IDX));
			appendString.accept(")) END");
		}
	}

}