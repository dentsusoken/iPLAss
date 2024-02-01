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

package org.iplass.mtp.entity.query.value.primary;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import org.iplass.mtp.entity.SelectValue;
import org.iplass.mtp.entity.query.ASTNode;
import org.iplass.mtp.entity.query.ASTTransformer;
import org.iplass.mtp.entity.query.QueryException;
import org.iplass.mtp.entity.query.value.ValueExpressionVisitor;
import org.iplass.mtp.impl.util.ConvertUtil;
import org.iplass.mtp.util.StringUtil;

/**
 * リテラルを表す。
 *
 * EQL上での各プロパティのリテラル表現は以下の形。
 * <table border=1>
 * <tr>
 * <th>プロパティ型</th><th>リテラル表現</th><th>例</th>
 * </tr>
 * <tr>
 * <td>String</td><td>'で囲まれた文字列表現。'は''でエスケープ</td><td>'aaaテスト文字'<br>'I''m a cat.'</td>
 * </tr>
 * <tr>
 * <td>Integer</td><td>数値表現のサフィックスとして、Iもしくはiを指定。もしくは、小数部指定を含まない数値表現。</td><td>1234I<br>10i<br>54</td>
 * </tr>
 * <tr>
 * <td>Float</td><td>数値表現のサフィックスとして、Fもしくはfを指定。もしくは、小数部指定を含む数値表現。</td><td>1234.123F<br>0.12f<br>10.5</td>
 * </tr>
 * <tr>
 * <td>Decimal</td><td>数値表現のサフィックスとして、Gもしくはgを指定。</td><td>1234.123G<br>10001g</td>
 * </tr>
 * <tr>
 * <td>Boolean</td><td>true もしくは false。大文字も可。</td><td>true<br>FALSE</td>
 * </tr>
 * <tr>
 * <td>Select</td><td>SelectValueのvalueを文字列して指定。サフィックスとしてSもしくはsを指定。</td><td>'A01'S<br>'1's</td>
 * </tr>
 * <tr>
 * <td>Date</td><td>'yyyy-MM-dd'形式の日付表現文字列。サフィックスとしてDもしくはdを指定。</td><td>'2012-12-11'D<br>'2011-11-15'd</td>
 * </tr>
 * <tr>
 * <td>Time</td><td>'HH:mm:ss'形式の日付表現文字列。サフィックスとしてTもしくはtを指定。</td><td>'03:01:00'T<br>'18:24:15't</td>
 * </tr>
 * <tr>
 * <td>DateTime</td><td>'yyyy-MM-dd HH:mm:ss.SSS'（タイムゾーン指定する場合：'yyyy-MM-dd HH:mm:ss.SSSXXX'）形式の日付表現文字列。<br>サフィックスとしてMもしくはmを指定。<br>タイムゾーン指定がない場合は、テナントローカルに設定されているタイムゾーンと判断する。</td><td>'2011-11-15 16:03:01.123'M<br>'2010-01-30 01:25:01.200'm<br>'2010-01-30 01:25:01.200+09:00'M</td>
 * </tr>
 * </table>
 * <p>
 * Literalはまた、当該値がネイティブのクエリーに変換される際にバインド変数として扱うかどうかのフラグbindableを持つ。Select文先頭のヒント句でbindが有効化された場合、
 * 当該フラグがtrueのものはバインド変数化されてクエリーされる。ただし実際の値がnullの場合はフラグをtrueにセットしてもバインドされない。
 * デフォルトはtrue<br>
 * bindableをfalseに設定するEQL上の文法は、リテラル値の前に/*+no_bind&#42;/をつける。<br>
 * 記述例：<br>
 * /*+no_bind&#42;/15<br>
 * /*+no_bind&#42;/'hoge'<br>
 * /*+ no_bind &#42;/'A01'S<br>
 * </p>
 *
 * @author K.Higuchi
 *
 */
public class Literal extends PrimaryValue {
	private static final long serialVersionUID = 468081182204820851L;

