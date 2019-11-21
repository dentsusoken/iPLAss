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

package org.iplass.mtp.impl.logging;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.slf4j.MDC;

class MdcAdapter implements Map<String, String> {
	
	private Map<String, String> mdcCopy;
	
	private void initCopy() {
		if (mdcCopy == null) {
			mdcCopy = MDC.getCopyOfContextMap();
			if (mdcCopy == null) {
				mdcCopy = Collections.emptyMap();
			}
		}
	}

	@Override
	public int size() {
		initCopy();
		return mdcCopy.size();
	}

	@Override
	public boolean isEmpty() {
		initCopy();
		return mdcCopy.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		initCopy();
		return mdcCopy.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		initCopy();
		return mdcCopy.containsValue(value);
	}

	@Override
	public String get(Object key) {
		if (mdcCopy != null) {
			return mdcCopy.get((String) key);
		}
		return MDC.get((String) key);
	}

	@Override
	public String put(String key, String value) {
		throw new UnsupportedOperationException("MdcAdapter is ReadOnly Map.");
	}

	@Override
	public String remove(Object key) {
		throw new UnsupportedOperationException("MdcAdapter is ReadOnly Map.");
	}

	@Override
	public void putAll(Map<? extends String, ? extends String> m) {
		throw new UnsupportedOperationException("MdcAdapter is ReadOnly Map.");
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException("MdcAdapter is ReadOnly Map.");
	}

	@Override
	public Set<String> keySet() {
		initCopy();
		return Collections.unmodifiableSet(mdcCopy.keySet());
	}

	@Override
	public Collection<String> values() {
		initCopy();
		return Collections.unmodifiableCollection(mdcCopy.values());
	}

	@Override
	public Set<Entry<String, String>> entrySet() {
		initCopy();
		return Collections.unmodifiableSet(mdcCopy.entrySet());
	}

}
