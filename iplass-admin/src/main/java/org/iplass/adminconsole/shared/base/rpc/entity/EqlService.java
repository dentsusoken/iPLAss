/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import org.iplass.adminconsole.shared.base.dto.entity.EqlResultInfo;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.client.rpc.XsrfProtectedService;

/**
 * EQL用Service
 */
@RemoteServiceRelativePath("service/eql")
public interface EqlService extends XsrfProtectedService {

	/**
	 * EQLを実行します。
	 *
	 * @param tenantId テナントID
	 * @param eql 実行EQL
	 * @param isSearcgAllVersion 全バージョン検索
	 * @return {@link EqlResultInfo}
	 */
	public EqlResultInfo execute(int tenantId, final String eql, final boolean isSearcgAllVersion);

}
