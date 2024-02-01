/*
 * Copyright (C) 2019 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.redis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;

public class RedisService implements Service {

	private Map<String, RedisServer> servers = new HashMap<String, RedisServer>();

	@Override
	public void init(Config config) {
		List<RedisServer> serverList = config.getValues("redisServers", RedisServer.class);
		serverList.forEach(server -> servers.put(server.getServerName(), server));
	}

	@Override
	public void destroy() {
	}

	public RedisServer getRedisServer(String serverName) {
		return servers.get(serverName);
	}

}
