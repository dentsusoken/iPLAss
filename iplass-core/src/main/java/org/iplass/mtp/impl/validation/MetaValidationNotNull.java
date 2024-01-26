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

import org.iplass.mtp.entity.ValidationContext;
import org.iplass.mtp.entity.definition.ValidationDefinition;
import org.iplass.mtp.entity.definition.validations.NotNullValidation;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.MetaEntity;
import org.iplass.mtp.impl.entity.property.MetaProperty;
import org.iplass.mtp.impl.util.ObjectUtil;

public class MetaValidationNotNull extends MetaValidation {
	private static final long serialVersionUID = -7042154660615309103L;

	@Override
	public MetaValidationNotNull copy() {
		return ObjectUtil.deepCopy(this);
//		ValidationNotNull copy = new ValidationNotNull();
//		copyTo(copy);
//		return copy;
	}

	@Override
	public NotNullValidation currentConfig(EntityContext context) {
		NotNullValidation def = new NotNullValidation();
		fillTo(def);
		return def;
	}

	@Override
	public ValidationHandler createRuntime(MetaEntity entity, MetaProperty property) {

		return new ValidationHandler(this) {

			@Override
			public boolean validate(Object value, ValidationContext context) {
				if (value == null) {
					return false;
				}
				if (value instanceof String) {
					if (((String) value).length() == 0) {
						return false;
					}
				}
				if (value instanceof Object[]) {
					if (((Object[])value).length == 0) {
						return false;
					}
				}
				return true;
			}

			@Override
			public boolean validateArray(Object[] values, ValidationContext context) {
				if (values == null) {
					return false;
				}
				if (values.length == 0) {
					return false;
				}
				return super.validateArray(values, context);
			}

//			public MetaData getMetaData() {
//				return MetaValidationNotNull.this;
//			}

		};
	}

	@Override
	public void applyConfig(ValidationDefinition definition) {
		fillFrom(definition);
	}


}
