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

package org.iplass.mtp.entity;

import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.entity.query.QueryException;
import org.iplass.mtp.entity.query.Where;
import org.iplass.mtp.entity.query.condition.Condition;
import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.entity.query.value.primary.Literal;
import org.iplass.mtp.impl.parser.ParseException;
import org.iplass.mtp.impl.query.QueryConstants;
import org.iplass.mtp.impl.query.QueryServiceHolder;
import org.iplass.mtp.impl.query.WhereSyntax;


/**
 * Entityデータを一括更新する際の更新条件。
 *
 *
 * @author K.Higuchi
 *
 */
public class UpdateCondition {

	private List<UpdateValue> values;
	private String definitionName;
	private Where where;
	private boolean lockStrictly = false;
	private boolean checkUpdatable = true;
	
	public UpdateCondition() {
	}

	/**
	 * コンストラクタ。
	 *
	 * @param definitionName 更新対象のEntity定義名
	 */
	public UpdateCondition(String definitionName) {
		this.definitionName = definitionName;
	}

	/**
	 * コンストラクタ。
	 *
	 * @param definitionName 更新対象のEntity定義名
	 * @param values 更新対象項目
	 * @param where 更新するEntityを指定する条件
	 */
	public UpdateCondition(String definitionName, List<UpdateValue> values, Where where) {
		this.definitionName = definitionName;
		this.values = values;
		this.where = where;
	}
	
	/**
	 * 当該更新対象propertyがupdatable=tureかどうかをチェックするか否か
	 * @return
	 */
	public boolean isCheckUpdatable() {
		return checkUpdatable;
	}

	/**
	 * 当該更新対象propertyがupdatable=tureかどうかをチェックするか否かをセット。
	 * デフォルトtrue
	 * 
	 * @param checkUpdatable
	 */
	public void setCheckUpdatable(boolean checkUpdatable) {
		this.checkUpdatable = checkUpdatable;
	}
	
	/**
	 * 指定のEntity属性を指定の値で更新するようにセット。
	 *
	 * @param entityField Entity属性名
	 * @param value 更新値
	 * @return
	 */
	public UpdateCondition value(String entityField, Object value) {
		if (values == null) {
			values = new ArrayList<UpdateValue>();
		}
		values.add(new UpdateValue(entityField, value));
		return this;
	}

	/**
	 * 指定のEntity属性を指定の値表現（ValueExpression）で更新するようにセット。
	 *
	 * @param entityField Entity属性名
	 * @param value 更新値のValueExpression
	 * @return
	 */
	public UpdateCondition value(String entityField, ValueExpression value) {
		if (values == null) {
			values = new ArrayList<UpdateValue>();
		}
		values.add(new UpdateValue(entityField, value));
		return this;
	}

	public Where where() {
		if (where == null) {
			where = new Where();
		}
		return where;
	}

	/**
	 * 更新条件を指定。
	 *
	 * @param whereClause
	 * @return
	 */
	public UpdateCondition where(String whereClause) {
		String whereStr = QueryConstants.WHERE + " " + whereClause;
		try {
			where = QueryServiceHolder.getInstance().getQueryParser().parse(whereStr, WhereSyntax.class);
		} catch (ParseException e) {
			throw new QueryException(e.getMessage(), e);
		}

		return this;
	}
	
	/**
	 * 更新時に厳密にロック（oid順にソートしてロック取得）を取得する場合。
	 * @see #setLockStrictly(boolean)
	 * @return
	 */
	public UpdateCondition lockStrictly() {
		lockStrictly = true;
		return this;
	}
	
	/**
	 * 当該更新対象propertyか更新可能かどうかをチェックしないように設定。
	 * 
	 * @return
	 */
	public UpdateCondition nocheckUpdatable() {
		this.checkUpdatable = false;
		return this;
	}

	/**
	 * 更新条件を指定。
	 *
	 * @param whereCondition
	 * @return
	 */
	public UpdateCondition where(Condition whereCondition) {
		where = new Where(whereCondition);
		return this;
	}

	public List<UpdateValue> getValues() {
		return values;
	}
	public void setValues(List<UpdateValue> values) {
		this.values = values;
	}
	public String getDefinitionName() {
		return definitionName;
	}
	public void setDefinitionName(String definitionName) {
		this.definitionName = definitionName;
	}
	public Where getWhere() {
		return where;
	}
	public void setWhere(Where where) {
		this.where = where;
	}
	public boolean isLockStrictly() {
		return lockStrictly;
	}
	/**
	 * 更新時に厳密にロック（oid順にソートしてロック取得）を取得する場合trueを設定。
	 * デフォルトfalse。
	 * ※デッドロックエラーを防ぎたい場合に利用。但し、他の更新処理においても更新順を必ずoid順にすることを守る必要あり。
	 * 
	 * @param lockStrictly
	 */
	public void setLockStrictly(boolean lockStrictly) {
		this.lockStrictly = lockStrictly;
	}


	/**
	 * 一括更新時の更新項目を表すクラス。
	 *
	 * @author K.Higuchi
	 *
	 */
	public static class UpdateValue {

		private String entityField;
		private ValueExpression value;

		public UpdateValue() {
		}

		/**
		 * コンストラクタ。
		 * 指定のEntity属性を指定の値で更新するように指定。
		 *
		 * @param entityField Entity属性名
		 * @param value 更新値
		 */
		public UpdateValue(String entityField, Object value) {
			this.entityField = entityField;
			if (value instanceof ValueExpression) {
				this.value = (ValueExpression) value;
			} else {
				this.value = new Literal(value);
			}
		}

		/**
		 * コンストラクタ。
		 * 指定のEntity属性を指定の値表現（ValueExpression）で更新するように指定。
		 *
		 * @param entityField Entity属性名
		 * @param value 更新値を表すValueExpression
		 */
		public UpdateValue(String entityField, ValueExpression value) {
			this.entityField = entityField;
			this.value = value;
		}

		public String getEntityField() {
			return entityField;
		}
		public void setEntityField(String entityField) {
			this.entityField = entityField;
		}
		public ValueExpression getValue() {
			return value;
		}
		public void setValue(ValueExpression value) {
			this.value = value;
		}

		public UpdateValue copy() {
			UpdateValue copy = new UpdateValue();
			copy.entityField = entityField;
			if (value != null) {
				copy.value = (ValueExpression) value.copy();
			}
			
			return copy;
		}
	}
	
	public UpdateCondition copy() {
		UpdateCondition copy = new UpdateCondition();
		if (values != null) {
			copy.values = new ArrayList<>(values.size());
			for (UpdateValue uv: values) {
				copy.values.add(uv.copy());
			}
		}
		copy.definitionName = definitionName;
		if (where != null) {
			copy.where = (Where) where.copy();
		}
		copy.lockStrictly = lockStrictly;
		copy.checkUpdatable = checkUpdatable;
		
		return copy;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("update ").append(definitionName);
		sb.append(" set ");
		if (values != null) {
			for (int i = 0; i < values.size(); i++) {
				if (i != 0) {
					sb.append(",");
				}
				UpdateValue uv = values.get(i);
				sb.append(uv.entityField).append("=").append(uv.value);
			}
		}
		if (where != null) {
			sb.append(" ").append(where);
		}
		return sb.toString();
	}

}
