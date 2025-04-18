/*
 * Copyright (C) 2025 DENTSU SOKEN INC. All Rights Reserved.
 */

package org.iplass.mtp.impl.definition.validation;

/**
 * デフォルトメタデータ定義名チェックValidator
 * 
 * <p>
 * メタデータ定義名の制限で一番ゆるいメタデータチェック用
 * </p>
 */
public class DefaultDefinitionNameCheckValidator extends BaseDefinitionNameCheckValidator {

	public DefaultDefinitionNameCheckValidator() {
		// TODO 暫定
		super(DefinitionNameCheckPattern.PATH_SLASH_NAME_PERIOD);
	}
}
