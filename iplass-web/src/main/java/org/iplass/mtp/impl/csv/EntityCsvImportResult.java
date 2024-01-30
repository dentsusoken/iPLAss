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

package org.iplass.mtp.impl.csv;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * EntityのCSVインポート結果
 */
public class EntityCsvImportResult implements Serializable {

	private static final long serialVersionUID = 8836699835950348705L;

	/** エラー有無 */
	private boolean isError = false;
	/** メッセージ */
	private List<String> messages;

	/** 登録件数 */
	private long insertCount = 0;
	/** 更新件数 */
	private long updateCount = 0;
	/** 削除件数 */
	private long deleteCount = 0;
	/** エラー件数 */
	private long errorCount = 0;
	/** マージ件数 */
	private long mergeCount = 0;

	/**
	 * コンストラクタ
	 */
	public EntityCsvImportResult() {
	}

	/**
	 * エラー有無を設定
	 * @param isError エラー有無
	 */
	public void setError(boolean isError) {
		this.isError = isError;
	}

	/**
	 * エラー有無を取得
	 * @return エラー有無
	 */
	public boolean isError() {
		return isError;
	}

	/**
	 * メッセージを取得
	 * @return メッセージ
	 */
	public List<String> getMessages() {
		return messages;
	}

	/**
	 * メッセージを設定
	 * @param messages メッセージ
	 */
	public void setMessages(List<String> messages) {
		this.messages = messages;
	}

	/**
	 * メッセージを追加
	 * @param message メッセージ
	 */
	public void addMessages(String message) {
		if (messages == null) {
			messages = new ArrayList<>();
		}
		messages.add(message);
	}

	/**
	 * メッセージをクリア
	 */
	public void clearMessages() {
		this.messages = null;
	}

	/**
	 * 登録件数を取得
	 * @return 登録件数
	 */
	public long getInsertCount() {
		return insertCount;
	}

	/**
	 * 登録件数を設定
	 * @param insertCount 登録件数
	 */
	public void setInsertCount(long insertCount) {
		this.insertCount = insertCount;
	}

	/**
	 * 更新件数を取得
	 * @return 更新件数
	 */
	public long getUpdateCount() {
		return updateCount;
	}

	/**
	 * 更新件数を設定
	 * @param updateCount 更新件数
	 */
	public void setUpdateCount(long updateCount) {
		this.updateCount = updateCount;
	}

	/**
	 * 削除件数を取得
	 * @return 削除件数
	 */
	public long getDeleteCount() {
		return deleteCount;
	}

	/**
	 * 削除件数を設定
	 * @param deleteCount 削除件数
	 */
	public void setDeleteCount(long deleteCount) {
		this.deleteCount = deleteCount;
	}

	/**
	 * エラー件数を取得
	 * @return エラー件数
	 */
	public long getErrorCount() {
		return errorCount;
	}

	/**
	 * エラー件数を設定
	 * @param errorCount エラー件数
	 */
	public void setErrorCount(long errorCount) {
		this.errorCount = errorCount;
	}

	/**
	 * マージ件数を取得
	 * @return マージ件数
	 */
	public long getMergeCount() {
		return mergeCount;
	}

	/**
	 * マージ件数を設定
	 * @param mergeCount マージ件数
	 */
	public void setMergeCount(long mergeCount) {
		this.mergeCount = mergeCount;
	}

	/**
	 * 登録件数加算
	 */
	public void inserted() {
		insertCount++;
	}

	/**
	 * 更新件数加算
	 */
	public void updated() {
		updateCount++;
	}

	/**
	 * 削除件数加算
	 */
	public void deleted() {
		deleteCount++;
	}

	/**
	 * エラー件数加算
	 */
	public void errored() {
		errorCount++;
	}

	/**
	 * マージ件数加算
	 */
	public void merged() {
		mergeCount++;
	}

}
