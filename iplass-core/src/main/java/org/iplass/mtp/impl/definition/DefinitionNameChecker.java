/*
 * Copyright (C) 2025 DENTSU SOKEN INC. All Rights Reserved.
 */

package org.iplass.mtp.impl.definition;

import java.util.Optional;
import java.util.regex.Pattern;

import org.iplass.mtp.impl.util.CoreResourceBundleUtil;
import org.iplass.mtp.util.StringUtil;

/**
 * メタデータ定義名チェッククラス
 */
public class DefinitionNameChecker {

	private static final String DEFAULT_PATTERN = "^[0-9a-zA-Z_][0-9a-zA-Z_-]*(/[0-9a-zA-Z_-]+)*(\\.[0-9a-zA-Z_-]+)*$";
	private static final String DEFAULT_MESSAGE_KEY = "impl.definition.DefinitionNameChecker.invalidPattern";

	private Pattern definitionNamePattern;
	private String messageKey;

	/**
	 * デフォルトの定義名Checkerを返却
	 * 
	 * <p>
	 * メタデータ定義名が「パスにスラッシュを利用、名前にピリオド含む」になってるかチェックするChecker
	 * </p>
	 * 
	 * @return デフォルトの定義名Checker
	 */
	public static DefinitionNameChecker getDefaultDefinitionNameChecker() {
		return new DefinitionNameChecker(DEFAULT_PATTERN, DEFAULT_MESSAGE_KEY);
	}

	/**
	 * コンストラクター
	 * 
	 * <p>
	 * メタデータ定義名のパターン（正規表現）を指定する場合に利用する
	 * </p>
	 * 
	 * @param definitionNamePattern 定義名のパターン（正規表現）
	 * @param messageKey エラーメッセージキー
	 */
	public DefinitionNameChecker(String definitionNamePattern, String messageKey) {
		// パターンかメッセージキーのどちらかが未指定だった場合はデフォルトの定義名チェックにする
		if (StringUtil.isEmpty(definitionNamePattern) || StringUtil.isEmpty(messageKey)) {
			this.definitionNamePattern = Pattern.compile(DEFAULT_PATTERN);
			this.messageKey = DEFAULT_MESSAGE_KEY;
			return;
		}

		this.definitionNamePattern = Pattern.compile(definitionNamePattern);
		this.messageKey = messageKey;
	}

	/**
	 * エラーメッセージを返却
	 * 
	 * <p>
	 * メッセージの取得元を変更する場合はオーバーライドする
	 * </p>
	 * 
	 * @param messageKey メッセージキー
	 * @param definitionName メタデータ定義名
	 * @return エラーメッセージ
	 */
	protected String getErrorMessage(String messageKey, String definitionName) {
		return CoreResourceBundleUtil.resourceString(messageKey, definitionName);
	}

	/**
	 * メタデータ定義名チェック
	 * 
	 * @param definitionName メタデータ定義名
	 * @return チェックOKの場合は空、チェックNGの場合はチェックエラーメッセージ
	 */
	public Optional<String> check(String definitionName) {
		// メタデータ定義名未指定ならチェックしない（チェックOKとする）
		if (StringUtil.isEmpty(definitionName)) {
			return Optional.empty();
		}

		String errorMessage = this.getErrorMessage(this.messageKey, definitionName);
		return this.definitionNamePattern.matcher(definitionName).matches() ? Optional.empty() : Optional.of(errorMessage);
	}

}
