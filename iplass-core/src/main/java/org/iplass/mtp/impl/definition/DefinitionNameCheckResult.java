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
	 * エラーなしのメタデータ定義名チェック結果を生成
	 * 
	 * @return エラーなしのメタデータ定義名チェック結果
	 */
	public static DefinitionNameCheckResult createSuccessResult() {
		return new DefinitionNameCheckResult();
	}

	/**
	 * エラーありのメタデータ定義名チェック結果を生成
	 * 
	 * @param errorMessage エラーメッセージ
	 * @return エラーありのメタデータ定義名チェック結果
	 */
	public static DefinitionNameCheckResult createErrorResult(String errorMessage) {
		return new DefinitionNameCheckResult(errorMessage);
	}

	private DefinitionNameCheckResult() {
		this.error = false;
	}

	private DefinitionNameCheckResult(String errorMessage) {
		this.error = true;
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
