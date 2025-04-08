/*
 * Copyright (C) 2025 DENTSU SOKEN INC. All Rights Reserved.
 */

package org.iplass.mtp.impl.tools.validator;

import java.util.Optional;

import org.iplass.mtp.impl.metadata.RootMetaData;

/**
 * メタデータ定義名チェックしないValidatorクラス
 * 
 * <p>
 * メタデータ定義名チェックしない場合に利用
 * </p>
 * 
 */
public class NoCheckValidator extends DefinitionNameCheckValidator<RootMetaData> {

	@Override
	public String getErrorMessage(RootMetaData meta) {
		return null;
	}

	@Override
	public String getRegularExpression() {
		return null;
	}

	@Override
	public Optional<String> validate(RootMetaData meta) {
		return Optional.empty();
	}
}
