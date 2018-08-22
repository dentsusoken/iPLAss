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

import org.iplass.mtp.entity.Entity;

/**
 * バルク更新の際の1Entityを表す。
 * 更新方法（INSERT/UPDATE/MERGE/DELETE）の指定と、その対象Entityを指定する。
 * 
 * @author K.Higuchi
 *
 */
public class BulkUpdateEntity {
	
	/**
	 * 更新方法
	 * 
	 * @author K.Higuchi
	 *
	 */
	public enum UpdateMethod {
		/**
		 * 新規追加
		 */
		INSERT,
		/**
		 * 更新。oid指定が必須。
		 */
		UPDATE,
		/**
		 * 当該Entityが存在したら、更新。存在しなかったら新規追加。oid指定が必須。<br>
		 * 更新/新規を判断するため、INSERT/UPDATEダイレクト指定より処理スピードは若干劣る。
		 */
		MERGE,
		/**
		 * 削除。oid指定が必須。
		 */
		DELETE
	}
	
	private UpdateMethod method;
	private Entity entity;
	
	public BulkUpdateEntity() {
	}
	
	public BulkUpdateEntity(UpdateMethod method, Entity entity) {
		super();
		this.method = method;
		this.entity = entity;
	}

	public UpdateMethod getMethod() {
		return method;
	}
	public void setMethod(UpdateMethod method) {
		this.method = method;
	}
	public Entity getEntity() {
		return entity;
	}
	public void setEntity(Entity entity) {
		this.entity = entity;
	}

}
