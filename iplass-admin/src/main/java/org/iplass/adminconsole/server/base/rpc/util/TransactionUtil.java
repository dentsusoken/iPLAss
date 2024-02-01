/*
 * Copyright (C) 2012 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.server.base.rpc.util;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.transaction.Transaction;
import org.iplass.mtp.transaction.TransactionManager;
import org.iplass.mtp.transaction.TransactionStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransactionUtil {

	private static final Logger log = LoggerFactory.getLogger(TransactionUtil.class);

	public static void setRollback() {
		//トランザクションのロールバック指定
		TransactionManager tm = ManagerLocator.getInstance().getManager(TransactionManager.class);
		Transaction t = tm.currentTransaction();
		if (t != null && t.getStatus() == TransactionStatus.ACTIVE && !t.isRollbackOnly()) {
			log.debug("set transaction rollback.");
			t.setRollbackOnly();
		}
	}
}
