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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import org.iplass.mtp.spi.Service;


public abstract class ConnectionFactory implements Service {
	//TODO change to interface
	
	public static final String SQL_COUNT_KEY = "mtp.rdb.log.sqlCount";

	public abstract Connection getConnection();

	public abstract Connection getConnection(Function<Connection, Connection> afterGetPhysicalConnectionHandler);

	public abstract int getWarnLogThreshold();
	public abstract boolean isWarnLogBefore();
	public abstract boolean isCountSqlExecution();
	
	public abstract AtomicInteger getCounterOfSqlExecution();

	public abstract TransactionIsolationLevel getTransactionIsolationLevel();
}
