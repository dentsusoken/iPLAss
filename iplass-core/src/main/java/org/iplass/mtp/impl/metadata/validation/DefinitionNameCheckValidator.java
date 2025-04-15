/*
 * Copyright (C) 2025 DENTSU SOKEN INC. All Rights Reserved.
 */

package org.iplass.mtp.impl.metadata.validation;

import java.util.Optional;

import org.iplass.mtp.impl.util.CoreResourceBundleUtil;
import org.iplass.mtp.util.StringUtil;

/**
 * メタデータ定義名チェックValidatorクラス
 */
public abstract class DefinitionNameCheckValidator {

	/**
	 * エラーメッセージを返却
	 * 
	 * @param definitionName メタデータ定義名
	 * @return エラーメッセージ
	 */
	public abstract String getErrorMessage(String definitionName);

	/**
	 * 定義名に許可する正規表現を返却
	 * 
	 * @return 定義名に許可する正規表現
	 */
	public abstract String getRegularExpression();

	/**
	 * メタデータ定義名チェック
	 * 
	 * @param definitionName メタデータ定義名
	 * @return チェックOKの場合は空、チェックNGの場合はチェックエラーメッセージ
	 */
	public Optional<String> validate(String definitionName) {
		// 必要な情報がなかったらチェックしない（チェックOKとする）
		String errorMessage = this.getErrorMessage(definitionName);
		String regularExpression = this.getRegularExpression();
		if (StringUtil.isEmpty(errorMessage) ||
				StringUtil.isEmpty(regularExpression) ||
				StringUtil.isEmpty(definitionName)) {
			return Optional.empty();
		}

		return this.check(definitionName, regularExpression) ? Optional.empty() : Optional.of(errorMessage);
	}

	protected boolean check(String definitionName, String regularExpression) {
		// メタデータ定義名未指定ならチェックしない
		if (StringUtil.isEmpty(definitionName)) {
			return true;
		}

		return definitionName.matches(regularExpression);
	}

	protected String getMessage(String key, Object... args) {
		return CoreResourceBundleUtil.resourceString(key, args);
	}
}
