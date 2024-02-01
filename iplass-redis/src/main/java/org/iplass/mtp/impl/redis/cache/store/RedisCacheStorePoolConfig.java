/*
 * Copyright (C) 2023 DENTSU SOKEN INC. All Rights Reserved.
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

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import io.lettuce.core.api.StatefulRedisConnection;

public class RedisCacheStorePoolConfig {
	private int maxTotal;
	private int minIdle;
	private int maxIdle;

	private Boolean blockWhenExhausted;
	private long maxWaitMillis = 0L;
	private Boolean testOnBorrow;
	private Boolean testOnReturn;

	private Boolean testWhileIdle;
	private long timeBetweenEvictionRunsMillis = 0L;
	private long minEvictableIdleTimeMillis = 0L;
	private int numTestsPerEvictionRun;

	public int getMaxTotal() {
		return maxTotal;
	}

	public void setMaxTotal(int maxTotal) {
		this.maxTotal = maxTotal;
	}

	public int getMinIdle() {
		return minIdle;
	}

	public void setMinIdle(int minIdle) {
		this.minIdle = minIdle;
	}

	public int getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}

	public Boolean getBlockWhenExhausted() {
		return blockWhenExhausted;
	}

	public void setBlockWhenExhausted(Boolean blockWhenExhausted) {
		this.blockWhenExhausted = blockWhenExhausted;
	}

	public long getMaxWaitMillis() {
		return maxWaitMillis;
	}

	public void setMaxWaitMillis(long maxWaitMillis) {
		this.maxWaitMillis = maxWaitMillis;
	}

	public Boolean getTestOnBorrow() {
		return testOnBorrow;
	}

	public void setTestOnBorrow(Boolean testOnBorrow) {
		this.testOnBorrow = testOnBorrow;
	}

	public Boolean getTestOnReturn() {
		return testOnReturn;
	}

	public void setTestOnReturn(Boolean testOnReturn) {
		this.testOnReturn = testOnReturn;
	}

	public Boolean getTestWhileIdle() {
		return testWhileIdle;
	}

	public void setTestWhileIdle(Boolean testWhileIdle) {
		this.testWhileIdle = testWhileIdle;
	}

	public long getTimeBetweenEvictionRunsMillis() {
		return timeBetweenEvictionRunsMillis;
	}

	public void setTimeBetweenEvictionRunsMillis(long timeBetweenEvictionRunsMillis) {
		this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
	}

	public long getMinEvictableIdleTimeMillis() {
		return minEvictableIdleTimeMillis;
	}

	public void setMinEvictableIdleTimeMillis(long minEvictableIdleTimeMillis) {
		this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
	}

	public int getNumTestsPerEvictionRun() {
		return numTestsPerEvictionRun;
	}

	public void setNumTestsPerEvictionRun(int numTestsPerEvictionRun) {
		this.numTestsPerEvictionRun = numTestsPerEvictionRun;
	}

	public void apply(GenericObjectPoolConfig<StatefulRedisConnection<Object, Object>> poolConfig) {
		if (maxTotal > 0) {
			poolConfig.setMaxTotal(maxTotal);
		}
		if (maxIdle > 0) {
			poolConfig.setMaxIdle(maxIdle);
		}
		if (minIdle > 0) {
			poolConfig.setMinIdle(minIdle);
		}

		if (blockWhenExhausted != null) {
			poolConfig.setBlockWhenExhausted(blockWhenExhausted);
		}
		if (maxWaitMillis > 0) {
			poolConfig.setMaxWait(Duration.ofMillis(maxWaitMillis));
		}
		if (testOnBorrow != null) {
			poolConfig.setTestOnBorrow(testOnBorrow);
		}
		if (testOnReturn != null) {
			poolConfig.setTestOnReturn(testOnReturn);
		}

		if (testWhileIdle != null) {
			poolConfig.setTestWhileIdle(testWhileIdle);
		}
		if (timeBetweenEvictionRunsMillis > 0) {
			poolConfig.setTimeBetweenEvictionRuns(Duration.ofMillis(timeBetweenEvictionRunsMillis));
		}
		if (minEvictableIdleTimeMillis > 0) {
			poolConfig.setMinEvictableIdleTime(Duration.ofMillis(minEvictableIdleTimeMillis));
		}
		if (numTestsPerEvictionRun > 0) {
			poolConfig.setNumTestsPerEvictionRun(numTestsPerEvictionRun);
		}

	}

}
