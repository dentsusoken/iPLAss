/*
 * Copyright (C) 2018 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.impl.auth.oauth;

import org.iplass.mtp.ApplicationException;

public class OAuthApplicationException extends ApplicationException {
	private static final long serialVersionUID = 1424987676539365135L;

	private String code;
	private String description;
	
	private static String toMsg(String code, String desc) {
		if (desc == null) {
			return code;
		} else {
			return code + ":" + desc;
		}
	}
	
	public OAuthApplicationException() {
		super();
	}

	public OAuthApplicationException(String code, String description, Throwable cause) {
		super(toMsg(code, description), cause);
		this.code = code;
		this.description = description;
	}

	public OAuthApplicationException(String code, String description) {
		super(toMsg(code, description));
		this.code = code;
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

}
