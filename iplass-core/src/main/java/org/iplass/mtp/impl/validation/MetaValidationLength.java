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

import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;

import org.iplass.mtp.entity.EntityRuntimeException;
import org.iplass.mtp.entity.definition.ValidationDefinition;
import org.iplass.mtp.entity.definition.validations.LengthValidation;
import org.iplass.mtp.impl.datastore.StoreService;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.MetaEntity;
import org.iplass.mtp.impl.entity.property.MetaProperty;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.spi.ServiceRegistry;


public class MetaValidationLength extends MetaValidation {

	private static final long serialVersionUID = -8535627564419203213L;

	private static final Pattern maxPattern = Pattern.compile("${max}", Pattern.LITERAL);
	private static final Pattern minPattern = Pattern.compile("${min}", Pattern.LITERAL);

	private Integer max;
	private Integer min;

	private boolean checkBytes;

	//Count surrogate pair characters as one character
	private boolean surrogatePairAsOneChar;

	public boolean isSurrogatePairAsOneChar() {
		return surrogatePairAsOneChar;
	}

	public void setSurrogatePairAsOneChar(boolean surrogatePairAsOneChar) {
		this.surrogatePairAsOneChar = surrogatePairAsOneChar;
	}

	public boolean isCheckBytes() {
		return checkBytes;
	}

	public void setCheckBytes(boolean checkBytes) {
		this.checkBytes = checkBytes;
	}

	public Integer getMax() {
		return max;
	}

	public void setMax(Integer max) {
		this.max = max;
	}

	public Integer getMin() {
		return min;
	}

	public void setMin(Integer min) {
		this.min = min;
	}

	@Override
	public MetaValidationLength copy() {
		return ObjectUtil.deepCopy(this);
	}

	@Override
	public ValidationHandler createRuntime(MetaEntity entity, MetaProperty property) {

		final String charset = ServiceRegistry.getRegistry().getService(StoreService.class).getCharset();
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
			public void init() {
			}

			@Override
			public boolean validate(Object value, ValidationContext context) {
				if (value == null) {
					return true;
				}
				if (value instanceof String) {

					String checkVal = (String) value;
					if (checkBytes) {
						try {
							int length = checkVal.getBytes(charset).length;
							if (min != null && length < min) {
								return false;
							}
							if (max != null && length > max) {
								return false;
							}
						} catch (UnsupportedEncodingException e) {
							throw new EntityRuntimeException(e);
						}
					} else {
						if (surrogatePairAsOneChar) {
							int codePointCount = checkVal.codePointCount(0, checkVal.length());
							if (min != null && codePointCount < min) {
								return false;
							}
							if (max != null && codePointCount > max) {
								return false;
							}
						} else {
							if (min != null && checkVal.length() < min) {
								return false;
							}
							if (max != null && checkVal.length() > max) {
								return false;
							}
						}
					}
					return true;
				}

				throw new EntityRuntimeException("not support type:" + value.getClass());
			}

//			public MetaData getMetaData() {
//				return MetaValidationLength.this;
//			}
		};
	}

	@Override
	public LengthValidation currentConfig(EntityContext context) {
		LengthValidation def = new LengthValidation();
		fillTo(def);
		def.setMax(max);
		def.setMin(min);
		def.setCheckBytes(checkBytes);
		def.setSurrogatePairAsOneChar(surrogatePairAsOneChar);
		return def;
	}

	@Override
	public void applyConfig(ValidationDefinition definition) {
		fillFrom(definition);
		LengthValidation def = (LengthValidation) definition;
		max = def.getMax();
		min = def.getMin();
		checkBytes = def.isCheckBytes();
		surrogatePairAsOneChar = def.isSurrogatePairAsOneChar();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (checkBytes ? 1231 : 1237);
		result = prime * result + ((max == null) ? 0 : max.hashCode());
		result = prime * result + ((min == null) ? 0 : min.hashCode());
		result = prime * result + (surrogatePairAsOneChar ? 1231 : 1237);
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
		MetaValidationLength other = (MetaValidationLength) obj;
		if (checkBytes != other.checkBytes)
			return false;
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
		if (surrogatePairAsOneChar != other.surrogatePairAsOneChar)
			return false;
		return true;
	}

}
