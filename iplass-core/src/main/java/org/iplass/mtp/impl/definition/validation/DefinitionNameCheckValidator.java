/*
 * Copyright (C) 2025 DENTSU SOKEN INC. All Rights Reserved.
 */

package org.iplass.mtp.impl.definition.validation;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

import org.iplass.mtp.impl.util.CoreResourceBundleUtil;
import org.iplass.mtp.util.StringUtil;

/**
 * メタデータ定義名チェックValidatorクラス
 */
public abstract class DefinitionNameCheckValidator {

	private Pattern definitionNamePattern;

	public DefinitionNameCheckValidator(Pattern definitionNamePattern) {
		this.definitionNamePattern = definitionNamePattern;
	}

	/**
	 * エラーメッセージを返却
	 * 
	 * @param definitionName メタデータ定義名
	 * @return エラーメッセージ
	 */
	protected abstract String getErrorMessage(String definitionName);

	/**
	 * メタデータ定義名チェック
	 * 
	 * @param definitionName メタデータ定義名
	 * @return チェックOKの場合は空、チェックNGの場合はチェックエラーメッセージ
	 */
	public Optional<String> validate(String definitionName) {
		// 必要な情報がなかったらチェックしない（チェックOKとする）
		String errorMessage = this.getErrorMessage(definitionName);
		if (Objects.isNull(this.definitionNamePattern) ||
				StringUtil.isEmpty(errorMessage) ||
				StringUtil.isEmpty(definitionName)) {
			return Optional.empty();
		}

		return this.definitionNamePattern.matcher(definitionName).matches() ? Optional.empty() : Optional.of(errorMessage);
	}

	/**
	 * メッセージ取得
	 * 
	 * @param key メッセージキー
	 * @param args メッセージ引数
	 * @return メッセージ
	 */
	protected String getMessage(String key, Object... args) {
		return CoreResourceBundleUtil.resourceString(key, args);
	}
}
