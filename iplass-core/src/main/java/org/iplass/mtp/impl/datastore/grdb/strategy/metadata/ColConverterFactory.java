/*
 * Copyright (C) 2017 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.datastore.grdb.strategy.metadata;

import org.iplass.mtp.impl.datastore.grdb.strategy.metadata.ColConverter.CastColConverter;
import org.iplass.mtp.impl.datastore.grdb.strategy.metadata.ColConverter.LongTextColConverter;
import org.iplass.mtp.impl.datastore.grdb.strategy.metadata.ColConverter.NoneColConverter;
import org.iplass.mtp.impl.entity.property.PropertyType;
import org.iplass.mtp.impl.properties.basic.DecimalType;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;

public class ColConverterFactory implements Service {

	public ColConverter getColConverter(PropertyType from, PropertyType to) {
		if (from == null) {
			return null;
		}

		switch (to.getEnumType()) {
		case DECIMAL:
			switch (from.getEnumType()) {
			case DECIMAL:
				DecimalType toDec = (DecimalType) to;
				DecimalType fromDec = (DecimalType) from;
				if (toDec.getScale() == fromDec.getScale()) {
					return new NoneColConverter(from, to);
				} else {
					return new CastColConverter(from, to);
				}
			case FLOAT:
			case INTEGER:
				return new CastColConverter(from, to);
			case STRING:
			case AUTONUMBER:
			case LONGTEXT:
			case BOOLEAN:
			case SELECT:
			case DATE:
			case TIME:
			case DATETIME:
			case BINARY:
			default:
				break;
			}
			break;
		case FLOAT:
			switch (from.getEnumType()) {
			case FLOAT:
				return new NoneColConverter(from, to);
			case DECIMAL:
			case INTEGER:
				return new CastColConverter(from, to);
			case STRING:
			case AUTONUMBER:
			case LONGTEXT:
			case BOOLEAN:
			case SELECT:
			case DATE:
			case TIME:
			case DATETIME:
			case BINARY:
			default:
				break;
			}
			break;
		case INTEGER:
			switch (from.getEnumType()) {
			case INTEGER:
				return new NoneColConverter(from, to);
			case FLOAT:
			case DECIMAL:
				return new CastColConverter(from, to);
			case STRING:
			case AUTONUMBER:
			case LONGTEXT:
			case BOOLEAN:
			case SELECT:
			case DATE:
			case TIME:
			case DATETIME:
			case BINARY:
			default:
				break;
			}
			break;
		case BOOLEAN:
			switch (from.getEnumType()) {
			case BOOLEAN:
				return new NoneColConverter(from, to);
			case STRING:
			case AUTONUMBER:
			case LONGTEXT:
			case INTEGER:
			case FLOAT:
			case DECIMAL:
			case SELECT:
			case DATE:
			case TIME:
			case DATETIME:
			case BINARY:
			default:
				break;
			}
			break;
		case SELECT:
			switch (from.getEnumType()) {
			case SELECT:
			case BOOLEAN:
				return new NoneColConverter(from, to);
			case STRING:
			case AUTONUMBER:
			case LONGTEXT:
			case INTEGER:
			case FLOAT:
			case DECIMAL:
			case DATE:
			case TIME:
			case DATETIME:
			case BINARY:
			default:
				break;
			}
			break;
		case STRING:
			switch (from.getEnumType()) {
			case STRING:
			case AUTONUMBER:
			case SELECT:
			case BOOLEAN:
				return new NoneColConverter(from, to);
			case INTEGER:
			case FLOAT:
			case DECIMAL:
			case DATE:
			case TIME:
			case DATETIME:
				return new CastColConverter(from, to);
			case LONGTEXT:
			case BINARY:
			default:
				break;
			}
			break;
		case AUTONUMBER:
			switch (from.getEnumType()) {
			case STRING:
			case AUTONUMBER:
			case SELECT:
			case BOOLEAN:
				return new NoneColConverter(from, to);
			case INTEGER:
			case FLOAT:
			case DECIMAL:
			case DATE:
			case TIME:
			case DATETIME:
				return new CastColConverter(from, to);
			case LONGTEXT:
			case BINARY:
			default:
				break;
			}
			break;
		case LONGTEXT:
			switch (from.getEnumType()) {
			case LONGTEXT:
				return new NoneColConverter(from, to);
			case STRING:
			case AUTONUMBER:
			case SELECT:
			case BOOLEAN:
			case INTEGER:
			case FLOAT:
			case DECIMAL:
			case DATE:
			case TIME:
			case DATETIME:
				return new LongTextColConverter(from, to);
			case BINARY:
			default:
				break;
			}
			break;
		case DATE:
			switch (from.getEnumType()) {
			case DATE:
				return new NoneColConverter(from, to);
			case DATETIME:
				return new CastColConverter(from, to);
			case TIME:
			case LONGTEXT:
			case STRING:
			case AUTONUMBER:
			case SELECT:
			case BOOLEAN:
			case INTEGER:
			case FLOAT:
			case DECIMAL:
			case BINARY:
			default:
				break;
			}
			break;
		case TIME:
			switch (from.getEnumType()) {
			case TIME:
				return new NoneColConverter(from, to);
			case DATETIME:
				return new CastColConverter(from, to);
			case DATE:
			case LONGTEXT:
			case STRING:
			case AUTONUMBER:
			case SELECT:
			case BOOLEAN:
			case INTEGER:
			case FLOAT:
			case DECIMAL:
			case BINARY:
			default:
				break;
			}
			break;
		case DATETIME:
			switch (from.getEnumType()) {
			case DATETIME:
			case TIME:
			case DATE:
				return new NoneColConverter(from, to);
			case LONGTEXT:
			case STRING:
			case AUTONUMBER:
			case SELECT:
			case BOOLEAN:
			case INTEGER:
			case FLOAT:
			case DECIMAL:
			case BINARY:
			default:
				break;
			}
			break;
		case BINARY:
			switch (from.getEnumType()) {
			case BINARY:
				return new NoneColConverter(from, to);
			case DATETIME:
			case TIME:
			case DATE:
			case LONGTEXT:
			case STRING:
			case AUTONUMBER:
			case SELECT:
			case BOOLEAN:
			case INTEGER:
			case FLOAT:
			case DECIMAL:
			default:
				break;
			}
			break;
		default:
			break;
		}

		return null;
	}
	
	@Override
	public void init(Config config) {
	}

	@Override
	public void destroy() {
	}

}
