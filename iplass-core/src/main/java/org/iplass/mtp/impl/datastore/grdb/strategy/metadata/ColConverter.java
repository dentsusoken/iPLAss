/*
 * Copyright (C) 2015 DENTSU SOKEN INC. All Rights Reserved.
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

import java.sql.Types;
import java.util.ArrayList;

import org.iplass.mtp.impl.entity.property.PropertyType;
import org.iplass.mtp.impl.properties.basic.BooleanType;
import org.iplass.mtp.impl.properties.basic.DateTimeType;
import org.iplass.mtp.impl.properties.basic.DateType;
import org.iplass.mtp.impl.properties.basic.DecimalType;
import org.iplass.mtp.impl.properties.basic.StringType;
import org.iplass.mtp.impl.properties.basic.TimeType;
import org.iplass.mtp.impl.properties.extend.AutoNumberType;
import org.iplass.mtp.impl.properties.extend.SelectType;
import org.iplass.mtp.impl.properties.extend.LongTextType.LongText;
import org.iplass.mtp.impl.rdb.adapter.BaseRdbTypeAdapter;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.function.FunctionAdapter;
import org.iplass.mtp.spi.ServiceRegistry;

/**
 * Colに保存されているデータを変換するクラス。
 *
 * 以下のような変換イメージ<br>
 *
 * 相互変換可<br>
　 NUM_G	DECIMAL(*)<->DECIMAL(*)<->INTEGER<->FLOAT<br>
　 DT_G	DATE<->DATETIME<br>
　 TM_G	DATETIME<->TIME<br>
　 STR_G	AUTONUMBER<->STRING<br>
　 <br>
 * 一方向<br>
　	*->STR_G->LONGTEXT<br>
　	BOOLEAN->SELECT<br>
 *
 * @author K.Higuchi
 *
 */
public abstract class ColConverter {
	
	private static ColConverterFactory factory = ServiceRegistry.getRegistry().getService(ColConverterFactory.class);

	protected PropertyType from;
	protected PropertyType to;

	ColConverter(PropertyType from, PropertyType to) {
		this.from = from;
		this.to = to;
	}

	public abstract void appendConvertExp(StringBuilder sb, String fromCol, RdbAdapter rdb);

	public abstract boolean canUseSameCol();

	public static class NoneColConverter extends ColConverter {
		public NoneColConverter(PropertyType from, PropertyType to) {
			super(from, to);
		}
		@Override
		public void appendConvertExp(StringBuilder sb, String fromCol,
				RdbAdapter rdb) {
			sb.append(fromCol);
		}
		@Override
		public boolean canUseSameCol() {
			return true;
		}
	}

	public static class LongTextColConverter extends ColConverter {

		private ColConverter wrapped;

		LongTextColConverter(PropertyType from, PropertyType to) {
			super(from, to);
			wrapped = factory.getColConverter(from, new StringType());
		}

		@Override
		public void appendConvertExp(StringBuilder sb, String fromCol,
				RdbAdapter rdb) {

			StringBuilder inner = new StringBuilder();
			FunctionAdapter fa = rdb.resolveFunction("CONCAT");
			ArrayList<CharSequence> arg = new ArrayList<>(2);
			StringBuilder arg1 = new StringBuilder();
			arg1.append('\'');
			LongText.appendLongTextFromStringExpression(arg1);
			arg1.append('\'');
			arg.add(arg1);
			StringBuilder arg2 = new StringBuilder();
			wrapped.appendConvertExp(arg2, fromCol, rdb);
			arg.add(arg2);

			fa.toSQL(inner, arg, rdb);

			//サイズを超える文字をtruncateするため
			//FIXME mysqlの場合、truncateされない。cast(... as CHAR)なので。ちゃんと文字数を設定ファイルより取得してtrimする。
			sb.append(rdb.cast(Types.VARCHAR, Types.VARCHAR, inner, null, null));

		}

		@Override
		public boolean canUseSameCol() {
			return to.equals(from);
		}

	}

	public static class CastColConverter extends ColConverter {

		CastColConverter(PropertyType from, PropertyType to) {
			super(from, to);
		}
		@Override
		public void appendConvertExp(StringBuilder sb, String fromCol, RdbAdapter rdb) {

			BaseRdbTypeAdapter fromTypeAdapter = rdb.getRdbTypeAdapter(from);
			BaseRdbTypeAdapter toTypeAdapter = rdb.getRdbTypeAdapter(to);

			StringBuilder fromColSb = new StringBuilder(fromCol.length() + 16);
			fromTypeAdapter.appendFromTypedCol(fromColSb, rdb,
					() -> fromColSb.append(fromCol));
			Integer scale;
			if (to instanceof DecimalType) {
				scale = ((DecimalType) to).getScale();
			} else {
				scale = null;
			}
			toTypeAdapter.appendToTypedCol(sb, rdb,
					() -> sb.append(rdb.cast(fromTypeAdapter.sqlType(), toTypeAdapter.sqlType(), fromColSb, null, scale)));
		}
		@Override
		public boolean canUseSameCol() {
			//FIXME PropertyType#getDataStoreEnumTypeで判断できるように
			
			switch (to.getEnumType()) {
			case DECIMAL:
				if (from instanceof DecimalType) {
					DecimalType toDec = (DecimalType) to;
					DecimalType fromDec = (DecimalType) from;
					return toDec.getScale() == fromDec.getScale();
				}
				return false;
			case SELECT:
				if (from instanceof SelectType) {
					return true;
				}
				if (from instanceof BooleanType) {
					return true;
				}
				return false;
			case DATETIME:
				if (from instanceof DateTimeType) {
					return true;
				}
				if (from instanceof DateType) {
					return true;
				}
				if (from instanceof TimeType) {
					return true;
				}
				return false;
			case AUTONUMBER:
				if (from instanceof AutoNumberType) {
					return true;
				}
				if (from instanceof StringType) {
					return true;
				}
				return false;
			case STRING:
				switch (from.getEnumType()) {
					case AUTONUMBER:
					case BOOLEAN:
					case SELECT:
					case STRING:
						return true;
					default:
						return false;
				}
			default:
				return to.equals(from);
			}
		}
	}

}
