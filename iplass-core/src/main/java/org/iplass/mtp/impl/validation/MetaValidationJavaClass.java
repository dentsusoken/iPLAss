/*
 * Copyright (C) 2019 DENTSU SOKEN INC. All Rights Reserved.
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

import java.util.Iterator;

import org.iplass.mtp.entity.PropertyValidator;
import org.iplass.mtp.entity.ValidationContext;
import org.iplass.mtp.entity.definition.ValidationDefinition;
import org.iplass.mtp.entity.definition.validations.JavaClassValidation;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.MetaEntity;
import org.iplass.mtp.impl.entity.property.MetaProperty;
import org.iplass.mtp.impl.util.ObjectUtil;

public class MetaValidationJavaClass extends MetaValidation {
	private static final long serialVersionUID = 6400430254617918235L;

	private String className;
	private boolean asArray = false;

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public boolean isAsArray() {
		return asArray;
	}

	public void setAsArray(boolean asArray) {
		this.asArray = asArray;
	}


	@Override
	public void applyConfig(ValidationDefinition definition) {
		fillFrom(definition);
		JavaClassValidation def = (JavaClassValidation) definition;
		className = def.getClassName();
		asArray = def.isAsArray();
	}

	@Override
	public MetaValidationJavaClass copy() {
		return ObjectUtil.deepCopy(this);
	}

	@Override
	public ValidationHandler createRuntime(MetaEntity entity, MetaProperty property) {
		return new ValidationHandlerJavaClass(entity, property);
	}

	@Override
	public JavaClassValidation currentConfig(EntityContext context) {
		JavaClassValidation def = new JavaClassValidation();
		fillTo(def);
		def.setClassName(className);
		def.setAsArray(asArray);
		return def;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (asArray ? 1231 : 1237);
		result = prime * result + ((className == null) ? 0 : className.hashCode());
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
		MetaValidationJavaClass other = (MetaValidationJavaClass) obj;
		if (asArray != other.asArray)
			return false;
		if (className == null) {
			if (other.className != null)
				return false;
		} else if (!className.equals(other.className))
			return false;
		return true;
	}

	private class ValidationHandlerJavaClass extends ValidationHandler {

		private PropertyValidator validator;

		ValidationHandlerJavaClass(MetaEntity entity, MetaProperty property) {
			super(MetaValidationJavaClass.this);

			try {
				validator = (PropertyValidator) Class.forName(className).newInstance();
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				throw new IllegalStateException("can not instantiate " + className, e);
			}
		}

		@Override
		public String generateErrorMessage(Object value,
				ValidationContext context, String propertyDisplayName,
				String entityDisplayName) {
			String msg = super.generateErrorMessage(value, context, propertyDisplayName,
					entityDisplayName);
			Iterator<String> it = context.getAttributeNames();
			while (it.hasNext()) {
				String key = it.next();
				String target = "${" + key + "}";
				msg = msg.replace(target, String.valueOf(context.getAttribute(key)));
			}
			return msg;
		}

		@Override
		public boolean validate(Object value, ValidationContext context) {
			return validator.validate(value, context);
		}

		@Override
		public boolean validateArray(Object[] values, ValidationContext context) {
			if (asArray) {
				return validate(values, context);
			} else {
				return super.validateArray(values, context);
			}
		}
	}
}
