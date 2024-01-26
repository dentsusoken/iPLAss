/*
 * Copyright (C) 2023 DENTSU SOKEN INC. All Rights Reserved.
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * MDC値として利用するセッションIDを解決する
 * 
 * @author SEKIGUCHI Naoya
 */
public class SessionIdMdcValueResolver implements MdcValueResolver {
	@Override
	public String resolve(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (null != session) {
			// セッションが存在すれば、セッションIDを返却
			return session.getId();
		}
		// セッションがなければ null を返却
		return null;
	}
}
