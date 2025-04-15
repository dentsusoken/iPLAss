/*
 * Copyright (C) 2025 DENTSU SOKEN INC. All Rights Reserved.
 */

package org.iplass.mtp.impl.metadata.validation;

/**
 * メタデータ定義名が下記になってるかチェックするValidatorクラス
 * <ul>
 * <li>英数字、ハイフン、アンダースコア、ピリオドのみ</li>
 * <li>パスにはスラッシュを利用</li>
 * </ul>
 */
public class PathSlashNamePeriodCheckValidator extends DefinitionNameCheckValidator {

	@Override
	public String getErrorMessage(String definitionName) {
		return this.getMessage("impl.metadata.validator.regularExpression.invalidPathSlashNamePeriod");
	}

	@Override
	public String getRegularExpression() {
		return DefinitionNameValidatorConstants.NAME_REG_EXP_PATH_SLASH_NAME_PERIOD;
	}
}
