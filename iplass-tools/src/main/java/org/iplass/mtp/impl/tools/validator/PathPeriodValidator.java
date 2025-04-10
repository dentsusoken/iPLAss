/*
 * Copyright (C) 2025 DENTSU SOKEN INC. All Rights Reserved.
 */

package org.iplass.mtp.impl.tools.validator;

import org.iplass.mtp.impl.metadata.RootMetaData;

/**
 * メタデータ定義名が下記になってるかチェックするValidatorクラス
 * <ul>
 * <li>英数字、ハイフン、アンダースコアのみ</li>
 * <li>パスにはピリオドを利用</li>
 * </ul>
 */
public class PathPeriodValidator<T extends RootMetaData> extends DefinitionNameCheckValidator<T> {

	@Override
	public String getErrorMessage(T meta) {
		return this.getMessage("validator.regularExpression.pathPeriod");
	}

	@Override
	public String getRegularExpression() {
		return DefinitionNameValidatorConstants.NAME_REG_EXP_PATH_PERIOD;
	}
}
