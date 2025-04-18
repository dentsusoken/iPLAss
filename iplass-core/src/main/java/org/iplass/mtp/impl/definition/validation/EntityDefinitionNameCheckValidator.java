/*
 * Copyright (C) 2025 DENTSU SOKEN INC. All Rights Reserved.
 */

package org.iplass.mtp.impl.definition.validation;

/**
 * Entity定義名（英数、アンダスコア、パスにピリオド、先頭の数字不可、マイナス不可）チェックValidator
 */
public class EntityDefinitionNameCheckValidator extends BaseDefinitionNameCheckValidator {

	public EntityDefinitionNameCheckValidator() {
		super(DefinitionNameCheckPattern.ENTITY_NAME);
	}
}
