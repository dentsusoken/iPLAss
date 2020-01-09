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
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.iplass.mtp.spi.Config;


public class DriverManagerConnectionFactory extends AbstractConnectionFactory {

	private String url;
	private Properties info;


	@Override
	protected Connection getConnectionInternal() {

		Connection con;

		//tomcatでサーバ再起動なしにwebappをリロードすると、Class.forName呼び出しないと1回目のConnection取得がエラーになる。
		try {
			Class.forName(info.getProperty("driver"));
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		try {
			con = DriverManager.getConnection(url, info);
		} catch (SQLException e) {
			throw new ConnectionException("can not get Driver Managed Connection:", e);
		}
		return con;

	}


	public void destroy() {
	}


	public void init(Config config) {
		super.init(config);

		info = new Properties();
		for (String name: config.getNames()) {
			if (!name.equals("warnLogThreshold")
					&& !name.equals("transactionIsolationLevel")
					&& !name.equals("warnLogBefore")
					&& !name.equals("countSqlExecution")) {
				info.put(name, config.getValue(name));
			}
		}
		url = (String) info.remove("url");
	}
}
