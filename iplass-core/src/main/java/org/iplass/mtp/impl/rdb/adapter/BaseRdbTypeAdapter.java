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

package org.iplass.mtp.impl.rdb.adapter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iplass.mtp.entity.EntityRuntimeException;
import org.iplass.mtp.impl.entity.property.PropertyService;
import org.iplass.mtp.impl.entity.property.PropertyType;
import org.iplass.mtp.impl.properties.basic.DecimalType;
import org.iplass.mtp.spi.ServiceRegistry;


public abstract class BaseRdbTypeAdapter implements RdbTypeAdapter {

	private static Map<Class<? extends BaseRdbTypeAdapter>, BaseRdbTypeAdapter> map;

	static {
		PropertyService ps = ServiceRegistry.getRegistry().getService(PropertyService.class);
		map = new HashMap<Class<? extends BaseRdbTypeAdapter>, BaseRdbTypeAdapter>();
		map.put(Null.class, new Null());
		map.put(DateTime.class, new DateTime(ps.getPropertyType(Timestamp.class)));
		map.put(Date.class, new Date(ps.getPropertyType(java.sql.Date.class)));
		map.put(Time.class, new Time(ps.getPropertyType(java.sql.Time.class)));
//		map.put(Decimal.class, new Decimal());
		map.put(Float.class, new Float(ps.getPropertyType(Double.class)));
		map.put(Integer.class, new Integer(ps.getPropertyType(Long.class)));
		map.put(Varchar.class, new Varchar(ps.getPropertyType(String.class)));
		map.put(Bool.class, new Bool(ps.getPropertyType(Boolean.class)));
	}

	public static BaseRdbTypeAdapter getInstance(Class<? extends BaseRdbTypeAdapter> type) {
		return map.get(type);
	}

	public static List<BaseRdbTypeAdapter> values() {
		List<BaseRdbTypeAdapter> values = new ArrayList<BaseRdbTypeAdapter>();
		for (BaseRdbTypeAdapter adaptor : map.values()) {
			values.add(adaptor);
		}
		return values;
	}

	protected PropertyType propertyType;

	public BaseRdbTypeAdapter(PropertyType propertyType) {
		this.propertyType = propertyType;
	}


	public int allocateColumnCount() {
		return 1;
	}





	//TODO 以下のメソッドの整理が必要

	//DBにストアされている文字列からこのタイプにする際、castが必要か？
//	public abstract boolean needCastExpFromString();

	public abstract int sqlType();

	public Object toDataStore(Object javaTypeValue) {
		return toRdb(propertyType.toDataStore(javaTypeValue));
	}

	public Object fromDataStore(ResultSet rs, int columnIndex) throws SQLException {
		return propertyType.fromDataStore(toJava(rs, columnIndex));
	}

	protected abstract Object toRdb(Object javaTypeValue);

	protected abstract Object toJava(ResultSet rs, int columnIndex) throws SQLException;

	public abstract String getColOfIndex();

	public void appendToSqlAsRealType(Object javaTypeValue, StringBuilder context, RdbAdapter rdb) {
		context.append(rdb.toSqlExp(toDataStore(javaTypeValue)));
	}
	
	public abstract void setParameter(int index, Object javaTypeValue, PreparedStatement stmt) throws SQLException;

	public void appendToTypedCol(StringBuilder context, RdbAdapter rdb, ValueHandleLogic callback) {
		callback.run();
	}

	public void appendFromTypedCol(StringBuilder context, RdbAdapter rdb, ValueHandleLogic callback) {
		callback.run();
	}

	public void appendToTypedColForPrepare(StringBuilder context, RdbAdapter rdb) {
		context.append("?");
	}

	public interface ValueHandleLogic {
		public void run();
	}

	public static class Null extends BaseRdbTypeAdapter {

		public Null() {
			super(null);
		}

		@Override
		public Object fromDataStore(ResultSet rs, int columnIndex)
				throws SQLException {
			return null;
		}

		@Override
		public Object toDataStore(Object javaTypeValue) {
			return null;
		}

		@Override
		public String getColOfIndex() {
			return null;
		}

		@Override
		public Object toJava(ResultSet rs, int columnIndex) throws SQLException {
			return null;
		}

		@Override
		public Object toRdb(Object javaTypeValue) {
			return null;
		}

		@Override
		public int sqlType() {
			return Types.NULL;
		}

		@Override
		public void appendToSqlAsRealType(Object javaTypeValue, StringBuilder context, RdbAdapter rdb) {
			context.append("NULL");
		}

