/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.entity.definition;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import org.iplass.mtp.entity.BinaryReference;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.SelectValue;

/**
 * Propertyの型により処理する際のswitch/case文で利用可能なemum。
 * 
 * @author K.Higuchi
 *
 */
public enum PropertyDefinitionType {
	
	AUTONUMBER,
	BINARY,
	BOOLEAN,
	DATE,
	DATETIME,
	DECIMAL,
	EXPRESSION,
	FLOAT,
	INTEGER,
	LONGTEXT,
	REFERENCE,
	SELECT,
	STRING,
	TIME;
	
	public Class<?> getJavaType() {
		switch (this) {
		case AUTONUMBER:
			return String.class;
		case BINARY:
			return BinaryReference.class;
		case BOOLEAN:
			return Boolean.class;
		case DATE:
			return Date.class;
		case DATETIME:
			return Timestamp.class;
		case DECIMAL:
			return BigDecimal.class;
		case EXPRESSION:
			return Object.class;
		case FLOAT:
			return Double.class;
		case INTEGER:
			return Long.class;
		case LONGTEXT:
			return String.class;
		case REFERENCE:
			return Entity.class;
		case SELECT:
			return SelectValue.class;
		case STRING:
			return String.class;
		case TIME:
			return Time.class;
		default:
			return null;
		}
	}

}
