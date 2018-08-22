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

package org.iplass.mtp.impl.transaction;

import org.iplass.mtp.transaction.Transaction;
import org.iplass.mtp.transaction.TransactionManager;
import org.iplass.mtp.transaction.TransactionStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalTransactionManager implements TransactionManager {
	
	private static final Logger logger = LoggerFactory.getLogger(LocalTransactionManager.class);
	
	static ThreadLocal<LocalTransaction> transaction = new ThreadLocal<LocalTransaction>();
	
	public Transaction newTransaction() {
		return newTransaction(false);
	}
	
	public Transaction currentTransaction() {
		Transaction t = transaction.get();
		if (t == null) {
			return Transaction.NO_TRANSACTION;
		} else {
			return t;
		}
	}
	
	public void checkAndClean() {
		for (LocalTransaction t = transaction.get(); t != null; t = transaction.get()) {
			if (t.getStatus() == TransactionStatus.ACTIVE) {
				logger.warn("transaction resource leak... check Logic!!:" + t);
				if (t.isRollbackOnly()) {
					logger.warn("rollback leaked transaction:" + t);
					t.rollback();
				} else {
					logger.warn("commit leaked transaction:" + t);
					t.commit();
				}
			} else {
				if (t instanceof LocalTransaction) {
					t.close();
				}
			}
		}
	}

	@Override
	public Transaction newTransaction(boolean readOnly) {
		return createTran(readOnly, false);
	}
	
	protected Transaction createTran(boolean readOnly, boolean noTransaction) {
		LocalTransaction current = transaction.get();
		LocalTransaction newT = new LocalTransaction(readOnly, current, noTransaction);
		transaction.set(newT);
		return newT;
	}

	@Override
	public Transaction suspend() {
		return createTran(false, true);
	}

	@Override
	public void resume(Transaction t) {
		((LocalTransaction) t).close();
	}
	
}
