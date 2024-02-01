/*
 * Copyright (C) 2020 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.webhook;

import org.iplass.mtp.SystemException;

public class WebhookException extends SystemException {

	private static final long serialVersionUID = 5631212745775599471L;

	public WebhookException() {
		super();
	}
	/**
	 * @param message
	 * @param cause
	 */
	public WebhookException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public WebhookException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public WebhookException(Throwable cause) {
		super(cause);
	}
}
