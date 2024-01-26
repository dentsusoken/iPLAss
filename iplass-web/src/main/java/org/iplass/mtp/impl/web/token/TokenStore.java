/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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

import java.io.Serializable;
import java.util.LinkedList;
import java.util.ListIterator;

import org.iplass.mtp.command.SessionContext;
import org.iplass.mtp.impl.web.WebFrontendService;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.util.StringUtil;


public class TokenStore implements Serializable {

	private static final long serialVersionUID = -7232300170363200575L;

	private static final String TOKEN_STORE_NAME = "org.iplass.mtp.tokenStore";
	public static final String TOKEN_PARAM_NAME = "_t";
	public static final String TOKEN_HEADER_NAME = "X-Transaction-Token";

	private final String fixedToken;
	private final LinkedList<String> list;
	private final int maxSize;

	public static TokenStore getTokenStore(SessionContext session) {
		if (session == null) {
			return null;
		}
		return (TokenStore) session.getAttribute(TOKEN_STORE_NAME);
	}

	public static String createNewToken(SessionContext session) {
		TokenStore tokenStore = (TokenStore) session.getAttribute(TOKEN_STORE_NAME);
		if (tokenStore == null) {
			int maxSize = ServiceRegistry.getRegistry().getService(WebFrontendService.class).getTransactionTokenMaxSize();
			//既にSerializeされているTokenStoreに影響ないように、サブクラスにて新ロジック実装
			tokenStore = new LRUTokenStore(maxSize);
			//tokenStore = new TokenStore(maxSize);
		}
		String token = tokenStore.createToken();
		session.setAttribute(TOKEN_STORE_NAME, tokenStore);//クラスタ対応のため必ず最後にセット
		return token;
	}
	
	public static String getFixedToken(SessionContext session) {
		TokenStore tokenStore = (TokenStore) session.getAttribute(TOKEN_STORE_NAME);
		if (tokenStore == null) {
			int maxSize = ServiceRegistry.getRegistry().getService(WebFrontendService.class).getTransactionTokenMaxSize();
			//既にSerializeされているTokenStoreに影響ないように、サブクラスにて新ロジック実装
			tokenStore = new LRUTokenStore(maxSize);
			//tokenStore = new TokenStore(maxSize);
			session.setAttribute(TOKEN_STORE_NAME, tokenStore);//fixedTokenでは値更新しないので、初回だけセット
		}
		return tokenStore.getFixedToken();
	}

	public TokenStore() {
		this(ServiceRegistry.getRegistry().getService(WebFrontendService.class).getTransactionTokenMaxSize());
	}

	public TokenStore(int maxSize) {
		list = new LinkedList<String>();
		this.maxSize = maxSize;
		fixedToken = StringUtil.randomToken();
	}
	
	String getFixedToken() {
		return fixedToken;
	}

	String createToken() {
		String token = StringUtil.randomToken();
		synchronized (this) {
			list.addFirst(token);
		}
		return token;
	}

	public void pushBack(String token) {
		if (!token.equals(fixedToken)) {
			synchronized (this) {
				list.addFirst(token);
			}
		}
	}
	
	public boolean isValidFixed(String token) {
		if (fixedToken.equals(token)) {
			return true;
		} else {
			return false;
		}
	}	
	
	public boolean isValid(String token, boolean withConsume) {
		synchronized (this) {
			boolean isValid = false;
			
			//固定トークンの場合は、withConsumeがtrueであってもvalidなトークンとする。
			if (fixedToken.equals(token)) {
				return true;
			}
			
			for (ListIterator<String> it = list.listIterator(); it.hasNext();) {
				if (it.next().equals(token)) {
					isValid = true;
					it.remove();
					break;
				}
			}
			if (isValid && !withConsume && list.size() <= maxSize) {
				list.addFirst(token);
			}
			return isValid;
		}
	}

}
