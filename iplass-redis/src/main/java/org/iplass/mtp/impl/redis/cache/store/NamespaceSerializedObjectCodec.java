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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Base64;

import org.iplass.mtp.SystemException;

import io.lettuce.core.codec.RedisCodec;

public class NamespaceSerializedObjectCodec implements RedisCodec<Object, Object> {

	private final Charset charset = Charset.forName("UTF-8");

	private final String prefix;

	public NamespaceSerializedObjectCodec(String namespace) {
		this.prefix = namespace + ":";
	}

	public Charset getCharset() {
		return charset;
	}

	public String getPrefix() {
		return prefix;
	}

	@Override
	public Object decodeKey(ByteBuffer bytes) {
		try {
			String strKey = removePrefix(charset.decode(bytes).toString());
			ObjectInputStream is = new ObjectInputStream(new ByteArrayInputStream(Base64.getDecoder().decode(strKey)));
			return is.readObject();
		} catch (IOException | ClassNotFoundException e) {
			throw new SystemException(e);
		}
	}

	@Override
	public Object decodeValue(ByteBuffer bytes) {
		try {
			byte[] array = new byte[bytes.remaining()];
			bytes.get(array);
			ObjectInputStream is = new ObjectInputStream(new ByteArrayInputStream(array));
			return is.readObject();
		} catch (Exception e) {
			throw new SystemException(e);
		}
	}

	@Override
	public ByteBuffer encodeKey(Object key) {
		try {
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			ObjectOutputStream os = new ObjectOutputStream(bytes);
			os.writeObject(key);
			String strKey = Base64.getEncoder().encodeToString(bytes.toByteArray());
			return charset.encode(addPrefix(strKey));
		} catch (IOException e) {
			throw new SystemException(e);
		}
	}

	@Override
	public ByteBuffer encodeValue(Object value) {
		// LuaScriptの引数として渡されるtimeToLiveはシリアライズしない（デシリアライズが必要になることはない）
		if (value instanceof Long) {
			return ByteBuffer.wrap(String.valueOf(value).getBytes());
		}

		try {
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			ObjectOutputStream os = new ObjectOutputStream(bytes);
			os.writeObject(value);
			return ByteBuffer.wrap(bytes.toByteArray());
		} catch (IOException e) {
			throw new SystemException(e);
		}
	}

	private String addPrefix(String key) {
		return key == null ? null : prefix + key;
	}

	private String removePrefix(String key) {
		if (key != null && key.startsWith(prefix)) {
			key = key.substring(prefix.length());
		}
		return key;
	}

}
