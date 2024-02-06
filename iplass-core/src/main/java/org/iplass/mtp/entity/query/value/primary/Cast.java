/*
 * Copyright (C) 2013 DENTSU SOKEN INC. All Rights Reserved.
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

import org.iplass.mtp.entity.definition.PropertyDefinitionType;
import org.iplass.mtp.entity.query.ASTNode;
import org.iplass.mtp.entity.query.ASTTransformer;
import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.entity.query.value.ValueExpressionVisitor;

/**
 * Cast関数を表す。
 * <br>
 * 文法：cast([valueExpression] as [type])<br>
 * 記述例：select cast(propA as integer) from SampleEntity<br>
 * Castのtypeとして指定可能な値は以下（Case Insensitive）。
 * <table border=1>
 * <tr>
 * <th>type</th><th>説明</th><th>変換元として可能な型</th>
 * </tr>
 * <tr>
 * <td>STRING</td><td>それぞれの値を文字列として変換。型引数で文字列長の指定が任意で可能。</td><td>すべての型</td>
 * </tr>
 * <tr>
 * <td>INTEGER</td><td>整数型に変換。文字列からは変換失敗する場合もある</td><td>STRING,INTEGER,FLOAT,DECIMAL,BOOLEAN</td>
 * </tr>
 * <tr>
 * <td>FLOAT</td><td>浮動小数点に変換。文字列からは変換失敗する場合もある</td><td>STRING,INTEGER,FLOAT,DECIMAL,BOOLEAN</td>
 * </tr>
 * <tr>
 * <td>DECIMAL</td><td>固定小数点に変換。文字列からは変換失敗する場合もある。型引数でscaleの指定が任意で可能。</td><td>STRING,INTEGER,FLOAT,DECIMAL,BOOLEAN</td>
 * </tr>
 * <tr>
 * <td>BOOLEAN</td><td>真偽値に変換。0 -> false, 1 -> true, それ以外nullに変換</td><td>STRING,INTEGER,FLOAT,DECIMAL,BOOLEAN</td>
 * </tr>
 * <tr>
 * <td>SELECT</td><td>SelectValueに変換。SelectValueのvalueにcast前の値の文字列表現をセット。displayNameはnull。<br>※select項目としてのみ利用可能</td><td>すべての型</td>
 * </tr>
 * <tr>
 * <td>DATE</td><td>日付型に変換。TIMEを変換した場合は、1970/1/1となる。文字列からは変換失敗する場合もある</td><td>STRING,DATE,DATETIME,TIME</td>
 * </tr>
 * <tr>
 * <td>TIME</td><td>時間型に変換。文字列からは変換失敗する場合もある</td><td>STRING,DATE,TIME,DATETIME</td>
 * </tr>
 * <tr>
 * <td>DATETIME</td><td>日時型に変換。文字列からは変換失敗する場合もある</td><td>STRING,DATE,TIME,DATETIME</td>
 * </tr>
 * </table>
 * 
 * @author K.Higuchi
 *
 */
public class Cast extends PrimaryValue {
	private static final long serialVersionUID = 2416190287903177109L;

	private ValueExpression value;
	private PropertyDefinitionType type;
	private List<Integer> typeArguments;
	
	public Cast() {
	}
	
	public Cast(ValueExpression value, PropertyDefinitionType type) {
		this.value = value;
		this.type = type;
	}
	
	public Cast(ValueExpression value, PropertyDefinitionType type, List<Integer> typeArguments) {
		this.value = value;
		this.type = type;
		this.typeArguments = typeArguments;
	}
	
	public Cast(ValueExpression value, PropertyDefinitionType type, Integer... typeArgs) {
		this.value = value;
		this.type = type;
		if (typeArgs != null && typeArgs.length > 0) {
			this.typeArguments = new ArrayList<>(typeArgs.length);
			for (Integer i: typeArgs) {
				this.typeArguments.add(i);
			}
		}
	}
	
	public ValueExpression getValue() {
		return value;
	}

	public void setValue(ValueExpression value) {
		this.value = value;
	}

	public PropertyDefinitionType getType() {
		return type;
	}

	public void setType(PropertyDefinitionType type) {
		this.type = type;
	}

	
	public List<Integer> getTypeArguments() {
		return typeArguments;
	}

	public void setTypeArguments(List<Integer> typeArguments) {
		this.typeArguments = typeArguments;
	}

	public Integer getTypeArgument(int index) {
		if (typeArguments == null) {
			return null;
		}
		if (typeArguments.size() <= index) {
			return null;
		}
		
		return typeArguments.get(index);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("cast(");
		sb.append(value);
		sb.append(" as ");
		sb.append(type);
		if (typeArguments != null && typeArguments.size() > 0) {
			sb.append("(");
			boolean first = true;
			for (Integer ta: typeArguments) {
				if (first) {
					first = false;
				} else {
					sb.append(",");
				}
				sb.append(ta);
			}
			sb.append(")");
		}
		
		sb.append(")");
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((typeArguments == null) ? 0 : typeArguments.hashCode());
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
		Cast other = (Cast) obj;
		if (type != other.type)
			return false;
		if (typeArguments == null) {
			if (other.typeArguments != null)
				return false;
		} else if (!typeArguments.equals(other.typeArguments))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	
	public void accept(ValueExpressionVisitor visitor) {
		if (visitor.visit(this)) {
			if (value != null) {
				value.accept(visitor);
			}
		}
	}

	public ASTNode accept(ASTTransformer transformer) {
		return transformer.visit(this);
	}

}
