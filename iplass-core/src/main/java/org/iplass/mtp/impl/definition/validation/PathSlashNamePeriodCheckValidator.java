/*
 * Copyright (C) 2025 DENTSU SOKEN INC. All Rights Reserved.
 */

package org.iplass.mtp.impl.definition.validation;

/**
 * メタデータ定義名（パスにスラッシュを利用、名前にピリオド含む）チェックValidator
 */
public class PathSlashNamePeriodCheckValidator extends BaseDefinitionNameCheckValidator {

	public PathSlashNamePeriodCheckValidator() {
		super(DefinitionNameCheckPattern.PATH_SLASH_NAME_PERIOD);
	}
}
