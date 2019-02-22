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

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;

public class NamespaceHandler {

	private final String namespace;
	private final String prefix;
	private final byte[] binaryPrefix;

	public NamespaceHandler(String namespace) {
		this.namespace = namespace;
		prefix = namespace + ":";
		binaryPrefix = prefix.getBytes(StandardCharsets.UTF_8);
	}

	public String getNamespace() {
		return namespace;
	}

	public String getPrefix() {
		return prefix;
	}

	public byte[] getBinaryPrefix() {
		return binaryPrefix;
	}

	public String add(String key) {
		return key == null ? null : prefix + key;
	}

	public byte[] add(byte[] key) {
		return key == null ? null : ArrayUtils.addAll(binaryPrefix, key);
	}

	public String remove(String key) {
		if (key != null && key.startsWith(prefix)) {
			key = key.substring(prefix.length());
		}
		return key;
	}

	public byte[] remove(byte[] key) {
		if (key != null && key.length >= binaryPrefix.length && Arrays.equals(binaryPrefix, Arrays.copyOf(key, binaryPrefix.length))) {
			key = Arrays.copyOfRange(key, binaryPrefix.length, key.length);
		}
		return key;
	}

}
