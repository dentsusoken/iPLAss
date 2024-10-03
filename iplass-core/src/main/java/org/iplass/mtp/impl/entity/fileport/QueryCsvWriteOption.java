/*
 * Copyright (C) 2018 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.entity.fileport;

import java.util.function.BiConsumer;
import java.util.function.Function;

import org.iplass.mtp.entity.query.Query;

public class QueryCsvWriteOption extends ParseOption {

	/** 出力文字コード */
	private String charset = "UTF-8";

	/** 列ごとにクォートを出力するか */
	private boolean quoteAll = false;

	/** 出力上限値。0以下は無制限 */
	private int limit = 0;

	/** 検索実行前Query処理 */
	private Function<Query, Query> beforeSearch = query -> query;

	/** 検索実行後Query処理 */
	private BiConsumer<Query, Object[]> afterSearch = (query, values) -> {};

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public boolean isQuoteAll() {
		return quoteAll;
	}

	public void setQuoteAll(boolean quoteAll) {
		this.quoteAll = quoteAll;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public Function<Query, Query> getBeforeSearch() {
		return beforeSearch;
	}

	public void setBeforeSearch(Function<Query, Query> beforeSearch) {
		this.beforeSearch = beforeSearch;
	}

	public BiConsumer<Query, Object[]> getAfterSearch() {
		return afterSearch;
	}

	public void setAfterSearch(BiConsumer<Query, Object[]> afterSearch) {
		this.afterSearch = afterSearch;
	}

	public QueryCsvWriteOption charset(String charset) {
		setCharset(charset);
		return this;
	}

	public QueryCsvWriteOption quoteAll(boolean quoteAll) {
		setQuoteAll(quoteAll);
		return this;
	}

	public QueryCsvWriteOption limit(int limit) {
		setLimit(limit);
		return this;
	}

	public QueryCsvWriteOption beforeSearch(Function<Query, Query> beforeSearch) {
		setBeforeSearch(beforeSearch);
		return this;
	}

	public QueryCsvWriteOption afterSearch(BiConsumer<Query, Object[]> afterSearch) {
		setAfterSearch(afterSearch);
		return this;
	}

	@Override
	public QueryCsvWriteOption dateFormat(String dateFormat) {
		setDateFormat(dateFormat);
		return this;
	}

	@Override
	public QueryCsvWriteOption datetimeSecFormat(String datetimeSecFormat) {
		setDatetimeSecFormat(datetimeSecFormat);
		return this;
	}

	@Override
	public QueryCsvWriteOption timeSecFormat(String timeSecFormat) {
		setTimeSecFormat(timeSecFormat);
		return this;
	}

}
