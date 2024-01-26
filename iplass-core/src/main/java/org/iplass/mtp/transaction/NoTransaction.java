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
package org.iplass.mtp.transaction;

/**
 * トランザクションがない状態を表すTransactionです。
 * 
 * @author K.Higuchi
 *
 */
final class NoTransaction implements Transaction {
	
	NoTransaction() {
	}

	@Override
	public void commit() {
		Holder.logger.warn("there is no transaction, so can not commit.");
	}

	@Override
	public void rollback() {
		Holder.logger.warn("there is no transaction, so can not rollback.");
	}

	@Override
	public void setRollbackOnly() {
		Holder.logger.warn("there is no transaction, so can not set rollbackOnly.");
	}

	@Override
	public boolean isRollbackOnly() {
		return false;
	}

	@Override
	public boolean isReadOnly() {
		return false;
	}

	@Override
	public TransactionStatus getStatus() {
		return TransactionStatus.NONE;
	}

	@Override
	public void setAttribute(Object key, Object value) {
		Holder.logger.warn("there is no transaction, so can not set attribute:key=" + key + ", value=" + value);
	}

	@Override
	public Object getAttribute(Object key) {
		return null;
	}

	@Override
	public Object removeAttribute(Object key) {
		Holder.logger.warn("there is no transaction, so can not remove attribute:key=" + key);
		return null;
	}

	@Override
	public void addTransactionListener(TransactionListener listener) {
		Holder.logger.warn("there is no transaction, so can not add TransactionListener:" + listener);
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof NoTransaction;
	}

	@Override
	public int hashCode() {
		return NoTransaction.class.getName().hashCode();
	}

}
