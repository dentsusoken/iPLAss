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

	private String pathPrefix;
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
	public static DefinitionNameChecker getDefaultDefinitionNameChecker(String pathPrefix) {
		return new DefinitionNameChecker(pathPrefix, DEFAULT_PATTERN, DEFAULT_MESSAGE_KEY);
	}

	/**
	 * コンストラクター
	 * 
	 * <p>
	 * メタデータ定義名のパターン（正規表現）を指定する場合に利用する
	 * </p>
	 * 
	 * @param pathPrefix パスプレフィックス
	 * @param definitionNamePattern 定義名のパターン（正規表現）
	 * @param messageKey エラーメッセージキー
	 */
	public DefinitionNameChecker(String pathPrefix, String definitionNamePattern, String messageKey) {
		this.pathPrefix = pathPrefix;

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
	 * メタデータパスチェック
	 * 
	 * @param path メタデータパス
	 * @return チェック結果
	 */
	public DefinitionNameCheckResult checkPathPrefix(String path) {
		if (StringUtil.isNotEmpty(path) && !path.startsWith(this.pathPrefix)) {
			return DefinitionNameCheckResult
					.createErrorResult(CoreResourceBundleUtil.resourceString("impl.definition.DefinitionNameChecker.invalidPath"));
		}

		return DefinitionNameCheckResult.createSuccessResult();
	}

	/**
	 * メタデータ定義名チェック
	 * 
	 * @param definitionName メタデータ定義名
	 * @return チェック結果
	 */
	public DefinitionNameCheckResult checkDefinitionName(String definitionName) {
		if (StringUtil.isNotEmpty(definitionName) && !this.definitionNamePattern.matcher(definitionName).matches()) {
			return DefinitionNameCheckResult.createErrorResult(this.getErrorMessage(this.messageKey, definitionName));
		}

		return DefinitionNameCheckResult.createSuccessResult();
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
		String errorMessage = CoreResourceBundleUtil.resourceString(messageKey, definitionName);
		if (StringUtil.isNotEmpty(errorMessage)) {
			return errorMessage;
		}

		// メッセージが取得できなかったら固定のエラーメッセージを使う
		return CoreResourceBundleUtil.resourceString("impl.definition.DefinitionNameChecker.invalidDefault", definitionName);
	}
}
