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

package org.iplass.mtp.impl.rdb.connection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Function;

import org.iplass.mtp.impl.transaction.LocalTransaction;
import org.iplass.mtp.impl.transaction.LocalTransactionManager;
import org.iplass.mtp.impl.transaction.TransactionService;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.transaction.Transaction;
import org.iplass.mtp.transaction.TransactionManager;
import org.iplass.mtp.transaction.TransactionStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class AbstractConnectionFactory extends ConnectionFactory {

	private static final Logger logger = LoggerFactory.getLogger(AbstractConnectionFactory.class);
	private int warnLogThreshold;
	private boolean warnLogBefore;
	private TransactionIsolationLevel transactionIsolationLevel;

	private boolean isDefault;

	public Connection getConnection() {
		return getConnection(null);
	}

	public Connection getConnection(Function<Connection, Connection> afterGetPhysicalConnectionHandler) {
		if (isDefault) {
			TransactionService ts = ServiceRegistry.getRegistry().getService(TransactionService.class);
			int warnLogThreshold = getWarnLogThreshold();

			TransactionManager tm = ts.getTransacitonManager();
			Transaction ct = tm.currentTransaction();
			ResourceHolder rh = ResourceHolder.getResourceHolder();
			//LocalTransactionの場合
			if (ct instanceof LocalTransaction) {
				LocalTransaction t = (LocalTransaction) ct;
				if (t.getStatus() == TransactionStatus.ACTIVE) {
					if (t.getCon() == null) {
						try {
							if (rh == null || rh.isInUse()) {
								//ResourceHolder使ってない場合/既にResourceHolder利用されている場合は物理Connection
								t.setCon(new LocalTransactionConnectionWrapper(
										getPhysicalConnection(afterGetPhysicalConnectionHandler), true, null, warnLogThreshold, warnLogBefore));
							} else {
								t.setCon(new LocalTransactionConnectionWrapper(
										getHoldingConnection(rh, afterGetPhysicalConnectionHandler), true, rh, warnLogThreshold, warnLogBefore));
							}
						} catch (SQLException e) {
							throw new ConnectionException(e);
						}
					}
					return t.getCon();
				} else {
					if (rh == null || rh.isInUse()) {
						return new LocalTransactionConnectionWrapper(
								getPhysicalConnection(afterGetPhysicalConnectionHandler), false, null, warnLogThreshold, warnLogBefore);
					} else {
						//未使用のResourceHolderのコネクション
						return new LocalTransactionConnectionWrapper(
								getHoldingConnection(rh, afterGetPhysicalConnectionHandler), false, rh, warnLogThreshold, warnLogBefore);
					}
				}
			}

			//LocalTransactionだけど、トランザクション起動されていない場合
			if (tm instanceof LocalTransactionManager) {
				if (rh == null || rh.isInUse()) {
					return new LocalTransactionConnectionWrapper(
							getPhysicalConnection(afterGetPhysicalConnectionHandler), false, null, warnLogThreshold, warnLogBefore);
				} else {
					return new LocalTransactionConnectionWrapper(
							getHoldingConnection(rh, afterGetPhysicalConnectionHandler), false, rh, warnLogThreshold, warnLogBefore);
				}
			}
		}
		//デフォルトのConnectionFactoryでない場合、、LocalTransactionでない場合、そのまま素直に生成
		//TODO トランザクション管理
		return new LocalTransactionConnectionWrapper(
				getPhysicalConnection(afterGetPhysicalConnectionHandler), false, null, warnLogThreshold, warnLogBefore);
	}

	Connection getHoldingConnection(ResourceHolder rh, Function<Connection, Connection> afterGetPhysicalConnectionHandler) {
		Connection con = rh.getConnection(this, afterGetPhysicalConnectionHandler);
		if (logger.isDebugEnabled()) {
			logger.debug("getConnection from ResourceHolder:" + con);
		}
		return con;
	}

	Connection getPhysicalConnection(Function<Connection, Connection> afterGetPhysicalConnectionHandler) {
		Connection con = getConnectionInternal();
		if (logger.isDebugEnabled()) {
			logger.debug("create physical connection:" + con);
		}
		if (transactionIsolationLevel != null) {
			try {
				con.setTransactionIsolation(transactionIsolationLevel.sqlIntValue());
			} catch (SQLException e) {
				throw new ConnectionException("Can not setTransactionIsolation level.", e);
			}
		}
		if (afterGetPhysicalConnectionHandler != null) {
			con = afterGetPhysicalConnectionHandler.apply(con);
		}
		return con;
	}

	protected abstract Connection getConnectionInternal();

	public void init(Config config) {

		String serviceName = config.getServiceName();
		if (serviceName.equals(ConnectionFactory.class.getName())) {
			isDefault = true;
		}

		warnLogThreshold = config.getValue("warnLogThreshold", Integer.TYPE, 0);
		warnLogBefore = config.getValue("warnLogBefore", Boolean.TYPE, true);
		transactionIsolationLevel = config.getValue("transactionIsolationLevel", TransactionIsolationLevel.class);
	}

	public boolean isWarnLogBefore() {
		return warnLogBefore;
	}

	public int getWarnLogThreshold() {
		return warnLogThreshold;
	}

	public TransactionIsolationLevel getTransactionIsolationLevel() {
		return transactionIsolationLevel;
	}

}
