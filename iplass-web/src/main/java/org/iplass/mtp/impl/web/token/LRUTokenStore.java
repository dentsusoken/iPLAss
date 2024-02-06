/*
 * Copyright (C) 2016 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.impl.web.token;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.iplass.mtp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LRUTokenStore extends TokenStore {
	private static final long serialVersionUID = 2864948027935155294L;

	private static Logger logger = LoggerFactory.getLogger(LRUTokenStore.class);
	
	private final String fixedToken;
	//listをLinkedList -> LinkedHashMapするにあたり、TokenStoreがSerializeされてしまっているので、サブクラスとして実装する。
	private final LinkedHashMap<String, Object> list;
	
	public LRUTokenStore(final int maxSize) {
		list = new LinkedHashMap<String, Object>(){
			private static final long serialVersionUID = -6502264709029793582L;

			@Override
			protected boolean removeEldestEntry(Entry<String, Object> eldest) {
				boolean ret = size() > maxSize;
				if (ret && logger.isDebugEnabled()) {
					logger.debug("remove(cause cache size over) transaction token:" + eldest.getKey());
				}
				return ret;
			}
		};
		fixedToken = StringUtil.randomToken();
	}
	
	@Override
	String getFixedToken() {
		return fixedToken;
	}

	@Override
	String createToken() {
		String token = StringUtil.randomToken();
		synchronized (this) {
			if (logger.isDebugEnabled()) {
				logger.debug("create transaction token:" + token);
			}
			list.put(token, Boolean.TRUE);
		}
		return token;
	}

	@Override
	public void pushBack(String token) {
		if (!token.equals(fixedToken)) {
			synchronized (this) {
				if (logger.isDebugEnabled()) {
					logger.debug("push back transaction token:" + token);
				}
				list.put(token, Boolean.TRUE);
			}
		}
	}
	
	@Override
	public boolean isValidFixed(String token) {
		if (fixedToken.equals(token)) {
			return true;
		} else {
			return false;
		}
	}	
	
	@Override
	public boolean isValid(String token, boolean withConsume) {
		synchronized (this) {
			
			//no consume if fixed token, and return true
			if (fixedToken.equals(token)) {
				return true;
			}
			
			if (list.remove(token) != null) {
				if (!withConsume) {
					//put to last
					list.put(token, Boolean.TRUE);
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("consume transaction token:" + token);
					}
				}
				return true;
			}
			
			return false;
		}
	}

}
