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
package org.iplass.mtp.web.template;

import org.iplass.mtp.entity.BinaryReference;
import org.iplass.mtp.entity.SelectValue;
import org.iplass.mtp.impl.util.ConvertUtil;

/**
 * バインドされるプロパティの値を文字列にフォーマット処理するためのインタフェースです。
 * 
 * @author K.Higuchi
 *
 */
public interface ValueFormatter /* extends Function<Object, String> */ {
	
	/**
	 * デフォルトのValueFormatterです。
	 * valueが文字列以外の場合、次のようにフォーマットします。
	 * <ul>
	 * <li>Integer、Double、BiｇDecimalなどの数値型<br>
	 * 数値を10進数表現で文字列に変換します。
	 * </li>
	 * <li>{@link SelectValue}型<br>
	 * SelectValueのvalueを出力します。
	 * </li>
	 * <li>{@link BinaryReference}型<br>
	 * BinaryReferenceのlobIdを出力します。
	 * </li>
	 * <li>{@link java.sql.Date}型<br>
	 * yyyy-MM-dd形式で出力します。
	 * </li>
	 * <li>{@link java.sql.Time}型<br>
	 * HH:mm:ss形式で出力します。
	 * </li>
	 * <li>{@link java.sql.Timestamp}型もしくは、java.sql.Date、java.sql.Time以外のjava.utilDate型<br>
	 * yyyy-MM-dd'T'HH:mm:ss.SSSXXX形式で出力します。
	 * </li>
	 * </ul>
	 * 
	 */
	public static final ValueFormatter DEFAULT_FORMATTER = new ValueFormatter() {
		@Override
		public String apply(Object value) {
			return ConvertUtil.convertToString(value);
		}
	};
	
	/**
	 * 引数で指定されたvalueをフォーマットして返すように実装します。
	 * 
	 * @param value フォーマット対象のプロパティ値
	 * @return フォーマットされた文字列
	 */
	public String apply(Object value);
}
