/*
 * Copyright (C) 2019 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.redis.cache.store;

import java.time.Duration;

import org.iplass.mtp.SystemException;
import org.iplass.mtp.impl.cache.CacheService;
import org.iplass.mtp.impl.cache.store.CacheHandler;
import org.iplass.mtp.impl.cache.store.CacheStore;
import org.iplass.mtp.impl.cache.store.CacheStoreFactory;
import org.iplass.mtp.impl.redis.RedisServer;
import org.iplass.mtp.impl.redis.RedisService;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.ServiceInitListener;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.resource.DefaultClientResources;

public class RedisCacheStoreFactory extends CacheStoreFactory implements ServiceInitListener<CacheService> {

	private RedisServer server;
	private RedisClient client;

	private String serverName;
	private long timeToLive = 0L; // Seconds(0以下無期限)

	@Override
	public CacheStore createCacheStore(String namespace) {
		return getIndexCount() > 0 ? new IndexedRedisCacheStore(this, namespace, timeToLive, getIndexCount(), false)
				: new RedisCacheStore(this, namespace, timeToLive, false);
	}

	@Override
	public boolean canUseForLocalCache() {
		return false;
	}

	@Override
	public boolean supportsIndex() {
		return true;
	}

	@Override
	public CacheHandler createCacheHandler(CacheStore store) {
		return new RedisCacheHandler(store);
	}

	@Override
	public CacheStoreFactory getLowerLevel() {
		return null;
	}

	@Override
	public void inited(CacheService service, Config config) {
		RedisService rs = config.getDependentService(RedisService.class);
		this.server = rs.getRedisServer(serverName);
		if (server == null) {
			throw new SystemException("Unknown redis server name: " + serverName);
		}

		ClientResources resouces = DefaultClientResources.builder().build();

		RedisURI.Builder uriBuilder = RedisURI.builder().withHost(server.getHost()).withPort(server.getPort())
				.withDatabase(server.getDatabase());
		if (server.getTimeout() > 0) {
			uriBuilder.withTimeout(Duration.ofSeconds(server.getTimeout()));
		}

		uriBuilder.withSsl(server.isSsl());
		if (server.getPassword() != null) {
			if (server.getUserName() != null) {
				uriBuilder.withAuthentication(server.getUserName(), server.getPassword().toCharArray());
			} else {
				uriBuilder.withPassword(server.getPassword().toCharArray());
			}
		}
		this.client = RedisClient.create(resouces, uriBuilder.build());
	}

	@Override
	public void destroyed() {
		if (client != null) {
			client.shutdown();
			client = null;
		}
	}

	public RedisClient getClient() {
		return client;
	}

	public RedisServer getServer() {
		return server;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public void setTimeToLive(long timeToLive) {
		this.timeToLive = timeToLive;
	}

}
