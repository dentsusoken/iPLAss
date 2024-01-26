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

package org.iplass.mtp.entity;

import org.iplass.mtp.entity.query.QueryException;
import org.iplass.mtp.entity.query.Where;
import org.iplass.mtp.entity.query.condition.Condition;
import org.iplass.mtp.impl.parser.ParseException;
import org.iplass.mtp.impl.query.QueryConstants;
import org.iplass.mtp.impl.query.QueryServiceHolder;
import org.iplass.mtp.impl.query.WhereSyntax;

/**
 * 一括削除の際の削除条件を表す。
 *
 *
 * @author K.Higuchi
 *
 */
public class DeleteCondition {

	private String definitionName;
	private Where where;
	private boolean lockStrictly;

	/**
	 * コンストラクタ。
	 *
	 * @param definitionName 削除対象のEntity定義名
	 */
	public DeleteCondition(String definitionName) {
		this.definitionName = definitionName;
	}

	/**
	 * コンストラクタ。
	 *
	 * @param definitionName 削除対象のEntity定義名
	 * @param where 削除対象のEntityを指定する条件
	 */
	public DeleteCondition(String definitionName, Where where) {
		this.definitionName = definitionName;
		this.where = where;
	}

	public Where where() {
		if (where == null) {
			where = new Where();
		}
		return where;
	}

	/**
	 * 削除条件を設定する。
	 *
	 * @param whereClause
	 * @return
	 */
	public DeleteCondition where(String whereClause) {
		String whereStr = QueryConstants.WHERE + " " + whereClause;
		try {
			where = QueryServiceHolder.getInstance().getQueryParser().parse(whereStr, WhereSyntax.class);
		} catch (ParseException e) {
			throw new QueryException(e.getMessage(), e);
		}

		return this;
	}

	/**
	 * 削除条件を設定する。
	 *
	 * @param whereCondition
	 * @return
	 */
	public DeleteCondition where(Condition whereCondition) {
		where = new Where(whereCondition);
		return this;
	}
	
	/**
	 * 削除時に厳密にロック（oid順にソートしてロック取得）を取得する場合。
	 * @see #setLockStrictly(boolean)
	 * @return
	 */
	public DeleteCondition lockStrictly() {
		lockStrictly = true;
		return this;
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
	 * 削除時に厳密にロック（oid順にソートしてロック取得）を取得する場合trueを設定。
	 * デフォルトfalse。
	 * ※デッドロックエラーを防ぎたい場合に利用。但し、他の更新処理においても更新順を必ずoid順にすることを守る必要あり。
	 * 
	 * @param lockStrictly
	 */
	public void setLockStrictly(boolean lockStrictly) {
		this.lockStrictly = lockStrictly;
	}

	public DeleteCondition() {
	}

	public DeleteCondition copy() {
		DeleteCondition copy = new DeleteCondition();
		copy.definitionName = definitionName;
		if (where != null) {
			copy.where = (Where) where.copy();
		}
		copy.lockStrictly = lockStrictly;
		
		return copy;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("delete from ").append(definitionName);
		if (where != null) {
			sb.append(" ").append(where);
		}
		return sb.toString();
	}

}
