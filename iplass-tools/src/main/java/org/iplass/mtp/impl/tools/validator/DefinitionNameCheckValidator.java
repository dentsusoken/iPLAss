/*
 * Copyright (C) 2025 DENTSU SOKEN INC. All Rights Reserved.
 */

package org.iplass.mtp.impl.tools.validator;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.iplass.mtp.impl.metadata.RootMetaData;
import org.iplass.mtp.impl.tools.ToolsResourceBundleUtil;
import org.iplass.mtp.util.StringUtil;

/**
 * メタデータ定義名チェックValidatorクラス
 */
public abstract class DefinitionNameCheckValidator<T extends RootMetaData> {

	/**
	 * エラーメッセージを返却
	 * 
	 * @param meta メタデータ
	 * @return エラーメッセージ
	 */
	public abstract String getErrorMessage(T meta);

	/**
	 * 定義名に許可する正規表現を返却
	 * 
	 * @return 定義名に許可する正規表現
	 */
	public abstract String getRegularExpression();

	/**
	 * メタデータ定義名チェック
	 * 
	 * @param meta メタデータ
	 * @return チェックOKの場合は空、チェックNGの場合はチェックエラーメッセージ
	 */
	public Optional<String> validate(T meta) {
		// 必要な情報がなかったらチェックしない（チェックOKとする）
		if (StringUtil.isEmpty(this.getErrorMessage(meta)) ||
				StringUtil.isEmpty(this.getRegularExpression()) ||
				Objects.isNull(meta)) {
			return Optional.empty();
		}

		return this.check(meta.getName(), this.getRegularExpression()) ? Optional.empty() : Optional.of(this.getErrorMessage(meta));
	}

	protected boolean check(String definitionName, String regularExpression) {
		Pattern p = Pattern.compile(regularExpression);
		Matcher m = p.matcher(definitionName);
		return m.find();
	}

	protected String getMessage(String key, Object... args) {
		return ToolsResourceBundleUtil.resourceString(key, args);
	}
}
