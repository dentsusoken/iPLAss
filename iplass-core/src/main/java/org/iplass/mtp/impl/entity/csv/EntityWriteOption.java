/*
 * Copyright (C) 2018 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.entity.csv;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.properties.SelectProperty;
import org.iplass.mtp.entity.query.OrderBy;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.Where;

public class EntityWriteOption extends ParseOption {

	/** 出力文字コード */
	private String charset = "UTF-8";

	/** 列ごとにクォートを出力するか */
	private boolean quoteAll;

	/** 全バージョンデータを出力するか */
	private boolean versioned = true;

	/** 参照Entityのバージョンを出力するか */
	private boolean withReferenceVersion = true;

	/** Binaryプロパティを出力するか */
	private boolean withBinary;

	/** Binaryデータの出力先ディレクトリ */
	private String exportBinaryDataDir;

	/** 被参照プロパティを出力するか */
	private boolean withMappedByReference;

	/** 出力プロパティ(直接指定) */
	private List<String> properties;

	/** Where条件 */
	private Where where;

	/** OrderBy条件 */
	private OrderBy orderBy;

	/** 出力上限値。0以下は無制限 */
	private int limit = 0;

	/** 多重度複数の参照を含む検索処理の一括ロード上限値。0以下は無制限 */
	private int batchLoadLimit = 0;

	/** CSVダウンロード時にLimitが指定されている場合にOrderByを必ず指定する。SQLServer対応。 */
	private boolean mustOrderByWithLimit;

	/** 列の表示名出力文字列を返す関数 */
	private Function<PropertyDefinition, String> columnDisplayName = property -> "";

	/** SelectPropertyのソートをするかを返す関数 */
	private Function<SelectProperty, Boolean> sortSelectValue = property -> false;

	/** 検索実行前Query処理 */
	private Function<Query, SearchQueryCsvContext> beforeSearch = query -> new SearchQueryCsvContext(query);

	/** 検索実行後Query処理 */
	private BiConsumer<Query, Entity> afterSearch = (query, entity) -> {};

	/**
	 * 出力文字コードを返します。
	 *
	 * @return 出力文字コード
	 */
	public String getCharset() {
		return charset;
	}

	/**
	 * 出力文字コードを設定します。
	 *
	 * @param charset 出力文字コード
	 */
	public void setCharset(String charset) {
		this.charset = charset;
	}

	/**
	 * 出力文字コードを設定します。
	 *
	 * @param charset 出力文字コード
	 * @return インスタンス
	 */
	public EntityWriteOption charset(String charset) {
		setCharset(charset);
		return this;
	}

	/**
	 * 列ごとにクォートを出力するかを返します。
	 *
	 * @return 列ごとにクォートを出力するか
	 */
	public boolean isQuoteAll() {
		return quoteAll;
	}

	/**
	 * 列ごとにクォートを出力するかを設定します。
	 *
	 * @param quoteAll 列ごとにクォートを出力するか
	 */
	public void setQuoteAll(boolean quoteAll) {
		this.quoteAll = quoteAll;
	}

	/**
	 * 列ごとにクォートを出力するかを設定します。
	 *
	 * @param quoteAll 列ごとにクォートを出力するか
	 * @return インスタンス
	 */
	public EntityWriteOption quoteAll(boolean quoteAll) {
		setQuoteAll(quoteAll);
		return this;
	}

	/**
	 * 全バージョンデータを出力するかを返します。
	 *
	 * @return 全バージョンデータを出力するか
	 */
	public boolean isVersioned() {
		return versioned;
	}

	/**
	 * 全バージョンデータを出力するかを設定します。
	 *
	 * @param versioned 全バージョンデータを出力するか
	 */
	public void setVersioned(boolean versioned) {
		this.versioned = versioned;
	}

	/**
	 * 全バージョンデータを出力するかを設定します。
	 *
	 * @param versioned 全バージョンデータを出力するか
	 * @return インスタンス
	 */
	public EntityWriteOption versioned(boolean versioned) {
		setVersioned(versioned);
		return this;
	}

	/**
	 * 参照Entityのバージョンを出力するかを返します。
	 *
	 * @return 参照Entityのバージョンを出力するか
	 */
	public boolean isWithReferenceVersion() {
		return withReferenceVersion;
	}

	/**
	 * 参照Entityのバージョンを出力するかを設定します。
	 *
	 * @param withReferenceVersion 参照Entityのバージョンを出力するか
	 */
	public void setWithReferenceVersion(boolean withReferenceVersion) {
		this.withReferenceVersion = withReferenceVersion;
	}

	/**
	 * 参照Entityのバージョンを出力するかを設定します。
	 *
	 * @param withReferenceVersion 参照Entityのバージョンを出力するか
	 * @return インスタンス
	 */
	public EntityWriteOption withReferenceVersion(boolean withReferenceVersion) {
		setWithReferenceVersion(withReferenceVersion);
		return this;
	}

	/**
	 * Binaryプロパティを出力するかを返します。
	 *
	 * @return Binaryプロパティを出力するか
	 */
	public boolean isWithBinary() {
		return withBinary;
	}

	/**
	 * Binaryプロパティを出力するかを設定します。
	 *
	 * @param withBinary Binaryプロパティを出力するか
	 */
	public void setWithBinary(boolean withBinary) {
		this.withBinary = withBinary;
	}

	/**
	 * Binaryプロパティを出力するかを設定します。
	 *
	 * @param withBinary Binaryプロパティを出力するか
	 * @return インスタンス
	 */
	public EntityWriteOption withBinary(boolean withBinary) {
		setWithBinary(withBinary);
		return this;
	}

	/**
	 * Binaryデータの出力先ディレクトリを返します。
	 *
	 * @return Binaryデータの出力先ディレクトリ
	 */
	public String getExportBinaryDataDir() {
		return exportBinaryDataDir;
	}

	/**
	 * Binaryデータの出力先ディレクトリを設定します。
	 *
	 * @param exportBinaryDataDir Binaryデータの出力先ディレクトリ
	 */
	public void setExportBinaryDataDir(String exportBinaryDataDir) {
		this.exportBinaryDataDir = exportBinaryDataDir;
	}

	/**
	 * Binaryデータの出力先ディレクトリを設定します。
	 *
	 * @param exportBinaryDataDir Binaryデータの出力先ディレクトリ
	 * @return インスタンス
	 */
	public EntityWriteOption exportBinaryDataDir(String exportBinaryDataDir) {
		setExportBinaryDataDir(exportBinaryDataDir);
		return this;
	}

	/**
	 * 被参照プロパティを出力するかを返します。
	 *
	 * @return 被参照プロパティを出力するか
	 */
	public boolean isWithMappedByReference() {
		return withMappedByReference;
	}

	/**
	 * 被参照プロパティを出力するかを設定します。
	 *
	 * @param withMappedByReference 被参照プロパティを出力するか
	 */
	public void setWithMappedByReference(boolean withMappedByReference) {
		this.withMappedByReference = withMappedByReference;
	}

	/**
	 * 被参照プロパティを出力するかを設定します。
	 *
	 * @param withMappedByReference 被参照プロパティを出力するか
	 * @return インスタンス
	 */
	public EntityWriteOption withMappedByReference(boolean withMappedByReference) {
		setWithMappedByReference(withMappedByReference);
		return this;
	}

	/**
	 * 出力プロパティ(直接指定)を返します。
	 *
	 * @return 出力プロパティ(直接指定)
	 */
	public List<String> getProperties() {
		return properties;
	}

	/**
	 * 出力プロパティ(直接指定)を設定します。
	 *
	 * @param properties 出力プロパティ(直接指定)
	 */
	public void setProperties(List<String> properties) {
		this.properties = properties;
	}

	/**
	 * 出力プロパティ(直接指定)を設定します。
	 *
	 * @param properties 出力プロパティ(直接指定)
	 * @return インスタンス
	 */
	public EntityWriteOption properties(List<String> properties) {
		setProperties(properties);
		return this;
	}

	/**
	 * Where条件を返します。
	 *
	 * @return Where条件
	 */
	public Where getWhere() {
		return where;
	}

	/**
	 * Where条件を設定します。
	 *
	 * @param where Where条件
	 */
	public void setWhere(Where where) {
		this.where = where;
	}

	/**
	 * Where条件を設定します。
	 *
	 * @param where Where条件
	 * @return インスタンス
	 */
	public EntityWriteOption where(Where where) {
		setWhere(where);
		return this;
	}

	/**
	 * OrderBy条件を返します。
	 *
	 * @return OrderBy条件
	 */
	public OrderBy getOrderBy() {
		return orderBy;
	}

	/**
	 * OrderBy条件を設定します。
	 *
	 * @param orderBy OrderBy条件
	 */
	public void setOrderBy(OrderBy orderBy) {
		this.orderBy = orderBy;
	}

	/**
	 * OrderBy条件を設定します。
	 *
	 * @param orderBy OrderBy条件
	 * @return インスタンス
	 */
	public EntityWriteOption orderBy(OrderBy orderBy) {
		setOrderBy(orderBy);
		return this;
	}

	/**
	 * 出力上限値を返します。
	 *
	 * @return 出力上限値
	 */
	public int getLimit() {
		return limit;
	}

	/**
	 * 出力上限値を設定します。0以下は無制限です。
	 *
	 * @param limit 出力上限値
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}

	/**
	 * 出力上限値を設定します。0以下は無制限です。
	 *
	 * @param limit 出力上限値
	 * @return インスタンス
	 */
	public EntityWriteOption limit(int limit) {
		setLimit(limit);
		return this;
	}

	/**
	 * 多重度複数の参照を含む検索処理の一括ロード上限値を返します。
	 *
	 * @return 多重度複数の参照を含む検索処理の一括ロード上限値
	 */
	public int getBatchLoadLimit() {
		return batchLoadLimit;
	}

	/**
	 * 多重度複数の参照を含む検索処理の一括ロード上限値を設定します。0以下は無制限です。
	 *
	 * @param batchLoadLimit 多重度複数の参照を含む検索処理の一括ロード上限値
	 */
	public void setBatchLoadLimit(int batchLoadLimit) {
		this.batchLoadLimit = batchLoadLimit;
	}

	/**
	 * 多重度複数の参照を含む検索処理の一括ロード上限値を設定します。0以下は無制限です。
	 *
	 * @param batchLoadLimit 多重度複数の参照を含む検索処理の一括ロード上限値
	 * @return インスタンス
	 */
	public EntityWriteOption batchLoadLimit(int batchLoadLimit) {
		setBatchLoadLimit(batchLoadLimit);
		return this;
	}

	/**
	 * CSVダウンロード時にLimitが指定されている場合にOrderByを必ず指定するかを返します。
	 *
	 * @return CSVダウンロード時にLimitが指定されている場合にOrderByを必ず指定するか
	 */
	public boolean isMustOrderByWithLimit() {
		return mustOrderByWithLimit;
	}

	/**
	 * CSVダウンロード時にLimitが指定されている場合にOrderByを必ず指定するかを設定します。
	 *
	 * @param mustOrderByWithLimit CSVダウンロード時にLimitが指定されている場合にOrderByを必ず指定するか
	 */
	public void setMustOrderByWithLimit(boolean mustOrderByWithLimit) {
		this.mustOrderByWithLimit = mustOrderByWithLimit;
	}

	/**
	 * CSVダウンロード時にLimitが指定されている場合にOrderByを必ず指定するかを設定します。
	 *
	 * @param mustOrderByWithLimit CSVダウンロード時にLimitが指定されている場合にOrderByを必ず指定するか
	 * @return インスタンス
	 */
	public EntityWriteOption mustOrderByWithLimit(boolean mustOrderByWithLimit) {
		setMustOrderByWithLimit(mustOrderByWithLimit);
		return this;
	}

	/**
	 *  列の表示名出力文字列を返す関数を返します。
	 *
	 * @return 列の表示名出力文字列を返す関数
	 */
	public Function<PropertyDefinition, String> getColumnDisplayName() {
		return columnDisplayName;
	}

	/**
	 * 列の表示名出力文字列を返す関数を設定します。
	 *
	 * @param columnDisplayName 列の表示名出力文字列を返す関数
	 */
	public void setColumnDisplayName(Function<PropertyDefinition, String> columnDisplayName) {
		this.columnDisplayName = columnDisplayName;
	}

	/**
	 * 列の表示名出力文字列を返す関数を設定します。
	 *
	 * @param columnDisplayName 列の表示名出力文字列を返す関数
	 * @return インスタンス
	 */
	public EntityWriteOption columnDisplayName(Function<PropertyDefinition, String> columnDisplayName) {
		setColumnDisplayName(columnDisplayName);
		return this;
	}

	/**
	 * SelectPropertyのソートするかを返す関数を返します。
	 *
	 * @return SelectPropertyのソートするかを返す関数
	 */
	public Function<SelectProperty, Boolean> getSortSelectValue() {
		return sortSelectValue;
	}

	/**
	 * SelectPropertyのソートするかを返す関数を設定します。
	 *
	 * @param sortSelectValue SelectPropertyのソートするかを返す関数
	 */
	public void setSortSelectValue(Function<SelectProperty, Boolean> sortSelectValue) {
		this.sortSelectValue = sortSelectValue;
	}

	/**
	 * SelectPropertyのソートをするかを返す関数を設定します。
	 *
	 * @param sortSelectValue SelectPropertyのソートをするかを返す関数
	 * @return インスタンス
	 */
	public EntityWriteOption sortSelectValue(Function<SelectProperty, Boolean> sortSelectValue) {
		setSortSelectValue(sortSelectValue);
		return this;
	}

	/**
	 * 検索実行前Query処理を返します。
	 *
	 * @return 検索実行前Query処理
	 */
	public Function<Query, SearchQueryCsvContext> getBeforeSearch() {
		return beforeSearch;
	}

	/**
	 * 検索実行前Query処理を設定します。
	 *
	 * @param beforeSearch 検索実行前Query処理
	 */
	public void setBeforeSearch(Function<Query, SearchQueryCsvContext> beforeSearch) {
		this.beforeSearch = beforeSearch;
	}

	/**
	 * 検索実行前Query処理を設定します。
	 *
	 * @param beforeSearch 検索実行前Query処理
	 * @return インスタンス
	 */
	public EntityWriteOption beforeSearch(Function<Query, SearchQueryCsvContext> beforeSearch) {
		setBeforeSearch(beforeSearch);
		return this;
	}

	/**
	 * 検索実行後Query処理を返します。
	 *
	 * @return 検索実行後Query処理
	 */
	public BiConsumer<Query, Entity> getAfterSearch() {
		return afterSearch;
	}

	/**
	 * 検索実行後Query処理を設定します。
	 *
	 * @param afterSearch 検索実行後Query処理
	 */
	public void setAfterSearch(BiConsumer<Query, Entity> afterSearch) {
		this.afterSearch = afterSearch;
	}

	/**
	 * 検索実行後Query処理を設定します。
	 *
	 * @param afterSearch 検索実行後Query処理
	 * @return インスタンス
	 */
	public EntityWriteOption afterSearch(BiConsumer<Query, Entity> afterSearch) {
		setAfterSearch(afterSearch);
		return this;
	}

	@Override
	public EntityWriteOption dateFormat(String dateFormat) {
		setDateFormat(dateFormat);
		return this;
	}

	@Override
	public EntityWriteOption datetimeSecFormat(String datetimeSecFormat) {
		setDatetimeSecFormat(datetimeSecFormat);
		return this;
	}

	@Override
	public EntityWriteOption timeSecFormat(String timeSecFormat) {
		setTimeSecFormat(timeSecFormat);
		return this;
	}

	public static class SearchQueryCsvContext {

		private Query query;

		private boolean doPrivileged;

		private String[] withoutConditionReferenceName;

		public SearchQueryCsvContext(Query query) {
			this.query = query;
		}

		public Query getQuery() {
		    return query;
		}

		public void setQuery(Query query) {
		    this.query = query;
		}

		public boolean isDoPrivileged() {
		    return doPrivileged;
		}

		public void setDoPrivileged(boolean doPrivileged) {
		    this.doPrivileged = doPrivileged;
		}

		public String[] getWithoutConditionReferenceName() {
			return withoutConditionReferenceName;
		}

		public void setWithoutConditionReferenceName(String... withoutConditionReferenceName) {
			this.withoutConditionReferenceName = withoutConditionReferenceName;
		}
	}
}
