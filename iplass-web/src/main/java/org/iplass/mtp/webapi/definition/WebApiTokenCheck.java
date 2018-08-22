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

package org.iplass.mtp.webapi.definition;

import java.io.Serializable;

public class WebApiTokenCheck implements Serializable {

	private static final long serialVersionUID = -6137919446875518790L;

	private boolean consume = true;
	private boolean exceptionRollback = true;
	private boolean useFixedToken = false;

	public WebApiTokenCheck() {
	}

	public boolean isUseFixedToken() {
		return useFixedToken;
	}

	public void setUseFixedToken(boolean useFixedToken) {
		this.useFixedToken = useFixedToken;
	}

	public boolean isConsume() {
		return consume;
	}

	public void setConsume(boolean consume) {
		this.consume = consume;
	}

	public boolean isExceptionRollback() {
		return exceptionRollback;
	}

	public void setExceptionRollback(boolean exceptionRollback) {
		this.exceptionRollback = exceptionRollback;
	}

}
