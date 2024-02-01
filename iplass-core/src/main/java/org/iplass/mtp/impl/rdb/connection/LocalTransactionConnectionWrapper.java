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

package org.iplass.mtp.impl.rdb.connection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalTransactionConnectionWrapper extends ConnectionWrapper {

	private static final Logger logger = LoggerFactory.getLogger(LocalTransactionConnectionWrapper.class);

	private boolean inTransaction;
	private ResourceHolder resourceHolder;

	private boolean isClosed = false;
	private volatile boolean isClosePhysical;
	
	LocalTransactionConnectionWrapper(Connection wrapped, boolean inTransaction, ResourceHolder resourceHolder, int warnLogThreshold, boolean warnLogBefore, boolean countSqlExecution) {
		super(wrapped, warnLogThreshold, warnLogBefore, countSqlExecution);
		this.inTransaction = inTransaction;
		this.resourceHolder = resourceHolder;
	}

	public void closePhysical() throws SQLException {
		if (resourceHolder != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("back to ResourceHolder:" + getWrapped());
			}
			resourceHolder.releaseConnection(getWrapped());
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("close physical connection:" + getWrapped());
			}
			getWrapped().close();
		}
		isClosePhysical = true;
	}
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		if (!isClosePhysical) {
			logger.warn("connection not closed. this cause connection leak... Connection:" + getWrapped());
			getWrapped().close();
		}
	}

	public void close() throws SQLException {
		if (isClosed) {
			return;
		}
		
		isClosed = true;
		
		//closeは、Transactionのcommit or rollback or ResourceHolderのfinalizeで行われる
		if (!inTransaction) {
			closePhysical();
		}
	}

	public void commit() throws SQLException {
		//commitはTransactionのcommitで行われる
		if (!inTransaction) {
			getWrapped().commit();
		}
	}

	public boolean isClosed() throws SQLException {
		//JDBC4.1で追加されたabort,setNetworkTimeoutにて、
		//別スレッドで物理コネクションがクローズされている可能性があるため
		//先に物理コネクションの状態をチェックする。
		boolean physicalClosed = getWrapped().isClosed();
		if (physicalClosed) {
			return physicalClosed;
		} else {
			return isClosed;
		}
	}

	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		if (!inTransaction) {
			getWrapped().releaseSavepoint(savepoint);
		}
	}

	public void rollback() throws SQLException {
		//rollbackはTransactionのrollbackで行われる
		if (!inTransaction) {
			getWrapped().rollback();
		}
	}

	public void rollback(Savepoint savepoint) throws SQLException {
		//rollbackはTransactionのrollbackで行われる
		if (!inTransaction) {
			getWrapped().rollback(savepoint);
		}
	}

	public void setAutoCommit(boolean autoCommit) throws SQLException {
		//トランザクション制御はTransactionで行われる
		if (!inTransaction) {
			getWrapped().setAutoCommit(autoCommit);
		}
	}

	public Savepoint setSavepoint() throws SQLException {
		//トランザクション制御はTransactionで行われる
		if (!inTransaction) {
			return getWrapped().setSavepoint();
		}
		return null;
	}

	public Savepoint setSavepoint(String name) throws SQLException {
		//トランザクション制御はTransactionで行われる
		if (!inTransaction) {
			return getWrapped().setSavepoint(name);
		}
		return null;
	}

	public boolean isInTransaction() {
		return inTransaction;
	}
}
