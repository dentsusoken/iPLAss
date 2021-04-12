/*
 * Copyright (C) 2021 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.web.mdc;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import javax.servlet.http.HttpServletRequest;

public class UuidMdcValueResolver implements MdcValueResolver {
	
	private boolean secure = true;

	public UuidMdcValueResolver() {
	}

	public UuidMdcValueResolver(boolean secure) {
		this.secure = secure;
	}

	public boolean isSecure() {
		return secure;
	}

	public void setSecure(boolean secure) {
		this.secure = secure;
	}

	@Override
	public String resolve(HttpServletRequest request) {
		if (secure) {
			return UUID.randomUUID().toString();
		} else {
			return insecureUUID().toString();
		}
	}

	private UUID insecureUUID() {
		//厳密なUUIDではないが、速度優先
		ThreadLocalRandom r = ThreadLocalRandom.current();
		return new UUID(r.nextLong(), r.nextLong());
	}

}
