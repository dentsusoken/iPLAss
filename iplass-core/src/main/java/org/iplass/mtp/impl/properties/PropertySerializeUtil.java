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

package org.iplass.mtp.impl.properties;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.iplass.mtp.entity.BinaryReference;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.SelectValue;
import org.iplass.mtp.entity.definition.PropertyDefinitionType;

public class PropertySerializeUtil {

	//カスタムのシリアライズ処理などで、型の識別子に利用

	public static final byte NULL = 0;
	public static final byte AUTONUMBER = 1;
	@Deprecated
	public static final byte BINARY_OLD = 2;//LobIdのみ保持
	public static final byte BINARY = 15;
	public static final byte BOOLEAN = 3;
	public static final byte DATE = 4;
	public static final byte DATETIME = 5;
	public static final byte DECIMAL = 6;
	public static final byte EXPRESSION = 7;//expressionは最終的にいずれかの型になる
	public static final byte FLOAT = 8;
	public static final byte INTEGER = 9;
	public static final byte LONGTEXT = 10;
	public static final byte REFERENCE = 11;
	public static final byte SELECT = 12;
	public static final byte STRING = 13;
	public static final byte TIME = 14;

	public static final byte ARRAY_MASK = 0b1000000;

	public static final byte NULL_ARRAY = NULL | ARRAY_MASK;
	public static final byte AUTONUMBER_ARRAY = AUTONUMBER | ARRAY_MASK;
	@Deprecated
	public static final byte BINARY_ARRAY_OLD = BINARY_OLD | ARRAY_MASK;//LobIdのみ保持
	public static final byte BINARY_ARRAY = BINARY | ARRAY_MASK;
	public static final byte BOOLEAN_ARRAY = BOOLEAN | ARRAY_MASK;
	public static final byte DATE_ARRAY = DATE | ARRAY_MASK;
	public static final byte DATETIME_ARRAY = DATETIME | ARRAY_MASK;
	public static final byte DECIMAL_ARRAY = DECIMAL | ARRAY_MASK;
	public static final byte EXPRESSION_ARRAY = EXPRESSION | ARRAY_MASK;//expressionは最終的にいずれかの型になる
	public static final byte FLOAT_ARRAY = FLOAT | ARRAY_MASK;
	public static final byte INTEGER_ARRAY = INTEGER | ARRAY_MASK;
	public static final byte LONGTEXT_ARRAY = LONGTEXT | ARRAY_MASK;
	public static final byte REFERENCE_ARRAY = REFERENCE | ARRAY_MASK;
	public static final byte SELECT_ARRAY = SELECT | ARRAY_MASK;
	public static final byte STRING_ARRAY = STRING | ARRAY_MASK;
	public static final byte TIME_ARRAY = TIME | ARRAY_MASK;

	private static final Map<Class<?>, Byte> typeMap;
	static {
		Map<Class<?>, Byte> map = new HashMap<>();
		map.put(null, NULL);
		map.put(BinaryReference.class, BINARY);
		map.put(Boolean.class, BOOLEAN);
		map.put(Date.class, DATE);
		map.put(Timestamp.class, DATETIME);
		map.put(BigDecimal.class, DECIMAL);
		map.put(Double.class, FLOAT);
		map.put(Long.class, INTEGER);
		map.put(Entity.class, REFERENCE);
		map.put(SelectValue.class, SELECT);
		map.put(String.class, STRING);
		map.put(Time.class, TIME);

		map.put(BinaryReference[].class, BINARY_ARRAY);
		map.put(Boolean[].class, BOOLEAN_ARRAY);
		map.put(boolean[].class, BOOLEAN_ARRAY);
		map.put(Date[].class, DATE_ARRAY);
		map.put(Timestamp[].class, DATETIME_ARRAY);
		map.put(BigDecimal[].class, DECIMAL_ARRAY);
		map.put(Double[].class, FLOAT_ARRAY);
		map.put(double[].class, FLOAT_ARRAY);
		map.put(Long[].class, INTEGER_ARRAY);
		map.put(long[].class, INTEGER_ARRAY);
		map.put(Entity[].class, REFERENCE_ARRAY);
		map.put(SelectValue[].class, SELECT_ARRAY);
		map.put(String[].class, STRING_ARRAY);
		map.put(Time[].class, TIME_ARRAY);

		typeMap = map;
	}


