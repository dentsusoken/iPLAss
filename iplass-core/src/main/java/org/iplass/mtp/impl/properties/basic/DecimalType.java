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

package org.iplass.mtp.impl.properties.basic;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.iplass.mtp.entity.EntityRuntimeException;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.PropertyDefinitionType;
import org.iplass.mtp.entity.definition.properties.DecimalProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DecimalType extends BasicType {
	private static final long serialVersionUID = -6395734773660560934L;

	private static Logger logger = LoggerFactory.getLogger(DecimalType.class);

	//Integer.MIN_VALUEは桁数未指定の意とする
	private int scale;
	private RoundingMode roundingMode;
	
	public DecimalType() {
	}
	
	public DecimalType(int scale, RoundingMode roundingMode) {
		this.scale = scale;
		this.roundingMode = roundingMode;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((roundingMode == null) ? 0 : roundingMode.hashCode());
		result = prime * result + scale;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DecimalType other = (DecimalType) obj;
		if (roundingMode == null) {
			if (other.roundingMode != null)
				return false;
		} else if (!roundingMode.equals(other.roundingMode))
			return false;
		if (scale != other.scale)
			return false;
		return true;
	}

	public int getScale() {
		return scale;
	}

	public void setScale(int scale) {
		this.scale = scale;
	}

	public RoundingMode getRoundingMode() {
		return roundingMode;
	}

	public void setRoundingMode(RoundingMode roundingMode) {
		this.roundingMode = roundingMode;
	}

	@Override
	public DecimalProperty createPropertyDefinitionInstance() {
		DecimalProperty def = new DecimalProperty();
		def.setScale(scale);
		switch (roundingMode) {
			case UP:
				def.setRoundingMode(org.iplass.mtp.entity.definition.properties.RoundingMode.UP);
				break;
			case DOWN:
				def.setRoundingMode(org.iplass.mtp.entity.definition.properties.RoundingMode.DOWN);
				break;
			case CEILING:
				def.setRoundingMode(org.iplass.mtp.entity.definition.properties.RoundingMode.CEILING);
				break;
			case FLOOR:
				def.setRoundingMode(org.iplass.mtp.entity.definition.properties.RoundingMode.FLOOR);
				break;
			case HALF_UP:
				def.setRoundingMode(org.iplass.mtp.entity.definition.properties.RoundingMode.HALF_UP);
				break;
			case HALF_DOWN:
				def.setRoundingMode(org.iplass.mtp.entity.definition.properties.RoundingMode.HALF_DOWN);
				break;
			case HALF_EVEN:
				def.setRoundingMode(org.iplass.mtp.entity.definition.properties.RoundingMode.HALF_EVEN);
				break;
			default:
				break;
		}
		return def;
	}

	@Override
	public void applyDefinition(PropertyDefinition def) {
		super.applyDefinition(def);
		
		scale = ((DecimalProperty) def).getScale();
		switch (((DecimalProperty) def).getRoundingMode()) {
			case UP:
				setRoundingMode(java.math.RoundingMode.UP);
				break;
			case DOWN:
				setRoundingMode(java.math.RoundingMode.DOWN);
				break;
			case CEILING:
				setRoundingMode(java.math.RoundingMode.CEILING);
				break;
			case FLOOR:
				setRoundingMode(java.math.RoundingMode.FLOOR);
				break;
			case HALF_UP:
				setRoundingMode(java.math.RoundingMode.HALF_UP);
				break;
			case HALF_DOWN:
				setRoundingMode(java.math.RoundingMode.HALF_DOWN);
				break;
			case HALF_EVEN:
				setRoundingMode(java.math.RoundingMode.HALF_EVEN);
				break;
			default:
				break;
		}
	}

	@Override
	public DecimalType copy() {
		return new DecimalType(scale, roundingMode);
	}

	@Override
	public Class<?> storeType() {
		return BigDecimal.class;
	}
	
	@Override
	public PropertyDefinitionType getEnumType() {
		return PropertyDefinitionType.DECIMAL;
	}
	
	@Override
	public Object toDataStore(Object toDataStore) {
		if (toDataStore == null) {
			return toDataStore;
		}
		if (toDataStore instanceof BigDecimal) {
			if (((BigDecimal) toDataStore).scale() != scale
					&& scale != Integer.MIN_VALUE) {
				return ((BigDecimal) toDataStore).setScale(scale, roundingMode);
			} else {
				return toDataStore;
			}
		}
		throw new EntityRuntimeException("DecimalProperty must set BigDecimal value");
	}

	@Override
	public String toString(Object value) {
		if (value == null) {
			return null;
		}
		
		if (((BigDecimal) value).scale() != scale
				&& scale != Integer.MIN_VALUE) {
			return ((BigDecimal) value).setScale(scale, roundingMode).toString();
		} else {
			return ((BigDecimal) value).toString();
		}
	}

	@Override
	public Object fromString(String strValue) {
		if (strValue == null) {
			return null;
		}
		try {
			return new BigDecimal(strValue);
		} catch (NumberFormatException e) {
			if (logger.isDebugEnabled()) {
				logger.debug("Can't parse to Decimal:" + strValue);
			}
			return null;
		}
	}
}
