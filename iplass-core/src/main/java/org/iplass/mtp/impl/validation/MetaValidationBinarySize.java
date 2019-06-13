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
import org.iplass.mtp.entity.ValidationContext;
import org.iplass.mtp.entity.definition.ValidationDefinition;
import org.iplass.mtp.entity.definition.validations.BinarySizeValidation;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.MetaEntity;
import org.iplass.mtp.impl.entity.property.MetaProperty;
import org.iplass.mtp.impl.util.ObjectUtil;


public class MetaValidationBinarySize extends MetaValidation {
	private static final long serialVersionUID = 1334805081077727638L;

	private static final Pattern maxPattern = Pattern.compile("${max}", Pattern.LITERAL);
	private static final Pattern minPattern = Pattern.compile("${min}", Pattern.LITERAL);

	private Long max;
	private Long min;

	public Long getMax() {
		return max;
	}
	public void setMax(Long max) {
		this.max = max;
	}
	public Long getMin() {
		return min;
	}
	public void setMin(Long min) {
		this.min = min;
	}

	@Override
	public MetaValidationBinarySize copy() {
		return ObjectUtil.deepCopy(this);
	}

	@Override
	public ValidationHandler createRuntime(MetaEntity entity, MetaProperty property) {
		return new ValidationHandler(this) {
			@Override
			public String generateErrorMessage(Object value,
					ValidationContext context, String propertyDisplayName,
					String entityDisplayName) {
				String msg = super.generateErrorMessage(value, context, propertyDisplayName,
						entityDisplayName);
				if (msg != null) {
					if (msg.contains("${max}")) {
						msg = maxPattern.matcher(msg).replaceAll(String.valueOf(max));
					}
					if (msg.contains("${min}")) {
						msg = minPattern.matcher(msg).replaceAll(String.valueOf(min));
					}
				}
				return msg;
			}

			@Override
			public boolean validate(Object value, ValidationContext context) {
				if (value == null) {
					return true;
				}
				if (value instanceof BinaryReference) {
					BinaryReference checkVal = (BinaryReference) value;
					if (min != null && checkVal.getSize() < min) {
						return false;
					}
					if (max != null && checkVal.getSize() > max) {
						return false;
					}
					return true;
				}

				throw new EntityRuntimeException("not support type:" + value.getClass());
			}
		};
	}

	@Override
	public BinarySizeValidation currentConfig(EntityContext context) {
		BinarySizeValidation def = new BinarySizeValidation();
		fillTo(def);
		def.setMax(max);
		def.setMin(min);
		return def;
	}

	@Override
	public void applyConfig(ValidationDefinition definition) {
		fillFrom(definition);
		BinarySizeValidation def = (BinarySizeValidation) definition;
		max = def.getMax();
		min = def.getMin();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((max == null) ? 0 : max.hashCode());
		result = prime * result + ((min == null) ? 0 : min.hashCode());
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
		MetaValidationBinarySize other = (MetaValidationBinarySize) obj;
		if (max == null) {
			if (other.max != null)
				return false;
		} else if (!max.equals(other.max))
			return false;
		if (min == null) {
			if (other.min != null)
				return false;
		} else if (!min.equals(other.min))
			return false;
		return true;
	}

}
