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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceHolder {
	private static final Logger logger = LoggerFactory.getLogger(ResourceHolder.class);

	private static final ThreadLocal<ResourceHolder> holder = new ThreadLocal<ResourceHolder>();

	private Connection connection;
	private boolean inUse = false;

	public static ResourceHolder getResourceHolder() {
		ResourceHolder r = holder.get();
		return r;
	}

	public static boolean init() {
		ResourceHolder r = holder.get();
		if (r == null) {
			r = new ResourceHolder();
			holder.set(r);
			return true;
		} else {
			return false;
		}
	}

	public static void fin() {
		ResourceHolder r = holder.get();
		if (r != null) {
			r.finallyProcess();
			holder.remove();
		}
	}

	private ResourceHolder() {
	}

	public boolean isInUse() {
		return inUse;
	}

	Connection getConnection(AbstractConnectionFactory conFactory, Function<Connection, Connection> afterGetPhysicalConnectionHandler) {
		if (connection == null) {
			connection = conFactory.getPhysicalConnection(afterGetPhysicalConnectionHandler);
		}

		if (inUse) {
			throw new IllegalStateException("ResourceHolder's Connection allready in use");
		}
		inUse = true;
		return connection;
	}

	void releaseConnection(Connection con) {
		if (con != connection) {
			throw new IllegalStateException("ResourceHolder's Connection unmatch!");
		}
		inUse = false;
	}

	public void finallyProcess() {
		try {

			if (connection != null && !connection.isClosed()) {
				if (logger.isDebugEnabled()) {
					logger.debug("close physical connection:" + connection);
				}
				connection.close();
			}
		} catch (SQLException e) {
			logger.error("can't close Connection, maybe resource leak... :" + e.toString(), e);
		}
		connection = null;
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		finallyProcess();
	}

	@Override
	public String toString() {
		return super.toString() + "[connection=" + connection + "]";
	}

}
