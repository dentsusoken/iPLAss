/*
 * Copyright (C) 2025 DENTSU SOKEN INC. All Rights Reserved.
 */

package org.iplass.mtp.impl.metadata.validation;

import java.util.Optional;

/**
 * メタデータ定義名チェックしないValidatorクラス
 * 
 * <p>
 * メタデータ定義名チェックしない場合に利用
 * </p>
 * 
 */
public class NoCheckValidator extends DefinitionNameCheckValidator {

	@Override
	public String getErrorMessage(String definitionName) {
		return null;
	}

	@Override
	public String getRegularExpression() {
		return null;
	}

	@Override
	public Optional<String> validate(String definitionName) {
		return Optional.empty();
	}
}
