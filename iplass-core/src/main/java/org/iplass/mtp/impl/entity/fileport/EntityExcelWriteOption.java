/*
 * Copyright (C) 2024 DENTSU SOKEN INC. All Rights Reserved.
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

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.properties.SelectProperty;
import org.iplass.mtp.entity.query.OrderBy;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.Where;
import org.iplass.mtp.impl.entity.fileport.EntityCsvWriteOption.SearchQueryCsvContext;

public class EntityExcelWriteOption extends ParseOption {

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

	/** ダウンロード時にLimitが指定されている場合にOrderByを必ず指定する。SQLServer対応。 */
	private boolean mustOrderByWithLimit;

	/** 列名の出力文字列を返す関数 */
	private Function<PropertyDefinition, String> columnName = property -> property.getName();

	/** 多重度複数プロパティの列名の出力文字列を返す関数 */
	private BiFunction<PropertyDefinition, Integer, String> multipleColumnName
			= (property, index) -> property.getName() + "[" + index + "]";

	/** SelectPropertyのソートをするかを返す関数 */
	private Function<SelectProperty, Boolean> sortSelectValue = property -> false;

	/** 検索実行前Query処理 */
	private Function<Query, SearchQueryCsvContext> beforeSearch = query -> new SearchQueryCsvContext(query);

	/** 検索実行後Query処理 */
	private BiConsumer<Query, Entity> afterSearch = (query, entity) -> {};

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
	public EntityExcelWriteOption versioned(boolean versioned) {
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
	public EntityExcelWriteOption withReferenceVersion(boolean withReferenceVersion) {
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
	public EntityExcelWriteOption withBinary(boolean withBinary) {
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
	public EntityExcelWriteOption exportBinaryDataDir(String exportBinaryDataDir) {
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
	public EntityExcelWriteOption withMappedByReference(boolean withMappedByReference) {
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
	public EntityExcelWriteOption properties(List<String> properties) {
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
	public EntityExcelWriteOption where(Where where) {
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
	public EntityExcelWriteOption orderBy(OrderBy orderBy) {
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
	public EntityExcelWriteOption limit(int limit) {
		setLimit(limit);
		return this;
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
	 * 多重度複数の参照を含む検索時に一括でロードするかを設定します。
	 *
	 * @param loadOnceOfHasMultipleReferenceEntity 多重度複数の参照を含む検索時に一括でロードするか
	 * @return インスタンス
	 */
	public EntityExcelWriteOption loadOnceOfHasMultipleReferenceEntity(boolean loadOnceOfHasMultipleReferenceEntity) {
		setLoadOnceOfHasMultipleReferenceEntity(loadOnceOfHasMultipleReferenceEntity);
		return this;
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

	/**
	 * 多重度複数の参照を含む検索時のロード単位を設定します。1以下は1件ずつロードします。
	 *
	 * @param loadSizeOfHasMultipleReferenceEntity 多重度複数の参照を含む検索時のロード単位
	 * @return インスタンス
	 */
	public EntityExcelWriteOption loadSizeOfHasMultipleReferenceEntity(int loadSizeOfHasMultipleReferenceEntity) {
		setLoadSizeOfHasMultipleReferenceEntity(loadSizeOfHasMultipleReferenceEntity);
		return this;
	}

	/**
	 * ダウンロード時にLimitが指定されている場合にOrderByを必ず指定するかを返します。
	 *
	 * @return ダウンロード時にLimitが指定されている場合にOrderByを必ず指定するか
	 */
	public boolean isMustOrderByWithLimit() {
		return mustOrderByWithLimit;
	}

	/**
	 * ダウンロード時にLimitが指定されている場合にOrderByを必ず指定するかを設定します。
	 *
	 * @param mustOrderByWithLimit ダウンロード時にLimitが指定されている場合にOrderByを必ず指定するか
	 */
	public void setMustOrderByWithLimit(boolean mustOrderByWithLimit) {
		this.mustOrderByWithLimit = mustOrderByWithLimit;
	}

	/**
	 * ダウンロード時にLimitが指定されている場合にOrderByを必ず指定するかを設定します。
	 *
	 * @param mustOrderByWithLimit ダウンロード時にLimitが指定されている場合にOrderByを必ず指定するか
	 * @return インスタンス
	 */
	public EntityExcelWriteOption mustOrderByWithLimit(boolean mustOrderByWithLimit) {
		setMustOrderByWithLimit(mustOrderByWithLimit);
		return this;
	}

	/**
	 *  列の出力文字列を返す関数を返します。
	 *
	 * @return 列の出力文字列を返す関数
	 */
	public Function<PropertyDefinition, String> getColumnName() {
		return columnName;
	}

	/**
	 * 列の出力文字列を返す関数を設定します。
	 *
	 * @param columnName 列の出力文字列を返す関数
	 */
	public void setColumnName(Function<PropertyDefinition, String> columnName) {
		this.columnName = columnName;
	}

	/**
	 * 列の出力文字列を返す関数を設定します。
	 *
	 * @param columnName 列の出力文字列を返す関数
	 * @return インスタンス
	 */
	public EntityExcelWriteOption columnName(Function<PropertyDefinition, String> columnName) {
		setColumnName(columnName);
		return this;
	}

	/**
	 *  多重度複数プロパティの列の出力文字列を返す関数を返します。
	 *
	 * @return 多重度複数プロパティの列の出力文字列を返す関数
	 */
	public BiFunction<PropertyDefinition, Integer, String> getMultipleColumnName() {
		return multipleColumnName;
	}

	/**
	 * 多重度複数プロパティの列の出力文字列を返す関数を設定します。
	 *
	 * @param multipleColumnName 多重度複数プロパティの列の出力文字列を返す関数
	 */
	public void setMultipleColumnName(BiFunction<PropertyDefinition, Integer, String> multipleColumnName) {
		this.multipleColumnName = multipleColumnName;
	}

	/**
	 * 多重度複数プロパティの列の出力文字列を返す関数を設定します。
	 *
	 * @param multipleColumnName 多重度複数プロパティの列の出力文字列を返す関数
	 * @return インスタンス
	 */
	public EntityExcelWriteOption multipleColumnName(BiFunction<PropertyDefinition, Integer, String> multipleColumnName) {
		setMultipleColumnName(multipleColumnName);
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
	public EntityExcelWriteOption sortSelectValue(Function<SelectProperty, Boolean> sortSelectValue) {
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
	public EntityExcelWriteOption beforeSearch(Function<Query, SearchQueryCsvContext> beforeSearch) {
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
	public EntityExcelWriteOption afterSearch(BiConsumer<Query, Entity> afterSearch) {
		setAfterSearch(afterSearch);
		return this;
	}

	@Override
	public EntityExcelWriteOption dateFormat(String dateFormat) {
		setDateFormat(dateFormat);
		return this;
	}

	@Override
	public EntityExcelWriteOption datetimeSecFormat(String datetimeSecFormat) {
		setDatetimeSecFormat(datetimeSecFormat);
		return this;
	}

	@Override
	public EntityExcelWriteOption timeSecFormat(String timeSecFormat) {
		setTimeSecFormat(timeSecFormat);
		return this;
	}

}
