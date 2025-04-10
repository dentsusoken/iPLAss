/*
 * Copyright (C) 2025 DENTSU SOKEN INC. All Rights Reserved.
 */

package org.iplass.mtp.impl.tools.validator;

import org.iplass.mtp.impl.metadata.RootMetaData;

/**
 * Entityメタデータ定義名をチェックするValidatorクラス
 * 
 * <p>
 * 下記のようになってるかチェックする
 * </p>
 * <ul>
 * <li>英数、アンダスコア、先頭の数字不可、マイナス不可</li>
 * <li>パスにはピリオドを利用</li>
 * </ul>
 */
public class EntityValidator<T extends RootMetaData> extends DefinitionNameCheckValidator<T> {

	@Override
	public String getErrorMessage(T meta) {
		return this.getMessage("validator.regularExpression.entityName");
	}

	@Override
	public String getRegularExpression() {
		return DefinitionNameValidatorConstants.ENTITY_NAME_REG_EXP_PATH_PERIOD;
	}
}
