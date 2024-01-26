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

package org.iplass.mtp.transaction;

/**
 * トランザクションのcommit、rollbackの通知を受け取るためのインタフェースです。
 * 
 * @see Transaction
 * @author K.Higuchi
 *
 */
public interface TransactionListener {
	
	/**
	 * トランザクションがcommitされた場合、通知されます。
	 * 
	 * @param t
	 */
	public default void afterCommit(Transaction t) {};
	
	/**
	 * トランザクションがrollbackされた場合、通知されます。
	 * 
	 * @param t
	 */
	public default void afterRollback(Transaction t) {};

}
