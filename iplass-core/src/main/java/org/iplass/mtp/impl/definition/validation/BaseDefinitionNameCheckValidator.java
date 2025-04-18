/*
 * Copyright (C) 2025 DENTSU SOKEN INC. All Rights Reserved.
 */

package org.iplass.mtp.impl.definition.validation;

/**
 * メタデータ定義名チェックValidatorクラス
 * 
 * <p>
 * {@link DefinitionNameCheckPattern}で定義されてる定義名Patternから生成
 * </p>
 */
public class BaseDefinitionNameCheckValidator extends DefinitionNameCheckValidator {

	private String errorMessage;

	public BaseDefinitionNameCheckValidator(DefinitionNameCheckPattern checkPattern) {
		super(checkPattern.getDefinitionNamePattern());
		this.errorMessage = this.getMessage(checkPattern.getMessageKey());
	}

	@Override
	protected String getErrorMessage(String definitionName) {
		return this.errorMessage;
	}
}
