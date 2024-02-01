/*
 * Copyright (C) 2013 DENTSU SOKEN INC. All Rights Reserved.
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

import org.iplass.mtp.impl.auth.UserContext;

public class CallableInput<V> implements Serializable {

	private static final long serialVersionUID = -2374924829357547133L;
	
	private final Callable<V> actual;
	private final UserContext userContext;
	private final boolean privilaged;
	private final String traceId;

	public CallableInput(Callable<V> actual, UserContext userContext, boolean privilaged, String traceId) {
		super();
		this.actual = actual;
		this.userContext = userContext;
		this.privilaged = privilaged;
		this.traceId = traceId;
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

	public String getTraceId() {
		return traceId;
	}

	@Override
	public String toString() {
		return actual.toString();
	}

}