	public static byte typeOf(PropertyDefinitionType pdType) {
		switch (pdType) {
		case AUTONUMBER:
			return AUTONUMBER;
		case BINARY:
			return BINARY;
		case BOOLEAN:
			return BOOLEAN;
		case DATE:
			return DATE;
		case DATETIME:
			return DATETIME;
		case DECIMAL:
			return DECIMAL;
		case FLOAT:
			return FLOAT;
		case INTEGER:
			return INTEGER;
		case LONGTEXT:
			return LONGTEXT;
		case REFERENCE:
			return REFERENCE;
		case SELECT:
			return SELECT;
		case STRING:
			return STRING;
		case TIME:
			return TIME;
		case EXPRESSION:
			return EXPRESSION;
		}
		throw new IllegalArgumentException("not support type:" + pdType);
	}

	public static Object read(DataInput is) throws IOException {
		byte dt = is.readByte();
		switch (dt) {
		case NULL:
			return null;
		case BINARY_OLD:
			return new BinaryReference(is.readLong(), null, null, 0);
		case BINARY:
			long lobId = is.readLong();
			dt = is.readByte();
			if (dt == NULL) {
				return new BinaryReference(lobId, null, null, 0);
			} else {
				return new BinaryReference(lobId, is.readUTF(), null, 0);
			}
		case BOOLEAN:
			return Boolean.valueOf(is.readBoolean());
		case DATE:
			return new Date(is.readLong());
		case DATETIME:
			Timestamp ts = new Timestamp(is.readLong());
			ts.setNanos(is.readInt());
			return ts;
		case DECIMAL:
			return new BigDecimal(is.readUTF());
		case FLOAT:
			return Double.valueOf(is.readDouble());
		case INTEGER:
			return Long.valueOf(is.readLong());
		case SELECT:
			String val = is.readUTF();
			byte ddt = is.readByte();
			if (ddt == NULL) {
				return new SelectValue(val);
			} else {
				return new SelectValue(val, is.readUTF());
			}
		case STRING:
			return is.readUTF();
		case TIME:
			return new Time(is.readLong());
		case STRING_ARRAY:
			int length = is.readInt();
			String[] strArray = new String[length];
			for (int i = 0; i< strArray.length; i++) {
				dt = is.readByte();
				if (dt == STRING) {
					strArray[i] = is.readUTF();
				}
			}
			return strArray;
		case INTEGER_ARRAY:
			length = is.readInt();
			Long[] longArray = new Long[length];
			for (int i = 0; i< longArray.length; i++) {
				dt = is.readByte();
				if (dt == INTEGER) {
					longArray[i] = is.readLong();
				}
			}
			return longArray;
		case FLOAT_ARRAY:
			length = is.readInt();
			Double[] doubleArray = new Double[length];
			for (int i = 0; i< doubleArray.length; i++) {
				dt = is.readByte();
				if (dt == FLOAT) {
					doubleArray[i] = is.readDouble();
				}
			}
			return doubleArray;
		case SELECT_ARRAY:
			length = is.readInt();
			SelectValue[] selectArray = new SelectValue[length];
			for (int i = 0; i< selectArray.length; i++) {
				dt = is.readByte();
				if (dt == SELECT) {
					selectArray[i] = new SelectValue(is.readUTF());
					if (is.readByte() != NULL) {
						selectArray[i].setDisplayName(is.readUTF());
					}
				}
			}
			return selectArray;
		case DATETIME_ARRAY:
			length = is.readInt();
			Timestamp[] tsArray = new Timestamp[length];
			for (int i = 0; i< tsArray.length; i++) {
				dt = is.readByte();
				if (dt == DATETIME) {
					tsArray[i] = new Timestamp(is.readLong());
					tsArray[i].setNanos(is.readInt());
				}
			}
			return tsArray;
		case DATE_ARRAY:
			length = is.readInt();
			Date[] dateArray = new Date[length];
			for (int i = 0; i< dateArray.length; i++) {
				dt = is.readByte();
				if (dt == DATE) {
					dateArray[i] = new Date(is.readLong());
				}
			}
			return dateArray;
		case BOOLEAN_ARRAY:
			length = is.readInt();
			Boolean[] boolArray = new Boolean[length];
			for (int i = 0; i< boolArray.length; i++) {
				dt = is.readByte();
				if (dt == BOOLEAN) {
					boolArray[i] = Boolean.valueOf(is.readBoolean());
				}
			}
			return boolArray;
		case BINARY_ARRAY_OLD:
			length = is.readInt();
			BinaryReference[] binOldArray = new BinaryReference[length];
			for (int i = 0; i< binOldArray.length; i++) {
				dt = is.readByte();
				if (dt == BINARY_OLD) {
					binOldArray[i] = new BinaryReference(is.readLong(), null, null, 0);
				}
			}
			return binOldArray;
		case BINARY_ARRAY:
			length = is.readInt();
			BinaryReference[] binArray = new BinaryReference[length];
			for (int i = 0; i< binArray.length; i++) {
				dt = is.readByte();
				if (dt == BINARY) {
					binArray[i] = new BinaryReference(is.readLong(), null, null, 0);
					if (is.readByte() != NULL) {
						binArray[i].setName(is.readUTF());
					}
				}
			}
			return binArray;
		case DECIMAL_ARRAY:
			length = is.readInt();
			BigDecimal[] decArray = new BigDecimal[length];
			for (int i = 0; i< decArray.length; i++) {
				dt = is.readByte();
				if (dt == DECIMAL) {
					decArray[i] = new BigDecimal(is.readUTF());
				}
			}
			return decArray;
		case TIME_ARRAY:
			length = is.readInt();
			Time[] timeArray = new Time[length];
			for (int i = 0; i< timeArray.length; i++) {
				dt = is.readByte();
				if (dt == TIME) {
					timeArray[i] = new Time(is.readLong());
				}
			}
			return timeArray;
		}

		throw new IllegalArgumentException("unknown data format type:" + dt);

	}

