/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
 *
 * Unless you have purchased a commercial license,
 * the following license terms apply:
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package org.iplass.mtp.impl.validation;

import jakarta.validation.Validator;

import org.iplass.mtp.entity.definition.ValidationDefinition;
import org.iplass.mtp.entity.definition.validations.BinarySizeValidation;
import org.iplass.mtp.entity.definition.validations.BinaryTypeValidation;
import org.iplass.mtp.entity.definition.validations.ExistsValidation;
import org.iplass.mtp.entity.definition.validations.JavaClassValidation;
import org.iplass.mtp.entity.definition.validations.LengthValidation;
import org.iplass.mtp.entity.definition.validations.NotNullValidation;
import org.iplass.mtp.entity.definition.validations.RangeValidation;
import org.iplass.mtp.entity.definition.validations.RegexValidation;
import org.iplass.mtp.entity.definition.validations.ScriptingValidation;
import org.iplass.mtp.impl.validation.bean.BeanValidationConfig;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;

public class ValidationService implements Service {

	private BeanValidationConfig beanValidation;

	private Validator validator;


	//TODO MetaDataRepository内の定義を取得する

	//TODO キャッシュ？

//	private Map<String, Class<? extends ValidationHandler<?>>> validatorMap = new HashMap<String, Class<? extends ValidationHandler<?>>>();

	public ValidationService() {

//		validatorMap.put(NotNullValidation.class.getName(), ValidationHandlerNotNull.class);
//		validatorMap.put(RangeValidation.class.getName(), ValidationHandlerRange.class);

	}

	public Validator getValidator() {
		return validator;
	}

	public BeanValidationConfig getBeanValidation() {
		return beanValidation;
	}

	public MetaValidation createValidationMetaData(ValidationDefinition def) {

		//TODO 設定の外部化
		MetaValidation meta = null;
		if (def instanceof NotNullValidation) {
			meta = new MetaValidationNotNull();
		} else if (def instanceof RangeValidation) {
			meta = new MetaValidationRange();
		} else if (def instanceof RegexValidation) {
			meta = new MetaValidationRegex();
		} else if (def instanceof LengthValidation) {
			meta = new MetaValidationLength();
		} else if (def instanceof ScriptingValidation) {
			meta = new MetaValidationScripting();
		} else if (def instanceof JavaClassValidation) {
			meta = new MetaValidationJavaClass();
		} else if (def instanceof BinarySizeValidation) {
			meta = new MetaValidationBinarySize();
		} else if (def instanceof BinaryTypeValidation) {
			meta = new MetaValidationBinaryType();
		} else if (def instanceof ExistsValidation) {
			meta = new MetaValidationExists();
		}

		if (meta != null) {
			meta.applyConfig(def);
		}

		return meta;
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(Config config) {
		//TODO どっか（MetaDataRepository？）から、validatorのリストを取得
		// TODO Auto-generated method stub
		beanValidation = config.getValue("beanValidation", BeanValidationConfig.class);
		if (beanValidation != null) {
			validator = beanValidation.getValidatorFactory().getValidator();
		}

	}

//	public List<ValidationHandler<?>> getAcceptableValidation(Class<?> type) {
//		return null;
//	}

}
