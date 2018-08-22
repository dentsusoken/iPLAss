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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iplass.mtp.SystemException;
import org.iplass.mtp.entity.EntityDuplicateValueException;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapterService;
import org.iplass.mtp.impl.rdb.connection.LocalTransactionConnectionWrapper;
import org.iplass.mtp.impl.util.CoreResourceBundleUtil;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.transaction.RollbackException;
import org.iplass.mtp.transaction.Transaction;
import org.iplass.mtp.transaction.TransactionException;
import org.iplass.mtp.transaction.TransactionListener;
import org.iplass.mtp.transaction.TransactionStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LocalTransaction implements Transaction {

	private static Logger logger = LoggerFactory.getLogger(LocalTransaction.class);

	private Map<Object, Object> transactionLocalAttribute;

	private List<TransactionListener> listenerList;

	private LocalTransactionConnectionWrapper con;

	private boolean rollbackOnly;

	private TransactionStatus status;

	private LocalTransaction stacked;

	private final boolean readOnly;

	private boolean finalized;

	//for test use only
	private boolean testRollbackMode;


	public LocalTransaction(boolean readOnly) {
		this(readOnly, null);
	}

	public LocalTransaction(boolean readOnly, LocalTransaction stacked) {
		this(readOnly, stacked, false);
	}

	public LocalTransaction(boolean readOnly, LocalTransaction stacked, boolean noTransaction) {
		this.readOnly = readOnly;
		this.stacked = stacked;
		if (noTransaction) {
			status = TransactionStatus.NONE;
		} else {
			status = TransactionStatus.ACTIVE;
		}
		if (logger.isDebugEnabled()) {
			if (noTransaction) {
				logger.debug("set Transaction to NOT_SUPPORTED:" + this + " with stacked:" + stacked);
			} else {
				logger.debug("create new Transaction:" + this + " with readOnly=" + readOnly + ", stacked:" + stacked);
			}
		}
	}

	public LocalTransaction getStacked() {
		return stacked;
	}

	public LocalTransactionConnectionWrapper getCon() {
		return con;
	}

	public void setCon(LocalTransactionConnectionWrapper con) throws SQLException {
		if (finalized) {
			throw new SystemException("Current Transaction Context already finalized");
		}

		if (this.con != null) {
			throw new SystemException("LocalTransaction only support one connection per transaction.");
		}
		this.con = con;
		try {
			if (con.getAutoCommit()) {
				con.getWrapped().setAutoCommit(false);
			}

			this.con.setReadOnly(readOnly);

		} catch (SQLException e) {
			//Connecionは生成されているけど、setAutoCommit()、setReadOnlyに失敗
			try {
				con.getWrapped().rollback();
			} catch (SQLException ee) {
				e.addSuppressed(ee);
			}
			try {
				con.getWrapped().close();
			} catch (SQLException ee) {
				e.addSuppressed(ee);
			}
			throw e;
		}
	}

	public void commit() {
		if (finalized) {
			logger.warn("commit called, but this Transaction Context already finalized." + this);
			return;
		}
		if (logger.isDebugEnabled()) {
			if (testRollbackMode) {
				logger.debug("commit Transaction(actually rollback by test rollback mode):" + this);
			} else {
				logger.debug("commit Transaction:" + this);
			}
		}

		boolean isCommit = false;
		if (rollbackOnly) {
			throw new RollbackException("setRolbackOnly called");//TODO ちゃんとしたメッセージ
		}
		try {
			if (con != null && !con.getWrapped().isClosed()) {
				if (testRollbackMode) {
					con.getWrapped().rollback();
				} else {
					con.getWrapped().commit();
				}
			}
			//初回のcommit時のみ、listenerに通知するようにする
			if (status == TransactionStatus.ACTIVE) {
				isCommit = true;
				status = TransactionStatus.COMMITTED;
			}
		} catch (SQLException | RuntimeException e) {
			status = TransactionStatus.NONE;
			//Oracleの遅延制約の場合は、このタイミングで整合性エラーが発生
			RdbAdapter rdb = ServiceRegistry.getRegistry().getService(RdbAdapterService.class).getRdbAdapter();
			if (e instanceof SQLException && rdb.isDuplicateValueException((SQLException) e)) {
				throw new EntityDuplicateValueException(resourceString("impl.transaction.LocalTransaction.duplicate", (Object[])null), e);
			}
			throw new TransactionException(e);
		} finally {
			if (con != null) {
				if (readOnly) {
					try {
						con.setReadOnly(false);
					} catch (SQLException e) {
						logger.error("set ReadOnly failed at transaction commit...:" + e, e);
					}
				}
				try {
					con.getWrapped().setAutoCommit(true);
				} catch (SQLException e) {
					logger.error("set AutoCommit failed at transaction commit...:" + e, e);
				}
				try {
					con.closePhysical();
				} catch (SQLException e) {
					logger.error("colse Connection failed at transaction commit...:" + e, e);
				}
			}
			con = null;
			close();
		}

		if (isCommit && listenerList != null) {
			for (TransactionListener l: listenerList) {
				l.afterCommit(this);
			}
		}
	}

	public void close() {
		if (!finalized) {
			if (logger.isDebugEnabled()) {
				logger.debug("close Transaction:" + this);
			}
			LocalTransactionManager.transaction.set(stacked);
			finalized = true;
		}
	}

	public TransactionStatus getStatus() {
		return status;
	}

	public boolean isRollbackOnly() {
		return rollbackOnly;
	}

	public void rollback() {
		if (finalized) {
			logger.warn("rollback called, but this Transaction Context already finalized." + this);
			return;
		}
		if (logger.isDebugEnabled()) {
			logger.debug("rollback Transaction:" + this);
		}

		boolean isRollback = false;
		try {
			if (con != null && !con.getWrapped().isClosed()) {
				con.getWrapped().rollback();
			}
			//初回のrollback時のみ、listenerに通知するようにする
			if (status == TransactionStatus.ACTIVE) {
				isRollback = true;
				status = TransactionStatus.ROLLEDBACK;
			}

		} catch (SQLException | RuntimeException e) {
			status = TransactionStatus.NONE;
			throw new TransactionException(e);
		} finally {
			if (con != null) {
				if (readOnly) {
					try {
						con.setReadOnly(false);
					} catch (SQLException e) {
						logger.error("set ReadOnly failed at transaction commit...:" + e, e);
					}
				}
				try {
					con.getWrapped().setAutoCommit(true);
				} catch (SQLException e) {
					logger.error("set AutoCommit failed at transaction commit...:" + e, e);
				}
				try {
					con.closePhysical();
				} catch (SQLException e) {
					logger.error("colse Connection failed at transaction commit...:" + e, e);
				}
			}
			con = null;
			close();
		}

		if (isRollback && listenerList != null) {
			for (TransactionListener l: listenerList) {
				l.afterRollback(this);
			}
		}
	}

	public void setRollbackOnly() {
		rollbackOnly = true;
	}

	public Object getAttribute(Object key) {
		if (transactionLocalAttribute == null) {
			return null;
		}
		return transactionLocalAttribute.get(key);
	}

	public void setAttribute(Object key, Object value) {
//		if (finalized) {
//			logger.warn("Current Transaction Context already finalized. so cant set key=" + key + ", value=" + value);
//			return;
//		}
		if (transactionLocalAttribute == null) {
			transactionLocalAttribute = new HashMap<Object, Object>();
		}
		transactionLocalAttribute.put(key, value);
	}

	public Object removeAttribute(Object key) {
		if (transactionLocalAttribute == null) {
			return null;
		}
		return transactionLocalAttribute.remove(key);
	}

	@Override
	public void addTransactionListener(TransactionListener listener) {
		if (finalized) {
			logger.warn("Current Transaction Context already finalized. so cant add TransactionListener=" + listener);
			return;
		}
		if (listenerList == null) {
			listenerList = new ArrayList<TransactionListener>();
		}
		listenerList.add(listener);
	}

	@Override
	public boolean isReadOnly() {
		return readOnly;
	}

	public void setTestRollbackMode(boolean testRollbackMode) {
		this.testRollbackMode = testRollbackMode;
	}

	private static String resourceString(String key, Object... arguments) {
		return CoreResourceBundleUtil.resourceString(key, arguments);
	}
}
