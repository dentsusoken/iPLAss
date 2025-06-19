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

package org.iplass.mtp.impl.fileport;

import java.io.Serializable;
import java.util.Set;

import org.iplass.mtp.entity.TargetVersion;

public class EntityFileUploadOption implements Serializable {

	/**
	 * シリアルバージョンUID
	 */
	private static final long serialVersionUID = 3380545639349852699L;

	/** ファイル種類 */
	private EntityFileType entityFileType = EntityFileType.CSV;

	/** ユニークキー */
	private String uniqueKey;

	/** Insertを許可しない */
	private boolean denyInsert = false;

	/** Updateを許可しない */
	private boolean denyUpdate = false;

	/** Deleteを許可しない */
	private boolean denyDelete = false;

	/** Insert対象プロパティ */
	private Set<String> insertProperties;

	/** Update対象プロパティ */
	private Set<String> updateProperties;

	/** トランザクション制御 */
	private TransactionType transactionType = TransactionType.ONCE;

	/** トランザクションを分割する場合のコミット件数 */
	private int commitLimit = 0;

	/** Referenceプロパティの場合はバージョンも指定 */
	private boolean withReferenceVersion = true;

	/** 特定Versionのみを削除する */
	private boolean deleteSpecificVersion = true;

	/** バージョン管理Entity以外の場合の更新時のデフォルトTargetVersion */
	private TargetVersion updateTargetVersionForNoneVersionedEntity = null;

	/** CsvUploadInterrupterクラス名 */
	private String interrupterClassName;

	/**
	 * ファイル種類を返します。
	 * @return ファイル種類
	 */
	public EntityFileType getEntityFileType() {
		return entityFileType;
	}

	/**
	 * ファイル種類を設定します。
	 * @param entityFileType ファイル種類
	 */
	public void setEntityFileType(EntityFileType entityFileType) {
		this.entityFileType = entityFileType;
	}

	/**
	 * ファイル種類を設定します。
	 * @param entityFileType ファイル種類
	 */
	public EntityFileUploadOption entityFileType(EntityFileType entityFileType) {
		this.entityFileType = entityFileType;
		return this;
	}

	/**
	 * ユニークキーを返します。
	 * @return ユニークキー
	 */
	public String getUniqueKey() {
		return uniqueKey;
	}

	/**
	 * ユニークキーを設定します。
	 * @param uniqueKey ユニークキー
	 */
	public void setUniqueKey(String uniqueKey) {
		this.uniqueKey = uniqueKey;
	}

	/**
	 * ユニークキーを設定します。
	 * @param uniqueKey ユニークキー
	 */
	public EntityFileUploadOption uniqueKey(String uniqueKey) {
		this.uniqueKey = uniqueKey;
		return this;
	}

	/**
	 * Insertを許可しないかを返します。
	 * @return Insertを許可しないか
	 */
	public boolean isDenyInsert() {
		return denyInsert;
	}

	/**
	 * Insertを許可しないかを設定します。
	 * @param denyInsert Insertを許可しないか
	 */
	public void setDenyInsert(boolean denyInsert) {
		this.denyInsert = denyInsert;
	}

	/**
	 * Insertを許可しないかを設定します。
	 * @param denyInsert Insertを許可しないか
	 */
	public EntityFileUploadOption denyInsert(boolean denyInsert) {
		this.denyInsert = denyInsert;
		return this;
	}

	/**
	 * Updateを許可しないかを返します。
	 * @return Updateを許可しないか
	 */
	public boolean isDenyUpdate() {
		return denyUpdate;
	}

	/**
	 * Updateを許可しないかを設定します。
	 * @param denyUpdate Updateを許可しないか
	 */
	public void setDenyUpdate(boolean denyUpdate) {
		this.denyUpdate = denyUpdate;
	}

	/**
	 * Updateを許可しないかを設定します。
	 * @param denyUpdate Updateを許可しないか
	 */
	public EntityFileUploadOption denyUpdate(boolean denyUpdate) {
		this.denyUpdate = denyUpdate;
		return this;
	}

	/**
	 * Deleteを許可しないかを返します。
	 * @return Deleteを許可しないか
	 */
	public boolean isDenyDelete() {
		return denyDelete;
	}

	/**
	 * Deleteを許可しないかを設定します。
	 * @param denyDelete Deleteを許可しないか
	 */
	public void setDenyDelete(boolean denyDelete) {
		this.denyDelete = denyDelete;
	}

	/**
	 * Deleteを許可しないかを設定します。
	 * @param denyDelete Deleteを許可しないか
	 */
	public EntityFileUploadOption denyDelete(boolean denyDelete) {
		this.denyDelete = denyDelete;
		return this;
	}

	/**
	 * Insert対象プロパティを返します。
	 * @return Insert対象プロパティ
	 */
	public Set<String> getInsertProperties() {
		return insertProperties;
	}

	/**
	 * Insert対象プロパティを設定します。
	 * @param insertProperties Insert対象プロパティ
	 */
	public void setInsertProperties(Set<String> insertProperties) {
		this.insertProperties = insertProperties;
	}

	/**
	 * Insert対象プロパティを設定します。
	 * @param insertProperties Insert対象プロパティ
	 */
	public EntityFileUploadOption insertProperties(Set<String> insertProperties) {
		this.insertProperties = insertProperties;
		return this;
	}

	/**
	 * Update対象プロパティを返します。
	 * @return Update対象プロパティ
	 */
	public Set<String> getUpdateProperties() {
		return updateProperties;
	}

