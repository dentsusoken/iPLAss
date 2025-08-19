/*
 * Copyright (C) 2025 DENTSU SOKEN INC. All Rights Reserved.
 */

package org.iplass.mtp.impl.definition;

import java.util.regex.Pattern;

import org.iplass.mtp.impl.util.CoreResourceBundleUtil;
import org.iplass.mtp.util.StringUtil;

/**
 * メタデータ定義名チェッククラス
 * 
 * <p>
 * 以下のチェックをする
 * <ul>
 * <li>メタデータのパスがメタデータ定義のパスに一致するかどうか</li>
 * <li>メタデータ定義名に指定できない文字列が含まれていないかどうか</li>
 * </ul>
 * </p>
 */
public class DefinitionNameChecker {

	private static final String DEFAULT_PATTERN = "^[0-9a-zA-Z_][0-9a-zA-Z_-]*(/[0-9a-zA-Z_-]+)*(\\.[0-9a-zA-Z_-]+)*$";
	private static final String DEFAULT_MESSAGE_KEY = "impl.definition.DefinitionNameChecker.invalidPattern";

	private static final String PATH_SLASH_PATTERN = "^[0-9a-zA-Z_][0-9a-zA-Z_-]*(/[0-9a-zA-Z_-]+)*$";
	private static final String PATH_SLASH_MESSAGE_KEY = "impl.definition.DefinitionNameChecker.NamePathSlash.invalidPattern";

	public static final String PATH_PERIOD_PATTERN = "^[0-9a-zA-Z_][0-9a-zA-Z_-]*(\\.[0-9a-zA-Z_-]+)*$";

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
	 * パスに【/】許可（名前に【.】は許可しない）する定義名Checkerを返却
	 * 
	 * <p>
	 * メタデータ定義名が「パスにスラッシュを利用、名前にピリオド含めない」になってるかチェックするChecker
	 * </p>
	 * 
	 * @return パスに【/】許可（名前に【.】は許可しない）する定義名Checker
	 */
	public static DefinitionNameChecker getPathSlashDefinitionNameChecker() {
		return new DefinitionNameChecker(PATH_SLASH_PATTERN, PATH_SLASH_MESSAGE_KEY);
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
		this.definitionNamePattern = Pattern.compile(this.getOrDefault(definitionNamePattern, DEFAULT_PATTERN));
		this.messageKey = this.getOrDefault(messageKey, DEFAULT_MESSAGE_KEY);
	}

	private String getOrDefault(String value, String defaultValue) {
		return StringUtil.isEmpty(value) ? defaultValue : value;
	}

	/**
	 * メタデータ定義名チェック
	 * 
	 * @param definitionName メタデータ定義名
	 * @return チェック結果
	 */
	public DefinitionNameCheckResult check(String definitionName) {
		if (StringUtil.isNotEmpty(definitionName) && !this.definitionNamePattern.matcher(definitionName).matches()) {
			return new DefinitionNameCheckResult(true, this.getErrorMessage(definitionName));
		}

		return new DefinitionNameCheckResult(false, null);
	}

	protected String getMessageKey() {
		return this.messageKey;
	}

	/**
	 * エラーメッセージを返却
	 * 
	 * <p>
	 * メッセージの取得元を変更する場合はオーバーライドする
	 * </p>
	 * 
	 * @param definitionName メタデータ定義名
	 * @return エラーメッセージ
	 */
	protected String getErrorMessage(String definitionName) {
		return CoreResourceBundleUtil.resourceString(this.messageKey, definitionName);
	}
}