	/** EQL上のDate型の文字列表現時のフォーマット（SimpleDateFormat形式） */
	public static final String DATE_FROMAT = "yyyy-MM-dd";

	/** EQL上のTime型の文字列表現時のフォーマット（SimpleDateFormat形式） */
	public static final String TIME_FROMAT = "HH:mm:ss";

	/** EQL上のDateTime型の文字列表現時のフォーマット。タイムゾーン未指定の場合（SimpleDateFormat形式）。テナントローカルのタイムゾーンで処理される */
	public static final String DATE_TIME_FROMAT = "yyyy-MM-dd HH:mm:ss.SSS";

	/** EQL上のDateTime型の文字列表現時のフォーマット。タイムゾーン指定ありの場合（SimpleDateFormat形式） */
	public static final String DATE_TIME_WITH_TZ_FROMAT = "yyyy-MM-dd HH:mm:ss.SSSXXX";
	
	public static final String NO_BIND_HINT = "/*+no_bind*/";
	
	private Object value;
	private boolean bindable = true;

	public Literal() {
	}

	public Literal(Object value) {
		this.value = value;
		checkValidLiteral();
	}

	public Literal(Object value, boolean bindable) {
		this.value = value;
		this.bindable = bindable;
		checkValidLiteral();
	}
	
	private void checkValidLiteral() {
		if (value instanceof ASTNode) {
			throw new QueryException(value + " [type:" + value.getClass().getName() + "] is not valid Literal.");
		}
	}
	
	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
		checkValidLiteral();
	}
	
	public boolean isBindable() {
		return bindable;
	}

	public void setBindable(boolean bindable) {
		this.bindable = bindable;
	}


	public void accept(ValueExpressionVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public String toString() {
		if (bindable) {
			if (value == null) {
				return "null";
			} else if (value instanceof String) {
				return "'" + StringUtil.escapeEql((String) value) + "'";
			} else if (value instanceof Date) {
				return "'" + ConvertUtil.formatDate((Date) value, DATE_FROMAT, false) + "'D";
			} else if (value instanceof Timestamp) {
				return "'" + ConvertUtil.formatDate((Timestamp) value, DATE_TIME_FROMAT, true) + "'M";
			} else if (value instanceof Time) {
				return "'" + ConvertUtil.formatDate((Time) value, TIME_FROMAT, false) + "'T";
			} else if (value instanceof SelectValue) {
				return "'" + ((SelectValue) value).getValue() + "'S";
			} else if (value instanceof BigDecimal) {
				return ((BigDecimal) value).toPlainString() + "G";
			} else {
				return value.toString();
			}
		} else {
			if (value == null) {
				return NO_BIND_HINT + "null";
			} else if (value instanceof String) {
				return NO_BIND_HINT + "'" + StringUtil.escapeEql((String) value) + "'";
			} else if (value instanceof Date) {
				return NO_BIND_HINT + "'" + ConvertUtil.formatDate((Date) value, DATE_FROMAT, false) + "'D";
			} else if (value instanceof Timestamp) {
				return NO_BIND_HINT + "'" + ConvertUtil.formatDate((Timestamp) value, DATE_TIME_FROMAT, true) + "'M";
			} else if (value instanceof Time) {
				return NO_BIND_HINT + "'" + ConvertUtil.formatDate((Time) value, TIME_FROMAT, false) + "'T";
			} else if (value instanceof SelectValue) {
				return NO_BIND_HINT + "'" + ((SelectValue) value).getValue() + "'S";
			} else if (value instanceof BigDecimal) {
				return NO_BIND_HINT + ((BigDecimal) value).toPlainString() + "G";
			} else {
				return NO_BIND_HINT + value.toString();
			}
		}
	}

	public ASTNode accept(ASTTransformer transformer) {
		return transformer.visit(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (bindable ? 1231 : 1237);
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		Literal other = (Literal) obj;
		if (bindable != other.bindable)
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

}
