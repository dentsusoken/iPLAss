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

/**
 * 別トランザクションと更新が競合した場合スローされる例外。
 * 
 * @author K.Higuchi
 *
 */
public class EntityConcurrentUpdateException extends EntityApplicationException {
	private static final long serialVersionUID = -7337391382174601963L;
	
	/** デッドロックの検知か */
	private boolean isDeadLock = false;
	/** Nowaiteの検知か */
	private boolean isNowait = false;

	public EntityConcurrentUpdateException() {
		super();
	}

	public EntityConcurrentUpdateException(String message, Throwable cause) {
		super(message, cause);
	}

	public EntityConcurrentUpdateException(String message) {
		super(message);
	}

	public EntityConcurrentUpdateException(Throwable cause) {
		super(cause);
	}

	/**
	 * デッドロックの検知かを取得します。
	 * @return デッドロックの検知か
	 */
	public boolean isDeadLock() {
	    return isDeadLock;
	}

	/**
	 * デッドロックの検知かを設定します。
	 * @param isDeadLock デッドロックの検知か
	 */
	public void setDeadLock(boolean isDeadLock) {
	    this.isDeadLock = isDeadLock;
	}

	/**
	 * Nowaiteの検知かを取得します。
	 * @return Nowaiteの検知か
	 */
	public boolean isNowait() {
	    return isNowait;
	}

	/**
	 * Nowaiteの検知かを設定します。
	 * @param isNowait Nowaiteの検知か
	 */
	public void setNowait(boolean isNowait) {
	    this.isNowait = isNowait;
	}

}