		@Override
		public void setParameter(int index, Object javaTypeValue,
				PreparedStatement stmt) throws SQLException {
			stmt.setNull(index, Types.NULL);
		}

	}

	public static class DateTime extends BaseRdbTypeAdapter {

		public DateTime(PropertyType propertyType) {
			super(propertyType);
		}

		@Override
		public Timestamp toJava(ResultSet rs, int columnIndex)
				throws SQLException {
			return rs.getTimestamp(columnIndex);
		}

		@Override
		public Timestamp toRdb(Object javaTypeValue) {
			if (javaTypeValue == null) {
				return null;
			}
			if (javaTypeValue instanceof Timestamp) {
				return (Timestamp) javaTypeValue;
			}
			if (javaTypeValue instanceof java.util.Date) {
				return new Timestamp(((java.util.Date) javaTypeValue).getTime());
			}

			try {
				return new Timestamp(DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM).parse(javaTypeValue.toString()).getTime());
			} catch (ParseException e) {
				throw new EntityRuntimeException("can not convert to Date:" + javaTypeValue);
			}
		}

		@Override
		public void appendToSqlAsRealType(Object javaTypeValue, StringBuilder context, RdbAdapter rdb) {
			Timestamp ts = toRdb(javaTypeValue);
			if (ts == null) {
				context.append("null");
			} else {
				context.append(rdb.toTimeStampExpression(ts));
			}
		}

		@Override
		public String getColOfIndex() {
			return "TS";
		}

		@Override
		public int sqlType() {
			return Types.TIMESTAMP;
		}

