/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import org.iplass.mtp.entity.EntityRuntimeException;
import org.iplass.mtp.entity.definition.ValidationDefinition;
import org.iplass.mtp.entity.definition.validations.RegexValidation;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.MetaEntity;
import org.iplass.mtp.impl.entity.property.MetaProperty;
import org.iplass.mtp.impl.util.ObjectUtil;


public class MetaValidationRegex extends MetaValidation {
	private static final long serialVersionUID = 8178209126020452120L;

	private String pattern;

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	@Override
	public MetaValidationRegex copy() {
		return ObjectUtil.deepCopy(this);
	}

	@Override
	public ValidationHandler createRuntime(MetaEntity entity, MetaProperty property) {
		return new ValidationHandler(this) {
			
			private Pattern compiledPattern;
			
			
			@Override
			public void init() {
				compiledPattern = Pattern.compile(pattern);
			}
			
			@Override
			public boolean validate(Object value, ValidationContext context) {
				if (value == null) {
					return true;
				}
				
				String checkVal = null;
				if (value instanceof Number) {
					checkVal = value.toString();
				} else if (value instanceof String) {
					checkVal = (String) value;
				} else {
					throw new EntityRuntimeException("not support type:" + value.getClass());
				}
				
				return compiledPattern.matcher(checkVal).matches();
				
			}
			
//			public MetaData getMetaData() {
//				return MetaValidationRegex.this;
//			}
		};
	}

	@Override
	public RegexValidation currentConfig(EntityContext context) {
		RegexValidation def = new RegexValidation();
		fillTo(def);
		def.setPattern(pattern);
		return def;
	}

	@Override
	public void applyConfig(ValidationDefinition definition) {
		fillFrom(definition);
		RegexValidation def = (RegexValidation) definition;
		pattern = def.getPattern();
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((pattern == null) ? 0 : pattern.hashCode());
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
		MetaValidationRegex other = (MetaValidationRegex) obj;
		if (pattern == null) {
			if (other.pattern != null)
				return false;
		} else if (!pattern.equals(other.pattern))
			return false;
		return true;
	}

	
}
