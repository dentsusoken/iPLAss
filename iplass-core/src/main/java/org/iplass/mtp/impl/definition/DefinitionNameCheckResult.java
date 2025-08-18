/*
 * Copyright (C) 2025 DENTSU SOKEN INC. All Rights Reserved.
 */

package org.iplass.mtp.impl.definition;

/**
 * メタデータ定義名チェック結果
 */
public class DefinitionNameCheckResult {

	private boolean error;
	private String errorMessage;

	/**
	 * コンストラクター
	 * 
	 * @param error エラーかどうか
	 * @param errorMessage エラーメッセージ
	 */
	DefinitionNameCheckResult(boolean error, String errorMessage) {
		this.error = error;
		this.errorMessage = errorMessage;
	}

	/**
	 * チェックエラーがあるかどうか
	 * 
	 * @return チェックエラーがあるかどうか
	 */
	public boolean hasError() {
		return this.error;
	}

	/**
	 * チェックエラーメッセージを返却
	 * 
	 * @return チェックエラーメッセージ
	 */
	public String getErrorMessage() {
		return this.errorMessage;
	}
}
