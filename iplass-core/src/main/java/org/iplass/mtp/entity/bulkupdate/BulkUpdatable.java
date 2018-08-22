/*
 * Copyright (C) 2015 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
package org.iplass.mtp.entity.bulkupdate;

import java.util.List;


/**
 * バルク更新対象を表すインタフェース。
 * {@link Iterable#iterator()}で更新対象のBulkUpdateEntityを返却するように実装する。
 * 
 * @author K.Higuchi
 *
 */
public interface BulkUpdatable extends Iterable<BulkUpdateEntity>, AutoCloseable {
	
	/**
	 * ラムダ式でBulkUpdatableを実装する場合に利用。
	 * 
	 * @param definitionName
	 * @return
	 * @see FunctionalEntityStream
	 */
	@SuppressWarnings("resource")
	public static FunctionalEntityStream as(String definitionName) {
		return new FunctionalEntityStream().definitionName(definitionName);
	}
	
	/**
	 * バルク更新対象のEntity定義名を返却するように実装。
	 * 
	 * @return
	 */
	public String getDefinitionName();
	
	/**
	 * バルク更新対象のEntityの更新処理が成功した場合呼び出されるコールバック。
	 * 
	 * @param updatedEntity
	 */
	public default void updated(BulkUpdateEntity updatedEntity) {
	}
	
	/**
	 * BulkUpdatableのクローズ処理を記述。
	 * 
	 */
	@Override
	public default void close() {
	}
	
	/**
	 * バルク更新（UPDATEおよびMERGEで更新と判断された場合）の際の更新対象のプロパティを指定する。
	 * 未指定（null）の場合は全項目を更新対象と判断。
	 * @return
	 */
	public default List<String> getUpdateProperties() {
		return null;
	}
	
	
}
