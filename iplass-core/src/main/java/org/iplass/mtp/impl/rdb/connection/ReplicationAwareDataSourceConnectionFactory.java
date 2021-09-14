/*
 * Copyright (C) 2021 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import java.io.Closeable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.iplass.mtp.spi.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReplicationAwareDataSourceConnectionFactory extends DataSourceConnectionFactory {
	private static Logger logger = LoggerFactory.getLogger(ReplicationAwareDataSourceConnectionFactory.class);
	
	private List<DataSource> replicaDataSource;
	private boolean directCreate;
	private Random rand = new Random();

	public List<DataSource> getReplicaDataSource() {
		return replicaDataSource;
	}

	@Override
	public void init(Config config) {
		super.init(config);
		
		replicaDataSource = config.getValues("replicaDataSource", DataSource.class);
		if (replicaDataSource != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("create replocaDataSource directly. DataSource:" + replicaDataSource);
			}
			directCreate = true;
		} else {
			//look up from JNDI
			if (config.getValues("replicaDataSourceName") != null) {
				List<String> replicaDataSourceName = config.getValues("replicaDataSourceName");
				
				if (logger.isDebugEnabled()) {
					logger.debug("look up replicaDataSource from JNDI. name:" + replicaDataSourceName);
				}

				InitialContext context = null;
				try {
					replicaDataSource = new ArrayList<>();
					context = getInitialContext();
					for (String rdsn: replicaDataSourceName) {
						replicaDataSource.add((DataSource) context.lookup(rdsn));
					}
				} catch (NamingException e) {
					throw new ConnectionException("can not create replicaDataSource:" + replicaDataSourceName, e);
				} finally {
					if (context != null) {
						try {
							context.close();
						} catch (NamingException e) {
							logger.warn("InitialContext.close() fail.maybe leak... " + e, e);
						}
					}
				}
			}
		}
	}

	@Override
	public void destroy() {
		super.destroy();
		if (directCreate && replicaDataSource != null) {
			for (DataSource rds: replicaDataSource) {
				try {
					if (rds instanceof Closeable) {
						((Closeable) rds).close();
					} else if (rds instanceof AutoCloseable) {
						((AutoCloseable) rds).close();
					}
				} catch (Exception e) {
					logger.warn("error in ReplicationAwareDataSourceConnectionFactory#destroy()" + e, e);
				}
			}
		}
		replicaDataSource = null;
		
	}

	@Override
	Connection getPhysicalConnection(Function<Connection, Connection> afterGetPhysicalConnectionHandler) {
		Connection con = new ReplicationAwareConnection(this, afterGetPhysicalConnectionHandler);
		if (logger.isDebugEnabled()) {
			logger.debug("create physical connection:" + con);
		}
		return con;
	}

	protected Connection getReplicaConnectionInternal() {
		
		DataSource target = null;
		if (replicaDataSource != null && replicaDataSource.size() > 0) {
			int i;
			if (replicaDataSource.size() == 1) {
				i = 0;
			} else {
				i = rand.nextInt(replicaDataSource.size());
			}
			if (logger.isDebugEnabled()) {
				logger.debug("get connection from replicaDataSource[" + i + "]");
			}
			target = replicaDataSource.get(i);
		}
		
		if (target == null) {
			//fallback to default dataSource
			if (logger.isDebugEnabled()) {
				logger.debug("replicaDataSource not defined, so fallback to default dataSource.");
			}
			target = getDataSource();
		}
		
		try {
			return target.getConnection();
		} catch (SQLException e) {
			throw new ConnectionException("can not get ReplicaDataSource Connection:", e);
		}
	}

}
