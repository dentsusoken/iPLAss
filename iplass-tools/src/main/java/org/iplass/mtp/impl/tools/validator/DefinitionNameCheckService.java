/*
 * Copyright (C) 2025 DENTSU SOKEN INC. All Rights Reserved.
 */

package org.iplass.mtp.impl.tools.validator;

import org.iplass.mtp.impl.metadata.RootMetaData;
import org.iplass.mtp.spi.Service;

/**
 * メタデータ定義名チェック用Serviceインターフェース
 */
public interface DefinitionNameCheckService extends Service {

	/**
	 * メタデータ定義名チェックValidatorを返却
	 * 
	 * @param <T> メタデータの型
	 * @param metaClass メタデータのクラス
	 * @return メタデータ定義名チェックValidator
	 */
	<T extends RootMetaData> DefinitionNameCheckValidator<RootMetaData> getValidator(Class<T> metaClass);
}