	/**
	 * Update対象プロパティを設定します。
	 * @param updateProperties Update対象プロパティ
	 */
	public void setUpdateProperties(Set<String> updateProperties) {
		this.updateProperties = updateProperties;
	}

	/**
	 * Update対象プロパティを設定します。
	 * @param updateProperties Update対象プロパティ
	 */
	public EntityFileUploadOption updateProperties(Set<String> updateProperties) {
		this.updateProperties = updateProperties;
		return this;
	}

	/**
	 * トランザクション制御を返します。
	 * @return トランザクション制御
	 */
	public TransactionType getTransactionType() {
		return transactionType;
	}

	/**
	 * トランザクション制御を設定します。
	 * @param transactionType トランザクション制御
	 */
	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}

	/**
	 * トランザクション制御を設定します。
	 * @param transactionType トランザクション制御
	 */
	public EntityFileUploadOption transactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
		return this;
	}

	/**
	 * トランザクションを分割する場合のコミット件数を返します。
	 * @return トランザクションを分割する場合のコミット件数
	 */
	public int getCommitLimit() {
		return commitLimit;
	}

	/**
	 * トランザクションを分割する場合のコミット件数を設定します。
	 * @param commitLimit トランザクションを分割する場合のコミット件数
	 */
	public void setCommitLimit(int commitLimit) {
		this.commitLimit = commitLimit;
	}

	/**
	 * トランザクションを分割する場合のコミット件数を設定します。
	 * @param commitLimit トランザクションを分割する場合のコミット件数
	 */
	public EntityFileUploadOption commitLimit(int commitLimit) {
		this.commitLimit = commitLimit;
		return this;
	}

	/**
	 * Referenceプロパティの場合はバージョンも指定するかを返します。
	 * @return Referenceプロパティの場合はバージョンも指定するか
	 */
	public boolean isWithReferenceVersion() {
		return withReferenceVersion;
	}

	/**
	 * Referenceプロパティの場合はバージョンも指定するかを設定します。
	 * @param withReferenceVersion Referenceプロパティの場合はバージョンも指定するか
	 */
	public void setWithReferenceVersion(boolean withReferenceVersion) {
		this.withReferenceVersion = withReferenceVersion;
	}

	/**
	 * Referenceプロパティの場合はバージョンも指定するかを設定します。
	 * @param withReferenceVersion Referenceプロパティの場合はバージョンも指定するか
	 */
	public EntityFileUploadOption withReferenceVersion(boolean withReferenceVersion) {
		this.withReferenceVersion = withReferenceVersion;
		return this;
	}

	/**
	 * 特定versionのみを削除するかを返します。
	 * @return 特定versionのみを削除するか
	 */
	public boolean isDeleteSpecificVersion() {
		return deleteSpecificVersion;
	}

	/**
	 * 特定versionのみを削除するかを設定します。
	 * @param deleteSpecificVersion 特定versionのみを削除するか
	 */
	public void setDeleteSpecificVersion(boolean deleteSpecificVersion) {
		this.deleteSpecificVersion = deleteSpecificVersion;
	}

	/**
	 * 特定versionのみを削除するかを設定します。
	 * @param deleteSpecificVersion 特定versionのみを削除するか
	 */
	public EntityFileUploadOption deleteSpecificVersion(boolean deleteSpecificVersion) {
		this.deleteSpecificVersion = deleteSpecificVersion;
		return this;
	}

	/**
	 * バージョン管理Entity以外の場合の更新時のデフォルトTargetVersionを返します。
	 * @return バージョン管理Entity以外の場合の更新時のデフォルトTargetVersion
	 */
	public TargetVersion getUpdateTargetVersionForNoneVersionedEntity() {
		return updateTargetVersionForNoneVersionedEntity;
	}

	/**
	 * バージョン管理Entity以外の場合の更新時のデフォルトTargetVersionを設定します。
	 * @param updateTargetVersionForNoneVersionedEntity バージョン管理Entity以外の場合の更新時のデフォルトTargetVersion
	 */
	public void setUpdateTargetVersionForNoneVersionedEntity(TargetVersion updateTargetVersionForNoneVersionedEntity) {
		this.updateTargetVersionForNoneVersionedEntity = updateTargetVersionForNoneVersionedEntity;
	}

	/**
	 * バージョン管理Entity以外の場合の更新時のデフォルトTargetVersionを設定します。
	 * @param updateTargetVersionForNoneVersionedEntity バージョン管理Entity以外の場合の更新時のデフォルトTargetVersion
	 */
	public EntityFileUploadOption updateTargetVersionForNoneVersionedEntity(TargetVersion updateTargetVersionForNoneVersionedEntity) {
		this.updateTargetVersionForNoneVersionedEntity = updateTargetVersionForNoneVersionedEntity;
		return this;
	}

	/**
	 * CsvUploadInterrupterクラス名を返します。
	 * @return CsvUploadInterrupterクラス名
	 */
	public String getInterrupterClassName() {
		return interrupterClassName;
	}

	/**
	 * CsvUploadInterrupterクラス名を設定します。
	 * @param interrupterClassName CsvUploadInterrupterクラス名
	 */
	public void setInterrupterClassName(String interrupterClassName) {
		this.interrupterClassName = interrupterClassName;
	}

	/**
	 * CsvUploadInterrupterクラス名を設定します。
	 * @param interrupterClassName CsvUploadInterrupterクラス名
	 */
	public EntityFileUploadOption interrupterClassName(String interrupterClassName) {
		this.interrupterClassName = interrupterClassName;
		return this;
	}

}