	public static void write(DataOutput os, Object value) throws IOException {

		byte bt = typeOf(value, false);
		os.writeByte(bt);
		switch (bt) {
		case NULL:
			break;
		case STRING:
			os.writeUTF((String) value);
			break;
		case INTEGER:
			os.writeLong(((Long) value).longValue());
			break;
		case FLOAT:
			os.writeDouble(((Double) value).doubleValue());
			break;
		case SELECT:
			SelectValue sv = (SelectValue) value;
			os.writeUTF(sv.getValue());
			if (sv.getDisplayName() == null) {
				os.writeByte(NULL);
			} else {
				os.writeByte(STRING);
				os.writeUTF(sv.getDisplayName());
			}
			break;
		case DATETIME:
			Timestamp ts = (Timestamp) value;
			os.writeLong(ts.getTime());
			os.writeInt(ts.getNanos());
			break;
		case DATE:
			os.writeLong(((Date) value).getTime());
			break;
		case BOOLEAN:
			os.writeBoolean(((Boolean) value).booleanValue());
			break;
		case BINARY:
			BinaryReference br = (BinaryReference) value;
			os.writeLong(br.getLobId());
			if (br.getName() == null) {
				os.writeByte(NULL);
			} else {
				os.writeByte(STRING);
				os.writeUTF(br.getName());
			}
			break;
		case DECIMAL:
			os.writeUTF(((BigDecimal) value).toString());
			break;
		case TIME:
			os.writeLong(((Time) value).getTime());
			break;
		case STRING_ARRAY:
			String[] strArray = (String[]) value;
			os.writeInt(strArray.length);
			for (int i = 0; i< strArray.length; i++) {
				if (strArray[i] == null) {
					os.writeByte(NULL);
				} else {
					os.writeByte(STRING);
					os.writeUTF(strArray[i]);
				}
			}
			break;
		case INTEGER_ARRAY:
			Long[] longArray = (Long[]) value;
			os.writeInt(longArray.length);
			for (int i = 0; i< longArray.length; i++) {
				if (longArray[i] == null) {
					os.writeByte(NULL);
				} else {
					os.writeByte(INTEGER);
					os.writeLong(longArray[i]);
				}
			}
			break;
		case FLOAT_ARRAY:
			Double[] doubleArray = (Double[]) value;
			os.writeInt(doubleArray.length);
			for (int i = 0; i< doubleArray.length; i++) {
				if (doubleArray[i] == null) {
					os.writeByte(NULL);
				} else {
					os.writeByte(FLOAT);
					os.writeDouble(doubleArray[i]);
				}
			}
			break;
		case SELECT_ARRAY:
			SelectValue[] selectArray = (SelectValue[]) value;
			os.writeInt(selectArray.length);
			for (int i = 0; i< selectArray.length; i++) {
				if (selectArray[i] == null) {
					os.writeByte(NULL);
				} else {
					os.writeByte(SELECT);
					os.writeUTF(selectArray[i].getValue());
					if (selectArray[i].getDisplayName() == null) {
						os.writeByte(NULL);
					} else {
						os.writeByte(STRING);
						os.writeUTF(selectArray[i].getDisplayName());
					}
				}
			}
			break;
		case DATETIME_ARRAY:
			Timestamp[] tsArray = (Timestamp[]) value;
			os.writeInt(tsArray.length);
			for (int i = 0; i< tsArray.length; i++) {
				if (tsArray[i] == null) {
					os.writeByte(NULL);
				} else {
					os.writeByte(DATETIME);
					os.writeLong(tsArray[i].getTime());
					os.writeInt(tsArray[i].getNanos());
				}
			}
			break;
		case DATE_ARRAY:
			Date[] dateArray = (Date[]) value;
			os.writeInt(dateArray.length);
			for (int i = 0; i< dateArray.length; i++) {
				if (dateArray[i] == null) {
					os.writeByte(NULL);
				} else {
					os.writeByte(DATE);
					os.writeLong(dateArray[i].getTime());
				}
			}
			break;
		case BOOLEAN_ARRAY:
			Boolean[] boolArray = (Boolean[]) value;
			os.writeInt(boolArray.length);
			for (int i = 0; i< boolArray.length; i++) {
				if (boolArray[i] == null) {
					os.writeByte(NULL);
				} else {
					os.writeByte(BOOLEAN);
					os.writeBoolean(boolArray[i].booleanValue());
				}
			}
			break;
		case BINARY_ARRAY:
			BinaryReference[] binArray = (BinaryReference[]) value;
			os.writeInt(binArray.length);
			for (int i = 0; i< binArray.length; i++) {
				if (binArray[i] == null) {
					os.writeByte(NULL);
				} else {
					os.writeByte(BINARY);
					os.writeLong(binArray[i].getLobId());
					if (binArray[i].getName() == null) {
						os.writeByte(NULL);
					} else {
						os.writeByte(STRING);
						os.writeUTF(binArray[i].getName());
					}
				}
			}
			break;
		case DECIMAL_ARRAY:
			BigDecimal[] decArray = (BigDecimal[]) value;
			os.writeInt(decArray.length);
			for (int i = 0; i< decArray.length; i++) {
				if (decArray[i] == null) {
					os.writeByte(NULL);
				} else {
					os.writeByte(DECIMAL);
					os.writeUTF(decArray[i].toString());
				}
			}
			break;
		case TIME_ARRAY:
			Time[] timeArray = (Time[]) value;
			os.writeInt(timeArray.length);
			for (int i = 0; i< timeArray.length; i++) {
				if (timeArray[i] == null) {
					os.writeByte(NULL);
				} else {
					os.writeByte(TIME);
					os.writeLong(timeArray[i].getTime());
				}
			}
			break;
		default:
			break;
		}
	}

	public static byte typeOf(Object value, boolean strictCheck) {
		if (value == null) {
			return NULL;
		}

		Byte bt = typeMap.get(value.getClass());
		if (bt != null) {
			return bt.byteValue();
		}

		if (strictCheck) {
			throw new IllegalArgumentException("not support type:" + value);
		} else {
			return NULL;
		}
	}

}
