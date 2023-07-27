/*
 * Copyright (C) 2023 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

public class RedisCacheStoreLuaScript {

	public static final String PUT = 
			"local cacheKey = KEYS[1]\n" + 
			"local timeToLive = tonumber(ARGV[1])\n" +
			"local cacheEntry = ARGV[2]\n" +
			"local previous = redis.call('GET', cacheKey)\n" +
			"if timeToLive > 0 then\n" +
			"	redis.call('SETEX', cacheKey, timeToLive, cacheEntry)\n" +
			"else\n" +
			"	redis.call('SET', cacheKey, cacheEntry)\n" +
			"end\n" +
			"return previous";

	public static final String PUT_IF_ABSENT = 
			"local cacheKey = KEYS[1]\n" + 
			"local timeToLive = tonumber(ARGV[1])\n" +
			"local cacheEntry = ARGV[2]\n" +
			"local previous = redis.call('GET', cacheKey)\n" +
			"if not previous then\n" +
			"	if timeToLive > 0 then\n" +
			"		redis.call('SETEX', cacheKey, timeToLive, cacheEntry)\n" +
			"	else\n" +
			"		redis.call('SET', cacheKey, cacheEntry)\n" +
			"	end\n" +
			"end\n" +
			"return previous";

	public static final String REMOVE = 
			"local cacheKey = KEYS[1]\n" + 
			"local previous = redis.call('GET', cacheKey)\n" +
			"if previous == nil then\n" +
			"	return nil\n" +
			"else\n" +
			"	redis.call('DEL', cacheKey)" +
			"end\n" +
			"return previous";

	public static final String REMOVE_ALL = "";

	public static final String REPLACE = 
			"local cacheKey = KEYS[1]\n" + 
			"local timeToLive = tonumber(ARGV[1])\n" +
			"local cacheEntry = ARGV[2]\n" +
			"local previous = redis.call('GET', cacheKey)\n" +
			"if previous == nil then\n" +
			"	return nil\n" +
			"end\n" +
			"if timeToLive > 0 then\n" +
			"	redis.call('SETEX', cacheKey, timeToLive, cacheEntry)\n" +
			"else\n" +
			"	redis.call('SET', cacheKey, cacheEntry)\n" +
			"end\n" +
			"return previous";

	public static final String REPLACE_NEW = 
			"local cacheKey = KEYS[1]\n" + 
			"local timeToLive = tonumber(ARGV[1])\n" +
			"local oldEntry = ARGV[2]\n" +
			"local cacheEntry = ARGV[3]\n" +
			"local previous = redis.call('GET', cacheKey)\n" +
			"if previous == nil or previous ~= oldEntry then\n" +
			"	return nil\n" +
			"end\n" +
			"if timeToLive > 0 then\n" +
			"	redis.call('SETEX', cacheKey, timeToLive, cacheEntry)\n" +
			"else\n" +
			"	redis.call('SET', cacheKey, cacheEntry)\n" +
			"end\n" +
			"return previous";
}
