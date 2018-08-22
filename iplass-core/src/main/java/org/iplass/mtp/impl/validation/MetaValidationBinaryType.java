/*
 * Copyright (C) 2016 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import java.util.regex.Pattern;

import org.iplass.mtp.entity.BinaryReference;
import org.iplass.mtp.entity.EntityRuntimeException;
import org.iplass.mtp.entity.definition.ValidationDefinition;
import org.iplass.mtp.entity.definition.validations.BinaryTypeValidation;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.MetaEntity;
import org.iplass.mtp.impl.entity.property.MetaProperty;
import org.iplass.mtp.impl.util.ObjectUtil;


public class MetaValidationBinaryType extends MetaValidation {
	private static final long serialVersionUID = 4585473131357405434L;

	private String acceptMimeTypesPattern;

	public String getAcceptMimeTypesPattern() {
		return acceptMimeTypesPattern;
	}
	public void setAcceptMimeTypesPattern(String acceptMimeTypesPattern) {
		this.acceptMimeTypesPattern = acceptMimeTypesPattern;
	}
	@Override
	public MetaValidationBinaryType copy() {
		return ObjectUtil.deepCopy(this);
	}

	@Override
	public ValidationHandler createRuntime(MetaEntity entity, MetaProperty property) {
		return new ValidationHandler(this) {
			private Pattern compiledPattern;

			@Override
			public void init() {
				compiledPattern = Pattern.compile(acceptMimeTypesPattern);
			}

			@Override
			public boolean validate(Object value, ValidationContext context) {
				if (value == null) {
					return true;
				}
				if (value instanceof BinaryReference) {
					BinaryReference checkVal = (BinaryReference) value;
					return compiledPattern.matcher(checkVal.getType()).matches();
				}

				throw new EntityRuntimeException("not support type:" + value.getClass());
			}
		};
	}

	@Override
	public BinaryTypeValidation currentConfig(EntityContext context) {
		BinaryTypeValidation def = new BinaryTypeValidation();
		fillTo(def);
		def.setAcceptMimeTypesPattern(acceptMimeTypesPattern);
		return def;
	}

	@Override
	public void applyConfig(ValidationDefinition definition) {
		fillFrom(definition);
		BinaryTypeValidation def = (BinaryTypeValidation) definition;
		acceptMimeTypesPattern = def.getAcceptMimeTypesPattern();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime
				* result
				+ ((acceptMimeTypesPattern == null) ? 0
						: acceptMimeTypesPattern.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		MetaValidationBinaryType other = (MetaValidationBinaryType) obj;
		if (acceptMimeTypesPattern == null) {
			if (other.acceptMimeTypesPattern != null)
				return false;
		} else if (!acceptMimeTypesPattern.equals(other.acceptMimeTypesPattern))
			return false;
		return true;
	}

}
