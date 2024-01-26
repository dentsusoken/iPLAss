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

import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.entity.query.ASTNode;
import org.iplass.mtp.entity.query.ASTTransformer;
import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.entity.query.value.ValueExpressionVisitor;

/**
 * 関数を表す。
 * Oracle、MySQL、Postgresql共通で次のファンクションを利用可能。
 *
 * <h5>文字列関数</h5>
 * <ul>
 * <li>REPLACE('変換対象文字列','変換前文字','変換後文字')　：String　：文字列を置換する</li>
 * <li>UPPER('変換対象文字列')　：String　：文字列を大文字変換する</li>
 * <li>LOWER('変換対象文字列')　：String　：文字列を小文字変換する</li>
 * <li>CONCAT('結合対象文字列1','結合対象文字列2')　：String　：文字列を結合する</li>
 * <li>SUBSTR('文字列',開始文字index,切り出す文字列の長さ)　：String　：文字列を切り出す。indexは1始まり。切り出す文字列の長さは省略可能</li>
 * <li>INSTR('文字列','検索文字列')　：Long　：検索文字列が最初に出現したindexを返す。indexは1始まり。</li>
 * <li>CHAR_LENGTH('文字列')　：Long　：文字列の長さを取得する</li>
 * </ul>
 *
 * <h5>数値関数</h5>
 * <ul>
 * <li>MOD(数値式,割る数)　：Double/BigDecimal/Long　：余りを計算する。引数の数値の型により返却される型は異なる</li>
 * <li>SQRT(数値式)　：Double　：平方根を計算する</li>
 * <li>POWER(数値式,指数)　：Double/BigDecimal/Long　：冪乗を計算する。引数の数値の型により返却される型は異なる</li>
 * <li>ABS(数値式)　：Double/BigDecimal/Long　：絶対値を計算する。引数の数値の型により返却される型は異なる</li>
 * <li>CEIL(数値式)　：Long　：小数部を切り上げする</li>
 * <li>FLOOR(数値式)　：Long　：小数部を切り下げする。TRUNCATEとの違いは、数値式が負数の場合の処理。</li>
 * <li>ROUND(数値式,小数位)　：BigDecimal/Long　：数値式を四捨五入（数値式がDouble型の場合は銀行丸め処理）する。引数の数値の型により返却される型は異なる。小数位の指定が、0以下の場合はLong</li>
 * <li>TRUNCATE(数値式,小数位)　：BigDecimal/Long　：数値式を切り捨てする。引数の数値の型により返却される型は異なる。小数位の指定が、0以下の場合はLong</li>
 * <li>SIN(数値式)　：Double　：サインを計算する</li>
 * <li>COS(数値式)　：Double　：コサインを計算する</li>
 * <li>TAN(数値式)　：Double　：タンジェントを計算する</li>
 * <li>ASIN(数値式)　：Double　：アークサインを計算する</li>
 * <li>ACOS(数値式)　：Double　：アークコサインを計算する</li>
 * <li>ATAN(数値式)　：Double　：アークタンジェントを計算する</li>
 * <li>ATAN2(数値式,数値式)　：Double　：y/xのアーク・タンジェントを計算する</li>
 *
 * </ul>
 *
 * <h5>日付関数</h5>
 * <ul>
 * <li>YEAR(日付)　：Long　：日付の年を取得する</li>
 * <li>MONTH(日付)　：Long　：日付の月（1~12）を取得する</li>
 * <li>DAY(日付)　：Long　：日付の日（1~31）を取得する</li>
 * <li>HOUR(日付)　：Long　：日付の時間（0~23）を取得する</li>
 * <li>MINUTE(日付)　：Long　：日付の分（0~59）を取得する</li>
 * <li>SECOND(日付)　：Long　：日付の秒（0~59）を取得する</li>
 * <li>DATE_ADD(日付,加算する値,単位)　：Timestamp　：指定の日付に指定の値を加算する。単位には'YEAR','MONTH','DAY','HOUR','MINUTE','SECOND'を指定可能。例：DATE_ADD('2012-12-12'D, 5, 'DAY')</li>
 * <li>DATE_DIFF(単位,日付1,日付2)　：Long　：日付1と日付2の差分を指定の単位で取得する。日付1の方が大きい場合、結果は負の値が返却される。単位には'YEAR','MONTH','DAY','HOUR','MINUTE','SECOND'を指定可能。例：DATE_DIFF('DAY', '2012-12-12'D, '2012-12-31'D)</li>
 * <li>CURRENT_DATE()　：Date　：現在の日付（年月日）を取得する</li>
 * <li>CURRENT_TIME()　：Time　：現在の時刻を取得する</li>
 * <li>CURRENT_DATETIME()　：Timestamp　：現在の日時を取得する</li>
 * <li>LOCALTIME(日時)　：Timestamp　：日時をテナントに設定されたローカル時間を示す値に変換する。例（テナントローカルのタイムゾーンの時間を取得）：HOUR(LOCALTIME(CURRENT_DATETIME()))</li>
 *
 * </ul>
 *
 * @author K.Higuchi
 *
 */
public class Function extends PrimaryValue {
	private static final long serialVersionUID = -5685250677990276754L;

	private String name;
	private List<ValueExpression> arguments;


	public Function() {
	}

	public Function(String name, List<ValueExpression> arguments) {
		this.name = name;
		this.arguments = arguments;
	}

	public Function(String name, ValueExpression... arguments) {
		this.name = name;
		if (arguments != null) {
			this.arguments = new ArrayList<ValueExpression>();
			for (ValueExpression ve: arguments) {
				this.arguments.add(ve);
			}
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ValueExpression> getArguments() {
		return arguments;
	}

	public void setArguments(List<ValueExpression> arguments) {
		this.arguments = arguments;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(name);
		sb.append("(");
		if (arguments != null) {
			for (int i = 0; i < arguments.size(); i++) {
				if (i != 0) {
					sb.append(",");
				}
				sb.append(arguments.get(i));
			}
		}
		sb.append(")");
		return sb.toString();
	}

	@Override
	public void accept(ValueExpressionVisitor visitor) {
		if (visitor.visit(this)) {
			if (getArguments() != null) {
				for (ValueExpression v: getArguments()) {
					v.accept(visitor);
				}
			}
		}
	}

	@Override
	public ASTNode accept(ASTTransformer transformer) {
		return transformer.visit(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((arguments == null) ? 0 : arguments.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Function other = (Function) obj;
		if (arguments == null) {
			if (other.arguments != null)
				return false;
		} else if (!arguments.equals(other.arguments))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
