/*
 * Copyright (C) 2013 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.async.rdb;

import java.io.Serializable;
import java.util.concurrent.Callable;

import org.iplass.mtp.auth.AuthContext;
import org.iplass.mtp.impl.auth.AuthService;
import org.iplass.mtp.impl.auth.UserContext;
import org.iplass.mtp.spi.ServiceRegistry;

public class CallableInput<V> implements Serializable {

	private static final long serialVersionUID = -2374924829357547133L;
	
	private final Callable<V> actual;
	private final UserContext userContext;
	private final boolean privilaged;

	public CallableInput(Callable<V> actual, UserContext userContext, boolean privilaged) {
		super();
		this.actual = actual;
		this.userContext = userContext;
		this.privilaged = privilaged;
	}
	
	public Callable<V> getActual() {
		return actual;
	}
	
	public UserContext getUserContext() {
		return userContext;
	}
	
	public boolean isPrivilaged() {
		return privilaged;
	}

	public V callImpl() throws Exception {
		if (userContext != null) {
			AuthService as = ServiceRegistry.getRegistry().getService(AuthService.class);
			try {
				return as.doSecuredAction(userContext, () -> {
					if (privilaged) {
						return AuthContext.doPrivileged(() -> {
							try { 
								return actual.call();
							} catch (Exception e) {
								throw new WrapException(e);
							}
						});
					} else {
						try { 
							return actual.call();
						} catch (Exception e) {
							throw new WrapException(e);
						}
					}
				});
			} catch (WrapException e) {
				throw (Exception) e.getCause();
			}
		} else {
			return actual.call();
		}
	}
	
	@Override
	public String toString() {
		return actual.toString();
	}

	private static class WrapException extends RuntimeException {
		private static final long serialVersionUID = -7618439229960105705L;
		public WrapException(Throwable cause) {
			super(cause);
		}
	}

}