		@Override
		public void setParameter(int index, Object javaTypeValue,
				PreparedStatement stmt) throws SQLException {
			Timestamp val = toRdb(javaTypeValue);
			stmt.setTimestamp(index, val);
		}
	}

	public static class Date extends BaseRdbTypeAdapter {

		public Date(PropertyType propertyType) {
			super(propertyType);
		}

		@Override
		public java.sql.Date toJava(ResultSet rs, int columnIndex)
				throws SQLException {
			return rs.getDate(columnIndex);
		}

		@Override
		public java.sql.Date toRdb(Object javaTypeValue) {
			if (javaTypeValue == null) {
				return null;
			}
			if (javaTypeValue instanceof java.sql.Date) {
				return (java.sql.Date) javaTypeValue;
			}
			if (javaTypeValue instanceof java.util.Date) {
				return new java.sql.Date(((java.util.Date) javaTypeValue).getTime());
			}

			try {
				return new java.sql.Date(DateFormat.getDateInstance(DateFormat.MEDIUM).parse(javaTypeValue.toString()).getTime());
			} catch (ParseException e) {
				throw new EntityRuntimeException("can not convert to Date:" + javaTypeValue);
			}
		}

		@Override
		public void appendToSqlAsRealType(Object javaTypeValue, StringBuilder context, RdbAdapter rdb) {
			java.sql.Date date = toRdb(javaTypeValue);
			if (date == null) {
				context.append("null");
			} else {
				context.append(rdb.toDateExpression(date));
			}
		}
		
		

		@Override
		public String getColOfIndex() {
			return "DATE";
		}

		@Override
		public int sqlType() {
			return Types.DATE;
		}

		@Override
		public void setParameter(int index, Object javaTypeValue,
				PreparedStatement stmt) throws SQLException {
			java.sql.Date val = toRdb(javaTypeValue);
			stmt.setDate(index, val);
		}

	}

	public static class Decimal extends BaseRdbTypeAdapter {
		//TODO スケールの考慮のため、これだけはFlayweightできないかも。ある程度のスケール分はキャッシュする？

		public Decimal(PropertyType propertyType) {
			super(propertyType);
		}

		@Override
		public void appendFromTypedCol(StringBuilder context, RdbAdapter rdb,
				ValueHandleLogic callback) {

			DecimalType type = (DecimalType) propertyType;
			if (type.getScale() < 0) {
				context.append("1");
				for (int i = 0; i > type.getScale(); i--) {
					context.append("0");
				}
				context.append("*(");
				super.appendToTypedCol(context, rdb, callback);
				context.append(")");
			} else if (type.getScale() > 0) {
				context.append("(");
				super.appendToTypedCol(context, rdb, callback);
				context.append(")/");
				context.append("1");
				for (int i = 0; i < type.getScale(); i++) {
					context.append("0");
				}

			} else {
				super.appendToTypedCol(context, rdb, callback);
			}
		}


		public void appendToTypedColForPrepare(StringBuilder context, RdbAdapter rdb) {
			DecimalType type = (DecimalType) propertyType;
			if (type.getScale() > 0) {
				context.append("1");
				for (int i = 0; i < type.getScale(); i++) {
					context.append("0");
				}
				context.append("*(?)");
			} else if (type.getScale() < 0) {
				context.append("(?)/");
				context.append("1");
				for (int i = 0; i > type.getScale(); i--) {
					context.append("0");
				}
			} else {
				super.appendToTypedColForPrepare(context, rdb);
			}
		}

		@Override
		public void appendToTypedCol(StringBuilder context, RdbAdapter rdb,
				ValueHandleLogic callback) {
			DecimalType type = (DecimalType) propertyType;
			if (type.getScale() > 0) {
				context.append("1");
				for (int i = 0; i < type.getScale(); i++) {
					context.append("0");
				}
				context.append("*(");
				super.appendToTypedCol(context, rdb, callback);
				context.append(")");
			} else if (type.getScale() < 0) {
				context.append("(");
				super.appendToTypedCol(context, rdb, callback);
				context.append(")/");
				context.append("1");
				for (int i = 0; i > type.getScale(); i--) {
					context.append("0");
				}

			} else {
				super.appendToTypedCol(context, rdb, callback);
			}
		}

		@Override
		public BigDecimal toJava(ResultSet rs, int columnIndex)
				throws SQLException {
			BigDecimal dec = rs.getBigDecimal(columnIndex);
			if (dec != null && dec.scale() != ((DecimalType) propertyType).getScale()) {
				RoundingMode rm =  ((DecimalType) propertyType).getRoundingMode();
				if (rm == null) {
					rm = RoundingMode.HALF_EVEN;
				}
				dec = dec.setScale(((DecimalType) propertyType).getScale(), rm);
			}
			return dec;
		}

		@Override
		public BigDecimal toRdb(Object javaTypeValue) {
			return (BigDecimal) javaTypeValue;
		}

		@Override
		public String getColOfIndex() {
			return "NUM";
		}

		@Override
		public int sqlType() {
			return Types.DECIMAL;
		}

		@Override
		public void setParameter(int index, Object javaTypeValue,
				PreparedStatement stmt) throws SQLException {
			BigDecimal val = (BigDecimal) toDataStore(javaTypeValue);
			stmt.setBigDecimal(index, val);
		}
	}

	public static class Float extends BaseRdbTypeAdapter {

		public Float(PropertyType propertyType) {
			super(propertyType);
		}

		@Override
		public Double toJava(ResultSet rs, int columnIndex) throws SQLException {
			if (rs.getObject(columnIndex) == null) {
				return null;
			} else {
				return rs.getDouble(columnIndex);
			}
		}

		@Override
		public Double toRdb(Object javaTypeValue) {
			if (javaTypeValue == null) {
				return null;
			}
			if (javaTypeValue instanceof Double) {
				return (Double) javaTypeValue;
			}
			if (javaTypeValue instanceof Number) {
				return ((Number) javaTypeValue).doubleValue();
			}
			return Double.parseDouble(javaTypeValue.toString());
		}

		@Override
		public String getColOfIndex() {
			return "DBL";
		}

		@Override
		public int sqlType() {
			return Types.DOUBLE;
		}

		@Override
		public void setParameter(int index, Object javaTypeValue,
				PreparedStatement stmt) throws SQLException {
			Double val = toRdb(javaTypeValue);
			if (val == null) {
				stmt.setNull(index, sqlType());
			} else {
				stmt.setDouble(index, val);
			}
		}
	}

	public static class Integer extends BaseRdbTypeAdapter {

		public Integer(PropertyType propertyType) {
			super(propertyType);
		}

		@Override
		public Long toJava(ResultSet rs, int columnIndex) throws SQLException {
			if (rs.getObject(columnIndex) == null) {
				return null;
			} else {
				return rs.getLong(columnIndex);
			}
		}

		@Override
		public Long toRdb(Object javaTypeValue) {
			if (javaTypeValue == null) {
				return null;
			}
			if (javaTypeValue instanceof Long) {
				return (Long) javaTypeValue;
			}
			if (javaTypeValue instanceof Number) {
				return ((Number) javaTypeValue).longValue();
			}
			return Long.parseLong(javaTypeValue.toString());
		}

		@Override
		public String getColOfIndex() {
			return "NUM";
		}

		@Override
		public int sqlType() {
			return Types.BIGINT;
		}

		@Override
		public void setParameter(int index, Object javaTypeValue,
				PreparedStatement stmt) throws SQLException {
			Long val = toRdb(javaTypeValue);
			if (val == null) {
				stmt.setNull(index, sqlType());
			} else {
				stmt.setLong(index, val);
			}
		}
	}

	public static class Time extends BaseRdbTypeAdapter {

		public Time(PropertyType propertyType) {
			super(propertyType);
		}

		@Override
		public java.sql.Time toJava(ResultSet rs, int columnIndex)
				throws SQLException {
			return rs.getTime(columnIndex);
		}

		@Override
		public java.sql.Time toRdb(Object javaTypeValue) {
			if (javaTypeValue == null) {
				return null;
			}
			if (javaTypeValue instanceof java.sql.Time) {
				return (java.sql.Time) javaTypeValue;
			}
			if (javaTypeValue instanceof java.util.Date) {
				return new java.sql.Time(((java.util.Date) javaTypeValue).getTime());
			}

			return java.sql.Time.valueOf(javaTypeValue.toString());
		}

		@Override
		public void appendToSqlAsRealType(Object javaTypeValue, StringBuilder context, RdbAdapter rdb) {
			java.sql.Time val = toRdb(javaTypeValue);
			if (val == null) {
				context.append("null");
			} else {
				context.append(rdb.toTimeExpression(val));
			}

		}

		@Override
		public String getColOfIndex() {
			return "TS";
		}

		@Override
		public int sqlType() {

			return Types.TIME;
		}

		@Override
		public void setParameter(int index, Object javaTypeValue,
				PreparedStatement stmt) throws SQLException {
			java.sql.Time val = toRdb(javaTypeValue);
			if (val != null) {
				//日付部分を切り捨て
				SimpleDateFormat fmt = new SimpleDateFormat("HH:mm:ss");
				val = java.sql.Time.valueOf(fmt.format(val));
			}

			stmt.setTime(index, val);
		}
	}

	public static class Varchar extends BaseRdbTypeAdapter {

		public Varchar(PropertyType propertyType) {
			super(propertyType);
		}

		@Override
		public String toJava(ResultSet rs, int columnIndex) throws SQLException {
			return rs.getString(columnIndex);
//			return rs.getNString(columnIndex);
		}

		@Override
		public String toRdb(Object javaTypeValue) {
			if (javaTypeValue == null) {
				return null;
			}
			return javaTypeValue.toString();
		}

		@Override
		public void appendToSqlAsRealType(Object javaTypeValue, StringBuilder context, RdbAdapter rdb) {
			String val = toRdb(javaTypeValue);
			if (val == null) {
				context.append("null");
			} else {
				context.append("'").append(rdb.sanitize(val)).append("'");
			}
		}


		@Override
		public String getColOfIndex() {
			return "STR";
		}

		@Override
		public int sqlType() {
			return Types.VARCHAR;
		}

		@Override
		public void setParameter(int index, Object javaTypeValue,
				PreparedStatement stmt) throws SQLException {
			String val = toRdb(javaTypeValue);
			stmt.setString(index, val);
		}

	}

	public static class Bool extends BaseRdbTypeAdapter {

		public Bool(PropertyType propertyType) {
			super(propertyType);
		}

		@Override
		public Boolean toJava(ResultSet rs, int columnIndex) throws SQLException {
			String val = rs.getString(columnIndex);

			if (val == null) {
				return null;
			}
			if (val.length() == 0) {
				return null;
			}
			if (val.equals("0")) {
				return Boolean.FALSE;
			}
			if (val.equals("1")) {
				return Boolean.TRUE;
			}

			return null;
		}

		@Override
		public String toRdb(Object javaTypeValue) {
			if (javaTypeValue == null) {
				return null;
			}
			Boolean val = null;
			if (javaTypeValue instanceof Boolean) {
				val = (Boolean) javaTypeValue;
			} else {
				val =  Boolean.valueOf(javaTypeValue.toString());
			}

			if (val.booleanValue() == false) {
				return "0";
			} else {
				return "1";
			}
		}

		@Override
		public void appendToSqlAsRealType(Object javaTypeValue, StringBuilder context, RdbAdapter rdb) {
			String val = toRdb(javaTypeValue);
			if (val == null) {
				context.append("null");
			} else {
				context.append("'").append(rdb.sanitize(val)).append("'");
			}
		}
		@Override
		public String getColOfIndex() {
			return "STR";
		}

		@Override
		public int sqlType() {
			return Types.VARCHAR;
		}

		@Override
		public void setParameter(int index, Object javaTypeValue,
				PreparedStatement stmt) throws SQLException {
			String val = toRdb(javaTypeValue);
			stmt.setString(index, val);
		}
	}

}
