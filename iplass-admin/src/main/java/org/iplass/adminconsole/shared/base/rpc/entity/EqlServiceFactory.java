/*
 * Copyright (C) 2018 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.shared.base.rpc.entity;

import org.iplass.adminconsole.shared.base.rpc.AbstractAdminServiceFactory;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.HasRpcToken;

public abstract class EqlServiceFactory extends AbstractAdminServiceFactory {

	private static EqlServiceAsync service;

	private EqlServiceFactory() {
	}

	public static EqlServiceAsync get() {
		if (service != null) {
			return service;
		}

		EqlServiceAsync service = GWT.create(EqlService.class);

		init((HasRpcToken)service);

		EqlServiceFactory.service = service;

		return service;

	}

}
