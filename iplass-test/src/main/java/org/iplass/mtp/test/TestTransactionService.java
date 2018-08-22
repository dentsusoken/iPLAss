/*
 * Copyright (C) 2016 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
package org.iplass.mtp.test;

import org.iplass.mtp.impl.transaction.LocalTransaction;
import org.iplass.mtp.impl.transaction.LocalTransactionManager;
import org.iplass.mtp.impl.transaction.TransactionService;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.transaction.Transaction;
import org.iplass.mtp.transaction.TransactionManager;

/**
 * テスト時に利用されるTransactionServiceの実装です。
 * 
 * @author K.Higuchi
 *
 */
class TestTransactionService extends TransactionService {
	
	static volatile boolean rollback;
	private TestTransactionManager tm;
	
	TestTransactionService() {
		tm = new TestTransactionManager();
	}
	
	private class TestTransactionManager extends LocalTransactionManager {

		@Override
		protected Transaction createTran(boolean readOnly, boolean noTransaction) {
			Transaction t = super.createTran(readOnly, noTransaction);
			if (rollback) {
				((LocalTransaction) t).setTestRollbackMode(true);
			}
			return t;
		}
		
	}

	@Override
	public void init(Config config) {
	}

	@Override
	public void destroy() {
	}

	@Override
	public TransactionManager getTransacitonManager() {
		return tm;
	}

}
