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

import java.math.BigDecimal;
import java.util.regex.Pattern;

import org.iplass.mtp.entity.EntityRuntimeException;
import org.iplass.mtp.entity.ValidationContext;
import org.iplass.mtp.entity.definition.ValidationDefinition;
import org.iplass.mtp.entity.definition.validations.RangeValidation;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.MetaEntity;
import org.iplass.mtp.impl.entity.property.MetaProperty;
import org.iplass.mtp.impl.util.ObjectUtil;


public class MetaValidationRange extends MetaValidation {
	private static final long serialVersionUID = -4575600888354853812L;

	private static final Pattern maxPattern = Pattern.compile("${max}", Pattern.LITERAL);
	private static final Pattern minPattern = Pattern.compile("${min}", Pattern.LITERAL);

	private String max;
	private String min;
	private boolean maxValueExcluded;
	private boolean minValueExcluded;

	public String getMax() {
		return max;
	}

	public void setMax(String max) {
		this.max = max;
	}

	public String getMin() {
		return min;
	}

	public void setMin(String min) {
		this.min = min;
	}

	public boolean isMaxValueExcluded() {
		return maxValueExcluded;
	}

	public void setMaxValueExcluded(boolean maxValueExcluded) {
		this.maxValueExcluded = maxValueExcluded;
	}

	public boolean isMinValueExcluded() {
		return minValueExcluded;
	}

	public void setMinValueExcluded(boolean minValueExcluded) {
		this.minValueExcluded = minValueExcluded;
	}

	@Override
	public MetaValidationRange copy() {
		return ObjectUtil.deepCopy(this);
		
//		ValidationRange copy = new ValidationRange();
//		copyTo(copy);
//		copy.max = max;
//		copy.min = min;
//		copy.maxValueExcluded = maxValueExcluded;
//		copy.minValueExcluded = minValueExcluded;
//		copy.nullEnabled = nullEnabled;
//		
//		return copy;
	}

	@Override
	public ValidationHandler createRuntime(MetaEntity entity, MetaProperty property) {
		return new ValidationHandler(this) {
			
			private boolean isMaxSpecify;
			private boolean isMinSpecify;
			private long maxLong;
			private long minLong;
			private double maxDouble;
			private double minDouble;
			private BigDecimal maxBigDecimal;
			private BigDecimal minBigDecimal;
			
			@Override
			public void init() {
				if (max != null && max.length() != 0) {
					isMaxSpecify = true;
					if (!max.contains(".")) {
						maxLong = Long.parseLong(max);
					}
					maxDouble = Double.parseDouble(max);
					maxBigDecimal = new BigDecimal(max);
				}
				if (min != null && min.length() != 0) {
					isMinSpecify = true;
					if (!min.contains(".")) {
						minLong = Long.parseLong(min);
					}
					minDouble = Double.parseDouble(min);
					minBigDecimal = new BigDecimal(min);
				}

			}
			
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
				
				if (value instanceof Long) {
					return checkLong((Long) value);
				}
				
				if (value instanceof Double) {
					return checkDouble((Double) value);
				}
				
				if (value instanceof BigDecimal) {
					return checkBigDecimal((BigDecimal) value);
				}
				
				//TODO 実装！
				
				throw new EntityRuntimeException("not support type:" + value.getClass());
			}
			
			private boolean checkDouble(Double value) {
				
				double dVal = value.doubleValue();
				
				if (isMaxSpecify) {
					if (maxValueExcluded) {
						if (dVal >= maxDouble) {
							return false;
						}
					} else {
						if (dVal > maxDouble) {
							return false;
						}
					}
				}
				
				if (isMinSpecify) {
					if (minValueExcluded) {
						if (dVal <= minDouble) {
							return false;
						}
					} else {
						if (dVal < minDouble) {
							return false;
						}
					}
				}
				
				return true;
			}

			private boolean checkLong(Long value) {
				long lVal = value.longValue();
				
				if (isMaxSpecify) {
					if (maxValueExcluded) {
						if (lVal >= maxLong) {
							return false;
						}
					} else {
						if (lVal > maxLong) {
							return false;
						}
					}
				}
				
				if (isMinSpecify) {
					if (minValueExcluded) {
						if (lVal <= minLong) {
							return false;
						}
					} else {
						if (lVal < minLong) {
							return false;
						}
					}
				}
				
				return true;
			}

			private boolean checkBigDecimal(BigDecimal value) {
				
				if (isMaxSpecify) {
					if (maxValueExcluded) {
						if (value.compareTo(maxBigDecimal) >= 0) {
							return false;
						}
					} else {
						if (value.compareTo(maxBigDecimal) > 0) {
							return false;
						}
					}
				}
				
				if (isMinSpecify) {
					if (minValueExcluded) {
						if (value.compareTo(minBigDecimal) <= 0) {
							return false;
						}
					} else {
						if (value.compareTo(minBigDecimal) < 0) {
							return false;
						}
					}
				}
				
				return true;
			}

//			public MetaData getMetaData() {
//				return MetaValidationRange.this;
//			}
			
		};
	}

	@Override
	public RangeValidation currentConfig(EntityContext context) {
		RangeValidation def = new RangeValidation();
		fillTo(def);
		def.setMax(max);
		def.setMin(min);
		def.setMaxValueExcluded(maxValueExcluded);
		def.setMinValueExcluded(minValueExcluded);
		return def;
	}

	@Override
	public void applyConfig(ValidationDefinition definition) {
		fillFrom(definition);
		RangeValidation def = (RangeValidation) definition;
		max = def.getMax();
		min = def.getMin();
		maxValueExcluded = def.isMaxValueExcluded();
		minValueExcluded = def.isMinValueExcluded();
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((max == null) ? 0 : max.hashCode());
		result = prime * result + (maxValueExcluded ? 1231 : 1237);
		result = prime * result + ((min == null) ? 0 : min.hashCode());
		result = prime * result + (minValueExcluded ? 1231 : 1237);
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
		MetaValidationRange other = (MetaValidationRange) obj;
		if (max == null) {
			if (other.max != null)
				return false;
		} else if (!max.equals(other.max))
			return false;
		if (maxValueExcluded != other.maxValueExcluded)
			return false;
		if (min == null) {
			if (other.min != null)
				return false;
		} else if (!min.equals(other.min))
			return false;
		if (minValueExcluded != other.minValueExcluded)
			return false;
		return true;
	}
	
}
