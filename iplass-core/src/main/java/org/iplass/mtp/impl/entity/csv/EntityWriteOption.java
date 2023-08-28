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

	/** 多重度複数の参照を含む検索時に一括でロードするか。 */
	private boolean loadOnceOfHasMultipleReferenceEntity;

	/** 多重度複数の参照を含む検索時のロード単位。1以下は1件ずつロードする。 */
	private int loadSizeOfHasMultipleReferenceEntity = 1;

	/** CSVダウンロード時にLimitが指定されている場合にOrderByを必ず指定する。SQLServer対応。 */
	private boolean mustOrderByWithLimit;

	/** 列の表示名出力文字列 */
	private Function<PropertyDefinition, String> columnDisplayName = property -> "";

	/** SelectPropertyのソート */
	private Function<SelectProperty, Boolean> sortSelectValue = property -> false;

	/** 検索実行前Query処理 */
	private Function<Query, SearchQueryCsvContext> beforeSearch = query -> new SearchQueryCsvContext(query);

	/** 検索実行後Query処理 */
	private BiConsumer<Query, Entity> afterSearch = (query, entity) -> {};

	public boolean isWithMappedByReference() {
		return withMappedByReference;
	}

	public void setWithMappedByReference(boolean withMappedByReference) {
		this.withMappedByReference = withMappedByReference;
	}

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

	public boolean isWithReferenceVersion() {
		return withReferenceVersion;
	}

	public void setWithReferenceVersion(boolean withReferenceVersion) {
		this.withReferenceVersion = withReferenceVersion;
	}

	public boolean isWithBinary() {
		return withBinary;
	}

	public void setWithBinary(boolean withBinary) {
		this.withBinary = withBinary;
	}

	public String getExportBinaryDataDir() {
		return exportBinaryDataDir;
	}

	public void setExportBinaryDataDir(String exportBinaryDataDir) {
		this.exportBinaryDataDir = exportBinaryDataDir;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	/**
	 * 多重度複数の参照を含む検索時に一括でロードするかを返します。
	 *
	 * @return 多重度複数の参照を含む検索時に一括でロードするか
	 */
	public boolean isLoadOnceOfHasMultipleReferenceEntity() {
		return loadOnceOfHasMultipleReferenceEntity;
	}

	/**
	 * 多重度複数の参照を含む検索時に一括でロードするかを設定します。
	 *
	 * @param loadOnceOfHasMultipleReferenceEntity 多重度複数の参照を含む検索時に一括でロードするか
	 */
	public void setLoadOnceOfHasMultipleReferenceEntity(boolean loadOnceOfHasMultipleReferenceEntity) {
		this.loadOnceOfHasMultipleReferenceEntity = loadOnceOfHasMultipleReferenceEntity;
	}

	/**
	 * 多重度複数の参照を含む検索時のロード単位を返します。
	 *
	 * @return 多重度複数の参照を含む検索時のロード単位
	 */
	public int getLoadSizeOfHasMultipleReferenceEntity() {
		return loadSizeOfHasMultipleReferenceEntity;
	}

	/**
	 * 多重度複数の参照を含む検索時のロード単位を設定します。1以下は1件ずつロードします。
	 *
	 * @param loadSizeOfHasMultipleReferenceEntity 多重度複数の参照を含む検索時のロード単位
	 */
	public void setLoadSizeOfHasMultipleReferenceEntity(int loadSizeOfHasMultipleReferenceEntity) {
		this.loadSizeOfHasMultipleReferenceEntity = loadSizeOfHasMultipleReferenceEntity;
	}

	public List<String> getProperties() {
		return properties;
	}

	public void setProperties(List<String> properties) {
		this.properties = properties;
	}

	public Where getWhere() {
		return where;
	}

	public void setWhere(Where where) {
		this.where = where;
	}

	public OrderBy getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(OrderBy orderBy) {
		this.orderBy = orderBy;
	}

	public boolean isMustOrderByWithLimit() {
		return mustOrderByWithLimit;
	}

	public void setMustOrderByWithLimit(boolean mustOrderByWithLimit) {
		this.mustOrderByWithLimit = mustOrderByWithLimit;
	}

	public boolean isVersioned() {
		return versioned;
	}

	public void setVersioned(boolean versioned) {
		this.versioned = versioned;
	}

	public Function<PropertyDefinition, String> getColumnDisplayName() {
		return columnDisplayName;
	}

	public void setColumnDisplayName(Function<PropertyDefinition, String> columnDisplayName) {
		this.columnDisplayName = columnDisplayName;
	}

	public Function<SelectProperty, Boolean> getSortSelectValue() {
		return sortSelectValue;
	}

	public void setSortSelectValue(Function<SelectProperty, Boolean> sortSelectValue) {
		this.sortSelectValue = sortSelectValue;
	}

	public Function<Query, SearchQueryCsvContext> getBeforeSearch() {
		return beforeSearch;
	}

	public void setBeforeSearch(Function<Query, SearchQueryCsvContext> beforeSearch) {
		this.beforeSearch = beforeSearch;
	}

	public BiConsumer<Query, Entity> getAfterSearch() {
		return afterSearch;
	}

	public void setAfterSearch(BiConsumer<Query, Entity> afterSearch) {
		this.afterSearch = afterSearch;
	}

	public EntityWriteOption charset(String charset) {
		setCharset(charset);
		return this;
	}

	public EntityWriteOption quoteAll(boolean quoteAll) {
		setQuoteAll(quoteAll);
		return this;
	}

	public EntityWriteOption withReferenceVersion(boolean withReferenceVersion) {
		setWithReferenceVersion(withReferenceVersion);
		return this;
	}

	public EntityWriteOption withBinary(boolean withBinary) {
		setWithBinary(withBinary);
		return this;
	}

	public EntityWriteOption exportBinaryDataDir(String exportBinaryDataDir) {
		setExportBinaryDataDir(exportBinaryDataDir);
		return this;
	}

	public EntityWriteOption withMappedByReference(boolean withMappedByReference) {
		setWithMappedByReference(withMappedByReference);
		return this;
	}

	public EntityWriteOption limit(int limit) {
		setLimit(limit);
		return this;
	}

	/**
	 * 多重度複数の参照を含む検索時に一括でロードするかを設定します。
	 *
	 * @param loadOnceOfHasMultipleReferenceEntity 多重度複数の参照を含む検索時に一括でロードするか
	 * @return インスタンス
	 */
	public EntityWriteOption loadOnceOfHasMultipleReferenceEntity(boolean loadOnceOfHasMultipleReferenceEntity) {
		setLoadOnceOfHasMultipleReferenceEntity(loadOnceOfHasMultipleReferenceEntity);
		return this;
	}

	/**
	 * 多重度複数の参照を含む検索時のロード単位を設定します。1以下は1件ずつロードします。
	 *
	 * @param loadSizeOfHasMultipleReferenceEntity 多重度複数の参照を含む検索時のロード単位
	 * @return インスタンス
	 */
	public EntityWriteOption loadSizeOfHasMultipleReferenceEntity(int loadSizeOfHasMultipleReferenceEntity) {
		setLoadSizeOfHasMultipleReferenceEntity(loadSizeOfHasMultipleReferenceEntity);
		return this;
	}

	public EntityWriteOption properties(List<String> properties) {
		setProperties(properties);
		return this;
	}

	public EntityWriteOption where(Where where) {
		setWhere(where);
		return this;
	}

	public EntityWriteOption orderBy(OrderBy orderBy) {
		setOrderBy(orderBy);
		return this;
	}

	public EntityWriteOption mustOrderByWithLimit(boolean mustOrderByWithLimit) {
		setMustOrderByWithLimit(mustOrderByWithLimit);
		return this;
	}

	public EntityWriteOption versioned(boolean versioned) {
		setVersioned(versioned);
		return this;
	}

	public EntityWriteOption columnDisplayName(Function<PropertyDefinition, String> columnDisplayName) {
		setColumnDisplayName(columnDisplayName);
		return this;
	}

	public EntityWriteOption sortSelectValue(Function<SelectProperty, Boolean> sortSelectValue) {
		setSortSelectValue(sortSelectValue);
		return this;
	}

	public EntityWriteOption beforeSearch(Function<Query, SearchQueryCsvContext> beforeSearch) {
		setBeforeSearch(beforeSearch);
		return this;
	}

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
