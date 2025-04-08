/*
 * Copyright (C) 2025 DENTSU SOKEN INC. All Rights Reserved.
 */

package org.iplass.mtp.impl.tools.validator;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.iplass.mtp.impl.metadata.RootMetaData;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.ServiceRegistry;

/**
 * メタデータ定義名チェック用Serviceクラス
 */
public class DefinitionNameCheckServiceImpl implements DefinitionNameCheckService {

	public static DefinitionNameCheckService getInstance() {
		return ServiceRegistry.getRegistry().getService(DefinitionNameCheckService.class);
	}

	private Map<String, DefinitionNameCheckValidator<RootMetaData>> validators;
	private NoCheckValidator noCheckValidator;

	@SuppressWarnings("unchecked")
	@Override
	public void init(Config config) {
		this.validators = config.getValue("validators", Map.class, new HashMap<>());
		this.noCheckValidator = new NoCheckValidator();
	}

	@Override
	public void destroy() {
		// 何もしない
	}

	@Override
	public <T extends RootMetaData> DefinitionNameCheckValidator<RootMetaData> getValidator(Class<T> metaClass) {
		if (Objects.isNull(metaClass)) {
			return this.noCheckValidator;
		}

		DefinitionNameCheckValidator<RootMetaData> validator = this.validators.get(metaClass.getName());
		// Validatorの設定がなければチェックしない（チェックOK）
		if (Objects.isNull(validator)) {
			return this.noCheckValidator;
		}

		return validator;
	}

}
